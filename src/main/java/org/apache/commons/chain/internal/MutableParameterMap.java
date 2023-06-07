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
package org.apache.commons.chain.internal;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Implementation of {@code Map} for mutable parameters with a
 * parameter-provider.
 *
 * @param <P> the type of the parameter-provider
 * @param <T> the type of results supplied by this parameters
 *
 * @author Graff Stefan
 * @since Chain 1.3
 */
public class MutableParameterMap<P, T> extends ParameterMap<P, T> {

    /**
     * Consumer which removes the given public or private parameter.
     * All values associated with the name are removed.
     */
    private final Consumer<String> removeConsumer;

    /**
     * BiConsumer which stores an {@link Object} in this parameter.
     */
    private final BiConsumer<String, T> setConsumer;

    /**
     * The constructor for an mutable parameter-map.
     *
     * @param parameter the parameter-provider
     * @param valueFunction Function to return the value of a parameter
     * @param namesSupplier Supplier to return all names of the parameter
     * @param removeConsumer Consumer to remove a value of the parameter
     * @param setConsumer BiConsumer to stores a value of the parameter
     */
    public MutableParameterMap(final P parameter, final Function<String, T> valueFunction,
            final Supplier<Enumeration<String>> namesSupplier, final Consumer<String> removeConsumer,
            final BiConsumer<String, T> setConsumer) {

        super(parameter, valueFunction, namesSupplier);
        this.removeConsumer = removeConsumer;
        this.setConsumer = setConsumer;
    }

    /**
     * Removes all of the mappings from this parameter-map.
     * The parameter-map will be empty after this call returns.
     */
    @Override
    public void clear() {
        for (String key : keySet()) {
            removeConsumer.accept(key);
        }
    }

    /**
     * Returns {@code true} if this parameter-map maps one or more keys
     * to the specified value.
     *
     * @param value value whose presence in this parameter-map is to be
     *        tested
     *
     * @return {@code true} if this parameter-map maps one or more keys
     *         to the specified value
     */
    @Override
    public boolean containsValue(Object value) {
        return value != null && super.containsValue(value);
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this
     * parameter-map. The set is not backed by the parameter-map, so
     * changes to the parameter-map are not reflected in the set,
     * and vice-versa.
     *
     * @return a set view of the mappings contained in this
     *         parameter-map
     */
    @Override
    public Set<Map.Entry<String, T>> entrySet() {
        return entrySet(true);
    }

    /**
     * Associates the specified value with the specified key in this
     * parameter-map. If the parameter-map previously contained a
     * mapping for the key, the old value is replaced.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     *
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     *         (A {@code null} return can also indicate that the
     *         parameter-map previously associated {@code null} with
     *         {@code key.)
     */
    @Override
    public T put(String key, T value) {
        if (value == null) {
            return remove(key);
        }
        final T previous = getValueFunction().apply(key);
        setConsumer.accept(key, value);
        return previous;
    }

    /**
     * Copies all of the mappings from the specified map to this
     * parameter-map. These mappings will replace any mappings that
     * this parameter-map had for any of the keys currently in the
     * specified map.
     *
     * @param map mappings to be stored in this parameter-map
     *
     * @throws NullPointerException if the specified map is null
     */
    @Override
    public void putAll(Map<? extends String, ? extends T> map) {
        map.forEach(this::put);
    }

    /**
     * Removes the mapping for the specified key from this
     * parameter-map if present.
     *
     * @param key key whose mapping is to be removed from the
     *        parameter-map
     *
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     *         (A {@code null} return can also indicate that the
     *         parameter-map previously associated {@code null} with
     *         {@code key.)
     */
    @Override
    public T remove(Object key) {
        final String skey = key(key);
        final T previous = getValueFunction().apply(skey);
        removeConsumer.accept(skey);
        return previous;
    }
}