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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.config.ConfigParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code ServletContextListener} that automatically
 * scans chain configuration files in the current web application at
 * startup time, and exposes the result in a {@link Catalog} under a
 * specified servlet context attribute. The following <em>context</em> init
 * parameters are utilized:
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
 * <li>Resources loaded from any {@code META-INF/chain-config.xml}
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
 * <p>This class requires Servlet 2.3 or later. If you are running on
 * Servlet 2.2 system, consider using {@link ChainServlet} instead.
 * Note that {@link ChainServlet} uses parameters of the
 * same names, but they are <em>servlet</em> init parameters instead
 * of <em>context</em> init parameters. Because of this, you can use
 * both facilities in the same application, if desired.</p>
 *
 * @author Craig R. McClanahan
 * @author Ted Husted
 * @version $Revision$ $Date$
 */
public class ChainListener implements ServletContextListener {

    // ------------------------------------------------------ Manifest Constants

    /**
     * The name of the context init parameter containing the name of the
     * servlet context attribute under which our resulting {@link Catalog}
     * will be stored.
     */
    public static final String CONFIG_ATTR = ChainInit.CONFIG_ATTR;

    /**
     * The name of the context init parameter containing a comma-delimited
     * list of class loader resources to be scanned.
     */
    public static final String CONFIG_CLASS_RESOURCE =
            ChainInit.CONFIG_CLASS_RESOURCE;

    /**
     * The name of the context init parameter containing a comma-delimited
     * list of web application resources to be scanned.
     */
    public static final String CONFIG_WEB_RESOURCE =
            ChainInit.CONFIG_WEB_RESOURCE;

    /**
     * The name of the context init parameter containing the fully
     * qualified class name of the {@code RuleSet} implementation
     * for configuring our {@link ConfigParser}.
     */
    public static final String RULE_SET = ChainInit.RULE_SET;

    // ------------------------------------------------------------ Constructors

    /**
     * The Default-Constructor for this class.
     */
    public ChainListener() {
    }

    // ------------------------------------------ ServletContextListener Methods

    /**
     * Remove the configured {@link Catalog} from the servlet context
     * attributes for this web application.
     *
     * @param event {@code ServletContextEvent} to be processed
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        final ServletContext context = event.getServletContext();
        ChainInit.destroy(context, context.getInitParameter(CONFIG_ATTR));
    }

    /**
     * Scan the required chain configuration resources, assemble the
     * configured chains into a {@link Catalog}, and expose it as a
     * servlet context attribute under the specified key.
     *
     * @param event {@code ServletContextEvent} to be processed
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        final Logger logger = LoggerFactory.getLogger(ChainListener.class);
        logger.info("Initializing chain listener");
        final ServletContext context = event.getServletContext();

        try {
            ChainInit.initialize(context, context.getInitParameter(CONFIG_ATTR), logger, true);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}