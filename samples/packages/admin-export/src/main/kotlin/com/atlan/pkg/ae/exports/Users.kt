/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae.exports

import AdminExportCfg
import com.atlan.api.UsersEndpoint
import com.atlan.model.admin.UserRequest
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.TabularWriter
import com.atlan.pkg.serde.cell.TimestampXformer
import mu.KLogger
import java.time.Instant

class Users(
    private val ctx: PackageContext<AdminExportCfg>,
    private val writer: TabularWriter,
    private val logger: KLogger,
) {
    fun export() {
        logger.info { "Exporting all users..." }
        writer.writeHeader(
            mapOf(
                "Username" to "",
                "First name" to "",
                "Last name" to "",
                "Email address" to "",
                "Groups" to "Groups the user is assigned to",
                "Created" to "Date and time when the user was invited to Atlan",
                "Enabled" to "Whether the user is allowed to login (true) or not (false)",
                "Last login" to "Last date and time when the user logged into Atlan",
                // "Total logins" to "Total number of times the user has logged into Atlan",
                "Personas" to "Personas assigned to the user",
                "License type" to "Type of license assigned to the user",
                "Designation" to "Designation of the user",
                "Group names" to "Non-technical names of groups the user is assigned to",
                "Extracted on" to "Date and time when the user was extracted",
            ),
        )
        val request =
            UserRequest
                .builder()
                .columns(UsersEndpoint.DEFAULT_PROJECTIONS)
                .column("profileRole")
                .column("profileRoleOther")
                .build()
        val ts = Instant.now().toString()
        ctx.client.users.list(request).forEach { user ->
            val personas = user.personas?.joinToString("\n") { it.displayName ?: "" } ?: ""
            val groups =
                ctx.client.users
                    .listGroups(user.id)
                    ?.records
            val technicalNames = groups?.joinToString("\n") { it.name ?: "" } ?: ""
            val designation =
                if (user.attributes?.profileRole?.get(0) == "Other") {
                    user.attributes?.profileRoleOther?.get(0)
                } else {
                    user.attributes?.profileRole?.get(0)
                }
            val nontechnicalNames = groups?.joinToString("\n") { it.alias ?: "" } ?: ""
            /*val loginCount =
                ctx.client.logs
                    .getEvents(
                        KeycloakEventRequest
                            .builder()
                            .type(KeycloakEventType.LOGIN)
                            .userId(user.id)
                            .build(),
                    ).count()*/
            val licenseType = user.assignedRole?.description ?: user.workspaceRole
            writer.writeRecord(
                listOf(
                    user.username,
                    user.firstName,
                    user.lastName,
                    user.email,
                    technicalNames,
                    TimestampXformer.encode(user.createdTimestamp),
                    user.enabled,
                    TimestampXformer.encode(user.lastLoginTime),
                    // loginCount,
                    personas,
                    licenseType,
                    designation,
                    nontechnicalNames,
                    ts,
                ),
            )
        }
    }
}
