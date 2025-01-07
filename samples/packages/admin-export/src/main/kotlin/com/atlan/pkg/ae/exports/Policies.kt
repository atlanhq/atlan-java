/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae.exports

import AdminExportCfg
import com.atlan.AtlanClient
import com.atlan.exception.AtlanException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.AuthPolicy
import com.atlan.pkg.PackageContext
import com.atlan.pkg.ae.AdminExporter.ConnectionId
import com.atlan.pkg.serde.TabularWriter
import com.atlan.serde.Serde
import mu.KLogger

class Policies(
    private val ctx: PackageContext<AdminExportCfg>,
    private val writer: TabularWriter,
    private val glossaryMap: Map<String, String>,
    private val connectionMap: Map<String, ConnectionId>,
    private val logger: KLogger,
) {
    fun export() {
        logger.info { "Exporting policies, ${ if (ctx.config.includeNativePolicies) "including" else "excluding" } out-of-the-box..." }
        writer.writeHeader(
            mapOf(
                "Policy name" to "",
                "Description" to "",
                "Parent type" to "Type of parent access control mechanism that owns the policy",
                "Parent name" to "Name of parent access control mechanism that owns the policy",
                "Kind" to "Kind of policy",
                "Type" to "Type of the policy",
                "Resources" to "Resources the policy controls",
            ),
        )
        AuthPolicy
            .select(ctx.client)
            .includeOnResults(AuthPolicy.NAME)
            .includeOnResults(AuthPolicy.DESCRIPTION)
            .includeOnResults(AuthPolicy.ACCESS_CONTROL)
            .includeOnResults(AuthPolicy.POLICY_RESOURCES)
            .includeOnResults(AuthPolicy.POLICY_SUB_CATEGORY)
            .includeOnResults(AuthPolicy.POLICY_TYPE)
            .includeOnRelations(Asset.NAME)
            .stream()
            .forEach { policy ->
                policy as AuthPolicy
                if (policy.accessControl != null || ctx.config.includeNativePolicies) {
                    val resources = getResources(ctx.client, policy)
                    writer.writeRecord(
                        listOf(
                            policy.name,
                            policy.description,
                            policy.accessControl?.typeName ?: "",
                            policy.accessControl?.name ?: "",
                            policy.policySubCategory,
                            policy.policyType,
                            resources,
                        ),
                    )
                }
            }
    }

    private fun getResources(
        client: AtlanClient,
        policy: AuthPolicy,
    ): String {
        if (policy.accessControl?.typeName == "Purpose") {
            // In this case the "resources" are tags, so we should translate the tag names
            try {
                return policy.policyResources?.joinToString("\n") {
                    client.atlanTagCache.getNameForSid(it.substringAfter("tag:")) ?: Serde.DELETED_AUDIT_OBJECT
                } ?: Serde.DELETED_AUDIT_OBJECT
            } catch (e: AtlanException) {
                logger.warn { "Unable to find the tag associated with the policy -- marking it as removed." }
                logger.debug(e) { "Full details:" }
            }
            return Serde.DELETED_AUDIT_OBJECT
        }
        // Otherwise, we should consider how to translate the resources based on the
        // subcategory of the policy
        return when (policy.policySubCategory) {
            "metadata", "data" -> {
                policy.policyResources?.joinToString("\n") {
                    val candidate = it.substringAfter("entity:")
                    connectionMap.getOrDefault(candidate, candidate).toString()
                } ?: ""
            }

            "glossary" -> {
                policy.policyResources
                    ?.map {
                        glossaryMap[it.substringAfter("entity:")]
                    }?.joinToString("\n") ?: ""
            }

            "domain" -> {
                policy.policyResources?.joinToString("\n") {
                    it.substringAfter("entity:")
                } ?: ""
            }

            else -> ""
        }
    }
}
