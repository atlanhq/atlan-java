/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae.exports

import com.atlan.Atlan
import com.atlan.pkg.serde.cell.TimestampXformer
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KLogger

class Users(
    private val xlsx: ExcelWriter,
    private val logger: KLogger,
) {

    fun export() {
        logger.info { "Exporting all users..." }
        val sheet = xlsx.createSheet("Users")
        xlsx.addHeader(
            sheet,
            mapOf(
                "Username" to "",
                "First name" to "",
                "Last name" to "",
                "Email address" to "",
                "Groups" to "Groups the user is assigned to",
                "Created" to "Date and time when the user was invited to Atlan",
                "Enabled" to "Whether the user is allowed to login (true) or not (false)",
                "Last login" to "Last date and time when the user logged into Atlan",
                "Personas" to "Personas assigned to the user",
            ),
        )
        val client = Atlan.getDefaultClient()
        client.users.list().forEach { user ->
            val personas = user.personas?.joinToString("\n") { it.displayName } ?: ""
            val groups = client.users.listGroups(user.id)?.records?.joinToString("\n") { it.name } ?: ""
            xlsx.appendRow(
                sheet,
                listOf(
                    user.username,
                    user.firstName,
                    user.lastName,
                    user.email,
                    groups,
                    TimestampXformer.encode(user.createdTimestamp),
                    user.enabled,
                    TimestampXformer.encode(user.lastLoginTime),
                    personas,
                ),
            )
        }
    }
}
