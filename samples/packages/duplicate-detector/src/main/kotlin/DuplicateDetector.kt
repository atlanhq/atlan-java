/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.AtlanClient
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.IColumn
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.enums.CertificateStatus
import com.atlan.model.enums.CustomMetadataHandling
import com.atlan.pkg.Utils
import com.atlan.util.ParallelBatch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.sequences.chunked
import kotlin.sequences.toList
import kotlin.streams.asSequence

object DuplicateDetector {
    private val logger = Utils.getLogger(this.javaClass.name)

    data class AssetKey(
        val typeName: String,
        val qualifiedName: String,
        val guid: String,
    )

    private val hashToAssetKeys = ConcurrentHashMap<Int, MutableSet<AssetKey>>()
    private val hashToColumns = ConcurrentHashMap<Int, Set<String>>()
    private val uniqueContainers = ConcurrentHashMap<AssetKey, AssetKey>()

    @JvmStatic
    fun main(args: Array<String>) {
        Utils.initializeContext<DuplicateDetectorCfg>().use { ctx ->
            val qnPrefix = ctx.config.qnPrefix
            val types = ctx.config.assetTypes

            logger.info {
                "Detecting duplicates across $types (for prefix $qnPrefix) on: ${ctx.client.baseUrl}"
            }
            findAssets(ctx.client, qnPrefix, types)

            val glossaryQN = glossaryForDuplicates(ctx.client, ctx.config.glossaryName)
            termsForDuplicates(ctx.client, glossaryQN)
        }
    }

    /**
     * Find the assets to compare with each other for deduplication.
     *
     * @param client connectivity to the Atlan tenant
     * @param qnPrefix a partial qualifiedName that every matching asset must start with
     * @param types collection of asset types to include
     * @param batchSize: maximum number of assets to look for per API request (page of results)
     */
    fun findAssets(
        client: AtlanClient,
        qnPrefix: String,
        types: Collection<String>,
        batchSize: Int = 300,
    ) {
        val request =
            client.assets
                .select()
                .where(Asset.TYPE_NAME.`in`(types))
                .where(Asset.QUALIFIED_NAME.startsWith(qnPrefix))
                .pageSize(batchSize)
                .includeOnResults(Table.COLUMNS)
                .includeOnRelations(Column.NAME)
        val totalAssetCount = request.count()
        val count = AtomicLong(0)
        logger.info { "Comparing a total of $totalAssetCount assets..." }
        request
            .stream(true)
            .forEach { asset ->
                val columns =
                    when (asset) {
                        is Table -> asset.columns
                        is View -> asset.columns
                        is MaterializedView -> asset.columns
                        else -> setOf()
                    }
                val columnNames =
                    columns
                        .stream()
                        .map(IColumn::getName)
                        .map { normalize(it) }
                        .toList()
                        .toSet()
                val containerKey = AssetKey(asset.typeName, asset.qualifiedName, asset.guid)
                if (columnNames.isNotEmpty() && uniqueContainers.put(containerKey, containerKey) == null) {
                    // Skip assets with no columns whatsoever
                    val hash = columnNames.hashCode()
                    if (!hashToAssetKeys.containsKey(hash)) {
                        hashToColumns[hash] = columnNames
                        hashToAssetKeys[hash] = mutableSetOf()
                    }
                    hashToAssetKeys[hash]?.add(containerKey)
                }
                Utils.logProgress(count, totalAssetCount, logger, batchSize)
            }
    }

    /**
     * Idempotently create (or fetch) a glossary to capture the duplicate assets.
     *
     * @param client connectivity to the Atlan tenant
     * @param glossaryName name of the glossary
     * @return the qualifiedName of the glossary
     */
    fun glossaryForDuplicates(
        client: AtlanClient,
        glossaryName: String,
    ): String =
        try {
            Glossary.findByName(client, glossaryName).qualifiedName
        } catch (e: NotFoundException) {
            val glossary =
                Glossary
                    .creator(glossaryName)
                    .assetIcon(AtlanIcon.COPY)
                    .userDescription("Each term represents a set of potential duplicate assets, based on assets that have the same set of columns (case-insensitive, in any order). The assets that are potential duplicates of each other are all linked to the same term.")
                    .build()
            logger.info { "Creating glossary to hold duplicates." }
            glossary.save(client).getResult(glossary).qualifiedName
        }

    /**
     * Idempotently create (or update) a term for each set of 2 or more potential duplicate assets,
     * and link those potential duplicate assets to the term.
     *
     * @param client connectivity to the Atlan tenant
     * @param glossaryQN qualifiedName of the glossary in which to manage the terms
     * @param batchSize maximum number of assets to update at a time
     */
    fun termsForDuplicates(
        client: AtlanClient,
        glossaryQN: String,
        batchSize: Int = 20,
    ) {
        val termCount = AtomicLong(0)
        val assetCount = AtomicLong(0)
        val totalSets =
            hashToAssetKeys.keys
                .stream()
                .filter { hashToAssetKeys[it]?.size!! > 1 }
                .count()
        logger.info { "Processing $totalSets total sets of duplicates..." }
        hashToAssetKeys.keys.forEach { hash ->
            val keys = hashToAssetKeys[hash]
            if (keys?.size!! > 1) {
                val columns = hashToColumns[hash]
                ParallelBatch(
                    client,
                    batchSize,
                    AtlanTagHandling.IGNORE,
                    CustomMetadataHandling.MERGE,
                    true,
                ).use { batch ->
                    val termName = "Dup. ($hash)"
                    val term =
                        try {
                            GlossaryTerm.findByNameFast(client, termName, glossaryQN)
                        } catch (e: NotFoundException) {
                            val toCreate =
                                GlossaryTerm
                                    .creator(termName, glossaryQN)
                                    .description(
                                        "Assets with the same set of ${columns?.size} columns:\n" +
                                            columns?.joinToString(
                                                separator = "\n",
                                            ) { "- $it" },
                                    ).certificateStatus(CertificateStatus.DRAFT)
                                    .build()
                            toCreate.save(client).getResult(toCreate)
                        }
                    val termRef = term.trimToReference()
                    keys
                        .stream()
                        .map(AssetKey::guid)
                        .asSequence()
                        .chunked(300)
                        .toList()
                        .forEach { chunk ->
                            client.assets
                                .select()
                                .where(Asset.GUID.`in`(chunk))
                                .includeOnRelations(Asset.QUALIFIED_NAME)
                                .pageSize(300)
                                .stream(true)
                                .forEach { asset ->
                                    assetCount.getAndIncrement()
                                    batch.add(
                                        asset
                                            .trimToRequired()
                                            .appendAssignedTerm(termRef)
                                            .build(),
                                    )
                                }
                        }
                    batch.flush()
                    Utils.logProgress(termCount, totalSets, logger)
                }
            }
        }
        logger.info { "Detected a total of $assetCount assets that could be de-duplicated across $totalSets unique sets of duplicates." }
    }

    /**
     * Normalize the provided column name for comparison purposes.
     * At the moment this:
     * 1. Removes any underscores (_)
     * 2. Makes the entire column name case-insensitive
     *
     * @param colName original name of the column
     * @return the normalized name of the column
     */
    private fun normalize(colName: String): String = colName.replace("_", "").lowercase()
}
