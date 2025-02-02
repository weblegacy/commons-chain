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
 * Implementation of {@link Command} that logs its identifier and
 * and throws an Exception.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class ExceptionCommand extends NonDelegatingCommand {

    // ------------------------------------------------------------ Constructor

    public ExceptionCommand() {
        this("");
    }

    /**
     * Construct an instance that will log the specified identifier
     *
     * @param id identifier to log for this Command instance
     */
    public ExceptionCommand(String id) {
        super(id);
    }

    // -------------------------------------------------------- Command Methods

    /**
     * Execution method for this Command
     *
     * @param context
     * @param chain
     *
     * @throws Exception any error
     */
    public void execute(Context context, Chain<Context> chain) throws Exception {
        super.execute(context);
        throw new ArithmeticException(this.id);
    }
}