/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.Link
import com.atlan.model.assets.Readme
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.LinkCache
import com.atlan.serde.Serde
import com.atlan.util.ParallelBatch
import mu.KLogger
import java.util.concurrent.atomic.AtomicLong

/**
 * Static object to transform asset references.
 */
object AssetRefXformer {
    private const val TYPE_QN_DELIMITER = "@"

    /**
     * Encodes (serializes) an asset reference into a string form.
     *
     * @param asset to be encoded
     * @return the string-encoded form for that asset
     */
    fun encode(asset: Asset): String {
        // Handle some assets as direct embeds
        return when (asset) {
            is Readme -> asset.description ?: ""
            is Link -> {
                // Transform to a set of useful, non-overlapping info
                Link._internal()
                    .name(asset.name)
                    .link(asset.link)
                    .build()
                    .toJson(Atlan.getDefaultClient())
            }
            is Glossary -> GlossaryXformer.encode(asset)
            is GlossaryCategory -> GlossaryCategoryXformer.encode(asset)
            is GlossaryTerm -> GlossaryTermXformer.encode(asset)
            is DataDomain -> DataDomainXformer.encode(asset)
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
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the asset reference represented by the string
     */
    fun decode(
        assetRef: String,
        fieldName: String,
    ): Asset {
        return when (fieldName) {
            Asset.README.atlanFieldName -> Readme._internal().description(assetRef).build()
            Asset.LINKS.atlanFieldName -> Atlan.getDefaultClient().readValue(assetRef, Link::class.java)
            GlossaryCategory.PARENT_CATEGORY.atlanFieldName,
            GlossaryTerm.CATEGORIES.atlanFieldName,
            -> GlossaryCategoryXformer.decode(assetRef, fieldName)
            GlossaryCategory.ANCHOR.atlanFieldName -> GlossaryXformer.decode(assetRef, fieldName)
            "assignedTerms", in GlossaryTermXformer.TERM_TO_TERM_FIELDS -> GlossaryTermXformer.decode(assetRef, fieldName)
            DataDomain.PARENT_DOMAIN.atlanFieldName -> DataDomainXformer.decode(assetRef, fieldName)
            else -> {
                val tokens = assetRef.split(TYPE_QN_DELIMITER)
                val typeName = tokens[0]
                val assetClass = Serde.getAssetClassForType(typeName)
                val method = assetClass.getMethod("refByQualifiedName", String::class.java)
                val qualifiedName = tokens.subList(1, tokens.size).joinToString(TYPE_QN_DELIMITER)
                method.invoke(null, qualifiedName) as Asset
            }
        }
    }

    /**
     * Batch up a complete related asset object from the provided asset and (partial) related asset details.
     *
     * @param from the asset to which another asset is to be related (should have at least its GUID and name)
     * @param relatedAssets the (partial) asset(s) that should be related to the asset, which needs to be completed
     * @param batch the batch through which to create the asset(s) / relationships
     * @param count the running count of how many relationships have been created
     * @param totalRelated the static total number of relationships anticipated
     * @param logger through which to log progress
     * @param batchSize maximum number of relationships / assets to create per API call
     */
    fun buildRelated(
        from: Asset,
        relatedAssets: Map<String, Collection<Asset>>,
        batch: ParallelBatch,
        count: AtomicLong,
        totalRelated: AtomicLong,
        logger: KLogger,
        batchSize: Int,
    ) {
        val totalCount = totalRelated.get()
        relatedAssets.forEach { (fieldName, relatives) ->
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
                        val existingLinks = LinkCache.getByAssetGuid(from.guid)
                        var found = false
                        var update: Link? = null
                        for (link in existingLinks) {
                            if (link.link == related.link) {
                                if (link.name == related.name) {
                                    // If the link is identical, skip it
                                    found = true
                                } else {
                                    // If the name has changed, update the name on the existing link
                                    update = Link.updater(link.qualifiedName, related.name).nullFields(related.nullFields).build()
                                }
                            }
                        }
                        if (!found && update == null) {
                            // Otherwise create an entirely new link
                            update = Link.creator(from, related.name, related.link).nullFields(related.nullFields).build()
                        }
                        if (update != null) {
                            // Only batch it if it won't be a noop
                            batch.add(update)
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
    fun requiresHandling(fieldName: String, candidate: Any): Boolean {
        return when (fieldName) {
            Asset.LINKS.atlanFieldName -> true
            Asset.README.atlanFieldName -> true
            else -> false
        }
    }
}
