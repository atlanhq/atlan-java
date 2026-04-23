/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum KnowledgeFolderType implements AtlanEnum {
    UPLOAD("UPLOAD"),
    AGENT_CREATED("AGENT_CREATED"),
    CONNECTOR_SYNC("CONNECTOR_SYNC"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    KnowledgeFolderType(String value) {
        this.value = value;
    }

    public static KnowledgeFolderType fromValue(String value) {
        for (KnowledgeFolderType b : KnowledgeFolderType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
