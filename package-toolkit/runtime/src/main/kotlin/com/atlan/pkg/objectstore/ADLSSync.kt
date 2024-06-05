/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

import com.azure.identity.ClientSecretCredentialBuilder
import com.azure.storage.common.StorageSharedKeyCredential
import com.azure.storage.file.datalake.DataLakeServiceClient
import com.azure.storage.file.datalake.DataLakeServiceClientBuilder
import com.azure.storage.file.datalake.models.ListPathsOptions
import mu.KLogger
import java.io.File

/**
 * Class to generally move data between ADLS and local storage.
 *
 * @param accountName name of the Azure account
 * @param containerName name of the container in ADLS to use for syncing
 * @param logger through which to record any problems
 * @param tenantId unique identifier (GUID) of the tenant (or blank to use Atlan's backing store in ADLS)
 * @param clientId unique identifier (GUID) of the client (or blank to use Atlan's backing store in ADLS)
 * @param clientSecret value of the secret for the client (note this is not the GUID of the client secret)
 */
class ADLSSync(
    private val accountName: String,
    private val containerName: String,
    private val logger: KLogger,
    private val tenantId: String,
    private val clientId: String,
    private val clientSecret: String,
) : ObjectStorageSyncer {

    private val adlsClient: DataLakeServiceClient
    init {
        if (tenantId.isNotBlank() && clientId.isNotBlank()) {
            val credential = ClientSecretCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build()
            adlsClient = DataLakeServiceClientBuilder()
                .endpoint("https://$accountName.dfs.core.windows.net")
                .credential(credential)
                .buildClient()
        } else {
            // Fallback to using Atlan's backing store if tenantId or clientId is empty
            val credential = StorageSharedKeyCredential(accountName, clientSecret)
            adlsClient = DataLakeServiceClientBuilder()
                .endpoint("https://$accountName.dfs.core.windows.net")
                .credential(credential)
                .buildClient()
        }
    }

    /** {@inheritDoc} */
    override fun copyFrom(prefix: String, localDirectory: String): List<String> {
        logger.info { "Syncing files from adls://$containerName/$prefix to $localDirectory" }

        val fsClient = adlsClient.getFileSystemClient(containerName)

        val localFilesLastModified = File(localDirectory).walkTopDown().filter { it.isFile }.map {
            it.relativeTo(File(localDirectory)).path to it.lastModified()
        }.toMap()

        val filesToDownload = mutableListOf<String>()
        fsClient.listPaths(ListPathsOptions().setPath(prefix), null).forEach { file ->
            val key = File(file.name).relativeTo(File(prefix)).path
            if (key.isNotBlank()) {
                if (key !in localFilesLastModified ||
                    file.lastModified.toInstant().toEpochMilli() > localFilesLastModified[key]!!
                ) {
                    filesToDownload.add(key)
                }
            }
        }

        val copiedList = mutableListOf<String>()
        filesToDownload.forEach { key ->
            val target = File(localDirectory, key).path
            downloadFrom(
                key,
                target,
            )
            copiedList.add(target)
        }
        return copiedList
    }

    /** {@inheritDoc} */
    override fun copyLatestFrom(prefix: String, extension: String, localDirectory: String): String {
        logger.info { "Copying latest $extension file from gcs://$containerName/$prefix to $localDirectory" }

        val fsClient = adlsClient.getFileSystemClient(containerName)

        val filesToDownload = mutableListOf<String>()

        fsClient.listPaths(ListPathsOptions().setPath(prefix), null).forEach { file ->
            val key = File(file.name).relativeTo(File(prefix)).path
            if (key.isNotBlank() && key.endsWith(extension)) {
                filesToDownload.add(key)
            }
        }
        filesToDownload.sortDescending()
        val latestFileKey = if (filesToDownload.isNotEmpty()) {
            filesToDownload[0]
        } else {
            ""
        }

        val localFilePath = if (latestFileKey.isNotBlank()) {
            val local = File(localDirectory, latestFileKey).path
            downloadFrom(
                latestFileKey,
                local,
            )
            local
        } else {
            ""
        }
        return localFilePath
    }

    /** {@inheritDoc} */
    override fun downloadFrom(remoteKey: String, localFile: String) {
        logger.info { " ... downloading adls://$containerName/$remoteKey to $localFile" }
        val local = File(localFile)
        if (local.exists()) {
            local.delete()
        }
        if (!local.parentFile.exists()) {
            local.parentFile.mkdirs()
        }
        val fsClient = adlsClient.getFileSystemClient(containerName)
        val fileClient = fsClient.getFileClient(remoteKey)
        fileClient.readToFile(localFile)
    }

    /** {@inheritDoc} */
    override fun copyTo(localDirectory: String, prefix: String): Boolean {
        logger.info { "Syncing files from $localDirectory to adls://$containerName/$prefix" }

        val fsClient = adlsClient.getFileSystemClient(containerName)

        val filesLastModified = fsClient.listPaths(ListPathsOptions().setPath(prefix), null).associate {
            File(it.name).relativeTo(File(prefix)).path to it.lastModified.toInstant().toEpochMilli()
        }

        val localFilesToUpload = mutableListOf<String>()
        File(localDirectory).walkTopDown().filter { it.isFile }.forEach { file ->
            val key = file.relativeTo(File(localDirectory)).path
            if (key.isNotBlank()) {
                if (key !in filesLastModified ||
                    file.lastModified() > filesLastModified[key]!!
                ) {
                    localFilesToUpload.add(key)
                }
            }
        }

        var anySynced = false

        localFilesToUpload.forEach {
            uploadTo(File(localDirectory, it).path, File(prefix, it).path)
            anySynced = true
        }
        return anySynced
    }

    /** {@inheritDoc} */
    override fun uploadTo(localFile: String, remoteKey: String) {
        logger.info { " ... uploading $localFile to adls://$containerName/$remoteKey" }
        // Note: no need to delete files first (putObject overwrites, including auto-versioning
        // if enabled on the bucket), and no need to create parent prefixes in ADLS
        val fsClient = adlsClient.getFileSystemClient(containerName)
        val fileClient = fsClient.getFileClient(remoteKey)
        fileClient.uploadFromFile(localFile, true)
    }
}
