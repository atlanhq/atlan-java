/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum PowerbiEndorsement implements AtlanEnum {
    PROMOTED("Promoted"),
    CERTIFIED("Certified"),
    MASTER_DATA("Master Data"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PowerbiEndorsement(String value) {
        this.value = value;
    }

    public static PowerbiEndorsement fromValue(String value) {
        for (PowerbiEndorsement b : PowerbiEndorsement.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
