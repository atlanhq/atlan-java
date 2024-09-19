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
import kotlin.io.path.bufferedWriter

object AEFConnection {
    val DB_CONNECTOR_TYPE = AtlanConnectorType.ICEBERG
    val BI_CONNECTOR_TYPE = AtlanConnectorType.SIGMA
    val SUPER_ADMINS = listOf("chris")
    private const val REF_CONNECTION_NAME = "AEF Reference"
    lateinit var db: Connection
    lateinit var bi: Connection

    fun create(
        assetsInput: String,
        scholar: Fellowship.Scholar? = null,
    ): File {
        val connectionName = scholar?.id ?: REF_CONNECTION_NAME
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
    ): File {
        return prepFile(assetsInput, connDB, connBI)
    }

    private fun prepFile(
        assetsInput: String,
        connDB: Connection,
        connBI: Connection,
    ): File {
        // Start by replacing the variables with proper connection qualifiedNames
        val input = Paths.get(assetsInput).toFile()
        val suffix = connDB.qualifiedName.substringAfterLast('/')
        val output = Paths.get("/tmp/${input.nameWithoutExtension}_$suffix.csv").toFile()
        // Skip the header line if this is anything other than the reference connection
        val skipLines = if (connDB.name == REF_CONNECTION_NAME) 0 else 1
        input.useLines { lines ->
            lines.drop(skipLines).forEach { line ->
                val revised =
                    line
                        .replace("{{DB_TYPE}}", connDB.connectorType.value)
                        .replace("{{BI_CONN_NAME}}", connBI.name)
                        .replace("{{DB_CONNECTION}}", connDB.qualifiedName)
                        .replace("{{BI_CONNECTION}}", connBI.qualifiedName)
                output.appendText("$revised\n")
            }
        }
        return output
    }

    fun loadAssets(
        inputFiles: List<File>,
        directory: String,
    ): ImportResults? {
        val singleFile = concatenateFiles(inputFiles, directory)
        val config =
            AssetImportCfg(
                importType = "DIRECT",
                assetsFile = singleFile.path,
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = false,
                trackBatches = true,
            )
        return Importer.import(config, directory)
    }

    private fun concatenateFiles(
        files: List<File>,
        directory: String,
    ): File {
        val output = Paths.get(directory, "combined_assets.csv").toFile()
        output.bufferedWriter().use { writer ->
            for (file in files) {
                file.useLines { lines ->
                    lines.forEach { line ->
                        writer.appendLine(line)
                    }
                }
            }
        }
        return output
    }
}
