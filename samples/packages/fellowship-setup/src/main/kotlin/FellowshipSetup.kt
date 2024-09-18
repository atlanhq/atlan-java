/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.AtlanClient
import com.atlan.model.admin.ApiToken
import com.atlan.model.admin.AtlanUser
import com.atlan.pkg.Utils
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import model.AEFConnection
import model.AEFCustomMetadata
import model.AEFPersona
import model.AEFRichText
import mu.KotlinLogging
import java.nio.file.Paths
import kotlin.system.exitProcess

object FellowshipSetup {
    private val logger = KotlinLogging.logger {}

    private lateinit var client: AtlanClient
    private lateinit var roster: Fellowship.Roster

    /**
     * Actually run the logic to clean up test assets.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<FellowshipSetupCfg>()

        val rosterFilename = Utils.getOrDefault(config.roster, "")
        val assetsFilename = Utils.getOrDefault(config.assets, "")
        if (rosterFilename.isBlank() || assetsFilename.isBlank()) {
            logger.error { "Missing required parameter - you must upload a file with the roster of scholars and assets to use for the Fellowship." }
            exitProcess(1)
        }

        client = Atlan.getDefaultClient()

        val rosterInput = Utils.getInputFile(rosterFilename, outputDirectory)
        val assetsInput = Utils.getInputFile(assetsFilename, outputDirectory)
        roster = readRoster(rosterInput)

        inviteUsers()
        createConnections(assetsInput, outputDirectory)
        AEFCustomMetadata.create() // NOTE: only do this AFTER connections exist, so it is available on all
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

    private fun createConnections(
        assetsInput: String,
        directory: String,
    ) {
        logger.info { "Creating reference connection." }
        val refFiles = AEFConnection.create(assetsInput)
        roster.scholars.forEach {
            logger.info { "Creating unique connection for user: ${it.emailAddress}" }
            val schFiles = AEFConnection.create(assetsInput, it)
        }
        // TODO: Combine the files
        AEFConnection.loadAssets(inputFiles, directory)
    }

    private fun createPersonas() {
        logger.info { "Creating reference persona." }
        AEFPersona.create()
        roster.scholars.forEach {
            logger.info { "Creating unique persona for user: ${it.emailAddress}" }
            AEFPersona.create(it)
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
                body = AEFRichText.getPlainTextEmail(it),
                html = AEFRichText.getHTMLEmail(it),
            )
        }
    }
}
