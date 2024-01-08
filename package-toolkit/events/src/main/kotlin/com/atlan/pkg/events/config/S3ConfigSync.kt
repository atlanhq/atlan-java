/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.events.config

import com.atlan.pkg.CustomConfig
import com.atlan.pkg.Utils
import com.atlan.pkg.s3.S3Sync
import mu.KotlinLogging
import java.io.File

/**
 * Utility class to synchronize configuration for an event-driven pipeline from JSON
 * files, themselves configured via a workflow template in the UI.
 * Note: this relies on two files being produced by the workflow configuration:
 * 1. config.json -- containing at least an "api_token" key, giving the GUID of the API token for the pipeline to execute through
 * 2. runtime.json -- containing the details of the workflow (X_ATLAN_AGENT_* headers), for injection into audit logs
 */
class S3ConfigSync {

    val logger = KotlinLogging.logger {}

    val localPath = "/tmp"
    val configPrefix = Utils.getEnvVar("CONFIG_PREFIX", "")
    val bucketName = Utils.getEnvVar("AWS_S3_BUCKET_NAME", "")
    val region = Utils.getEnvVar("AWS_S3_REGION", "ap-south-1")

    companion object {
        const val CONFIG_FILE = "/tmp/config.json"
        const val RUNTIME_FILE = "/tmp/runtime.json"
    }

    /**
     * Download any configuration files from S3 into the running container.
     *
     * @return the synced configuration, if any, otherwise null
     */
    inline fun <reified T : CustomConfig> sync(): T? {
        val s3Sync = S3Sync(bucketName, region, logger)
        val anySynced = s3Sync.copyFromS3(configPrefix, localPath)
        return if (anySynced) {
            Utils.parseConfig(File(CONFIG_FILE).readText(), File(RUNTIME_FILE).readText())
        } else {
            null
        }
    }
}
