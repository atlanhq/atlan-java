/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.Atlan
import com.atlan.exception.ApiException
import com.atlan.exception.AtlanException
import com.atlan.exception.ErrorCode
import com.atlan.exception.NotFoundException
import com.atlan.exception.PermissionException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.core.AtlanAsyncMutator.MAX_ASYNC_RETRIES
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.net.RequestOptions
import com.atlan.pkg.serde.cell.ConnectionXformer
import com.atlan.pkg.util.AssetResolver
import mu.KotlinLogging

object ConnectionCache : AssetCache<Connection>() {
    private val logger = KotlinLogging.logger {}

    private val includesOnResults: List<AtlanField> = listOf(Connection.NAME, Connection.CONNECTOR_TYPE, Connection.STATUS)

    private val EXEMPLAR_CONNECTION =
        Connection.creator("Sample Connection", AtlanConnectorType.SNOWFLAKE)
            .description("Could be empty")
            .build()

    /** {@inheritDoc} */
    override fun lookupByName(name: String?) {
        val result = lookupByIdentity(name)
        if (result != null) cache(result.guid, name, result)
    }

    /** {@inheritDoc}  */
    private fun lookupByIdentity(identity: String?): Connection? {
        val tokens = identity?.split(ConnectionXformer.CONNECTION_DELIMITER)
        if (tokens?.size == 2) {
            val name = tokens[0]
            val type = tokens[1]
            try {
                val found = Connection.findByName(name, AtlanConnectorType.fromValue(type), includesOnResults)
                return found[0]
            } catch (e: NotFoundException) {
                logger.warn { "Unable to find connection: $identity" }
                logger.debug(e) { "Full stack trace:" }
            } catch (e: AtlanException) {
                logger.warn { "Unable to lookup or find connection: $identity" }
                logger.debug(e) { "Full stack trace:" }
            }
        } else {
            logger.warn { "Unable to lookup or find connection, unexpected reference: $identity" }
        }
        identity?.let { addToIgnore(identity) }
        return null
    }

    /** {@inheritDoc} */
    override fun lookupById(id: String?) {
        val result = lookupById(id, 0, Atlan.getDefaultClient().maxNetworkRetries)
        if (result != null) cache(result.guid, getIdentityForAsset(result), result)
    }

    /** {@inheritDoc}  */
    private fun lookupById(
        guid: String?,
        currentAttempt: Int,
        maxRetries: Int,
    ): Connection? {
        try {
            val connection =
                Connection.select()
                    .where(Connection.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .pageSize(1)
                    .stream()
                    .findFirst()
            if (connection.isPresent) {
                return isAccessible(connection.get())
            } else {
                if (currentAttempt >= maxRetries) {
                    logger.warn { "No connection found with GUID: $guid" }
                } else {
                    Thread.sleep(HttpClient.waitTime(currentAttempt).toMillis())
                    return lookupById(guid, currentAttempt + 1, maxRetries)
                }
            }
        } catch (e: AtlanException) {
            logger.warn { "Unable to lookup or find connection: $guid" }
            logger.debug(e) { "Full stack trace:" }
        }
        guid?.let { addToIgnore(guid) }
        return null
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: Connection): String {
        return if (asset.connectorType == null) "" else getIdentityForAsset(asset.name, asset.connectorType)
    }

    /**
     * Build a connection identity from its component parts.
     *
     * @param name of the connection
     * @param type of the connector for the connection (as a valid connector type)
     * @return identity for the connection
     */
    fun getIdentityForAsset(
        name: String,
        type: AtlanConnectorType,
    ): String {
        return ConnectionXformer.encode(name, type.value)
    }

    /**
     * Get a map of all connections in the cache, indexed by their identity with values
     * giving the resolved qualifiedName for the connection.
     *
     * @return map of all connections, indexed by their identity
     */
    fun getIdentityMap(): Map<AssetResolver.ConnectionIdentity, String> {
        val map = mutableMapOf<AssetResolver.ConnectionIdentity, String>()
        listAll().forEach { (_, connection) ->
            val connectorType =
                if (connection.connectorType == null) {
                    "(not enumerated)"
                } else {
                    connection.connectorType.value
                }
            map[AssetResolver.ConnectionIdentity(connection.name, connectorType)] = connection.qualifiedName
        }
        return map
    }

    /** {@inheritDoc} */
    override fun refreshCache() {
        val request =
            Connection.select()
                .includesOnResults(includesOnResults)
                .pageSize(1)
                .toRequest()
        val response = request.search()
        logger.info { "Caching all ${response.approximateCount ?: 0} connections, up-front..." }
        initializeOffHeap("connection", response, EXEMPLAR_CONNECTION)
        Connection.select()
            .includesOnResults(includesOnResults)
            .stream(true)
            .forEach { connection ->
                connection as Connection
                cache(connection.guid, getIdentityForAsset(connection), connection)
            }
    }

    /**
     * Uniquely for connections, we need to ensure they are accessible before
     * caching them, as any other operation that interacts with them will need more
     * than the search to succeed to do anything with them.
     *
     * @param connection the result from a search
     * @return the accessible connection, in full
     */
    private fun isAccessible(connection: Asset): Connection {
        try {
            val candidate =
                Atlan.getDefaultClient().assets.get(
                    connection.guid,
                    false,
                    false,
                    RequestOptions.from(Atlan.getDefaultClient())
                        .maxNetworkRetries(MAX_ASYNC_RETRIES)
                        .build(),
                )
            if (candidate?.asset == null) {
                // Since the retry logic in this case is actually embedded in the retrieveMinimal
                // call, if we get to this point without retrieving the connection we have by
                // definition overrun the retry limit
                throw ApiException(ErrorCode.RETRY_OVERRUN, null)
            }
            return candidate.asset as Connection
        } catch (e: PermissionException) {
            // If we get a permission exception after the built-in retries above, throw it
            // onwards as a retry overrun
            throw ApiException(ErrorCode.RETRY_OVERRUN, e)
        }
    }
}
