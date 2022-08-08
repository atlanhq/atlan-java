/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.api.EntityBulkEndpoint;
import com.atlan.api.EntityGuidEndpoint;
import com.atlan.api.EntityUniqueAttributesEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.model.responses.EntityResponse;
import com.atlan.net.AtlanObject;
import java.util.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class Entity extends AtlanObject {
    /** Internal tracking of fields that should be serialized with null values. */
    transient Set<String> nullFields;

    /** Retrieve the list of fields to be serialized with null values. */
    public Set<String> getNullFields() {
        if (nullFields == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(nullFields);
    }

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
    @EqualsAndHashCode.Exclude
    Map<String, Object> attributes;

    /**
     * Map of relationships for the entity and their values. This is intended for use by internal (de)serialization
     * only. For actual attributes and their values, use the top-level strongly-typed getters and setters.
     */
    @EqualsAndHashCode.Exclude
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

    /**
     * Creates the entity. If no entity exists, the classifications and businessAttributes will be ?
     * @return details of the created or updated entity
     * @throws AtlanException on any error during the API invocation
     */
    public EntityMutationResponse upsert() throws AtlanException {
        return EntityBulkEndpoint.upsert(this, false, false);
    }

    /**
     * If no entity exists, has the same behavior as the {@link #upsert()} method.
     * If an entity does exist, optionally overwrites any classifications and / or businessAttributes.
     * @param replaceClassifications whether to replace classifications during an update (true) or not (false)
     * @param replaceCustomMetadata whether to replace custom metadata during an update (true) or not (false)
     * @return details of the created or updated entity
     * @throws AtlanException on any error during the API invocation
     */
    public EntityMutationResponse upsert(boolean replaceClassifications, boolean replaceCustomMetadata)
            throws AtlanException {
        return EntityBulkEndpoint.upsert(this, replaceClassifications, replaceCustomMetadata);
    }

    /**
     * Retrieves an entity by its GUID, complete with all of its relationships.
     * @param guid of the entity to retrieve
     * @return the requested full entity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the entity does not exist
     */
    public static Entity retrieveFull(String guid) throws AtlanException {
        EntityResponse response = EntityGuidEndpoint.retrieve(guid, false, false);
        return response.getEntity();
    }

    /**
     * Retrieves a minimal entity by its GUID, without its relationships.
     * @param guid of the entity to retrieve
     * @return the requested minimal entity, without its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the entity does not exist
     */
    public static Entity retrieveMinimal(String guid) throws AtlanException {
        EntityResponse response = EntityGuidEndpoint.retrieve(guid, true, true);
        return response.getEntity();
    }

    /**
     * Retrieves an entity by its qualifiedName, complete with all of its relationships.
     * @param typeName the type of the entity to retrieve
     * @param qualifiedName the unique name of the entity to retrieve
     * @return the requested full entity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the entity does not exist
     */
    public static Entity retrieveFull(String typeName, String qualifiedName) throws AtlanException {
        EntityResponse response = EntityUniqueAttributesEndpoint.retrieve(typeName, qualifiedName, false, false);
        return response.getEntity();
    }

    /**
     * Retrieves an entity by its qualifiedName, without its relationships.
     * @param typeName the type of the entity to retrieve
     * @param qualifiedName the unique name of the entity to retrieve
     * @return the requested minimal entity, without its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the entity does not exist
     */
    public static Entity retrieveMinimal(String typeName, String qualifiedName) throws AtlanException {
        EntityResponse response = EntityUniqueAttributesEndpoint.retrieve(typeName, qualifiedName, true, true);
        return response.getEntity();
    }

    /**
     * Soft-deletes an entity by its GUID. This operation can be reversed by updating the entity and changing
     * its {@link #status} to {@code ACTIVE}.
     * @param guid of the entity to soft-delete
     * @return details of the soft-deleted entity
     * @throws AtlanException on any error during the API invocation
     */
    public static EntityMutationResponse delete(String guid) throws AtlanException {
        return EntityBulkEndpoint.delete(guid, AtlanDeleteType.SOFT);
    }

    /**
     * Hard-deletes (purges) an entity by its GUID. This operation is irreversible. The entity to purge must
     * currently be in an active state (soft-deleted entities cannot be purged).
     * @param guid of the entity to hard-delete
     * @return details of the hard-deleted entity
     * @throws AtlanException on any error during the API invocation
     */
    public static EntityMutationResponse purge(String guid) throws AtlanException {
        return EntityBulkEndpoint.delete(guid, AtlanDeleteType.HARD);
    }
}
