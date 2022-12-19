/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
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
@SuppressWarnings("cast")
public class Table extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Table";

    /** Fixed typeName for Tables. */
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

    /** TBC */
    @Attribute
    @Singular("putQueryPreviewConfig")
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

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TablePartition> partitions;

    /** Schema in which this table exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    Schema schema;

    /** Columns that exist within this table. */
    @Attribute
    @Singular
    SortedSet<Column> columns;

    /** Queries that involve this table. */
    @Attribute
    @Singular
    SortedSet<AtlanQuery> queries;

    /**
     * Reference to a Table by GUID.
     *
     * @param guid the GUID of the Table to reference
     * @return reference to a Table that can be used for defining a relationship to a Table
     */
    public static Table refByGuid(String guid) {
        return Table.builder().guid(guid).build();
    }

    /**
     * Reference to a Table by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Table to reference
     * @return reference to a Table that can be used for defining a relationship to a Table
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
     * Builds the minimal object necessary to update a Table.
     *
     * @param qualifiedName of the Table
     * @param name of the Table
     * @return the minimal request necessary to update the Table, as a builder
     */
    public static TableBuilder<?, ?> updater(String qualifiedName, String name) {
        return Table.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Table, from a potentially
     * more-complete Table object.
     *
     * @return the minimal object necessary to update the Table, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Table are not found in the initial object
     */
    @Override
    public TableBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating Table is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a Table by its GUID, complete with all of its relationships.
     *
     * @param guid of the Table to retrieve
     * @return the requested full Table, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Table does not exist or the provided GUID is not a Table
     */
    public static Table retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof Table) {
            return (Table) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a Table.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a Table by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Table to retrieve
     * @return the requested full Table, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Table does not exist
     */
    public static Table retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof Table) {
            return (Table) entity;
        } else {
            throw new NotFoundException(
                    "No Table found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
        }
    }

    /**
     * Restore the archived (soft-deleted) Table to active.
     *
     * @param qualifiedName for the Table
     * @return true if the Table is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a Table.
     *
     * @param qualifiedName of the Table
     * @param name of the Table
     * @return the updated Table, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Table removeDescription(String qualifiedName, String name) throws AtlanException {
        return (Table)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a Table.
     *
     * @param qualifiedName of the Table
     * @param name of the Table
     * @return the updated Table, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Table removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (Table) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a Table.
     *
     * @param qualifiedName of the Table
     * @param name of the Table
     * @return the updated Table, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Table removeOwners(String qualifiedName, String name) throws AtlanException {
        return (Table) Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a Table.
     *
     * @param qualifiedName of the Table
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Table, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Table updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (Table) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Table.
     *
     * @param qualifiedName of the Table
     * @param name of the Table
     * @return the updated Table, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Table removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Table)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Table.
     *
     * @param qualifiedName of the Table
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
     * Remove the announcement from a Table.
     *
     * @param qualifiedName of the Table
     * @param name of the Table
     * @return the updated Table, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Table removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Table)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Table.
     *
     * @param qualifiedName of the Table
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the Table
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Table.
     *
     * @param qualifiedName of the Table
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the Table
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the Table.
     *
     * @param qualifiedName for the Table
     * @param name human-readable name of the Table
     * @param terms the list of terms to replace on the Table, or null to remove all terms from the Table
     * @return the Table that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Table replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (Table) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Table, without replacing existing terms linked to the Table.
     * Note: this operation must make two API calls — one to retrieve the Table's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the Table
     * @param terms the list of terms to append to the Table
     * @return the Table that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Table appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Table) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Table, without replacing all existing terms linked to the Table.
     * Note: this operation must make two API calls — one to retrieve the Table's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Table
     * @param terms the list of terms to remove from the Table, which must be referenced by GUID
     * @return the Table that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Table removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Table) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
