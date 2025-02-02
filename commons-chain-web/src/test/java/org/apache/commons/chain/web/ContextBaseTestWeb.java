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
package org.apache.commons.chain.web;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.ContextTestCase;
import org.junit.jupiter.api.Test;

/**
 * Test case for the {@code ContextBase} class.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public abstract class ContextBaseTestWeb<C extends Context> extends ContextTestCase<C> {

    // ---------------------------------------------------------- Constructors

    /**
     * The Default-Constructor for this class.
     */
    public ContextBaseTestWeb() {
    }

    // ------------------------------------------------ Individual Test Methods

    /**
     * Test serialization
     *
     * @throws Exception any error
     */
    @Test
    public void testSerialization() throws Exception {
        // ContextBase is implicitly declared Serializable because it
        // extends HashMap. However, it is not possible to make
        // the concrete subclasses of WebContext Serializable, because
        // the underlying container objects that they wrap will not be.
        // Therefore, skip testing serializability of these implementations
    	return;
    }
}