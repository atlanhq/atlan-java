import com.atlan.Atlan
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.core.CustomMetadataAttributes
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType
import com.atlan.model.fields.CustomMetadataField
import com.atlan.model.typedefs.AttributeDef
import com.atlan.model.typedefs.CustomMetadataDef
import com.atlan.pkg.PackageTest
import com.atlan.util.AssetBatch
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private const val TARGET_DB_NAME_1 = "db_test02"

private const val TARGET_DB_NAME_2 = "db_test03"

class EnrichmentMigratorPatternTest : PackageTest() {
    private val c1 = makeUnique("emsc1")
    private val c2 = makeUnique("emsc2")
    private val cm1 = makeUnique("emscm")
    private val now = Instant.now().toEpochMilli()
    private var targetConnectionQualifiedName = ""
    private val targetTableQualifiedNamesByName = mutableMapOf<String, String>()

    private val files = listOf(
        "asset-export.csv",
        "debug.log",
    )

    private fun createConnections() {
        Connection.creator(c1, AtlanConnectorType.MSSQL)
            .build()
            .save()
            .block()
        Connection.creator(c2, AtlanConnectorType.POSTGRES)
            .build()
            .save()
            .block()
    }

    private fun createCustomMetadata() {
        CustomMetadataDef.creator(cm1)
            .attributeDef(AttributeDef.of("dateSingle", AtlanCustomAttributePrimitiveType.DATE, false))
            .build()
            .create()
    }

    private fun createAssets() {
        val client = Atlan.getDefaultClient()
        val connection1 = Connection.findByName(c1, AtlanConnectorType.MSSQL)[0]!!
        val connection2 = Connection.findByName(c2, AtlanConnectorType.POSTGRES)[0]!!
        this.targetConnectionQualifiedName = connection2.qualifiedName
        val batch = AssetBatch(client, 20)
        val db1 = Database.creator("db_test01", connection1.qualifiedName).build()
        batch.add(db1)
        val db2 = Database.creator(TARGET_DB_NAME_1, connection2.qualifiedName).build()
        batch.add(db2)
        val db3 = Database.creator(TARGET_DB_NAME_2, connection2.qualifiedName).build()
        batch.add(db3)
        val sch1 = Schema.creator("sch1", db1).build()
        batch.add(sch1)
        val sch2 = Schema.creator("sch1", db2).build()
        batch.add(sch2)
        val sch3 = Schema.creator("sch1", db3).build()
        batch.add(sch3)
        val tbl1 = Table.creator("tbl1", sch1)
            .customMetadata(
                cm1,
                CustomMetadataAttributes.builder()
                    .attribute("dateSingle", now)
                    .build(),
            )
            .build()
        batch.add(tbl1)
        val tbl2 = Table.creator("tbl1", sch2).build()
        batch.add(tbl2)
        val tbl3 = Table.creator("tbl1", sch3).build()
        batch.add(tbl3)
        val response = batch.flush()
        response.getCreatedAssets(Table::class.java).forEach { table ->
            this.targetTableQualifiedNamesByName.put(table.name, table.qualifiedName)
        }
    }

    @BeforeClass
    fun beforeClass() {
        createConnections()
        createCustomMetadata()
        createAssets()
        setup(
            EnrichmentMigratorCfg(
                sourceConnection = listOf(Connection.findByName(c1, AtlanConnectorType.MSSQL)?.get(0)?.qualifiedName!!),
                targetConnection = listOf(Connection.findByName(c2, AtlanConnectorType.POSTGRES)?.get(0)?.qualifiedName!!),
                sourceQnPrefix = "db_test..",
                isPrefixDatabasePattern = true,
                failOnErrors = false,
                cmLimitType = "INCLUDE",
                customMetadata = "$cm1::dateSingle",
            ),
        )
        EnrichmentMigrator.main(arrayOf(testDirectory))
        Thread.sleep(15000)
    }

    @Test
    fun getDatabaseNames_when_only_one_match_then_returns_one_name() {
        val connection = Connection.findByName(c1, AtlanConnectorType.MSSQL)[0]!!
        val databaseNames = EnrichmentMigrator.getDatabaseNames(connection.qualifiedName, "db_test..")
        assertEquals(1, databaseNames.size)
        assertEquals(listOf("db_test01"), databaseNames)
    }

    @Test
    fun getDatabaseNames_when_multiple_matches_then_returns_multiple_name() {
        val connection = Connection.findByName(c2, AtlanConnectorType.POSTGRES)[0]!!
        val databaseNames = EnrichmentMigrator.getDatabaseNames(connection.qualifiedName, "db_test..")
        assertEquals(2, databaseNames.size)
        assertEquals(listOf(TARGET_DB_NAME_1, TARGET_DB_NAME_2), databaseNames)
    }

    @Test
    fun getDatabaseNames_when_no_matches_then_returns_empty_list() {
        val connection = Connection.findByName(c2, AtlanConnectorType.POSTGRES)[0]!!
        val databaseNames = EnrichmentMigrator.getDatabaseNames(connection.qualifiedName, "")
        assertEquals(0, databaseNames.size)
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
        val targetConnection = Connection.findByName(c2, AtlanConnectorType.POSTGRES)[0]!!
        validateFilesExist(
            listOf(
                "CSA_EM_transformed_${this.targetConnectionQualifiedName}_$TARGET_DB_NAME_1.csv".replace("/", "_"),
                "CSA_EM_transformed_${this.targetConnectionQualifiedName}_$TARGET_DB_NAME_2.csv".replace("/", "_"),
            ),
        )
    }

    @Test
    fun datesOnTarget() {
        val targetConnection = Connection.findByName(c2, AtlanConnectorType.POSTGRES)[0]!!
        val client = Atlan.getDefaultClient()
        val cmField = CustomMetadataField.of(client, cm1, "dateSingle")
        this.targetTableQualifiedNamesByName.forEach { entry ->
            val request = Table.select()
                .where(Table.QUALIFIED_NAME.eq(entry.value))
                .where(cmField.hasAnyValue())
                .includeOnResults(cmField)
                .toRequest()
            val response = retrySearchUntil(request, 1)
            response.stream()
                .forEach {
                    val cm = it.customMetadataSets
                    assertNotNull(cm)
                    val attrs = cm[cm1]?.attributes
                    assertNotNull(attrs)
                    assertEquals(1, attrs.size)
                    assertEquals(now, attrs["dateSingle"])
                }
        }
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        val client = Atlan.getDefaultClient()
        removeConnection(c1, AtlanConnectorType.MSSQL)
        removeConnection(c2, AtlanConnectorType.POSTGRES)
        client.typeDefs.purge(client.customMetadataCache.getIdForName(cm1))
        teardown(context.failedTests.size() > 0)
    }
}
