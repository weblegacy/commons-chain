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
package org.apache.commons.chain.web.javax.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.chain.web.AbstractSessionScopeMap;
import org.apache.commons.chain.web.MutableParameterMap;

/**
 * Private implementation of {@code Map} for HTTP session attributes.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
final class ServletSessionScopeMap extends AbstractSessionScopeMap<HttpSession, HttpServletRequest> {

    /**
     * The constructor for the servlet session attributes.
     *
     * @param request the servlet-request.
     */
    ServletSessionScopeMap(HttpServletRequest request) {
        super(request);
    }

    /**
     * Returns the current {@code HttpSession} associated with this request or, if
     * there is no current session and <code>create</code> is true, returns a new
     * session.
     *
     * <p>If {@code create} is {@code false} and the request has no valid
     * {@code HttpSession}, this method returns {@code null}.</p>
     *
     * <p>To make sure the session is properly maintained, you must call this
     * method before the response is committed. If the container is using cookies
     * to maintain session integrity and is asked to create a new session when the
     * response is committed, an IllegalStateException is thrown.</p>
     *
     * @param create {@code true} to create a new session for this request if
     *               necessary; {@code false} to return {@code null} if there's no
     *               current session
     *
     * @return the {@code HttpSession} associated with this request or {@code null}
     *         if {@code create} is {@code false} and the request has no valid
     *         session
     */
    @Override
    protected HttpSession getSession(final boolean create) {
        return getRequest().getSession(create);
    }

    /**
     * Creates a new mutable-parameter-map to access the session-attributes.
     *
     * @return a new mutable-parameter-map to access the session-attributes
     */
    @Override
    protected MutableParameterMap<HttpSession, Object> createParameterMap() {
        return new MutableParameterMap<>(getSession(), getSession()::getAttribute,
                getSession()::getAttributeNames, getSession()::removeAttribute,
                getSession()::setAttribute);
    }
}