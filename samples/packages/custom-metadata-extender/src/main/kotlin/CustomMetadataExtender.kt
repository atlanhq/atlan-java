/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.exception.AtlanException
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Glossary
import com.atlan.model.typedefs.AttributeDef
import com.atlan.pkg.Utils
import mu.KotlinLogging
import kotlin.system.exitProcess

object CustomMetadataExtender {
    private val logger = KotlinLogging.logger {}

    /**
     * Actually run the logic to extend the custom metadata to additional connections and / or glossaries.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val config = Utils.setPackageOps<CustomMetadataExtenderCfg>()

        val cmName = Utils.getOrDefault(config.customMetadata, "")
        val connectionQNs = Utils.getOrDefault(config.connectionQualifiedName, listOf())
        val glossaryNames = Utils.getAsList(config.glossaries)

        if (cmName.isBlank()) {
            logger.error { "Missing required parameter - you must specify the name of the custom metadata to extend." }
            exitProcess(3)
        }

        if (connectionQNs.isEmpty() && glossaryNames.isEmpty()) {
            logger.error { "Missing required parameter - you must provide AT LEAST some additional connections or additional glossaries." }
            exitProcess(4)
        }

        extendCM(cmName, connectionQNs, glossaryNames)
    }

    /**
     * Look up the qualifiedNames of all glossary names provided.
     *
     * @param glossaryNames simple names of glossaries
     * @return list of corresponding qualifiedNames for the provided glossaries
     */
    fun getGlossaryQualifiedNames(glossaryNames: List<String>): List<String> {
        val list = mutableListOf<String>()
        glossaryNames.forEach { gn ->
            try {
                val glossary = Glossary.findByName(gn)
                list.add(glossary.qualifiedName)
            } catch (e: NotFoundException) {
                logger.warn { "Unable to find glossary '$gn' - skipping..." }
            } catch (e: AtlanException) {
                logger.error(e) { "Problem while trying to find glossary: $gn" }
            }
        }
        return list
    }

    /**
     * Actually add the token as a connection admin, appending it to any pre-existing
     * connection admins (rather than replacing).
     *
     * @param cmName human-readable name of the custom metadata to extend
     * @param connectionQNs list of qualifiedNames of connections to add to the custom metadata
     * @param glossaryNames list of names of glossaries to add to the custom metadata
     */
    fun extendCM(cmName: String, connectionQNs: List<String>, glossaryNames: List<String>) {
        logger.info { "Extending custom metadata $cmName with connections: $connectionQNs" }
        val glossaryQNs = getGlossaryQualifiedNames(glossaryNames)
        logger.info { "Extending custom metadata $cmName with glossaries: $glossaryQNs" }
        val cm = Atlan.getDefaultClient().customMetadataCache.getCustomMetadataDef(cmName)
        if (cm == null) {
            logger.error { "Unable to find custom metadata with name: $cmName" }
        } else {
            val attrs = mutableListOf<AttributeDef>()
            cm.attributeDefs.forEach { attr ->
                val options = attr.options.toBuilder()
                    .applicableConnections(connectionQNs)
                    .applicableGlossaries(glossaryQNs)
                    .build()
                attrs.add(
                    attr.toBuilder()
                        .options(options)
                        .build(),
                )
            }
            val revised = cm.toBuilder()
                .clearAttributeDefs()
                .attributeDefs(attrs)
                .build()
            try {
                revised.update()
            } catch (e: AtlanException) {
                logger.error(e) { "Problem while updating $cmName" }
            }
        }
    }
}
