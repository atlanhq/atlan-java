/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum KafkaTopicCleanupPolicy implements AtlanEnum {
    COMPACT("compact"),
    DELETE("delete");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    KafkaTopicCleanupPolicy(String value) {
        this.value = value;
    }

    public static KafkaTopicCleanupPolicy fromValue(String value) {
        for (KafkaTopicCleanupPolicy b : KafkaTopicCleanupPolicy.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
