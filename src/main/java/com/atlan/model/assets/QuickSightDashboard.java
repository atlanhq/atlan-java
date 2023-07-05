/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
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
        Asset asset = Asset.retrieveFull(guid);
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
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
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
        return Asset.restore(TYPE_NAME, qualifiedName);
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
        return (QuickSightDashboard) Asset.removeDescription(updater(qualifiedName, name));
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
        return (QuickSightDashboard) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (QuickSightDashboard) Asset.removeOwners(updater(qualifiedName, name));
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
        return (QuickSightDashboard) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return (QuickSightDashboard) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (QuickSightDashboard)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return (QuickSightDashboard) Asset.removeAnnouncement(updater(qualifiedName, name));
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
        return (QuickSightDashboard) Asset.replaceTerms(updater(qualifiedName, name), terms);
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
        return (QuickSightDashboard) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
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
        return (QuickSightDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
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
        return (QuickSightDashboard) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
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
        return (QuickSightDashboard) Asset.appendAtlanTags(
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
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
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
        Asset.addAtlanTags(
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
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
