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
package org.apache.commons.chain.config;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

/**
 * Digester rule that will cause the top-most element on the Digester
 * stack (if it is a {@link Command} to be registered with the next-to-top
 * element on the Digester stack (if it is a {@link Catalog} or {@link Chain}).
 * To be registered with a {@link Catalog}, the top-most element must contain
 * a value for the specified attribute that contains the name under which
 * it should be registered.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
class ConfigRegisterRule extends Rule {

    // ----------------------------------------------------- Instance Variables

    /**
     * The name of the attribute under which we can retrieve the name
     * this command should be registered with.
     */
    private final String nameAttribute;

    // ----------------------------------------------------------- Constructors

    /**
     * Construct a new instance of this rule that looks for an attribute
     * with the specified name.
     *
     * @param nameAttribute Name of the attribute containing the name under
     *  which this command should be registered
     */
    ConfigRegisterRule(String nameAttribute) {
        super();
        this.nameAttribute = nameAttribute;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Register the top {@link Command} if appropriate.
     *
     * @param namespace the namespace URI of the matching element, or an
     *        empty string if the parser is not namespace aware or the
     *        element has no namespace
     * @param name the local name if the parser is namespace aware, or
     *        just the element name otherwise
     * @param attributes The attribute list of this element
     */
    @Override
    public void begin(String namespace, String name, Attributes attributes)
        throws Exception {

        // Is the top object a Command?
        Object top = digester.peek(0);
        if (!(top instanceof Command)) {
            return;
        }

        /* All commands can consume a generic context. Here we depend on
         * the configuration being correct because the rule binding is
         * dynamic. */
        @SuppressWarnings("unchecked")
        Command<Context> command = (Command<Context>) top;

        // Is the next object a Catalog or a Chain?
        Object next = digester.peek(1);
        if (next == null) {
            return;
        }

        // Register the top element appropriately
        if (next instanceof Catalog) {
            String nameValue = attributes.getValue(nameAttribute);
            if (nameValue != null) {
                /* We are dynamically building a catalog and assigning
                 * generics to the most base types possible. */
                @SuppressWarnings("unchecked")
                Catalog<Context> catalog = (Catalog<Context>) next;
                catalog.addCommand(nameValue, command);
            }
        } else if (next instanceof Chain) {
            /* Like above - the chain is being dynamically generated,
             * so we can add a generic context signature at compile-time. */
            @SuppressWarnings("unchecked")
            Chain<Context> chain = (Chain<Context>) next;
            chain.addCommand(command);
        }
    }
}