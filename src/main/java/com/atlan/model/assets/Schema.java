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
 * Instance of a database schema in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class Schema extends Asset implements ISchema, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Schema";

    /** Fixed typeName for Schemas. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Database in which this schema exists. */
    @Attribute
    IDatabase database;

    /** TBC */
    @Attribute
    String databaseName;

    /** TBC */
    @Attribute
    String databaseQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> dbtSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    Boolean isProfiled;

    /** TBC */
    @Attribute
    Long lastProfiledAt;

    /** Materialized views that exist within this schema. */
    @Attribute
    @Singular
    @JsonProperty("materialisedViews")
    SortedSet<IMaterializedView> materializedViews;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** Stored procedures (routines) that are defined within this schema. */
    @Attribute
    @Singular
    SortedSet<IProcedure> procedures;

    /** TBC */
    @Attribute
    Long queryCount;

    /** TBC */
    @Attribute
    Long queryCountUpdatedAt;

    /** TBC */
    @Attribute
    Long queryUserCount;

    /** TBC */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** TBC */
    @Attribute
    String schemaName;

    /** TBC */
    @Attribute
    String schemaQualifiedName;

    /** Snowflake Pipes that are defined within this schema. */
    @Attribute
    @Singular
    SortedSet<ISnowflakePipe> snowflakePipes;

    /** Snowflake Streams that are defined within this schema. */
    @Attribute
    @Singular
    SortedSet<ISnowflakeStream> snowflakeStreams;

    /** Tags applied to this schema in Snowflake. */
    @Attribute
    @Singular
    SortedSet<ISnowflakeTag> snowflakeTags;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

    /** Number of tables in this schema. */
    @Attribute
    Integer tableCount;

    /** TBC */
    @Attribute
    String tableName;

    /** TBC */
    @Attribute
    String tableQualifiedName;

    /** Tables that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<ITable> tables;

    /** Number of views in this schema. */
    @Attribute
    @JsonProperty("viewsCount")
    Integer viewCount;

    /** TBC */
    @Attribute
    String viewName;

    /** TBC */
    @Attribute
    String viewQualifiedName;

    /** Views that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<IView> views;

    /**
     * Reference to a Schema by GUID.
     *
     * @param guid the GUID of the Schema to reference
     * @return reference to a Schema that can be used for defining a relationship to a Schema
     */
    public static Schema refByGuid(String guid) {
        return Schema.builder().guid(guid).build();
    }

    /**
     * Reference to a Schema by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Schema to reference
     * @return reference to a Schema that can be used for defining a relationship to a Schema
     */
    public static Schema refByQualifiedName(String qualifiedName) {
        return Schema.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Schema by its GUID, complete with all of its relationships.
     *
     * @param guid of the Schema to retrieve
     * @return the requested full Schema, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Schema does not exist or the provided GUID is not a Schema
     */
    public static Schema retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof Schema) {
            return (Schema) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "Schema");
        }
    }

    /**
     * Retrieves a Schema by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Schema to retrieve
     * @return the requested full Schema, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Schema does not exist
     */
    public static Schema retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof Schema) {
            return (Schema) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "Schema");
        }
    }

    /**
     * Restore the archived (soft-deleted) Schema to active.
     *
     * @param qualifiedName for the Schema
     * @return true if the Schema is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a schema.
     *
     * @param name of the schema
     * @param databaseQualifiedName unique name of the database in which this schema exists
     * @return the minimal request necessary to create the schema, as a builder
     */
    public static SchemaBuilder<?, ?> creator(String name, String databaseQualifiedName) {
        String[] tokens = databaseQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return Schema.builder()
                .name(name)
                .qualifiedName(generateQualifiedName(name, databaseQualifiedName))
                .connectorType(connectorType)
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .database(Database.refByQualifiedName(databaseQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique schema name.
     *
     * @param name of the schema
     * @param databaseQualifiedName unique name of the database in which this schema exists
     * @return a unique name for the schema
     */
    public static String generateQualifiedName(String name, String databaseQualifiedName) {
        return databaseQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a Schema.
     *
     * @param qualifiedName of the Schema
     * @param name of the Schema
     * @return the minimal request necessary to update the Schema, as a builder
     */
    public static SchemaBuilder<?, ?> updater(String qualifiedName, String name) {
        return Schema.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Schema, from a potentially
     * more-complete Schema object.
     *
     * @return the minimal object necessary to update the Schema, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Schema are not found in the initial object
     */
    @Override
    public SchemaBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Schema", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a Schema.
     *
     * @param qualifiedName of the Schema
     * @param name of the Schema
     * @return the updated Schema, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Schema removeDescription(String qualifiedName, String name) throws AtlanException {
        return (Schema) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Schema.
     *
     * @param qualifiedName of the Schema
     * @param name of the Schema
     * @return the updated Schema, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Schema removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (Schema) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a Schema.
     *
     * @param qualifiedName of the Schema
     * @param name of the Schema
     * @return the updated Schema, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Schema removeOwners(String qualifiedName, String name) throws AtlanException {
        return (Schema) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a Schema.
     *
     * @param qualifiedName of the Schema
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Schema, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Schema updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (Schema) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Schema.
     *
     * @param qualifiedName of the Schema
     * @param name of the Schema
     * @return the updated Schema, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Schema removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Schema) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a Schema.
     *
     * @param qualifiedName of the Schema
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Schema updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (Schema) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Schema.
     *
     * @param qualifiedName of the Schema
     * @param name of the Schema
     * @return the updated Schema, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Schema removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Schema) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the Schema.
     *
     * @param qualifiedName for the Schema
     * @param name human-readable name of the Schema
     * @param terms the list of terms to replace on the Schema, or null to remove all terms from the Schema
     * @return the Schema that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Schema replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Schema) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Schema, without replacing existing terms linked to the Schema.
     * Note: this operation must make two API calls — one to retrieve the Schema's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the Schema
     * @param terms the list of terms to append to the Schema
     * @return the Schema that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Schema appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (Schema) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Schema, without replacing all existing terms linked to the Schema.
     * Note: this operation must make two API calls — one to retrieve the Schema's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Schema
     * @param terms the list of terms to remove from the Schema, which must be referenced by GUID
     * @return the Schema that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Schema removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (Schema) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a Schema, without replacing existing Atlan tags linked to the Schema.
     * Note: this operation must make two API calls — one to retrieve the Schema's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Schema
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Schema
     */
    public static Schema appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (Schema) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Schema, without replacing existing Atlan tags linked to the Schema.
     * Note: this operation must make two API calls — one to retrieve the Schema's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Schema
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Schema
     */
    public static Schema appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Schema) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Schema.
     *
     * @param qualifiedName of the Schema
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Schema
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Schema.
     *
     * @param qualifiedName of the Schema
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Schema
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
     * Remove an Atlan tag from a Schema.
     *
     * @param qualifiedName of the Schema
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Schema
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
