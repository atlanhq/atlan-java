/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.S3Bucket
import com.atlan.model.assets.S3Object
import com.atlan.model.assets.S3Prefix
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanStatus
import com.atlan.model.enums.S3ObjectLockMode
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.PersistentConnectionCache
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test handling of deferred creation details across strings for S3, including prefixes.
 */
class S3WithPrefixesTest : PackageTest("swp") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.S3

    private val testFile = "s3-with-prefixes.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for connections
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{CNAME}", conn1)
                output.appendText("$revised\n")
            }
        }
    }

    private val connectionAttrs: List<AtlanField> =
        listOf(
            Connection.NAME,
            Connection.CONNECTOR_TYPE,
            Connection.ADMIN_ROLES,
            Connection.ADMIN_GROUPS,
            Connection.ADMIN_USERS,
        )

    private val bucketAttrs: List<AtlanField> =
        listOf(
            S3Bucket.NAME,
            S3Bucket.CONNECTION_QUALIFIED_NAME,
            S3Bucket.CONNECTOR_TYPE,
            S3Bucket.S3OBJECT_COUNT,
            S3Bucket.S3ENCRYPTION,
            S3Bucket.S3BUCKET_VERSIONING_ENABLED,
            S3Bucket.AWS_REGION,
            S3Bucket.AWS_ACCOUNT_ID,
            S3Bucket.SOURCE_CREATED_AT,
            S3Bucket.AWS_TAGS,
        )

    private val prefixAttrs: List<AtlanField> =
        listOf(
            S3Prefix.NAME,
            S3Prefix.CONNECTION_QUALIFIED_NAME,
            S3Prefix.CONNECTOR_TYPE,
            S3Prefix.S3BUCKET_NAME,
            S3Prefix.S3BUCKET_QUALIFIED_NAME,
            S3Prefix.S3BUCKET,
            S3Prefix.S3PARENT_PREFIX_QUALIFIED_NAME,
            S3Prefix.S3PREFIX_HIERARCHY,
            S3Prefix.S3PARENT_PREFIX,
            S3Prefix.S3OBJECT_COUNT,
            S3Prefix.AWS_REGION,
            S3Prefix.AWS_ACCOUNT_ID,
            S3Prefix.SOURCE_CREATED_AT,
            S3Prefix.AWS_TAGS,
        )

    private val objectAttrs: List<AtlanField> =
        listOf(
            S3Object.NAME,
            S3Object.CONNECTION_QUALIFIED_NAME,
            S3Object.CONNECTOR_TYPE,
            S3Object.S3BUCKET_NAME,
            S3Object.S3BUCKET_QUALIFIED_NAME,
            S3Object.BUCKET,
            S3Object.S3PARENT_PREFIX_QUALIFIED_NAME,
            S3Object.S3PREFIX_HIERARCHY,
            S3Object.S3PREFIX,
            S3Object.AWS_REGION,
            S3Object.AWS_ACCOUNT_ID,
            S3Object.SOURCE_CREATED_AT,
            S3Object.AWS_TAGS,
            S3Object.S3OBJECT_KEY,
            S3Object.S3OBJECT_LAST_MODIFIED_TIME,
            S3Object.S3OBJECT_SIZE,
            S3Object.S3OBJECT_STORAGE_CLASS,
            S3Object.S3OBJECT_CONTENT_TYPE,
            S3Object.S3OBJECT_CONTENT_DISPOSITION,
            S3Object.S3OBJECT_VERSION_ID,
            S3Object.S3E_TAG,
            S3Object.S3OBJECT_LOCK_MODE,
            S3Object.S3OBJECT_LOCK_RETAIN_UNTIL,
            S3Object.S3OBJECT_LOCK_LEGAL_HOLD_ENABLED,
            S3Object.AWS_OWNER_ID,
            S3Object.AWS_OWNER_NAME,
        )

    override fun setup() {
        prepFile()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(conn1, conn1Type)
    }

    @Test
    fun connection1Created() {
        val found = Connection.findByName(client, conn1, conn1Type, connectionAttrs)
        assertNotNull(found)
        assertEquals(1, found.size)
        val c1 = found[0]
        assertEquals(conn1, c1.name)
        assertEquals(conn1Type, c1.connectorType)
    }

    @Test
    fun bucket1Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Bucket
                .select(client)
                .where(S3Bucket.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Bucket.NAME.eq("bucket-1"))
                .includesOnResults(bucketAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val b = found[0] as S3Bucket
        assertEquals("bucket-1", b.name)
        assertEquals(c1.qualifiedName, b.connectionQualifiedName)
        assertEquals(conn1Type, b.connectorType)
        assertEquals(5, b.s3ObjectCount)
        assertEquals("SSEAlgorithm - AES256", b.s3Encryption)
        assertTrue(b.s3BucketVersioningEnabled)
        assertEquals("us-east-1", b.awsRegion)
        assertEquals("123456789012", b.awsAccountId)
        assertEquals(1761647178499L, b.sourceCreatedAt)
        assertEquals(2, b.awsTags.size)
        assertEquals(3, b.s3Prefixes.size)
    }

    @Test
    fun bucket2Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Bucket
                .select(client)
                .where(S3Bucket.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Bucket.NAME.eq("bucket-2"))
                .includesOnResults(bucketAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val b = found[0] as S3Bucket
        assertEquals("bucket-2", b.name)
        assertEquals(c1.qualifiedName, b.connectionQualifiedName)
        assertEquals(conn1Type, b.connectorType)
        assertEquals(2, b.s3ObjectCount)
        assertEquals("AES256", b.s3Encryption)
        assertEquals("ap-south-1", b.awsRegion)
        assertEquals("1234567890", b.awsAccountId)
        assertEquals(1761647178499L, b.sourceCreatedAt)
        assertEquals(2, b.awsTags.size)
        assertTrue(b.s3Prefixes == null || b.s3Prefixes.isEmpty())
    }

    @Test
    fun prefix1Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Prefix
                .select(client)
                .where(S3Prefix.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Prefix.NAME.eq("prefix-1"))
                .includesOnResults(prefixAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val p = found[0] as S3Prefix
        assertEquals("prefix-1", p.name)
        assertEquals(c1.qualifiedName, p.connectionQualifiedName)
        assertEquals(conn1Type, p.connectorType)
        assertEquals("bucket-1", p.s3BucketName)
        assertEquals("${c1.qualifiedName}/bucket-1", p.s3BucketQualifiedName)
        assertNotNull(p.s3Bucket)
        assertEquals("bucket-1", p.s3Bucket.name)
        assertEquals(2, p.s3ObjectCount)
        assertEquals("us-east-1", p.awsRegion)
        assertEquals("123456789012", p.awsAccountId)
        assertEquals(1761647178499L, p.sourceCreatedAt)
    }

    @Test
    fun prefix2Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Prefix
                .select(client)
                .where(S3Prefix.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Prefix.NAME.eq("prefix-2"))
                .includesOnResults(prefixAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val p = found[0] as S3Prefix
        assertEquals("prefix-2", p.name)
        assertEquals(c1.qualifiedName, p.connectionQualifiedName)
        assertEquals(conn1Type, p.connectorType)
        assertEquals("bucket-1", p.s3BucketName)
        assertEquals("${c1.qualifiedName}/bucket-1", p.s3BucketQualifiedName)
        assertNotNull(p.s3Bucket)
        assertEquals("bucket-1", p.s3Bucket.name)
        assertEquals(1, p.s3PrefixCount)
        assertEquals("us-east-1", p.awsRegion)
        assertEquals("123456789012", p.awsAccountId)
        assertEquals(1761647178499L, p.sourceCreatedAt)
    }

    @Test
    fun prefix3Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Prefix
                .select(client)
                .where(S3Prefix.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Prefix.NAME.eq("prefix-3"))
                .includesOnResults(prefixAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val p = found[0] as S3Prefix
        assertEquals("prefix-3", p.name)
        assertEquals(c1.qualifiedName, p.connectionQualifiedName)
        assertEquals(conn1Type, p.connectorType)
        assertEquals("bucket-1", p.s3BucketName)
        assertEquals("${c1.qualifiedName}/bucket-1", p.s3BucketQualifiedName)
        assertNotNull(p.s3Bucket)
        assertEquals("bucket-1", p.s3Bucket.name)
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/", p.s3ParentPrefixQualifiedName)
        assertEquals(1, p.s3PrefixHierarchy.size)
        assertEquals("prefix-2", p.s3PrefixHierarchy[0]["name"])
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/", p.s3PrefixHierarchy[0]["qualifiedName"])
        assertNotNull(p.s3ParentPrefix)
        assertEquals("prefix-2", p.s3ParentPrefix.name)
        assertEquals(1, p.s3ObjectCount)
        assertEquals(1, p.s3PrefixCount)
        assertEquals("us-east-1", p.awsRegion)
        assertEquals("123456789012", p.awsAccountId)
        assertEquals(1761647178499L, p.sourceCreatedAt)
        assertEquals(2, p.awsTags.size)
    }

    @Test
    fun prefix4Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Prefix
                .select(client)
                .where(S3Prefix.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Prefix.NAME.eq("prefix-4"))
                .includesOnResults(prefixAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val p = found[0] as S3Prefix
        assertEquals("prefix-4", p.name)
        assertEquals(c1.qualifiedName, p.connectionQualifiedName)
        assertEquals(conn1Type, p.connectorType)
        assertEquals("bucket-1", p.s3BucketName)
        assertEquals("${c1.qualifiedName}/bucket-1", p.s3BucketQualifiedName)
        assertNotNull(p.s3Bucket)
        assertEquals("bucket-1", p.s3Bucket.name)
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/prefix-3/", p.s3ParentPrefixQualifiedName)
        assertEquals(2, p.s3PrefixHierarchy.size)
        assertEquals("prefix-3", p.s3PrefixHierarchy[0]["name"])
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/prefix-3/", p.s3PrefixHierarchy[0]["qualifiedName"])
        assertEquals("prefix-2", p.s3PrefixHierarchy[1]["name"])
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/", p.s3PrefixHierarchy[1]["qualifiedName"])
        assertNotNull(p.s3ParentPrefix)
        assertEquals("prefix-3", p.s3ParentPrefix.name)
        assertEquals(1, p.s3ObjectCount)
        assertEquals("us-east-1", p.awsRegion)
        assertEquals("123456789012", p.awsAccountId)
        assertEquals(1761647178499L, p.sourceCreatedAt)
    }

    @Test
    fun prefix5Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Prefix
                .select(client)
                .where(S3Prefix.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Prefix.NAME.eq("prefix-5"))
                .includesOnResults(prefixAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val p = found[0] as S3Prefix
        assertEquals("prefix-5", p.name)
        assertEquals(c1.qualifiedName, p.connectionQualifiedName)
        assertEquals(conn1Type, p.connectorType)
        assertEquals("bucket-1", p.s3BucketName)
        assertEquals("${c1.qualifiedName}/bucket-1", p.s3BucketQualifiedName)
        assertNotNull(p.s3Bucket)
        assertEquals("bucket-1", p.s3Bucket.name)
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/prefix-3/", p.s3ParentPrefixQualifiedName)
        assertEquals(1, p.s3ObjectCount)
        assertEquals("us-east-1", p.awsRegion)
        assertEquals("123456789012", p.awsAccountId)
        assertEquals(1761647178499L, p.sourceCreatedAt)
    }

    @Test
    fun object1Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Object
                .select(client)
                .where(S3Object.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Object.NAME.eq("object-1"))
                .includesOnResults(objectAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val o = found[0] as S3Object
        assertEquals("object-1", o.name)
        assertEquals(c1.qualifiedName, o.connectionQualifiedName)
        assertEquals(conn1Type, o.connectorType)
        assertEquals("bucket-1", o.s3BucketName)
        assertEquals("${c1.qualifiedName}/bucket-1", o.s3BucketQualifiedName)
        assertNotNull(o.bucket)
        assertEquals("bucket-1", o.bucket.name)
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-1/", o.s3ParentPrefixQualifiedName)
        assertEquals(1, o.s3PrefixHierarchy.size)
        assertEquals("prefix-1", o.s3PrefixHierarchy[0]["name"])
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-1/", o.s3PrefixHierarchy[0]["qualifiedName"])
        assertNotNull(o.s3Prefix)
        assertEquals("prefix-1", o.s3Prefix.name)
        assertEquals("us-east-1", o.awsRegion)
        assertEquals("123456789012", o.awsAccountId)
        assertEquals(1761647178499L, o.sourceCreatedAt)
        assertEquals("prefix-1/object-1", o.s3ObjectKey)
        assertEquals(1704067200000L, o.s3ObjectLastModifiedTime)
        assertEquals("text/csv", o.s3ObjectContentType)
        assertEquals("987654321098", o.awsOwnerId)
        assertEquals("Data Team", o.awsOwnerName)
    }

    @Test
    fun object2Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Object
                .select(client)
                .where(S3Object.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Object.NAME.eq("object-2"))
                .includesOnResults(objectAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val o = found[0] as S3Object
        assertEquals("object-2", o.name)
        assertEquals(c1.qualifiedName, o.connectionQualifiedName)
        assertEquals(conn1Type, o.connectorType)
        assertEquals("bucket-1", o.s3BucketName)
        assertEquals("${c1.qualifiedName}/bucket-1", o.s3BucketQualifiedName)
        assertNotNull(o.bucket)
        assertEquals("bucket-1", o.bucket.name)
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-1/", o.s3ParentPrefixQualifiedName)
        assertEquals(1, o.s3PrefixHierarchy.size)
        assertEquals("prefix-1", o.s3PrefixHierarchy[0]["name"])
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-1/", o.s3PrefixHierarchy[0]["qualifiedName"])
        assertNotNull(o.s3Prefix)
        assertEquals("prefix-1", o.s3Prefix.name)
        assertEquals("us-east-1", o.awsRegion)
        assertEquals("123456789012", o.awsAccountId)
        assertEquals(1761647178499L, o.sourceCreatedAt)
        assertEquals("prefix-1/object-2", o.s3ObjectKey)
        assertEquals("STANDARD", o.s3ObjectStorageClass)
        assertEquals("application/json", o.s3ObjectContentType)
        assertEquals("abc123def456", o.s3ETag)
    }

    @Test
    fun object3Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Object
                .select(client)
                .where(S3Object.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Object.NAME.eq("object-3"))
                .includesOnResults(objectAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val o = found[0] as S3Object
        assertEquals("object-3", o.name)
        assertEquals(c1.qualifiedName, o.connectionQualifiedName)
        assertEquals(conn1Type, o.connectorType)
        assertEquals("bucket-1", o.s3BucketName)
        assertEquals("${c1.qualifiedName}/bucket-1", o.s3BucketQualifiedName)
        assertNotNull(o.bucket)
        assertEquals("bucket-1", o.bucket.name)
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/prefix-3/", o.s3ParentPrefixQualifiedName)
        assertEquals(2, o.s3PrefixHierarchy.size)
        assertEquals("prefix-3", o.s3PrefixHierarchy[0]["name"])
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/prefix-3/", o.s3PrefixHierarchy[0]["qualifiedName"])
        assertEquals("prefix-2", o.s3PrefixHierarchy[1]["name"])
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/", o.s3PrefixHierarchy[1]["qualifiedName"])
        assertNotNull(o.s3Prefix)
        assertEquals("prefix-3", o.s3Prefix.name)
        assertEquals("us-east-1", o.awsRegion)
        assertEquals("123456789012", o.awsAccountId)
        assertEquals(1761647178499L, o.sourceCreatedAt)
        assertEquals("prefix-2/prefix-3/object-3", o.s3ObjectKey)
        assertEquals(1048576, o.s3ObjectSize)
        assertEquals("version-123", o.s3ObjectVersionId)
        assertEquals(S3ObjectLockMode.GOVERNANCE, o.s3ObjectLockMode)
        assertTrue(o.s3ObjectLockLegalHoldEnabled)
    }

    @Test
    fun object4Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Object
                .select(client)
                .where(S3Object.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Object.NAME.eq("object-4"))
                .includesOnResults(objectAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val o = found[0] as S3Object
        assertEquals("object-4", o.name)
        assertEquals(c1.qualifiedName, o.connectionQualifiedName)
        assertEquals(conn1Type, o.connectorType)
        assertEquals("bucket-1", o.s3BucketName)
        assertEquals("${c1.qualifiedName}/bucket-1", o.s3BucketQualifiedName)
        assertNotNull(o.bucket)
        assertEquals("bucket-1", o.bucket.name)
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/prefix-3/prefix-4/", o.s3ParentPrefixQualifiedName)
        assertEquals(3, o.s3PrefixHierarchy.size)
        assertEquals("prefix-4", o.s3PrefixHierarchy[0]["name"])
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/prefix-3/prefix-4/", o.s3PrefixHierarchy[0]["qualifiedName"])
        assertEquals("prefix-3", o.s3PrefixHierarchy[1]["name"])
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/prefix-3/", o.s3PrefixHierarchy[1]["qualifiedName"])
        assertEquals("prefix-2", o.s3PrefixHierarchy[2]["name"])
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-2/", o.s3PrefixHierarchy[2]["qualifiedName"])
        assertNotNull(o.s3Prefix)
        assertEquals("prefix-4", o.s3Prefix.name)
        assertEquals("us-east-1", o.awsRegion)
        assertEquals("123456789012", o.awsAccountId)
        assertEquals(1761647178499L, o.sourceCreatedAt)
        assertEquals(1, o.awsTags.size)
        assertEquals("prefix-2/prefix-3/prefix-4/object-4", o.s3ObjectKey)
        assertEquals(1704067200000L, o.s3ObjectLastModifiedTime)
        assertEquals("STANDARD", o.s3ObjectStorageClass)
    }

    @Test
    fun object5Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Object
                .select(client)
                .where(S3Object.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Object.NAME.eq("object-5"))
                .includesOnResults(objectAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val o = found[0] as S3Object
        assertEquals("object-5", o.name)
        assertEquals(c1.qualifiedName, o.connectionQualifiedName)
        assertEquals(conn1Type, o.connectorType)
        assertEquals("bucket-1", o.s3BucketName)
        assertEquals("${c1.qualifiedName}/bucket-1", o.s3BucketQualifiedName)
        assertNotNull(o.bucket)
        assertEquals("bucket-1", o.bucket.name)
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-5/", o.s3ParentPrefixQualifiedName)
        assertEquals(1, o.s3PrefixHierarchy.size)
        assertEquals("prefix-5", o.s3PrefixHierarchy[0]["name"])
        assertEquals("${c1.qualifiedName}/bucket-1/prefix-5/", o.s3PrefixHierarchy[0]["qualifiedName"])
        assertNotNull(o.s3Prefix)
        assertEquals("prefix-4", o.s3Prefix.name)
        assertEquals("us-east-1", o.awsRegion)
        assertEquals("123456789012", o.awsAccountId)
        assertEquals(1761647178499L, o.sourceCreatedAt)
        assertEquals("prefix-5/object-5", o.s3ObjectKey)
        assertEquals("attachment", o.s3ObjectContentDisposition)
        assertEquals(S3ObjectLockMode.COMPLIANCE, o.s3ObjectLockMode)
        assertEquals(1735689600000L, o.s3ObjectLockRetainUntil)
    }

    @Test
    fun object6Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Object
                .select(client)
                .where(S3Object.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Object.NAME.eq("object-6"))
                .includesOnResults(objectAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val o = found[0] as S3Object
        assertEquals("object-6", o.name)
        assertEquals(c1.qualifiedName, o.connectionQualifiedName)
        assertEquals(conn1Type, o.connectorType)
        assertEquals("bucket-2", o.s3BucketName)
        assertEquals("${c1.qualifiedName}/bucket-2", o.s3BucketQualifiedName)
        assertNotNull(o.bucket)
        assertEquals("bucket-2", o.bucket.name)
        assertEquals("ap-south-1", o.awsRegion)
        assertEquals("1234567890", o.awsAccountId)
        assertEquals(1761647178499L, o.sourceCreatedAt)
        assertEquals("object-6", o.s3ObjectKey)
        assertEquals(1048576, o.s3ObjectSize)
    }

    @Test
    fun object7Created() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            S3Object
                .select(client)
                .where(S3Object.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(S3Object.NAME.eq("object-7"))
                .includesOnResults(objectAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val o = found[0] as S3Object
        assertEquals("object-7", o.name)
        assertEquals(c1.qualifiedName, o.connectionQualifiedName)
        assertEquals(conn1Type, o.connectorType)
        assertEquals("bucket-2", o.s3BucketName)
        assertEquals("${c1.qualifiedName}/bucket-2", o.s3BucketQualifiedName)
        assertNotNull(o.bucket)
        assertEquals("bucket-2", o.bucket.name)
        assertEquals("ap-south-1", o.awsRegion)
        assertEquals("1234567890", o.awsAccountId)
        assertEquals(1761647178499L, o.sourceCreatedAt)
        assertEquals("object-7", o.s3ObjectKey)
    }

    @Test
    fun connectionCacheCreated() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val dbFile = Paths.get(testDirectory, "connection-cache", "${c1.qualifiedName}.sqlite").toFile()
        assertTrue(dbFile.isFile)
        assertTrue(dbFile.exists())
        val cache = PersistentConnectionCache(dbFile.path)
        val assets = cache.listAssets()
        assertNotNull(assets)
        assertFalse(assets.isEmpty())
        assertEquals(14, assets.size)
        assertEquals(setOf(S3Bucket.TYPE_NAME, S3Prefix.TYPE_NAME, S3Object.TYPE_NAME), assets.map { it.typeName }.toSet())
        assertEquals(2, assets.count { it.typeName == S3Bucket.TYPE_NAME })
        assertEquals(5, assets.count { it.typeName == S3Prefix.TYPE_NAME })
        assertEquals(7, assets.count { it.typeName == S3Object.TYPE_NAME })
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
