/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption.exports

import com.atlan.Atlan
import com.atlan.model.enums.AuditActionType
import com.atlan.model.search.AuditSearch
import com.atlan.model.search.AuditSearchRequest
import com.atlan.pkg.adoption.exports.AssetChanges.Companion.EXCLUDE_TYPES
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KLogger

class DetailedUserChanges(
    private val xlsx: ExcelWriter,
    private val logger: KLogger,
    private val start: Long,
    private val end: Long,
    private val includeAutomations: String,
) {
    fun export() {
        logger.info { "Exporting details of all user-made changes between [$start, $end]..." }
        val sheet = xlsx.createSheet("User changes")
        xlsx.addHeader(
            sheet,
            mapOf(
                "Time" to "Time at which the change occurred",
                "Username" to "User who made the change",
                "Action" to "Type of change the user made",
                "Type" to "Type of asset",
                "Qualified name" to "Unique name of the asset",
                "Agent" to "Mechanism through which the asset was changed",
                "Details" to "Further details about the mechanism through which the asset was changed",
            ),
        )
        val builder =
            AuditSearch.builder(Atlan.getDefaultClient())
                .whereNot(AuditSearchRequest.ENTITY_TYPE.`in`(EXCLUDE_TYPES))
        when (includeAutomations) {
            "NONE" -> builder.whereNot(AuditSearchRequest.AGENT.`in`(listOf("sdk", "workflow")))
            "WFL" -> builder.whereNot(AuditSearchRequest.AGENT.eq("sdk"))
            "SDK" -> builder.whereNot(AuditSearchRequest.AGENT.eq("workflow"))
            else -> logger.info { " ... including ALL automations -- this could be a large amount of data (and take a LONG time)." }
        }
        if (start > 0) {
            builder.where(AuditSearchRequest.CREATED.gte(start))
        }
        if (end > 0) {
            builder.where(AuditSearchRequest.CREATED.lt(end))
        }
        builder.stream()
            .forEach {
                val agent =
                    when (it.action) {
                        AuditActionType.PROPAGATED_ATLAN_TAG_ADD,
                        AuditActionType.PROPAGATED_ATLAN_TAG_UPDATE,
                        AuditActionType.PROPAGATED_ATLAN_TAG_DELETE,
                        -> "background"
                        else -> it.headers?.get("x-atlan-agent") ?: "UI"
                    }
                xlsx.appendRow(
                    sheet,
                    listOf(
                        it.timestamp ?: "",
                        it.user ?: "",
                        it.action?.value ?: "",
                        it.typeName ?: "",
                        it.entityQualifiedName ?: "",
                        agent,
                        it.headers?.get("x-atlan-agent-id") ?: "",
                    ),
                )
            }
    }
}
