package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.cache.CustomMetadataCacheJ;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.CustomMetadataAttributesJ;
import com.atlan.model.core.CustomMetadataUpdateRequestJ;
import com.atlan.model.responses.EntityResponseJ;
import com.atlan.net.ApiResourceJ;
import java.util.Map;

public class EntityGuidEndpointJ {

    private static final String endpoint = "/api/meta/entity/guid/";

    /** Retrieves any entity by its GUID. */
    public static EntityResponseJ retrieve(String guid, boolean ignoreRelationships, boolean minExtInfo)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getBaseUrl(),
                String.format(
                        "%s%s?ignoreRelationships=%s&minExtInfo=%s",
                        endpoint, ApiResourceJ.urlEncodeId(guid), ignoreRelationships, minExtInfo));
        return ApiResourceJ.request(ApiResourceJ.RequestMethod.GET, url, "", EntityResponseJ.class, null);
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
    public static void updateCustomMetadataAttributes(String guid, String cmName, CustomMetadataAttributesJ values)
            throws AtlanException {
        String cmId = CustomMetadataCacheJ.getIdForName(cmName);
        if (cmId != null) {
            if (values != null) {
                String url = String.format(
                        "%s%s",
                        Atlan.getBaseUrl(),
                        String.format(
                                "%s%s/businessmetadata?isOverwrite=false", endpoint, ApiResourceJ.urlEncodeId(guid)));
                CustomMetadataUpdateRequestJ cmur =
                        new CustomMetadataUpdateRequestJ(cmName, values.getAttributes(), true);
                ApiResourceJ.request(ApiResourceJ.RequestMethod.POST, url, cmur, null, null);
            }
        } else {
            throw new InvalidRequestException(
                    "No custom metadata found with the provided name.",
                    "customMetadataName",
                    cmName,
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
    public static void replaceCustomMetadata(String guid, String cmName, CustomMetadataAttributesJ values)
            throws AtlanException {
        String cmId = CustomMetadataCacheJ.getIdForName(cmName);
        if (cmId != null) {
            if (values != null) {
                String url = String.format(
                        "%s%s",
                        Atlan.getBaseUrl(),
                        String.format(
                                "%s%s/businessmetadata/%s",
                                endpoint, ApiResourceJ.urlEncodeId(guid), ApiResourceJ.urlEncodeId(cmId)));
                CustomMetadataUpdateRequestJ cmur =
                        new CustomMetadataUpdateRequestJ(cmName, values.getAttributes(), false);
                ApiResourceJ.request(ApiResourceJ.RequestMethod.POST, url, cmur, null, null);
            }
        } else {
            throw new InvalidRequestException(
                    "No custom metadata found with the provided name.",
                    "customMetadataName",
                    cmName,
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
        String cmId = CustomMetadataCacheJ.getIdForName(cmName);
        if (cmId != null) {
            Map<String, Object> map = CustomMetadataCacheJ.getEmptyAttributes(cmName);
            CustomMetadataAttributesJ cma =
                    CustomMetadataAttributesJ.builder().attributes(map).build();
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
                    "No custom metadata found with the provided name.",
                    "customMetadataName",
                    cmName,
                    "ATLAN-JAVA-CLIENT-400-030",
                    400,
                    null);
        }
    }
}
