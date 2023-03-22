/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.cache.ClassificationCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AssetResponse;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.core.Classification;
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
     * Add one or more classifications to the provided asset.
     * Note: if one or more of the provided classifications already exists on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-01A}.
     *
     * @param typeName type of asset to which to add the classifications
     * @param qualifiedName of the asset to which to add the classifications
     * @param classificationNames human-readable names of the classifications to add to the asset
     * @throws AtlanException on any API issues, or if any one of the classifications already exists on the asset
     */
    public static void addClassifications(String typeName, String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        addClassifications(typeName, qualifiedName, classificationNames, true, true, false);
    }

    /**
     * Add one or more classifications to the provided asset.
     * Note: if one or more of the provided classifications already exists on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-01A}.
     *
     * @param typeName type of asset to which to add the classifications
     * @param qualifiedName of the asset to which to add the classifications
     * @param classificationNames human-readable names of the classifications to add to the asset
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API issues, or if any one of the classifications already exists on the asset
     */
    public static void addClassifications(
            String typeName,
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        List<Classification> classifications = new ArrayList<>();
        for (String classificationName : classificationNames) {
            // Note: here we need to NOT translate to an ID because the serde of
            // Classification objects will automatically handle the translation for us
            classifications.add(Classification.builder()
                    .typeName(classificationName)
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
        ApiResource.request(ApiResource.RequestMethod.POST, url, new ClassificationList(classifications), null, null);
    }

    /**
     * Removes a single classification from the provided asset.
     * Note: if the provided classification does not exist on the asset, an InvalidRequestException
     * will be thrown with error code {@code ATLAS-400-00-06D}, unless {@code idempotent} is set to true.
     *
     * @param typeName type of asset from which to remove the classification
     * @param qualifiedName of the asset from which to remove the classification
     * @param classificationName human-readable name of the classification to remove from the asset
     * @param idempotent whether to throw an error if the classification does not exist on the asset (false) or behave
     *                   the same as if the classification was removed even though it did not exist on the asset (true)
     * @throws AtlanException on any API issue, or if the classification does not exist on the asset
     */
    public static void removeClassification(
            String typeName, String qualifiedName, String classificationName, boolean idempotent)
            throws AtlanException {
        // Note: here we need to directly translate to an ID because it is a path
        // parameter in the API call
        String classificationId = ClassificationCache.getIdForName(classificationName);
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format(
                        "%s%s/classification/%s?attr:qualifiedName=%s",
                        endpoint, typeName, classificationId, ApiResource.urlEncode(qualifiedName)));
        try {
            ApiResource.request(ApiResource.RequestMethod.DELETE, url, "", null, null);
        } catch (InvalidRequestException e) {
            if (idempotent && e.getMessage().equals("ATLAS-400-00-06D")) {
                log.debug(
                        "Attempted to remove classification '{}' from asset that does not have the classification, ignoring: {}",
                        classificationName,
                        qualifiedName);
            } else {
                throw e;
            }
        }
    }

    /**
     * Request class for handling classification additions.
     */
    public static class ClassificationList extends AtlanObject {
        private final List<Classification> classifications;

        public ClassificationList(List<Classification> classifications) {
            this.classifications = classifications;
        }

        @Override
        public String toJson() {
            try {
                return Serde.mapper.writeValueAsString(classifications);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Unable to serialize list of classifications.", e);
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
