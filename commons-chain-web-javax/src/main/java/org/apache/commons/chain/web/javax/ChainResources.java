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
package org.apache.commons.chain.web.javax;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.web.CheckedConsumer;
import org.apache.commons.chain.web.CheckedFunction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility methods for loading class loader and web application resources
 * to configure a {@link Catalog}. These methods are shared between
 * {@code ChainListener} and {@code ChainServlet}.
 *
 * @author Craig R. McClanahan
 * @author Ted Husted
 */
final class ChainResources {

    /**
     * This class uses a private constructor because it is a utility class.
     */
    private ChainResources() {
    }

    // ---------------------------------------------------------- Static Methods

    /**
     * Parse the specified class loader resources.
     *
     * @param <E> the type of the exception from parse-function
     * @param resources Comma-delimited list of resources (or {@code null})
     * @param parse parse-function to parse the XML document
     */
    static <E extends Exception> void parseClassResources(String resources,
                                    CheckedConsumer<URL, E> parse) {

        ClassLoader loader =
            Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ChainResources.class.getClassLoader();
        }
        parseResources(loader::getResource, resources, parse);
    }

    /**
     * Parse the specified web application resources.
     *
     * @param <E> the type of the exception from parse-function
     * @param context {@code ServletContext} for this web application
     * @param resources Comma-delimited list of resources (or {@code null})
     * @param parse parse-function to parse the XML document
     */
    static <E extends Exception> void parseWebResources(ServletContext context,
                                  String resources,
                                  CheckedConsumer<URL, E> parse) {

        parseResources(context::getResource, resources, parse);
    }

    /**
     * Parse the specified resources with a resource-get-function.
     *
     * @param <ER> the type of the exception from resource-function
     * @param <EP> the type of the exception from parse-function
     * @param resourceFunction function to get the {@link URL} from a path
     * @param resources Comma-delimited list of resources (or {@code null})
     * @param parse parse-function to parse the XML document
     */
    private static <ER extends Exception, EP extends Exception> void parseResources(
            CheckedFunction<String, URL, ER> resourceFunction, String resources,
            CheckedConsumer<URL, EP> parse) {

        if (resources == null) {
            return;
        }
        Log log = LogFactory.getLog(ChainResources.class);
        String[] paths = getResourcePaths(resources);
        String path = null;
        try {
            for (String path2 : paths) {
                path = path2;
                URL url = resourceFunction.apply(path);
                if (url == null) {
                    throw new IllegalStateException("Missing chain config resource '" + path + "'");
                }
                if (log.isDebugEnabled()) {
                    log.debug("Loading chain config resource '" + path + "'");
                }
                parse.accept(url);
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception parsing chain config resource '" + path + "': "
                 + e.getMessage());
        }
    }

    /**
     * Parse the resource string into an array of paths. Empty entries will
     * be skipped. (That is, all entries in the array are non-empty paths.)
     *
     * @param resources A comma-delimited list of resource paths (or
     *        {@code null}).
     *
     * @return An array of non-empty paths. The array itself may be empty.
     *
     * @since Chain 1.1
     */
    static String[] getResourcePaths(String resources) {
        final List<String> paths = new ArrayList<>();

        if (resources != null) {
            String path;
            int comma;

            int lastComma = 0;
            while ((comma = resources.indexOf(',', lastComma)) >= 0) {
                path = resources.substring(lastComma, comma).trim();
                if (path.length() > 0) {
                    paths.add(path);
                }
                lastComma = comma + 1;
            }
            path = resources.substring(lastComma).trim();
            if (path.length() > 0) {
                paths.add(path);
            }
        }

        return paths.toArray(new String[0]);
    }
}