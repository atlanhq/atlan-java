/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.model.core.AtlanObject;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * Generic class through which to cache any objects efficiently, off-heap, to avoid risking extreme
 * memory usage.
 */
@Slf4j
public class OffHeapObjectCache<T extends AtlanObject> extends AbstractOffHeapCache<String, T> {

    private final AtlanClient client;

    /**
     * Construct new object cache.
     *
     * @param client connectivity to the Atlan tenant
     * @param name to distinguish which cache is which
     */
    public OffHeapObjectCache(AtlanClient client, String name) {
        super(name);
        this.client = client;
    }

    /** {@inheritDoc} */
    @Override
    protected byte[] serializeKey(String key) {
        return key.getBytes(StandardCharsets.UTF_8);
    }

    /** {@inheritDoc} */
    @Override
    protected String deserializeKey(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /** {@inheritDoc} */
    @Override
    protected byte[] serializeValue(T value) {
        byte[] typeName = value.getClass().getCanonicalName().getBytes(StandardCharsets.UTF_8);
        int typeNameLength = typeName.length;
        try {
            byte[] json = client.writeValueAsBytes(value);
            ByteBuffer buffer = ByteBuffer.allocate(typeNameLength + 4 + json.length);
            buffer.putInt(typeNameLength);
            buffer.put(typeName);
            buffer.put(json);
            if (buffer.hasArray()) {
                return buffer.array();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to serialize value.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    protected T deserializeValue(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int typeNameLength = buffer.getInt();
        byte[] typeNameBytes = new byte[typeNameLength];
        buffer.get(typeNameBytes);
        String typeName = new String(typeNameBytes, StandardCharsets.UTF_8);
        try {
            Class<?> type = Class.forName(typeName);
            byte[] json = new byte[buffer.remaining()];
            buffer.get(json);
            return (T) client.readValue(json, type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find type: " + typeName + ".", e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to deserialize value.", e);
        }
    }
}
