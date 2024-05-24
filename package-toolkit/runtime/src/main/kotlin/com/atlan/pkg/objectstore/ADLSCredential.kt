/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.objectstore

import com.atlan.pkg.model.Credential

data class ADLSCredential(val from: Credential) {
    val clientId = from.username ?: ""
    val clientSecret = from.password ?: ""
    val tenantId = (from.extra?.get("azure_tenant_id") ?: "") as String
    val storageAccount = (from.extra?.get("storage_account_name") ?: "") as String
    val containerName = (from.extra?.get("adls_container") ?: "") as String
    val objectPrefix = (from.extra?.get("adls_prefix") ?: "") as String
    val objectKey = (from.extra?.get("adls_key") ?: "") as String
}
