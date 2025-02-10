/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanEnum

object EnumXformer {
    fun encode(enum: AtlanEnum): String = enum.value

    fun decode(
        enum: String,
        enumClass: Class<AtlanEnum>,
        fieldName: String,
    ): AtlanEnum {
        val method = enumClass.getMethod("fromValue", String::class.java)
        // Apply some automated normalization to certain enum values
        val normalizedValue =
            when (enumClass) {
                AtlanConnectorType::class.java -> enum.lowercase()
                else -> enum
            }
        val result: Any? = method.invoke(null, normalizedValue)
        if (result != null) {
            return result as AtlanEnum
        }
        // If the first step of validation does not give us a result, try making the value all-caps as an additional minimal fuzzy-matching step
        val allCaps = normalizedValue.uppercase()
        val fuzzy: Any? = method.invoke(null, allCaps)
        if (fuzzy != null) {
            return fuzzy as AtlanEnum
        }
        // And if that still doesn't work, give up and fail with an appropriate error message
        throw IllegalArgumentException("No matching value found for $enumClass (in field $fieldName): $enum")
    }
}
