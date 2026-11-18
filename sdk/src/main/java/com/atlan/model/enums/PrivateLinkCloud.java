/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum PrivateLinkCloud implements AtlanEnum {
    AWS("AWS"),
    AZURE("AZURE"),
    GCP("GCP"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PrivateLinkCloud(String value) {
        this.value = value;
    }

    public static PrivateLinkCloud fromValue(String value) {
        for (PrivateLinkCloud b : PrivateLinkCloud.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
