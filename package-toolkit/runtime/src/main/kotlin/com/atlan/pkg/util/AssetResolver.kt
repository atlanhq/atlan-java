/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.util

import com.atlan.model.assets.Asset
import com.atlan.util.AssetBatch.AssetIdentity
import java.io.IOException

/**
 * Interface for resolving asset identities entirely from CSV file input (no calls to Atlan).
 */
interface AssetResolver {
    /**
     * Build a connection identity from an asset's tenant-agnostic qualifiedName.
     *
     * @param agnosticQualifiedName the tenant-agnostic qualifiedName of an asset
     * @return connection identity used for that asset
     */
    fun getConnectionIdentityFromQN(agnosticQualifiedName: String): ConnectionIdentity? {
        val tokens = agnosticQualifiedName.split("/")
        return if (tokens.size > 1) {
            ConnectionIdentity(tokens[0], tokens[1].lowercase())
        } else {
            null
        }
    }

    /**
     * Calculate the qualifiedName components from a row of data, completely in-memory (no calls to Atlan).
     *
     * @param row of data
     * @param header list of column names giving their position
     * @param typeName for which to determine the qualifiedName
     * @return details about the qualifiedName(s) inherent in this row of data
     */
    fun getQualifiedNameDetails(
        row: List<String>,
        header: List<String>,
        typeName: String,
    ): QualifiedNameDetails

    data class QualifiedNameDetails(
        val uniqueQN: String,
        val partialQN: String,
        val parentUniqueQN: String,
        val parentPartialQN: String,
    )

    data class ConnectionIdentity(
        val name: String,
        val type: String,
    ) {
        override fun toString(): String = "$name/$type"
    }

    /**
     * Resolve the asset represented by a row of values in a CSV to an asset identity.
     *
     * @param values row of values for that asset from the CSV
     * @param header order of column names in the CSV file being processed
     * @param connectionsMap cache of connection qualifiedNames, keyed by connection identity
     * @return a unique asset identity for that row of the CSV
     */
    @Throws(IOException::class)
    fun resolveAsset(
        values: List<String>,
        header: List<String>,
        connectionsMap: Map<ConnectionIdentity, String>,
    ): AssetIdentity? {
        val typeIdx = header.indexOf(Asset.TYPE_NAME.atlanFieldName)
        if (typeIdx < 0) {
            throw IOException(
                "Unable to find the column 'typeName'. This is a mandatory column in the input CSV.",
            )
        }
        val typeName = values[typeIdx]
        val qnDetails = getQualifiedNameDetails(values, header, typeName)
        val agnosticQN = qnDetails.uniqueQN
        val connectionIdentity = getConnectionIdentityFromQN(agnosticQN)
        if (connectionIdentity != null && connectionsMap.containsKey(connectionIdentity)) {
            val qualifiedName =
                agnosticQN.replaceFirst(connectionIdentity.toString(), connectionsMap[connectionIdentity]!!)
            return AssetIdentity(typeName, qualifiedName)
        }
        return null
    }
}
