/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.model.assets.AuthPolicy
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.Persona
import com.atlan.model.assets.Purpose
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.cell.TimestampXformer
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KotlinLogging
import java.io.File

/**
 * Actually run the export of admin objects.
 */
object AdminExporter {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<AdminExportCfg>()

        val objectsToInclude = Utils.getOrDefault(config.objectsToInclude, listOf("users", "groups"))
        val includeNativePolicies = Utils.getOrDefault(config.includeNativePolicies, false)

        // Before we start processing, will pre-cache all glossaries,
        // so we can resolve them to meaningful names
        val glossaryMap = preloadGlossaryNameMap()
        val connectionMap = preloadConnectionMap()

        val exportFile = "$outputDirectory${File.separator}admin-export.xlsx"
        ExcelWriter(exportFile).use { xlsx ->
            objectsToInclude.forEach { objectName ->
                when (objectName) {
                    "users" -> exportUsers(xlsx)
                    "groups" -> exportGroups(xlsx)
                    "personas" -> exportPersonas(xlsx, glossaryMap, connectionMap)
                    "purposes" -> exportPurposes(xlsx)
                    "policies" -> exportPolicies(xlsx, includeNativePolicies, glossaryMap, connectionMap)
                }
            }
        }
    }

    fun exportUsers(xlsx: ExcelWriter) {
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

    fun exportGroups(xlsx: ExcelWriter) {
        logger.info { "Exporting all groups..." }
        val sheet = xlsx.createSheet("Groups")
        xlsx.addHeader(
            sheet,
            mapOf(
                "Group name" to "Name of the group, as it appears in the UI",
                "Internal name" to "Name of the group, as it must be specified programmatically",
                "Number of users" to "",
                "Default" to "Whether the group will be assigned to newly invited users by default (true) or not (false)",
                "Created at" to "Date and time when the group was created",
                "Created by" to "User who created the group",
                "Updated at" to "Date and time when the group was last updated",
                "Updated by" to "User who last updated the group",
            ),
        )
        Atlan.getDefaultClient().groups.list().forEach { group ->
            val createdAt = group.attributes?.createdAt?.get(0)?.toLong() ?: -1
            val updatedAt = group.attributes?.updatedAt?.get(0)?.toLong() ?: -1
            xlsx.appendRow(
                sheet,
                listOf(
                    group.alias,
                    group.name,
                    group.userCount,
                    group.isDefault,
                    if (createdAt > 0) TimestampXformer.encode(createdAt) else "",
                    group.attributes?.createdBy?.get(0) ?: "",
                    if (updatedAt > 0) TimestampXformer.encode(updatedAt) else "",
                    group.attributes?.updatedBy?.get(0) ?: "",
                ),
            )
        }
    }

    fun exportPersonas(xlsx: ExcelWriter, glossaryMap: Map<String, String>, connectionMap: Map<String, ConnectionId>) {
        logger.info { "Exporting all personas..." }
        val sheet = xlsx.createSheet("Personas")
        xlsx.addHeader(
            sheet,
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
            ),
        )
        Persona.select()
            .includeOnResults(Persona.NAME)
            .includeOnResults(Persona.DESCRIPTION)
            .includeOnResults(Persona.PERSONA_USERS)
            .includeOnResults(Persona.PERSONA_GROUPS)
            .includeOnResults(Persona.POLICIES)
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
                val connections = mutableSetOf<ConnectionId>()
                val glossaries = mutableSetOf<String>()
                val domains = mutableSetOf<String>()
                persona.policies.forEach { policy ->
                    when (policy.policySubCategory) {
                        "metadata" -> {
                            metadataPolicyCount += 1
                            if (connectionMap.containsKey(policy.connectionQualifiedName))
                                connections.add(connectionMap[policy.connectionQualifiedName]!!)
                        }
                        "data" -> {
                            dataPolicyCount += 1
                            if (connectionMap.containsKey(policy.connectionQualifiedName))
                                connections.add(connectionMap[policy.connectionQualifiedName]!!)
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
                xlsx.appendRow(
                    sheet,
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
                    ),
                )
            }
    }

    fun exportPurposes(xlsx: ExcelWriter) {
        logger.info { "Exporting all purposes..." }
        val sheet = xlsx.createSheet("Purposes")
        xlsx.addHeader(
            sheet,
            mapOf(
                "Purpose name" to "",
                "Description" to "",
                "Tags" to "Tags controlled by this purpose",
                "Metadata policies" to "",
                "Data policies" to "",
                "Groups" to "Groups to which these policies are applied",
                "Users" to "Users to which these policies are applied",
            ),
        )
        Purpose.select()
            .includeOnResults(Purpose.NAME)
            .includeOnResults(Purpose.DESCRIPTION)
            .includeOnResults(Purpose.PURPOSE_ATLAN_TAGS)
            .includeOnResults(Purpose.POLICIES)
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
                xlsx.appendRow(
                    sheet,
                    listOf(
                        purpose.name,
                        purpose.description,
                        purpose.purposeAtlanTags.joinToString("\n"),
                        metadataPolicyCount,
                        dataPolicyCount,
                        groups.joinToString("\n"),
                        users.joinToString("\n"),
                    ),
                )
            }
    }

    fun exportPolicies(xlsx: ExcelWriter, includeNative: Boolean, glossaryMap: Map<String, String>, connectionMap: Map<String, ConnectionId>) {
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
                    val resources = if (policy.accessControl.name == "Purpose") {
                        policy.policyResources?.map {
                            client.atlanTagCache.getNameForId(it.substringAfter("tag:"))
                        }?.joinToString("\n") ?: ""
                    } else {
                        when (policy.policySubCategory) {
                            "metadata", "data", "domain" -> {
                                policy.policyResources?.joinToString("\n") {
                                    it.substringAfter("entity:")
                                } ?: ""
                            }

                            "glossary" -> {
                                policy.policyResources?.map {
                                    glossaryMap[it.substringAfter("entity:")]
                                }?.joinToString("\n") ?: ""
                            }

                            else -> ""
                        }
                    }
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

    private fun preloadGlossaryNameMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        Glossary.select()
            .pageSize(50)
            .stream()
            .forEach {
                map[it.qualifiedName] = it.name
            }
        return map
    }

    private fun preloadConnectionMap(): Map<String, ConnectionId> {
        val map = mutableMapOf<String, ConnectionId>()
        Connection.select()
            .pageSize(50)
            .includeOnResults(Connection.CONNECTOR_TYPE)
            .stream()
            .forEach {
                map[it.qualifiedName] = ConnectionId(it.connectorType, it.name)
            }
        return map
    }

    data class ConnectionId(val type: AtlanConnectorType, val name: String) {
        override fun toString(): String {
            return "$name (${type.value})"
        }
    }
}
