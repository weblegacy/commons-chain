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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBaseTestCase;
import org.apache.commons.chain.web.WebContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@code ContextBaseTestCase} to validate the
 * extra functionality of this implementation.
 */
public class PortletWebContextTestCase extends ContextBaseTestCase {

    // ----------------------------------------------------- Instance Variables

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

    // -------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    @BeforeEach
    public void init() {
        pcontext = new MockPortletContext();
        pcontext.setAttribute("akey1", "avalue1");
        pcontext.setAttribute("akey2", "avalue2");
        pcontext.setAttribute("akey3", "avalue3");
        pcontext.setAttribute("akey4", "avalue4");
        ((MockPortletContext) pcontext).addInitParameter("ikey1", "ivalue1");
        ((MockPortletContext) pcontext).addInitParameter("ikey2", "ivalue2");
        ((MockPortletContext) pcontext).addInitParameter("ikey3", "ivalue3");
        session = new MockPortletSession(pcontext);
        session.setAttribute("skey1", "svalue1");
        session.setAttribute("skey2", "svalue2");
        session.setAttribute("skey3", "svalue3");
        request = new MockPortletRequest(null, pcontext, session);
        request.setAttribute("rkey1", "rvalue1");
        request.setAttribute("rkey2", "rvalue2");
        ((MockPortletRequest) request).addParameter("pkey1", "pvalue1");
        ((MockPortletRequest) request).addParameter("pkey2", "pvalue2a");
        ((MockPortletRequest) request).addParameter("pkey2", "pvalue2b");
        context = createContext();
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
    }

    // ------------------------------------------------ Individual Test Methods

    /**
     * Test {@code getApplicationScope()}
     */
    @Test
    public void testApplicationScope() {
        Map map = ((WebContext) context).getApplicationScope();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 4);
        assertEquals("avalue1", (String) map.get("akey1"));
        assertEquals("avalue2", (String) map.get("akey2"));
        assertEquals("avalue3", (String) map.get("akey3"));
        assertEquals("avalue4", (String) map.get("akey4"));

        // Transparency - entrySet()
        checkEntrySet(map, true);
 
        // Transparency - removal via web object
        pcontext.removeAttribute("akey1");
        checkMapSize(map, 3);
        assertNull(map.get("akey1"));

        // Transparency - removal via map
        map.remove("akey2");
        checkMapSize(map, 2);
        assertNull(pcontext.getAttribute("akey2"));

        // Transparency - addition via web object
        pcontext.setAttribute("akeyA", "avalueA");
        checkMapSize(map, 3);
        assertEquals("avalueA", (String) map.get("akeyA"));

        // Transparency - addition via map
        map.put("akeyB", "avalueB");
        checkMapSize(map, 4);
        assertEquals("avalueB", (String) pcontext.getAttribute("akeyB"));

        // Transparency - replacement via web object
        pcontext.setAttribute("akeyA", "newvalueA");
        checkMapSize(map, 4);
        assertEquals("newvalueA", (String) map.get("akeyA"));

        // Transparency - replacement via map
        map.put("akeyB", "newvalueB");
        assertEquals(4, map.size());
        assertEquals("newvalueB", (String) pcontext.getAttribute("akeyB"));

        // Clearing the map
        map.clear();
        checkMapSize(map, 0);

        // Test putAll()
        Map values = new HashMap();
        values.put(new Integer(1), "One");
        values.put("2", "Two");
        map.putAll(values);
        assertEquals("One", map.get("1"), "putAll(1)");
        assertEquals("Two", map.get("2"), "putAll(2)");
        checkMapSize(map, 2);
    }

    /**
     * Test {@code equals()} and {@code hashCode()}
     * Copied from ContextBaseTestCase with customized creation of "other"
     */
    @Test
    public void testEquals() {
        // Compare to self
        assertTrue(context.equals(context));
        assertEquals(context.hashCode(), context.hashCode());

        // Compare to equivalent instance
        Context other = new PortletWebContext(pcontext, request, response);
        // assertTrue(context.equals(other));
        assertEquals(context.hashCode(), other.hashCode());

        // Compare to non-equivalent instance - other modified
        other.put("bop", "bop value");
        // assertFalse(context.equals(other));
        assertNotEquals(context.hashCode(), other.hashCode());

        // Compare to non-equivalent instance - self modified
        other = new PortletWebContext(pcontext, request, response);
        context.put("bop", "bop value");
        // assertFalse(context.equals(other));
        assertNotEquals(context.hashCode(), other.hashCode());
    }

    /**
     * Test {@code getHeader()}
     */
    @Test
    public void testHeader() {
        Map map = ((WebContext) context).getHeader();
        assertNotNull(map, "Header Map Null");

        // Initial contents
        checkMapSize(map, 0);

        try {
            map.put("hkey3", "hvalue3");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
    }

    /**
     * Test {@code getHeaderValues()}
     */
    @Test
    public void testHeaderValues() {
        Map map = ((WebContext) context).getHeaderValues();
        assertNotNull(map, "HeaderValues Map Null");

        // Initial contents
        checkMapSize(map, 0);
 
        try {
            map.put("hkey3", "ABC");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
    }

    /**
     * Test {@code getInitParam()}
     */
    @Test
    public void testInitParam() {
        Map map = ((WebContext) context).getInitParam();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 3);
        assertEquals("ivalue1", (String) map.get("ikey1"));
        assertEquals("ivalue2", (String) map.get("ikey2"));
        assertEquals("ivalue3", (String) map.get("ikey3"));
        assertTrue(map.containsKey("ikey1"));
        assertTrue(map.containsKey("ikey2"));
        assertTrue(map.containsKey("ikey3"));
        assertTrue(map.containsValue("ivalue1"));
        assertTrue(map.containsValue("ivalue2"));
        assertTrue(map.containsValue("ivalue3"));

        // Transparency - entrySet()
        checkEntrySet(map, false);
 
        // Unsupported operations on read-only map
        try {
            map.clear();
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.put("ikey4", "ivalue4");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.putAll(new HashMap());
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.remove("ikey1");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
    }

    /**
     * Test {@code getParam()}
     */
    @Test
    public void testParam() {
        Map map = ((WebContext) context).getParam();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 2);
        assertEquals("pvalue1", (String) map.get("pkey1"));
        assertEquals("pvalue2a", (String) map.get("pkey2"));
        assertTrue(map.containsKey("pkey1"));
        assertTrue(map.containsKey("pkey2"));
        assertTrue(map.containsValue("pvalue1"));
        assertTrue(map.containsValue("pvalue2a"));

        checkEntrySet(map, false);

        // Unsupported operations on read-only map
        try {
            map.clear();
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.put("pkey3", "pvalue3");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.putAll(new HashMap());
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.remove("pkey1");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
    }

    /**
     * Test {@code getParamValues()}
     */
    @Test
    public void testParamValues() {
        Map map = ((WebContext) context).getParamValues();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 2);
        Object value1 = map.get("pkey1");
        assertNotNull(value1);
        assertInstanceOf(String[].class, value1);
        String values1[] = (String[]) value1;
        assertEquals(1, values1.length);
        assertEquals("pvalue1", values1[0]);
        Object value2 = map.get("pkey2");
        assertNotNull(value2);
        assertInstanceOf(String[].class, value2);
        String values2[] = (String[]) value2;
        assertEquals(2, values2.length);
        assertEquals("pvalue2a", values2[0]);
        assertEquals("pvalue2b", values2[1]);
        assertTrue(map.containsKey("pkey1"));
        assertTrue(map.containsKey("pkey2"));
        assertTrue(map.containsValue(values1));
        assertTrue(map.containsValue(values2));

        // Unsupported operations on read-only map
        try {
            map.clear();
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.put("pkey3", values2);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.putAll(new HashMap());
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.remove("pkey1");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
    }

    /**
     * Test {@code getCookies()}
     */
    @Test
    public void testCookies() {
        Map map = ((WebContext) context).getCookies();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 0);

        try {
            map.put("ckey3", "XXX");
            fail("map.put() Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
    }

    /**
     * Test state of newly created instance
     */
    @Test
    public void testPristine() {
        super.testPristine();
        PortletWebContext pwcontext = (PortletWebContext) context;

        // Properties should all be non-null
        assertNotNull(pwcontext.getApplicationScope());
        assertNotNull(pwcontext.getHeader());
        assertNotNull(pwcontext.getHeaderValues());
        assertNotNull(pwcontext.getInitParam());
        assertNotNull(pwcontext.getParam());
        assertNotNull(pwcontext.getParamValues());
        assertNotNull(pwcontext.getCookies());
        assertNotNull(pwcontext.getRequestScope());
        assertNotNull(pwcontext.getSessionScope());

        // Attribute-property transparency
        assertTrue(pwcontext.getApplicationScope() ==
                     pwcontext.get("applicationScope"));
        assertTrue(pwcontext.getHeader() ==
                     pwcontext.get("header"));
        assertTrue(pwcontext.getHeaderValues() ==
                     pwcontext.get("headerValues"));
        assertTrue(pwcontext.getInitParam() ==
                     pwcontext.get("initParam"));
        assertTrue(pwcontext.getParam() ==
                     pwcontext.get("param"));
        assertTrue(pwcontext.getParamValues() ==
                     pwcontext.get("paramValues"));
        assertTrue(pwcontext.getCookies() ==
                     pwcontext.get("cookies"));
        assertTrue(pwcontext.getRequestScope() ==
                     pwcontext.get("requestScope"));
        assertTrue(pwcontext.getSessionScope() ==
                     pwcontext.get("sessionScope"));
    }

    /**
     * Test {@code release()}
     */
    @Test
    public void testRelease() {
        PortletWebContext pwcontext = (PortletWebContext) context;
        pwcontext.release();

        // Properties should all be null
        assertNull(pwcontext.getApplicationScope(), "getApplicationScope()");
        assertNull(pwcontext.getHeader(), "getHeader()");
        assertNull(pwcontext.getHeaderValues(), "getHeaderValues()");
        assertNull(pwcontext.getInitParam(), "getInitParam()");
        assertNull(pwcontext.getParam(), "getParam()");
        assertNull(pwcontext.getParamValues(), "getParamValues()");
        assertNull(pwcontext.getRequestScope(), "getRequestScope()");
        assertNull(pwcontext.getSessionScope(), "getSessionScope()");

        // Attributes should all be null
        assertNull(pwcontext.get("applicationScope"), "applicationScope");
        assertNull(pwcontext.get("header"), "header");
        assertNull(pwcontext.get("headerValues"), "headerValues");
        assertNull(pwcontext.get("initParam"), "initParam");
        assertNull(pwcontext.get("param"), "param");
        assertNull(pwcontext.get("paramValues"), "paramValues");
        assertNull(pwcontext.get("requestScope"), "requestScope");
        assertNull(pwcontext.get("sessionScope"), "sessionScope");
    }

    /**
     * Test {@code getRequestScope()}
     */
    @Test
    public void testRequestScope() {
        Map map = ((WebContext) context).getRequestScope();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 2);
        assertEquals("rvalue1", (String) map.get("rkey1"));
        assertEquals("rvalue2", (String) map.get("rkey2"));

        // Transparency - entrySet()
        checkEntrySet(map, true);
 
        // Transparency - removal via web object
        request.removeAttribute("rkey1");
        checkMapSize(map, 1);
        assertNull(map.get("rkey1"));

        // Transparency - removal via map
        map.remove("rkey2");
        checkMapSize(map, 0);
        assertNull(request.getAttribute("rkey2"));

        // Transparency - addition via web object
        request.setAttribute("rkeyA", "rvalueA");
        checkMapSize(map, 1);
        assertEquals("rvalueA", (String) map.get("rkeyA"));

        // Transparency - addition via map
        map.put("rkeyB", "rvalueB");
        checkMapSize(map, 2);
        assertEquals("rvalueB", (String) request.getAttribute("rkeyB"));

        // Transparency - replacement via web object
        request.setAttribute("rkeyA", "newvalueA");
        checkMapSize(map, 2);
        assertEquals("newvalueA", (String) map.get("rkeyA"));

        // Transparency - replacement via map
        map.put("rkeyB", "newvalueB");
        checkMapSize(map, 2);
        assertEquals("newvalueB", (String) request.getAttribute("rkeyB"));

        // Clearing the map
        map.clear();
        checkMapSize(map, 0);

        // Test putAll()
        Map values = new HashMap();
        values.put(new Integer(1), "One");
        values.put("2", "Two");
        map.putAll(values);
        assertEquals("One", map.get("1"), "putAll(1)");
        assertEquals("Two", map.get("2"), "putAll(2)");
        checkMapSize(map, 2);
    }

    /**
     * Test {@code getSessionScope()}
     */
    @Test
    public void testSessionScope() {
        Map map = ((WebContext) context).getSessionScope();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 3);
        assertEquals("svalue1", (String) map.get("skey1"));
        assertEquals("svalue2", (String) map.get("skey2"));
        assertEquals("svalue3", (String) map.get("skey3"));

        // Transparency - entrySet()
        checkEntrySet(map, true);
 
        // Transparency - removal via web object
        session.removeAttribute("skey1");
        checkMapSize(map, 2);
        assertNull(map.get("skey1"));

        // Transparency - removal via map
        map.remove("skey2");
        checkMapSize(map, 1);
        assertNull(session.getAttribute("skey2"));

        // Transparency - addition via web object
        session.setAttribute("skeyA", "svalueA");
        checkMapSize(map, 2);
        assertEquals("svalueA", (String) map.get("skeyA"));

        // Transparency - addition via map
        map.put("skeyB", "svalueB");
        checkMapSize(map, 3);
        assertEquals("svalueB", (String) session.getAttribute("skeyB"));

        // Transparency - replacement via web object
        session.setAttribute("skeyA", "newvalueA");
        checkMapSize(map, 3);
        assertEquals("newvalueA", (String) map.get("skeyA"));

        // Transparency - replacement via map
        map.put("skeyB", "newvalueB");
        checkMapSize(map, 3);
        assertEquals("newvalueB", (String) session.getAttribute("skeyB"));

        // Clearing the map
        map.clear();
        checkMapSize(map, 0);

        // Test putAll()
        Map values = new HashMap();
        values.put(new Integer(1), "One");
        values.put("2", "Two");
        map.putAll(values);
        assertEquals("One", map.get("1"), "putAll(1)");
        assertEquals("Two", map.get("2"), "putAll(2)");
        checkMapSize(map, 2);
    }

    /**
     * Test {@code getSessionScope()} without Session
     */
    @Test
    public void testSessionScopeWithoutSession() {
        // Create a Context without a session
        PortletWebContext ctx = new PortletWebContext(pcontext, 
           new MockPortletRequest(), response);
        assertNull(ctx.getRequest().getPortletSession(false), "Session(A)");

        // Get the session Map & check session doesn't exist
        Map sessionMap = ctx.getSessionScope();
        assertNull(ctx.getRequest().getPortletSession(false), "Session(B)");
        assertNotNull(sessionMap, "Session Map(A)");

        // test clear()
        sessionMap.clear();
        assertNull(ctx.getRequest().getPortletSession(false), "Session(C)");

        // test containsKey()
        assertFalse(sessionMap.containsKey("ABC"), "containsKey()");
        assertNull(ctx.getRequest().getPortletSession(false), "Session(D)");

        // test containsValue()
        assertFalse(sessionMap.containsValue("ABC"), "containsValue()");
        assertNull(ctx.getRequest().getPortletSession(false), "Session(E)");

        // test entrySet()
        Set entrySet = sessionMap.entrySet();
        assertNotNull(entrySet, "entrySet");
        assertEquals(0, entrySet.size(), "entrySet Size");
        assertNull(ctx.getRequest().getPortletSession(false), "Session(F)");

        // test equals()
        assertFalse(sessionMap.equals("ABC"), "equals()");
        assertNull(ctx.getRequest().getPortletSession(false), "Session(G)");

        // test get()
        assertNull(sessionMap.get("ABC"), "get()");
        assertNull(ctx.getRequest().getPortletSession(false), "Session(H)");

        // test hashCode()
        sessionMap.hashCode();
        assertNull(ctx.getRequest().getPortletSession(false), "Session(I)");

        // test isEmpty()
        assertTrue(sessionMap.isEmpty(), "isEmpty()");
        assertNull(ctx.getRequest().getPortletSession(false), "Session(J)");

        // test keySet()
        Set keySet = sessionMap.keySet();
        assertNotNull(keySet, "keySet");
        assertEquals(0, keySet.size(), "keySet Size");
        assertNull(ctx.getRequest().getPortletSession(false), "Session(K)");

        // test putAll() with an empty Map
        sessionMap.putAll(new HashMap());
        assertNull(ctx.getRequest().getPortletSession(false), "Session(L)");

        // test remove()
        assertNull(sessionMap.remove("ABC"), "remove()");
        assertNull(ctx.getRequest().getPortletSession(false), "Session(M)");

        // test size()
        assertEquals(0, sessionMap.size(), "size() Size");
        assertNull(ctx.getRequest().getPortletSession(false), "Session(N)");

        // test values()
        Collection values = sessionMap.values();
        assertNotNull(values, "values");
        assertEquals(0, values.size(), "values Size");
        assertNull(ctx.getRequest().getPortletSession(false), "Session(O)");

        // test put()
        try {
            assertNull(sessionMap.put("ABC", "XYZ"), "put()");
            assertNotNull(ctx.getRequest().getPortletSession(false), "Session(P)");
        } catch(UnsupportedOperationException ex) {
            // expected: currently MockPortletRequest throws this
            //           when trying to create a PortletSession
        }
    }

    // ------------------------------------------------------- Protected Methods

    protected void checkMapSize(Map map, int size) {
        // Check reported size of the map
        assertEquals(size, map.size(), "checkMapSize(A)");
        // Iterate over key set
        int nk = 0;
        Iterator keys = map.keySet().iterator();
        while (keys.hasNext()) {
            keys.next();
            nk++;
        }
        assertEquals(size, nk, "checkMapSize(B)");
        // Iterate over entry set
        int nv = 0;
        Iterator values = map.entrySet().iterator();
        while (values.hasNext()) {
            values.next();
            nv++;
        }
        assertEquals(size, nv, "checkMapSize(C)");
        // Count the values
        assertEquals(size, map.values().size(), "checkMapSize(D)");
    }

    /**
     * Test to ensure proper {@code entrySet()} and are modifiable optionally
     *
     * @param map to test
     * @param modifiable {@code true} map is modifiable
     */
    protected void checkEntrySet(Map map, boolean modifiable) {
        assertTrue(map.size() > 1, "checkEntrySet(A)");
        Set entries = map.entrySet();
        assertTrue(map.size() == entries.size());
        Object o = entries.iterator().next();

        assertInstanceOf(Map.Entry.class, o, "checkEntrySet(B)");

        if (!modifiable) {
            try {
                ((Map.Entry)o).setValue(new Object());
                fail("Should have thrown UnsupportedOperationException");
            } catch (UnsupportedOperationException e) {
                ; // expected result
            }
        } else {
            // Should pass and not throw UnsupportedOperationException
            Map.Entry e = (Map.Entry)o;
            e.setValue(e.setValue(new Object()));
        }    
    }    

    /**
     * Create a new instance of the appropriate Context type for this test case
     *
     * @return new instance of the appropriate Context type 
     */
    protected Context createContext() {
        return new PortletWebContext(pcontext, request, response);
    }
}