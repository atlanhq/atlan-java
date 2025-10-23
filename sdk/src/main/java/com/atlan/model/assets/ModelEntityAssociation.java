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
import com.atlan.model.enums.ModelCardinalityType;
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
 * Instance of a data entity association in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class ModelEntityAssociation extends Asset
        implements IModelEntityAssociation, IModel, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModelEntityAssociation";

    /** Fixed typeName for ModelEntityAssociations. */
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

    /** (Deprecated) Cardinality of the data entity association. */
    @Attribute
    ModelCardinalityType modelEntityAssociationCardinality;

    /** Entity from which this association is related. */
    @Attribute
    IModelEntity modelEntityAssociationFrom;

    /** Label when read from the association from which this entity is related. */
    @Attribute
    String modelEntityAssociationFromLabel;

    /** Maximum cardinality of the data entity from which the association exists. */
    @Attribute
    Long modelEntityAssociationFromMaxCardinality;

    /** Minimum cardinality of the data entity from which the association exists. */
    @Attribute
    Long modelEntityAssociationFromMinCardinality;

    /** Unique name of the association from which this entity is related. */
    @Attribute
    String modelEntityAssociationFromQualifiedName;

    /** (Deprecated) Label of the data entity association. */
    @Attribute
    String modelEntityAssociationLabel;

    /** Entity to which this association is related. */
    @Attribute
    IModelEntity modelEntityAssociationTo;

    /** Label when read from the association to which this entity is related. */
    @Attribute
    String modelEntityAssociationToLabel;

    /** Maximum cardinality of the data entity to which the association exists. */
    @Attribute
    Long modelEntityAssociationToMaxCardinality;

    /** Minimum cardinality of the data entity to which the association exists. */
    @Attribute
    Long modelEntityAssociationToMinCardinality;

    /** Unique name of the association to which this entity is related. */
    @Attribute
    String modelEntityAssociationToQualifiedName;

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
     * Builds the minimal object necessary to create a relationship to a ModelEntityAssociation, from a potentially
     * more-complete ModelEntityAssociation object.
     *
     * @return the minimal object necessary to relate to the ModelEntityAssociation
     * @throws InvalidRequestException if any of the minimal set of required properties for a ModelEntityAssociation relationship are not found in the initial object
     */
    @Override
    public ModelEntityAssociation trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ModelEntityAssociation assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModelEntityAssociation assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ModelEntityAssociation assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ModelEntityAssociation assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ModelEntityAssociations will be included
     * @return a fluent search that includes all ModelEntityAssociation assets
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
     * Reference to a ModelEntityAssociation by GUID. Use this to create a relationship to this ModelEntityAssociation,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ModelEntityAssociation to reference
     * @return reference to a ModelEntityAssociation that can be used for defining a relationship to a ModelEntityAssociation
     */
    public static ModelEntityAssociation refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModelEntityAssociation by GUID. Use this to create a relationship to this ModelEntityAssociation,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ModelEntityAssociation to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModelEntityAssociation that can be used for defining a relationship to a ModelEntityAssociation
     */
    public static ModelEntityAssociation refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ModelEntityAssociation._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ModelEntityAssociation by qualifiedName. Use this to create a relationship to this ModelEntityAssociation,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ModelEntityAssociation to reference
     * @return reference to a ModelEntityAssociation that can be used for defining a relationship to a ModelEntityAssociation
     */
    public static ModelEntityAssociation refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModelEntityAssociation by qualifiedName. Use this to create a relationship to this ModelEntityAssociation,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ModelEntityAssociation to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModelEntityAssociation that can be used for defining a relationship to a ModelEntityAssociation
     */
    public static ModelEntityAssociation refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ModelEntityAssociation._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ModelEntityAssociation by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelEntityAssociation to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModelEntityAssociation, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelEntityAssociation does not exist or the provided GUID is not a ModelEntityAssociation
     */
    @JsonIgnore
    public static ModelEntityAssociation get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a ModelEntityAssociation by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelEntityAssociation to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ModelEntityAssociation, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelEntityAssociation does not exist or the provided GUID is not a ModelEntityAssociation
     */
    @JsonIgnore
    public static ModelEntityAssociation get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ModelEntityAssociation) {
                return (ModelEntityAssociation) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof ModelEntityAssociation) {
                return (ModelEntityAssociation) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ModelEntityAssociation by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelEntityAssociation to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ModelEntityAssociation, including any relationships
     * @return the requested ModelEntityAssociation, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelEntityAssociation does not exist or the provided GUID is not a ModelEntityAssociation
     */
    @JsonIgnore
    public static ModelEntityAssociation get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a ModelEntityAssociation by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelEntityAssociation to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ModelEntityAssociation, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the ModelEntityAssociation
     * @return the requested ModelEntityAssociation, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelEntityAssociation does not exist or the provided GUID is not a ModelEntityAssociation
     */
    @JsonIgnore
    public static ModelEntityAssociation get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = ModelEntityAssociation.select(client)
                    .where(ModelEntityAssociation.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof ModelEntityAssociation) {
                return (ModelEntityAssociation) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = ModelEntityAssociation.select(client)
                    .where(ModelEntityAssociation.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof ModelEntityAssociation) {
                return (ModelEntityAssociation) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ModelEntityAssociation to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ModelEntityAssociation
     * @return true if the ModelEntityAssociation is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelEntityAssociation.
     *
     * @param name of the ModelEntityAssociation
     * @param from entity (version-agnostic) from which the association exists
     * @param to entity (version-agnostic) to which the association exists
     * @return the minimal request necessary to create the ModelEntityAssociation, as a builder
     * @throws InvalidRequestException if the from or to are provided without a modelVersionAgnosticQualifiedName
     */
    public static ModelEntityAssociationBuilder<?, ?> creator(String name, ModelEntity from, ModelEntity to)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("from_connectionQualifiedName", from.getConnectionQualifiedName());
        map.put("from_qualifiedName", from.getModelVersionAgnosticQualifiedName());
        map.put("to_qualifiedName", to.getModelVersionAgnosticQualifiedName());
        validateRelationship(ModelEntity.TYPE_NAME, map);
        return creator(
                        name,
                        from.getConnectionQualifiedName(),
                        from.getModelVersionAgnosticQualifiedName(),
                        to.getModelVersionAgnosticQualifiedName())
                .modelEntityAssociationFrom(from.trimToReference())
                .modelEntityAssociationTo(to.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ModelEntityAssociation.
     *
     * @param name of the ModelEntityAssociation
     * @param fromQualifiedName unique (version-agnostic) name of the entity from which the association exists
     * @param toQualifiedName unique (version-agnostic) name of the entity to which the association exists
     * @return the minimal request necessary to create the ModelEntityAssociation, as a builder
     */
    public static ModelEntityAssociationBuilder<?, ?> creator(
            String name, String fromQualifiedName, String toQualifiedName) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(fromQualifiedName);
        return creator(name, connectionQualifiedName, fromQualifiedName, toQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelEntityAssociation.
     *
     * @param name of the ModelEntityAssociation
     * @param connectionQualifiedName unique name of the connection in which to create this ModelEntityAssociation
     * @param fromQualifiedName unique (version-agnostic) name of the entity from which the association exists
     * @param toQualifiedName unique (version-agnostic) name of the entity to which the association exists
     * @return the minimal request necessary to create the ModelEntityAssociation, as a builder
     */
    public static ModelEntityAssociationBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String fromQualifiedName, String toQualifiedName) {
        String qualifiedName = generateQualifiedName(name, fromQualifiedName, toQualifiedName);
        String modelQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(fromQualifiedName);
        String modelName = IModel.getNameFromSlug(StringUtils.getNameFromQualifiedName(modelQualifiedName));
        return ModelEntityAssociation._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(qualifiedName)
                .modelVersionAgnosticQualifiedName(qualifiedName)
                .connectionQualifiedName(connectionQualifiedName)
                .modelName(modelName)
                .modelQualifiedName(modelQualifiedName)
                .modelEntityAssociationFrom(ModelEntity.refByQualifiedName(fromQualifiedName))
                .modelEntityAssociationFromQualifiedName(fromQualifiedName)
                .modelEntityAssociationTo(ModelEntity.refByQualifiedName(toQualifiedName))
                .modelEntityAssociationToQualifiedName(toQualifiedName)
                .modelEntityAssociationLabel(name);
    }

    /**
     * Builds the minimal object necessary to update a ModelEntityAssociation.
     *
     * @param versionAgnosticQualifiedName of the ModelEntityAssociation
     * @param name of the ModelEntityAssociation
     * @return the minimal request necessary to update the ModelEntityAssociation, as a builder
     */
    public static ModelEntityAssociationBuilder<?, ?> updater(String versionAgnosticQualifiedName, String name) {
        return ModelEntityAssociation._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .modelVersionAgnosticQualifiedName(versionAgnosticQualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to update a ModelEntityAssociation.
     *
     * @param qualifiedName of the ModelEntityAssociation
     * @param name of the ModelEntityAssociation
     * @return the minimal request necessary to update the ModelEntityAssociation, as a builder
     */
    public static ModelEntityAssociationBuilder<?, ?> updaterForVersion(String qualifiedName, String name) {
        return ModelEntityAssociation._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique ModelEntityAssociation name.
     *
     * @param name of the ModelEntityAssociation
     * @param fromQualifiedName unique (version-agnostic) name of the entity from which the association exists
     * @param toQualifiedName unique (version-agnostic) name of the entity to which the association exists
     * @return a unique name for the ModelEntityAssociation
     */
    public static String generateQualifiedName(String name, String fromQualifiedName, String toQualifiedName) {
        return fromQualifiedName + "<<" + name + ">>" + toQualifiedName;
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModelEntityAssociation, from a potentially
     * more-complete ModelEntityAssociation object.
     *
     * @return the minimal object necessary to update the ModelEntityAssociation, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModelEntityAssociation are not found in the initial object
     */
    @Override
    public ModelEntityAssociationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("modelVersionAgnosticQualifiedName", this.getModelVersionAgnosticQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getModelVersionAgnosticQualifiedName(), this.getName());
    }

    public abstract static class ModelEntityAssociationBuilder<
                    C extends ModelEntityAssociation, B extends ModelEntityAssociationBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a ModelEntityAssociation.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModelEntityAssociation
     * @param name of the ModelEntityAssociation
     * @return the updated ModelEntityAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntityAssociation removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelEntityAssociation) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ModelEntityAssociation.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModelEntityAssociation
     * @param name of the ModelEntityAssociation
     * @return the updated ModelEntityAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntityAssociation removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelEntityAssociation) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ModelEntityAssociation.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModelEntityAssociation's owners
     * @param qualifiedName of the ModelEntityAssociation
     * @param name of the ModelEntityAssociation
     * @return the updated ModelEntityAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntityAssociation removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelEntityAssociation) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ModelEntityAssociation.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModelEntityAssociation's certificate
     * @param qualifiedName of the ModelEntityAssociation
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModelEntityAssociation, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntityAssociation updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ModelEntityAssociation)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModelEntityAssociation.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModelEntityAssociation's certificate
     * @param qualifiedName of the ModelEntityAssociation
     * @param name of the ModelEntityAssociation
     * @return the updated ModelEntityAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntityAssociation removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelEntityAssociation) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ModelEntityAssociation.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModelEntityAssociation's announcement
     * @param qualifiedName of the ModelEntityAssociation
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntityAssociation updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ModelEntityAssociation)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModelEntityAssociation.
     *
     * @param client connectivity to the Atlan client from which to remove the ModelEntityAssociation's announcement
     * @param qualifiedName of the ModelEntityAssociation
     * @param name of the ModelEntityAssociation
     * @return the updated ModelEntityAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelEntityAssociation removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelEntityAssociation) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ModelEntityAssociation.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ModelEntityAssociation's assigned terms
     * @param qualifiedName for the ModelEntityAssociation
     * @param name human-readable name of the ModelEntityAssociation
     * @param terms the list of terms to replace on the ModelEntityAssociation, or null to remove all terms from the ModelEntityAssociation
     * @return the ModelEntityAssociation that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModelEntityAssociation replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModelEntityAssociation) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModelEntityAssociation, without replacing existing terms linked to the ModelEntityAssociation.
     * Note: this operation must make two API calls — one to retrieve the ModelEntityAssociation's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ModelEntityAssociation
     * @param qualifiedName for the ModelEntityAssociation
     * @param terms the list of terms to append to the ModelEntityAssociation
     * @return the ModelEntityAssociation that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ModelEntityAssociation appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModelEntityAssociation) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModelEntityAssociation, without replacing all existing terms linked to the ModelEntityAssociation.
     * Note: this operation must make two API calls — one to retrieve the ModelEntityAssociation's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ModelEntityAssociation
     * @param qualifiedName for the ModelEntityAssociation
     * @param terms the list of terms to remove from the ModelEntityAssociation, which must be referenced by GUID
     * @return the ModelEntityAssociation that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ModelEntityAssociation removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModelEntityAssociation) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ModelEntityAssociation, without replacing existing Atlan tags linked to the ModelEntityAssociation.
     * Note: this operation must make two API calls — one to retrieve the ModelEntityAssociation's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModelEntityAssociation
     * @param qualifiedName of the ModelEntityAssociation
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModelEntityAssociation
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static ModelEntityAssociation appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (ModelEntityAssociation) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModelEntityAssociation, without replacing existing Atlan tags linked to the ModelEntityAssociation.
     * Note: this operation must make two API calls — one to retrieve the ModelEntityAssociation's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModelEntityAssociation
     * @param qualifiedName of the ModelEntityAssociation
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModelEntityAssociation
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static ModelEntityAssociation appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ModelEntityAssociation) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ModelEntityAssociation.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ModelEntityAssociation
     * @param qualifiedName of the ModelEntityAssociation
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModelEntityAssociation
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
