/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.pkg.Utils
import com.atlan.util.AssetBatch
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicLong

private val logger = KotlinLogging.logger {}

/**
 * Actually run the logic to propagate owners from schema down to children tables.
 */
fun main() {
    val config = Utils.setPackageOps<OwnerPropagatorCfg>()

    val qnPrefix = Utils.getOrDefault(config.qnPrefix, "default")
    val batchSize = Utils.getOrDefault(config.batchSize, 50)

    val tables = findTables(qnPrefix, batchSize)
    propagateOwner(tables, batchSize)
}

/**
 * Find all the tables within a schema.
 *
 * @param qnPrefix qualifiedName of the schema (or higher level, to apply across multiple schemas)
 * @param batchSize maximum number of results to retrieve per page (API request)
 * @return list of tables that were found
 */
fun findTables(qnPrefix: String, batchSize: Int): List<Asset> {
    val assets = mutableListOf<Asset>()
    Table.select()
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
 * @param tables list of tables to which to propagate owners
 * @param batchSize maximum number of tables to update per batch (API request)
 */
fun propagateOwner(tables: List<Asset>, batchSize: Int) {
    val totalCount = tables.size.toLong()
    val batch = AssetBatch(Atlan.getDefaultClient(), batchSize)
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
    logger.info("Total assets created: {}", batch.created.size)
    logger.info("Total assets updated: {}", batch.updated.size)
    logger.info("Total batches that failed : {}", batch.failures.size)
}
