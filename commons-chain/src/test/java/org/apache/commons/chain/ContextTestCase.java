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
package org.apache.commons.chain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case for the {@code ContextBase} class.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public abstract class ContextTestCase<C extends Context> {

    // ---------------------------------------------------- Instance Variables

    /**
     * The {@link Context} instance under test.
     */
    protected C context = null;

    // ---------------------------------------------------------- Constructors

    /**
     * The Default-Constructor for this class.
     */
    public ContextTestCase() {
    }

    // -------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    @BeforeEach
    public void init() {
        context = createContext();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @AfterEach
    public void tearDown() {
        context = null;
    }

    // ------------------------------------------------ Individual Test Methods

    /**
     * Test ability to get, put, and remove attributes
     */
    @Test
    public void testAttributes() {
        Object value = null;
        checkAttributeCount(0);

        context.put("foo", "This is foo");
        checkAttributeCount(1);
        value = context.get("foo");
        assertNotNull(value, "Returned foo");
        assertInstanceOf(String.class, value, "Returned foo type");
        assertEquals("This is foo", value,
                     "Returned foo value");

        context.put("bar", "This is bar");
        checkAttributeCount(2);
        value = context.get("bar");
        assertNotNull(value, "Returned bar");
        assertInstanceOf(String.class, value, "Returned bar type");
        assertEquals("This is bar", value,
                     "Returned bar value");

        context.put("baz", "This is baz");
        checkAttributeCount(3);
        value = context.get("baz");
        assertNotNull(value, "Returned baz");
        assertInstanceOf(String.class, value, "Returned baz type");
        assertEquals("This is baz", value,
                     "Returned baz value");

        context.put("baz", "This is new baz");
        checkAttributeCount(3); // Replaced, not added
        value = context.get("baz");
        assertNotNull(value, "Returned baz");
        assertInstanceOf(String.class, value, "Returned baz type");
        assertEquals("This is new baz", value,
                     "Returned baz value");

        context.remove("bar");
        checkAttributeCount(2);
        assertNull(context.get("bar"),
                   "Did not return bar");
        assertNotNull(context.get("foo"),
                      "Still returned foo");
        assertNotNull(context.get("baz"),
                      "Still returned baz");

        context.clear();
        checkAttributeCount(0);
        assertNull(context.get("foo"),
                   "Did not return foo");
        assertNull(context.get("bar"),
                   "Did not return bar");
        assertNull(context.get("baz"),
                   "Did not return baz");
    }

    /**
     * Test {@code containsKey()} and {@code containsValue()}
     */
    @Test
    public void testContains() {
        assertFalse(context.containsKey("bop"));
        assertFalse(context.containsValue("bop value"));
        context.put("bop", "bop value");
        assertTrue(context.containsKey("bop"));
        assertTrue(context.containsValue("bop value"));
        context.remove("bop");
        assertFalse(context.containsKey("bop"));
        assertFalse(context.containsValue("bop value"));
    }

    /**
     * Test {@code equals()} and {@code hashCode()}
     */
    @Test
    public void testEquals() {
        // Compare to self
        assertTrue(context.equals(context));
        assertEquals(context.hashCode(), context.hashCode());

        // Compare to equivalent instance
        Context other = createContext();
        assertTrue(context.equals(other));
        assertEquals(context.hashCode(), other.hashCode());

        // Compare to non-equivalent instance - other modified
        other.put("bop", "bop value");
        assertFalse(context.equals(other));
        assertNotEquals(context.hashCode(), other.hashCode());

        // Compare to non-equivalent instance - self modified
        other = createContext(); // reset to equivalence
        context.put("bop", "bop value");
        assertFalse(context.equals(other));
        assertNotEquals(context.hashCode(), other.hashCode());
    }

    /**
     * Test {@code keySet()}
     */
    @Test
    public void testKeySet() {
        Set<String> keySet = null;
        Collection<String> all = new ArrayList<>();

        // Unsupported operations
        keySet = context.keySet();
        try {
            keySet.add("bop");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // Expected result
        }
        try {
            Collection<String> adds = new ArrayList<>();
            adds.add("bop");
            keySet.addAll(adds);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // Expected result
        }

        // Before-modification checks
        keySet = context.keySet();
        assertEquals(createContext().size(), keySet.size());
        assertFalse(keySet.contains("foo"));
        assertFalse(keySet.contains("bar"));
        assertFalse(keySet.contains("baz"));
        assertFalse(keySet.contains("bop"));

        // Add the new elements
        context.put("foo", "foo value");
        context.put("bar", "bar value");
        context.put("baz", "baz value");
        all.add("foo");
        all.add("bar");
        all.add("baz");

        // After-modification checks
        keySet = context.keySet();
        assertEquals(expectedAttributeCount() + 3, keySet.size());
        assertTrue(keySet.contains("foo"));
        assertTrue(keySet.contains("bar"));
        assertTrue(keySet.contains("baz"));
        assertFalse(keySet.contains("bop"));
        assertTrue(keySet.containsAll(all));

        // Remove a single element via remove()
        context.remove("bar");
        all.remove("bar");
        keySet = context.keySet();
        assertEquals(expectedAttributeCount() + 2, keySet.size());
        assertTrue(keySet.contains("foo"));
        assertFalse(keySet.contains("bar"));
        assertTrue(keySet.contains("baz"));
        assertFalse(keySet.contains("bop"));
        assertTrue(keySet.containsAll(all));

        // Remove a single element via keySet.remove()
        keySet.remove("baz");
        all.remove("baz");
        keySet = context.keySet();
        assertEquals(expectedAttributeCount() + 1, keySet.size());
        assertTrue(keySet.contains("foo"));
        assertFalse(keySet.contains("bar"));
        assertFalse(keySet.contains("baz"));
        assertFalse(keySet.contains("bop"));
        assertTrue(keySet.containsAll(all));

        // Remove all elements via keySet.clear()
        keySet.clear();
        all.clear();
        assertEquals(expectedAttributeCount(), keySet.size());
        assertFalse(keySet.contains("foo"));
        assertFalse(keySet.contains("bar"));
        assertFalse(keySet.contains("baz"));
        assertFalse(keySet.contains("bop"));
        assertTrue(keySet.containsAll(all));

        // Add the new elements #2
        context.put("foo", "foo value");
        context.put("bar", "bar value");
        context.put("baz", "baz value");
        all.add("foo");
        all.add("bar");
        all.add("baz");

        // After-modification checks #2
        keySet = context.keySet();
        assertEquals(expectedAttributeCount() + 3, keySet.size());
        assertTrue(keySet.contains("foo"));
        assertTrue(keySet.contains("bar"));
        assertTrue(keySet.contains("baz"));
        assertFalse(keySet.contains("bop"));
        assertTrue(keySet.containsAll(all));
    }

    /**
     * Test state of newly created instance
     */
    @Test
    public void testPristine() {
        checkAttributeCount(0);
        assertNull(context.get("foo"),
                   "No 'foo' attribute");
    }

    /**
     * Test {@code putAll()}
     */
    @Test
    public void testPutAll() {
        // Check preconditions
        checkAttributeCount(0);
        assertNull(context.get("foo"));
        assertNull(context.get("bar"));
        assertNull(context.get("baz"));
        assertFalse(context.containsKey("foo"));
        assertFalse(context.containsKey("bar"));
        assertFalse(context.containsKey("baz"));
        assertFalse(context.containsValue("foo value"));
        assertFalse(context.containsValue("bar value"));
        assertFalse(context.containsValue("baz value"));

        // Call putAll()
        Map<String, String> adds = new HashMap<>();
        adds.put("foo", "foo value");
        adds.put("bar", "bar value");
        adds.put("baz", "baz value");
        context.putAll(adds);

        // Check postconditions
        checkAttributeCount(3);
        assertEquals("foo value", context.get("foo"));
        assertEquals("bar value", context.get("bar"));
        assertEquals("baz value", context.get("baz"));
        assertTrue(context.containsKey("foo"));
        assertTrue(context.containsKey("bar"));
        assertTrue(context.containsKey("baz"));
        assertTrue(context.containsValue("foo value"));
        assertTrue(context.containsValue("bar value"));
        assertTrue(context.containsValue("baz value"));
    }

    /**
     * Test serialization
     *
     * @throws Exception any error
     */
    @Test
    public void testSerialization() throws Exception {
        // Set up the context with some parameters
        context.put("foo", "foo value");
        context.put("bar", "bar value");
        context.put("baz", "baz value");
        checkAttributeCount(3);

        // Serialize to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(context);
        oos.close();

        // Deserialize back to a new object
        ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        @SuppressWarnings("unchecked")
        C newContext = (C) ois.readObject();
        context = newContext;
        ois.close();

        // Do some rudimentary checks to make sure we have the same contents
        assertTrue(context.containsKey("foo"));
        assertTrue(context.containsKey("bar"));
        assertTrue(context.containsKey("baz"));
        checkAttributeCount(3);
    }

    // -------------------------------------------------------- Support Methods

    /**
     * Verify the number of defined attributes
     *
     * @param expected the expected value
     */
    protected void checkAttributeCount(int expected) {
        int actual = 0;
        Iterator<String> keys = context.keySet().iterator();
        while (keys.hasNext()) {
            keys.next();
            actual++;
        }
        assertEquals(expectedAttributeCount() + expected, actual,
                     "Correct attribute count");
        if (expected == 0) {
            assertTrue(context.isEmpty(), "Context should be empty");
        } else {
            assertFalse(context.isEmpty(), "Context should not be empty");
        }
    }

    /**
     * Return the expected {@code size()} for a Context for this test case
     *
     * @return the expected {@code size()} for a Context
     */
    protected int expectedAttributeCount() {
        return createContext().size();
    }

    /**
     * Create a new instance of the appropriate Context type for this test case
     *
     * @return the new instance of the appropriate Context type
     */
    protected abstract C createContext();
}