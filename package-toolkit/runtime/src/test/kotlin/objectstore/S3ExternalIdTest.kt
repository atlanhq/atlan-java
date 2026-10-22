/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package objectstore

import com.atlan.pkg.model.Credential
import com.atlan.pkg.objectstore.S3Credential
import com.atlan.pkg.objectstore.S3Sync
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Coverage for CSA-641: an external ID captured on the S3 credential must be sent when assuming the role,
 * and must be left off the request when none is provided.
 */
class S3ExternalIdTest {
    private fun credential(extra: Map<String, Any>?) =
        Credential(
            authType = "s3",
            host = null,
            port = null,
            username = null,
            password = null,
            extra = extra,
            connector = "csa-connectors-objectstore",
            connectorConfigName = "csa-connectors-objectstore",
            connectorType = "rest",
            description = null,
            connection = null,
            id = "credential_123",
            name = "example-credential",
            isActive = true,
            level = null,
            metadata = null,
            tenantId = "example-tenant",
            createdBy = "example-user",
            createdAt = 0L,
            updatedBy = null,
            updatedAt = null,
            version = "v1",
        )

    @Test
    fun externalIdReadFromExtra() {
        val s3 =
            S3Credential(
                credential(
                    mapOf(
                        "aws_role_arn" to "arn:aws:iam::123456789012:role/example-role",
                        "aws_external_id" to "example-external-id",
                    ),
                ),
            )
        assertEquals("arn:aws:iam::123456789012:role/example-role", s3.roleArn)
        assertEquals("example-external-id", s3.externalId)
    }

    @Test
    fun externalIdDefaultsToEmpty() {
        assertEquals("", S3Credential(credential(mapOf("aws_role_arn" to "arn:aws:iam::123456789012:role/example-role"))).externalId)
        assertEquals("", S3Credential(credential(null)).externalId)
    }

    @Test
    fun requestIncludesExternalIdWhenProvided() {
        val request = S3Sync.buildAssumeRoleRequest("arn:aws:iam::123456789012:role/example-role", "example-external-id")
        assertEquals("arn:aws:iam::123456789012:role/example-role", request.roleArn())
        assertEquals("example-external-id", request.externalId())
    }

    @Test
    fun requestOmitsExternalIdWhenBlank() {
        assertNull(S3Sync.buildAssumeRoleRequest("arn:aws:iam::123456789012:role/example-role").externalId())
        assertNull(S3Sync.buildAssumeRoleRequest("arn:aws:iam::123456789012:role/example-role", " ").externalId())
    }
}
