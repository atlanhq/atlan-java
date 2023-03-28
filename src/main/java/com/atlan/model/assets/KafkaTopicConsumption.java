/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about how a Kafka topic is consumed.
 */
@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class KafkaTopicConsumption extends AtlanObject {
    /** Name of the Kafka topic. */
    String topicName;

    /** Partition of the Kafka topic. */
    String topicPartition;

    /** TBC */
    Long topicLag;

    /** TBC */
    Long topicCurrentOffset;
}
