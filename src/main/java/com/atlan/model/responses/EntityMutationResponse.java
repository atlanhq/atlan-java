/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.responses;

import com.atlan.model.core.Entity;
import com.atlan.net.ApiResource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EntityMutationResponse extends ApiResource {
    /** Assets that were changed. */
    MutatedEntities mutatedEntities;

    /** Map of assigned unique identifiers for the created assets. */
    Map<String, String> guidAssignments;

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
