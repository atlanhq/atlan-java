/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.events;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanEventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Details that wrap events when sent through AWS (EventBridge).
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AwsEventWrapper extends AtlanObject {
    /** TBC */
    @Builder.Default
    String version = "0";

    /** Unique identifier (GUID) of the event. */
    String id;

    /** Type of the event. */
    @JsonProperty("detail-type")
    AtlanEventType detailType;

    /** Source of the event. */
    @Builder.Default
    String source = "com.atlan.kafka";

    /** TBC */
    String account;

    /** Time at which the event was created. */
    String time;

    /** AWS region on which the event was created. */
    String region;

    /** TBC */
    @JsonIgnore
    List<Object> resources;

    /** Details of the event. */
    AtlanEvent detail;

    /**
     * Retrieve the detailed payload from the event.
     *
     * @return the detailed message (payload) contained in the event.
     */
    @JsonIgnore
    public AtlanEventPayload getPayload() {
        if (detail != null) {
            return detail.getPayload();
        }
        return null;
    }
}
