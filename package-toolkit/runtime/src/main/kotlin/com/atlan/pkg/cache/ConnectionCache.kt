/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.exception.AtlanException
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Glossary
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.pkg.serde.cell.ConnectionXformer
import mu.KotlinLogging

object ConnectionCache : AssetCache() {
    private val logger = KotlinLogging.logger {}

    private val includesOnResults: List<AtlanField> = listOf(Connection.NAME, Connection.CONNECTOR_TYPE, Connection.STATUS)

    /** {@inheritDoc}  */
    override fun lookupAssetByIdentity(identity: String?): Asset? {
        val tokens = identity?.split(ConnectionXformer.CONNECTION_DELIMITER)
        if (tokens?.size == 2) {
            val name = tokens[0]
            val type = tokens[1]
            try {
                val found = Connection.findByName(name, AtlanConnectorType.fromValue(type), includesOnResults)
                return found[0]
            } catch (e: NotFoundException) {
                logger.warn { "Unable to find connection: $identity" }
                logger.debug("Full stack trace:", e)
            } catch (e: AtlanException) {
                logger.error("Unable to lookup or find connection: {}", identity, e)
            }
        } else {
            logger.error { "Unable to lookup or find connection, unexpected reference: $identity" }
        }
        return null
    }

    /** {@inheritDoc}  */
    override fun lookupAssetByGuid(guid: String?, currentAttempt: Int, maxRetries: Int): Asset? {
        try {
            val connection =
                Connection.select(true)
                    .where(Glossary.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .pageSize(2)
                    .stream()
                    .findFirst()
            if (connection.isPresent) {
                return connection.get()
            } else {
                if (currentAttempt >= maxRetries) {
                    logger.error { "No connection found with GUID: $guid" }
                } else {
                    Thread.sleep(HttpClient.waitTime(currentAttempt).toMillis())
                    return lookupAssetByGuid(guid, currentAttempt + 1, maxRetries)
                }
            }
        } catch (e: AtlanException) {
            logger.error("Unable to lookup or find connection: {}", guid, e)
        }
        return null
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: Asset): String {
        return ConnectionXformer.encode(asset)
    }

    /**
     * Build a connection identity from its component parts.
     *
     * @param name of the connection
     * @param type of the connector for the connection (as a string)
     * @return identity for the connection
     */
    fun getIdentityForAsset(name: String, type: String): String {
        return ConnectionXformer.encode(name, type)
    }

    /** {@inheritDoc} */
    override fun preload() {
        logger.info { "Caching all connections, up-front..." }
        Connection.select()
            .includesOnResults(includesOnResults)
            .stream(true)
            .forEach { connection ->
                addByGuid(connection.guid, connection)
            }
    }
}
