/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a table in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Table extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Table";

    /** Fixed typeName for tables. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Number of columns in this table. */
    @Attribute
    Long columnCount;

    /** Number of rows in this table. */
    @Attribute
    Long rowCount;

    /** Size of the table in bytes. */
    @Attribute
    Long sizeBytes;

    /** TBC */
    @Attribute
    String alias;

    /** Whether this table is temporary (true) or not (false). */
    @Attribute
    Boolean isTemporary;

    /** TBC */
    @Attribute
    Boolean isQueryPreview;

    /** Unused attributes. */
    @JsonIgnore
    Map<String, String> queryPreviewConfig;

    /** TBC */
    @Attribute
    String externalLocation;

    /** TBC */
    @Attribute
    String externalLocationRegion;

    /** TBC */
    @Attribute
    String externalLocationFormat;

    /** TBC */
    @Attribute
    Boolean isPartitioned;

    /** TBC */
    @Attribute
    String partitionStrategy;

    /** TBC */
    @Attribute
    Long partitionCount;

    /** TBC */
    @Attribute
    String partitionList;

    /** Schema in which this table exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    Schema schema;

    /** Columns that exist within this table. */
    @Singular
    @Attribute
    SortedSet<Column> columns;

    /**
     * Reference to a table by GUID.
     *
     * @param guid the GUID of the table to reference
     * @return reference to a table that can be used for defining a relationship to a table
     */
    public static Table refByGuid(String guid) {
        return Table.builder().guid(guid).build();
    }

    /**
     * Reference to a table by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the table to reference
     * @return reference to a table that can be used for defining a relationship to a table
     */
    public static Table refByQualifiedName(String qualifiedName) {
        return Table.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create a table.
     *
     * @param name of the table
     * @param schemaQualifiedName unique name of the schema in which this table exists
     * @return the minimal request necessary to create the table, as a builder
     */
    public static TableBuilder<?, ?> creator(String name, String schemaQualifiedName) {
        String[] tokens = schemaQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return Table.builder()
                .name(name)
                .qualifiedName(schemaQualifiedName + "/" + name)
                .connectorType(connectorType)
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .schema(Schema.refByQualifiedName(schemaQualifiedName))
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a table.
     *
     * @param qualifiedName of the table
     * @param name of the table
     * @return the minimal request necessary to update the table, as a builder
     */
    public static TableBuilder<?, ?> updater(String qualifiedName, String name) {
        return Table.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a table, from a potentially
     * more-complete table object.
     *
     * @return the minimal object necessary to update the table, as a builder
     */
    @Override
    protected TableBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a table.
     *
     * @param qualifiedName of the table
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated table, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Table updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (Table) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a table.
     *
     * @param qualifiedName of the table
     * @param name of the table
     * @return the updated table, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Table removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Table)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a table.
     *
     * @param qualifiedName of the table
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Table updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (Table) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a table.
     *
     * @param qualifiedName of the table
     * @param name of the table
     * @return the updated table, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Table removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Table)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a table.
     *
     * @param qualifiedName of the table
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the table
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a table.
     *
     * @param qualifiedName of the table
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the table
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the table.
     *
     * @param qualifiedName for the table
     * @param name human-readable name of the table
     * @param terms the list of terms to replace on the table, or null to remove all terms from the table
     * @return the table that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Table replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (Table) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the table, without replacing existing terms linked to the table.
     * Note: this operation must make two API calls — one to retrieve the table's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the table
     * @param terms the list of terms to append to the table
     * @return the table that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Table appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Table) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a table, without replacing all existing terms linked to the table.
     * Note: this operation must make two API calls — one to retrieve the table's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the table
     * @param terms the list of terms to remove from the table, which must be referenced by GUID
     * @return the table that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Table removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Table) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
