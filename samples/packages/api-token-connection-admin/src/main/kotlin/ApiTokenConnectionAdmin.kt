/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.AtlanClient
import com.atlan.exception.AtlanException
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.core.AssetMutationResponse
import com.atlan.pkg.Utils
import kotlin.system.exitProcess

object ApiTokenConnectionAdmin {
    private val logger = Utils.getLogger(ApiTokenConnectionAdmin.javaClass.name)

    /**
     * Actually run the logic to add the API token as a connection admin.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        Utils.initializeContext<APITokenConnectionAdminCfg>().use { ctx ->
            val connectionQN = Utils.reuseConnection(ctx.client, ctx.config.connectionQualifiedName?.let { it[0] })

            if (connectionQN.isBlank() || ctx.config.apiTokenGuid.isBlank()) {
                logger.error { "Missing required parameter - you must provide BOTH a connection and the name of an API token." }
                exitProcess(4)
            }

            val apiTokenId = getIdForToken(ctx.client, ctx.config.apiTokenGuid)
            val connection = getConnectionWithAdmins(ctx.client, connectionQN)
            addTokenAsConnectionAdmin(ctx.client, connection, apiTokenId)
        }
    }

    /**
     * Retrieve the API token's pseudo-username, that can be used anywhere a username can be used.
     *
     * @param client connectivity to the Atlan tenant
     * @param apiTokenGuid name of the API token for which to fetch the pseudo-username
     * @return the pseudo-username of the API token
     */
    fun getIdForToken(
        client: AtlanClient,
        apiTokenGuid: String,
    ): String {
        logger.info { "Looking up API token: $apiTokenGuid" }
        val token = client.apiTokens.getByGuid(apiTokenGuid)
        if (token == null) {
            logger.error { "Unable to find any API token with the GUID: $apiTokenGuid" }
            exitProcess(5)
        }
        return "service-account-${token.clientId}"
    }

    /**
     * Retrieve the connection with its existing admins.
     *
     * @param client connectivity to the Atlan tenant
     * @param connectionQN qualifiedName of the connection
     * @return the connection with its existing admins
     */
    fun getConnectionWithAdmins(
        client: AtlanClient,
        connectionQN: String,
    ): Asset {
        logger.info { "Looking up connection details: $connectionQN" }
        val found =
            Connection
                .select(client)
                .where(Connection.QUALIFIED_NAME.eq(connectionQN))
                .includeOnResults(Connection.ADMIN_USERS)
                .stream()
                .findFirst()
        if (found.isEmpty) {
            logger.error { "Unable to find the specified connection: $connectionQN" }
            exitProcess(6)
        }
        return found.get()
    }

    /**
     * Actually add the token as a connection admin, appending it to any pre-existing
     * connection admins (rather than replacing).
     *
     * @param client connectivity to the Atlan tenant
     * @param connection the connection to add the API token to, with its existing admin users present
     * @param apiToken the API token to append as a connection admin
     */
    fun addTokenAsConnectionAdmin(
        client: AtlanClient,
        connection: Asset,
        apiToken: String,
    ) {
        logger.info { "Adding API token $apiToken as connection admin for: ${connection.qualifiedName}" }
        val existingAdmins = connection.adminUsers
        val stillValidAdmins = mutableListOf<String>()
        existingAdmins.forEach {
            try {
                client.userCache.getIdForName(it)
                stillValidAdmins.add(it)
            } catch (e: NotFoundException) {
                logger.warn { "Removing existing connection admin as they no longer exist: $it" }
            }
        }
        try {
            val response =
                connection
                    .trimToRequired()
                    .adminUsers(stillValidAdmins)
                    .adminUser(apiToken)
                    .build()
                    .save(client)
            when (val result = response?.getMutation(connection)) {
                AssetMutationResponse.MutationType.UPDATED -> {
                    logger.info { " ... successfully updated the connection with API token as a new admin." }
                }

                AssetMutationResponse.MutationType.NOOP -> {
                    logger.info { " ... API token is already an admin on the connection - no changes made." }
                }

                AssetMutationResponse.MutationType.CREATED -> {
                    logger.error { " ... somehow created the connection - that should not have happened." }
                }

                AssetMutationResponse.MutationType.DELETED -> {
                    logger.error { " ... somehow deleted the connection - that should not have happened." }
                }

                else -> {
                    logger.warn { "Unexpected connection change result: $result" }
                }
            }
        } catch (e: AtlanException) {
            logger.error("Unable to add the API token as a connection admin.", e)
        }
    }
}
