/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package xformers.cell

import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.Link
import com.atlan.model.assets.Readme
import com.atlan.serde.Serde

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
            is Readme -> asset.description
            is Link -> {
                // Transform to a set of useful, non-overlapping info
                Link._internal()
                    .name(asset.name)
                    .link(asset.link)
                    .build()
                    .toJson(Atlan.getDefaultClient())
            }
            is GlossaryTerm -> AssignedTermXformer.encode(asset)
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
            "readme" -> Readme._internal().description(assetRef).build()
            "links" -> Atlan.getDefaultClient().readValue(assetRef, Link::class.java)
            "assignedTerms" -> AssignedTermXformer.decode(assetRef, fieldName)
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
     * Completes a related object that requires special handling.
     *
     * @param asset the complete asset to which the related asset should be related
     * @param related the (partial) related asset that needs to be completed
     * @return the complete related object
     */
    fun getRelated(
        asset: Asset,
        related: Asset,
    ): Asset {
        return when (related) {
            is Readme -> Readme.creator(asset, related.description).nullFields(related.nullFields).build()
            is Link -> Link.creator(asset, related.name, related.link).nullFields(related.nullFields).build()
            else -> {
                TODO("Special related assets handling only implemented for links and readmes")
            }
        }
    }

    /**
     * Indicates whether the provided object requires any special handling as a relationship.
     *
     * @param candidate the value to check
     * @return true if the value requires special relationship handling, otherwise false
     */
    fun requiresHandling(candidate: Any): Boolean {
        return candidate is Readme || candidate is Link
    }
}
