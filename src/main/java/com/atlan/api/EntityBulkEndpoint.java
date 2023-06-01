/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.core.AssetDeletionResponse;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.core.ConnectionCreationResponse;
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
public class EntityBulkEndpoint extends AtlasEndpoint {

    private static final String endpoint = "/entity/bulk";

    /**
     * Creates any asset, optionally overwriting an existing entity's Atlan tags and entirely
     * ignoring any custom metadata.
     *
     * @param value asset to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public static AssetMutationResponse upsert(Asset value, boolean replaceAtlanTags) throws AtlanException {
        return upsert(Collections.singletonList(value), replaceAtlanTags);
    }

    /**
     * Creates any assets, optionally overwriting the existing assets' Atlan tags and entirely
     * ignoring any custom metadata.
     *
     * @param values assets to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public static AssetMutationResponse upsert(List<Asset> values, boolean replaceAtlanTags) throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=false&overwriteBusinessAttributes=false",
                        endpoint, replaceAtlanTags));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        return ApiResource.request(ApiResource.RequestMethod.POST, url, beq, AssetMutationResponse.class, null);
    }

    /**
     * Creates any assets, optionally overwriting the existing assets' Atlan tags and merging any
     * provided custom metadata values (but leaving any existing custom metadata values as-is).
     *
     * @param values assets to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public static AssetMutationResponse upsertMergingCM(List<Asset> values, boolean replaceAtlanTags)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=true&overwriteBusinessAttributes=false",
                        endpoint, replaceAtlanTags));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        return ApiResource.request(ApiResource.RequestMethod.POST, url, beq, AssetMutationResponse.class, null);
    }

    /**
     * Creates any assets, optionally overwriting the existing assets' Atlan tags and replacing all
     * custom metadata values on the asset with the ones provided (wiping out any existing custom metadata
     * on the asset that is not also provided in the request).
     *
     * @param values assets to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public static AssetMutationResponse upsertReplacingCM(List<Asset> values, boolean replaceAtlanTags)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=true&overwriteBusinessAttributes=true",
                        endpoint, replaceAtlanTags));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        return ApiResource.request(ApiResource.RequestMethod.POST, url, beq, AssetMutationResponse.class, null);
    }

    /**
     * Creates any assets, optionally overwriting the existing assets' Atlan tags.
     * Custom metadata will always be entirely ignored through this method.
     *
     * @param value connection to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public static ConnectionCreationResponse connectionUpsert(Connection value, boolean replaceAtlanTags)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=false&overwriteBusinessAttributes=false",
                        endpoint, replaceAtlanTags));
        BulkEntityRequest beq = BulkEntityRequest.builder()
                .entities(Collections.singletonList(value))
                .build();
        return ApiResource.request(ApiResource.RequestMethod.POST, url, beq, ConnectionCreationResponse.class, null);
    }

    /**
     * Deletes any asset.
     *
     * @param guid unique ID of the asset to delete
     * @param deleteType whether to soft-delete (archive) or hard-delete (purge) the asset
     * @return the results of the deletion
     * @throws AtlanException on any API interaction problems
     */
    public static AssetDeletionResponse delete(String guid, AtlanDeleteType deleteType) throws AtlanException {
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
    public static AssetDeletionResponse delete(List<String> guids, AtlanDeleteType deleteType) throws AtlanException {
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
                        "%s%s", getBaseUrl(), String.format("%s?%s&deleteType=%s", endpoint, guidList, deleteType));
                return ApiResource.request(
                        ApiResource.RequestMethod.DELETE, url, "", AssetDeletionResponse.class, null);
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
                getBaseUrl(),
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
