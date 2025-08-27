/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.assets.LookerDashboard
import com.atlan.model.assets.MetabaseDashboard
import com.atlan.model.assets.MicroStrategyReport
import com.atlan.model.assets.ModeReport
import com.atlan.model.assets.PowerBIDashboard
import com.atlan.model.assets.PresetDashboard
import com.atlan.model.assets.QlikChart
import com.atlan.model.assets.QuickSightDashboard
import com.atlan.model.assets.SigmaPage
import com.atlan.model.assets.SisenseDashboard
import com.atlan.model.assets.TableauDashboard
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import mu.KLogger

class DLAxL(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "DLAxL - Dashboard-Level Assets without Lineage",
        "DLAxL",
        "**Total active BI dashboard-level assets in Atlan that do *not* have any lineage.** This is useful to:\n" +
            "- Check whether you expect lineage for these dashboards\n" +
            "- If not, whether these dashboards are actually used or could be removed to reduce overheads",
        Reporter.CAT_SAVINGS,
        client,
        batchSize,
        logger,
        caveats = "False positives could exist, when lineage is missing due to: not all data tools being loaded, improper sequence of crawling data tools, or due to bugs or lack of lineage support for the data tools involved.",
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        client.assets
            .select()
            .where(Asset.TYPE_NAME.`in`(DLA.DASHBOARD_LEVEL))
            .withoutLineage()
            .pageSize(batchSize)
            .includesOnResults(DLA.ATTRIBUTES)
            .includeOnResults(Asset.HAS_LINEAGE)
            .includeOnRelations(Asset.NAME)

    /** {@inheritDoc} */
    override fun getDetailedHeader(): Map<String, String> =
        mapOf(
            "Connector" to "Type of the data source",
            "Parent" to "Name of the parent that contains the dashboard-level asset",
            "Name" to "Name of the dashboard-level asset itself",
            "Link" to "Link to the detailed asset within Atlan",
        )

    /** {@inheritDoc} */
    override fun getDetailedRecord(asset: Asset): List<Any> {
        val parentName =
            when (asset) {
                is LookerDashboard -> asset.folder?.name ?: ""
                is MetabaseDashboard -> asset.metabaseCollection?.name ?: ""
                is MicroStrategyReport -> asset.microStrategyProject?.name ?: ""
                is ModeReport -> asset.modeWorkspaceName ?: ""
                is PowerBIDashboard -> asset.workspace?.name ?: ""
                is PresetDashboard -> asset.presetWorkspace?.name ?: ""
                is QlikChart -> asset.qlikSpaceQualifiedName ?: ""
                is QuickSightDashboard -> asset.quickSightSheetName ?: ""
                is SigmaPage -> asset.sigmaWorkbook?.name ?: ""
                is SisenseDashboard -> asset.sisenseFolder?.name ?: ""
                is TableauDashboard -> asset.projectQualifiedName ?: ""
                else -> ""
            }
        return listOf(
            asset.connectorType?.value ?: "",
            parentName,
            asset.name ?: "",
            getAssetLink(asset.guid),
        )
    }
}
