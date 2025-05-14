/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae.exports

import AdminExportCfg
import com.atlan.model.assets.AuthPolicy
import com.atlan.model.assets.Persona
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.TabularWriter
import com.atlan.pkg.util.AssetResolver
import mu.KLogger
import java.time.Instant

class Personas(
    private val ctx: PackageContext<AdminExportCfg>,
    private val writer: TabularWriter,
    private val glossaryMap: Map<String, String>,
    private val connectionMap: Map<String, AssetResolver.ConnectionIdentity>,
    private val logger: KLogger,
) {
    fun export() {
        logger.info { "Exporting all personas..." }
        writer.writeHeader(
            mapOf(
                "Persona name" to "",
                "Description" to "",
                "Users" to "Users assigned to this persona",
                "Groups" to "Groups assigned to this persona",
                "Metadata policies" to "",
                "Data policies" to "",
                "Glossary policies" to "",
                "Domain policies" to "",
                "Connections" to "Connections controlled by the policies on this persona",
                "Glossaries" to "Glossaries controlled by the policies on this persona",
                "Domains" to "Domains controlled by the policies on this persona",
                "Navigation" to "Default landing page",
                "Hide asset types" to "Asset types that should be hidden",
                "Hide sidebar tabs" to "Sidebar tabs that should be hidden",
                "Hide asset filters" to "Asset search filters that should be hidden",
                "Hide asset metadata" to "Asset metadata that should be hidden",
                "Display preferences" to "Specific preferences on displaying assets",
                "Custom metadata to hide" to "Custom metadata tabs that should be hidden",
                "Extracted on" to "Date and time when the persona was extracted",
            ),
        )
        val ts = Instant.now().toString()
        Persona
            .select(ctx.client)
            .includeOnResults(Persona.NAME)
            .includeOnResults(Persona.DESCRIPTION)
            .includeOnResults(Persona.PERSONA_USERS)
            .includeOnResults(Persona.PERSONA_GROUPS)
            .includeOnResults(Persona.POLICIES)
            .includeOnResults(Persona.DEFAULT_NAVIGATION)
            .includeOnResults(Persona.DENY_ASSET_FILTERS)
            .includeOnResults(Persona.DENY_ASSET_METADATA_TYPES)
            .includeOnResults(Persona.DENY_ASSET_TABS)
            .includeOnResults(Persona.DENY_ASSET_TYPES)
            .includeOnResults(Persona.DENY_CUSTOM_METADATA_GUIDS)
            .includeOnResults(Persona.DENY_NAVIGATION_PAGES)
            .includeOnResults(Persona.DISPLAY_PREFERENCES)
            .includeOnRelations(AuthPolicy.NAME)
            .includeOnRelations(AuthPolicy.POLICY_TYPE)
            .includeOnRelations(AuthPolicy.POLICY_SUB_CATEGORY)
            .includeOnRelations(AuthPolicy.CONNECTION_QUALIFIED_NAME)
            .includeOnRelations(AuthPolicy.POLICY_RESOURCES)
            .stream()
            .forEach { persona ->
                persona as Persona
                var metadataPolicyCount = 0
                var dataPolicyCount = 0
                var glossaryPolicyCount = 0
                var domainPolicyCount = 0
                val connections = mutableSetOf<AssetResolver.ConnectionIdentity>()
                val glossaries = mutableSetOf<String>()
                val domains = mutableSetOf<String>()
                val denyCustomMetadata = Preferences.getCustomMetadataToDeny(ctx, "persona", persona.denyCustomMetadataGuids, logger)
                persona.policies.forEach { policy ->
                    when (policy.policySubCategory) {
                        "metadata" -> {
                            metadataPolicyCount += 1
                            if (connectionMap.containsKey(policy.connectionQualifiedName)) {
                                connections.add(connectionMap[policy.connectionQualifiedName]!!)
                            }
                        }

                        "data" -> {
                            dataPolicyCount += 1
                            if (connectionMap.containsKey(policy.connectionQualifiedName)) {
                                connections.add(connectionMap[policy.connectionQualifiedName]!!)
                            }
                        }

                        "glossary" -> {
                            glossaryPolicyCount += 1
                            policy.policyResources.forEach { resource ->
                                val glossaryQN = resource.substringAfter("entity:")
                                glossaries.add(glossaryMap.getOrDefault(glossaryQN, glossaryQN))
                            }
                        }

                        "domain" -> {
                            domainPolicyCount += 1
                            policy.policyResources.forEach { resource ->
                                domains.add(resource.substringAfter("entity:"))
                            }
                        }
                    }
                }
                writer.writeRecord(
                    listOf(
                        persona.name,
                        persona.description,
                        persona.personaUsers.joinToString("\n"),
                        persona.personaGroups.joinToString("\n"),
                        metadataPolicyCount,
                        dataPolicyCount,
                        glossaryPolicyCount,
                        domainPolicyCount,
                        connections.joinToString("\n"),
                        glossaries.joinToString("\n"),
                        domains.joinToString("\n"),
                        persona.defaultNavigation,
                        persona.denyAssetTypes.joinToString("\n"),
                        persona.denyAssetTabs.joinToString("\n") { it.value },
                        persona.denyAssetFilters.joinToString("\n") { it.value },
                        persona.denyAssetMetadataTypes.joinToString("\n"),
                        persona.displayPreferences.joinToString("\n"),
                        denyCustomMetadata.joinToString("\n"),
                        ts,
                    ),
                )
            }
    }
}
