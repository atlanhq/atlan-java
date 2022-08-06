/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.responses.EntityXMutationResponse;
import com.atlan.model.responses.EntityXResponse;
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
public abstract class EntityX extends AtlanObject {
    /** Internal tracking of fields that should be serialized with null values. */
    transient Set<String> nullFields;

    /**
     * Add a field to be serialized with a null value.
     * @param fieldName to serialize with a null value
     */
    public void addNullField(String fieldName) {
        if (nullFields == null) {
            nullFields = new LinkedHashSet<>();
        }
        nullFields.add(fieldName);
    }

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
     * Map of attributes in the entity and their values. This is intended for use by internal (de)serialization
     * only. For actual attributes and their values, use the top-level strongly-typed getters and setters.
     */
    Map<String, Object> attributes;

    /**
     * Map of relationships for the entity and their values. This is intended for use by internal (de)serialization
     * only. For actual attributes and their values, use the top-level strongly-typed getters and setters.
     */
    Map<String, Object> relationshipAttributes;

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

    /** Details on the handler used for deletion of the asset. */
    final String deleteHandler;

    /** Unused. */
    List<String> classificationNames;

    /** Unused. */
    Boolean isIncomplete;

    /** Unused. */
    List<String> meaningNames;

    /** Creates any entity, ignoring any classifications and businessAttributes provided. */
    public static EntityXMutationResponse create(EntityX value) throws AtlanException {
        return create(value, false, false);
    }

    /**
     * Creates any entity, optionally overwriting an existing entity's classifications and / or
     * businessAttributes.
     */
    public static EntityXMutationResponse create(
            EntityX value, boolean replaceClassifications, boolean replaceBusinessAttributes) throws AtlanException {
        return create(Collections.singletonList(value), replaceClassifications, replaceBusinessAttributes);
    }

    /** Creates any entities, ignoring any classifications and businessAttributes provided. */
    public static EntityXMutationResponse create(List<EntityX> values) throws AtlanException {
        return create(values, false, false);
    }

    /**
     * Creates any entities, optionally overwriting the existing entities' classifications and / or
     * businessAttributes.
     */
    public static EntityXMutationResponse create(
            List<EntityX> values, boolean replaceClassifications, boolean replaceBusinessAttributes)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getApiBase(),
                String.format(
                        "/api/meta/entity/bulk?replaceClassifications=%s&replaceBusinessAttributes=%s",
                        replaceClassifications, replaceBusinessAttributes));
        BulkEntityXRequest beq = BulkEntityXRequest.builder().entities(values).build();
        return ApiResource.request(ApiResource.RequestMethod.POST, url, beq, EntityXMutationResponse.class, null);
    }

    /** Retrieves any entity by its GUID. */
    public static EntityXResponse retrieve(String guid) throws AtlanException {
        return retrieve(guid, false, false);
    }

    /** Retrieves any entity by its GUID. */
    public static EntityXResponse retrieve(String guid, boolean ignoreRelationships, boolean minExtInfo)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getApiBase(),
                String.format(
                        "/api/meta/entity/guid/%s?ignoreRelationships=%s&minExtInfo=%s",
                        ApiResource.urlEncodeId(guid), ignoreRelationships, minExtInfo));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", EntityXResponse.class, null);
    }

    /** Retrieves any entity by its qualifiedName. */
    public static EntityXResponse retrieve(String typeName, String qualifiedName) throws AtlanException {
        return retrieve(typeName, qualifiedName, false, false);
    }

    /** Retrieves any entity by its qualifiedName. */
    public static EntityXResponse retrieve(
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
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", EntityXResponse.class, null);
    }

    /** Updates any entity, ignoring any classifications and businessAttributes provided. */
    public static EntityXMutationResponse update(EntityX value) throws AtlanException {
        return create(value, false, false);
    }

    /**
     * Updates any entity, optionally overwriting an existing entity's classifications and / or
     * businessAttributes.
     */
    public static EntityXMutationResponse update(
            EntityX value, boolean replaceClassifications, boolean replaceBusinessAttributes) throws AtlanException {
        return create(Collections.singletonList(value), replaceClassifications, replaceBusinessAttributes);
    }

    /** Updates any entities, ignoring any classifications and businessAttributes provided. */
    public static EntityXMutationResponse update(List<EntityX> values) throws AtlanException {
        return create(values, false, false);
    }

    /** Soft-deletes any entity */
    public static EntityXMutationResponse delete(String guid) throws AtlanException {
        return delete(guid, AtlanDeleteType.SOFT);
    }

    /** Deletes any entity */
    public static EntityXMutationResponse delete(String guid, AtlanDeleteType deleteType) throws AtlanException {
        return delete(Collections.singletonList(guid), deleteType);
    }

    /** Deletes any entities */
    public static EntityXMutationResponse delete(List<String> guids, AtlanDeleteType deleteType) throws AtlanException {
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
                        ApiResource.RequestMethod.DELETE, url, "", EntityXMutationResponse.class, null);
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
