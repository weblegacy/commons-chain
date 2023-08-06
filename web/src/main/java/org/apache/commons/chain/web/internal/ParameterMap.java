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
package org.apache.commons.chain.web.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.chain.web.MapEntry;

/**
 * Implementation of {@code Map} for immutable parameters with a
 * parameter-provider.
 *
 * @param <P> the type of the parameter-provider
 * @param <T> the type of results supplied by this parameters
 *
 * @author Graff Stefan
 * @since Chain 1.3
 */
public class ParameterMap<P, T> implements Map<String, T> {

    /**
     * The parameter-provider.
     */
    private final P parameter;

    /**
     * Function to return the value of a parameter as an
     * {@link String}, or {@code null} if no parameter of the
     * given name exists.
     */
    private final Function<String, T> valueFunction;

    /**
     * Supplier to return an {@link Enumeration} of {@link String}
     * objects containing the names of the parameters contained
     * in this object.
     */
    private final Supplier<Enumeration<String>> namesSupplier;

    /**
     * The constructor for an immutable parameter-map.
     *
     * @param parameter the parameter-provider
     * @param valueFunction Function to return the value of a parameter
     * @param namesSupplier Supplier to return all names of the parameter
     */
    public ParameterMap(final P parameter, final Function<String, T> valueFunction,
            final Supplier<Enumeration<String>> namesSupplier) {

        this.parameter = parameter;
        this.valueFunction = valueFunction;
        this.namesSupplier = namesSupplier;
    }

    /**
     * Removes all of the mappings from this parameter-map.
     * The parameter-map will be empty after this call returns.
     *
     * @throws UnsupportedOperationException because it is an
     *         immutable parameter-map
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns {@code true} if this parameter-map contains a mapping
     * for the specified key.
     *
     * @param key The key whose presence in this parameter-map is to
     *            be tested
     *
     * @return {@code true} if this parameter-map contains a mapping
     *         for the specified key.
     */
    @Override
    public boolean containsKey(Object key) {
        return valueFunction.apply(key(key)) != null;
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
        final Enumeration<String> keys = namesSupplier.get();
        while (keys.hasMoreElements()) {
            final T next = valueFunction.apply(keys.nextElement());
            if (value.equals(next)) {
                return true;
            }
        }
        return false;
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
        return entrySet(false);
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this parameter-map contains no mapping
     * for the key.
     *
     * <p>A return value of {@code null} does not <i>necessarily</i>
     * indicate that the parameter-map contains no mapping for the key;
     * it's also possible that the parameter-map explicitly maps the key
     * to {@code null}. The {@link #containsKey containsKey} operation
     * may be used to distinguish these two cases.</p>
     *
     * @param key the key whose associated value is to be returned
     *
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this parameter-map contains no mapping for
     *         the key
     *
     * @see #put(Object, Object)
     */
    @Override
    public T get(Object key) {
        return valueFunction.apply(key(key));
    }

    /**
     * Returns {@code true} if this parameter-map contains no
     * key-value mappings.
     *
     * @return {@code true} if this parameter-map contains no
     *         key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return !namesSupplier.get().hasMoreElements();
    }

    /**
     * Returns a {@link Set} view of the keys contained in this
     * parameter-map. The set is not backed by the parameter-map, so
     * changes to the parameter-map are not reflected in the set, and
     * vice-versa.
     *
     * @return a set view of the keys contained in this parameter-map
     */
    @Override
    public Set<String> keySet() {
        final Set<String> set = new HashSet<>();
        final Enumeration<String> keys = namesSupplier.get();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return set;
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
     *
     * @throws UnsupportedOperationException because it is an
     *         immutable parameter-map
     */
    @Override
    public T put(String key, T value) {
        throw new UnsupportedOperationException();
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
     * @throws UnsupportedOperationException because it is an
     *         immutable parameter-map
     */
    @Override
    public void putAll(Map<? extends String, ? extends T> map) {
        throw new UnsupportedOperationException();
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
     *
     * @throws UnsupportedOperationException because it is an
     *         immutable parameter-map
     */
    @Override
    public T remove(Object key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the number of key-value mappings in this parameter-map.
     *
     * @return the number of key-value mappings in this parameter-map
     */
    @Override
    public int size() {
        int n = 0;
        final Enumeration<String> keys = namesSupplier.get();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return n;
    }

    /**
     * Returns a {@link Collection} view of the values contained in
     * this parameter-map. The collection is not backed by the
     * parameter-map, so changes to the parameter-map are not
     * reflected in the collection, and vice-versa.
     *
     * @return a view of the values contained in this parameter-map
     */
    @Override
    public Collection<T> values() {
        final List<T> list = new ArrayList<>();
        final Enumeration<String> keys = namesSupplier.get();
        while (keys.hasMoreElements()) {
            list.add(valueFunction.apply(keys.nextElement()));
        }
        return list;
    }

    /**
     * Returns the hash code value for this parameter-map. The
     * hash code of a parameter-map is defined to be the sum of
     * the hash codes of each entry in the parameter-map's
     * {@code entrySet()} view. This ensures that {@code m1.equals(m2)}
     * implies that {@code m1.hashCode()==m2.hashCode()} for any two
     * parameter-maps {@code m1} and {@code m2}, as required by the
     * general contract of {@link Object#hashCode}.
     *
     * @implSpec
     * This implementation calls the {@code hashCode()} from the
     * parameter-provider.
     *
     * @return the hash code value for this parameter-map
     */
    @Override
    public int hashCode() {
        return getParameter().hashCode();
    }

    /**
     * Compares the specified object with this parameter-map for equality.
     * Returns {@code true} if the given object is also a parameter-map
     * and the two parameter-maps represent the same mappings. More formally,
     * two parameter-maps {@code m1} and {@code m2} represent the same
     * mappings if {@code m1.entrySet().equals(m2.entrySet())}.
     *
     * @implSpec
     * This implementation first checks if the specified object is this
     * parameter-map; if so it returns {@code true}. Then, it checks if
     * the specified object is the identical class this parameter-map; if
     * not, it returns {@code false}. If so, it calls the {@code equals()}
     * from the parameter-provider and returns its return-code.
     *
     * @param obj object to be compared for equality with this
     *        parameter-map
     *
     * @return {@code true} if the specified object is equal to this
     *         parameter-map
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ParameterMap<?, ?> other = (ParameterMap<?, ?>) obj;
        return Objects.equals(getParameter(), other.getParameter());
    }

    /**
     * Returns a string representation of this parameter-map.
     * The string representation consists of a list of key-value
     * mappings in the order returned by the parameter-map's
     * {@code entrySet} view's iterator, enclosed in braces
     * ({@code "{}"}). Adjacent mappings are separated by the
     * characters {@code ", "} (comma and space). Each key-value
     * mapping is rendered as the key followed by an equals sign
     * ({@code "="}) followed by the associated value. Keys and
     * values are converted to strings as by
     * {@link String#valueOf(Object)}.
     *
     * @return a string representation of this parameter-map
     */
    public String toString() {
        final Iterator<Entry<String, T>> entries = entrySet().iterator();
        if (entries.hasNext()) {
            return "{}";
        }

        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            final Entry<String, T> entrie = entries.next();
            final String key = entrie.getKey();
            final T value = entrie.getValue();
            sb
                .append(key)
                .append('=')
                .append(value);

            if (entries.hasNext()) {
                sb.append(',').append(' ');
            } else {
                return sb.append('}').toString();
            }
        }
    }

    /**
     * Returns the parameter-class.
     *
     * @return the parameter-class
     */
    protected P getParameter() {
        return parameter;
    }

    /**
     * Returns the Function to return the value of a parameter as an
     * {@link String}, or {@code null} if no parameter of the given
     * name exists.
     *
     * @return the Function to return the value
     */
    protected  Function<String, T> getValueFunction() {
        return valueFunction;
    }

    /**
     * Returns the Supplier to return an {@link Enumeration} of
     * {@link String} objects containing the names of the parameters
     * contained in this object.
     *
     * @return Supplier to return an {@link Enumeration} with all
     *         names of the parameters
     */
    protected Supplier<Enumeration<String>> getNamesSupplier() {
        return namesSupplier;
    }

    /**
     * Returns a {@link Set} view of the mappings contained
     * in this map.
     *
     * @param modifiable Whether the entries should allow
     *                   modification or not
     *
     * @return a set view of the mappings contained in this map
     */
    protected Set<Map.Entry<String, T>> entrySet(boolean modifiable) {
        final Set<Map.Entry<String, T>> set = new HashSet<>();
        final Enumeration<String> keys = namesSupplier.get();
        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            set.add(new MapEntry<>(key, valueFunction.apply(key), modifiable));
        }
        return set;
    }

    /**
     * Converts the {@code key} to a {@link String}.
     *
     * @param key the key
     *
     * @return the key as {@link String}
     */
    protected static String key(Object key) {
        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key instanceof String) {
            return (String) key;
        } else {
            return key.toString();
        }
    }
}