/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.atlan.model.enums.AtlanEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.annotation.processing.Generated;

@Generated(value="com.atlan.generators.ModelGeneratorV2")
public enum DataModelTool implements AtlanEnum {
    MAGIC_DRAW("MagicDraw"),
    E_R_WIN("ERWin"),
;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataModelTool(String value) {
        this.value = value;
    }

    public static DataModelTool fromValue(String value) {
        for (DataModelTool b : DataModelTool.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
