/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum SchemaRegistrySchemaCompatibility implements AtlanEnum {
    BACKWARD("BACKWARD"),
    BACKWARD_TRANSITIVE("BACKWARD_TRANSITIVE"),
    FORWARD("FORWARD"),
    FORWARD_TRANSITIVE("FORWARD_TRANSITIVE"),
    FULL("FULL"),
    FULL_TRANSITIVE("FULL_TRANSITIVE"),
    NONE("NONE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    SchemaRegistrySchemaCompatibility(String value) {
        this.value = value;
    }

    public static SchemaRegistrySchemaCompatibility fromValue(String value) {
        for (SchemaRegistrySchemaCompatibility b : SchemaRegistrySchemaCompatibility.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
