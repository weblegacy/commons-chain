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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@code Map} for session attributes with a
 * parameter-provider.
 *
 * @param <S> the type of the session-class
 * @param <R> the type of the request-class
 *
 * @author Graff Stefan
 * @since Chain 1.3
 */
public abstract class AbstractSessionScopeMap<S, R> implements Map<String, Object> {

    /**
     * The Session-class.
     */
    private S session = null;

    /**
     * The Request-class.
     */
    private R request;

    /**
     * Mutable-Parameter-Map with the session-attributes.
     */
    private MutableParameterMap<S, Object> parameterMap = null;

    /**
     * The constructor for the session attributes.
     *
     * @param request the request-class
     */
    public AbstractSessionScopeMap(final R request) {
        this.request = request;
        sessionExists();
    }

    /**
     * Removes all of the mappings from this session-map.
     * The session-map will be empty after this call returns.
     */
    @Override
    public void clear() {
        if (sessionExists()) {
            parameterMap.clear();
        }
    }

    /**
     * Returns {@code true} if this session-map contains a mapping
     * for the specified key.
     *
     * @param key The key whose presence in this session-map is to
     *            be tested
     *
     * @return {@code true} if this session-map contains a mapping
     *         for the specified key.
     */
    @Override
    public boolean containsKey(Object key) {
        return sessionExists() && parameterMap.containsKey(key);
    }

    /**
     * Returns {@code true} if this session-map maps one or more keys
     * to the specified value.
     *
     * @param value value whose presence in this session-map is to be
     *        tested
     *
     * @return {@code true} if this session-map maps one or more keys
     *         to the specified value
     */
    @Override
    public boolean containsValue(Object value) {
        return value != null && sessionExists() && parameterMap.containsValue(value);
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this
     * session-map. The set is not backed by the session-map, so
     * changes to the session-map are not reflected in the set,
     * and vice-versa.
     *
     * @return a set view of the mappings contained in this
     *         session-map
     */
    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        return sessionExists() ? parameterMap.entrySet() : new HashSet<>();
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this session-map contains no mapping
     * for the key.
     *
     * <p>A return value of {@code null} does not <i>necessarily</i>
     * indicate that the session-map contains no mapping for the key;
     * it's also possible that the session-map explicitly maps the
     * key to {@code null}. The {@link #containsKey containsKey}
     * operation may be used to distinguish these two cases.</p>
     *
     * @param key the key whose associated value is to be returned
     *
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this session-map contains no mapping for
     *         the key
     *
     * @see #put(Object, Object)
     */
    @Override
    public Object get(Object key) {
        return sessionExists() ? parameterMap.get(key) : null;
    }

    /**
     * Returns {@code true} if this session-map contains no
     * key-value mappings.
     *
     * @return {@code true} if this session-map contains no
     *         key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return !sessionExists() || parameterMap.isEmpty();
    }

    /**
     * Returns a {@link Set} view of the keys contained in this
     * session-map. The set is not backed by the session-map, so
     * changes to the session-map are not reflected in the set, and
     * vice-versa.
     *
     * @return a set view of the keys contained in this session-map
     */
    @Override
    public Set<String> keySet() {
        return sessionExists() ? parameterMap.keySet() : new HashSet<>();
    }

    /**
     * Associates the specified value with the specified key in this
     * session-map. If the session-map previously contained a
     * mapping for the key, the old value is replaced.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     *
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     *         (A {@code null} return can also indicate that the
     *         session-map previously associated {@code null} with
     *         {@code key.)
     */
    @Override
    public Object put(String key, Object value) {
        if (value == null) {
            return remove(key);
        }

        // Ensure the Session is created, if it
        // doesn't exist
        return sessionExists(true) ? parameterMap.put(key, value) : null;
    }

    /**
     * Copies all of the mappings from the specified map to this
     * session-map. These mappings will replace any mappings that
     * this session-map had for any of the keys currently in the
     * specified map.
     *
     * @param map mappings to be stored in this session-map
     *
     * @throws NullPointerException if the specified map is null
     */
    @Override
    public void putAll(Map<? extends String, ? extends Object> map) {
        map.forEach(this::put);
    }

    /**
     * Removes the mapping for the specified key from this
     * session-map if present.
     *
     * @param key key whose mapping is to be removed from the
     *        session-map
     *
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     *         (A {@code null} return can also indicate that the
     *         session-map previously associated {@code null} with
     *         {@code key.)
     */
    @Override
    public Object remove(Object key) {
        return sessionExists() ? parameterMap.remove(key) : null;
    }

    /**
     * Returns the number of key-value mappings in this session-map.
     *
     * @return the number of key-value mappings in this session-map
     */
    @Override
    public int size() {
        return sessionExists() ? parameterMap.size() : 0;
    }

    /**
     * Returns a {@link Collection} view of the values contained in
     * this session-map. The collection is not backed by the
     * session-map, so changes to the session-map are not
     * reflected in the collection, and vice-versa.
     *
     * @return a view of the values contained in this session-map
     */
    @Override
    public Collection<Object> values() {
        return sessionExists() ? parameterMap.values() : new ArrayList<>();
    }

    /**
     * Returns the hash code value for this session-map. The
     * hash code of a session-map is defined to be the sum of
     * the hash codes of each entry in the session-map's
     * {@code entrySet()} view. This ensures that {@code m1.equals(m2)}
     * implies that {@code m1.hashCode()==m2.hashCode()} for any two
     * session-maps {@code m1} and {@code m2}, as required by the
     * general contract of {@link Object#hashCode}.
     *
     * @return the hash code value for this session-map
     */
    @Override
    public int hashCode() {
        return sessionExists() ? parameterMap.hashCode() : 0;
    }

    /**
     * Compares the specified object with this session-map for equality.
     * Returns {@code true} if the given object is also a session-map
     * and the two session-maps represent the same mappings. More formally,
     * two session-maps {@code m1} and {@code m2} represent the same
     * mappings if {@code m1.entrySet().equals(m2.entrySet())}.
     *
     * @param obj object to be compared for equality with this
     *        session-map
     *
     * @return {@code true} if the specified object is equal to this
     *         session-map
     */
    @Override
    public boolean equals(Object obj) {
        return sessionExists() && parameterMap.equals(obj);
    }

    /**
     * Returns a string representation of this session-map.
     *
     * @return a string representation of this session-map
     */
    public String toString() {
        if (request != null) {
            return "{request: " + request + '}';
        }

        if (session != null) {
            final StringBuilder sb = new StringBuilder();
            sb
                .append("{session: ")
                .append(session);

            if (parameterMap != null) {
                sb
                    .append(", parameters: ")
                    .append(parameterMap);
            }
            return sb.append('}').toString();
        }
        return "{}";
    }

    /**
     * Returns the session-class.
     *
     * @return the session-class
     */
    protected S getSession() {
        return session;
    };

    /**
     * Returns the request-class.
     *
     * @return the request-class
     */
    protected R getRequest() {
        return request;
    };

    /**
     * Returns {@code true} if a session exists.
     *
     * @return {@code true} if a session exists
     */
    protected boolean sessionExists() {
        return sessionExists(false);
    }

    /**
     * Returns {@code true} if a session exists or creates a new
     * session if the parameter {@¢ode create} is set to {@code true}.
     *
     * @param create {@code true} to create a new session if no
     *               session exists
     *
     * @return {@code true} if a session exists
     */
    protected boolean sessionExists(boolean create) {
        if (session == null && request != null) {
            session = getSession(create);
            if (session != null) {
                request = null;
                parameterMap = createParameterMap();
            }
        }

        if (session == null) {
            parameterMap = null;
            return false;
        } else {
            return parameterMap != null;
        }
    }

    /**
     * Returns the current session or, if there is no current session and
     * the given flag is {@code true}, creates one and returns the new session.
     *
     * <p>If the given flag is {@code false} and there is no current
     * session, this method returns {@code null}.</p>
     *
     * @param create {@code true} to create a new session, {@code false} to return
     *               {@code null} if there is no current session
     *
     * @return the session
     */
    protected abstract S getSession(boolean create);

    /**
     * Creates a new mutable-parameter-map to access the session-attributes.
     *
     * @return a new mutable-parameter-map to access the session-attributes
     */
    protected abstract MutableParameterMap<S, Object> createParameterMap();
}