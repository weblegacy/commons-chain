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
package org.apache.commons.chain.web;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.config.ConfigParser;
import org.apache.commons.chain.impl.CatalogBase;
import org.apache.commons.chain.web.internal.CheckedConsumer;
import org.apache.commons.digester.RuleSet;
import org.apache.commons.logging.Log;

/**
 * Context-initializer that automatically scans chain configuration files
 * in the current web application at startup time, and exposes the result
 * in a {@link Catalog} under a specified servlet context attribute. The
 * following <em>context</em> init parameters are utilized:
 * <ul>
 * <li><strong>org.apache.commons.chain.CONFIG_CLASS_RESOURCE</strong> -
 *     comma-delimited list of chain configuration resources to be loaded
 *     via {@code ClassLoader.getResource()} calls. If not specified,
 *     no class loader resources will be loaded.</li>
 * <li><strong>org.apache.commons.chain.CONFIG_WEB_RESOURCE</strong> -
 *     comma-delimited list of chain configuration webapp resources
 *     to be loaded. If not specified, no web application resources
 *     will be loaded.</li>
 * <li><strong>org.apache.commons.chain.CONFIG_ATTR</strong> -
 *     Name of the servlet context attribute under which the
 *     resulting {@link Catalog} will be created or updated.
 *     If not specified, it is expected that parsed resources will
 *     contain {@code &lt;catalog&gt;} elements (which will
 *     cause registration of the created {@link Catalog}s into
 *     the {@link CatalogFactory} for this application, and no
 *     servet context attribute will be created.
 *     <strong>NOTE</strong> - This parameter is deprecated.</li>
 * <li><strong>org.apache.commons.chain.RULE_SET</strong> -
 *     Fully qualified class name of a Digester {@code RuleSet}
 *     implementation to use for parsing configuration resources (this
 *     class must have a public zero-args constructor). If not defined,
 *     the standard {@code RuleSet} implementation will be used.</li>
 * </ul>
 *
 * <p>When a web application that has configured this listener is
 * started, it will acquire the {@link Catalog} under the specified servlet
 * context attribute key, creating a new one if there is none already there.
 * This {@link Catalog} will then be populated by scanning configuration
 * resources from the following sources (loaded in this order):</p>
 * <ul>
 * <li>Optional: Resources loaded from any {@code META-INF/chain-config.xml}
 *     resource found in a JAR file in {@code /WEB-INF/lib}.</li>
 * <li>Resources loaded from specified resource paths from the
 *     webapp's class loader (via {@code ClassLoader.getResource()}).</li>
 * <li>Resources loaded from specified resource paths in the web application
 *     archive (via {@code ServetContext.getResource()}).</li>
 * </ul>
 *
 * <p>If no attribute key is specified, on the other hand, parsed configuration
 * resources are expected to contain {@code &lt;catalog&gt;} elements,
 * and the catalogs will be registered with the {@link CatalogFactory}
 * for this web application.</p>
 *
 * @author Craig R. McClanahan
 * @author Ted Husted
 */
final class ChainInit {

    /**
     * The name of the context init parameter containing the name of the
     * servlet context attribute under which our resulting {@link Catalog}
     * will be stored.
     */
    static final String CONFIG_ATTR =
        "org.apache.commons.chain.CONFIG_ATTR";

    /**
     * The name of the context init parameter containing a comma-delimited
     * list of class loader resources to be scanned.
     */
    static final String CONFIG_CLASS_RESOURCE =
        "org.apache.commons.chain.CONFIG_CLASS_RESOURCE";

    /**
     * The name of the context init parameter containing a comma-delimited
     * list of web application resources to be scanned.
     */
    static final String CONFIG_WEB_RESOURCE =
        "org.apache.commons.chain.CONFIG_WEB_RESOURCE";

    /**
     * The name of the context init parameter containing the fully
     * qualified class name of the {@code RuleSet} implementation
     * for configuring our {@link ConfigParser}.
     */
    static final String RULE_SET =
        "org.apache.commons.chain.RULE_SET";

    /**
     * Remove the configured {@link Catalog} from the servlet context
     * attributes for this web application.
     *
     * @param context the servlet-context
     * @param attr the value of the {@code CONFIG_ATTR}
     */
    static void destroy(ServletContext context, String attr) {
        if (attr != null) {
            context.removeAttribute(attr);
        }
        CatalogFactory.clear();
    }

    /**
     * Private constructor.
     */
    private ChainInit() {
    }

    /**
     * Scan the required chain configuration resources, assemble the
     * configured chains into a {@link Catalog}, and expose it as a
     * servlet context attribute under the specified key.
     *
     * @param context the servlet-context
     * @param attr the value of the {@code CONFIG_ATTR}
     * @param log to use for logging
     * @param parseJarResources {@code true} to parse resources in jar-files
     */
    @SuppressWarnings("deprecation")
    static void initialize(ServletContext context, String attr, Log log, boolean parseJarResources) throws ServletException {
        String classResources = context.getInitParameter(CONFIG_CLASS_RESOURCE);
        String ruleSet = context.getInitParameter(RULE_SET);
        String webResources = context.getInitParameter(CONFIG_WEB_RESOURCE);

        // Retrieve or create the Catalog instance we may be updating
        Catalog<?> catalog = null;
        if (attr != null) {
            catalog = (Catalog<?>) context.getAttribute(attr);
            if (catalog == null) {
                catalog = new CatalogBase<>();
            }
        }

        // Construct the configuration resource parser we will use
        ConfigParser parser = new ConfigParser();
        if (ruleSet != null) {
            try {
                ClassLoader loader =
                    Thread.currentThread().getContextClassLoader();
                if (loader == null) {
                    loader = ChainInit.class.getClassLoader();
                }
                Class<? extends RuleSet> clazz = loader
                        .loadClass(ruleSet)
                        .asSubclass(RuleSet.class);
                parser.setRuleSet(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new ServletException("Exception initalizing RuleSet '"
                                           + ruleSet + "' instance ", e);
            }
        }

        // Parse the resources specified in our init parameters (if any)
        final CheckedConsumer<URL, Exception> parse;
        if (attr == null) {
            parse = parser::parse;
        } else {
            final Catalog<?> cat = catalog;
            parse = url -> parser.parse(cat, url);
        }
        if (parseJarResources) {
            parseJarResources(context, parse, log);
        }
        ChainResources.parseClassResources(classResources, parse);
        ChainResources.parseWebResources(context, webResources, parse);

        // Expose the completed catalog (if requested)
        if (attr != null) {
            context.setAttribute(attr, catalog);
        }
    }

    // --------------------------------------------------------- Private Methods

    /**
     * Parse resources found in JAR files in the {@code /WEB-INF/lib}
     * subdirectory (if any).
     *
     * @param <E> the type of the exception from parse-function
     * @param context {@code ServletContext} for this web application
     * @param parse parse-function to parse the XML document
     * @param log to use for logging
     */
    private static <E extends Exception> void parseJarResources(ServletContext context,
                CheckedConsumer<URL, E> parse, Log log) {

        Set<String> jars = context.getResourcePaths("/WEB-INF/lib");
        if (jars == null) {
            jars = Collections.emptySet();
        }
        String path = null;
        Iterator<String> paths = jars.iterator();
        while (paths.hasNext()) {

            path = paths.next();
            if (!path.endsWith(".jar")) {
                continue;
            }
            URL resourceURL = null;
            try {
                URL jarURL = context.getResource(path);
                path = jarURL.toExternalForm();

                resourceURL = new URL("jar:"
                                      + translate(path)
                                      + "!/META-INF/chain-config.xml");
                path = resourceURL.toExternalForm();

                InputStream is = null;
                try {
                    is = resourceURL.openStream();
                } catch (Exception e) {
                    // means there is no such resource
                    if (log.isTraceEnabled()) {
                        log.trace("OpenStream: " + resourceURL, e);
                    }
                }
                if (is == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Not Found: " + resourceURL);
                    }
                    continue;
                } else {
                    is.close();
                }
                if (log.isDebugEnabled()) {
                    log.debug("Parsing: " + resourceURL);
                }
                parse.accept(resourceURL);
            } catch (Exception e) {
                throw new RuntimeException("Exception parsing chain config resource '"
                     + path + "': " + e.getMessage());
            }
        }
    }

    /**
     * Translate space character into {@code %20} to avoid problems
     * with paths that contain spaces on some JVMs.
     *
     * @param value Value to translate
     *
     * @return the translated value
     */
    private static String translate(String value) {
        while (true) {
            int index = value.indexOf(' ');
            if (index < 0) {
                break;
            }
            value = value.substring(0, index) + "%20" + value.substring(index + 1);
        }
        return value;
    }
}