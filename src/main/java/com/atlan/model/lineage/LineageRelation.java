/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Captures the relationships in lineage. The specific form of those relationships will vary
 * depending on whether lineage was requested with {@code hideProcess} enabled or disabled.
 */
@Data
@EqualsAndHashCode
public class LineageRelation {

    /** Source GUID for the lineage relation (could be an entity or a process). */
    String fromEntityId;

    /** Target GUID for the lineage relation (could be an entity or a process). */
    String toEntityId;

    /** GUID of the process that joins the entities, if {@code hideProcess} was true in the request. */
    String processId;

    /** Relationship GUID that joins the two objects in lineage, if {@code hideProcess} was false in the request. */
    String relationshipId;

    /**
     * When true, indicates that this relationship contains a full lineage link:
     * specifically that {@link #fromEntityId} and {@link #toEntityId} both represent
     * a non-Process asset, and {@link #processId} gives the GUID of the process that
     * links them.
     *
     * @return true if this is a full lineage link
     */
    @JsonIgnore
    public boolean isFullLink() {
        return processId != null;
    }
}
