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

import org.apache.commons.chain.web.javax.GetLocaleCommand;

/**
 * Concrete implementation of {@link GetLocaleCommand} for
 * the Servlet API.
 */
public class ServletGetLocaleCommand extends GetLocaleCommand<ServletWebContext> {

    /**
     * Construct a new instance to get the locale from the
     * Servlet API.
     */
    public ServletGetLocaleCommand() {
        super(c -> c.getRequest().getLocale());
    }
}