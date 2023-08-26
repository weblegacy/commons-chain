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
package org.apache.commons.chain.tests.generic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.generic.DispatchLookupCommand;
import org.apache.commons.chain.impl.CatalogBase;
import org.apache.commons.chain.impl.ContextBase;
import org.apache.commons.chain.tests.impl.NonDelegatingCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case for the {@code DispatchLookupCommand} class.
 *
 * @author Sean Schofield
 * @version $Revision$
 */
public class DispatchLookupCommandTestCase {

    // ---------------------------------------------------- Instance Variables

    /**
     * The instance of {@link Catalog} to use when looking up commands
     */
    protected Catalog<Context> catalog;

    /**
     * The {@link DispatchLookupCommand} instance under test.
     */
    protected DispatchLookupCommand<Context> command;

    /**
     * The {@link Context} instance on which to execute the chain.
     */
    protected Context context = null;

    // ---------------------------------------------------------- Constructors

    /**
     * The Default-Constructor for this class.
     */
    public DispatchLookupCommandTestCase() {
    }

    // -------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    @BeforeEach
    public void init() {
        catalog = new CatalogBase<>();
        CatalogFactory.getInstance().setCatalog(catalog);
        command = new DispatchLookupCommand<>();
        context = new ContextBase();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @AfterEach
    public void tearDown() {
        catalog = null;
        CatalogFactory.clear();
        command = null;
        context = null;
    }

    // ------------------------------------------------ Individual Test Methods

    /**
     * Test ability to lookup and execute a dispatch method on a single
     * non-delegating command
     */
    @Test
    public void testExecuteDispatchLookup_1a() {
        // use default catalog
        catalog.addCommand("fooCommand", new TestCommand("1"));

        // command should lookup the fooCommand and execute the fooMethod
        command.setName("fooCommand");
        command.setMethod("fooMethod");

        try {
            assertTrue(command.execute(context),
                       "Command should return true");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }

        // command should lookup the fooCommand and execute the barMethod
        command.setMethod("barMethod");

        try {
            assertTrue(command.execute(context),
                       "Command should return true");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }

        checkExecuteLog("1/1");
    }

    /**
     * Test IllegalArgumentException when incorrect command name specified
     */
    @Test
    public void testExecuteDispatchLookup_2() {
        // use default catalog
        catalog.addCommand("barCommand", new TestCommand("2"));

        // command should lookup the fooCommand and execute the fooMethod
        command.setName("fooCommand");
        command.setMethod("fooMethod");

        assertThrows(IllegalArgumentException.class,
                     () ->command.execute(context),
                     "Expected IllegalArgumentException");
    }

    /**
     * Test ability to lookup and execute a dispatch method on a single
     * non-delegating command (using context to specify method name)
     */
    @Test
    public void testExecuteDispatchLookup_3() {
        // use default catalog
        catalog.addCommand("fooCommand", new TestCommand("3"));

        // command should lookup the fooCommand and execute the fooMethod
        command.setName("fooCommand");
        command.setMethodKey("methodKey");
        context.put("methodKey", "fooMethod");

        try {
            assertTrue(command.execute(context),
                       "Command should return true");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }

        // command should lookup the fooCommand and execute the barMethod
        command.setMethodKey("methodKey");
        context.put("methodKey", "barMethod");

        try {
            assertTrue(command.execute(context),
                       "Command should return true");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }

        checkExecuteLog("3/3");
    }

    // -------------------------------------------------------- Support Methods

    /**
     * Verify the contents of the execution log
     *
     * @param expected the expected value
     */
    protected void checkExecuteLog(String expected) {
        StringBuffer log = (StringBuffer) context.get("log");
        assertNotNull(log, "Context failed to return log");
        assertEquals(expected, log.toString(),
                     "Context returned correct log");
    }

    // ---------------------------------------------------------- Inner Classes

    public class TestCommand extends NonDelegatingCommand {
        public TestCommand(String id) {
            super(id);
        }

        public boolean fooMethod(Context context) {
            log(context, id);
            return true;
        }

        public boolean barMethod(Context context) {
            log(context, id);
            return true;
        }
    }
}