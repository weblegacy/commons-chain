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
package org.apache.commons.chain.tests.impl;

import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 * Implementation of {@link Command} that logs its identifier and
 * and attempts to add a new {@link Command} to the {@link Chain}. This
 * should cause an IllegalStateException if the {@link Chain} implementation
 * subclasses {@code ChainBase}.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class AddingCommand extends NonDelegatingCommand {

    // ------------------------------------------------------------ Constructor

    public AddingCommand() {
        this("", null);
    }

    /**
     * Construct an instance that will log the specified identifier
     *
     * @param id identifier to log for this Command instance
     * @param parent the parent Chain
     */
    public AddingCommand(String id, Chain<Context> parent) {
        super(id);
        this.parent = parent;
    }

    /**
     * The parent Chain
     */
    private Chain<Context> parent = null;

    // -------------------------------------------------------- Command Methods

    /**
     * Execution method for this Command
     *
     * @param context The {@link Context} to be processed by this
     *        {@link Command}
     * @param chain the parent Chain
     *
     * @return {@code true} if the processing of this {@link Context}
     *         has been completed, or {@code false} if the processing
     *         of this {@link Context} should be delegated to a
     *         subsequent {@link Command} in an enclosing {@link Chain}
     *
     * @throws Exception general purpose exception return
     *         to indicate abnormal termination
     * @throws IllegalArgumentException if {@code context}
     *         is {@code null}
     */
    public boolean execute(Context context, Chain<Context> chain) throws Exception {
        super.execute(context);
        parent.addCommand(new NonDelegatingCommand("NEW")); // Should cause ISE
        return true;
    }
}