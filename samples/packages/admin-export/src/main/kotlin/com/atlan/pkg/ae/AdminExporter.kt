/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae

import AdminExportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Glossary
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.ae.exports.Groups
import com.atlan.pkg.ae.exports.Personas
import com.atlan.pkg.ae.exports.Policies
import com.atlan.pkg.ae.exports.Purposes
import com.atlan.pkg.ae.exports.Users
import com.atlan.pkg.serde.xls.ExcelWriter
import java.io.File

/**
 * Actually run the export of admin objects.
 */
object AdminExporter {
    private val logger = Utils.getLogger(this.javaClass.name)
    private const val FILENAME = "admin-export.xlsx"

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<AdminExportCfg>().use { ctx ->

            // Before we start processing, will pre-cache all glossaries,
            // so we can resolve them to meaningful names
            val glossaryMap = preloadGlossaryNameMap(ctx)
            val connectionMap = preloadConnectionMap(ctx)

            val exportFile = "$outputDirectory${File.separator}$FILENAME"
            ExcelWriter(exportFile).use { xlsx ->
                ctx.config.objectsToInclude.forEach { objectName ->
                    when (objectName) {
                        "users" -> Users(ctx, xlsx, logger).export()
                        "groups" -> Groups(ctx, xlsx, logger).export()
                        "personas" -> Personas(ctx, xlsx, glossaryMap, connectionMap, logger).export()
                        "purposes" -> Purposes(ctx, xlsx, logger).export()
                        "policies" -> Policies(ctx, xlsx, glossaryMap, connectionMap, logger).export()
                    }
                }
            }

            when (ctx.config.deliveryType) {
                "EMAIL" -> {
                    val emails = Utils.getAsList(ctx.config.emailAddresses)
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
                        ctx.config.targetPrefix,
                        ctx.config.targetKey,
                    )
                }
            }
        }
    }

    private fun preloadGlossaryNameMap(ctx: PackageContext<AdminExportCfg>): Map<String, String> {
        val map = mutableMapOf<String, String>()
        Glossary.select(ctx.client)
            .stream()
            .forEach {
                map[it.qualifiedName] = it.name
            }
        return map
    }

    private fun preloadConnectionMap(ctx: PackageContext<AdminExportCfg>): Map<String, ConnectionId> {
        val map = mutableMapOf<String, ConnectionId>()
        Connection.select(ctx.client)
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
