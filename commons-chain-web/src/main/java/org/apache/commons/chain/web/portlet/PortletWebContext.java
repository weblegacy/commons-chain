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
package org.apache.commons.chain.web.portlet;

import java.util.Collections;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.http.Cookie;

import org.apache.commons.chain.web.WebContext;

/**
 * Concrete implementation of {@link WebContext} suitable for use in
 * portlets. The abstract methods are mapped to the appropriate
 * collections of the underlying portlet context, request, and response
 * instances that are passed to the constructor (or the initialize method).
 *
 * @author Craig R. McClanahan
 */
public class PortletWebContext extends WebContext {
    private static final long serialVersionUID = 7590757017994210786L;

    // ------------------------------------------------------ Instance Variables

    /**
     * The lazily instantiated {@code Map} of application scope
     * attributes.
     */
    private transient Map<String, Object> applicationScope = null;

    /**
     * The {@code PortletContext} for this web application.
     */
    private transient PortletContext context = null;

    /**
     * The lazily instantiated {@code Map} of header name-value
     * combinations (immutable).
     */
    private transient Map<String, String> header = null;

    /**
     * The lazily instantiated {@code Map} of header name-values
     * combinations (immutable).
     */
    private transient Map<String, String[]> headerValues = null;

    /**
     * The lazily instantiated {@code Map} of context initialization
     * parameters.
     */
    private transient Map<String, String> initParam = null;

    /**
     * The lazily instantiated {@code Map} of cookies.
     */
    private transient Map<String, Cookie> cookieValues = null;

    /**
     * The lazily instantiated {@code Map} of request
     * parameter name-value.
     */
    private transient Map<String, String> param = null;

    /**
     * The lazily instantiated {@code Map} of request
     * parameter name-values.
     */
    private transient Map<String, String[]> paramValues = null;

    /**
     * The {@code PortletRequest} for this request.
     */
    private transient PortletRequest request = null;

    /**
     * The lazily instantiated {@code Map} of request scope
     * attributes.
     */
    private transient Map<String, Object> requestScope = null;

    /**
     * The {@code PortletResponse} for this request.
     */
    private transient PortletResponse response = null;

    /**
     * The lazily instantiated {@code Map} of session scope
     * attributes.
     */
    private transient Map<String, Object> sessionScope = null;

    // ------------------------------------------------------------ Constructors

    /**
     * Construct an uninitialized {@link PortletWebContext} instance.
     */
    public PortletWebContext() {
    }

    /**
     * Construct a {@link PortletWebContext} instance that is initialized
     * with the specified Portlet API objects.
     *
     * @param context The {@code PortletContext} for this web application
     * @param request The {@code PortletRequest} for this request
     * @param response The {@code PortletResponse} for this request
     */
    public PortletWebContext(PortletContext context,
                             PortletRequest request,
                             PortletResponse response) {

        initialize(context, request, response);
    }

    // ---------------------------------------------------------- Public Methods

    /**
     * Return the {@link PortletContext} for this context.
     *
     * @return The {@code PortletContext} for this request
     */
    public PortletContext getContext() {
        return this.context;
    }

    /**
     * Return the {@link PortletRequest} for this context.
     *
     * @return The {@code PortletRequest} for this context.
     */
    public PortletRequest getRequest() {
        return this.request;
    }

    /**
     * Return the {@link PortletResponse} for this context.
     *
     * @return The {@code PortletResponse} for this context.
     */
    public PortletResponse getResponse() {
        return this.response;
    }

    /**
     * Initialize (or reinitialize) this {@link PortletWebContext} instance
     * for the specified Portlet API objects.
     *
     * @param context The {@code PortletContext} for this web application
     * @param request The {@code PortletRequest} for this request
     * @param response The {@code PortletResponse} for this request
     */
    public void initialize(PortletContext context,
                           PortletRequest request,
                           PortletResponse response) {

        // Save the specified Portlet API object references
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

        // Release references to Portlet API objects
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
    public Map<String, Object> getApplicationScope() {
        if (applicationScope == null && context != null) {
            applicationScope = new PortletApplicationScopeMap(context);
        }
        return applicationScope;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Header values Map.
     */
    @Override
    public Map<String, String> getHeader() {
        if (header == null && request != null) {
            // header = new PortletHeaderMap(request);
            header = Collections.emptyMap();
        }
        return header;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Header values Map.
     */
    @Override
    public Map<String, String[]> getHeaderValues() {
        if (headerValues == null && request != null) {
            // headerValues = new PortletHeaderValuesMap(request);
            headerValues = Collections.emptyMap();
        }
        return headerValues;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Initialization parameter Map.
     */
    @Override
    public Map<String, String> getInitParam() {
        if (initParam == null && context != null) {
            initParam = new PortletInitParamMap(context);
        }
        return initParam;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Request parameter Map.
     */
    @Override
    public Map<String, String> getParam() {
        if (param == null && request != null) {
            param = new PortletParamMap(request);
        }
        return param;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Request parameter Map.
     */
    @Override
    public Map<String, String[]> getParamValues() {
        if (paramValues == null && request != null) {
            paramValues = new PortletParamValuesMap(request);
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
    public Map<String, Cookie> getCookies() {
        if (cookieValues == null && request != null) {
            cookieValues = new PortletCookieMap(request);
        }
        return cookieValues;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Request scope Map.
     */
    @Override
    public Map<String, Object> getRequestScope() {
        if (requestScope == null && request != null) {
            requestScope = new PortletRequestScopeMap(request);
        }
        return requestScope;
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Session scope Map.
     */
    @Override
    public Map<String, Object> getSessionScope() {
        if (sessionScope == null && request != null) {
            sessionScope = new PortletSessionScopeMap(request);
        }
        return sessionScope;
    }
}