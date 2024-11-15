/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.ModelCardinalityType;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a data attribute association in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class ModelAttributeAssociation extends Asset
        implements IModelAttributeAssociation, IModel, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModelAttributeAssociation";

    /** Fixed typeName for ModelAttributeAssociations. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** ApplicationContainer asset containing this Catalog asset. */
    @Attribute
    IApplicationContainer applicationContainer;

    /** Qualified name of the Application Container that contains this asset. */
    @Attribute
    String assetApplicationQualifiedName;

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

    /** Cardinality of the data attribute association. */
    @Attribute
    ModelCardinalityType modelAttributeAssociationCardinality;

    /** Attribute from which this association is related. */
    @Attribute
    IModelAttribute modelAttributeAssociationFrom;

    /** Unique name of the association from which this attribute is related. */
    @Attribute
    String modelAttributeAssociationFromQualifiedName;

    /** Label of the data attribute association. */
    @Attribute
    String modelAttributeAssociationLabel;

    /** Attribute to which this association is related. */
    @Attribute
    IModelAttribute modelAttributeAssociationTo;

    /** Unique name of the association to which this attribute is related. */
    @Attribute
    String modelAttributeAssociationToQualifiedName;

    /** Business date for the asset. */
    @Attribute
    @Date
    Long modelBusinessDate;

    /** Model domain in which this asset exists. */
    @Attribute
    String modelDomain;

    /** Unique name of the entity association to which this attribute is related. */
    @Attribute
    String modelEntityAssociationQualifiedName;

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
     * Builds the minimal object necessary to create a relationship to a ModelAttributeAssociation, from a potentially
     * more-complete ModelAttributeAssociation object.
     *
     * @return the minimal object necessary to relate to the ModelAttributeAssociation
     * @throws InvalidRequestException if any of the minimal set of required properties for a ModelAttributeAssociation relationship are not found in the initial object
     */
    @Override
    public ModelAttributeAssociation trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ModelAttributeAssociation assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModelAttributeAssociation assets will be included.
     *
     * @return a fluent search that includes all ModelAttributeAssociation assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all ModelAttributeAssociation assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModelAttributeAssociation assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ModelAttributeAssociation assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ModelAttributeAssociation assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ModelAttributeAssociations will be included
     * @return a fluent search that includes all ModelAttributeAssociation assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all ModelAttributeAssociation assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ModelAttributeAssociations will be included
     * @return a fluent search that includes all ModelAttributeAssociation assets
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
     * Reference to a ModelAttributeAssociation by GUID. Use this to create a relationship to this ModelAttributeAssociation,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ModelAttributeAssociation to reference
     * @return reference to a ModelAttributeAssociation that can be used for defining a relationship to a ModelAttributeAssociation
     */
    public static ModelAttributeAssociation refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModelAttributeAssociation by GUID. Use this to create a relationship to this ModelAttributeAssociation,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ModelAttributeAssociation to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModelAttributeAssociation that can be used for defining a relationship to a ModelAttributeAssociation
     */
    public static ModelAttributeAssociation refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ModelAttributeAssociation._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a ModelAttributeAssociation by qualifiedName. Use this to create a relationship to this ModelAttributeAssociation,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ModelAttributeAssociation to reference
     * @return reference to a ModelAttributeAssociation that can be used for defining a relationship to a ModelAttributeAssociation
     */
    public static ModelAttributeAssociation refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModelAttributeAssociation by qualifiedName. Use this to create a relationship to this ModelAttributeAssociation,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ModelAttributeAssociation to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModelAttributeAssociation that can be used for defining a relationship to a ModelAttributeAssociation
     */
    public static ModelAttributeAssociation refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ModelAttributeAssociation._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ModelAttributeAssociation by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the ModelAttributeAssociation to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModelAttributeAssociation, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelAttributeAssociation does not exist or the provided GUID is not a ModelAttributeAssociation
     */
    @JsonIgnore
    public static ModelAttributeAssociation get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a ModelAttributeAssociation by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelAttributeAssociation to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModelAttributeAssociation, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelAttributeAssociation does not exist or the provided GUID is not a ModelAttributeAssociation
     */
    @JsonIgnore
    public static ModelAttributeAssociation get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a ModelAttributeAssociation by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModelAttributeAssociation to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ModelAttributeAssociation, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModelAttributeAssociation does not exist or the provided GUID is not a ModelAttributeAssociation
     */
    @JsonIgnore
    public static ModelAttributeAssociation get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ModelAttributeAssociation) {
                return (ModelAttributeAssociation) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof ModelAttributeAssociation) {
                return (ModelAttributeAssociation) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ModelAttributeAssociation to active.
     *
     * @param qualifiedName for the ModelAttributeAssociation
     * @return true if the ModelAttributeAssociation is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ModelAttributeAssociation to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ModelAttributeAssociation
     * @return true if the ModelAttributeAssociation is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelAttributeAssociation.
     *
     * @param name of the ModelAttributeAssociation
     * @param from entity (version-agnostic) from which the association exists
     * @param to entity (version-agnostic) to which the association exists
     * @return the minimal request necessary to create the ModelAttributeAssociation, as a builder
     * @throws InvalidRequestException if the from or to are provided without a modelVersionAgnosticQualifiedName
     */
    public static ModelAttributeAssociationBuilder<?, ?> creator(String name, ModelAttribute from, ModelAttribute to)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("from_connectionQualifiedName", from.getConnectionQualifiedName());
        map.put("from_qualifiedName", from.getModelVersionAgnosticQualifiedName());
        map.put("to_qualifiedName", to.getModelVersionAgnosticQualifiedName());
        validateRelationship(ModelAttribute.TYPE_NAME, map);
        return creator(
                        name,
                        from.getConnectionQualifiedName(),
                        from.getModelVersionAgnosticQualifiedName(),
                        to.getModelVersionAgnosticQualifiedName())
                .modelAttributeAssociationFrom(from.trimToReference())
                .modelAttributeAssociationTo(to.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ModelAttributeAssociation.
     *
     * @param name of the ModelAttributeAssociation
     * @param fromQualifiedName unique (version-agnostic) name of the entity from which the association exists
     * @param toQualifiedName unique (version-agnostic) name of the entity to which the association exists
     * @return the minimal request necessary to create the ModelAttributeAssociation, as a builder
     */
    public static ModelAttributeAssociationBuilder<?, ?> creator(
            String name, String fromQualifiedName, String toQualifiedName) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(fromQualifiedName);
        return creator(name, connectionQualifiedName, fromQualifiedName, toQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelAttributeAssociation.
     *
     * @param name of the ModelAttributeAssociation
     * @param connectionQualifiedName unique name of the connection in which to create this ModelAttributeAssociation
     * @param fromQualifiedName unique (version-agnostic) name of the entity from which the association exists
     * @param toQualifiedName unique (version-agnostic) name of the entity to which the association exists
     * @return the minimal request necessary to create the ModelAttributeAssociation, as a builder
     */
    public static ModelAttributeAssociationBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String fromQualifiedName, String toQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        String qualifiedName = generateQualifiedName(name, fromQualifiedName, toQualifiedName);
        String modelEntityQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(fromQualifiedName);
        String modelEntityName = IModel.getNameFromSlug(StringUtils.getNameFromQualifiedName(modelEntityQualifiedName));
        String modelQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(modelEntityQualifiedName);
        String modelName = IModel.getNameFromSlug(StringUtils.getNameFromQualifiedName(modelQualifiedName));
        return ModelAttributeAssociation._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(qualifiedName)
                .modelVersionAgnosticQualifiedName(qualifiedName)
                .connectorType(connectorType)
                .connectionQualifiedName(connectionQualifiedName)
                .modelName(modelName)
                .modelQualifiedName(modelQualifiedName)
                .modelEntityName(modelEntityName)
                .modelEntityQualifiedName(modelEntityQualifiedName)
                .modelAttributeAssociationFrom(ModelAttribute.refByQualifiedName(fromQualifiedName))
                .modelAttributeAssociationFromQualifiedName(fromQualifiedName)
                .modelAttributeAssociationTo(ModelAttribute.refByQualifiedName(toQualifiedName))
                .modelAttributeAssociationToQualifiedName(toQualifiedName)
                .modelAttributeAssociationLabel(name);
    }

    /**
     * Builds the minimal object necessary to update a ModelAttributeAssociation.
     *
     * @param versionAgnosticQualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the minimal request necessary to update the ModelAttributeAssociation, as a builder
     */
    public static ModelAttributeAssociationBuilder<?, ?> updater(String versionAgnosticQualifiedName, String name) {
        return ModelAttributeAssociation._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .modelVersionAgnosticQualifiedName(versionAgnosticQualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to update a ModelAttributeAssociation.
     *
     * @param qualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the minimal request necessary to update the ModelAttributeAssociation, as a builder
     */
    public static ModelAttributeAssociationBuilder<?, ?> updaterForVersion(String qualifiedName, String name) {
        return ModelAttributeAssociation._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique ModelAttributeAssociation name.
     *
     * @param name of the ModelAttributeAssociation
     * @param fromQualifiedName unique (version-agnostic) name of the entity from which the association exists
     * @param toQualifiedName unique (version-agnostic) name of the entity to which the association exists
     * @return a unique name for the ModelAttributeAssociation
     */
    public static String generateQualifiedName(String name, String fromQualifiedName, String toQualifiedName) {
        return fromQualifiedName + "<<" + name + ">>" + toQualifiedName;
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModelAttributeAssociation, from a potentially
     * more-complete ModelAttributeAssociation object.
     *
     * @return the minimal object necessary to update the ModelAttributeAssociation, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModelAttributeAssociation are not found in the initial object
     */
    @Override
    public ModelAttributeAssociationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("modelVersionAgnosticQualifiedName", this.getModelVersionAgnosticQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getModelVersionAgnosticQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ModelAttributeAssociation.
     *
     * @param qualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the updated ModelAttributeAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a ModelAttributeAssociation.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the updated ModelAttributeAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelAttributeAssociation) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ModelAttributeAssociation.
     *
     * @param qualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the updated ModelAttributeAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a ModelAttributeAssociation.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the updated ModelAttributeAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelAttributeAssociation) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ModelAttributeAssociation.
     *
     * @param qualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the updated ModelAttributeAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a ModelAttributeAssociation.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModelAttributeAssociation's owners
     * @param qualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the updated ModelAttributeAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelAttributeAssociation) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ModelAttributeAssociation.
     *
     * @param qualifiedName of the ModelAttributeAssociation
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModelAttributeAssociation, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a ModelAttributeAssociation.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModelAttributeAssociation's certificate
     * @param qualifiedName of the ModelAttributeAssociation
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModelAttributeAssociation, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ModelAttributeAssociation)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModelAttributeAssociation.
     *
     * @param qualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the updated ModelAttributeAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a ModelAttributeAssociation.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModelAttributeAssociation's certificate
     * @param qualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the updated ModelAttributeAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelAttributeAssociation) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ModelAttributeAssociation.
     *
     * @param qualifiedName of the ModelAttributeAssociation
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a ModelAttributeAssociation.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModelAttributeAssociation's announcement
     * @param qualifiedName of the ModelAttributeAssociation
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ModelAttributeAssociation)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModelAttributeAssociation.
     *
     * @param qualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the updated ModelAttributeAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation removeAnnouncement(String qualifiedName, String name)
            throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a ModelAttributeAssociation.
     *
     * @param client connectivity to the Atlan client from which to remove the ModelAttributeAssociation's announcement
     * @param qualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the updated ModelAttributeAssociation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModelAttributeAssociation) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ModelAttributeAssociation.
     *
     * @param qualifiedName for the ModelAttributeAssociation
     * @param name human-readable name of the ModelAttributeAssociation
     * @param terms the list of terms to replace on the ModelAttributeAssociation, or null to remove all terms from the ModelAttributeAssociation
     * @return the ModelAttributeAssociation that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the ModelAttributeAssociation.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ModelAttributeAssociation's assigned terms
     * @param qualifiedName for the ModelAttributeAssociation
     * @param name human-readable name of the ModelAttributeAssociation
     * @param terms the list of terms to replace on the ModelAttributeAssociation, or null to remove all terms from the ModelAttributeAssociation
     * @return the ModelAttributeAssociation that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModelAttributeAssociation) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModelAttributeAssociation, without replacing existing terms linked to the ModelAttributeAssociation.
     * Note: this operation must make two API calls — one to retrieve the ModelAttributeAssociation's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ModelAttributeAssociation
     * @param terms the list of terms to append to the ModelAttributeAssociation
     * @return the ModelAttributeAssociation that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the ModelAttributeAssociation, without replacing existing terms linked to the ModelAttributeAssociation.
     * Note: this operation must make two API calls — one to retrieve the ModelAttributeAssociation's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ModelAttributeAssociation
     * @param qualifiedName for the ModelAttributeAssociation
     * @param terms the list of terms to append to the ModelAttributeAssociation
     * @return the ModelAttributeAssociation that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModelAttributeAssociation) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModelAttributeAssociation, without replacing all existing terms linked to the ModelAttributeAssociation.
     * Note: this operation must make two API calls — one to retrieve the ModelAttributeAssociation's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ModelAttributeAssociation
     * @param terms the list of terms to remove from the ModelAttributeAssociation, which must be referenced by GUID
     * @return the ModelAttributeAssociation that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a ModelAttributeAssociation, without replacing all existing terms linked to the ModelAttributeAssociation.
     * Note: this operation must make two API calls — one to retrieve the ModelAttributeAssociation's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ModelAttributeAssociation
     * @param qualifiedName for the ModelAttributeAssociation
     * @param terms the list of terms to remove from the ModelAttributeAssociation, which must be referenced by GUID
     * @return the ModelAttributeAssociation that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModelAttributeAssociation removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModelAttributeAssociation) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ModelAttributeAssociation, without replacing existing Atlan tags linked to the ModelAttributeAssociation.
     * Note: this operation must make two API calls — one to retrieve the ModelAttributeAssociation's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ModelAttributeAssociation
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModelAttributeAssociation
     */
    public static ModelAttributeAssociation appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModelAttributeAssociation, without replacing existing Atlan tags linked to the ModelAttributeAssociation.
     * Note: this operation must make two API calls — one to retrieve the ModelAttributeAssociation's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModelAttributeAssociation
     * @param qualifiedName of the ModelAttributeAssociation
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModelAttributeAssociation
     */
    public static ModelAttributeAssociation appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (ModelAttributeAssociation) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModelAttributeAssociation, without replacing existing Atlan tags linked to the ModelAttributeAssociation.
     * Note: this operation must make two API calls — one to retrieve the ModelAttributeAssociation's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ModelAttributeAssociation
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModelAttributeAssociation
     */
    public static ModelAttributeAssociation appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a ModelAttributeAssociation, without replacing existing Atlan tags linked to the ModelAttributeAssociation.
     * Note: this operation must make two API calls — one to retrieve the ModelAttributeAssociation's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModelAttributeAssociation
     * @param qualifiedName of the ModelAttributeAssociation
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModelAttributeAssociation
     */
    public static ModelAttributeAssociation appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ModelAttributeAssociation) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ModelAttributeAssociation.
     *
     * @param qualifiedName of the ModelAttributeAssociation
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModelAttributeAssociation
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a ModelAttributeAssociation.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ModelAttributeAssociation
     * @param qualifiedName of the ModelAttributeAssociation
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModelAttributeAssociation
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
