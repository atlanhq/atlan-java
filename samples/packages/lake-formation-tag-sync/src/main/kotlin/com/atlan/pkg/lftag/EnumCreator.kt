/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.atlan.Atlan
import com.atlan.model.typedefs.EnumDef
import mu.KotlinLogging

class EnumCreator(val tagToMetadataMapper: TagToMetadataMapper) {
    private val logger = KotlinLogging.logger {}
    private val defaultClient = Atlan.getDefaultClient()
    private val customMetadataCache = defaultClient.customMetadataCache
    private val enumCache = defaultClient.enumCache

    fun createOptions(
        tagKey: String,
        values: Set<String>,
    ) {
        val (setName, attributeName) = tagToMetadataMapper.getSetAndAttributeNames(tagKey)
        if (setName.isNotBlank() && attributeName.isNotBlank()) {
            val attrId = customMetadataCache.getAttrIdForName(setName, attributeName)
            val attribDef = customMetadataCache.getAttributeDef(attrId)
            val options = attribDef.options
            if (options.isEnum) {
                val enumName = options.enumType
                val enum = enumCache.getByName(enumName)
                val currentValues = enum.elementDefs.map { it.value }.toSet()
                val missingValues = values.minus(currentValues)
                if (currentValues.isNotEmpty()) {
                    val response =
                        EnumDef.updater(enumName, missingValues.toList(), false)
                            .build()
                            .update()
                    println(response)
                }
            } else {
                logger.warn { "'$tagKey' mapped to '$setName.$attributeName' which is not an option. So $values were not added." }
            }
        } else {
            logger.warn { "'$tagKey' did not map to a CustomMetadata Attribute. So $values were not added." }
        }
    }
}
