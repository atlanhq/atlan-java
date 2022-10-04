/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.GuidReference;
import com.atlan.model.relations.Reference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a connection in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Connection extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Connection";

    /** Fixed typeName for column processes. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Type of connection. */
    @Attribute
    AtlanConnectionCategory category;

    /** Unused attributes */
    @JsonIgnore
    String subCategory;

    @JsonIgnore
    Map<String, String> queryPreviewConfig;

    @JsonIgnore
    String queryConfig;

    /** Host name of the connection's source. */
    @Attribute
    String host;

    /** Port number to the connection's source. */
    @Attribute
    Integer port;

    /** When true, allow the source to be queried. */
    @Attribute
    Boolean allowQuery;

    /** When true, allow data previews of the source. */
    @Attribute
    Boolean allowQueryPreview;

    /** TBC */
    @Attribute
    String credentialStrategy;

    /** Maximum number of rows that can be returned for the source. */
    @Attribute
    Long rowLimit;

    /** TBC */
    @Attribute
    String defaultCredentialGuid;

    /** TBC */
    @Attribute
    String connectorIcon;

    /** TBC */
    @Attribute
    String connectorImage;

    /** TBC */
    @Attribute
    String sourceLogo;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> connectionDbtEnvironments;

    /**
     * Determine the connector type from the provided qualifiedName.
     *
     * @param tokens of the qualifiedName, from which to determine the connector type
     * @return the connector type, or null if the qualifiedName is not for a connected asset
     */
    protected static AtlanConnectorType getConnectorTypeFromQualifiedName(String[] tokens) {
        if (tokens.length > 1) {
            return AtlanConnectorType.fromValue(tokens[1]);
        }
        return null;
    }

    /**
     * Builds the minimal object necessary to create a connection.
     * Note: at least one of {@code #adminRoles}, {@code #adminGroups}, or {@code #adminUsers} must be
     * provided or an InvalidRequestException will be thrown.
     *
     * @param name of the connection
     * @param connectorType type of the connection's connector (this determines what logo appears for the assets)
     * @param adminRoles the GUIDs of the roles that can administer this connection
     * @param adminGroups the names of the groups that can administer this connection
     * @param adminUsers the names of the users that can administer this connection
     * @return the minimal object necessary to create the connection, as a builder
     * @throws InvalidRequestException if no admin has been defined for the connection
     */
    public static ConnectionBuilder<?, ?> creator(
            String name,
            AtlanConnectorType connectorType,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws InvalidRequestException {
        boolean adminFound = false;
        ConnectionBuilder<?, ?> builder = Connection.builder()
                .name(name)
                .qualifiedName(generateQualifiedName(connectorType))
                .category(connectorType.getCategory())
                .connectorType(connectorType);
        if (adminRoles != null && !adminRoles.isEmpty()) {
            adminFound = true;
            builder = builder.adminRoles(adminRoles);
        }
        if (adminGroups != null && !adminGroups.isEmpty()) {
            adminFound = true;
            builder = builder.adminGroups(adminGroups);
        }
        if (adminUsers != null && !adminUsers.isEmpty()) {
            adminFound = true;
            builder = builder.adminUsers(adminUsers);
        }
        if (adminFound) {
            return builder;
        } else {
            throw new InvalidRequestException(
                    "No admin provided for the connection, will not attempt to create one.",
                    "adminRoles,adminGroups,adminUsers",
                    "ATLAN-CLIENT-CONNECTION-400-001",
                    400,
                    null);
        }
    }

    /**
     * Builds the minimal object necessary to update a connection.
     *
     * @param qualifiedName of the connection
     * @param name of the connection
     * @return the minimal object necessary to update the connection, as a builder
     */
    public static ConnectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return Connection.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a connection, from a potentially
     * more-complete connection object.
     *
     * @return the minimal object necessary to update the connection, as a builder
     */
    @Override
    protected ConnectionBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique connection name.
     * @param connectorType type of the connection's connector
     * @return a unique name for the connection
     */
    private static String generateQualifiedName(AtlanConnectorType connectorType) {
        long now = System.currentTimeMillis() / 1000;
        return "default/" + connectorType.getValue() + "/" + now;
    }

    /**
     * Update the certificate on a connection.
     *
     * @param qualifiedName of the connection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated connection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Connection updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (Connection) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a connection.
     *
     * @param qualifiedName of the connection
     * @param name of the connection
     * @return the updated connection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Connection removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Connection)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a connection.
     *
     * @param qualifiedName of the connection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Connection updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (Connection) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a connection.
     *
     * @param qualifiedName of the connection
     * @param name of the connection
     * @return the updated connection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Connection removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Connection)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a connection.
     *
     * @param qualifiedName of the connection
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the connection
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a connection.
     *
     * @param qualifiedName of the connection
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the connection
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the connection.
     *
     * @param qualifiedName for the connection
     * @param name human-readable name of the connection
     * @param terms the list of terms to replace on the connection, or null to remove all terms from the connection
     * @return the connection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Connection replaceTerms(String qualifiedName, String name, List<Reference> terms)
            throws AtlanException {
        return (Connection) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the connection, without replacing existing terms linked to the connection.
     * Note: this operation must make two API calls — one to retrieve the connection's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the connection
     * @param terms the list of terms to append to the connection
     * @return the connection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Connection appendTerms(String qualifiedName, List<Reference> terms) throws AtlanException {
        return (Connection) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a connection, without replacing all existing terms linked to the connection.
     * Note: this operation must make two API calls — one to retrieve the connection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the connection
     * @param terms the list of terms to remove from the connection, which must be referenced by GUID
     * @return the connection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Connection removeTerms(String qualifiedName, List<GuidReference> terms) throws AtlanException {
        return (Connection) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
