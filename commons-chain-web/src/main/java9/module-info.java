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
@SuppressWarnings({"requires-automatic", "requires-transitive-automatic"})
module org.apache.commons.chain.web {
    requires transitive org.apache.commons.chain;

    requires commons.logging;
    requires transitive static javax.faces.api;
    requires transitive static javax.servlet.api;
    requires transitive static portlet.api;

    exports org.apache.commons.chain.web;
    exports org.apache.commons.chain.web.faces;
    exports org.apache.commons.chain.web.portlet;
    exports org.apache.commons.chain.web.servlet;
}