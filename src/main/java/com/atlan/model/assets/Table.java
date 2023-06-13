/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a database table in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class Table extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Table";

    /** Fixed typeName for Tables. */
    @Getter(onMethod_ = {@Override})
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

    /** Columns that exist within this table. */
    @Attribute
    @Singular
    SortedSet<Column> columns;

    /** Queries that involve this table. */
    @Attribute
    @Singular
    SortedSet<AtlanQuery> queries;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<Table> facts;

    /** Schema in which this table exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    Schema schema;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<Table> dimensions;

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
     * Retrieves a Table by its GUID, complete with all of its relationships.
     *
     * @param guid of the Table to retrieve
     * @return the requested full Table, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Table does not exist or the provided GUID is not a Table
     */
    public static Table retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof Table) {
            return (Table) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "Table");
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
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof Table) {
            return (Table) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "Table");
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
                .qualifiedName(generateQualifiedName(name, schemaQualifiedName))
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
     * Generate a unique table name.
     *
     * @param name of the table
     * @param schemaQualifiedName unique name of the schema in which this table exists
     * @return a unique name for the table
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/" + name;
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
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Table", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
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
        return (Table) Asset.removeDescription(updater(qualifiedName, name));
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
        return (Table) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (Table) Asset.removeOwners(updater(qualifiedName, name));
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
    public static Table updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
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
        return (Table) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (Table) Asset.removeAnnouncement(updater(qualifiedName, name));
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

    /**
     * Add Atlan tags to a Table, without replacing existing Atlan tags linked to the Table.
     * Note: this operation must make two API calls — one to retrieve the Table's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Table
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Table
     */
    public static Table appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (Table) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Table, without replacing existing Atlan tags linked to the Table.
     * Note: this operation must make two API calls — one to retrieve the Table's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Table
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Table
     */
    public static Table appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Table) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Table.
     *
     * @param qualifiedName of the Table
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Table
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Table.
     *
     * @param qualifiedName of the Table
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Table
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a Table.
     *
     * @param qualifiedName of the Table
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Table
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
