/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.contracts;

import com.atlan.model.enums.AtlanEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum DataContractStatus implements AtlanEnum {
    DRAFT("draft"),
    VERIFIED("verified");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataContractStatus(String value) {
        this.value = value;
    }

    public static DataContractStatus fromValue(String value) {
        for (DataContractStatus b : DataContractStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
