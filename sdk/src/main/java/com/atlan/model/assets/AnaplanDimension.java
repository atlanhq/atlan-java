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
 * Instances of an AnaplanDimension in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class AnaplanDimension extends Asset
        implements IAnaplanDimension, IAnaplan, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AnaplanDimension";

    /** Fixed typeName for AnaplanDimensions. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Views related to the column dimension. */
    @Attribute
    @Singular
    SortedSet<IAnaplanView> anaplanColumnViews;

    /** Line items related to the dimension. */
    @Attribute
    @Singular
    SortedSet<IAnaplanLineItem> anaplanLineItems;

    /** Model containing the dimension. */
    @Attribute
    IAnaplanModel anaplanModel;

    /** Simple name of the AnaplanModel asset that contains this asset (AnaplanModule and everything under its hierarchy). */
    @Attribute
    String anaplanModelName;

    /** Unique name of the AnaplanModel asset that contains this asset (AnaplanModule and everything under its hierarchy). */
    @Attribute
    String anaplanModelQualifiedName;

    /** Simple name of the AnaplanModule asset that contains this asset (AnaplanLineItem, AnaplanList, AnaplanView and everything under their hierarchy). */
    @Attribute
    String anaplanModuleName;

    /** Unique name of the AnaplanModule asset that contains this asset (AnaplanLineItem, AnaplanList, AnaplanView and everything under their hierarchy). */
    @Attribute
    String anaplanModuleQualifiedName;

    /** Views related to the page dimension. */
    @Attribute
    @Singular
    SortedSet<IAnaplanView> anaplanPageViews;

    /** Views related to the row dimension. */
    @Attribute
    @Singular
    SortedSet<IAnaplanView> anaplanRowViews;

    /** Id/Guid of the Anaplan asset in the source system. */
    @Attribute
    String anaplanSourceId;

    /** Simple name of the AnaplanWorkspace asset that contains this asset (AnaplanModel and everything under its hierarchy). */
    @Attribute
    String anaplanWorkspaceName;

    /** Unique name of the AnaplanWorkspace asset that contains this asset (AnaplanModel and everything under its hierarchy). */
    @Attribute
    String anaplanWorkspaceQualifiedName;

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

    /** Attributes implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

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
     * Builds the minimal object necessary to create a relationship to a AnaplanDimension, from a potentially
     * more-complete AnaplanDimension object.
     *
     * @return the minimal object necessary to relate to the AnaplanDimension
     * @throws InvalidRequestException if any of the minimal set of required properties for a AnaplanDimension relationship are not found in the initial object
     */
    @Override
    public AnaplanDimension trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AnaplanDimension assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AnaplanDimension assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AnaplanDimension assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AnaplanDimension assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AnaplanDimensions will be included
     * @return a fluent search that includes all AnaplanDimension assets
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
     * Reference to a AnaplanDimension by GUID. Use this to create a relationship to this AnaplanDimension,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AnaplanDimension to reference
     * @return reference to a AnaplanDimension that can be used for defining a relationship to a AnaplanDimension
     */
    public static AnaplanDimension refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AnaplanDimension by GUID. Use this to create a relationship to this AnaplanDimension,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AnaplanDimension to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AnaplanDimension that can be used for defining a relationship to a AnaplanDimension
     */
    public static AnaplanDimension refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AnaplanDimension._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AnaplanDimension by qualifiedName. Use this to create a relationship to this AnaplanDimension,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AnaplanDimension to reference
     * @return reference to a AnaplanDimension that can be used for defining a relationship to a AnaplanDimension
     */
    public static AnaplanDimension refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AnaplanDimension by qualifiedName. Use this to create a relationship to this AnaplanDimension,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AnaplanDimension to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AnaplanDimension that can be used for defining a relationship to a AnaplanDimension
     */
    public static AnaplanDimension refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AnaplanDimension._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AnaplanDimension by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanDimension to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AnaplanDimension, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanDimension does not exist or the provided GUID is not a AnaplanDimension
     */
    @JsonIgnore
    public static AnaplanDimension get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AnaplanDimension by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanDimension to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AnaplanDimension, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanDimension does not exist or the provided GUID is not a AnaplanDimension
     */
    @JsonIgnore
    public static AnaplanDimension get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AnaplanDimension) {
                return (AnaplanDimension) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AnaplanDimension) {
                return (AnaplanDimension) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AnaplanDimension by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanDimension to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AnaplanDimension, including any relationships
     * @return the requested AnaplanDimension, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanDimension does not exist or the provided GUID is not a AnaplanDimension
     */
    @JsonIgnore
    public static AnaplanDimension get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AnaplanDimension by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanDimension to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AnaplanDimension, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AnaplanDimension
     * @return the requested AnaplanDimension, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanDimension does not exist or the provided GUID is not a AnaplanDimension
     */
    @JsonIgnore
    public static AnaplanDimension get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AnaplanDimension.select(client)
                    .where(AnaplanDimension.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AnaplanDimension) {
                return (AnaplanDimension) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AnaplanDimension.select(client)
                    .where(AnaplanDimension.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AnaplanDimension) {
                return (AnaplanDimension) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AnaplanDimension to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AnaplanDimension
     * @return true if the AnaplanDimension is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Anaplan dimension.
     *
     * @param name of the dimension
     * @param model in which the dimension should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the dimension, as a builder
     * @throws InvalidRequestException if the model provided is without a qualifiedName
     */
    public static AnaplanDimension.AnaplanDimensionBuilder<?, ?> creator(String name, AnaplanModel model)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("modelQualifiedName", model.getQualifiedName());
        map.put("modelName", model.getName());
        map.put("connectionQualifiedName", model.getConnectionQualifiedName());
        map.put("workspaceName", model.getAnaplanWorkspaceName());
        map.put("workspaceQualifiedName", model.getAnaplanWorkspaceQualifiedName());
        validateRelationship(AnaplanWorkspace.TYPE_NAME, map);
        return creator(
                        name,
                        model.getConnectionQualifiedName(),
                        model.getName(),
                        model.getQualifiedName(),
                        model.getAnaplanWorkspaceName(),
                        model.getAnaplanWorkspaceQualifiedName())
                .anaplanModel(model.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Anaplan dimension.
     *
     * @param name of the dimension
     * @param modelQualifiedName unique name of the model in which this dimension exists
     * @return the minimal request necessary to create the dimension, as a builder
     */
    public static AnaplanDimension.AnaplanDimensionBuilder<?, ?> creator(String name, String modelQualifiedName) {
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(modelQualifiedName);
        String workspaceName = StringUtils.getNameFromQualifiedName(workspaceQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        String modelName = StringUtils.getNameFromQualifiedName(modelQualifiedName);
        return creator(
                name, connectionQualifiedName, modelName, modelQualifiedName, workspaceName, workspaceQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Anaplan dimension.
     *
     * @param name of the dimension
     * @param connectionQualifiedName unique name of the connection in which to create the dimension
     * @param modelName name of the model in which to create the dimension
     * @param modelQualifiedName unique name of the model in which to create the dimension
     * @param workspaceName name of the workspace in which to create the dimension
     * @param workspaceQualifiedName unique name of the workspace in which to create the dimension
     * @return the minimal request necessary to create the dimension, as a builder
     */
    public static AnaplanDimension.AnaplanDimensionBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String modelName,
            String modelQualifiedName,
            String workspaceName,
            String workspaceQualifiedName) {
        return AnaplanDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, modelQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .anaplanWorkspaceName(workspaceName)
                .anaplanWorkspaceQualifiedName(workspaceQualifiedName)
                .anaplanModelName(modelName)
                .anaplanModelQualifiedName(modelQualifiedName)
                .anaplanModel(AnaplanModel.refByQualifiedName(modelQualifiedName));
    }

    /**
     * Generate a unique dimension name.
     *
     * @param name of the dimension
     * @param modelQualifiedName unique name of the model in which this dimension exists
     * @return a unique name for the dimension
     */
    public static String generateQualifiedName(String name, String modelQualifiedName) {
        return modelQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a AnaplanDimension.
     *
     * @param qualifiedName of the AnaplanDimension
     * @param name of the AnaplanDimension
     * @return the minimal request necessary to update the AnaplanDimension, as a builder
     */
    public static AnaplanDimensionBuilder<?, ?> updater(String qualifiedName, String name) {
        return AnaplanDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AnaplanDimension, from a potentially
     * more-complete AnaplanDimension object.
     *
     * @return the minimal object necessary to update the AnaplanDimension, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AnaplanDimension are not found in the initial object
     */
    @Override
    public AnaplanDimensionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class AnaplanDimensionBuilder<
                    C extends AnaplanDimension, B extends AnaplanDimensionBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a AnaplanDimension.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AnaplanDimension
     * @param name of the AnaplanDimension
     * @return the updated AnaplanDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanDimension removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanDimension) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AnaplanDimension.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AnaplanDimension
     * @param name of the AnaplanDimension
     * @return the updated AnaplanDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanDimension removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanDimension) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AnaplanDimension.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AnaplanDimension's owners
     * @param qualifiedName of the AnaplanDimension
     * @param name of the AnaplanDimension
     * @return the updated AnaplanDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanDimension removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanDimension) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AnaplanDimension.
     *
     * @param client connectivity to the Atlan tenant on which to update the AnaplanDimension's certificate
     * @param qualifiedName of the AnaplanDimension
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AnaplanDimension, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanDimension updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AnaplanDimension)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AnaplanDimension.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AnaplanDimension's certificate
     * @param qualifiedName of the AnaplanDimension
     * @param name of the AnaplanDimension
     * @return the updated AnaplanDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanDimension removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanDimension) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AnaplanDimension.
     *
     * @param client connectivity to the Atlan tenant on which to update the AnaplanDimension's announcement
     * @param qualifiedName of the AnaplanDimension
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanDimension updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AnaplanDimension)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AnaplanDimension.
     *
     * @param client connectivity to the Atlan client from which to remove the AnaplanDimension's announcement
     * @param qualifiedName of the AnaplanDimension
     * @param name of the AnaplanDimension
     * @return the updated AnaplanDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanDimension removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanDimension) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AnaplanDimension.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AnaplanDimension's assigned terms
     * @param qualifiedName for the AnaplanDimension
     * @param name human-readable name of the AnaplanDimension
     * @param terms the list of terms to replace on the AnaplanDimension, or null to remove all terms from the AnaplanDimension
     * @return the AnaplanDimension that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AnaplanDimension replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AnaplanDimension) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AnaplanDimension, without replacing existing terms linked to the AnaplanDimension.
     * Note: this operation must make two API calls — one to retrieve the AnaplanDimension's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AnaplanDimension
     * @param qualifiedName for the AnaplanDimension
     * @param terms the list of terms to append to the AnaplanDimension
     * @return the AnaplanDimension that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AnaplanDimension appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AnaplanDimension) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AnaplanDimension, without replacing all existing terms linked to the AnaplanDimension.
     * Note: this operation must make two API calls — one to retrieve the AnaplanDimension's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AnaplanDimension
     * @param qualifiedName for the AnaplanDimension
     * @param terms the list of terms to remove from the AnaplanDimension, which must be referenced by GUID
     * @return the AnaplanDimension that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AnaplanDimension removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AnaplanDimension) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AnaplanDimension, without replacing existing Atlan tags linked to the AnaplanDimension.
     * Note: this operation must make two API calls — one to retrieve the AnaplanDimension's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AnaplanDimension
     * @param qualifiedName of the AnaplanDimension
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AnaplanDimension
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AnaplanDimension appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AnaplanDimension) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AnaplanDimension, without replacing existing Atlan tags linked to the AnaplanDimension.
     * Note: this operation must make two API calls — one to retrieve the AnaplanDimension's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AnaplanDimension
     * @param qualifiedName of the AnaplanDimension
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AnaplanDimension
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AnaplanDimension appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AnaplanDimension) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AnaplanDimension.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AnaplanDimension
     * @param qualifiedName of the AnaplanDimension
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AnaplanDimension
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
