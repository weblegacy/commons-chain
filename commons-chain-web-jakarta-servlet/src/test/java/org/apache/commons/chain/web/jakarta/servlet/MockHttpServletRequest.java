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
package org.apache.commons.chain.web.jakarta.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.chain.web.MockEnumeration;
import org.apache.commons.chain.web.MockPrincipal;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.Part;

/**
 * Mock Object for {@code HttpServletRequest} (Version 2.3)
 */
public class MockHttpServletRequest implements HttpServletRequest {
    public MockHttpServletRequest() {
    }

    public MockHttpServletRequest(HttpSession session) {
        setHttpSession(session);
    }

    public MockHttpServletRequest(String contextPath, String servletPath,
                                  String pathInfo, String queryString) {
        setPathElements(contextPath, servletPath, pathInfo, queryString);
    }

    public MockHttpServletRequest(String contextPath, String servletPath,
                                  String pathInfo, String queryString,
                                  HttpSession session) {
        setPathElements(contextPath, servletPath, pathInfo, queryString);
        setHttpSession(session);
    }

    protected Map<String, Object> attributes = new HashMap<>();
    protected String contextPath = null;
    protected Map<String, String[]> headers = new HashMap<>();
    protected Cookie[] cookies = new Cookie[0];
    protected Locale locale = null;
    protected Map<String, String[]> parameters = new HashMap<>();
    protected String pathInfo = null;
    protected Principal principal = null;
    protected String queryString = null;
    protected String servletPath = null;
    protected HttpSession session = null;

    // --------------------------------------------------------- Public Methods

    public void addHeader(String name, String value) {
        String values[] = headers.get(name);
        if (values == null) {
            String results[] = new String[] { value };
            headers.put(name, results);
            return;
        }
        String results[] = new String[values.length + 1];
        System.arraycopy(values, 0, results, 0, values.length);
        results[values.length] = value;
        headers.put(name, results);
    }

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

    public void addCookie(String name, String value) {
        addCookie(new Cookie(name, value));
    }

    public void addCookie(Cookie cookie) {
        Cookie[] newValues = new Cookie[cookies.length + 1];
        System.arraycopy(cookies, 0, newValues, 0, cookies.length);
        cookies = newValues;
        cookies[cookies.length - 1] = cookie;
    }

    public void setHttpSession(HttpSession session) {
        this.session = session;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setPathElements(String contextPath, String servletPath,
                                String pathInfo, String queryString) {

        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.pathInfo = pathInfo;
        this.queryString = queryString;
    }

    public void setUserPrincipal(Principal principal) {
        this.principal = principal;
    }

    // --------------------------------------------- HttpServletRequest Methods

    @Override
    public String getAuthType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public Cookie[] getCookies() {
        return cookies;
    }

    @Override
    public long getDateHeader(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getHeader(String name) {
        String values[] = headers.get(name);
        if (values != null) {
            return values[0];
        } else {
            return null;
        }
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return new MockEnumeration<>(headers.keySet().iterator());
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        String headers[] = this.headers.get(name);
        if (headers == null) {
            headers = new String[0];
        }
        return new MockEnumeration<>(Arrays.asList(headers).iterator());
    }

    @Override
    public int getIntHeader(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMethod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    @Override
    public String getPathTranslated() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getQueryString() {
        return queryString;
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
    public String getRequestURI() {
        StringBuffer sb = new StringBuffer();
        if (contextPath != null) {
            sb.append(contextPath);
        }
        if (servletPath != null) {
            sb.append(servletPath);
        }
        if (pathInfo != null) {
            sb.append(pathInfo);
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (create && (session == null)) {
            throw new UnsupportedOperationException();
        }
        return session;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUserInRole(String role) {
        if (principal instanceof MockPrincipal) {
            return ((MockPrincipal) principal).isUserInRole(role);
        } else {
            return false;
        }
    }

    // ------------------------------------------------- ServletRequest Methods

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return new MockEnumeration<>(attributes.keySet().iterator());
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getContentLength() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServletInputStream getInputStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        throw new UnsupportedOperationException();
    }

    public String getLocalAddr() {
        throw new UnsupportedOperationException();
    }

    public String getLocalName() {
        throw new UnsupportedOperationException();
    }

    public int getLocalPort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getParameter(String name) {
        String values[] = parameters.get(name);
        if (values != null) {
            return values[0];
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameters;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new MockEnumeration<>(parameters.keySet().iterator());
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameters.get(name);
    }

    @Override
    public String getProtocol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BufferedReader getReader() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRemoteAddr() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRemoteHost() {
        throw new UnsupportedOperationException();
    }

    public int getRemotePort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getScheme() {
        return "http";
    }

    @Override
    public String getServerName() {
        return "localhost";
    }

    @Override
    public int getServerPort() {
        return 8080;
    }

    @Override
    public boolean isSecure() {
        return false;
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
    public void setCharacterEncoding(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getContentLengthLong() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServletContext getServletContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
            throws IllegalStateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAsyncStarted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAsyncSupported() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AsyncContext getAsyncContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DispatcherType getDispatcherType() {
        throw new UnsupportedOperationException();
    }

// ***************************************************************************
//    Only from JEE10
//
//    @Override
//    public String getRequestId() {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public String getProtocolRequestId() {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public ServletConnection getServletConnection() {
//        throw new UnsupportedOperationException();
//    }
//
// ***************************************************************************

    @Override
    public String changeSessionId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void login(String username, String password) throws ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void logout() throws ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }
}