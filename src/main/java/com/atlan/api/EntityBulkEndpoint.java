/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.net.ApiResource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * API endpoints for operating on multiple assets (entities) at the same time (in bulk).
 */
public class EntityBulkEndpoint {

    private static final String endpoint = "/api/meta/entity/bulk";

    /**
     * Creates any asset, optionally overwriting an existing entity's classifications and / or
     * custom metadata.
     *
     * @param value asset to upsert
     * @param replaceClassifications whether to overwrite any existing classifications (true) or not (false)
     * @param replaceCustomMetadata whether to overwrite any existing custom metadata (true) or not (false)
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public static AssetMutationResponse upsert(
            Asset value, boolean replaceClassifications, boolean replaceCustomMetadata) throws AtlanException {
        return upsert(Collections.singletonList(value), replaceClassifications, replaceCustomMetadata);
    }

    /**
     * Creates any assets, optionally overwriting the existing assets' classifications and / or
     * custom metadata.
     *
     * @param values assets to upsert
     * @param replaceClassifications whether to overwrite any existing classifications (true) or not (false)
     * @param replaceCustomMetadata whether to overwrite any existing custom metadata (true) or not (false)
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public static AssetMutationResponse upsert(
            List<Asset> values, boolean replaceClassifications, boolean replaceCustomMetadata) throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=%s&overwriteBusinessAttributes=%s",
                        endpoint, replaceClassifications, replaceCustomMetadata, replaceCustomMetadata));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        return ApiResource.request(ApiResource.RequestMethod.POST, url, beq, AssetMutationResponse.class, null);
    }

    /**
     * Deletes any asset.
     *
     * @param guid unique ID of the asset to delete
     * @param deleteType whether to soft-delete (archive) or hard-delete (purge) the asset
     * @return the results of the deletion
     * @throws AtlanException on any API interaction problems
     */
    public static AssetMutationResponse delete(String guid, AtlanDeleteType deleteType) throws AtlanException {
        return delete(Collections.singletonList(guid), deleteType);
    }

    /**
     * Deletes any assets.
     *
     * @param guids unique IDs of the assets to delete
     * @param deleteType whether to soft-delete (archive) or hard-delete (purge) the assets
     * @return the results of the deletion
     * @throws AtlanException on any API interaction problems
     */
    public static AssetMutationResponse delete(List<String> guids, AtlanDeleteType deleteType) throws AtlanException {
        if (guids != null) {
            StringBuilder guidList = new StringBuilder();
            for (String guid : guids) {
                if (guid != null) {
                    guidList.append("guid=").append(guid).append("&");
                }
            }
            if (guidList.length() > 0) {
                // Remove the final comma
                guidList.setLength(guidList.length() - 1);
                String url = String.format(
                        "%s%s",
                        Atlan.getBaseUrl(), String.format("%s?%s&deleteType=%s", endpoint, guidList, deleteType));
                return ApiResource.request(
                        ApiResource.RequestMethod.DELETE, url, "", AssetMutationResponse.class, null);
            }
        }
        throw new InvalidRequestException(ErrorCode.MISSING_GUID_FOR_DELETE);
    }

    /**
     * Restores any asset from a soft-deleted (archived) to an active state.
     *
     * @param value asset to restore
     * @return the results of the restoration (the restored asset will be in the list of updated assets)
     * @throws AtlanException on any API interaction problems
     */
    public static AssetMutationResponse restore(Asset value) throws AtlanException {
        return restore(Collections.singletonList(value));
    }

    /**
     * Restores any assets in the list provided from a soft-deleted (archived) to active state.
     *
     * @param values assets to restore
     * @return the results of the restoration (any restored assets will be in the list of updated assets)
     * @throws AtlanException on any API interaction problems
     */
    public static AssetMutationResponse restore(List<Asset> values) throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=false&replaceBusinessAttributes=false&overwriteBusinessAttributes=false",
                        endpoint));
        List<Asset> culled = new ArrayList<>();
        for (Asset one : values) {
            culled.add(one.trimToRequired().status(AtlanStatus.ACTIVE).build());
        }
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(culled).build();
        return ApiResource.request(ApiResource.RequestMethod.POST, url, beq, AssetMutationResponse.class, null);
    }

    /**
     * Request class for updating many assets together (in bulk).
     */
    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    static class BulkEntityRequest extends AtlanObject {
        /** List of assets to operate on in bulk. */
        List<Asset> entities;
    }
}
