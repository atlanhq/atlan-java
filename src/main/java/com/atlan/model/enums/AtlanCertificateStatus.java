/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanCertificateStatus implements AtlanEnum {
    VERIFIED("VERIFIED"),
    DRAFT("DRAFT"),
    DEPRECATED("DEPRECATED");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanCertificateStatus(String value) {
        this.value = value;
    }

    public static AtlanCertificateStatus fromValue(String value) {
        for (AtlanCertificateStatus b : AtlanCertificateStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
