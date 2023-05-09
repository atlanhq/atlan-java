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
 * Instance of a Looker view in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class LookerView extends Looker {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerView";

    /** Fixed typeName for LookerViews. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String projectName;

    /** TBC */
    @Attribute
    LookerProject project;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerField> fields;

    /**
     * Reference to a LookerView by GUID.
     *
     * @param guid the GUID of the LookerView to reference
     * @return reference to a LookerView that can be used for defining a relationship to a LookerView
     */
    public static LookerView refByGuid(String guid) {
        return LookerView.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerView by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerView to reference
     * @return reference to a LookerView that can be used for defining a relationship to a LookerView
     */
    public static LookerView refByQualifiedName(String qualifiedName) {
        return LookerView.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a LookerView by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerView to retrieve
     * @return the requested full LookerView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerView does not exist or the provided GUID is not a LookerView
     */
    public static LookerView retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof LookerView) {
            return (LookerView) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "LookerView");
        }
    }

    /**
     * Retrieves a LookerView by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerView to retrieve
     * @return the requested full LookerView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerView does not exist
     */
    public static LookerView retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof LookerView) {
            return (LookerView) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "LookerView");
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerView to active.
     *
     * @param qualifiedName for the LookerView
     * @return true if the LookerView is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param name of the LookerView
     * @return the minimal request necessary to update the LookerView, as a builder
     */
    public static LookerViewBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerView.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerView, from a potentially
     * more-complete LookerView object.
     *
     * @return the minimal object necessary to update the LookerView, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerView are not found in the initial object
     */
    @Override
    public LookerViewBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "LookerView", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param name of the LookerView
     * @return the updated LookerView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerView removeDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerView) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param name of the LookerView
     * @return the updated LookerView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerView removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerView) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param name of the LookerView
     * @return the updated LookerView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerView removeOwners(String qualifiedName, String name) throws AtlanException {
        return (LookerView) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerView, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerView updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (LookerView) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param name of the LookerView
     * @return the updated LookerView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerView removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LookerView) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerView updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LookerView) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param name of the LookerView
     * @return the updated LookerView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerView removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LookerView) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the LookerView.
     *
     * @param qualifiedName for the LookerView
     * @param name human-readable name of the LookerView
     * @param terms the list of terms to replace on the LookerView, or null to remove all terms from the LookerView
     * @return the LookerView that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerView replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (LookerView) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerView, without replacing existing terms linked to the LookerView.
     * Note: this operation must make two API calls — one to retrieve the LookerView's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerView
     * @param terms the list of terms to append to the LookerView
     * @return the LookerView that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerView appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerView) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerView, without replacing all existing terms linked to the LookerView.
     * Note: this operation must make two API calls — one to retrieve the LookerView's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerView
     * @param terms the list of terms to remove from the LookerView, which must be referenced by GUID
     * @return the LookerView that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerView removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerView) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a LookerView, without replacing existing classifications linked to the LookerView.
     * Note: this operation must make two API calls — one to retrieve the LookerView's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the LookerView
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems
     * @return the updated LookerView
     */
    public static LookerView appendClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        return (LookerView) Asset.appendClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a LookerView, without replacing existing classifications linked to the LookerView.
     * Note: this operation must make two API calls — one to retrieve the LookerView's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the LookerView
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LookerView
     */
    public static LookerView appendClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (LookerView) Asset.appendClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add classifications to a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LookerView
     * @deprecated see {@link #appendClassifications(String, List)} instead
     */
    @Deprecated
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LookerView
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
     * Remove a classification from a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the LookerView
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
