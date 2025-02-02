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

/**
 * A {@link Catalog} is a collection of named {@link Command}s (or
 * {@link Chain}s) that can be used to retrieve the set of commands that
 * should be performed based on a symbolic identifier. Use of catalogs
 * is optional, but convenient when there are multiple possible chains
 * that can be selected and executed based on environmental conditions.
 *
 * @param <C> Type of the context associated with this command
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public interface Catalog<C extends Context> {

    /**
     * A default context attribute for storing a default {@link Catalog},
     * provided as a convenience only.
     */
    String CATALOG_KEY = "org.apache.commons.chain.CATALOG";

    /**
     * Add a new name and associated {@link Command} or {@link Chain}
     * to the set of named commands known to this {@link Catalog},
     * replacing any previous command for that name.
     *
     * @param <CMD> the {@link Command} type to be added in the {@link Catalog}
     * @param name Name of the new command
     * @param command {@link Command} or {@link Chain} to be returned
     *        for later lookups on this name
     */
    <CMD extends Command<C>> void addCommand(String name, CMD command);

    /**
     * Return the {@link Command} or {@link Chain} associated with the
     * specified name, if any; otherwise, return {@code null}.
     *
     * @param <CMD> the expected {@link Command} type to be returned
     * @param name Name for which a {@link Command} or {@link Chain}
     *        should be retrieved
     *
     * @return The Command associated with the specified name.
     */
    <CMD extends Command<C>> CMD getCommand(String name);

    /**
     * Return an {@code Iterator} over the set of named commands
     * known to this {@link Catalog}. If there are no known commands,
     * an empty Iterator is returned.
     *
     * @return An iterator of the names in this Catalog.
     */
    Iterator<String> getNames();
}