/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package xformers.cell

import com.atlan.Atlan
import com.atlan.model.structs.AtlanStruct

object StructXformer {
    fun encode(struct: AtlanStruct): String {
        return struct.toJson(Atlan.getDefaultClient())
    }

    fun decode(
        struct: String,
        structClass: Class<AtlanStruct>,
    ): AtlanStruct {
        return Atlan.getDefaultClient().readValue(struct, structClass)
    }
}
