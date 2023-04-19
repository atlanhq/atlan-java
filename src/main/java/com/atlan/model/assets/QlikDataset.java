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
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * TBC
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class QlikDataset extends Qlik {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QlikDataset";

    /** Fixed typeName for QlikDatasets. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String qlikDatasetTechnicalName;

    /** TBC */
    @Attribute
    String qlikDatasetType;

    /** TBC */
    @Attribute
    String qlikDatasetUri;

    /** TBC */
    @Attribute
    String qlikDatasetSubtype;

    /** TBC */
    @Attribute
    QlikSpace qlikSpace;

    /**
     * Reference to a QlikDataset by GUID.
     *
     * @param guid the GUID of the QlikDataset to reference
     * @return reference to a QlikDataset that can be used for defining a relationship to a QlikDataset
     */
    public static QlikDataset refByGuid(String guid) {
        return QlikDataset.builder().guid(guid).build();
    }

    /**
     * Reference to a QlikDataset by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QlikDataset to reference
     * @return reference to a QlikDataset that can be used for defining a relationship to a QlikDataset
     */
    public static QlikDataset refByQualifiedName(String qualifiedName) {
        return QlikDataset.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the minimal request necessary to update the QlikDataset, as a builder
     */
    public static QlikDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return QlikDataset.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QlikDataset, from a potentially
     * more-complete QlikDataset object.
     *
     * @return the minimal object necessary to update the QlikDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QlikDataset are not found in the initial object
     */
    @Override
    public QlikDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QlikDataset", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a QlikDataset by its GUID, complete with all of its relationships.
     *
     * @param guid of the QlikDataset to retrieve
     * @return the requested full QlikDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikDataset does not exist or the provided GUID is not a QlikDataset
     */
    public static QlikDataset retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QlikDataset) {
            return (QlikDataset) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QlikDataset");
        }
    }

    /**
     * Retrieves a QlikDataset by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QlikDataset to retrieve
     * @return the requested full QlikDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikDataset does not exist
     */
    public static QlikDataset retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof QlikDataset) {
            return (QlikDataset) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QlikDataset");
        }
    }

    /**
     * Restore the archived (soft-deleted) QlikDataset to active.
     *
     * @param qualifiedName for the QlikDataset
     * @return true if the QlikDataset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeDescription(String qualifiedName, String name) throws AtlanException {
        return (QlikDataset) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (QlikDataset) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeOwners(String qualifiedName, String name) throws AtlanException {
        return (QlikDataset) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikDataset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (QlikDataset) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (QlikDataset) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (QlikDataset) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (QlikDataset) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the QlikDataset
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the QlikDataset
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
     * Remove a classification from a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the QlikDataset
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the QlikDataset.
     *
     * @param qualifiedName for the QlikDataset
     * @param name human-readable name of the QlikDataset
     * @param terms the list of terms to replace on the QlikDataset, or null to remove all terms from the QlikDataset
     * @return the QlikDataset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikDataset replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QlikDataset) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QlikDataset, without replacing existing terms linked to the QlikDataset.
     * Note: this operation must make two API calls — one to retrieve the QlikDataset's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QlikDataset
     * @param terms the list of terms to append to the QlikDataset
     * @return the QlikDataset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QlikDataset appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (QlikDataset) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikDataset, without replacing all existing terms linked to the QlikDataset.
     * Note: this operation must make two API calls — one to retrieve the QlikDataset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QlikDataset
     * @param terms the list of terms to remove from the QlikDataset, which must be referenced by GUID
     * @return the QlikDataset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (QlikDataset) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
