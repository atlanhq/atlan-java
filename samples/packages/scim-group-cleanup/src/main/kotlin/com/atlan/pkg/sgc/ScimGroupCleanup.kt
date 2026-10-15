/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.sgc

import ScimGroupCleanupCfg
import com.atlan.model.admin.AtlanGroup
import com.atlan.model.admin.UserResponse
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import mu.KotlinLogging

/**
 * Utility to diagnose and clean up stale SCIM group mappings.
 *
 * This addresses issues where Okta Push Groups fails with a stale externalId error,
 * indicating an orphaned mapping in the SCIM/Keycloak backend.
 *
 * The utility can:
 * 1. DIAGNOSTIC mode: List group details and identify potential stale mappings
 * 2. CLEANUP mode: Delete and optionally recreate the group to clear stale SCIM mappings
 */
object ScimGroupCleanup {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        Utils.initializeContext<ScimGroupCleanupCfg>().use { ctx ->
            val groupName = ctx.config.groupName
            val operationMode = ctx.config.operationMode
            val recreateGroup = ctx.config.recreateGroup

            logger.info { "==================================================" }
            logger.info { "SCIM Group Cleanup Utility" }
            logger.info { "==================================================" }
            logger.info { "Target group: $groupName" }
            logger.info { "Operation mode: $operationMode" }
            logger.info { "==================================================" }

            when (operationMode) {
                "DIAGNOSTIC" -> runDiagnostic(ctx, groupName)
                "CLEANUP" -> runCleanup(ctx, groupName, recreateGroup)
                else -> {
                    logger.error { "Invalid operation mode: $operationMode. Must be DIAGNOSTIC or CLEANUP." }
                }
            }

            logger.info { "==================================================" }
            logger.info { "Cleanup operation completed" }
            logger.info { "==================================================" }
        }
    }

    /**
     * Run diagnostic mode to identify group details and potential issues.
     */
    private fun runDiagnostic(
        ctx: PackageContext<ScimGroupCleanupCfg>,
        groupName: String,
    ) {
        logger.info { "Running diagnostic for group: $groupName" }

        try {
            val groups = AtlanGroup.get(ctx.client, groupName)

            if (groups.isNullOrEmpty()) {
                logger.warn { "No group found with name: $groupName" }
                logger.info { "This could indicate:" }
                logger.info { "  1. The group name is incorrect" }
                logger.info { "  2. The group was already deleted" }
                logger.info { "  3. There is a stale SCIM mapping with no corresponding Atlan group" }
                return
            }

            groups.forEach { group ->
                logger.info { "Found group:" }
                logger.info { "  ID: ${group.id}" }
                logger.info { "  Name (internal): ${group.name}" }
                logger.info { "  Alias (display): ${group.alias}" }
                logger.info { "  Path: ${group.path}" }
                logger.info { "  User count: ${group.userCount}" }
                logger.info { "  Default group: ${group.isDefault}" }

                if (group.attributes != null) {
                    logger.info { "  Created at: ${group.attributes.createdAt}" }
                    logger.info { "  Created by: ${group.attributes.createdBy}" }
                    logger.info { "  Updated at: ${group.attributes.updatedAt}" }
                    logger.info { "  Updated by: ${group.attributes.updatedBy}" }
                }

                // Try to fetch group members
                try {
                    val members: UserResponse = group.fetchUsers(ctx.client)
                    logger.info { "  Total members: ${members.totalRecord}" }
                    if (members.records != null && members.records.isNotEmpty()) {
                        logger.info { "  Sample members:" }
                        members.records.take(5).forEach { user ->
                            logger.info { "    - ${user.username} (${user.email})" }
                        }
                        if (members.records.size > 5) {
                            logger.info { "    ... and ${members.records.size - 5} more" }
                        }
                    }
                } catch (e: Exception) {
                    logger.error(e) { "  Failed to fetch group members" }
                }
            }

            if (groups.size > 1) {
                logger.warn { "Multiple groups found with similar names. This might indicate orphaned groups." }
                logger.info { "Consider running cleanup on specific groups if needed." }
            }

            logger.info { "" }
            logger.info { "Diagnostic complete. To clean up stale SCIM mappings:" }
            logger.info { "1. Back up any important group member assignments" }
            logger.info { "2. Run this utility in CLEANUP mode" }
            logger.info { "3. Re-push the group from Okta using SCIM Push Groups" }
        } catch (e: Exception) {
            logger.error(e) { "Error during diagnostic" }
        }
    }

    /**
     * Run cleanup mode to delete the group and optionally recreate it.
     * This removes stale SCIM mappings.
     */
    private fun runCleanup(
        ctx: PackageContext<ScimGroupCleanupCfg>,
        groupName: String,
        recreateGroup: Boolean,
    ) {
        logger.info { "Running cleanup for group: $groupName" }
        logger.warn { "This will DELETE the group and its member assignments!" }

        try {
            val groups = AtlanGroup.get(ctx.client, groupName)

            if (groups.isNullOrEmpty()) {
                logger.warn { "No group found with name: $groupName. Nothing to clean up." }
                return
            }

            // Store group details for potential recreation
            val groupsToDelete = mutableListOf<GroupSnapshot>()

            groups.forEach { group ->
                logger.info { "Processing group: ${group.alias} (ID: ${group.id})" }

                // Capture group snapshot
                val snapshot =
                    GroupSnapshot(
                        id = group.id,
                        name = group.name,
                        alias = group.alias,
                        path = group.path,
                        description = group.attributes?.description?.firstOrNull(),
                        isDefault = group.isDefault,
                    )

                // Capture members
                try {
                    val members = group.fetchUsers(ctx.client)
                    snapshot.memberIds = members.records?.map { it.id } ?: emptyList()
                    logger.info { "  Captured ${snapshot.memberIds.size} group members for backup" }
                } catch (e: Exception) {
                    logger.error(e) { "  Failed to capture group members" }
                }

                groupsToDelete.add(snapshot)
            }

            // Delete groups
            groupsToDelete.forEach { snapshot ->
                logger.info { "Deleting group: ${snapshot.alias} (ID: ${snapshot.id})" }
                try {
                    AtlanGroup.delete(ctx.client, snapshot.id)
                    logger.info { "  Successfully deleted group ${snapshot.alias}" }
                    logger.info { "  This should have cleared any stale SCIM mappings for externalId" }
                } catch (e: Exception) {
                    logger.error(e) { "  Failed to delete group ${snapshot.alias}" }
                }
            }

            // Recreate if requested
            if (recreateGroup && groupsToDelete.isNotEmpty()) {
                logger.info { "" }
                logger.info { "Recreating groups..." }

                // Wait a bit for backend cleanup
                Thread.sleep(2000)

                groupsToDelete.forEach { snapshot ->
                    logger.info { "Recreating group: ${snapshot.alias}" }
                    try {
                        val newGroup =
                            AtlanGroup
                                .creator(snapshot.alias)
                                .attributes(
                                    AtlanGroup.GroupAttributes
                                        .builder()
                                        .alias(listOf(snapshot.alias))
                                        .apply {
                                            if (snapshot.description != null) {
                                                description(listOf(snapshot.description))
                                            }
                                            if (snapshot.isDefault) {
                                                isDefault(listOf("true"))
                                            }
                                        }.build(),
                                ).build()

                        val newId = newGroup.create(ctx.client)
                        logger.info { "  Created new group with ID: $newId" }

                        // Restore members if any
                        if (snapshot.memberIds.isNotEmpty()) {
                            logger.info { "  Restoring ${snapshot.memberIds.size} members..." }
                            try {
                                // Get the newly created group
                                val recreatedGroups = AtlanGroup.get(ctx.client, snapshot.alias)
                                if (!recreatedGroups.isNullOrEmpty()) {
                                    val recreatedGroup = recreatedGroups[0]
                                    ctx.client.groups.create(recreatedGroup, snapshot.memberIds)
                                    logger.info { "  Successfully restored group members" }
                                }
                            } catch (e: Exception) {
                                logger.error(e) { "  Failed to restore group members. Manual reassignment may be needed." }
                                logger.error { "  Member IDs: ${snapshot.memberIds.joinToString(", ")}" }
                            }
                        }
                    } catch (e: Exception) {
                        logger.error(e) { "  Failed to recreate group ${snapshot.alias}" }
                    }
                }
            }

            logger.info { "" }
            logger.info { "Cleanup complete. The stale SCIM mapping should now be cleared." }
            logger.info { "You can now try pushing the group from Okta again via SCIM Push Groups." }
        } catch (e: Exception) {
            logger.error(e) { "Error during cleanup" }
        }
    }

    /**
     * Snapshot of a group's details for backup and potential recreation.
     */
    private data class GroupSnapshot(
        val id: String,
        val name: String,
        val alias: String,
        val path: String?,
        val description: String?,
        val isDefault: Boolean,
        var memberIds: List<String> = emptyList(),
    )
}
