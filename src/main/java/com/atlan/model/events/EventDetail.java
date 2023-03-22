/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.events;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class EventDetail extends AtlanObject {

    /** TBC */
    @JsonIgnore
    Object source;

    /** TBC */
    @JsonIgnore
    Object version;

    /** TBC */
    @Builder.Default
    String msgCompressionKind = "NONE";

    /** TBC */
    @Builder.Default
    Long msgSplitIdx = 1L;

    /** TBC */
    @Builder.Default
    Long msgSplitCount = 1L;

    /** Originating IP address for the event. */
    String msgSourceIP;

    /** TBC */
    String msgCreatedBy;

    /** Timestamp (epoch) for when the event was created, in milliseconds. */
    Long msgCreationTime;

    /** TBC */
    @Builder.Default
    Boolean spooled = false;

    /** Detailed contents (payload) of the event. */
    AtlanEventPayload message;
}
