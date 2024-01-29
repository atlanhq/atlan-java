/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.exception.AtlanException
import com.atlan.exception.ConflictException
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Badge
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.AtlanTagColor
import com.atlan.model.enums.BadgeComparisonOperator
import com.atlan.model.enums.BadgeConditionColor
import com.atlan.model.structs.BadgeCondition
import com.atlan.model.typedefs.AttributeDef
import com.atlan.model.typedefs.CustomMetadataDef
import com.atlan.model.typedefs.CustomMetadataOptions
import com.atlan.pkg.Utils
import com.atlan.pkg.events.EventUtils
import com.atlan.pkg.events.WriteConfig
import mu.KotlinLogging
import kotlin.system.exitProcess

/**
 * Creates the custom metadata needed to capture the asset scores,
 * in addition to writing out the configuration for the pipeline.
 */
object CreateCMWriteConfig {

    private val logger = KotlinLogging.logger {}

    const val CM_SCORING = "Scorecard"
    const val CM_ATTR_COMPOSITE_SCORE = "Overall"

    @JvmStatic
    fun main(args: Array<String>) {
        EventUtils.setLogging()
        val config = Utils.parseConfig<AssetScorerCfg>(
            Utils.getEnvVar("NESTED_CONFIG", ""),
            Utils.buildRuntimeConfig(),
        )
        Utils.setClient()
        Utils.setWorkflowOpts(config.runtime)
        createCMIfNotExists(config)
        WriteConfig.main(args)
    }

    /**
     * Check if the custom metadata already exists, and if so simply return.
     * If not, go ahead and create the custom metadata structure and an associated badge.
     */
    private fun createCMIfNotExists(config: AssetScorerCfg) {
        try {
            Atlan.getDefaultClient().customMetadataCache.getIdForName(CM_SCORING)
        } catch (e: NotFoundException) {
            logger.info { "Creating scorecard custom metadata $CM_SCORING.$CM_ATTR_COMPOSITE_SCORE" }
            try {
                val initialScore = AttributeDef.of(
                    CM_ATTR_COMPOSITE_SCORE,
                    AtlanCustomAttributePrimitiveType.DECIMAL,
                    null,
                    false,
                )
                    .toBuilder()
                    .description("Overall composite score for the asset, based on sum of all of its component scores.")
                    .build()
                val overallScore = if (config.assetTypes != null) {
                    initialScore.toBuilder().options(
                        initialScore.options.toBuilder()
                            .applicableAssetTypes(config.assetTypes.toSet())
                            .build(),
                    )
                        .build()
                } else {
                    initialScore
                }
                val customMetadataDef = CustomMetadataDef.creator(CM_SCORING)
                    .attributeDef(overallScore)
                    .description("Scoring for this asset based on how much of its context is populated.")
                    .options(CustomMetadataOptions.withIcon(AtlanIcon.GAUGE, AtlanTagColor.GRAY, true))
                    .build()
                customMetadataDef.create()
                logger.info { "Created $CM_SCORING custom metadata structure." }
                val badge = Badge.creator(CM_ATTR_COMPOSITE_SCORE, CM_SCORING, CM_ATTR_COMPOSITE_SCORE)
                    .userDescription(
                        "Overall asset score. Indicates how enriched and ready for re-use this asset is, out of a total possible score of 10.",
                    )
                    .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.GTE, "3.5", BadgeConditionColor.GREEN))
                    .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.LT, "3.5", BadgeConditionColor.YELLOW))
                    .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.LTE, "2.5", BadgeConditionColor.RED))
                    .build()
                try {
                    badge.save()
                    logger.info { "Created $CM_SCORING badge." }
                } catch (eBadge: AtlanException) {
                    logger.error("Unable to create badge over {}.{}.", CM_SCORING, CM_ATTR_COMPOSITE_SCORE, eBadge)
                    exitProcess(1)
                }
            } catch (conflict: ConflictException) {
                // Handle cross-thread race condition that the typedef has since been created
                try {
                    Atlan.getDefaultClient().customMetadataCache.getIdForName(CM_SCORING)
                } catch (eConflict: AtlanException) {
                    logger.error(
                        "Unable to look up {} custom metadata, even though it should already exist.",
                        CM_SCORING,
                        eConflict,
                    )
                    exitProcess(2)
                }
            } catch (eStruct: AtlanException) {
                logger.error("Unable to create {} custom metadata structure.", CM_SCORING, eStruct)
                exitProcess(3)
            }
        } catch (e: AtlanException) {
            logger.error("Unable to look up {} custom metadata.", CM_SCORING, e)
            exitProcess(4)
        }
    }
}
