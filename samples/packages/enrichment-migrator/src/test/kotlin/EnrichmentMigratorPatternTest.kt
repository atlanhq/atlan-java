/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.exception.InvalidRequestException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.util.AssetBatch
import mu.KotlinLogging
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EnrichmentMigratorPatternTest : PackageTest() {
    override val logger = KotlinLogging.logger {}

    private val TARGET_DB_NAME_1 = "db_test02"
    private val TARGET_DB_NAME_2 = "db_test03"
    private val SOURCE_DATABASE_NAME = "db_test01"
    private val DB_NAME_PATTERN = "db_test.."
    private val USER_DESCRIPTION = "Some user description"

    private val c1 = makeUnique("empc1")
    private val c2 = makeUnique("empc2")
    private var sourceConnectionQualifiedName = ""
    private var targetConnectionQualifiedName = ""
    private val targetTableQualifiedNamesByName = mutableMapOf<String, String>()

    private val files = listOf(
        "asset-export.csv",
        "debug.log",
    )

    private fun createConnections() {
        val batch = AssetBatch(Atlan.getDefaultClient(), 5)
        batch.add(Connection.creator(c1, AtlanConnectorType.MSSQL).build())
        batch.add(Connection.creator(c2, AtlanConnectorType.POSTGRES).build())
        batch.flush()
    }

    private fun createAssets() {
        val client = Atlan.getDefaultClient()
        val connection1 = Connection.findByName(c1, AtlanConnectorType.MSSQL)[0]!!
        this.sourceConnectionQualifiedName = connection1.qualifiedName
        val connection2 = Connection.findByName(c2, AtlanConnectorType.POSTGRES)[0]!!
        this.targetConnectionQualifiedName = connection2.qualifiedName
        val batch = AssetBatch(client, 20)
        val db1 = Database.creator(SOURCE_DATABASE_NAME, connection1.qualifiedName).build()
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
            .userDescription(USER_DESCRIPTION)
            .build()
        batch.add(tbl1)
        val tbl2 = Table.creator("tbl1", sch2).build()
        batch.add(tbl2)
        val tbl3 = Table.creator("tbl1", sch3).build()
        batch.add(tbl3)
        val response = batch.flush()
        response.getCreatedAssets(Table::class.java).forEach { table ->
            this.targetTableQualifiedNamesByName[table.name] = table.qualifiedName
        }
    }

    override fun setup() {
        createConnections()
        createAssets()
        setup(
            EnrichmentMigratorCfg(
                sourceConnection = listOf(Connection.findByName(c1, AtlanConnectorType.MSSQL)?.get(0)?.qualifiedName!!),
                targetConnection = listOf(Connection.findByName(c2, AtlanConnectorType.POSTGRES)?.get(0)?.qualifiedName!!),
                sourceQnPrefix = SOURCE_DATABASE_NAME,
                targetDatabasePattern = DB_NAME_PATTERN,
                failOnErrors = false,
                limitType = "EXCLUDE",
            ),
        )
        EnrichmentMigrator.main(arrayOf(testDirectory))
        Thread.sleep(15000)
    }

    override fun teardown() {
        removeConnection(c1, AtlanConnectorType.MSSQL)
        removeConnection(c2, AtlanConnectorType.POSTGRES)
    }

    @Test
    fun getDatabaseNamesWhenOnlyOneMatchThenReturnsOneName() {
        val connection = Connection.findByName(c1, AtlanConnectorType.MSSQL)[0]!!
        val databaseNames = EnrichmentMigrator.getDatabaseNames(connection.qualifiedName, DB_NAME_PATTERN)
        assertEquals(1, databaseNames.size)
        assertEquals(listOf(SOURCE_DATABASE_NAME), databaseNames)
    }

    @Test
    fun getDatabaseNamesWhenMultipleMatchesThenReturnsMultipleName() {
        val connection = Connection.findByName(c2, AtlanConnectorType.POSTGRES)[0]!!
        val databaseNames = EnrichmentMigrator.getDatabaseNames(connection.qualifiedName, DB_NAME_PATTERN)
        assertEquals(2, databaseNames.size)
        assertEquals(listOf(TARGET_DB_NAME_1, TARGET_DB_NAME_2), databaseNames)
    }

    @Test
    fun getDatabaseNamesWhenNoMatchesThenReturnsEmptyList() {
        val connection = Connection.findByName(c2, AtlanConnectorType.POSTGRES)[0]!!
        val databaseNames = EnrichmentMigrator.getDatabaseNames(connection.qualifiedName, "")
        assertEquals(0, databaseNames.size)
    }

    @Test
    fun getSourceDatabaseNamesWhenIsPrefixAndOneMatchReturnsName() {
        assertEquals(
            SOURCE_DATABASE_NAME,
            EnrichmentMigrator.getSourceDatabaseNames(
                DB_NAME_PATTERN,
                this.sourceConnectionQualifiedName,
                SOURCE_DATABASE_NAME,
            ),
        )
    }

    @Test
    fun getSourceDatabaseNamesWhenIsPrefixAndMoreThanOneFoundThrowsException() {
        val exception = assertFailsWith<InvalidRequestException>(
            block = {
                EnrichmentMigrator.getSourceDatabaseNames(
                    DB_NAME_PATTERN,
                    this.targetConnectionQualifiedName,
                    ".*",
                )
            },
        )
        assertEquals(
            "ATLAN-JAVA-400-047 Expected only one database name(s) matching the given pattern .* " +
                "but found 2. Suggestion: Use a more restrictive regular expression.",
            exception.message,
        )
    }

    @Test
    fun getSourceDatabaseNameWhenIsPrefixAndNoneFoundThrowsException() {
        val exception = assertFailsWith<InvalidRequestException>(
            block = {
                EnrichmentMigrator.getSourceDatabaseNames(
                    DB_NAME_PATTERN,
                    this.sourceConnectionQualifiedName,
                    ".",
                )
            },
        )
        assertEquals(
            "ATLAN-JAVA-400-047 Expected only one database name(s) matching the given pattern . " +
                "but found 0. Suggestion: Use a more restrictive regular expression.",
            exception.message,
        )
    }

    @Test
    fun getSourceDatabaseNamesWhenIsNotPrefixThenReturnsEmptyString() {
        assertEquals(
            "",
            EnrichmentMigrator.getSourceDatabaseNames(
                "",
                this.sourceConnectionQualifiedName,
                SOURCE_DATABASE_NAME,
            ),
        )
    }

    @Test
    fun getTargetDatabaseNamesWhenHasMatchReturnsNames() {
        assertEquals(
            listOf(TARGET_DB_NAME_1, TARGET_DB_NAME_2),
            EnrichmentMigrator.getTargetDatabaseName(
                this.targetConnectionQualifiedName,
                DB_NAME_PATTERN,
            ),
        )
    }

    @Test
    fun getTargetDatabaseNamesWhenIsPrefixAndNoneFoundThrowsException() {
        val exception = assertFailsWith<InvalidRequestException>(
            block = {
                EnrichmentMigrator.getTargetDatabaseName(
                    this.targetConnectionQualifiedName,
                    ".",
                )
            },
        )
        assertEquals(
            "ATLAN-JAVA-400-047 Expected at least one database name(s) matching the given pattern " +
                ". but found 0. Suggestion: Use a more restrictive regular expression.",
            exception.message,
        )
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
        validateFilesExist(
            listOf(
                "CSA_EM_transformed_${this.targetConnectionQualifiedName}_$TARGET_DB_NAME_1.csv".replace("/", "_"),
                "CSA_EM_transformed_${this.targetConnectionQualifiedName}_$TARGET_DB_NAME_2.csv".replace("/", "_"),
            ),
        )
    }

    @Test
    fun userDescriptionOnTarget() {
        val client = Atlan.getDefaultClient()
        this.targetTableQualifiedNamesByName.forEach { entry ->
            val request = Table.select()
                .where(Table.QUALIFIED_NAME.eq(entry.value))
                .includeOnResults(Asset.USER_DESCRIPTION)
                .toRequest()
            val response = retrySearchUntil(request, 1)
            response.stream()
                .forEach {
                    assertEquals(USER_DESCRIPTION, it.userDescription)
                }
        }
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
