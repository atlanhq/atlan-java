/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a database in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Database extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Database";

    /** Fixed typeName for Databases. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Number of schemas in this database. */
    @Attribute
    Integer schemaCount;

    /** Schemas that exist within this database. */
    @Attribute
    @Singular
    SortedSet<Schema> schemas;

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
                .qualifiedName(connectionQualifiedName + "/" + name)
                .connectorType(connectorType)
                .connectionQualifiedName(connectionQualifiedName);
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
    public static Database updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
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
     * Add classifications to a Database.
     *
     * @param qualifiedName of the Database
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the Database
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Database.
     *
     * @param qualifiedName of the Database
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the Database
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
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
    public static Database replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
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
    public static Database appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
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
    public static Database removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Database) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
