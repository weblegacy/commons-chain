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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;

/**
 * Convenience base class for {@link Chain} implementations.
 *
 * @param <C> Type of the context associated with this chain
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class ChainBase<C extends Context> implements Chain<C> {

    // ----------------------------------------------------------- Constructors

    /**
     * Construct a {@link Chain} with no configured {@link Command}s.
     */
    public ChainBase() {
    }

    /**
     * Construct a {@link Chain} configured with the specified
     * {@link Command}.
     *
     * @param command The {@link Command} to be configured
     *
     * @throws IllegalArgumentException if {@code command}
     *         is {@code null}
     */
    public ChainBase(Command<C> command) {
        addCommand(command);
    }

    /**
     * Construct a {@link Chain} configured with the specified
     * {@link Command}s.
     *
     * @param commands The {@link Command}s to be configured
     *
     * @throws IllegalArgumentException if {@code commands},
     *         or one of the individual {@link Command} elements,
     *         is {@code null}
     */
    public ChainBase(Command<C>[] commands) {
        if (commands == null) {
            throw new IllegalArgumentException();
        }
        for (Command<C> command : commands) {
            addCommand(command);
        }
    }

    /**
     * Construct a {@link Chain} configured with the specified
     * {@link Command}s.
     *
     * @param commands The {@link Command}s to be configured
     *
     * @throws IllegalArgumentException if {@code commands},
     *         or one of the individual {@link Command} elements,
     *         is {@code null}
     */
    public ChainBase(Collection<Command<C>> commands) {
        if (commands == null) {
            throw new IllegalArgumentException();
        }
        commands.forEach(this::addCommand);
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * The list of {@link Command}s configured for this {@link Chain}, in
     * the order in which they may delegate processing to the remainder of
     * the {@link Chain}.
     */
    protected final ArrayList<Command<C>> commands = new ArrayList<>();

    /**
     * Flag indicating whether the configuration of our commands list
     * has been frozen by a call to the {@code execute()} method.
     */
    protected boolean frozen = false;

    // ---------------------------------------------------------- Chain Methods

    /**
     * See the {@link Chain} JavaDoc.
     *
     * @param command The {@link Command} to be added
     *
     * @throws IllegalArgumentException if {@code command}
     *         is {@code null}
     * @throws IllegalStateException if no further configuration is allowed
     */
    public <CMD extends Command<C>> void addCommand(CMD command) {
        if (command == null) {
            throw new IllegalArgumentException();
        }
        if (frozen) {
            throw new IllegalStateException();
        }
        commands.add(command);
    }

    /**
     * See the {@link Chain} JavaDoc.
     *
     * @param context The {@link Context} to be processed by this
     *  {@link Chain}
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
    public boolean execute(C context) throws Exception {
        // Verify our parameters
        if (context == null) {
            throw new IllegalArgumentException();
        }

        // Freeze the configuration of the command list
        if (!frozen) {
            frozen = true;
            commands.trimToSize();
        }

        // Execute the commands in this list until one returns true
        // or throws an exception
        boolean saveResult = false;
        Exception saveException = null;
        int i = 0;
        int n = commands.size();
        for (i = 0; i < n; i++) {
            try {
                saveResult = commands.get(i).execute(context);
                if (saveResult) {
                    break;
                }
            } catch (Exception e) {
                saveException = e;
                break;
            }
        }

        // Call postprocess methods on Filters in reverse order
        if (i >= n) { // Fell off the end of the chain
            i--;
        }
        boolean handled = false;
        boolean result = false;
        for (int j = i; j >= 0; j--) {
            Command<C> command = commands.get(j);
            if (command instanceof Filter) {
                try {
                    result =
                        ((Filter<C>) command).postprocess(context,
                                                          saveException);
                    if (result) {
                        handled = true;
                    }
                } catch (Exception e) {
                      // Silently ignore
                }
            }
        }

        // Return the exception or result state from the last execute()
        if (saveException != null && !handled) {
            throw saveException;
        } else {
            return saveResult;
        }
    }

    // -------------------------------------------------------- Package Methods

    /**
     * Return an array of the configured {@link Command}s for this
     * {@link Chain}. This method is package private, and is used only
     * for the unit tests.
     */
    List<Command<C>> getCommands() {
        return commands;
    }
}