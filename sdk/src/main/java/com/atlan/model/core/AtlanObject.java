/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.AtlanClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public abstract class AtlanObject implements Serializable {
    private static final long serialVersionUID = 2L;

    @JsonIgnore
    protected transient JsonNode rawJsonObject;

    public AtlanObject() {
        // Do nothing - needed for Lombok SuperBuilder generations...
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("<%s#%s>", this.getClass().getName(), this.hashCode());
    }

    /**
     * Serialize the object to a JSON string.
     * @param client the client through which to serialize the object
     * @return the serialized JSON string
     */
    public String toJson(AtlanClient client) {
        try {
            return client.writeValueAsString(this);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to serialize: " + this.getClass().getName() + " -- " + e.getMessage(), e);
        }
    }

    /**
     * Returns the raw JsonNode exposed by the Jackson library. This can be used to access properties
     * that are not directly exposed by Atlan's Java library.
     *
     * <p>Note: You should always prefer using the standard property accessors whenever possible.
     * Because this method exposes Jackson's underlying API, it is not considered fully stable. Atlan's
     * Java library might move off Jackson in the future and this method would be removed or change
     * significantly.</p>
     *
     * @return The raw JsonNode.
     */
    @JsonIgnore
    public JsonNode getRawJsonObject() {
        return rawJsonObject;
    }

    /**
     * Sets the raw response from the API. This is used to expose properties that are not
     * directly exposed by Atlan's Java library.
     *
     * @param rawJsonObject the raw JSON from the API response
     */
    @JsonIgnore
    public void setRawJsonObject(JsonNode rawJsonObject) {
        this.rawJsonObject = rawJsonObject;
    }

    public abstract static class AtlanObjectBuilder<C extends AtlanObject, B extends AtlanObjectBuilder<C, B>> {}
}
