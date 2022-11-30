/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.search.AuditDetail;
import com.atlan.serde.ClassificationDeserializer;
import com.atlan.serde.ClassificationSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@JsonDeserialize(using = ClassificationDeserializer.class)
@JsonSerialize(using = ClassificationSerializer.class)
public class Classification extends AtlanObject implements AuditDetail {

    public static Classification of(String classificationName, String entityGuid) {
        return Classification.builder()
                .typeName(classificationName)
                .entityGuid(entityGuid)
                .entityStatus(AtlanStatus.ACTIVE)
                .propagate(true)
                .removePropagationsOnEntityDelete(true)
                .restrictPropagationThroughLineage(false)
                .build();
    }

    public Classification() {
        // Necessary for Jackson deserialization
    }

    /**
     * Name of the classification. Note that this is the static-hashed unique name of the
     * classification, not hte human-readable displayName.
     */
    String typeName;

    /** Unique identifier of the entity to which this classification is attached. */
    String entityGuid;

    /** Status of the entity. */
    AtlanStatus entityStatus;

    /**
     * Whether to propagate this classification to other entities related to the entity to which the
     * classification is attached.
     */
    Boolean propagate;

    /**
     * Whether to remove this classification from other entities to which it has been propagated when
     * the classification is removed from this entity.
     */
    Boolean removePropagationsOnEntityDelete;

    /**
     * Whether to prevent this classification from propagating through lineage (true) or allow it to
     * propagate through lineage (false).
     */
    Boolean restrictPropagationThroughLineage;

    /** Unused. List<Object> attributes; List<Object> validityPeriods; */
}
