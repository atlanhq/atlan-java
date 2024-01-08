/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.s3

import mu.KLogger
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File

/**
 * Class to generally move data between S3 and local storage.
 *
 * @param bucketName name of the bucket in S3 to use for syncing
 * @param region AWS region through which to sync S3
 * @param logger through which to record any problems
 */
class S3Sync(
    private val bucketName: String,
    private val region: String,
    private val logger: KLogger,
) {
    /**
     * Copy all files from the provided S3 prefix to the specified local directory.
     * Note: files that already exist in the local directory will be replaced by files from S3,
     * whenever the S3 last modification timestamp is greater than the local file's.
     *
     * @param s3Prefix from which to copy the files
     * @param localDirectory into which to copy the files
     * @return true if any files were copied, otherwise false
     */
    fun copyFromS3(s3Prefix: String, localDirectory: String): Boolean {
        logger.info("Syncing files from s3://$bucketName/$s3Prefix to $localDirectory")

        val s3Client = S3Client.builder().region(Region.of(region)).build()
        val request = ListObjectsV2Request.builder()
            .bucket(bucketName)
            .prefix(s3Prefix)
            .build()

        val localFilesLastModified = File(localDirectory).walkTopDown().filter { it.isFile }.map {
            it.relativeTo(File(localDirectory)).path to it.lastModified()
        }.toMap()

        val s3FilesToDownload = mutableListOf<String>()
        s3Client.listObjectsV2(request).contents().forEach { file ->
            val key = File(file.key()).relativeTo(File(s3Prefix)).path
            if (key.isNotBlank()) {
                if (key !in localFilesLastModified ||
                    file.lastModified().toEpochMilli() > localFilesLastModified[key]!!
                ) {
                    s3FilesToDownload.add(key)
                }
            }
        }

        var anySynced = false

        s3FilesToDownload.forEach {
            val localFile = File(localDirectory, it)
            if (localFile.exists()) {
                localFile.delete()
            }
            if (!localFile.parentFile.exists()) {
                localFile.parentFile.mkdirs()
            }
            val prefix = File(s3Prefix, it).path
            logger.info("Downloading s3://$bucketName/$prefix to ${localFile.path}")
            s3Client.getObject(
                GetObjectRequest.builder().bucket(bucketName).key(prefix).build(),
                localFile.toPath(),
            )
            anySynced = true
        }
        return anySynced
    }

    /**
     * Copy all files from the provided local directory to the specified S3 prefix.
     * Note: files that already exist in S3 will be replaced by files from the local directory,
     * whenever the local file's last modification timestamp is greater than S3's.
     *
     * @param localDirectory from which to copy the files
     * @param s3Prefix into which to copy the files
     * @return true if any files were copied, otherwise false
     */
    fun copyToS3(localDirectory: String, s3Prefix: String): Boolean {
        logger.info("Syncing files from $localDirectory to s3://$bucketName/$s3Prefix")

        val s3Client = S3Client.builder().region(Region.of(region)).build()
        val request = ListObjectsV2Request.builder()
            .bucket(bucketName)
            .prefix(s3Prefix)
            .build()
        val s3FilesLastModified = s3Client.listObjectsV2(request).contents().associate {
            File(it.key()).relativeTo(File(s3Prefix)).path to it.lastModified().toEpochMilli()
        }

        val localFilesToUpload = mutableListOf<String>()
        File(localDirectory).walkTopDown().filter { it.isFile }.forEach { file ->
            val key = file.relativeTo(File(localDirectory)).path
            if (key.isNotBlank()) {
                if (key !in s3FilesLastModified ||
                    file.lastModified() > s3FilesLastModified[key]!!
                ) {
                    localFilesToUpload.add(key)
                }
            }
        }

        var anySynced = false

        localFilesToUpload.forEach {
            // Note: no need to delete files first (putObject overwrites, including auto-versioning
            // if enabled on the bucket), and no need to create parent prefixes in S3
            val localFile = File(localDirectory, it)
            val prefix = File(s3Prefix, it).path
            logger.info("Uploading ${localFile.path} to s3://$bucketName/$prefix")
            s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(prefix).build(),
                localFile.toPath(),
            )
            anySynced = true
        }
        return anySynced
    }
}
