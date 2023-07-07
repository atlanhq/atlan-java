/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.QuickSightFolderType;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a QuickSight Folder in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class QuickSightFolder extends Asset
        implements IQuickSightFolder, IQuickSight, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QuickSightFolder";

    /** Fixed typeName for QuickSightFolders. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IQuickSightAnalysis> quickSightAnalyses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IQuickSightDashboard> quickSightDashboards;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IQuickSightDataset> quickSightDatasets;

    /** Detailed path of the folder */
    @Attribute
    @Singular("addQuickSightFolderHierarchy")
    List<Map<String, String>> quickSightFolderHierarchy;

    /** Shared or private type of folder */
    @Attribute
    QuickSightFolderType quickSightFolderType;

    /** TBC */
    @Attribute
    String quickSightId;

    /** TBC */
    @Attribute
    String quickSightSheetId;

    /** TBC */
    @Attribute
    String quickSightSheetName;

    /**
     * Reference to a QuickSightFolder by GUID.
     *
     * @param guid the GUID of the QuickSightFolder to reference
     * @return reference to a QuickSightFolder that can be used for defining a relationship to a QuickSightFolder
     */
    public static QuickSightFolder refByGuid(String guid) {
        return QuickSightFolder.builder().guid(guid).build();
    }

    /**
     * Reference to a QuickSightFolder by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QuickSightFolder to reference
     * @return reference to a QuickSightFolder that can be used for defining a relationship to a QuickSightFolder
     */
    public static QuickSightFolder refByQualifiedName(String qualifiedName) {
        return QuickSightFolder.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QuickSightFolder by its GUID, complete with all of its relationships.
     *
     * @param guid of the QuickSightFolder to retrieve
     * @return the requested full QuickSightFolder, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightFolder does not exist or the provided GUID is not a QuickSightFolder
     */
    public static QuickSightFolder retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a QuickSightFolder by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the QuickSightFolder to retrieve
     * @return the requested full QuickSightFolder, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightFolder does not exist or the provided GUID is not a QuickSightFolder
     */
    public static QuickSightFolder retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QuickSightFolder) {
            return (QuickSightFolder) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QuickSightFolder");
        }
    }

    /**
     * Retrieves a QuickSightFolder by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QuickSightFolder to retrieve
     * @return the requested full QuickSightFolder, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightFolder does not exist
     */
    public static QuickSightFolder retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a QuickSightFolder by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the QuickSightFolder to retrieve
     * @return the requested full QuickSightFolder, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightFolder does not exist
     */
    public static QuickSightFolder retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof QuickSightFolder) {
            return (QuickSightFolder) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QuickSightFolder");
        }
    }

    /**
     * Restore the archived (soft-deleted) QuickSightFolder to active.
     *
     * @param qualifiedName for the QuickSightFolder
     * @return true if the QuickSightFolder is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) QuickSightFolder to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the QuickSightFolder
     * @return true if the QuickSightFolder is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param name of the QuickSightFolder
     * @return the minimal request necessary to update the QuickSightFolder, as a builder
     */
    public static QuickSightFolderBuilder<?, ?> updater(String qualifiedName, String name) {
        return QuickSightFolder.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QuickSightFolder, from a potentially
     * more-complete QuickSightFolder object.
     *
     * @return the minimal object necessary to update the QuickSightFolder, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QuickSightFolder are not found in the initial object
     */
    @Override
    public QuickSightFolderBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QuickSightFolder", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param name of the QuickSightFolder
     * @return the updated QuickSightFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a QuickSightFolder.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QuickSightFolder
     * @param name of the QuickSightFolder
     * @return the updated QuickSightFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightFolder) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param name of the QuickSightFolder
     * @return the updated QuickSightFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a QuickSightFolder.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QuickSightFolder
     * @param name of the QuickSightFolder
     * @return the updated QuickSightFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightFolder) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param name of the QuickSightFolder
     * @return the updated QuickSightFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a QuickSightFolder.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QuickSightFolder's owners
     * @param qualifiedName of the QuickSightFolder
     * @param name of the QuickSightFolder
     * @return the updated QuickSightFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightFolder) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightFolder, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a QuickSightFolder.
     *
     * @param client connectivity to the Atlan tenant on which to update the QuickSightFolder's certificate
     * @param qualifiedName of the QuickSightFolder
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightFolder, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (QuickSightFolder)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param name of the QuickSightFolder
     * @return the updated QuickSightFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a QuickSightFolder.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QuickSightFolder's certificate
     * @param qualifiedName of the QuickSightFolder
     * @param name of the QuickSightFolder
     * @return the updated QuickSightFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightFolder) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a QuickSightFolder.
     *
     * @param client connectivity to the Atlan tenant on which to update the QuickSightFolder's announcement
     * @param qualifiedName of the QuickSightFolder
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (QuickSightFolder)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param name of the QuickSightFolder
     * @return the updated QuickSightFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a QuickSightFolder.
     *
     * @param client connectivity to the Atlan client from which to remove the QuickSightFolder's announcement
     * @param qualifiedName of the QuickSightFolder
     * @param name of the QuickSightFolder
     * @return the updated QuickSightFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightFolder) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QuickSightFolder.
     *
     * @param qualifiedName for the QuickSightFolder
     * @param name human-readable name of the QuickSightFolder
     * @param terms the list of terms to replace on the QuickSightFolder, or null to remove all terms from the QuickSightFolder
     * @return the QuickSightFolder that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the QuickSightFolder.
     *
     * @param client connectivity to the Atlan tenant on which to replace the QuickSightFolder's assigned terms
     * @param qualifiedName for the QuickSightFolder
     * @param name human-readable name of the QuickSightFolder
     * @param terms the list of terms to replace on the QuickSightFolder, or null to remove all terms from the QuickSightFolder
     * @return the QuickSightFolder that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (QuickSightFolder) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QuickSightFolder, without replacing existing terms linked to the QuickSightFolder.
     * Note: this operation must make two API calls — one to retrieve the QuickSightFolder's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QuickSightFolder
     * @param terms the list of terms to append to the QuickSightFolder
     * @return the QuickSightFolder that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the QuickSightFolder, without replacing existing terms linked to the QuickSightFolder.
     * Note: this operation must make two API calls — one to retrieve the QuickSightFolder's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the QuickSightFolder
     * @param qualifiedName for the QuickSightFolder
     * @param terms the list of terms to append to the QuickSightFolder
     * @return the QuickSightFolder that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightFolder) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightFolder, without replacing all existing terms linked to the QuickSightFolder.
     * Note: this operation must make two API calls — one to retrieve the QuickSightFolder's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QuickSightFolder
     * @param terms the list of terms to remove from the QuickSightFolder, which must be referenced by GUID
     * @return the QuickSightFolder that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightFolder, without replacing all existing terms linked to the QuickSightFolder.
     * Note: this operation must make two API calls — one to retrieve the QuickSightFolder's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the QuickSightFolder
     * @param qualifiedName for the QuickSightFolder
     * @param terms the list of terms to remove from the QuickSightFolder, which must be referenced by GUID
     * @return the QuickSightFolder that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightFolder removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightFolder) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QuickSightFolder, without replacing existing Atlan tags linked to the QuickSightFolder.
     * Note: this operation must make two API calls — one to retrieve the QuickSightFolder's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightFolder
     */
    public static QuickSightFolder appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightFolder, without replacing existing Atlan tags linked to the QuickSightFolder.
     * Note: this operation must make two API calls — one to retrieve the QuickSightFolder's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QuickSightFolder
     * @param qualifiedName of the QuickSightFolder
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightFolder
     */
    public static QuickSightFolder appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (QuickSightFolder) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightFolder, without replacing existing Atlan tags linked to the QuickSightFolder.
     * Note: this operation must make two API calls — one to retrieve the QuickSightFolder's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightFolder
     */
    public static QuickSightFolder appendAtlanTags(
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
     * Add Atlan tags to a QuickSightFolder, without replacing existing Atlan tags linked to the QuickSightFolder.
     * Note: this operation must make two API calls — one to retrieve the QuickSightFolder's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QuickSightFolder
     * @param qualifiedName of the QuickSightFolder
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightFolder
     */
    public static QuickSightFolder appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QuickSightFolder) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightFolder
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightFolder.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QuickSightFolder
     * @param qualifiedName of the QuickSightFolder
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightFolder
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightFolder
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
     * Add Atlan tags to a QuickSightFolder.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QuickSightFolder
     * @param qualifiedName of the QuickSightFolder
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightFolder
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
     * Remove an Atlan tag from a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QuickSightFolder
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a QuickSightFolder.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a QuickSightFolder
     * @param qualifiedName of the QuickSightFolder
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QuickSightFolder
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
