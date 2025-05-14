/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanConnectionCategory
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.csv.CSVImporter
import com.atlan.pkg.util.AssetResolver
import mu.KLogger
import java.util.regex.Pattern
import java.util.stream.Stream

/**
 * Import connections into Atlan from a provided CSV file.
 *
 * Only the connections and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context through which this package is running
 * @param inputFile the input file containing connection details
 * @param logger through which to record logging
 */
class ConnectionImporter(
    ctx: PackageContext<AssetImportCfg>,
    private val inputFile: String,
    logger: KLogger,
) : CSVImporter(
        ctx = ctx,
        filename = inputFile,
        logger = logger,
        typeNameFilter = Connection.TYPE_NAME,
        attrsToOverwrite = attributesToClear(ctx.config.assetsAttrToOverwrite.toMutableList(), "assets", logger),
        creationHandling = if (ctx.config.assetsUpsertSemantic == "update") AssetCreationHandling.NONE else AssetCreationHandling.FULL,
        batchSize = 1,
        trackBatches = true,
        fieldSeparator = ctx.config.assetsFieldSeparator[0],
    ) {
    companion object {
        const val CONNECTOR_TYPE = "connectorType"
        const val DEFERRED_QN_PATTERN = "\\{\\{([a-zA-Z0-9-]+)/(.+?)\\}\\}(/.*)?"
        val deferredQN = Pattern.compile(DEFERRED_QN_PATTERN)!!

        /**
         * Parse the connector type from the deferred connection definition.
         * @param qualifiedName from which to parse
         * @return the details of the deferred connector type
         */
        fun getDeferredIdentity(qualifiedName: String): AssetResolver.ConnectionIdentity? {
            val matcher = deferredQN.matcher(qualifiedName)
            return if (matcher.matches()) {
                AssetResolver.ConnectionIdentity(matcher.group(2), matcher.group(1))
            } else null
        }

        /**
         * Attempt to resolve a qualifiedName that may have deferred connection details.
         * @param connectionsMap mapping of existing connections
         * @param qualifiedName to resolve
         * @return the qualifiedName, resolved
         */
        fun resolveDeferredQN(
            connectionsMap: Map<AssetResolver.ConnectionIdentity, String>,
            qualifiedName: String,
        ): String {
            val matcher = deferredQN.matcher(qualifiedName)
            return if (matcher.matches()) {
                val connectorType = matcher.group(1)
                val connectionName = matcher.group(2)
                val remainder = matcher.group(3) ?: ""
                val connectionId = AssetResolver.ConnectionIdentity(connectionName, connectorType)
                val resolvedConnectionQN = connectionsMap.getOrElse(connectionId) {
                    throw IllegalStateException("Unable to resolve deferred connection -- no connection found by name and type: $qualifiedName")
                }
                "$resolvedConnectionQN$remainder"
            } else qualifiedName
        }
    }

    /** {@inheritDoc} */
    @Suppress("UNCHECKED_CAST")
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(Connection.NAME.atlanFieldName)?.let { it as String } ?: ""
        val qualifiedName = deserializer.getValue(Connection.QUALIFIED_NAME.atlanFieldName)?.let { it as String } ?: ""
        val cName = deserializer.getValue(Connection.CONNECTOR_NAME.atlanFieldName)?.let { it as String }
        val cType = deserializer.getValue(Connection.CONNECTOR_TYPE.atlanFieldName)?.let { it as String }
        val customType = deserializer.getValue(Connection.CUSTOM_CONNECTOR_TYPE.atlanFieldName)?.let { it as String }
        val deferredIdentity = getDeferredIdentity(qualifiedName)
        val resolvedType = if (qualifiedName.isEmpty()) {
            customType ?: cType ?: cName
        } else deferredIdentity?.type ?: Connection.getConnectorFromQualifiedName(qualifiedName)
        if (resolvedType == null || resolvedType.isEmpty()) {
            throw NoSuchElementException("Invalid connectorType provided for the connection, cannot be processed: $qualifiedName (name: $name)")
        }
        val resolvedName = deferredIdentity?.name ?: name
        val identity = ctx.connectionCache.getIdentityForAsset(resolvedName, resolvedType)
        val existing = ctx.connectionCache.getByIdentity(identity)
        return if (existing != null) {
            existing.trimToRequired()
        } else {
            val users = deserializer.getValue(Connection.ADMIN_USERS.atlanFieldName)?.let { it as List<String> }
            val groups = deserializer.getValue(Connection.ADMIN_GROUPS.atlanFieldName)?.let { it as List<String> }
            val roles = deserializer.getValue(Connection.ADMIN_ROLES.atlanFieldName)?.let { it as List<String> }
            val ct = AtlanConnectorType.fromValue(resolvedType)
            if (ct != null && ct != AtlanConnectorType.UNKNOWN_CUSTOM) {
                Connection.creator(ctx.client, name, ct, roles, groups, users)
            } else {
                val category = deserializer.getValue(Connection.CATEGORY.atlanFieldName)?.let { it as AtlanConnectionCategory }
                Connection.creator(ctx.client, name, resolvedType, category, roles, groups, users)
            }
        }
    }

    /** {@inheritDoc} */
    override fun cacheCreated(list: Stream<Asset>) {
        // Cache any assets that were created by processing
        list.forEach { asset ->
            // We must look up the asset and then cache to ensure we have the necessary identity
            // characteristics and status
            ctx.connectionCache.cacheById(asset.guid)
        }
    }
}
