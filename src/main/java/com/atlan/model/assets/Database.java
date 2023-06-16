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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a database in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class Database extends Asset implements IDatabase, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Database";

    /** Fixed typeName for Databases. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

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

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

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

    /** Number of schemas in this database. */
    @Attribute
    Integer schemaCount;

    /** TBC */
    @Attribute
    String schemaName;

    /** TBC */
    @Attribute
    String schemaQualifiedName;

    /** Schemas that exist within this database. */
    @Attribute
    @Singular
    SortedSet<ISchema> schemas;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

    /** TBC */
    @Attribute
    String tableName;

    /** TBC */
    @Attribute
    String tableQualifiedName;

    /** TBC */
    @Attribute
    String viewName;

    /** TBC */
    @Attribute
    String viewQualifiedName;

    /**
     * Reference to a Database by GUID.
     *
     * @param guid the GUID of the Database to reference
     * @return reference to a Database that can be used for defining a relationship to a Database
     */
    public static Database refByGuid(String guid) {
        return Database.builder().guid(guid).build();
    }

    /**
     * Reference to a Database by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Database to reference
     * @return reference to a Database that can be used for defining a relationship to a Database
     */
    public static Database refByQualifiedName(String qualifiedName) {
        return Database.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Database by its GUID, complete with all of its relationships.
     *
     * @param guid of the Database to retrieve
     * @return the requested full Database, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Database does not exist or the provided GUID is not a Database
     */
    public static Database retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof Database) {
            return (Database) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "Database");
        }
    }

    /**
     * Retrieves a Database by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Database to retrieve
     * @return the requested full Database, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Database does not exist
     */
    public static Database retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof Database) {
            return (Database) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "Database");
        }
    }

    /**
     * Restore the archived (soft-deleted) Database to active.
     *
     * @param qualifiedName for the Database
     * @return true if the Database is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a database.
     *
     * @param name of the database
     * @param connectionQualifiedName unique name of the specific instance of the software / system that hosts the database
     * @return the minimal request necessary to create the database, as a builder
     */
    public static DatabaseBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        AtlanConnectorType connectorType =
                Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName.split("/"));
        return Database.builder()
                .name(name)
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .connectorType(connectorType)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique database name.
     *
     * @param name of the database
     * @param connectionQualifiedName unique name of the specific instance of the software / system that hosts the database
     * @return a unique name for the database
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a Database.
     *
     * @param qualifiedName of the Database
     * @param name of the Database
     * @return the minimal request necessary to update the Database, as a builder
     */
    public static DatabaseBuilder<?, ?> updater(String qualifiedName, String name) {
        return Database.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Database, from a potentially
     * more-complete Database object.
     *
     * @return the minimal object necessary to update the Database, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Database are not found in the initial object
     */
    @Override
    public DatabaseBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Database", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a Database.
     *
     * @param qualifiedName of the Database
     * @param name of the Database
     * @return the updated Database, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Database removeDescription(String qualifiedName, String name) throws AtlanException {
        return (Database) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Database.
     *
     * @param qualifiedName of the Database
     * @param name of the Database
     * @return the updated Database, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Database removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (Database) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a Database.
     *
     * @param qualifiedName of the Database
     * @param name of the Database
     * @return the updated Database, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Database removeOwners(String qualifiedName, String name) throws AtlanException {
        return (Database) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a Database.
     *
     * @param qualifiedName of the Database
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Database, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Database updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (Database) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Database.
     *
     * @param qualifiedName of the Database
     * @param name of the Database
     * @return the updated Database, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Database removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Database) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a Database.
     *
     * @param qualifiedName of the Database
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Database updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (Database) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Database.
     *
     * @param qualifiedName of the Database
     * @param name of the Database
     * @return the updated Database, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Database removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Database) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the Database.
     *
     * @param qualifiedName for the Database
     * @param name human-readable name of the Database
     * @param terms the list of terms to replace on the Database, or null to remove all terms from the Database
     * @return the Database that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Database replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Database) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Database, without replacing existing terms linked to the Database.
     * Note: this operation must make two API calls — one to retrieve the Database's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the Database
     * @param terms the list of terms to append to the Database
     * @return the Database that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Database appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (Database) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Database, without replacing all existing terms linked to the Database.
     * Note: this operation must make two API calls — one to retrieve the Database's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Database
     * @param terms the list of terms to remove from the Database, which must be referenced by GUID
     * @return the Database that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Database removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (Database) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a Database, without replacing existing Atlan tags linked to the Database.
     * Note: this operation must make two API calls — one to retrieve the Database's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Database
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Database
     */
    public static Database appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (Database) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Database, without replacing existing Atlan tags linked to the Database.
     * Note: this operation must make two API calls — one to retrieve the Database's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Database
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Database
     */
    public static Database appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Database) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Database.
     *
     * @param qualifiedName of the Database
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Database
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Database.
     *
     * @param qualifiedName of the Database
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Database
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
     * Remove an Atlan tag from a Database.
     *
     * @param qualifiedName of the Database
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Database
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
