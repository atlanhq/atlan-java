/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.core.*;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.lineage.LineageListRequest;
import com.atlan.model.lineage.LineageListResponse;
import com.atlan.model.lineage.LineageRequest;
import com.atlan.model.lineage.LineageResponse;
import com.atlan.model.search.AuditSearchRequest;
import com.atlan.model.search.AuditSearchResponse;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * API endpoints for operating on assets.
 */
@Slf4j
public class AssetEndpoint extends AtlasEndpoint {

    private static final String audit_endpoint = "/entity/auditSearch";
    private static final String bulk_endpoint = "/entity/bulk";
    private static final String guid_endpoint = "/entity/guid/";
    private static final String unique_attr_endpoint = "/entity/uniqueAttribute/type/";
    private static final String search_endpoint = "/search/indexsearch";
    private static final String lineage_endpoint = "/lineage/getlineage";
    private static final String lineage_list_endpoint = "/lineage/list";

    private final AtlanClient client;

    public AssetEndpoint(AtlanClient client) {
        this.client = client;
    }

    /**
     * Run the requested search.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public AuditSearchResponse auditLogs(AuditSearchRequest request) throws AtlanException {
        return auditLogs(request, null);
    }

    /**
     * Run the requested search.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @param options to override default client settings
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public AuditSearchResponse auditLogs(AuditSearchRequest request, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(client), audit_endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, AuditSearchResponse.class, options);
    }

    /**
     * Creates any asset, optionally overwriting an existing entity's Atlan tags and entirely
     * ignoring any custom metadata.
     *
     * @param value asset to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public AssetMutationResponse save(Asset value, boolean replaceAtlanTags) throws AtlanException {
        return save(List.of(value), replaceAtlanTags, null);
    }

    /**
     * Creates any asset, optionally overwriting an existing entity's Atlan tags and entirely
     * ignoring any custom metadata.
     *
     * @param value asset to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @param options to override default client settings
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public AssetMutationResponse save(Asset value, boolean replaceAtlanTags, RequestOptions options)
            throws AtlanException {
        return save(List.of(value), replaceAtlanTags, options);
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
    public AssetMutationResponse save(List<Asset> values, boolean replaceAtlanTags) throws AtlanException {
        return save(values, replaceAtlanTags, null);
    }

    /**
     * Creates any assets, optionally overwriting the existing assets' Atlan tags and entirely
     * ignoring any custom metadata.
     *
     * @param values assets to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @param options to override default client settings
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public AssetMutationResponse save(List<Asset> values, boolean replaceAtlanTags, RequestOptions options)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(client),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=false&overwriteBusinessAttributes=false",
                        bulk_endpoint, replaceAtlanTags));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, beq, AssetMutationResponse.class, options);
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
    public AssetMutationResponse saveMergingCM(List<Asset> values, boolean replaceAtlanTags) throws AtlanException {
        return saveMergingCM(values, replaceAtlanTags, null);
    }

    /**
     * Creates any assets, optionally overwriting the existing assets' Atlan tags and merging any
     * provided custom metadata values (but leaving any existing custom metadata values as-is).
     *
     * @param values assets to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @param options to override default client settings
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public AssetMutationResponse saveMergingCM(List<Asset> values, boolean replaceAtlanTags, RequestOptions options)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(client),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=true&overwriteBusinessAttributes=false",
                        bulk_endpoint, replaceAtlanTags));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, beq, AssetMutationResponse.class, options);
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
    public AssetMutationResponse saveReplacingCM(List<Asset> values, boolean replaceAtlanTags) throws AtlanException {
        return saveReplacingCM(values, replaceAtlanTags, null);
    }

    /**
     * Creates any assets, optionally overwriting the existing assets' Atlan tags and replacing all
     * custom metadata values on the asset with the ones provided (wiping out any existing custom metadata
     * on the asset that is not also provided in the request).
     *
     * @param values assets to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @param options to override default client settings
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public AssetMutationResponse saveReplacingCM(List<Asset> values, boolean replaceAtlanTags, RequestOptions options)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                client.getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=true&overwriteBusinessAttributes=true",
                        bulk_endpoint, replaceAtlanTags));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        return ApiResource.request(client, ApiResource.RequestMethod.POST, url, beq, AssetMutationResponse.class, null);
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
    public ConnectionCreationResponse save(Connection value, boolean replaceAtlanTags) throws AtlanException {
        return save(value, replaceAtlanTags, null);
    }

    /**
     * Creates any assets, optionally overwriting the existing assets' Atlan tags.
     * Custom metadata will always be entirely ignored through this method.
     *
     * @param value connection to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @param options to override default client settings
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public ConnectionCreationResponse save(Connection value, boolean replaceAtlanTags, RequestOptions options)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                client.getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=false&overwriteBusinessAttributes=false",
                        bulk_endpoint, replaceAtlanTags));
        BulkEntityRequest beq =
                BulkEntityRequest.builder().entities(List.of(value)).build();
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, beq, ConnectionCreationResponse.class, options);
    }

    /**
     * Deletes any asset.
     *
     * @param guid unique ID of the asset to delete
     * @param deleteType whether to soft-delete (archive) or hard-delete (purge) the asset
     * @return the results of the deletion
     * @throws AtlanException on any API interaction problems
     */
    public AssetDeletionResponse delete(String guid, AtlanDeleteType deleteType) throws AtlanException {
        return delete(List.of(guid), deleteType, null);
    }

    /**
     * Deletes any assets.
     *
     * @param guids unique IDs of the assets to delete
     * @param deleteType whether to soft-delete (archive) or hard-delete (purge) the assets
     * @return the results of the deletion
     * @throws AtlanException on any API interaction problems
     */
    public AssetDeletionResponse delete(List<String> guids, AtlanDeleteType deleteType) throws AtlanException {
        return delete(guids, deleteType, null);
    }

    /**
     * Deletes any assets.
     *
     * @param guids unique IDs of the assets to delete
     * @param deleteType whether to soft-delete (archive) or hard-delete (purge) the assets
     * @param options to override default client settings
     * @return the results of the deletion
     * @throws AtlanException on any API interaction problems
     */
    public AssetDeletionResponse delete(List<String> guids, AtlanDeleteType deleteType, RequestOptions options)
            throws AtlanException {
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
                        getBaseUrl(client), String.format("%s?%s&deleteType=%s", bulk_endpoint, guidList, deleteType));
                return ApiResource.request(
                        client, ApiResource.RequestMethod.DELETE, url, "", AssetDeletionResponse.class, options);
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
    public AssetMutationResponse restore(Asset value) throws AtlanException {
        return restore(List.of(value));
    }

    /**
     * Restores any asset from a soft-deleted (archived) to an active state.
     *
     * @param value asset to restore
     * @param options to override default client settings
     * @return the results of the restoration (the restored asset will be in the list of updated assets)
     * @throws AtlanException on any API interaction problems
     */
    public AssetMutationResponse restore(Asset value, RequestOptions options) throws AtlanException {
        return restore(List.of(value), options);
    }

    /**
     * Restores any assets in the list provided from a soft-deleted (archived) to active state.
     *
     * @param values assets to restore
     * @return the results of the restoration (any restored assets will be in the list of updated assets)
     * @throws AtlanException on any API interaction problems
     */
    public AssetMutationResponse restore(List<Asset> values) throws AtlanException {
        return restore(values, null);
    }

    /**
     * Restores any assets in the list provided from a soft-deleted (archived) to active state.
     *
     * @param values assets to restore
     * @param options to override default client settings
     * @return the results of the restoration (any restored assets will be in the list of updated assets)
     * @throws AtlanException on any API interaction problems
     */
    public AssetMutationResponse restore(List<Asset> values, RequestOptions options) throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(client),
                String.format(
                        "%s?replaceClassifications=false&replaceBusinessAttributes=false&overwriteBusinessAttributes=false",
                        bulk_endpoint));
        List<Asset> culled = new ArrayList<>();
        for (Asset one : values) {
            culled.add(one.trimToRequired().status(AtlanStatus.ACTIVE).build());
        }
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(culled).build();
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, beq, AssetMutationResponse.class, options);
    }

    /**
     * Retrieves any entity by its GUID.
     *
     * @param guid unique ID (GUID) of the entity to retrieve
     * @param ignoreRelationships whether to exclude the entity's relationships (true) or include them (false) in the response
     * @param minExtInfo TBC
     * @return the requested entity and its details, if it exists
     * @throws AtlanException on any API interaction problems
     */
    public AssetResponse get(String guid, boolean ignoreRelationships, boolean minExtInfo) throws AtlanException {
        return get(guid, ignoreRelationships, minExtInfo, null);
    }

    /**
     * Retrieves any entity by its GUID.
     *
     * @param guid unique ID (GUID) of the entity to retrieve
     * @param ignoreRelationships whether to exclude the entity's relationships (true) or include them (false) in the response
     * @param minExtInfo TBC
     * @param options to override default client settings
     * @return the requested entity and its details, if it exists
     * @throws AtlanException on any API interaction problems
     */
    public AssetResponse get(String guid, boolean ignoreRelationships, boolean minExtInfo, RequestOptions options)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(client),
                String.format(
                        "%s%s?ignoreRelationships=%s&minExtInfo=%s",
                        guid_endpoint, ApiResource.urlEncodeId(guid), ignoreRelationships, minExtInfo));
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", AssetResponse.class, options);
    }

    /**
     * Updates only the provided custom metadata attributes on the asset. This will leave all other custom metadata
     * attributes, even within the same named custom metadata, unchanged.
     *
     * @param guid unique identifier of the entity for which to update the custom metadata attributes
     * @param cmName the name of the custom metadata to update
     * @param values the values of the custom metadata attributes to change
     * @throws AtlanException on any API issue
     */
    public void updateCustomMetadataAttributes(String guid, String cmName, CustomMetadataAttributes values)
            throws AtlanException {
        updateCustomMetadataAttributes(guid, cmName, values, null);
    }

    /**
     * Updates only the provided custom metadata attributes on the asset. This will leave all other custom metadata
     * attributes, even within the same named custom metadata, unchanged.
     *
     * @param guid unique identifier of the entity for which to update the custom metadata attributes
     * @param cmName the name of the custom metadata to update
     * @param values the values of the custom metadata attributes to change
     * @param options to override default client settings
     * @throws AtlanException on any API issue
     */
    public void updateCustomMetadataAttributes(
            String guid, String cmName, CustomMetadataAttributes values, RequestOptions options) throws AtlanException {
        if (values != null) {
            // Ensure the custom metadata exists first — this will throw an exception if not
            client.getCustomMetadataCache().getIdForName(cmName);
            String url = String.format(
                    "%s%s",
                    getBaseUrl(client),
                    String.format(
                            "%s%s/businessmetadata?isOverwrite=false", guid_endpoint, ApiResource.urlEncodeId(guid)));
            CustomMetadataUpdateRequest cmur =
                    new CustomMetadataUpdateRequest(client, cmName, values.getAttributes(), true);
            ApiResource.request(client, ApiResource.RequestMethod.POST, url, cmur, null, options);
        }
    }

    /**
     * Replaces specific custom metadata for the specified entity. This will replace everything within that named
     * custom metadata, but not touch any of the other named custom metadata.
     *
     * @param guid unique identifier of the entity for which to replace the custom metadata
     * @param cmName the name of the custom metadata to replace
     * @param values the values to replace
     * @throws AtlanException on any API issue
     */
    public void replaceCustomMetadata(String guid, String cmName, CustomMetadataAttributes values)
            throws AtlanException {
        replaceCustomMetadata(guid, cmName, values, null);
    }

    /**
     * Replaces specific custom metadata for the specified entity. This will replace everything within that named
     * custom metadata, but not touch any of the other named custom metadata.
     *
     * @param guid unique identifier of the entity for which to replace the custom metadata
     * @param cmName the name of the custom metadata to replace
     * @param values the values to replace
     * @param options to override the default client settings
     * @throws AtlanException on any API issue
     */
    public void replaceCustomMetadata(
            String guid, String cmName, CustomMetadataAttributes values, RequestOptions options) throws AtlanException {
        String cmId = client.getCustomMetadataCache().getIdForName(cmName);
        Map<String, Object> baseMap = client.getCustomMetadataCache().getEmptyAttributes(cmName);
        if (values != null && !values.isEmpty()) {
            baseMap.putAll(values.getAttributes());
        }
        String url = String.format(
                "%s%s",
                getBaseUrl(client),
                String.format(
                        "%s%s/businessmetadata/%s",
                        guid_endpoint, ApiResource.urlEncodeId(guid), ApiResource.urlEncodeId(cmId)));
        CustomMetadataUpdateRequest cmur = new CustomMetadataUpdateRequest(client, cmName, baseMap, false);
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, cmur, null, options);
    }

    /**
     * Removes specific custom metadata from the specified entity.
     *
     * @param guid unique identifier of the entity from which to remove the custom metadata
     * @param cmName the name of the custom metadata to remove
     * @throws AtlanException on any API issue
     */
    public void removeCustomMetadata(String guid, String cmName) throws AtlanException {
        removeCustomMetadata(guid, cmName, null);
    }

    /**
     * Removes specific custom metadata from the specified entity.
     *
     * @param guid unique identifier of the entity from which to remove the custom metadata
     * @param cmName the name of the custom metadata to remove
     * @param options to override the default client settings
     * @throws AtlanException on any API issue
     */
    public void removeCustomMetadata(String guid, String cmName, RequestOptions options) throws AtlanException {
        // Ensure the custom metadata exists first — this will throw an exception if not
        client.getCustomMetadataCache().getIdForName(cmName);
        Map<String, Object> map = client.getCustomMetadataCache().getEmptyAttributes(cmName);
        CustomMetadataAttributes cma =
                CustomMetadataAttributes.builder().attributes(map).build();
        updateCustomMetadataAttributes(guid, cmName, cma, options);
        // TODO: this endpoint currently does not work (500 response)
        /* String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s%s/businessmetadata/%s",
                        endpoint, ApiResource.urlEncodeId(guid), ApiResource.urlEncodeId(cmId)));
        ApiResource.request(ApiResource.RequestMethod.DELETE, url, "", null, null); */
    }

    /**
     * Retrieves any asset by its qualifiedName.
     *
     * @param typeName type of asset to be retrieved
     * @param qualifiedName qualifiedName of the asset to be updated
     * @param ignoreRelationships whether to include relationships (false) or exclude them (true)
     * @param minExtInfo whether to minimize extra info (true) or not (false)
     * @return the requested asset
     * @throws AtlanException on any API issue, or if the asset cannot be found
     */
    public AssetResponse get(String typeName, String qualifiedName, boolean ignoreRelationships, boolean minExtInfo)
            throws AtlanException {
        return get(typeName, qualifiedName, ignoreRelationships, minExtInfo, null);
    }

    /**
     * Retrieves any asset by its qualifiedName.
     *
     * @param typeName type of asset to be retrieved
     * @param qualifiedName qualifiedName of the asset to be updated
     * @param ignoreRelationships whether to include relationships (false) or exclude them (true)
     * @param minExtInfo whether to minimize extra info (true) or not (false)
     * @param options to override the default client settings
     * @return the requested asset
     * @throws AtlanException on any API issue, or if the asset cannot be found
     */
    public AssetResponse get(
            String typeName,
            String qualifiedName,
            boolean ignoreRelationships,
            boolean minExtInfo,
            RequestOptions options)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(client),
                String.format(
                        "%s%s?attr:qualifiedName=%s&ignoreRelationships=%s&minExtInfo=%s",
                        unique_attr_endpoint,
                        ApiResource.urlEncodeId(typeName),
                        ApiResource.urlEncodeId(qualifiedName),
                        ignoreRelationships,
                        minExtInfo));
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", AssetResponse.class, options);
    }

    /**
     * Updates any simple attributes provided. Note that this only supports adding or updating
     * the values of these attributes — it is not possible to REMOVE (null) attributes through
     * this endpoint.
     *
     * @param typeName type of asset to be updated
     * @param qualifiedName qualifiedName of the asset to be updated
     * @param value the asset containing only the attributes to be updated
     * @return the set of changed entities
     * @throws AtlanException on any API issue
     */
    public AssetMutationResponse updateAttributes(String typeName, String qualifiedName, Asset value)
            throws AtlanException {
        return updateAttributes(typeName, qualifiedName, value, null);
    }

    /**
     * Updates any simple attributes provided. Note that this only supports adding or updating
     * the values of these attributes — it is not possible to REMOVE (null) attributes through
     * this endpoint.
     *
     * @param typeName type of asset to be updated
     * @param qualifiedName qualifiedName of the asset to be updated
     * @param value the asset containing only the attributes to be updated
     * @param options to override the default client settings
     * @return the set of changed entities
     * @throws AtlanException on any API issue
     */
    public AssetMutationResponse updateAttributes(
            String typeName, String qualifiedName, Asset value, RequestOptions options) throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(client),
                String.format(
                        "%s%s?attr:qualifiedName=%s",
                        unique_attr_endpoint, typeName, ApiResource.urlEncode(qualifiedName)));
        SingleEntityRequest seq = SingleEntityRequest.builder().entity(value).build();
        return ApiResource.request(
                client, ApiResource.RequestMethod.PUT, url, seq, AssetMutationResponse.class, options);
    }

    /**
     * Add one or more Atlan tags to the provided asset.
     * Note: if one or more of the provided Atlan tags already exists on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-01A}.
     *
     * @param typeName type of asset to which to add the Atlan tags
     * @param qualifiedName of the asset to which to add the Atlan tags
     * @param atlanTagNames human-readable names of the Atlan tags to add to the asset
     * @throws AtlanException on any API issues, or if any one of the Atlan tags already exists on the asset
     */
    public void addAtlanTags(String typeName, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(typeName, qualifiedName, atlanTagNames, true, true, false, null);
    }

    /**
     * Add one or more Atlan tags to the provided asset.
     * Note: if one or more of the provided Atlan tags already exists on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-01A}.
     *
     * @param typeName type of asset to which to add the Atlan tags
     * @param qualifiedName of the asset to which to add the Atlan tags
     * @param atlanTagNames human-readable names of the Atlan tags to add to the asset
     * @param options to override the default client settings
     * @throws AtlanException on any API issues, or if any one of the Atlan tags already exists on the asset
     */
    public void addAtlanTags(String typeName, String qualifiedName, List<String> atlanTagNames, RequestOptions options)
            throws AtlanException {
        addAtlanTags(typeName, qualifiedName, atlanTagNames, true, true, false, options);
    }

    /**
     * Add one or more Atlan tags to the provided asset.
     * Note: if one or more of the provided Atlan tags already exists on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-01A}.
     *
     * @param typeName type of asset to which to add the Atlan tags
     * @param qualifiedName of the asset to which to add the Atlan tags
     * @param atlanTagNames human-readable names of the Atlan tags to add to the asset
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API issues, or if any one of the Atlan tags already exists on the asset
     */
    public void addAtlanTags(
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        addAtlanTags(
                typeName,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation,
                null);
    }

    /**
     * Add one or more Atlan tags to the provided asset.
     * Note: if one or more of the provided Atlan tags already exists on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-01A}.
     *
     * @param typeName type of asset to which to add the Atlan tags
     * @param qualifiedName of the asset to which to add the Atlan tags
     * @param atlanTagNames human-readable names of the Atlan tags to add to the asset
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @param options to override the default client settings
     * @throws AtlanException on any API issues, or if any one of the Atlan tags already exists on the asset
     */
    public void addAtlanTags(
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation,
            RequestOptions options)
            throws AtlanException {
        List<AtlanTag> tags = new ArrayList<>();
        for (String atlanTagName : atlanTagNames) {
            // Note: here we need to NOT translate to an ID because the serde of
            // Atlan tag objects will automatically handle the translation for us
            tags.add(AtlanTag.builder()
                    .typeName(atlanTagName)
                    .propagate(propagate)
                    .removePropagationsOnEntityDelete(removePropagationsOnDelete)
                    .restrictPropagationThroughLineage(restrictLineagePropagation)
                    .build());
        }
        String url = String.format(
                "%s%s",
                getBaseUrl(client),
                String.format(
                        "%s%s/classifications?attr:qualifiedName=%s",
                        unique_attr_endpoint, typeName, ApiResource.urlEncode(qualifiedName)));
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, new AtlanTagList(tags), null, options);
    }

    /**
     * Removes a single Atlan tag from the provided asset.
     * Note: if the provided Atlan tag does not exist on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-06D}, unless {@code idempotent} is set to true.
     *
     * @param typeName type of asset from which to remove the Atlan tag
     * @param qualifiedName of the asset from which to remove the Atlan tag
     * @param atlanTagName human-readable name of the Atlan tag to remove from the asset
     * @param idempotent whether to throw an error if the Atlan tag does not exist on the asset (false) or behave
     *                   the same as if the Atlan tag was removed even though it did not exist on the asset (true)
     * @throws AtlanException on any API issue, or if the Atlan tag does not exist on the asset
     */
    public void removeAtlanTag(String typeName, String qualifiedName, String atlanTagName, boolean idempotent)
            throws AtlanException {
        removeAtlanTag(typeName, qualifiedName, atlanTagName, idempotent, null);
    }

    /**
     * Removes a single Atlan tag from the provided asset.
     * Note: if the provided Atlan tag does not exist on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-06D}, unless {@code idempotent} is set to true.
     *
     * @param typeName type of asset from which to remove the Atlan tag
     * @param qualifiedName of the asset from which to remove the Atlan tag
     * @param atlanTagName human-readable name of the Atlan tag to remove from the asset
     * @param idempotent whether to throw an error if the Atlan tag does not exist on the asset (false) or behave
     *                   the same as if the Atlan tag was removed even though it did not exist on the asset (true)
     * @param options to override the default client settings
     * @throws AtlanException on any API issue, or if the Atlan tag does not exist on the asset
     */
    public void removeAtlanTag(
            String typeName, String qualifiedName, String atlanTagName, boolean idempotent, RequestOptions options)
            throws AtlanException {
        // Note: here we need to directly translate to an ID because it is a path
        // parameter in the API call
        String atlanTagId = client.getAtlanTagCache().getIdForName(atlanTagName);
        String url = String.format(
                "%s%s",
                getBaseUrl(client),
                String.format(
                        "%s%s/classification/%s?attr:qualifiedName=%s",
                        unique_attr_endpoint, typeName, atlanTagId, ApiResource.urlEncode(qualifiedName)));
        try {
            ApiResource.request(client, ApiResource.RequestMethod.DELETE, url, "", null, options);
        } catch (InvalidRequestException e) {
            if (idempotent && e.getMessage().equals("ATLAS-400-00-06D")) {
                log.debug(
                        "Attempted to remove Atlan tag '{}' from asset that does not have the Atlan tag, ignoring: {}",
                        atlanTagName,
                        qualifiedName);
            } else {
                throw e;
            }
        }
    }

    /**
     * Run the requested search.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public IndexSearchResponse search(IndexSearchRequest request) throws AtlanException {
        return search(request, null);
    }

    /**
     * Run the requested search.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @param options to override default client settings
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public IndexSearchResponse search(IndexSearchRequest request, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(client), search_endpoint);
        if (request.getDsl().getSort() == null || request.getDsl().getSort().isEmpty()) {
            // If no sort has been provided, explicitly sort by _doc for consistency of paging
            // operations
            request = request.toBuilder()
                    .dsl(request.getDsl().toBuilder()
                            .sortOption(SortOptions.of(
                                    s -> s.field(f -> f.field("_doc").order(SortOrder.Asc))))
                            .build())
                    .build();
        }
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, IndexSearchResponse.class, options);
    }

    /**
     * Fetch the requested lineage. This is an older, slower operation that may be deprecated
     * in the future. If possible, use the lineage list operation instead.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     * @see #lineage(LineageListRequest)
     */
    public LineageResponse lineage(LineageRequest request) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(client), lineage_endpoint);
        return ApiResource.request(client, ApiResource.RequestMethod.POST, url, request, LineageResponse.class, null);
    }

    /**
     * Retrieve lineage using the higher-performance "list" API.
     *
     * @param request detailing the lineage to retrieve
     * @return the results of the lineage
     * @throws AtlanException on any API interaction problems
     */
    public LineageListResponse lineage(LineageListRequest request) throws AtlanException {
        return lineage(request, null);
    }

    /**
     * Retrieve lineage using the higher-performance "list" API.
     *
     * @param request detailing the lineage to retrieve
     * @param options to override default client settings
     * @return the results of the lineage
     * @throws AtlanException on any API interaction problems
     */
    public LineageListResponse lineage(LineageListRequest request, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(client), lineage_list_endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, LineageListResponse.class, options);
    }

    /**
     * Request class for handling Atlan tag additions.
     */
    public static class AtlanTagList extends AtlanObject {
        private final List<AtlanTag> tags;

        public AtlanTagList(List<AtlanTag> tags) {
            this.tags = tags;
        }

        @Override
        public String toJson() {
            try {
                return Atlan.getDefaultClient().writeValueAsString(tags);
            } catch (IOException e) {
                throw new RuntimeException("Unable to serialize list of Atlan tags.", e);
            }
        }
    }

    /**
     * Request class for updating a single asset.
     */
    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    static class SingleEntityRequest extends AtlanObject {
        /** The asset to update. */
        Asset entity;
    }

    /**
     * Request class for updating custom metadata on an entity.
     */
    @EqualsAndHashCode(callSuper = false)
    static class CustomMetadataUpdateRequest extends AtlanObject {

        /** Whether to include the custom metadata name as an outer wrapper (true) or not (false). */
        private final transient boolean includeName;

        /** Human-readable name of the custom metadata. */
        private final transient String name;

        /** Mapping of custom metadata attributes to values, all by internal IDs. */
        private final transient Map<String, Object> attributes;

        private final transient AtlanClient client;

        public CustomMetadataUpdateRequest(
                AtlanClient client, String name, Map<String, Object> attributes, boolean includeName) {
            this.client = client;
            this.name = name;
            this.attributes = attributes;
            this.includeName = includeName;
        }

        /**
         * Convert the embedded map directly into JSON, rather than leaving it with a wrapped 'attributes'.
         *
         * @return the JSON for the embedded map
         */
        @Override
        public String toJson() {
            Map<String, Object> businessMetadataAttributes = new LinkedHashMap<>();
            try {
                client.getCustomMetadataCache().getIdMapFromNameMap(name, attributes, businessMetadataAttributes);
                if (includeName) {
                    Map<String, Map<String, Object>> wrapped = new LinkedHashMap<>();
                    String cmId = client.getCustomMetadataCache().getIdForName(name);
                    wrapped.put(cmId, businessMetadataAttributes);
                    return Atlan.getDefaultClient().writeValueAsString(wrapped);
                } else {
                    return Atlan.getDefaultClient().writeValueAsString(businessMetadataAttributes);
                }
            } catch (AtlanException | IOException e) {
                throw new RuntimeException(
                        "Unable to serialize custom metadata for '" + name + "' with: " + attributes, e);
            }
        }
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
