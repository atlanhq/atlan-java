/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.GoogleDataStudioAssetType;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
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
@Slf4j
public class DataStudioAsset extends Google {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataStudioAsset";

    /** Fixed typeName for DataStudioAssets. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Type of Google Data Studio asset. */
    @Attribute
    GoogleDataStudioAssetType dataStudioAssetType;

    /** Title for the asset. */
    @Attribute
    String dataStudioAssetTitle;

    /** Owner of the asset within Google Data Studio. */
    @Attribute
    String dataStudioAssetOwner;

    /** Whether the asset is soft-deleted in Google Data Studio (true) or not (false). */
    @Attribute
    Boolean isTrashedDataStudioAsset;

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
        Asset asset = Asset.retrieveFull(guid);
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
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
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
        return Asset.restore(TYPE_NAME, qualifiedName);
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
        return (DataStudioAsset) Asset.removeDescription(updater(qualifiedName, name));
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
        return (DataStudioAsset) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (DataStudioAsset) Asset.removeOwners(updater(qualifiedName, name));
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
        return (DataStudioAsset) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return (DataStudioAsset) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (DataStudioAsset) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return (DataStudioAsset) Asset.removeAnnouncement(updater(qualifiedName, name));
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
    public static DataStudioAsset replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (DataStudioAsset) Asset.replaceTerms(updater(qualifiedName, name), terms);
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
    public static DataStudioAsset appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (DataStudioAsset) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
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
    public static DataStudioAsset removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (DataStudioAsset) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
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
        return (DataStudioAsset) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
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
        return (DataStudioAsset) Asset.appendAtlanTags(
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
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
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
        Asset.addAtlanTags(
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
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
