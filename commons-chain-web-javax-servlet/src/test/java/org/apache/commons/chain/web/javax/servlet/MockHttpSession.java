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
package org.apache.commons.chain.web.javax.servlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.chain.web.MockEnumeration;

/**
 * Mock Object for {@code HttpSession} (Version 2.3)
 */
public class MockHttpSession implements HttpSession {
    public MockHttpSession() {
    }

    public MockHttpSession(ServletContext servletContext) {
        setServletContext(servletContext);
    }

    protected Map<String, Object> attributes = new HashMap<>();
    protected ServletContext servletContext = null;

    // --------------------------------------------------------- Public Methods

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    // ---------------------------------------------------- HttpSession Methods

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return new MockEnumeration<>(attributes.keySet().iterator());
    }

    @Override
    public long getCreationTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLastAccessedTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxInactiveInterval() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    @SuppressWarnings("deprecation")
    public javax.servlet.http.HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public Object getValue(String name) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public String[] getValueNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNew() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public void putValue(String name, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Deprecated
    @Override
    public void removeValue(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (value == null) {
            attributes.remove(name);
        } else {
            attributes.put(name, value);
        }
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        throw new UnsupportedOperationException();
    }
}