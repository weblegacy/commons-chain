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
package org.apache.commons.chain.web.javax.portlet;

import org.apache.commons.chain.web.javax.SetLocaleCommand;

/**
 * Concrete implementation of {@link SetLocaleCommand} for
 * the Portlet API.
 */
public class PortletSetLocaleCommand extends SetLocaleCommand<PortletWebContext> {

    /**
     * Construct a new instance to set the locale into the
     * Portlet API.
     */
    public PortletSetLocaleCommand() {
//      super((context, locale) -> context.getResponse().setLocale(locale));
//      Not supported by the Portlet API
        super((context, locale) -> { });
    }
}