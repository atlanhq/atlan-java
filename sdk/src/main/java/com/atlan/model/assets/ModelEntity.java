/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of an entity within a version of a data model in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class ModelEntity extends Asset implements IModelEntity, IModel, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModelEntity";

    /** Fixed typeName for ModelEntitys. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Tasks to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** Processes to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> inputToSparkJobs;

    /** Business date for the asset. */
    @Attribute
    @Date
    Long modelBusinessDate;

    /** Model domain in which this asset exists. */
    @Attribute
    String modelDomain;

    /** Number of attributes in the entity. */
    @Attribute
    Long modelEntityAttributeCount;

    /** Individual attributes that make up the entity. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelEntityAttributes;

    /** General entity, representing shared characteristics of specialized entities. */
    @Attribute
    IModelEntity modelEntityGeneralizationEntity;

    /** Name of the general entity. */
    @Attribute
    String modelEntityGeneralizationName;

    /** Unique identifier for the general entity. */
    @Attribute
    String modelEntityGeneralizationQualifiedName;

    /** Assets that implement this entity. */
    @Attribute
    @Singular
    SortedSet<ICatalog> modelEntityImplementedByAssets;

    /** Entities from which this entity is mapped. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelEntityMappedFromEntities;

    /** Entities to which this entity is mapped. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelEntityMappedToEntities;

    /** Simple name of the entity in which this asset exists, or empty if it is itself a data model entity. */
    @Attribute
    String modelEntityName;

    /** Unique name of the entity in which this asset exists, or empty if it is itself a data model entity. */
    @Attribute
    String modelEntityQualifiedName;

    /** Association from which this entity is related. */
    @Attribute
    @Singular
    SortedSet<IModelEntityAssociation> modelEntityRelatedFromEntities;

    /** Association to which this entity is related. */
    @Attribute
    @Singular
    SortedSet<IModelEntityAssociation> modelEntityRelatedToEntities;

    /** Specialized entities derived from the general entity. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelEntitySpecializationEntities;

    /** Subject area of the entity. */
    @Attribute
    String modelEntitySubjectArea;

    /** Business expiration date for the asset. */
    @Attribute
    @Date
    Long modelExpiredAtBusinessDate;

    /** System expiration date for the asset. */
    @Attribute
    @Date
    Long modelExpiredAtSystemDate;

    /** Attributes implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Simple name of the model in which this asset exists, or empty if it is itself a data model. */
    @Attribute
    String modelName;

    /** Model namespace in which this asset exists. */
    @Attribute
    String modelNamespace;

    /** Unique name of the model in which this asset exists, or empty if it is itself a data model. */
    @Attribute
    String modelQualifiedName;

    /** System date for the asset. */
    @Attribute
    @Date
    Long modelSystemDate;

    /** Type of the model asset (conceptual, logical, physical). */
    @Attribute
    String modelType;

    /** Unique name of the parent in which this asset exists, irrespective of the version (always implies the latest version). */
    @Attribute
    String modelVersionAgnosticQualifiedName;

    /** Simple name of the version in which this asset exists, or empty if it is itself a data model version. */
    @Attribute
    String modelVersionName;

    /** Unique name of the version in which this asset exists, or empty if it is itself a data model version. */
    @Attribute
    String modelVersionQualifiedName;

    /** Data model version(s) in which this entity exists. */
    @Attribute
    @Singular
    SortedSet<IModelVersion> modelVersions;

    /** Tasks from which this asset is output. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** Processes from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> outputFromSparkJobs;

    /**
     * Builds the minimal object necessary to create a relationship to a ModelEntity, from a potentially
     * more-complete ModelEntity object.
     *
     * @return the minimal object necessary to relate to the ModelEntity
     * @throws InvalidRequestException if any of the minimal set of required properties for a ModelEntity relationship are not found in the initial object
     */
    @Override
    public ModelEntity trimToReference() throws InvalidRequestException {
        if (this.getGuid() != null && !this.getGuid().isEmpty()) {
            return refByGuid(this.getGuid());
        }
        if (this.getQualifiedName() != null && !this.getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getQualifiedName());
        }
        if (this.getUniqueAttributes() != null
                && this.getUniqueAttributes().getQualifiedName() != null
                && !this.getUniqueAttributes().getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getUniqueAttributes().getQualifiedName());
        }
        throw new InvalidRequestException(
                ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, TYPE_NAME, "guid, qualifiedName");
    }

    /**
     * Start a fluent search that will return all ModelEntity assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModelEntity assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ModelEntity assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ModelEntity assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ModelEntitys will be included
     * @return a fluent search that includes all ModelEntity assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(Asset.TYPE_NAME.eq(TYPE_NAME));
        if (!includeArchived) {
            builder.active();
        }
        return builder;
    }

    /**
     * Reference to a ModelEntity by GUID. Use this to create a relationship to this ModelEntity,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ModelEntity to reference
     * @return reference to a ModelEntity that can be used for defining a relationship to a ModelEntity
     */
    public static ModelEntity refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModelEntity by GUID. Use this to create a relationship to this ModelEntity,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ModelEntity to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModelEntity that can be used for defining a relationship to a ModelEntity
     */
    public static ModelEntity refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ModelEntity._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ModelEntity by qualifiedName. Use this to create a relationship to this ModelEntity,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ModelEntity to reference
     * @return reference to a ModelEntity that can be used for defining a relationship to a ModelEntity
     */
    public static ModelEntity refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModelEntity by qualifiedName. Use this to create a relationship to this ModelEntity,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ModelEntity to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModelEntity that can be used for defining a relationship to a ModelEntity
     */
    public static ModelEntity refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ModelEntity._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ModelEntity by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelEntity to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModelEntity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelEntity does not exist or the provided GUID is not a ModelEntity
     */
    @JsonIgnore
    public static ModelEntity get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a ModelEntity by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelEntity to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ModelEntity, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelEntity does not exist or the provided GUID is not a ModelEntity
     */
    @JsonIgnore
    public static ModelEntity get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ModelEntity) {
                return (ModelEntity) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof ModelEntity) {
                return (ModelEntity) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ModelEntity by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelEntity to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ModelEntity, including any relationships
     * @return the requested ModelEntity, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelEntity does not exist or the provided GUID is not a ModelEntity
     */
    @JsonIgnore
    public static ModelEntity get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a ModelEntity by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelEntity to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ModelEntity, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the ModelEntity
     * @return the requested ModelEntity, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelEntity does not exist or the provided GUID is not a ModelEntity
     */
    @JsonIgnore
    public static ModelEntity get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = ModelEntity.select(client)
                    .where(ModelEntity.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof ModelEntity) {
                return (ModelEntity) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = ModelEntity.select(client)
                    .where(ModelEntity.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof ModelEntity) {
                return (ModelEntity) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ModelEntity to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ModelEntity
     * @return true if the ModelEntity is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelEntity.
     *
     * @param name of the ModelEntity
     * @param model (version-agnostic) in which the entity should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the ModelEntity, as a builder
     * @throws InvalidRequestException if the model provided is without a qualifiedName
     */
    public static ModelEntityBuilder<?, ?> creator(String name, ModelDataModel model) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", model.getConnectionQualifiedName());
        map.put("name", model.getName());
        map.put("qualifiedName", model.getQualifiedName());
        map.put("modelType", model.getModelType());
        validateRelationship(ModelDataModel.TYPE_NAME, map);
        return creator(
                name,
                model.getConnectionQualifiedName(),
                model.getName(),
                model.getQualifiedName(),
                model.getModelType());
    }

    /**
     * Builds the minimal object necessary to create a ModelEntity.
     *
     * @param name of the ModelEntity
     * @param modelQualifiedName unique (version-agnostic) name of the model in which this ModelEntity exists
     * @param modelType type of model in which this entity exists
     * @return the minimal request necessary to create the ModelEntity, as a builder
     */
    public static ModelEntityBuilder<?, ?> creator(String name, String modelQualifiedName, String modelType) {
        String modelSlug = StringUtils.getNameFromQualifiedName(modelQualifiedName);
        String modelName = IModel.getNameFromSlug(modelSlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(modelQualifiedName);
        return creator(name, connectionQualifiedName, modelName, modelQualifiedName, modelType);
    }

    /**
     * Builds the minimal object necessary to create a ModelEntity.
     *
     * @param name of the ModelEntity
     * @param connectionQualifiedName unique name of the connection in which to create this ModelEntity
     * @param modelName simple name (version-agnostic) of the model in which to create this ModelEntity
     * @param modelQualifiedName unique name (version-agnostic) of the model in which to create this ModelEntity
     * @param modelType type of model in which this entity exists
     * @return the minimal request necessary to create the ModelEntity, as a builder
     */
    public static ModelEntityBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String modelName,
            String modelQualifiedName,
            String modelType) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return ModelEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .modelName(modelName)
                .modelQualifiedName(modelQualifiedName)
                .modelType(modelType)
                .modelVersionAgnosticQualifiedName(generateQualifiedName(name, modelQualifiedName))
                .connectorType(connectorType)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelEntity.
     *
     * @param name of the ModelEntity
     * @param version in which the entity should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the ModelEntity, as a builder
     * @throws InvalidRequestException if the version provided is without a qualifiedName
     */
    public static ModelEntityBuilder<?, ?> creatorForVersion(String name, ModelVersion version)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", version.getConnectionQualifiedName());
        map.put("name", version.getName());
        map.put("qualifiedName", version.getQualifiedName());
        validateRelationship(ModelVersion.TYPE_NAME, map);
        return creatorForVersion(
                        name, version.getConnectionQualifiedName(), version.getName(), version.getQualifiedName())
                .modelVersion(version.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ModelEntity.
     *
     * @param name of the ModelEntity
     * @param versionQualifiedName unique name of the version in which this ModelEntity exists
     * @return the minimal request necessary to create the ModelEntity, as a builder
     */
    public static ModelEntityBuilder<?, ?> creatorForVersion(String name, String versionQualifiedName) {
        String versionSlug = StringUtils.getNameFromQualifiedName(versionQualifiedName);
        String versionName = IModel.getNameFromSlug(versionSlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(versionQualifiedName);
        return creatorForVersion(name, connectionQualifiedName, versionName, versionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelEntity.
     *
     * @param name of the ModelEntity
     * @param connectionQualifiedName unique name of the connection in which to create this ModelEntity
     * @param versionName simple name of the version in which to create this ModelEntity
     * @param versionQualifiedName unique name of the version in which to create this ModelEntity
     * @return the minimal request necessary to create the ModelEntity, as a builder
     */
    public static ModelEntityBuilder<?, ?> creatorForVersion(
            String name, String connectionQualifiedName, String versionName, String versionQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return ModelEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, versionQualifiedName))
                .modelVersionName(versionName)
                .modelVersionQualifiedName(versionQualifiedName)
                .modelVersion(ModelVersion.refByQualifiedName(versionQualifiedName))
                .connectorType(connectorType)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ModelEntity.
     *
     * @param versionAgnosticQualifiedName of the ModelEntity
     * @param name of the ModelEntity
     * @return the minimal request necessary to update the ModelEntity, as a builder
     */
    public static ModelEntityBuilder<?, ?> updater(String versionAgnosticQualifiedName, String name) {
        return ModelEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .modelVersionAgnosticQualifiedName(versionAgnosticQualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to update a ModelEntity.
     *
     * @param qualifiedName of the ModelEntity
     * @param name of the ModelEntity
     * @return the minimal request necessary to update the ModelEntity, as a builder
     */
    public static ModelEntityBuilder<?, ?> updaterForVersion(String qualifiedName, String name) {
        return ModelEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique ModelEntity name.
     *
     * @param name of the ModelEntity
     * @param parentQualifiedName unique name of the model or version in which this ModelEntity exists
     * @return a unique name for the ModelEntity
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + IModel.getSlugForName(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModelEntity, from a potentially
     * more-complete ModelEntity object.
     *
     * @return the minimal object necessary to update the ModelEntity, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModelEntity are not found in the initial object
     */
    @Override
    public ModelEntityBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("modelVersionAgnosticQualifiedName", this.getModelVersionAgnosticQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getModelVersionAgnosticQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ModelEntity.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModelEntity
     * @param name of the ModelEntity
     * @return the updated ModelEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntity removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelEntity) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ModelEntity.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModelEntity
     * @param name of the ModelEntity
     * @return the updated ModelEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntity removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelEntity) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ModelEntity.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModelEntity's owners
     * @param qualifiedName of the ModelEntity
     * @param name of the ModelEntity
     * @return the updated ModelEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntity removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelEntity) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ModelEntity.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModelEntity's certificate
     * @param qualifiedName of the ModelEntity
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModelEntity, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntity updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ModelEntity)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModelEntity.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModelEntity's certificate
     * @param qualifiedName of the ModelEntity
     * @param name of the ModelEntity
     * @return the updated ModelEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntity removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelEntity) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ModelEntity.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModelEntity's announcement
     * @param qualifiedName of the ModelEntity
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntity updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ModelEntity)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModelEntity.
     *
     * @param client connectivity to the Atlan client from which to remove the ModelEntity's announcement
     * @param qualifiedName of the ModelEntity
     * @param name of the ModelEntity
     * @return the updated ModelEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntity removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelEntity) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ModelEntity.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ModelEntity's assigned terms
     * @param qualifiedName for the ModelEntity
     * @param name human-readable name of the ModelEntity
     * @param terms the list of terms to replace on the ModelEntity, or null to remove all terms from the ModelEntity
     * @return the ModelEntity that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModelEntity replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModelEntity) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModelEntity, without replacing existing terms linked to the ModelEntity.
     * Note: this operation must make two API calls — one to retrieve the ModelEntity's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ModelEntity
     * @param qualifiedName for the ModelEntity
     * @param terms the list of terms to append to the ModelEntity
     * @return the ModelEntity that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ModelEntity appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModelEntity) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModelEntity, without replacing all existing terms linked to the ModelEntity.
     * Note: this operation must make two API calls — one to retrieve the ModelEntity's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ModelEntity
     * @param qualifiedName for the ModelEntity
     * @param terms the list of terms to remove from the ModelEntity, which must be referenced by GUID
     * @return the ModelEntity that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ModelEntity removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModelEntity) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ModelEntity, without replacing existing Atlan tags linked to the ModelEntity.
     * Note: this operation must make two API calls — one to retrieve the ModelEntity's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModelEntity
     * @param qualifiedName of the ModelEntity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModelEntity
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static ModelEntity appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ModelEntity) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModelEntity, without replacing existing Atlan tags linked to the ModelEntity.
     * Note: this operation must make two API calls — one to retrieve the ModelEntity's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModelEntity
     * @param qualifiedName of the ModelEntity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModelEntity
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static ModelEntity appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ModelEntity) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ModelEntity.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ModelEntity
     * @param qualifiedName of the ModelEntity
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModelEntity
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
