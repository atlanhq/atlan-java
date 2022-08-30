/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.net.ApiResource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EntityMutationResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Assets that were changed. */
    MutatedEntities mutatedEntities;

    /** Map of assigned unique identifiers for the created assets. */
    Map<String, String> guidAssignments;

    /**
     * List of entities that were partially updated.
     * This will only be populated by certain API calls, and actually duplicates the details you'll find
     * in {@link MutatedEntities#PARTIAL_UPDATE} for those same API calls.
     */
    List<Entity> partialUpdatedEntities;

    /**
     * Retrieve the list of entities that were created.
     * @return list of created entities, or an empty list if none were created
     */
    public List<Entity> getCreatedEntities() {
        if (mutatedEntities != null) {
            List<Entity> created = mutatedEntities.getCREATE();
            return created == null ? Collections.emptyList() : created;
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve the list of entities that were updated.
     * @return list of updated entities, or an empty list of none were updated
     */
    public List<Entity> getUpdatedEntities() {
        if (mutatedEntities != null) {
            List<Entity> updated = mutatedEntities.getUPDATE();
            return updated == null ? Collections.emptyList() : updated;
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve the list of entities that were partially updated.
     * Note: this should only ever be populated by calls to the {@link com.atlan.api.EntityUniqueAttributesEndpoint}
     * @return list of partially updated entities, or an empty list of none were partially updated
     */
    public List<Entity> getPartiallyUpdatedEntities() {
        if (mutatedEntities != null) {
            List<Entity> updated = mutatedEntities.getPARTIAL_UPDATE();
            return updated == null ? Collections.emptyList() : updated;
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve the list of entities that were deleted.
     * @return list of deleted entities, or an empty list of none were deleted
     */
    public List<Entity> getDeletedEntities() {
        if (mutatedEntities != null) {
            List<Entity> deleted = mutatedEntities.getDELETE();
            return deleted == null ? Collections.emptyList() : deleted;
        }
        return Collections.emptyList();
    }
}
