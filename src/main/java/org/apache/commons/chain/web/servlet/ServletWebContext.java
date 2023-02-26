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

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.chain.web.WebContext;

/**
 * Concrete implementation of {@link WebContext} suitable for use in
 * Servlets and JSP pages. The abstract methods are mapped to the appropriate
 * collections of the underlying servlet context, request, and response
 * instances that are passed to the constructor (or the initialize method).
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class ServletWebContext extends WebContext {

    // ------------------------------------------------------------ Constructors

    /**
     * Construct an uninitialized {@link ServletWebContext} instance.
     */
    public ServletWebContext() {
    }

    /**
     * Construct a {@link ServletWebContext} instance that is initialized
     * with the specified Servlet API objects.
     *
     * @param context The {@code ServletContext} for this web application
     * @param request The {@code HttpServletRequest} for this request
     * @param response The {@code HttpServletResponse} for this request
     */
    public ServletWebContext(ServletContext context,
                             HttpServletRequest request,
                             HttpServletResponse response) {

        initialize(context, request, response);
    }

    // ------------------------------------------------------ Instance Variables

    /**
     * The lazily instantiated {@code Map} of application scope
     * attributes.
     */
    private Map applicationScope = null;

    /**
     * The {@code ServletContext} for this web application.
     */
    protected ServletContext context = null;

    /**
     * The lazily instantiated {@code Map} of header name-value
     * combinations (immutable).
     */
    private Map header = null;

    /**
     * The lazily instantiated {@code Map} of header name-values
     * combinations (immutable).
     */
    private Map headerValues = null;

    /**
     * The lazily instantiated {@code Map} of context initialization
     * parameters.
     */
    private Map initParam = null;

    /**
     * The lazily instantiated {@code Map} of cookies.
     */
    private Map cookieValues = null;

    /**
     * The lazily instantiated {@code Map} of request
     * parameter name-value.
     */
    private Map param = null;

    /**
     * The lazily instantiated {@code Map} of request
     * parameter name-values.
     */
    private Map paramValues = null;

    /**
     * The {@code HttpServletRequest} for this request.
     */
    protected HttpServletRequest request = null;

    /**
     * The lazily instantiated {@code Map} of request scope
     * attributes.
     */
    private Map requestScope = null;

    /**
     * The {@code HttpServletResponse} for this request.
     */
    protected HttpServletResponse response = null;

    /**
     * The lazily instantiated {@code Map} of session scope
     * attributes.
     */
    private Map sessionScope = null;

    // ---------------------------------------------------------- Public Methods

    /**
     * Return the {@link ServletContext} for this context.
     *
     * @return The {@code ServletContext} for this context.
     */
    public ServletContext getContext() {
        return this.context;
    }

    /**
     * Return the {@link HttpServletRequest} for this context.
     *
     * @return The {@code HttpServletRequest} for this context.
     */
    public HttpServletRequest getRequest() {
        return this.request;
    }

    /**
     * Return the {@link HttpServletResponse} for this context.
     *
     * @return The {@code HttpServletResponse} for this context.
     */
    public HttpServletResponse getResponse() {
        return this.response;
    }

    /**
     * Initialize (or reinitialize) this {@link ServletWebContext} instance
     * for the specified Servlet API objects.
     *
     * @param context The {@code ServletContext} for this web application
     * @param request The {@code HttpServletRequest} for this request
     * @param response The {@code HttpServletResponse} for this request
     */
    public void initialize(ServletContext context,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        // Save the specified Servlet API object references
        this.context = context;
        this.request = request;
        this.response = response;

        // Perform other setup as needed
    }

    /**
     * Release references to allocated resources acquired in
     * {@code initialize()} of via subsequent processing. After this
     * method is called, subsequent calls to any other method than
     * {@code initialize()} will return undefined results.
     */
    public void release() {
        // Release references to allocated collections
        applicationScope = null;
        header = null;
        headerValues = null;
        initParam = null;
        param = null;
        paramValues = null;
        cookieValues = null;
        requestScope = null;
        sessionScope = null;

        // Release references to Servlet API objects
        context = null;
        request = null;
        response = null;
    }

    // ------------------------------------------------------ WebContext Methods

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Application scope Map.
     */
    @Override
    public Map getApplicationScope() {
        if (applicationScope == null && context != null) {
            applicationScope = new ServletApplicationScopeMap(context);
        }
        return applicationScope;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Header values Map.
     */
    @Override
    public Map getHeader() {
        if (header == null && request != null) {
            header = new ServletHeaderMap(request);
        }
        return header;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Header values Map.
     */
    @Override
    public Map getHeaderValues() {
        if (headerValues == null && request != null) {
            headerValues = new ServletHeaderValuesMap(request);
        }
        return headerValues;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Initialization parameter Map.
     */
    @Override
    public Map getInitParam() {
        if (initParam == null && context != null) {
            initParam = new ServletInitParamMap(context);
        }
        return initParam;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Request parameter Map.
     */
    @Override
    public Map getParam() {
        if (param == null && request != null) {
            param = new ServletParamMap(request);
        }
        return param;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Request parameter Map.
     */
    @Override
    public Map getParamValues() {
        if (paramValues == null && request != null) {
            paramValues = new ServletParamValuesMap(request);
        }
        return paramValues;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Map of Cookies.
     * @since Chain 1.1
     */
    @Override
    public Map getCookies() {
        if (cookieValues == null && request != null) {
            cookieValues = new ServletCookieMap(request);
        }
        return cookieValues;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Request scope Map.
     */
    @Override
    public Map getRequestScope() {
        if (requestScope == null && request != null) {
            requestScope = new ServletRequestScopeMap(request);
        }
        return requestScope;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Session scope Map.
     */
    @Override
    public Map getSessionScope() {
        if (sessionScope == null && request != null) {
            sessionScope = new ServletSessionScopeMap(request);
        }
        return sessionScope;
    }
}