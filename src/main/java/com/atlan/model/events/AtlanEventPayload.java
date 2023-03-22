/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.events;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "operationType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AssetCreatePayload.class, name = AssetCreatePayload.TYPE_NAME),
    @JsonSubTypes.Type(value = AssetUpdatePayload.class, name = AssetUpdatePayload.TYPE_NAME),
    @JsonSubTypes.Type(value = AssetDeletePayload.class, name = AssetDeletePayload.TYPE_NAME),
    @JsonSubTypes.Type(value = CustomMetadataUpdatePayload.class, name = CustomMetadataUpdatePayload.TYPE_NAME),
    @JsonSubTypes.Type(value = ClassificationAddPayload.class, name = ClassificationAddPayload.TYPE_NAME),
    @JsonSubTypes.Type(value = ClassificationDeletePayload.class, name = ClassificationDeletePayload.TYPE_NAME),
})
public abstract class AtlanEventPayload extends AtlanObject {
    /** Type of the event payload. */
    String type;

    /** Type of the operation the event contains a payload for. */
    String operationType;

    /** Time (epoch) the event was triggered in the source system, in milliseconds. */
    Long eventTime;
}
