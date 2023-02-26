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
package org.apache.commons.chain.impl;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;
import org.apache.commons.chain.Context;

/**
 * Convenience base class for {@link Context} implementations.
 *
 * <p>In addition to the minimal functionality required by the {@link Context}
 * interface, this class implements the recommended support for
 * <em>Attribute-Property Transparency</em>. This is implemented by
 * analyzing the available JavaBeans properties of this class (or its
 * subclass), exposes them as key-value pairs in the {@code Map},
 * with the key being the name of the property itself.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - Because {@code empty} is a
 * read-only property defined by the {@code Map} interface, it may not
 * be utilized as an attribute key or property name.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class ContextBase extends HashMap implements Context {

    // ------------------------------------------------------------ Constructors

    /**
     * Default, no argument constructor.
     */
    public ContextBase() {
        super();
        initialize();
    }

    /**
     * Initialize the contents of this {@link Context} by copying the
     * values from the specified {@code Map}. Any keys in {@code map}
     * that correspond to local properties will cause the setter method for
     * that property to be called.
     *
     * @param map Map whose key-value pairs are added
     *
     * @throws IllegalArgumentException if an exception is thrown
     *         writing a local property value.
     * @throws UnsupportedOperationException if a local property does not
     *         have a write method.
     */
    public ContextBase(Map map) {
        super(map);
        initialize();
        putAll(map);
    }

    // ------------------------------------------------------ Instance Variables

    // NOTE - PropertyDescriptor instances are not Serializable, so the
    // following variables must be declared as transient. When a ContextBase
    // instance is deserialized, the no-arguments constructor is called,
    // and the initialize() method called there will repopulate them.
    // Therefore, no special restoration activity is required.

    /**
     * The {@code PropertyDescriptor}s for all JavaBeans properties
     * of this {@link Context} implementation class, keyed by property name.
     * This collection is allocated only if there are any JavaBeans
     * properties.
     */
    private transient Map descriptors = null;

    /**
     * The same {@code PropertyDescriptor}s as an array.
     */
    private transient PropertyDescriptor[] pd = null;

    /**
     * Distinguished singleton value that is stored in the map for each
     * key that is actually a property. This value is used to ensure that
     * {@code equals()} comparisons will always fail.
     */
    private static Object singleton;

    static {
        singleton = new Serializable() {
                public boolean equals(Object object) {
                    return false;
                }
            };
    }

    /**
     * Zero-length array of parameter values for calling property getters.
     */
    private static Object[] zeroParams = new Object[0];

    // ------------------------------------------------------------- Map Methods

    /**
     * Override the default {@code Map} behavior to clear all keys and
     * values except those corresponding to JavaBeans properties.
     */
    @Override
    public void clear() {
        if (descriptors == null) {
            super.clear();
        } else {
            Iterator keys = keySet().iterator();
            while (keys.hasNext()) {
                Object key = keys.next();
                if (!descriptors.containsKey(key)) {
                    keys.remove();
                }
            }
        }
    }

    /**
     * Override the default {@code Map} behavior to return
     * {@code true} if the specified value is present in either the
     * underlying {@code Map} or one of the local property values.
     *
     * @param value the value look for in the context.
     *
     * @return {@code true} if found in this context otherwise
     *         {@code false}.
     *
     * @throws IllegalArgumentException if a property getter
     *         throws an exception
     */
    @Override
    public boolean containsValue(Object value) {

        // Case 1 -- no local properties
        if (descriptors == null) {
            return super.containsValue(value);
        }

        // Case 2 -- value found in the underlying Map
        else if (super.containsValue(value)) {
            return true;
        }

        // Case 3 -- check the values of our readable properties
        for (int i = 0; i < pd.length; i++) {
            if (pd[i].getReadMethod() != null) {
                Object prop = readProperty(pd[i]);
                if (value == null) {
                    if (prop == null) {
                        return true;
                    }
                } else if (value.equals(prop)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Override the default {@code Map} behavior to return a
     * {@code Set} that meets the specified default behavior except
     * for attempts to remove the key for a property of the {@link Context}
     * implementation class, which will throw
     * {@code UnsupportedOperationException}.
     *
     * @return Set of entries in the Context.
     */
    @Override
    public Set entrySet() {
        return new EntrySetImpl();
    }

    /**
     * Override the default {@code Map} behavior to return the value
     * of a local property if the specified key matches a local property name.
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - If the specified
     * {@code key} identifies a write-only property, {@code null}
     * will arbitrarily be returned, in order to avoid difficulties implementing
     * the contracts of the {@code Map} interface.</p>
     *
     * @param key Key of the value to be returned
     *
     * @return The value for the specified key.
     *
     * @throws IllegalArgumentException if an exception is thrown
     *         reading this local property value.
     * @throws UnsupportedOperationException if this local property does not
     *         have a read method.
     */
    @Override
    public Object get(Object key) {
        // Case 1 -- no local properties
        if (descriptors == null) {
            return super.get(key);
        }

        // Case 2 -- this is a local property
        if (key != null) {
            PropertyDescriptor descriptor =
                (PropertyDescriptor) descriptors.get(key);
            if (descriptor != null) {
                if (descriptor.getReadMethod() != null) {
                    return readProperty(descriptor);
                } else {
                    return null;
                }
            }
        }

        // Case 3 -- retrieve value from our underlying Map
        return super.get(key);
    }

    /**
     * Override the default {@code Map} behavior to return
     * {@code true} if the underlying {@code Map} only contains
     * key-value pairs for local properties (if any).
     *
     * @return {@code true} if this Context is empty, otherwise
     *         {@code false}.
     */
    @Override
    public boolean isEmpty() {
        // Case 1 -- no local properties
        if (descriptors == null) {
            return super.isEmpty();
        }

        // Case 2 -- compare key count to property count
        return super.size() <= descriptors.size();
    }

    /**
     * Override the default {@code Map} behavior to return a
     * {@code Set} that meets the specified default behavior except
     * for attempts to remove the key for a property of the {@link Context}
     * implementation class, which will throw
     * {@code UnsupportedOperationException}.
     *
     * @return The set of keys for objects in this Context.
     */
    @Override
    public Set keySet() {
        return super.keySet();
    }

    /**
     * Override the default {@code Map} behavior to set the value of a
     * local property if the specified key matches a local property name.
     *
     * @param key Key of the value to be stored or replaced
     * @param value New value to be stored
     *
     * @return The value added to the Context.
     *
     * @throws IllegalArgumentException if an exception is thrown
     *         reading or writing this local property value.
     * @throws UnsupportedOperationException if this local property does not
     *         have both a read method and a write method
     */
    @Override
    public Object put(Object key, Object value) {
        // Case 1 -- no local properties
        if (descriptors == null) {
            return super.put(key, value);
        }

        // Case 2 -- this is a local property
        if (key != null) {
            PropertyDescriptor descriptor =
                (PropertyDescriptor) descriptors.get(key);
            if (descriptor != null) {
                Object previous = null;
                if (descriptor.getReadMethod() != null) {
                    previous = readProperty(descriptor);
                }
                writeProperty(descriptor, value);
                return previous;
            }
        }

        // Case 3 -- store or replace value in our underlying map
        return super.put(key, value);
    }

    /**
     * Override the default {@code Map} behavior to call the
     * {@code put()} method individually for each key-value pair
     * in the specified {@code Map}.
     *
     * @param map {@code Map} containing key-value pairs to store
     *        (or replace)
     *
     * @throws IllegalArgumentException if an exception is thrown
     *         reading or writing a local property value.
     * @throws UnsupportedOperationException if a local property does not
     *         have both a read method and a write method
     */
    @Override
    public void putAll(Map map) {
        Iterator pairs = map.entrySet().iterator();
        while (pairs.hasNext()) {
            Map.Entry pair = (Map.Entry) pairs.next();
            put(pair.getKey(), pair.getValue());
        }
    }

    /**
     * Override the default {@code Map} behavior to throw
     * {@code UnsupportedOperationException} on any attempt to
     * remove a key that is the name of a local property.
     *
     * @param key Key to be removed
     *
     * @return The value removed from the Context.
     *
     * @throws UnsupportedOperationException if the specified
     *         {@code key} matches the name of a local property
     */
    @Override
    public Object remove(Object key) {
        // Case 1 -- no local properties
        if (descriptors == null) {
            return super.remove(key);
        }

        // Case 2 -- this is a local property
        if (key != null) {
            PropertyDescriptor descriptor =
                (PropertyDescriptor) descriptors.get(key);
            if (descriptor != null) {
                throw new UnsupportedOperationException
                    ("Local property '" + key + "' cannot be removed");
            }
        }

        // Case 3 -- remove from underlying Map
        return super.remove(key);
    }

    /**
     * Override the default {@code Map} behavior to return a
     * {@code Collection} that meets the specified default behavior except
     * for attempts to remove the key for a property of the {@link Context}
     * implementation class, which will throw
     * {@code UnsupportedOperationException}.
     *
     * @return The collection of values in this Context.
     */
    @Override
    public Collection values() {
        return new ValuesImpl();
    }

    // --------------------------------------------------------- Private Methods

    /**
     * Return an {@code Iterator} over the set of {@code Map.Entry}
     * objects representing our key-value pairs.
     */
    private Iterator entriesIterator() {
        return new EntrySetIterator();
    }

    /**
     * Return a {@code Map.Entry} for the specified key value, if it
     * is present; otherwise, return {@code null}.
     *
     * @param key Attribute key or property name
     */
    private Map.Entry entry(Object key) {
        if (containsKey(key)) {
            return new MapEntryImpl(key, get(key));
        } else {
            return null;
        }
    }

    /**
     * Customize the contents of our underlying {@code>Map} so that
     * it contains keys corresponding to all of the JavaBeans properties of
     * the {@link Context} implementation class.
     *
     * @throws IllegalArgumentException if an exception is thrown
     *         writing this local property value
     * @throws UnsupportedOperationException if this local property does not
     *         have a write method.
     */
    private void initialize() {
        // Retrieve the set of property descriptors for this Context class
        try {
            pd = Introspector.getBeanInfo(getClass())
                    .getPropertyDescriptors();
        } catch (IntrospectionException e) {
            pd = new PropertyDescriptor[0]; // Should never happen
        }

        // Initialize the underlying Map contents
        for (int i = 0; i < pd.length; i++) {
            String name = pd[i].getName();

            // Add descriptor (ignoring getClass() and isEmpty())
            if (!("class".equals(name) || "empty".equals(name))) {
                if (descriptors == null) {
                    descriptors = new HashMap(pd.length - 2);
                }
                descriptors.put(name, pd[i]);
                super.put(name, singleton);
            }
        }
    }

    /**
     * Get and return the value for the specified property.
     *
     * @param descriptor {@code PropertyDescriptor} for the
     *        specified property
     *
     * @return the value of the specified property
     * 
     * @throws IllegalArgumentException if an exception is thrown
     *         reading this local property value.
     * @throws UnsupportedOperationException if this local property does not
     *         have a read method.
     */
    private Object readProperty(PropertyDescriptor descriptor) {
        try {
            Method method = descriptor.getReadMethod();
            if (method == null) {
                throw new UnsupportedOperationException
                    ("Property '" + descriptor.getName()
                     + "' is not readable");
            }
            return method.invoke(this, zeroParams);
        } catch (Exception e) {
            throw new UnsupportedOperationException
                ("Exception reading property '" + descriptor.getName()
                 + "': " + e.getMessage());
        }
    }

    /**
     * Remove the specified key-value pair, if it exists, and return
     * {@code true}. If this pair does not exist, return {@code false}.
     *
     * @param entry Key-value pair to be removed
     *
     * @throws UnsupportedOperationException if the specified key
     *         identifies a property instead of an attribute.
     */
    private boolean remove(Map.Entry entry) {
        Map.Entry actual = entry(entry.getKey());
        if (actual == null) {
            return false;
        } else if (!entry.equals(actual)) {
            return false;
        } else {
            remove(entry.getKey());
            return true;
        }
    }

    /**
     * Return an {@code Iterator} over the set of values in this
     * {@code Map}.
     */
    private Iterator valuesIterator() {
        return new ValuesIterator();
    }

    /**
     * Set the value for the specified property.
     *
     * @param descriptor {@code PropertyDescriptor} for the
     *        specified property
     * @param value The new value for this property (must be of the
     *        correct type)
     *
     * @throws IllegalArgumentException if an exception is thrown
     *         writing this local property value.
     * @throws UnsupportedOperationException if this local property does not
     *         have a write method.
     */
    private void writeProperty(PropertyDescriptor descriptor, Object value) {
        try {
            Method method = descriptor.getWriteMethod();
            if (method == null) {
                throw new UnsupportedOperationException
                    ("Property '" + descriptor.getName()
                     + "' is not writeable");
            }
            method.invoke(this, new Object[] {value});
        } catch (Exception e) {
            throw new UnsupportedOperationException
                ("Exception writing property '" + descriptor.getName()
                 + "': " + e.getMessage());
        }
    }

    // --------------------------------------------------------- Private Classes

    /**
     * Private implementation of {@code Set} that implements the
     * semantics required for the value returned by {@code entrySet()}.
     */
    private class EntrySetImpl extends AbstractSet {

        @Override
        public void clear() {
            ContextBase.this.clear();
        }

        @Override
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Entry actual = ContextBase.this.entry(entry.getKey());
            if (actual != null) {
                return actual.equals(entry);
            } else {
                return false;
            }
        }

        @Override
        public boolean isEmpty() {
            return ContextBase.this.isEmpty();
        }

        @Override
        public Iterator iterator() {
            return ContextBase.this.entriesIterator();
        }

        @Override
        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry) {
                return ContextBase.this.remove((Map.Entry) obj);
            } else {
                return false;
            }
        }

        @Override
        public int size() {
            return ContextBase.this.size();
        }
    }

    /**
     * Private implementation of {@code Iterator} for the
     * {@code Set} returned by {@code entrySet()}.
     */
    private class EntrySetIterator implements Iterator {

        private Map.Entry entry = null;
        private Iterator keys = ContextBase.this.keySet().iterator();

        @Override
        public boolean hasNext() {
            return keys.hasNext();
        }

        @Override
        public Map.Entry next() {
            entry = ContextBase.this.entry(keys.next());
            return entry;
        }

        @Override
        public void remove() {
            ContextBase.this.remove(entry);
        }
    }

    /**
     * Private implementation of {@code Map.Entry} for each item in
     * {@code EntrySetImpl}.
     */
    private class MapEntryImpl implements Map.Entry {

        MapEntryImpl(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        private Object key;
        private Object value;

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            if (key == null) {
                return entry.getKey() == null;
            }
            if (key.equals(entry.getKey())) {
                if (value == null) {
                    return entry.getValue() == null;
                } else {
                    return value.equals(entry.getValue());
                }
            } else {
                return false;
            }
        }

        @Override
        public Object getKey() {
            return this.key;
        }

        @Override
        public Object getValue() {
            return this.value;
        }

        @Override
        public int hashCode() {
            return ((key == null ? 0 : key.hashCode())
                   ^ (value == null ? 0 : value.hashCode()));
        }

        @Override
        public Object setValue(Object value) {
            Object previous = this.value;
            ContextBase.this.put(this.key, value);
            this.value = value;
            return previous;
        }

        @Override
        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    /**
     * Private implementation of {@code Collection} that implements the
     * semantics required for the value returned by {@code values()}.
     */
    private class ValuesImpl extends AbstractCollection {

        @Override
        public void clear() {
            ContextBase.this.clear();
        }

        @Override
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return ContextBase.this.containsValue(entry.getValue());
        }

        @Override
        public boolean isEmpty() {
            return ContextBase.this.isEmpty();
        }

        @Override
        public Iterator iterator() {
            return ContextBase.this.valuesIterator();
        }

        @Override
        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry) {
                return ContextBase.this.remove((Map.Entry) obj);
            } else {
                return false;
            }
        }

        public int size() {
            return ContextBase.this.size();
        }
    }

    /**
     * Private implementation of {@code Iterator} for the
     * {@code Collection} returned by {@code values()}.
     */
    private class ValuesIterator implements Iterator {

        private Map.Entry entry = null;
        private Iterator keys = ContextBase.this.keySet().iterator();

        @Override
        public boolean hasNext() {
            return keys.hasNext();
        }

        @Override
        public Object next() {
            entry = ContextBase.this.entry(keys.next());
            return entry.getValue();
        }

        @Override
        public void remove() {
            ContextBase.this.remove(entry);
        }
    }
}