/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.probable.guacamole.model.enums;

import com.atlan.model.enums.AtlanEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.probable.guacamole.generators.POJOGenerator")
public enum GuacamoleTemperature implements AtlanEnum {
    HOT("Hot"),
    MILD("Mild"),
    COLD("Cold"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    GuacamoleTemperature(String value) {
        this.value = value;
    }

    public static GuacamoleTemperature fromValue(String value) {
        for (GuacamoleTemperature b : GuacamoleTemperature.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
