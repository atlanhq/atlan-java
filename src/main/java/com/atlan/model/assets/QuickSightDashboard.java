/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * TBC
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class QuickSightDashboard extends QuickSight {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QuickSightDashboard";

    /** Fixed typeName for QuickSightDashboards. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    Long quickSightDashboardPublishedVersionNumber;

    /** TBC */
    @Attribute
    Long quickSightDashboardLastPublishedTime;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<QuickSightFolder> quickSightDashboardFolders;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<QuickSightDashboardVisual> quickSightDashboardVisuals;

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
     * Add classifications to a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the QuickSightDashboard
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the QuickSightDashboard
     */
    public static void addClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove a classification from a QuickSightDashboard.
     *
     * @param qualifiedName of the QuickSightDashboard
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the QuickSightDashboard
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
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
    public static QuickSightDashboard replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
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
    public static QuickSightDashboard appendTerms(String qualifiedName, List<GlossaryTerm> terms)
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
    public static QuickSightDashboard removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
