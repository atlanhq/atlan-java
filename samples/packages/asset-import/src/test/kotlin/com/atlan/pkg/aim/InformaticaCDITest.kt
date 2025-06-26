/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.ColumnProcess
import com.atlan.model.assets.Connection
import com.atlan.model.assets.FlowV08ControlOperation
import com.atlan.model.assets.FlowV08Dataset
import com.atlan.model.assets.FlowV08DatasetOperation
import com.atlan.model.assets.FlowV08Field
import com.atlan.model.assets.FlowV08FieldOperation
import com.atlan.model.assets.FlowV08Folder
import com.atlan.model.assets.FlowV08Project
import com.atlan.model.assets.FlowV08ReusableUnit
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
            FlowV08Project
                .select(client)
                .where(FlowV08Project.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                // .includeOnResults(FlowV08Project.FLOW_V02GROUPINGS)
                // .includeOnResults(FlowV08Project.FLOW_V02DATASETS)
                // .includeOnResults(FlowV08Project.FLOW_V02FIELDS)
                .stream()
                .map { it as FlowV08Project }
                .toList()
        assertEquals(1, projects.size)
    }

    @Test
    fun folderExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val folders =
            FlowV08Folder
                .select(client)
                .where(FlowV08Folder.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV08Folder.FLOW_V08PROJECT_NAME)
                .includeOnResults(FlowV08Folder.FLOW_V08PROJECT_QUALIFIED_NAME)
                // .includeOnResults(FlowV08Folder.FLOW_V02GROUPINGS)
                // .includeOnResults(FlowV08Folder.FLOW_V02DATASETS)
                // .includeOnResults(FlowV08Folder.FLOW_V02FIELDS)
                .stream()
                .map { it as FlowV08Folder }
                .toList()
        assertEquals(1, folders.size)
        assertEquals("Atlan", folders.first().flowV08ProjectName)
        assertTrue(folders.first().flowV08ProjectQualifiedName.endsWith("Atlan"))
    }

    @Test
    fun visibleLineageExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val processes =
            LineageProcess
                .select(client)
                .where(LineageProcess.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(LineageProcess.FLOW_V08ORCHESTRATED_BY)
                .stream()
                .map { it as LineageProcess }
                .toList()
        assertEquals(5, processes.size)
        val orchestratedBy = processes.groupBy { it.flowV08OrchestratedBy.qualifiedName }
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
            FlowV08ControlOperation
                .select(client)
                .where(FlowV08ControlOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV08ControlOperation.ASSET_USER_DEFINED_TYPE.eq("Mapping Task"))
                .includeOnResults(FlowV08ControlOperation.FLOW_V08DATA_RESULTS)
                .includeOnResults(FlowV08ControlOperation.FLOW_V08REUSABLE_UNIT_NAME)
                .includeOnResults(FlowV08ControlOperation.FLOW_V08REUSABLE_UNIT_QUALIFIED_NAME)
                .includeOnResults(FlowV08ControlOperation.NAME)
                .includeOnResults(FlowV08ControlOperation.FLOW_V08PROJECT_NAME)
                .includeOnResults(FlowV08ControlOperation.FLOW_V08PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV08ControlOperation.FLOW_V08FOLDER_NAME)
                .includeOnResults(FlowV08ControlOperation.FLOW_V08FOLDER_QUALIFIED_NAME)
                .stream()
                .map { it as FlowV08ControlOperation }
                .toList()
        assertEquals(2, mt.size)
        mt.forEach { task ->
            assertEquals("Atlan", task.flowV08ProjectName)
            assertEquals("sample_folder", task.flowV08FolderName)
            assertTrue(task.flowV08ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(task.flowV08FolderQualifiedName.endsWith("sample_folder"))
            when (task.name) {
                "MultiMap" -> {
                    // 12 here when we may e2s and t2e to the task-level (otherwise 4)
                    // When the task-level is a control flow op, this should just be the 4 resolved lineage processes
                    assertEquals(12, task.flowV08DataResults.size)
                    assertEquals("${connection.qualifiedName}/MultiMap_mapping", task.flowV08ReusableUnitQualifiedName)
                    assertEquals("MultiMap (mapping)", task.flowV08ReusableUnitName)
                }
                "Complex" -> {
                    // 3 here when we may e2s and t2e to the task-level (otherwise 1)
                    // When the task-level is a control flow op, this should just be the 1 resolved lineage process
                    assertEquals(3, task.flowV08DataResults.size)
                    assertEquals("${connection.qualifiedName}/Complex_mapping", task.flowV08ReusableUnitQualifiedName)
                    assertEquals("Complex (mapping)", task.flowV08ReusableUnitName)
                }
            }
        }
    }

    @Test
    fun mappingsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mappings =
            FlowV08ReusableUnit
                .select(client)
                .where(FlowV08ReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV08ReusableUnit.ASSET_USER_DEFINED_TYPE.eq("Mapping"))
                .includeOnResults(FlowV08ReusableUnit.FLOW_V08DATA_FLOWS)
                .includeOnResults(FlowV08ReusableUnit.NAME)
                .includeOnResults(FlowV08ReusableUnit.FLOW_V08PROJECT_NAME)
                .includeOnResults(FlowV08ReusableUnit.FLOW_V08PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV08ReusableUnit.FLOW_V08FOLDER_NAME)
                .includeOnResults(FlowV08ReusableUnit.FLOW_V08FOLDER_QUALIFIED_NAME)
                .includeOnResults(FlowV08ReusableUnit.FLOW_V08DATASET_COUNT)
                .stream()
                .map { it as FlowV08ReusableUnit }
                .toList()
        assertEquals(2, mappings.size)
        mappings.forEach { mapping ->
            assertEquals("Atlan", mapping.flowV08ProjectName)
            assertEquals("sample_folder", mapping.flowV08FolderName)
            assertTrue(mapping.flowV08ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(mapping.flowV08FolderQualifiedName.endsWith("sample_folder"))
            when (mapping.name) {
                "MultiMap (mapping)" -> {
                    // 4 here when we only map the inside lineage portions, otherwise 12
                    assertEquals(4, mapping.flowV08DataFlows.size)
                    assertEquals(8, mapping.flowV08DatasetCount)
                }
                "Complex (mapping)" -> {
                    // 2 here when we only map the inside lineage portions, otherwise 4
                    assertEquals(2, mapping.flowV08DataFlows.size)
                    assertEquals(3, mapping.flowV08DatasetCount)
                }
            }
        }
    }

    @Test
    fun mappletExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mapplets =
            FlowV08ReusableUnit
                .select(client)
                .where(FlowV08ReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV08ReusableUnit.ASSET_USER_DEFINED_TYPE.eq("Mapplet"))
                .includeOnResults(FlowV08ReusableUnit.FLOW_V08DATA_FLOWS)
                .includeOnResults(FlowV08ReusableUnit.FLOW_V08ABSTRACTS)
                .includeOnResults(FlowV08ReusableUnit.NAME)
                .includeOnResults(FlowV08ReusableUnit.FLOW_V08PROJECT_NAME)
                .includeOnResults(FlowV08ReusableUnit.FLOW_V08PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV08ReusableUnit.FLOW_V08FOLDER_NAME)
                .includeOnResults(FlowV08ReusableUnit.FLOW_V08FOLDER_QUALIFIED_NAME)
                .includeOnRelations(Asset.QUALIFIED_NAME)
                .stream()
                .map { it as FlowV08ReusableUnit }
                .toList()
        assertEquals(1, mapplets.size)
        mapplets.forEach { mapplet ->
            assertEquals("Atlan", mapplet.flowV08ProjectName)
            assertEquals("sample_folder", mapplet.flowV08FolderName)
            assertTrue(mapplet.flowV08ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(mapplet.flowV08FolderQualifiedName.endsWith("sample_folder"))
            when (mapplet.name) {
                "Mapplet" -> {
                    assertEquals(3, mapplet.flowV08DataFlows.size)
                    assertEquals(1, mapplet.flowV08Abstracts.size)
                    assertEquals("${connection.qualifiedName}/Complex/transformations/Mapplet", mapplet.flowV08Abstracts.first().qualifiedName)
                }
            }
        }
    }

    @Test
    fun interimDatasetForMappletReferencesItsMapplet() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val ids =
            FlowV08Dataset
                .select(client)
                .where(FlowV08Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV08Dataset.NAME.eq("Mapplet"))
                .includeOnResults(FlowV08Dataset.FLOW_V08DETAILED_BY)
                .includeOnResults(FlowV08Dataset.FLOW_V08PROJECT_NAME)
                .includeOnResults(FlowV08Dataset.FLOW_V08PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV08Dataset.FLOW_V08FOLDER_NAME)
                .includeOnResults(FlowV08Dataset.FLOW_V08FOLDER_QUALIFIED_NAME)
                .includeOnRelations(Asset.QUALIFIED_NAME)
                .stream()
                .map { it as FlowV08Dataset }
                .toList()
        assertEquals(1, ids.size)
        assertEquals("${connection.qualifiedName}/Mapplet", ids[0].flowV08DetailedBy.qualifiedName)
        assertEquals("Atlan", ids[0].flowV08ProjectName)
        assertEquals("sample_folder", ids[0].flowV08FolderName)
        assertTrue(ids[0].flowV08ProjectQualifiedName.endsWith("Atlan"))
        assertTrue(ids[0].flowV08FolderQualifiedName.endsWith("sample_folder"))
    }

    @Test
    fun dataFlowV08OpsNotInTopLevelLineage() {
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
            // Note: we need to EXPLICITLY EXCLUDE FlowV08DataOperation from lineage, if we want to avoid having it
            //  in the traversals...
            val builder =
                FluentLineage
                    .builder(client, table.guid)
                    .includeOnResults(Asset.NAME)
                    .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                    .whereAsset(Asset.TYPE_NAME.inLineage.neq(FlowV08DatasetOperation.TYPE_NAME))
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
            FlowV08Dataset
                .select(client)
                .where(FlowV08Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV08Dataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/MultiMap"))
                .stream()
                .map { it as FlowV08Dataset }
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
            FlowV08FieldOperation
                .select(client)
                .where(FlowV08FieldOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV08FieldOperation.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/MultiMap"))
                .stream()
                .map { it as FlowV08FieldOperation }
                .toList()
        assertEquals(46, fieldOps.size)
    }

    @Test
    fun complexInnerLineage() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val interims =
            FlowV08Dataset
                .select(client)
                .where(FlowV08Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV08Dataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/Complex"))
                .stream()
                .map { it as FlowV08Dataset }
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
            FlowV08Dataset
                .select(client)
                .where(FlowV08Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowV08Dataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/Mapplet"))
                .stream()
                .map { it as FlowV08Dataset }
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
            FlowV08DatasetOperation
                .select(client)
                .where(FlowV08DatasetOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV08DatasetOperation.INPUTS)
                .includeOnResults(FlowV08DatasetOperation.OUTPUTS)
                .stream()
                .map { it as FlowV08DatasetOperation }
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
            FlowV08Dataset
                .select(client)
                .where(FlowV08Dataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV08Dataset.INPUT_TO_PROCESSES)
                .includeOnResults(FlowV08Dataset.OUTPUT_FROM_PROCESSES)
                .includeOnResults(FlowV08Dataset.NAME)
                .includeOnResults(FlowV08Dataset.FLOW_V08PROJECT_NAME)
                .includeOnResults(FlowV08Dataset.FLOW_V08PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV08Dataset.FLOW_V08FOLDER_NAME)
                .includeOnResults(FlowV08Dataset.FLOW_V08FOLDER_QUALIFIED_NAME)
                .includeOnResults(FlowV08Dataset.FLOW_V08REUSABLE_UNIT_NAME)
                .includeOnResults(FlowV08Dataset.FLOW_V08REUSABLE_UNIT_QUALIFIED_NAME)
                .includeOnResults(FlowV08Dataset.FLOW_V08TYPE)
                .includeOnResults(FlowV08Dataset.FLOW_V08FIELD_COUNT)
                .includeOnResults(FlowV08Dataset.FLOW_V08FIELDS)
                .stream()
                .map { it as FlowV08Dataset }
                .toList()
        assertEquals(15, ids.size)
        ids.forEach { id ->
            // And every single one of them acts as at least an input to or output from a FlowV08DataOperation
            val ops = id.inputToProcesses.union(id.outputFromProcesses)
            assertFalse(ops.isEmpty())
            val types = ops.map { it.typeName }.toSet()
            assertEquals(setOf(FlowV08DatasetOperation.TYPE_NAME), types)
            assertEquals("Atlan", id.flowV08ProjectName)
            assertEquals("sample_folder", id.flowV08FolderName)
            assertTrue(id.flowV08ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(id.flowV08FolderQualifiedName.endsWith("sample_folder"))
            when (id.name) {
                "Source2" -> {
                    assertEquals("SOURCE", id.flowV08Type)
                    assertEquals("MultiMap (mapping)", id.flowV08ReusableUnitName)
                    assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(5, id.flowV08FieldCount)
                    assertEquals(5, id.flowV08Fields.size)
                }
                "Target2" -> {
                    assertEquals("TARGET", id.flowV08Type)
                    assertEquals("MultiMap (mapping)", id.flowV08ReusableUnitName)
                    assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(3, id.flowV08FieldCount)
                    assertEquals(3, id.flowV08Fields.size)
                }
                "Source3" -> {
                    assertEquals("SOURCE", id.flowV08Type)
                    assertEquals("MultiMap (mapping)", id.flowV08ReusableUnitName)
                    assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(3, id.flowV08FieldCount)
                    assertEquals(3, id.flowV08Fields.size)
                }
                "Source" -> {
                    assertEquals("SOURCE", id.flowV08Type)
                    when (id.flowV08ReusableUnitName) {
                        "MultiMap (mapping)" -> {
                            assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                            assertEquals(4, id.flowV08FieldCount)
                            assertEquals(4, id.flowV08Fields.size)
                        }
                        "Complex (mapping)" -> {
                            assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("Complex_mapping"))
                        }
                    }
                }
                "Target3" -> {
                    assertEquals("TARGET", id.flowV08Type)
                    assertEquals("MultiMap (mapping)", id.flowV08ReusableUnitName)
                    assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(6, id.flowV08FieldCount)
                    assertEquals(6, id.flowV08Fields.size)
                }
                "Target" -> {
                    assertEquals("TARGET", id.flowV08Type)
                    when (id.flowV08ReusableUnitName) {
                        "MultiMap (mapping)" -> {
                            assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                            assertEquals(4, id.flowV08FieldCount)
                            assertEquals(4, id.flowV08Fields.size)
                        }
                        "Complex (mapping)" -> {
                            assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("Complex_mapping"))
                        }
                    }
                }
                "Source1" -> {
                    assertEquals("SOURCE", id.flowV08Type)
                    assertEquals("MultiMap (mapping)", id.flowV08ReusableUnitName)
                    assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(4, id.flowV08FieldCount)
                    assertEquals(4, id.flowV08Fields.size)
                }
                "Target1" -> {
                    assertEquals("TARGET", id.flowV08Type)
                    assertEquals("MultiMap (mapping)", id.flowV08ReusableUnitName)
                    assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(4, id.flowV08FieldCount)
                    assertEquals(4, id.flowV08Fields.size)
                }
                "Mapplet" -> {
                    assertEquals("MAPPLET", id.flowV08Type)
                    assertEquals("Complex (mapping)", id.flowV08ReusableUnitName)
                    assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("Complex_mapping"))
                }
                "Input" -> {
                    assertEquals("INPUT", id.flowV08Type)
                    assertEquals("Mapplet", id.flowV08ReusableUnitName)
                    assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("Mapplet"))
                }
                "Expression" -> {
                    assertEquals("EXPRESSION", id.flowV08Type)
                    assertEquals("Mapplet", id.flowV08ReusableUnitName)
                    assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("Mapplet"))
                }
                "Aggregator" -> {
                    assertEquals("AGGREGATION", id.flowV08Type)
                    assertEquals("Mapplet", id.flowV08ReusableUnitName)
                    assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("Mapplet"))
                }
                "Output" -> {
                    assertEquals("OUTPUT", id.flowV08Type)
                    assertEquals("Mapplet", id.flowV08ReusableUnitName)
                    assertTrue(id.flowV08ReusableUnitQualifiedName.endsWith("Mapplet"))
                }
            }
            assertFalse(id.flowV08ReusableUnitName.isNullOrEmpty())
            assertFalse(id.flowV08ReusableUnitQualifiedName.isNullOrEmpty())
        }
    }

    @Test
    fun interimFieldsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val fields =
            FlowV08Field
                .select(client)
                .where(FlowV08Field.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowV08Field.INPUT_TO_PROCESSES)
                .includeOnResults(FlowV08Field.OUTPUT_FROM_PROCESSES)
                .includeOnResults(FlowV08Field.NAME)
                .includeOnResults(FlowV08Field.FLOW_V08PROJECT_NAME)
                .includeOnResults(FlowV08Field.FLOW_V08PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowV08Field.FLOW_V08FOLDER_NAME)
                .includeOnResults(FlowV08Field.FLOW_V08FOLDER_QUALIFIED_NAME)
                // TODO: reusable unit de-normed attributes
                .includeOnResults(FlowV08Field.FLOW_V08DATASET_NAME)
                .includeOnResults(FlowV08Field.FLOW_V08DATASET_QUALIFIED_NAME)
                .includeOnResults(FlowV08Field.FLOW_V08DATASET)
                .includeOnResults(FlowV08Field.FLOW_V08DATA_TYPE)
                .stream()
                .map { it as FlowV08Field }
                .toList()
        assertEquals(33, fields.size)
        fields.forEach { field ->
            assertEquals("Atlan", field.flowV08ProjectName)
            assertEquals("sample_folder", field.flowV08FolderName)
            assertTrue(field.flowV08ProjectQualifiedName.endsWith("Atlan"))
            assertTrue(field.flowV08FolderQualifiedName.endsWith("sample_folder"))
            assertFalse(field.flowV08DatasetName.isNullOrEmpty())
            assertFalse(field.flowV08DatasetName.isNullOrEmpty())
            when (field.name) {
                "MOVIE_ID", "RELEASE_YEAR", "CUSTOMER_ID", "EMP_ID", "ORDER_ID", "AMOUNT" -> {
                    assertEquals("decimal", field.flowV08DataType)
                }
                "TITLE", "RATING", "STUDIO_NAME", "CHARACTER_NAME", "CUSTOMER_NAME", "COUNTRY", "EMP_NAME", "DEPARTMENT" -> {
                    assertEquals("string", field.flowV08DataType)
                }
                "SALARY" -> {
                    assertTrue(field.flowV08DataType == "decimal" || field.flowV08DataType == "double")
                }
                "ORDER_DATE" -> {
                    assertEquals("date/time", field.flowV08DataType)
                }
            }
        }
    }

    @Test
    fun flowGroupingsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val groupings =
            FlowV08ReusableUnit
                .select(client)
                .where(FlowV08ReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .stream()
                .toList()
        assertEquals(3, groupings.size)
    }
}
