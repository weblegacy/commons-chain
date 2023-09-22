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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case for the {@code ChainBase} class.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class ChainBaseTestCase {

    // ---------------------------------------------------- Instance Variables

    /**
     * The {@link Chain} instance under test.
     */
    protected Chain<Context> chain = null;

    /**
     * The {@link Context} instance on which to execute the chain.
     */
    protected Context context = null;

    // ---------------------------------------------------------- Constructors

    /**
     * The Default-Constructor for this class.
     */
    public ChainBaseTestCase() {
    }

    // -------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    @BeforeEach
    public void init() {
        chain = new ChainBaseEx<>();
        context = new ContextBase();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @AfterEach
    public void tearDown() {
        chain = null;
        context = null;
    }

    // ------------------------------------------------ Individual Test Methods

    /**
     * Test the ability to add commands
     */
    @Test
    public void testCommands() {
        checkCommandCount(0);

        Command<Context> command1 = new NonDelegatingCommand("1");
        chain.addCommand(command1);
        checkCommandCount(1);

        Command<Context> command2 = new DelegatingCommand("2");
        chain.addCommand(command2);
        checkCommandCount(2);

        Command<Context> command3 = new ExceptionCommand("3");
        chain.addCommand(command3);
        checkCommandCount(3);
    }

    /**
     * Test execution of a single non-delegating command
     */
    @Test
    public void testExecute1a() {
        chain.addCommand(new NonDelegatingCommand("1"));
        try {
            assertTrue(chain.execute(context),
                       "Chain returned true");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1");
    }

    /**
     * Test execution of a single delegating command
     */
    @Test
    public void testExecute1b() {
        chain.addCommand(new DelegatingCommand("1"));
        try {
            assertFalse(chain.execute(context),
                        "Chain returned false");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1");
    }

    /**
     * Test execution of a single exception-throwing command
     */
    @Test
    public void testExecute1c() {
        chain.addCommand(new ExceptionCommand("1"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("1", e.getMessage(), "Correct exception id");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1");
    }

    /**
     * Test execution of an attempt to add a new Command while executing
     */
    @Test
    public void testExecute1d() {
        chain.addCommand(new AddingCommand("1", chain));
        try {
            chain.execute(context);
        } catch (IllegalStateException e) {
            ; // Expected result
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1");
    }

    /**
     * Test execution of a chain that should return {@code true}
     */
    @Test
    public void testExecute2a() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new DelegatingCommand("2"));
        chain.addCommand(new NonDelegatingCommand("3"));
        try {
            assertTrue(chain.execute(context),
                       "Chain returned true");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/3");
    }

    /**
     * Test execution of a chain that should return {@code false}
     */
    @Test
    public void testExecute2b() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new DelegatingCommand("2"));
        chain.addCommand(new DelegatingCommand("3"));
        try {
            assertFalse(chain.execute(context),
                        "Chain returned false");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/3");
    }

    /**
     * Test execution of a chain that should throw an exception
     */
    @Test
    public void testExecute2c() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new DelegatingCommand("2"));
        chain.addCommand(new ExceptionCommand("3"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("3", e.getMessage(), "Correct exception id");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/3");
    }

    /**
     * Test execution of a chain that should throw an exception in the middle
     */
    @Test
    public void testExecute2d() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new ExceptionCommand("2"));
        chain.addCommand(new NonDelegatingCommand("3"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("2", e.getMessage(), "Correct exception id");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2");
    }

    /**
     * Test execution of a single non-delegating filter
     */
    @Test
    public void testExecute3a() {
        chain.addCommand(new NonDelegatingFilter("1", "a"));
        try {
            assertTrue(chain.execute(context),
                       "Chain returned true");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/a");
    }

    /**
     * Test execution of a single delegating filter
     */
    @Test
    public void testExecute3b() {
        chain.addCommand(new DelegatingFilter("1", "a"));
        try {
            assertFalse(chain.execute(context),
                        "Chain returned false");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/a");
    }

    /**
     * Test execution of a single exception-throwing filter
     */
    @Test
    public void testExecute3c() {
        chain.addCommand(new ExceptionFilter("1", "a"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("1", e.getMessage(), "Correct exception id");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/a");
    }

    /**
     * Test execution of a chain that should return {@code true}
     */
    @Test
    public void testExecute4a() {
        chain.addCommand(new DelegatingFilter("1", "a"));
        chain.addCommand(new DelegatingCommand("2"));
        chain.addCommand(new NonDelegatingFilter("3", "c"));
        try {
            assertTrue(chain.execute(context),
                       "Chain returned true");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/3/c/a");
    }

    /**
     * Test execution of a chain that should return {@code false}
     */
    @Test
    public void testExecute4b() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new DelegatingFilter("2", "b"));
        chain.addCommand(new DelegatingCommand("3"));
        try {
            assertFalse(chain.execute(context),
                        "Chain returned false");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/3/b");
    }

    /**
     * Test execution of a chain that should throw an exception
     */
    @Test
    public void testExecute4c() {
        chain.addCommand(new DelegatingFilter("1", "a"));
        chain.addCommand(new DelegatingFilter("2", "b"));
        chain.addCommand(new ExceptionFilter("3", "c"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("3", e.getMessage(), "Correct exception id");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/3/c/b/a");
    }

    /**
     * Test execution of a chain that should throw an exception in the middle
     */
    @Test
    public void testExecute4d() {
        chain.addCommand(new DelegatingFilter("1", "a"));
        chain.addCommand(new ExceptionFilter("2", "b"));
        chain.addCommand(new NonDelegatingFilter("3", "c"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("2", e.getMessage(), "Correct exception id");
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/b/a");
    }

    /**
     * Test state of newly created instance
     */
    @Test
    public void testNewInstance() {
        checkCommandCount(0);
    }

    // -------------------------------------------------------- Support Methods

    /**
     * Verify the number of configured commands
     *
     * @param expected the expected value
     */
    protected void checkCommandCount(int expected) {
        if (chain instanceof ChainBaseEx) {
            Command<Context>[] commands = ((ChainBaseEx<Context>) chain).getCommands();
            assertNotNull(commands,
                          "getCommands() returned a non-null list");
            assertEquals(expected, commands.length, "Correct command count");
        }
    }

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
}