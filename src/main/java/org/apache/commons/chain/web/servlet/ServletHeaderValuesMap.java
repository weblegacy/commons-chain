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
package org.apache.commons.chain.web.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain.web.MapEntry;

/**
 * Private implementation of {@code Map} for servlet request
 * name-values[].
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
final class ServletHeaderValuesMap implements Map<String, String[]> {

    private final HttpServletRequest request;

    public ServletHeaderValuesMap(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {
        return request.getHeader(key(key)) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String[])) {
            return false;
        }

        Enumeration<?> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            Object next = request.getHeaders(keys.nextElement().toString());
            if (Objects.deepEquals(value, next)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Map.Entry<String, String[]>> entrySet() {
        Set<Map.Entry<String, String[]>> set = new HashSet<>();
        Enumeration<?> keys = request.getHeaderNames();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement().toString();
            set.add(new MapEntry<String[]>(key, enum2Array(request.getHeaders(key)), false));
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        return request.equals(o);
    }

    @Override
    public String[] get(Object key) {
        return enum2Array(request.getHeaders(key(key)));
    }

    @Override
    public int hashCode() {
        return request.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return !request.getHeaderNames().hasMoreElements();
    }

    @Override
    public Set<String> keySet() {
        Set<String> set = new HashSet<>();
        Enumeration<?> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement().toString());
        }
        return set;
    }

    @Override
    public String[] put(String key, String[] value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends String[]> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        int n = 0;
        Enumeration<?> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }

    @Override
    public Collection<String[]> values() {
        List<String[]> list = new ArrayList<>();
        Enumeration<?> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            list.add(enum2Array(request.getHeaders(keys.nextElement().toString())));
        }
        return list;
    }

    private static String key(Object key) {
        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key instanceof String) {
            return (String) key;
        } else {
            return key.toString();
        }
    }

    private static String[] enum2Array(Enumeration<?> values) {
        List<String> list = new ArrayList<>();
        while (values.hasMoreElements()) {
            list.add(values.nextElement().toString());
        }
        return list.toArray(new String[0]);
    }
}