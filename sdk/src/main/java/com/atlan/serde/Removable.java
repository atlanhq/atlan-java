/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A class to capture a value that can be explicitly set to null, so that we can
 * serialize it accordingly.
 */
@JsonSerialize(using = RemovableSerializer.class)
public class Removable {

    /**
     * Type of empty value (primitives and lists are treated differently on serialization).
     */
    public enum TYPE {
        PRIMITIVE,
        LIST;
    }

    /**
     * Single null value that can be used for any removable type.
     */
    public static final Removable NULL = new Removable(true);

    /**
     * Single null value that can be used for any list- or set-based removable type.
     */
    public static final Removable EMPTY_LIST = new Removable(true, TYPE.LIST);

    private TYPE type = TYPE.PRIMITIVE;
    private boolean jsonNull = false;

    private Removable(boolean jsonNull) {
        this.jsonNull = jsonNull;
    }

    private Removable(boolean jsonNull, TYPE type) {
        this.jsonNull = jsonNull;
        this.type = type;
    }

    /**
     * Retrieve the underlying type for this object.
     *
     * @return the value
     */
    public TYPE getType() {
        return type;
    }

    /**
     * Indicates whether we should explicitly set a null value in JSON serialization
     * or not.
     *
     * @return true if we should serialize the value as null
     */
    public boolean isJsonNull() {
        return jsonNull;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return type == TYPE.LIST ? "[]" : "null";
    }
}
