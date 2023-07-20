/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.KeycloakEventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class KeycloakEvent extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Where the event occurred (usually {@code atlan-frontend}). */
    String clientId;

    /** TBC */
    @JsonIgnore
    String details;

    /** IP address from which the user logged in. */
    String ipAddress;

    /** Unique identifier of the realm in which the event was logged (usually {@code default}. */
    String realmId;

    /** Unique identifier (GUID) of the session for the event. */
    String sessionId;

    /** Time (epoch) when the event occurred, in milliseconds. */
    Long time;

    /** Type of event that occurred. */
    KeycloakEventType type;

    /** Unique identifier (GUID) of the user that triggered the event. */
    String userId;
}
