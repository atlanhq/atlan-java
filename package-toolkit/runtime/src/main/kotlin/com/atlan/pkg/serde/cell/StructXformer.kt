/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.AtlanClient
import com.atlan.model.structs.AtlanStruct

object StructXformer {
    fun encode(
        client: AtlanClient,
        struct: AtlanStruct,
    ): String = struct.toJson(client)

    fun decode(
        client: AtlanClient,
        struct: String,
        structClass: Class<AtlanStruct>,
    ): AtlanStruct = client.readValue(struct, structClass)
}
