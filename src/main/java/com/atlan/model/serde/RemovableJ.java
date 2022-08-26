package com.atlan.model.serde;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A class to capture a value that can be explicitly set to null, so that we can
 * serialize it accordingly.
 */
@JsonSerialize(using = RemovableJSerializer.class)
public class RemovableJ {

    public enum TYPE {
        PRIMITIVE,
        LIST;
    }

    /**
     * Single null value that can be used for any removable type.
     */
    public static final RemovableJ NULL = new RemovableJ(true);

    public static final RemovableJ EMPTY_LIST = new RemovableJ(true, TYPE.LIST);

    private TYPE type = TYPE.PRIMITIVE;
    private boolean jsonNull = false;

    private RemovableJ(boolean jsonNull) {
        this.jsonNull = jsonNull;
    }

    private RemovableJ(boolean jsonNull, TYPE type) {
        this.jsonNull = jsonNull;
        this.type = type;
    }

    /**
     * Retrieve the underlying type for this object.
     * @return the value
     */
    public TYPE getType() {
        return type;
    }

    /**
     * Indicates whether we should explicitly set a null value in JSON serialization
     * or not.
     * @return true if we should serialize the value as null
     */
    public boolean isJsonNull() {
        return jsonNull;
    }

    @Override
    public String toString() {
        return type == TYPE.LIST ? "[]" : "null";
    }
}
