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
package org.apache.commons.chain.web.portlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequest;

import org.apache.commons.chain.web.MapEntry;

/**
 * Private implementation of {@code Map} for portlet request
 * attributes.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
final class PortletRequestScopeMap implements Map<String, Object> {

    private final PortletRequest request;

    public PortletRequestScopeMap(PortletRequest request) {
        this.request = request;
    }

    @Override
    public void clear() {
        Enumeration<?> keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            request.removeAttribute(keys.nextElement().toString());
        }
    }

    @Override
    public boolean containsKey(Object key) {
        return request.getAttribute(key(key)) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            return false;
        }
        Enumeration<?> keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            Object next = request.getAttribute(keys.nextElement().toString());
            if (value.equals(next)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        Set<Map.Entry<String, Object>> set = new HashSet<>();
        Enumeration<?> keys = request.getAttributeNames();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement().toString();
            set.add(new MapEntry<Object>(key, request.getAttribute(key), true));
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        return request.equals(o);
    }

    @Override
    public Object get(Object key) {
        return request.getAttribute(key(key));
    }

    @Override
    public int hashCode() {
        return request.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return !request.getAttributeNames().hasMoreElements();
    }

    @Override
    public Set<String> keySet() {
        Set<String> set = new HashSet<>();
        Enumeration<?> keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement().toString());
        }
        return set;
    }

    @Override
    public Object put(String key, Object value) {
        if (value == null) {
            return remove(key);
        }
        Object previous = request.getAttribute(key);
        request.setAttribute(key, value);
        return previous;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> map) {
        map.forEach(this::put);
    }

    @Override
    public Object remove(Object key) {
        String skey = key(key);
        Object previous = request.getAttribute(skey);
        request.removeAttribute(skey);
        return previous;
    }

    @Override
    public int size() {
        int n = 0;
        Enumeration<?> keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return n;
    }

    @Override
    public Collection<Object> values() {
        List<Object> list = new ArrayList<>();
        Enumeration<?> keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            list.add(request.getAttribute(keys.nextElement().toString()));
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
}