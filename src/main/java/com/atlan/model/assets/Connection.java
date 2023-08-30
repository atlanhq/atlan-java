/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.ApiToken;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.ConnectionCreationResponse;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.QueryUsernameStrategy;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.CompoundQuery;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a connection in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class Connection extends Asset implements IConnection, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Connection";

    /** Fixed typeName for Connections. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** When true, allow the source to be queried. */
    @Attribute
    Boolean allowQuery;

    /** When true, allow data previews of the source. */
    @Attribute
    Boolean allowQueryPreview;

    /** Type of connection. */
    @Attribute
    AtlanConnectionCategory category;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> connectionDbtEnvironments;

    /** TBC */
    @Attribute
    String connectionSSOCredentialGuid;

    /** Despite the name, this is not used for anything. Only the value of connectorName impacts icons. */
    @Attribute
    String connectorIcon;

    /** Despite the name, this is not used for anything. Only the value of connectorName impacts icons. */
    @Attribute
    String connectorImage;

    /** TBC */
    @Attribute
    String credentialStrategy;

    /** TBC */
    @Attribute
    String defaultCredentialGuid;

    /** Whether the connection has popularity insights (true) or not (false). */
    @Attribute
    Boolean hasPopularityInsights;

    /** Host name of the connection's source. */
    @Attribute
    String host;

    /** Whether sample data can be previewed for this connection (true) or not (false). */
    @Attribute
    Boolean isSampleDataPreviewEnabled;

    /** TBC */
    @Attribute
    String policyStrategy;

    /** Number of days over which popularity is calculated, for example 30 days. */
    @Attribute
    Long popularityInsightsTimeframe;

    /** Port number to the connection's source. */
    @Attribute
    Integer port;

    /** TBC */
    @Attribute
    String previewCredentialStrategy;

    /** TBC */
    @Attribute
    String queryConfig;

    /** TBC */
    @Attribute
    @Singular("putQueryPreviewConfig")
    Map<String, String> queryPreviewConfig;

    /** TBC */
    @Attribute
    Long queryTimeout;

    /** TBC */
    @Attribute
    QueryUsernameStrategy queryUsernameStrategy;

    /** Maximum number of rows that can be returned for the source. */
    @Attribute
    Long rowLimit;

    /** Despite the name, this is not used for anything. Only the value of connectorName impacts icons. */
    @Attribute
    String sourceLogo;

    /** Subtype of the connection. */
    @Attribute
    String subCategory;

    /**
     * Builds the minimal object necessary to create a relationship to a Connection, from a potentially
     * more-complete Connection object.
     *
     * @return the minimal object necessary to relate to the Connection
     * @throws InvalidRequestException if any of the minimal set of required properties for a Connection relationship are not found in the initial object
     */
    @Override
    public Connection trimToReference() throws InvalidRequestException {
        if (this.getGuid() != null && !this.getGuid().isEmpty()) {
            return refByGuid(this.getGuid());
        }
        if (this.getQualifiedName() != null && !this.getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getQualifiedName());
        }
        if (this.getUniqueAttributes() != null
                && this.getUniqueAttributes().getQualifiedName() != null
                && !this.getUniqueAttributes().getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getUniqueAttributes().getQualifiedName());
        }
        throw new InvalidRequestException(
                ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, TYPE_NAME, "guid, qualifiedName");
    }

    /**
     * Start a fluent search that will return all Connection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Connection assets will be included.
     *
     * @return a fluent search that includes all Connection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all Connection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Connection assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all Connection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all Connection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) Connections will be included
     * @return a fluent search that includes all Connection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all Connection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Connections will be included
     * @return a fluent search that includes all Connection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(CompoundQuery.assetType(TYPE_NAME));
        if (!includeArchived) {
            builder.where(CompoundQuery.ACTIVE);
        }
        return builder;
    }

    /**
     * Start an asset filter that will return all Connection assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Connection assets will be included.
     *
     * @return an asset filter that includes all Connection assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all Connection assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Connection assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all Connection assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all Connection assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) Connections will be included
     * @return an asset filter that includes all Connection assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all Connection assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Connections will be included
     * @return an asset filter that includes all Connection assets
     * @deprecated replaced by {@link #select(AtlanClient, boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a Connection by GUID.
     *
     * @param guid the GUID of the Connection to reference
     * @return reference to a Connection that can be used for defining a relationship to a Connection
     */
    public static Connection refByGuid(String guid) {
        return Connection._internal().guid(guid).build();
    }

    /**
     * Reference to a Connection by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Connection to reference
     * @return reference to a Connection that can be used for defining a relationship to a Connection
     */
    public static Connection refByQualifiedName(String qualifiedName) {
        return Connection._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Connection by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the Connection to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Connection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist or the provided GUID is not a Connection
     */
    @JsonIgnore
    public static Connection get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a Connection by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Connection to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Connection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist or the provided GUID is not a Connection
     */
    @JsonIgnore
    public static Connection get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a Connection by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Connection to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Connection, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist or the provided GUID is not a Connection
     */
    @JsonIgnore
    public static Connection get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Connection) {
                return (Connection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof Connection) {
                return (Connection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a Connection by its GUID, complete with all of its relationships.
     *
     * @param guid of the Connection to retrieve
     * @return the requested full Connection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist or the provided GUID is not a Connection
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static Connection retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a Connection by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the Connection to retrieve
     * @return the requested full Connection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist or the provided GUID is not a Connection
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static Connection retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a Connection by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Connection to retrieve
     * @return the requested full Connection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static Connection retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a Connection by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the Connection to retrieve
     * @return the requested full Connection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static Connection retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) Connection to active.
     *
     * @param qualifiedName for the Connection
     * @return true if the Connection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) Connection to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the Connection
     * @return true if the Connection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
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
    public static AtlanConnectorType getConnectorTypeFromQualifiedName(String[] tokens) {
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
        return creator(Atlan.getDefaultClient(), name, connectorType, adminRoles, adminGroups, adminUsers);
    }

    /**
     * Builds the minimal object necessary to create a connection.
     * Note: at least one of {@code #adminRoles}, {@code #adminGroups}, or {@code #adminUsers} must be
     * provided or an InvalidRequestException will be thrown.
     *
     * @param client connectivity to the Atlan tenant where the connection is intended to be created
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
            AtlanClient client,
            String name,
            AtlanConnectorType connectorType,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws AtlanException {
        boolean adminFound = false;
        ConnectionBuilder<?, ?> builder = Connection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(connectorType.getValue()))
                .category(connectorType.getCategory())
                .connectorType(connectorType);
        if (adminRoles != null && !adminRoles.isEmpty()) {
            for (String roleId : adminRoles) {
                client.getRoleCache().getNameForId(roleId);
            }
            adminFound = true;
            builder.adminRoles(adminRoles);
        } else {
            builder.nullField("adminRoles");
        }
        if (adminGroups != null && !adminGroups.isEmpty()) {
            for (String groupAlias : adminGroups) {
                client.getGroupCache().getIdForAlias(groupAlias);
            }
            adminFound = true;
            builder.adminGroups(adminGroups);
        } else {
            builder.nullField("adminGroups");
        }
        if (adminUsers != null && !adminUsers.isEmpty()) {
            for (String userName : adminUsers) {
                try {
                    client.getUserCache().getIdForName(userName);
                } catch (NotFoundException e) {
                    // If we cannot find the username, fallback to looking for an API token
                    ApiToken token = client.apiTokens.getById(userName);
                    if (token == null) {
                        // If that also turns up no results, re-throw the NotFoundException
                        throw e;
                    }
                }
            }
            adminFound = true;
            builder.adminUsers(adminUsers);
        } else {
            builder.nullField("adminUsers");
        }
        if (adminFound) {
            return builder;
        } else {
            throw new InvalidRequestException(ErrorCode.NO_CONNECTION_ADMIN);
        }
    }

    /**
     * If an asset with the same qualifiedName exists, updates the existing asset. Otherwise, creates the asset.
     * No Atlan tags or custom metadata will be changed if updating an existing asset, irrespective of what
     * is included in the asset itself when the method is called.
     *
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws NotFoundException if any of the provided connection admins do not actually exist
     * @deprecated see {@link #save()} instead
     */
    @Deprecated
    @Override
    public ConnectionCreationResponse upsert() throws AtlanException {
        return save(Atlan.getDefaultClient());
    }

    /**
     * If an asset with the same qualifiedName exists, updates the existing asset. Otherwise, creates the asset.
     * No Atlan tags or custom metadata will be changed if updating an existing asset, irrespective of what
     * is included in the asset itself when the method is called.
     *
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws NotFoundException if any of the provided connection admins do not actually exist
     */
    @Override
    public ConnectionCreationResponse save() throws AtlanException {
        return save(Atlan.getDefaultClient());
    }

    /**
     * If an asset with the same qualifiedName exists, updates the existing asset. Otherwise, creates the asset.
     * No Atlan tags or custom metadata will be changed if updating an existing asset, irrespective of what
     * is included in the asset itself when the method is called.
     *
     * @param client connectivity to the Atlan tenant where this connection should be saved
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws NotFoundException if any of the provided connection admins do not actually exist
     */
    @Override
    public ConnectionCreationResponse save(AtlanClient client) throws AtlanException {
        // Validate the provided connection admins prior to attempting to create
        // (the cache retrievals will throw errors directly if there are any)
        if (adminRoles != null && !adminRoles.isEmpty()) {
            for (String roleId : adminRoles) {
                client.getRoleCache().getNameForId(roleId);
            }
        }
        if (adminGroups != null && !adminGroups.isEmpty()) {
            for (String groupAlias : adminGroups) {
                client.getGroupCache().getIdForAlias(groupAlias);
            }
        }
        if (adminUsers != null && !adminUsers.isEmpty()) {
            for (String userName : adminUsers) {
                try {
                    client.getUserCache().getIdForName(userName);
                } catch (NotFoundException e) {
                    // If we cannot find the username, fallback to looking for an API token
                    ApiToken token = client.apiTokens.getById(userName);
                    if (token == null) {
                        // If that also turns up no results, re-throw the NotFoundException
                        throw e;
                    }
                }
            }
        }
        return client.assets.save(this, false);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save()} method.
     * If an asset does exist, optionally overwrites any Atlan tags. Custom metadata will always
     * be entirely ignored using this method.
     *
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws NotFoundException if any of the provided connection admins do not actually exist
     * @deprecated see {@link #save(boolean)} instead
     */
    @Deprecated
    @Override
    public ConnectionCreationResponse upsert(boolean replaceAtlanTags) throws AtlanException {
        return save(Atlan.getDefaultClient(), replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save()} method.
     * If an asset does exist, optionally overwrites any Atlan tags. Custom metadata will always
     * be entirely ignored using this method.
     *
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws NotFoundException if any of the provided connection admins do not actually exist
     */
    @Override
    public ConnectionCreationResponse save(boolean replaceAtlanTags) throws AtlanException {
        return save(Atlan.getDefaultClient(), replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save()} method.
     * If an asset does exist, optionally overwrites any Atlan tags. Custom metadata will always
     * be entirely ignored using this method.
     *
     * @param client connectivity to the Atlan tenant where this connection should be saved
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws NotFoundException if any of the provided connection admins do not actually exist
     */
    @Override
    public ConnectionCreationResponse save(AtlanClient client, boolean replaceAtlanTags) throws AtlanException {
        // Validate the provided connection admins prior to attempting to create
        // (the cache retrievals will throw errors directly if there are any)
        if (adminRoles != null && !adminRoles.isEmpty()) {
            for (String roleId : adminRoles) {
                client.getRoleCache().getNameForId(roleId);
            }
        }
        if (adminGroups != null && !adminGroups.isEmpty()) {
            for (String groupAlias : adminGroups) {
                client.getGroupCache().getIdForAlias(groupAlias);
            }
        }
        if (adminUsers != null && !adminUsers.isEmpty()) {
            for (String userName : adminUsers) {
                client.getUserCache().getIdForName(userName);
            }
        }
        return client.assets.save(this, replaceAtlanTags);
    }

    /**
     * Add the API token configured for the default client as an admin for this Connection.
     * This is necessary to allow the API token to manage policies for the connection.
     *
     * @param impersonationToken a bearer token for an actual user who is already an admin for the Connection, NOT an API token
     * @throws AtlanException on any error during API invocation
     */
    public AssetMutationResponse addApiTokenAsAdmin(final String impersonationToken) throws AtlanException {
        return Asset.addApiTokenAsAdmin(getGuid(), impersonationToken);
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
        return Connection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
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
     * Find a connection by its human-readable name and type. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the connection, if found.
     *
     * @param name of the connection
     * @param type of the connection
     * @return all connections with that name and type, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the connection does not exist
     */
    public static List<Connection> findByName(String name, AtlanConnectorType type) throws AtlanException {
        return findByName(name, type, (List<AtlanField>) null);
    }

    /**
     * Find a connection by its human-readable name and type.
     *
     * @param name of the connection
     * @param type of the connection
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the connection
     * @return all connections with that name and type, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the connection does not exist
     */
    public static List<Connection> findByName(String name, AtlanConnectorType type, Collection<String> attributes)
            throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, type, attributes);
    }

    /**
     * Find a connection by its human-readable name and type.
     *
     * @param name of the connection
     * @param type of the connection
     * @param attributes an optional collection of attributes (checked) to retrieve for the connection
     * @return all connections with that name and type, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the connection does not exist
     */
    public static List<Connection> findByName(String name, AtlanConnectorType type, List<AtlanField> attributes)
            throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, type, attributes);
    }

    /**
     * Find a connection by its human-readable name and type. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the connection, if found.
     *
     * @param client connectivity to the Atlan tenant in which to search for the connection
     * @param name of the connection
     * @param type of the connection
     * @return all connections with that name and type, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the connection does not exist
     */
    public static List<Connection> findByName(AtlanClient client, String name, AtlanConnectorType type)
            throws AtlanException {
        return findByName(client, name, type, (List<AtlanField>) null);
    }

    /**
     * Find a connection by its human-readable name and type.
     *
     * @param client connectivity to the Atlan tenant in which to search for the connection
     * @param name of the connection
     * @param type of the connection
     * @param attributes an optional collection of attributes to retrieve for the connection
     * @return all connections with that name and type, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the connection does not exist
     */
    public static List<Connection> findByName(
            AtlanClient client, String name, AtlanConnectorType type, Collection<String> attributes)
            throws AtlanException {
        List<Connection> results = new ArrayList<>();
        Connection.select(client)
                .where(Connection.NAME.eq(name))
                .where(Connection.CONNECTOR_TYPE.eq(type.getValue()))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof Connection)
                .forEach(c -> results.add((Connection) c));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.CONNECTION_NOT_FOUND_BY_NAME, name, type.getValue());
        }
        return results;
    }

    /**
     * Find a connection by its human-readable name and type.
     *
     * @param client connectivity to the Atlan tenant in which to search for the connection
     * @param name of the connection
     * @param type of the connection
     * @param attributes an optional collection of attributes (checked) to retrieve for the connection
     * @return all connections with that name and type, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the connection does not exist
     */
    public static List<Connection> findByName(
            AtlanClient client, String name, AtlanConnectorType type, List<AtlanField> attributes)
            throws AtlanException {
        List<Connection> results = new ArrayList<>();
        Connection.select(client)
                .where(Connection.NAME.eq(name))
                .where(Connection.CONNECTOR_TYPE.eq(type.getValue()))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof Connection)
                .forEach(c -> results.add((Connection) c));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.CONNECTION_NOT_FOUND_BY_NAME, name, type.getValue());
        }
        return results;
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
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a Connection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Connection
     * @param name of the Connection
     * @return the updated Connection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Connection removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Connection) Asset.removeDescription(client, updater(qualifiedName, name));
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
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a Connection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Connection
     * @param name of the Connection
     * @return the updated Connection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Connection removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Connection) Asset.removeUserDescription(client, updater(qualifiedName, name));
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
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a Connection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Connection's owners
     * @param qualifiedName of the Connection
     * @param name of the Connection
     * @return the updated Connection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Connection removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Connection) Asset.removeOwners(client, updater(qualifiedName, name));
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
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a Connection.
     *
     * @param client connectivity to the Atlan tenant on which to update the Connection's certificate
     * @param qualifiedName of the Connection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Connection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Connection updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (Connection)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
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
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a Connection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Connection's certificate
     * @param qualifiedName of the Connection
     * @param name of the Connection
     * @return the updated Connection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Connection removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Connection) Asset.removeCertificate(client, updater(qualifiedName, name));
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
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a Connection.
     *
     * @param client connectivity to the Atlan tenant on which to update the Connection's announcement
     * @param qualifiedName of the Connection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Connection updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (Connection)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
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
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a Connection.
     *
     * @param client connectivity to the Atlan client from which to remove the Connection's announcement
     * @param qualifiedName of the Connection
     * @param name of the Connection
     * @return the updated Connection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Connection removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Connection) Asset.removeAnnouncement(client, updater(qualifiedName, name));
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
    public static Connection replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the Connection.
     *
     * @param client connectivity to the Atlan tenant on which to replace the Connection's assigned terms
     * @param qualifiedName for the Connection
     * @param name human-readable name of the Connection
     * @param terms the list of terms to replace on the Connection, or null to remove all terms from the Connection
     * @return the Connection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Connection replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (Connection) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
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
    public static Connection appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the Connection, without replacing existing terms linked to the Connection.
     * Note: this operation must make two API calls — one to retrieve the Connection's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the Connection
     * @param qualifiedName for the Connection
     * @param terms the list of terms to append to the Connection
     * @return the Connection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Connection appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Connection) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
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
    public static Connection removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a Connection, without replacing all existing terms linked to the Connection.
     * Note: this operation must make two API calls — one to retrieve the Connection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the Connection
     * @param qualifiedName for the Connection
     * @param terms the list of terms to remove from the Connection, which must be referenced by GUID
     * @return the Connection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Connection removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Connection) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a Connection, without replacing existing Atlan tags linked to the Connection.
     * Note: this operation must make two API calls — one to retrieve the Connection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Connection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Connection
     */
    public static Connection appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Connection, without replacing existing Atlan tags linked to the Connection.
     * Note: this operation must make two API calls — one to retrieve the Connection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Connection
     * @param qualifiedName of the Connection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Connection
     */
    public static Connection appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Connection) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Connection, without replacing existing Atlan tags linked to the Connection.
     * Note: this operation must make two API calls — one to retrieve the Connection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Connection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Connection
     */
    public static Connection appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Connection, without replacing existing Atlan tags linked to the Connection.
     * Note: this operation must make two API calls — one to retrieve the Connection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Connection
     * @param qualifiedName of the Connection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Connection
     */
    public static Connection appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Connection) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Connection.
     *
     * @param qualifiedName of the Connection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Connection
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Connection.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the Connection
     * @param qualifiedName of the Connection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Connection
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Connection.
     *
     * @param qualifiedName of the Connection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Connection
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
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Connection.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the Connection
     * @param qualifiedName of the Connection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Connection
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a Connection.
     *
     * @param qualifiedName of the Connection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Connection
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a Connection.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Connection
     * @param qualifiedName of the Connection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Connection
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
