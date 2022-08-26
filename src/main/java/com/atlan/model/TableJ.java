/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.ReferenceJ;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a table in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class TableJ extends SQLJ {
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
    ReferenceJ atlanSchema;

    /** Columns that exist within this table. */
    @Singular
    @Attribute
    List<ReferenceJ> columns;

    /**
     * Builds the minimal object necessary to create a table.
     *
     * @param name of the table
     * @param connectorName name of the connector (software / system) that hosts the table
     * @param schemaName name of schema in which this table exists
     * @param schemaQualifiedName unique name of the schema in which this table exists
     * @param databaseName name of database in which this table exists
     * @param databaseQualifiedName unique name of the database in which this table exists
     * @param connectionQualifiedName unique name of the specific instance of the software / system that hosts the table
     * @return the minimal request necessary to create the table, as a builder
     */
    public static TableJBuilder<?, ?> creator(
            String name,
            String connectorName,
            String schemaName,
            String schemaQualifiedName,
            String databaseName,
            String databaseQualifiedName,
            String connectionQualifiedName) {
        // TODO: can we simplify the argument list by just taking qualifiedNames and extracting the
        //  non-qualifiedNames from these?
        return TableJ.builder()
                .name(name)
                .qualifiedName(schemaQualifiedName + "/" + name)
                .connectorName(connectorName)
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
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
    public static TableJBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableJ.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a table, from a potentially
     * more-complete table object.
     *
     * @return the minimal object necessary to update the table, as a builder
     */
    @Override
    protected TableJBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a table.
     * @param qualifiedName of the table
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated table, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableJ updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (TableJ) AssetJ.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
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
    public static TableJ updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TableJ) AssetJ.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }
}
