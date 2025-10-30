/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.enums.CustomMetadataHandling
import com.atlan.model.enums.LinkIdempotencyInvariant
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageContext
import com.atlan.pkg.cache.AssetCache
import com.atlan.pkg.cache.TypeDefCache
import com.atlan.pkg.serde.csv.CSVImporter
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import mu.KLogger
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * Base set of reusable mechanisms across imports of all types.
 *
 * @param ctx context in which the package is running
 * @param filename name of the file to import
 * @param logger through which to write log entries
 */
abstract class AbstractBaseImporter(
    ctx: PackageContext<AssetImportCfg>,
    filename: String,
    logger: KLogger,
    attrsToOverwrite: List<AtlanField>,
    updateOnly: Boolean = false,
    batchSize: Int = 20,
    caseSensitive: Boolean = true,
    creationHandling: AssetCreationHandling = AssetCreationHandling.FULL,
    customMetadataHandling: CustomMetadataHandling = CustomMetadataHandling.MERGE,
    atlanTagHandling: AtlanTagHandling = AtlanTagHandling.REPLACE,
    tableViewAgnostic: Boolean = false,
    trackBatches: Boolean = true,
    fieldSeparator: Char = ',',
    linkIdempotency: LinkIdempotencyInvariant = LinkIdempotencyInvariant.URL,
    typeNameFilter: String = "",
) : CSVImporter(
        ctx,
        filename,
        logger = logger,
        attrsToOverwrite = attrsToOverwrite,
        updateOnly = updateOnly,
        batchSize = batchSize,
        caseSensitive = caseSensitive,
        creationHandling = creationHandling,
        customMetadataHandling = customMetadataHandling,
        atlanTagHandling = atlanTagHandling,
        tableViewAgnostic = tableViewAgnostic,
        trackBatches = trackBatches,
        fieldSeparator = fieldSeparator,
        linkIdempotency = linkIdempotency,
        typeNameFilter = typeNameFilter,
    ) {
    protected var header = emptyList<String>()
    protected val mapToSecondPass = mutableMapOf<String, MutableSet<String>>()
    protected var levelToProcess = 0
    private val alreadyHandled =
        setOf(
            "asset_readme",
            "asset_links",
        )
    private val hasCachedPrereqs = AtomicBoolean(false)

    /** {@inheritDoc} */
    override fun preprocessRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): List<String> {
        // Check if the type on this row has any cyclical relationships as headers in the input file
        val typeName = CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" })
        // Short-circuit if we're restricting to certain rows and this is not one of them
        if (typeNameFilter.isNotEmpty() && typeNameFilter != typeName) return row
        if (this.header.isEmpty()) this.header = header
        checkCyclicalRelationships(typeName, ctx.typeDefCache.getCyclicalRelationshipsForType(typeName))
        return row
    }

    /**
     * Import the assets, handling multiple passes if any cyclical relationships are present
     * (or doing only a single pass if no cyclical relationships are present).
     *
     * @param typeToProcess name of the asset type to process
     * @param columnsToSkip names of any columns to skip processing
     * @param columnsToAlwaysInclude names of any columns that should always be included (in all passes)
     */
    fun import(
        typeToProcess: String,
        columnsToSkip: Set<String>,
        columnsToAlwaysInclude: Set<String>,
    ): ImportResults? {
        cacheAnyPrereqs()
        val cyclicalForType = mapToSecondPass.getOrElse(typeToProcess) { emptySet() }
        if (cyclicalForType.isEmpty()) {
            // If there are no cyclical relationships for this type, do everything in one pass
            logger.info { "--- Importing $typeToProcess assets... ---" }
            val results = super.import(columnsToSkip)
            if (results != null) ctx.processedResults.add(results)
        } else {
            // Otherwise, import assets without any cyclical relationships, first
            logger.info { "--- Importing $typeToProcess assets in a first pass, without any cyclical relationships... ---" }
            val firstPassSkip = columnsToSkip.toMutableSet()
            firstPassSkip.addAll(cyclicalForType)
            val firstPassResults = super.import(firstPassSkip)
            if (firstPassResults != null) {
                ctx.processedResults.add(firstPassResults)
                runCyclicalUpdatePass(typeToProcess, columnsToSkip, firstPassSkip, columnsToAlwaysInclude, ctx.processedResults)
            }
        }
        return ImportResults.combineAll(ctx.client, true, *ctx.processedResults.toTypedArray())
    }

    /**
     * Import the assets, handling multiple passes first of all to handle the hierarchical nature
     * of these assets, and then secondly to handle any cyclical relationships that are present
     * (if necessary).
     *
     * @param cache the cache to keep updated as the levels of the hierarchy are processed
     * @param typeToProcess name of the asset type to process
     * @param columnsToSkip names of any columns to skip processing
     * @param columnsToAlwaysInclude names of any columns that should always be included, in all passes
     * @param maxDepth maximum depth detected for the hierarchy
     */
    fun importHierarchy(
        cache: AssetCache<*>,
        typeToProcess: String,
        columnsToSkip: Set<String>,
        columnsToAlwaysInclude: Set<String>,
        maxDepth: AtomicInteger,
    ): ImportResults? {
        cacheAnyPrereqs(cache)
        val cyclicalForType = mapToSecondPass.getOrElse(typeToProcess) { emptySet() }
        val firstPassSkip = columnsToSkip.toMutableSet()
        firstPassSkip.addAll(cyclicalForType)
        firstPassSkip.removeAll(columnsToAlwaysInclude) // these must remain for all passes
        // Import categories by level, top-to-bottom, and stop when we hit a level with no categories
        logger.info { "Loading $typeToProcess in multiple passes, by level..." }
        while (levelToProcess < maxDepth.get()) {
            levelToProcess += 1
            logger.info { "--- Loading level $levelToProcess $typeToProcess... ---" }
            val results = super.import(firstPassSkip)
            if (results != null) ctx.processedResults.add(results)
        }
        // Now do the second pass with cyclical relationships, which we can do in any order
        // as now all the various levels of the hierarchy should already exist from the first pass...
        if (cyclicalForType.isNotEmpty() && ctx.processedResults.isNotEmpty()) {
            runCyclicalUpdatePass(typeToProcess, columnsToSkip, firstPassSkip, columnsToAlwaysInclude, ctx.processedResults)
        }
        return ImportResults.combineAll(ctx.client, true, *ctx.processedResults.toTypedArray())
    }

    private fun cacheAnyPrereqs(cache: AssetCache<*>? = null) {
        if (!hasCachedPrereqs.get()) {
            cache?.preload()
            val results = preprocess()
            if (results.hasLinks) {
                ctx.linkCache.preload()
            }
            if (results.hasTermAssignments) {
                ctx.termCache.preload()
            }
            if (results.hasDomainRelationship) {
                ctx.dataDomainCache.preload()
            }
            if (results.hasProductRelationship) {
                ctx.dataProductCache.preload()
            }
            hasCachedPrereqs.set(true)
        }
    }

    private fun runCyclicalUpdatePass(
        typeToProcess: String,
        columnsToSkip: Set<String>,
        firstPassSkip: Set<String>,
        columnsToAlwaysInclude: Set<String>,
        passResults: MutableList<ImportResults>,
    ) {
        val secondPassSkip = header.toMutableSet() // Start from the premise we will ignore everything
        secondPassSkip.removeAll(firstPassSkip) // Add back in anything we skipped in the first pass
        secondPassSkip.addAll(columnsToSkip) // Continue to skip anything we should always skip
        secondPassSkip.removeAll(columnsToAlwaysInclude) // Finally, continue to keep anything we should always keep
        // In this second pass we need to ignore fields that were loaded in the first pass,
        // or we will end up with duplicates (links) or extra audit log messages (tags, README)
        logger.info { "--- Loading cyclical relationships for $typeToProcess (second pass)... ---" }
        // Note: for the second pass, ignore any tags or custom metadata (should have been set in the first
        // pass, if there were any, so no need to set them again (and add noise to activity log))
        val secondPassResults =
            super.import(
                secondPassSkip,
                AtlanTagHandling.IGNORE,
                CustomMetadataHandling.IGNORE,
            )
        if (secondPassResults != null) passResults.add(secondPassResults)
    }

    private fun checkCyclicalRelationships(
        typeName: String,
        relationships: Set<TypeDefCache.RelationshipEnds>,
    ) {
        relationships
            .filter { !alreadyHandled.contains(it.name) }
            .forEach { relationship ->
                val one = relationship.end1
                val two = relationship.end2
                if (one != two && header.contains(one) && header.contains(two)) {
                    // If both ends of the same relationship are in the input file, throw an error
                    // alerting the user that this can't work, and they'll need to pick one end or the other
                    throw IllegalStateException(
                        """
                        Both ends of the same relationship found in the input file for type $typeName: $one <> $two.
                        You should only use one end of this relationship or the other when importing.
                        """.trimIndent(),
                    )
                }
                // Retain any of the cyclical relationships that remain so that we can second-pass process them
                if (header.contains(one)) {
                    mapToSecondPass.getOrPut(typeName) { mutableSetOf() }.add(one)
                } else if (header.contains(two)) {
                    mapToSecondPass.getOrPut(typeName) { mutableSetOf() }.add(two)
                }
            }
    }
}
