/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.chain;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.chain.impl.CatalogFactoryBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link CatalogFactory} is a class used to store and retrieve
 * {@link Catalog}s. The factory allows for a default {@link Catalog}
 * as well as {@link Catalog}s stored with a name key. Follows the
 * Factory pattern (see GoF).
 *
 * <p>The base {@code CatalogFactory} implementation also implements
 * a resolution mechanism which allows lookup of a command based on a single
 * String which encodes both the catalog and command names.</p>
 *
 * @param <C> Type of the context associated with this command
 *
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */
public abstract class CatalogFactory<C extends Context> {

    // ------------------------------------------------------- Static Variables

    /**
     * Values passed to the {@code getCommand(String)} method should
     * use this as the delimiter between the "catalog" name and the "command"
     * name.
     */
    public static final String DELIMITER = ":";

    /**
     * The set of registered {@link CatalogFactory} instances,
     * keyed by the relevant class loader.
     */
    private static final ConcurrentHashMap<ClassLoader, CatalogFactory<?>> FACTORIES = new ConcurrentHashMap<>();

    // --------------------------------------------------------- Public Methods

    /**
     * Gets the default instance of Catalog associated with the factory
     * (if any); otherwise, return {@code null}.
     *
     * @return the default Catalog instance
     */
    public abstract Catalog<C> getCatalog();

    /**
     * Sets the default instance of Catalog associated with the factory.
     *
     * @param catalog the default Catalog instance
     */
    public abstract void setCatalog(Catalog<C> catalog);

    /**
     * Retrieves a Catalog instance by name (if any); otherwise
     * return {@code null}.
     *
     * @param name the name of the Catalog to retrieve
     *
     * @return the specified Catalog
     */
    public abstract Catalog<C> getCatalog(String name);

    /**
     * Adds a named instance of Catalog to the factory (for subsequent
     * retrieval later).
     *
     * @param name the name of the Catalog to add
     * @param catalog the Catalog to add
     */
    public abstract void addCatalog(String name, Catalog<C> catalog);

    /**
     * Return an {@code Iterator} over the set of named
     * {@link Catalog}s known to this {@link CatalogFactory}.
     * If there are no known catalogs, an empty Iterator is returned.
     *
     * @return An Iterator of the names of the Catalogs known by this factory.
     */
    public abstract Iterator<String> getNames();

    /**
     * The Default-Constructor for this class.
     */
    public CatalogFactory() {
    }

    /**
     * Return a {@code Command} based on the given commandID.
     *
     * <p>At this time, the structure of commandID is relatively simple: if the
     * commandID contains a DELIMITER, treat the segment of the commandID
     * up to (but not including) the DELIMITER as the name of a catalog, and the
     * segment following the DELIMITER as a command name within that catalog.
     * If the commandID contains no DELIMITER, treat the commandID as the name
     * of a command in the default catalog.</p>
     *
     * <p>To preserve the possibility of future extensions to this lookup
     * mechanism, the DELIMITER string should be considered reserved, and
     * should not be used in command names. commandID values which contain
     * more than one DELIMITER will cause an {@code IllegalArgumentException}
     * to be thrown.</p>
     *
     * @param <CMD> the expected {@link Command} type to be returned
     * @param commandID the identifier of the command to return
     *
     * @return the command located with commandID, or {@code null}
     *         if either the command name or the catalog name cannot be
     *         resolved
     *
     * @throws IllegalArgumentException if the commandID contains more than
     *         one DELIMITER
     *
     * @since Chain 1.1
     */
    public <CMD extends Command<C>> CMD getCommand(String commandID) {
        String commandName = commandID;
        String catalogName = null;
        Catalog<C> catalog = null;

        if (commandID != null) {
            int splitPos = commandID.indexOf(DELIMITER);
            if (splitPos != -1) {
                catalogName = commandID.substring(0, splitPos);
                commandName = commandID.substring(splitPos + DELIMITER.length());
                if (commandName.contains(DELIMITER)) {
                    throw new IllegalArgumentException("commandID ["
                            + commandID + "] has too many delimiters (reserved for future use)");
                }
            }
        }

        if (catalogName != null) {
            catalog = this.getCatalog(catalogName);
            if (catalog == null) {
                Logger logger = LoggerFactory.getLogger(CatalogFactory.class);
                logger.warn("No catalog found for name: {}.", catalogName);
                return null;
            }
        } else {
            catalog = this.getCatalog();
            if (catalog == null) {
                Logger logger = LoggerFactory.getLogger(CatalogFactory.class);
                logger.warn("No default catalog found.");
                return null;
            }
        }

        return catalog.getCommand(commandName);
    }

    // -------------------------------------------------------- Static Methods

    /**
     * Return the singleton {@link CatalogFactory} instance
     * for the relevant {@code ClassLoader}. For applications
     * that use a thread context class loader (such as web applications
     * running inside a servet container), this will return a separate
     * instance for each application, even if this class is loaded from
     * a shared parent class loader.
     *
     * @param <C> Type of the context associated with this command
     *
     * @return the per-application singleton instance of {@link CatalogFactory}
     */
    public static <C extends Context> CatalogFactory<C> getInstance() {
        ClassLoader cl = getClassLoader();

        @SuppressWarnings("unchecked")
        CatalogFactory<C> ret = (CatalogFactory<C>) FACTORIES.computeIfAbsent(cl, k -> new CatalogFactoryBase<>());
        return ret;
    }

    /**
     * Clear all references to registered catalogs, as well as to the
     * relevant class loader. This method should be called, for example,
     * when a web application utilizing this class is removed from
     * service, to allow for garbage collection.
     */
    public static void clear() {
        FACTORIES.remove(getClassLoader());
    }

    // ------------------------------------------------------- Private Methods

    /**
     * Return the relevant {@code ClassLoader} to use as a Map key
     * for this request. If there is a thread context class loader, return
     * that; otherwise, return the class loader that loaded this class.
     *
     * @return the relevant {@code ClassLoader} to use as a Map key for this request
     */
    private static ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = CatalogFactory.class.getClassLoader();
        }
        return cl;
    }
}