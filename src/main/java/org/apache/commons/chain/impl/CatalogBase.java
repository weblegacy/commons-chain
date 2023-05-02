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
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 * Simple in-memory implementation of {@link Catalog}. This class can
 * also be used as the basis for more advanced implementations.
 *
 * <p>This implementation is thread-safe.</p>
 *
 * @param <C> Type of the context associated with this catalog
 *
 * @author Craig R. McClanahan
 * @author Matthew J. Sgarlata
 * @version $Revision$ $Date$
 */
public class CatalogBase<C extends Context> implements Catalog<C> {

    // ----------------------------------------------------- Instance Variables

    /**
     * The map of named {@link Command}s, keyed by name.
     */
    private final Map<String, Command<C>> commands;

    // --------------------------------------------------------- Constructors

    /**
     * Create an empty catalog.
     */
    public CatalogBase() {
        commands = new ConcurrentHashMap<>();
    }

    /**
     * Create a catalog whose commands are those specified in the given {@code Map}.
     * All Map keys should be {@code String} and all values should be {@code Command}.
     *
     * @param commands Map of Commands.
     *
     * @since Chain 1.1
     */
    public CatalogBase(Map<String, Command<C>> commands) {
        this.commands = new ConcurrentHashMap<>(commands);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Add a new name and associated {@link Command}
     * to the set of named commands known to this {@link Catalog},
     * replacing any previous command for that name.
     *
     * @param <CMD> the {@link Command} type to be added in the {@link Catalog}
     * @param name Name of the new command
     * @param command {@link Command} to be returned
     *        for later lookups on this name
     */
    @Override
    public <CMD extends Command<C>> void addCommand(String name, CMD command) {
        commands.put(name, command);
    }

    /**
     * Return the {@link Command} associated with the
     * specified name, if any; otherwise, return {@code null}.
     *
     * @param <CMD> the expected {@link Command} type to be returned
     * @param name Name for which a {@link Command}
     *        should be retrieved
     *
     * @return The Command associated with the specified name.
     */
    @Override
    public <CMD extends Command<C>> CMD getCommand(String name) {
        @SuppressWarnings("unchecked") // it would throw ClassCastException if users try to cast to a different type
        CMD command = (CMD) commands.get(name);
        return command;
    }

    /**
     * Return an {@code Iterator} over the set of named commands
     * known to this {@link Catalog}. If there are no known commands,
     * an empty Iterator is returned.
     *
     * @return An iterator of the names in this Catalog.
     */
    @Override
    public Iterator<String> getNames() {
        return commands.keySet().iterator();
    }

    /**
     * Converts this Catalog to a String. Useful for debugging purposes.
     *
     * @return a representation of this catalog as a String
     */
    @Override
    public String toString() {
        Iterator<String> names = getNames();
        StringBuilder str = new StringBuilder();
        str
            .append('[')
            .append(this.getClass().getName())
            .append(": ");

        while (names.hasNext()) {
            str.append(names.next());
            if (names.hasNext()) {
                str.append(", ");
            }
        }
        str.append(']');

        return str.toString();
    }
}