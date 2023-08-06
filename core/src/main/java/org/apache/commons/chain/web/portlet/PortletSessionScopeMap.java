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

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.commons.chain.internal.MutableParameterMap;
import org.apache.commons.chain.internal.AbstractSessionScopeMap;

/**
 * Private implementation of {@code Map} for portlet session attributes.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
final class PortletSessionScopeMap extends AbstractSessionScopeMap<PortletSession, PortletRequest> {

    /**
     * The constructor for the portlet session attributes.
     *
     * @param request the portlet-request.
     */
    PortletSessionScopeMap(PortletRequest request) {
        super(request);
    }

    /**
     * Returns the current portlet session or, if there is no current session and
     * the given flag is {@code true}, creates one and returns the new session.
     *
     * <p>If the given flag is {@code false} and there is no current portlet
     * session, this method returns {@code null}.</p>
     *
     * <p>Creating a new portlet session will result in creating a new
     * {@code HttpSession} on which the portlet session is based.</p>
     *
     * @param create {@code true} to create a new session, {@code false} to return
     *               {@code null} if there is no current session
     *
     * @return the portlet session
     */
    @Override
    protected PortletSession getSession(final boolean create) {
        return getRequest().getPortletSession(create);
    }

    /**
     * Creates a new mutable-parameter-map to access the session-attributes.
     *
     * @return a new mutable-parameter-map to access the session-attributes
     */
    @Override
    protected MutableParameterMap<PortletSession, Object> createParameterMap() {
        return new MutableParameterMap<>(getSession(), getSession()::getAttribute,
                () -> getSession().getAttributeNames(PortletSession.PORTLET_SCOPE),
                getSession()::removeAttribute, getSession()::setAttribute);
    }
}