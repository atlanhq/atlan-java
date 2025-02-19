/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.GlossaryCategory;
import com.atlan.model.assets.IReferenceable;
import com.atlan.model.core.*;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.lineage.LineageListRequest;
import com.atlan.model.lineage.LineageListResponse;
import com.atlan.model.search.*;
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
    private static final String lineage_list_endpoint = "/lineage/list";

    public AssetEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Start a fluent search that will return all assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) assets will be included.
     *
     * @return a fluent search that includes all assets
     */
    public FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(false);
    }

    /**
     * Start a fluent search that will return all assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) assets will be included
     * @return a fluent search that includes all assets
     */
    public FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(Asset.SUPER_TYPE_NAMES.eq("Referenceable"));
        if (!includeArchived) {
            builder.active();
        }
        return builder;
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
        String url = String.format("%s%s", getBaseUrl(), audit_endpoint);
        boolean missingSort =
                request.getDsl().getSort() == null || request.getDsl().getSort().isEmpty();
        boolean missingGuidSort = true;
        if (!missingSort) {
            // If there is some sort, see whether GUID is already included
            for (SortOptions option : request.getDsl().getSort()) {
                if (option.isField()) {
                    String fieldName = option.field().field();
                    if (AuditSearchRequest.ENTITY_ID.getKeywordFieldName().equals(fieldName)) {
                        missingGuidSort = false;
                        break;
                    }
                }
            }
        }
        if (missingGuidSort) {
            // If there is no sort by GUID, always add it as a final (tie-breaker) criteria
            // to ensure there is consistent paging (unfortunately sorting by _doc still has duplicates
            // across large number of pages)
            request = request.toBuilder()
                    .dsl(request.getDsl().toBuilder()
                            .sortOption(AuditSearchRequest.ENTITY_ID.order(SortOrder.Asc))
                            .build())
                    .build();
        }
        AuditSearchResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, AuditSearchResponse.class, options);
        response.setClient(client);
        response.setRequest(request);
        return response;
    }

    /**
     * Creates any asset, not updating any of the existing asset's Atlan tags and entirely
     * ignoring any custom metadata.
     *
     * @param value asset to upsert
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public AsyncCreationResponse save(Asset value) throws AtlanException {
        return save(value, null);
    }

    /**
     * Creates any asset, not updating any of the existing asset's Atlan tags and entirely
     * ignoring any custom metadata.
     *
     * @param value asset to upsert
     * @param options to override default client settings
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public AsyncCreationResponse save(Asset value, RequestOptions options) throws AtlanException {
        return save(value, false, options);
    }

    /**
     * Creates any asset, optionally overwriting an existing asset's Atlan tags and entirely
     * ignoring any custom metadata.
     *
     * @param value asset to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public AsyncCreationResponse save(Asset value, boolean replaceAtlanTags) throws AtlanException {
        return save(List.of(value), replaceAtlanTags, null);
    }

    /**
     * Creates any asset, optionally overwriting an existing asset's Atlan tags and entirely
     * ignoring any custom metadata.
     *
     * @param value asset to upsert
     * @param replaceAtlanTags whether to overwrite any existing Atlan tags (true) or not (false)
     * @param options to override default client settings
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public AsyncCreationResponse save(Asset value, boolean replaceAtlanTags, RequestOptions options)
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
    public AsyncCreationResponse save(List<Asset> values, boolean replaceAtlanTags) throws AtlanException {
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
    public AsyncCreationResponse save(List<Asset> values, boolean replaceAtlanTags, RequestOptions options)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=false&overwriteBusinessAttributes=false",
                        bulk_endpoint, replaceAtlanTags));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        AsyncCreationResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, beq, AsyncCreationResponse.class, options);
        response.setClient(client);
        return response;
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
    public AsyncCreationResponse saveMergingCM(List<Asset> values, boolean replaceAtlanTags) throws AtlanException {
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
    public AsyncCreationResponse saveMergingCM(List<Asset> values, boolean replaceAtlanTags, RequestOptions options)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=true&overwriteBusinessAttributes=false",
                        bulk_endpoint, replaceAtlanTags));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        AsyncCreationResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, beq, AsyncCreationResponse.class, options);
        response.setClient(client);
        return response;
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
    public AsyncCreationResponse saveReplacingCM(List<Asset> values, boolean replaceAtlanTags) throws AtlanException {
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
    public AsyncCreationResponse saveReplacingCM(List<Asset> values, boolean replaceAtlanTags, RequestOptions options)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=true&overwriteBusinessAttributes=true",
                        bulk_endpoint, replaceAtlanTags));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        AsyncCreationResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, beq, AsyncCreationResponse.class, options);
        response.setClient(client);
        return response;
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
     * @throws InvalidRequestException if you attempt to archive a category, as categories can only be purged
     */
    public AssetDeletionResponse delete(List<String> guids, AtlanDeleteType deleteType, RequestOptions options)
            throws AtlanException {
        if (guids != null) {
            if (deleteType == AtlanDeleteType.SOFT) {
                List<String> categoryGuids = new ArrayList<>();
                client.assets.select().where(Asset.GUID.in(guids)).stream()
                        .filter(a -> a instanceof GlossaryCategory)
                        .forEach(c -> categoryGuids.add(c.getGuid()));
                if (!categoryGuids.isEmpty()) {
                    throw new InvalidRequestException(
                            ErrorCode.CATEGORIES_CANNOT_BE_ARCHIVED, String.join(",", categoryGuids));
                }
            }
            StringBuilder guidList = new StringBuilder();
            for (String guid : guids) {
                if (guid != null) {
                    guidList.append("guid=").append(guid).append("&");
                }
            }
            if (!guidList.isEmpty()) {
                // Remove the final comma
                guidList.setLength(guidList.length() - 1);
                String url = String.format(
                        "%s%s",
                        getBaseUrl(), String.format("%s?%s&deleteType=%s", bulk_endpoint, guidList, deleteType));
                AssetDeletionResponse response = ApiResource.request(
                        client, ApiResource.RequestMethod.DELETE, url, "", AssetDeletionResponse.class, options);
                response.setClient(client);
                return response;
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
                getBaseUrl(),
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
     * Retrieves any asset by its GUID.
     *
     * @param guid unique ID (GUID) of the asset to retrieve
     * @param ignoreRelationships whether to exclude the asset's relationships (true) or include them (false) in the response
     * @param minExtInfo TBC
     * @return the requested asset and its details, if it exists
     * @throws AtlanException on any API interaction problems
     */
    public AssetResponse get(String guid, boolean ignoreRelationships, boolean minExtInfo) throws AtlanException {
        return get(guid, ignoreRelationships, minExtInfo, null);
    }

    /**
     * Retrieves any asset by its GUID.
     *
     * @param guid unique ID (GUID) of the asset to retrieve
     * @param ignoreRelationships whether to exclude the asset's relationships (true) or include them (false) in the response
     * @param minExtInfo TBC
     * @param options to override default client settings
     * @return the requested asset and its details, if it exists
     * @throws AtlanException on any API interaction problems
     */
    public AssetResponse get(String guid, boolean ignoreRelationships, boolean minExtInfo, RequestOptions options)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s%s?ignoreRelationships=%s&minExtInfo=%s",
                        guid_endpoint, ApiResource.urlEncodeId(guid), ignoreRelationships, minExtInfo));
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", AssetResponse.class, options);
    }

    /**
     * Updates only the provided custom metadata attributes on the asset. This will leave all other custom metadata
     * attributes, even within the same named custom metadata, unchanged.
     *
     * @param guid unique identifier of the asset for which to update the custom metadata attributes
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
     * @param guid unique identifier of the asset for which to update the custom metadata attributes
     * @param cmName the name of the custom metadata to update
     * @param values the values of the custom metadata attributes to change
     * @param options to override default client settings
     * @throws AtlanException on any API issue
     */
    public void updateCustomMetadataAttributes(
            String guid, String cmName, CustomMetadataAttributes values, RequestOptions options) throws AtlanException {
        if (values != null) {
            // Ensure the custom metadata exists first — this will throw an exception if not
            client.getCustomMetadataCache().getSidForName(cmName);
            String url = String.format(
                    "%s%s",
                    getBaseUrl(),
                    String.format(
                            "%s%s/businessmetadata?isOverwrite=false", guid_endpoint, ApiResource.urlEncodeId(guid)));
            CustomMetadataUpdateRequest cmur = new CustomMetadataUpdateRequest(cmName, values.getAttributes(), true);
            ApiResource.request(client, ApiResource.RequestMethod.POST, url, cmur, options);
        }
    }

    /**
     * Replaces specific custom metadata for the specified asset. This will replace everything within that named
     * custom metadata, but not touch any of the other named custom metadata.
     *
     * @param guid unique identifier of the asset for which to replace the custom metadata
     * @param cmName the name of the custom metadata to replace
     * @param values the values to replace
     * @throws AtlanException on any API issue
     */
    public void replaceCustomMetadata(String guid, String cmName, CustomMetadataAttributes values)
            throws AtlanException {
        replaceCustomMetadata(guid, cmName, values, null);
    }

    /**
     * Replaces specific custom metadata for the specified asset. This will replace everything within that named
     * custom metadata, but not touch any of the other named custom metadata.
     *
     * @param guid unique identifier of the asset for which to replace the custom metadata
     * @param cmName the name of the custom metadata to replace
     * @param values the values to replace
     * @param options to override the default client settings
     * @throws AtlanException on any API issue
     */
    public void replaceCustomMetadata(
            String guid, String cmName, CustomMetadataAttributes values, RequestOptions options) throws AtlanException {
        String cmId = client.getCustomMetadataCache().getSidForName(cmName);
        Map<String, Object> baseMap = client.getCustomMetadataCache().getEmptyAttributes(cmName);
        if (values != null && !values.isEmpty()) {
            baseMap.putAll(values.getAttributes());
        }
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s%s/businessmetadata/%s",
                        guid_endpoint, ApiResource.urlEncodeId(guid), ApiResource.urlEncodeId(cmId)));
        CustomMetadataUpdateRequest cmur = new CustomMetadataUpdateRequest(cmName, baseMap, false);
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, cmur, options);
    }

    /**
     * Removes specific custom metadata from the specified asset.
     *
     * @param guid unique identifier of the asset from which to remove the custom metadata
     * @param cmName the name of the custom metadata to remove
     * @throws AtlanException on any API issue
     */
    public void removeCustomMetadata(String guid, String cmName) throws AtlanException {
        removeCustomMetadata(guid, cmName, null);
    }

    /**
     * Removes specific custom metadata from the specified asset.
     *
     * @param guid unique identifier of the asset from which to remove the custom metadata
     * @param cmName the name of the custom metadata to remove
     * @param options to override the default client settings
     * @throws AtlanException on any API issue
     */
    public void removeCustomMetadata(String guid, String cmName, RequestOptions options) throws AtlanException {
        // Ensure the custom metadata exists first — this will throw an exception if not
        client.getCustomMetadataCache().getSidForName(cmName);
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
     * @param qualifiedName qualifiedName of the asset to be retrieved
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
                getBaseUrl(),
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
                getBaseUrl(),
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
        addAtlanTags(typeName, qualifiedName, atlanTagNames, false, true, false, null);
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
        addAtlanTags(typeName, qualifiedName, atlanTagNames, false, true, false, options);
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
     * @param restrictHierarchyPropagation whether to avoid propagating through hierarchy (true) or do propagate through hierarchy (false)
     * @throws AtlanException on any API issues, or if any one of the Atlan tags already exists on the asset
     */
    public void addAtlanTags(
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation,
            boolean restrictHierarchyPropagation)
            throws AtlanException {
        addAtlanTags(
                typeName,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation,
                restrictHierarchyPropagation,
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
        addAtlanTags(
                typeName,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation,
                false,
                options);
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
     * @param restrictHierarchyPropagation whether to avoid propagating through hierarchy (true) or do propagate through hierarchy (false)
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
            boolean restrictHierarchyPropagation,
            RequestOptions options)
            throws AtlanException {
        modifyTags(
                typeName,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation,
                restrictHierarchyPropagation,
                options,
                ApiResource.RequestMethod.POST);
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
        String atlanTagId = client.getAtlanTagCache().getSidForName(atlanTagName);
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s%s/classification/%s?attr:qualifiedName=%s",
                        unique_attr_endpoint, typeName, atlanTagId, ApiResource.urlEncode(qualifiedName)));
        try {
            ApiResource.request(client, ApiResource.RequestMethod.DELETE, url, "", options);
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
     * Update one or more Atlan tags on the provided asset.
     * Note: if one or more of the provided Atlan tags does not exist on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-06D}.
     *
     * @param typeName type of asset on which to update the Atlan tags
     * @param qualifiedName of the asset on which to update the Atlan tags
     * @param atlanTagNames human-readable names of the Atlan tags to update on the asset
     * @param propagate whether to propagate the Atlan tags (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API issues, or if any one of the Atlan tags already exists on the asset
     */
    public void updateAtlanTags(
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        updateAtlanTags(
                typeName,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation,
                null);
    }

    /**
     * Update one or more Atlan tags on the provided asset.
     * Note: if one or more of the provided Atlan tags does not exist on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-06D}.
     *
     * @param typeName type of asset on which to update the Atlan tags
     * @param qualifiedName of the asset on which to update the Atlan tags
     * @param atlanTagNames human-readable names of the Atlan tags to update on the asset
     * @param propagate whether to propagate the Atlan tags (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @param restrictHierarchyPropagation whether to avoid propagating through hierarchy (true) or do propagate through hierarchy (false)
     * @throws AtlanException on any API issues, or if any one of the Atlan tags already exists on the asset
     */
    public void updateAtlanTags(
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation,
            boolean restrictHierarchyPropagation)
            throws AtlanException {
        updateAtlanTags(
                typeName,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation,
                restrictHierarchyPropagation,
                null);
    }

    /**
     * Update one or more Atlan tags on the provided asset.
     * Note: if one or more of the provided Atlan tags does not exist on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-06D}.
     *
     * @param typeName type of asset on which to update the Atlan tags
     * @param qualifiedName of the asset on which to update the Atlan tags
     * @param atlanTagNames human-readable names of the Atlan tags to update on the asset
     * @param propagate whether to propagate the Atlan tags (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @param options to override the default client settings
     * @throws AtlanException on any API issues, or if any one of the Atlan tags already exists on the asset
     */
    public void updateAtlanTags(
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation,
            RequestOptions options)
            throws AtlanException {
        updateAtlanTags(
                typeName,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation,
                false,
                options);
    }

    /**
     * Update one or more Atlan tags on the provided asset.
     * Note: if one or more of the provided Atlan tags does not exist on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-06D}.
     *
     * @param typeName type of asset on which to update the Atlan tags
     * @param qualifiedName of the asset on which to update the Atlan tags
     * @param atlanTagNames human-readable names of the Atlan tags to update on the asset
     * @param propagate whether to propagate the Atlan tags (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @param restrictHierarchyPropagation whether to avoid propagating through hierarchy (true) or do propagate through hierarchy (false)
     * @param options to override the default client settings
     * @throws AtlanException on any API issues, or if any one of the Atlan tags already exists on the asset
     */
    public void updateAtlanTags(
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation,
            boolean restrictHierarchyPropagation,
            RequestOptions options)
            throws AtlanException {
        modifyTags(
                typeName,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation,
                restrictHierarchyPropagation,
                options,
                ApiResource.RequestMethod.PUT);
    }

    /**
     * Add or update one or more Atlan tags on the provided asset.
     * The only difference between the two operations is the method of the API call: PUT for an update, POST for an addition.
     *
     * @param typeName type of asset on which to update the Atlan tags
     * @param qualifiedName of the asset on which to update the Atlan tags
     * @param atlanTagNames human-readable names of the Atlan tags to update on the asset
     * @param propagate whether to propagate the Atlan tags (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @param restrictHierarchyPropagation whether to avoid propagating through hierarchy (true) or do propagate through hierarchy (false)
     * @param options to override the default client settings
     * @param method of the call: PUT for an update, POST for an addition
     * @throws AtlanException on any API issues, or if any one of the Atlan tags already exists on the asset
     */
    private void modifyTags(
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation,
            boolean restrictHierarchyPropagation,
            RequestOptions options,
            ApiResource.RequestMethod method)
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
                    .restrictPropagationThroughHierarchy(restrictHierarchyPropagation)
                    .build());
        }
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s%s/classifications?attr:qualifiedName=%s",
                        unique_attr_endpoint, typeName, ApiResource.urlEncode(qualifiedName)));
        ApiResource.request(client, method, url, new AtlanTagList(tags), options);
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
        String url = String.format("%s%s", getBaseUrl(), search_endpoint);
        boolean missingSort =
                request.getDsl().getSort() == null || request.getDsl().getSort().isEmpty();
        boolean missingGuidSort = true;
        if (!missingSort) {
            // If there is some sort, see whether GUID is already included
            for (SortOptions option : request.getDsl().getSort()) {
                if (option.isField()) {
                    String fieldName = option.field().field();
                    if (IReferenceable.GUID.getKeywordFieldName().equals(fieldName)) {
                        missingGuidSort = false;
                        break;
                    }
                }
            }
        }
        if (missingGuidSort) {
            // If there is no sort by GUID, always add it as a final (tie-breaker) criteria
            // to ensure there is consistent paging (unfortunately sorting by _doc still has duplicates
            // across large number of pages)
            request = request.toBuilder()
                    .dsl(request.getDsl().toBuilder()
                            .sortOption(IReferenceable.GUID.order(SortOrder.Asc))
                            .build())
                    .build();
        }
        IndexSearchResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, IndexSearchResponse.class, options);
        response.setClient(client);
        return response;
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
        String url = String.format("%s%s", getBaseUrl(), lineage_list_endpoint);
        LineageListResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, LineageListResponse.class, options);
        response.setClient(client);
        return response;
    }

    /**
     * Request class for handling Atlan tag additions.
     */
    public static class AtlanTagList extends AtlanObject {
        private static final long serialVersionUID = 2L;

        private final List<AtlanTag> tags;

        public AtlanTagList(List<AtlanTag> tags) {
            this.tags = tags;
        }

        @Override
        public String toJson(AtlanClient client) {
            try {
                return client.writeValueAsString(tags);
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
        private static final long serialVersionUID = 2L;

        /** The asset to update. */
        Asset entity;
    }

    /**
     * Request class for updating custom metadata on an asset.
     */
    @EqualsAndHashCode(callSuper = false)
    static class CustomMetadataUpdateRequest extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Whether to include the custom metadata name as an outer wrapper (true) or not (false). */
        private final transient boolean includeName;

        /** Human-readable name of the custom metadata. */
        private final transient String name;

        /** Mapping of custom metadata attributes to values, all by internal IDs. */
        private final transient Map<String, Object> attributes;

        public CustomMetadataUpdateRequest(String name, Map<String, Object> attributes, boolean includeName) {
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
        public String toJson(AtlanClient client) {
            Map<String, Object> businessMetadataAttributes = new LinkedHashMap<>();
            try {
                client.getCustomMetadataCache().getIdMapFromNameMap(name, attributes, businessMetadataAttributes);
                if (includeName) {
                    Map<String, Map<String, Object>> wrapped = new LinkedHashMap<>();
                    String cmId = client.getCustomMetadataCache().getSidForName(name);
                    wrapped.put(cmId, businessMetadataAttributes);
                    return client.writeValueAsString(wrapped);
                } else {
                    return client.writeValueAsString(businessMetadataAttributes);
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
        private static final long serialVersionUID = 2L;

        /** List of assets to operate on in bulk. */
        List<Asset> entities;
    }
}
