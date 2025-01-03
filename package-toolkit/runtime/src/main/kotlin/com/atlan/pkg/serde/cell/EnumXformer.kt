/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.enums.AtlanEnum

object EnumXformer {
    fun encode(enum: AtlanEnum): String {
        return enum.value
    }

    fun decode(
        enum: String,
        enumClass: Class<AtlanEnum>,
        fieldName: String,
    ): AtlanEnum {
        val method = enumClass.getMethod("fromValue", String::class.java)
        val result: Any? = method.invoke(null, enum)
        if (result == null) {
            throw IllegalArgumentException("$enumClass (in field $fieldName) does not have any matching value for: $enum")
        } else {
            return result as AtlanEnum
        }
    }
}
