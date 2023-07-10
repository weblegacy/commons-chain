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
package org.apache.commons.chain2.impl;

import static java.util.Collections.unmodifiableMap;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.Command;

/**
 * <p>Simple in-memory implementation of {@link Catalog}.  This class can
 * also be used as the basis for more advanced implementations.</p>
 *
 * <p>This implementation is thread-safe.</p>
 *
 * @param <K> the type of keys maintained by the context associated with this catalog
 * @param <V> the type of mapped values
 * @param <C> Type of the context associated with this catalog
 *
 */
public class CatalogBase<K, V, C extends Map<K, V>> implements Catalog<K, V, C> {

    // ----------------------------------------------------- Instance Variables

    /**
     * <p>The map of named {@link Command}s, keyed by name.
     */
    private final Map<String, Command<K, V, C>> commands = new ConcurrentHashMap<String, Command<K, V, C>>();

    // --------------------------------------------------------- Constructors

    /**
     * Create an empty catalog.
     */
    public CatalogBase() { }

    /**
     * <p>Create a catalog whose commands are those specified in the given <code>Map</code>.
     * All Map keys should be <code>String</code> and all values should be <code>Command</code>.</p>
     *
     * @param commands Map of Commands.
     *
     * @throws IllegalArgumentException if <code>commands</code>
     * is <code>null</code>
     *
     * @since 1.1
     */
    public CatalogBase(Map<String, Command<K, V, C>> commands) {
        if (commands == null) {
            throw new IllegalArgumentException("'commands' parameter must be not null");
        }
        this.commands.putAll( commands );
    }

    // --------------------------------------------------------- Public Methods

    /**
     * <p>Add a new name and associated {@link Command}
     * to the set of named commands known to this {@link Catalog},
     * replacing any previous command for that name.
     *
     * @param <CMD> the {@link Command} type to be added in the {@link Catalog}
     * @param name Name of the new command
     * @param command {@link Command} to be returned
     *  for later lookups on this name
     */
    public <CMD extends Command<K, V, C>> void addCommand(String name, CMD command) {
        commands.put(name, command);
    }

    /**
     * <p>Return the {@link Command} associated with the
     * specified name, if any; otherwise, return <code>null</code>.</p>
     *
     * @param <CMD> the expected {@link Command} type to be returned
     * @param name Name for which a {@link Command}
     *  should be retrieved
     * @return The Command associated with the specified name.
     */
    public <CMD extends Command<K, V, C>> CMD getCommand(String name) {
        @SuppressWarnings("unchecked") // it would throw ClassCastException if users try to cast to a different type
        CMD command = (CMD) commands.get(name);
        return command;
    }

    /**
     * Returns the map of named {@link Command}s, keyed by name.
     *
     * @return The map of named {@link Command}s, keyed by name.
     * @since 2.0
     */
    public Map<String, Command<K, V, C>> getCommands() {
        return unmodifiableMap(commands);
    }

    /**
     * <p>Return an <code>Iterator</code> over the set of named commands
     * known to this {@link Catalog}.  If there are no known commands,
     * an empty Iterator is returned.</p>
     * @return An iterator of the names in this Catalog.
     */
    public Iterator<String> getNames() {
        return commands.keySet().iterator();
    }

    /**
     * Converts this Catalog to a String.  Useful for debugging purposes.
     * @return a representation of this catalog as a String
     */
    @Override
    public String toString() {
        Iterator<String> names = getNames();
        StringBuilder str = new StringBuilder("[").append(this.getClass().getName()).append(": ");

        while (names.hasNext()) {
            str.append(names.next());
            if (names.hasNext()) {
                str.append(", ");
            }
        }

        return str.append("]").toString();
    }

}
