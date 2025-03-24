/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowSerde
import com.atlan.pkg.util.AssetResolver.ConnectionIdentity
import mu.KLogger

class ConnectionXformer(
    private val ctx: PackageContext<RelationalAssetsBuilderCfg>,
    completeHeaders: List<String>,
    preprocessedDetails: Importer.Results,
    deferredConnectionCache: MutableMap<ConnectionIdentity, String>,
    private val logger: KLogger,
) : AssetXformer(
        ctx = ctx,
        completeHeaders = completeHeaders,
        typeNameFilter = Connection.TYPE_NAME,
        preprocessedDetails = preprocessedDetails,
        deferredConnectionCache = deferredConnectionCache,
        logger = logger,
    ) {
    companion object {
        const val CONNECTOR_TYPE = "connectorType"
    }

    override fun mapAsset(inputRow: Map<String, String>): Map<String, String> {
        val connectorType = getConnectorType(inputRow)
        val connectionName = trimWhitespace(inputRow.getOrElse(Asset.CONNECTION_NAME.atlanFieldName) { "" })
        val connectionQN = getConnectionQN(ctx, inputRow)
        val resolvedQN =
            connectionQN.ifBlank {
                Connection.creator(ctx.client, connectionName, AtlanConnectorType.fromValue(connectorType)).build().qualifiedName
            }
        val connectionId = ConnectionIdentity(connectionName, connectorType)
        if (!deferredConnectionCache.containsKey(connectionId)) {
            deferredConnectionCache[connectionId] = resolvedQN
        }
        return if (resolvedQN.isNotBlank()) {
            return mapOf(
                RowSerde.getHeaderForField(Asset.QUALIFIED_NAME) to resolvedQN,
                RowSerde.getHeaderForField(Asset.TYPE_NAME) to typeNameFilter,
                RowSerde.getHeaderForField(Asset.NAME) to connectionName,
                RowSerde.getHeaderForField(Asset.CONNECTOR_TYPE) to connectorType,
                RowSerde.getHeaderForField(Connection.ADMIN_USERS.atlanFieldName) to trimWhitespace(inputRow.getOrElse(Connection.ADMIN_USERS.atlanFieldName) { "" }),
                RowSerde.getHeaderForField(Connection.ADMIN_GROUPS.atlanFieldName) to trimWhitespace(inputRow.getOrElse(Connection.ADMIN_GROUPS.atlanFieldName) { "" }),
                RowSerde.getHeaderForField(Connection.ADMIN_ROLES.atlanFieldName) to trimWhitespace(inputRow.getOrElse(Connection.ADMIN_ROLES.atlanFieldName) { "" }),
            )
        } else {
            mapOf()
        }
    }
}
