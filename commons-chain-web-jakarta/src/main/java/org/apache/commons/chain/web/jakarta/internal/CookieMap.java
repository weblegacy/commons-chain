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
package org.apache.commons.chain.web.jakarta.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.chain.web.MapEntry;
import org.apache.commons.chain.web.ParameterMap;

import jakarta.servlet.http.Cookie;

/**
 * Implementation of {@code Map} for cookies with
 * a parameter-provider.
 *
 * @param <P> the type of the parameter-provider
 *
 * @author Graff Stefan
 * @since Chain 1.3
 */
public class CookieMap<P> extends ParameterMap<P, Cookie> {

    /**
     * Supplier to return the {@link Cookie}-Array in this object.
     */
    private final Supplier<Cookie[]> cookiesSupplier;

    /**
     * The constructor for the {@code Map} for cookies.
     *
     * @param request         the request with the cookies
     * @param cookiesSupplier Supplier to return the {@link Cookie}-Array in this
     *                        object
     */
    public CookieMap(final P request, final Supplier<Cookie[]> cookiesSupplier) {
        super(request, null, null);
        this.cookiesSupplier = cookiesSupplier;
    }

    /**
     * Returns {@code true} if this cookie-map contains a mapping
     * for the specified cookie-name.
     *
     * @param key The key whose presence in this cookie-map is to
     *            be tested
     *
     * @return {@code true} if this cookie-map contains a mapping
     *         for the specified cookie-name.
     */
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    /**
     * Returns {@code true} if this cookie-map maps one or more keys
     * to the specified cookie.
     *
     * @param value cookie whose presence in this cookie-map is to be
     *        tested
     *
     * @return {@code true} if this cookie-map maps one or more keys
     *         to the specified cookie
     */
    @Override
    public boolean containsValue(Object value) {
        for (Cookie cookie : values()) {
            if (cookie.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this
     * cookie-map. The set is not backed by the cookie-map, so
     * changes to the cookie-map are not reflected in the set,
     * and vice-versa.
     *
     * @return a set view of the mappings contained in this cookie-map
     */
    @Override
    public Set<Map.Entry<String, Cookie>> entrySet() {
        final Set<Map.Entry<String, Cookie>> set = new HashSet<>();
        for (Cookie cookie : values()) {
            set.add(new MapEntry<>(cookie.getName(), cookie, false));
        }
        return set;
    }

    /**
     * Returns the cookie to which the specified cookie-name is mapped,
     * or {@code null} if this cookie-map contains no mapping for the
     * cookie-name.
     *
     * @param key the cookie-name whose associated value is to be
     *            returned
     *
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this cookie-map contains no mapping for
     *         the cookie-name
     *
     * @see #put(Object, Object)
     */
    @Override
    public Cookie get(Object key) {
        final Collection<Cookie> cookies = values();
        if (!cookies.isEmpty()) {
            final String skey = key(key);
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(skey)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * Returns {@code true} if this cookie-map contains no cookies.
     *
     * @return {@code true} if this cookie-map contains no cookies
     */
    @Override
    public boolean isEmpty() {
        final Cookie[] cookies = cookiesSupplier.get();
        return cookies == null || cookies.length == 0;
    }

    /**
     * Returns a {@link Set} view of the cookies contained in this
     * cookie-map. The set is not backed by the cookie-map, so
     * changes to the cookie-map are not reflected in the set, and
     * vice-versa.
     *
     * @return a set view of the cookies contained in this cookie-map
     */
    @Override
    public Set<String> keySet() {
        final Collection<Cookie> cookies = values();
        final Set<String> set = new HashSet<String>(Math.max((int) (cookies.size() / .75f) + 1, 16));
        for (Cookie cookie : cookies) {
            set.add(cookie.getName());
        }
        return set;
    }

    /**
     * Returns the number of cookies in this cookie-map.
     *
     * @return the number of cookies in this cookie-map
     */
    @Override
    public int size() {
        final Cookie[] cookies = cookiesSupplier.get();
        return cookies == null ? 0 : cookies.length;
    }

    /**
     * Returns a {@link Collection} view of the cookies contained in
     * this cookie-map. The collection is not backed by the
     * cookie-map, so changes to the cookie-map are not
     * reflected in the collection, and vice-versa.
     *
     * @return a view of the cookies contained in this cookie-map
     */
    @Override
    public Collection<Cookie> values() {
        final Cookie[] cookies = cookiesSupplier.get();
        return cookies == null ? Collections.emptyList() : Arrays.asList(cookies);
    }

    /**
     * Returns the hash code value for this cookie-map. The
     * hash code of a cookie-map is defined to be the sum of
     * the hash codes of each entry in the cookie-map's
     * {@code entrySet()} view. This ensures that {@code m1.equals(m2)}
     * implies that {@code m1.hashCode()==m2.hashCode()} for any two
     * parameter-maps {@code m1} and {@code m2}, as required by the
     * general contract of {@link Object#hashCode}.
     *
     * @implSpec
     * This implementation calls the {@code hashCode()} from the
     * parameter-map.
     *
     * @return the hash code value for this cookie-map
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Compares the specified object with this cookie-map for equality.
     * Returns {@code true} if the given object is also a cookie-map
     * and the two cookie-maps represent the same mappings. More formally,
     * two cookie-maps {@code m1} and {@code m2} represent the same
     * mappings if {@code m1.entrySet().equals(m2.entrySet())}.
     *
     * @param obj object to be compared for equality with this
     *        cookie-map
     *
     * @return {@code true} if the specified object is equal to this
     *         cookie-map
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}