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
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Tableau project in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class TableauProject extends Tableau {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauProject";

    /** Fixed typeName for TableauProjects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String siteQualifiedName;

    /** TBC */
    @Attribute
    String topLevelProjectQualifiedName;

    /** TBC */
    @Attribute
    Boolean isTopLevelProject;

    /** TBC */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    TableauProject parentProject;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauWorkbook> workbooks;

    /** TBC */
    @Attribute
    TableauSite site;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauDatasource> datasources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauFlow> flows;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauProject> childProjects;

    /**
     * Reference to a TableauProject by GUID.
     *
     * @param guid the GUID of the TableauProject to reference
     * @return reference to a TableauProject that can be used for defining a relationship to a TableauProject
     */
    public static TableauProject refByGuid(String guid) {
        return TableauProject.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauProject by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauProject to reference
     * @return reference to a TableauProject that can be used for defining a relationship to a TableauProject
     */
    public static TableauProject refByQualifiedName(String qualifiedName) {
        return TableauProject.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a TableauProject by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauProject to retrieve
     * @return the requested full TableauProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauProject does not exist or the provided GUID is not a TableauProject
     */
    public static TableauProject retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof TableauProject) {
            return (TableauProject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "TableauProject");
        }
    }

    /**
     * Retrieves a TableauProject by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauProject to retrieve
     * @return the requested full TableauProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauProject does not exist
     */
    public static TableauProject retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof TableauProject) {
            return (TableauProject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "TableauProject");
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauProject to active.
     *
     * @param qualifiedName for the TableauProject
     * @return true if the TableauProject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the minimal request necessary to update the TableauProject, as a builder
     */
    public static TableauProjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauProject.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauProject, from a potentially
     * more-complete TableauProject object.
     *
     * @return the minimal object necessary to update the TableauProject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauProject are not found in the initial object
     */
    @Override
    public TableauProjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "TableauProject", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauProject) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauProject) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeOwners(String qualifiedName, String name) throws AtlanException {
        return (TableauProject) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauProject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TableauProject) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (TableauProject) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TableauProject) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (TableauProject) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TableauProject.
     *
     * @param qualifiedName for the TableauProject
     * @param name human-readable name of the TableauProject
     * @param terms the list of terms to replace on the TableauProject, or null to remove all terms from the TableauProject
     * @return the TableauProject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauProject replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauProject) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauProject, without replacing existing terms linked to the TableauProject.
     * Note: this operation must make two API calls — one to retrieve the TableauProject's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauProject
     * @param terms the list of terms to append to the TableauProject
     * @return the TableauProject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauProject appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauProject) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauProject, without replacing all existing terms linked to the TableauProject.
     * Note: this operation must make two API calls — one to retrieve the TableauProject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauProject
     * @param terms the list of terms to remove from the TableauProject, which must be referenced by GUID
     * @return the TableauProject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauProject) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a TableauProject, without replacing existing classifications linked to the TableauProject.
     * Note: this operation must make two API calls — one to retrieve the TableauProject's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the TableauProject
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems
     * @return the updated TableauProject
     */
    public static TableauProject appendClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        return (TableauProject) Asset.appendClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a TableauProject, without replacing existing classifications linked to the TableauProject.
     * Note: this operation must make two API calls — one to retrieve the TableauProject's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the TableauProject
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauProject
     */
    public static TableauProject appendClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TableauProject) Asset.appendClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add classifications to a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauProject
     * @deprecated see {@link #appendClassifications(String, List)} instead
     */
    @Deprecated
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauProject
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
     * Remove a classification from a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the TableauProject
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
