/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.AtlanClient

object MapXformer {
    fun encode(
        client: AtlanClient,
        map: Map<*, *>,
    ): String = client.writeValueAsString(map)

    fun decode(
        client: AtlanClient,
        value: String,
        mapClass: Class<Map<*, *>>,
    ): Map<*, *> = client.readValue(value, mapClass)
}
