/* SPDX-License-Identifier: Apache-2.0
   Copyright 2025 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.util.AssetBatch
import de.siegmar.fastcsv.reader.CsvReader
import org.testng.annotations.Test
import java.nio.file.Paths
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

/*
 * Test export of domain and asset relationships
 */
class ExportDomainRelationshipTest : PackageTest("edr") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val c1 = makeUnique("c1")
    private val connectorType = AtlanConnectorType.RDS

    private val files =
        listOf(
            "asset-export.csv",
            "debug.log",
        )

    private fun createConnections() {
        Connection
            .creator(client, c1, connectorType)
            .build()
            .save(client)
            .block()
    }

    private fun createDomain(): DataDomain {
        val dmn1 = DataDomain.creator(c1).build()
        val response = dmn1.save(client)
        return response.getResult(dmn1)
    }

    private fun createAssets(domainGuid: String) {
        val connection1 = Connection.findByName(client, c1, connectorType)[0]!!
        AssetBatch(client, 50).use { batch ->
            val db1 = Database.creator("db1", connection1.qualifiedName).build()
            val sch1 = Schema.creator("sch1", db1).build()
            val tbl1 =
                Table
                    .creator("tbl1", sch1)
                    .userDescription("Export Domain Relationship Test Table Java SDK")
                    .build()
            batch.add(db1)
            batch.add(sch1)
            batch.add(tbl1)
            batch.flush()
            val update =
                Table
                    .updater(tbl1.qualifiedName, tbl1.name)
                    .domainGUIDs(listOf(domainGuid))
                    .build()
            batch.add(update)
            batch.flush()
        }
    }

    override fun setup() {
        createConnections()
        val domain = createDomain()
        createAssets(domain.guid)
        runCustomPackage(
            AssetExportBasicCfg(
                exportScope = "ALL",
                qnPrefix = Connection.findByName(client, c1, connectorType)?.get(0)?.qualifiedName!!,
                includeGlossaries = false,
                includeProducts = false,
            ),
            Exporter::main,
        )
    }

    override fun teardown() {
        removeConnection(c1, connectorType)
        removeDomain(c1)
    }

    private fun prepFile() {
        // Prepare a copy of the file with unique names for domains and connection
        val testFile = "input.csv"
        val input = Paths.get("src", "test", "resources", "domain_relation.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        val connection = Connection.findByName(client, c1, connectorType)?.get(0)?.qualifiedName!!
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CONNECTIONNAME}}", connection)
                        .replace("{{CONNECTIONID}}", c1)
                        .replace("{{DOMAINNAME}}", c1)
                output.appendText("$revised\n")
            }
        }
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }

    @Test
    fun isExported() {
        prepFile()
        val testFile = "$testDirectory/input.csv"
        val originalFile = "$testDirectory/asset-export.csv"

        val testReader =
            CsvReader
                .builder()
                .fieldSeparator(',')
                .quoteCharacter('"')
                .skipEmptyLines(true)
                .allowMissingFields(false)
                .allowExtraFields(false)

        val originalReader =
            CsvReader
                .builder()
                .fieldSeparator(',')
                .quoteCharacter('"')
                .skipEmptyLines(true)
                .allowMissingFields(false)
                .allowExtraFields(false)

        val testRecords = testReader.ofCsvRecord(Paths.get(testFile)).stream().toList()
        val originalRecords = originalReader.ofCsvRecord(Paths.get(originalFile)).stream().toList()

        assertFalse(testRecords.isEmpty(), "Test CSV file is empty")
        assertFalse(originalRecords.isEmpty(), "Original CSV file is empty")

        val testHeader = testRecords.first().fields
        val originalHeader = originalRecords.first().fields

        val testQualifiedNameIndex = testHeader.indexOf("qualifiedName")
        val originalQualifiedNameIndex = originalHeader.indexOf("qualifiedName")

        assert(testQualifiedNameIndex != -1) { "Column 'qualifiedName' not found in test CSV" }
        assert(originalQualifiedNameIndex != -1) { "Column 'qualifiedName' not found in original CSV" }

        val testRowsByQualifiedName = testRecords.drop(1).associateBy { it.fields[testQualifiedNameIndex] }
        val originalRowsByQualifiedName = originalRecords.drop(1).associateBy { it.fields[originalQualifiedNameIndex] }

        for ((qualifiedName, testRow) in testRowsByQualifiedName) {
            val originalRow = originalRowsByQualifiedName[qualifiedName]
            assertNotNull(originalRow, "Row with qualifiedName '$qualifiedName' is missing in the original file.")
        }
    }
}
