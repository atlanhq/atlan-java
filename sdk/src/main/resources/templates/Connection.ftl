<#macro all>
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
     * Builds the minimal object necessary to create a connection, using "All Admins" as the default
     * set of connection admins.
     *
     * @param client connectivity to the Atlan tenant where the connection is intended to be created
     * @param name of the connection
     * @param connectorType type of the connection's connector (this determines what logo appears for the assets)
     * @return the minimal object necessary to create the connection, as a builder
     * @throws AtlanException on any error related to the request, such as an inability to retrieve the existing admins in the system
     */
    public static ConnectionBuilder<?, ?> creator(AtlanClient client, String name, AtlanConnectorType connectorType) throws AtlanException {
        return creator(client, name, connectorType, List.of(client.getRoleCache().getIdForName("$admin")), null, null);
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
                client.getRoleCache().getNameForId(roleId);
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
                client.getRoleCache().getNameForId(roleId);
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
    public AsyncCreationResponse save(AtlanClient client, boolean replaceAtlanTags)
            throws AtlanException {
        // Validate the provided connection admins prior to attempting to create
        // (the cache retrievals will throw errors directly if there are any)
        if (adminRoles != null && !adminRoles.isEmpty()) {
            for (String roleId : adminRoles) {
                client.getRoleCache().getNameForId(roleId);
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
    public AssetMutationResponse addApiTokenAsAdmin(AtlanClient client, final String impersonationToken) throws AtlanException {
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
    public static List<Connection> findByName(AtlanClient client, String name, AtlanConnectorType type) throws AtlanException {
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
    public static List<Connection> findByName(AtlanClient client, String name, AtlanConnectorType type, Collection<String> attributes)
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
    public static List<Connection> findByName(AtlanClient client, String name, AtlanConnectorType type, List<AtlanField> attributes)
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
     * Retrieve the qualifiedNames of all connections that exist in Atlan.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the qualifiedNames
     * @return list of all connection qualifiedNames
     * @throws AtlanException on any API problems
     */
    public static List<String> getAllQualifiedNames(AtlanClient client) throws AtlanException {
        return Connection.select(client)
            .includeOnResults(Connection.QUALIFIED_NAME)
            .pageSize(50)
            .stream()
            .map(Asset::getQualifiedName)
            .collect(Collectors.toList());
    }
</#macro>
