/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.model.assets.Asset;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class AssetMutationResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Assets that were changed. */
    @JsonProperty("mutatedEntities")
    MutatedAssets mutatedAssets;

    /** Map of assigned unique identifiers for the created assets. */
    Map<String, String> guidAssignments;

    /**
     * List of assets that were partially updated.
     * This will only be populated by certain API calls, and actually duplicates the details you'll find
     * in {@link MutatedAssets#PARTIAL_UPDATE} for those same API calls.
     */
    @JsonProperty("partialUpdatedEntities")
    List<Asset> partialUpdatedAssets;

    /**
     * Retrieve the list of assets that were created.
     * @return list of created assets, or an empty list if none were created
     */
    public List<Asset> getCreatedAssets() {
        if (mutatedAssets != null) {
            List<Asset> created = mutatedAssets.getCREATE();
            return created == null ? Collections.emptyList() : created;
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve the list of assets that were updated.
     * @return list of updated assets, or an empty list of none were updated
     */
    public List<Asset> getUpdatedAssets() {
        if (mutatedAssets != null) {
            List<Asset> updated = mutatedAssets.getUPDATE();
            return updated == null ? Collections.emptyList() : updated;
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve the list of assets that were partially updated.
     * Note: this should only ever be populated by calls to the {@link com.atlan.api.EntityUniqueAttributesEndpoint}
     * @return list of partially updated assets, or an empty list of none were partially updated
     */
    public List<Asset> getPartiallyUpdatedAssets() {
        if (mutatedAssets != null) {
            List<Asset> updated = mutatedAssets.getPARTIAL_UPDATE();
            return updated == null ? Collections.emptyList() : updated;
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve the list of assets that were deleted.
     * @return list of deleted assets, or an empty list of none were deleted
     */
    public List<Asset> getDeletedAssets() {
        if (mutatedAssets != null) {
            List<Asset> deleted = mutatedAssets.getDELETE();
            return deleted == null ? Collections.emptyList() : deleted;
        }
        return Collections.emptyList();
    }
}
