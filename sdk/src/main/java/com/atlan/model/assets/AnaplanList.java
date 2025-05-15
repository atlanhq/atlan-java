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
 * Instances of an AnaplanList in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class AnaplanList extends Asset implements IAnaplanList, IAnaplan, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AnaplanList";

    /** Fixed typeName for AnaplanLists. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** AnaplanLineItem assets containing this AnaplanList. */
    @Attribute
    @Singular
    SortedSet<IAnaplanLineItem> anaplanLineItems;

    /** Item Count of the AnaplanList from the source system. */
    @Attribute
    Long anaplanListItemCount;

    /** AnaplanModel asset containing this AnaplanList. */
    @Attribute
    IAnaplanModel anaplanModel;

    /** Simple name of the AnaplanModel asset that contains this asset(AnaplanModule and everthing under it's hierarchy). */
    @Attribute
    String anaplanModelName;

    /** Unique name of the AnaplanModel asset that contains this asset(AnaplanModule and everthing under it's hierarchy). */
    @Attribute
    String anaplanModelQualifiedName;

    /** Simple name of the AnaplanModule asset that contains this asset(AnaplanLineItem, AnaplanList, AnaplanView and everthing under their hierarchy). */
    @Attribute
    String anaplanModuleName;

    /** Unique name of the AnaplanModule asset that contains this asset(AnaplanLineItem, AnaplanList, AnaplanView and everthing under their hierarchy). */
    @Attribute
    String anaplanModuleQualifiedName;

    /** Id/Guid of the Anaplan asset in the source system. */
    @Attribute
    String anaplanSourceId;

    /** Simple name of the AnaplanWorkspace asset that contains this asset(AnaplanModel and everthing under it's hierarchy). */
    @Attribute
    String anaplanWorkspaceName;

    /** Unique name of the AnaplanWorkspace asset that contains this asset(AnaplanModel and everthing under it's hierarchy). */
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
     * Builds the minimal object necessary to create a relationship to a AnaplanList, from a potentially
     * more-complete AnaplanList object.
     *
     * @return the minimal object necessary to relate to the AnaplanList
     * @throws InvalidRequestException if any of the minimal set of required properties for a AnaplanList relationship are not found in the initial object
     */
    @Override
    public AnaplanList trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AnaplanList assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AnaplanList assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AnaplanList assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AnaplanList assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AnaplanLists will be included
     * @return a fluent search that includes all AnaplanList assets
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
     * Reference to a AnaplanList by GUID. Use this to create a relationship to this AnaplanList,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AnaplanList to reference
     * @return reference to a AnaplanList that can be used for defining a relationship to a AnaplanList
     */
    public static AnaplanList refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AnaplanList by GUID. Use this to create a relationship to this AnaplanList,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AnaplanList to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AnaplanList that can be used for defining a relationship to a AnaplanList
     */
    public static AnaplanList refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AnaplanList._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AnaplanList by qualifiedName. Use this to create a relationship to this AnaplanList,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AnaplanList to reference
     * @return reference to a AnaplanList that can be used for defining a relationship to a AnaplanList
     */
    public static AnaplanList refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AnaplanList by qualifiedName. Use this to create a relationship to this AnaplanList,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AnaplanList to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AnaplanList that can be used for defining a relationship to a AnaplanList
     */
    public static AnaplanList refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AnaplanList._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AnaplanList by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanList to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AnaplanList, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanList does not exist or the provided GUID is not a AnaplanList
     */
    @JsonIgnore
    public static AnaplanList get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AnaplanList by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanList to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AnaplanList, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanList does not exist or the provided GUID is not a AnaplanList
     */
    @JsonIgnore
    public static AnaplanList get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AnaplanList) {
                return (AnaplanList) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AnaplanList) {
                return (AnaplanList) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AnaplanList by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanList to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AnaplanList, including any relationships
     * @return the requested AnaplanList, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanList does not exist or the provided GUID is not a AnaplanList
     */
    @JsonIgnore
    public static AnaplanList get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AnaplanList by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AnaplanList to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AnaplanList, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AnaplanList
     * @return the requested AnaplanList, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AnaplanList does not exist or the provided GUID is not a AnaplanList
     */
    @JsonIgnore
    public static AnaplanList get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AnaplanList.select(client)
                    .where(AnaplanList.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AnaplanList) {
                return (AnaplanList) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AnaplanList.select(client)
                    .where(AnaplanList.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AnaplanList) {
                return (AnaplanList) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AnaplanList to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AnaplanList
     * @return true if the AnaplanList is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Anaplan list.
     *
     * @param name of the list
     * @param model in which the list should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the list, as a builder
     * @throws InvalidRequestException if the model provided is without a qualifiedName
     */
    public static AnaplanList.AnaplanListBuilder<?, ?> creator(String name, AnaplanModel model)
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
     * Builds the minimal object necessary to create a Anaplan list.
     *
     * @param name of the list
     * @param modelQualifiedName unique name of the model in which this list exists
     * @return the minimal request necessary to create the list, as a builder
     */
    public static AnaplanList.AnaplanListBuilder<?, ?> creator(String name, String modelQualifiedName) {
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(modelQualifiedName);
        String workspaceName = StringUtils.getNameFromQualifiedName(workspaceQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        String modelName = StringUtils.getNameFromQualifiedName(modelQualifiedName);
        return creator(
                name, connectionQualifiedName, modelName, modelQualifiedName, workspaceName, workspaceQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Anaplan list.
     *
     * @param name of the list
     * @param connectionQualifiedName unique name of the connection in which to create the list
     * @param modelName name of the model in which to create the list
     * @param modelQualifiedName unique name of the model in which to create the list
     * @param workspaceName name of the workspace in which to create the list
     * @param workspaceQualifiedName unique name of the workspace in which to create the list
     * @return the minimal request necessary to create the list, as a builder
     */
    public static AnaplanList.AnaplanListBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String modelName,
            String modelQualifiedName,
            String workspaceName,
            String workspaceQualifiedName) {
        return AnaplanList._internal()
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
     * Generate a unique list name.
     *
     * @param name of the list
     * @param modelQualifiedName unique name of the model in which this list exists
     * @return a unique name for the list
     */
    public static String generateQualifiedName(String name, String modelQualifiedName) {
        return modelQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a AnaplanList.
     *
     * @param qualifiedName of the AnaplanList
     * @param name of the AnaplanList
     * @return the minimal request necessary to update the AnaplanList, as a builder
     */
    public static AnaplanListBuilder<?, ?> updater(String qualifiedName, String name) {
        return AnaplanList._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AnaplanList, from a potentially
     * more-complete AnaplanList object.
     *
     * @return the minimal object necessary to update the AnaplanList, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AnaplanList are not found in the initial object
     */
    @Override
    public AnaplanListBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class AnaplanListBuilder<C extends AnaplanList, B extends AnaplanListBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a AnaplanList.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AnaplanList
     * @param name of the AnaplanList
     * @return the updated AnaplanList, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanList removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanList) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AnaplanList.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AnaplanList
     * @param name of the AnaplanList
     * @return the updated AnaplanList, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanList removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanList) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AnaplanList.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AnaplanList's owners
     * @param qualifiedName of the AnaplanList
     * @param name of the AnaplanList
     * @return the updated AnaplanList, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanList removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanList) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AnaplanList.
     *
     * @param client connectivity to the Atlan tenant on which to update the AnaplanList's certificate
     * @param qualifiedName of the AnaplanList
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AnaplanList, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanList updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AnaplanList)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AnaplanList.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AnaplanList's certificate
     * @param qualifiedName of the AnaplanList
     * @param name of the AnaplanList
     * @return the updated AnaplanList, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanList removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanList) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AnaplanList.
     *
     * @param client connectivity to the Atlan tenant on which to update the AnaplanList's announcement
     * @param qualifiedName of the AnaplanList
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanList updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AnaplanList)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AnaplanList.
     *
     * @param client connectivity to the Atlan client from which to remove the AnaplanList's announcement
     * @param qualifiedName of the AnaplanList
     * @param name of the AnaplanList
     * @return the updated AnaplanList, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AnaplanList removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AnaplanList) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AnaplanList.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AnaplanList's assigned terms
     * @param qualifiedName for the AnaplanList
     * @param name human-readable name of the AnaplanList
     * @param terms the list of terms to replace on the AnaplanList, or null to remove all terms from the AnaplanList
     * @return the AnaplanList that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AnaplanList replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AnaplanList) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AnaplanList, without replacing existing terms linked to the AnaplanList.
     * Note: this operation must make two API calls — one to retrieve the AnaplanList's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AnaplanList
     * @param qualifiedName for the AnaplanList
     * @param terms the list of terms to append to the AnaplanList
     * @return the AnaplanList that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AnaplanList appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AnaplanList) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AnaplanList, without replacing all existing terms linked to the AnaplanList.
     * Note: this operation must make two API calls — one to retrieve the AnaplanList's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AnaplanList
     * @param qualifiedName for the AnaplanList
     * @param terms the list of terms to remove from the AnaplanList, which must be referenced by GUID
     * @return the AnaplanList that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AnaplanList removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AnaplanList) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AnaplanList, without replacing existing Atlan tags linked to the AnaplanList.
     * Note: this operation must make two API calls — one to retrieve the AnaplanList's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AnaplanList
     * @param qualifiedName of the AnaplanList
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AnaplanList
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AnaplanList appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AnaplanList) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AnaplanList, without replacing existing Atlan tags linked to the AnaplanList.
     * Note: this operation must make two API calls — one to retrieve the AnaplanList's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AnaplanList
     * @param qualifiedName of the AnaplanList
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AnaplanList
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AnaplanList appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AnaplanList) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AnaplanList.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AnaplanList
     * @param qualifiedName of the AnaplanList
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AnaplanList
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
