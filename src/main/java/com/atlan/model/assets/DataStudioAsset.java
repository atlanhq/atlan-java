/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.GoogleDataStudioAssetType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.GoogleLabel;
import com.atlan.model.structs.GoogleTag;
import com.atlan.util.QueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Google Data Studio asset in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class DataStudioAsset extends Asset
        implements IDataStudioAsset, IDataStudio, IGoogle, IBI, ICloud, IAsset, IReferenceable, ICatalog {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataStudioAsset";

    /** Fixed typeName for DataStudioAssets. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Owner of the asset within Google Data Studio. */
    @Attribute
    String dataStudioAssetOwner;

    /** Title for the asset. */
    @Attribute
    String dataStudioAssetTitle;

    /** Type of Google Data Studio asset. */
    @Attribute
    GoogleDataStudioAssetType dataStudioAssetType;

    /** TBC */
    @Attribute
    @Singular
    List<GoogleLabel> googleLabels;

    /** TBC */
    @Attribute
    String googleLocation;

    /** TBC */
    @Attribute
    String googleLocationType;

    /** TBC */
    @Attribute
    String googleProjectId;

    /** TBC */
    @Attribute
    String googleProjectName;

    /** TBC */
    @Attribute
    Long googleProjectNumber;

    /** TBC */
    @Attribute
    String googleService;

    /** TBC */
    @Attribute
    @Singular
    List<GoogleTag> googleTags;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** Whether the asset is soft-deleted in Google Data Studio (true) or not (false). */
    @Attribute
    Boolean isTrashedDataStudioAsset;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Start an asset filter that will return all DataStudioAsset assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataStudioAsset assets will be included.
     *
     * @return an asset filter that includes all DataStudioAsset assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all DataStudioAsset assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataStudioAsset assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all DataStudioAsset assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all DataStudioAsset assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DataStudioAssets will be included
     * @return an asset filter that includes all DataStudioAsset assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all DataStudioAsset assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DataStudioAssets will be included
     * @return an asset filter that includes all DataStudioAsset assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a DataStudioAsset by GUID.
     *
     * @param guid the GUID of the DataStudioAsset to reference
     * @return reference to a DataStudioAsset that can be used for defining a relationship to a DataStudioAsset
     */
    public static DataStudioAsset refByGuid(String guid) {
        return DataStudioAsset.builder().guid(guid).build();
    }

    /**
     * Reference to a DataStudioAsset by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DataStudioAsset to reference
     * @return reference to a DataStudioAsset that can be used for defining a relationship to a DataStudioAsset
     */
    public static DataStudioAsset refByQualifiedName(String qualifiedName) {
        return DataStudioAsset.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a DataStudioAsset by its GUID, complete with all of its relationships.
     *
     * @param guid of the DataStudioAsset to retrieve
     * @return the requested full DataStudioAsset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataStudioAsset does not exist or the provided GUID is not a DataStudioAsset
     */
    public static DataStudioAsset retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a DataStudioAsset by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the DataStudioAsset to retrieve
     * @return the requested full DataStudioAsset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataStudioAsset does not exist or the provided GUID is not a DataStudioAsset
     */
    public static DataStudioAsset retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof DataStudioAsset) {
            return (DataStudioAsset) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "DataStudioAsset");
        }
    }

    /**
     * Retrieves a DataStudioAsset by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DataStudioAsset to retrieve
     * @return the requested full DataStudioAsset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataStudioAsset does not exist
     */
    public static DataStudioAsset retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a DataStudioAsset by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the DataStudioAsset to retrieve
     * @return the requested full DataStudioAsset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataStudioAsset does not exist
     */
    public static DataStudioAsset retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof DataStudioAsset) {
            return (DataStudioAsset) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "DataStudioAsset");
        }
    }

    /**
     * Restore the archived (soft-deleted) DataStudioAsset to active.
     *
     * @param qualifiedName for the DataStudioAsset
     * @return true if the DataStudioAsset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
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
        return DataStudioAsset.builder()
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.DATASTUDIO)
                .dataStudioAssetType(assetType);
    }

    /**
     * Builds the minimal object necessary to update a DataStudioAsset.
     *
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the minimal request necessary to update the DataStudioAsset, as a builder
     */
    public static DataStudioAssetBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataStudioAsset.builder().qualifiedName(qualifiedName).name(name);
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DataStudioAsset", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DataStudioAsset.
     *
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the updated DataStudioAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

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
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the updated DataStudioAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
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
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the updated DataStudioAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
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
     * @param qualifiedName of the DataStudioAsset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DataStudioAsset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
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
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DataStudioAsset.
     *
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the updated DataStudioAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
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
     * @param qualifiedName of the DataStudioAsset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
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
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DataStudioAsset.
     *
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the updated DataStudioAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
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
     * @param qualifiedName for the DataStudioAsset
     * @param name human-readable name of the DataStudioAsset
     * @param terms the list of terms to replace on the DataStudioAsset, or null to remove all terms from the DataStudioAsset
     * @return the DataStudioAsset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
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
     * @param qualifiedName for the DataStudioAsset
     * @param terms the list of terms to append to the DataStudioAsset
     * @return the DataStudioAsset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
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
     */
    public static DataStudioAsset appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DataStudioAsset) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DataStudioAsset, without replacing all existing terms linked to the DataStudioAsset.
     * Note: this operation must make two API calls — one to retrieve the DataStudioAsset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DataStudioAsset
     * @param terms the list of terms to remove from the DataStudioAsset, which must be referenced by GUID
     * @return the DataStudioAsset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
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
     */
    public static DataStudioAsset removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DataStudioAsset) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DataStudioAsset, without replacing existing Atlan tags linked to the DataStudioAsset.
     * Note: this operation must make two API calls — one to retrieve the DataStudioAsset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DataStudioAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DataStudioAsset
     */
    public static DataStudioAsset appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
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
     */
    public static DataStudioAsset appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DataStudioAsset) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataStudioAsset, without replacing existing Atlan tags linked to the DataStudioAsset.
     * Note: this operation must make two API calls — one to retrieve the DataStudioAsset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DataStudioAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DataStudioAsset
     */
    public static DataStudioAsset appendAtlanTags(
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
     */
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
     * Add Atlan tags to a DataStudioAsset.
     *
     * @param qualifiedName of the DataStudioAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataStudioAsset
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataStudioAsset.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DataStudioAsset
     * @param qualifiedName of the DataStudioAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataStudioAsset
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataStudioAsset.
     *
     * @param qualifiedName of the DataStudioAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataStudioAsset
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DataStudioAsset.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DataStudioAsset
     * @param qualifiedName of the DataStudioAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataStudioAsset
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
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
     * @param qualifiedName of the DataStudioAsset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataStudioAsset
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DataStudioAsset.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DataStudioAsset
     * @param qualifiedName of the DataStudioAsset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataStudioAsset
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
