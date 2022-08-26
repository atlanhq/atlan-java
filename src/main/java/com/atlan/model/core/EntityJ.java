/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.api.EntityBulkEndpointJ;
import com.atlan.api.EntityGuidEndpointJ;
import com.atlan.api.EntityUniqueAttributesEndpointJ;
import com.atlan.exception.AtlanException;
import com.atlan.model.*;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.responses.EntityMutationResponseJ;
import com.atlan.model.responses.EntityResponseJ;
import com.atlan.model.serde.EntityJDeserializer;
import com.atlan.model.serde.EntityJSerializer;
import com.atlan.net.AtlanObjectJ;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@JsonSerialize(using = EntityJSerializer.class)
@JsonDeserialize(using = EntityJDeserializer.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "typeName")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ReadmeJ.class, name = ReadmeJ.TYPE_NAME),
    @JsonSubTypes.Type(value = GlossaryJ.class, name = GlossaryJ.TYPE_NAME),
    @JsonSubTypes.Type(value = GlossaryCategoryJ.class, name = GlossaryCategoryJ.TYPE_NAME),
    @JsonSubTypes.Type(value = GlossaryTermJ.class, name = GlossaryTermJ.TYPE_NAME),
    @JsonSubTypes.Type(value = ConnectionJ.class, name = ConnectionJ.TYPE_NAME),
    @JsonSubTypes.Type(value = TableJ.class, name = TableJ.TYPE_NAME),
    @JsonSubTypes.Type(value = ColumnJ.class, name = ColumnJ.TYPE_NAME),
    @JsonSubTypes.Type(value = S3BucketJ.class, name = S3BucketJ.TYPE_NAME),
    @JsonSubTypes.Type(value = S3ObjectJ.class, name = S3ObjectJ.TYPE_NAME),
    @JsonSubTypes.Type(value = LineageProcessJ.class, name = LineageProcessJ.TYPE_NAME),
    @JsonSubTypes.Type(value = ColumnProcessJ.class, name = ColumnProcessJ.TYPE_NAME),
})
@SuppressWarnings("cast")
public abstract class EntityJ extends AtlanObjectJ {
    /** Internal tracking of fields that should be serialized with null values. */
    @JsonIgnore
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
    String typeName;

    /** Globally-unique identifier for this entity. */
    String guid;

    /** Human-readable name of the entity. */
    String displayText;

    /** Classifications assigned to the entity. */
    @Singular
    Set<ClassificationJ> classifications;

    /**
     * Map of custom metadata attributes and values defined on the entity. The map is keyed by the human-readable
     * name of the custom metadata set, and the values are a further mapping from human-readable attribute name
     * to the value for that attribute on this entity.
     */
    @Singular("customMetadata")
    Map<String, CustomMetadataAttributesJ> customMetadataSets;

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

    /** The names of the classifications that exist on the asset. */
    Set<String> classificationNames;

    /** Unused. */
    Boolean isIncomplete;

    /** Unused. */
    Set<String> meaningNames;

    /** Remove the certificate from the asset, if any is set on the asset. */
    public void removeCustomMetadata() {
        // It is sufficient to simply exclude businessAttributes from a request in order
        // for them to be removed, as long as the "replaceBusinessAttributes" flag is set
        // to true (which it must be for any update to work to businessAttributes anyway)
        customMetadataSets = null;
    }

    /** Remove the classifications from the asset, if the asset is classified with any. */
    public void removeClassifications() {
        // It is sufficient to simply exclude classifications from a request in order
        // for them to be removed, as long as the "replaceClassifications" flag is set to
        // true (which it must be for any update to work to classifications anyway)
        classifications = null;
        classificationNames = null;
    }

    /**
     * Creates the entity. If no entity exists, the classifications and businessAttributes will be ?
     * @return details of the created or updated entity
     * @throws AtlanException on any error during the API invocation
     */
    public EntityMutationResponseJ upsert() throws AtlanException {
        return EntityBulkEndpointJ.upsert(this, false, false);
    }

    /**
     * If no entity exists, has the same behavior as the {@link #upsert()} method.
     * If an entity does exist, optionally overwrites any classifications and / or businessAttributes.
     * @param replaceClassifications whether to replace classifications during an update (true) or not (false)
     * @param replaceCustomMetadata whether to replace custom metadata during an update (true) or not (false)
     * @return details of the created or updated entity
     * @throws AtlanException on any error during the API invocation
     */
    public EntityMutationResponseJ upsert(boolean replaceClassifications, boolean replaceCustomMetadata)
            throws AtlanException {
        return EntityBulkEndpointJ.upsert(this, replaceClassifications, replaceCustomMetadata);
    }

    /**
     * Retrieves an entity by its GUID, complete with all of its relationships.
     * @param guid of the entity to retrieve
     * @return the requested full entity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the entity does not exist
     */
    public static EntityJ retrieveFull(String guid) throws AtlanException {
        EntityResponseJ response = EntityGuidEndpointJ.retrieve(guid, false, false);
        return response.getEntity();
    }

    /**
     * Retrieves a minimal entity by its GUID, without its relationships.
     * @param guid of the entity to retrieve
     * @return the requested minimal entity, without its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the entity does not exist
     */
    public static EntityJ retrieveMinimal(String guid) throws AtlanException {
        EntityResponseJ response = EntityGuidEndpointJ.retrieve(guid, true, true);
        return response.getEntity();
    }

    /**
     * Retrieves an entity by its qualifiedName, complete with all of its relationships.
     * @param typeName the type of the entity to retrieve
     * @param qualifiedName the unique name of the entity to retrieve
     * @return the requested full entity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the entity does not exist
     */
    public static EntityJ retrieveFull(String typeName, String qualifiedName) throws AtlanException {
        EntityResponseJ response = EntityUniqueAttributesEndpointJ.retrieve(typeName, qualifiedName, false, false);
        return response.getEntity();
    }

    /**
     * Retrieves an entity by its qualifiedName, without its relationships.
     * @param typeName the type of the entity to retrieve
     * @param qualifiedName the unique name of the entity to retrieve
     * @return the requested minimal entity, without its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the entity does not exist
     */
    public static EntityJ retrieveMinimal(String typeName, String qualifiedName) throws AtlanException {
        EntityResponseJ response = EntityUniqueAttributesEndpointJ.retrieve(typeName, qualifiedName, true, true);
        return response.getEntity();
    }

    /**
     * Soft-deletes an entity by its GUID. This operation can be reversed by updating the entity and changing
     * its {@link #status} to {@code ACTIVE}.
     * @param guid of the entity to soft-delete
     * @return details of the soft-deleted entity
     * @throws AtlanException on any error during the API invocation
     */
    public static EntityMutationResponseJ delete(String guid) throws AtlanException {
        return EntityBulkEndpointJ.delete(guid, AtlanDeleteType.SOFT);
    }

    /**
     * Hard-deletes (purges) an entity by its GUID. This operation is irreversible. The entity to purge must
     * currently be in an active state (soft-deleted entities cannot be purged).
     * @param guid of the entity to hard-delete
     * @return details of the hard-deleted entity
     * @throws AtlanException on any error during the API invocation
     */
    public static EntityMutationResponseJ purge(String guid) throws AtlanException {
        return EntityBulkEndpointJ.delete(guid, AtlanDeleteType.HARD);
    }

    /**
     * Update only the provided custom metadata attributes on the asset. This will leave all other custom metadata
     * attributes, even within the same named custom metadata, unchanged.
     *
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to update
     * @param attributes the values of the custom metadata attributes to change
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void updateCustomMetadataAttributes(String guid, String cmName, CustomMetadataAttributesJ attributes)
            throws AtlanException {
        EntityGuidEndpointJ.updateCustomMetadataAttributes(guid, cmName, attributes);
    }

    /**
     * Replace specific custom metadata on the asset. This will replace everything within the named custom metadata,
     * but will not change any of the other named custom metadata on the asset.
     *
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to replace
     * @param attributes the values of the attributes to replace for the custom metadata
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void replaceCustomMetadata(String guid, String cmName, CustomMetadataAttributesJ attributes)
            throws AtlanException {
        EntityGuidEndpointJ.replaceCustomMetadata(guid, cmName, attributes);
    }

    /**
     * Remove specific custom metadata from an asset.
     *
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to remove
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void removeCustomMetadata(String guid, String cmName) throws AtlanException {
        EntityGuidEndpointJ.removeCustomMetadata(guid, cmName);
    }

    /**
     * Add classifications to an asset.
     *
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the asset
     */
    protected static void addClassifications(String typeName, String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        EntityUniqueAttributesEndpointJ.addClassifications(typeName, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from an asset.
     *
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param classificationName human-readable name of the classifications to remove
     * @throws AtlanException on any API problems, or if any of the classification does not exist on the asset
     */
    protected static void removeClassification(String typeName, String qualifiedName, String classificationName)
            throws AtlanException {
        EntityUniqueAttributesEndpointJ.removeClassification(typeName, qualifiedName, classificationName, true);
    }
}
