/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package model

import AssetImportCfg
import com.atlan.Atlan
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Readme
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.aim.Importer
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.util.AssetBatch
import java.io.File
import java.nio.file.Paths

object AEFConnection {
    val DB_CONNECTOR_TYPE = AtlanConnectorType.ICEBERG
    val BI_CONNECTOR_TYPE = AtlanConnectorType.SIGMA
    val SUPER_ADMINS = listOf("chris")
    lateinit var db: Connection
    lateinit var bi: Connection

    fun create(
        assetsInput: String,
        scholar: Fellowship.Scholar? = null,
    ): Map<String, File> {
        val connectionName = scholar?.id ?: "AEF Reference"
        val description =
            if (scholar != null) {
                "Connection to uniquely isolate assets for user ID: ${scholar.id} during the Atlan Engineering Fellowship."
            } else {
                "Connection containing an immutable reference set of assets for the Atlan Engineering Fellowship."
            }
        val batch = AssetBatch(Atlan.getDefaultClient(), 5)
        val db =
            Connection.creator(connectionName, DB_CONNECTOR_TYPE, null, null, SUPER_ADMINS)
                .description(description)
                .build()
        batch.add(db)
        val bi =
            Connection.creator(connectionName, BI_CONNECTOR_TYPE, null, null, SUPER_ADMINS)
                .description(description)
                .build()
        batch.add(bi)
        val response = batch.flush()
        val resultDB = response.getResult(db)
        if (scholar != null) {
            Fellowship.dbConnections[scholar.id] = resultDB
        } else {
            this.db = resultDB
        }
        Readme.creator(resultDB, AEFRichText.getConnectionReadme())
            .build().save()
        val resultBI = response.getResult(bi)
        if (scholar != null) {
            Fellowship.biConnections[scholar.id] = resultBI
        } else {
            this.bi = resultBI
        }
        return createAssetsFiles(resultDB, resultBI, assetsInput)
    }

    private fun createAssetsFiles(
        connDB: Connection,
        connBI: Connection,
        assetsInput: String,
    ): Map<String, File> {
        return prepFile(assetsInput, connDB, connBI)
    }

    private fun prepFile(
        assetsInput: String,
        connDB: Connection,
        connBI: Connection,
    ): Map<String, File> {
        // Start by replacing the variables with proper connection qualifiedNames
        val input = Paths.get(assetsInput).toFile()
        val suffix = connDB.qualifiedName.substringAfterLast('/')
        val output = Paths.get("/tmp/${input.nameWithoutExtension}_$suffix.csv").toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{DB_CONNECTION}}", connDB.qualifiedName)
                        .replace("{{BI_CONNECTION}}", connBI.qualifiedName)
                output.appendText("$revised\n")
            }
        }

        // TODO: Then break the single file into many, by asset type
        val fileMap = mutableMapOf<String, File>()

        // And return the list of separate files in the order they should be loaded
        return fileMap
    }

    // TODO: Really we want to run this once per asset-typed file (all databases across all connections, all tables across all connections, etc)
    // TODO: Actually, ^^ won't help -- will still iterate internally to update different connection caches per run
    // TODO: Bigger fix == update the AIM package so that it can load multiple files (in order) before updating the connection cache
    fun loadAssets(
        inputFiles: List<File>,
        directory: String,
    ): ImportResults? {
        var combinedResults: ImportResults? = null
        inputFiles.forEach {
            val config =
                AssetImportCfg(
                    importType = "DIRECT",
                    assetsFile = it.path,
                    assetsUpsertSemantic = "upsert",
                    assetsFailOnErrors = false,
                    trackBatches = true,
                )
            val results = Importer.import(config, directory)
            combinedResults = combinedResults?.combinedWith(results) ?: results
        }
        return combinedResults
    }
}
