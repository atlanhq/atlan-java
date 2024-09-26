/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

import mu.KLogger
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import kotlin.io.path.copyTo

/**
 * Class to generally move data between local storage (pretending to be an object store) and local storage.
 *
 * @param baseDirectory to use to represent the remote object store's root (bucket)
 * @param logger through which to record any problems
 */
class LocalSync(
    private val baseDirectory: String,
    private val logger: KLogger,
) : ObjectStorageSyncer {

    /** {@inheritDoc} */
    override fun copyFrom(
        prefix: String,
        localDirectory: String,
    ): List<String> {
        logger.info { "Syncing files from local://$baseDirectory/$prefix to $localDirectory" }

        val localFilesLastModified =
            File(localDirectory).walkTopDown().filter { it.isFile }.map {
                it.relativeTo(File(localDirectory)).path to it.lastModified()
            }.toMap()

        val filesToDownload = mutableListOf<String>()
        val remoteDirectory = Paths.get(baseDirectory, prefix).toFile()
        remoteDirectory.walkTopDown().filter { it.isFile }.forEach {
            val key = it.relativeTo(remoteDirectory).path
            if (key.isNotBlank()) {
                if (key !in localFilesLastModified || it.lastModified() > localFilesLastModified[key]!!) {
                    filesToDownload.add(key)
                }
            }
        }

        val copiedList = mutableListOf<String>()
        filesToDownload.forEach {
            val target = File(localDirectory, it).path
            downloadFrom(
                File(prefix, it).path,
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
        logger.info { "Copying latest $extension file from local://$baseDirectory/$prefix to $localDirectory" }

        val filesToDownload = mutableListOf<String>()
        val remoteDirectory = Paths.get(baseDirectory, prefix).toFile()
        remoteDirectory.walkTopDown().filter { it.isFile }.forEach {
            val key = it.relativeTo(remoteDirectory).path
            if (key.isNotBlank() && key.endsWith(extension)) {
                filesToDownload.add(key)
            }
        }
        filesToDownload.sortDescending()
        val latestFile =
            if (filesToDownload.isNotEmpty()) {
                filesToDownload[0]
            } else {
                ""
            }

        val localFilePath =
            if (latestFile.isNotBlank()) {
                val local = File(localDirectory, latestFile).path
                downloadFrom(
                    File(prefix, latestFile).path,
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
        logger.info { " ... downloading local://$baseDirectory/$remoteKey to $localFile" }
        try {
            val local = File(localFile)
            if (local.exists()) {
                local.delete()
            }
            if (!local.parentFile.exists()) {
                local.parentFile.mkdirs()
            }
            val remote = Paths.get(baseDirectory, remoteKey)
            remote.copyTo(local.toPath(), true)
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    /** {@inheritDoc} */
    override fun copyTo(
        localDirectory: String,
        prefix: String,
    ): Boolean {
        logger.info { "Syncing files from $localDirectory to local://$baseDirectory/$prefix" }

        val remoteDirectory = Paths.get(baseDirectory, prefix).toFile()
        val remoteFilesLastModified =
            remoteDirectory.walkTopDown().filter { it.isFile }.associate {
                it.relativeTo(remoteDirectory).path to it.lastModified()
            }

        val localFilesToUpload = mutableListOf<String>()
        File(localDirectory).walkTopDown().filter { it.isFile }.forEach { file ->
            val key = file.relativeTo(File(localDirectory)).path
            if (key.isNotBlank()) {
                if (key !in remoteFilesLastModified ||
                    file.lastModified() > remoteFilesLastModified[key]!!
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
        logger.info { " ... uploading $localFile to local://$baseDirectory/$remoteKey" }
        // Note: no need to delete files first (putObject overwrites, including auto-versioning
        // if enabled on the bucket), and no need to create parent prefixes in S3
        try {
            val local = File(localFile)
            val objectKey = Paths.get(baseDirectory, remoteKey).toFile()
            local.copyTo(objectKey, true)
        } catch (e: Exception) {
            throw IOException(e)
        }
    }
}
