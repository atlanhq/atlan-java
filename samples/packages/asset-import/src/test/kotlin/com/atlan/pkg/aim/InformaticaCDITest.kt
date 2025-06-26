/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.ColumnProcess
import com.atlan.model.assets.Connection
import com.atlan.model.assets.FlowV09ControlOperation
import com.atlan.model.assets.FlowV09Dataset
import com.atlan.model.assets.FlowV09DatasetOperation
import com.atlan.model.assets.FlowV09Field
import com.atlan.model.assets.FlowV09FieldOperation
import com.atlan.model.assets.FlowV09Folder
import com.atlan.model.assets.FlowV09Project
import com.atlan.model.assets.FlowV09ReusableUnit
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
            FlowV09Project
                .select(client)
                .where(FlowV09Project.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                // .includeOnResults(FlowV09Project.FLOW_V02GROUPINGS)
                // .includeOnResults(FlowV09Project.FLOW_V02DATASETS)
                // .includeOnResults(FlowV09Project.FLOW_V02FIELDS)
                .stream()
                .map { it as FlowV09Project }
                .toList()
        assertEquals(1, projects.size)
    }

    @Test
    fun folderExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val folders =
            FlowV09Folder
                .select(client)
                .where(FlowV09Folder.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV09Folder.FLOW_V09PROJECT_NAME)
                .includeOnResults(FlowV09Folder.FLOW_V09PROJECT_QUALIFIED_NAME)
                // .includeOnResults(FlowV09Folder.FLOW_V02GROUPINGS)
                // .includeOnResults(FlowV09Folder.FLOW_V02DATASETS)
                // .includeOnResults(FlowV09Folder.FLOW_V02FIELDS)
                .stream()
                .map { it as FlowV09Folder }
                .toList()
        assertEquals(1, folders.size)
        assertEquals("Atlan", folders.first().flowV09ProjectName)
        assertTrue(folders.first().flowV09ProjectQualifiedName.endsWith("Atlan"))
    }

    @Test
    fun visibleLineageExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val processes =
            LineageProcess
                .select(client)
                .where(LineageProcess.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(LineageProcess.FLOW_V09ORCHESTRATED_BY)
                .stream()
                .map { it as LineageProcess }
                .toList()
        assertEquals(5, processes.size)
        val orchestratedBy = processes.groupBy { it.flowV09OrchestratedBy.qualifiedName }
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
            FlowV09ControlOperation
                .select(client)
                .where(FlowV09ControlOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV09ControlOperation.ASSET_USER_DEFINED_TYPE.eq("Mapping Task"))
                .includeOnResults(FlowV09ControlOperation.FLOW_V09DATA_RESULTS)
                .includeOnResults(FlowV09ControlOperation.FLOW_V09REUSABLE_UNIT_NAME)
                .includeOnResults(FlowV09ControlOperation.FLOW_V09REUSABLE_UNIT_QUALIFIED_NAME)
                .includeOnResults(FlowV09ControlOperation.NAME)
                .includeOnResults(FlowV09ControlOperation.FLOW_V09PROJECT_NAME)
                .includeOnResults(FlowV09ControlOperation.FLOW_V09PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV09ControlOperation.FLOW_V09FOLDER_NAME)
                .includeOnResults(FlowV09ControlOperation.FLOW_V09FOLDER_QUALIFIED_NAME)
                .stream()
                .map { it as FlowV09ControlOperation }
                .toList()
        assertEquals(2, mt.size)
        mt.forEach { task ->
            assertEquals("Atlan", task.flowV09ProjectName)
            assertEquals("sample_folder", task.flowV09FolderName)
            assertTrue(task.flowV09ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(task.flowV09FolderQualifiedName.endsWith("sample_folder"))
            when (task.name) {
                "MultiMap" -> {
                    // 12 here when we may e2s and t2e to the task-level (otherwise 4)
                    // When the task-level is a control flow op, this should just be the 4 resolved lineage processes
                    assertEquals(12, task.flowV09DataResults.size)
                    assertEquals("${connection.qualifiedName}/MultiMap_mapping", task.flowV09ReusableUnitQualifiedName)
                    assertEquals("MultiMap (mapping)", task.flowV09ReusableUnitName)
                }
                "Complex" -> {
                    // 3 here when we may e2s and t2e to the task-level (otherwise 1)
                    // When the task-level is a control flow op, this should just be the 1 resolved lineage process
                    assertEquals(3, task.flowV09DataResults.size)
                    assertEquals("${connection.qualifiedName}/Complex_mapping", task.flowV09ReusableUnitQualifiedName)
                    assertEquals("Complex (mapping)", task.flowV09ReusableUnitName)
                }
            }
        }
    }

    @Test
    fun mappingsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mappings =
            FlowV09ReusableUnit
                .select(client)
                .where(FlowV09ReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV09ReusableUnit.ASSET_USER_DEFINED_TYPE.eq("Mapping"))
                .includeOnResults(FlowV09ReusableUnit.FLOW_V09DATA_FLOWS)
                .includeOnResults(FlowV09ReusableUnit.NAME)
                .includeOnResults(FlowV09ReusableUnit.FLOW_V09PROJECT_NAME)
                .includeOnResults(FlowV09ReusableUnit.FLOW_V09PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV09ReusableUnit.FLOW_V09FOLDER_NAME)
                .includeOnResults(FlowV09ReusableUnit.FLOW_V09FOLDER_QUALIFIED_NAME)
                .includeOnResults(FlowV09ReusableUnit.FLOW_V09DATASET_COUNT)
                .stream()
                .map { it as FlowV09ReusableUnit }
                .toList()
        assertEquals(2, mappings.size)
        mappings.forEach { mapping ->
            assertEquals("Atlan", mapping.flowV09ProjectName)
            assertEquals("sample_folder", mapping.flowV09FolderName)
            assertTrue(mapping.flowV09ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(mapping.flowV09FolderQualifiedName.endsWith("sample_folder"))
            when (mapping.name) {
                "MultiMap (mapping)" -> {
                    // 4 here when we only map the inside lineage portions, otherwise 12
                    assertEquals(4, mapping.flowV09DataFlows.size)
                    assertEquals(8, mapping.flowV09DatasetCount)
                }
                "Complex (mapping)" -> {
                    // 2 here when we only map the inside lineage portions, otherwise 4
                    assertEquals(2, mapping.flowV09DataFlows.size)
                    assertEquals(3, mapping.flowV09DatasetCount)
                }
            }
        }
    }

    @Test
    fun mappletExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mapplets =
            FlowV09ReusableUnit
                .select(client)
                .where(FlowV09ReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV09ReusableUnit.ASSET_USER_DEFINED_TYPE.eq("Mapplet"))
                .includeOnResults(FlowV09ReusableUnit.FLOW_V09DATA_FLOWS)
                .includeOnResults(FlowV09ReusableUnit.FLOW_V09ABSTRACTS)
                .includeOnResults(FlowV09ReusableUnit.NAME)
                .includeOnResults(FlowV09ReusableUnit.FLOW_V09PROJECT_NAME)
                .includeOnResults(FlowV09ReusableUnit.FLOW_V09PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV09ReusableUnit.FLOW_V09FOLDER_NAME)
                .includeOnResults(FlowV09ReusableUnit.FLOW_V09FOLDER_QUALIFIED_NAME)
                .includeOnRelations(Asset.QUALIFIED_NAME)
                .stream()
                .map { it as FlowV09ReusableUnit }
                .toList()
        assertEquals(1, mapplets.size)
        mapplets.forEach { mapplet ->
            assertEquals("Atlan", mapplet.flowV09ProjectName)
            assertEquals("sample_folder", mapplet.flowV09FolderName)
            assertTrue(mapplet.flowV09ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(mapplet.flowV09FolderQualifiedName.endsWith("sample_folder"))
            when (mapplet.name) {
                "Mapplet" -> {
                    assertEquals(3, mapplet.flowV09DataFlows.size)
                    assertEquals(1, mapplet.flowV09Abstracts.size)
                    assertEquals("${connection.qualifiedName}/Complex/transformations/Mapplet", mapplet.flowV09Abstracts.first().qualifiedName)
                }
            }
        }
    }

    @Test
    fun interimDatasetForMappletReferencesItsMapplet() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val ids =
            FlowV09Dataset
                .select(client)
                .where(FlowV09Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV09Dataset.NAME.eq("Mapplet"))
                .includeOnResults(FlowV09Dataset.FLOW_V09DETAILED_BY)
                .includeOnResults(FlowV09Dataset.FLOW_V09PROJECT_NAME)
                .includeOnResults(FlowV09Dataset.FLOW_V09PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV09Dataset.FLOW_V09FOLDER_NAME)
                .includeOnResults(FlowV09Dataset.FLOW_V09FOLDER_QUALIFIED_NAME)
                .includeOnRelations(Asset.QUALIFIED_NAME)
                .stream()
                .map { it as FlowV09Dataset }
                .toList()
        assertEquals(1, ids.size)
        assertEquals("${connection.qualifiedName}/Mapplet", ids[0].flowV09DetailedBy.qualifiedName)
        assertEquals("Atlan", ids[0].flowV09ProjectName)
        assertEquals("sample_folder", ids[0].flowV09FolderName)
        assertTrue(ids[0].flowV09ProjectQualifiedName.endsWith("Atlan"))
        assertTrue(ids[0].flowV09FolderQualifiedName.endsWith("sample_folder"))
    }

    @Test
    fun dataFlowV09OpsNotInTopLevelLineage() {
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
            // Note: we need to EXPLICITLY EXCLUDE FlowV09DataOperation from lineage, if we want to avoid having it
            //  in the traversals...
            val builder =
                FluentLineage
                    .builder(client, table.guid)
                    .includeOnResults(Asset.NAME)
                    .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                    .whereAsset(Asset.TYPE_NAME.inLineage.neq(FlowV09DatasetOperation.TYPE_NAME))
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
            FlowV09Dataset
                .select(client)
                .where(FlowV09Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV09Dataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/MultiMap"))
                .stream()
                .map { it as FlowV09Dataset }
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
            FlowV09FieldOperation
                .select(client)
                .where(FlowV09FieldOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV09FieldOperation.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/MultiMap"))
                .stream()
                .map { it as FlowV09FieldOperation }
                .toList()
        assertEquals(46, fieldOps.size)
    }

    @Test
    fun complexInnerLineage() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val interims =
            FlowV09Dataset
                .select(client)
                .where(FlowV09Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV09Dataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/Complex"))
                .stream()
                .map { it as FlowV09Dataset }
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
            FlowV09Dataset
                .select(client)
                .where(FlowV09Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV09Dataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/Mapplet"))
                .stream()
                .map { it as FlowV09Dataset }
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
            FlowV09DatasetOperation
                .select(client)
                .where(FlowV09DatasetOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV09DatasetOperation.INPUTS)
                .includeOnResults(FlowV09DatasetOperation.OUTPUTS)
                .stream()
                .map { it as FlowV09DatasetOperation }
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
            FlowV09Dataset
                .select(client)
                .where(FlowV09Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV09Dataset.INPUT_TO_PROCESSES)
                .includeOnResults(FlowV09Dataset.OUTPUT_FROM_PROCESSES)
                .includeOnResults(FlowV09Dataset.NAME)
                .includeOnResults(FlowV09Dataset.FLOW_V09PROJECT_NAME)
                .includeOnResults(FlowV09Dataset.FLOW_V09PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV09Dataset.FLOW_V09FOLDER_NAME)
                .includeOnResults(FlowV09Dataset.FLOW_V09FOLDER_QUALIFIED_NAME)
                .includeOnResults(FlowV09Dataset.FLOW_V09REUSABLE_UNIT_NAME)
                .includeOnResults(FlowV09Dataset.FLOW_V09REUSABLE_UNIT_QUALIFIED_NAME)
                .includeOnResults(FlowV09Dataset.FLOW_V09TYPE)
                .includeOnResults(FlowV09Dataset.FLOW_V09FIELD_COUNT)
                .includeOnResults(FlowV09Dataset.FLOW_V09FIELDS)
                .stream()
                .map { it as FlowV09Dataset }
                .toList()
        assertEquals(15, ids.size)
        ids.forEach { id ->
            // And every single one of them acts as at least an input to or output from a FlowV09DataOperation
            val ops = id.inputToProcesses.union(id.outputFromProcesses)
            assertFalse(ops.isEmpty())
            val types = ops.map { it.typeName }.toSet()
            assertEquals(setOf(FlowV09DatasetOperation.TYPE_NAME), types)
            assertEquals("Atlan", id.flowV09ProjectName)
            assertEquals("sample_folder", id.flowV09FolderName)
            assertTrue(id.flowV09ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(id.flowV09FolderQualifiedName.endsWith("sample_folder"))
            when (id.name) {
                "Source2" -> {
                    assertEquals("SOURCE", id.flowV09Type)
                    assertEquals("MultiMap (mapping)", id.flowV09ReusableUnitName)
                    assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(5, id.flowV09FieldCount)
                    assertEquals(5, id.flowV09Fields.size)
                }
                "Target2" -> {
                    assertEquals("TARGET", id.flowV09Type)
                    assertEquals("MultiMap (mapping)", id.flowV09ReusableUnitName)
                    assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(3, id.flowV09FieldCount)
                    assertEquals(3, id.flowV09Fields.size)
                }
                "Source3" -> {
                    assertEquals("SOURCE", id.flowV09Type)
                    assertEquals("MultiMap (mapping)", id.flowV09ReusableUnitName)
                    assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(3, id.flowV09FieldCount)
                    assertEquals(3, id.flowV09Fields.size)
                }
                "Source" -> {
                    assertEquals("SOURCE", id.flowV09Type)
                    when (id.flowV09ReusableUnitName) {
                        "MultiMap (mapping)" -> {
                            assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                            assertEquals(4, id.flowV09FieldCount)
                            assertEquals(4, id.flowV09Fields.size)
                        }
                        "Complex (mapping)" -> {
                            assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("Complex_mapping"))
                        }
                    }
                }
                "Target3" -> {
                    assertEquals("TARGET", id.flowV09Type)
                    assertEquals("MultiMap (mapping)", id.flowV09ReusableUnitName)
                    assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(6, id.flowV09FieldCount)
                    assertEquals(6, id.flowV09Fields.size)
                }
                "Target" -> {
                    assertEquals("TARGET", id.flowV09Type)
                    when (id.flowV09ReusableUnitName) {
                        "MultiMap (mapping)" -> {
                            assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                            assertEquals(4, id.flowV09FieldCount)
                            assertEquals(4, id.flowV09Fields.size)
                        }
                        "Complex (mapping)" -> {
                            assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("Complex_mapping"))
                        }
                    }
                }
                "Source1" -> {
                    assertEquals("SOURCE", id.flowV09Type)
                    assertEquals("MultiMap (mapping)", id.flowV09ReusableUnitName)
                    assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(4, id.flowV09FieldCount)
                    assertEquals(4, id.flowV09Fields.size)
                }
                "Target1" -> {
                    assertEquals("TARGET", id.flowV09Type)
                    assertEquals("MultiMap (mapping)", id.flowV09ReusableUnitName)
                    assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(4, id.flowV09FieldCount)
                    assertEquals(4, id.flowV09Fields.size)
                }
                "Mapplet" -> {
                    assertEquals("MAPPLET", id.flowV09Type)
                    assertEquals("Complex (mapping)", id.flowV09ReusableUnitName)
                    assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("Complex_mapping"))
                }
                "Input" -> {
                    assertEquals("INPUT", id.flowV09Type)
                    assertEquals("Mapplet", id.flowV09ReusableUnitName)
                    assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("Mapplet"))
                }
                "Expression" -> {
                    assertEquals("EXPRESSION", id.flowV09Type)
                    assertEquals("Mapplet", id.flowV09ReusableUnitName)
                    assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("Mapplet"))
                }
                "Aggregator" -> {
                    assertEquals("AGGREGATION", id.flowV09Type)
                    assertEquals("Mapplet", id.flowV09ReusableUnitName)
                    assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("Mapplet"))
                }
                "Output" -> {
                    assertEquals("OUTPUT", id.flowV09Type)
                    assertEquals("Mapplet", id.flowV09ReusableUnitName)
                    assertTrue(id.flowV09ReusableUnitQualifiedName.endsWith("Mapplet"))
                }
            }
            assertFalse(id.flowV09ReusableUnitName.isNullOrEmpty())
            assertFalse(id.flowV09ReusableUnitQualifiedName.isNullOrEmpty())
        }
    }

    @Test
    fun interimFieldsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val fields =
            FlowV09Field
                .select(client)
                .where(FlowV09Field.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV09Field.INPUT_TO_PROCESSES)
                .includeOnResults(FlowV09Field.OUTPUT_FROM_PROCESSES)
                .includeOnResults(FlowV09Field.NAME)
                .includeOnResults(FlowV09Field.FLOW_V09PROJECT_NAME)
                .includeOnResults(FlowV09Field.FLOW_V09PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV09Field.FLOW_V09FOLDER_NAME)
                .includeOnResults(FlowV09Field.FLOW_V09FOLDER_QUALIFIED_NAME)
                .includeOnResults(FlowV09Field.FLOW_V09REUSABLE_UNIT_NAME)
                .includeOnResults(FlowV09Field.FLOW_V09REUSABLE_UNIT_QUALIFIED_NAME)
                .includeOnResults(FlowV09Field.FLOW_V09DATASET_NAME)
                .includeOnResults(FlowV09Field.FLOW_V09DATASET_QUALIFIED_NAME)
                .includeOnResults(FlowV09Field.FLOW_V09DATASET)
                .includeOnResults(FlowV09Field.FLOW_V09DATA_TYPE)
                .stream()
                .map { it as FlowV09Field }
                .toList()
        assertEquals(33, fields.size)
        fields.forEach { field ->
            assertEquals("Atlan", field.flowV09ProjectName)
            assertEquals("sample_folder", field.flowV09FolderName)
            assertTrue(field.flowV09ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(field.flowV09FolderQualifiedName.endsWith("sample_folder"))
            assertFalse(field.flowV09DatasetName.isNullOrEmpty())
            assertFalse(field.flowV09DatasetName.isNullOrEmpty())
            assertEquals("MultiMap (mapping)", field.flowV09ReusableUnitName)
            assertTrue(field.flowV09ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
            when (field.name) {
                "MOVIE_ID", "RELEASE_YEAR", "CUSTOMER_ID", "EMP_ID", "ORDER_ID", "AMOUNT" -> {
                    assertEquals("decimal", field.flowV09DataType)
                }
                "TITLE", "RATING", "STUDIO_NAME", "CHARACTER_NAME", "CUSTOMER_NAME", "COUNTRY", "EMP_NAME", "DEPARTMENT" -> {
                    assertEquals("string", field.flowV09DataType)
                }
                "SALARY" -> {
                    assertTrue(field.flowV09DataType == "decimal" || field.flowV09DataType == "double")
                }
                "ORDER_DATE" -> {
                    assertEquals("date/time", field.flowV09DataType)
                }
            }
        }
    }

    @Test
    fun flowGroupingsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val groupings =
            FlowV09ReusableUnit
                .select(client)
                .where(FlowV09ReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .stream()
                .toList()
        assertEquals(3, groupings.size)
    }
}
