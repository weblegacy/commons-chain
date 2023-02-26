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
package org.apache.commons.chain;

/**
 * A {@link Filter} is a specialized {@link Command} that also expects
 * the {@link Chain} that is executing it to call the
 * {@code postprocess()} method if it called the {@code execute()}
 * method. This promise must be fulfilled in spite of any possible
 * exceptions thrown by the {@code execute()} method of this
 * {@link Command}, or any subsequent {@link Command} whose
 * {@code execute()} method was called. The owning {@link Chain}
 * must call the {@code postprocess()} method of each {@link Filter}
 * in a {@link Chain} in reverse order of the invocation of their
 * {@code execute()} methods.
 *
 * <p>The most common use case for a {@link Filter}, as opposed to a
 * {@link Command}, is where potentially expensive resources must be acquired
 * and held until the processing of a particular request has been completed,
 * even if execution is delegated to a subsequent {@link Command} via the
 * {@code execute()} returning {@code false}. A {@link Filter}
 * can reliably release such resources in the {@code postprocess()}
 * method, which is guaranteed to be called by the owning {@link Chain}.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public interface Filter extends Command {

    /**
     * Execute any cleanup activities, such as releasing resources that
     * were acquired during the {@code execute()} method of this
     * {@link Filter} instance.
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
   boolean postprocess(Context context, Exception exception);
}