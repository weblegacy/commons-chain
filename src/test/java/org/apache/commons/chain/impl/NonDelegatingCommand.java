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
 * Implementation of {@link Command} that simply logs its identifier
 * and returns.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class NonDelegatingCommand implements Command<Context> {

    // ------------------------------------------------------------ Constructor

    public NonDelegatingCommand() {
        this("");
    }

    /**
     * Construct an instance that will log the specified identifier
     *
     * @param id identifier to log for this Command instance
     */
    public NonDelegatingCommand(String id) {
        this.id = id;
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * The identifier to log for this Command instance
     */
    protected String id = null;

    String getId() {
        return (this.id);
    }

    public void setId(String id) {
    this.id = id;
    }

    // -------------------------------------------------------- Command Methods

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
    public boolean execute(Context context) throws Exception {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        log(context, id);
        return true;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Log the specified {@code id} into a StringBuffer attribute
     * named "log" in the specified {@code context}, creating it if
     * necessary.
     *
     * @param context The {@link Context} into which we log the identifiers
     * @param id The identifier to be logged
     */
    protected void log(Context context, String id) {
        StringBuffer sb = (StringBuffer) context.get("log");
        if (sb == null) {
            sb = new StringBuffer();
            context.put("log", sb);
        }
        if (sb.length() > 0) {
            sb.append('/');
        }
        sb.append(id);
    }
}