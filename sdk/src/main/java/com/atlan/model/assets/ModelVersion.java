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
 * Instance of a version of a data model in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class ModelVersion extends Asset implements IModelVersion, IModel, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModelVersion";

    /** Fixed typeName for ModelVersions. */
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

    /** Data model for which this version exists. */
    @Attribute
    IModelDataModel modelDataModel;

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

    /** Individual entities that make up this version of the data model. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelVersionEntities;

    /** Number of entities in the version. */
    @Attribute
    Long modelVersionEntityCount;

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
     * Builds the minimal object necessary to create a relationship to a ModelVersion, from a potentially
     * more-complete ModelVersion object.
     *
     * @return the minimal object necessary to relate to the ModelVersion
     * @throws InvalidRequestException if any of the minimal set of required properties for a ModelVersion relationship are not found in the initial object
     */
    @Override
    public ModelVersion trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ModelVersion assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModelVersion assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ModelVersion assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ModelVersion assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ModelVersions will be included
     * @return a fluent search that includes all ModelVersion assets
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
     * Reference to a ModelVersion by GUID. Use this to create a relationship to this ModelVersion,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ModelVersion to reference
     * @return reference to a ModelVersion that can be used for defining a relationship to a ModelVersion
     */
    public static ModelVersion refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModelVersion by GUID. Use this to create a relationship to this ModelVersion,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ModelVersion to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModelVersion that can be used for defining a relationship to a ModelVersion
     */
    public static ModelVersion refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ModelVersion._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ModelVersion by qualifiedName. Use this to create a relationship to this ModelVersion,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ModelVersion to reference
     * @return reference to a ModelVersion that can be used for defining a relationship to a ModelVersion
     */
    public static ModelVersion refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModelVersion by qualifiedName. Use this to create a relationship to this ModelVersion,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ModelVersion to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModelVersion that can be used for defining a relationship to a ModelVersion
     */
    public static ModelVersion refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ModelVersion._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ModelVersion by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelVersion to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModelVersion, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelVersion does not exist or the provided GUID is not a ModelVersion
     */
    @JsonIgnore
    public static ModelVersion get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a ModelVersion by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelVersion to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ModelVersion, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelVersion does not exist or the provided GUID is not a ModelVersion
     */
    @JsonIgnore
    public static ModelVersion get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ModelVersion) {
                return (ModelVersion) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof ModelVersion) {
                return (ModelVersion) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ModelVersion by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelVersion to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ModelVersion, including any relationships
     * @return the requested ModelVersion, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelVersion does not exist or the provided GUID is not a ModelVersion
     */
    @JsonIgnore
    public static ModelVersion get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a ModelVersion by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelVersion to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ModelVersion, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the ModelVersion
     * @return the requested ModelVersion, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelVersion does not exist or the provided GUID is not a ModelVersion
     */
    @JsonIgnore
    public static ModelVersion get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = ModelVersion.select(client)
                    .where(ModelVersion.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof ModelVersion) {
                return (ModelVersion) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = ModelVersion.select(client)
                    .where(ModelVersion.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof ModelVersion) {
                return (ModelVersion) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ModelVersion to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ModelVersion
     * @return true if the ModelVersion is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelVersion.
     *
     * @param name of the ModelVersion
     * @param model in which the version should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the ModelVersion, as a builder
     * @throws InvalidRequestException if the model provided is without a qualifiedName
     */
    public static ModelVersionBuilder<?, ?> creator(String name, ModelDataModel model) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", model.getConnectionQualifiedName());
        map.put("name", model.getName());
        map.put("qualifiedName", model.getQualifiedName());
        validateRelationship(ModelDataModel.TYPE_NAME, map);
        return creator(name, model.getConnectionQualifiedName(), model.getName(), model.getQualifiedName())
                .modelDataModel(model.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ModelVersion.
     *
     * @param name of the ModelVersion
     * @param modelQualifiedName unique name of the model in which this ModelVersion exists
     * @return the minimal request necessary to create the ModelVersion, as a builder
     */
    public static ModelVersionBuilder<?, ?> creator(String name, String modelQualifiedName) {
        String modelSlug = StringUtils.getNameFromQualifiedName(modelQualifiedName);
        String modelName = IModel.getNameFromSlug(modelSlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(modelQualifiedName);
        return creator(name, connectionQualifiedName, modelName, modelQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelVersion.
     *
     * @param name of the ModelVersion
     * @param connectionQualifiedName unique name of the connection in which to create this ModelVersion
     * @param modelName simple name of the model in which to create this ModelVersion
     * @param modelQualifiedName unique name of the model in which to create this ModelVersion
     * @return the minimal request necessary to create the ModelVersion, as a builder
     */
    public static ModelVersionBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String modelName, String modelQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return ModelVersion._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, modelQualifiedName))
                .modelName(modelName)
                .modelQualifiedName(modelQualifiedName)
                .modelDataModel(ModelDataModel.refByQualifiedName(modelQualifiedName))
                .connectorType(connectorType)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ModelVersion.
     *
     * @param qualifiedName of the ModelVersion
     * @param name of the ModelVersion
     * @return the minimal request necessary to update the ModelVersion, as a builder
     */
    public static ModelVersionBuilder<?, ?> updater(String qualifiedName, String name) {
        return ModelVersion._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique ModelVersion name.
     *
     * @param name of the ModelVersion
     * @param modelQualifiedName unique name of the model in which this ModelVersion exists
     * @return a unique name for the ModelVersion
     */
    public static String generateQualifiedName(String name, String modelQualifiedName) {
        return modelQualifiedName + "/" + IModel.getSlugForName(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModelVersion, from a potentially
     * more-complete ModelVersion object.
     *
     * @return the minimal object necessary to update the ModelVersion, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModelVersion are not found in the initial object
     */
    @Override
    public ModelVersionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModelVersion
     * @param name of the ModelVersion
     * @return the updated ModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelVersion removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelVersion) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModelVersion
     * @param name of the ModelVersion
     * @return the updated ModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelVersion removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelVersion) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ModelVersion.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModelVersion's owners
     * @param qualifiedName of the ModelVersion
     * @param name of the ModelVersion
     * @return the updated ModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelVersion removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelVersion) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModelVersion's certificate
     * @param qualifiedName of the ModelVersion
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModelVersion, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModelVersion updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ModelVersion)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModelVersion.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModelVersion's certificate
     * @param qualifiedName of the ModelVersion
     * @param name of the ModelVersion
     * @return the updated ModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelVersion removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelVersion) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModelVersion's announcement
     * @param qualifiedName of the ModelVersion
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModelVersion updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ModelVersion)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModelVersion.
     *
     * @param client connectivity to the Atlan client from which to remove the ModelVersion's announcement
     * @param qualifiedName of the ModelVersion
     * @param name of the ModelVersion
     * @return the updated ModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelVersion removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelVersion) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ModelVersion's assigned terms
     * @param qualifiedName for the ModelVersion
     * @param name human-readable name of the ModelVersion
     * @param terms the list of terms to replace on the ModelVersion, or null to remove all terms from the ModelVersion
     * @return the ModelVersion that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModelVersion replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModelVersion) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModelVersion, without replacing existing terms linked to the ModelVersion.
     * Note: this operation must make two API calls — one to retrieve the ModelVersion's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ModelVersion
     * @param qualifiedName for the ModelVersion
     * @param terms the list of terms to append to the ModelVersion
     * @return the ModelVersion that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ModelVersion appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModelVersion) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModelVersion, without replacing all existing terms linked to the ModelVersion.
     * Note: this operation must make two API calls — one to retrieve the ModelVersion's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ModelVersion
     * @param qualifiedName for the ModelVersion
     * @param terms the list of terms to remove from the ModelVersion, which must be referenced by GUID
     * @return the ModelVersion that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ModelVersion removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModelVersion) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ModelVersion, without replacing existing Atlan tags linked to the ModelVersion.
     * Note: this operation must make two API calls — one to retrieve the ModelVersion's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModelVersion
     * @param qualifiedName of the ModelVersion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModelVersion
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static ModelVersion appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ModelVersion) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModelVersion, without replacing existing Atlan tags linked to the ModelVersion.
     * Note: this operation must make two API calls — one to retrieve the ModelVersion's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModelVersion
     * @param qualifiedName of the ModelVersion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModelVersion
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static ModelVersion appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ModelVersion) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ModelVersion.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ModelVersion
     * @param qualifiedName of the ModelVersion
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModelVersion
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
