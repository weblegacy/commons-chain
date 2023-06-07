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

import org.apache.commons.chain.internal.ParameterMap;

/**
 * Private implementation of {@code Map} for portlet parameter
 * name-value.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
final class PortletParamMap extends ParameterMap<PortletRequest, String> {

    /**
     * The constructor for the portlet parameter name-value.
     *
     * @param request the portlet-request for the adapter.
     */
    @SuppressWarnings("deprecation")
    PortletParamMap(PortletRequest request) {
        super(request, request::getParameter, request::getParameterNames);
    }
}