/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.ADLSAccount
import com.atlan.model.assets.ADLSContainer
import com.atlan.model.assets.ADLSObject
import com.atlan.model.assets.APIPath
import com.atlan.model.assets.APISpec
import com.atlan.model.assets.AirflowDag
import com.atlan.model.assets.AirflowTask
import com.atlan.model.assets.AnomaloCheck
import com.atlan.model.assets.Asset
import com.atlan.model.assets.AtlanCollection
import com.atlan.model.assets.AtlanQuery
import com.atlan.model.assets.AuthPolicy
import com.atlan.model.assets.AzureEventHub
import com.atlan.model.assets.AzureEventHubConsumerGroup
import com.atlan.model.assets.AzureServiceBusNamespace
import com.atlan.model.assets.AzureServiceBusTopic
import com.atlan.model.assets.BIProcess
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
import com.atlan.model.assets.DataStudioAsset
import com.atlan.model.assets.Database
import com.atlan.model.assets.DatabricksUnityCatalogTag
import com.atlan.model.assets.DbtColumnProcess
import com.atlan.model.assets.DbtMetric
import com.atlan.model.assets.DbtModel
import com.atlan.model.assets.DbtModelColumn
import com.atlan.model.assets.DbtProcess
import com.atlan.model.assets.DbtSource
import com.atlan.model.assets.DbtTest
import com.atlan.model.assets.DomoCard
import com.atlan.model.assets.DomoDashboard
import com.atlan.model.assets.DomoDataset
import com.atlan.model.assets.DomoDatasetColumn
import com.atlan.model.assets.DynamoDBGlobalSecondaryIndex
import com.atlan.model.assets.DynamoDBLocalSecondaryIndex
import com.atlan.model.assets.DynamoDBTable
import com.atlan.model.assets.Folder
import com.atlan.model.assets.GCSBucket
import com.atlan.model.assets.GCSObject
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
import com.atlan.model.enums.AtlanTypeCategory
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.csv.CSVImporter
import com.atlan.pkg.serde.csv.CSVPreprocessor
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.serde.csv.RowPreprocessor
import mu.KLogger

/**
 * Import assets into Atlan from a provided CSV file.
 *
 * Only the assets and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context in which the package is running
 * @param filename name of the file to import
 * @param logger through which to write log entries
 */
class AssetImporter(
    ctx: PackageContext<AssetImportCfg>,
    filename: String,
    logger: KLogger,
) : CSVImporter(
        ctx,
        filename,
        logger = logger,
        attrsToOverwrite = attributesToClear(ctx.config.assetsAttrToOverwrite.toMutableList(), "assets", logger),
        updateOnly = ctx.config.assetsUpsertSemantic == "update",
        batchSize = ctx.config.assetsBatchSize.toInt(),
        caseSensitive = ctx.config.assetsCaseSensitive,
        creationHandling = Utils.getCreationHandling(ctx.config.assetsUpsertSemantic, AssetCreationHandling.NONE),
        tableViewAgnostic = ctx.config.assetsTableViewAgnostic,
        failOnErrors = ctx.config.assetsFailOnErrors,
        trackBatches = ctx.config.trackBatches,
        fieldSeparator = ctx.config.assetsFieldSeparator[0],
    ) {
    private var header = emptyList<String>()
    private var typeToProcess = ""
    private val cyclicalRelationships = mutableMapOf<String, Set<String>>()
    private val mapToSecondPass = mutableMapOf<String, Set<String>>()
    private val secondPassRemain =
        setOf(
            Asset.QUALIFIED_NAME.atlanFieldName,
            Asset.NAME.atlanFieldName,
            Folder.PARENT_QUALIFIED_NAME.atlanFieldName,
            Folder.COLLECTION_QUALIFIED_NAME.atlanFieldName,
        )

    /** {@inheritDoc} */
    override fun preprocess(
        outputFile: String?,
        outputHeaders: List<String>?,
    ): RowPreprocessor.Results {
        // Retrieve all relationships and filter to any cyclical relationships
        // (meaning relationships where both ends are of the same type)
        val typeDefs = ctx.client.typeDefs.list(AtlanTypeCategory.RELATIONSHIP)
        typeDefs.relationshipDefs.stream()
            .filter { it.endDef1.type == it.endDef2.type }
            .forEach { cyclicalRelationships[it.endDef1.type] = setOf(it.endDef1.name, it.endDef2.name) }
        val results = super.preprocess(outputFile, outputHeaders)
        return results
    }

    /** {@inheritDoc} */
    override fun preprocessRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): List<String> {
        // Check if the type on this row has any cyclical relationships as headers in the input file
        val typeName = CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" })
        if (!mapToSecondPass.containsKey(typeName)) {
            if (this.header.isEmpty()) this.header = header
            val cyclical = cyclicalRelationships.getOrElse(typeName) { emptySet() }.toList()
            if (cyclical.size == 2) {
                val one = cyclical[0]
                val two = cyclical[1]
                if (header.contains(one) && header.contains(two)) {
                    // If both ends of the same relationship are in the input file, throw an error
                    // alerting the user that this can't work and they'll need to pick one end or the other
                    throw IllegalStateException(
                        """
                        Both ends of the same relationship found in the input file for type $typeName: $one <> $two.
                        You should only use one end of this relationship or the other when importing.
                        """.trimIndent(),
                    )
                }
            }
            // Retain any of the cyclical relationships that remain so that we can second-pass process them
            val secondPassColumns = cyclical.filter { header.contains(it) }.toSet()
            mapToSecondPass[typeName] = secondPassColumns
        }
        return row
    }

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(Asset.GUID.atlanFieldName)
        if (updateOnly) {
            val cyclicalToSkip = mapToSecondPass.flatMap { it.value }
            // If we're only updating, process as before (in-parallel, any order)
            return if (cyclicalToSkip.isEmpty()) {
                // Skip any second-pass logic if there are no cyclical relationships
                logger.info { "--- Loading assets... ---" }
                super.import(colsToSkip)
            } else {
                // Otherwise, import assets without any cyclical relationships, first
                logger.info { "--- Loading assets in a first pass, without any cyclical relationships... ---" }
                val firstPassSkip = colsToSkip.toMutableSet()
                firstPassSkip.addAll(mapToSecondPass.flatMap { it.value })
                val firstPassResults = super.import(firstPassSkip)
                if (firstPassResults != null) {
                    val secondPassSkip = colsToSkip.toMutableSet()
                    secondPassSkip.addAll(header)
                    secondPassSkip.removeAll(firstPassSkip)
                    secondPassSkip.removeAll(secondPassRemain)
                    // In this second pass we need to ignore fields that were loaded in the first pass,
                    // or we will end up with duplicates (links) or extra audit log messages (tags, README)
                    logger.info { "--- Loading cyclical relationships (second pass)... ---" }
                    val secondPassResults = super.import(secondPassSkip)
                    ImportResults.combineAll(ctx.client, true, firstPassResults, secondPassResults)
                } else {
                    null
                }
            }
        } else {
            // Otherwise, we need to do multi-pass loading (at multiple levels):
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
            val individualResults = mutableListOf<ImportResults?>()
            typeLoadingOrder.forEach {
                typeToProcess = it
                val cyclicalForType = mapToSecondPass.getOrElse(typeToProcess) { emptySet() }
                if (cyclicalForType.isEmpty()) {
                    // If there are no cyclical relationships for this type, do everything in one pass
                    logger.info { "--- Importing $typeToProcess assets... ---" }
                    val results = super.import(colsToSkip)
                    if (results != null) individualResults.add(results)
                } else {
                    // Otherwise, import assets without any cyclical relationships, first
                    logger.info { "--- Importing $typeToProcess assets in a first pass, without any cyclical relationships... ---" }
                    val firstPassSkip = colsToSkip.toMutableSet()
                    firstPassSkip.addAll(cyclicalForType)
                    val firstPassResults = super.import(firstPassSkip)
                    if (firstPassResults != null) {
                        individualResults.add(firstPassResults)
                        val secondPassSkip = colsToSkip.toMutableSet()
                        secondPassSkip.addAll(header)
                        secondPassSkip.removeAll(firstPassSkip)
                        secondPassSkip.removeAll(secondPassRemain)
                        // In this second pass we need to ignore fields that were loaded in the first pass,
                        // or we will end up with duplicates (links) or extra audit log messages (tags, README)
                        logger.info { "--- Loading cyclical relationships for $typeToProcess (second pass)... ---" }
                        val secondPassResults = super.import(secondPassSkip)
                        if (secondPassResults != null) individualResults.add(secondPassResults)
                    }
                }
            }
            return ImportResults.combineAll(ctx.client, true, *individualResults.toTypedArray())
        }
    }

    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val typeName = deserializer.typeName
        return FieldSerde.getBuilderForType(typeName).qualifiedName(deserializer.qualifiedName)
    }

    /** {@inheritDoc} */
    override fun includeRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): Boolean {
        return if (updateOnly) {
            // If we are only updating, process in-parallel, in any order
            row.size >= typeIdx && CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" }).isNotBlank()
        } else {
            // If we are doing more than only updates, process the assets in top-down order
            return row.size >= typeIdx && CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" }) == typeToProcess
        }
    }

    data class TypeGrouping(
        val prefix: String,
        val types: List<String>,
    )

    companion object {
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
                    "SQL",
                    listOf(
                        Database.TYPE_NAME,
                        Schema.TYPE_NAME,
                        Procedure.TYPE_NAME,
                        SnowflakePipe.TYPE_NAME,
                        SnowflakeStream.TYPE_NAME,
                        SnowflakeTag.TYPE_NAME,
                        DatabricksUnityCatalogTag.TYPE_NAME,
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
                        DbtModel.TYPE_NAME,
                        DbtSource.TYPE_NAME,
                        DbtMetric.TYPE_NAME,
                        DbtModelColumn.TYPE_NAME,
                        DbtTest.TYPE_NAME,
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
        fun getLoadOrder(types: Set<String>): List<String> {
            return types.sortedBy { t ->
                ordering.flatMap { it.types }.indexOf(t).takeIf { it >= 0 } ?: Int.MAX_VALUE
            }
        }
    }

    /** Pre-process the assets import file. */
    private fun preprocess(): Results {
        return Preprocessor(filename, fieldSeparator, logger).preprocess<Results>()
    }

    private class Preprocessor(
        originalFile: String,
        fieldSeparator: Char,
        logger: KLogger,
    ) : CSVPreprocessor(
            filename = originalFile,
            logger = logger,
            fieldSeparator = fieldSeparator,
        ) {
        private val typesInFile = mutableSetOf<String>()

        /** {@inheritDoc} */
        override fun preprocessRow(
            row: List<String>,
            header: List<String>,
            typeIdx: Int,
            qnIdx: Int,
        ): List<String> {
            // Keep a running collection of the types that are in the file
            val typeName = CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" })
            if (typeName.isNotBlank()) {
                typesInFile.add(row[typeIdx])
            }
            return row
        }

        /** {@inheritDoc} */
        override fun finalize(
            header: List<String>,
            outputFile: String?,
        ): RowPreprocessor.Results {
            val results = super.finalize(header, outputFile)
            return Results(
                hasLinks = results.hasLinks,
                hasTermAssignments = results.hasTermAssignments,
                typesInFile = typesInFile,
            )
        }
    }

    private class Results(
        hasLinks: Boolean,
        hasTermAssignments: Boolean,
        val typesInFile: Set<String>,
    ) : RowPreprocessor.Results(
            hasLinks = hasLinks,
            hasTermAssignments = hasTermAssignments,
            outputFile = null,
        )
}
