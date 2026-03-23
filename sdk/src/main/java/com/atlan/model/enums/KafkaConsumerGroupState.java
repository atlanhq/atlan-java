/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum KafkaConsumerGroupState implements AtlanEnum {
    STABLE("Stable"),
    EMPTY("Empty"),
    PREPARING_REBALANCE("PreparingRebalance"),
    COMPLETING_REBALANCE("CompletingRebalance"),
    DEAD("Dead"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    KafkaConsumerGroupState(String value) {
        this.value = value;
    }

    public static KafkaConsumerGroupState fromValue(String value) {
        for (KafkaConsumerGroupState b : KafkaConsumerGroupState.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
