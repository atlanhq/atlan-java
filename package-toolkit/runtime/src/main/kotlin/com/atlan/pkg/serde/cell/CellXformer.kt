/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.cache.ReflectionCache
import com.atlan.model.assets.Asset
import com.atlan.model.core.AtlanTag
import com.atlan.model.enums.AtlanEnum
import com.atlan.model.fields.AtlanField
import com.atlan.model.structs.AtlanStruct
import com.atlan.pkg.PackageContext
import mu.KLogger
import java.io.IOException
import java.util.SortedSet
import java.util.TreeSet

object CellXformer {
    const val LIST_DELIMITER = "\n"
    private const val NEWLINE_SENTINEL = "§LF±"

    fun encode(
        ctx: PackageContext<*>,
        value: Any?,
        field: AtlanField? = null,
        guid: String? = null,
        dates: Boolean = false,
    ): String {
        return when (value) {
            is String -> {
                if (field == Asset.DOMAIN_GUIDS) {
                    DataDomainXformer.encodeFromGuid(ctx, value)
                } else {
                    encodeString(value)
                }
            }
            is Collection<*> -> {
                val list = mutableListOf<String>()
                for (element in value) {
                    val encoded = encode(ctx, element, field, guid, dates)
                    list.add(encoded)
                }
                return getDelimitedList(list)
            }
            is Map<*, *> -> {
                val list = mutableListOf<String>()
                for ((key, embeddedValue) in value) {
                    list.add(key.toString() + "=" + encode(ctx, embeddedValue, field, guid, dates))
                }
                return getDelimitedList(list)
            }
            is Asset -> AssetRefXformer.encode(ctx, value)
            is AtlanEnum -> EnumXformer.encode(value)
            is AtlanStruct -> StructXformer.encode(ctx.client, value)
            is AtlanTag -> AtlanTagXformer.encode(ctx.client, guid!!, value)
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
        ctx: PackageContext<*>,
        assetClass: Class<*>?,
        value: String?,
        type: Class<*>,
        innerType: Class<*>?,
        fieldName: String,
        logger: KLogger,
    ): Any? =
        if (value.isNullOrEmpty()) {
            null
        } else if (String::class.java.isAssignableFrom(type)) {
            when (fieldName) {
                in UserXformer.FIELDS -> UserXformer.decode(ctx, value, fieldName)
                in GroupXformer.FIELDS -> GroupXformer.decode(ctx, value, fieldName)
                in RoleXformer.FIELDS -> RoleXformer.decode(ctx, value, fieldName)
                in DataTypeXformer.FIELDS -> DataTypeXformer.decode(value, fieldName)
                else -> decodeString(ctx, value)
            }
        } else if (Boolean::class.java.isAssignableFrom(type) || java.lang.Boolean::class.java.isAssignableFrom(type)) {
            decodeBoolean(value)
        } else if (Integer::class.java.isAssignableFrom(type) || java.lang.Integer::class.java.isAssignableFrom(type)) {
            decodeInt(value)
        } else if (Long::class.java.isAssignableFrom(type) || java.lang.Long::class.java.isAssignableFrom(type)) {
            if (ReflectionCache.isDate(assetClass, fieldName)) {
                TimestampXformer.decode(value, fieldName)
            } else {
                decodeLong(value)
            }
        } else if (Double::class.java.isAssignableFrom(type) || java.lang.Double::class.java.isAssignableFrom(type)) {
            decodeDouble(value)
        } else if (Collection::class.java.isAssignableFrom(type)) {
            // Start by checking whether the list is simple or complex
            val values = parseDelimitedList(value)
            val list = mutableListOf<Any>()
            if (innerType != null) {
                for (element in values) {
                    val decoded = decode(ctx, assetClass, element, innerType, null, fieldName, logger)
                    if (decoded != null) {
                        list.add(decoded)
                    }
                }
            }
            when (type) {
                Collection::class.java, List::class.java -> list
                Set::class.java, SortedSet::class.java -> TreeSet(list)
                else -> throw IOException("Unable to deserialize cell to Java class (in $fieldName): $type")
            }
        } else if (Map::class.java.isAssignableFrom(type)) {
            MapXformer.decode(ctx.client, value, type as Class<Map<*, *>>)
        } else if (Asset::class.java.isAssignableFrom(type)) {
            AssetRefXformer.decode(ctx, value, fieldName)
        } else if (AtlanEnum::class.java.isAssignableFrom(type)) {
            EnumXformer.decode(value, type as Class<AtlanEnum>, fieldName)
        } else if (AtlanStruct::class.java.isAssignableFrom(type)) {
            StructXformer.decode(ctx.client, value, type as Class<AtlanStruct>)
        } else if (AtlanTag::class.java.isAssignableFrom(type)) {
            AtlanTagXformer.decode(ctx.client, value, logger)
        } else if (type.isInterface) {
            // Relationships between assets are defined via interfaces, so this would mean
            // there should be asset references
            AssetRefXformer.decode(ctx, value, fieldName)
        } else {
            throw IOException("Unhandled data type (in $fieldName): $type")
        }

    fun getDelimitedList(values: List<String>?): String =
        if (values.isNullOrEmpty()) {
            ""
        } else {
            values.joinToString(LIST_DELIMITER)
        }

    fun parseDelimitedList(values: String?): List<String> =
        if (values.isNullOrEmpty()) {
            listOf()
        } else {
            values.split(LIST_DELIMITER).map { it.trim() }
        }

    fun encodeString(value: String): String =
        if (value.contains(LIST_DELIMITER)) {
            value.replace(LIST_DELIMITER, NEWLINE_SENTINEL)
        } else {
            value
        }

    fun decodeString(
        ctx: PackageContext<*>,
        value: String,
    ): String =
        if (value.contains(NEWLINE_SENTINEL)) {
            value.replace(NEWLINE_SENTINEL, LIST_DELIMITER)
        } else {
            // Attempt to resolve the string as a deferred qualifiedName
            // (if not a qualifiedName will just return the original string as-is)
            AssetRefXformer.resolveDeferredQN(ctx, value)
        }

    fun decodeBoolean(value: String): Boolean = value.toBoolean()

    fun decodeInt(value: String): Int = value.toInt()

    fun decodeLong(value: String): Long = value.toLong()

    fun decodeDouble(value: String): Double = value.toDouble()
}
