/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package objectstore

import com.atlan.pkg.objectstore.S3Sync
import mu.KotlinLogging
import java.io.IOException

/**
 * Entry point for the child JVM in [S3SyncMultipartUploadTest]: uploads one file through the public
 * [S3Sync.uploadTo] path and, like `Utils.updateConnectionCache`, catches the IOException and returns from main.
 * The endpoint comes from the `aws.endpointUrlS3` system property set by the parent.
 */
object S3UploadChild {
    const val UPLOADED = "CHILD_RESULT=UPLOADED"
    const val FAILED = "CHILD_RESULT=FAILED"
    const val RETURNED = "CHILD_MAIN_RETURNED"

    @JvmStatic
    fun main(args: Array<String>) {
        val (localFile, remoteKey) = args
        val logger = KotlinLogging.logger {}
        val sync = S3Sync("test-bucket", "us-east-1", logger, "test-access-key", "test-secret-key")
        try {
            sync.uploadTo(localFile, remoteKey)
            println(UPLOADED)
        } catch (e: IOException) {
            println("$FAILED ${e.cause}")
        }
        println(RETURNED)
    }
}
