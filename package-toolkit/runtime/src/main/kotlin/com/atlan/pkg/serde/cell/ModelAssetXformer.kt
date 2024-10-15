/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.assets.IModel
import com.atlan.model.assets.ModelAttribute
import com.atlan.model.assets.ModelAttributeAssociation
import com.atlan.model.assets.ModelDataModel
import com.atlan.model.assets.ModelEntity
import com.atlan.model.assets.ModelEntityAssociation
import com.atlan.model.assets.ModelVersion
import com.atlan.pkg.serde.cell.AssetRefXformer.TYPE_QN_DELIMITER

/**
 * Static object to transform term references.
 */
object ModelAssetXformer {
    val MODEL_ASSET_REF_FIELDS =
        setOf(
            ModelDataModel.MODEL_VERSIONS.atlanFieldName,
            ModelVersion.MODEL_DATA_MODEL.atlanFieldName,
            ModelVersion.MODEL_VERSION_ENTITIES.atlanFieldName,
            ModelEntity.MODEL_ENTITY_ATTRIBUTES.atlanFieldName,
            ModelEntity.MODEL_ENTITY_MAPPED_FROM_ENTITIES.atlanFieldName,
            ModelEntity.MODEL_ENTITY_MAPPED_TO_ENTITIES.atlanFieldName,
            ModelEntity.MODEL_ENTITY_RELATED_FROM_ENTITIES.atlanFieldName,
            ModelEntity.MODEL_ENTITY_RELATED_TO_ENTITIES.atlanFieldName,
            ModelEntity.MODEL_VERSIONS,
            ModelAttribute.MODEL_ATTRIBUTE_ENTITIES.atlanFieldName,
            ModelAttribute.MODEL_ATTRIBUTE_MAPPED_FROM_ATTRIBUTES.atlanFieldName,
            ModelAttribute.MODEL_ATTRIBUTE_MAPPED_TO_ATTRIBUTES.atlanFieldName,
            ModelAttribute.MODEL_ATTRIBUTE_RELATED_FROM_ATTRIBUTES.atlanFieldName,
            ModelAttribute.MODEL_ATTRIBUTE_RELATED_TO_ATTRIBUTES.atlanFieldName,
            ModelEntityAssociation.MODEL_ENTITY_ASSOCIATION_FROM.atlanFieldName,
            ModelEntityAssociation.MODEL_ENTITY_ASSOCIATION_TO.atlanFieldName,
            ModelAttributeAssociation.MODEL_ATTRIBUTE_ASSOCIATION_FROM.atlanFieldName,
            ModelAttributeAssociation.MODEL_ATTRIBUTE_ASSOCIATION_TO.atlanFieldName,
        )

    /**
     * Encodes (serializes) a term reference into a string form.
     *
     * @param asset to be encoded
     * @return the string-encoded form for that asset
     */
    fun encode(asset: Asset): String {
        // Handle some assets as direct embeds
        return when (asset) {
            is IModel -> {
                val ma = asset as IModel
                "${ma.typeName}$TYPE_QN_DELIMITER${ma.modelVersionAgnosticQualifiedName}"
            }
            else -> AssetRefXformer.encode(asset)
        }
    }

    /**
     * Decodes (deserializes) a string form into a term reference object.
     *
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the term reference represented by the string
     */
    fun decode(
        assetRef: String,
        fieldName: String,
    ): Asset {
        return when (fieldName) {
            in MODEL_ASSET_REF_FIELDS -> {
                val tokens = assetRef.split(TYPE_QN_DELIMITER)
                if (tokens.size > 1) {
                    ModelDataModel._internal() // start with a model so we have versionAgnosticQN
                        .typeName(tokens[0]) // override the type
                        .modelVersionAgnosticQualifiedName(assetRef.substringAfter(TYPE_QN_DELIMITER))
                        .build()
                } else {
                    throw NoSuchElementException("Model asset $assetRef not found (via $fieldName).")
                }
            }
            else -> AssetRefXformer.decode(assetRef, fieldName)
        }
    }
}
