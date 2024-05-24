/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

import com.atlan.pkg.model.Credential

data class GCSCredential(val from: Credential) {
    val projectId = from.username ?: ""
    val serviceAccountJson = from.password ?: ""
    val bucket = (from.extra?.get("gcs_bucket") ?: "") as String
    val objectPrefix = (from.extra?.get("gcs_prefix") ?: "") as String
    val objectKey = (from.extra?.get("gcs_key") ?: "") as String
}
