/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.atlan.pkg.lftag.model.LFTagPair

class TagToMetadataMapper(private val metadataMap: Map<String, String>) {
    val missingTagKeys = mutableSetOf<String>()
    private val regex = "::".toRegex()

    fun getTagValues(tags: List<LFTagPair>, row: MutableMap<String, String>) {
        tags.forEach { tag ->
            val tagKey = if (tag.tagKey.startsWith("subdomain")) "Subdomain" else tag.tagKey
            if (tagKey in metadataMap) {
                val cmName = metadataMap.getValue(tagKey)
                row[cmName] = tag.tagValues[0]
            }
            this.missingTagKeys.add(tagKey)
        }
    }

    fun getSetAndAttributeNames(tagKey: String): Pair<String, String> {
        val setAndAttributeName = metadataMap.getOrDefault(if (tagKey.startsWith("subdomain")) "Subdomain" else tagKey, "::")
        val (first, second) = setAndAttributeName.split(regex)
        return Pair(first, second)
    }
}
