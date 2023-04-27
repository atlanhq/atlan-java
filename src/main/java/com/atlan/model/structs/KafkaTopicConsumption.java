/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the consumption of a Kafka topic.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class KafkaTopicConsumption extends AtlanObject {

    /**
     * Quickly create a new KafkaTopicConsumption.
     * @param topicName Name of the Kafka topic.
     * @param topicPartition Partition of the Kafka topic.
     * @param topicLag TBC
     * @param topicCurrentOffset TBC
     * @return a KafkaTopicConsumption with the provided information
     */
    public static KafkaTopicConsumption of(
            String topicName, String topicPartition, Long topicLag, Long topicCurrentOffset) {
        return KafkaTopicConsumption.builder()
                .topicName(topicName)
                .topicPartition(topicPartition)
                .topicLag(topicLag)
                .topicCurrentOffset(topicCurrentOffset)
                .build();
    }

    /** Name of the Kafka topic. */
    String topicName;

    /** Partition of the Kafka topic. */
    String topicPartition;

    /** TBC */
    Long topicLag;

    /** TBC */
    Long topicCurrentOffset;
}
