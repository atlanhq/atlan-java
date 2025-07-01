/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.ColumnProcess
import com.atlan.model.assets.Connection
import com.atlan.model.assets.FlowControlOperation
import com.atlan.model.assets.FlowDataset
import com.atlan.model.assets.FlowDatasetOperation
import com.atlan.model.assets.FlowField
import com.atlan.model.assets.FlowFieldOperation
import com.atlan.model.assets.FlowFolder
import com.atlan.model.assets.FlowProject
import com.atlan.model.assets.FlowReusableUnit
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
    private val connectorType = AtlanConnectorType.INFORMATICA_CDI
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
            FlowProject
                .select(client)
                .where(FlowProject.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                // .includeOnResults(FlowProject.FLOW_V02GROUPINGS)
                // .includeOnResults(FlowProject.FLOW_V02DATASETS)
                // .includeOnResults(FlowProject.FLOW_V02FIELDS)
                .stream()
                .map { it as FlowProject }
                .toList()
        assertEquals(1, projects.size)
    }

    @Test
    fun folderExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val folders =
            FlowFolder
                .select(client)
                .where(FlowFolder.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowFolder.FLOW_PROJECT_NAME)
                .includeOnResults(FlowFolder.FLOW_PROJECT_QUALIFIED_NAME)
                // .includeOnResults(FlowFolder.FLOW_V02GROUPINGS)
                // .includeOnResults(FlowFolder.FLOW_V02DATASETS)
                // .includeOnResults(FlowFolder.FLOW_V02FIELDS)
                .stream()
                .map { it as FlowFolder }
                .toList()
        assertEquals(1, folders.size)
        assertEquals("Atlan", folders.first().flowProjectName)
        assertTrue(folders.first().flowProjectQualifiedName.endsWith("Atlan"))
    }

    @Test
    fun visibleLineageExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val processes =
            LineageProcess
                .select(client)
                .where(LineageProcess.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(LineageProcess.FLOW_ORCHESTRATED_BY)
                .stream()
                .map { it as LineageProcess }
                .toList()
        assertEquals(5, processes.size)
        val orchestratedBy = processes.groupBy { it.flowOrchestratedBy.qualifiedName }
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
            FlowControlOperation
                .select(client)
                .where(FlowControlOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowControlOperation.ASSET_USER_DEFINED_TYPE.eq("Mapping Task"))
                .includeOnResults(FlowControlOperation.FLOW_DATA_RESULTS)
                .includeOnResults(FlowControlOperation.FLOW_REUSABLE_UNIT_NAME)
                .includeOnResults(FlowControlOperation.FLOW_REUSABLE_UNIT_QUALIFIED_NAME)
                .includeOnResults(FlowControlOperation.NAME)
                .includeOnResults(FlowControlOperation.FLOW_PROJECT_NAME)
                .includeOnResults(FlowControlOperation.FLOW_PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowControlOperation.FLOW_FOLDER_NAME)
                .includeOnResults(FlowControlOperation.FLOW_FOLDER_QUALIFIED_NAME)
                .stream()
                .map { it as FlowControlOperation }
                .toList()
        assertEquals(2, mt.size)
        mt.forEach { task ->
            assertEquals("Atlan", task.flowProjectName)
            assertEquals("sample_folder", task.flowFolderName)
            assertTrue(task.flowProjectQualifiedName.endsWith("Atlan"))
            assertTrue(task.flowFolderQualifiedName.endsWith("sample_folder"))
            when (task.name) {
                "MultiMap" -> {
                    // 12 here when we may e2s and t2e to the task-level (otherwise 4)
                    // When the task-level is a control flow op, this should just be the 4 resolved lineage processes
                    assertEquals(4, task.flowDataResults.size)
                    assertEquals("${connection.qualifiedName}/MultiMap_mapping", task.flowReusableUnitQualifiedName)
                    assertEquals("MultiMap (mapping)", task.flowReusableUnitName)
                }
                "Complex" -> {
                    // 3 here when we may e2s and t2e to the task-level (otherwise 1)
                    // When the task-level is a control flow op, this should just be the 1 resolved lineage process
                    assertEquals(1, task.flowDataResults.size)
                    assertEquals("${connection.qualifiedName}/Complex_mapping", task.flowReusableUnitQualifiedName)
                    assertEquals("Complex (mapping)", task.flowReusableUnitName)
                }
            }
        }
    }

    @Test
    fun mappingsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mappings =
            FlowReusableUnit
                .select(client)
                .where(FlowReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowReusableUnit.ASSET_USER_DEFINED_TYPE.eq("Mapping"))
                .includeOnResults(FlowReusableUnit.FLOW_DATA_FLOWS)
                .includeOnResults(FlowReusableUnit.NAME)
                .includeOnResults(FlowReusableUnit.FLOW_PROJECT_NAME)
                .includeOnResults(FlowReusableUnit.FLOW_PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowReusableUnit.FLOW_FOLDER_NAME)
                .includeOnResults(FlowReusableUnit.FLOW_FOLDER_QUALIFIED_NAME)
                .includeOnResults(FlowReusableUnit.FLOW_DATASET_COUNT)
                .stream()
                .map { it as FlowReusableUnit }
                .toList()
        assertEquals(2, mappings.size)
        mappings.forEach { mapping ->
            assertEquals("Atlan", mapping.flowProjectName)
            assertEquals("sample_folder", mapping.flowFolderName)
            assertTrue(mapping.flowProjectQualifiedName.endsWith("Atlan"))
            assertTrue(mapping.flowFolderQualifiedName.endsWith("sample_folder"))
            when (mapping.name) {
                "MultiMap (mapping)" -> {
                    // 4 here when we only map the inside lineage portions, otherwise 12
                    assertEquals(12, mapping.flowDataFlows.size)
                    assertEquals(8, mapping.flowDatasetCount)
                }
                "Complex (mapping)" -> {
                    // 2 here when we only map the inside lineage portions, otherwise 4
                    assertEquals(4, mapping.flowDataFlows.size)
                    assertEquals(3, mapping.flowDatasetCount)
                }
            }
        }
    }

    @Test
    fun mappletExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mapplets =
            FlowReusableUnit
                .select(client)
                .where(FlowReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowReusableUnit.ASSET_USER_DEFINED_TYPE.eq("Mapplet"))
                .includeOnResults(FlowReusableUnit.FLOW_DATA_FLOWS)
                .includeOnResults(FlowReusableUnit.FLOW_ABSTRACTS)
                .includeOnResults(FlowReusableUnit.NAME)
                .includeOnResults(FlowReusableUnit.FLOW_PROJECT_NAME)
                .includeOnResults(FlowReusableUnit.FLOW_PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowReusableUnit.FLOW_FOLDER_NAME)
                .includeOnResults(FlowReusableUnit.FLOW_FOLDER_QUALIFIED_NAME)
                .includeOnRelations(Asset.QUALIFIED_NAME)
                .stream()
                .map { it as FlowReusableUnit }
                .toList()
        assertEquals(1, mapplets.size)
        mapplets.forEach { mapplet ->
            assertEquals("Atlan", mapplet.flowProjectName)
            assertEquals("sample_folder", mapplet.flowFolderName)
            assertTrue(mapplet.flowProjectQualifiedName.endsWith("Atlan"))
            assertTrue(mapplet.flowFolderQualifiedName.endsWith("sample_folder"))
            when (mapplet.name) {
                "Mapplet" -> {
                    assertEquals(3, mapplet.flowDataFlows.size)
                    assertEquals(1, mapplet.flowAbstracts.size)
                    assertEquals("${connection.qualifiedName}/Complex/transformations/Mapplet", mapplet.flowAbstracts.first().qualifiedName)
                }
            }
        }
    }

    @Test
    fun interimDatasetForMappletReferencesItsMapplet() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val ids =
            FlowDataset
                .select(client)
                .where(FlowDataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowDataset.NAME.eq("Mapplet"))
                .includeOnResults(FlowDataset.FLOW_DETAILED_BY)
                .includeOnResults(FlowDataset.FLOW_PROJECT_NAME)
                .includeOnResults(FlowDataset.FLOW_PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowDataset.FLOW_FOLDER_NAME)
                .includeOnResults(FlowDataset.FLOW_FOLDER_QUALIFIED_NAME)
                .includeOnRelations(Asset.QUALIFIED_NAME)
                .stream()
                .map { it as FlowDataset }
                .toList()
        assertEquals(1, ids.size)
        assertEquals("${connection.qualifiedName}/Mapplet", ids[0].flowDetailedBy.qualifiedName)
        assertEquals("Atlan", ids[0].flowProjectName)
        assertEquals("sample_folder", ids[0].flowFolderName)
        assertTrue(ids[0].flowProjectQualifiedName.endsWith("Atlan"))
        assertTrue(ids[0].flowFolderQualifiedName.endsWith("sample_folder"))
    }

    @Test
    fun dataFlowOpsNotInTopLevelLineage() {
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
            // Note: we need to EXPLICITLY EXCLUDE FlowDataOperation from lineage, if we want to avoid having it
            //  in the traversals...
            val builder =
                FluentLineage
                    .builder(client, table.guid)
                    .includeOnResults(Asset.NAME)
                    .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                    .whereAsset(Asset.TYPE_NAME.inLineage.neq(FlowDatasetOperation.TYPE_NAME))
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
            FlowDataset
                .select(client)
                .where(FlowDataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowDataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/MultiMap"))
                .stream()
                .map { it as FlowDataset }
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
            FlowFieldOperation
                .select(client)
                .where(FlowFieldOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowFieldOperation.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/MultiMap"))
                .stream()
                .map { it as FlowFieldOperation }
                .toList()
        assertEquals(46, fieldOps.size)
    }

    @Test
    fun complexInnerLineage() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val interims =
            FlowDataset
                .select(client)
                .where(FlowDataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowDataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/Complex"))
                .stream()
                .map { it as FlowDataset }
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
            FlowDataset
                .select(client)
                .where(FlowDataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowDataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/Mapplet"))
                .stream()
                .map { it as FlowDataset }
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
            FlowDatasetOperation
                .select(client)
                .where(FlowDatasetOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowDatasetOperation.INPUTS)
                .includeOnResults(FlowDatasetOperation.OUTPUTS)
                .stream()
                .map { it as FlowDatasetOperation }
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
            FlowDataset
                .select(client)
                .where(FlowDataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowDataset.INPUT_TO_PROCESSES)
                .includeOnResults(FlowDataset.OUTPUT_FROM_PROCESSES)
                .includeOnResults(FlowDataset.NAME)
                .includeOnResults(FlowDataset.FLOW_PROJECT_NAME)
                .includeOnResults(FlowDataset.FLOW_PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowDataset.FLOW_FOLDER_NAME)
                .includeOnResults(FlowDataset.FLOW_FOLDER_QUALIFIED_NAME)
                .includeOnResults(FlowDataset.FLOW_REUSABLE_UNIT_NAME)
                .includeOnResults(FlowDataset.FLOW_REUSABLE_UNIT_QUALIFIED_NAME)
                .includeOnResults(FlowDataset.FLOW_TYPE)
                .includeOnResults(FlowDataset.FLOW_FIELD_COUNT)
                .includeOnResults(FlowDataset.FLOW_FIELDS)
                .includeOnResults(FlowDataset.FLOW_EXPRESSION)
                .includeOnResults(FlowDataset.FLOW_QUERY)
                .stream()
                .map { it as FlowDataset }
                .toList()
        assertEquals(15, ids.size)
        ids.forEach { id ->
            // And every single one of them acts as at least an input to or output from a FlowDataOperation
            val ops = id.inputToProcesses.union(id.outputFromProcesses)
            assertFalse(ops.isEmpty())
            val types = ops.map { it.typeName }.toSet()
            assertEquals(setOf(FlowDatasetOperation.TYPE_NAME), types)
            assertEquals("Atlan", id.flowProjectName)
            assertEquals("sample_folder", id.flowFolderName)
            assertTrue(id.flowProjectQualifiedName.endsWith("Atlan"))
            assertTrue(id.flowFolderQualifiedName.endsWith("sample_folder"))
            when (id.name) {
                "Source2" -> {
                    assertEquals("SOURCE", id.flowType)
                    assertEquals("MultiMap (mapping)", id.flowReusableUnitName)
                    assertTrue(id.flowReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(5, id.flowFieldCount)
                    assertEquals(5, id.flowFields.size)
                    assertEquals("MOVIE_ID = MOVIE_ID", id.flowExpression)
                    assertEquals("SELECT * FROM LOGAN_DATA.INFORMATICA_CDI.MOVIE_DETAILS", id.flowQuery)
                }
                "Target2" -> {
                    assertEquals("TARGET", id.flowType)
                    assertEquals("MultiMap (mapping)", id.flowReusableUnitName)
                    assertTrue(id.flowReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(3, id.flowFieldCount)
                    assertEquals(3, id.flowFields.size)
                }
                "Source3" -> {
                    assertEquals("SOURCE", id.flowType)
                    assertEquals("MultiMap (mapping)", id.flowReusableUnitName)
                    assertTrue(id.flowReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(3, id.flowFieldCount)
                    assertEquals(3, id.flowFields.size)
                    assertEquals("SELECT * FROM LOGAN_DATA.INFORMATICA_CDI.CUSTOMERS01", id.flowQuery)
                }
                "Source" -> {
                    assertEquals("SOURCE", id.flowType)
                    when (id.flowReusableUnitName) {
                        "MultiMap (mapping)" -> {
                            assertTrue(id.flowReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                            assertEquals(4, id.flowFieldCount)
                            assertEquals(4, id.flowFields.size)
                            assertEquals("SELECT * FROM LOGAN_DATA.INFORMATICA_CDI.EMPLOYEES_SR1", id.flowQuery)
                        }
                        "Complex (mapping)" -> {
                            assertTrue(id.flowReusableUnitQualifiedName.endsWith("Complex_mapping"))
                        }
                    }
                }
                "Target3" -> {
                    assertEquals("TARGET", id.flowType)
                    assertEquals("MultiMap (mapping)", id.flowReusableUnitName)
                    assertTrue(id.flowReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(6, id.flowFieldCount)
                    assertEquals(6, id.flowFields.size)
                }
                "Target" -> {
                    assertEquals("TARGET", id.flowType)
                    when (id.flowReusableUnitName) {
                        "MultiMap (mapping)" -> {
                            assertTrue(id.flowReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                            assertEquals(4, id.flowFieldCount)
                            assertEquals(4, id.flowFields.size)
                        }
                        "Complex (mapping)" -> {
                            assertTrue(id.flowReusableUnitQualifiedName.endsWith("Complex_mapping"))
                        }
                    }
                }
                "Source1" -> {
                    assertEquals("SOURCE", id.flowType)
                    assertEquals("MultiMap (mapping)", id.flowReusableUnitName)
                    assertTrue(id.flowReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(4, id.flowFieldCount)
                    assertEquals(4, id.flowFields.size)
                    assertEquals("SELECT * FROM LOGAN_DATA.INFORMATICA_CDI.EMPLOYEES_SR1", id.flowQuery)
                }
                "Target1" -> {
                    assertEquals("TARGET", id.flowType)
                    assertEquals("MultiMap (mapping)", id.flowReusableUnitName)
                    assertTrue(id.flowReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
                    assertEquals(4, id.flowFieldCount)
                    assertEquals(4, id.flowFields.size)
                }
                "Mapplet" -> {
                    assertEquals("MAPPLET", id.flowType)
                    assertEquals("Complex (mapping)", id.flowReusableUnitName)
                    assertTrue(id.flowReusableUnitQualifiedName.endsWith("Complex_mapping"))
                }
                "Input" -> {
                    assertEquals("INPUT", id.flowType)
                    assertEquals("Mapplet", id.flowReusableUnitName)
                    assertTrue(id.flowReusableUnitQualifiedName.endsWith("Mapplet"))
                }
                "Expression" -> {
                    assertEquals("EXPRESSION", id.flowType)
                    assertEquals("Mapplet", id.flowReusableUnitName)
                    assertTrue(id.flowReusableUnitQualifiedName.endsWith("Mapplet"))
                }
                "Aggregator" -> {
                    assertEquals("AGGREGATION", id.flowType)
                    assertEquals("Mapplet", id.flowReusableUnitName)
                    assertTrue(id.flowReusableUnitQualifiedName.endsWith("Mapplet"))
                }
                "Output" -> {
                    assertEquals("OUTPUT", id.flowType)
                    assertEquals("Mapplet", id.flowReusableUnitName)
                    assertTrue(id.flowReusableUnitQualifiedName.endsWith("Mapplet"))
                }
            }
            assertFalse(id.flowReusableUnitName.isNullOrEmpty())
            assertFalse(id.flowReusableUnitQualifiedName.isNullOrEmpty())
        }
    }

    @Test
    fun interimFieldsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val fields =
            FlowField
                .select(client)
                .where(FlowField.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowField.INPUT_TO_PROCESSES)
                .includeOnResults(FlowField.OUTPUT_FROM_PROCESSES)
                .includeOnResults(FlowField.NAME)
                .includeOnResults(FlowField.FLOW_PROJECT_NAME)
                .includeOnResults(FlowField.FLOW_PROJECT_QUALIFIED_NAME)
                .includeOnResults(FlowField.FLOW_FOLDER_NAME)
                .includeOnResults(FlowField.FLOW_FOLDER_QUALIFIED_NAME)
                .includeOnResults(FlowField.FLOW_REUSABLE_UNIT_NAME)
                .includeOnResults(FlowField.FLOW_REUSABLE_UNIT_QUALIFIED_NAME)
                .includeOnResults(FlowField.FLOW_DATASET_NAME)
                .includeOnResults(FlowField.FLOW_DATASET_QUALIFIED_NAME)
                .includeOnResults(FlowField.FLOW_DATASET)
                .includeOnResults(FlowField.FLOW_DATA_TYPE)
                .stream()
                .map { it as FlowField }
                .toList()
        assertEquals(33, fields.size)
        fields.forEach { field ->
            assertEquals("Atlan", field.flowProjectName)
            assertEquals("sample_folder", field.flowFolderName)
            assertTrue(field.flowProjectQualifiedName.endsWith("Atlan"))
            assertTrue(field.flowFolderQualifiedName.endsWith("sample_folder"))
            assertFalse(field.flowDatasetName.isNullOrEmpty())
            assertFalse(field.flowDatasetName.isNullOrEmpty())
            assertEquals("MultiMap (mapping)", field.flowReusableUnitName)
            assertTrue(field.flowReusableUnitQualifiedName.endsWith("MultiMap_mapping"))
            when (field.name) {
                "MOVIE_ID", "RELEASE_YEAR", "CUSTOMER_ID", "EMP_ID", "ORDER_ID", "AMOUNT" -> {
                    assertEquals("decimal", field.flowDataType)
                }
                "TITLE", "RATING", "STUDIO_NAME", "CHARACTER_NAME", "CUSTOMER_NAME", "COUNTRY", "EMP_NAME", "DEPARTMENT" -> {
                    assertEquals("string", field.flowDataType)
                }
                "SALARY" -> {
                    assertTrue(field.flowDataType == "decimal" || field.flowDataType == "double")
                }
                "ORDER_DATE" -> {
                    assertEquals("date/time", field.flowDataType)
                }
            }
        }
    }

    @Test
    fun flowGroupingsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val groupings =
            FlowReusableUnit
                .select(client)
                .where(FlowReusableUnit.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .stream()
                .toList()
        assertEquals(3, groupings.size)
    }
}
