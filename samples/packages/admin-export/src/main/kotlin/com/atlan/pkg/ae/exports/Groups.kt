/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae.exports

import AdminExportCfg
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.TabularWriter
import com.atlan.pkg.serde.cell.TimestampXformer
import mu.KLogger

class Groups(
    private val ctx: PackageContext<AdminExportCfg>,
    private val ts: String,
    private val writer: TabularWriter,
    private val logger: KLogger,
) {
    fun export() {
        logger.info { "Exporting all groups..." }
        writer.writeHeader(
            mapOf(
                "Group name" to "Name of the group, as it appears in the UI",
                "Internal name" to "Name of the group, as it must be specified programmatically",
                "Number of users" to "",
                "Default" to "Whether the group will be assigned to newly invited users by default (true) or not (false)",
                "Created at" to "Date and time when the group was created",
                "Created by" to "User who created the group",
                "Updated at" to "Date and time when the group was last updated",
                "Updated by" to "User who last updated the group",
                "Extracted on" to "Date and time when the group was extracted",
            ),
        )
        ctx.client.groups.list().forEach { group ->
            val createdAt =
                group.attributes
                    ?.createdAt
                    ?.get(0)
                    ?.toLong() ?: -1
            val updatedAt =
                group.attributes
                    ?.updatedAt
                    ?.get(0)
                    ?.toLong() ?: -1
            writer.writeRecord(
                listOf(
                    group.alias,
                    group.name,
                    group.userCount,
                    group.isDefault,
                    if (createdAt > 0) TimestampXformer.encode(createdAt) else "",
                    group.attributes?.createdBy?.get(0) ?: "",
                    if (updatedAt > 0) TimestampXformer.encode(updatedAt) else "",
                    group.attributes?.updatedBy?.get(0) ?: "",
                    ts,
                ),
            )
        }
    }
}
