/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

import com.atlan.pkg.Utils
import com.atlan.pkg.Utils.getEnvVar
import com.atlan.pkg.model.Credential

/**
 * Credentials captured for GCS connectivity.
 */
data class GCSCredential(
    val from: Credential,
) {
    /** GCP project ID provided, or the default project for Atlan's backing store if none was provided. */
    val projectId = Utils.getOrDefault(from.username ?: "", getEnvVar("GCP_PROJECT_ID"))
    val serviceAccountJson = from.password ?: ""

    /** Bucket provided, or the default bucket for Atlan's backing store if none was provided. */
    val bucket = Utils.getOrDefault((from.extra?.get("gcs_bucket") ?: "") as String, getEnvVar("GCP_STORAGE_BUCKET"))
}
