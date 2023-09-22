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
package org.apache.commons.chain.web.javax.faces;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;

import org.apache.commons.chain.web.javax.WebContext;

/**
 * Concrete implementation of {@link WebContext} suitable for use in
 * JavaServer Faces apps. The abstract methods are mapped to the appropriate
 * collections of the underlying {@code FacesContext} instance
 * that is passed to the constructor (or the initialize method).
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class FacesWebContext extends WebContext {
    private static final long serialVersionUID = -1429681424077509130L;

    // ------------------------------------------------------ Instance Variables

    /**
     * The {@code FacesContext} instance for the request represented
     * by this {@link WebContext}.
     */
    private transient FacesContext context = null;

    /**
     * The lazily instantiated {@code Map} of cookies.
     */
    private transient Map<String, Cookie> cookieValues = null;

    // ------------------------------------------------------------ Constructors

    /**
     * Construct an uninitialized {@link FacesWebContext} instance.
     */
    public FacesWebContext() {
    }

    /**
     * Construct a {@link FacesWebContext} instance that is initialized
     * with the specified JavaServer Faces API objects.
     *
     * @param context The {@code FacesContext} for this request
     */
    public FacesWebContext(FacesContext context) {
        initialize(context);
    }
    // ---------------------------------------------------------- Public Methods

    /**
     * Return the {@code FacesContext} instance for the request
     * associated with this {@link FacesWebContext}.
     *
     * @return The {@code FacesContext} for this request
     */
    public FacesContext getContext() {
        return this.context;
    }

    /**
     * Initialize (or reinitialize) this {@link FacesWebContext} instance
     * for the specified JavaServer Faces API objects.
     *
     * @param context The {@code FacesContext} for this request
     */
    public void initialize(FacesContext context) {
        this.context = context;
    }

    /**
     * Release references to allocated resources acquired in
     * {@code initialize()} of via subsequent processing. After this
     * method is called, subsequent calls to any other method than
     * {@code initialize()} will return undefined results.
     */
    public void release() {
        context = null;
        cookieValues = null;
    }

    // ------------------------------------------------------ WebContext Methods

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Application scope Map.
     */
    @Override
    public Map<String, Object> getApplicationScope() {
        return context.getExternalContext().getApplicationMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Header values Map.
     */
    @Override
    public Map<String, String> getHeader() {
        return context.getExternalContext().getRequestHeaderMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Header values Map.
     */
    @Override
    public Map<String, String[]> getHeaderValues() {
        return context.getExternalContext().getRequestHeaderValuesMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Initialization parameter Map.
     */
    @Override
    public Map<String, String> getInitParam() {
        return context.getExternalContext().getInitParameterMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Request parameter Map.
     */
    @Override
    public Map<String, String> getParam() {
        return context.getExternalContext().getRequestParameterMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Request parameter Map.
     */
    @Override
    public Map<String, String[]> getParamValues() {
        return context.getExternalContext().getRequestParameterValuesMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Map of Cookies.
     * @since Chain 1.1
     */
    @Override
    public Map<String, Cookie> getCookies() {
        if (cookieValues == null) {
            final Map<String, Object> cookiesSrc = context.getExternalContext().getRequestCookieMap();
            final Map<String, Cookie> cookiesDest = new HashMap<>(cookiesSrc.size());

            cookiesSrc.forEach((k, v) -> cookiesDest.put(k, (Cookie) v));

            cookieValues = Collections.unmodifiableMap(cookiesDest);
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
        return context.getExternalContext().getRequestMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Session scope Map.
     */
    @Override
    public Map<String, Object> getSessionScope() {
        return context.getExternalContext().getSessionMap();
    }
}