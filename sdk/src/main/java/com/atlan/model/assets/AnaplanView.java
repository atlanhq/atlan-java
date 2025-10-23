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
 * Instances of an AnaplanView in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class AnaplanView extends Asset implements IAnaplanView, IAnaplan, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AnaplanView";

    /** Fixed typeName for AnaplanViews. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Column dimensions related to the view. */
    @Attribute
    @Singular
    SortedSet<IAnaplanDimension> anaplanColumnDimensions;

    /** Simple name of the AnaplanModel asset that contains this asset (AnaplanModule and everything under its hierarchy). */
    @Attribute
    String anaplanModelName;

    /** Unique name of the AnaplanModel asset that contains this asset (AnaplanModule and everything under its hierarchy). */
    @Attribute
    String anaplanModelQualifiedName;

    /** Module containing the view. */
    @Attribute
    IAnaplanModule anaplanModule;

    /** Simple name of the AnaplanModule asset that contains this asset (AnaplanLineItem, AnaplanList, AnaplanView and everything under their hierarchy). */
    @Attribute
    String anaplanModuleName;

    /** Unique name of the AnaplanModule asset that contains this asset (AnaplanLineItem, AnaplanList, AnaplanView and everything under their hierarchy). */
    @Attribute
    String anaplanModuleQualifiedName;

    /** Page dimensions related to the view. */
    @Attribute
    @Singular
    SortedSet<IAnaplanDimension> anaplanPageDimensions;

    /** Row dimensions related to the view. */
    @Attribute
    @Singular
    SortedSet<IAnaplanDimension> anaplanRowDimensions;

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
     * Builds the minimal object necessary to create a relationship to a AnaplanView, from a potentially
     * more-complete AnaplanView object.
     *
     * @return the minimal object necessary to relate to the AnaplanView
     * @throws InvalidRequestException if any of the minimal set of required properties for a AnaplanView relationship are not found in the initial object
     */
    @Override
    public AnaplanView trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AnaplanView assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AnaplanView assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AnaplanView assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AnaplanView assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AnaplanViews will be included
     * @return a fluent search that includes all AnaplanView assets
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
     * Reference to a AnaplanView by GUID. Use this to create a relationship to this AnaplanView,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AnaplanView to reference
     * @return reference to a AnaplanView that can be used for defining a relationship to a AnaplanView
     */
    public static AnaplanView refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AnaplanView by GUID. Use this to create a relationship to this AnaplanView,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AnaplanView to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AnaplanView that can be used for defining a relationship to a AnaplanView
     */
    public static AnaplanView refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AnaplanView._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AnaplanView by qualifiedName. Use this to create a relationship to this AnaplanView,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AnaplanView to reference
     * @return reference to a AnaplanView that can be used for defining a relationship to a AnaplanView
     */
    public static AnaplanView refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AnaplanView by qualifiedName. Use this to create a relationship to this AnaplanView,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AnaplanView to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AnaplanView that can be used for defining a relationship to a AnaplanView
     */
    public static AnaplanView refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AnaplanView._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AnaplanView by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanView to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AnaplanView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanView does not exist or the provided GUID is not a AnaplanView
     */
    @JsonIgnore
    public static AnaplanView get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AnaplanView by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanView to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AnaplanView, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanView does not exist or the provided GUID is not a AnaplanView
     */
    @JsonIgnore
    public static AnaplanView get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AnaplanView) {
                return (AnaplanView) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AnaplanView) {
                return (AnaplanView) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AnaplanView by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanView to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AnaplanView, including any relationships
     * @return the requested AnaplanView, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanView does not exist or the provided GUID is not a AnaplanView
     */
    @JsonIgnore
    public static AnaplanView get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AnaplanView by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanView to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AnaplanView, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AnaplanView
     * @return the requested AnaplanView, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanView does not exist or the provided GUID is not a AnaplanView
     */
    @JsonIgnore
    public static AnaplanView get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AnaplanView.select(client)
                    .where(AnaplanView.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AnaplanView) {
                return (AnaplanView) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AnaplanView.select(client)
                    .where(AnaplanView.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AnaplanView) {
                return (AnaplanView) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AnaplanView to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AnaplanView
     * @return true if the AnaplanView is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Anaplan view.
     *
     * @param name of the view
     * @param module in which the view should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the view, as a builder
     * @throws InvalidRequestException if the module provided is without a qualifiedName
     */
    public static AnaplanView.AnaplanViewBuilder<?, ?> creator(String name, AnaplanModule module)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", module.getConnectionQualifiedName());
        map.put("workspaceQualifiedName", module.getAnaplanWorkspaceQualifiedName());
        map.put("workspaceName", module.getAnaplanWorkspaceName());
        map.put("modelQualifiedName", module.getAnaplanModelQualifiedName());
        map.put("modelName", module.getAnaplanModelName());
        map.put("moduleQualifiedName", module.getQualifiedName());
        map.put("moduleName", module.getName());
        validateRelationship(AnaplanWorkspace.TYPE_NAME, map);
        return creator(
                        name,
                        module.getConnectionQualifiedName(),
                        module.getName(),
                        module.getQualifiedName(),
                        module.getAnaplanModelName(),
                        module.getAnaplanModelQualifiedName(),
                        module.getAnaplanWorkspaceName(),
                        module.getAnaplanWorkspaceQualifiedName())
                .anaplanModule(module.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Anaplan view.
     *
     * @param name of the view
     * @param moduleQualifiedName unique name of the module in which this view exists
     * @return the minimal request necessary to create the view, as a builder
     */
    public static AnaplanView.AnaplanViewBuilder<?, ?> creator(String name, String moduleQualifiedName) {
        String moduleName = StringUtils.getNameFromQualifiedName(moduleQualifiedName);
        String modelQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(moduleQualifiedName);
        String modelName = StringUtils.getNameFromQualifiedName(modelQualifiedName);
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(modelQualifiedName);
        String workspaceName = StringUtils.getNameFromQualifiedName(workspaceQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return creator(
                name,
                connectionQualifiedName,
                moduleName,
                moduleQualifiedName,
                modelName,
                modelQualifiedName,
                workspaceName,
                workspaceQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Anaplan view.
     *
     * @param name of the view
     * @param connectionQualifiedName unique name of the connection in which to create the view
     * @param moduleName name of the module in which to creat the view
     * @param moduleQualifiedName unique name of the module in which to create the view
     * @param modelName name of the model in which to create the view
     * @param modelQualifiedName unique name of the model in which to create the view
     * @param workspaceName name of the workspace in which to create the view
     * @param workspaceQualifiedName unique name of the workspace in which to create the view
     * @return the minimal request necessary to create the view, as a builder
     */
    public static AnaplanView.AnaplanViewBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String moduleName,
            String moduleQualifiedName,
            String modelName,
            String modelQualifiedName,
            String workspaceName,
            String workspaceQualifiedName) {
        return AnaplanView._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, moduleQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .anaplanWorkspaceName(workspaceName)
                .anaplanWorkspaceQualifiedName(workspaceQualifiedName)
                .anaplanModelName(modelName)
                .anaplanModelQualifiedName(modelQualifiedName)
                .anaplanModuleName(moduleName)
                .anaplanModuleQualifiedName(moduleQualifiedName)
                .anaplanModule(AnaplanModule.refByQualifiedName(moduleQualifiedName));
    }

    /**
     * Generate a unique view name.
     *
     * @param name of the view
     * @param moduleQualifiedName unique name of the module in which this view exists
     * @return a unique name for the view
     */
    public static String generateQualifiedName(String name, String moduleQualifiedName) {
        return moduleQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a AnaplanView.
     *
     * @param qualifiedName of the AnaplanView
     * @param name of the AnaplanView
     * @return the minimal request necessary to update the AnaplanView, as a builder
     */
    public static AnaplanViewBuilder<?, ?> updater(String qualifiedName, String name) {
        return AnaplanView._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AnaplanView, from a potentially
     * more-complete AnaplanView object.
     *
     * @return the minimal object necessary to update the AnaplanView, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AnaplanView are not found in the initial object
     */
    @Override
    public AnaplanViewBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class AnaplanViewBuilder<C extends AnaplanView, B extends AnaplanViewBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a AnaplanView.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AnaplanView
     * @param name of the AnaplanView
     * @return the updated AnaplanView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanView removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanView) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AnaplanView.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AnaplanView
     * @param name of the AnaplanView
     * @return the updated AnaplanView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanView removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanView) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AnaplanView.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AnaplanView's owners
     * @param qualifiedName of the AnaplanView
     * @param name of the AnaplanView
     * @return the updated AnaplanView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanView removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanView) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AnaplanView.
     *
     * @param client connectivity to the Atlan tenant on which to update the AnaplanView's certificate
     * @param qualifiedName of the AnaplanView
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AnaplanView, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanView updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AnaplanView)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AnaplanView.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AnaplanView's certificate
     * @param qualifiedName of the AnaplanView
     * @param name of the AnaplanView
     * @return the updated AnaplanView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanView removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanView) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AnaplanView.
     *
     * @param client connectivity to the Atlan tenant on which to update the AnaplanView's announcement
     * @param qualifiedName of the AnaplanView
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanView updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AnaplanView)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AnaplanView.
     *
     * @param client connectivity to the Atlan client from which to remove the AnaplanView's announcement
     * @param qualifiedName of the AnaplanView
     * @param name of the AnaplanView
     * @return the updated AnaplanView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanView removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanView) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AnaplanView.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AnaplanView's assigned terms
     * @param qualifiedName for the AnaplanView
     * @param name human-readable name of the AnaplanView
     * @param terms the list of terms to replace on the AnaplanView, or null to remove all terms from the AnaplanView
     * @return the AnaplanView that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AnaplanView replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AnaplanView) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AnaplanView, without replacing existing terms linked to the AnaplanView.
     * Note: this operation must make two API calls — one to retrieve the AnaplanView's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AnaplanView
     * @param qualifiedName for the AnaplanView
     * @param terms the list of terms to append to the AnaplanView
     * @return the AnaplanView that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AnaplanView appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AnaplanView) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AnaplanView, without replacing all existing terms linked to the AnaplanView.
     * Note: this operation must make two API calls — one to retrieve the AnaplanView's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AnaplanView
     * @param qualifiedName for the AnaplanView
     * @param terms the list of terms to remove from the AnaplanView, which must be referenced by GUID
     * @return the AnaplanView that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AnaplanView removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AnaplanView) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AnaplanView, without replacing existing Atlan tags linked to the AnaplanView.
     * Note: this operation must make two API calls — one to retrieve the AnaplanView's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AnaplanView
     * @param qualifiedName of the AnaplanView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AnaplanView
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AnaplanView appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AnaplanView) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AnaplanView, without replacing existing Atlan tags linked to the AnaplanView.
     * Note: this operation must make two API calls — one to retrieve the AnaplanView's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AnaplanView
     * @param qualifiedName of the AnaplanView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AnaplanView
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AnaplanView appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AnaplanView) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AnaplanView.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AnaplanView
     * @param qualifiedName of the AnaplanView
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AnaplanView
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
