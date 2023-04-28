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
 * A {@link Chain} represents a configured list of
 * {@link Command}s that will be executed in order to perform processing
 * on a specified {@link Context}. Each included {@link Command} will be
 * executed in turn, until either one of them returns {@code true},
 * one of the executed {@link Command}s throws an exception,
 * or the end of the chain has been reached. The {@link Chain} itself will
 * return the return value of the last {@link Command} that was executed
 * (if no exception was thrown), or rethrow the thrown exception.
 *
 * <p>Note that {@link Chain} extends {@link Command}, so that the two can
 * be used interchangeably when a {@link Command} is expected. This makes it
 * easy to assemble workflows in a hierarchical manner by combining subchains
 * into an overall processing chain.</p>
 *
 * <p>To protect applications from evolution of this interface, specialized
 * implementations of {@link Chain} should generally be created by extending
 * the provided base class {@link org.apache.commons.chain.impl.ChainBase})
 * rather than directly implementing this interface.</p>
 *
 * <p>{@link Chain} implementations should be designed in a thread-safe
 * manner, suitable for execution on multiple threads simultaneously. In
 * general, this implies that the state information identifying which
 * {@link Command} is currently being executed should be maintained in a
 * local variable inside the {@code execute()} method, rather than
 * in an instance variable. The {@link Command}s in a {@link Chain} may be
 * configured (via calls to {@code addCommand()}) at any time before
 * the {@code execute()} method of the {@link Chain} is first called.
 * After that, the configuration of the {@link Chain} is frozen.</p>
 *
 * @param <C> Type of the context associated with this chain
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public interface Chain<C extends Context> extends Command<C> {

    /**
     * Add a {@link Command} to the list of {@link Command}s that will
     * be called in turn when this {@link Chain}'s {@code execute()}
     * method is called. Once {@code execute()} has been called
     * at least once, it is no longer possible to add additional
     * {@link Command}s; instead, an exception will be thrown.</p>
     *
     * @param <CMD> the {@link Command} type to be added in the {@link Chain}
     * @param command The {@link Command} to be added
     *
     * @throws IllegalArgumentException if {@code command}
     *         is {@code null}
     * @throws IllegalStateException if this {@link Chain} has already
     *         been executed at least once, so no further configuration
     *         is allowed
     */
    <CMD extends Command<C>> void addCommand(CMD command);

    /**
     * Execute the processing represented by this {@link Chain} according
     * to the following algorithm.
     * <ul>
     * <li>If there are no configured {@link Command}s in the {@link Chain},
     *     return {@code false}.</li>
     * <li>Call the {@code execute()} method of each {@link Command}
     *     configured on this chain, in the order they were added via calls
     *     to the {@code addCommand()} method, until the end of the
     *     configured {@link Command}s is encountered, or until one of
     *     the executed {@link Command}s returns {@code true}
     *     or throws an exception.</li>
     * <li>Walk backwards through the {@link Command}s whose
     *     {@code execute()} methods, starting with the last one that
     *     was executed. If this {@link Command} instance is also a
     *     {@link Filter}, call its {@code postprocess()} method,
     *     discarding any exception that is thrown.</li>
     * <li>If the last {@link Command} whose {@code execute()} method
     *     was called threw an exception, rethrow that exception.</li>
     * <li>Otherwise, return the value returned by the {@code execute()}
     *     method of the last {@link Command} that was executed. This will be
     *     {@code true} if the last {@link Command} indicated that
     *     processing of this {@link Context} has been completed, or
     *     {@code false} if none of the called {@link Command}s
     *     returned {@code true}.</li>
     * </ul>
     *
     * @param context The {@link Context} to be processed by this
     *        {@link Chain}
     *
     * @return {@code true} if the processing of this {@link Context}
     *         has been completed, or {@code false} if the processing
     *         of this {@link Context} should be delegated to a
     *         subsequent {@link Command} in an enclosing {@link Chain}
     *
     * @throws Exception if thrown by one of the {@link Command}s
     *         in this {@link Chain} but not handled by a
     *         {@code postprocess()} method of a {@link Filter}
     * @throws IllegalArgumentException if {@code context}
     *         is {@code null}
     */
    boolean execute(C context) throws Exception;
}