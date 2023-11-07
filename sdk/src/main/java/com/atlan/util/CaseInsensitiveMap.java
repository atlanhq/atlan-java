/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.util;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A case-insensitive {@link Map}.
 *
 * <p>All keys are expected to be strings (though {@code null} is a valid key). The map remembers
 * the case of the last key to be set, and {@link Map#keySet()} or {@link Map#entrySet()} will
 * contain case-sensitive keys. However, querying and contains testing is case-insensitive.</p>
 */
public class CaseInsensitiveMap<V> extends AbstractMap<String, V> implements Map<String, V>, Cloneable, Serializable {
    private static final long serialVersionUID = 107333939521129358L;

    @SuppressWarnings("serial")
    private Map<String, Entry<String, V>> store;

    /** Instantiates a new instance of the {@link CaseInsensitiveMap} class. */
    public CaseInsensitiveMap() {
        this.store = new HashMap<String, Entry<String, V>>();
    }

    /**
     * Returns an instance of {@link CaseInsensitiveMap} using the contents of another map.
     *
     * @param map the map to create the {@link CaseInsensitiveMap} from
     * @return the {@link CaseInsensitiveMap}
     */
    public static <V> CaseInsensitiveMap<V> of(Map<String, V> map) {
        if (map == null) {
            return null;
        }
        CaseInsensitiveMap<V> ciMap = new CaseInsensitiveMap<>();
        ciMap.putAll(map);
        return ciMap;
    }

    // Query Operations

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(Object key) {
        String keyLower = convertKey(key);
        return this.store.containsKey(keyLower);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(Object value) {
        return this.values().contains(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V get(Object key) {
        String keyLower = convertKey(key);
        Entry<String, V> entry = this.store.get(keyLower);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    // Modification Operations

    /**
     * {@inheritDoc}
     */
    @Override
    public V put(String key, V value) {
        String keyLower = convertKey(key);
        this.store.put(keyLower, new AbstractMap.SimpleEntry<String, V>(key, value));
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V remove(Object key) {
        String keyLower = convertKey(key);
        Entry<String, V> entry = this.store.remove(keyLower);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    // Bulk Operations

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.store.clear();
    }

    // Views

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> keySet() {
        return this.store.values().stream().map(entry -> entry.getKey()).collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<V> values() {
        return this.store.values().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Entry<String, V>> entrySet() {
        return this.store.values().stream().collect(Collectors.toSet());
    }

    // Utility

    private static String convertKey(Object key) {
        if (key == null) {
            return null;
        } else if (key instanceof String) {
            return ((String) key).toLowerCase(Locale.ROOT);
        }
        throw new IllegalArgumentException("key must be a String");
    }
}
