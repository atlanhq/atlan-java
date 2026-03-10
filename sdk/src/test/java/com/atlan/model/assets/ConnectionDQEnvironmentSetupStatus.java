/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum ConnectionDQEnvironmentSetupStatus implements AtlanEnum {
    NOT_STARTED("NOT_STARTED"),
    IN_PROGRESS("IN_PROGRESS"),
    SUCCESSFUL("SUCCESSFUL"),
    FAILED("FAILED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ConnectionDQEnvironmentSetupStatus(String value) {
        this.value = value;
    }

    public static ConnectionDQEnvironmentSetupStatus fromValue(String value) {
        for (ConnectionDQEnvironmentSetupStatus b : ConnectionDQEnvironmentSetupStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
