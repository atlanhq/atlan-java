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
public class TableauWorksheet extends Tableau {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauWorksheet";

    /** Fixed typeName for TableauWorksheets. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String siteQualifiedName;

    /** TBC */
    @Attribute
    String projectQualifiedName;

    /** TBC */
    @Attribute
    String topLevelProjectQualifiedName;

    /** TBC */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    String workbookQualifiedName;

    /** TBC */
    @Attribute
    TableauWorkbook workbook;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauDatasourceField> datasourceFields;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauCalculatedField> calculatedFields;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauDashboard> dashboards;

    /**
     * Reference to a TableauWorksheet by GUID.
     *
     * @param guid the GUID of the TableauWorksheet to reference
     * @return reference to a TableauWorksheet that can be used for defining a relationship to a TableauWorksheet
     */
    public static TableauWorksheet refByGuid(String guid) {
        return TableauWorksheet.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauWorksheet by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauWorksheet to reference
     * @return reference to a TableauWorksheet that can be used for defining a relationship to a TableauWorksheet
     */
    public static TableauWorksheet refByQualifiedName(String qualifiedName) {
        return TableauWorksheet.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the minimal request necessary to update the TableauWorksheet, as a builder
     */
    public static TableauWorksheetBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauWorksheet.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauWorksheet, from a potentially
     * more-complete TableauWorksheet object.
     *
     * @return the minimal object necessary to update the TableauWorksheet, as a builder
     */
    @Override
    protected TableauWorksheetBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauWorksheet, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (TableauWorksheet) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the updated TableauWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (TableauWorksheet)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TableauWorksheet) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the updated TableauWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (TableauWorksheet)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauWorksheet
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the TableauWorksheet
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the TableauWorksheet.
     *
     * @param qualifiedName for the TableauWorksheet
     * @param name human-readable name of the TableauWorksheet
     * @param terms the list of terms to replace on the TableauWorksheet, or null to remove all terms from the TableauWorksheet
     * @return the TableauWorksheet that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauWorksheet) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauWorksheet, without replacing existing terms linked to the TableauWorksheet.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheet's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauWorksheet
     * @param terms the list of terms to append to the TableauWorksheet
     * @return the TableauWorksheet that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauWorksheet) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauWorksheet, without replacing all existing terms linked to the TableauWorksheet.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheet's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauWorksheet
     * @param terms the list of terms to remove from the TableauWorksheet, which must be referenced by GUID
     * @return the TableauWorksheet that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauWorksheet) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
