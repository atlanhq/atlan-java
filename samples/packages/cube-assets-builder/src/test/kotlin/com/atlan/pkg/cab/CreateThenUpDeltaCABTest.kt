/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Cube
import com.atlan.model.assets.CubeDimension
import com.atlan.model.assets.CubeField
import com.atlan.model.assets.CubeHierarchy
import com.atlan.model.assets.Readme
import com.atlan.model.assets.Schema
import com.atlan.model.core.AtlanAsyncMutator
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.AtlanTagColor
import com.atlan.model.enums.CertificateStatus
import com.atlan.model.fields.AtlanField
import com.atlan.model.typedefs.AtlanTagDef
import com.atlan.net.RequestOptions
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.PersistentConnectionCache
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Test creation of cube assets followed by an upsert of the same cube assets, including calculating the delta.
 */
class CreateThenUpDeltaCABTest : PackageTest("ctud") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.AZURE_ANALYSIS_SERVICES
    private val tag1 = makeUnique("t1")
    private val tag2 = makeUnique("t2")

    private val testFile = "input.csv"
    private val revisedFile = "revised.csv"

    private val files =
        listOf(
            testFile,
            revisedFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for connections
        val input = Paths.get("src", "test", "resources", "assets.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("essbase", "azure-analysis-services")
                        .replace("{{CONNECTION1}}", conn1)
                        .replace("{{TAG1}}", tag1)
                        .replace("{{TAG2}}", tag2)
                        .replace("{{API_TOKEN_USER}}", client.users.currentUser.username)
                output.appendText("$revised\n")
            }
        }
    }

    private fun modifyFile() {
        // Modify the loaded file to make some changes (testing upsert)
        val input = Paths.get(testDirectory, testFile).toFile()
        val output = Paths.get(testDirectory, revisedFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                if (!line.contains("TEST_HIERARCHY2")) {
                    if (!line.contains("COL2")) {
                        val revised =
                            line
                                .replace("Test ", "Revised ")
                                .replace("{{API_TOKEN_USER}}", client.users.currentUser.username)
                        output.appendText("$revised\n")
                    } else {
                        val revised =
                            line
                                .replace("{{API_TOKEN_USER}}", client.users.currentUser.username)
                        output.appendText("$revised\n")
                    }
                }
            }
        }
    }

    private fun createTags() {
        val maxNetworkRetries = 30
        val t1 = AtlanTagDef.creator(tag1, AtlanIcon.DATABASE, AtlanTagColor.GREEN).build()
        val t2 = AtlanTagDef.creator(tag2, AtlanIcon.COLUMNS, AtlanTagColor.RED).build()
        client.typeDefs.create(
            listOf(t1, t2),
            RequestOptions.from(client).maxNetworkRetries(maxNetworkRetries).build(),
        )
    }

    private val connectionAttrs: List<AtlanField> =
        listOf(
            Connection.NAME,
            Connection.CONNECTOR_TYPE,
            Connection.ADMIN_ROLES,
            Connection.ADMIN_GROUPS,
            Connection.ADMIN_USERS,
        )

    private val cubeAttrs: List<AtlanField> =
        listOf(
            Cube.NAME,
            Cube.CONNECTION_QUALIFIED_NAME,
            Cube.CONNECTOR_TYPE,
            Cube.DISPLAY_NAME,
            Cube.DESCRIPTION,
            Cube.CUBE_DIMENSIONS,
            Cube.CUBE_DIMENSION_COUNT,
        )

    private val dimensionAttrs: List<AtlanField> =
        listOf(
            CubeDimension.NAME,
            CubeDimension.CONNECTION_QUALIFIED_NAME,
            CubeDimension.CONNECTOR_TYPE,
            CubeDimension.DISPLAY_NAME,
            CubeDimension.DESCRIPTION,
            CubeDimension.CUBE_NAME,
            CubeDimension.CUBE_QUALIFIED_NAME,
            CubeDimension.CUBE_HIERARCHIES,
            CubeDimension.CUBE_HIERARCHY_COUNT,
        )

    private val hierarchyAttrs: List<AtlanField> =
        listOf(
            CubeHierarchy.NAME,
            CubeHierarchy.STATUS,
            CubeHierarchy.CONNECTION_QUALIFIED_NAME,
            CubeHierarchy.CONNECTOR_TYPE,
            CubeHierarchy.CUBE_NAME,
            CubeHierarchy.CUBE_QUALIFIED_NAME,
            CubeHierarchy.CUBE_DIMENSION_NAME,
            CubeHierarchy.CUBE_DIMENSION_QUALIFIED_NAME,
            CubeHierarchy.DISPLAY_NAME,
            CubeHierarchy.DESCRIPTION,
            CubeHierarchy.CERTIFICATE_STATUS,
            CubeHierarchy.CERTIFICATE_STATUS_MESSAGE,
            CubeHierarchy.README,
            CubeHierarchy.ATLAN_TAGS,
            CubeHierarchy.CUBE_FIELDS,
            CubeHierarchy.CUBE_FIELD_COUNT,
        )

    private val fieldAttrs: List<AtlanField> =
        listOf(
            CubeField.NAME,
            CubeField.STATUS,
            CubeField.CONNECTION_QUALIFIED_NAME,
            CubeField.CONNECTOR_TYPE,
            CubeField.CUBE_NAME,
            CubeField.CUBE_QUALIFIED_NAME,
            CubeField.CUBE_DIMENSION_NAME,
            CubeField.CUBE_DIMENSION_QUALIFIED_NAME,
            CubeField.CUBE_HIERARCHY_NAME,
            CubeField.CUBE_HIERARCHY_QUALIFIED_NAME,
            CubeField.DISPLAY_NAME,
            CubeField.DESCRIPTION,
            CubeField.CUBE_FIELD_LEVEL,
            CubeField.CUBE_FIELD_GENERATION,
            CubeField.CUBE_PARENT_FIELD,
            CubeField.CUBE_NESTED_FIELDS,
            CubeField.CUBE_SUB_FIELD_COUNT,
        )

    override fun setup() {
        prepFile()
        createTags()
        runCustomPackage(
            CubeAssetsBuilderCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(conn1, conn1Type)
        removeTag(tag1)
        removeTag(tag2)
    }

    @Test(groups = ["cab.ctud.create"])
    fun connection1Created() {
        validateConnection()
    }

    private fun validateConnection() {
        val found = Connection.findByName(client, conn1, conn1Type, connectionAttrs)
        assertNotNull(found)
        assertEquals(1, found.size)
        val c1 = found[0]
        assertEquals(conn1, c1.name)
        assertEquals(conn1Type, c1.connectorType)
        val adminRoleId = client.roleCache.getIdForSid("\$admin")
        assertEquals(setOf(adminRoleId), c1.adminRoles)
        val apiToken = client.users.currentUser.username
        assertEquals(setOf("chris", apiToken), c1.adminUsers)
        assertEquals(setOf("admins"), c1.adminGroups)
    }

    @Test(groups = ["cab.ctud.create"])
    fun cube1Created() {
        validateCube("Test cube")
    }

    private fun validateCube(displayName: String) {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Cube
                .select(client)
                .where(Cube.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(cubeAttrs)
                .includeOnRelations(Schema.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val cube = found[0] as Cube
        assertEquals("TEST_CUBE", cube.name)
        assertEquals(displayName, cube.displayName)
        assertEquals(c1.qualifiedName, cube.connectionQualifiedName)
        assertEquals(conn1Type, cube.connectorType)
        assertEquals(1, cube.cubeDimensions.size)
        assertEquals("TEST_DIM", cube.cubeDimensions.first().name)
        assertEquals(1, cube.cubeDimensionCount)
    }

    @Test(groups = ["cab.ctud.create"])
    fun dim1Created() {
        validateDimension("Test dimension")
    }

    private fun validateDimension(
        displayName: String,
        expectedCount: Long = 2,
    ) {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            CubeDimension
                .select(client)
                .where(CubeDimension.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(dimensionAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val dim = found[0] as CubeDimension
        assertEquals("TEST_DIM", dim.name)
        assertEquals(displayName, dim.displayName)
        assertEquals(c1.qualifiedName, dim.connectionQualifiedName)
        assertEquals(conn1Type, dim.connectorType)
        assertEquals("TEST_CUBE", dim.cubeName)
        assertTrue(dim.cubeQualifiedName.endsWith("/TEST_CUBE"))
        assertEquals(expectedCount.toInt(), dim.cubeHierarchies.size)
        val hierarchyNames =
            dim.cubeHierarchies
                .stream()
                .map { it.name }
                .toList()
        assertTrue(hierarchyNames.contains("TEST_HIERARCHY1"))
        if (expectedCount > 1) assertTrue(hierarchyNames.contains("TEST_HIERARCHY2"))
        assertEquals(expectedCount, dim.cubeHierarchyCount)
    }

    @Test(groups = ["cab.ctud.create"])
    fun hierarchy1Created() {
        validateHierarchy("Test hierarchy1")
    }

    private fun validateHierarchy(displayName: String) {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            CubeHierarchy
                .select(client)
                .where(CubeHierarchy.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(CubeHierarchy.NAME.eq("TEST_HIERARCHY1"))
                .includesOnResults(hierarchyAttrs)
                .includeOnRelations(Asset.NAME)
                .includeOnRelations(Readme.DESCRIPTION)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val hier = found[0] as CubeHierarchy
        assertEquals("TEST_HIERARCHY1", hier.name)
        assertEquals(displayName, hier.displayName)
        assertEquals(c1.qualifiedName, hier.connectionQualifiedName)
        assertEquals(conn1Type, hier.connectorType)
        assertEquals("TEST_CUBE", hier.cubeName)
        assertTrue(hier.cubeQualifiedName.endsWith("/TEST_CUBE"))
        assertEquals("TEST_DIM", hier.cubeDimensionName)
        assertTrue(hier.cubeDimensionQualifiedName.endsWith("/TEST_CUBE/TEST_DIM"))
        assertEquals(CertificateStatus.VERIFIED, hier.certificateStatus)
        assertEquals("Ready to use", hier.certificateStatusMessage)
        assertEquals("<h1>Table readme</h1>", hier.readme.description)
        assertEquals(2, hier.atlanTags.size)
        val tagNames =
            hier.atlanTags
                .stream()
                .map { it.typeName }
                .toList()
        assertTrue(tagNames.contains(tag1))
        assertTrue(tagNames.contains(tag2))
        hier.atlanTags.forEach { tag ->
            when (tag.typeName) {
                tag1 -> {
                    assertTrue(tag.propagate)
                    assertTrue(tag.removePropagationsOnEntityDelete)
                    assertFalse(tag.restrictPropagationThroughLineage)
                }

                tag2 -> {
                    assertFalse(tag.propagate)
                }
            }
        }
        assertEquals(3, hier.cubeFields.size)
        val colNames =
            hier.cubeFields
                .stream()
                .map { it.name }
                .toList()
        assertTrue(colNames.contains("COL1"))
        assertTrue(colNames.contains("COL2"))
        assertTrue(colNames.contains("COL3"))
        assertEquals(3, hier.cubeFieldCount)
        AtlanAsyncMutator.blockForBackgroundTasks(client, listOf(hier.guid), 60, logger)
    }

    @Test(groups = ["cab.ctud.create"])
    fun fieldsForHierarchy1Created() {
        validateFieldsForHierarchy1("Test field 1", "Test field 2", "Test field 3")
    }

    private fun validateFieldsForHierarchy1(
        displayCol1: String,
        displayCol2: String,
        displayCol3: String,
    ) {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            CubeField
                .select(client)
                .where(CubeField.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(CubeField.CUBE_HIERARCHY_NAME.eq("TEST_HIERARCHY1"))
                .includesOnResults(fieldAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 3)
        val found = response.assets
        assertEquals(3, found.size)
        val fieldNames =
            found
                .stream()
                .map { it.name }
                .toList()
        assertTrue(fieldNames.contains("COL1"))
        assertTrue(fieldNames.contains("COL2"))
        assertTrue(fieldNames.contains("COL3"))
        found.forEach { field ->
            field as CubeField
            assertEquals(c1.qualifiedName, field.connectionQualifiedName)
            assertEquals(conn1Type, field.connectorType)
            assertEquals("TEST_CUBE", field.cubeName)
            assertTrue(field.cubeQualifiedName.endsWith("/TEST_CUBE"))
            assertEquals("TEST_DIM", field.cubeDimensionName)
            assertTrue(field.cubeDimensionQualifiedName.endsWith("/TEST_CUBE/TEST_DIM"))
            assertEquals("TEST_HIERARCHY1", field.cubeHierarchyName)
            assertTrue(field.cubeHierarchyQualifiedName.endsWith("/TEST_CUBE/TEST_DIM/TEST_HIERARCHY1"))
            when (field.name) {
                "COL1" -> {
                    assertEquals(displayCol1, field.displayName)
                    assertNull(field.cubeParentField)
                    assertEquals(3, field.cubeFieldLevel)
                    assertEquals(1, field.cubeFieldGeneration)
                    assertNotNull(field.cubeNestedFields)
                    assertEquals(1, field.cubeNestedFields.size)
                    assertEquals(1, field.cubeSubFieldCount)
                }

                "COL2" -> {
                    assertEquals(displayCol2, field.displayName)
                    assertNotNull(field.cubeParentField)
                    assertEquals("COL1", field.cubeParentField.name)
                    assertEquals(2, field.cubeFieldLevel)
                    assertEquals(2, field.cubeFieldGeneration)
                    assertNotNull(field.cubeNestedFields)
                    assertEquals(1, field.cubeNestedFields.size)
                    assertEquals(1, field.cubeSubFieldCount)
                }

                "COL3" -> {
                    assertEquals(displayCol3, field.displayName)
                    assertNotNull(field.cubeParentField)
                    assertEquals("COL2", field.cubeParentField.name)
                    assertEquals(1, field.cubeFieldLevel)
                    assertEquals(3, field.cubeFieldGeneration)
                    assertTrue(field.cubeNestedFields.isNullOrEmpty())
                    assertEquals(0, field.cubeSubFieldCount)
                }
            }
        }
    }

    @Test(groups = ["cab.ctud.create"])
    fun hierarchy2Created() {
        validateHierarchy2()
    }

    private fun validateHierarchy2() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            CubeHierarchy
                .select(client)
                .where(CubeHierarchy.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(CubeHierarchy.NAME.eq("TEST_HIERARCHY2"))
                .includesOnResults(hierarchyAttrs)
                .includeOnRelations(Asset.NAME)
                .includeOnRelations(Readme.DESCRIPTION)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val hier = found[0] as CubeHierarchy
        assertEquals("TEST_HIERARCHY2", hier.name)
        assertEquals("Test hierarchy2", hier.displayName)
        assertEquals(c1.qualifiedName, hier.connectionQualifiedName)
        assertEquals(conn1Type, hier.connectorType)
        assertEquals(CertificateStatus.DRAFT, hier.certificateStatus)
        assertTrue(hier.certificateStatusMessage.isNullOrBlank())
        assertEquals("<h2>View readme</h2>", hier.readme.description)
        assertEquals(1, hier.atlanTags.size)
        assertEquals(tag1, hier.atlanTags.first().typeName)
        assertTrue(hier.atlanTags.first().propagate)
        assertTrue(hier.atlanTags.first().removePropagationsOnEntityDelete)
        assertTrue(hier.atlanTags.first().restrictPropagationThroughLineage)
        val fieldNames =
            hier.cubeFields
                .stream()
                .map { it.name }
                .toList()
        assertTrue(fieldNames.contains("COL4"))
        assertTrue(fieldNames.contains("COL5"))
        assertEquals(2, hier.cubeFieldCount)
        AtlanAsyncMutator.blockForBackgroundTasks(client, listOf(hier.guid), 60, logger)
    }

    @Test(groups = ["cab.ctud.create"])
    fun fieldsForHierarchy2Created() {
        validateFieldsForHierarchy2()
    }

    private fun validateFieldsForHierarchy2() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            CubeField
                .select(client)
                .where(CubeField.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(CubeField.CUBE_HIERARCHY_NAME.eq("TEST_HIERARCHY2"))
                .includesOnResults(fieldAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        val found = response.assets
        assertEquals(2, found.size)
        val fieldNames =
            found
                .stream()
                .map { it.name }
                .toList()
        assertTrue(fieldNames.contains("COL4"))
        assertTrue(fieldNames.contains("COL5"))
        found.forEach { field ->
            field as CubeField
            assertEquals(c1.qualifiedName, field.connectionQualifiedName)
            assertEquals(conn1Type, field.connectorType)
            assertEquals("TEST_CUBE", field.cubeName)
            assertTrue(field.cubeQualifiedName.endsWith("/TEST_CUBE"))
            assertEquals("TEST_DIM", field.cubeDimensionName)
            assertTrue(field.cubeDimensionQualifiedName.endsWith("/TEST_CUBE/TEST_DIM"))
            assertEquals("TEST_HIERARCHY2", field.cubeHierarchyName)
            assertTrue(field.cubeHierarchyQualifiedName.endsWith("/TEST_CUBE/TEST_DIM/TEST_HIERARCHY2"))
            when (field.name) {
                "COL4" -> {
                    assertEquals("Test field 4", field.displayName)
                    assertNull(field.cubeParentField)
                    assertEquals(2, field.cubeFieldLevel)
                    assertEquals(1, field.cubeFieldGeneration)
                    assertNotNull(field.cubeNestedFields)
                    assertEquals(1, field.cubeNestedFields.size)
                    assertEquals(1, field.cubeSubFieldCount)
                }

                "COL2" -> {
                    assertEquals("Test field 5", field.displayName)
                    assertNotNull(field.cubeParentField)
                    assertEquals("COL4", field.cubeParentField.name)
                    assertEquals(1, field.cubeFieldLevel)
                    assertEquals(2, field.cubeFieldGeneration)
                    assertTrue(field.cubeNestedFields.isNullOrEmpty())
                    assertEquals(0, field.cubeSubFieldCount)
                }
            }
        }
    }

    @Test(groups = ["cab.ctud.create"])
    fun connectionCacheCreated() {
        validateConnectionCache()
    }

    private fun validateConnectionCache(created: Boolean = true) {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val dbFile = Paths.get(testDirectory, "connection-cache", "${c1.qualifiedName}.sqlite").toFile()
        assertTrue(dbFile.isFile)
        assertTrue(dbFile.exists())
        val cache = PersistentConnectionCache(dbFile.path)
        val assets = cache.listAssets()
        assertNotNull(assets)
        assertFalse(assets.isEmpty())
        if (created) {
            assertEquals(9, assets.size)
            assertEquals(setOf(Cube.TYPE_NAME, CubeDimension.TYPE_NAME, CubeHierarchy.TYPE_NAME, CubeField.TYPE_NAME), assets.map { it.typeName }.toSet())
            assertEquals(5, assets.count { it.typeName == CubeField.TYPE_NAME })
            assertEquals(2, assets.count { it.typeName == CubeHierarchy.TYPE_NAME })
            assertEquals(1, assets.count { it.typeName == CubeDimension.TYPE_NAME })
            assertEquals(1, assets.count { it.typeName == Cube.TYPE_NAME })
        } else {
            assertEquals(6, assets.size)
            assertEquals(setOf(Cube.TYPE_NAME, CubeDimension.TYPE_NAME, CubeHierarchy.TYPE_NAME, CubeField.TYPE_NAME), assets.map { it.typeName }.toSet())
            assertEquals(3, assets.count { it.typeName == CubeField.TYPE_NAME })
            assertEquals(1, assets.count { it.typeName == CubeHierarchy.TYPE_NAME })
            assertEquals(1, assets.count { it.typeName == CubeDimension.TYPE_NAME })
            assertEquals(1, assets.count { it.typeName == Cube.TYPE_NAME })
        }
    }

    @Test(groups = ["cab.ctud.runUpdate"], dependsOnGroups = ["cab.ctud.create"])
    fun upsertRevisions() {
        modifyFile()
        runCustomPackage(
            CubeAssetsBuilderCfg(
                assetsFile = Paths.get(testDirectory, revisedFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
                deltaSemantic = "full",
                deltaReloadCalculation = "changes",
                previousFileDirect = Paths.get(testDirectory, testFile).toString(),
                deltaRemovalType = "purge",
            ),
            Importer::main,
        )
        // Allow Elastic index and deletion to become consistent
        Thread.sleep(10000)
    }

    @Test(groups = ["cab.ctud.update"], dependsOnGroups = ["cab.ctud.runUpdate"])
    fun connectionUnchanged() {
        validateConnection()
    }

    @Test(groups = ["cab.ctud.update"], dependsOnGroups = ["cab.ctud.runUpdate"])
    fun cubeChanged() {
        validateCube("Revised cube")
    }

    @Test(groups = ["cab.ctud.update"], dependsOnGroups = ["cab.ctud.runUpdate"])
    fun dimensionChanged() {
        validateDimension("Revised dimension", 1)
    }

    @Test(groups = ["cab.ctud.update"], dependsOnGroups = ["cab.ctud.runUpdate"])
    fun hierarchy1Changed() {
        validateHierarchy("Revised hierarchy1")
    }

    @Test(groups = ["cab.ctud.update"], dependsOnGroups = ["cab.ctud.runUpdate"])
    fun fieldsForHierarchy1Changed() {
        validateFieldsForHierarchy1("Revised field 1", "Test field 2", "Test field 3")
    }

    @Test(groups = ["cab.ctud.update"], dependsOnGroups = ["cab.ctud.runUpdate"])
    fun hierarchy2Gone() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            CubeHierarchy
                .select(client)
                .where(CubeHierarchy.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(CubeHierarchy.NAME.eq("TEST_HIERARCHY2"))
                .includesOnResults(hierarchyAttrs)
                .includeOnRelations(Asset.NAME)
                .includeOnRelations(Readme.DESCRIPTION)
                .toRequest()
        val response = retrySearchUntil(request, 0)
        assertTrue(response.assets.isNullOrEmpty())
    }

    @Test(groups = ["cab.ctud.update"], dependsOnGroups = ["cab.ctud.runUpdate"])
    fun fieldsForHierarchy2Gone() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            CubeField
                .select(client)
                .where(CubeField.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(CubeField.CUBE_HIERARCHY_NAME.eq("TEST_HIERARCHY2"))
                .includesOnResults(fieldAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 0)
        assertTrue(response.assets.isNullOrEmpty())
    }

    @Test(groups = ["cab.ctud.update"], dependsOnGroups = ["cab.ctud.runUpdate"])
    fun connectionCacheUpdated() {
        validateConnectionCache(false)
    }

    @Test(dependsOnGroups = ["cab.ctud.*"])
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(dependsOnGroups = ["cab.ctud.*"])
    fun previousRunFilesCreated() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val directory = Paths.get(testDirectory, Importer.PREVIOUS_FILES_PREFIX, c1.qualifiedName).toFile()
        assertNotNull(directory)
        assertTrue(directory.isDirectory)
        val files = directory.walkTopDown().filter { it.isFile }.toList()
        assertEquals(2, files.size)
    }

    @Test(dependsOnGroups = ["cab.ctud.*"])
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
