/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AuditActionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class EntityAudit extends AtlanObject {

    /** Unique name of the asset. */
    String entityQualifiedName;

    /** Type of the asset. */
    String typeName;

    /** Unique identifier (GUID) of the asset. */
    String entityId;

    /** Time (epoch) at which the activity started, in milliseconds. */
    Long timestamp;

    /** Time (epoch) at which the activity completed, in milliseconds. */
    Long created;

    /** User who carried out the activity. */
    String user;

    /** The type of activity that was done. */
    AuditActionType action;

    /** Unused. */
    final Object details;

    /** Unique identifier of the activity. */
    String eventKey;

    /** Unused. */
    final Object entity;

    /** Unused. */
    final Object type;

    /**
     * Details of the activity.
     * In practice this will either be details about a classification (for classification-
     * related actions) or an entity (for other actions).
     */
    AuditDetail detail;

    /**
     * Minimal details about the asset that was acted upon.
     * Note that this contains current details about the asset, not the state of the
     * asset immediately before or after the given activity.
     */
    Entity entityDetail;

    /** Unused. */
    final Object headers;
}
