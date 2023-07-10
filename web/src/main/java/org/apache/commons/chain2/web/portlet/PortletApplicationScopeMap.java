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
package org.apache.commons.chain2.web.portlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.portlet.PortletContext;

import org.apache.commons.chain2.web.MapEntry;

/**
 * <p>Private implementation of <code>Map</code> for portlet context
 * attributes.</p>
 *
 */
final class PortletApplicationScopeMap implements Map<String, Object> {

    public PortletApplicationScopeMap(PortletContext context) {
        this.context = context;
    }

    private final PortletContext context;

    public void clear() {
        for (String key : keySet()) {
            context.removeAttribute(key);
        }
    }

    public boolean containsKey(Object key) {
        return (context.getAttribute(key(key)) != null);
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            return (false);
        }
        Enumeration<String> keys = context.getAttributeNames();
        while (keys.hasMoreElements()) {
            Object next = context.getAttribute(keys.nextElement());
            if (value.equals(next)) {
                return (true);
            }
        }
        return (false);
    }

    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> set = new HashSet<Entry<String, Object>>();
        Enumeration<String> keys = context.getAttributeNames();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            set.add(new MapEntry<String, Object>(key, context.getAttribute(key), true));
        }
        return (set);
    }

    @Override
    public boolean equals(Object o) {
        return (context.equals(o));
    }

    public Object get(Object key) {
        return (context.getAttribute(key(key)));
    }

    @Override
    public int hashCode() {
        return (context.hashCode());
    }

    public boolean isEmpty() {
        return (size() < 1);
    }

    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        Enumeration<String> keys = context.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return (set);
    }

    public Object put(String key, Object value) {
        if (value == null) {
            return (remove(key));
        }
        Object previous = context.getAttribute(key);
        context.setAttribute(key, value);
        return (previous);
    }

    public void putAll(Map<? extends String, ? extends Object> map) {
        for (Entry<? extends String, ? extends Object> entry : map.entrySet()) {
            put(key(entry.getKey()), entry.getValue());
        }
    }

    public Object remove(Object key) {
        String skey = key(key);
        Object previous = context.getAttribute(skey);
        context.removeAttribute(skey);
        return (previous);
    }

    public int size() {
        int n = 0;
        Enumeration<String> keys = context.getAttributeNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }

    public Collection<Object> values() {
        List<Object> list = new ArrayList<Object>();
        Enumeration<String> keys = context.getAttributeNames();
        while (keys.hasMoreElements()) {
            list.add(context.getAttribute(keys.nextElement()));
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
