/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.AtlanClient
import com.atlan.exception.AtlanException
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.Glossary
import com.atlan.model.typedefs.AttributeDef
import com.atlan.model.typedefs.AttributeDefOptions
import com.atlan.pkg.Utils
import kotlin.system.exitProcess

object CustomMetadataExtender {
    private val logger = Utils.getLogger(CustomMetadataExtender.javaClass.name)

    /**
     * Actually run the logic to extend the custom metadata to additional connections and / or glossaries.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        Utils.initializeContext<CustomMetadataExtenderCfg>().use { ctx ->

            val cmName = ctx.config.customMetadata
            val connectionQNs = ctx.config.connectionQualifiedName
            val glossaryNames = Utils.getAsList(ctx.config.glossaries)
            val domains = ctx.config.domains
            val domainName = ctx.config.domainsSpecific

            if (cmName.isBlank()) {
                logger.error { "Missing required parameter - you must specify the name of the custom metadata to extend." }
                exitProcess(3)
            }

            if (connectionQNs.isEmpty() && glossaryNames.isEmpty() && (domains == "NONE" || (domains == "SOME" && domainName.isEmpty()))) {
                logger.error { "Missing required parameter - you must provide AT LEAST some additional connections, additional glossaries, or an additional domain." }
                exitProcess(4)
            }

            extendCM(ctx.client, cmName, connectionQNs, glossaryNames, domains, domainName)
        }
    }

    /**
     * Look up the qualifiedNames of all glossary names provided.
     *
     * @param client connectivity to the Atlan tenant
     * @param glossaryNames simple names of glossaries
     * @return list of corresponding qualifiedNames for the provided glossaries
     */
    fun getGlossaryQualifiedNames(
        client: AtlanClient,
        glossaryNames: List<String>,
    ): List<String> {
        val list = mutableListOf<String>()
        glossaryNames.forEach { gn ->
            try {
                val glossary = Glossary.findByName(client, gn)
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
     * @param client connectivity to the Atlan tenant
     * @param cmName human-readable name of the custom metadata to extend
     * @param connectionQNs list of qualifiedNames of connections to add to the custom metadata
     * @param glossaryNames list of names of glossaries to add to the custom metadata
     * @param domains whether to extend to all, some, or no domains
     * @param domainName name of a single domain to extend to (if domains is some)
     */
    fun extendCM(
        client: AtlanClient,
        cmName: String,
        connectionQNs: List<String>,
        glossaryNames: List<String>,
        domains: String,
        domainName: String,
    ) {
        logger.info { "Extending custom metadata $cmName with connections: $connectionQNs" }
        val glossaryQNs = getGlossaryQualifiedNames(client, glossaryNames)
        logger.info { "Extending custom metadata $cmName with glossaries: $glossaryQNs" }
        val domainQNs = mutableSetOf<String>()
        when (domains) {
            "ALL" -> {
                logger.info { "Extending custom metadata to all domains." }
                domainQNs.addAll(AttributeDefOptions.ALL_DOMAINS)
            }

            "SOME" -> {
                try {
                    val found = DataDomain.findByName(client, domainName)
                    if (found.size > 1) {
                        logger.warn { "Found multiple domains with the name $domainName, taking only the first" }
                    }
                    domainQNs.add(found[0].qualifiedName)
                } catch (e: NotFoundException) {
                    logger.warn { "Unable to find domain with name $domainName -- skipping." }
                } catch (e: AtlanException) {
                    logger.error(e) { "Error attempting to lookup domain with name $domainName" }
                }
            }

            else -> {
                logger.info { "Not extending custom metadata to any additional domains." }
            }
        }
        val cm = client.customMetadataCache.getByName(cmName)
        if (cm == null) {
            logger.error { "Unable to find custom metadata with name: $cmName" }
        } else {
            val attrs = mutableListOf<AttributeDef>()
            cm.attributeDefs.forEach { attr ->
                val options =
                    attr.options
                        .toBuilder()
                        .applicableConnections(connectionQNs)
                        .applicableGlossaries(glossaryQNs)
                        .applicableDomains(domainQNs)
                        .build()
                attrs.add(
                    attr
                        .toBuilder()
                        .options(options)
                        .build(),
                )
            }
            val revised =
                cm
                    .toBuilder()
                    .clearAttributeDefs()
                    .attributeDefs(attrs)
                    .build()
            try {
                revised.update(client)
            } catch (e: AtlanException) {
                logger.error(e) { "Problem while updating $cmName" }
            }
        }
    }
}
