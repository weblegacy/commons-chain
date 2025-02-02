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
 * A {@link Command} encapsulates a unit of processing work to be
 * performed, whose purpose is to examine and/or modify the state of a
 * transaction that is represented by a {@link Context}. Individual
 * {@link Command}s can be assembled into a {@link Chain}, which allows
 * them to either complete the required processing or delegate further
 * processing to the next {@link Command} in the {@link Chain}.
 *
 * <p>{@link Command} implementations should be designed in a thread-safe
 * manner, suitable for inclusion in multiple {@link Chain}s that might be
 * processed by different threads simultaneously. In general, this implies
 * that {@link Command} classes should not maintain state information in
 * instance variables. Instead, state information should be maintained via
 * suitable modifications to the attributes of the {@link Context} that is
 * passed to the {@code execute()} command.</p>
 *
 * <p>{@link Command} implementations typically retrieve and store state
 * information in the {@link Context} instance that is passed as a parameter
 * to the {@code execute()} method, using particular keys into the
 * {@code Map} that can be acquired via {@code Context.getAttributes()}. To
 * improve interoperability of {@link Command} implementations, a useful
 * design pattern is to expose the key values used as JavaBeans properties
 * of the {@link Command} implementation class itself. For example, a
 * {@link Command} that requires an input and an output key might implement
 * the following properties:</p>
 *
 * <pre>
 *   private String inputKey = "input";
 *   public String getInputKey() {
 *     return this.inputKey;
 *   }
 *   public void setInputKey(String inputKey) {
 *     this.inputKey = inputKey;
 *   }
 *
 *   private String outputKey = "output";
 *   public String getOutputKey() {
 *     return this.outputKey;
 *   }
 *   public void setOutputKey(String outputKey) {
 *     this.outputKey = outputKey;
 *   }
 * </pre>
 *
 * <p>And the operation of accessing the "input" information in the context
 * would be executed by calling:</p>
 *
 * <pre>
 *   String input = (String) context.get(getInputKey());
 * </pre>
 *
 * <p>instead of hard coding the attribute name. The use of the "Key"
 * suffix on such property names is a useful convention to identify properties
 * being used in this fashion, as opposed to JavaBeans properties that simply
 * configure the internal operation of this {@link Command}.</p>
 *
 * @param <C> Type of the context associated with this command
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public interface Command<C extends Context> {

    /**
     * Commands should return {@code CONTINUE_PROCESSING} if the processing
     * of the given {@link Context} should be delegated to a subsequent
     * {@link Command} in an enclosing {@link Chain}.
     *
     * @since Chain 1.1
     */
    boolean CONTINUE_PROCESSING = false;

    /**
     * Commands should return {@code PROCESSING_COMPLETE}
     * if the processing of the given {@link Context}
     * has been completed.
     *
     * @since Chain 1.1
     */
    boolean PROCESSING_COMPLETE = true;

    /**
     * Execute a unit of processing work to be performed. This
     * {@link Command} may either complete the required processing
     * and return {@code true}, or delegate remaining processing
     * to the next {@link Command} in a {@link Chain} containing this
     * {@link Command} by returning {@code false}.
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
    boolean execute(C context) throws Exception;
}