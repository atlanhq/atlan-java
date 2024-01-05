/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae.exports

import com.atlan.Atlan
import com.atlan.pkg.serde.cell.TimestampXformer
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KLogger

class Groups(
    private val xlsx: ExcelWriter,
    private val logger: KLogger,
) {
    fun export() {
        logger.info { "Exporting all groups..." }
        val sheet = xlsx.createSheet("Groups")
        xlsx.addHeader(
            sheet,
            mapOf(
                "Group name" to "Name of the group, as it appears in the UI",
                "Internal name" to "Name of the group, as it must be specified programmatically",
                "Number of users" to "",
                "Default" to "Whether the group will be assigned to newly invited users by default (true) or not (false)",
                "Created at" to "Date and time when the group was created",
                "Created by" to "User who created the group",
                "Updated at" to "Date and time when the group was last updated",
                "Updated by" to "User who last updated the group",
            ),
        )
        Atlan.getDefaultClient().groups.list().forEach { group ->
            val createdAt = group.attributes?.createdAt?.get(0)?.toLong() ?: -1
            val updatedAt = group.attributes?.updatedAt?.get(0)?.toLong() ?: -1
            xlsx.appendRow(
                sheet,
                listOf(
                    group.alias,
                    group.name,
                    group.userCount,
                    group.isDefault,
                    if (createdAt > 0) TimestampXformer.encode(createdAt) else "",
                    group.attributes?.createdBy?.get(0) ?: "",
                    if (updatedAt > 0) TimestampXformer.encode(updatedAt) else "",
                    group.attributes?.updatedBy?.get(0) ?: "",
                ),
            )
        }
    }
}
