/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import mu.KotlinLogging
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Cache that contains a listing of all asset identities for a given connection, and stores
 * these persistently in object storage for use by other workflows (i.e. in considering lineage).
 *
 * @param dbFile the file path to the SQLite database file
 * @param metrics ???
 * @param ignoreQNs a collection of qualifiedNames to ignore
 */
class PersistentConnectionCache(
    private val dbFile: String,
    val metrics: Any,
    private val ignoreQNs: Collection<String> = emptySet(),
) {
    private val logger = KotlinLogging.logger {}
    private val dbString = "jdbc:sqlite:$dbFile"

    init {
        val tableQuery = """
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
            connection.commit()
        }
    }

    /**
     * Remove the provided assets from the persistent connection cache.
     *
     * @param assets to remove from the cache
     */
    fun deleteAssets(assets: Collection<Asset>) {
        DriverManager.getConnection(dbString).use { connection ->
            connection.prepareStatement("delete from entities where type_name = ? and qual_name = ?").use { ps ->
                assets.forEach { a ->
                    ps.setString(1, a.typeName)
                    ps.setString(2, a.qualifiedName)
                    ps.executeUpdate()
                }
            }
            connection.commit()
        }
    }

    /**
     * Add the provided assets to the persistent connection cache.
     *
     * @param assets to add to the cache
     */
    fun addAssets(assets: Collection<Asset>) {
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
                        ps.setInt(5, if (a is Column) a.order else -1)
                        ps.setString(6, a.tenantId)
                        ps.executeUpdate()
                    }
                }
            }
            connection.commit()
        }
    }
}
