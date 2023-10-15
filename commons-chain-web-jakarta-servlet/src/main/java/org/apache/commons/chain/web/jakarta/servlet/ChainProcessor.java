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
package org.apache.commons.chain.web.jakarta.servlet;

import java.io.IOException;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.web.jakarta.ChainServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom subclass of {@link ChainServlet} that also dispatches incoming
 * requests to a configurable {@link Command} loaded from the specified
 * {@link Catalog}.
 *
 * <p>In addition to the <em>servlet</em> init parameters supported by
 * {@link ChainServlet}, this class supports the following additional
 * parameters:</p>
 * <ul>
 * <li><strong>org.apache.commons.chain.CATALOG</strong> - Name of the
 *     catalog from which to acquire commands to be executed. If not
 *     specified, the default catalog for this application will be used.</li>
 * <li><strong>org.apache.commons.chain.COMMAND</strong> - Name of the
 *     {@link Command} (looked up in our configured {@link Catalog} used
 *     to process all incoming servlet requests. If not specified,
 *     defaults to {@code command}.</li>
 * </ul>
 *
 * <p>Also, the {@code org.apache.commons.chain.CONFIG_ATTR}
 * init parameter is also used to identify the
 * {@link org.apache.commons.chain.Context} attribute under
 * which our configured {@link Catalog} will be made available to
 * {@link Command}s processing our requests, in addition to its definition
 * of the {@code ServletContext} attribute key under which the
 * {@link Catalog} is available.</p>
 */

public class ChainProcessor extends ChainServlet {
    private static final long serialVersionUID = -6817532768031279260L;

    // ------------------------------------------------------ Manifest Constants

    /**
     * The name of the servlet init parameter containing the name of the
     * {@link Catalog} to use for processing incoming requests.
     */
    public static final String CATALOG =
        "org.apache.commons.chain.CATALOG";

    /**
     * The default request attribute under which we expose the
     * {@link Catalog} being used to subordinate {@link Command}s.
     */
    public static final String CATALOG_DEFAULT =
        "org.apache.commons.chain.CATALOG";

    /**
     * The name of the servlet init parameter containing the name of the
     * {@link Command} (loaded from our configured {@link Catalog} to use
     * for processing each incoming request.
     */
    public static final String COMMAND =
        "org.apache.commons.chain.COMMAND";

    /**
     * The default command name.
     */
    private static final String COMMAND_DEFAULT = "command";

    // ------------------------------------------------------ Instance Variables

    /**
     * The name of the context attribute under which our {@link Catalog}
     * is stored. This value is also used as the name of the context
     * attribute under which the catalog is exposed to commands. If not
     * specified, we will look up commands in the appropriate
     * {@link Catalog} retrieved from our {@link CatalogFactory}
     */
    private String attribute = null;

    /**
     * The name of the {@link Catalog} to retrieve from the
     * {@link CatalogFactory} for this application, or {@code null}
     * to select the default {@link Catalog}.
     */
    private String catalog = null;

    /**
     * The name of the {@link Command} to be executed for each incoming
     * request.
     */
    private String command = null;

    // ------------------------------------------------------------ Constructors

    /**
     * The Default-Constructor for this class.
     */
    public ChainProcessor() {
    }

    // --------------------------------------------------------- Servlet Methods

    /**
     * Clean up as this application is shut down.
     */
    @Override
    public void destroy() {
        super.destroy();
        attribute = null;
        catalog = null;
        command = null;
    }

    /**
     * Cache the name of the command we should execute for each request.
     *
     * @throws ServletException if an initialization error occurs
     */
    @Override
    public void init() throws ServletException {
        super.init();
        attribute = getServletConfig().getInitParameter(CONFIG_ATTR);
        catalog = getServletConfig().getInitParameter(CATALOG);
        command = getServletConfig().getInitParameter(COMMAND);
        if (command == null) {
            command = COMMAND_DEFAULT;
        }
    }

    /**
     * Configure a {@link ServletWebContext} for the current request, and
     * pass it to the {@code execute()} method of the specified
     * {@link Command}, loaded from our configured {@link Catalog}.
     *
     * @param request The request we are processing
     * @param response The response we are creating
     *
     * @throws IOException if an input/output error occurs
     * @throws ServletException if a servlet exception occurs
     */
    @Override
    public void service(HttpServletRequest request,
                        HttpServletResponse response)
        throws IOException, ServletException {

        ServletWebContext context =
            new ServletWebContext(getServletContext(), request, response);
        Catalog<ServletWebContext> theCatalog = null;
        if (attribute != null) {
            @SuppressWarnings("unchecked")
            Catalog<ServletWebContext> catalog = (Catalog<ServletWebContext>)
                    getServletContext().getAttribute(this.attribute);
            theCatalog = catalog;
        } else if (catalog != null) {
            theCatalog = CatalogFactory.<ServletWebContext>getInstance().getCatalog(catalog);
        } else {
            theCatalog = CatalogFactory.<ServletWebContext>getInstance().getCatalog();
        }
        if (attribute == null) {
            request.setAttribute(CATALOG_DEFAULT, theCatalog);
        }
        Command<ServletWebContext> command = theCatalog.getCommand(this.command);
        try {
            command.execute(context);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}