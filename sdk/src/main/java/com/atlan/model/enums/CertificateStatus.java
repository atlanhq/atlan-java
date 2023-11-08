/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum CertificateStatus implements AtlanEnum {
    DEPRECATED("DEPRECATED"),
    DRAFT("DRAFT"),
    VERIFIED("VERIFIED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    CertificateStatus(String value) {
        this.value = value;
    }

    public static CertificateStatus fromValue(String value) {
        for (CertificateStatus b : CertificateStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
