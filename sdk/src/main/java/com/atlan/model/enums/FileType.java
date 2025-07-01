/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum FileType implements AtlanEnum {
    PDF("pdf"),
    DOC("doc"),
    XLS("xls"),
    PPT("ppt"),
    CSV("csv"),
    TXT("txt"),
    JSON("json"),
    XML("xml"),
    ZIP("zip"),
    YXDB("yxdb"),
    XLSM("xlsm"),
    HYPER("hyper"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    FileType(String value) {
        this.value = value;
    }

    public static FileType fromValue(String value) {
        for (FileType b : FileType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
