/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.events;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AtlanEvent extends AtlanObject {
    private static final long serialVersionUID = 2L;

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
    @JsonProperty("message")
    AtlanEventPayload payload;
}
