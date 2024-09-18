/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package model

import com.atlan.model.assets.Badge
import com.atlan.model.assets.Connection
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.AtlanTagColor
import com.atlan.model.enums.BadgeComparisonOperator
import com.atlan.model.enums.BadgeConditionColor
import com.atlan.model.structs.BadgeCondition
import com.atlan.model.typedefs.AttributeDef
import com.atlan.model.typedefs.AttributeDefOptions
import com.atlan.model.typedefs.CustomMetadataDef
import com.atlan.model.typedefs.CustomMetadataOptions

object AEFCustomMetadata {
    const val SET_NAME = "DetectiData Table Tracker"

    lateinit var badge: Badge

    private val limitApplicability =
        AttributeDefOptions.builder()
            .applicableConnections(Connection.getAllQualifiedNames())
            .applicableAssetType(Table.TYPE_NAME)
            .applicableAssetType(View.TYPE_NAME)
            .applicableAssetType(MaterializedView.TYPE_NAME)
            .build()

    private val attributes =
        listOf<AttributeDef>(
            AttributeDef.of(
                "Trust Score",
                AtlanCustomAttributePrimitiveType.DECIMAL,
                false,
                limitApplicability.toBuilder().showInOverview(true).build(),
            ).toBuilder().description("Score (out of 100) indicating the overall trustworthiness of the asset.").build(),
            AttributeDef.of(
                "Accuracy",
                AtlanCustomAttributePrimitiveType.DECIMAL,
                false,
                limitApplicability,
            ).toBuilder().description("Score (out of 100) indicating how well the data reflects reality.").build(),
            AttributeDef.of(
                "Completeness",
                AtlanCustomAttributePrimitiveType.DECIMAL,
                false,
                limitApplicability,
            ).toBuilder().description("Score (out of 100) indicating whether all the data required for the asset's use is present and available to be used.").build(),
            AttributeDef.of(
                "Uniqueness",
                AtlanCustomAttributePrimitiveType.DECIMAL,
                false,
                limitApplicability,
            ).toBuilder().description("Percentage of non-duplicated records in the asset.").build(),
            AttributeDef.of(
                "Validity",
                AtlanCustomAttributePrimitiveType.DECIMAL,
                false,
                limitApplicability,
            ).toBuilder().description("Percentage of records in the asset that conform to the expected format, type, and range.").build(),
        )

    /* Other attributes:
    AttributeDef.of(
            "Consistency",
            AtlanCustomAttributePrimitiveType.DECIMAL,
            false,
            limitApplicability,
        ).toBuilder().description("Percentage of non-conflicting records across data sets.").build(),
    AttributeDef.of(
        "Timeliness",
        AtlanCustomAttributePrimitiveType.DECIMAL,
        false,
        limitApplicability,
    ).toBuilder().description("Score (out of 100) indicating the freshness of the data against expectations.").build(),*/

    fun buildDefinition(): CustomMetadataDef {
        val builder =
            CustomMetadataDef.creator(SET_NAME)
                .description("Table Tracker details from DetectiData scans.")
                .options(CustomMetadataOptions.withIcon(AtlanIcon.DETECTIVE, AtlanTagColor.GRAY, true))
        attributes.forEach {
            builder.attributeDef(it)
        }
        return builder.build()
    }

    fun create(): CustomMetadataDef {
        val cmDef = buildDefinition().create()
        createBadge()
        return cmDef
    }

    private fun createBadge() {
        val toCreate =
            Badge.creator("TTTS", SET_NAME, "Trust Score")
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.GTE, 80.0, BadgeConditionColor.GREEN))
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.GTE, 50.0, BadgeConditionColor.YELLOW))
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.LT, 50.0, BadgeConditionColor.RED))
                .build()
        val response = toCreate.save()
        badge = response.getResult(toCreate)
    }
}
