/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae.exports

import com.atlan.Atlan
import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.assets.AuthPolicy
import com.atlan.pkg.ae.AdminExporter.ConnectionId
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KLogger

class Policies(
    private val xlsx: ExcelWriter,
    private val includeNative: Boolean,
    private val glossaryMap: Map<String, String>,
    private val connectionMap: Map<String, ConnectionId>,
    private val logger: KLogger,
) {

    fun export() {
        logger.info { "Exporting policies, ${ if (includeNative) "including" else "excluding" } out-of-the-box..." }
        val sheet = xlsx.createSheet("Policies")
        xlsx.addHeader(
            sheet,
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
        val client = Atlan.getDefaultClient()
        AuthPolicy.select()
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
                if (policy.accessControl != null || includeNative) {
                    val resources = getResources(client, policy)
                    xlsx.appendRow(
                        sheet,
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

    private fun getResources(client: AtlanClient, policy: AuthPolicy): String {
        if (policy.accessControl.typeName == "Purpose") {
            // In this case the "resources" are tags, so we should translate the tag names
            return policy.policyResources?.joinToString("\n") {
                client.atlanTagCache.getNameForId(it.substringAfter("tag:"))
            } ?: ""
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
                policy.policyResources?.map {
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
