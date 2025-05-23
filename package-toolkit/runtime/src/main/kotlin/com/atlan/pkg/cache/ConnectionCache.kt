/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.exception.ApiException
import com.atlan.exception.AtlanException
import com.atlan.exception.ErrorCode
import com.atlan.exception.NotFoundException
import com.atlan.exception.PermissionException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.core.AtlanAsyncMutator.MAX_ASYNC_RETRIES
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanStatus
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.net.RequestOptions
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.cell.ConnectionXformer
import com.atlan.pkg.util.AssetResolver

class ConnectionCache(
    val ctx: PackageContext<*>,
) : AssetCache<Connection>(ctx, "connection") {
    private val logger = Utils.getLogger(this.javaClass.name)

    private val includesOnResults: List<AtlanField> = listOf(Connection.NAME, Connection.CONNECTOR_TYPE, Connection.STATUS)

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
                val found = Connection.findByName(client, name, type, includesOnResults)
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
        val result = lookupById(id, 0, ctx.client.maxNetworkRetries)
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
                Connection
                    .select(client)
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
    override fun getIdentityForAsset(asset: Connection): String = getIdentityForAsset(asset.name, asset.connectorName ?: Connection.getConnectorFromQualifiedName(asset.qualifiedName))

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
    ): String = getIdentityForAsset(name, type.value)

    /**
     * Build a connection identity from its component parts.
     *
     * @param name of the connection
     * @param type of the connector for the connection (as a valid connector type)
     * @return identity for the connection
     */
    fun getIdentityForAsset(
        name: String,
        type: String,
    ): String = if (type.isBlank()) "" else ConnectionXformer.encode(name, type)

    /**
     * Get a map of all connections in the cache, indexed by their identity with values
     * giving the resolved qualifiedName for the connection.
     *
     * @return map of all connections, indexed by their identity
     */
    fun getIdentityMap(): Map<AssetResolver.ConnectionIdentity, String> {
        val map = mutableMapOf<AssetResolver.ConnectionIdentity, String>()
        listAll().forEach { (_, connection) ->
            map[AssetResolver.ConnectionIdentity(connection.name, connection.connectorName ?: Connection.getConnectorFromQualifiedName(connection.qualifiedName))] = connection.qualifiedName
        }
        return map
    }

    /**
     * Inject a connection into the cache -- should only be used for testing purposes.
     *
     * @param name of the connection
     * @param type of the connection's connector
     * @param qualifiedName resolved qualifiedName of the connection
     */
    fun inject(
        name: String,
        type: String,
        qualifiedName: String,
    ) {
        val connectionId = getIdentityForAsset(name, type)
        val injected =
            Connection
                ._internal()
                .guid("-1")
                .qualifiedName(qualifiedName)
                .name(name)
                .connectorName(type)
                .status(AtlanStatus.ACTIVE)
                .build()
        cache("-1", connectionId, injected)
    }

    /** {@inheritDoc} */
    override fun refreshCache() {
        val count = Connection.select(client).count()
        logger.info { "Caching all $count connections, up-front..." }
        Connection
            .select(client)
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
                ctx.client.assets.get(
                    connection.guid,
                    false,
                    false,
                    RequestOptions
                        .from(ctx.client)
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
