/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.ColumnProcess
import com.atlan.model.assets.Connection
import com.atlan.model.assets.FlowV07ControlOperation
import com.atlan.model.assets.FlowV07Dataset
import com.atlan.model.assets.FlowV07DatasetOperation
import com.atlan.model.assets.FlowV07FieldOperation
import com.atlan.model.assets.FlowV07Folder
import com.atlan.model.assets.FlowV07Project
import com.atlan.model.assets.FlowV07ReusableUnit
import com.atlan.model.assets.ILineageProcess
import com.atlan.model.assets.LineageProcess
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanLineageDirection
import com.atlan.model.lineage.FluentLineage
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import org.testng.Assert.assertFalse
import org.testng.annotations.Test
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Test export of detailed view and change information.
 */
class InformaticaCDITest : PackageTest("cdi") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val c1 = makeUnique("c1")
    private val connectorType = "iics"
    val testFile = "iics-cdi-assets.csv"

    private fun prepFile() {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{CNAME}", c1)
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        prepFile()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(c1, connectorType)
    }

    @Test
    fun connectionExists() {
        val connection = Connection.findByName(client, c1, connectorType)
        assertNotNull(connection)
        assertFalse(connection.isEmpty())
        assertEquals(1, connection.size)
    }

    @Test
    fun projectExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val projects =
            FlowV07Project
                .select(client)
                .where(FlowV07Project.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                // .includeOnResults(FlowV07Project.FLOW_V02GROUPINGS)
                // .includeOnResults(FlowV07Project.FLOW_V02DATASETS)
                // .includeOnResults(FlowV07Project.FLOW_V02FIELDS)
                .stream()
                .map { it as FlowV07Project }
                .toList()
        assertEquals(1, projects.size)
    }

    @Test
    fun folderExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val folders =
            FlowV07Folder
                .select(client)
                .where(FlowV07Folder.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV07Folder.FLOW_V07PROJECT_NAME)
                .includeOnResults(FlowV07Folder.FLOW_V07PROJECT_QUALIFIED_NAME)
                // .includeOnResults(FlowV07Folder.FLOW_V02GROUPINGS)
                // .includeOnResults(FlowV07Folder.FLOW_V02DATASETS)
                // .includeOnResults(FlowV07Folder.FLOW_V02FIELDS)
                .stream()
                .map { it as FlowV07Folder }
                .toList()
        assertEquals(1, folders.size)
        assertEquals("Atlan", folders.first().flowV07ProjectName)
        assertTrue(folders.first().flowV07ProjectQualifiedName.endsWith("Atlan"))
    }

    @Test
    fun visibleLineageExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val processes =
            LineageProcess
                .select(client)
                .where(LineageProcess.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(LineageProcess.FLOW_V07ORCHESTRATED_BY)
                .stream()
                .map { it as LineageProcess }
                .toList()
        assertEquals(5, processes.size)
        val orchestratedBy = processes.groupBy { it.flowV07OrchestratedBy.qualifiedName }
        orchestratedBy.forEach { (k, v) ->
            when (k) {
                "${connection.qualifiedName}/MultiMap" -> {
                    assertEquals(4, v.size)
                }
                "${connection.qualifiedName}/Complex" -> {
                    assertEquals(1, v.size)
                }
            }
        }
    }

    @Test
    fun visibleColumnLineageExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val colProcesses =
            ColumnProcess
                .select(client)
                .where(ColumnProcess.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .stream()
                .map { it as ColumnProcess }
                .toList()
        assertEquals(13, colProcesses.size)
    }

    @Test
    fun mappingTasksExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mt =
            FlowV07ControlOperation
                .select(client)
                .where(FlowV07ControlOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV07ControlOperation.ASSET_USER_DEFINED_TYPE.eq("Mapping Task"))
                .includeOnResults(FlowV07ControlOperation.FLOW_V07DATA_RESULTS)
                .includeOnResults(FlowV07ControlOperation.FLOW_V07REUSABLE_UNIT_NAME)
                .includeOnResults(FlowV07ControlOperation.FLOW_V07REUSABLE_UNIT_QUALIFIED_NAME)
                .includeOnResults(FlowV07ControlOperation.NAME)
                .includeOnResults(FlowV07ControlOperation.FLOW_V07PROJECT_NAME)
                .includeOnResults(FlowV07ControlOperation.FLOW_V07PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV07ControlOperation.FLOW_V07FOLDER_NAME)
                .includeOnResults(FlowV07ControlOperation.FLOW_V07FOLDER_QUALIFIED_NAME)
                .stream()
                .map { it as FlowV07ControlOperation }
                .toList()
        assertEquals(2, mt.size)
        mt.forEach { task ->
            assertEquals("Atlan", task.flowV07ProjectName)
            assertEquals("sample_folder", task.flowV07FolderName)
            assertTrue(task.flowV07ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(task.flowV07FolderQualifiedName.endsWith("sample_folder"))
            when (task.name) {
                "MultiMap" -> {
                    // 12 here when we may e2s and t2e to the task-level (otherwise 4)
                    // When the task-level is a control flow op, this should just be the 4 resolved lineage processes
                    assertEquals(4, task.flowV07DataResults.size)
                    assertEquals("${connection.qualifiedName}/MultiMap_mapping", task.flowV07ReusableUnitQualifiedName)
                    assertEquals("MultiMap (mapping)", task.flowV07ReusableUnitName)
                }
                "Complex" -> {
                    // 3 here when we may e2s and t2e to the task-level (otherwise 1)
                    // When the task-level is a control flow op, this should just be the 1 resolved lineage process
                    assertEquals(1, task.flowV07DataResults.size)
                    assertEquals("${connection.qualifiedName}/Complex_mapping", task.flowV07ReusableUnitQualifiedName)
                    assertEquals("Complex (mapping)", task.flowV07ReusableUnitName)
                }
            }
        }
    }

    @Test
    fun mappingsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mappings =
            FlowV07ReusableUnit
                .select(client)
                .where(FlowV07ReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV07ReusableUnit.ASSET_USER_DEFINED_TYPE.eq("Mapping"))
                .includeOnResults(FlowV07ReusableUnit.FLOW_V07DATA_FLOWS)
                .includeOnResults(FlowV07ReusableUnit.NAME)
                .includeOnResults(FlowV07ReusableUnit.FLOW_V07PROJECT_NAME)
                .includeOnResults(FlowV07ReusableUnit.FLOW_V07PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV07ReusableUnit.FLOW_V07FOLDER_NAME)
                .includeOnResults(FlowV07ReusableUnit.FLOW_V07FOLDER_QUALIFIED_NAME)
                .stream()
                .map { it as FlowV07ReusableUnit }
                .toList()
        assertEquals(2, mappings.size)
        mappings.forEach { mapping ->
            assertEquals("Atlan", mapping.flowV07ProjectName)
            assertEquals("sample_folder", mapping.flowV07FolderName)
            assertTrue(mapping.flowV07ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(mapping.flowV07FolderQualifiedName.endsWith("sample_folder"))
            when (mapping.name) {
                "MultiMap (mapping)" -> {
                    // 4 here when we only map the inside lineage portions, otherwise 12
                    assertEquals(12, mapping.flowV07DataFlows.size)
                }
                "Complex (mapping)" -> {
                    // 2 here when we only map the inside lineage portions, otherwise 4
                    assertEquals(4, mapping.flowV07DataFlows.size)
                }
            }
        }
    }

    @Test
    fun mappletExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mapplets =
            FlowV07ReusableUnit
                .select(client)
                .where(FlowV07ReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV07ReusableUnit.ASSET_USER_DEFINED_TYPE.eq("Mapplet"))
                .includeOnResults(FlowV07ReusableUnit.FLOW_V07DATA_FLOWS)
                .includeOnResults(FlowV07ReusableUnit.FLOW_V07ABSTRACTS)
                .includeOnResults(FlowV07ReusableUnit.NAME)
                .includeOnResults(FlowV07ReusableUnit.FLOW_V07PROJECT_NAME)
                .includeOnResults(FlowV07ReusableUnit.FLOW_V07PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV07ReusableUnit.FLOW_V07FOLDER_NAME)
                .includeOnResults(FlowV07ReusableUnit.FLOW_V07FOLDER_QUALIFIED_NAME)
                .includeOnRelations(Asset.QUALIFIED_NAME)
                .stream()
                .map { it as FlowV07ReusableUnit }
                .toList()
        assertEquals(1, mapplets.size)
        mapplets.forEach { mapplet ->
            assertEquals("Atlan", mapplet.flowV07ProjectName)
            assertEquals("sample_folder", mapplet.flowV07FolderName)
            assertTrue(mapplet.flowV07ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(mapplet.flowV07FolderQualifiedName.endsWith("sample_folder"))
            when (mapplet.name) {
                "Mapplet" -> {
                    assertEquals(3, mapplet.flowV07DataFlows.size)
                    assertEquals(1, mapplet.flowV07Abstracts.size)
                    assertEquals("${connection.qualifiedName}/Complex/transformations/Mapplet", mapplet.flowV07Abstracts.first().qualifiedName)
                }
            }
        }
    }

    @Test
    fun interimDatasetForMappletReferencesItsMapplet() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val ids =
            FlowV07Dataset
                .select(client)
                .where(FlowV07Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV07Dataset.NAME.eq("Mapplet"))
                .includeOnResults(FlowV07Dataset.FLOW_V07DETAILED_BY)
                .includeOnResults(FlowV07Dataset.FLOW_V07PROJECT_NAME)
                .includeOnResults(FlowV07Dataset.FLOW_V07PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV07Dataset.FLOW_V07FOLDER_NAME)
                .includeOnResults(FlowV07Dataset.FLOW_V07FOLDER_QUALIFIED_NAME)
                .includeOnRelations(Asset.QUALIFIED_NAME)
                .stream()
                .map { it as FlowV07Dataset }
                .toList()
        assertEquals(1, ids.size)
        assertEquals("${connection.qualifiedName}/Mapplet", ids[0].flowV07DetailedBy.qualifiedName)
        assertEquals("Atlan", ids[0].flowV07ProjectName)
        assertEquals("sample_folder", ids[0].flowV07FolderName)
        assertTrue(ids[0].flowV07ProjectQualifiedName.endsWith("Atlan"))
        assertTrue(ids[0].flowV07FolderQualifiedName.endsWith("sample_folder"))
    }

    @Test
    fun dataFlowV07OpsNotInTopLevelLineage() {
        val connection = Connection.findByName(client, "production", AtlanConnectorType.SNOWFLAKE)[0]!!
        val iics = Connection.findByName(client, c1, connectorType)[0]!!
        val tables =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(Table.DATABASE_NAME.eq("LOGAN_DATA"))
                .where(Table.SCHEMA_NAME.eq("INFORMATICA_CDI"))
                .includeOnResults(Table.NAME)
                .stream()
                .map { it as Table }
                .toList()
        assertEquals(10, tables.size)
        tables.forEach { table ->
            // Note: we need to EXPLICITLY EXCLUDE FlowV07DataOperation from lineage, if we want to avoid having it
            //  in the traversals...
            val builder =
                FluentLineage
                    .builder(client, table.guid)
                    .includeOnResults(Asset.NAME)
                    .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                    .whereAsset(Asset.TYPE_NAME.inLineage.neq(FlowV07DatasetOperation.TYPE_NAME))
            val lineage =
                when (table.name) {
                    "SOURCETABLE", "CUSTOMERS01", "DISNEY_MOVIES", "EMPLOYEES_SR1", "EMPLOYEES_SR" -> {
                        builder
                            .direction(AtlanLineageDirection.DOWNSTREAM)
                            .stream()
                            .toList()
                    }
                    "TARGETTABLE", "CUSTOMER_RESULTANT_JOINER", "MOVIE_DETAILS", "Routersimple02", "Routersimple01" -> {
                        builder
                            .direction(AtlanLineageDirection.UPSTREAM)
                            .stream()
                            .toList()
                    }
                    else -> emptyList<Asset>()
                }
            val filtered = lineage.filter { it.connectionQualifiedName == connection.qualifiedName || it.connectionQualifiedName == iics.qualifiedName }
            validateLineage(filtered, 1)
        }
    }

    @Test
    fun multiMapInnerLineage() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val interims =
            FlowV07Dataset
                .select(client)
                .where(FlowV07Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV07Dataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/MultiMap"))
                .stream()
                .map { it as FlowV07Dataset }
                .toList()
        assertEquals(8, interims.size)
        interims.forEach { interim ->
            val builder =
                FluentLineage
                    .builder(client, interim.guid)
                    .includeOnResults(Asset.NAME)
                    .depth(100)
            val lineage =
                when (interim.name) {
                    "Source", "Source1", "Source2", "Source3" -> {
                        builder
                            .direction(AtlanLineageDirection.DOWNSTREAM)
                            .stream()
                            .toList()
                    }
                    "Target", "Target1", "Target2", "Target3" -> {
                        builder
                            .direction(AtlanLineageDirection.UPSTREAM)
                            .stream()
                            .toList()
                    }
                    else -> emptyList<Asset>()
                }
            validateLineage(lineage, 2)
        }
    }

    @Test
    fun multiMapInnerColumnLineage() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val fieldOps =
            FlowV07FieldOperation
                .select(client)
                .where(FlowV07FieldOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV07FieldOperation.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/MultiMap"))
                .stream()
                .map { it as FlowV07FieldOperation }
                .toList()
        assertEquals(46, fieldOps.size)
    }

    @Test
    fun complexInnerLineage() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val interims =
            FlowV07Dataset
                .select(client)
                .where(FlowV07Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV07Dataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/Complex"))
                .stream()
                .map { it as FlowV07Dataset }
                .toList()
        assertEquals(3, interims.size)
        interims.forEach { interim ->
            val builder =
                FluentLineage
                    .builder(client, interim.guid)
                    .includeOnResults(Asset.NAME)
                    .depth(100)
            var totalExpected = 0
            val lineage =
                when (interim.name) {
                    "Source" -> {
                        totalExpected = 3
                        builder
                            .direction(AtlanLineageDirection.DOWNSTREAM)
                            .stream()
                            .toList()
                    }
                    "Mapplet" -> {
                        totalExpected = 2
                        builder
                            .direction(AtlanLineageDirection.DOWNSTREAM)
                            .stream()
                            .toList()
                    }
                    "Target" -> {
                        totalExpected = 3
                        builder
                            .direction(AtlanLineageDirection.UPSTREAM)
                            .stream()
                            .toList()
                    }
                    else -> {
                        totalExpected = 0
                        emptyList<Asset>()
                    }
                }
            validateLineage(lineage, totalExpected)
        }
    }

    @Test
    fun complexMappletLineage() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val interims =
            FlowV07Dataset
                .select(client)
                .where(FlowV07Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV07Dataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/Mapplet"))
                .stream()
                .map { it as FlowV07Dataset }
                .toList()
        assertEquals(4, interims.size)
        interims.forEach { interim ->
            val builder =
                FluentLineage
                    .builder(client, interim.guid)
                    .includeOnResults(Asset.NAME)
                    .depth(100)
            var totalExpected = 0
            val lineage =
                when (interim.name) {
                    "Input" -> {
                        totalExpected = 3
                        builder
                            .direction(AtlanLineageDirection.DOWNSTREAM)
                            .stream()
                            .toList()
                    }
                    "Expression" -> {
                        totalExpected = 2
                        builder
                            .direction(AtlanLineageDirection.DOWNSTREAM)
                            .stream()
                            .toList()
                    }
                    "Aggregator" -> {
                        totalExpected = 1
                        builder
                            .direction(AtlanLineageDirection.DOWNSTREAM)
                            .stream()
                            .toList()
                    }
                    "Output" -> {
                        totalExpected = 3
                        builder
                            .direction(AtlanLineageDirection.UPSTREAM)
                            .stream()
                            .toList()
                    }
                    else -> {
                        totalExpected = 0
                        emptyList<Asset>()
                    }
                }
            validateLineage(lineage, totalExpected)
        }
    }

    // ... OTHER BASIC TESTS ...

    private fun validateLineage(
        assetList: List<Asset>,
        expectedNonProcessAssets: Int,
    ) {
        if (expectedNonProcessAssets > 0) {
            assertFalse(assetList.isEmpty())
            val nonProcess =
                assetList
                    .filter { it !is ILineageProcess }
                    .toList()
            assertEquals(expectedNonProcessAssets, nonProcess.size)
        } else {
            assertTrue(assetList.isEmpty())
        }
    }

    @Test
    fun drilldownLineageExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val operations =
            FlowV07DatasetOperation
                .select(client)
                .where(FlowV07DatasetOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV07DatasetOperation.INPUTS)
                .includeOnResults(FlowV07DatasetOperation.OUTPUTS)
                .stream()
                .map { it as FlowV07DatasetOperation }
                .toList()
        assertEquals(19, operations.size)
        operations.forEach { operation ->
            // And every single one of them has at least one input and one output
            assertFalse(operation.inputs.isEmpty())
            assertFalse(operation.outputs.isEmpty())
        }
    }

    @Test
    fun interimDatasetsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val ids =
            FlowV07Dataset
                .select(client)
                .where(FlowV07Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV07Dataset.INPUT_TO_PROCESSES)
                .includeOnResults(FlowV07Dataset.OUTPUT_FROM_PROCESSES)
                .includeOnResults(FlowV07Dataset.NAME)
                .stream()
                .map { it as FlowV07Dataset }
                .toList()
        assertEquals(15, ids.size)
        ids.forEach { id ->
            // And every single one of them acts as at least an input to or output from a FlowV07DataOperation
            val ops = id.inputToProcesses.union(id.outputFromProcesses)
            assertFalse(ops.isEmpty())
            val types = ops.map { it.typeName }.toSet()
            assertEquals(setOf(FlowV07DatasetOperation.TYPE_NAME), types)
        }
    }

    @Test
    fun flowGroupingsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val groupings =
            FlowV07ReusableUnit
                .select(client)
                .where(FlowV07ReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .stream()
                .toList()
        assertEquals(3, groupings.size)
    }
}
