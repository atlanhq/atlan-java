/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import java.util.Map;
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
public class Session extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Map of clientId (GUIDs) to client names. */
    Map<String, String> clients;

    /** Unique identifier (GUID) for the session. */
    String id;

    /** IP address from which the session originated. */
    String ipAddress;

    /** Time (epoch) at which the session was last used, in nanoseconds. */
    Long lastAccess;

    /** Time (epoch) at which the session was created, in nanoseconds. */
    Long start;

    /** Unique identifier (GUID) of the user that started the session. */
    String userId;

    /** Username of the user that started the session. */
    String username;
}
