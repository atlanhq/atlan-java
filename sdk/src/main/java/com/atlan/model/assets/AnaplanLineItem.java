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
 * Instances of an AnaplanLineItem in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class AnaplanLineItem extends Asset
        implements IAnaplanLineItem, IAnaplan, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AnaplanLineItem";

    /** Fixed typeName for AnaplanLineItems. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Dimensions related to the line item. */
    @Attribute
    @Singular
    SortedSet<IAnaplanDimension> anaplanDimensions;

    /** Formula of the AnaplanLineItem from the source system. */
    @Attribute
    String anaplanLineItemFormula;

    /** Lists related to the line item. */
    @Attribute
    @Singular
    SortedSet<IAnaplanList> anaplanLists;

    /** Simple name of the AnaplanModel asset that contains this asset(AnaplanModule and everthing under its hierarchy). */
    @Attribute
    String anaplanModelName;

    /** Unique name of the AnaplanModel asset that contains this asset(AnaplanModule and everthing under its hierarchy). */
    @Attribute
    String anaplanModelQualifiedName;

    /** Module containing the line item. */
    @Attribute
    IAnaplanModule anaplanModule;

    /** Simple name of the AnaplanModule asset that contains this asset(AnaplanLineItem, AnaplanList, AnaplanView and everthing under their hierarchy). */
    @Attribute
    String anaplanModuleName;

    /** Unique name of the AnaplanModule asset that contains this asset(AnaplanLineItem, AnaplanList, AnaplanView and everthing under their hierarchy). */
    @Attribute
    String anaplanModuleQualifiedName;

    /** Id/Guid of the Anaplan asset in the source system. */
    @Attribute
    String anaplanSourceId;

    /** Simple name of the AnaplanWorkspace asset that contains this asset(AnaplanModel and everthing under its hierarchy). */
    @Attribute
    String anaplanWorkspaceName;

    /** Unique name of the AnaplanWorkspace asset that contains this asset(AnaplanModel and everthing under its hierarchy). */
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
     * Builds the minimal object necessary to create a relationship to a AnaplanLineItem, from a potentially
     * more-complete AnaplanLineItem object.
     *
     * @return the minimal object necessary to relate to the AnaplanLineItem
     * @throws InvalidRequestException if any of the minimal set of required properties for a AnaplanLineItem relationship are not found in the initial object
     */
    @Override
    public AnaplanLineItem trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AnaplanLineItem assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AnaplanLineItem assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AnaplanLineItem assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AnaplanLineItem assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AnaplanLineItems will be included
     * @return a fluent search that includes all AnaplanLineItem assets
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
     * Reference to a AnaplanLineItem by GUID. Use this to create a relationship to this AnaplanLineItem,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AnaplanLineItem to reference
     * @return reference to a AnaplanLineItem that can be used for defining a relationship to a AnaplanLineItem
     */
    public static AnaplanLineItem refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AnaplanLineItem by GUID. Use this to create a relationship to this AnaplanLineItem,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AnaplanLineItem to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AnaplanLineItem that can be used for defining a relationship to a AnaplanLineItem
     */
    public static AnaplanLineItem refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AnaplanLineItem._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AnaplanLineItem by qualifiedName. Use this to create a relationship to this AnaplanLineItem,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AnaplanLineItem to reference
     * @return reference to a AnaplanLineItem that can be used for defining a relationship to a AnaplanLineItem
     */
    public static AnaplanLineItem refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AnaplanLineItem by qualifiedName. Use this to create a relationship to this AnaplanLineItem,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AnaplanLineItem to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AnaplanLineItem that can be used for defining a relationship to a AnaplanLineItem
     */
    public static AnaplanLineItem refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AnaplanLineItem._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AnaplanLineItem by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanLineItem to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AnaplanLineItem, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanLineItem does not exist or the provided GUID is not a AnaplanLineItem
     */
    @JsonIgnore
    public static AnaplanLineItem get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AnaplanLineItem by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanLineItem to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AnaplanLineItem, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanLineItem does not exist or the provided GUID is not a AnaplanLineItem
     */
    @JsonIgnore
    public static AnaplanLineItem get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AnaplanLineItem) {
                return (AnaplanLineItem) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AnaplanLineItem) {
                return (AnaplanLineItem) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AnaplanLineItem by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanLineItem to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AnaplanLineItem, including any relationships
     * @return the requested AnaplanLineItem, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanLineItem does not exist or the provided GUID is not a AnaplanLineItem
     */
    @JsonIgnore
    public static AnaplanLineItem get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AnaplanLineItem by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanLineItem to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AnaplanLineItem, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AnaplanLineItem
     * @return the requested AnaplanLineItem, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanLineItem does not exist or the provided GUID is not a AnaplanLineItem
     */
    @JsonIgnore
    public static AnaplanLineItem get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AnaplanLineItem.select(client)
                    .where(AnaplanLineItem.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AnaplanLineItem) {
                return (AnaplanLineItem) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AnaplanLineItem.select(client)
                    .where(AnaplanLineItem.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AnaplanLineItem) {
                return (AnaplanLineItem) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AnaplanLineItem to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AnaplanLineItem
     * @return true if the AnaplanLineItem is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Anaplan lineitem.
     *
     * @param name of the lineitem
     * @param module in which the lineitem should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the lineitem, as a builder
     * @throws InvalidRequestException if the module provided is without a qualifiedName
     */
    public static AnaplanLineItem.AnaplanLineItemBuilder<?, ?> creator(String name, AnaplanModule module)
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
     * Builds the minimal object necessary to create a Anaplan lineitem.
     *
     * @param name of the lineitem
     * @param moduleQualifiedName unique name of the module in which this lineitem exists
     * @return the minimal request necessary to create the lineitem, as a builder
     */
    public static AnaplanLineItem.AnaplanLineItemBuilder<?, ?> creator(String name, String moduleQualifiedName) {
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
     * Builds the minimal object necessary to create a Anaplan lineitem.
     *
     * @param name of the lineitem
     * @param connectionQualifiedName unique name of the connection in which to create the lineitem
     * @param moduleName name of the module in which to creat the lineitem
     * @param moduleQualifiedName unique name of the module in which to create the lineitem
     * @param modelName name of the model in which to create the lineitem
     * @param modelQualifiedName unique name of the model in which to create the lineitem
     * @param workspaceName name of the workspace in which to create the lineitem
     * @param workspaceQualifiedName unique name of the workspace in which to create the lineitem
     * @return the minimal request necessary to create the lineitem, as a builder
     */
    public static AnaplanLineItem.AnaplanLineItemBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String moduleName,
            String moduleQualifiedName,
            String modelName,
            String modelQualifiedName,
            String workspaceName,
            String workspaceQualifiedName) {
        return AnaplanLineItem._internal()
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
     * Generate a unique lineitem name.
     *
     * @param name of the lineitem
     * @param moduleQualifiedName unique name of the module in which this lineitem exists
     * @return a unique name for the lineitem
     */
    public static String generateQualifiedName(String name, String moduleQualifiedName) {
        return moduleQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a AnaplanLineItem.
     *
     * @param qualifiedName of the AnaplanLineItem
     * @param name of the AnaplanLineItem
     * @return the minimal request necessary to update the AnaplanLineItem, as a builder
     */
    public static AnaplanLineItemBuilder<?, ?> updater(String qualifiedName, String name) {
        return AnaplanLineItem._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AnaplanLineItem, from a potentially
     * more-complete AnaplanLineItem object.
     *
     * @return the minimal object necessary to update the AnaplanLineItem, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AnaplanLineItem are not found in the initial object
     */
    @Override
    public AnaplanLineItemBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class AnaplanLineItemBuilder<
                    C extends AnaplanLineItem, B extends AnaplanLineItemBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a AnaplanLineItem.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AnaplanLineItem
     * @param name of the AnaplanLineItem
     * @return the updated AnaplanLineItem, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanLineItem removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanLineItem) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AnaplanLineItem.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AnaplanLineItem
     * @param name of the AnaplanLineItem
     * @return the updated AnaplanLineItem, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanLineItem removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanLineItem) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AnaplanLineItem.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AnaplanLineItem's owners
     * @param qualifiedName of the AnaplanLineItem
     * @param name of the AnaplanLineItem
     * @return the updated AnaplanLineItem, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanLineItem removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanLineItem) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AnaplanLineItem.
     *
     * @param client connectivity to the Atlan tenant on which to update the AnaplanLineItem's certificate
     * @param qualifiedName of the AnaplanLineItem
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AnaplanLineItem, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanLineItem updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AnaplanLineItem)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AnaplanLineItem.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AnaplanLineItem's certificate
     * @param qualifiedName of the AnaplanLineItem
     * @param name of the AnaplanLineItem
     * @return the updated AnaplanLineItem, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanLineItem removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanLineItem) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AnaplanLineItem.
     *
     * @param client connectivity to the Atlan tenant on which to update the AnaplanLineItem's announcement
     * @param qualifiedName of the AnaplanLineItem
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanLineItem updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AnaplanLineItem)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AnaplanLineItem.
     *
     * @param client connectivity to the Atlan client from which to remove the AnaplanLineItem's announcement
     * @param qualifiedName of the AnaplanLineItem
     * @param name of the AnaplanLineItem
     * @return the updated AnaplanLineItem, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanLineItem removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanLineItem) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AnaplanLineItem.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AnaplanLineItem's assigned terms
     * @param qualifiedName for the AnaplanLineItem
     * @param name human-readable name of the AnaplanLineItem
     * @param terms the list of terms to replace on the AnaplanLineItem, or null to remove all terms from the AnaplanLineItem
     * @return the AnaplanLineItem that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AnaplanLineItem replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AnaplanLineItem) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AnaplanLineItem, without replacing existing terms linked to the AnaplanLineItem.
     * Note: this operation must make two API calls — one to retrieve the AnaplanLineItem's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AnaplanLineItem
     * @param qualifiedName for the AnaplanLineItem
     * @param terms the list of terms to append to the AnaplanLineItem
     * @return the AnaplanLineItem that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AnaplanLineItem appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AnaplanLineItem) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AnaplanLineItem, without replacing all existing terms linked to the AnaplanLineItem.
     * Note: this operation must make two API calls — one to retrieve the AnaplanLineItem's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AnaplanLineItem
     * @param qualifiedName for the AnaplanLineItem
     * @param terms the list of terms to remove from the AnaplanLineItem, which must be referenced by GUID
     * @return the AnaplanLineItem that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AnaplanLineItem removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AnaplanLineItem) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AnaplanLineItem, without replacing existing Atlan tags linked to the AnaplanLineItem.
     * Note: this operation must make two API calls — one to retrieve the AnaplanLineItem's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AnaplanLineItem
     * @param qualifiedName of the AnaplanLineItem
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AnaplanLineItem
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AnaplanLineItem appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AnaplanLineItem) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AnaplanLineItem, without replacing existing Atlan tags linked to the AnaplanLineItem.
     * Note: this operation must make two API calls — one to retrieve the AnaplanLineItem's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AnaplanLineItem
     * @param qualifiedName of the AnaplanLineItem
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AnaplanLineItem
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AnaplanLineItem appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AnaplanLineItem) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AnaplanLineItem.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AnaplanLineItem
     * @param qualifiedName of the AnaplanLineItem
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AnaplanLineItem
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
