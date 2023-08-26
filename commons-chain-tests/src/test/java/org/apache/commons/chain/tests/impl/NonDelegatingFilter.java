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
import org.apache.commons.chain.Filter;

/**
 * Implementation of {@link Filter} that logs its identifier and
 * and returns [@code true}.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class NonDelegatingFilter extends NonDelegatingCommand
    implements Filter<Context> {

    // ------------------------------------------------------------- Constructor

    public NonDelegatingFilter() {
        this("", "");
    }

    /**
     * Construct an instance that will log the specified identifier
     *
     * @param id1 first identifier to log for this Command instance
     * @param id2 second identifier to log for this Command instance
     */
    public NonDelegatingFilter(String id1, String id2) {
        super(id1);
        this.id2 = id2;
    }

    // -------------------------------------------------------------- Properties

    protected String id2 = null;
    public String getId2() {
        return this.id2;
    }
    public void setId2(String id2) {
        this.id2 = id2;
    }

    // --------------------------------------------------------- Command Methods

    /**
     * Execution method for this Command
     *
     * @param context The {@link Context} to be processed by this
     *        {@link Command}
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
    @Override
    public boolean execute(Context context) throws Exception {
        super.execute(context);
        return true;
    }

    /**
     * Postprocess method for this Filter
      *
     * @param context The {@link Context} to be processed by this
     *        {@link Filter}
     * @param exception The {@code Exception} (if any) that was thrown
     *        by the last {@link Command} that was executed; otherwise
     *        {@code null}
     *
     * @return If a non-null {@code exception} was "handled" by this
     *         method (and therefore need not be rethrown), return
     *         {@code true}; otherwise return {@code false}
     *
     * @throws IllegalArgumentException if {@code context}
     *         is {@code null}
     */
    @Override
    public boolean postprocess(Context context, Exception exception) {
        log(context, id2);
        return false;
    }
}