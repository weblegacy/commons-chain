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

import java.util.Enumeration;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Implementation of {@code Map} for immutable parameter name-values with
 * a parameter-provider.
 *
 * @param <P> the type of the parameter-provider
 *
 * @author Graff Stefan
 * @since Chain 1.3
 */
public class ParamValuesMap<P> extends ParameterMap<P, String[]> {

    /**
     * The constructor for an immutable parameter-map.
     *
     * @param parameter the parameter-provider
     * @param valueFunction Function to return the value of a parameter
     * @param namesSupplier Supplier to return all names of the parameter
     */
    public ParamValuesMap(final P parameter, final Function<String, String[]> valueFunction,
            final Supplier<Enumeration<String>> namesSupplier) {

        super(parameter, valueFunction, namesSupplier);
    }

    /**
     * Returns {@code true} if this parameter-map maps one or more keys
     * to the specified value.
     *
     * @param value value whose presence in this parameter-map is to be
     *        tested
     *
     * @return {@code true} if this parameter-map maps one or more keys
     *         to the specified value
     */
    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String[])) {
            return false;
        }

        final Enumeration<String> keys = getNamesSupplier().get();
        while (keys.hasMoreElements()) {
            final String[] next = getValueFunction().apply(keys.nextElement());
            if (Objects.deepEquals(value, next)) {
                return true;
            }
        }
        return false;
    }
}