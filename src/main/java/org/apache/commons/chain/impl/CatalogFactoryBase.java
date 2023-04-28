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

package org.apache.commons.chain.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Context;

/**
 * A simple implementation of {@link CatalogFactory}.
 *
 * @param <C> Type of the context associated with this command
 *
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */
public class CatalogFactoryBase<C extends Context> extends CatalogFactory<C> {

    // ----------------------------------------------------------- Constructors

    /**
     * Construct an empty instance of {@link CatalogFactoryBase}. This
     * constructor is intended solely for use by {@link CatalogFactory}.
     */
    public CatalogFactoryBase() {
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * The default {@link Catalog} for this {@link CatalogFactory}.
     */
    private Catalog<C> catalog = null;

    /**
     * Map of named {@link Catalog}s, keyed by catalog name.
     */
    private final Map<String, Catalog<C>> catalogs = new ConcurrentHashMap<>();

    // --------------------------------------------------------- Public Methods

    /**
     * Gets the default instance of Catalog associated with the factory
     * (if any); otherwise, return {@code null}.
     *
     * @return the default Catalog instance
     */
    @Override
    public Catalog<C> getCatalog() {
        return catalog;
    }

    /**
     * Sets the default instance of Catalog associated with the factory.
     *
     * @param catalog the default Catalog instance
     */
    @Override
    public void setCatalog(Catalog<C> catalog) {
        this.catalog = catalog;
    }

    /**
     * Retrieves a Catalog instance by name (if any); otherwise
     * return {@code null}.
     *
     * @param name the name of the Catalog to retrieve
     *
     * @return the specified Catalog
     */
    @Override
    public Catalog<C> getCatalog(String name) {
        return catalogs.get(name);
    }

    /**
     * Adds a named instance of Catalog to the factory (for subsequent
     * retrieval later).
     *
     * @param name the name of the Catalog to add
     * @param catalog the Catalog to add
     */
    @Override
    public void addCatalog(String name, Catalog<C> catalog) {
        catalogs.put(name, catalog);
    }

    /**
     * Return an {@code Iterator} over the set of named
     * {@link Catalog}s known to this {@link CatalogFactory}.
     * If there are no known catalogs, an empty Iterator is returned.
     *
     * @return An Iterator of the names of the Catalogs known by this factory.
     */
    @Override
    public Iterator<String> getNames() {
        return catalogs.keySet().iterator();
    }
}