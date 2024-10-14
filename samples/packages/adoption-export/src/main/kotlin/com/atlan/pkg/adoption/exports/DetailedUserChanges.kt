/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption.exports

import com.atlan.Atlan
import com.atlan.model.search.AuditSearch
import com.atlan.model.search.AuditSearchRequest
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KLogger

class DetailedUserChanges(
    private val xlsx: ExcelWriter,
    private val logger: KLogger,
    private val start: Long,
    private val end: Long,
) {
    fun export() {
        logger.info { "Exporting details of all user-made changes between $start - $end..." }
        val sheet = xlsx.createSheet("User changes")
        xlsx.addHeader(
            sheet,
            mapOf(
                "Time" to "Time at which the change occurred",
                "Username" to "User who made the change",
                "Action" to "Type of change the user made",
                "Asset type" to "Type of asset that was changed",
                "GUID" to "Globally unique identifier of the asset that was changed",
            ),
        )
        val builder =
            AuditSearch.builder(Atlan.getDefaultClient())
                .whereNot(AuditSearchRequest.AGENT.eq("workflow")) // Exclude crawled changes
        if (start > 0) {
            builder.where(AuditSearchRequest.CREATED.gte(start))
        }
        if (end > 0) {
            builder.where(AuditSearchRequest.CREATED.lt(end))
        }
        builder.stream()
            .forEach {
                xlsx.appendRow(
                    sheet,
                    listOf(
                        it.timestamp ?: "",
                        it.user ?: "",
                        it.action?.value ?: "",
                        it.typeName ?: "",
                        it.entityId ?: "",
                    ),
                )
            }
    }
}
