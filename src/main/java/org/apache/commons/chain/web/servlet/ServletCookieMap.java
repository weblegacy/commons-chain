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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import org.apache.commons.chain.web.MapEntry;

/**
 * Private implementation of {@code Map} for servlet cookies.
 *
 * @version $Revision$ $Date$
 * @since Chain 1.1
 */
final class ServletCookieMap implements Map {

    public ServletCookieMap(HttpServletRequest request) {
        this.request = request;
    }

    private HttpServletRequest request = null;

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Set entrySet() {
        Set set = new HashSet();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                set.add(new MapEntry(cookies[i].getName(), cookies[i], false));
            }
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        return request.equals(o);
    }

    @Override
    public Object get(Object key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(key(key))) {
                    return cookies[i];
                }
            }
        }
        return null;
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
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                set.add(cookies[i].getName());
            }
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
        Cookie[] cookies = request.getCookies();
        return cookies == null ?  0 : cookies.length;
    }

    @Override
    public Collection values() {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                list.add(cookies[i]);
            }
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