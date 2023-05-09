/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

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
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a QuickSight Folder in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class QuickSightFolder extends QuickSight {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QuickSightFolder";

    /** Fixed typeName for QuickSightFolders. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    QuickSightFolderType quickSightFolderType;

    /** TBC */
    @Attribute
    @Singular("addQuickSightFolderHierarchy")
    List<Map<String, String>> quickSightFolderHierarchy;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<QuickSightDashboard> quickSightDashboards;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<QuickSightAnalysis> quickSightAnalyses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<QuickSightDataset> quickSightDatasets;

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
        Asset asset = Asset.retrieveFull(guid);
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
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
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
        return Asset.restore(TYPE_NAME, qualifiedName);
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
        return (QuickSightFolder) Asset.removeDescription(updater(qualifiedName, name));
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
        return (QuickSightFolder) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (QuickSightFolder) Asset.removeOwners(updater(qualifiedName, name));
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
        return (QuickSightFolder) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return (QuickSightFolder) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (QuickSightFolder) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return (QuickSightFolder) Asset.removeAnnouncement(updater(qualifiedName, name));
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
    public static QuickSightFolder replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightFolder) Asset.replaceTerms(updater(qualifiedName, name), terms);
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
    public static QuickSightFolder appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (QuickSightFolder) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
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
    public static QuickSightFolder removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (QuickSightFolder) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a QuickSightFolder, without replacing existing classifications linked to the QuickSightFolder.
     * Note: this operation must make two API calls — one to retrieve the QuickSightFolder's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightFolder
     */
    public static QuickSightFolder appendClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        return (QuickSightFolder) Asset.appendClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a QuickSightFolder, without replacing existing classifications linked to the QuickSightFolder.
     * Note: this operation must make two API calls — one to retrieve the QuickSightFolder's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightFolder
     */
    public static QuickSightFolder appendClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QuickSightFolder) Asset.appendClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add classifications to a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the QuickSightFolder
     * @deprecated see {@link #appendClassifications(String, List)} instead
     */
    @Deprecated
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the QuickSightFolder
     * @deprecated see {@link #appendClassifications(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
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
     * Remove a classification from a QuickSightFolder.
     *
     * @param qualifiedName of the QuickSightFolder
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the QuickSightFolder
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
