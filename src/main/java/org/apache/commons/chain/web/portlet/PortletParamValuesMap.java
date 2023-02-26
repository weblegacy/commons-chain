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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.portlet.PortletRequest;
import org.apache.commons.chain.web.MapEntry;

/**
 * Private implementation of {@code Map} for portlet parameter
 * name-values[].
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
final class PortletParamValuesMap implements Map {

    public PortletParamValuesMap(PortletRequest request) {
        this.request = request;
    }

    private PortletRequest request = null;

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {
        return request.getParameter(key(key)) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Iterator values = values().iterator();
        while (values.hasNext()) {
            if (value.equals(values.next())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set entrySet() {
        Set set = new HashSet();
        Enumeration keys = request.getParameterNames();
        String key;
        while (keys.hasMoreElements()) {
            key = (String) keys.nextElement();
            set.add(new MapEntry(key, request.getParameterValues(key), false));
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        return request.equals(o);
    }

    @Override
    public Object get(Object key) {
        return request.getParameterValues(key(key));
    }

    @Override
    public int hashCode() {
        return request.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return size() < 1;
    }

    @Override
    public Set keySet() {
        Set set = new HashSet();
        Enumeration keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return set;
    }

    @Override
    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        int n = 0;
        Enumeration keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return n;
    }

    @Override
    public Collection values() {
        List list = new ArrayList();
        Enumeration keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            list.add(request.getParameterValues((String) keys.nextElement()));
        }
        return list;
    }

    private String key(Object key) {
        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key instanceof String) {
            return (String) key;
        } else {
            return key.toString();
        }
    }
}