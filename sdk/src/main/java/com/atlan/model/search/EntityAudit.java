/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.assets.Asset;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AuditActionType;
import com.atlan.serde.EntityAuditDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Detailed entry in the audit log. These objects should be treated as immutable.
 */
@Getter
@JsonDeserialize(using = EntityAuditDeserializer.class)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class EntityAudit extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Unique name of the asset. */
    final String entityQualifiedName;

    /** Type of the asset. */
    final String typeName;

    /** Unique identifier (GUID) of the asset. */
    final String entityId;

    /** Time (epoch) at which the activity started, in milliseconds. */
    final Long timestamp;

    /** Time (epoch) at which the activity completed, in milliseconds. */
    final Long created;

    /** User who carried out the activity. */
    final String user;

    /** The type of activity that was done. */
    final AuditActionType action;

    /** Unused. */
    final Object details;

    /** Unique identifier of the activity. */
    final String eventKey;

    /** Unused. */
    final Object entity;

    /** Unused. */
    final Object type;

    /**
     * Details of the activity.
     * In practice this will either be details about an Atlan tag (for Atlan tag-
     * related actions) or an asset (for other actions).
     */
    final AuditDetail detail;

    /**
     * Minimal details about the asset that was acted upon.
     * Note that this contains current details about the asset, not the state of the
     * asset immediately before or after the given activity.
     */
    final Asset entityDetail;

    /** Headers detailing how the action was taken, if not by a user. */
    final Map<String, String> headers;

    public abstract static class EntityAuditBuilder<C extends EntityAudit, B extends EntityAuditBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
