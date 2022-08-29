/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import java.util.AbstractMap;

/**
 * A KeyValuePair holds a key and a value. This class is used to represent parameters when encoding
 * API requests.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class KeyValuePair<K, V> extends AbstractMap.SimpleEntry<K, V> {
    private static final long serialVersionUID = 1L;

    /**
     * Initializes a new instance of the {@link KeyValuePair} class using the specified key and value.
     *
     * @param key the key
     * @param value the value
     */
    public KeyValuePair(K key, V value) {
        super(key, value);
    }
}
