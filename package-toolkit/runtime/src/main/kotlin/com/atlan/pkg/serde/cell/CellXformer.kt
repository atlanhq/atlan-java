/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.cache.ReflectionCache
import com.atlan.model.assets.Asset
import com.atlan.model.core.AtlanTag
import com.atlan.model.enums.AtlanEnum
import com.atlan.model.structs.AtlanStruct
import java.io.IOException
import java.util.SortedSet
import java.util.TreeSet

object CellXformer {
    const val LIST_DELIMITER = "\n"

    fun encode(
        value: Any?,
        guid: String,
        dates: Boolean,
    ): String {
        return when (value) {
            is String -> value
            is Collection<*> -> {
                val list = mutableListOf<String>()
                for (element in value) {
                    val encoded = encode(element, guid, dates)
                    list.add(encoded)
                }
                return getDelimitedList(list)
            }
            is Map<*, *> -> {
                val list = mutableListOf<String>()
                for ((key, embeddedValue) in value) {
                    list.add(key.toString() + "=" + encode(embeddedValue, guid, dates))
                }
                return getDelimitedList(list)
            }
            is Asset -> AssetRefXformer.encode(value)
            is AtlanEnum -> EnumXformer.encode(value)
            is AtlanStruct -> StructXformer.encode(value)
            is AtlanTag -> AtlanTagXformer.encode(guid, value)
            is Long -> {
                if (dates) {
                    TimestampXformer.encode(value)
                } else {
                    value.toString()
                }
            }
            is Any -> value.toString()
            else -> ""
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun decode(
        value: String?,
        type: Class<*>,
        innerType: Class<*>?,
        fieldName: String,
    ): Any? {
        return if (value.isNullOrEmpty()) {
            null
        } else if (String::class.java.isAssignableFrom(type)) {
            when (fieldName) {
                in UserXformer.FIELDS -> UserXformer.decode(value, fieldName)
                in GroupXformer.FIELDS -> GroupXformer.decode(value, fieldName)
                in RoleXformer.FIELDS -> RoleXformer.decode(value, fieldName)
                in DataTypeXformer.FIELDS -> DataTypeXformer.decode(value, fieldName)
                else -> value
            }
        } else if (Boolean::class.java.isAssignableFrom(type) || java.lang.Boolean::class.java.isAssignableFrom(type)) {
            value.toBoolean()
        } else if (Integer::class.java.isAssignableFrom(type) || java.lang.Integer::class.java.isAssignableFrom(type)) {
            value.toInt()
        } else if (Long::class.java.isAssignableFrom(type) || java.lang.Long::class.java.isAssignableFrom(type)) {
            if (ReflectionCache.isDate(type, fieldName)) {
                TimestampXformer.decode(value, fieldName)
            } else {
                value.toLong()
            }
        } else if (Double::class.java.isAssignableFrom(type) || java.lang.Double::class.java.isAssignableFrom(type)) {
            value.toDouble()
        } else if (Collection::class.java.isAssignableFrom(type)) {
            // Start by checking whether the list is simple or complex
            val values = parseDelimitedList(value)
            val list = mutableListOf<Any>()
            if (innerType != null) {
                for (element in values) {
                    val decoded = decode(element, innerType, null, fieldName)
                    if (decoded != null) {
                        list.add(decoded)
                    }
                }
            }
            when (type) {
                Collection::class.java, List::class.java -> list
                Set::class.java, SortedSet::class.java -> TreeSet(list)
                else -> throw IOException("Unable to deserialize cell to Java class: $type")
            }
        } else if (Map::class.java.isAssignableFrom(type)) {
            TODO("Not yet implemented for import")
        } else if (Asset::class.java.isAssignableFrom(type)) {
            AssetRefXformer.decode(value, fieldName)
        } else if (AtlanEnum::class.java.isAssignableFrom(type)) {
            EnumXformer.decode(value, type as Class<AtlanEnum>)
        } else if (AtlanStruct::class.java.isAssignableFrom(type)) {
            StructXformer.decode(value, type as Class<AtlanStruct>)
        } else if (AtlanTag::class.java.isAssignableFrom(type)) {
            AtlanTagXformer.decode(value)
        } else if (type.isInterface) {
            // Relationships between assets are defined via interfaces, so this would mean
            // there should be asset references
            AssetRefXformer.decode(value, fieldName)
        } else {
            throw IOException("Unhandled data type for $fieldName: $type")
        }
    }

    private fun getDelimitedList(values: List<String>?): String {
        return if (values.isNullOrEmpty()) {
            ""
        } else {
            values.joinToString(LIST_DELIMITER)
        }
    }

    private fun parseDelimitedList(values: String?): List<String> {
        return if (values.isNullOrEmpty()) {
            listOf()
        } else {
            values.split(LIST_DELIMITER).map { it.trim() }
        }
    }
}
