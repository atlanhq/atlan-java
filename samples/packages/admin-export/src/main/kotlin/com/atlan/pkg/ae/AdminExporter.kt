/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae

import AdminExportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Glossary
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.Utils.validatePathIsSafe
import com.atlan.pkg.ae.exports.Groups
import com.atlan.pkg.ae.exports.Personas
import com.atlan.pkg.ae.exports.Policies
import com.atlan.pkg.ae.exports.Purposes
import com.atlan.pkg.ae.exports.Users
import com.atlan.pkg.serde.csv.CSVWriter
import com.atlan.pkg.serde.xls.ExcelWriter
import com.atlan.pkg.util.AssetResolver
import java.io.File

/**
 * Actually run the export of admin objects.
 */
object AdminExporter {
    private val logger = Utils.getLogger(this.javaClass.name)

    private const val FILENAME = "admin-export.xlsx"

    private const val USERS_FILE = "users.csv"
    private const val GROUPS_FILE = "groups.csv"
    private const val PERSONAS_FILE = "personas.csv"
    private const val PURPOSES_FILE = "purposes.csv"
    private const val POLICIES_FILE = "policies.csv"

    @JvmStatic
    fun main(args: Array<String>) {
        val od = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<AdminExportCfg>().use { ctx ->

            val now = Utils.getNowAsISO8601()

            // Before we start processing, will pre-cache all glossaries,
            // so we can resolve them to meaningful names
            val glossaryMap = preloadGlossaryNameMap(ctx)
            val connectionMap = preloadConnectionMap(ctx)
            val xlsxOutput = ctx.config.fileFormat == "XLSX"

            val outputDirectory = validatePathIsSafe(od)
            outputDirectory.toFile().mkdirs()

            // Touch every file, just so they exist, to avoid any Argo failures
            // (Check for any custom names provided, and only fallback to default names where there aren't any)
            val xlsxFileActual = validatePathIsSafe(outputDirectory, Utils.getOrDefault(ctx.config.targetKey, FILENAME))
            xlsxFileActual.toFile().createNewFile()
            val usersFileActual = validatePathIsSafe(outputDirectory, Utils.getOrDefault(ctx.config.usersFilename, USERS_FILE))
            usersFileActual.toFile().createNewFile()
            val groupsFileActual = validatePathIsSafe(outputDirectory, Utils.getOrDefault(ctx.config.groupsFilename, GROUPS_FILE))
            groupsFileActual.toFile().createNewFile()
            val personasFileActual = validatePathIsSafe(outputDirectory, Utils.getOrDefault(ctx.config.personasFilename, PERSONAS_FILE))
            personasFileActual.toFile().createNewFile()
            val purposesFileActual = validatePathIsSafe(outputDirectory, Utils.getOrDefault(ctx.config.purposesFilename, PURPOSES_FILE))
            purposesFileActual.toFile().createNewFile()
            val policiesFileActual = validatePathIsSafe(outputDirectory, Utils.getOrDefault(ctx.config.policiesFilename, POLICIES_FILE))
            policiesFileActual.toFile().createNewFile()

            val fileOutputs = mutableListOf<String>()

            if (xlsxOutput) {
                ExcelWriter(xlsxFileActual.toString()).use { xlsx ->
                    ctx.config.objectsToInclude.forEach { objectName ->
                        when (objectName) {
                            "users" -> Users(ctx, xlsx.createSheet("Users"), logger).export()
                            "groups" -> Groups(ctx, xlsx.createSheet("Groups"), logger).export()
                            "personas" -> Personas(ctx, xlsx.createSheet("Personas"), glossaryMap, connectionMap, logger).export()
                            "purposes" -> Purposes(ctx, xlsx.createSheet("Purposes"), logger).export()
                            "policies" -> Policies(ctx, xlsx.createSheet("Policies"), glossaryMap, connectionMap, logger).export()
                        }
                    }
                }
                fileOutputs.add(xlsxFileActual.toString())
            } else {
                ctx.config.objectsToInclude.forEach { objectName ->
                    when (objectName) {
                        "users" -> CSVWriter(usersFileActual.toString()).use { csv -> Users(ctx, csv, logger).export() }
                        "groups" -> CSVWriter(groupsFileActual.toString()).use { csv -> Groups(ctx, csv, logger).export() }
                        "personas" -> CSVWriter(personasFileActual.toString()).use { csv -> Personas(ctx, csv, glossaryMap, connectionMap, logger).export() }
                        "purposes" -> CSVWriter(purposesFileActual.toString()).use { csv -> Purposes(ctx, csv, logger).export() }
                        "policies" -> CSVWriter(policiesFileActual.toString()).use { csv -> Policies(ctx, csv, glossaryMap, connectionMap, logger).export() }
                    }
                }
                fileOutputs.addAll(listOf(usersFileActual.toString(), groupsFileActual.toString(), personasFileActual.toString(), purposesFileActual.toString(), policiesFileActual.toString()))
            }

            when (ctx.config.deliveryType) {
                "EMAIL" -> {
                    val emails = Utils.getAsList(ctx.config.emailAddresses)
                    if (emails.isNotEmpty()) {
                        Utils.sendEmail(
                            "[Atlan] Admin Export results",
                            emails,
                            "Hi there! As requested, please find attached the results of the Admin Export package.\n\nAll the best!\nAtlan",
                            fileOutputs.map { File(it) },
                        )
                    }
                }

                "CLOUD" -> {
                    if (xlsxOutput) {
                        Utils.uploadOutputFile(
                            xlsxFileActual.toString(),
                            ctx.config.targetPrefix,
                            xlsxFileActual.fileName.toString(),
                            now,
                        )
                    } else {
                        fileOutputs.forEach {
                            // When using CSVs, ignore any key specified and use the filename itself
                            Utils.uploadOutputFile(
                                it,
                                ctx.config.targetPrefix,
                                key = null,
                                timestamp = now,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun preloadGlossaryNameMap(ctx: PackageContext<AdminExportCfg>): Map<String, String> {
        val map = mutableMapOf<String, String>()
        Glossary
            .select(ctx.client)
            .stream()
            .forEach {
                map[it.qualifiedName] = it.name
            }
        return map
    }

    private fun preloadConnectionMap(ctx: PackageContext<AdminExportCfg>): Map<String, AssetResolver.ConnectionIdentity> {
        val map = mutableMapOf<String, AssetResolver.ConnectionIdentity>()
        Connection
            .select(ctx.client)
            .includeOnResults(Connection.CONNECTOR_TYPE)
            .stream()
            .forEach {
                if (it.connectorType != null) {
                    map[it.qualifiedName] = AssetResolver.ConnectionIdentity(it.name, it.connectorType.value)
                } else if (!it.customConnectorType.isNullOrBlank()) {
                    map[it.qualifiedName] = AssetResolver.ConnectionIdentity(it.name, it.customConnectorType)
                }
            }
        return map
    }
}
