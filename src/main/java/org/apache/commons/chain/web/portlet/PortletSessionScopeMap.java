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
import javax.portlet.PortletSession;

import org.apache.commons.chain.web.MapEntry;

/**
 * Private implementation of {@code Map} for portlet session
 * attributes.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
final class PortletSessionScopeMap implements Map<String, Object> {

    private PortletSession session = null;
    private PortletRequest request = null;

    public PortletSessionScopeMap(PortletRequest request) {
        this.request = request;
        sessionExists();
    }

    @Override
    public void clear() {
        if (sessionExists()) {
            Enumeration<?> keys =
                    session.getAttributeNames(PortletSession.PORTLET_SCOPE);
            while (keys.hasMoreElements()) {
                session.removeAttribute(keys.nextElement().toString());
            }
        }
    }

    @Override
    public boolean containsKey(Object key) {
        if (sessionExists()) {
            return session.getAttribute(key(key)) != null;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null || !sessionExists()) {
            return (false);
        }
        Enumeration<?> keys =
            session.getAttributeNames(PortletSession.PORTLET_SCOPE);
        while (keys.hasMoreElements()) {
            Object next = session.getAttribute(keys.nextElement().toString());
            if (value.equals(next)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        Set<Map.Entry<String, Object>> set = new HashSet<>();
        if (sessionExists()) {
            Enumeration<?> keys =
                session.getAttributeNames(PortletSession.PORTLET_SCOPE);
            String key;
            while (keys.hasMoreElements()) {
                key = keys.nextElement().toString();
                set.add(new MapEntry<Object>(key, session.getAttribute(key), true));
            }
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        if (sessionExists()) {
            return session.equals(o);
        } else {
            return false;
        }
    }

    @Override
    public Object get(Object key) {
        if (sessionExists()) {
            return session.getAttribute(key(key));
        } else {
            return null;
        }
    }

    @Override
    public int hashCode() {
        if (sessionExists()) {
            return (session.hashCode());
        } else {
            return 0;
        }
    }

    @Override
    public boolean isEmpty() {
        return !(sessionExists() && session.getAttributeNames().hasMoreElements());
    }

    @Override
    public Set<String> keySet() {
        Set<String> set = new HashSet<>();
        if (sessionExists()) {
            Enumeration<?> keys =
                session.getAttributeNames(PortletSession.PORTLET_SCOPE);
            while (keys.hasMoreElements()) {
                set.add(keys.nextElement().toString());
            }
        }
        return set;
    }

    @Override
    public Object put(String key, Object value) {
        if (value == null) {
            return remove(key);
        }

        // Ensure the Session is created, if it
        // doesn't exist
        if (session == null) {
            session = request.getPortletSession();
            request = null;
        }

        Object previous = session.getAttribute(key);
        session.setAttribute(key, value);
        return previous;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> map) {
        map.forEach(this::put);
    }

    @Override
    public Object remove(Object key) {
        if (sessionExists()) {
            String skey = key(key);
            Object previous = session.getAttribute(skey);
            session.removeAttribute(skey);
            return previous;
        } else {
            return null;
        }
    }

    @Override
    public int size() {
        int n = 0;
        if (sessionExists()) {
            Enumeration<?> keys =
                session.getAttributeNames(PortletSession.PORTLET_SCOPE);
            while (keys.hasMoreElements()) {
                keys.nextElement();
                n++;
            }
        }
        return n;
    }

    @Override
    public Collection<Object> values() {
        List<Object> list = new ArrayList<>();
        if (sessionExists()) {
            Enumeration<?> keys =
                session.getAttributeNames(PortletSession.PORTLET_SCOPE);
            while (keys.hasMoreElements()) {
                list.add(session.getAttribute(keys.nextElement().toString()));
            }
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

    private boolean sessionExists() {
        if (session == null) {
            session = request.getPortletSession(false);
            if (session != null) {
                request = null;
            }
        }
        return session != null;
    }
}