/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

import com.azure.identity.ClientSecretCredentialBuilder
import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobContainerClientBuilder
import com.azure.storage.blob.models.ListBlobsOptions
import com.azure.storage.common.StorageSharedKeyCredential
import com.azure.storage.file.datalake.DataLakeServiceClient
import com.azure.storage.file.datalake.DataLakeServiceClientBuilder
import com.azure.storage.file.datalake.models.ListPathsOptions
import mu.KLogger
import java.io.File
import java.io.IOException

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
    private val adlsClient: DataLakeServiceClient?
    private val blobContainerClient: BlobContainerClient?

    init {
        if (tenantId.isNotBlank() && clientId.isNotBlank()) {
            logger.info { "Authenticating to ADLS using provided tenant and client IDs and secrets." }
            val credential =
                ClientSecretCredentialBuilder()
                    .tenantId(tenantId)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build()
            adlsClient =
                DataLakeServiceClientBuilder()
                    .endpoint("https://$accountName.dfs.core.windows.net")
                    .credential(credential)
                    .buildClient()
            blobContainerClient = null
        } else {
            logger.info { "Passing through authentication to backing ADLS instance of the tenant." }
            // Fallback to using Atlan's backing store if tenantId or clientId is empty
            // (Note: we must use a blob container client here as the file system client
            //  otherwise cannot do any uploads.)
            val credential = StorageSharedKeyCredential(accountName, clientSecret)
            blobContainerClient =
                BlobContainerClientBuilder()
                    .endpoint("https://$accountName.blob.core.windows.net")
                    .credential(credential)
                    .containerName(containerName)
                    .buildClient()
            adlsClient = null
        }
    }

    /** {@inheritDoc} */
    override fun copyFrom(
        prefix: String,
        localDirectory: String,
    ): List<String> {
        logger.info { "Syncing files from adls://$containerName/$prefix to $localDirectory" }

        val filesToDownload = mutableListOf<String>()
        val localFilesLastModified =
            File(localDirectory)
                .walkTopDown()
                .filter { it.isFile }
                .map {
                    it.relativeTo(File(localDirectory)).path to it.lastModified()
                }.toMap()

        if (adlsClient != null) {
            val fsClient = adlsClient.getFileSystemClient(containerName)
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
        } else if (blobContainerClient != null) {
            blobContainerClient.listBlobs(ListBlobsOptions().setPrefix(prefix), null).forEach { blob ->
                val key = File(blob.name).relativeTo(File(prefix)).path
                if (key.isNotBlank()) {
                    if (key !in localFilesLastModified ||
                        blob.properties.lastModified
                            .toInstant()
                            .toEpochMilli() > localFilesLastModified[key]!!
                    ) {
                        filesToDownload.add(key)
                    }
                }
            }
        } else {
            throw IllegalStateException("No ADLS client configured -- cannot download.")
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
    override fun copyLatestFrom(
        prefix: String,
        extension: String,
        localDirectory: String,
    ): String {
        logger.info { "Copying latest $extension file from adls://$containerName/$prefix to $localDirectory" }

        val filesToDownload = mutableListOf<String>()

        if (adlsClient != null) {
            val fsClient = adlsClient.getFileSystemClient(containerName)
            fsClient.listPaths(ListPathsOptions().setPath(prefix), null).forEach { file ->
                val key = File(file.name).relativeTo(File(prefix)).path
                if (key.isNotBlank() && key.endsWith(extension)) {
                    filesToDownload.add(key)
                }
            }
        } else if (blobContainerClient != null) {
            blobContainerClient.listBlobs(ListBlobsOptions().setPrefix(prefix), null).forEach { blob ->
                val key = File(blob.name).relativeTo(File(prefix)).path
                if (key.isNotBlank() && key.endsWith(extension)) {
                    filesToDownload.add(key)
                }
            }
        } else {
            throw IllegalStateException("No ADLS client configured -- cannot download.")
        }
        filesToDownload.sortDescending()
        val latestFileKey =
            if (filesToDownload.isNotEmpty()) {
                filesToDownload[0]
            } else {
                ""
            }

        val localFilePath =
            if (latestFileKey.isNotBlank()) {
                val local = File(localDirectory, latestFileKey).path
                downloadFrom(
                    File(prefix, latestFileKey).path,
                    local,
                )
                local
            } else {
                ""
            }
        return localFilePath
    }

    /** {@inheritDoc} */
    override fun downloadFrom(
        remoteKey: String,
        localFile: String,
    ) {
        logger.info { " ... downloading adls://$containerName/$remoteKey to $localFile" }
        try {
            val local = File(localFile)
            if (local.exists()) {
                local.delete()
            }
            if (!local.parentFile.exists()) {
                local.parentFile.mkdirs()
            }
            if (adlsClient != null) {
                val fsClient = adlsClient.getFileSystemClient(containerName)
                val fileClient = fsClient.getFileClient(remoteKey)
                fileClient.readToFile(localFile)
            } else if (blobContainerClient != null) {
                val blobClient = blobContainerClient.getBlobClient(remoteKey)
                blobClient.downloadToFile(localFile)
            } else {
                throw IllegalStateException("No ADLS client configured -- cannot download.")
            }
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    /** {@inheritDoc} */
    override fun copyTo(
        localDirectory: String,
        prefix: String,
    ): Boolean {
        logger.info { "Syncing files from $localDirectory to adls://$containerName/$prefix" }

        val localFilesToUpload = mutableListOf<String>()

        val filesLastModified =
            if (adlsClient != null) {
                val fsClient = adlsClient.getFileSystemClient(containerName)
                fsClient.listPaths(ListPathsOptions().setPath(prefix), null).associate {
                    File(it.name).relativeTo(File(prefix)).path to it.lastModified.toInstant().toEpochMilli()
                }
            } else if (blobContainerClient != null) {
                blobContainerClient.listBlobs(ListBlobsOptions().setPrefix(prefix), null).associate {
                    File(it.name).relativeTo(File(prefix)).path to
                        it.properties.lastModified
                            .toInstant()
                            .toEpochMilli()
                }
            } else {
                throw IllegalStateException("No ADLS client configured -- cannot upload.")
            }

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
    override fun uploadTo(
        localFile: String,
        remoteKey: String,
    ) {
        logger.info { " ... uploading $localFile to adls://$containerName/$remoteKey" }
        // Note: no need to delete files first (putObject overwrites, including auto-versioning
        // if enabled on the bucket), and no need to create parent prefixes in ADLS
        try {
            if (adlsClient != null) {
                val fsClient = adlsClient.getFileSystemClient(containerName)
                val fileClient = fsClient.getFileClient(remoteKey)
                fileClient.uploadFromFile(localFile, true)
            } else if (blobContainerClient != null) {
                val blobClient = blobContainerClient.getBlobClient(remoteKey)
                blobClient.uploadFromFile(localFile, true)
            } else {
                throw IllegalStateException("No ADLS client configured -- cannot upload.")
            }
        } catch (e: Exception) {
            throw IOException(e)
        }
    }
}
