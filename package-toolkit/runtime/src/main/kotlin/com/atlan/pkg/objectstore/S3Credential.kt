/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

import com.atlan.pkg.Utils
import com.atlan.pkg.Utils.getEnvVar
import com.atlan.pkg.model.Credential

/**
 * Credentials captured for S3 connectivity.
 */
data class S3Credential(
    val from: Credential,
) {
    val accessKey = from.username ?: ""
    val secretKey = from.password ?: ""

    /** Region provided, or the region of Atlan's backing store if none was provided. */
    val region = Utils.getOrDefault((from.extra?.get("region") ?: "") as String, getEnvVar("AWS_S3_REGION"))

    /** Bucket provided, or the default bucket for Atlan's backing store if none was provided. */
    val bucket = Utils.getOrDefault((from.extra?.get("bucket") ?: "") as String, Utils.getOrDefault((from.extra?.get("s3_bucket") ?: "") as String, getEnvVar("AWS_S3_BUCKET_NAME")))

    /** Prefix provided or empty string */
    val prefix = (from.extra?.get("prefix") ?: "") as String

    /** Role ARN or empty string */
    val roleArn = (from.extra?.get("aws_role_arn") ?: "") as String
}
