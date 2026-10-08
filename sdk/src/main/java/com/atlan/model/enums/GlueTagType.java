/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum GlueTagType implements AtlanEnum {
    PROPERTY("property"),
    RESOURCE_TAG("resource_tag"),
    LF_TAG("lf_tag"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    GlueTagType(String value) {
        this.value = value;
    }

    public static GlueTagType fromValue(String value) {
        for (GlueTagType b : GlueTagType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
