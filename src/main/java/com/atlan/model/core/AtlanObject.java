/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Override
    public String toString() {
        return String.format(
                "<%s@%s> JSON: %s", this.getClass().getName(), System.identityHashCode(this), this.toJson());
    }

    public String toJson() {
        try {
            return Serde.mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Unable to serialize this object: {}", this.getClass().getName(), e);
        }
        return null;
    }
}
