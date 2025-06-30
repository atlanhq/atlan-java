/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
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
 * Instance of an attribute within a data model entity in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class ModelAttribute extends Asset implements IModelAttribute, IModel, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModelAttribute";

    /** Fixed typeName for ModelAttributes. */
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

    /** Type of the attribute. */
    @Attribute
    String modelAttributeDataType;

    /** Entity (or versions of an entity) in which this attribute exists. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelAttributeEntities;

    /** When true, this attribute has relationships with other attributes. */
    @Attribute
    Boolean modelAttributeHasRelationships;

    /** Assets that implement this attribute. */
    @Attribute
    @Singular
    SortedSet<ICatalog> modelAttributeImplementedByAssets;

    /** When true, the values in this attribute are derived data. */
    @Attribute
    Boolean modelAttributeIsDerived;

    /** When true, this attribute is a foreign key to another entity. */
    @Attribute
    Boolean modelAttributeIsForeign;

    /** When true, the values in this attribute can be null. */
    @Attribute
    Boolean modelAttributeIsNullable;

    /** When true, this attribute forms the primary key for the entity. */
    @Attribute
    Boolean modelAttributeIsPrimary;

    /** Attributes from which this attribute is mapped. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelAttributeMappedFromAttributes;

    /** Attributes to which this attribute is mapped. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelAttributeMappedToAttributes;

    /** Precision of the attribute. */
    @Attribute
    Long modelAttributePrecision;

    /** Association from which this attribute is related. */
    @Attribute
    @Singular
    SortedSet<IModelAttributeAssociation> modelAttributeRelatedFromAttributes;

    /** Association to which this attribute is related. */
    @Attribute
    @Singular
    SortedSet<IModelAttributeAssociation> modelAttributeRelatedToAttributes;

    /** Scale of the attribute. */
    @Attribute
    Long modelAttributeScale;

    /** Business date for the asset. */
    @Attribute
    @Date
    Long modelBusinessDate;

    /** Model domain in which this asset exists. */
    @Attribute
    String modelDomain;

    /** Simple name of the entity in which this asset exists, or empty if it is itself a data model entity. */
    @Attribute
    String modelEntityName;

    /** Unique name of the entity in which this asset exists, or empty if it is itself a data model entity. */
    @Attribute
    String modelEntityQualifiedName;

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
     * Builds the minimal object necessary to create a relationship to a ModelAttribute, from a potentially
     * more-complete ModelAttribute object.
     *
     * @return the minimal object necessary to relate to the ModelAttribute
     * @throws InvalidRequestException if any of the minimal set of required properties for a ModelAttribute relationship are not found in the initial object
     */
    @Override
    public ModelAttribute trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ModelAttribute assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModelAttribute assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ModelAttribute assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ModelAttribute assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ModelAttributes will be included
     * @return a fluent search that includes all ModelAttribute assets
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
     * Reference to a ModelAttribute by GUID. Use this to create a relationship to this ModelAttribute,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ModelAttribute to reference
     * @return reference to a ModelAttribute that can be used for defining a relationship to a ModelAttribute
     */
    public static ModelAttribute refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModelAttribute by GUID. Use this to create a relationship to this ModelAttribute,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ModelAttribute to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModelAttribute that can be used for defining a relationship to a ModelAttribute
     */
    public static ModelAttribute refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ModelAttribute._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ModelAttribute by qualifiedName. Use this to create a relationship to this ModelAttribute,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ModelAttribute to reference
     * @return reference to a ModelAttribute that can be used for defining a relationship to a ModelAttribute
     */
    public static ModelAttribute refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModelAttribute by qualifiedName. Use this to create a relationship to this ModelAttribute,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ModelAttribute to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModelAttribute that can be used for defining a relationship to a ModelAttribute
     */
    public static ModelAttribute refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ModelAttribute._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ModelAttribute by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelAttribute to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModelAttribute, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelAttribute does not exist or the provided GUID is not a ModelAttribute
     */
    @JsonIgnore
    public static ModelAttribute get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a ModelAttribute by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelAttribute to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ModelAttribute, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelAttribute does not exist or the provided GUID is not a ModelAttribute
     */
    @JsonIgnore
    public static ModelAttribute get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ModelAttribute) {
                return (ModelAttribute) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof ModelAttribute) {
                return (ModelAttribute) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ModelAttribute by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelAttribute to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ModelAttribute, including any relationships
     * @return the requested ModelAttribute, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelAttribute does not exist or the provided GUID is not a ModelAttribute
     */
    @JsonIgnore
    public static ModelAttribute get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a ModelAttribute by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelAttribute to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ModelAttribute, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the ModelAttribute
     * @return the requested ModelAttribute, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelAttribute does not exist or the provided GUID is not a ModelAttribute
     */
    @JsonIgnore
    public static ModelAttribute get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = ModelAttribute.select(client)
                    .where(ModelAttribute.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof ModelAttribute) {
                return (ModelAttribute) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = ModelAttribute.select(client)
                    .where(ModelAttribute.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof ModelAttribute) {
                return (ModelAttribute) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ModelAttribute to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ModelAttribute
     * @return true if the ModelAttribute is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelAttribute.
     *
     * @param name of the ModelAttribute
     * @param entity (version-agnostic) in which the entity should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the ModelAttribute, as a builder
     * @throws InvalidRequestException if the entity provided is without a qualifiedName
     */
    public static ModelAttributeBuilder<?, ?> creator(String name, ModelEntity entity) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", entity.getConnectionQualifiedName());
        map.put("name", entity.getName());
        map.put("qualifiedName", entity.getModelVersionAgnosticQualifiedName());
        map.put("type", entity.getModelType());
        validateRelationship(ModelEntity.TYPE_NAME, map);
        return creator(
                        name,
                        entity.getConnectionQualifiedName(),
                        entity.getName(),
                        entity.getModelVersionAgnosticQualifiedName(),
                        entity.getModelType())
                .clearModelAttributeEntities()
                .modelAttributeEntity(entity.trimToReference().toBuilder()
                        .semantic(SaveSemantic.APPEND)
                        .build());
    }

    /**
     * Builds the minimal object necessary to create a ModelAttribute.
     *
     * @param name of the ModelAttribute
     * @param entityQualifiedName unique (version-agnostic) name of the entity in which this ModelAttribute exists
     * @param modelType type of model in which this attribute exists
     * @return the minimal request necessary to create the ModelAttribute, as a builder
     */
    public static ModelAttributeBuilder<?, ?> creator(String name, String entityQualifiedName, String modelType) {
        String entitySlug = StringUtils.getNameFromQualifiedName(entityQualifiedName);
        String entityName = IModel.getNameFromSlug(entitySlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(entityQualifiedName);
        return creator(name, connectionQualifiedName, entityName, entityQualifiedName, modelType);
    }

    /**
     * Builds the minimal object necessary to create a ModelAttribute.
     *
     * @param name of the ModelAttribute
     * @param connectionQualifiedName unique name of the connection in which to create this ModelAttribute
     * @param entityName simple name (version-agnostic) of the entity in which to create this ModelAttribute
     * @param entityQualifiedName unique name (version-agnostic) of the entity in which to create this ModelAttribute
     * @param modelType type of model in which this attribute exists
     * @return the minimal request necessary to create the ModelAttribute, as a builder
     */
    public static ModelAttributeBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String entityName,
            String entityQualifiedName,
            String modelType) {
        String modelQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(entityQualifiedName);
        String modelName = IModel.getNameFromSlug(StringUtils.getNameFromQualifiedName(modelQualifiedName));
        return ModelAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .modelName(modelName)
                .modelQualifiedName(modelQualifiedName)
                .modelEntityName(entityName)
                .modelEntityQualifiedName(entityQualifiedName)
                .modelAttributeEntity(ModelEntity.refByQualifiedName(entityQualifiedName, SaveSemantic.APPEND))
                .modelVersionAgnosticQualifiedName(generateQualifiedName(name, entityQualifiedName))
                .modelType(modelType)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ModelAttribute.
     *
     * @param versionAgnosticQualifiedName of the ModelAttribute
     * @param name of the ModelAttribute
     * @return the minimal request necessary to update the ModelAttribute, as a builder
     */
    public static ModelAttributeBuilder<?, ?> updater(String versionAgnosticQualifiedName, String name) {
        return ModelAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .modelVersionAgnosticQualifiedName(versionAgnosticQualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to update a ModelAttribute.
     *
     * @param qualifiedName of the ModelAttribute
     * @param name of the ModelAttribute
     * @return the minimal request necessary to update the ModelAttribute, as a builder
     */
    public static ModelAttributeBuilder<?, ?> updaterForVersion(String qualifiedName, String name) {
        return ModelAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique ModelAttribute name.
     *
     * @param name of the ModelAttribute
     * @param parentQualifiedName unique name of the model or version in which this ModelAttribute exists
     * @return a unique name for the ModelAttribute
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + IModel.getSlugForName(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModelAttribute, from a potentially
     * more-complete ModelAttribute object.
     *
     * @return the minimal object necessary to update the ModelAttribute, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModelAttribute are not found in the initial object
     */
    @Override
    public ModelAttributeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("modelVersionAgnosticQualifiedName", this.getModelVersionAgnosticQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getModelVersionAgnosticQualifiedName(), this.getName());
    }

    public abstract static class ModelAttributeBuilder<C extends ModelAttribute, B extends ModelAttributeBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a ModelAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModelAttribute
     * @param name of the ModelAttribute
     * @return the updated ModelAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttribute removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelAttribute) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ModelAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModelAttribute
     * @param name of the ModelAttribute
     * @return the updated ModelAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttribute removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelAttribute) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ModelAttribute.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModelAttribute's owners
     * @param qualifiedName of the ModelAttribute
     * @param name of the ModelAttribute
     * @return the updated ModelAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttribute removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelAttribute) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ModelAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModelAttribute's certificate
     * @param qualifiedName of the ModelAttribute
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModelAttribute, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttribute updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ModelAttribute)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModelAttribute.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModelAttribute's certificate
     * @param qualifiedName of the ModelAttribute
     * @param name of the ModelAttribute
     * @return the updated ModelAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttribute removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelAttribute) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ModelAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModelAttribute's announcement
     * @param qualifiedName of the ModelAttribute
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttribute updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ModelAttribute)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModelAttribute.
     *
     * @param client connectivity to the Atlan client from which to remove the ModelAttribute's announcement
     * @param qualifiedName of the ModelAttribute
     * @param name of the ModelAttribute
     * @return the updated ModelAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttribute removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelAttribute) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ModelAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ModelAttribute's assigned terms
     * @param qualifiedName for the ModelAttribute
     * @param name human-readable name of the ModelAttribute
     * @param terms the list of terms to replace on the ModelAttribute, or null to remove all terms from the ModelAttribute
     * @return the ModelAttribute that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModelAttribute replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModelAttribute) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModelAttribute, without replacing existing terms linked to the ModelAttribute.
     * Note: this operation must make two API calls — one to retrieve the ModelAttribute's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ModelAttribute
     * @param qualifiedName for the ModelAttribute
     * @param terms the list of terms to append to the ModelAttribute
     * @return the ModelAttribute that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ModelAttribute appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModelAttribute) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModelAttribute, without replacing all existing terms linked to the ModelAttribute.
     * Note: this operation must make two API calls — one to retrieve the ModelAttribute's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ModelAttribute
     * @param qualifiedName for the ModelAttribute
     * @param terms the list of terms to remove from the ModelAttribute, which must be referenced by GUID
     * @return the ModelAttribute that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ModelAttribute removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModelAttribute) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ModelAttribute, without replacing existing Atlan tags linked to the ModelAttribute.
     * Note: this operation must make two API calls — one to retrieve the ModelAttribute's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModelAttribute
     * @param qualifiedName of the ModelAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModelAttribute
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static ModelAttribute appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ModelAttribute) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModelAttribute, without replacing existing Atlan tags linked to the ModelAttribute.
     * Note: this operation must make two API calls — one to retrieve the ModelAttribute's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModelAttribute
     * @param qualifiedName of the ModelAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModelAttribute
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static ModelAttribute appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ModelAttribute) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ModelAttribute.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ModelAttribute
     * @param qualifiedName of the ModelAttribute
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModelAttribute
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
