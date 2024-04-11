/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.search.AuditDetail;
import com.atlan.model.structs.SourceTagAttachment;
import com.atlan.serde.AtlanTagDeserializer;
import com.atlan.serde.AtlanTagSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Comparator;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@JsonDeserialize(using = AtlanTagDeserializer.class)
@JsonSerialize(using = AtlanTagSerializer.class)
@ToString(callSuper = true)
public class AtlanTag extends AtlanObject implements AuditDetail, Comparable<AtlanTag> {
    private static final long serialVersionUID = 2L;

    private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
    private static final Comparator<AtlanTag> atlanTagComparator = Comparator.comparing(
                    AtlanTag::getTypeName, stringComparator)
            .thenComparing(AtlanTag::getEntityGuid, stringComparator);

    /**
     * Construct an Atlan tag assignment for an entity that is being created or updated.
     *
     * @param atlanTagName human-readable name of the Atlan tag
     * @return an Atlan tag assignment with default settings for propagation
     */
    public static AtlanTag of(String atlanTagName) {
        return AtlanTag.builder()
                .typeName(atlanTagName)
                .propagate(true)
                .removePropagationsOnEntityDelete(true)
                .restrictPropagationThroughLineage(false)
                .restrictPropagationThroughHierarchy(false)
                .build();
    }

    /**
     * Construct an Atlan tag assignment for a specific entity.
     *
     * @param atlanTagName human-readable name of the Atlan tag
     * @param entityGuid unique identifier (GUID) of the entity to which the Atlan tag is to be assigned
     * @return an Atlan tag assignment with default settings for propagation and a specific entity assignment
     */
    public static AtlanTag of(String atlanTagName, String entityGuid) {
        return AtlanTag.builder()
                .typeName(atlanTagName)
                .entityGuid(entityGuid)
                .entityStatus(AtlanStatus.ACTIVE)
                .propagate(true)
                .removePropagationsOnEntityDelete(true)
                .restrictPropagationThroughLineage(false)
                .restrictPropagationThroughHierarchy(false)
                .build();
    }

    public AtlanTag() {
        // Necessary for Jackson deserialization
    }

    /**
     * Name of the Atlan tag. Note that this is the static-hashed unique name of the
     * Atlan tag, not the human-readable displayName.
     */
    String typeName;

    /** Unique identifier of the entity to which this Atlan tag is attached. */
    String entityGuid;

    /** Status of the entity. */
    AtlanStatus entityStatus;

    /**
     * Whether to propagate this Atlan tag to other entities related to the entity to which the
     * Atlan tag is attached.
     */
    Boolean propagate;

    /**
     * Whether to remove this Atlan tag from other entities to which it has been propagated when
     * the Atlan tag is removed from this entity.
     */
    Boolean removePropagationsOnEntityDelete;

    /**
     * Whether to prevent this Atlan tag from propagating through lineage (true) or allow it to
     * propagate through lineage (false).
     */
    Boolean restrictPropagationThroughLineage;

    /**
     * Whether to prevent this Atlan tag from propagating through hierarchy (true) or allow it to
     * propagate through hierarchy (false).
     */
    Boolean restrictPropagationThroughHierarchy;

    /** List of attachments of this tag to source-specific tags. */
    @Singular
    List<SourceTagAttachment> sourceTagAttachments;

    /* Unused. List<Object> attributes; List<Object> validityPeriods; */

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(AtlanTag o) {
        return atlanTagComparator.compare(this, o);
    }
}
