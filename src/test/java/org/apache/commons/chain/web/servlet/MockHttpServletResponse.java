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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Mock Object for {@code HttpServletResponse} (Version 2.3)
 */
public class MockHttpServletResponse implements HttpServletResponse {

    // ------------------------------------------------------ Instance Variables

    private Locale locale = null;

    // ---------------------------------------------------------- Public Methods

    // --------------------------------------------- HttpServletResponse Methods

    @Override
    public void addCookie(Cookie cookie) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addDateHeader(String name, long value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addHeader(String name, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addIntHeader(String name, int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsHeader(String name) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public String encodeRedirectUrl(String url) {
        return encodeRedirectURL(url);
    }

    @Override
    public String encodeRedirectURL(String url) {
        return url;
    }

    @Deprecated
    @Override
    public String encodeUrl(String url) {
        return encodeURL(url);
    }

    @Override
    public String encodeURL(String url) {
        return url;
    }

    @Override
    public void sendError(int status) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendError(int status, String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendRedirect(String location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDateHeader(String name, long value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHeader(String name, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIntHeader(String name, int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatus(int status) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public void setStatus(int status, String message) {
        throw new UnsupportedOperationException();
    }

    // ------------------------------------------------- ServletResponse Methods

    @Override
    public void flushBuffer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBufferSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException();
    }

    public String getContentType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCommitted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetBuffer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBufferSize(int size) {
        throw new UnsupportedOperationException();
    }

    public void setCharacterEncoding(String encoding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContentLength(int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContentType(String type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}