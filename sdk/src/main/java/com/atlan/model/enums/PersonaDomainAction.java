/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PersonaDomainAction implements AtlanEnum, AtlanPolicyAction {
    CREATE_DOMAIN("persona-domain-create"),
    READ_DOMAIN("persona-domain-read"),
    UPDATE_DOMAIN("persona-domain-update"),
    DELETE_DOMAIN("persona-domain-delete"),
    CREATE_SUBDOMAIN("persona-domain-sub-domain-create"),
    READ_SUBDOMAIN("persona-domain-sub-domain-read"),
    UPDATE_SUBDOMAIN("persona-domain-sub-domain-update"),
    DELETE_SUBDOMAIN("persona-domain-sub-domain-delete"),
    CREATE_PRODUCTS("persona-domain-product-create"),
    READ_PRODUCTS("persona-domain-product-read"),
    UPDATE_PRODUCTS("persona-domain-product-update"),
    DELETE_PRODUCTS("persona-domain-product-delete");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PersonaDomainAction(String value) {
        this.value = value;
    }

    public static PersonaDomainAction fromValue(String value) {
        for (PersonaDomainAction b : PersonaDomainAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
