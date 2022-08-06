/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.model.enums.AtlanStatus;
import com.atlan.net.AtlanObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class Classification extends AtlanObject {

    public static Classification of(String classificationName, String entityGuid) {
        return Classification.builder()
                .typeName(classificationName) // TODO: translate this from display name to internal name
                .entityGuid(entityGuid)
                .entityStatus(AtlanStatus.ACTIVE)
                .propagate(true)
                .removePropagationsOnEntityDelete(true)
                .restrictPropagationThroughLineage(false)
                .build();
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
