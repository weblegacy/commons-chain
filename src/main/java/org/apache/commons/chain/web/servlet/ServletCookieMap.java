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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain.web.MapEntry;

/**
 * Private implementation of {@code Map} for servlet cookies.
 *
 * @version $Revision$ $Date$
 * @since Chain 1.1
 */
final class ServletCookieMap implements Map<String, Cookie> {

    private final HttpServletRequest request;

    public ServletCookieMap(HttpServletRequest request) {
        this.request = request;
    }

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
            for (Cookie cookie : cookies) {
                if (cookie.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Set<Map.Entry<String, Cookie>> entrySet() {
        Set<Map.Entry<String, Cookie>> set = new HashSet<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                set.add(new MapEntry<Cookie>(cookie.getName(), cookie, false));
            }
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        return request.equals(o);
    }

    @Override
    public Cookie get(Object key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String skey = key(key);
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(skey)) {
                    return cookie;
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
    public Set<String> keySet() {
        Set<String> set = new HashSet<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                set.add(cookie.getName());
            }
        }
        return set;
    }

    @Override
    public Cookie put(String key, Cookie value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends String, ? extends Cookie> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        Cookie[] cookies = request.getCookies();
        return cookies == null ?  0 : cookies.length;
    }

    @Override
    public Collection<Cookie> values() {
        Cookie[] cookies = request.getCookies();
        return Arrays.asList(cookies);
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