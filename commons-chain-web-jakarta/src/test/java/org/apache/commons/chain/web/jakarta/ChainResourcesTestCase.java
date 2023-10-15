/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.chain.web.jakarta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Test case for {@link org.apache.commons.chain.web.jakarta.ChainResources}
 */
public class ChainResourcesTestCase {

    // ----------------------------------------------------- Instance Variables

    protected TestData[] data = new TestData[] {
        new TestData("a,b,c",            new String[] {"a", "b", "c"}),
        new TestData("a , b , c ",       new String[] {"a", "b", "c"}),
        new TestData("a,\tb,\tc ",       new String[] {"a", "b", "c"}),
        new TestData("\na,\nb,\nc\n",    new String[] {"a", "b", "c"}),
        new TestData("a,,b,c ",          new String[] {"a", "b", "c"}),
        new TestData(",a,b,,c,,",        new String[] {"a", "b", "c"}),
        new TestData(null,               new String[] {}),
        new TestData("",                 new String[] {}),
        new TestData(",",                new String[] {}),
        new TestData(",,",               new String[] {})
    };

    // ----------------------------------------------------------- Constructors

    /**
     * The Default-Constructor for this class.
     */
    public ChainResourcesTestCase() {
    }

    // ------------------------------------------------ Individual Test Methods

    @Test
    public void testGetPaths() throws Exception {
        for (TestData datum : data) {
            String[] expected = datum.getExpected();
            String[] actual = ChainResources.getResourcePaths(datum.getInput());

            assertNotNull(actual);
            assertEquals(expected.length, actual.length);
            for (int j = 0; j < actual.length; j++) {
                assertEquals(expected[j], actual[j]);
            }
        }
    }

    // ---------------------------------------------------------- Inner classes

    /**
     * Container for test data for one test
     */
    public static final class TestData {
        private String input;
        private String[] expected;
        public TestData(String input, String[] expected) {
            this.input = input;
            this.expected = expected;
        }
        public String getInput() {
            return input;
        }
        public String[] getExpected() {
            return expected;
        }
    }
}