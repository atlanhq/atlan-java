/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.ADLSAccount
import com.atlan.model.assets.ADLSContainer
import com.atlan.model.assets.ADLSObject
import com.atlan.model.assets.APIField
import com.atlan.model.assets.APIObject
import com.atlan.model.assets.APIPath
import com.atlan.model.assets.APISpec
import com.atlan.model.assets.AirflowDag
import com.atlan.model.assets.AirflowTask
import com.atlan.model.assets.AnomaloCheck
import com.atlan.model.assets.Application
import com.atlan.model.assets.ApplicationField
import com.atlan.model.assets.Asset
import com.atlan.model.assets.AtlanCollection
import com.atlan.model.assets.AtlanQuery
import com.atlan.model.assets.AuthPolicy
import com.atlan.model.assets.AzureEventHub
import com.atlan.model.assets.AzureEventHubConsumerGroup
import com.atlan.model.assets.AzureServiceBusNamespace
import com.atlan.model.assets.AzureServiceBusTopic
import com.atlan.model.assets.BIProcess
import com.atlan.model.assets.BigqueryTag
import com.atlan.model.assets.CalculationView
import com.atlan.model.assets.Cognite3DModel
import com.atlan.model.assets.CogniteAsset
import com.atlan.model.assets.CogniteEvent
import com.atlan.model.assets.CogniteFile
import com.atlan.model.assets.CogniteSequence
import com.atlan.model.assets.CogniteTimeSeries
import com.atlan.model.assets.CognosDashboard
import com.atlan.model.assets.CognosDatasource
import com.atlan.model.assets.CognosExploration
import com.atlan.model.assets.CognosFile
import com.atlan.model.assets.CognosFolder
import com.atlan.model.assets.CognosModule
import com.atlan.model.assets.CognosPackage
import com.atlan.model.assets.CognosReport
import com.atlan.model.assets.Column
import com.atlan.model.assets.ColumnProcess
import com.atlan.model.assets.Connection
import com.atlan.model.assets.CosmosMongoDBAccount
import com.atlan.model.assets.CosmosMongoDBCollection
import com.atlan.model.assets.CosmosMongoDBDatabase
import com.atlan.model.assets.Cube
import com.atlan.model.assets.CubeDimension
import com.atlan.model.assets.CubeField
import com.atlan.model.assets.CubeHierarchy
import com.atlan.model.assets.CustomEntity
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.assets.DataStudioAsset
import com.atlan.model.assets.Database
import com.atlan.model.assets.DatabricksUnityCatalogTag
import com.atlan.model.assets.DbtColumnProcess
import com.atlan.model.assets.DbtMetric
import com.atlan.model.assets.DbtModel
import com.atlan.model.assets.DbtModelColumn
import com.atlan.model.assets.DbtProcess
import com.atlan.model.assets.DbtSource
import com.atlan.model.assets.DbtTag
import com.atlan.model.assets.DbtTest
import com.atlan.model.assets.DomoCard
import com.atlan.model.assets.DomoDashboard
import com.atlan.model.assets.DomoDataset
import com.atlan.model.assets.DomoDatasetColumn
import com.atlan.model.assets.DynamoDBGlobalSecondaryIndex
import com.atlan.model.assets.DynamoDBLocalSecondaryIndex
import com.atlan.model.assets.DynamoDBTable
import com.atlan.model.assets.File
import com.atlan.model.assets.FlowControlOperation
import com.atlan.model.assets.FlowDataset
import com.atlan.model.assets.FlowDatasetOperation
import com.atlan.model.assets.FlowField
import com.atlan.model.assets.FlowFieldOperation
import com.atlan.model.assets.FlowFolder
import com.atlan.model.assets.FlowProject
import com.atlan.model.assets.FlowReusableUnit
import com.atlan.model.assets.Folder
import com.atlan.model.assets.GCSBucket
import com.atlan.model.assets.GCSObject
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.KafkaConsumerGroup
import com.atlan.model.assets.KafkaTopic
import com.atlan.model.assets.LineageProcess
import com.atlan.model.assets.LookerDashboard
import com.atlan.model.assets.LookerExplore
import com.atlan.model.assets.LookerField
import com.atlan.model.assets.LookerFolder
import com.atlan.model.assets.LookerLook
import com.atlan.model.assets.LookerModel
import com.atlan.model.assets.LookerProject
import com.atlan.model.assets.LookerQuery
import com.atlan.model.assets.LookerTile
import com.atlan.model.assets.LookerView
import com.atlan.model.assets.MCIncident
import com.atlan.model.assets.MCMonitor
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.MatillionComponent
import com.atlan.model.assets.MatillionGroup
import com.atlan.model.assets.MatillionJob
import com.atlan.model.assets.MatillionProject
import com.atlan.model.assets.MetabaseCollection
import com.atlan.model.assets.MetabaseDashboard
import com.atlan.model.assets.MetabaseQuestion
import com.atlan.model.assets.MicroStrategyAttribute
import com.atlan.model.assets.MicroStrategyCube
import com.atlan.model.assets.MicroStrategyDocument
import com.atlan.model.assets.MicroStrategyDossier
import com.atlan.model.assets.MicroStrategyFact
import com.atlan.model.assets.MicroStrategyMetric
import com.atlan.model.assets.MicroStrategyProject
import com.atlan.model.assets.MicroStrategyReport
import com.atlan.model.assets.MicroStrategyVisualization
import com.atlan.model.assets.ModeChart
import com.atlan.model.assets.ModeCollection
import com.atlan.model.assets.ModeQuery
import com.atlan.model.assets.ModeReport
import com.atlan.model.assets.ModeWorkspace
import com.atlan.model.assets.ModelAttribute
import com.atlan.model.assets.ModelAttributeAssociation
import com.atlan.model.assets.ModelDataModel
import com.atlan.model.assets.ModelEntity
import com.atlan.model.assets.ModelEntityAssociation
import com.atlan.model.assets.ModelVersion
import com.atlan.model.assets.MongoDBCollection
import com.atlan.model.assets.MongoDBDatabase
import com.atlan.model.assets.Persona
import com.atlan.model.assets.PowerBIColumn
import com.atlan.model.assets.PowerBIDashboard
import com.atlan.model.assets.PowerBIDataflow
import com.atlan.model.assets.PowerBIDataset
import com.atlan.model.assets.PowerBIDatasource
import com.atlan.model.assets.PowerBIMeasure
import com.atlan.model.assets.PowerBIPage
import com.atlan.model.assets.PowerBIReport
import com.atlan.model.assets.PowerBITable
import com.atlan.model.assets.PowerBITile
import com.atlan.model.assets.PowerBIWorkspace
import com.atlan.model.assets.PresetChart
import com.atlan.model.assets.PresetDashboard
import com.atlan.model.assets.PresetDataset
import com.atlan.model.assets.PresetWorkspace
import com.atlan.model.assets.Procedure
import com.atlan.model.assets.Purpose
import com.atlan.model.assets.QlikApp
import com.atlan.model.assets.QlikChart
import com.atlan.model.assets.QlikDataset
import com.atlan.model.assets.QlikSheet
import com.atlan.model.assets.QlikSpace
import com.atlan.model.assets.QlikStream
import com.atlan.model.assets.QuickSightAnalysis
import com.atlan.model.assets.QuickSightAnalysisVisual
import com.atlan.model.assets.QuickSightDashboard
import com.atlan.model.assets.QuickSightDashboardVisual
import com.atlan.model.assets.QuickSightDataset
import com.atlan.model.assets.QuickSightDatasetField
import com.atlan.model.assets.QuickSightFolder
import com.atlan.model.assets.RedashDashboard
import com.atlan.model.assets.RedashQuery
import com.atlan.model.assets.RedashVisualization
import com.atlan.model.assets.S3Bucket
import com.atlan.model.assets.S3Object
import com.atlan.model.assets.S3Prefix
import com.atlan.model.assets.SalesforceDashboard
import com.atlan.model.assets.SalesforceField
import com.atlan.model.assets.SalesforceObject
import com.atlan.model.assets.SalesforceOrganization
import com.atlan.model.assets.SalesforceReport
import com.atlan.model.assets.Schema
import com.atlan.model.assets.SigmaDataElement
import com.atlan.model.assets.SigmaDataElementField
import com.atlan.model.assets.SigmaDataset
import com.atlan.model.assets.SigmaDatasetColumn
import com.atlan.model.assets.SigmaPage
import com.atlan.model.assets.SigmaWorkbook
import com.atlan.model.assets.SisenseDashboard
import com.atlan.model.assets.SisenseDatamodel
import com.atlan.model.assets.SisenseDatamodelTable
import com.atlan.model.assets.SisenseFolder
import com.atlan.model.assets.SisenseWidget
import com.atlan.model.assets.SnowflakePipe
import com.atlan.model.assets.SnowflakeStream
import com.atlan.model.assets.SnowflakeTag
import com.atlan.model.assets.SodaCheck
import com.atlan.model.assets.SparkJob
import com.atlan.model.assets.SupersetChart
import com.atlan.model.assets.SupersetDashboard
import com.atlan.model.assets.SupersetDataset
import com.atlan.model.assets.Table
import com.atlan.model.assets.TablePartition
import com.atlan.model.assets.TableauCalculatedField
import com.atlan.model.assets.TableauDashboard
import com.atlan.model.assets.TableauDatasource
import com.atlan.model.assets.TableauDatasourceField
import com.atlan.model.assets.TableauFlow
import com.atlan.model.assets.TableauMetric
import com.atlan.model.assets.TableauProject
import com.atlan.model.assets.TableauSite
import com.atlan.model.assets.TableauWorkbook
import com.atlan.model.assets.TableauWorksheet
import com.atlan.model.assets.ThoughtspotAnswer
import com.atlan.model.assets.ThoughtspotColumn
import com.atlan.model.assets.ThoughtspotDashlet
import com.atlan.model.assets.ThoughtspotLiveboard
import com.atlan.model.assets.ThoughtspotTable
import com.atlan.model.assets.ThoughtspotView
import com.atlan.model.assets.ThoughtspotWorksheet
import com.atlan.model.assets.View
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.enums.CustomMetadataHandling
import com.atlan.model.enums.LinkIdempotencyInvariant
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.AssetRefXformer.getDeferredIdentity
import com.atlan.pkg.serde.cell.AssetRefXformer.resolveDeferredQN
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.util.AssetResolver
import com.atlan.pkg.util.AssetResolver.QualifiedNameDetails
import com.atlan.pkg.util.DeltaProcessor
import com.atlan.util.AssetBatch.AssetIdentity
import com.atlan.util.StringUtils
import mu.KLogger
import java.io.IOException

/**
 * Import assets into Atlan from a provided CSV file.
 *
 * Only the assets and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context in which the package is running
 * @param delta the processor containing any details about file deltas
 * @param filename name of the file to import
 * @param logger through which to write log entries
 */
class AssetImporter(
    ctx: PackageContext<AssetImportCfg>,
    private val delta: DeltaProcessor?,
    filename: String,
    logger: KLogger,
) : AbstractBaseImporter(
        ctx,
        filename,
        logger = logger,
        attrsToOverwrite =
            attributesToClear(
                ctx.config
                    .getEffectiveValue(
                        AssetImportCfg::assetsAttrToOverwrite,
                        AssetImportCfg::assetsConfig,
                    ).toMutableList(),
                "assets",
                logger,
            ),
        updateOnly = ctx.config.assetsUpsertSemantic == "update",
        batchSize =
            ctx.config
                .getEffectiveValue(
                    AssetImportCfg::assetsBatchSize,
                    AssetImportCfg::assetsConfig,
                ).toInt(),
        caseSensitive =
            ctx.config.getEffectiveValue(
                AssetImportCfg::assetsCaseSensitive,
                AssetImportCfg::assetsConfig,
            ),
        creationHandling = Utils.getCreationHandling(ctx.config.assetsUpsertSemantic, AssetCreationHandling.NONE),
        customMetadataHandling =
            Utils.getCustomMetadataHandling(
                ctx.config.getEffectiveValue(
                    AssetImportCfg::assetsCmHandling,
                    AssetImportCfg::assetsConfig,
                ),
                CustomMetadataHandling.MERGE,
            ),
        atlanTagHandling =
            Utils.getAtlanTagHandling(
                ctx.config.getEffectiveValue(
                    AssetImportCfg::assetsTagHandling,
                    AssetImportCfg::assetsConfig,
                ),
                AtlanTagHandling.REPLACE,
            ),
        tableViewAgnostic =
            ctx.config.getEffectiveValue(
                AssetImportCfg::assetsTableViewAgnostic,
                AssetImportCfg::assetsConfig,
            ),
        trackBatches = ctx.config.trackBatches,
        fieldSeparator =
            ctx.config.getEffectiveValue(
                AssetImportCfg::assetsFieldSeparator,
                AssetImportCfg::assetsConfig,
            )[0],
        linkIdempotency =
            Utils.getLinkIdempotency(
                ctx.config.getEffectiveValue(
                    AssetImportCfg::assetsLinkIdempotency,
                    AssetImportCfg::assetsConfig,
                ),
                LinkIdempotencyInvariant.URL,
            ),
    ) {
    private var typeToProcess = ""
    private val secondPassRemain =
        setOf(
            Asset.QUALIFIED_NAME.atlanFieldName,
            Asset.NAME.atlanFieldName,
            Folder.PARENT_QUALIFIED_NAME.atlanFieldName,
            Folder.COLLECTION_QUALIFIED_NAME.atlanFieldName,
            Asset.TYPE_NAME.atlanFieldName,
            Asset.CONNECTION_QUALIFIED_NAME.atlanFieldName,
            Asset.TENANT_ID.atlanFieldName,
            Column.ORDER.atlanFieldName,
        )

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(Asset.GUID.atlanFieldName)
        colsToSkip.add(Asset.QUALIFIED_NAME.atlanFieldName) // will be resolved later
        // Irrespective of update-only or not, multi-pass load (at multiple levels):
        //  - Import assets in tiered order, top-to-bottom
        //  - Stop when we have processed all the types in the file
        val includes = preprocess()
        if (includes.hasLinks) {
            ctx.linkCache.preload()
        }
        if (includes.hasTermAssignments) {
            ctx.termCache.preload()
        }
        val typeLoadingOrder = getLoadOrder(includes.typesInFile)
        logger.info { "Asset loading order: $typeLoadingOrder" }
        typeLoadingOrder.forEach {
            typeToProcess = it
            val results = super.import(typeToProcess, colsToSkip, secondPassRemain)
            if (results != null) ctx.processedResults.extendWith(results, true)
        }
        return ctx.processedResults
    }

    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val typeName = deserializer.typeName
        val qualifiedName = deserializer.qualifiedName
        val resolvedQN = resolveDeferredQN(ctx, qualifiedName)
        return FieldSerde.getBuilderForType(typeName).qualifiedName(resolvedQN)
    }

    /** {@inheritDoc} */
    override fun includeRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): Boolean {
        // Note: skip all Connection asset types, since they will be loaded up-front
        val candidateRow = row.size >= typeIdx && CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" }) == typeToProcess
        // Only proceed processing this candidate row if we're doing non-delta processing, or we have
        // detected that it needs to be loaded via the delta processing
        return if (candidateRow) {
            delta?.resolveAsset(row, header)?.let { identity ->
                delta.reloadAsset(identity)
            } ?: true
        } else {
            false
        }
    }

    data class TypeGrouping(
        val prefix: String,
        val types: List<String>,
    )

    companion object : AssetResolver {
        val GLOSSARY_TYPES =
            listOf(
                Glossary.TYPE_NAME,
                GlossaryTerm.TYPE_NAME,
                GlossaryCategory.TYPE_NAME,
            )
        val DATA_PRODUCT_TYPES =
            listOf(
                DataDomain.TYPE_NAME,
                DataProduct.TYPE_NAME,
            )
        const val NO_CONNECTION_QN = "NO_CONNECTION_FOUND"
        private val ordering =
            listOf(
                TypeGrouping(
                    "__root",
                    listOf(
                        Connection.TYPE_NAME,
                        AtlanCollection.TYPE_NAME,
                        Folder.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Access Control",
                    listOf(
                        Persona.TYPE_NAME,
                        Purpose.TYPE_NAME,
                        AuthPolicy.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Applications",
                    listOf(
                        Application.TYPE_NAME,
                        ApplicationField.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "SQL",
                    listOf(
                        BigqueryTag.TYPE_NAME,
                        DatabricksUnityCatalogTag.TYPE_NAME,
                        Database.TYPE_NAME,
                        Schema.TYPE_NAME,
                        SnowflakeTag.TYPE_NAME,
                        Procedure.TYPE_NAME,
                        SnowflakePipe.TYPE_NAME,
                        SnowflakeStream.TYPE_NAME,
                        Table.TYPE_NAME,
                        View.TYPE_NAME,
                        MaterializedView.TYPE_NAME,
                        CalculationView.TYPE_NAME,
                        TablePartition.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Cube",
                    listOf(
                        Cube.TYPE_NAME,
                        CubeDimension.TYPE_NAME,
                        CubeHierarchy.TYPE_NAME,
                        CubeField.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "API",
                    listOf(
                        APISpec.TYPE_NAME,
                        APIPath.TYPE_NAME,
                        APIObject.TYPE_NAME,
                        APIField.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Airflow",
                    listOf(
                        AirflowDag.TYPE_NAME,
                        AirflowTask.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "DynamoDB",
                    listOf(
                        DynamoDBTable.TYPE_NAME,
                        DynamoDBGlobalSecondaryIndex.TYPE_NAME,
                        DynamoDBLocalSecondaryIndex.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "S3",
                    listOf(
                        S3Bucket.TYPE_NAME,
                        S3Prefix.TYPE_NAME,
                        S3Object.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "QuickSight",
                    listOf(
                        QuickSightFolder.TYPE_NAME,
                        QuickSightAnalysis.TYPE_NAME,
                        QuickSightDashboard.TYPE_NAME,
                        QuickSightDataset.TYPE_NAME,
                        QuickSightAnalysisVisual.TYPE_NAME,
                        QuickSightDashboardVisual.TYPE_NAME,
                        QuickSightDatasetField.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "ADLS",
                    listOf(
                        ADLSAccount.TYPE_NAME,
                        ADLSContainer.TYPE_NAME,
                        ADLSObject.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "AzureEventHub",
                    listOf(
                        AzureEventHub.TYPE_NAME,
                        AzureEventHubConsumerGroup.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "AzureServiceBus",
                    listOf(
                        AzureServiceBusNamespace.TYPE_NAME,
                        AzureServiceBusTopic.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "CosmosMongoDB",
                    listOf(
                        CosmosMongoDBAccount.TYPE_NAME,
                        CosmosMongoDBDatabase.TYPE_NAME,
                        CosmosMongoDBCollection.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "MongoDB",
                    listOf(
                        MongoDBDatabase.TYPE_NAME,
                        MongoDBCollection.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Multi-parent",
                    listOf(
                        Column.TYPE_NAME,
                        AtlanQuery.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Cognite",
                    listOf(
                        CogniteAsset.TYPE_NAME,
                        Cognite3DModel.TYPE_NAME,
                        CogniteEvent.TYPE_NAME,
                        CogniteFile.TYPE_NAME,
                        CogniteSequence.TYPE_NAME,
                        CogniteTimeSeries.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Domo",
                    listOf(
                        DomoDataset.TYPE_NAME,
                        DomoCard.TYPE_NAME,
                        DomoDatasetColumn.TYPE_NAME,
                        DomoDashboard.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "GCS",
                    listOf(
                        GCSBucket.TYPE_NAME,
                        GCSObject.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "DataStudio",
                    listOf(DataStudioAsset.TYPE_NAME),
                ),
                TypeGrouping(
                    "Cognos",
                    listOf(
                        CognosFolder.TYPE_NAME,
                        CognosDatasource.TYPE_NAME,
                        CognosDashboard.TYPE_NAME,
                        CognosExploration.TYPE_NAME,
                        CognosFile.TYPE_NAME,
                        CognosModule.TYPE_NAME,
                        CognosPackage.TYPE_NAME,
                        CognosReport.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Kafka",
                    listOf(
                        KafkaTopic.TYPE_NAME,
                        KafkaConsumerGroup.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Looker",
                    listOf(
                        LookerProject.TYPE_NAME,
                        LookerFolder.TYPE_NAME,
                        LookerModel.TYPE_NAME,
                        LookerDashboard.TYPE_NAME,
                        LookerExplore.TYPE_NAME,
                        LookerQuery.TYPE_NAME,
                        LookerView.TYPE_NAME,
                        LookerLook.TYPE_NAME,
                        LookerField.TYPE_NAME,
                        LookerTile.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Matillion",
                    listOf(
                        MatillionGroup.TYPE_NAME,
                        MatillionProject.TYPE_NAME,
                        MatillionJob.TYPE_NAME,
                        MatillionComponent.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Metabase",
                    listOf(
                        MetabaseCollection.TYPE_NAME,
                        MetabaseDashboard.TYPE_NAME,
                        MetabaseQuestion.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "MicroStrategy",
                    listOf(
                        MicroStrategyProject.TYPE_NAME,
                        MicroStrategyDocument.TYPE_NAME,
                        MicroStrategyDossier.TYPE_NAME,
                        MicroStrategyReport.TYPE_NAME,
                        MicroStrategyCube.TYPE_NAME,
                        MicroStrategyVisualization.TYPE_NAME,
                        MicroStrategyAttribute.TYPE_NAME,
                        MicroStrategyFact.TYPE_NAME,
                        MicroStrategyMetric.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Mode",
                    listOf(
                        ModeWorkspace.TYPE_NAME,
                        ModeCollection.TYPE_NAME,
                        ModeReport.TYPE_NAME,
                        ModeQuery.TYPE_NAME,
                        ModeChart.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "PowerBI",
                    listOf(
                        PowerBIWorkspace.TYPE_NAME,
                        PowerBIDataflow.TYPE_NAME,
                        PowerBIDataset.TYPE_NAME,
                        PowerBIDashboard.TYPE_NAME,
                        PowerBIDatasource.TYPE_NAME,
                        PowerBIReport.TYPE_NAME,
                        PowerBITable.TYPE_NAME,
                        PowerBITile.TYPE_NAME,
                        PowerBIPage.TYPE_NAME,
                        PowerBIColumn.TYPE_NAME,
                        PowerBIMeasure.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Preset",
                    listOf(
                        PresetWorkspace.TYPE_NAME,
                        PresetDashboard.TYPE_NAME,
                        PresetChart.TYPE_NAME,
                        PresetDataset.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Qlik",
                    listOf(
                        QlikSpace.TYPE_NAME,
                        QlikStream.TYPE_NAME,
                        QlikApp.TYPE_NAME,
                        QlikDataset.TYPE_NAME,
                        QlikSheet.TYPE_NAME,
                        QlikChart.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Redash",
                    listOf(
                        RedashDashboard.TYPE_NAME,
                        RedashQuery.TYPE_NAME,
                        RedashVisualization.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Salesforce",
                    listOf(
                        SalesforceOrganization.TYPE_NAME,
                        SalesforceDashboard.TYPE_NAME,
                        SalesforceObject.TYPE_NAME,
                        SalesforceReport.TYPE_NAME,
                        SalesforceField.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Sigma",
                    listOf(
                        SigmaWorkbook.TYPE_NAME,
                        SigmaDataset.TYPE_NAME,
                        SigmaPage.TYPE_NAME,
                        SigmaDatasetColumn.TYPE_NAME,
                        SigmaDataElement.TYPE_NAME,
                        SigmaDataElementField.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Sisense",
                    listOf(
                        SisenseFolder.TYPE_NAME,
                        SisenseDashboard.TYPE_NAME,
                        SisenseDatamodel.TYPE_NAME,
                        SisenseWidget.TYPE_NAME,
                        SisenseDatamodelTable.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Superset",
                    listOf(
                        SupersetDashboard.TYPE_NAME,
                        SupersetChart.TYPE_NAME,
                        SupersetDataset.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Tableau",
                    listOf(
                        TableauSite.TYPE_NAME,
                        TableauProject.TYPE_NAME,
                        TableauFlow.TYPE_NAME,
                        TableauMetric.TYPE_NAME,
                        TableauWorkbook.TYPE_NAME,
                        TableauDashboard.TYPE_NAME,
                        TableauDatasource.TYPE_NAME,
                        TableauWorksheet.TYPE_NAME,
                        TableauCalculatedField.TYPE_NAME,
                        TableauDatasourceField.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Thoughtspot",
                    listOf(
                        ThoughtspotLiveboard.TYPE_NAME,
                        ThoughtspotAnswer.TYPE_NAME,
                        ThoughtspotWorksheet.TYPE_NAME,
                        ThoughtspotTable.TYPE_NAME,
                        ThoughtspotView.TYPE_NAME,
                        ThoughtspotDashlet.TYPE_NAME,
                        ThoughtspotColumn.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Model",
                    listOf(
                        ModelDataModel.TYPE_NAME,
                        ModelVersion.TYPE_NAME,
                        ModelEntity.TYPE_NAME,
                        ModelAttribute.TYPE_NAME,
                        ModelEntityAssociation.TYPE_NAME,
                        ModelAttributeAssociation.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Soda",
                    listOf(SodaCheck.TYPE_NAME),
                ),
                TypeGrouping(
                    "Anomalo",
                    listOf(AnomaloCheck.TYPE_NAME),
                ),
                TypeGrouping(
                    "MC",
                    listOf(
                        MCMonitor.TYPE_NAME,
                        MCIncident.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Dbt",
                    listOf(
                        DbtTag.TYPE_NAME,
                        DbtModel.TYPE_NAME,
                        DbtSource.TYPE_NAME,
                        DbtMetric.TYPE_NAME,
                        DbtModelColumn.TYPE_NAME,
                        DbtTest.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Custom",
                    listOf(
                        File.TYPE_NAME,
                        CustomEntity.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Flows",
                    listOf(
                        FlowProject.TYPE_NAME,
                        FlowFolder.TYPE_NAME,
                        FlowControlOperation.TYPE_NAME,
                        FlowReusableUnit.TYPE_NAME,
                        FlowDataset.TYPE_NAME,
                        FlowField.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Lineage",
                    listOf(
                        LineageProcess.TYPE_NAME,
                        DbtProcess.TYPE_NAME,
                        BIProcess.TYPE_NAME,
                        ColumnProcess.TYPE_NAME,
                        DbtColumnProcess.TYPE_NAME,
                        FlowDatasetOperation.TYPE_NAME,
                        FlowFieldOperation.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Spark",
                    listOf(SparkJob.TYPE_NAME),
                ),
            )

        /**
         * Sort the provided set of type names into an appropriate top-down loading order.
         *
         * @param types to sort into a loading order
         * @return an ordered (top-down) list of types
         */
        fun getLoadOrder(types: Set<String>): List<String> =
            types.filter { it != Connection.TYPE_NAME }.sortedBy { t ->
                ordering
                    .flatMap { it.types }
                    .indexOf(t)
                    .takeIf { it >= 0 }
                    ?: Int.MAX_VALUE
            }

        /** {@inheritDoc} */
        override fun resolveAsset(
            ctx: PackageContext<*>,
            values: List<String>,
            header: List<String>,
        ): AssetIdentity {
            val typeIdx = header.indexOf(Asset.TYPE_NAME.atlanFieldName)
            if (typeIdx < 0) {
                throw IOException(
                    "Unable to find the column 'typeName'. This is a mandatory column in the input CSV.",
                )
            }
            val qnIdx = header.indexOf(Asset.QUALIFIED_NAME.atlanFieldName)
            if (qnIdx < 0) {
                throw IOException(
                    "Unable to find the column 'qualifiedName'. This is a mandatory column in the input CSV.",
                )
            }
            val typeName = CSVXformer.trimWhitespace(values[typeIdx])
            val qualifiedName = CSVXformer.trimWhitespace(values[qnIdx])
            val resolvedQN = resolveDeferredQN(ctx, qualifiedName)
            return AssetIdentity(typeName, resolvedQN)
        }

        /** {@inheritDoc} */
        override fun getQualifiedNameDetails(
            row: List<String>,
            header: List<String>,
            typeName: String,
        ): QualifiedNameDetails = throw IllegalStateException("This method should never be called. Please raise an issue if you discover this in any log file.")
    }

    /** {@inheritDoc} */
    override fun preprocess(
        outputFile: String?,
        outputHeaders: List<String>?,
    ): Results = Preprocessor(ctx, filename, fieldSeparator, logger).preprocess<Results>()

    class Preprocessor(
        override val ctx: PackageContext<*>,
        originalFile: String,
        fieldSeparator: Char,
        logger: KLogger,
    ) : AbstractBaseImporter.Preprocessor(
            ctx = ctx,
            originalFile = originalFile,
            fieldSeparator = fieldSeparator,
            logger = logger,
            requiredHeaders =
                mapOf(
                    Asset.TYPE_NAME.atlanFieldName to emptySet(),
                    Asset.QUALIFIED_NAME.atlanFieldName to setOf(),
                    Asset.NAME.atlanFieldName to emptySet(),
                ),
        ) {
        private val connectionQNs = mutableSetOf<String>()
        private val aimCtx = ctx as PackageContext<AssetImportCfg>
        private val relaxedCQN = aimCtx.config.relaxedCqn
        private val glossaryTypes = mutableSetOf<String>()
        private val productTypes = mutableListOf<String>()

        /** {@inheritDoc} */
        override fun preprocessRow(
            row: List<String>,
            header: List<String>,
            typeIdx: Int,
            qnIdx: Int,
        ): List<String> {
            val updated = super.preprocessRow(row, header, typeIdx, qnIdx)
            // Keep a running collection of the types that are in the file
            val typeName = CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" })
            if (typeName.isNotBlank() && !invalidTypes.contains(typeName)) {
                val qualifiedName = CSVXformer.trimWhitespace(row.getOrNull(header.indexOf(Asset.QUALIFIED_NAME.atlanFieldName)) ?: "")
                if (typeName.isNotBlank() && typeName in GLOSSARY_TYPES) {
                    glossaryTypes.add(typeName)
                }
                if (typeName.isNotBlank() && typeName in DATA_PRODUCT_TYPES) {
                    productTypes.add(typeName)
                }
                if (!glossaryTypes.contains(typeName) && !productTypes.contains(typeName)) {
                    val connectionQNFromAsset = StringUtils.getConnectionQualifiedName(qualifiedName, relaxedCQN)
                    val deferredIdentity = getDeferredIdentity(qualifiedName)
                    if (connectionQNFromAsset != null) {
                        connectionQNs.add(connectionQNFromAsset)
                    } else if (typeName == Connection.TYPE_NAME) {
                        // If the qualifiedName comes back as null and the asset itself is a connection, add it
                        if (StringUtils.isValidConnectionQN(qualifiedName, relaxedCQN)) {
                            connectionQNs.add(qualifiedName)
                        } else if (deferredIdentity == null) {
                            throw IllegalStateException(
                                "Found a connection without a valid qualifiedName: $qualifiedName -- must be of the form 'default/connectorType/nnnnnnnnnn', where connectorType is a valid connector type (like 'snowflake') and nnnnnnnnnn is an epoch-style timestamp down to seconds granularity.",
                            )
                        }
                    } else if (deferredIdentity == null) {
                        throw IllegalStateException("Found an asset without a valid qualifiedName (of type $typeName): $qualifiedName")
                    } else {
                        val deferredId = ctx.connectionCache.getIdentityForAsset(deferredIdentity.name, deferredIdentity.type)
                        connectionQNs.add(ctx.connectionCache.getByIdentity(deferredId)?.qualifiedName ?: NO_CONNECTION_QN)
                    }
                }
            }
            return updated
        }

        /** {@inheritDoc} */
        override fun finalize(
            header: List<String>,
            outputFile: String?,
        ): Results {
            val results = super.finalize(header, outputFile)
            if (invalidTypes.isNotEmpty()) {
                throw IllegalArgumentException("Invalid types were supplied in the input file, which cannot be loaded. Remove these or replace with a valid typeName: $invalidTypes")
            }
            if (glossaryTypes.isNotEmpty()) {
                throw IllegalStateException("Found assets that should be loaded via the glossaries file, of types: $glossaryTypes")
            }
            if (productTypes.isNotEmpty()) {
                throw IllegalStateException("Found assets that should be loaded via the data products file, of types: $productTypes")
            }
            return Results(
                connectionQN = if (connectionQNs.isNotEmpty()) connectionQNs.first() else NO_CONNECTION_QN,
                multipleConnections = connectionQNs.size > 1,
                hasLinks = results.hasLinks,
                hasTermAssignments = results.hasTermAssignments,
                outputFile = outputFile ?: filename,
                hasDomainRelationship = results.hasDomainRelationship,
                hasProductRelationship = results.hasProductRelationship,
                typesInFile = typesInFile,
                mapToSecondPass = results.mapToSecondPass,
            )
        }
    }

    class Results(
        connectionQN: String,
        multipleConnections: Boolean,
        hasLinks: Boolean,
        hasTermAssignments: Boolean,
        outputFile: String,
        hasDomainRelationship: Boolean,
        hasProductRelationship: Boolean,
        mapToSecondPass: Map<String, Set<String>>,
        val typesInFile: Set<String>,
    ) : AbstractBaseImporter.Results(
            assetRootName = connectionQN,
            hasLinks = hasLinks,
            hasTermAssignments = hasTermAssignments,
            multipleConnections = multipleConnections,
            outputFile = outputFile,
            hasDomainRelationship = hasDomainRelationship,
            hasProductRelationship = hasProductRelationship,
            mapToSecondPass = mapToSecondPass,
        )
}
