/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a connection in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class Connection extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Connection";

    /** Fixed typeName for Connections. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Type of connection. */
    @Attribute
    AtlanConnectionCategory category;

    /** TBC */
    @Attribute
    String subCategory;

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
    @Singular("putQueryPreviewConfig")
    Map<String, String> queryPreviewConfig;

    /** TBC */
    @Attribute
    String queryConfig;

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
     * Reference to a Connection by GUID.
     *
     * @param guid the GUID of the Connection to reference
     * @return reference to a Connection that can be used for defining a relationship to a Connection
     */
    public static Connection refByGuid(String guid) {
        return Connection.builder().guid(guid).build();
    }

    /**
     * Reference to a Connection by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Connection to reference
     * @return reference to a Connection that can be used for defining a relationship to a Connection
     */
    public static Connection refByQualifiedName(String qualifiedName) {
        return Connection.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
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
     * Generate a unique connection name.
     *
     * @param connectorType type of the connection's connector
     * @return a unique name for the connection
     */
    private static String generateQualifiedName(AtlanConnectorType connectorType) {
        long now = System.currentTimeMillis() / 1000;
        return "default/" + connectorType.getValue() + "/" + now;
    }

    /**
     * Builds the minimal object necessary to update a Connection.
     *
     * @param qualifiedName of the Connection
     * @param name of the Connection
     * @return the minimal request necessary to update the Connection, as a builder
     */
    public static ConnectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return Connection.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Connection, from a potentially
     * more-complete Connection object.
     *
     * @return the minimal object necessary to update the Connection, as a builder
     */
    @Override
    protected ConnectionBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieve the epoch component of the connection name from its qualifiedName.
     *
     * @param qualifiedName of the connection
     * @return the epoch component of the qualifiedName
     */
    @JsonIgnore
    public static String getEpochFromQualifiedName(String qualifiedName) {
        return qualifiedName.substring(qualifiedName.lastIndexOf("/") + 1);
    }

    /**
     * Find a connection by its human-readable name and type.
     *
     * @param name of the connection
     * @param type of the connection
     * @param attributes an optional collection of attributes to retrieve for the connection
     * @return all connections with that name and type, if found
     * @throws AtlanException on any API problems, or if the connection does not exist
     */
    public static List<Connection> findByName(String name, AtlanConnectorType type, Collection<String> attributes)
            throws AtlanException {
        Query byType = QueryFactory.withType(TYPE_NAME);
        Query byName = QueryFactory.withExactName(name);
        Query active = QueryFactory.active();
        Query byConnectorType = TermQuery.of(t -> t.field("connectorName").value(type.getValue()))
                ._toQuery();
        Query filter = BoolQuery.of(b -> b.filter(byType, byName, active, byConnectorType))
                ._toQuery();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> builder = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().query(filter).build());
        if (attributes != null && !attributes.isEmpty()) {
            builder.attributes(attributes);
        }
        IndexSearchRequest request = builder.build();
        IndexSearchResponse response = request.search();
        List<Connection> connections = new ArrayList<>();
        if (response != null) {
            List<Entity> results = response.getEntities();
            while (results != null) {
                for (Entity result : results) {
                    if (result instanceof Connection) {
                        connections.add((Connection) result);
                    }
                }
                response = response.getNextPage();
                results = response.getEntities();
            }
        }
        if (connections.isEmpty()) {
            throw new NotFoundException(
                    "Unable to find a connection with the name '" + name + "' of type: " + type.getValue(),
                    "ATLAN-JAVA-CLIENT-404-095",
                    404,
                    null);
        } else {
            return connections;
        }
    }

    /**
     * Retrieves a Connection by its GUID, complete with all of its relationships.
     *
     * @param guid of the Connection to retrieve
     * @return the requested full Connection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist or the provided GUID is not a Connection
     */
    public static Connection retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof Connection) {
            return (Connection) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a Connection.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a Connection by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Connection to retrieve
     * @return the requested full Connection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist
     */
    public static Connection retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof Connection) {
            return (Connection) entity;
        } else {
            throw new NotFoundException(
                    "No Connection found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
        }
    }

    /**
     * Update the certificate on a Connection.
     *
     * @param qualifiedName of the Connection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Connection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Connection updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (Connection) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Connection.
     *
     * @param qualifiedName of the Connection
     * @param name of the Connection
     * @return the updated Connection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Connection removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Connection)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Connection.
     *
     * @param qualifiedName of the Connection
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
     * Remove the announcement from a Connection.
     *
     * @param qualifiedName of the Connection
     * @param name of the Connection
     * @return the updated Connection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Connection removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Connection)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Connection.
     *
     * @param qualifiedName of the Connection
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the Connection
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Connection.
     *
     * @param qualifiedName of the Connection
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the Connection
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the Connection.
     *
     * @param qualifiedName for the Connection
     * @param name human-readable name of the Connection
     * @param terms the list of terms to replace on the Connection, or null to remove all terms from the Connection
     * @return the Connection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Connection replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (Connection) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Connection, without replacing existing terms linked to the Connection.
     * Note: this operation must make two API calls — one to retrieve the Connection's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the Connection
     * @param terms the list of terms to append to the Connection
     * @return the Connection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Connection appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Connection) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Connection, without replacing all existing terms linked to the Connection.
     * Note: this operation must make two API calls — one to retrieve the Connection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Connection
     * @param terms the list of terms to remove from the Connection, which must be referenced by GUID
     * @return the Connection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Connection removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Connection) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
