/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import CreateCMWriteConfig.CM_ATTR_COMPOSITE_SCORE
import CreateCMWriteConfig.CM_SCORING
import com.atlan.Atlan
import com.atlan.AtlanClient
import com.atlan.events.AtlanEventHandler
import com.atlan.exception.AtlanException
import com.atlan.exception.ErrorCode
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.ICatalog
import com.atlan.model.core.CustomMetadataAttributes
import com.atlan.model.enums.CertificateStatus
import com.atlan.model.events.AtlanEvent
import com.atlan.pkg.Utils
import com.atlan.pkg.events.AbstractNumaflowHandler
import com.atlan.pkg.events.EventUtils
import org.slf4j.Logger
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Calculates an overall completeness score for an asset,
 * based on the configured metrics.
 */
object AssetScorer : AbstractNumaflowHandler(Handler) {

    private lateinit var config: AssetScorerCfg

    @JvmStatic
    fun main(args: Array<String>) {
        EventUtils.setEventOps<AssetScorerCfg>()?.let {
            config = it
            EventUtils.useApiToken(config.apiToken)
            EventUtils.startHandler(this)
        }
    }

    object Handler : AtlanEventHandler {

        private val SCORED_ATTRS = setOf(
            Asset.DESCRIPTION.atlanFieldName,
            Asset.USER_DESCRIPTION.atlanFieldName,
            Asset.OWNER_USERS.atlanFieldName,
            Asset.OWNER_GROUPS.atlanFieldName,
            Asset.ASSIGNED_TERMS.atlanFieldName,
            Asset.HAS_LINEAGE.atlanFieldName,
            Asset.README.atlanFieldName,
            Asset.ATLAN_TAGS.atlanFieldName,
            ICatalog.INPUT_TO_PROCESSES.atlanFieldName,
            ICatalog.OUTPUT_FROM_PROCESSES.atlanFieldName,
            GlossaryTerm.ASSIGNED_ENTITIES.atlanFieldName,
            GlossaryTerm.SEE_ALSO.atlanFieldName,
            GlossaryTerm.LINKS.atlanFieldName,
        )

        private lateinit var ASSET_TYPES: List<String>

        private fun setup() {
            ASSET_TYPES = Utils.getOrDefault(config.assetTypes, listOf())
        }

        /** {@inheritDoc}  */
        override fun validatePrerequisites(event: AtlanEvent, logger: Logger): Boolean {
            return Atlan.getDefaultClient().customMetadataCache.getIdForName(CM_SCORING) != null && event.payload?.asset != null
        }

        /** {@inheritDoc}  */
        @Throws(AtlanException::class)
        override fun getCurrentState(client: AtlanClient, fromEvent: Asset, logger: Logger): Asset? {
            setup()
            return if (fromEvent.typeName in ASSET_TYPES) {
                val searchAttrs = SCORED_ATTRS.toMutableSet()
                searchAttrs.addAll(client.customMetadataCache.getAttributesForSearchResults(CM_SCORING))
                return AtlanEventHandler.getCurrentViewOfAsset(client, fromEvent, searchAttrs, true, true)
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
            // Calculate the score
            val score: Double = if (asset is GlossaryTerm) {
                val sDescription = if (AtlanEventHandler.hasDescription(asset)) 15 else 0
                val sRelatedTerm = if (!asset.seeAlso.isNullOrEmpty()) 10 else 0
                val sLinks = if (!asset.links.isNullOrEmpty()) 10 else 0
                val sRelatedAsset = if (!asset.assignedEntities.isNullOrEmpty()) 20 else 0
                val sCertificate = when (asset.certificateStatus) {
                    CertificateStatus.DRAFT -> 15
                    CertificateStatus.VERIFIED -> 25
                    else -> 0
                }
                val sReadme = if (AtlanEventHandler.hasReadme(asset)) {
                    when {
                        (asset.readme.description.length > 1000) -> 20
                        (asset.readme.description.length > 500) -> 10
                        (asset.readme.description.length > 100) -> 5
                        else -> 0
                    }
                } else {
                    0
                }
                (sDescription + sRelatedTerm + sLinks + sRelatedAsset + sCertificate + sReadme).toDouble()
            } else if (asset != null && !asset.typeName.startsWith("AtlasGlossary")) {
                // We will not score glossaries or categories
                val sDescription = if (AtlanEventHandler.hasDescription(asset)) 20 else 0
                val sOwner = if (AtlanEventHandler.hasOwner(asset)) 20 else 0
                val sTerms = if (AtlanEventHandler.hasAssignedTerms(asset)) 20 else 0
                val sTags = if (AtlanEventHandler.hasAtlanTags(asset)) 20 else 0
                val sLineage = if (AtlanEventHandler.hasLineage(asset)) 20 else 0
                (sDescription + sOwner + sLineage + sTerms + sTags).toDouble()
            } else {
                -1.0
            }
            return if (score >= 0 && asset != null) {
                val cma = CustomMetadataAttributes.builder()
                    .attribute(CM_ATTR_COMPOSITE_SCORE, round(score / 20))
                    .build()
                val revised = asset.trimToRequired().customMetadata(CM_SCORING, cma).build()
                if (hasChanges(asset, revised, logger)) setOf(revised) else emptySet()
            } else {
                emptySet()
            }
        }

        /** {@inheritDoc}  */
        override fun hasChanges(original: Asset, modified: Asset, logger: Logger?): Boolean {
            val scoreOriginal = if (original.customMetadataSets?.containsKey(CM_SCORING) == true) {
                original.customMetadataSets[CM_SCORING]!!.attributes[CM_ATTR_COMPOSITE_SCORE] ?: -1
            } else {
                -1
            }
            val scoreModified = if (modified.customMetadataSets?.containsKey(CM_SCORING) == true) {
                modified.customMetadataSets[CM_SCORING]!!.attributes[CM_ATTR_COMPOSITE_SCORE] ?: -1
            } else {
                -1
            }
            return scoreOriginal != scoreModified
        }

        // Note: can reuse default saveChanges

        private fun round(number: Double): Double {
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }
    }
}
