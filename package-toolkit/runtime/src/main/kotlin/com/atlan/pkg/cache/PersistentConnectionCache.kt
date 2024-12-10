/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import mu.KotlinLogging
import java.sql.DriverManager
import java.sql.SQLException
import java.util.stream.Stream

/**
 * Cache that contains a listing of all asset identities for a given connection, and stores
 * these persistently in object storage for use by other workflows (i.e. in considering lineage).
 *
 * @param dbFile the file path to the SQLite database file
 * @param ignoreQNs a collection of qualifiedNames to ignore
 */
class PersistentConnectionCache(
    private val dbFile: String,
    private val ignoreQNs: Collection<String> = emptySet(),
) {
    private val logger = KotlinLogging.logger {}
    private val dbString = "jdbc:sqlite:$dbFile"

    init {
        val tableQuery =
            """
            CREATE TABLE entities (
                type_name TEXT NOT NULL,
                qual_name TEXT NOT NULL,
                con_qual_name TEXT,
                name TEXT,
                order_seq INTEGER,
                tenant_id TEXT,
                PRIMARY KEY (type_name, qual_name)
            )
            """.trimIndent()

        DriverManager.getConnection(dbString).use { connection ->
            connection.createStatement().use { statement ->
                try {
                    statement.executeQuery("SELECT type_name FROM entities LIMIT 1")
                } catch (e: SQLException) {
                    // Only create a new database if there is not an existing one already
                    statement.executeUpdate("DROP TABLE IF EXISTS entities")
                    // sqlite does not have create or replace, so we drop and create
                    statement.executeUpdate(tableQuery)
                    // if querying on both typename and qualified name, this is far more efficient and drops run times from 2.5 hours to 30 mins
                    statement.executeUpdate("CREATE INDEX typename_index ON entities(type_name, qual_name COLLATE NOCASE)")
                    statement.executeUpdate("CREATE INDEX qualified_name_index ON entities(qual_name COLLATE NOCASE)")
                    statement.executeUpdate("CREATE INDEX name_index ON entities(name COLLATE NOCASE)")
                }
            }
        }
    }

    /**
     * Remove the provided assets from the persistent connection cache.
     *
     * @param assets to remove from the cache
     */
    fun deleteAssets(assets: Stream<Asset>) {
        DriverManager.getConnection(dbString).use { connection ->
            connection.prepareStatement("delete from entities where type_name = ? and qual_name = ?").use { ps ->
                assets.forEach { a ->
                    ps.setString(1, a.typeName)
                    ps.setString(2, a.qualifiedName)
                    ps.executeUpdate()
                }
            }
        }
    }

    /**
     * Add the provided assets to the persistent connection cache.
     *
     * @param assets to add to the cache
     */
    fun addAssets(assets: Stream<Asset>) {
        DriverManager.getConnection(dbString).use { connection ->
            connection.prepareStatement("insert or replace into entities values(?, ?, ?, ?, ?, ?)").use { ps ->
                assets.forEach { a ->
                    if (a.qualifiedName in ignoreQNs) {
                        logger.debug { "Skipping ${a.qualifiedName} from being added to the cache" }
                    } else {
                        ps.setString(1, a.typeName)
                        ps.setString(2, a.qualifiedName)
                        ps.setString(3, a.connectionQualifiedName)
                        ps.setString(4, a.name)
                        ps.setInt(5, if (a is Column) a.order ?: -1 else -1)
                        ps.setString(6, if (a.tenantId.isNullOrBlank()) "default" else a.tenantId)
                        ps.executeUpdate()
                    }
                }
            }
        }
    }

    /**
     * List the assets held in the persistent cache.
     *
     * @return a list of all assets held in the persistent cache
     */
    fun listAssets(): List<Asset> {
        val list = mutableListOf<Asset>()
        DriverManager.getConnection(dbString).use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery("SELECT * FROM entities").use { results ->
                    while (results.next()) {
                        val typeName = results.getString("type_name")
                        val qualifiedName = results.getString("qual_name")
                        val connectionQN = results.getString("con_qual_name")
                        val name = results.getString("name")
                        val order = results.getInt("order_seq")
                        val tenantId = results.getString("tenant_id")
                        list.add(
                            Column._internal()
                                .typeName(typeName)
                                .qualifiedName(qualifiedName)
                                .connectionQualifiedName(connectionQN)
                                .name(name)
                                .order(order)
                                .tenantId(tenantId)
                                .build(),
                        )
                    }
                }
            }
        }
        return list
    }

    companion object {
        /** Fields that need to be present on every asset to be added to connection cache. */
        val REQUIRED_FIELDS =
            listOf(
                Asset.TYPE_NAME,
                Asset.QUALIFIED_NAME,
                Asset.CONNECTION_QUALIFIED_NAME,
                Asset.NAME,
                Column.ORDER,
                Asset.TENANT_ID,
            )
    }
}
