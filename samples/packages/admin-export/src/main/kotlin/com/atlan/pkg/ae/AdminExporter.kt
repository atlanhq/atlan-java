/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae

import AdminExportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Glossary
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.Utils
import com.atlan.pkg.ae.exports.Groups
import com.atlan.pkg.ae.exports.Personas
import com.atlan.pkg.ae.exports.Policies
import com.atlan.pkg.ae.exports.Purposes
import com.atlan.pkg.ae.exports.Users
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KotlinLogging
import java.io.File

/**
 * Actually run the export of admin objects.
 */
object AdminExporter {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<AdminExportCfg>()

        val objectsToInclude = Utils.getOrDefault(config.objectsToInclude, listOf("users", "groups"))
        val includeNativePolicies = Utils.getOrDefault(config.includeNativePolicies, false)
        val deliveryType = Utils.getOrDefault(config.deliveryType, "DIRECT")

        // Before we start processing, will pre-cache all glossaries,
        // so we can resolve them to meaningful names
        val glossaryMap = preloadGlossaryNameMap()
        val connectionMap = preloadConnectionMap()

        val exportFile = "$outputDirectory${File.separator}admin-export.xlsx"
        ExcelWriter(exportFile).use { xlsx ->
            objectsToInclude.forEach { objectName ->
                when (objectName) {
                    "users" -> Users(xlsx, logger).export()
                    "groups" -> Groups(xlsx, logger).export()
                    "personas" -> Personas(xlsx, glossaryMap, connectionMap, logger).export()
                    "purposes" -> Purposes(xlsx, logger).export()
                    "policies" -> Policies(xlsx, includeNativePolicies, glossaryMap, connectionMap, logger).export()
                }
            }
        }

        when (deliveryType) {
            "EMAIL" -> {
                val emails = Utils.getOrDefault(config.emailAddresses, "")
                    .split(',')
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
                    .toList()
                if (emails.isNotEmpty()) {
                    Utils.sendEmail(
                        "[Atlan] Admin Export results",
                        emails,
                        "Hi there! As requested, please find attached the results of the Admin Export package.\n\nAll the best!\nAtlan",
                        listOf(File(exportFile)),
                    )
                }
            }
            "CLOUD" -> {
                Utils.uploadOutputFile(
                    exportFile,
                    Utils.getOrDefault(config.targetPrefix, ""),
                    Utils.getOrDefault(config.targetKey, ""),
                )
            }
        }
    }

    private fun preloadGlossaryNameMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        Glossary.select()
            .pageSize(50)
            .stream()
            .forEach {
                map[it.qualifiedName] = it.name
            }
        return map
    }

    private fun preloadConnectionMap(): Map<String, ConnectionId> {
        val map = mutableMapOf<String, ConnectionId>()
        Connection.select()
            .pageSize(50)
            .includeOnResults(Connection.CONNECTOR_TYPE)
            .stream()
            .forEach {
                map[it.qualifiedName] = ConnectionId(it.connectorType, it.name)
            }
        return map
    }

    data class ConnectionId(val type: AtlanConnectorType, val name: String) {
        override fun toString(): String {
            return "$name (${type.value})"
        }
    }
}
