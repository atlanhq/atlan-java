/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.exception.InvalidRequestException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import com.atlan.util.AssetBatch
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EnrichmentMigratorPatternTest : PackageTest("p") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val targetDbName1 = "db_test02"
    private val targetDbName2 = "db_test03"
    private val sourceDbName = "db_test01"
    private val dbNamePattern = "db_test.."
    private val userDescription = "Some user description"

    private val c1 = makeUnique("c1")
    private val c2 = makeUnique("c2")
    private val c1Type = AtlanConnectorType.AWS_GREENGRASS
    private val c2Type = AtlanConnectorType.MPARTICLE
    private var sourceConnectionQualifiedName = ""
    private var targetConnectionQualifiedName = ""
    private val targetTableQualifiedNamesByName = mutableMapOf<String, String>()

    private val files =
        listOf(
            "asset-export.csv",
            "transformed-file.csv",
            "debug.log",
        )

    private fun createConnections() {
        AssetBatch(client, 5).use { batch ->
            batch.add(Connection.creator(client, c1, c1Type).build())
            batch.add(Connection.creator(client, c2, c2Type).build())
            batch.flush()
        }
    }

    private fun createAssets() {
        val connection1 = Connection.findByName(client, c1, c1Type)[0]!!
        this.sourceConnectionQualifiedName = connection1.qualifiedName
        val connection2 = Connection.findByName(client, c2, c2Type)[0]!!
        this.targetConnectionQualifiedName = connection2.qualifiedName
        AssetBatch(client, 20).use { batch ->
            val db1 = Database.creator(sourceDbName, connection1.qualifiedName).build()
            batch.add(db1)
            val db2 = Database.creator(targetDbName1, connection2.qualifiedName).build()
            batch.add(db2)
            val db3 = Database.creator(targetDbName2, connection2.qualifiedName).build()
            batch.add(db3)
            val sch1 = Schema.creator("sch1", db1).build()
            batch.add(sch1)
            val sch2 = Schema.creator("sch1", db2).build()
            batch.add(sch2)
            val sch3 = Schema.creator("sch1", db3).build()
            batch.add(sch3)
            val tbl1 =
                Table
                    .creator("tbl1", sch1)
                    .userDescription(userDescription)
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
    }

    override fun setup() {
        createConnections()
        createAssets()
        runCustomPackage(
            EnrichmentMigratorCfg(
                sourceConnection = listOf(Connection.findByName(client, c1, c1Type)?.get(0)?.qualifiedName!!),
                targetConnection = listOf(Connection.findByName(client, c2, c2Type)?.get(0)?.qualifiedName!!),
                sourceQnPrefix = sourceDbName,
                targetDatabasePattern = dbNamePattern,
                failOnErrors = false,
                limitType = "EXCLUDE",
            ),
            EnrichmentMigrator::main,
        )
        runCustomPackage(
            AssetImportCfg(
                assetsFile = "$testDirectory${File.separator}transformed-file.csv",
                assetsUpsertSemantic = "update",
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(c1, c1Type)
        removeConnection(c2, c2Type)
    }

    @Test
    fun getDatabaseNamesWhenOnlyOneMatchThenReturnsOneName() {
        val connection = Connection.findByName(client, c1, c1Type)[0]!!
        val databaseNames = EnrichmentMigrator.getDatabaseNames(client, connection.qualifiedName, dbNamePattern)
        assertEquals(1, databaseNames.size)
        assertEquals(listOf(sourceDbName), databaseNames)
    }

    @Test
    fun getDatabaseNamesWhenMultipleMatchesThenReturnsMultipleName() {
        val connection = Connection.findByName(client, c2, c2Type)[0]!!
        val databaseNames = EnrichmentMigrator.getDatabaseNames(client, connection.qualifiedName, dbNamePattern)
        assertEquals(2, databaseNames.size)
        assertEquals(listOf(targetDbName1, targetDbName2), databaseNames)
    }

    @Test
    fun getDatabaseNamesWhenNoMatchesThenReturnsEmptyList() {
        val connection = Connection.findByName(client, c2, c2Type)[0]!!
        val databaseNames = EnrichmentMigrator.getDatabaseNames(client, connection.qualifiedName, "")
        assertEquals(0, databaseNames.size)
    }

    @Test
    fun getSourceDatabaseNamesWhenIsPrefixAndOneMatchReturnsName() {
        assertEquals(
            sourceDbName,
            EnrichmentMigrator.getSourceDatabaseNames(
                client,
                dbNamePattern,
                this.sourceConnectionQualifiedName,
                sourceDbName,
            ),
        )
    }

    @Test
    fun getSourceDatabaseNamesWhenIsPrefixAndMoreThanOneFoundThrowsException() {
        val exception =
            assertFailsWith<InvalidRequestException>(
                block = {
                    EnrichmentMigrator.getSourceDatabaseNames(
                        client,
                        dbNamePattern,
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
        val exception =
            assertFailsWith<InvalidRequestException>(
                block = {
                    EnrichmentMigrator.getSourceDatabaseNames(
                        client,
                        dbNamePattern,
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
                client,
                "",
                this.sourceConnectionQualifiedName,
                sourceDbName,
            ),
        )
    }

    @Test
    fun getTargetDatabaseNamesWhenHasMatchReturnsNames() {
        assertEquals(
            listOf(targetDbName1, targetDbName2),
            EnrichmentMigrator.getTargetDatabaseName(
                client,
                this.targetConnectionQualifiedName,
                dbNamePattern,
            ),
        )
    }

    @Test
    fun getTargetDatabaseNamesWhenIsPrefixAndNoneFoundThrowsException() {
        val exception =
            assertFailsWith<InvalidRequestException>(
                block = {
                    EnrichmentMigrator.getTargetDatabaseName(
                        client,
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
    }

    @Test
    fun userDescriptionOnTarget() {
        this.targetTableQualifiedNamesByName.forEach { entry ->
            val request =
                Table
                    .select(client)
                    .where(Table.QUALIFIED_NAME.eq(entry.value))
                    .includeOnResults(Asset.USER_DESCRIPTION)
                    .toRequest()
            val response = retrySearchUntil(request, 1)
            response
                .stream()
                .forEach {
                    assertEquals(userDescription, it.userDescription)
                }
        }
    }

    @Test
    fun userDescriptionInFileForTarget2() {
        val targetConnection = Connection.findByName(client, c2, c2Type)[0]!!
        fileHasLineStartingWith(
            filename = "transformed-file.csv",
            line =
                """
                "${targetConnection.qualifiedName}/db_test02/sch1/tbl1","Table","tbl1",,,"Some user description"
                """.trimIndent(),
        )
    }

    @Test
    fun userDescriptionInFileForTarget3() {
        val targetConnection = Connection.findByName(client, c2, c2Type)[0]!!
        fileHasLineStartingWith(
            filename = "transformed-file.csv",
            line =
                """
                "${targetConnection.qualifiedName}/db_test03/sch1/tbl1","Table","tbl1",,,"Some user description"
                """.trimIndent(),
        )
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
