/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.AtlanClient;
import com.atlan.serde.Serde;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public abstract class AtlanObject {

    public AtlanObject() {
        // Do nothing - needed for Lombok SuperBuilder generations...
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("<%s#%s> JSON: %s", this.getClass().getName(), this.hashCode(), rawJson());
    }

    private String rawJson() {
        try {
            return Serde.allInclusiveMapper.writeValueAsString(this);
        } catch (IOException e) {
            log.error("Unable to serialize this object: {}", this.getClass().getName(), e);
        }
        return null;
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
            log.error("Unable to serialize this object: {}", this.getClass().getName(), e);
        }
        return null;
    }
}
