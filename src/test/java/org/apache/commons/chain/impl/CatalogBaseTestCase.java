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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Iterator;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case for the {@code CatalogBase} class.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class CatalogBaseTestCase {

    // ---------------------------------------------------- Instance Variables

    /**
     * The {@link Catalog} instance under test.
     */
    protected CatalogBase catalog = null;

    // -------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    @BeforeEach
    public void init() {
        catalog = new CatalogBase();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @AfterEach
    public void tearDown() {
        catalog = null;
    }

    // ------------------------------------------------ Individual Test Methods

    /**
     * Test adding commands
     */
    @Test
    public void testAddCommand() {
        addCommands();
        checkCommandCount(8);
    }

    /**
     * Test getting commands
     */
    @Test
    public void testGetCommand() {
        addCommands();
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
    }

    // The getNames() method is implicitly tested by checkCommandCount()

    /**
     * Test pristine instance
     */
    @Test
    public void testPristine() {
        checkCommandCount(0);
        assertNull(catalog.getCommand("AddingCommand"));
        assertNull(catalog.getCommand("DelegatingCommand"));
        assertNull(catalog.getCommand("DelegatingFilter"));
        assertNull(catalog.getCommand("ExceptionCommand"));
        assertNull(catalog.getCommand("ExceptionFilter"));
        assertNull(catalog.getCommand("NonDelegatingCommand"));
        assertNull(catalog.getCommand("NonDelegatingFilter"));
        assertNull(catalog.getCommand("ChainBase"));
    }

    // -------------------------------------------------------- Support Methods

    /**
     * Add an interesting set of commands to the catalog
     */
    protected void addCommands() {
        catalog.addCommand("AddingCommand", new AddingCommand("", null));
        catalog.addCommand("DelegatingCommand", new DelegatingCommand(""));
        catalog.addCommand("DelegatingFilter", new DelegatingFilter("", ""));
        catalog.addCommand("ExceptionCommand", new ExceptionCommand(""));
        catalog.addCommand("ExceptionFilter", new ExceptionFilter("", ""));
        catalog.addCommand("NonDelegatingCommand", new NonDelegatingCommand(""));
        catalog.addCommand("NonDelegatingFilter", new NonDelegatingFilter("", ""));
        catalog.addCommand("ChainBase", new ChainBase());
    }

    /**
     * Verify the number of configured commands
     *
     * @param expected the expected value
     */
    protected void checkCommandCount(int expected) {
        int n = 0;
        Iterator names = catalog.getNames();
        while (names.hasNext()) {
            String name = (String) names.next();
            n++;
            assertNotNull(catalog.getCommand(name), name + " exists");
        }
        assertEquals(expected, n, "Correct command count");
    }
}