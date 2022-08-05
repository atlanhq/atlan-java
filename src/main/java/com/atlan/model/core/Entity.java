/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.model.responses.EntityResponse;
import com.atlan.net.ApiResource;
import com.atlan.net.AtlanObject;
import java.util.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public abstract class Entity extends AtlanObject {
    /** Name of the type definition that defines this entity. */
    transient String typeName;

    /** Globally-unique identifier for this entity. */
    String guid;

    /** Human-readable name of the entity. */
    String displayText;

    /** Classifications assigned to the entity. */
    @Singular
    List<Classification> classifications;

    /**
     * Map of attributes in the entity and their values. The specific keys of this map will vary by
     * type, so are described in the subtypes of this object.
     */
    transient Attributes attributes;

    /**
     * Map of relationships for the entity and their values. The specific keys of this map will vary
     * by type, so are described in the subtypes of this object.
     */
    transient RelationshipAttributes relationshipAttributes;

    /** Map of custom metadata attributes and values defined on the entity. */
    // @Singular
    Map<String, Object> businessAttributes;

    /** Status of the entity. */
    final AtlanStatus status;

    /** User or account that created the entity. */
    final String createdBy;

    /** User or account that last updated the entity. */
    final String updatedBy;

    /** Time (epoch) at which the entity was created, in milliseconds. */
    final Long createTime;

    /** Time (epoch) at which the entity was last updated, in milliseconds. */
    final Long updateTime;

    /** Unused. */
    List<String> classificationNames;

    /** Unused. */
    Boolean isIncomplete;

    /** Unused. */
    List<String> meaningNames;

    /** Unused. */
    List<Object> meanings;

    /** Creates any entity, ignoring any classifications and businessAttributes provided. */
    public static EntityMutationResponse create(Entity value) throws AtlanException {
        return create(value, false, false);
    }

    /**
     * Creates any entity, optionally overwriting an existing entity's classifications and / or
     * businessAttributes.
     */
    public static EntityMutationResponse create(
            Entity value, boolean replaceClassifications, boolean replaceBusinessAttributes) throws AtlanException {
        return create(Collections.singletonList(value), replaceClassifications, replaceBusinessAttributes);
    }

    /** Creates any entities, ignoring any classifications and businessAttributes provided. */
    public static EntityMutationResponse create(List<Entity> values) throws AtlanException {
        return create(values, false, false);
    }

    /**
     * Creates any entities, optionally overwriting the existing entities' classifications and / or
     * businessAttributes.
     */
    public static EntityMutationResponse create(
            List<Entity> values, boolean replaceClassifications, boolean replaceBusinessAttributes)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getApiBase(),
                String.format(
                        "/api/meta/entity/bulk?replaceClassifications=%s&replaceBusinessAttributes=%s",
                        replaceClassifications, replaceBusinessAttributes));
        BulkEntityRequest beq = BulkEntityRequest.builder().entities(values).build();
        return ApiResource.request(ApiResource.RequestMethod.POST, url, beq, EntityMutationResponse.class, null);
    }

    /** Retrieves any entity by its GUID. */
    public static EntityResponse retrieve(String guid) throws AtlanException {
        return retrieve(guid, false, false);
    }

    /** Retrieves any entity by its GUID. */
    public static EntityResponse retrieve(String guid, boolean ignoreRelationships, boolean minExtInfo)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getApiBase(),
                String.format(
                        "/api/meta/entity/guid/%s?ignoreRelationships=%s&minExtInfo=%s",
                        ApiResource.urlEncodeId(guid), ignoreRelationships, minExtInfo));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", EntityResponse.class, null);
    }

    /** Retrieves any entity by its qualifiedName. */
    public static EntityResponse retrieve(String typeName, String qualifiedName) throws AtlanException {
        return retrieve(typeName, qualifiedName, false, false);
    }

    /** Retrieves any entity by its qualifiedName. */
    public static EntityResponse retrieve(
            String typeName, String qualifiedName, boolean ignoreRelationships, boolean minExtInfo)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getApiBase(),
                String.format(
                        "/api/meta/entity/uniqueAttribute/type/%s?attr:qualifiedName=%s&ignoreRelationships=%s&minExtInfo=%s",
                        ApiResource.urlEncodeId(typeName),
                        ApiResource.urlEncodeId(qualifiedName),
                        ignoreRelationships,
                        minExtInfo));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", EntityResponse.class, null);
    }

    /** Updates any entity, ignoring any classifications and businessAttributes provided. */
    public static EntityMutationResponse update(Entity value) throws AtlanException {
        return create(value, false, false);
    }

    /**
     * Updates any entity, optionally overwriting an existing entity's classifications and / or
     * businessAttributes.
     */
    public static EntityMutationResponse update(
            Entity value, boolean replaceClassifications, boolean replaceBusinessAttributes) throws AtlanException {
        return create(Collections.singletonList(value), replaceClassifications, replaceBusinessAttributes);
    }

    /** Updates any entities, ignoring any classifications and businessAttributes provided. */
    public static EntityMutationResponse update(List<Entity> values) throws AtlanException {
        return create(values, false, false);
    }

    /** Soft-deletes any entity */
    public static EntityMutationResponse delete(String guid) throws AtlanException {
        return delete(guid, AtlanDeleteType.SOFT);
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
                        Atlan.getApiBase(),
                        String.format("/api/meta/entity/bulk?%s&deleteType=%s", guidList, deleteType));
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
