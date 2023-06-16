/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka assets
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = KafkaTopic.class, name = KafkaTopic.TYPE_NAME),
    @JsonSubTypes.Type(value = KafkaConsumerGroup.class, name = KafkaConsumerGroup.TYPE_NAME),
})
@Slf4j
public abstract class Kafka extends Asset implements IKafka, IEventStore, ICatalog, IAsset, IReferenceable {

    public static final String TYPE_NAME = "Kafka";

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
