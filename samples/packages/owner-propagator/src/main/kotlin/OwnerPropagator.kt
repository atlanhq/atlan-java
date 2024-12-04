/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.pkg.Utils
import com.atlan.util.AssetBatch
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicLong

object OwnerPropagator {
    private val logger = KotlinLogging.logger {}

    /**
     * Actually run the logic to propagate owners from schema down to children tables.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        Utils.initializeContext<OwnerPropagatorCfg>().use { ctx ->
            val batchSize = 20
            val tables = findTables(ctx.client, ctx.config.qnPrefix, batchSize)
            propagateOwner(ctx.client, tables, batchSize)
        }
    }

    /**
     * Find all the tables within a schema.
     *
     * @param client connectivity to the Atlan tenant
     * @param qnPrefix qualifiedName of the schema (or higher level, to apply across multiple schemas)
     * @param batchSize maximum number of results to retrieve per page (API request)
     * @return list of tables that were found
     */
    fun findTables(
        client: AtlanClient,
        qnPrefix: String,
        batchSize: Int,
    ): List<Asset> {
        val assets = mutableListOf<Asset>()
        Table.select(client)
            .where(Table.QUALIFIED_NAME.startsWith(qnPrefix))
            .includeOnResults(Table.SCHEMA)
            .includeOnResults(Table.OWNER_USERS)
            .includeOnRelations(Schema.OWNER_USERS)
            .pageSize(batchSize)
            .stream(true)
            .forEach {
                assets.add(it)
            }
        return assets
    }

    /**
     * Propagate the owner from each table's parent schema to the table.
     *
     * @param client connectivity to the Atlan tenant
     * @param tables list of tables to which to propagate owners
     * @param batchSize maximum number of tables to update per batch (API request)
     */
    fun propagateOwner(
        client: AtlanClient,
        tables: List<Asset>,
        batchSize: Int,
    ) {
        val totalCount = tables.size.toLong()
        AssetBatch(client, batchSize).use { batch ->
            val count = AtomicLong(0)
            tables.forEach {
                val table = it as Table
                val schemaOwners = table.schema.ownerUsers
                batch.add(
                    table.trimToRequired()
                        .ownerUsers(table.ownerUsers)
                        .ownerUsers(schemaOwners)
                        .build(),
                )
                Utils.logProgress(count, totalCount, logger, batchSize)
            }
            batch.flush()
            logger.info { "Total assets created: ${batch.created.size}" }
            logger.info { "Total assets updated: ${batch.updated.size}" }
            logger.info { "Total batches that failed: ${batch.failures.size}" }
        }
    }
}
