/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae.exports

import AdminExportCfg
import com.atlan.api.UsersEndpoint
import com.atlan.model.admin.AtlanGroup
import com.atlan.model.admin.AtlanUser
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
                .limit(100)
                .build()

        // Collect all users upfront so we can bulk-fetch group memberships in one shot
        logger.info { "Fetching all users from Heracles..." }
        val allUsers =
            ctx.client.users
                .list(request)
                .toList()
        logger.info { "Fetched ${allUsers.size} users. Resolving group memberships via IAM API..." }

        // Bulk-fetch user details + group memberships from Redis in a single call per batch
        logger.info { "Resolving user details and group memberships via IdentityEndpoint (bulk IAM API)..." }
        val iamUsersMap = ctx.client.identity.getUsers(allUsers.mapNotNull { it.id })
        logger.info { "Resolved user details from IAM Redis API, resolving group aliases..." }

        // Collect all unique group IDs seen across all users, then resolve aliases in bulk
        val allGroupIds =
            iamUsersMap.values
                .flatMap { it.groups ?: emptyList<AtlanGroup>() }
                .map { it.id }
                .distinct()
        val groupAliasMap = ctx.client.identity.getGroupAliases(allGroupIds)

        val ts = Instant.now().toString()
        allUsers.forEach { user ->
            val iamUser: AtlanUser? = iamUsersMap[user.id]
            val personas = user.personas?.joinToString("\n") { it.displayName ?: "" } ?: ""
            val groups = iamUser?.groups ?: emptyList<AtlanGroup>()
            val technicalNames = groups.joinToString("\n") { it.name }
            val nontechnicalNames = groups.joinToString("\n") { groupAliasMap[it.id] ?: it.name }
            val designation =
                if (user.attributes?.profileRole?.get(0) == "Other") {
                    user.attributes?.profileRoleOther?.get(0)
                } else {
                    user.attributes?.profileRole?.get(0)
                }
            val licenseType = licenseTypeOf(user)
            writer.writeRecord(
                listOf(
                    iamUser?.username ?: user.username,
                    iamUser?.firstName ?: user.firstName,
                    iamUser?.lastName ?: user.lastName,
                    iamUser?.email ?: user.email,
                    technicalNames,
                    TimestampXformer.encode(iamUser?.createdTimestamp ?: user.createdTimestamp),
                    iamUser?.enabled ?: user.enabled,
                    TimestampXformer.encode(user.lastLoginTime),
                    personas,
                    licenseType,
                    designation,
                    nontechnicalNames,
                    ts,
                ),
            )
        }
    }

    companion object {
        /**
         * Resolve the license type (workspace role) to display for a user, preferring the human-readable
         * display name and never emitting a raw "$"-prefixed system identifier (CSA-449).
         *
         * Resolution order:
         *  1. assignedRole.description (display name, e.g. "Guest") — populated for most users
         *  2. humanized assignedRole.name (e.g. "$guest" -> "Guest") — when description is null/blank
         *  3. humanized workspaceRole — when assignedRole itself is null
         *  4. empty string — when nothing is available
         */
        internal fun licenseTypeOf(user: AtlanUser): String =
            user.assignedRole?.description?.ifBlank { null }
                ?: user.assignedRole?.name?.humanizeRole()
                ?: user.workspaceRole?.humanizeRole()
                ?: ""

        /**
         * Convert a system role identifier into a human-readable display name, e.g. "$guest" -> "Guest".
         * Strips a leading "$" and capitalizes the first character; other values are returned unchanged.
         * Blank input returns null so callers can fall through to the next option.
         */
        private fun String.humanizeRole(): String? = ifBlank { null }?.removePrefix("$")?.replaceFirstChar(Char::uppercaseChar)
    }
}
