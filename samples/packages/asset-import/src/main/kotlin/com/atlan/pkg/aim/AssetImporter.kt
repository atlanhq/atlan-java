/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.ADLSAccount
import com.atlan.model.assets.ADLSContainer
import com.atlan.model.assets.ADLSObject
import com.atlan.model.assets.APIPath
import com.atlan.model.assets.APISpec
import com.atlan.model.assets.AirflowDag
import com.atlan.model.assets.AirflowTask
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
import com.atlan.model.assets.DMAttribute
import com.atlan.model.assets.DMAttributeAssociation
import com.atlan.model.assets.DMDataModel
import com.atlan.model.assets.DMEntity
import com.atlan.model.assets.DMEntityAssociation
import com.atlan.model.assets.DMVersion
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
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.csv.CSVImporter
import com.atlan.pkg.serde.csv.ImportResults
import mu.KotlinLogging

/**
 * Import assets into Atlan from a provided CSV file.
 *
 * Only the assets and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param filename name of the file to import
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param updateOnly if true, only update an asset (first check it exists), if false allow upserts (create if it does not exist)
 * @param batchSize maximum number of records to save per API request
 * @param caseSensitive (only applies when updateOnly is true) attempt to match assets case-sensitively (true) or case-insensitively (false)
 * @param creationHandling if assets are to be created, how they should be created (as full assets or only partial assets)
 * @param tableViewAgnostic if true, tables and views will be treated interchangeably (an asset in the batch marked as a table will attempt to match a view if not found as a table, and vice versa)
 * @param failOnErrors if true, fail if errors are encountered, otherwise continue processing
 * @param trackBatches if true, minimal details about every asset created or updated is tracked (if false, only counts of each are tracked)
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 */
class AssetImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
    private val caseSensitive: Boolean = true,
    private val creationHandling: AssetCreationHandling = AssetCreationHandling.NONE,
    private val tableViewAgnostic: Boolean = false,
    private val failOnErrors: Boolean = true,
    private val trackBatches: Boolean = true,
    private val fieldSeparator: Char = ',',
) : CSVImporter(
        filename,
        logger = KotlinLogging.logger {},
        attrsToOverwrite = attrsToOverwrite,
        updateOnly = updateOnly,
        batchSize = batchSize,
        caseSensitive = caseSensitive,
        creationHandling = creationHandling,
        tableViewAgnostic = tableViewAgnostic,
        failOnErrors = failOnErrors,
        trackBatches = trackBatches,
        fieldSeparator = fieldSeparator,
    ) {
    private val typesInFile = mutableSetOf<String>()
    private var typeToProcess = ""

    /** {@inheritDoc} */
    override fun preprocessRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): List<String> {
        // Keep a running collection of the types that are in the file
        typesInFile.add(row[typeIdx])
        return super.preprocessRow(row, header, typeIdx, qnIdx)
    }

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        if (updateOnly) {
            // If we're only updating, process as before (in-parallel, any order)
            return super.import(columnsToSkip)
        } else {
            // Otherwise, we need to do multi-pass loading:
            //  - Import assets in tiered order, top-to-bottom
            //  - Stop when we have processed all the types in the file
            val typeLoadingOrder = getLoadOrder(typesInFile)
            logger.info { "Asset loading order: $typeLoadingOrder" }
            var combinedResults: ImportResults? = null
            typeLoadingOrder.forEach {
                typeToProcess = it
                logger.info { "--- Importing $typeToProcess assets... ---" }
                val results = super.import(columnsToSkip)
                combinedResults = combinedResults?.combinedWith(results) ?: results
            }
            return combinedResults
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
            row.size >= typeIdx && row[typeIdx].isNotBlank()
        } else {
            // If we are doing more than only updates, process the assets in top-down order
            return row.size >= typeIdx && row[typeIdx] == typeToProcess
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
                    "DM",
                    listOf(
                        DMDataModel.TYPE_NAME,
                        DMVersion.TYPE_NAME,
                        DMEntity.TYPE_NAME,
                        DMAttribute.TYPE_NAME,
                        DMEntityAssociation.TYPE_NAME,
                        DMAttributeAssociation.TYPE_NAME,
                    ),
                ),
                TypeGrouping(
                    "Soda",
                    listOf(SodaCheck.TYPE_NAME),
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
}
