/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.*
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
            var combinedResults: ImportResults? = null
            typeLoadingOrder.forEach {
                typeToProcess = it
                logger.info { "Loading $typeToProcess assets..." }
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
        private val ordering = listOf(
            TypeGrouping(
                "__root",
                listOf(
                    Connection.TYPE_NAME,
                    AtlanCollection.TYPE_NAME,
                    Folder.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "Access Control",
                listOf(
                    Persona.TYPE_NAME,
                    Purpose.TYPE_NAME,
                    AuthPolicy.TYPE_NAME,
                )
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
                )
            ),
            TypeGrouping(
                "Cube",
                listOf(
                    Cube.TYPE_NAME,
                    CubeDimension.TYPE_NAME,
                    CubeHierarchy.TYPE_NAME,
                    CubeField.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "API",
                listOf(
                    APISpec.TYPE_NAME,
                    APIPath.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "Airflow",
                listOf(
                    AirflowDag.TYPE_NAME,
                    AirflowTask.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "DynamoDB",
                listOf(
                    DynamoDBTable.TYPE_NAME,
                    DynamoDBGlobalSecondaryIndex.TYPE_NAME,
                    DynamoDBLocalSecondaryIndex.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "S3",
                listOf(
                    S3Bucket.TYPE_NAME,
                    S3Object.TYPE_NAME,
                )
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
                )
            ),
            TypeGrouping(
                "ADLS",
                listOf(
                    ADLSAccount.TYPE_NAME,
                    ADLSContainer.TYPE_NAME,
                    ADLSObject.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "AzureEventHub",
                listOf(
                    AzureEventHub.TYPE_NAME,
                    AzureEventHubConsumerGroup.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "AzureServiceBus",
                listOf(
                    AzureServiceBusNamespace.TYPE_NAME,
                    AzureServiceBusTopic.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "CosmosMongoDB",
                listOf(
                    CosmosMongoDBAccount.TYPE_NAME,
                    CosmosMongoDBDatabase.TYPE_NAME,
                    CosmosMongoDBCollection.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "MongoDB",
                listOf(
                    MongoDBDatabase.TYPE_NAME,
                    MongoDBCollection.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "Multi-parent",
                listOf(
                    Column.TYPE_NAME,
                    AtlanQuery.TYPE_NAME,
                )
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
                )
            ),
            TypeGrouping(
                "Domo",
                listOf(
                    DomoDataset.TYPE_NAME,
                    DomoCard.TYPE_NAME,
                    DomoDatasetColumn.TYPE_NAME,
                    DomoDashboard.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "GCS",
                listOf(
                    GCSBucket.TYPE_NAME,
                    GCSObject.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "DataStudio",
                listOf(DataStudioAsset.TYPE_NAME)
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
                )
            ),
            TypeGrouping(
                "Kafka",
                listOf(
                    KafkaTopic.TYPE_NAME,
                    KafkaConsumerGroup.TYPE_NAME,
                )
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
                )
            ),
            TypeGrouping(
                "Matillion",
                listOf(
                    MatillionGroup.TYPE_NAME,
                    MatillionProject.TYPE_NAME,
                    MatillionJob.TYPE_NAME,
                    MatillionComponent.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "Metabase",
                listOf(
                    MetabaseCollection.TYPE_NAME,
                    MetabaseDashboard.TYPE_NAME,
                    MetabaseQuestion.TYPE_NAME,
                )
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
                )
            ),
            TypeGrouping(
                "Mode",
                listOf(
                    ModeWorkspace.TYPE_NAME,
                    ModeCollection.TYPE_NAME,
                    ModeReport.TYPE_NAME,
                    ModeQuery.TYPE_NAME,
                    ModeChart.TYPE_NAME,
                )
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
                )
            ),
            TypeGrouping(
                "Preset",
                listOf(
                    PresetWorkspace.TYPE_NAME,
                    PresetDashboard.TYPE_NAME,
                    PresetChart.TYPE_NAME,
                    PresetDataset.TYPE_NAME,
                )
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
                )
            ),
            TypeGrouping(
                "Redash",
                listOf(
                    RedashDashboard.TYPE_NAME,
                    RedashQuery.TYPE_NAME,
                    RedashVisualization.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "Salesforce",
                listOf(
                    SalesforceOrganization.TYPE_NAME,
                    SalesforceDashboard.TYPE_NAME,
                    SalesforceObject.TYPE_NAME,
                    SalesforceReport.TYPE_NAME,
                    SalesforceField.TYPE_NAME,
                )
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
                )
            ),
            TypeGrouping(
                "Sisense",
                listOf(
                    SisenseFolder.TYPE_NAME,
                    SisenseDashboard.TYPE_NAME,
                    SisenseDatamodel.TYPE_NAME,
                    SisenseWidget.TYPE_NAME,
                    SisenseDatamodelTable.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "Superset",
                listOf(
                    SupersetDashboard.TYPE_NAME,
                    SupersetChart.TYPE_NAME,
                    SupersetDataset.TYPE_NAME,
                )
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
                )
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
                )
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
                )
            ),
            TypeGrouping(
                "Soda",
                listOf(SodaCheck.TYPE_NAME)
            ),
            TypeGrouping(
                "MC",
                listOf(
                    MCMonitor.TYPE_NAME,
                    MCIncident.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "Dbt",
                listOf(
                    DbtModel.TYPE_NAME,
                    DbtSource.TYPE_NAME,
                    DbtMetric.TYPE_NAME,
                    DbtModelColumn.TYPE_NAME,
                    DbtTest.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "Lineage",
                listOf(
                    LineageProcess.TYPE_NAME,
                    DbtProcess.TYPE_NAME,
                    BIProcess.TYPE_NAME,
                    ColumnProcess.TYPE_NAME,
                    DbtColumnProcess.TYPE_NAME,
                )
            ),
            TypeGrouping(
                "Spark",
                listOf(SparkJob.TYPE_NAME)
            )
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
