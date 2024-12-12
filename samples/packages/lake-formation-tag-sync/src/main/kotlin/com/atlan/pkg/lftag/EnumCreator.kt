/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.atlan.AtlanClient
import com.atlan.model.typedefs.EnumDef
import com.atlan.pkg.Utils

class EnumCreator(private val client: AtlanClient, val tagToMetadataMapper: TagToMetadataMapper) {
    private val logger = Utils.getLogger(this.javaClass.name)
    private val customMetadataCache = client.customMetadataCache
    private val enumCache = client.enumCache

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
                        EnumDef.updater(client, enumName, missingValues.toList(), false)
                            .build()
                            .update(client)
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
