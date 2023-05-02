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

import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderParameters;
import javax.portlet.WindowState;
import javax.servlet.http.Cookie;

import org.apache.commons.chain.web.MockEnumeration;
import org.apache.commons.chain.web.MockPrincipal;

/**
 * Mock Object for {@code PortletRequest}
 */
public class MockPortletRequest implements PortletRequest {
    private String contextPath;
    private String authType;
    private Locale locale;
    private String scheme     = "http";
    private String serverName = "localhost";
    private int    serverPort = 8080;
    private PortalContext portalContext;
    private PortletContext context;
    private PortletSession session;
    private PortletMode portletMode;
    private PortletPreferences portletPreferences;
    private WindowState windowState;
    private Principal principal;
    private Map<String, String[]> parameters = new HashMap<>();
    private Map<String, Object> attributes = new HashMap<>();
    private Map<String, String[]> properties = new HashMap<>();
    private Cookie[] cookies = new Cookie[0];

    public MockPortletRequest() {
        this(null, null, null);
    }

    public MockPortletRequest(String contextPath, PortletContext context, PortletSession session) {
        this.contextPath = contextPath;
        this.context = context == null ? new MockPortletContext() : context;
        this.session = session;
    }

    // --------------------------------------------------------- Public Methods

    public void addParameter(String name, String value) {
        String values[] = parameters.get(name);
        if (values == null) {
            String results[] = new String[] { value };
            parameters.put(name, results);
            return;
        }
        String results[] = new String[values.length + 1];
        System.arraycopy(values, 0, results, 0, values.length);
        results[values.length] = value;
        parameters.put(name, results);
    }

    public void addProperty(String name, String value) {
        String values[] = properties.get(name);
        if (values == null) {
            String results[] = new String[] { value };
            properties.put(name, results);
            return;
        }
        String results[] = new String[values.length + 1];
        System.arraycopy(values, 0, results, 0, values.length);
        results[values.length] = value;
        properties.put(name, results);
    }

    public void addCookie(String name, String value) {
        addCookie(new Cookie(name, value));
    }

    public void addCookie(Cookie cookie) {
        Cookie[] newValues = new Cookie[cookies.length + 1];
        System.arraycopy(cookies, 0, newValues, 0, cookies.length);
        cookies = newValues;
        cookies[cookies.length - 1] = cookie;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setPortalContext(PortalContext portalContext) {
        this.portalContext = portalContext;
    }

    public void setPortletContext(PortletContext context) {
        this.context = context;
    }

    public void setPortletMode(PortletMode portletMode) {
        this.portletMode = portletMode;
    }

    public void setPortletPreferences(PortletPreferences portletPreferences) {
        this.portletPreferences = portletPreferences;
    }

    public void setPortletSession(PortletSession session) {
        this.session = session;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setUserPrincipal(Principal principal) {
        this.principal = principal;
    }

    public void setUserPrincipal(WindowState windowState) {
        this.windowState = windowState;
    }

    // --------------------------------------------- PortletRequest Methods

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return new MockEnumeration<>(attributes.keySet().iterator());
    }

    @Override
    public String getAuthType() {
        return authType;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public String getParameter(String name) {
        String values[] = parameters.get(name);
        if (values != null) {
            return values[0];
        } else {
            return null;
        }
    }

    @Override
    @Deprecated
    public Map<String, String[]> getParameterMap() {
        return parameters;
    }

    @Override
    @Deprecated
    public Enumeration<String> getParameterNames() {
        return new MockEnumeration<>(parameters.keySet().iterator());
    }

    @Override
    @Deprecated
    public String[] getParameterValues(String name) {
        return parameters.get(name);
    }

    @Override
    public PortalContext getPortalContext() {
        return portalContext;
    }

    @Override
    public PortletMode getPortletMode() {
        return portletMode;
    }

    @Override
    public PortletSession getPortletSession() {
        return getPortletSession(true);
    }

    @Override
    public PortletSession getPortletSession(boolean create) {
        if (create && session == null) {
            session = new MockPortletSession(context);
        }
        return session;
    }

    @Override
    public PortletPreferences getPreferences() {
        return portletPreferences;
    }

    @Override
    public Enumeration<String> getProperties(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProperty(String name) {
        String values[] = properties.get(name);
        if (values != null) {
            return values[0];
        } else {
            return null;
        }
     }

    @Override
    public Enumeration<String> getPropertyNames() {
        return new MockEnumeration<>(properties.keySet().iterator());
    }

    @Override
    public String getRemoteUser() {
        if (principal != null) {
            return principal.getName();
        } else {
            return null;
        }
    }

    @Override
    public String getRequestedSessionId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getResponseContentType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Enumeration<String> getResponseContentTypes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public WindowState getWindowState() {
        return windowState;
    }

    @Override
    public boolean isPortletModeAllowed(PortletMode mode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public boolean isUserInRole(String role) {
        if ((principal != null) && (principal instanceof MockPrincipal)) {
            return ((MockPrincipal)principal).isUserInRole(role);
        } else {
            return false;
        }
    }

    @Override
    public boolean isWindowStateAllowed(WindowState state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
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
    public RenderParameters getRenderParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PortletContext getPortletContext() {
        return context;
    }

    @Override
    public String getWindowID() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie[] getCookies() {
        return cookies;
    }

    @Override
    @Deprecated
    public Map<String, String[]> getPrivateParameterMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public Map<String, String[]> getPublicParameterMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getUserAgent() {
        throw new UnsupportedOperationException();
    }
}