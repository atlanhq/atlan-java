/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

import com.atlan.pkg.Utils.getEnvVar
import com.atlan.pkg.model.Credential

data class S3Credential(val from: Credential) {
    val accessKey = from.username ?: ""
    val secretKey = from.password ?: ""
    val region = (from.extra?.get("region") ?: getEnvVar("AWS_S3_REGION")) as String
    val bucket = (from.extra?.get("s3_bucket") ?: getEnvVar("AWS_S3_BUCKET_NAME")) as String
    val objectPrefix = (from.extra?.get("s3_prefix") ?: "") as String
    val objectKey = (from.extra?.get("s3_key") ?: "") as String
}
