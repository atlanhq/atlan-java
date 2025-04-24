/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.AtlanClient;
import com.atlan.model.assets.Asset;
import com.atlan.net.ApiResource;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetMutationResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Connectivity to the Atlan tenant where the save operation was run. */
    @Setter
    @JsonIgnore
    protected transient AtlanClient client;

    /** Kind of mutation that was applied to any given asset. */
    public enum MutationType {
        CREATED,
        UPDATED,
        DELETED,
        NOOP,
        UNKNOWN
    }

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

    /**
     * Retrieve the real GUID that was assigned to the asset provided in the request.
     *
     * @param input asset that was sent in the request that produced this mutation response
     * @return the GUID ultimately assigned to that asset, or null if the provided asset is not part of this response
     */
    @JsonIgnore
    public String getAssignedGuid(Asset input) {
        if (input == null) {
            return null;
        }
        String guid = input.getGuid();
        String assigned = null;
        if (guidAssignments != null) {
            // If the provided GUID was either a placeholder, or a real UUID but not
            // a matching UUID, it will be present in the guidAssignments
            assigned = guidAssignments.getOrDefault(guid, null);
        }
        if (assigned == null && StringUtils.isUUID(guid)) {
            // If not in the map, but a valid UUID, then the GUID was used as-is
            assigned = guid;
        }
        return assigned;
    }

    /**
     * Determine the type of mutation that was applied to the asset provided in the request.
     *
     * @param input asset that was sent in the request that produced this mutation response
     */
    @JsonIgnore
    public MutationType getMutation(Asset input) {
        String guid = getAssignedGuid(input);
        // If the asset is not anywhere in the response, then we'll leave it as an unknown
        // (though technically we could probably say it was a NOOP, it might be useful to
        // distinguish these).
        MutationType type = MutationType.UNKNOWN;
        if (guid != null) {
            if (getCreatedAssets().stream().anyMatch(a -> guid.equals(a.getGuid()))) {
                type = MutationType.CREATED;
            } else if (getUpdatedAssets().stream().anyMatch(a -> guid.equals(a.getGuid()))
                    || getPartiallyUpdatedAssets().stream().anyMatch(a -> guid.equals(a.getGuid()))) {
                type = MutationType.UPDATED;
            } else if (getDeletedAssets().stream().anyMatch(a -> guid.equals(a.getGuid()))) {
                type = MutationType.DELETED;
            } else {
                // If it was there in the GUID assignments, but does not appear anywhere in
                // actual asset lists, then it was a no-op (nothing happened to the asset)
                type = MutationType.NOOP;
            }
        }
        return type;
    }

    /**
     * Retrieve the specific mutation result for the asset provided in the request.
     *
     * @param input asset that was sent in the request that produced this mutation response
     * @return the resulting asset from the request, or null if none was found (or if the asset was entirely unchanged)
     * @param <T> the type of the asset
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public <T extends Asset> T getResult(T input) {
        String guid = getAssignedGuid(input);
        List<Asset> found = getCreatedAssets().stream()
                .filter(a -> guid.equals(a.getGuid()))
                .collect(Collectors.toList());
        if (found.isEmpty()) {
            found = getUpdatedAssets().stream()
                    .filter(a -> guid.equals(a.getGuid()))
                    .collect(Collectors.toList());
        }
        if (found.isEmpty()) {
            found = getPartiallyUpdatedAssets().stream()
                    .filter(a -> guid.equals(a.getGuid()))
                    .collect(Collectors.toList());
        }
        if (found.isEmpty()) {
            found = getDeletedAssets().stream()
                    .filter(a -> guid.equals(a.getGuid()))
                    .collect(Collectors.toList());
        }
        return found.isEmpty() ? null : (T) found.get(0);
    }
}
