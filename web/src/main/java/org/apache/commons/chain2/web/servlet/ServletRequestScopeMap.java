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
package org.apache.commons.chain2.web.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain2.web.MapEntry;

/**
 * <p>Private implementation of <code>Map</code> for servlet request
 * attributes.</p>
 *
 */
final class ServletRequestScopeMap implements Map<String, Object> {

    public ServletRequestScopeMap(HttpServletRequest request) {
        this.request = request;
    }

    private final HttpServletRequest request;

    public void clear() {
        for (String key : keySet()) {
            request.removeAttribute(key);
        }
    }

    public boolean containsKey(Object key) {
        return (request.getAttribute(key(key)) != null);
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            return (false);
        }
        @SuppressWarnings( "unchecked" ) // it is known that attribute names are String
        Enumeration<String> keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            Object next = request.getAttribute(keys.nextElement());
            if (value.equals(next)) {
                return (true);
            }
        }
        return (false);
    }

    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> set = new HashSet<Entry<String, Object>>();
        @SuppressWarnings( "unchecked" ) // it is known that attribute names are String
        Enumeration<String> keys = request.getAttributeNames();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            set.add(new MapEntry<String, Object>(key, request.getAttribute(key), true));
        }
        return (set);
    }

    @Override
    public boolean equals(Object o) {
        return (request.equals(o));
    }

    public Object get(Object key) {
        return (request.getAttribute(key(key)));
    }

    @Override
    public int hashCode() {
        return (request.hashCode());
    }

    public boolean isEmpty() {
        return (size() < 1);
    }

    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        @SuppressWarnings( "unchecked" ) // it is known that attribute names are String
        Enumeration<String> keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return (set);
    }

    public Object put(String key, Object value) {
        if (value == null) {
            return (remove(key));
        }
        Object previous = request.getAttribute(key);
        request.setAttribute(key, value);
        return (previous);
    }

    public void putAll(Map<? extends String, ? extends Object> map) {
        for (Entry<? extends String, ? extends Object> entry : map.entrySet()) {
            put(key(entry.getKey()), entry.getValue());
        }
    }

    public Object remove(Object key) {
        String skey = key(key);
        Object previous = request.getAttribute(skey);
        request.removeAttribute(skey);
        return (previous);
    }

    public int size() {
        int n = 0;
        @SuppressWarnings( "unchecked" ) // it is known that attribute names are String
        Enumeration<String> keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }

    public Collection<Object> values() {
        List<Object> list = new ArrayList<Object>();
        @SuppressWarnings( "unchecked" ) // it is known that attribute names are String
        Enumeration<String> keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            list.add(request.getAttribute(keys.nextElement()));
        }
        return (list);
    }

    private String key(Object key) {
        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key instanceof String) {
            return ((String) key);
        } else {
            return (key.toString());
        }
    }

}
