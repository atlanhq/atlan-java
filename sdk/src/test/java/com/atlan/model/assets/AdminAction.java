/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AdminAction implements AtlanEnum, AtlanPolicyAction {
    ADMIN_TASK_CUD("admin-task-cud"),
    ADMIN_AUDITS("admin-audits"),
    ADMIN_EXPORT("admin-export"),
    ADMIN_FEATURE_FLAG_CUD("admin-featureFlag-cud"),
    ADMIN_IMPORT("admin-import"),
    ADMIN_PURGE("admin-purge"),
    ADMIN_REPAIR_INDEX("admin-repair-index"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AdminAction(String value) {
        this.value = value;
    }

    public static AdminAction fromValue(String value) {
        for (AdminAction b : AdminAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
