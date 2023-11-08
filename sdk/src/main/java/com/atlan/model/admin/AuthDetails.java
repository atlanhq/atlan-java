/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
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
public class AuthDetails extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Unique identifier (GUID) of the client that carried out the operation. */
    String clientId;

    /** IP address from which the operation was carried out. */
    String ipAddress;

    /** Unique name of the realm from which the operation was carried out. */
    String realmId;

    /** Unique identifier (GUID) of the user who carried out the operation. */
    String userId;
}
