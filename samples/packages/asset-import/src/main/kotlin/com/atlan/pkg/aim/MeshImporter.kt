/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.enums.CustomMetadataHandling
import com.atlan.model.enums.LinkIdempotencyInvariant
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.AssetImporter.Companion.DATA_PRODUCT_TYPES
import com.atlan.pkg.cache.AssetCache
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.RowPreprocessor
import mu.KLogger

/**
 * Import data domains and data products (only) into Atlan from a provided CSV file.
 *
 * Only the domains/products and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context in which the package is running
 * @param filename name of the file to import
 * @param cache of existing domains and products (will be preloaded by import)
 * @param typeNameFilter name of the specific type that should be handled by this importer
 * @param logger through which to log any problems
 */
abstract class MeshImporter(
    ctx: PackageContext<AssetImportCfg>,
    filename: String,
    protected val cache: AssetCache<*>,
    typeNameFilter: String,
    logger: KLogger,
    trackBatches: Boolean = ctx.config.trackBatches,
) : AbstractBaseImporter(
        ctx = ctx,
        filename = filename,
        logger = logger,
        attrsToOverwrite =
            attributesToClear(
                ctx.config
                    .getEffectiveValue(
                        AssetImportCfg::dataProductsAttrToOverwrite,
                        AssetImportCfg::dataProductsConfig,
                    ).toMutableList(),
                "dataProducts",
                logger,
            ),
        updateOnly = ctx.config.dataProductsUpsertSemantic == "update",
        customMetadataHandling =
            Utils.getCustomMetadataHandling(
                ctx.config.getEffectiveValue(
                    AssetImportCfg::dataProductsCmHandling,
                    AssetImportCfg::dataProductsConfig,
                ),
                CustomMetadataHandling.MERGE,
            ),
        atlanTagHandling =
            Utils.getAtlanTagHandling(
                ctx.config.getEffectiveValue(
                    AssetImportCfg::dataProductsTagHandling,
                    AssetImportCfg::dataProductsConfig,
                ),
                AtlanTagHandling.REPLACE,
            ),
        batchSize =
            ctx.config
                .getEffectiveValue(
                    AssetImportCfg::dataProductsBatchSize,
                    AssetImportCfg::dataProductsConfig,
                ).toInt(),
        typeNameFilter = typeNameFilter,
        fieldSeparator =
            ctx.config.getEffectiveValue(
                AssetImportCfg::dataProductsFieldSeparator,
                AssetImportCfg::dataProductsConfig,
            )[0],
        trackBatches = trackBatches,
        linkIdempotency =
            Utils.getLinkIdempotency(
                ctx.config.getEffectiveValue(
                    AssetImportCfg::dataProductsLinkIdempotency,
                    AssetImportCfg::dataProductsConfig,
                ),
                LinkIdempotencyInvariant.URL,
            ),
    ) {
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
    ) : AbstractBaseImporter.Preprocessor(
            ctx = ctx,
            originalFile = originalFile,
            fieldSeparator = fieldSeparator,
            logger = logger,
        ) {
        private val nonProductTypes = mutableSetOf<String>()

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
            if (typeName.isNotBlank() && !invalidTypes.contains(typeName) && typeName !in DATA_PRODUCT_TYPES) {
                nonProductTypes.add(typeName)
            }
            return updated
        }

        /** {@inheritDoc} */
        override fun finalize(
            header: List<String>,
            outputFile: String?,
        ): RowPreprocessor.Results {
            val results = super.finalize(header, outputFile)
            if (nonProductTypes.isNotEmpty()) {
                throw IllegalStateException("Found non-product assets that should be loaded via another file, of types: $nonProductTypes")
            }
            return results
        }
    }
}
