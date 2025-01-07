/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Column

/**
 * Utility class to convert data types for columns.
 */
object DataTypeXformer {
    val FIELDS =
        setOf(
            Column.DATA_TYPE.atlanFieldName,
        )

    /** Types that appear to be spurious values in the data — we will skip these entirely.  */
    private val spuriousValues = setOf("DATA TYPE", "DATA_TYPE", "HIERARCHYID", "NULL")

    /** Mappings from one type to another.  */
    private val typeMap = mapOf("NVARCHAR" to "VARCHAR")

    /**
     * Normalize the provided data type into a form that can be used in Atlan.
     *
     * @param type either a simple or SQL-style data type
     * @param fieldName name of the field where the value was found
     * @return a normalized data type for use in Atlan
     */
    fun decode(
        type: String?,
        fieldName: String,
    ): String? {
        if (type.isNullOrBlank()) {
            return null
        }
        return when (fieldName) {
            in FIELDS -> getMappedType(getTypeOnly(type)!!)
            else -> type
        }
    }

    /**
     * Retrieve the mapped data type for the provided type, or null if there is no mapped type (for spurious types).
     *
     * @param type to map
     * @return the mapped data type
     */
    private fun getMappedType(type: String): String? =
        if (spuriousValues.contains(type)) {
            // If it's in our list of spurious values, return null
            null
        } else {
            // Otherwise returned the mapped type (if any), and finally fallback to
            // returning the received type itself
            typeMap.getOrDefault(type, type)
        }

    /**
     * Retrieve only the type name from the provided SQL type string.
     * Note: this will also uppercase the typename for case-insensitive comparison purposes.
     *
     * @param sqlType from which to retrieve the type name
     * @return the type name (alone), or null if none could be found
     */
    private fun getTypeOnly(sqlType: String?): String? =
        if (sqlType != null) {
            if (sqlType.contains("(")) {
                sqlType.substring(0, sqlType.indexOf("(")).trim { it <= ' ' }.uppercase()
            } else {
                sqlType.uppercase()
            }
        } else {
            null
        }

    /**
     * Retrieve the maximum length defined by the provided SQL type string.
     *
     * @param sqlType from which to retrieve the maximum length
     * @return the maximum length, or null if none could be found
     */
    fun getMaxLength(sqlType: String?): Long? {
        if (sqlType != null) {
            if (sqlType.contains("(") && !sqlType.contains(",")) {
                val length =
                    sqlType
                        .substring(sqlType.indexOf("(") + 1, sqlType.indexOf(")"))
                        .trim { it <= ' ' }
                // TODO: should probably make this more general by catching a format exception...
                if (length != "MAX") {
                    return length.toLong()
                }
            }
        }
        return null
    }

    /**
     * Retrieve the precision defined by the provided SQL type string.
     *
     * @param sqlType from which to retrieve the precision
     * @return the precision, or null if none could be found
     */
    fun getPrecision(sqlType: String?): Int? {
        if (sqlType != null) {
            if (sqlType.contains("(") && sqlType.contains(",")) {
                val precision =
                    sqlType
                        .substring(sqlType.indexOf("(") + 1, sqlType.indexOf(","))
                        .trim { it <= ' ' }
                return precision.toInt()
            }
        }
        return null
    }

    /**
     * Retrieve the scale defined by the provided SQL type string.
     *
     * @param sqlType from which to retrieve the scale
     * @return the scale, or null if none could be found
     */
    fun getScale(sqlType: String?): Double? {
        if (sqlType != null) {
            if (sqlType.contains("(") && sqlType.contains(",")) {
                val scale =
                    sqlType
                        .substring(sqlType.indexOf(",") + 1, sqlType.indexOf(")"))
                        .trim { it <= ' ' }
                return scale.toDouble()
            }
        }
        return null
    }
}
