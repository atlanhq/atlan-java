/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

import com.atlan.pkg.Utils
import com.atlan.pkg.Utils.getEnvVar
import com.atlan.pkg.model.Credential

/**
 * Credentials captured for ADLS connectivity.
 */
data class ADLSCredential(
    val from: Credential,
) {
    val clientId = from.username ?: ""

    /** Client secret provided, or the access key for Atlan's backing store if none was provided. */
    val clientSecret = Utils.getOrDefault((from.password ?: ""), getEnvVar("AZURE_STORAGE_ACCESS_KEY"))
    val tenantId = (from.extra?.get("azure_tenant_id") ?: "") as String

    /** Storage account provided, or the storage account for Atlan's backing store if none was provided. */
    val storageAccount = Utils.getOrDefault((from.extra?.get("storage_account_name") ?: "") as String, getEnvVar("AZURE_STORAGE_ACCOUNT"))

    /** Container name provided, or the default container used for Atlan's backing store if none was provided. */
    val containerName = Utils.getOrDefault((from.extra?.get("adls_container") ?: "") as String, getEnvVar("AZURE_STORAGE_CONTAINER_NAME"))
}
