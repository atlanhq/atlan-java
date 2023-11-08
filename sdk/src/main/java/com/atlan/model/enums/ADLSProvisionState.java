/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum ADLSProvisionState implements AtlanEnum {
    CREATING("Creating"),
    RESOLVING_DNS("ResolvingDNS"),
    SUCCEEDED("Succeeded"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ADLSProvisionState(String value) {
        this.value = value;
    }

    public static ADLSProvisionState fromValue(String value) {
        for (ADLSProvisionState b : ADLSProvisionState.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
