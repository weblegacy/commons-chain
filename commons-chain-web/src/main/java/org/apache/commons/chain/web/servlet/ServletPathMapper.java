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
package org.apache.commons.chain.web.servlet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.generic.LookupCommand;

/**
 * {@link Command} that uses the "servlet path" component of the request URI
 * to select a {@link Command} from the appropriate {@link Catalog}, and
 * execute it. To use this command, you would typically map an instance
 * of {@link ChainProcessor} to an extension pattern like "*.execute" and
 * then arrange that this is the default command to be executed. In such
 * an environment, a request for a context relative URI of "/foo.execute"
 * would cause the "/foo.execute" command to be loaded and executed.
 *
 * @author Craig R. McClanahan
 */
public class ServletPathMapper extends LookupCommand<ServletWebContext> {

    // ------------------------------------------------------ Instance Variables

    private String catalogKey = ChainProcessor.CATALOG_DEFAULT;

    // ------------------------------------------------------------ Constructors

    /**
     * The Default-Constructor for this class.
     */
    public ServletPathMapper() {
    }

    // -------------------------------------------------------------- Properties

    /**
     * Return the context key under which our {@link Catalog} has been
     * stored.
     *
     * @return The context key for the Catalog.
     *
     * @deprecated Use catalogName to specify the name of the catalog in the
     *  catalog factory
     */
    @Deprecated
    public String getCatalogKey() {
        return this.catalogKey;
    }

    /**
     * Set the context key under which our {@link Catalog} has been
     * stored.
     *
     * @param catalogKey The new catalog key
     *
     * @deprecated Use catalogName to specify the name of the catalog in the
     *  catalog factory
     */
    @Deprecated
    public void setCatalogKey(String catalogKey) {
        this.catalogKey = catalogKey;
    }

    // --------------------------------------------------------- Command Methods

    /**
     * Look up the servlet path information for this request, and use it to
     * select an appropriate {@link Command} to be executed.
     *
     * @param context Context for the current request
     *
     * @return The name of the {@link Command} instance
     *
     * @since Chain 1.2
     */
    @Override
    protected String getCommandName(ServletWebContext context) {
        // Look up the servlet path for this request
        final HttpServletRequest request = context.getRequest();
        final Object attrServletPath = request.getAttribute("javax.servlet.include.servlet_path");
        final String servletPath = attrServletPath == null
                ? request.getServletPath()
                : attrServletPath.toString();

        return servletPath;
    }

    /**
     * Return the {@link Catalog} to look up the {@link Command} in.
     *
     * @param context {@link Context} for this request
     *
     * @return The catalog.
     *
     * @throws IllegalArgumentException if no {@link Catalog}
     *         can be found
     *
     * @since Chain 1.2
     */
    @Override
    protected Catalog<ServletWebContext> getCatalog(ServletWebContext context) {
        /* If the object returned from the passed context is not a valid catalog
         * then we use the super class's catalog extraction logic to pull it
         * or to error gracefully.
         */
        Object testCatalog = context.get(getCatalogKey());

        /* Assume that the underlying implementation is following convention and
         * returning a catalog with the current context.
         */
        @SuppressWarnings("unchecked")
        Catalog<ServletWebContext> catalog = testCatalog instanceof Catalog
                    ? (Catalog<ServletWebContext>) testCatalog
                    : super.getCatalog(context);

        return catalog;
    }
}