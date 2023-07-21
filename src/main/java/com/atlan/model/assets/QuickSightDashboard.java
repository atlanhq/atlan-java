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
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.QueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a QuickSight Dashboard in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class QuickSightDashboard extends Asset
        implements IQuickSightDashboard, IQuickSight, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QuickSightDashboard";

    /** Fixed typeName for QuickSightDashboards. */
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
    SortedSet<IQuickSightFolder> quickSightDashboardFolders;

    /** Last published time of dashboard */
    @Attribute
    Long quickSightDashboardLastPublishedTime;

    /** Version number of the dashboard published */
    @Attribute
    Long quickSightDashboardPublishedVersionNumber;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IQuickSightDashboardVisual> quickSightDashboardVisuals;

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
     * Start an asset filter that will return all QuickSightDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QuickSightDashboard assets will be included.
     *
     * @return an asset filter that includes all QuickSightDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(false);
    }

    /**
     * Start an asset filter that will return all QuickSightDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) QuickSightDashboards will be included
     * @return an asset filter that includes all QuickSightDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder = AssetFilter.builder().filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a QuickSightDashboard by GUID.
     *
     * @param guid the GUID of the QuickSightDashboard to reference
     * @return reference to a QuickSightDashboard that can be used for defining a relationship to a QuickSightDashboard
     */
    public static QuickSightDashboard refByGuid(String guid) {
        return QuickSightDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a QuickSightDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QuickSightDashboard to reference
     * @return reference to a QuickSightDashboard that can be used for defining a relationship to a QuickSightDashboard
     */
    public static QuickSightDashboard refByQualifiedName(String qualifiedName) {
        return QuickSightDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QuickSightDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the QuickSightDashboard to retrieve
     * @return the requested full QuickSightDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDashboard does not exist or the provided GUID is not a QuickSightDashboard
     */
    public static QuickSightDashboard retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a QuickSightDashboard by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the QuickSightDashboard to retrieve
     * @return the requested full QuickSightDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDashboard does not exist or the provided GUID is not a QuickSightDashboard
     */
    public static QuickSightDashboard retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QuickSightDashboard) {
            return (QuickSightDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QuickSightDashboard");
        }
    }

    /**
     * Retrieves a QuickSightDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QuickSightDashboard to retrieve
     * @return the requested full QuickSightDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDashboard does not exist
     */
    public static QuickSightDashboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a QuickSightDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the QuickSightDashboard to retrieve
     * @return the requested full QuickSightDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDashboard does not exist
     */
    public static QuickSightDashboard retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof QuickSightDashboard) {
            return (QuickSightDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QuickSightDashboard");
        }
    }

    /**
     * Restore the archived (soft-deleted) QuickSightDashboard to active.
     *
     * @param qualifiedName for the QuickSightDashboard
     * @return true if the QuickSightDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) QuickSightDashboard to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the QuickSightDashboard
     * @return true if the QuickSightDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param name of the QuickSightDashboard
     * @return the minimal request necessary to update the QuickSightDashboard, as a builder
     */
    public static QuickSightDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return QuickSightDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QuickSightDashboard, from a potentially
     * more-complete QuickSightDashboard object.
     *
     * @return the minimal object necessary to update the QuickSightDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QuickSightDashboard are not found in the initial object
     */
    @Override
    public QuickSightDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QuickSightDashboard", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param name of the QuickSightDashboard
     * @return the updated QuickSightDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a QuickSightDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QuickSightDashboard
     * @param name of the QuickSightDashboard
     * @return the updated QuickSightDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDashboard) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param name of the QuickSightDashboard
     * @return the updated QuickSightDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a QuickSightDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QuickSightDashboard
     * @param name of the QuickSightDashboard
     * @return the updated QuickSightDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDashboard) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param name of the QuickSightDashboard
     * @return the updated QuickSightDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a QuickSightDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QuickSightDashboard's owners
     * @param qualifiedName of the QuickSightDashboard
     * @param name of the QuickSightDashboard
     * @return the updated QuickSightDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDashboard) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a QuickSightDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the QuickSightDashboard's certificate
     * @param qualifiedName of the QuickSightDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (QuickSightDashboard)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param name of the QuickSightDashboard
     * @return the updated QuickSightDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a QuickSightDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QuickSightDashboard's certificate
     * @param qualifiedName of the QuickSightDashboard
     * @param name of the QuickSightDashboard
     * @return the updated QuickSightDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDashboard) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a QuickSightDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the QuickSightDashboard's announcement
     * @param qualifiedName of the QuickSightDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (QuickSightDashboard)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param name of the QuickSightDashboard
     * @return the updated QuickSightDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a QuickSightDashboard.
     *
     * @param client connectivity to the Atlan client from which to remove the QuickSightDashboard's announcement
     * @param qualifiedName of the QuickSightDashboard
     * @param name of the QuickSightDashboard
     * @return the updated QuickSightDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDashboard) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QuickSightDashboard.
     *
     * @param qualifiedName for the QuickSightDashboard
     * @param name human-readable name of the QuickSightDashboard
     * @param terms the list of terms to replace on the QuickSightDashboard, or null to remove all terms from the QuickSightDashboard
     * @return the QuickSightDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the QuickSightDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to replace the QuickSightDashboard's assigned terms
     * @param qualifiedName for the QuickSightDashboard
     * @param name human-readable name of the QuickSightDashboard
     * @param terms the list of terms to replace on the QuickSightDashboard, or null to remove all terms from the QuickSightDashboard
     * @return the QuickSightDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (QuickSightDashboard) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QuickSightDashboard, without replacing existing terms linked to the QuickSightDashboard.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QuickSightDashboard
     * @param terms the list of terms to append to the QuickSightDashboard
     * @return the QuickSightDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the QuickSightDashboard, without replacing existing terms linked to the QuickSightDashboard.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the QuickSightDashboard
     * @param qualifiedName for the QuickSightDashboard
     * @param terms the list of terms to append to the QuickSightDashboard
     * @return the QuickSightDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightDashboard) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightDashboard, without replacing all existing terms linked to the QuickSightDashboard.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QuickSightDashboard
     * @param terms the list of terms to remove from the QuickSightDashboard, which must be referenced by GUID
     * @return the QuickSightDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightDashboard, without replacing all existing terms linked to the QuickSightDashboard.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the QuickSightDashboard
     * @param qualifiedName for the QuickSightDashboard
     * @param terms the list of terms to remove from the QuickSightDashboard, which must be referenced by GUID
     * @return the QuickSightDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboard removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightDashboard) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QuickSightDashboard, without replacing existing Atlan tags linked to the QuickSightDashboard.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightDashboard
     */
    public static QuickSightDashboard appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightDashboard, without replacing existing Atlan tags linked to the QuickSightDashboard.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QuickSightDashboard
     * @param qualifiedName of the QuickSightDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightDashboard
     */
    public static QuickSightDashboard appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (QuickSightDashboard) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightDashboard, without replacing existing Atlan tags linked to the QuickSightDashboard.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightDashboard
     */
    public static QuickSightDashboard appendAtlanTags(
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
     * Add Atlan tags to a QuickSightDashboard, without replacing existing Atlan tags linked to the QuickSightDashboard.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QuickSightDashboard
     * @param qualifiedName of the QuickSightDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightDashboard
     */
    public static QuickSightDashboard appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QuickSightDashboard) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QuickSightDashboard
     * @param qualifiedName of the QuickSightDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightDashboard
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
     * Add Atlan tags to a QuickSightDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QuickSightDashboard
     * @param qualifiedName of the QuickSightDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightDashboard
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
     * Remove an Atlan tag from a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QuickSightDashboard
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a QuickSightDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a QuickSightDashboard
     * @param qualifiedName of the QuickSightDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QuickSightDashboard
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
