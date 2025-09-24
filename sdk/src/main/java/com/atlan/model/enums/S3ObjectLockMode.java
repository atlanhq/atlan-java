/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum S3ObjectLockMode implements AtlanEnum {
    GOVERNANCE("GOVERNANCE"),
    COMPLIANCE("COMPLIANCE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    S3ObjectLockMode(String value) {
        this.value = value;
    }

    public static S3ObjectLockMode fromValue(String value) {
        for (S3ObjectLockMode b : S3ObjectLockMode.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
