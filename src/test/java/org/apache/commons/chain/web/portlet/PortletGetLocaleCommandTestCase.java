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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Locale;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link org.apache.commons.chain.web.portlet.PortletGetLocaleCommand}
 */
public class PortletGetLocaleCommandTestCase {

    // ----------------------------------------------------- Instance Variables

    protected Locale locale = null;

    /**
     * Portlet API Objects - Context
     */
    protected PortletContext pcontext = null;

    /**
     * Portlet API Objects - Request
     */
    protected PortletRequest request = null;

    /**
     * Portlet API Objects - Response
     */

    protected PortletResponse response = null;

    /**
     * Portlet API Objects - Session
     */
    protected PortletSession session = null;

    /**
     * Chain API Objects - context
     */
    protected PortletWebContext context = null;

    /**
     * Chain API Objects - command
     */
    protected PortletGetLocaleCommand command = null;

    // -------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    @BeforeEach
    public void init() {
        locale = new Locale("en", "US");

        // Set up Portlet API Objects
        pcontext = new MockPortletContext();
        session = new MockPortletSession(pcontext);
        request = new MockPortletRequest(null, pcontext, session);
        ((MockPortletRequest) request).setLocale(locale);

        // Set up Chain API Objects
        context = new PortletWebContext(pcontext, request, response);
        command = new PortletGetLocaleCommand();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @AfterEach
    public void tearDown() {
        pcontext = null;
        session = null;
        request = null;
        response = null;

        context = null;
        command = null;
    }

    // ------------------------------------------------- Individual Test Methods

    /**
     * Test configured behavior
     *
     * @throws Exception any error
     */
    @Test
    public void testConfigured() throws Exception {
        command.setLocaleKey("special");
        assertEquals("special", command.getLocaleKey());
        check(context, command);
    }

    /**
     * Test default behavior
     *
     * @throws Exception any error
     */
    @Test
    public void testDefaut() throws Exception {
        assertEquals("locale", command.getLocaleKey());
        check(context, command);
    }

    // --------------------------------------------------------- Support Methods

    protected void check(PortletWebContext context, PortletGetLocaleCommand command)
            throws Exception {

        String localeKey = command.getLocaleKey();
        assertNotNull(localeKey);
        Object value = context.get(localeKey);
        assertNull(value);
        boolean result = command.execute(context);
        assertFalse(result);
        value = context.get(localeKey);
        assertNotNull(value);
        assertInstanceOf(Locale.class, value);
        assertEquals(locale, value);
    }
}