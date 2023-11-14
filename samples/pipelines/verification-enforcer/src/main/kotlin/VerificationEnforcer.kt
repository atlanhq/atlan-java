/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.AtlanClient
import com.atlan.events.AtlanEventHandler
import com.atlan.exception.AtlanException
import com.atlan.exception.ErrorCode
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.ICatalog
import com.atlan.model.enums.CertificateStatus
import com.atlan.pkg.events.AbstractNumaflowHandler
import com.atlan.pkg.events.EventUtils
import com.atlan.pkg.events.config.EventConfig
import com.atlan.pkg.serde.MultiSelectDeserializer
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.slf4j.Logger

/**
 * Ensures any asset marked VERIFIED meets a minimum standard,
 * or if not it is automatically reverted to DRAFT.
 */
object VerificationEnforcer : AbstractNumaflowHandler(Handler) {

    const val DEFAULT_ENFORCEMENT_MESSAGE = "To be verified, an asset must have a description, at least one owner, and lineage."
    private lateinit var config: Cfg

    @JvmStatic
    fun main(args: Array<String>) {
        val configCandidate = EventUtils.setEventOps<Cfg>()
        if (configCandidate != null) {
            config = configCandidate
            EventUtils.useApiToken(config.apiTokenId)
            EventUtils.startHandler(this)
        }
    }

    /**
     * Expected configuration for the event processing.
     */
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    data class Cfg(
        @JsonDeserialize(using = MultiSelectDeserializer::class)
        @JsonProperty("must-haves") val mustHaves: List<String>?,
        @JsonProperty("enforcement-message") val enforcementMessage: String?,
        @JsonDeserialize(using = MultiSelectDeserializer::class)
        @JsonProperty("asset-types") val assetTypes: List<String>?,
        @JsonProperty("api-token") val apiTokenId: String?,
    ) : EventConfig()

    /**
     * Logic for the event processing.
     */
    object Handler : AtlanEventHandler {

        private val REQUIRED_ATTRS = setOf(
            Asset.CERTIFICATE_STATUS.atlanFieldName,
            Asset.DESCRIPTION.atlanFieldName,
            Asset.USER_DESCRIPTION.atlanFieldName,
            Asset.OWNER_USERS.atlanFieldName,
            Asset.OWNER_GROUPS.atlanFieldName,
            Asset.HAS_LINEAGE.atlanFieldName,
            Asset.README.atlanFieldName,
            Asset.ASSIGNED_TERMS.atlanFieldName,
            Asset.ATLAN_TAGS.atlanFieldName,
            ICatalog.INPUT_TO_PROCESSES.atlanFieldName,
            ICatalog.OUTPUT_FROM_PROCESSES.atlanFieldName,
        )

        private lateinit var MUST_HAVES: List<String>
        private lateinit var ASSET_TYPES: List<String>
        private lateinit var ENFORCEMENT_MESSAGE: String

        private fun setup() {
            MUST_HAVES = config.mustHaves ?: listOf()
            ASSET_TYPES = config.assetTypes ?: listOf()
            ENFORCEMENT_MESSAGE = config.enforcementMessage ?: DEFAULT_ENFORCEMENT_MESSAGE
        }

        // Note: we can just re-use the default validatePrerequisites

        /** {@inheritDoc}  */
        @Throws(AtlanException::class)
        override fun getCurrentState(client: AtlanClient, fromEvent: Asset, logger: Logger): Asset? {
            setup()
            val includeTerms = MUST_HAVES.contains("term")
            val includeTags = MUST_HAVES.contains("tag")
            return if (fromEvent.typeName in ASSET_TYPES) {
                AtlanEventHandler.getCurrentViewOfAsset(
                    client,
                    fromEvent,
                    REQUIRED_ATTRS,
                    includeTerms,
                    includeTags,
                )
                    ?: throw NotFoundException(
                        ErrorCode.ASSET_NOT_FOUND_BY_QN, fromEvent.qualifiedName, fromEvent.typeName,
                    )
            } else {
                logger.info(
                    "Skipped checking asset of {}, not configured to check these assets: {}",
                    fromEvent.typeName,
                    fromEvent.qualifiedName,
                )
                null
            }
        }

        /** {@inheritDoc}  */
        @Throws(AtlanException::class)
        override fun calculateChanges(asset: Asset?, logger: Logger): Collection<Asset> {
            // We only need to consider enforcement if the asset is currently verified
            if (asset != null) {
                if (asset.certificateStatus == CertificateStatus.VERIFIED) {
                    if (!valid(asset)) {
                        logger.info(
                            "Asset missing some required information to be verified, reverting to DRAFT: {}",
                            asset.qualifiedName,
                        )
                        return setOf(
                            asset.trimToRequired()
                                .certificateStatus(CertificateStatus.DRAFT)
                                .certificateStatusMessage(ENFORCEMENT_MESSAGE)
                                .build(),
                        )
                    } else {
                        logger.info(
                            "Asset has all required information present to be verified, no enforcement required: {}",
                            asset.qualifiedName,
                        )
                    }
                } else {
                    logger.info(
                        "Asset is no longer verified, no enforcement action to consider: {}",
                        asset.qualifiedName,
                    )
                }
            }
            return emptySet()
        }

        // Note: we can just re-use the default hasChanges
        // Note: can reuse default saveChanges

        /**
         * Determine whether the provided asset is valid, according to the criteria supplied
         * in the configuration of the pipeline.
         *
         * @param asset to validate
         */
        private fun valid(asset: Asset): Boolean {
            var overallValid = true
            for (next in MUST_HAVES) {
                overallValid = overallValid && when (next) {
                    "description" -> AtlanEventHandler.hasDescription(asset)
                    "owner" -> AtlanEventHandler.hasOwner(asset)
                    "readme" -> AtlanEventHandler.hasReadme(asset)
                    "tag" -> AtlanEventHandler.hasAtlanTags(asset)
                    // Only check these last two on non-glossary asset types
                    "lineage" -> !asset.typeName.startsWith("AtlasGlossary") && AtlanEventHandler.hasLineage(asset)
                    "term" -> !asset.typeName.startsWith("AtlasGlossary") && AtlanEventHandler.hasAssignedTerms(asset)
                    else -> false
                }
            }
            return overallValid
        }
    }
}
