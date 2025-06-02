/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import mu.KLogger
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * Class to generally move data between GCS and local storage.
 *
 * @param projectId identifier of the GCP project
 * @param bucketName name of the bucket in GCS to use for syncing
 * @param logger through which to record any problems
 * @param credentials JSON application credentials (as a string), or empty to use Atlan's backing store
 */
class GCSSync(
    private val projectId: String,
    private val bucketName: String,
    private val logger: KLogger,
    private val credentials: String,
) : ObjectStorageSyncer {
    private val storage =
        if (credentials.isNotBlank()) {
            logger.info { "Authenticating to GCS using provided credentials." }
            StorageOptions
                .newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(credentials.byteInputStream()))
                .build()
                .service
        } else {
            logger.info { "Passing through authentication to backing GCS instance of the tenant." }
            StorageOptions
                .newBuilder()
                .setProjectId(projectId)
                .build()
                .service
        }

    /** {@inheritDoc} */
    override fun copyFrom(
        prefix: String,
        localDirectory: String,
    ): List<String> {
        logger.info { "Syncing files from gcs://$bucketName/$prefix to $localDirectory" }

        val bucket = storage.get(bucketName)

        val localFilesLastModified =
            File(localDirectory)
                .walkTopDown()
                .filter { it.isFile }
                .map {
                    it.relativeTo(File(localDirectory)).path to it.lastModified()
                }.toMap()

        val filesToDownload = mutableListOf<String>()
        bucket.list(Storage.BlobListOption.prefix(prefix)).iterateAll().forEach { file ->
            val info = file.asBlobInfo()
            val key = File(info.name).relativeTo(File(prefix)).path
            if (key.isNotBlank()) {
                if (key !in localFilesLastModified ||
                    info.updateTimeOffsetDateTime.toInstant().toEpochMilli() > localFilesLastModified[key]!!
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
    override fun copyLatestFrom(
        prefix: String,
        extension: String,
        localDirectory: String,
    ): String {
        logger.info { "Copying latest $extension file from gcs://$bucketName/$prefix to $localDirectory" }

        val bucket = storage.get(bucketName)

        val filesToDownload = mutableListOf<String>()

        bucket.list(Storage.BlobListOption.prefix(prefix)).iterateAll().forEach { file ->
            val info = file.asBlobInfo()
            val key = File(info.name).relativeTo(File(prefix)).path
            if (key.isNotBlank() && key.endsWith(extension)) {
                filesToDownload.add(key)
            }
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
        logger.info { " ... downloading gcs://$bucketName/$remoteKey to $localFile" }
        try {
            val local = File(localFile)
            if (local.exists()) {
                local.delete()
            }
            if (!local.parentFile.exists()) {
                local.parentFile.mkdirs()
            }
            val blob = storage.get(bucketName, remoteKey)
            FileOutputStream(local).use { fos ->
                blob.downloadTo(fos)
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
        logger.info { "Syncing files from $localDirectory to gcs://$bucketName/$prefix" }

        val bucket = storage.get(bucketName)

        val filesLastModified =
            bucket.list(Storage.BlobListOption.prefix(prefix)).iterateAll().associate {
                val info = it.asBlobInfo()
                File(info.name).relativeTo(File(prefix)).path to info.updateTimeOffsetDateTime.toInstant().toEpochMilli()
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
    override fun uploadTo(
        localFile: String,
        remoteKey: String,
    ) {
        logger.info { " ... uploading $localFile to gcs://$bucketName/$remoteKey" }
        // Note: no need to delete files first (putObject overwrites, including auto-versioning
        // if enabled on the bucket), and no need to create parent prefixes in GCS
        try {
            val local = File(localFile)
            val bucket = storage.get(bucketName)
            FileInputStream(local).use { fis ->
                bucket.create(remoteKey, fis)
            }
        } catch (e: Exception) {
            throw IOException(e)
        }
    }
}
