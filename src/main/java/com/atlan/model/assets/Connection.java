/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.api.EntityBulkEndpoint;
import com.atlan.cache.GroupCache;
import com.atlan.cache.RoleCache;
import com.atlan.cache.UserCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.ConnectionCreationResponse;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.KeywordFields;
import com.atlan.model.enums.QueryUsernameStrategy;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a connection in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class Connection extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Connection";

    /** Fixed typeName for Connections. */
    @Getter(onMethod_ = {@Override})
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

    /** TBC */
    @Attribute
    String previewCredentialStrategy;

    /** TBC */
    @Attribute
    String policyStrategy;

    /** TBC */
    @Attribute
    QueryUsernameStrategy queryUsernameStrategy;

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

    /** Whether sample data can be previewed for this connection (true) or not (false). */
    @Attribute
    Boolean isSampleDataPreviewEnabled;

    /** Number of days over which popularity is calculated, for example 30 days. */
    @Attribute
    Long popularityInsightsTimeframe;

    /** Whether the connection has popularity insights (true) or not (false). */
    @Attribute
    Boolean hasPopularityInsights;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> connectionDbtEnvironments;

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
     * Retrieves a Connection by its GUID, complete with all of its relationships.
     *
     * @param guid of the Connection to retrieve
     * @return the requested full Connection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist or the provided GUID is not a Connection
     */
    public static Connection retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof Connection) {
            return (Connection) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "Connection");
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
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof Connection) {
            return (Connection) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "Connection");
        }
    }

    /**
     * Restore the archived (soft-deleted) Connection to active.
     *
     * @param qualifiedName for the Connection
     * @return true if the Connection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Determine the connector type from the provided qualifiedName.
     *
     * @param qualifiedName of the connection
     * @return the connector type, or null if the qualifiedName is not for a connected asset
     */
    public static AtlanConnectorType getConnectorTypeFromQualifiedName(String qualifiedName) {
        return getConnectorTypeFromQualifiedName(qualifiedName.split("/"));
    }

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
     * @throws InvalidRequestException if no admin has been defined for the connection, or an invalid admin has been defined
     * @throws NotFoundException if a non-existent admin has been defined for the connection
     * @throws AtlanException on any other error related to the request, such as an inability to retrieve the existing admins in the system
     */
    public static ConnectionBuilder<?, ?> creator(
            String name,
            AtlanConnectorType connectorType,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws AtlanException {
        boolean adminFound = false;
        ConnectionBuilder<?, ?> builder = Connection.builder()
                .name(name)
                .qualifiedName(generateQualifiedName(connectorType.getValue()))
                .category(connectorType.getCategory())
                .connectorType(connectorType);
        if (adminRoles != null && !adminRoles.isEmpty()) {
            for (String roleId : adminRoles) {
                RoleCache.getNameForId(roleId);
            }
            adminFound = true;
            builder.adminRoles(adminRoles);
        }
        if (adminGroups != null && !adminGroups.isEmpty()) {
            for (String groupAlias : adminGroups) {
                GroupCache.getIdForAlias(groupAlias);
            }
            adminFound = true;
            builder.adminGroups(adminGroups);
        }
        if (adminUsers != null && !adminUsers.isEmpty()) {
            for (String userName : adminUsers) {
                UserCache.getIdForName(userName);
            }
            adminFound = true;
            builder.adminUsers(adminUsers);
        }
        if (adminFound) {
            return builder;
        } else {
            throw new InvalidRequestException(ErrorCode.NO_CONNECTION_ADMIN);
        }
    }

    /**
     * If an asset with the same qualifiedName exists, updates the existing asset. Otherwise, creates the asset.
     * No classifications or custom metadata will be changed if updating an existing asset, irrespective of what
     * is included in the asset itself when the method is called.
     *
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws NotFoundException if any of the provided connection admins do not actually exist
     */
    @Override
    public ConnectionCreationResponse upsert() throws AtlanException {
        // Validate the provided connection admins prior to attempting to create
        // (the cache retrievals will throw errors directly if there are any)
        if (adminRoles != null && !adminRoles.isEmpty()) {
            for (String roleId : adminRoles) {
                RoleCache.getNameForId(roleId);
            }
        }
        if (adminGroups != null && !adminGroups.isEmpty()) {
            for (String groupAlias : adminGroups) {
                GroupCache.getIdForAlias(groupAlias);
            }
        }
        if (adminUsers != null && !adminUsers.isEmpty()) {
            for (String userName : adminUsers) {
                UserCache.getIdForName(userName);
            }
        }
        return EntityBulkEndpoint.connectionUpsert(this, false, false);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #upsert()} method.
     * If an asset does exist, optionally overwrites any classifications and / or custom metadata.
     *
     * @param replaceClassifications whether to replace classifications during an update (true) or not (false)
     * @param replaceCustomMetadata whether to replace custom metadata during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws NotFoundException if any of the provided connection admins do not actually exist
     */
    @Override
    public ConnectionCreationResponse upsert(boolean replaceClassifications, boolean replaceCustomMetadata)
            throws AtlanException {
        // Validate the provided connection admins prior to attempting to create
        // (the cache retrievals will throw errors directly if there are any)
        if (adminRoles != null && !adminRoles.isEmpty()) {
            for (String roleId : adminRoles) {
                RoleCache.getNameForId(roleId);
            }
        }
        if (adminGroups != null && !adminGroups.isEmpty()) {
            for (String groupAlias : adminGroups) {
                GroupCache.getIdForAlias(groupAlias);
            }
        }
        if (adminUsers != null && !adminUsers.isEmpty()) {
            for (String userName : adminUsers) {
                UserCache.getIdForName(userName);
            }
        }
        return EntityBulkEndpoint.connectionUpsert(this, replaceClassifications, replaceCustomMetadata);
    }

    /**
     * Generate a unique connection name.
     *
     * @param connectorType the name of the type of the connection's connector
     * @return a unique name for the connection
     */
    private static String generateQualifiedName(String connectorType) {
        long now = System.currentTimeMillis() / 1000;
        return "default/" + connectorType + "/" + now;
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
     * @throws InvalidRequestException if any of the minimal set of required properties for Connection are not found in the initial object
     */
    @Override
    public ConnectionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Connection", String.join(",", missing));
        }
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
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the connection does not exist
     */
    public static List<Connection> findByName(String name, AtlanConnectorType type, Collection<String> attributes)
            throws AtlanException {
        Query filter = QueryFactory.CompoundQuery.builder()
                .must(QueryFactory.beActive())
                .must(QueryFactory.beOfType(TYPE_NAME))
                .must(QueryFactory.have(KeywordFields.NAME).eq(name))
                .must(QueryFactory.have(KeywordFields.CONNECTOR_TYPE).eq(type.getValue()))
                .build()
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
            List<Asset> results = response.getAssets();
            while (results != null) {
                for (Asset result : results) {
                    if (result instanceof Connection) {
                        connections.add((Connection) result);
                    }
                }
                response = response.getNextPage();
                results = response.getAssets();
            }
        }
        if (connections.isEmpty()) {
            throw new NotFoundException(ErrorCode.CONNECTION_NOT_FOUND_BY_NAME, name, type.getValue());
        } else {
            return connections;
        }
    }

    /**
     * Remove the system description from a Connection.
     *
     * @param qualifiedName of the Connection
     * @param name of the Connection
     * @return the updated Connection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Connection removeDescription(String qualifiedName, String name) throws AtlanException {
        return (Connection) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Connection.
     *
     * @param qualifiedName of the Connection
     * @param name of the Connection
     * @return the updated Connection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Connection removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (Connection) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a Connection.
     *
     * @param qualifiedName of the Connection
     * @param name of the Connection
     * @return the updated Connection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Connection removeOwners(String qualifiedName, String name) throws AtlanException {
        return (Connection) Asset.removeOwners(updater(qualifiedName, name));
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
    public static Connection updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
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
        return (Connection) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (Connection) Asset.removeAnnouncement(updater(qualifiedName, name));
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
     * Add classifications to a Connection.
     *
     * @param qualifiedName of the Connection
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the Connection
     */
    public static void addClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
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
}
