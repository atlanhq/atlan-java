/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AsyncCreationResponse;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.ConnectionDQEnvironmentSetupStatus;
import com.atlan.model.enums.QueryUsernameStrategy;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a connection to a data source in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class Connection extends Asset implements IConnection, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Connection";

    /** Fixed typeName for Connections. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Whether using this connection to run queries on the source is allowed (true) or not (false). */
    @Attribute
    Boolean allowQuery;

    /** Whether using this connection to run preview queries on the source is allowed (true) or not (false). */
    @Attribute
    Boolean allowQueryPreview;

    /** Type of connection, for example WAREHOUSE, RDBMS, etc. */
    @Attribute
    AtlanConnectionCategory category;

    /** Unique identifier (GUID) for the data quality credentials to use for this connection. */
    @Attribute
    String connectionDQCredentialGuid;

    /** Error message if data quality environment setup failed for this connection. */
    @Attribute
    String connectionDQEnvironmentSetupErrorMessage;

    /** Status of the data quality environment setup for this connection. */
    @Attribute
    ConnectionDQEnvironmentSetupStatus connectionDQEnvironmentSetupStatus;

    /** Timestamp when the data quality environment setup status was last updated. */
    @Attribute
    @Date
    Long connectionDQEnvironmentSetupStatusUpdatedAt;

    /** Name of the database in the source environment for data quality. */
    @Attribute
    String connectionDQEnvironmentSourceDatabaseName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> connectionDbtEnvironments;

    /** Whether data quality is enabled for this connection (true) or not (false). */
    @Attribute
    Boolean connectionIsDQEnabled;

    /** Unique identifier (GUID) for the SSO credentials to use for this connection. */
    @Attribute
    String connectionSSOCredentialGuid;

    /** Unused. Only the value of connectorType impacts icons. */
    @Attribute
    String connectorIcon;

    /** Unused. Only the value of connectorType impacts icons. */
    @Attribute
    String connectorImage;

    /** Credential strategy to use for this connection for queries. */
    @Attribute
    String credentialStrategy;

    /** Unique identifier (GUID) for the default credentials to use for this connection. */
    @Attribute
    String defaultCredentialGuid;

    /** Whether this connection has popularity insights (true) or not (false). */
    @Attribute
    Boolean hasPopularityInsights;

    /** Host name of this connection's source. */
    @Attribute
    String host;

    /** Connection process to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<IConnectionProcess> inputToConnectionProcesses;

    /** Whether sample data can be previewed for this connection (true) or not (false). */
    @Attribute
    Boolean isSampleDataPreviewEnabled;

    /** Number of rows after which results should be uploaded to storage. */
    @Attribute
    Long objectStorageUploadThreshold;

    /** Connection processs from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<IConnectionProcess> outputFromConnectionProcesses;

    /** Policy strategy is a configuration that determines whether the Atlan policy will be applied to the results of insight queries and whether the query will be rewritten, applicable for stream api call made from insight screen */
    @Attribute
    String policyStrategy;

    /** Policy strategy is a configuration that determines whether the Atlan policy will be applied to the results of insight queries and whether the query will be rewritten. policyStrategyForSamplePreview config is applicable for sample preview call from assets screen */
    @Attribute
    String policyStrategyForSamplePreview;

    /** Number of days over which popularity is calculated, for example 30 days. */
    @Attribute
    Long popularityInsightsTimeframe;

    /** Port number to this connection's source. */
    @Attribute
    Integer port;

    /** Credential strategy to use for this connection for preview queries. */
    @Attribute
    String previewCredentialStrategy;

    /** Query config for this connection. */
    @Attribute
    String queryConfig;

    /** Configuration for preview queries. */
    @Attribute
    @Singular("putQueryPreviewConfig")
    Map<String, String> queryPreviewConfig;

    /** Maximum time a query should be allowed to run before timing out. */
    @Attribute
    Long queryTimeout;

    /** Username strategy to use for this connection for queries. */
    @Attribute
    QueryUsernameStrategy queryUsernameStrategy;

    /** Maximum number of rows that can be returned for the source. */
    @Attribute
    Long rowLimit;

    /** Unused. Only the value of connectorType impacts icons. */
    @Attribute
    String sourceLogo;

    /** Subcategory of this connection. */
    @Attribute
    String subCategory;

    /** Whether to upload to S3, GCP, or another storage location (true) or not (false). */
    @Attribute
    Boolean useObjectStorage;

    /** TBC */
    @Attribute
    Boolean vectorEmbeddingsEnabled;

    /** TBC */
    @Attribute
    @Date
    Long vectorEmbeddingsUpdatedAt;

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
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Connections will be included
     * @return a fluent search that includes all Connection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(Asset.TYPE_NAME.eq(TYPE_NAME));
        if (!includeArchived) {
            builder.active();
        }
        return builder;
    }

    /**
     * Reference to a Connection by GUID. Use this to create a relationship to this Connection,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the Connection to reference
     * @return reference to a Connection that can be used for defining a relationship to a Connection
     */
    public static Connection refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Connection by GUID. Use this to create a relationship to this Connection,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the Connection to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Connection that can be used for defining a relationship to a Connection
     */
    public static Connection refByGuid(String guid, Reference.SaveSemantic semantic) {
        return Connection._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a Connection by qualifiedName. Use this to create a relationship to this Connection,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the Connection to reference
     * @return reference to a Connection that can be used for defining a relationship to a Connection
     */
    public static Connection refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Connection by qualifiedName. Use this to create a relationship to this Connection,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the Connection to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Connection that can be used for defining a relationship to a Connection
     */
    public static Connection refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return Connection._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
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
        return get(client, id, false);
    }

    /**
     * Retrieves a Connection by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Connection to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Connection, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist or the provided GUID is not a Connection
     */
    @JsonIgnore
    public static Connection get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Connection) {
                return (Connection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof Connection) {
                return (Connection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a Connection by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Connection to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Connection, including any relationships
     * @return the requested Connection, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist or the provided GUID is not a Connection
     */
    @JsonIgnore
    public static Connection get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a Connection by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Connection to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Connection, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the Connection
     * @return the requested Connection, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Connection does not exist or the provided GUID is not a Connection
     */
    @JsonIgnore
    public static Connection get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = Connection.select(client)
                    .where(Connection.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof Connection) {
                return (Connection) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = Connection.select(client)
                    .where(Connection.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof Connection) {
                return (Connection) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
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
     * Determine the connector type from the provided qualifiedName.
     *
     * @param qualifiedName of the connection
     * @return the connector type, or null if the qualifiedName is not for a connected asset
     */
    public static String getConnectorFromQualifiedName(String qualifiedName) {
        String[] tokens = qualifiedName.split("/");
        AtlanConnectorType ct = getConnectorTypeFromQualifiedName(tokens);
        if (ct == null || ct == AtlanConnectorType.UNKNOWN_CUSTOM) {
            return getConnectorFromQualifiedName(tokens);
        }
        return ct.getValue();
    }

    /**
     * Determine the connector type from the provided qualifiedName.
     *
     * @param tokens of the qualifiedName, from which to determine the connector type
     * @return the connector type, or null if the qualifiedName is not for a connected asset
     */
    public static String getConnectorFromQualifiedName(String[] tokens) {
        if (tokens.length > 1) {
            return tokens[1].toLowerCase();
        }
        return null;
    }

    /**
     * Builds the minimal object necessary to create a connection, using "All Admins" as the default
     * set of connection admins.
     *
     * @param client connectivity to the Atlan tenant where the connection is intended to be created
     * @param name of the connection
     * @param connectorType type of the connection's connector (this determines what logo appears for the assets)
     * @return the minimal object necessary to create the connection, as a builder
     * @throws AtlanException on any error related to the request, such as an inability to retrieve the existing admins in the system
     */
    public static ConnectionBuilder<?, ?> creator(AtlanClient client, String name, AtlanConnectorType connectorType)
            throws AtlanException {
        return creator(
                client, name, connectorType, List.of(client.getRoleCache().getIdForSid("$admin")), null, null);
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
     * @param adminGroups the (internal) names of the groups that can administer this connection
     * @param adminUsers the (internal) names of the users that can administer this connection
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
                client.getRoleCache().getNameForSid(roleId);
            }
            adminFound = true;
            builder.adminRoles(adminRoles);
        } else {
            builder.nullField("adminRoles");
        }
        if (adminGroups != null && !adminGroups.isEmpty()) {
            for (String groupAlias : adminGroups) {
                client.getGroupCache().getIdForName(groupAlias);
            }
            adminFound = true;
            builder.adminGroups(adminGroups);
        } else {
            builder.nullField("adminGroups");
        }
        if (adminUsers != null && !adminUsers.isEmpty()) {
            for (String userName : adminUsers) {
                client.getUserCache().getIdForName(userName);
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
     * Builds the minimal object necessary to create a connection, using "All Admins" as the default
     * set of connection admins.
     *
     * @param client connectivity to the Atlan tenant where the connection is intended to be created
     * @param name of the connection
     * @param connectorName name of the connection's connector (this determines what logo appears for the assets)
     * @param category category of the connection
     * @return the minimal object necessary to create the connection, as a builder
     * @throws AtlanException on any error related to the request, such as an inability to retrieve the existing admins in the system
     */
    public static ConnectionBuilder<?, ?> creator(
            AtlanClient client, String name, String connectorName, AtlanConnectionCategory category)
            throws AtlanException {
        return creator(
                client,
                name,
                connectorName,
                category,
                List.of(client.getRoleCache().getIdForSid("$admin")),
                null,
                null);
    }

    /**
     * Builds the minimal object necessary to create a connection.
     * Note: at least one of {@code #adminRoles}, {@code #adminGroups}, or {@code #adminUsers} must be
     * provided or an InvalidRequestException will be thrown.
     *
     * @param client connectivity to the Atlan tenant where the connection is intended to be created
     * @param name of the connection
     * @param connectorName name of the connection's connector (this determines what logo appears for the assets)
     * @param category category of the connection
     * @param adminRoles the GUIDs of the roles that can administer this connection
     * @param adminGroups the (internal) names of the groups that can administer this connection
     * @param adminUsers the (internal) names of the users that can administer this connection
     * @return the minimal object necessary to create the connection, as a builder
     * @throws InvalidRequestException if no admin has been defined for the connection, or an invalid admin has been defined
     * @throws NotFoundException if a non-existent admin has been defined for the connection
     * @throws AtlanException on any other error related to the request, such as an inability to retrieve the existing admins in the system
     */
    public static ConnectionBuilder<?, ?> creator(
            AtlanClient client,
            String name,
            String connectorName,
            AtlanConnectionCategory category,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws AtlanException {
        boolean adminFound = false;
        ConnectionBuilder<?, ?> builder = Connection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(connectorName))
                .category(category)
                .customConnectorType(connectorName);
        if (adminRoles != null && !adminRoles.isEmpty()) {
            for (String roleId : adminRoles) {
                client.getRoleCache().getNameForSid(roleId);
            }
            adminFound = true;
            builder.adminRoles(adminRoles);
        } else {
            builder.nullField("adminRoles");
        }
        if (adminGroups != null && !adminGroups.isEmpty()) {
            for (String groupAlias : adminGroups) {
                client.getGroupCache().getIdForName(groupAlias);
            }
            adminFound = true;
            builder.adminGroups(adminGroups);
        } else {
            builder.nullField("adminGroups");
        }
        if (adminUsers != null && !adminUsers.isEmpty()) {
            for (String userName : adminUsers) {
                client.getUserCache().getIdForName(userName);
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
     * @param client connectivity to the Atlan tenant where this connection should be saved
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws NotFoundException if any of the provided connection admins do not actually exist
     */
    @Override
    public AsyncCreationResponse save(AtlanClient client) throws AtlanException {
        // Validate the provided connection admins prior to attempting to create
        // (the cache retrievals will throw errors directly if there are any)
        if (adminRoles != null && !adminRoles.isEmpty()) {
            for (String roleId : adminRoles) {
                client.getRoleCache().getNameForSid(roleId);
            }
        }
        if (adminGroups != null && !adminGroups.isEmpty()) {
            for (String groupAlias : adminGroups) {
                client.getGroupCache().getIdForName(groupAlias);
            }
        }
        if (adminUsers != null && !adminUsers.isEmpty()) {
            for (String userName : adminUsers) {
                client.getUserCache().getIdForName(userName);
            }
        }
        return client.assets.save(this);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save(AtlanClient)} method.
     * If an asset does exist, optionally overwrites any Atlan tags. Custom metadata will always
     * be entirely ignored using this method.
     *
     * @param client connectivity to the Atlan tenant where this connection should be saved
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws NotFoundException if any of the provided connection admins do not actually exist
     * @deprecated see {@link #save(AtlanClient)}
     */
    @Deprecated
    @Override
    public AsyncCreationResponse save(AtlanClient client, boolean replaceAtlanTags) throws AtlanException {
        // Validate the provided connection admins prior to attempting to create
        // (the cache retrievals will throw errors directly if there are any)
        if (adminRoles != null && !adminRoles.isEmpty()) {
            for (String roleId : adminRoles) {
                client.getRoleCache().getNameForSid(roleId);
            }
        }
        if (adminGroups != null && !adminGroups.isEmpty()) {
            for (String groupAlias : adminGroups) {
                client.getGroupCache().getIdForName(groupAlias);
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
     * @param client connectivity to the Atlan tenant
     * @param impersonationToken a bearer token for an actual user who is already an admin for the Connection, NOT an API token
     * @throws AtlanException on any error during API invocation
     */
    public AssetMutationResponse addApiTokenAsAdmin(AtlanClient client, final String impersonationToken)
            throws AtlanException {
        return Asset.addApiTokenAsAdmin(client, getGuid(), impersonationToken);
    }

    /**
     * Generate a unique connection name.
     *
     * @param connectorType the name of the type of the connection's connector
     * @return a unique name for the connection
     */
    public static synchronized String generateQualifiedName(AtlanConnectorType connectorType) {
        return generateQualifiedName(connectorType.getValue());
    }

    /**
     * Generate a unique connection name.
     *
     * @param connectorType the name of the type of the connection's connector
     * @return a unique name for the connection
     */
    private static synchronized String generateQualifiedName(String connectorType) {
        long now = System.currentTimeMillis() / 1000;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.warn("Connection qualifiedName construction interrupted - exclusivity cannot be guaranteed.", e);
        }
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
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
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
        return findByName(client, name, type == null ? "" : type.getValue(), attributes);
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
        return findByName(client, name, type == null ? "" : type.getValue(), attributes);
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
    public static List<Connection> findByName(AtlanClient client, String name, String type) throws AtlanException {
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
            AtlanClient client, String name, String type, Collection<String> attributes) throws AtlanException {
        List<Connection> results = new ArrayList<>();
        Connection.select(client)
                .where(Connection.NAME.eq(name))
                .where(Connection.CONNECTOR_NAME.eq(type))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof Connection)
                .forEach(c -> results.add((Connection) c));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.CONNECTION_NOT_FOUND_BY_NAME, name, type);
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
    public static List<Connection> findByName(AtlanClient client, String name, String type, List<AtlanField> attributes)
            throws AtlanException {
        List<Connection> results = new ArrayList<>();
        Connection.select(client)
                .where(Connection.NAME.eq(name))
                .where(Connection.CONNECTOR_NAME.eq(type))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof Connection)
                .forEach(c -> results.add((Connection) c));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.CONNECTION_NOT_FOUND_BY_NAME, name, type);
        }
        return results;
    }

    /**
     * Retrieve the qualifiedNames of all connections that exist in Atlan.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the qualifiedNames
     * @return list of all connection qualifiedNames
     * @throws AtlanException on any API problems
     */
    public static List<String> getAllQualifiedNames(AtlanClient client) throws AtlanException {
        return Connection.select(client).includeOnResults(Connection.QUALIFIED_NAME).pageSize(50).stream()
                .map(Asset::getQualifiedName)
                .collect(Collectors.toList());
    }

    public abstract static class ConnectionBuilder<C extends Connection, B extends ConnectionBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

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
     * @param client connectivity to the Atlan tenant on which to append terms to the Connection
     * @param qualifiedName for the Connection
     * @param terms the list of terms to append to the Connection
     * @return the Connection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static Connection appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Connection) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
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
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static Connection removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Connection) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
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
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static Connection appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Connection) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
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
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
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
     * Remove an Atlan tag from a Connection.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Connection
     * @param qualifiedName of the Connection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Connection
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
