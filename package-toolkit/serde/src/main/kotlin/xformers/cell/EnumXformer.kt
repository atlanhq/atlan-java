/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package xformers.cell

import com.atlan.model.enums.AtlanEnum

object EnumXformer {
    fun encode(enum: AtlanEnum): String {
        return enum.value
    }

    fun decode(
        enum: String,
        enumClass: Class<AtlanEnum>,
    ): AtlanEnum {
        val method = enumClass.getMethod("fromValue", String::class.java)
        return method.invoke(null, enum) as AtlanEnum
    }
}
