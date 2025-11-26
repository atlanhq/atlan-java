/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.enums.CustomMetadataHandling
import com.atlan.model.enums.LinkIdempotencyInvariant
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.AssetImporter.Companion.GLOSSARY_TYPES
import com.atlan.pkg.cache.AssetCache
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.csv.CSVXformer
import mu.KLogger
import java.util.stream.Stream

/**
 * Import glossaries, terms and categories (only) into Atlan from a provided CSV file.
 *
 * Only the terms and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context in which the package is running
 * @param filename name of the file to import
 * @param cache of existing glossaries, terms or categories (will be preloaded by import)
 * @param typeNameFilter name of the specific type that should be handled by this importer
 * @param logger through which to log any problems
 */
abstract class GTCImporter(
    ctx: PackageContext<AssetImportCfg>,
    filename: String,
    protected val cache: AssetCache<*>,
    typeNameFilter: String,
    logger: KLogger,
) : AbstractBaseImporter(
        ctx,
        filename,
        logger,
        typeNameFilter = typeNameFilter,
        attrsToOverwrite =
            attributesToClear(
                ctx.config
                    .getEffectiveValue(
                        AssetImportCfg::glossariesAttrToOverwrite,
                        AssetImportCfg::glossariesConfig,
                    ).toMutableList(),
                "glossaries",
                logger,
            ),
        updateOnly = ctx.config.glossariesUpsertSemantic == "update",
        customMetadataHandling =
            Utils.getCustomMetadataHandling(
                ctx.config.getEffectiveValue(
                    AssetImportCfg::glossariesCmHandling,
                    AssetImportCfg::glossariesConfig,
                ),
                CustomMetadataHandling.MERGE,
            ),
        atlanTagHandling =
            Utils.getAtlanTagHandling(
                ctx.config.getEffectiveValue(
                    AssetImportCfg::glossariesTagHandling,
                    AssetImportCfg::glossariesConfig,
                ),
                AtlanTagHandling.REPLACE,
            ),
        batchSize =
            ctx.config
                .getEffectiveValue(
                    AssetImportCfg::glossariesBatchSize,
                    AssetImportCfg::glossariesConfig,
                ).toInt(),
        fieldSeparator =
            ctx.config.getEffectiveValue(
                AssetImportCfg::glossariesFieldSeparator,
                AssetImportCfg::glossariesConfig,
            )[0],
        trackBatches = true,
        linkIdempotency =
            Utils.getLinkIdempotency(
                ctx.config.getEffectiveValue(
                    AssetImportCfg::glossariesLinkIdempotency,
                    AssetImportCfg::glossariesConfig,
                ),
                LinkIdempotencyInvariant.URL,
            ),
    ) {
    // Note: Always track batches (above) for GTC importers, to ensure cache is managed

    /** {@inheritDoc} */
    override fun cacheCreated(list: Stream<Asset>) {
        // Cache any assets that were created by processing
        list.forEach { asset ->
            // We must look up the asset and then cache to ensure we have the necessary identity
            // characteristics and status
            cache.cacheById(asset.guid)
        }
    }

    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val qualifiedName = generateQualifiedName(deserializer)
        return FieldSerde
            .getBuilderForType(typeNameFilter)
            .qualifiedName(qualifiedName)
    }

    /**
     * Determine the qualifiedName for the glossary, term or category, irrespective of whether it is
     * present in the input file or not. Since these qualifiedNames are generated, and the object may
     * have been created in a previous pass (and cached), we can resolve to its known qualifiedName
     * here based on the information in the row of the input file.
     *
     * @param deserializer a row of deserialized values
     * @return the qualifiedName, calculated from the deserialized values
     */
    private fun generateQualifiedName(deserializer: RowDeserializer): String {
        val cacheId = getCacheId(deserializer)
        return cache.getByIdentity(cacheId)?.qualifiedName ?: cacheId
    }

    /**
     * Calculate the cache identity for this row of the CSV, based purely on the information in the CSV.
     *
     * @param deserializer a row of deserialized values
     * @return the cache identity for the row
     */
    abstract fun getCacheId(deserializer: RowDeserializer): String

    /** {@inheritDoc} */
    override fun preprocess(
        outputFile: String?,
        outputHeaders: List<String>?,
    ): Results = Preprocessor(ctx, filename, fieldSeparator, logger).preprocess<Results>()

    open class Preprocessor(
        override val ctx: PackageContext<*>,
        originalFile: String,
        fieldSeparator: Char,
        logger: KLogger,
        override val requiredHeaders: Map<String, Set<String>>,
    ) : AbstractBaseImporter.Preprocessor(
            ctx = ctx,
            originalFile = originalFile,
            fieldSeparator = fieldSeparator,
            logger = logger,
            requiredHeaders = requiredHeaders,
        ) {
        private val nonGlossaryTypes = mutableSetOf<String>()

        /** {@inheritDoc} */
        override fun preprocessRow(
            row: List<String>,
            header: List<String>,
            typeIdx: Int,
            qnIdx: Int,
        ): List<String> {
            val updated = super.preprocessRow(row, header, typeIdx, qnIdx)
            // Keep a running collection of the types that are in the file
            val typeName = CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" })
            if (typeName.isNotBlank() && !invalidTypes.contains(typeName) && typeName !in GLOSSARY_TYPES) {
                nonGlossaryTypes.add(typeName)
            }
            return updated
        }

        /** {@inheritDoc} */
        override fun finalize(
            header: List<String>,
            outputFile: String?,
        ): Results {
            val results = super.finalize(header, outputFile)
            if (nonGlossaryTypes.isNotEmpty()) {
                throw IllegalStateException("Found non-glossary assets that should be loaded via another file, of types: $nonGlossaryTypes")
            }
            return results
        }
    }
}
