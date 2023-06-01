/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.cache.AtlanTagCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AssetResponse;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.core.AtlanTag;
import com.atlan.net.ApiResource;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * API endpoints for operating on a single asset (entity), based on its unique attributes (primarily {@code qualifiedName}).
 */
@Slf4j
public class EntityUniqueAttributesEndpoint extends AtlasEndpoint {

    private static final String endpoint = "/entity/uniqueAttribute/type/";

    /**
     * Retrieves any asset by its qualifiedName.
     *
     * @param typeName type of asset to be retrieved
     * @param qualifiedName qualifiedName of the asset to be updated
     * @param ignoreRelationships whether to include relationships (false) or exclude them (true)
     * @param minExtInfo whether to minimize extra info (true) or not (false)
     */
    public static AssetResponse retrieve(
            String typeName, String qualifiedName, boolean ignoreRelationships, boolean minExtInfo)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s%s?attr:qualifiedName=%s&ignoreRelationships=%s&minExtInfo=%s",
                        endpoint,
                        ApiResource.urlEncodeId(typeName),
                        ApiResource.urlEncodeId(qualifiedName),
                        ignoreRelationships,
                        minExtInfo));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", AssetResponse.class, null);
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
    public static AssetMutationResponse updateAttributes(String typeName, String qualifiedName, Asset value)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format("%s%s?attr:qualifiedName=%s", endpoint, typeName, ApiResource.urlEncode(qualifiedName)));
        SingleEntityRequest seq = SingleEntityRequest.builder().entity(value).build();
        return ApiResource.request(ApiResource.RequestMethod.PUT, url, seq, AssetMutationResponse.class, null);
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
    public static void addAtlanTags(String typeName, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        addAtlanTags(typeName, qualifiedName, atlanTagNames, true, true, false);
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
    public static void addAtlanTags(
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
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
                getBaseUrl(),
                String.format(
                        "%s%s/classifications?attr:qualifiedName=%s",
                        endpoint, typeName, ApiResource.urlEncode(qualifiedName)));
        ApiResource.request(ApiResource.RequestMethod.POST, url, new AtlanTagList(tags), null, null);
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
    public static void removeAtlanTag(String typeName, String qualifiedName, String atlanTagName, boolean idempotent)
            throws AtlanException {
        // Note: here we need to directly translate to an ID because it is a path
        // parameter in the API call
        String atlanTagId = AtlanTagCache.getIdForName(atlanTagName);
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s%s/classification/%s?attr:qualifiedName=%s",
                        endpoint, typeName, atlanTagId, ApiResource.urlEncode(qualifiedName)));
        try {
            ApiResource.request(ApiResource.RequestMethod.DELETE, url, "", null, null);
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
                return Serde.mapper.writeValueAsString(tags);
            } catch (JsonProcessingException e) {
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
}
