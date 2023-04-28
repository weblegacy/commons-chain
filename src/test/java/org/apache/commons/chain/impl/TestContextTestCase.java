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
package org.apache.commons.chain.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@code ContextBaseTestCase} to validate property
 * delegation.
 */
public class TestContextTestCase extends ContextBaseTestCase<TestContext> {

    // ---------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    @BeforeEach
    public void init() {
        context = createContext();
    }

    // ------------------------------------------------- Individual Test Methods

    /**
     * Test state of newly created instance
     */
    @Test
    public void testPristine() {
        super.testPristine();
        assertEquals("readOnly", (String) context.get("readOnly"));
        assertEquals("readWrite", (String) context.get("readWrite"));
        assertEquals("writeOnly", context.returnWriteOnly());
    }

    /**
     * Test a read only property on the Context implementation class
     */
    @Test
    public void testReadOnly() {
        Object readOnly = context.get("readOnly");
        assertNotNull(readOnly, "readOnly found");
        assertInstanceOf(String.class, readOnly,
                         "readOnly String");
        assertEquals("readOnly", readOnly, "readOnly value");

        try {
            context.put("readOnly", "new readOnly");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // Expected result
        }
        assertEquals("readOnly", (String) context.get("readOnly"),
                     "readOnly unchanged");
    }

    /**
     * Test a read write property on the Context implementation class
     */
    @Test
    public void testReadWrite() {
        Object readWrite = context.get("readWrite");
        assertNotNull(readWrite, "readWrite found");
        assertInstanceOf(String.class, readWrite,
                         "readWrite String");
        assertEquals("readWrite", readWrite, "readWrite value");

        context.put("readWrite", "new readWrite");
        readWrite = context.get("readWrite");
        assertNotNull(readWrite, "readWrite found");
        assertInstanceOf(String.class, readWrite,
                         "readWrite String");
        assertEquals("new readWrite", readWrite, "readWrite value");
    }

    /**
     * Test a write only property on the Context implementation class
     */
    @Test
    public void testWriteOnly() {
        Object writeOnly = context.returnWriteOnly();
        assertNotNull(writeOnly, "writeOnly found");
        assertInstanceOf(String.class, writeOnly,
                         "writeOnly String");
        assertEquals("writeOnly", writeOnly, "writeOnly value");

        context.put("writeOnly", "new writeOnly");
        writeOnly = context.returnWriteOnly();
        assertNotNull(writeOnly, "writeOnly found");
        assertInstanceOf(String.class, writeOnly,
                         "writeOnly String");
        assertEquals("new writeOnly", writeOnly, "writeOnly value");
    }

    // ------------------------------------------------------- Protected Methods

    /**
     * Create a new instance of the appropriate Context type for this test case
     */
    protected TestContext createContext() {
        return new TestContext();
    }
}