/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

import mu.KLogger
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.sts.StsClient
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest
import java.io.File
import java.io.IOException

/**
 * Class to generally move data between S3 and local storage.
 *
 * @param bucketName name of the bucket in S3 to use for syncing
 * @param region AWS region through which to sync S3
 * @param logger through which to record any problems
 * @param accessKey (optional) AWS access key, if using as the form of authentication
 * @param secretKey (optional) AWS secret key, if using as the form of authentication
 */
class S3Sync(
    private val bucketName: String,
    private val region: String,
    private val logger: KLogger,
    private val accessKey: String = "",
    private val secretKey: String = "",
    roleArn: String = "",
) : ObjectStorageSyncer {
    private val credential: AwsCredentials? =
        if (roleArn.isNotBlank()) {
            logger.info { "Authenticating to S3 using provided IAM role ARN." }
            val stsClient =
                StsClient
                    .builder()
                    .region(Region.of(region))
                    .build()
            val roleRequest =
                AssumeRoleRequest
                    .builder()
                    .roleArn(roleArn)
                    .roleSessionName("AuthRoleSession")
                    .build()
            val roleResponse = stsClient.assumeRole(roleRequest)
            val myCreds = roleResponse.credentials()
            AwsSessionCredentials.create(myCreds.accessKeyId(), myCreds.secretAccessKey(), myCreds.sessionToken())
        } else if (accessKey.isNotBlank()) {
            logger.info { "Authenticating to S3 using provided access and secret keys." }
            AwsBasicCredentials.create(accessKey, secretKey)
        } else {
            logger.info { "Passing through authentication to backing S3 instance of the tenant." }
            null
        }
    private val s3Client =
        if (credential != null) {
            S3Client
                .builder()
                .credentialsProvider(StaticCredentialsProvider.create(credential))
                .region(Region.of(region))
                .build()
        } else {
            S3Client
                .builder()
                .region(Region.of(region))
                .build()
        }

    /** {@inheritDoc} */
    override fun copyFrom(
        prefix: String,
        localDirectory: String,
    ): List<String> {
        logger.info { "Syncing files from s3://$bucketName/$prefix to $localDirectory" }

        val request =
            ListObjectsV2Request
                .builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build()

        val localFilesLastModified =
            File(localDirectory)
                .walkTopDown()
                .filter { it.isFile }
                .map {
                    it.relativeTo(File(localDirectory)).path to it.lastModified()
                }.toMap()

        val s3FilesToDownload = mutableListOf<String>()
        s3Client.listObjectsV2(request).contents().forEach { file ->
            val key = File(file.key()).relativeTo(File(prefix)).path
            if (key.isNotBlank()) {
                if (key !in localFilesLastModified ||
                    file.lastModified().toEpochMilli() > localFilesLastModified[key]!!
                ) {
                    s3FilesToDownload.add(key)
                }
            }
        }

        val copiedList = mutableListOf<String>()
        s3FilesToDownload.forEach {
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
        logger.info { "Copying latest $extension file from s3://$bucketName/$prefix to $localDirectory" }

        val request =
            ListObjectsV2Request
                .builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build()

        val s3FilesToDownload = mutableListOf<String>()
        s3Client.listObjectsV2(request).contents().forEach { file ->
            val key = File(file.key()).relativeTo(File(prefix)).path
            if (key.isNotBlank() && key.endsWith(extension)) {
                s3FilesToDownload.add(key)
            }
        }
        s3FilesToDownload.sortDescending()
        val latestFile =
            if (s3FilesToDownload.isNotEmpty()) {
                s3FilesToDownload[0]
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
        logger.info { " ... downloading s3://$bucketName/$remoteKey to $localFile" }
        try {
            val local = File(localFile)
            if (local.exists()) {
                local.delete()
            }
            if (!local.parentFile.exists()) {
                local.parentFile.mkdirs()
            }
            val objectKey = File(remoteKey).path
            s3Client.getObject(
                GetObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build(),
                local.toPath(),
            )
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    /** {@inheritDoc} */
    override fun copyTo(
        localDirectory: String,
        prefix: String,
    ): Boolean {
        logger.info { "Syncing files from $localDirectory to s3://$bucketName/$prefix" }

        // val s3Client = S3Client.builder().region(Region.of(region)).build()
        val request =
            ListObjectsV2Request
                .builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build()
        val s3FilesLastModified =
            s3Client.listObjectsV2(request).contents().associate {
                File(it.key()).relativeTo(File(prefix)).path to it.lastModified().toEpochMilli()
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
        logger.info { " ... uploading $localFile to s3://$bucketName/$remoteKey" }
        // Note: no need to delete files first (putObject overwrites, including auto-versioning
        // if enabled on the bucket), and no need to create parent prefixes in S3
        try {
            val local = File(localFile)
            val objectKey = File(remoteKey).path
            s3Client.putObject(
                PutObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build(),
                local.toPath(),
            )
        } catch (e: Exception) {
            throw IOException(e)
        }
    }
}
