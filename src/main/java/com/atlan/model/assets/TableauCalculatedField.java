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
public class TableauCalculatedField extends Tableau {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauCalculatedField";

    /** Fixed typeName for TableauCalculatedFields. */
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
    String workbookQualifiedName;

    /** TBC */
    @Attribute
    String datasourceQualifiedName;

    /** TBC */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    String dataCategory;

    /** TBC */
    @Attribute
    String role;

    /** TBC */
    @Attribute
    String tableauDataType;

    /** TBC */
    @Attribute
    String formula;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> upstreamFields;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauWorksheet> worksheets;

    /** TBC */
    @Attribute
    TableauDatasource datasource;

    /**
     * Reference to a TableauCalculatedField by GUID.
     *
     * @param guid the GUID of the TableauCalculatedField to reference
     * @return reference to a TableauCalculatedField that can be used for defining a relationship to a TableauCalculatedField
     */
    public static TableauCalculatedField refByGuid(String guid) {
        return TableauCalculatedField.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauCalculatedField by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauCalculatedField to reference
     * @return reference to a TableauCalculatedField that can be used for defining a relationship to a TableauCalculatedField
     */
    public static TableauCalculatedField refByQualifiedName(String qualifiedName) {
        return TableauCalculatedField.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param name of the TableauCalculatedField
     * @return the minimal request necessary to update the TableauCalculatedField, as a builder
     */
    public static TableauCalculatedFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauCalculatedField.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauCalculatedField, from a potentially
     * more-complete TableauCalculatedField object.
     *
     * @return the minimal object necessary to update the TableauCalculatedField, as a builder
     */
    @Override
    protected TableauCalculatedFieldBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauCalculatedField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (TableauCalculatedField)
                Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param name of the TableauCalculatedField
     * @return the updated TableauCalculatedField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (TableauCalculatedField)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TableauCalculatedField)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param name of the TableauCalculatedField
     * @return the updated TableauCalculatedField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (TableauCalculatedField)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauCalculatedField
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the TableauCalculatedField
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the TableauCalculatedField.
     *
     * @param qualifiedName for the TableauCalculatedField
     * @param name human-readable name of the TableauCalculatedField
     * @param terms the list of terms to replace on the TableauCalculatedField, or null to remove all terms from the TableauCalculatedField
     * @return the TableauCalculatedField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauCalculatedField) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauCalculatedField, without replacing existing terms linked to the TableauCalculatedField.
     * Note: this operation must make two API calls — one to retrieve the TableauCalculatedField's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauCalculatedField
     * @param terms the list of terms to append to the TableauCalculatedField
     * @return the TableauCalculatedField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField appendTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauCalculatedField) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauCalculatedField, without replacing all existing terms linked to the TableauCalculatedField.
     * Note: this operation must make two API calls — one to retrieve the TableauCalculatedField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauCalculatedField
     * @param terms the list of terms to remove from the TableauCalculatedField, which must be referenced by GUID
     * @return the TableauCalculatedField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauCalculatedField) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
