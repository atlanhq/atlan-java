/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.model

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Captures the details of credentials once they have been retrieved from the vault.
 *
 * @param authType connectivity option that was selected
 * @param host hostname that was provided as part of sensitive credentials (if any)
 * @param port port that was provided as part of sensitive credentials (if any)
 * @param username sensitive details that were captured as a username
 * @param password sensitive details that were captured as a password
 * @param extra any additional sensitive details captured as part of the credential
 * @param connector the source defined in the credential config
 * @param connectorConfigName name of the credential configmap
 * @param connectorType the connectorType defined in the credential config
 * @param description TBC
 * @param connection TBC
 * @param id unique identifier (GUID) of this credential
 * @param name name of the workflow associated with this credential
 * @param isActive whether the credential is active (true) or not (false)
 * @param level TBC
 * @param metadata TBC
 * @param tenantId TBC
 * @param createdBy user who created the credential
 * @param createdAt time in milliseconds (epoch) at which the user created the credential
 * @param updatedAt time in milliseconds (epoch) at which a user last updated the credential
 * @param version unique name for the version of the credential
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Credential(
    val authType: String,
    val host: String?,
    val port: Int?,
    val username: String?,
    val password: String?,
    val extra: Map<String, Any>?,
    val connector: String,
    val connectorConfigName: String,
    val connectorType: String,
    val description: String?,
    val connection: Any?,
    val id: String,
    val name: String,
    val isActive: Boolean,
    val level: Any?,
    val metadata: Any?,
    val tenantId: String,
    val createdBy: String,
    val createdAt: Long,
    val updatedAt: Long,
    val version: String,
)
