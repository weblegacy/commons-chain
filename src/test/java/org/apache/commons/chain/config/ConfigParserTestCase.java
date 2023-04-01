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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.AddingCommand;
import org.apache.commons.chain.impl.CatalogBase;
import org.apache.commons.chain.impl.CatalogFactoryBase;
import org.apache.commons.chain.impl.ChainBase;
import org.apache.commons.chain.impl.ContextBase;
import org.apache.commons.chain.impl.DelegatingCommand;
import org.apache.commons.chain.impl.DelegatingFilter;
import org.apache.commons.chain.impl.ExceptionCommand;
import org.apache.commons.chain.impl.ExceptionFilter;
import org.apache.commons.chain.impl.NonDelegatingCommand;
import org.apache.commons.chain.impl.NonDelegatingFilter;
import org.apache.commons.digester.Digester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test Case for {@code org.apache.commons.chain.config.ConfigParser}.
 */
public class ConfigParserTestCase {
    private static final String DEFAULT_XML =
        "/org/apache/commons/chain/config/test-config.xml";

    // ------------------------------------------------------ Instance Variables

    /**
     * The {@code Catalog} to contain our configured commands.
     */
    protected Catalog catalog = null;

    /**
     * The {@code Context} to use for execution tests.
     */
    protected Context context = null;

    /**
     * The {@code ConfigParser} instance under test.
     */
    protected ConfigParser parser = null;

    // ---------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    @BeforeEach
    public void init() {
        catalog = new CatalogBase();
        context = new ContextBase();
        parser = new ConfigParser();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @AfterEach
    public void tearDown() {
        parser = null;
        context = null;
        catalog = null;
    }

    // ------------------------------------------------ Individual Test Methods

    /**
     *  Load the default test-config.xml file and examine the results
     *
     * @throws Exception any error
     */
    @Test
    public void testDefaut() throws Exception {
        // Check overall command count
        load(DEFAULT_XML);
        checkCommandCount(17);

        // Check individual single command instances
        Command command = null;

        command = catalog.getCommand("AddingCommand");
        assertNotNull(command);
        assertInstanceOf(AddingCommand.class, command);

        command = catalog.getCommand("DelegatingCommand");
        assertNotNull(command);
        assertInstanceOf(DelegatingCommand.class, command);

        command = catalog.getCommand("DelegatingFilter");
        assertNotNull(command);
        assertInstanceOf(DelegatingFilter.class, command);

        command = catalog.getCommand("ExceptionCommand");
        assertNotNull(command);
        assertInstanceOf(ExceptionCommand.class, command);

        command = catalog.getCommand("ExceptionFilter");
        assertNotNull(command);
        assertInstanceOf(ExceptionFilter.class, command);

        command = catalog.getCommand("NonDelegatingCommand");
        assertNotNull(command);
        assertInstanceOf(NonDelegatingCommand.class, command);

        command = catalog.getCommand("NonDelegatingFilter");
        assertNotNull(command);
        assertInstanceOf(NonDelegatingFilter.class, command);

        command = catalog.getCommand("ChainBase");
        assertNotNull(command);
        assertInstanceOf(ChainBase.class, command);
        assertInstanceOf(TestChain.class, command);

        // Check configurable properties instance
        TestCommand tcommand = (TestCommand) catalog.getCommand("Configurable");
        assertNotNull(tcommand);
        assertEquals(tcommand.getFoo(), "Foo Value");
        assertEquals(tcommand.getBar(), "Bar Value");
    }

    /**
     * Test execution of chain "Execute2a"
     *
     * @throws Exception any error
     */
    @Test
    public void testExecute2a() throws Exception {
        load(DEFAULT_XML);
        assertTrue(catalog.getCommand("Execute2a").execute(context),
                   "Chain returned true");
        checkExecuteLog("1/2/3");
    }

    /**
     * Test execution of chain "Execute2b"
     *
     * @throws Exception any error
     */
    @Test
    public void testExecute2b() throws Exception {
        load(DEFAULT_XML);
        assertFalse(catalog.getCommand("Execute2b").execute(context),
                    "Chain returned false");
        checkExecuteLog("1/2/3");
    }

    /**
     * Test execution of chain "Execute2c"
     *
     * @throws Exception any error
     */
    @Test
    public void testExecute2c() throws Exception {
        load(DEFAULT_XML);
        try {
            catalog.getCommand("Execute2c").execute(context);
        } catch (ArithmeticException e) {
            assertEquals("3", e.getMessage(),
                         "Correct exception id");
        }
        checkExecuteLog("1/2/3");
    }

    /**
     * Test execution of chain "Execute2d"
     *
     * @throws Exception any error
     */
    @Test
    public void testExecute2d() throws Exception {
        load(DEFAULT_XML);
        try {
            catalog.getCommand("Execute2d").execute(context);
        } catch (ArithmeticException e) {
            assertEquals("2", e.getMessage(),
                         "Correct exception id");
        }
        checkExecuteLog("1/2");
    }

    /**
     * Test execution of chain "Execute4a"
     *
     * @throws Exception any error
     */
    @Test
    public void testExecute4a() throws Exception {
        load(DEFAULT_XML);
        assertTrue(catalog.getCommand("Execute4a").execute(context),
                   "Chain returned true");
        checkExecuteLog("1/2/3/c/a");
    }

    /**
     * Test execution of chain "Execute2b"
     *
     * @throws Exception any error
     */
    @Test
    public void testExecute4b() throws Exception {
        load(DEFAULT_XML);
        assertFalse(catalog.getCommand("Execute4b").execute(context),
                    "Chain returned false");
        checkExecuteLog("1/2/3/b");
    }

    /**
     * Test execution of chain "Execute4c"
     *
     * @throws Exception any error
     */
    @Test
    public void testExecute4c() throws Exception {
        load(DEFAULT_XML);
        try {
            catalog.getCommand("Execute4c").execute(context);
        } catch (ArithmeticException e) {
            assertEquals("3", e.getMessage(),
                         "Correct exception id");
        }
        checkExecuteLog("1/2/3/c/b/a");
    }

    /**
     * Test execution of chain "Execute4d"
     *
     * @throws Exception any error
     */
    @Test
    public void testExecute4d() throws Exception {
        load(DEFAULT_XML);
        try {
            catalog.getCommand("Execute4d").execute(context);
        } catch (ArithmeticException e) {
            assertEquals("2", e.getMessage(),
                         "Correct exception id");
        }
        checkExecuteLog("1/2/b/a");
    }

    /**
     * Test a pristine ConfigParser instance
     */
    @Test
    public void testPristine() {
        // Validate the "digester" property
        Digester digester = parser.getDigester();
        assertNotNull(digester, "Returned a Digester instance");
        assertFalse(digester.getNamespaceAware(),
                    "Default namespaceAware");
        assertTrue(digester.getUseContextClassLoader(),
                   "Default useContextClassLoader");
        assertFalse(digester.getValidating(),
                    "Default validating");

        // Validate the "ruleSet" property
        ConfigRuleSet ruleSet = (ConfigRuleSet) parser.getRuleSet();
        assertNotNull(ruleSet, "Returned a RuleSet instance");
        assertEquals("chain", ruleSet.getChainElement(),
                     "Default chainElement");
        assertEquals("className", ruleSet.getClassAttribute(),
                     "Default classAttribute");
        assertEquals("command", ruleSet.getCommandElement(),
                     "Default commandElement");
        assertEquals("name", ruleSet.getNameAttribute(),
                     "Default nameAttribute");
        assertNull(ruleSet.getNamespaceURI(),
                   "Default namespaceURI");

        // Validate the "useContextClassLoader" property
        assertTrue(parser.getUseContextClassLoader(),
                   "Defaults to use context class loader");

        // Ensure that there are no preconfigured commands in the catalog
        checkCommandCount(0);
    }

    // --------------------------------------------------------- Private Methods

    /**
     * Verify the number of configured commands
     *
     * @param expected the expected value
     */
    protected void checkCommandCount(int expected) {
        int n = 0;
        Iterator<String> names = catalog.getNames();
        while (names.hasNext()) {
            String name = (String) names.next();
            n++;
            assertNotNull(catalog.getCommand(name), name + " exists");
        }
        assertEquals(expected, n, "Correct command count");
    }

    /**
     * Verify the contents of the execution log
     *
     * @param expected the expected value
     */
    protected void checkExecuteLog(String expected) {
        StringBuffer log = (StringBuffer) context.get("log");
        assertNotNull(log, "Context returned log");
        assertEquals(expected, log.toString(),
                     "Context returned correct log");
    }

    /**
     * Load the specified catalog from the specified resource path
     *
     * @param path resource path to load specified catalog
     *
     * @throws Exception any error
     */
    protected void load(String path) throws Exception {
        CatalogFactoryBase.clear();
        parser.parse(this.getClass().getResource(path));
        catalog = CatalogFactoryBase.getInstance().getCatalog();
    }
}