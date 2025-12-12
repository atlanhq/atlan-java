/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AtlanAppDeploymentOperation implements AtlanEnum {
    INSTALL("INSTALL"),
    UPGRADE("UPGRADE"),
    DOWNGRADE("DOWNGRADE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanAppDeploymentOperation(String value) {
        this.value = value;
    }

    public static AtlanAppDeploymentOperation fromValue(String value) {
        for (AtlanAppDeploymentOperation b : AtlanAppDeploymentOperation.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
