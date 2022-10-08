/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class TableauProject extends Tableau {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauProject";

    /** Fixed typeName for TableauProjects. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
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
    @Singular
    SortedSet<TableauWorkbook> workbooks;

    /** TBC */
    @Attribute
    TableauSite site;

    /** TBC */
    @Attribute
    TableauProject parentProject;

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
     */
    @Override
    protected TableauProjectBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
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
    public static TableauProject updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
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
        return (TableauProject)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
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
        return (TableauProject)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauProject
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
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
}
