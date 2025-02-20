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
import com.atlan.pkg.serde.csv.CSVWriter
import com.atlan.pkg.serde.xls.ExcelWriter
import java.io.File
import java.nio.file.Paths

/**
 * Actually run the export of admin objects.
 */
object AdminExporter {
    private val logger = Utils.getLogger(this.javaClass.name)

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<AdminExportCfg>().use { ctx ->

            // Before we start processing, will pre-cache all glossaries,
            // so we can resolve them to meaningful names
            val glossaryMap = preloadGlossaryNameMap(ctx)
            val connectionMap = preloadConnectionMap(ctx)
            val xlsxOutput = ctx.config.fileFormat == "XLSX"

            // These names must remain as-is, for Argo to have its outputs
            val xlsxFile = "$outputDirectory${File.separator}admin-export.xlsx"
            val usersFile = "$outputDirectory${File.separator}users.csv"
            val groupsFile = "$outputDirectory${File.separator}groups.csv"
            val personasFile = "$outputDirectory${File.separator}personas.csv"
            val purposesFile = "$outputDirectory${File.separator}purposes.csv"
            val policiesFile = "$outputDirectory${File.separator}policies.csv"

            // Check for any custom names provided, and only fallback to default names where there aren't any
            val xlsxFileActual = Utils.getOrDefault("$outputDirectory${File.separator}${ctx.config.targetKey}", xlsxFile)
            val usersFileActual = Utils.getOrDefault("$outputDirectory${File.separator}${ctx.config.usersFilename}", usersFile)
            val groupsFileActual = Utils.getOrDefault("$outputDirectory${File.separator}${ctx.config.groupsFilename}", groupsFile)
            val personasFileActual = Utils.getOrDefault("$outputDirectory${File.separator}${ctx.config.personasFilename}", personasFile)
            val purposesFileActual = Utils.getOrDefault("$outputDirectory${File.separator}${ctx.config.purposesFilename}", purposesFile)
            val policiesFileActual = Utils.getOrDefault("$outputDirectory${File.separator}${ctx.config.policiesFilename}", policiesFile)

            // Touch every file, just so they exist, to avoid any Argo failures
            Paths.get(outputDirectory).toFile().mkdirs()
            Paths.get(xlsxFile).toFile().createNewFile()
            Paths.get(usersFile).toFile().createNewFile()
            Paths.get(groupsFile).toFile().createNewFile()
            Paths.get(personasFile).toFile().createNewFile()
            Paths.get(purposesFile).toFile().createNewFile()
            Paths.get(policiesFile).toFile().createNewFile()

            val fileOutputs = mutableListOf<String>()

            if (xlsxOutput) {
                ExcelWriter(xlsxFileActual).use { xlsx ->
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
                fileOutputs.add(xlsxFile)
            } else {
                ctx.config.objectsToInclude.forEach { objectName ->
                    when (objectName) {
                        "users" -> CSVWriter(usersFileActual).use { csv -> Users(ctx, csv, logger).export() }
                        "groups" -> CSVWriter(groupsFileActual).use { csv -> Groups(ctx, csv, logger).export() }
                        "personas" -> CSVWriter(personasFileActual).use { csv -> Personas(ctx, csv, glossaryMap, connectionMap, logger).export() }
                        "purposes" -> CSVWriter(purposesFileActual).use { csv -> Purposes(ctx, csv, logger).export() }
                        "policies" -> CSVWriter(policiesFileActual).use { csv -> Policies(ctx, csv, glossaryMap, connectionMap, logger).export() }
                    }
                }
                fileOutputs.addAll(listOf(usersFileActual, groupsFileActual, personasFileActual, purposesFileActual, policiesFileActual))
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
                            xlsxFile,
                            ctx.config.targetPrefix,
                            xlsxFileActual,
                        )
                    } else {
                        fileOutputs.forEach {
                            // When using CSVs, ignore any key specified and use the filename itself
                            Utils.uploadOutputFile(
                                it,
                                ctx.config.targetPrefix,
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

    private fun preloadConnectionMap(ctx: PackageContext<AdminExportCfg>): Map<String, ConnectionId> {
        val map = mutableMapOf<String, ConnectionId>()
        Connection
            .select(ctx.client)
            .includeOnResults(Connection.CONNECTOR_TYPE)
            .stream()
            .forEach {
                map[it.qualifiedName] = ConnectionId(it.connectorType, it.name)
            }
        return map
    }

    data class ConnectionId(
        val type: AtlanConnectorType,
        val name: String,
    ) {
        override fun toString(): String = "$name (${type.value})"
    }
}
