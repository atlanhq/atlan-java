/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.model.assets.AuthPolicy
import com.atlan.model.assets.Persona
import com.atlan.model.assets.Purpose
import com.atlan.pkg.Utils
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

        val exportFile = "$outputDirectory${File.separator}admin-export.xlsx"
        ExcelWriter(exportFile).use { xlsx ->
            objectsToInclude.forEach { objectName ->
                when (objectName) {
                    "users" -> exportUsers(xlsx)
                    "groups" -> exportGroups(xlsx)
                    "personas" -> exportPersonas(xlsx)
                    "purposes" -> exportPurposes(xlsx)
                    "policies" -> exportPolicies(xlsx, includeNativePolicies)
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
            ),
        )
        Atlan.getDefaultClient().users.list().forEach { user ->
            xlsx.appendRow(
                sheet,
                listOf(
                    user.username,
                    user.firstName,
                    user.lastName,
                    user.email,
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
                "Group name" to "",
                "Number of users" to "",
            ),
        )
        Atlan.getDefaultClient().groups.list().forEach { group ->
            xlsx.appendRow(
                sheet,
                listOf(
                    group.name,
                    group.userCount,
                ),
            )
        }
    }

    fun exportPersonas(xlsx: ExcelWriter) {
        logger.info { "Exporting all personas..." }
        val sheet = xlsx.createSheet("Personas")
        xlsx.addHeader(
            sheet,
            mapOf(
                "Persona name" to "",
                "Description" to "",
                "Users" to "Users assigned to this persona",
                "Groups" to "Groups assigned to this persona",
            ),
        )
        Persona.select()
            .includeOnResults(Persona.NAME)
            .includeOnResults(Persona.DESCRIPTION)
            .includeOnResults(Persona.PERSONA_USERS)
            .includeOnResults(Persona.PERSONA_GROUPS)
            .stream()
            .forEach { persona ->
                persona as Persona
                xlsx.appendRow(
                    sheet,
                    listOf(
                        persona.name,
                        persona.description,
                        persona.personaUsers,
                        persona.personaGroups,
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
            ),
        )
        Purpose.select()
            .includeOnResults(Purpose.NAME)
            .includeOnResults(Purpose.DESCRIPTION)
            .includeOnResults(Purpose.PURPOSE_ATLAN_TAGS)
            .stream()
            .forEach { purpose ->
                purpose as Purpose
                xlsx.appendRow(
                    sheet,
                    listOf(
                        purpose.name,
                        purpose.description,
                        purpose.purposeAtlanTags,
                    ),
                )
            }
    }

    fun exportPolicies(xlsx: ExcelWriter, includeNative: Boolean) {
        logger.info { "Exporting policies, ${ if (includeNative) "including" else "excluding" } out-of-the-box..." }
        val sheet = xlsx.createSheet("Policies")
        xlsx.addHeader(
            sheet,
            mapOf(
                "Policy name" to "",
                "Description" to "",
                "Parent type" to "Type of parent access control mechanism that owns the policy",
                "Parent name" to "Name of parent access control mechanism that owns the policy",
                "Type" to "Type of the policy",
                "Resources" to "Resources the policy controls",
            ),
        )
        AuthPolicy.select()
            .includeOnResults(AuthPolicy.NAME)
            .includeOnResults(AuthPolicy.DESCRIPTION)
            .includeOnResults(AuthPolicy.ACCESS_CONTROL)
            .includeOnResults(AuthPolicy.POLICY_RESOURCES)
            .includeOnResults(AuthPolicy.POLICY_TYPE)
            .includeOnRelations(Asset.NAME)
            .stream()
            .forEach { policy ->
                policy as AuthPolicy
                if (policy.accessControl != null || includeNative) {
                    xlsx.appendRow(
                        sheet,
                        listOf(
                            policy.name,
                            policy.description,
                            policy.accessControl?.typeName ?: "",
                            policy.accessControl?.name ?: "",
                            policy.policyType,
                            policy.policyResources,
                        ),
                    )
                }
            }
    }
}
