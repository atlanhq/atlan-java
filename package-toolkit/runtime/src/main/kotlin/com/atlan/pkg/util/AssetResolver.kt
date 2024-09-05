/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.util

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
            ConnectionIdentity(tokens[0], tokens[1])
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

    data class ConnectionIdentity(val name: String, val type: String) {
        override fun toString(): String {
            return "$name/$type"
        }
    }
}
