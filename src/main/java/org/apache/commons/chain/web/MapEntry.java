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
package org.apache.commons.chain.web;

import java.util.Map;

/**
 * {@code Map.Entry} implementation that can be constructed to either be read-only
 * or not.
 *
 * @version $Revision$ $Date$
 */
public class MapEntry implements Map.Entry {

    /**
     * The entry key.
     */
    private Object key;

    /**
     * The entry value.
     */
    private Object value;

    /**
     * Whether the entry can be modified.
     */
    private boolean modifiable = false;

    /**
     * Creates a map entry that can either allow modifications or not.
     *
     * @param key The entry key
     * @param value The entry value
     * @param modifiable Whether the entry should allow modification or not
     */
    public MapEntry(Object key, Object value, boolean modifiable) {
        this.key = key;
        this.value = value;
        this.modifiable = modifiable;
    }

    /**
     * Gets the entry key.
     *
     * @return The entry key
     */
    @Override
    public Object getKey() {
        return key;
    }

    /**
     * Gets the entry value.
     *
     * @return The entry key
     */
    @Override
    public Object getValue() {
        return value;
    }

    /**
     * Sets the entry value if the entry can be modified.
     *
     * @param val The new value
     *
     * @return The old entry value
     *
     * @throws UnsupportedOperationException If the entry cannot be modified
     */
    @Override
    public Object setValue(Object val) {
        if (modifiable) {
            Object oldVal = this.value;
            this.value = val;
            return oldVal;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Determines if this entry is equal to the passed object.
     *
     * @param o The object to test
     *
     * @return True if equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry)o;
            return (this.getKey() == null ?
                    entry.getKey() == null : this.getKey().equals(entry.getKey()))  &&
                   (this.getValue() == null ?
                    entry.getValue() == null : this.getValue().equals(entry.getValue()));
        }
        return false;
    }

    /**
     * Returns the hashcode for this entry.
     *
     * @return The and'ed hashcode of the key and value
     */
    @Override
    public int hashCode() {
        return (this.getKey() == null   ? 0 : this.getKey().hashCode()) ^
               (this.getValue() == null ? 0 : this.getValue().hashCode());
    }
}