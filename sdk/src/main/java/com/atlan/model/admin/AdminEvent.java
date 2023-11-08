/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AdminOperationType;
import com.atlan.model.enums.AdminResourceType;
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
public class AdminEvent extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Type of admin operation that occurred. */
    AdminOperationType operationType;

    /** Unique identifier of the realm in which the event occurred (usually {@code default}). */
    String realmId;

    /** Detailed resource that was created or changed as a result of the operation. */
    String representation;

    /** Location of the resource that was created or changed as a result of the operation. */
    String resourcePath;

    /** Type of resource for the admin operation that occurred. */
    AdminResourceType resourceType;

    /** Time (epoch) when the admin operation occurred, in milliseconds. */
    Long time;

    /** Details of who carried out the operation. */
    AuthDetails authDetails;
}
