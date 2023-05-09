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
 * Instance of a ThoughtSpot Liveboard in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class ThoughtspotLiveboard extends Thoughtspot {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ThoughtspotLiveboard";

    /** Fixed typeName for ThoughtspotLiveboards. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Dashlets within the Liveboard. */
    @Attribute
    @Singular
    SortedSet<ThoughtspotDashlet> thoughtspotDashlets;

    /**
     * Reference to a ThoughtspotLiveboard by GUID.
     *
     * @param guid the GUID of the ThoughtspotLiveboard to reference
     * @return reference to a ThoughtspotLiveboard that can be used for defining a relationship to a ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard refByGuid(String guid) {
        return ThoughtspotLiveboard.builder().guid(guid).build();
    }

    /**
     * Reference to a ThoughtspotLiveboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ThoughtspotLiveboard to reference
     * @return reference to a ThoughtspotLiveboard that can be used for defining a relationship to a ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard refByQualifiedName(String qualifiedName) {
        return ThoughtspotLiveboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a ThoughtspotLiveboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the ThoughtspotLiveboard to retrieve
     * @return the requested full ThoughtspotLiveboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ThoughtspotLiveboard does not exist or the provided GUID is not a ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ThoughtspotLiveboard) {
            return (ThoughtspotLiveboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ThoughtspotLiveboard");
        }
    }

    /**
     * Retrieves a ThoughtspotLiveboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ThoughtspotLiveboard to retrieve
     * @return the requested full ThoughtspotLiveboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ThoughtspotLiveboard does not exist
     */
    public static ThoughtspotLiveboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof ThoughtspotLiveboard) {
            return (ThoughtspotLiveboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ThoughtspotLiveboard");
        }
    }

    /**
     * Restore the archived (soft-deleted) ThoughtspotLiveboard to active.
     *
     * @param qualifiedName for the ThoughtspotLiveboard
     * @return true if the ThoughtspotLiveboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the minimal request necessary to update the ThoughtspotLiveboard, as a builder
     */
    public static ThoughtspotLiveboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return ThoughtspotLiveboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ThoughtspotLiveboard, from a potentially
     * more-complete ThoughtspotLiveboard object.
     *
     * @return the minimal object necessary to update the ThoughtspotLiveboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ThoughtspotLiveboard are not found in the initial object
     */
    @Override
    public ThoughtspotLiveboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ThoughtspotLiveboard", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return (ThoughtspotLiveboard) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (ThoughtspotLiveboard) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return (ThoughtspotLiveboard) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ThoughtspotLiveboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (ThoughtspotLiveboard)
                Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (ThoughtspotLiveboard) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (ThoughtspotLiveboard)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (ThoughtspotLiveboard) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ThoughtspotLiveboard.
     *
     * @param qualifiedName for the ThoughtspotLiveboard
     * @param name human-readable name of the ThoughtspotLiveboard
     * @param terms the list of terms to replace on the ThoughtspotLiveboard, or null to remove all terms from the ThoughtspotLiveboard
     * @return the ThoughtspotLiveboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ThoughtspotLiveboard, without replacing existing terms linked to the ThoughtspotLiveboard.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotLiveboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ThoughtspotLiveboard
     * @param terms the list of terms to append to the ThoughtspotLiveboard
     * @return the ThoughtspotLiveboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard appendTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ThoughtspotLiveboard, without replacing all existing terms linked to the ThoughtspotLiveboard.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotLiveboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ThoughtspotLiveboard
     * @param terms the list of terms to remove from the ThoughtspotLiveboard, which must be referenced by GUID
     * @return the ThoughtspotLiveboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a ThoughtspotLiveboard, without replacing existing classifications linked to the ThoughtspotLiveboard.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotLiveboard's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems
     * @return the updated ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard appendClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.appendClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a ThoughtspotLiveboard, without replacing existing classifications linked to the ThoughtspotLiveboard.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotLiveboard's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard appendClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.appendClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add classifications to a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ThoughtspotLiveboard
     * @deprecated see {@link #appendClassifications(String, List)} instead
     */
    @Deprecated
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ThoughtspotLiveboard
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
     * Remove a classification from a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the ThoughtspotLiveboard
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
