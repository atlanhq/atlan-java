/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.responses;

import com.atlan.model.core.EntityJ;
import com.atlan.net.ApiResourceJ;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EntityMutationResponseJ extends ApiResourceJ {
    private static final long serialVersionUID = 2L;

    /** Assets that were changed. */
    MutatedEntitiesJ mutatedEntities;

    /** Map of assigned unique identifiers for the created assets. */
    Map<String, String> guidAssignments;

    /**
     * List of entities that were partially updated.
     * This will only be populated by certain API calls, and actually duplicates the details you'll find
     * in {@link MutatedEntitiesJ#PARTIAL_UPDATE} for those same API calls.
     */
    List<EntityJ> partialUpdatedEntities;

    /**
     * Retrieve the list of entities that were created.
     * @return list of created entities, or an empty list if none were created
     */
    public List<EntityJ> getCreatedEntities() {
        if (mutatedEntities != null) {
            List<EntityJ> created = mutatedEntities.getCREATE();
            return created == null ? Collections.emptyList() : created;
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve the list of entities that were updated.
     * @return list of updated entities, or an empty list of none were updated
     */
    public List<EntityJ> getUpdatedEntities() {
        if (mutatedEntities != null) {
            List<EntityJ> updated = mutatedEntities.getUPDATE();
            return updated == null ? Collections.emptyList() : updated;
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve the list of entities that were partially updated.
     * Note: this should only ever be populated by calls to the {@link com.atlan.api.EntityUniqueAttributesEndpoint}
     * @return list of partially updated entities, or an empty list of none were partially updated
     */
    public List<EntityJ> getPartiallyUpdatedEntities() {
        if (mutatedEntities != null) {
            List<EntityJ> updated = mutatedEntities.getPARTIAL_UPDATE();
            return updated == null ? Collections.emptyList() : updated;
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve the list of entities that were deleted.
     * @return list of deleted entities, or an empty list of none were deleted
     */
    public List<EntityJ> getDeletedEntities() {
        if (mutatedEntities != null) {
            List<EntityJ> deleted = mutatedEntities.getDELETE();
            return deleted == null ? Collections.emptyList() : deleted;
        }
        return Collections.emptyList();
    }
}
