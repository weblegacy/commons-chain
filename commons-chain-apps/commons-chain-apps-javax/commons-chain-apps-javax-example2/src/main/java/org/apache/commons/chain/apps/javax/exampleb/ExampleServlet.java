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
package org.apache.commons.chain.apps.javax.exampleb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.web.javax.servlet.ServletWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom chain servlet implementation.
 */
public class ExampleServlet extends HttpServlet {
    private static final long serialVersionUID = 3232461564544399204L;

    private String servletName;

    /**
     * Cache the name of the servlet.
     *
     * @throws ServletException if an initialization error occurs
     */
    public void init() throws ServletException {
        super.init();
        Logger logger = LoggerFactory.getLogger(ExampleServlet.class);
        servletName = getServletConfig().getServletName();
        logger.info("Initializing chain example servlet '{}'", servletName);
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
    public void service(HttpServletRequest request,
                        HttpServletResponse response)
        throws IOException, ServletException {

        CatalogFactory<ServletWebContext> factory = CatalogFactory.getInstance();
        Catalog<ServletWebContext> catalog = factory.getCatalog(servletName);
        if (catalog == null) {
            catalog = factory.getCatalog();
        }

        ServletWebContext context =
            new ServletWebContext(getServletContext(), request, response);
        Command<ServletWebContext> command = catalog.getCommand("COMMAND_MAPPER");
        try {
            command.execute(context);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}