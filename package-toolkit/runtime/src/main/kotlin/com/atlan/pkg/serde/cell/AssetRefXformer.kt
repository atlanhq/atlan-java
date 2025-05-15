/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.exception.AtlanException
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
import com.atlan.model.enums.LinkIdempotencyInvariant
import com.atlan.model.relations.Reference.SaveSemantic
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.util.AssetResolver
import com.atlan.serde.Serde
import com.atlan.util.ParallelBatch
import mu.KLogger
import java.util.concurrent.atomic.AtomicLong
import java.util.regex.Pattern

/**
 * Static object to transform asset references.
 */
object AssetRefXformer {
    const val TYPE_QN_DELIMITER = "@"
    const val APPEND_PREFIX = "++"
    const val REMOVE_PREFIX = "--"
    const val DEFERRED_QN_PATTERN = "\\{\\{([a-zA-Z0-9-]+)/(.+?)\\}\\}(/.*)?"
    val deferredQN = Pattern.compile(DEFERRED_QN_PATTERN)!!

    /**
     * Translate the provided asset reference into a reference and save semantic pair.
     *
     * @param assetRef the asset reference to translate
     * @return a pair of the asset reference and semantic (separated out)
     */
    fun getSemantic(assetRef: String): Pair<String, SaveSemantic> {
        val (ref, semantic) =
            when {
                assetRef.startsWith(APPEND_PREFIX) -> Pair(assetRef.substringAfter(APPEND_PREFIX), SaveSemantic.APPEND)
                assetRef.startsWith(REMOVE_PREFIX) -> Pair(assetRef.substringAfter(REMOVE_PREFIX), SaveSemantic.REMOVE)
                else -> Pair(assetRef, SaveSemantic.REPLACE)
            }
        return Pair(ref, semantic)
    }

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
            Asset.LINKS.atlanFieldName -> {
                val (linkJson, semantic) = getSemantic(assetRef)
                val link = ctx.client.readValue(linkJson, Link::class.java)
                link.toBuilder().semantic(semantic).build()
            }
            GlossaryCategory.PARENT_CATEGORY.atlanFieldName,
            GlossaryTerm.CATEGORIES.atlanFieldName,
            -> GlossaryCategoryXformer.decode(ctx, assetRef, fieldName)
            GlossaryCategory.ANCHOR.atlanFieldName -> GlossaryXformer.decode(ctx, assetRef, fieldName)
            "assignedTerms", in GlossaryTermXformer.TERM_TO_TERM_FIELDS -> GlossaryTermXformer.decode(ctx, assetRef, fieldName)
            DataDomain.PARENT_DOMAIN.atlanFieldName, DataProduct.DATA_DOMAIN.atlanFieldName -> DataDomainXformer.decode(ctx, assetRef, fieldName)
            in ModelAssetXformer.MODEL_ASSET_REF_FIELDS -> ModelAssetXformer.decode(ctx, assetRef, fieldName)
            else -> {
                val (ref, semantic) = getSemantic(assetRef)
                val typeName = ref.substringBefore(TYPE_QN_DELIMITER)
                val qualifiedName = ref.substringAfter(TYPE_QN_DELIMITER)
                return getRefByQN(ctx, typeName, qualifiedName, semantic)
            }
        }
    }

    /**
     * Create a reference by qualifiedName for the given asset.
     *
     * @param ctx context of the running custom package
     * @param typeName of the asset
     * @param qualifiedName of the asset
     * @param semantic to use when persisting the relationship (append, remove, or replace)
     * @return the asset reference represented by the parameters
     */
    fun getRefByQN(
        ctx: PackageContext<*>,
        typeName: String,
        qualifiedName: String,
        semantic: SaveSemantic = SaveSemantic.REPLACE,
    ): Asset {
        val assetClass = Serde.getAssetClassForType(typeName)
        val method = assetClass.getMethod("refByQualifiedName", String::class.java, SaveSemantic::class.java)
        val resolvedQN = resolveDeferredQN(ctx, qualifiedName)
        return method.invoke(null, resolvedQN, semantic) as Asset
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
     * @param linkIdempotency how to determine whether a Link should be updated vs created (to avoid duplicates)
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
        linkIdempotency: LinkIdempotencyInvariant,
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
                            when (linkIdempotency) {
                                LinkIdempotencyInvariant.URL -> {
                                    if (link.link == related.link) {
                                        logger.debug { "Found matching link for: ${link.link}" }
                                        if (link.name == related.name) {
                                            // If the link is identical, skip it
                                            logger.debug { "Found matching name: ${link.name}" }
                                            found = true
                                            break
                                        } else {
                                            // If the name has changed, update the name AND qualifiedName on the existing link
                                            logger.debug { "Name changed from : ${link.name} to ${related.name} with qualifiedName: ${link.qualifiedName}" }
                                            update =
                                                getLinkWithPotentialNewQN(
                                                    ctx,
                                                    from,
                                                    link,
                                                    related,
                                                    logger,
                                                )
                                            break
                                        }
                                    }
                                }
                                LinkIdempotencyInvariant.NAME -> {
                                    if (link.name == related.name) {
                                        logger.debug { "Found matching name for: ${link.name}" }
                                        if (link.link == related.link) {
                                            // If the link is identical, skip it
                                            logger.debug { "Found matching link: ${link.link}" }
                                            found = true
                                            break
                                        } else {
                                            // If the URL has changed, update the URL on the existing link (and ensure qualifiedName is idempotent)
                                            logger.debug { "URL changed from: ${link.link} to ${related.link} with qualifiedName: ${link.qualifiedName}" }
                                            update =
                                                getLinkWithPotentialNewQN(
                                                    ctx,
                                                    from,
                                                    link,
                                                    related,
                                                    logger,
                                                )
                                            break
                                        }
                                    }
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

    private fun getLinkWithPotentialNewQN(
        ctx: PackageContext<*>,
        from: Asset,
        original: Link,
        updated: Link,
        logger: KLogger,
    ): Link {
        val guidToUpdate =
            if (original.guid.startsWith("-")) {
                // If we have a placeholder GUID, we need to actually look up the link to resolve a real GUID
                try {
                    val resolved = Link.get(ctx.client, original.qualifiedName)
                    // If we get a real GUID, then cache it again
                    ctx.linkCache.replace(original.guid, resolved)
                    resolved.guid
                } catch (e: Exception) {
                    logger.warn { "Unable to resolve link to a real GUID, falling back to placeholder (may cause duplicates)." }
                    original.guid
                }
            } else {
                original.guid
            }
        return Link
            .creator(from, updated.name, updated.link, true)
            .guid(guidToUpdate) // Note: this should allow the qualifiedName to be updated
            .nullFields(updated.nullFields)
            .status(AtlanStatus.ACTIVE)
            .build()
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

    /**
     * Parse the connector type from the deferred connection definition.
     *
     * @param qualifiedName from which to parse
     * @return the details of the deferred connector type
     */
    fun getDeferredIdentity(qualifiedName: String): AssetResolver.ConnectionIdentity? {
        val matcher = deferredQN.matcher(qualifiedName)
        return if (matcher.matches()) {
            AssetResolver.ConnectionIdentity(matcher.group(2), matcher.group(1))
        } else {
            null
        }
    }

    /**
     * Attempt to resolve a qualifiedName that may have deferred connection details.
     *
     * @param ctx context of the running package
     * @param qualifiedName to resolve
     * @return the qualifiedName, resolved
     */
    fun resolveDeferredQN(
        ctx: PackageContext<*>,
        qualifiedName: String,
    ): String {
        val matcher = deferredQN.matcher(qualifiedName)
        return if (matcher.matches()) {
            val connectorType = matcher.group(1)
            val connectionName = matcher.group(2)
            val remainder = matcher.group(3) ?: ""
            val connectionId = ctx.connectionCache.getIdentityForAsset(connectionName, connectorType)
            try {
                val resolvedConnectionQN =
                    ctx.connectionCache.getByIdentity(connectionId)?.qualifiedName
                        ?: throw IllegalStateException("Unable to resolve deferred connection -- no connection found by name and type: $qualifiedName")
                "$resolvedConnectionQN$remainder"
            } catch (e: AtlanException) {
                throw IllegalStateException("Unable to resolve deferred connection -- no connection found by name and type: $qualifiedName", e)
            }
        } else {
            qualifiedName
        }
    }
}
