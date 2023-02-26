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
package org.apache.commons.chain.web.faces;

import java.util.Map;
import javax.faces.context.FacesContext;
import org.apache.commons.chain.web.WebContext;

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

    // ------------------------------------------------------ Instance Variables

    /**
     * The {@code FacesContext} instance for the request represented
     * by this {@link WebContext}.
     */
    private FacesContext context = null;

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
    }

    // ------------------------------------------------------ WebContext Methods

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Application scope Map.
     */
    @Override
    public Map getApplicationScope() {
        return context.getExternalContext().getApplicationMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Header values Map.
     */
    @Override
    public Map getHeader() {
        return context.getExternalContext().getRequestHeaderMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Header values Map.
     */
    @Override
    public Map getHeaderValues() {
        return context.getExternalContext().getRequestHeaderValuesMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Initialization parameter Map.
     */
    @Override
    public Map getInitParam() {
        return context.getExternalContext().getInitParameterMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Request parameter Map.
     */
    @Override
    public Map getParam() {
        return context.getExternalContext().getRequestParameterMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Request parameter Map.
     */
    @Override
    public Map getParamValues() {
        return context.getExternalContext().getRequestParameterValuesMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Map of Cookies.
     * @since Chain 1.1
     */
    @Override
    public Map getCookies() {
        return context.getExternalContext().getRequestCookieMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Request scope Map.
     */
    @Override
    public Map getRequestScope() {
        return context.getExternalContext().getRequestMap();
    }

    /**
     * See the {@link WebContext}'s Javadoc.
     *
     * @return Session scope Map.
     */
    @Override
    public Map getSessionScope() {
        return context.getExternalContext().getSessionMap();
    }
}