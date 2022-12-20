/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AssetResponse;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.net.ApiResource;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * API endpoints for operating on a single entity, based on its unique ID (GUID).
 */
@Slf4j
public class EntityGuidEndpoint {

    private static final String endpoint = "/api/meta/entity/guid/";

    /** Retrieves any entity by its GUID. */

    /**
     * Retrieves any entity by its GUID.
     *
     * @param guid unique ID (GUID) of the entity to retrieve
     * @param ignoreRelationships whether to exclude the entity's relationships (true) or include them (false) in the response
     * @param minExtInfo TBC
     * @return the requested entity and its details, if it exists
     * @throws AtlanException on any API interaction problems
     */
    public static AssetResponse retrieve(String guid, boolean ignoreRelationships, boolean minExtInfo)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getBaseUrl(),
                String.format(
                        "%s%s?ignoreRelationships=%s&minExtInfo=%s",
                        endpoint, ApiResource.urlEncodeId(guid), ignoreRelationships, minExtInfo));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", AssetResponse.class, null);
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
    public static void updateCustomMetadataAttributes(String guid, String cmName, CustomMetadataAttributes values)
            throws AtlanException {
        String cmId = CustomMetadataCache.getIdForName(cmName);
        if (cmId != null) {
            if (values != null) {
                String url = String.format(
                        "%s%s",
                        Atlan.getBaseUrl(),
                        String.format(
                                "%s%s/businessmetadata?isOverwrite=false", endpoint, ApiResource.urlEncodeId(guid)));
                CustomMetadataUpdateRequest cmur =
                        new CustomMetadataUpdateRequest(cmName, values.getAttributes(), true);
                ApiResource.request(ApiResource.RequestMethod.POST, url, cmur, null, null);
            }
        } else {
            throw new InvalidRequestException(
                    "No custom metadata found with the provided name: " + cmName,
                    "customMetadataName",
                    "ATLAN-JAVA-CLIENT-400-030",
                    400,
                    null);
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
    public static void replaceCustomMetadata(String guid, String cmName, CustomMetadataAttributes values)
            throws AtlanException {
        String cmId = CustomMetadataCache.getIdForName(cmName);
        if (cmId != null) {
            if (values != null) {
                String url = String.format(
                        "%s%s",
                        Atlan.getBaseUrl(),
                        String.format(
                                "%s%s/businessmetadata/%s",
                                endpoint, ApiResource.urlEncodeId(guid), ApiResource.urlEncodeId(cmId)));
                CustomMetadataUpdateRequest cmur =
                        new CustomMetadataUpdateRequest(cmName, values.getAttributes(), false);
                ApiResource.request(ApiResource.RequestMethod.POST, url, cmur, null, null);
            }
        } else {
            throw new InvalidRequestException(
                    "No custom metadata found with the provided name: " + cmName,
                    "customMetadataName",
                    "ATLAN-JAVA-CLIENT-400-030",
                    400,
                    null);
        }
    }

    /**
     * Removes specific custom metadata from the specified entity.
     *
     * @param guid unique identifier of the entity from which to remove the custom metadata
     * @param cmName the name of the custom metadata to remove
     * @throws AtlanException on any API issue
     */
    public static void removeCustomMetadata(String guid, String cmName) throws AtlanException {
        String cmId = CustomMetadataCache.getIdForName(cmName);
        if (cmId != null) {
            Map<String, Object> map = CustomMetadataCache.getEmptyAttributes(cmName);
            CustomMetadataAttributes cma =
                    CustomMetadataAttributes.builder().attributes(map).build();
            updateCustomMetadataAttributes(guid, cmName, cma);
            // TODO: this endpoint currently does not work (500 response)
            /* String url = String.format(
                    "%s%s",
                    Atlan.getBaseUrl(),
                    String.format(
                            "%s%s/businessmetadata/%s",
                            endpoint, ApiResource.urlEncodeId(guid), ApiResource.urlEncodeId(cmId)));
            ApiResource.request(ApiResource.RequestMethod.DELETE, url, "", null, null); */
        } else {
            throw new InvalidRequestException(
                    "No custom metadata found with the provided name: " + cmName,
                    "customMetadataName",
                    "ATLAN-JAVA-CLIENT-400-030",
                    400,
                    null);
        }
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
        public String toJson() {
            Map<String, Object> businessMetadataAttributes = new LinkedHashMap<>();
            try {
                CustomMetadataCache.getIdMapFromNameMap(name, attributes, businessMetadataAttributes);
                if (includeName) {
                    Map<String, Map<String, Object>> wrapped = new LinkedHashMap<>();
                    String cmId = CustomMetadataCache.getIdForName(name);
                    wrapped.put(cmId, businessMetadataAttributes);
                    return Serde.mapper.writeValueAsString(wrapped);
                } else {
                    return Serde.mapper.writeValueAsString(businessMetadataAttributes);
                }
            } catch (AtlanException | JsonProcessingException e) {
                log.error("Unable to serialize custom metadata for '{}' with: {}", name, attributes, e);
            }
            return null;
        }
    }
}
