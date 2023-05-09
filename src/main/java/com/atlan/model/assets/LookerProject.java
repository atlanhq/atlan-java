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
 * Instance of a Looker project in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class LookerProject extends Looker {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerProject";

    /** Fixed typeName for LookerProjects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerModel> models;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerExplore> explores;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerField> fields;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerView> views;

    /**
     * Reference to a LookerProject by GUID.
     *
     * @param guid the GUID of the LookerProject to reference
     * @return reference to a LookerProject that can be used for defining a relationship to a LookerProject
     */
    public static LookerProject refByGuid(String guid) {
        return LookerProject.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerProject by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerProject to reference
     * @return reference to a LookerProject that can be used for defining a relationship to a LookerProject
     */
    public static LookerProject refByQualifiedName(String qualifiedName) {
        return LookerProject.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a LookerProject by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerProject to retrieve
     * @return the requested full LookerProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerProject does not exist or the provided GUID is not a LookerProject
     */
    public static LookerProject retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof LookerProject) {
            return (LookerProject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "LookerProject");
        }
    }

    /**
     * Retrieves a LookerProject by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerProject to retrieve
     * @return the requested full LookerProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerProject does not exist
     */
    public static LookerProject retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof LookerProject) {
            return (LookerProject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "LookerProject");
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerProject to active.
     *
     * @param qualifiedName for the LookerProject
     * @return true if the LookerProject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a LookerProject.
     *
     * @param qualifiedName of the LookerProject
     * @param name of the LookerProject
     * @return the minimal request necessary to update the LookerProject, as a builder
     */
    public static LookerProjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerProject.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerProject, from a potentially
     * more-complete LookerProject object.
     *
     * @return the minimal object necessary to update the LookerProject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerProject are not found in the initial object
     */
    @Override
    public LookerProjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "LookerProject", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a LookerProject.
     *
     * @param qualifiedName of the LookerProject
     * @param name of the LookerProject
     * @return the updated LookerProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerProject removeDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerProject) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a LookerProject.
     *
     * @param qualifiedName of the LookerProject
     * @param name of the LookerProject
     * @return the updated LookerProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerProject removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerProject) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a LookerProject.
     *
     * @param qualifiedName of the LookerProject
     * @param name of the LookerProject
     * @return the updated LookerProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerProject removeOwners(String qualifiedName, String name) throws AtlanException {
        return (LookerProject) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a LookerProject.
     *
     * @param qualifiedName of the LookerProject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerProject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerProject updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (LookerProject) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerProject.
     *
     * @param qualifiedName of the LookerProject
     * @param name of the LookerProject
     * @return the updated LookerProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerProject removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LookerProject) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a LookerProject.
     *
     * @param qualifiedName of the LookerProject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerProject updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LookerProject) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerProject.
     *
     * @param qualifiedName of the LookerProject
     * @param name of the LookerProject
     * @return the updated LookerProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerProject removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LookerProject) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the LookerProject.
     *
     * @param qualifiedName for the LookerProject
     * @param name human-readable name of the LookerProject
     * @param terms the list of terms to replace on the LookerProject, or null to remove all terms from the LookerProject
     * @return the LookerProject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerProject replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (LookerProject) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerProject, without replacing existing terms linked to the LookerProject.
     * Note: this operation must make two API calls — one to retrieve the LookerProject's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerProject
     * @param terms the list of terms to append to the LookerProject
     * @return the LookerProject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerProject appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerProject) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerProject, without replacing all existing terms linked to the LookerProject.
     * Note: this operation must make two API calls — one to retrieve the LookerProject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerProject
     * @param terms the list of terms to remove from the LookerProject, which must be referenced by GUID
     * @return the LookerProject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerProject removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerProject) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a LookerProject, without replacing existing classifications linked to the LookerProject.
     * Note: this operation must make two API calls — one to retrieve the LookerProject's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the LookerProject
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems
     * @return the updated LookerProject
     */
    public static LookerProject appendClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        return (LookerProject) Asset.appendClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a LookerProject, without replacing existing classifications linked to the LookerProject.
     * Note: this operation must make two API calls — one to retrieve the LookerProject's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the LookerProject
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LookerProject
     */
    public static LookerProject appendClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (LookerProject) Asset.appendClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add classifications to a LookerProject.
     *
     * @param qualifiedName of the LookerProject
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LookerProject
     * @deprecated see {@link #appendClassifications(String, List)} instead
     */
    @Deprecated
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a LookerProject.
     *
     * @param qualifiedName of the LookerProject
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LookerProject
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
     * Remove a classification from a LookerProject.
     *
     * @param qualifiedName of the LookerProject
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the LookerProject
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
