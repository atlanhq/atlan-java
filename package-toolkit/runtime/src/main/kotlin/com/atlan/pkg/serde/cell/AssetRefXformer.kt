/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.IModel
import com.atlan.model.assets.Link
import com.atlan.model.assets.Readme
import com.atlan.model.enums.AtlanStatus
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.serde.Serde
import com.atlan.util.ParallelBatch
import mu.KLogger
import java.util.concurrent.atomic.AtomicLong

/**
 * Static object to transform asset references.
 */
object AssetRefXformer {
    const val TYPE_QN_DELIMITER = "@"

    /**
     * Encodes (serializes) an asset reference into a string form.
     *
     * @param ctx context of the running custom package
     * @param asset to be encoded
     * @return the string-encoded form for that asset
     */
    fun encode(
        ctx: PackageContext<*>,
        asset: Asset,
    ): String {
        // Handle some assets as direct embeds
        return when (asset) {
            is Readme -> asset.description ?: ""
            is Link -> {
                // Transform to a set of useful, non-overlapping info
                Link
                    ._internal()
                    .name(asset.name)
                    .link(asset.link)
                    .build()
                    .toJson(ctx.client)
            }
            is Glossary -> GlossaryXformer.encode(ctx, asset)
            is GlossaryCategory -> GlossaryCategoryXformer.encode(ctx, asset)
            is GlossaryTerm -> GlossaryTermXformer.encode(ctx, asset)
            is DataDomain -> DataDomainXformer.encode(ctx, asset)
            is IModel -> ModelAssetXformer.encode(ctx, asset)
            else -> {
                var qualifiedName = asset.qualifiedName
                if (asset.qualifiedName.isNullOrEmpty() && asset.uniqueAttributes != null) {
                    qualifiedName = asset.uniqueAttributes.qualifiedName
                }
                "${asset.typeName}$TYPE_QN_DELIMITER$qualifiedName"
            }
        }
    }

    /**
     * Decodes (deserializes) a string form into an asset reference object.
     *
     * @param ctx context of the running custom package
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the asset reference represented by the string
     */
    fun decode(
        ctx: PackageContext<*>,
        assetRef: String,
        fieldName: String,
    ): Asset {
        return when (fieldName) {
            Asset.README.atlanFieldName -> Readme._internal().description(assetRef).build()
            Asset.LINKS.atlanFieldName -> ctx.client.readValue(assetRef, Link::class.java)
            GlossaryCategory.PARENT_CATEGORY.atlanFieldName,
            GlossaryTerm.CATEGORIES.atlanFieldName,
            -> GlossaryCategoryXformer.decode(ctx, assetRef, fieldName)
            GlossaryCategory.ANCHOR.atlanFieldName -> GlossaryXformer.decode(ctx, assetRef, fieldName)
            "assignedTerms", in GlossaryTermXformer.TERM_TO_TERM_FIELDS -> GlossaryTermXformer.decode(ctx, assetRef, fieldName)
            DataDomain.PARENT_DOMAIN.atlanFieldName, DataProduct.DATA_DOMAIN.atlanFieldName -> DataDomainXformer.decode(ctx, assetRef, fieldName)
            in ModelAssetXformer.MODEL_ASSET_REF_FIELDS -> ModelAssetXformer.decode(ctx, assetRef, fieldName)
            else -> {
                val typeName = assetRef.substringBefore(TYPE_QN_DELIMITER)
                val qualifiedName = assetRef.substringAfter(TYPE_QN_DELIMITER)
                return getRefByQN(typeName, qualifiedName)
            }
        }
    }

    /**
     * Create a reference by qualifiedName for the given asset.
     *
     * @param typeName of the asset
     * @param qualifiedName of the asset
     * @return the asset reference represented by the parameters
     */
    fun getRefByQN(
        typeName: String,
        qualifiedName: String,
    ): Asset {
        val assetClass = Serde.getAssetClassForType(typeName)
        val method = assetClass.getMethod("refByQualifiedName", String::class.java)
        return method.invoke(null, qualifiedName) as Asset
    }

    /**
     * Batch up a complete related asset object from the provided asset and (partial) related asset details.
     *
     * @param ctx context of the running custom package
     * @param from the asset to which another asset is to be related (should have at least its GUID and name)
     * @param relatedAssets the (partial) asset(s) that should be related to the asset, which needs to be completed
     * @param batch the batch through which to create the asset(s) / relationships
     * @param count the running count of how many relationships have been created
     * @param totalRelated the static total number of relationships anticipated
     * @param logger through which to log progress
     * @param batchSize maximum number of relationships / assets to create per API call
     */
    fun buildRelated(
        ctx: PackageContext<*>,
        from: Asset,
        relatedAssets: Map<String, Collection<Asset>>,
        batch: ParallelBatch,
        count: AtomicLong,
        totalRelated: AtomicLong,
        logger: KLogger,
        batchSize: Int,
    ) {
        val totalCount = totalRelated.get()
        relatedAssets.forEach { (_, relatives) ->
            for (related in relatives) {
                when (related) {
                    is Readme -> {
                        batch.add(
                            Readme.creator(from, related.description ?: "").nullFields(related.nullFields).build(),
                        )
                        Utils.logProgress(count, totalCount, logger, batchSize)
                    }

                    is Link -> {
                        // Will be a performance hit, but probably not much other optimal way to do this
                        // other than preloading all links into a cache?
                        val existingLinks = ctx.linkCache.getByAssetGuid(from.guid)
                        var found = false
                        var update: Link? = null
                        for (link in existingLinks) {
                            if (link.link == related.link) {
                                logger.debug { "Found matching link for: ${link.link}" }
                                if (link.name == related.name) {
                                    // If the link is identical, skip it
                                    logger.debug { "Found matching name: ${link.name}" }
                                    found = true
                                    break
                                } else {
                                    // If the name has changed, update the name on the existing link
                                    logger.debug { "Name changed from : ${link.name} to ${related.name} with qualifiedName: ${link.qualifiedName}" }
                                    update =
                                        Link
                                            .updater(link.qualifiedName, related.name)
                                            .link(link.link)
                                            .nullFields(related.nullFields)
                                            .build()
                                    break
                                }
                            }
                        }
                        if (!found && update == null) {
                            logger.debug { "No match found for : ${related.link} creating new link" }
                            // Otherwise create an entirely new link (idempotently)
                            update =
                                Link
                                    .creator(from, related.name, related.link, true)
                                    .nullFields(related.nullFields)
                                    .status(AtlanStatus.ACTIVE)
                                    .build()
                        }
                        if (update != null) {
                            // Only batch it if it won't be a noop, and update the cache with this new link
                            batch.add(update)
                            ctx.linkCache.add(update)
                        }
                        Utils.logProgress(count, totalCount, logger, batchSize)
                    }
                }
            }
        }
    }

    /**
     * Indicates whether the provided object requires any special handling as a relationship.
     *
     * @param fieldName name of the field where the value is found
     * @param candidate the value to check
     * @return true if the value requires special relationship handling, otherwise false
     */
    fun requiresHandling(
        fieldName: String,
        candidate: Any,
    ): Boolean =
        when (fieldName) {
            Asset.LINKS.atlanFieldName -> true
            Asset.README.atlanFieldName -> true
            else -> false
        }
}
