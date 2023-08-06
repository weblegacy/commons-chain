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
package org.apache.commons.chain.web.internal;

import java.util.Objects;

/**
 * Represents a function that accepts one argument and produces a result.
 * With this functional interface it is possible that an exception is thrown.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the exception to the operation
 *
 * @since Chain 1.8
 */
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Throwable> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     *
     * @return the function result
     *
     * @throws E the possible exception
     */
    R apply(T t) throws E;

    /**
     * Returns a composed {@code CheckedFunction} that first applies
     * the {@code before} function to its input, and then applies this
     * function to the result. If evaluation of either function throws an
     * exception, it is relayed to the caller of the composed function.
     *
     * @param <V> the type of input to the {@code before} function, and to the
     *           composed function
     * @param before the function to apply before this function is applied
     *
     * @return a composed function that first applies the {@code before}
     * function and then applies this function
     *
     * @throws NullPointerException if before is null
     *
     * @see #andThen(Function)
     */
    default <V> CheckedFunction<V, R, E> compose(CheckedFunction<? super V, ? extends T, E> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * Returns a composed {@code CheckedFunction} that first applies
     * this function to its input, and then applies the {@code after}
     * function to the result. If evaluation of either function throws
     * an exception, it is relayed to the caller of the composed function.
     *
     * @param <V> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     *
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     *
     * @throws NullPointerException if after is null
     *
     * @see #compose(Function)
     */
    default <V> CheckedFunction<T, V, E> andThen(CheckedFunction<? super R, ? extends V, E> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @param <E> the type of the exception of the input and output objects
     *            to the function
     *
     * @return a function that always returns its input argument
     */
    static <T, E extends Throwable> CheckedFunction<T, T, E> identity() {
        return t -> t;
    }
}
