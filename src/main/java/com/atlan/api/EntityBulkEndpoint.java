/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.requests.BulkEntityRequest;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.net.ApiResource;
import java.util.Collections;
import java.util.List;

/**
 * API endpoints for operating on multiple entities at the same time (in bulk).
 */
public class EntityBulkEndpoint {

    private static final String endpoint = "/api/meta/entity/bulk";

    /**
     * Creates any entity, optionally overwriting an existing entity's classifications and / or
     * custom metadata.
     *
     * @param value entity to upsert
     * @param replaceClassifications whether to overwrite any existing classifications (true) or not (false)
     * @param replaceCustomMetadata whether to overwrite any existing custom metadata (true) or not (false)
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public static EntityMutationResponse upsert(
            Entity value, boolean replaceClassifications, boolean replaceCustomMetadata) throws AtlanException {
        return upsert(Collections.singletonList(value), replaceClassifications, replaceCustomMetadata);
    }

    /**
     * Creates any entities, optionally overwriting the existing entities' classifications and / or
     * custom metadata.
     *
     * @param values entities to upsert
     * @param replaceClassifications whether to overwrite any existing classifications (true) or not (false)
     * @param replaceCustomMetadata whether to overwrite any existing custom metadata (true) or not (false)
     * @return the results of the upsert
     * @throws AtlanException on any API interaction problems
     */
    public static EntityMutationResponse upsert(
            List<Entity> values, boolean replaceClassifications, boolean replaceCustomMetadata) throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=%s",
                        endpoint, replaceClassifications, replaceCustomMetadata));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        return ApiResource.request(ApiResource.RequestMethod.POST, url, beq, EntityMutationResponse.class, null);
    }

    /**
     * Deletes any entity.
     *
     * @param guid unique ID of the entity to delete
     * @param deleteType whether to soft-delete (archive) or hard-delete (purge) the entity
     * @return the results of the deletion
     * @throws AtlanException on any API interaction problems
     */
    public static EntityMutationResponse delete(String guid, AtlanDeleteType deleteType) throws AtlanException {
        return delete(Collections.singletonList(guid), deleteType);
    }

    /**
     * Deletes any entities.
     *
     * @param guids unique IDs of the entities to delete
     * @param deleteType whether to soft-delete (archive) or hard-delete (purge) the entities
     * @return the results of the deletion
     * @throws AtlanException on any API interaction problems
     */
    public static EntityMutationResponse delete(List<String> guids, AtlanDeleteType deleteType) throws AtlanException {
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
                        ApiResource.RequestMethod.DELETE, url, "", EntityMutationResponse.class, null);
            }
        }
        throw new InvalidRequestException(
                "Insufficient information provided to delete entities: no GUID provided.",
                "guid",
                "ATLAN-JAVA-CLIENT-400",
                400,
                null);
    }
}
