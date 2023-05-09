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
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Qlik App in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class QlikApp extends Qlik {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QlikApp";

    /** Fixed typeName for QlikApps. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Whether section access or data masking is enabled (true) or not (false). */
    @Attribute
    Boolean qlikHasSectionAccess;

    /** Origin App ID of the Qlik app. */
    @Attribute
    String qlikOriginAppId;

    /** Whether the app is encrypted (true) or not (false). */
    @Attribute
    Boolean qlikIsEncrypted;

    /** Whether the app is in direct query mode (true) or not (false). */
    @Attribute
    Boolean qlikIsDirectQueryMode;

    /** Static space taken up by the app. */
    @Attribute
    Long qlikAppStaticByteSize;

    /** Space in which the app exists. */
    @Attribute
    QlikSpace qlikSpace;

    /** Sheets that exist within the app. */
    @Attribute
    @Singular
    SortedSet<QlikSheet> qlikSheets;

    /**
     * Reference to a QlikApp by GUID.
     *
     * @param guid the GUID of the QlikApp to reference
     * @return reference to a QlikApp that can be used for defining a relationship to a QlikApp
     */
    public static QlikApp refByGuid(String guid) {
        return QlikApp.builder().guid(guid).build();
    }

    /**
     * Reference to a QlikApp by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QlikApp to reference
     * @return reference to a QlikApp that can be used for defining a relationship to a QlikApp
     */
    public static QlikApp refByQualifiedName(String qualifiedName) {
        return QlikApp.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QlikApp by its GUID, complete with all of its relationships.
     *
     * @param guid of the QlikApp to retrieve
     * @return the requested full QlikApp, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikApp does not exist or the provided GUID is not a QlikApp
     */
    public static QlikApp retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QlikApp) {
            return (QlikApp) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QlikApp");
        }
    }

    /**
     * Retrieves a QlikApp by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QlikApp to retrieve
     * @return the requested full QlikApp, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikApp does not exist
     */
    public static QlikApp retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof QlikApp) {
            return (QlikApp) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QlikApp");
        }
    }

    /**
     * Restore the archived (soft-deleted) QlikApp to active.
     *
     * @param qualifiedName for the QlikApp
     * @return true if the QlikApp is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QlikApp.
     *
     * @param qualifiedName of the QlikApp
     * @param name of the QlikApp
     * @return the minimal request necessary to update the QlikApp, as a builder
     */
    public static QlikAppBuilder<?, ?> updater(String qualifiedName, String name) {
        return QlikApp.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QlikApp, from a potentially
     * more-complete QlikApp object.
     *
     * @return the minimal object necessary to update the QlikApp, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QlikApp are not found in the initial object
     */
    @Override
    public QlikAppBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QlikApp", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QlikApp.
     *
     * @param qualifiedName of the QlikApp
     * @param name of the QlikApp
     * @return the updated QlikApp, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikApp removeDescription(String qualifiedName, String name) throws AtlanException {
        return (QlikApp) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QlikApp.
     *
     * @param qualifiedName of the QlikApp
     * @param name of the QlikApp
     * @return the updated QlikApp, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikApp removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (QlikApp) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QlikApp.
     *
     * @param qualifiedName of the QlikApp
     * @param name of the QlikApp
     * @return the updated QlikApp, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikApp removeOwners(String qualifiedName, String name) throws AtlanException {
        return (QlikApp) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QlikApp.
     *
     * @param qualifiedName of the QlikApp
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikApp, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikApp updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (QlikApp) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QlikApp.
     *
     * @param qualifiedName of the QlikApp
     * @param name of the QlikApp
     * @return the updated QlikApp, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikApp removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (QlikApp) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QlikApp.
     *
     * @param qualifiedName of the QlikApp
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikApp updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (QlikApp) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QlikApp.
     *
     * @param qualifiedName of the QlikApp
     * @param name of the QlikApp
     * @return the updated QlikApp, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikApp removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (QlikApp) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QlikApp.
     *
     * @param qualifiedName for the QlikApp
     * @param name human-readable name of the QlikApp
     * @param terms the list of terms to replace on the QlikApp, or null to remove all terms from the QlikApp
     * @return the QlikApp that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikApp replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QlikApp) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QlikApp, without replacing existing terms linked to the QlikApp.
     * Note: this operation must make two API calls — one to retrieve the QlikApp's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QlikApp
     * @param terms the list of terms to append to the QlikApp
     * @return the QlikApp that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QlikApp appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (QlikApp) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikApp, without replacing all existing terms linked to the QlikApp.
     * Note: this operation must make two API calls — one to retrieve the QlikApp's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QlikApp
     * @param terms the list of terms to remove from the QlikApp, which must be referenced by GUID
     * @return the QlikApp that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QlikApp removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (QlikApp) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a QlikApp, without replacing existing classifications linked to the QlikApp.
     * Note: this operation must make two API calls — one to retrieve the QlikApp's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the QlikApp
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems
     * @return the updated QlikApp
     */
    public static QlikApp appendClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        return (QlikApp) Asset.appendClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a QlikApp, without replacing existing classifications linked to the QlikApp.
     * Note: this operation must make two API calls — one to retrieve the QlikApp's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the QlikApp
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QlikApp
     */
    public static QlikApp appendClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QlikApp) Asset.appendClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add classifications to a QlikApp.
     *
     * @param qualifiedName of the QlikApp
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the QlikApp
     * @deprecated see {@link #appendClassifications(String, List)} instead
     */
    @Deprecated
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a QlikApp.
     *
     * @param qualifiedName of the QlikApp
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the QlikApp
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
     * Remove a classification from a QlikApp.
     *
     * @param qualifiedName of the QlikApp
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the QlikApp
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
