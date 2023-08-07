/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.model.assets.Asset;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
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
    @JsonIgnore
    public List<Asset> getCreatedAssets() {
        return mutatedAssets == null ? Collections.emptyList() : sublist(mutatedAssets.getCREATE(), Asset.class);
    }

    /**
     * Retrieve the sublist of assets that were created, of the provided type.
     *
     * @param type of assets to filter the created list by
     * @return list of created assets, only of the requested type, or an empty list of none of that type were created
     * @param <T> the type of created assets to filter
     */
    @JsonIgnore
    public <T extends Asset> List<T> getCreatedAssets(Class<T> type) {
        return mutatedAssets == null ? Collections.emptyList() : sublist(mutatedAssets.getCREATE(), type);
    }

    /**
     * Retrieve the list of assets that were updated.
     * @return list of updated assets, or an empty list of none were updated
     */
    @JsonIgnore
    public List<Asset> getUpdatedAssets() {
        return mutatedAssets == null ? Collections.emptyList() : sublist(mutatedAssets.getUPDATE(), Asset.class);
    }

    /**
     * Retrieve the sublist of assets that were updated, of the provided type.
     *
     * @param type of assets to filter the updated list by
     * @return list of updated assets, only of the requested type, or an empty list of none of that type were updated
     * @param <T> the type of updated assets to filter
     */
    @JsonIgnore
    public <T extends Asset> List<T> getUpdatedAssets(Class<T> type) {
        return mutatedAssets == null ? Collections.emptyList() : sublist(mutatedAssets.getUPDATE(), type);
    }

    /**
     * Retrieve the list of assets that were partially updated.
     * Note: this should only ever be populated by calls to the certain endpoints
     * @return list of partially updated assets, or an empty list of none were partially updated
     */
    @JsonIgnore
    public List<Asset> getPartiallyUpdatedAssets() {
        return mutatedAssets == null
                ? Collections.emptyList()
                : sublist(mutatedAssets.getPARTIAL_UPDATE(), Asset.class);
    }

    /**
     * Retrieve the sublist of assets that were partially updated, of the provided type.
     * Note: this should only ever be populated by calls to the certain endpoints
     *
     * @param type of assets to filter the partially updated list by
     * @return list of partially updated assets, only of the requested type, or an empty list of none of that type were partially updated
     * @param <T> the type of partially updated assets to filter
     */
    @JsonIgnore
    public <T extends Asset> List<T> getPartiallyUpdatedAssets(Class<T> type) {
        return mutatedAssets == null ? Collections.emptyList() : sublist(mutatedAssets.getPARTIAL_UPDATE(), type);
    }

    /**
     * Retrieve the list of assets that were deleted.
     * @return list of deleted assets, or an empty list of none were deleted
     */
    @JsonIgnore
    public List<Asset> getDeletedAssets() {
        return mutatedAssets == null ? Collections.emptyList() : sublist(mutatedAssets.getDELETE(), Asset.class);
    }

    /**
     * Retrieve the sublist of assets that were deleted, of the provided type.
     *
     * @param type of assets to filter the deleted list by
     * @return list of deleted assets, only of the requested type, or an empty list of none of that type were deleted
     * @param <T> the type of deleted assets to filter
     */
    @JsonIgnore
    public <T extends Asset> List<T> getDeletedAssets(Class<T> type) {
        return mutatedAssets == null ? Collections.emptyList() : sublist(mutatedAssets.getDELETE(), type);
    }

    @SuppressWarnings("unchecked")
    private <T extends Asset> List<T> sublist(List<Asset> list, Class<T> type) {
        if (type == Asset.class) {
            return list == null ? Collections.emptyList() : (List<T>) list;
        } else {
            return list == null
                    ? Collections.emptyList()
                    : list.stream().filter(type::isInstance).map(type::cast).collect(Collectors.toList());
        }
    }
}
