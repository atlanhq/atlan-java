/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

import com.atlan.pkg.Utils
import com.atlan.pkg.Utils.getEnvVar
import com.atlan.pkg.model.Credential

data class S3Credential(val from: Credential) {
    val accessKey = from.username ?: ""
    val secretKey = from.password ?: ""
    val region = Utils.getOrDefault((from.extra?.get("region") ?: "") as String, getEnvVar("AWS_S3_REGION"))
    val bucket = Utils.getOrDefault((from.extra?.get("s3_bucket") ?: "") as String, getEnvVar("AWS_S3_BUCKET_NAME"))
    val objectPrefix = (from.extra?.get("s3_prefix") ?: "") as String
    val objectKey = (from.extra?.get("s3_key") ?: "") as String
}
