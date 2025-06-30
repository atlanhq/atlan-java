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
import com.atlan.model.enums.GoogleDataStudioAssetType;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.GoogleLabel;
import com.atlan.model.structs.GoogleTag;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Google Data Studio asset in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class DataStudioAsset extends Asset
        implements IDataStudioAsset, IDataStudio, IGoogle, IBI, ICloud, IAsset, IReferenceable, ICatalog {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataStudioAsset";

    /** Fixed typeName for DataStudioAssets. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Owner of the asset, from Google Data Studio. */
    @Attribute
    String dataStudioAssetOwner;

    /** Title of the Google Data Studio asset. */
    @Attribute
    String dataStudioAssetTitle;

    /** Type of the Google Data Studio asset, for example: REPORT or DATA_SOURCE. */
    @Attribute
    GoogleDataStudioAssetType dataStudioAssetType;

    /** List of labels that have been applied to the asset in Google. */
    @Attribute
    @Singular
    List<GoogleLabel> googleLabels;

    /** Location of this asset in Google. */
    @Attribute
    String googleLocation;

    /** Type of location of this asset in Google. */
    @Attribute
    String googleLocationType;

    /** ID of the project in which the asset exists. */
    @Attribute
    String googleProjectId;

    /** Name of the project in which the asset exists. */
    @Attribute
    String googleProjectName;

    /** Number of the project in which the asset exists. */
    @Attribute
    Long googleProjectNumber;

    /** Service in Google in which the asset exists. */
    @Attribute
    String googleService;

    /** List of tags that have been applied to the asset in Google. */
    @Attribute
    @Singular
    List<GoogleTag> googleTags;

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

    /** Whether the Google Data Studio asset has been trashed (true) or not (false). */
    @Attribute
    Boolean isTrashedDataStudioAsset;

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
     * Builds the minimal object necessary to create a relationship to a DataStudioAsset, from a potentially
     * more-complete DataStudioAsset object.
     *
     * @return the minimal object necessary to relate to the DataStudioAsset
     * @throws InvalidRequestException if any of the minimal set of required properties for a DataStudioAsset relationship are not found in the initial object
     */
    @Override
    public DataStudioAsset trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DataStudioAsset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataStudioAsset assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DataStudioAsset assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DataStudioAsset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DataStudioAssets will be included
     * @return a fluent search that includes all DataStudioAsset assets
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
     * Reference to a DataStudioAsset by GUID. Use this to create a relationship to this DataStudioAsset,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DataStudioAsset to reference
     * @return reference to a DataStudioAsset that can be used for defining a relationship to a DataStudioAsset
     */
    public static DataStudioAsset refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataStudioAsset by GUID. Use this to create a relationship to this DataStudioAsset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DataStudioAsset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataStudioAsset that can be used for defining a relationship to a DataStudioAsset
     */
    public static DataStudioAsset refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DataStudioAsset._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DataStudioAsset by qualifiedName. Use this to create a relationship to this DataStudioAsset,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DataStudioAsset to reference
     * @return reference to a DataStudioAsset that can be used for defining a relationship to a DataStudioAsset
     */
    public static DataStudioAsset refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataStudioAsset by qualifiedName. Use this to create a relationship to this DataStudioAsset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DataStudioAsset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataStudioAsset that can be used for defining a relationship to a DataStudioAsset
     */
    public static DataStudioAsset refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DataStudioAsset._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DataStudioAsset by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataStudioAsset to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DataStudioAsset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataStudioAsset does not exist or the provided GUID is not a DataStudioAsset
     */
    @JsonIgnore
    public static DataStudioAsset get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DataStudioAsset by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataStudioAsset to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DataStudioAsset, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataStudioAsset does not exist or the provided GUID is not a DataStudioAsset
     */
    @JsonIgnore
    public static DataStudioAsset get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DataStudioAsset) {
                return (DataStudioAsset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DataStudioAsset) {
                return (DataStudioAsset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DataStudioAsset by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataStudioAsset to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DataStudioAsset, including any relationships
     * @return the requested DataStudioAsset, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataStudioAsset does not exist or the provided GUID is not a DataStudioAsset
     */
    @JsonIgnore
    public static DataStudioAsset get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DataStudioAsset by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataStudioAsset to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DataStudioAsset, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DataStudioAsset
     * @return the requested DataStudioAsset, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataStudioAsset does not exist or the provided GUID is not a DataStudioAsset
     */
    @JsonIgnore
    public static DataStudioAsset get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DataStudioAsset.select(client)
                    .where(DataStudioAsset.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DataStudioAsset) {
                return (DataStudioAsset) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DataStudioAsset.select(client)
                    .where(DataStudioAsset.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DataStudioAsset) {
                return (DataStudioAsset) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DataStudioAsset to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DataStudioAsset
     * @return true if the DataStudioAsset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Google Data Studio asset.
     *
     * @param name of the asset
     * @param connectionQualifiedName unique name of the connection through which the asset is accessible
     * @param assetType type of the asset
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static DataStudioAssetBuilder<?, ?> creator(
            String name, String connectionQualifiedName, GoogleDataStudioAssetType assetType) {
        return DataStudioAsset.creator(
                name, connectionQualifiedName, assetType, UUID.randomUUID().toString());
    }

    /**
     * Builds the minimal object necessary to create a Google Data Studio asset.
     *
     * @param name of the asset
     * @param connectionQualifiedName unique name of the connection through which the asset is accessible
     * @param assetType type of the asset
     * @param gdsId unique identifier of the asset within Google Data Studio
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static DataStudioAssetBuilder<?, ?> creator(
            String name, String connectionQualifiedName, GoogleDataStudioAssetType assetType, String gdsId) {
        return DataStudioAsset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(gdsId, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .dataStudioAssetType(assetType);
    }

    /**
     * Generate a unique DataStudioAsset name.
     *
     * @param gdsId unique identifier of the asset within Google Data Studio
     * @param connectionQualifiedName unique name of the connection through which the DataStudioAsset is accessible
     * @return a unique name for the DataStudioAsset
     */
    public static String generateQualifiedName(String gdsId, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + gdsId;
    }

    /**
     * Builds the minimal object necessary to update a DataStudioAsset.
     *
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the minimal request necessary to update the DataStudioAsset, as a builder
     */
    public static DataStudioAssetBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataStudioAsset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataStudioAsset, from a potentially
     * more-complete DataStudioAsset object.
     *
     * @return the minimal object necessary to update the DataStudioAsset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataStudioAsset are not found in the initial object
     */
    @Override
    public DataStudioAssetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class DataStudioAssetBuilder<
                    C extends DataStudioAsset, B extends DataStudioAssetBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a DataStudioAsset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the updated DataStudioAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataStudioAsset) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DataStudioAsset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the updated DataStudioAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataStudioAsset) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DataStudioAsset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataStudioAsset's owners
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the updated DataStudioAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataStudioAsset) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DataStudioAsset.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataStudioAsset's certificate
     * @param qualifiedName of the DataStudioAsset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DataStudioAsset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DataStudioAsset)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DataStudioAsset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataStudioAsset's certificate
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the updated DataStudioAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataStudioAsset) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DataStudioAsset.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataStudioAsset's announcement
     * @param qualifiedName of the DataStudioAsset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DataStudioAsset)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DataStudioAsset.
     *
     * @param client connectivity to the Atlan client from which to remove the DataStudioAsset's announcement
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the updated DataStudioAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataStudioAsset) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DataStudioAsset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DataStudioAsset's assigned terms
     * @param qualifiedName for the DataStudioAsset
     * @param name human-readable name of the DataStudioAsset
     * @param terms the list of terms to replace on the DataStudioAsset, or null to remove all terms from the DataStudioAsset
     * @return the DataStudioAsset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DataStudioAsset) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DataStudioAsset, without replacing existing terms linked to the DataStudioAsset.
     * Note: this operation must make two API calls — one to retrieve the DataStudioAsset's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DataStudioAsset
     * @param qualifiedName for the DataStudioAsset
     * @param terms the list of terms to append to the DataStudioAsset
     * @return the DataStudioAsset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DataStudioAsset appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DataStudioAsset) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DataStudioAsset, without replacing all existing terms linked to the DataStudioAsset.
     * Note: this operation must make two API calls — one to retrieve the DataStudioAsset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DataStudioAsset
     * @param qualifiedName for the DataStudioAsset
     * @param terms the list of terms to remove from the DataStudioAsset, which must be referenced by GUID
     * @return the DataStudioAsset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DataStudioAsset removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DataStudioAsset) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DataStudioAsset, without replacing existing Atlan tags linked to the DataStudioAsset.
     * Note: this operation must make two API calls — one to retrieve the DataStudioAsset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataStudioAsset
     * @param qualifiedName of the DataStudioAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DataStudioAsset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DataStudioAsset appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DataStudioAsset) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataStudioAsset, without replacing existing Atlan tags linked to the DataStudioAsset.
     * Note: this operation must make two API calls — one to retrieve the DataStudioAsset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataStudioAsset
     * @param qualifiedName of the DataStudioAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DataStudioAsset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DataStudioAsset appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DataStudioAsset) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DataStudioAsset.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DataStudioAsset
     * @param qualifiedName of the DataStudioAsset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataStudioAsset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
