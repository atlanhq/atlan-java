/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.AtlanClient
import com.atlan.model.admin.ApiToken
import com.atlan.model.admin.AtlanUser
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Persona
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AuthPolicyType
import com.atlan.model.enums.PersonaMetadataAction
import com.atlan.pkg.Utils
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import mu.KotlinLogging
import java.nio.file.Paths
import kotlin.system.exitProcess

object FellowshipSetup {
    private val logger = KotlinLogging.logger {}

    private lateinit var client: AtlanClient
    private lateinit var roster: Fellowship.Roster
    private val SUPER_ADMINS = listOf("chris")

    /**
     * Actually run the logic to clean up test assets.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<FellowshipSetupCfg>()

        val rosterFilename = Utils.getOrDefault(config.roster, "")
        if (rosterFilename.isBlank()) {
            logger.error { "Missing required parameter - you must upload a file with the roster of scholars for in the Fellowship." }
            exitProcess(1)
        }

        client = Atlan.getDefaultClient()

        val rosterInput = Utils.getInputFile(rosterFilename, outputDirectory)
        roster = readRoster(rosterInput)

        inviteUsers()
        createConnections()
        createPersonas()
        createApiTokens()
        emailScholars(outputDirectory)
    }

    private fun readRoster(rosterInput: String): Fellowship.Roster {
        val scholars = mutableListOf<Fellowship.Scholar>()
        val inputFile = Paths.get(rosterInput)
        val builder =
            CsvReader.builder()
                .fieldSeparator(',')
                .quoteCharacter('"')
                .skipEmptyLines(true)
                .ignoreDifferentFieldCount(false)
        builder.ofCsvRecord(inputFile).use { reader ->
            reader.stream().skip(1).forEach { r: CsvRecord ->
                if (r.fieldCount >= 3 && !r.fields[0].isNullOrBlank() && !r.fields[1].isNullOrBlank() && !r.fields[2].isNullOrBlank()) {
                    scholars.add(
                        Fellowship.Scholar(
                            firstName = r.fields[0].trim(),
                            lastName = r.fields[1].trim(),
                            emailAddress = r.fields[2].trim(),
                        ),
                    )
                }
            }
        }
        return Fellowship.Roster(scholars.toSet())
    }

    private fun inviteUsers() {
        roster.scholars.forEach {
            logger.info { "Inviting user: ${it.emailAddress}" }
            AtlanUser.creator(it.emailAddress, "\$admin")
                .firstName(it.firstName)
                .lastName(it.lastName)
                .build()
                .create()
            val user = AtlanUser.getByEmail(it.emailAddress)!![0]
            Fellowship.users[it.id] = user
        }
    }

    private fun createConnections() {
        roster.scholars.forEach {
            logger.info { "Creating unique connection for user: ${it.emailAddress}" }
            val toCreate =
                Connection.creator(it.id, AtlanConnectorType.ICEBERG, null, null, SUPER_ADMINS)
                    .description("Connection to uniquely isolate assets for user ID: ${it.id} during the Atlan Engineering Fellowship.")
                    .build()
            val response = toCreate.save().block()
            val result = response.getResult(toCreate)
            Fellowship.connections[it.id] = result
        }
    }

    private fun createPersonas() {
        roster.scholars.forEach {
            logger.info { "Creating unique persona for user: ${it.emailAddress}" }
            val toCreate =
                Persona.creator(it.id)
                    .description("Access control for user ID ${it.id} during the Atlan Engineering Fellowship.")
                    .personaUsers(SUPER_ADMINS)
                    .personaUser(Fellowship.users[it.id]!!.username)
                    .build()
            val response = toCreate.save()
            val result = response.getResult(toCreate)
            Fellowship.personas[it.id] = result
            val connectionQN = Fellowship.connections[it.id]!!.qualifiedName
            Persona.createMetadataPolicy(
                "All assets for ${it.id}",
                result.guid,
                AuthPolicyType.ALLOW,
                PersonaMetadataAction.entries,
                connectionQN,
                listOf("entity:$connectionQN"),
            ).build().save()
        }
    }

    private fun createApiTokens() {
        roster.scholars.forEach {
            logger.info { "Creating unique API token for user: ${it.emailAddress}" }
            val token =
                ApiToken.create(
                    it.id,
                    "Access token for user ID ${it.id} during the Atlan Engineering Fellowship.",
                    setOf(Fellowship.personas[it.id]!!.qualifiedName),
                    -1L,
                )
            Fellowship.apiTokens[it.id] = token
        }
    }

    private fun emailScholars(directory: String) {
        roster.scholars.forEach {
            val tokenFile = Paths.get(directory, "${it.id}.txt").toFile()
            tokenFile.appendText(Fellowship.apiTokens[it.id]!!.attributes.accessToken)
            Utils.sendEmail(
                "[Atlan Engineering Fellowship] Onboarding",
                listOf(it.emailAddress),
                attachments = listOf(tokenFile),
                body = EmailBuilder.getPlain(it),
                html = EmailBuilder.getHTML(it),
            )
        }
    }
}
