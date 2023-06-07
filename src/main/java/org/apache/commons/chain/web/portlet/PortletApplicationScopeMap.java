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

import javax.portlet.PortletContext;

import org.apache.commons.chain.internal.MutableParameterMap;

/**
 * Private implementation of {@code Map} for portlet context
 * attributes.
 *
 * @author Craig R. McClanahan
 */
final class PortletApplicationScopeMap extends MutableParameterMap<Object, Object> {

    /**
     * The constructor for the portlet context attributes.
     *
     * @param context the portlet-context for the adapter.
     */
    PortletApplicationScopeMap(PortletContext context) {
        super(context, context::getAttribute, context::getAttributeNames,
                context::removeAttribute, context::setAttribute);
    }
}
