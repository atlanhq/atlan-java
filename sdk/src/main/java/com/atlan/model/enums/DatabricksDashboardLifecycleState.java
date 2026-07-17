/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DatabricksDashboardLifecycleState implements AtlanEnum {
    ACTIVE("ACTIVE"),
    TRASHED("TRASHED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DatabricksDashboardLifecycleState(String value) {
        this.value = value;
    }

    public static DatabricksDashboardLifecycleState fromValue(String value) {
        for (DatabricksDashboardLifecycleState b : DatabricksDashboardLifecycleState.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
