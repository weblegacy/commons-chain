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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;

import org.apache.commons.chain.web.MockEnumeration;

/**
 * Mock Object for {@code PortletContext}
 */
public class MockPortletContext implements PortletContext {
    private int majorVersion = 3;
    private int minorVersion = 1;
    private String portletContextName = "MockPortletContext";
    private String serverInfo = portletContextName;
    private Map<String, String> parameters = new HashMap<>();
    private Map<String, Object> attributes = new HashMap<>();

    // --------------------------------------------------------- Public Methods

    public void setPortletContextName(String portletContextName) {
        this.portletContextName = portletContextName;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    public void addInitParameter(String name, String value) {
        parameters.put(name, value);
    }

    // ------------------------------------------------- PortletContext Methods

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return new MockEnumeration<>(attributes.keySet().iterator());
    }

    @Override
    public String getInitParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return new MockEnumeration<>(parameters.keySet().iterator());
    }

    @Override
    public int getMajorVersion() {
        return majorVersion;
    }

    @Override
    public String getMimeType(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMinorVersion() {
        return minorVersion;
    }

    @Override
    public PortletRequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPortletContextName() {
        return portletContextName;
    }

    @Override
    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PortletRequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getServerInfo() {
        return serverInfo;
    }

    @Override
    public void log(String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void log(String message, Throwable exception) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public Enumeration<String> getContainerRuntimeOptions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getEffectiveMajorVersion() {
        return majorVersion;
    }

    @Override
    public int getEffectiveMinorVersion() {
        return minorVersion;
    }

    @Override
    public String getContextPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClassLoader getClassLoader() {
        return MockPortletContext.class.getClassLoader();
    }
}