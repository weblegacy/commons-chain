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
package org.apache.commons.chain.internal;

import java.lang.reflect.Array;

public final class Utilities {

    private Utilities() {
    }

    /**
     * Creates a new array with the specified component type and
     * length.
     * Invoking this method is equivalent to creating an array
     * as follows:
     * <blockquote>
     * <pre>
     * int[] x = {length};
     * Utilities.newArray(componentType, length);
     * </pre>
     * </blockquote>
     *
     * <p>The number of dimensions of the new array must not
     * exceed 255.
     *
     * @param <T> the component-type of the new array
     * @param componentType the {@code Class} object representing the
     * component type of the new array
     * @param length the length of the new array
     *
     * @return the new array
     *
     * @exception NullPointerException if the specified
     * {@code componentType} parameter is null
     * @exception IllegalArgumentException if componentType is {@link
     * Void#TYPE} or if the number of dimensions of the requested array
     * instance exceed 255.
     * @exception NegativeArraySizeException if the specified {@code length}
     * is negative
     */
    public static <T> T[] newArray(final Class<?> componentType, final int length) {
        @SuppressWarnings("unchecked")
        final T[] ret = (T[]) Array.newInstance(componentType, length);
        return ret;
    }
}
