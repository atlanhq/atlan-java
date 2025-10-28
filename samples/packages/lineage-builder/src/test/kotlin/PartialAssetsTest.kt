/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.LineageProcess
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanAnnouncementType
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanLineageDirection
import com.atlan.model.enums.CertificateStatus
import com.atlan.model.lineage.FluentLineage
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.PersistentConnectionCache
import com.atlan.pkg.lb.Loader
import org.testng.Assert
import java.io.File
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Test creation of lineage using partial assets.
 */
class PartialAssetsTest : PackageTest("pa") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val connectionName = makeUnique("c1")
    private val connectorType = AtlanConnectorType.MATILLION

    private val testFile = "input.csv"
    private val revisedFile = "revised.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for connection
        val input = Paths.get("src", "test", "resources", "test_lineage.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{CONNECTION}", connectionName)
                output.appendText("$revised\n")
            }
        }
    }

    private fun modifyFile() {
        // Modify the loaded file to make some changes (testing case-insensitive matching)
        val input = Paths.get(testDirectory, testFile).toFile()
        val output = Paths.get(testDirectory, revisedFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                if (line.contains("db2/schema2/target_view")) {
                    val revised =
                        line
                            .replace("db2/schema2/target_view", "DB2/schema2/Target_View")
                    output.appendText("$revised\n")
                } else {
                    output.appendText("$line\n")
                }
            }
        }
    }

    private fun createConnection(): Connection {
        val c1 = Connection.creator(client, connectionName, connectorType).build()
        val response = c1.save(client).block()
        return response.getResult(c1)
    }

    override fun setup() {
        createConnection()
        prepFile()
        runCustomPackage(
            LineageBuilderCfg(
                lineageFile = Paths.get(testDirectory, testFile).toString(),
                lineageUpsertSemantic = "partial",
                lineageFailOnErrors = false,
            ),
            Loader::main,
        )
        runCustomPackage(
            AssetImportCfg(
                assetsFile = "$testDirectory${File.separator}transformed.csv",
                assetsUpsertSemantic = "partial",
                assetsFailOnErrors = true,
                assetsConfig = "advanced",
                trackBatches = true,
            ),
            com.atlan.pkg.aim.Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(connectionName, connectorType)
    }

    @Test(groups = ["lb.pa.create"])
    fun connectionCreated() {
        val c1 = Connection.findByName(client, connectionName, connectorType)
        assertEquals(1, c1.size)
        assertEquals(connectionName, c1[0].name)
    }

    @Test(groups = ["lb.pa.create"])
    fun partialSourceCreated() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(Table.NAME)
                .includeOnResults(Table.IS_PARTIAL)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val tables = response.assets
        assertEquals(1, tables.size)
        assertTrue(tables[0].isPartial)
        assertEquals("source_table", tables[0].name)
    }

    @Test(groups = ["lb.pa.create"])
    fun partialTargetCreated() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            View
                .select(client)
                .where(View.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(View.NAME)
                .includeOnResults(View.IS_PARTIAL)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val views = response.assets
        assertEquals(1, views.size)
        assertTrue(views[0].isPartial)
        assertEquals("target_view", views[0].name)
    }

    @Test(groups = ["lb.pa.create"])
    fun lineageProcessCreated() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            LineageProcess
                .select(client)
                .where(LineageProcess.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(LineageProcess.NAME)
                .includeOnResults(LineageProcess.IS_PARTIAL)
                .includeOnResults(LineageProcess.SQL)
                .includeOnResults(LineageProcess.CERTIFICATE_STATUS)
                .includeOnResults(LineageProcess.ANNOUNCEMENT_TYPE)
                .includeOnResults(LineageProcess.ANNOUNCEMENT_TITLE)
                .includeOnResults(LineageProcess.ANNOUNCEMENT_MESSAGE)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val lineage = response.assets
        assertEquals(1, lineage.size)
        val process = lineage[0] as LineageProcess
        // assertFalse(process.isPartial)
        assertEquals("source_table > target_view", process.name)
        assertEquals("select * from db1.schema1.source_table", process.sql)
        assertEquals(CertificateStatus.DRAFT, process.certificateStatus)
        assertEquals(AtlanAnnouncementType.INFORMATION, process.announcementType)
        assertEquals("Testing lineage builder", process.announcementTitle)
        assertEquals("Only a test...", process.announcementMessage)
    }

    @Test(groups = ["lb.pa.create"])
    fun downstreamLineageExists() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(Table.NAME)
                .includeOnResults(Table.IS_PARTIAL)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val table = response.assets[0]!!
        val downstream =
            FluentLineage
                .builder(client, table.guid)
                .direction(AtlanLineageDirection.DOWNSTREAM)
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.IS_PARTIAL)
                .depth(5)
                .stream()
                .toList()
        assertEquals(2, downstream.size)
        // assertFalse(downstream[0].isPartial)
        assertTrue(downstream[0] is LineageProcess)
        assertEquals("source_table > target_view", downstream[0].name)
        assertTrue(downstream[1].isPartial)
        assertTrue(downstream[1] is View)
        assertEquals("target_view", downstream[1].name)
    }

    @Test(groups = ["lb.pa.create"])
    fun upstreamLineageExists() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            View
                .select(client)
                .where(View.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(View.NAME)
                .includeOnResults(View.IS_PARTIAL)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val view = response.assets[0]!!
        val upstream =
            FluentLineage
                .builder(client, view.guid)
                .direction(AtlanLineageDirection.UPSTREAM)
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.IS_PARTIAL)
                .depth(5)
                .stream()
                .toList()
        assertEquals(2, upstream.size)
        // assertFalse(upstream[0].isPartial)
        assertTrue(upstream[0] is LineageProcess)
        assertEquals("source_table > target_view", upstream[0].name)
        assertTrue(upstream[1].isPartial)
        assertTrue(upstream[1] is Table)
        assertEquals("source_table", upstream[1].name)
    }

    @Test(groups = ["lb.pa.create"])
    fun connectionCacheCreated() {
        validateConnectionCache()
    }

    private fun validateConnectionCache() {
        val c1 = Connection.findByName(client, connectionName, connectorType)[0]!!
        val dbFile = Paths.get(testDirectory, "connection-cache", "${c1.qualifiedName}.sqlite").toFile()
        Assert.assertTrue(dbFile.isFile)
        Assert.assertTrue(dbFile.exists())
        val cache = PersistentConnectionCache(dbFile.path)
        val assets = cache.listAssets()
        assertNotNull(assets)
        Assert.assertFalse(assets.isEmpty())
        assertEquals(3, assets.size)
        assertEquals(setOf(Table.TYPE_NAME, View.TYPE_NAME, LineageProcess.TYPE_NAME), assets.map { it.typeName }.toSet())
        assertEquals(1, assets.count { it.typeName == Table.TYPE_NAME })
        assertEquals(1, assets.count { it.typeName == View.TYPE_NAME })
        assertEquals(1, assets.count { it.typeName == LineageProcess.TYPE_NAME })
    }

    @Test(groups = ["lb.pa.runUpdate"], dependsOnGroups = ["lb.pa.create"])
    fun upsertRevisions() {
        // Convert partial view into full view
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            View
                .select(client)
                .where(View.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(View.NAME)
                .includeOnResults(View.IS_PARTIAL)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val view = response.assets[0]!!
        assertTrue(view is View)
        val revised =
            view
                .toBuilder()
                .isPartial(false)
                .description("And with a description now, too...")
                .build()
        val resp = revised.save(client)
        val result = resp.getResult(revised)
        assertFalse(result.isPartial)
        modifyFile()
        runCustomPackage(
            LineageBuilderCfg(
                lineageFile = Paths.get(testDirectory, revisedFile).toString(),
                lineageUpsertSemantic = "partial",
                lineageCaseSensitive = false,
                lineageFailOnErrors = true,
            ),
            Loader::main,
        )
        // Allow Elastic index and deletion to become consistent
        Thread.sleep(5000)
    }

    @Test(groups = ["lb.pa.update"], dependsOnGroups = ["lb.pa.runUpdate"])
    fun testRevisions() {
        // Validate no portion of the full view has been clobbered by the update
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            View
                .select(client)
                .where(View.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(View.NAME)
                .includeOnResults(View.IS_PARTIAL)
                .includeOnResults(View.DESCRIPTION)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val views = response.assets
        assertEquals(1, views.size)
        assertFalse(views[0].isPartial)
        assertEquals("target_view", views[0].name)
        assertEquals("And with a description now, too...", views[0].description)
    }

    @Test(groups = ["lb.pa.update"], dependsOnGroups = ["lb.pa.runUpdate"])
    fun connectionCacheUpdated() {
        validateConnectionCache()
    }

    @Test(dependsOnGroups = ["lb.pa.*"])
    fun filesCreated() {
        validateFilesExist(files)
    }
}
