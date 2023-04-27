/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.QuickSightDatasetImportMode;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * TBC
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class QuickSightDataset extends QuickSight {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QuickSightDataset";

    /** Fixed typeName for QuickSightDatasets. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    QuickSightDatasetImportMode quickSightDatasetImportMode;

    /** TBC */
    @Attribute
    Long quickSightDatasetColumnCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<QuickSightFolder> quickSightDatasetFolders;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<QuickSightDatasetField> quickSightDatasetFields;

    /**
     * Reference to a QuickSightDataset by GUID.
     *
     * @param guid the GUID of the QuickSightDataset to reference
     * @return reference to a QuickSightDataset that can be used for defining a relationship to a QuickSightDataset
     */
    public static QuickSightDataset refByGuid(String guid) {
        return QuickSightDataset.builder().guid(guid).build();
    }

    /**
     * Reference to a QuickSightDataset by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QuickSightDataset to reference
     * @return reference to a QuickSightDataset that can be used for defining a relationship to a QuickSightDataset
     */
    public static QuickSightDataset refByQualifiedName(String qualifiedName) {
        return QuickSightDataset.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QuickSightDataset by its GUID, complete with all of its relationships.
     *
     * @param guid of the QuickSightDataset to retrieve
     * @return the requested full QuickSightDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDataset does not exist or the provided GUID is not a QuickSightDataset
     */
    public static QuickSightDataset retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QuickSightDataset) {
            return (QuickSightDataset) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QuickSightDataset");
        }
    }

    /**
     * Retrieves a QuickSightDataset by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QuickSightDataset to retrieve
     * @return the requested full QuickSightDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDataset does not exist
     */
    public static QuickSightDataset retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof QuickSightDataset) {
            return (QuickSightDataset) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QuickSightDataset");
        }
    }

    /**
     * Restore the archived (soft-deleted) QuickSightDataset to active.
     *
     * @param qualifiedName for the QuickSightDataset
     * @return true if the QuickSightDataset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QuickSightDataset.
     *
     * @param qualifiedName of the QuickSightDataset
     * @param name of the QuickSightDataset
     * @return the minimal request necessary to update the QuickSightDataset, as a builder
     */
    public static QuickSightDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return QuickSightDataset.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QuickSightDataset, from a potentially
     * more-complete QuickSightDataset object.
     *
     * @return the minimal object necessary to update the QuickSightDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QuickSightDataset are not found in the initial object
     */
    @Override
    public QuickSightDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QuickSightDataset", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QuickSightDataset.
     *
     * @param qualifiedName of the QuickSightDataset
     * @param name of the QuickSightDataset
     * @return the updated QuickSightDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDataset removeDescription(String qualifiedName, String name) throws AtlanException {
        return (QuickSightDataset) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QuickSightDataset.
     *
     * @param qualifiedName of the QuickSightDataset
     * @param name of the QuickSightDataset
     * @return the updated QuickSightDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDataset removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (QuickSightDataset) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QuickSightDataset.
     *
     * @param qualifiedName of the QuickSightDataset
     * @param name of the QuickSightDataset
     * @return the updated QuickSightDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDataset removeOwners(String qualifiedName, String name) throws AtlanException {
        return (QuickSightDataset) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QuickSightDataset.
     *
     * @param qualifiedName of the QuickSightDataset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightDataset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDataset updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (QuickSightDataset) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QuickSightDataset.
     *
     * @param qualifiedName of the QuickSightDataset
     * @param name of the QuickSightDataset
     * @return the updated QuickSightDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDataset removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (QuickSightDataset) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QuickSightDataset.
     *
     * @param qualifiedName of the QuickSightDataset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDataset updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (QuickSightDataset) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QuickSightDataset.
     *
     * @param qualifiedName of the QuickSightDataset
     * @param name of the QuickSightDataset
     * @return the updated QuickSightDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDataset removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (QuickSightDataset) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QuickSightDataset.
     *
     * @param qualifiedName for the QuickSightDataset
     * @param name human-readable name of the QuickSightDataset
     * @param terms the list of terms to replace on the QuickSightDataset, or null to remove all terms from the QuickSightDataset
     * @return the QuickSightDataset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDataset replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightDataset) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QuickSightDataset, without replacing existing terms linked to the QuickSightDataset.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDataset's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QuickSightDataset
     * @param terms the list of terms to append to the QuickSightDataset
     * @return the QuickSightDataset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDataset appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (QuickSightDataset) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightDataset, without replacing all existing terms linked to the QuickSightDataset.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDataset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QuickSightDataset
     * @param terms the list of terms to remove from the QuickSightDataset, which must be referenced by GUID
     * @return the QuickSightDataset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDataset removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (QuickSightDataset) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a QuickSightDataset.
     *
     * @param qualifiedName of the QuickSightDataset
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the QuickSightDataset
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a QuickSightDataset.
     *
     * @param qualifiedName of the QuickSightDataset
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the QuickSightDataset
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
     * Remove a classification from a QuickSightDataset.
     *
     * @param qualifiedName of the QuickSightDataset
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the QuickSightDataset
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
