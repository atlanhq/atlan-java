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
import com.atlan.model.relations.Reference
import com.atlan.model.relations.UniqueAttributes
import com.atlan.pkg.serde.cell.AssetRefXformer.TYPE_QN_DELIMITER

/**
 * Static object to transform model asset references.
 */
object ModelAssetXformer {
    private val MODEL_ASSET_MULTI_VERSIONED_FIELDS =
        setOf(
            ModelDataModel.MODEL_VERSIONS.atlanFieldName,
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
        )
    val MODEL_ASSET_REF_FIELDS =
        MODEL_ASSET_MULTI_VERSIONED_FIELDS +
            setOf(
                ModelVersion.MODEL_DATA_MODEL.atlanFieldName,
                ModelEntityAssociation.MODEL_ENTITY_ASSOCIATION_FROM.atlanFieldName,
                ModelEntityAssociation.MODEL_ENTITY_ASSOCIATION_TO.atlanFieldName,
                ModelAttributeAssociation.MODEL_ATTRIBUTE_ASSOCIATION_FROM.atlanFieldName,
                ModelAttributeAssociation.MODEL_ATTRIBUTE_ASSOCIATION_TO.atlanFieldName,
            )

    /**
     * Encodes (serializes) a model asset reference into a string form.
     *
     * @param asset to be encoded
     * @return the string-encoded form for that asset
     */
    fun encode(asset: Asset): String {
        // Use the version-agnostic qualifiedName for data model assets
        return when (asset) {
            is IModel -> {
                val ma = asset as IModel
                "${ma.typeName}$TYPE_QN_DELIMITER${ma.modelVersionAgnosticQualifiedName}"
            }
            else -> AssetRefXformer.encode(asset)
        }
    }

    /**
     * Decodes (deserializes) a string form into a model asset reference object.
     *
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the model asset reference represented by the string
     */
    fun decode(
        assetRef: String,
        fieldName: String,
    ): Asset {
        val typeName = assetRef.substringBefore(TYPE_QN_DELIMITER)
        val qualifiedName = assetRef.substringAfter(TYPE_QN_DELIMITER)
        return when (fieldName) {
            in MODEL_ASSET_MULTI_VERSIONED_FIELDS -> {
                if (typeName.isNotBlank() && qualifiedName.isNotBlank()) {
                    ModelDataModel._internal() // start with a model so we have versionAgnosticQN
                        .typeName(typeName) // override the type
                        .uniqueAttributes(
                            UniqueAttributes.builder().qualifiedName(qualifiedName).build(),
                        )
                        .semantic(Reference.SaveSemantic.APPEND) // append, for multi-version holding
                        .modelVersionAgnosticQualifiedName(qualifiedName)
                        .build()
                } else {
                    throw NoSuchElementException("Model asset $assetRef not found (via $fieldName).")
                }
            }
            in MODEL_ASSET_REF_FIELDS -> {
                if (typeName.isNotBlank() && qualifiedName.isNotBlank()) {
                    ModelDataModel._internal() // start with a model so we have versionAgnosticQN
                        .typeName(typeName) // override the type
                        .uniqueAttributes(
                            UniqueAttributes.builder().qualifiedName(qualifiedName).build(),
                        )
                        .semantic(Reference.SaveSemantic.REPLACE)
                        .modelVersionAgnosticQualifiedName(qualifiedName)
                        .build()
                } else {
                    throw NoSuchElementException("Model asset $assetRef not found (via $fieldName).")
                }
            }
            else -> AssetRefXformer.decode(assetRef, fieldName)
        }
    }
}
