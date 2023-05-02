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
package org.apache.commons.chain.web;

import java.security.Principal;

/**
 * Mock <strong>Principal</strong> object for low-level unit tests.
 */
public class MockPrincipal implements Principal {

    public MockPrincipal() {
        this.name = "";
        this.roles = new String[0];
    }

    public MockPrincipal(String name) {
        this.name = name;
        this.roles = new String[0];
    }

    public MockPrincipal(String name, String roles[]) {
        this.name = name;
        this.roles = roles;
    }

    protected String name = null;

    protected String roles[] = null;

    @Override
    public String getName() {
        return this.name;
    }

    public boolean isUserInRole(String role) {
        for (String role2 : roles) {
            if (role.equals(role2)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Principal)) {
            return false;
        }
        Principal p = (Principal) o;
        if (name == null) {
            return p.getName() == null;
        } else {
            return name.equals(p.getName());
        }
    }

    @Override
    public int hashCode() {
        if (name == null) {
            return "".hashCode();
        } else {
            return name.hashCode();
        }
    }
}