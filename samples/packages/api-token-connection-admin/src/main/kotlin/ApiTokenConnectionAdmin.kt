/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.exception.AtlanException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.core.AssetMutationResponse
import com.atlan.pkg.Utils
import mu.KotlinLogging
import kotlin.system.exitProcess

object ApiTokenConnectionAdmin {
    private val logger = KotlinLogging.logger {}

    /**
     * Actually run the logic to add the API token as a connection admin.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val config = Utils.setPackageOps<APITokenConnectionAdminCfg>()

        val connectionQN = Utils.reuseConnection(config.connectionQualifiedName?.let { it[0] })
        val apiTokenGuid = Utils.getOrDefault(config.apiTokenGuid, "")

        if (connectionQN == "" || apiTokenGuid == "") {
            logger.error { "Missing required parameter - you must provide BOTH a connection and the name of an API token." }
            exitProcess(4)
        }

        val apiTokenId = getIdForToken(apiTokenGuid)
        val connection = getConnectionWithAdmins(connectionQN)
        addTokenAsConnectionAdmin(connection, apiTokenId)
    }

    /**
     * Retrieve the API token's pseudo-username, that can be used anywhere a username can be used.
     *
     * @param apiTokenGuid name of the API token for which to fetch the pseudo-username
     * @return the pseudo-username of the API token
     */
    fun getIdForToken(apiTokenGuid: String): String {
        logger.info { "Looking up API token: $apiTokenGuid" }
        val token = Atlan.getDefaultClient().apiTokens.getByGuid(apiTokenGuid)
        if (token == null) {
            logger.error { "Unable to find any API token with the GUID: $apiTokenGuid" }
            exitProcess(5)
        }
        return "service-account-${token.clientId}"
    }

    /**
     * Retrieve the connection with its existing admins.
     *
     * @param connectionQN qualifiedName of the connection
     * @return the connection with its existing admins
     */
    fun getConnectionWithAdmins(connectionQN: String): Asset {
        logger.info { "Looking up connection details: $connectionQN" }
        val found = Connection.select()
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
     * @param connection the connection to add the API token to, with its existing admin users present
     * @param apiToken the API token to append as a connection admin
     */
    fun addTokenAsConnectionAdmin(connection: Asset, apiToken: String) {
        logger.info { "Adding API token $apiToken as connection admin for: ${connection.qualifiedName}" }
        val existingAdmins = connection.adminUsers
        try {
            val response = connection.trimToRequired()
                .adminUsers(existingAdmins)
                .adminUser(apiToken)
                .build()
                .save()
            when (val result = response?.getMutation(connection)) {
                AssetMutationResponse.MutationType.UPDATED -> logger.info { " ... successfully updated the connection with API token as a new admin." }
                AssetMutationResponse.MutationType.NOOP -> logger.info { " ... API token is already an admin on the connection - no changes made." }
                AssetMutationResponse.MutationType.CREATED -> logger.error { " ... somehow created the connection - that should not have happened." }
                AssetMutationResponse.MutationType.DELETED -> logger.error { " ... somehow deleted the connection - that should not have happened." }
                else -> {
                    logger.warn { "Unexpected connection change result: $result" }
                }
            }
        } catch (e: AtlanException) {
            logger.error("Unable to add the API token as a connection admin.", e)
        }
    }
}
