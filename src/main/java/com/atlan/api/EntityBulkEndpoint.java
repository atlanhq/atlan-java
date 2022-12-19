/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.net.ApiResource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

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
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=%s&overwriteBusinessAttributes=%s",
                        endpoint, replaceClassifications, replaceCustomMetadata, replaceCustomMetadata));
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

    /**
     * Restores any entity from a soft-deleted (archived) to an active state.
     *
     * @param value entity to restore
     * @return the results of the restoration (the restored entity will be in the list of updated entities)
     * @throws AtlanException on any API interaction problems
     */
    public static EntityMutationResponse restore(Entity value) throws AtlanException {
        return restore(Collections.singletonList(value));
    }

    /**
     * Restores any entities in the list provided from a soft-deleted (archived) to active state.
     *
     * @param values entities to restore
     * @return the results of the restoration (any restored entities will be in the list of updated entities)
     * @throws AtlanException on any API interaction problems
     */
    public static EntityMutationResponse restore(List<Entity> values) throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=false&replaceBusinessAttributes=false&overwriteBusinessAttributes=false",
                        endpoint));
        List<Entity> culled = new ArrayList<>();
        for (Entity one : values) {
            culled.add(((Asset) one).trimToRequired().status(AtlanStatus.ACTIVE).build());
        }
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(culled).build();
        return ApiResource.request(ApiResource.RequestMethod.POST, url, beq, EntityMutationResponse.class, null);
    }

    /**
     * Request class for updating many entities together (in bulk).
     */
    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    static class BulkEntityRequest extends AtlanObject {
        /** List of entities to operate on in bulk. */
        List<Entity> entities;
    }
}
