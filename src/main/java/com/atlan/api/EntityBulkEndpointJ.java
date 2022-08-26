package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.BulkEntityRequestJ;
import com.atlan.model.core.EntityJ;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.responses.EntityMutationResponseJ;
import com.atlan.net.ApiResourceJ;
import java.util.Collections;
import java.util.List;

public class EntityBulkEndpointJ {

    private static final String endpoint = "/api/meta/entity/bulk";

    /**
     * Creates any entity, optionally overwriting an existing entity's classifications and / or
     * businessAttributes.
     */
    public static EntityMutationResponseJ upsert(
            EntityJ value, boolean replaceClassifications, boolean replaceBusinessAttributes) throws AtlanException {
        return upsert(Collections.singletonList(value), replaceClassifications, replaceBusinessAttributes);
    }

    /**
     * Creates any entities, optionally overwriting the existing entities' classifications and / or
     * businessAttributes.
     */
    public static EntityMutationResponseJ upsert(
            List<EntityJ> values, boolean replaceClassifications, boolean replaceBusinessAttributes)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getBaseUrl(),
                String.format(
                        "%s?replaceClassifications=%s&replaceBusinessAttributes=%s",
                        endpoint, replaceClassifications, replaceBusinessAttributes));
        BulkEntityRequestJ beq = BulkEntityRequestJ.builder().entities(values).build();
        return ApiResourceJ.request(ApiResourceJ.RequestMethod.POST, url, beq, EntityMutationResponseJ.class, null);
    }

    /** Deletes any entity */
    public static EntityMutationResponseJ delete(String guid, AtlanDeleteType deleteType) throws AtlanException {
        return delete(Collections.singletonList(guid), deleteType);
    }

    /** Deletes any entities */
    public static EntityMutationResponseJ delete(List<String> guids, AtlanDeleteType deleteType) throws AtlanException {
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
                return ApiResourceJ.request(
                        ApiResourceJ.RequestMethod.DELETE, url, "", EntityMutationResponseJ.class, null);
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
