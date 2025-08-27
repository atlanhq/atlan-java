/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataStudioAsset
import com.atlan.model.assets.LookerDashboard
import com.atlan.model.assets.MetabaseDashboard
import com.atlan.model.assets.MicroStrategyReport
import com.atlan.model.assets.ModeReport
import com.atlan.model.assets.PowerBIDashboard
import com.atlan.model.assets.PresetDashboard
import com.atlan.model.assets.QlikChart
import com.atlan.model.assets.QuickSightDashboard
import com.atlan.model.assets.RedashDashboard
import com.atlan.model.assets.SigmaPage
import com.atlan.model.assets.SisenseDashboard
import com.atlan.model.assets.TableauDashboard
import com.atlan.model.assets.ThoughtspotLiveboard
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import mu.KLogger

class DLA(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "DLA - Dashboard-Level Assets",
        "DLA",
        "**Total active BI dashboard-level assets in Atlan.** This is useful to:\n" +
            "- Monitor the footprint of the business intelligence side of your data ecosystem\n" +
            "- Monitor Atlan's rollout across your business intelligence ecosystem\n" +
            "- Provide the basis for calculating a percentage of assets from other metrics relative to the overall footprint of your business intelligence ecosystem",
        Reporter.CAT_HEADLINES,
        client,
        batchSize,
        logger,
    ) {
    companion object {
        val DASHBOARD_LEVEL =
            setOf(
                DataStudioAsset.TYPE_NAME,
                LookerDashboard.TYPE_NAME,
                MetabaseDashboard.TYPE_NAME,
                MicroStrategyReport.TYPE_NAME,
                ModeReport.TYPE_NAME,
                PowerBIDashboard.TYPE_NAME,
                PresetDashboard.TYPE_NAME,
                QlikChart.TYPE_NAME,
                QuickSightDashboard.TYPE_NAME,
                RedashDashboard.TYPE_NAME,
                SigmaPage.TYPE_NAME,
                SisenseDashboard.TYPE_NAME,
                TableauDashboard.TYPE_NAME,
                ThoughtspotLiveboard.TYPE_NAME,
            )
        val ATTRIBUTES =
            setOf(
                Asset.CONNECTOR_TYPE,
                Asset.NAME,
                LookerDashboard.FOLDER,
                MetabaseDashboard.METABASE_COLLECTION,
                MicroStrategyReport.MICRO_STRATEGY_PROJECT,
                ModeReport.MODE_WORKSPACE_NAME,
                PowerBIDashboard.WORKSPACE,
                PresetDashboard.PRESET_WORKSPACE,
                QlikChart.QLIK_SPACE_QUALIFIED_NAME,
                QuickSightDashboard.QUICK_SIGHT_SHEET_NAME,
                SigmaPage.SIGMA_WORKBOOK,
                SisenseDashboard.SISENSE_FOLDER,
                TableauDashboard.PROJECT_QUALIFIED_NAME,
            )
    }

    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        client.assets
            .select()
            .where(Asset.TYPE_NAME.`in`(DASHBOARD_LEVEL))
            .pageSize(batchSize)
}
