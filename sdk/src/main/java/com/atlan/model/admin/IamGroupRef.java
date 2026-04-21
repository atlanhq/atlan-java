/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a group reference returned by the Redis-backed IAM identity API.
 * Contains the Keycloak group ID and technical name.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IamGroupRef extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Unique identifier of the group. */
    String id;

    /** Technical name of the group (Keycloak group name). */
    String name;
}
