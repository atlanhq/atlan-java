/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae.exports

import AdminExportCfg
import com.atlan.model.assets.AuthPolicy
import com.atlan.model.assets.Purpose
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.TabularWriter
import mu.KLogger
import java.time.Instant

class Purposes(
    private val ctx: PackageContext<AdminExportCfg>,
    private val writer: TabularWriter,
    private val logger: KLogger,
) {
    fun export() {
        logger.info { "Exporting all purposes..." }
        writer.writeHeader(
            mapOf(
                "Purpose name" to "",
                "Description" to "",
                "Tags" to "Tags controlled by this purpose",
                "Metadata policies" to "",
                "Data policies" to "",
                "Groups" to "Groups to which these policies are applied",
                "Users" to "Users to which these policies are applied",
                "Navigation" to "Default landing page",
                "Hide asset types" to "Asset types that should be hidden",
                "Hide sidebar tabs" to "Sidebar tabs that should be hidden",
                "Hide asset filters" to "Asset search filters that should be hidden",
                "Custom metadata to hide" to "Custom metadata tabs that should be hidden",
                "Extracted on" to "Date and time when the purpose was extracted",
            ),
        )
        val ts = Instant.now().toString()
        Purpose
            .select(ctx.client)
            .includeOnResults(Purpose.NAME)
            .includeOnResults(Purpose.DESCRIPTION)
            .includeOnResults(Purpose.PURPOSE_ATLAN_TAGS)
            .includeOnResults(Purpose.POLICIES)
            .includeOnResults(Purpose.DEFAULT_NAVIGATION)
            .includeOnResults(Purpose.DENY_ASSET_TYPES)
            .includeOnResults(Purpose.DENY_ASSET_TABS)
            .includeOnResults(Purpose.DENY_ASSET_FILTERS)
            .includeOnResults(Purpose.DENY_CUSTOM_METADATA_GUIDS)
            .includeOnRelations(AuthPolicy.NAME)
            .includeOnRelations(AuthPolicy.POLICY_TYPE)
            .includeOnRelations(AuthPolicy.POLICY_SUB_CATEGORY)
            .includeOnRelations(AuthPolicy.POLICY_GROUPS)
            .includeOnRelations(AuthPolicy.POLICY_USERS)
            .stream()
            .forEach { purpose ->
                purpose as Purpose
                var metadataPolicyCount = 0
                var dataPolicyCount = 0
                val groups = mutableSetOf<String>()
                val users = mutableSetOf<String>()
                val denyCustomMetadata = Preferences.getCustomMetadataToDeny(ctx, "purpose", purpose.denyCustomMetadataGuids, logger)
                purpose.policies.forEach { policy ->
                    when (policy.policySubCategory) {
                        "metadata" -> {
                            metadataPolicyCount += 1
                            groups.addAll(policy.policyGroups)
                            users.addAll(policy.policyUsers)
                        }
                        "data" -> {
                            dataPolicyCount += 1
                            groups.addAll(policy.policyGroups)
                            users.addAll(policy.policyUsers)
                        }
                    }
                }
                writer.writeRecord(
                    listOf(
                        purpose.name,
                        purpose.description,
                        purpose.purposeAtlanTags.joinToString("\n"),
                        metadataPolicyCount,
                        dataPolicyCount,
                        groups.joinToString("\n"),
                        users.joinToString("\n"),
                        purpose.defaultNavigation,
                        purpose.denyAssetTypes.joinToString("\n"),
                        purpose.denyAssetTabs.joinToString(separator = "\n") { it.value },
                        purpose.denyAssetFilters.joinToString(separator = "\n") { it.value },
                        denyCustomMetadata.joinToString("\n"),
                        ts,
                    ),
                )
            }
    }
}
