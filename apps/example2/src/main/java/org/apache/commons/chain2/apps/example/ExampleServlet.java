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
package org.apache.commons.chain2.apps.example;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.CatalogFactory;
import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.impl.CatalogFactoryBase;
import org.apache.commons.chain2.web.servlet.ServletWebContext;
import org.apache.commons.chain2.web.servlet.ServletWebContextBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Custom chain servlet implementation.
 *
 */
public class ExampleServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 20120724L;

    private String servletName;

    /**
     * <p>Cache the name of the servlet.</p>
     *
     * @throws ServletException if an initialization error occurs
     */
    public void init() throws ServletException {
        super.init();
        Log log = LogFactory.getLog(ExampleServlet.class);
        servletName = getServletConfig().getServletName();
        if (log.isInfoEnabled()) {
            log.info("Initializing chain example servlet '"
                     + servletName + "'");
        }
    }

    /**
     * <p>Configure a {@link org.apache.commons.chain2.web.servlet.ServletWebContext} for the current request, and
     * pass it to the <code>execute()</code> method of the specified
     * {@link Command}, loaded from our configured {@link Catalog}.</p>
     *
     * @param request The request we are processing
     * @param response The response we are creating
     *
     * @throws IOException if an input/output error occurs
     */
    public void service(HttpServletRequest request,
                        HttpServletResponse response)
        throws IOException {

        CatalogFactory<String, Object, ServletWebContext<String, Object>> factory =
                CatalogFactoryBase.getInstance();
        Catalog<String, Object, ServletWebContext<String, Object>> catalog =
                factory.getCatalog(servletName);

        if (catalog == null) {
            catalog = factory.getCatalog();
        }

        ServletWebContext<String, Object> context =
            new ServletWebContextBase(getServletContext(), request, response);

        Command<String, Object, ServletWebContext<String, Object>> command = catalog.
                <Command<String, Object, ServletWebContext<String, Object>>>getCommand("COMMAND_MAPPER");
        command.execute(context);
    }

}
