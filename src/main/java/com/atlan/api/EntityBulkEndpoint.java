package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.BulkEntityRequest;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.net.ApiResource;
import java.util.Collections;
import java.util.List;

public class EntityBulkEndpoint {

    private static final String endpoint = "/api/meta/entity/bulk";

    /**
     * Creates any entity, optionally overwriting an existing entity's classifications and / or
     * businessAttributes.
     */
    public static EntityMutationResponse upsert(
            Entity value, boolean replaceClassifications, boolean replaceBusinessAttributes) throws AtlanException {
        return upsert(Collections.singletonList(value), replaceClassifications, replaceBusinessAttributes);
    }

    /**
     * Creates any entities, optionally overwriting the existing entities' classifications and / or
     * businessAttributes.
     */
    public static EntityMutationResponse upsert(
            List<Entity> values, boolean replaceClassifications, boolean replaceBusinessAttributes)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getApiBase(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=%s",
                        endpoint, replaceClassifications, replaceBusinessAttributes));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        return ApiResource.request(ApiResource.RequestMethod.POST, url, beq, EntityMutationResponse.class, null);
    }

    /** Deletes any entity */
    public static EntityMutationResponse delete(String guid, AtlanDeleteType deleteType) throws AtlanException {
        return delete(Collections.singletonList(guid), deleteType);
    }

    /** Deletes any entities */
    public static EntityMutationResponse delete(List<String> guids, AtlanDeleteType deleteType) throws AtlanException {
        if (guids != null) {
            StringBuilder guidList = new StringBuilder();
            for (String guid : guids) {
                if (guid != null) {
                    guidList.append("guid=").append(guid).append(",");
                }
            }
            if (guidList.length() > 0) {
                // Remove the final comma
                guidList.setLength(guidList.length() - 1);
                String url = String.format(
                        "%s%s",
                        Atlan.getApiBase(), String.format("%s?%s&deleteType=%s", endpoint, guidList, deleteType));
                return ApiResource.request(
                        ApiResource.RequestMethod.DELETE, url, "", EntityMutationResponse.class, null);
            }
        }
        throw new InvalidRequestException(
                "Insufficient information provided to delete entities: no GUID provided.",
                "guid",
                "N/A",
                "ATLAN-JAVA-CLIENT-400",
                400,
                null);
    }
}
