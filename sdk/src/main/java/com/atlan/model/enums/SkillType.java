/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum SkillType implements AtlanEnum {
    SYSTEM("SYSTEM"),
    CONTEXT_REPO("CONTEXT_REPO"),
    CUSTOM("CUSTOM"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    SkillType(String value) {
        this.value = value;
    }

    public static SkillType fromValue(String value) {
        for (SkillType b : SkillType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
