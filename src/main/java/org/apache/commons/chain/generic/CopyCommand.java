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
package org.apache.commons.chain.generic;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 * Copy a specified literal value, or a context attribute stored under
 * the {@code fromKey} (if any), to the {@code toKey}.
 *
 * @param <C> Type of the context associated with this command
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class CopyCommand<C extends Context> implements Command<C> {

    // -------------------------------------------------------------- Properties

    private String fromKey = null;

    /**
     * Return the context attribute key for the source attribute.
     *
     * @return The source attribute key.
     */
    public String getFromKey() {
        return this.fromKey;
    }

    /**
     * Set the context attribute key for the source attribute.
     *
     * @param fromKey The new key
     */
    public void setFromKey(String fromKey) {
        this.fromKey = fromKey;
    }

    private String toKey = null;

    /**
     * Return the context attribute key for the destination attribute.
     *
     * @return The destination attribute key.
     */
    public String getToKey() {
        return this.toKey;
    }

    /**
     * Set the context attribute key for the destination attribute.
     *
     * @param toKey The new key
     */
    public void setToKey(String toKey) {
        this.toKey = toKey;
    }

    private String value = null;

    /**
     * Return the literal value to be copied.
     *
     * @return The literal value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Set the literal value to be copied.
     *
     * @param value The new value
     */
    public void setValue(String value) {
        this.value = value;
    }

    // ---------------------------------------------------------- Filter Methods

    /**
     * Copy a specified literal value, or a context attribute stored under
     * the {@code fromKey} (if any), to the {@code toKey}.
     *
     * @param context {@link Context} in which we are operating
     *
     * @return {@code false} so that processing will continue
     *
     * @throws Exception in the if an error occurs during execution.
     */
    public boolean execute(C context) throws Exception {
        Object value = this.value;

        if (value == null) {
            value = context.get(getFromKey());
        }

        if (value != null) {
            context.put(getToKey(), value);
        } else {
            context.remove(getToKey());
        }

        return false;
    }
}