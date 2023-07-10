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
package org.apache.commons.chain2.cookbook.agility.impl;

import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.cookbook.agility.Controller;
import org.apache.commons.chain2.cookbook.agility.Request;
import org.apache.commons.chain2.cookbook.agility.RequestHandler;
import org.apache.commons.chain2.impl.CatalogBase;

/**
 */
public class ControllerCatalog extends CatalogBase<String, Object, RequestContext>
        implements Controller {

    public RequestHandler getHandler(String name) {

        return (RequestHandler) getCommand(name);
    }

    public void addHandler(RequestHandler handler) {
        @SuppressWarnings("unchecked")
        Command<String, Object, RequestContext> command =
                (Command<String, Object, RequestContext>)handler;

        this.addCommand(handler.getName(), command);
    }

    public void process(Request request) {
        RequestHandler handler = getHandler(request.getName());

        if (handler != null) {
            handler.handle(request);
        }
    }

}
