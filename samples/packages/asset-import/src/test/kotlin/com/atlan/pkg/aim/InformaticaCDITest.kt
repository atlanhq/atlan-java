/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.ColumnProcess
import com.atlan.model.assets.Connection
import com.atlan.model.assets.FlowV06ControlOperation
import com.atlan.model.assets.FlowV06Dataset
import com.atlan.model.assets.FlowV06DatasetOperation
import com.atlan.model.assets.FlowV06FieldOperation
import com.atlan.model.assets.FlowV06Folder
import com.atlan.model.assets.FlowV06ReusableUnit
import com.atlan.model.assets.FlowV06Project
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
            FlowV06Project
                .select(client)
                .where(FlowV06Project.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                // .includeOnResults(FlowV06Project.FLOW_V02GROUPINGS)
                // .includeOnResults(FlowV06Project.FLOW_V02DATASETS)
                // .includeOnResults(FlowV06Project.FLOW_V02FIELDS)
                .stream()
                .map { it as FlowV06Project }
                .toList()
        assertEquals(1, projects.size)
    }

    @Test
    fun folderExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val folders =
            FlowV06Folder
                .select(client)
                .where(FlowV06Folder.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV06Folder.FLOW_V06PROJECT_NAME)
                .includeOnResults(FlowV06Folder.FLOW_V06PROJECT_QUALIFIED_NAME)
                // .includeOnResults(FlowV06Folder.FLOW_V02GROUPINGS)
                // .includeOnResults(FlowV06Folder.FLOW_V02DATASETS)
                // .includeOnResults(FlowV06Folder.FLOW_V02FIELDS)
                .stream()
                .map { it as FlowV06Folder }
                .toList()
        assertEquals(1, folders.size)
        assertEquals("Atlan", folders.first().flowV06ProjectName)
        assertTrue(folders.first().flowV06ProjectQualifiedName.endsWith("Atlan"))
    }

    @Test
    fun visibleLineageExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val processes =
            LineageProcess
                .select(client)
                .where(LineageProcess.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(LineageProcess.FLOW_V06ORCHESTRATED_BY)
                .stream()
                .map { it as LineageProcess }
                .toList()
        assertEquals(5, processes.size)
        val orchestratedBy = processes.groupBy { it.flowV06OrchestratedBy.qualifiedName }
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
            FlowV06ControlOperation
                .select(client)
                .where(FlowV06ControlOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV06ControlOperation.ASSET_USER_DEFINED_TYPE.eq("Mapping Task"))
                .includeOnResults(FlowV06ControlOperation.FLOW_V06DATA_RESULTS)
                .includeOnResults(FlowV06ControlOperation.FLOW_V06REUSABLE_UNIT_NAME)
                .includeOnResults(FlowV06ControlOperation.FLOW_V06REUSABLE_UNIT_QUALIFIED_NAME)
                .includeOnResults(FlowV06ControlOperation.NAME)
                .includeOnResults(FlowV06ControlOperation.FLOW_V06PROJECT_NAME)
                .includeOnResults(FlowV06ControlOperation.FLOW_V06PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV06ControlOperation.FLOW_V06FOLDER_NAME)
                .includeOnResults(FlowV06ControlOperation.FLOW_V06FOLDER_QUALIFIED_NAME)
                .stream()
                .map { it as FlowV06ControlOperation }
                .toList()
        assertEquals(2, mt.size)
        mt.forEach { task ->
            assertEquals("Atlan", task.flowV06ProjectName)
            assertEquals("sample_folder", task.flowV06FolderName)
            assertTrue(task.flowV06ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(task.flowV06FolderQualifiedName.endsWith("sample_folder"))
            when (task.name) {
                "MultiMap" -> {
                    // 12 here when we may e2s and t2e to the task-level (otherwise 4)
                    // When the task-level is a control flow op, this should just be the 4 resolved lineage processes
                    assertEquals(4, task.flowV06DataResults.size)
                    assertEquals("${connection.qualifiedName}/MultiMap_mapping", task.flowV06ReusableUnitQualifiedName)
                    assertEquals("MultiMap (mapping)", task.flowV06ReusableUnitName)
                }
                "Complex" -> {
                    // 3 here when we may e2s and t2e to the task-level (otherwise 1)
                    // When the task-level is a control flow op, this should just be the 1 resolved lineage process
                    assertEquals(1, task.flowV06DataResults.size)
                    assertEquals("${connection.qualifiedName}/Complex_mapping", task.flowV06ReusableUnitQualifiedName)
                    assertEquals("Complex (mapping)", task.flowV06ReusableUnitName)
                }
            }
        }
    }

    @Test
    fun mappingsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mappings =
            FlowV06ReusableUnit
                .select(client)
                .where(FlowV06ReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV06ReusableUnit.ASSET_USER_DEFINED_TYPE.eq("Mapping"))
                .includeOnResults(FlowV06ReusableUnit.FLOW_V06DATA_FLOWS)
                .includeOnResults(FlowV06ReusableUnit.NAME)
                .includeOnResults(FlowV06ReusableUnit.FLOW_V06PROJECT_NAME)
                .includeOnResults(FlowV06ReusableUnit.FLOW_V06PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV06ReusableUnit.FLOW_V06FOLDER_NAME)
                .includeOnResults(FlowV06ReusableUnit.FLOW_V06FOLDER_QUALIFIED_NAME)
                .stream()
                .map { it as FlowV06ReusableUnit }
                .toList()
        assertEquals(2, mappings.size)
        mappings.forEach { mapping ->
            assertEquals("Atlan", mapping.flowV06ProjectName)
            assertEquals("sample_folder", mapping.flowV06FolderName)
            assertTrue(mapping.flowV06ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(mapping.flowV06FolderQualifiedName.endsWith("sample_folder"))
            when (mapping.name) {
                "MultiMap (mapping)" -> {
                    // 4 here when we only map the inside lineage portions, otherwise 12
                    assertEquals(12, mapping.flowV06DataFlows.size)
                }
                "Complex (mapping)" -> {
                    // 2 here when we only map the inside lineage portions, otherwise 4
                    assertEquals(4, mapping.flowV06DataFlows.size)
                }
            }
        }
    }

    @Test
    fun mappletExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mapplets =
            FlowV06ReusableUnit
                .select(client)
                .where(FlowV06ReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV06ReusableUnit.ASSET_USER_DEFINED_TYPE.eq("Mapplet"))
                .includeOnResults(FlowV06ReusableUnit.FLOW_V06DATA_FLOWS)
                .includeOnResults(FlowV06ReusableUnit.FLOW_V06ABSTRACTS)
                .includeOnResults(FlowV06ReusableUnit.NAME)
                .includeOnResults(FlowV06ReusableUnit.FLOW_V06PROJECT_NAME)
                .includeOnResults(FlowV06ReusableUnit.FLOW_V06PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV06ReusableUnit.FLOW_V06FOLDER_NAME)
                .includeOnResults(FlowV06ReusableUnit.FLOW_V06FOLDER_QUALIFIED_NAME)
                .includeOnRelations(Asset.QUALIFIED_NAME)
                .stream()
                .map { it as FlowV06ReusableUnit }
                .toList()
        assertEquals(1, mapplets.size)
        mapplets.forEach { mapplet ->
            assertEquals("Atlan", mapplet.flowV06ProjectName)
            assertEquals("sample_folder", mapplet.flowV06FolderName)
            assertTrue(mapplet.flowV06ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(mapplet.flowV06FolderQualifiedName.endsWith("sample_folder"))
            when (mapplet.name) {
                "Mapplet" -> {
                    assertEquals(3, mapplet.flowV06DataFlows.size)
                    assertEquals(1, mapplet.flowV06Abstracts.size)
                    assertEquals("${connection.qualifiedName}/Complex/transformations/Mapplet", mapplet.flowV06Abstracts.first().qualifiedName)
                }
            }
        }
    }

    @Test
    fun interimDatasetForMappletReferencesItsMapplet() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val ids =
            FlowV06Dataset
                .select(client)
                .where(FlowV06Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV06Dataset.NAME.eq("Mapplet"))
                .includeOnResults(FlowV06Dataset.FLOW_V06DETAILED_BY)
                .includeOnResults(FlowV06Dataset.FLOW_V06PROJECT_NAME)
                .includeOnResults(FlowV06Dataset.FLOW_V06PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV06Dataset.FLOW_V06FOLDER_NAME)
                .includeOnResults(FlowV06Dataset.FLOW_V06FOLDER_QUALIFIED_NAME)
                .includeOnRelations(Asset.QUALIFIED_NAME)
                .stream()
                .map { it as FlowV06Dataset }
                .toList()
        assertEquals(1, ids.size)
        assertEquals("${connection.qualifiedName}/Mapplet", ids[0].flowV06DetailedBy.qualifiedName)
        assertEquals("Atlan", ids[0].flowV06ProjectName)
        assertEquals("sample_folder", ids[0].flowV06FolderName)
        assertTrue(ids[0].flowV06ProjectQualifiedName.endsWith("Atlan"))
        assertTrue(ids[0].flowV06FolderQualifiedName.endsWith("sample_folder"))
    }

    @Test
    fun dataFlowV06OpsNotInTopLevelLineage() {
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
            // Note: we need to EXPLICITLY EXCLUDE FlowV06DataOperation from lineage, if we want to avoid having it
            //  in the traversals...
            val builder =
                FluentLineage
                    .builder(client, table.guid)
                    .includeOnResults(Asset.NAME)
                    .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                    .whereAsset(Asset.TYPE_NAME.inLineage.neq(FlowV06DatasetOperation.TYPE_NAME))
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
            FlowV06Dataset
                .select(client)
                .where(FlowV06Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV06Dataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/MultiMap"))
                .stream()
                .map { it as FlowV06Dataset }
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
            FlowV06FieldOperation
                .select(client)
                .where(FlowV06FieldOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV06FieldOperation.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/MultiMap"))
                .stream()
                .map { it as FlowV06FieldOperation }
                .toList()
        assertEquals(46, fieldOps.size)
    }

    @Test
    fun complexInnerLineage() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val interims =
            FlowV06Dataset
                .select(client)
                .where(FlowV06Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV06Dataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/Complex"))
                .stream()
                .map { it as FlowV06Dataset }
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
            FlowV06Dataset
                .select(client)
                .where(FlowV06Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV06Dataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/Mapplet"))
                .stream()
                .map { it as FlowV06Dataset }
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
            FlowV06DatasetOperation
                .select(client)
                .where(FlowV06DatasetOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV06DatasetOperation.INPUTS)
                .includeOnResults(FlowV06DatasetOperation.OUTPUTS)
                .stream()
                .map { it as FlowV06DatasetOperation }
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
            FlowV06Dataset
                .select(client)
                .where(FlowV06Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV06Dataset.INPUT_TO_PROCESSES)
                .includeOnResults(FlowV06Dataset.OUTPUT_FROM_PROCESSES)
                .includeOnResults(FlowV06Dataset.NAME)
                .stream()
                .map { it as FlowV06Dataset }
                .toList()
        assertEquals(15, ids.size)
        ids.forEach { id ->
            // And every single one of them acts as at least an input to or output from a FlowV06DataOperation
            val ops = id.inputToProcesses.union(id.outputFromProcesses)
            assertFalse(ops.isEmpty())
            val types = ops.map { it.typeName }.toSet()
            assertEquals(setOf(FlowV06DatasetOperation.TYPE_NAME), types)
        }
    }

    @Test
    fun flowGroupingsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val groupings =
            FlowV06ReusableUnit
                .select(client)
                .where(FlowV06ReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .stream()
                .toList()
        assertEquals(3, groupings.size)
    }
}
