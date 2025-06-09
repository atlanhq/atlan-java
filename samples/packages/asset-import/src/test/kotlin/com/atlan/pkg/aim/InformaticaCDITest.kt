/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.FlowDataOperation
import com.atlan.model.assets.FlowInterimDataset
import com.atlan.model.assets.FlowProcessGrouping
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
    fun visibleLineageExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val processes =
            LineageProcess
                .select(client)
                .where(LineageProcess.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(LineageProcess.FLOW_GROUPING)
                .stream()
                .toList()
        assertEquals(5, processes.size)
    }

    @Test
    fun mappingTasksExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mt =
            FlowProcessGrouping
                .select(client)
                .where(FlowProcessGrouping.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowProcessGrouping.ASSET_USER_DEFINED_TYPE.eq("Mapping Task"))
                .includeOnResults(FlowProcessGrouping.FLOW_DATA_FLOWS)
                .includeOnResults(FlowProcessGrouping.NAME)
                .stream()
                .map { it as FlowProcessGrouping }
                .toList()
        assertEquals(2, mt.size)
        mt.forEach { task ->
            when (task.name) {
                "MultiMap" -> {
                    assertEquals(4, task.flowDataFlows.size)
                }
                "Complex" -> {
                    assertEquals(1, task.flowDataFlows.size)
                }
            }
        }
    }

    @Test
    fun mappingsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mappings =
            FlowProcessGrouping
                .select(client)
                .where(FlowProcessGrouping.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowProcessGrouping.ASSET_USER_DEFINED_TYPE.eq("Mapping"))
                .includeOnResults(FlowProcessGrouping.FLOW_DATA_FLOWS)
                .includeOnResults(FlowProcessGrouping.NAME)
                .stream()
                .map { it as FlowProcessGrouping }
                .toList()
        assertEquals(2, mappings.size)
        mappings.forEach { mapping ->
            when (mapping.name) {
                "MultiMap (mapping)" -> {
                    assertEquals(12, mapping.flowDataFlows.size)
                }
                "Complex (mapping)" -> {
                    assertEquals(4, mapping.flowDataFlows.size)
                }
            }
        }
    }

    @Test
    fun mappletExists() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val mapplets =
            FlowProcessGrouping
                .select(client)
                .where(FlowProcessGrouping.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowProcessGrouping.ASSET_USER_DEFINED_TYPE.eq("Mapplet"))
                .includeOnResults(FlowProcessGrouping.FLOW_DATA_FLOWS)
                .includeOnResults(FlowProcessGrouping.FLOW_ABSTRACTS)
                .includeOnResults(FlowProcessGrouping.NAME)
                .includeOnRelations(Asset.QUALIFIED_NAME)
                .stream()
                .map { it as FlowProcessGrouping }
                .toList()
        assertEquals(1, mapplets.size)
        mapplets.forEach { mapplet ->
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
            FlowInterimDataset
                .select(client)
                .where(FlowInterimDataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowInterimDataset.NAME.eq("Mapplet"))
                .includeOnResults(FlowInterimDataset.FLOW_DETAILED_BY)
                .includeOnRelations(Asset.QUALIFIED_NAME)
                .stream()
                .map { it as FlowInterimDataset }
                .toList()
        assertEquals(1, ids.size)
        assertEquals("${connection.qualifiedName}/Mapplet", ids[0].flowDetailedBy.qualifiedName)
    }

    @Test
    fun dataFlowOpsNotInTopLevelLineage() {
        val connection = Connection.findByName(client, "production", AtlanConnectorType.SNOWFLAKE)[0]!!
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
                    .whereAsset(Asset.TYPE_NAME.inLineage.neq(FlowDataOperation.TYPE_NAME))
            val lineage =
                when (table.name) {
                    "SOURCETABLE", "CUSTOMERS01", "DISNEY_MOVIES", "EMPLOYEES_SR1", "EMPLOYEES_SR" -> {
                        builder
                            .direction(AtlanLineageDirection.DOWNSTREAM)
                            .stream()
                            .toList()
                    }
                    "TARGETTABLE", "CUSTOMER_RESULTANT_JOIN", "MOVIE_DETAILS", "Routersimple02", "Routersimple01" -> {
                        builder
                            .direction(AtlanLineageDirection.UPSTREAM)
                            .stream()
                            .toList()
                    }
                    else -> emptyList<Asset>()
                }
            validateLineage(lineage, 1)
        }
    }

    @Test
    fun multiMapInnerLineage() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val interims =
            FlowInterimDataset
                .select(client)
                .where(FlowInterimDataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowInterimDataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/MultiMap"))
                .stream()
                .map { it as FlowInterimDataset }
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
    fun complexInnerLineage() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val interims =
            FlowInterimDataset
                .select(client)
                .where(FlowInterimDataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowInterimDataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/Complex"))
                .stream()
                .map { it as FlowInterimDataset }
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
            FlowInterimDataset
                .select(client)
                .where(FlowInterimDataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowInterimDataset.QUALIFIED_NAME.startsWith("${connection.qualifiedName}/Mapplet"))
                .stream()
                .map { it as FlowInterimDataset }
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
            FlowDataOperation
                .select(client)
                .where(FlowDataOperation.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowDataOperation.INPUTS)
                .includeOnResults(FlowDataOperation.OUTPUTS)
                .stream()
                .map { it as FlowDataOperation }
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
            FlowInterimDataset
                .select(client)
                .where(FlowInterimDataset.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(FlowInterimDataset.INPUT_TO_PROCESSES)
                .includeOnResults(FlowInterimDataset.OUTPUT_FROM_PROCESSES)
                .includeOnResults(FlowInterimDataset.NAME)
                .stream()
                .map { it as FlowInterimDataset }
                .toList()
        assertEquals(15, ids.size)
        ids.forEach { id ->
            // And every single one of them acts as at least an input to or output from a FlowDataOperation
            val ops = id.inputToProcesses.union(id.outputFromProcesses)
            assertFalse(ops.isEmpty())
            val types = ops.map { it.typeName }.toSet()
            assertEquals(setOf(FlowDataOperation.TYPE_NAME), types)
        }
    }

    @Test
    fun flowGroupingsExist() {
        val connection = Connection.findByName(client, c1, connectorType)[0]!!
        val groupings =
            FlowProcessGrouping
                .select(client)
                .where(FlowProcessGrouping.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .stream()
                .toList()
        assertEquals(5, groupings.size)
    }
}
