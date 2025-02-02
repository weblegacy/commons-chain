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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case for the {@code CatalogFactoryBase} class.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class CatalogFactoryBaseTestCase {

    // ---------------------------------------------------- Instance Variables

    /**
     * The {@link CatalogFactory} instance under test.
     */
    protected CatalogFactory<Context> factory = null;

    // ---------------------------------------------------------- Constructors

    /**
     * The Default-Constructor for this class.
     */
    public CatalogFactoryBaseTestCase() {
    }

    // -------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    @BeforeEach
    public void init() {
        CatalogFactory.clear();
        factory = CatalogFactory.getInstance();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @AfterEach
    public void tearDown() {
        factory = null;
    }

    // ------------------------------------------------ Individual Test Methods

    /**
     * Test a pristine instance of {@link CatalogFactory}.
     */
    @Test
    public void testPristine() {
        assertNotNull(factory);
        assertNull(factory.getCatalog());
        assertNull(factory.getCatalog("foo"));
        assertEquals(0, getCatalogCount());
    }

    /**
     * Test the default {@link Catalog} instance.
     */
    @Test
    public void testDefaultCatalog() {
        Catalog<Context> catalog = new CatalogBase<>();
        factory.setCatalog(catalog);
        assertTrue(catalog == factory.getCatalog());
        assertEquals(0, getCatalogCount());
    }

    /**
     * Test adding a specifically named {@link Catalog} instance.
     */
    @Test
    public void testSpecificCatalog() {
        Catalog<Context> catalog = new CatalogBase<>();
        factory.setCatalog(catalog);
        catalog = new CatalogBase<>();
        factory.addCatalog("foo", catalog);
        assertTrue(catalog == factory.getCatalog("foo"));
        assertEquals(1, getCatalogCount());
        factory.addCatalog("foo", new CatalogBase<>());
        assertEquals(1, getCatalogCount());
        assertTrue(!(catalog == factory.getCatalog("foo")));
        CatalogFactory.clear();
        factory = CatalogFactory.getInstance();
        assertEquals(0, getCatalogCount());
    }

    /**
     * Test {@code getCatalog()} method.
     */
    @Test
    public void testCatalogIdentifier() {
        Catalog<Context> defaultCatalog = new CatalogBase<>();
        Command<Context> defaultFoo = new NonDelegatingCommand();
        defaultCatalog.addCommand("foo", defaultFoo);
        Command<Context> fallback = new NonDelegatingCommand();
        defaultCatalog.addCommand("noSuchCatalog:fallback", fallback);

        factory.setCatalog(defaultCatalog);

        Catalog<Context> specificCatalog = new CatalogBase<>();
        Command<Context> specificFoo = new NonDelegatingCommand();
        specificCatalog.addCommand("foo", specificFoo);
        factory.addCatalog("specific", specificCatalog);

        Command<Context> command = factory.getCommand("foo");
        assertSame(defaultFoo, command);

        command = factory.getCommand("specific:foo");
        assertSame(specificFoo, command);

        command = factory.getCommand("void");
        assertNull(command);

        command = factory.getCommand("foo:void");
        assertNull(command);

        command = factory.getCommand("specific:void");
        assertNull(command);

        command = factory.getCommand("noSuchCatalog:fallback");
        assertNull(command);

        try {
            command = factory.getCommand("multiple:delimiters:reserved");
            fail("A command ID with more than one delimiter should throw an IllegalArgumentException");
        }
        catch (IllegalArgumentException ex) {
            // expected behavior
        }
    }

    // ------------------------------------------------------- Support Methods

    /**
     * Return the number of {@link Catalog}s defined in our
     * {@link CatalogFactory}.
     *
     * @return the number of {@link Catalog}s
     */
    private int getCatalogCount() {
        Iterator<String> names = factory.getNames();
        assertNotNull(names);
        int n = 0;
        while (names.hasNext()) {
            names.next();
            n++;
        }
        return n;
    }
}