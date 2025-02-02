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

import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 * Extended {@link ChainBase} for testing.
 *
 * @param <C> Type of the context associated with this chain
 *
 * @author Stefan Graff
 */
public class ChainBaseEx<C extends Context> extends ChainBase<C> {

    // -------------------------------------------------------- Testing Methods

    /**
     * Return an array of the configured {@link Command}s for this
     * {@link Chain}. This method is package private, and is used only
     * for the unit tests.
     *
     * @return an array of the configured {@link Command}s for this {@link Chain}
     */
    @Override
    public Command<C>[] getCommands() {
        return super.getCommands();
    }
}