/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.FlowDataOperation
import com.atlan.model.assets.FlowInterimDataset
import com.atlan.model.assets.FlowProcessGrouping
import com.atlan.model.assets.LineageProcess
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import org.testng.Assert.assertFalse
import org.testng.annotations.Test
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test export of detailed view and change information.
 */
class InformaticaCDITest : PackageTest("cdi") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val c1 = makeUnique("c1")
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
        removeConnection(c1, "iics")
    }

    @Test
    fun connectionExists() {
        val connection = Connection.findByName(client, "Informatica", "iics")
        assertNotNull(connection)
        assertFalse(connection.isEmpty())
        assertEquals(1, connection.size)
    }

    @Test
    fun visibleLineageExists() {
        val connection = Connection.findByName(client, "Informatica", "iics")[0]!!
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
        val connection = Connection.findByName(client, "Informatica", "iics")[0]!!
        val mt =
            FlowProcessGrouping
                .select(client)
                .where(FlowProcessGrouping.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowProcessGrouping.SUB_TYPE.eq("Mapping Task"))
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
        val connection = Connection.findByName(client, "Informatica", "iics")[0]!!
        val mappings =
            FlowProcessGrouping
                .select(client)
                .where(FlowProcessGrouping.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowProcessGrouping.SUB_TYPE.eq("Mapping"))
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
        val connection = Connection.findByName(client, "Informatica", "iics")[0]!!
        val mapplets =
            FlowProcessGrouping
                .select(client)
                .where(FlowProcessGrouping.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(FlowProcessGrouping.SUB_TYPE.eq("Mapplet"))
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
        val connection = Connection.findByName(client, "Informatica", "iics")[0]!!
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

    // ... OTHER BASIC TESTS ...

    @Test
    fun drilldownLineageExists() {
        val connection = Connection.findByName(client, "Informatica", "iics")[0]!!
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
        val connection = Connection.findByName(client, "Informatica", "iics")[0]!!
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
        val connection = Connection.findByName(client, "Informatica", "iics")[0]!!
        val groupings =
            FlowProcessGrouping
                .select(client)
                .where(FlowProcessGrouping.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .stream()
                .toList()
        assertEquals(5, groupings.size)
    }
}
