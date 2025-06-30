/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.model.fields.RelationField;
import com.atlan.model.relations.RelationshipAttributes;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.PopularityInsights;
import com.atlan.model.structs.StarredDetails;
import com.atlan.serde.AssetDeserializer;
import com.atlan.serde.AssetSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;

/**
 * Base class for catalog assets. Catalog assets include any asset that can participate in lineage.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface ICatalog {

    public static final String TYPE_NAME = "Catalog";

    /** Tasks to which this asset provides input. */
    RelationField INPUT_TO_AIRFLOW_TASKS = new RelationField("inputToAirflowTasks");

    /** Processes to which this asset provides input. */
    RelationField INPUT_TO_PROCESSES = new RelationField("inputToProcesses");

    /** TBC */
    RelationField INPUT_TO_SPARK_JOBS = new RelationField("inputToSparkJobs");

    /** Attributes implemented by this asset. */
    RelationField MODEL_IMPLEMENTED_ATTRIBUTES = new RelationField("modelImplementedAttributes");

    /** Entities implemented by this asset. */
    RelationField MODEL_IMPLEMENTED_ENTITIES = new RelationField("modelImplementedEntities");

    /** Tasks from which this asset is output. */
    RelationField OUTPUT_FROM_AIRFLOW_TASKS = new RelationField("outputFromAirflowTasks");

    /** Processes from which this asset is produced as output. */
    RelationField OUTPUT_FROM_PROCESSES = new RelationField("outputFromProcesses");

    /** TBC */
    RelationField OUTPUT_FROM_SPARK_JOBS = new RelationField("outputFromSparkJobs");

    /**
     * Reference to an asset by its qualifiedName.
     *
     * @param typeName the type of the asset being referenced
     * @param qualifiedName the qualifiedName of the asset to reference
     * @return reference to an asset that can be used for defining a lineage relationship to the asset
     */
    public static ICatalog getLineageReference(String typeName, String qualifiedName) {
        ICatalog ref = null;
        switch (typeName) {
            case ADLSAccount.TYPE_NAME:
                ref = ADLSAccount.refByQualifiedName(qualifiedName);
                break;
            case ADLSContainer.TYPE_NAME:
                ref = ADLSContainer.refByQualifiedName(qualifiedName);
                break;
            case ADLSObject.TYPE_NAME:
                ref = ADLSObject.refByQualifiedName(qualifiedName);
                break;
            case AIApplication.TYPE_NAME:
                ref = AIApplication.refByQualifiedName(qualifiedName);
                break;
            case AIModel.TYPE_NAME:
                ref = AIModel.refByQualifiedName(qualifiedName);
                break;
            case APIField.TYPE_NAME:
                ref = APIField.refByQualifiedName(qualifiedName);
                break;
            case APIObject.TYPE_NAME:
                ref = APIObject.refByQualifiedName(qualifiedName);
                break;
            case APIPath.TYPE_NAME:
                ref = APIPath.refByQualifiedName(qualifiedName);
                break;
            case APIQuery.TYPE_NAME:
                ref = APIQuery.refByQualifiedName(qualifiedName);
                break;
            case APISpec.TYPE_NAME:
                ref = APISpec.refByQualifiedName(qualifiedName);
                break;
            case AdfActivity.TYPE_NAME:
                ref = AdfActivity.refByQualifiedName(qualifiedName);
                break;
            case AdfDataflow.TYPE_NAME:
                ref = AdfDataflow.refByQualifiedName(qualifiedName);
                break;
            case AdfDataset.TYPE_NAME:
                ref = AdfDataset.refByQualifiedName(qualifiedName);
                break;
            case AdfLinkedservice.TYPE_NAME:
                ref = AdfLinkedservice.refByQualifiedName(qualifiedName);
                break;
            case AdfPipeline.TYPE_NAME:
                ref = AdfPipeline.refByQualifiedName(qualifiedName);
                break;
            case AirflowDag.TYPE_NAME:
                ref = AirflowDag.refByQualifiedName(qualifiedName);
                break;
            case AirflowTask.TYPE_NAME:
                ref = AirflowTask.refByQualifiedName(qualifiedName);
                break;
            case AnaplanApp.TYPE_NAME:
                ref = AnaplanApp.refByQualifiedName(qualifiedName);
                break;
            case AnaplanDimension.TYPE_NAME:
                ref = AnaplanDimension.refByQualifiedName(qualifiedName);
                break;
            case AnaplanLineItem.TYPE_NAME:
                ref = AnaplanLineItem.refByQualifiedName(qualifiedName);
                break;
            case AnaplanList.TYPE_NAME:
                ref = AnaplanList.refByQualifiedName(qualifiedName);
                break;
            case AnaplanModel.TYPE_NAME:
                ref = AnaplanModel.refByQualifiedName(qualifiedName);
                break;
            case AnaplanModule.TYPE_NAME:
                ref = AnaplanModule.refByQualifiedName(qualifiedName);
                break;
            case AnaplanPage.TYPE_NAME:
                ref = AnaplanPage.refByQualifiedName(qualifiedName);
                break;
            case AnaplanSystemDimension.TYPE_NAME:
                ref = AnaplanSystemDimension.refByQualifiedName(qualifiedName);
                break;
            case AnaplanView.TYPE_NAME:
                ref = AnaplanView.refByQualifiedName(qualifiedName);
                break;
            case AnaplanWorkspace.TYPE_NAME:
                ref = AnaplanWorkspace.refByQualifiedName(qualifiedName);
                break;
            case AnomaloCheck.TYPE_NAME:
                ref = AnomaloCheck.refByQualifiedName(qualifiedName);
                break;
            case Application.TYPE_NAME:
                ref = Application.refByQualifiedName(qualifiedName);
                break;
            case ApplicationField.TYPE_NAME:
                ref = ApplicationField.refByQualifiedName(qualifiedName);
                break;
            case AtlanQuery.TYPE_NAME:
                ref = AtlanQuery.refByQualifiedName(qualifiedName);
                break;
            case AzureEventHub.TYPE_NAME:
                ref = AzureEventHub.refByQualifiedName(qualifiedName);
                break;
            case AzureEventHubConsumerGroup.TYPE_NAME:
                ref = AzureEventHubConsumerGroup.refByQualifiedName(qualifiedName);
                break;
            case AzureServiceBusNamespace.TYPE_NAME:
                ref = AzureServiceBusNamespace.refByQualifiedName(qualifiedName);
                break;
            case AzureServiceBusSchema.TYPE_NAME:
                ref = AzureServiceBusSchema.refByQualifiedName(qualifiedName);
                break;
            case AzureServiceBusTopic.TYPE_NAME:
                ref = AzureServiceBusTopic.refByQualifiedName(qualifiedName);
                break;
            case BigqueryTag.TYPE_NAME:
                ref = BigqueryTag.refByQualifiedName(qualifiedName);
                break;
            case CalculationView.TYPE_NAME:
                ref = CalculationView.refByQualifiedName(qualifiedName);
                break;
            case CassandraColumn.TYPE_NAME:
                ref = CassandraColumn.refByQualifiedName(qualifiedName);
                break;
            case CassandraIndex.TYPE_NAME:
                ref = CassandraIndex.refByQualifiedName(qualifiedName);
                break;
            case CassandraKeyspace.TYPE_NAME:
                ref = CassandraKeyspace.refByQualifiedName(qualifiedName);
                break;
            case CassandraTable.TYPE_NAME:
                ref = CassandraTable.refByQualifiedName(qualifiedName);
                break;
            case CassandraView.TYPE_NAME:
                ref = CassandraView.refByQualifiedName(qualifiedName);
                break;
            case Cognite3DModel.TYPE_NAME:
                ref = Cognite3DModel.refByQualifiedName(qualifiedName);
                break;
            case CogniteAsset.TYPE_NAME:
                ref = CogniteAsset.refByQualifiedName(qualifiedName);
                break;
            case CogniteEvent.TYPE_NAME:
                ref = CogniteEvent.refByQualifiedName(qualifiedName);
                break;
            case CogniteFile.TYPE_NAME:
                ref = CogniteFile.refByQualifiedName(qualifiedName);
                break;
            case CogniteSequence.TYPE_NAME:
                ref = CogniteSequence.refByQualifiedName(qualifiedName);
                break;
            case CogniteTimeSeries.TYPE_NAME:
                ref = CogniteTimeSeries.refByQualifiedName(qualifiedName);
                break;
            case CognosDashboard.TYPE_NAME:
                ref = CognosDashboard.refByQualifiedName(qualifiedName);
                break;
            case CognosDatasource.TYPE_NAME:
                ref = CognosDatasource.refByQualifiedName(qualifiedName);
                break;
            case CognosExploration.TYPE_NAME:
                ref = CognosExploration.refByQualifiedName(qualifiedName);
                break;
            case CognosFile.TYPE_NAME:
                ref = CognosFile.refByQualifiedName(qualifiedName);
                break;
            case CognosFolder.TYPE_NAME:
                ref = CognosFolder.refByQualifiedName(qualifiedName);
                break;
            case CognosModule.TYPE_NAME:
                ref = CognosModule.refByQualifiedName(qualifiedName);
                break;
            case CognosPackage.TYPE_NAME:
                ref = CognosPackage.refByQualifiedName(qualifiedName);
                break;
            case CognosReport.TYPE_NAME:
                ref = CognosReport.refByQualifiedName(qualifiedName);
                break;
            case Column.TYPE_NAME:
                ref = Column.refByQualifiedName(qualifiedName);
                break;
            case CosmosMongoDBAccount.TYPE_NAME:
                ref = CosmosMongoDBAccount.refByQualifiedName(qualifiedName);
                break;
            case CosmosMongoDBCollection.TYPE_NAME:
                ref = CosmosMongoDBCollection.refByQualifiedName(qualifiedName);
                break;
            case CosmosMongoDBDatabase.TYPE_NAME:
                ref = CosmosMongoDBDatabase.refByQualifiedName(qualifiedName);
                break;
            case Cube.TYPE_NAME:
                ref = Cube.refByQualifiedName(qualifiedName);
                break;
            case CubeDimension.TYPE_NAME:
                ref = CubeDimension.refByQualifiedName(qualifiedName);
                break;
            case CubeField.TYPE_NAME:
                ref = CubeField.refByQualifiedName(qualifiedName);
                break;
            case CubeHierarchy.TYPE_NAME:
                ref = CubeHierarchy.refByQualifiedName(qualifiedName);
                break;
            case CustomEntity.TYPE_NAME:
                ref = CustomEntity.refByQualifiedName(qualifiedName);
                break;
            case DataContract.TYPE_NAME:
                ref = DataContract.refByQualifiedName(qualifiedName);
                break;
            case DataDomain.TYPE_NAME:
                ref = DataDomain.refByQualifiedName(qualifiedName);
                break;
            case DataProduct.TYPE_NAME:
                ref = DataProduct.refByQualifiedName(qualifiedName);
                break;
            case DataStudioAsset.TYPE_NAME:
                ref = DataStudioAsset.refByQualifiedName(qualifiedName);
                break;
            case Database.TYPE_NAME:
                ref = Database.refByQualifiedName(qualifiedName);
                break;
            case DatabricksUnityCatalogTag.TYPE_NAME:
                ref = DatabricksUnityCatalogTag.refByQualifiedName(qualifiedName);
                break;
            case DataverseAttribute.TYPE_NAME:
                ref = DataverseAttribute.refByQualifiedName(qualifiedName);
                break;
            case DataverseEntity.TYPE_NAME:
                ref = DataverseEntity.refByQualifiedName(qualifiedName);
                break;
            case DbtColumnProcess.TYPE_NAME:
                ref = DbtColumnProcess.refByQualifiedName(qualifiedName);
                break;
            case DbtMetric.TYPE_NAME:
                ref = DbtMetric.refByQualifiedName(qualifiedName);
                break;
            case DbtModel.TYPE_NAME:
                ref = DbtModel.refByQualifiedName(qualifiedName);
                break;
            case DbtModelColumn.TYPE_NAME:
                ref = DbtModelColumn.refByQualifiedName(qualifiedName);
                break;
            case DbtProcess.TYPE_NAME:
                ref = DbtProcess.refByQualifiedName(qualifiedName);
                break;
            case DbtSeed.TYPE_NAME:
                ref = DbtSeed.refByQualifiedName(qualifiedName);
                break;
            case DbtSource.TYPE_NAME:
                ref = DbtSource.refByQualifiedName(qualifiedName);
                break;
            case DbtTag.TYPE_NAME:
                ref = DbtTag.refByQualifiedName(qualifiedName);
                break;
            case DbtTest.TYPE_NAME:
                ref = DbtTest.refByQualifiedName(qualifiedName);
                break;
            case DocumentDBCollection.TYPE_NAME:
                ref = DocumentDBCollection.refByQualifiedName(qualifiedName);
                break;
            case DocumentDBDatabase.TYPE_NAME:
                ref = DocumentDBDatabase.refByQualifiedName(qualifiedName);
                break;
            case DomoCard.TYPE_NAME:
                ref = DomoCard.refByQualifiedName(qualifiedName);
                break;
            case DomoDashboard.TYPE_NAME:
                ref = DomoDashboard.refByQualifiedName(qualifiedName);
                break;
            case DomoDataset.TYPE_NAME:
                ref = DomoDataset.refByQualifiedName(qualifiedName);
                break;
            case DomoDatasetColumn.TYPE_NAME:
                ref = DomoDatasetColumn.refByQualifiedName(qualifiedName);
                break;
            case DynamoDBGlobalSecondaryIndex.TYPE_NAME:
                ref = DynamoDBGlobalSecondaryIndex.refByQualifiedName(qualifiedName);
                break;
            case DynamoDBLocalSecondaryIndex.TYPE_NAME:
                ref = DynamoDBLocalSecondaryIndex.refByQualifiedName(qualifiedName);
                break;
            case DynamoDBTable.TYPE_NAME:
                ref = DynamoDBTable.refByQualifiedName(qualifiedName);
                break;
            case File.TYPE_NAME:
                ref = File.refByQualifiedName(qualifiedName);
                break;
            case FivetranConnector.TYPE_NAME:
                ref = FivetranConnector.refByQualifiedName(qualifiedName);
                break;
            case FlowDataset.TYPE_NAME:
                ref = FlowDataset.refByQualifiedName(qualifiedName);
                break;
            case FlowField.TYPE_NAME:
                ref = FlowField.refByQualifiedName(qualifiedName);
                break;
            case Function.TYPE_NAME:
                ref = Function.refByQualifiedName(qualifiedName);
                break;
            case GCSBucket.TYPE_NAME:
                ref = GCSBucket.refByQualifiedName(qualifiedName);
                break;
            case GCSObject.TYPE_NAME:
                ref = GCSObject.refByQualifiedName(qualifiedName);
                break;
            case Insight.TYPE_NAME:
                ref = Insight.refByQualifiedName(qualifiedName);
                break;
            case KafkaConsumerGroup.TYPE_NAME:
                ref = KafkaConsumerGroup.refByQualifiedName(qualifiedName);
                break;
            case KafkaTopic.TYPE_NAME:
                ref = KafkaTopic.refByQualifiedName(qualifiedName);
                break;
            case Link.TYPE_NAME:
                ref = Link.refByQualifiedName(qualifiedName);
                break;
            case LookerDashboard.TYPE_NAME:
                ref = LookerDashboard.refByQualifiedName(qualifiedName);
                break;
            case LookerExplore.TYPE_NAME:
                ref = LookerExplore.refByQualifiedName(qualifiedName);
                break;
            case LookerField.TYPE_NAME:
                ref = LookerField.refByQualifiedName(qualifiedName);
                break;
            case LookerFolder.TYPE_NAME:
                ref = LookerFolder.refByQualifiedName(qualifiedName);
                break;
            case LookerLook.TYPE_NAME:
                ref = LookerLook.refByQualifiedName(qualifiedName);
                break;
            case LookerModel.TYPE_NAME:
                ref = LookerModel.refByQualifiedName(qualifiedName);
                break;
            case LookerProject.TYPE_NAME:
                ref = LookerProject.refByQualifiedName(qualifiedName);
                break;
            case LookerQuery.TYPE_NAME:
                ref = LookerQuery.refByQualifiedName(qualifiedName);
                break;
            case LookerTile.TYPE_NAME:
                ref = LookerTile.refByQualifiedName(qualifiedName);
                break;
            case LookerView.TYPE_NAME:
                ref = LookerView.refByQualifiedName(qualifiedName);
                break;
            case MCIncident.TYPE_NAME:
                ref = MCIncident.refByQualifiedName(qualifiedName);
                break;
            case MCMonitor.TYPE_NAME:
                ref = MCMonitor.refByQualifiedName(qualifiedName);
                break;
            case MaterializedView.TYPE_NAME:
                ref = MaterializedView.refByQualifiedName(qualifiedName);
                break;
            case MatillionComponent.TYPE_NAME:
                ref = MatillionComponent.refByQualifiedName(qualifiedName);
                break;
            case MatillionGroup.TYPE_NAME:
                ref = MatillionGroup.refByQualifiedName(qualifiedName);
                break;
            case MatillionJob.TYPE_NAME:
                ref = MatillionJob.refByQualifiedName(qualifiedName);
                break;
            case MatillionProject.TYPE_NAME:
                ref = MatillionProject.refByQualifiedName(qualifiedName);
                break;
            case MetabaseCollection.TYPE_NAME:
                ref = MetabaseCollection.refByQualifiedName(qualifiedName);
                break;
            case MetabaseDashboard.TYPE_NAME:
                ref = MetabaseDashboard.refByQualifiedName(qualifiedName);
                break;
            case MetabaseQuestion.TYPE_NAME:
                ref = MetabaseQuestion.refByQualifiedName(qualifiedName);
                break;
            case MicroStrategyAttribute.TYPE_NAME:
                ref = MicroStrategyAttribute.refByQualifiedName(qualifiedName);
                break;
            case MicroStrategyCube.TYPE_NAME:
                ref = MicroStrategyCube.refByQualifiedName(qualifiedName);
                break;
            case MicroStrategyDocument.TYPE_NAME:
                ref = MicroStrategyDocument.refByQualifiedName(qualifiedName);
                break;
            case MicroStrategyDossier.TYPE_NAME:
                ref = MicroStrategyDossier.refByQualifiedName(qualifiedName);
                break;
            case MicroStrategyFact.TYPE_NAME:
                ref = MicroStrategyFact.refByQualifiedName(qualifiedName);
                break;
            case MicroStrategyMetric.TYPE_NAME:
                ref = MicroStrategyMetric.refByQualifiedName(qualifiedName);
                break;
            case MicroStrategyProject.TYPE_NAME:
                ref = MicroStrategyProject.refByQualifiedName(qualifiedName);
                break;
            case MicroStrategyReport.TYPE_NAME:
                ref = MicroStrategyReport.refByQualifiedName(qualifiedName);
                break;
            case MicroStrategyVisualization.TYPE_NAME:
                ref = MicroStrategyVisualization.refByQualifiedName(qualifiedName);
                break;
            case ModeChart.TYPE_NAME:
                ref = ModeChart.refByQualifiedName(qualifiedName);
                break;
            case ModeCollection.TYPE_NAME:
                ref = ModeCollection.refByQualifiedName(qualifiedName);
                break;
            case ModeQuery.TYPE_NAME:
                ref = ModeQuery.refByQualifiedName(qualifiedName);
                break;
            case ModeReport.TYPE_NAME:
                ref = ModeReport.refByQualifiedName(qualifiedName);
                break;
            case ModeWorkspace.TYPE_NAME:
                ref = ModeWorkspace.refByQualifiedName(qualifiedName);
                break;
            case ModelAttribute.TYPE_NAME:
                ref = ModelAttribute.refByQualifiedName(qualifiedName);
                break;
            case ModelAttributeAssociation.TYPE_NAME:
                ref = ModelAttributeAssociation.refByQualifiedName(qualifiedName);
                break;
            case ModelDataModel.TYPE_NAME:
                ref = ModelDataModel.refByQualifiedName(qualifiedName);
                break;
            case ModelEntity.TYPE_NAME:
                ref = ModelEntity.refByQualifiedName(qualifiedName);
                break;
            case ModelEntityAssociation.TYPE_NAME:
                ref = ModelEntityAssociation.refByQualifiedName(qualifiedName);
                break;
            case ModelVersion.TYPE_NAME:
                ref = ModelVersion.refByQualifiedName(qualifiedName);
                break;
            case MongoDBCollection.TYPE_NAME:
                ref = MongoDBCollection.refByQualifiedName(qualifiedName);
                break;
            case MongoDBDatabase.TYPE_NAME:
                ref = MongoDBDatabase.refByQualifiedName(qualifiedName);
                break;
            case PowerBIApp.TYPE_NAME:
                ref = PowerBIApp.refByQualifiedName(qualifiedName);
                break;
            case PowerBIColumn.TYPE_NAME:
                ref = PowerBIColumn.refByQualifiedName(qualifiedName);
                break;
            case PowerBIDashboard.TYPE_NAME:
                ref = PowerBIDashboard.refByQualifiedName(qualifiedName);
                break;
            case PowerBIDataflow.TYPE_NAME:
                ref = PowerBIDataflow.refByQualifiedName(qualifiedName);
                break;
            case PowerBIDataflowEntityColumn.TYPE_NAME:
                ref = PowerBIDataflowEntityColumn.refByQualifiedName(qualifiedName);
                break;
            case PowerBIDataset.TYPE_NAME:
                ref = PowerBIDataset.refByQualifiedName(qualifiedName);
                break;
            case PowerBIDatasource.TYPE_NAME:
                ref = PowerBIDatasource.refByQualifiedName(qualifiedName);
                break;
            case PowerBIMeasure.TYPE_NAME:
                ref = PowerBIMeasure.refByQualifiedName(qualifiedName);
                break;
            case PowerBIPage.TYPE_NAME:
                ref = PowerBIPage.refByQualifiedName(qualifiedName);
                break;
            case PowerBIReport.TYPE_NAME:
                ref = PowerBIReport.refByQualifiedName(qualifiedName);
                break;
            case PowerBITable.TYPE_NAME:
                ref = PowerBITable.refByQualifiedName(qualifiedName);
                break;
            case PowerBITile.TYPE_NAME:
                ref = PowerBITile.refByQualifiedName(qualifiedName);
                break;
            case PowerBIWorkspace.TYPE_NAME:
                ref = PowerBIWorkspace.refByQualifiedName(qualifiedName);
                break;
            case PresetChart.TYPE_NAME:
                ref = PresetChart.refByQualifiedName(qualifiedName);
                break;
            case PresetDashboard.TYPE_NAME:
                ref = PresetDashboard.refByQualifiedName(qualifiedName);
                break;
            case PresetDataset.TYPE_NAME:
                ref = PresetDataset.refByQualifiedName(qualifiedName);
                break;
            case PresetWorkspace.TYPE_NAME:
                ref = PresetWorkspace.refByQualifiedName(qualifiedName);
                break;
            case Procedure.TYPE_NAME:
                ref = Procedure.refByQualifiedName(qualifiedName);
                break;
            case QlikApp.TYPE_NAME:
                ref = QlikApp.refByQualifiedName(qualifiedName);
                break;
            case QlikChart.TYPE_NAME:
                ref = QlikChart.refByQualifiedName(qualifiedName);
                break;
            case QlikDataset.TYPE_NAME:
                ref = QlikDataset.refByQualifiedName(qualifiedName);
                break;
            case QlikSheet.TYPE_NAME:
                ref = QlikSheet.refByQualifiedName(qualifiedName);
                break;
            case QlikSpace.TYPE_NAME:
                ref = QlikSpace.refByQualifiedName(qualifiedName);
                break;
            case QlikStream.TYPE_NAME:
                ref = QlikStream.refByQualifiedName(qualifiedName);
                break;
            case QuickSightAnalysis.TYPE_NAME:
                ref = QuickSightAnalysis.refByQualifiedName(qualifiedName);
                break;
            case QuickSightAnalysisVisual.TYPE_NAME:
                ref = QuickSightAnalysisVisual.refByQualifiedName(qualifiedName);
                break;
            case QuickSightDashboard.TYPE_NAME:
                ref = QuickSightDashboard.refByQualifiedName(qualifiedName);
                break;
            case QuickSightDashboardVisual.TYPE_NAME:
                ref = QuickSightDashboardVisual.refByQualifiedName(qualifiedName);
                break;
            case QuickSightDataset.TYPE_NAME:
                ref = QuickSightDataset.refByQualifiedName(qualifiedName);
                break;
            case QuickSightDatasetField.TYPE_NAME:
                ref = QuickSightDatasetField.refByQualifiedName(qualifiedName);
                break;
            case QuickSightFolder.TYPE_NAME:
                ref = QuickSightFolder.refByQualifiedName(qualifiedName);
                break;
            case Readme.TYPE_NAME:
                ref = Readme.refByQualifiedName(qualifiedName);
                break;
            case ReadmeTemplate.TYPE_NAME:
                ref = ReadmeTemplate.refByQualifiedName(qualifiedName);
                break;
            case RedashDashboard.TYPE_NAME:
                ref = RedashDashboard.refByQualifiedName(qualifiedName);
                break;
            case RedashQuery.TYPE_NAME:
                ref = RedashQuery.refByQualifiedName(qualifiedName);
                break;
            case RedashVisualization.TYPE_NAME:
                ref = RedashVisualization.refByQualifiedName(qualifiedName);
                break;
            case S3Bucket.TYPE_NAME:
                ref = S3Bucket.refByQualifiedName(qualifiedName);
                break;
            case S3Object.TYPE_NAME:
                ref = S3Object.refByQualifiedName(qualifiedName);
                break;
            case SalesforceDashboard.TYPE_NAME:
                ref = SalesforceDashboard.refByQualifiedName(qualifiedName);
                break;
            case SalesforceField.TYPE_NAME:
                ref = SalesforceField.refByQualifiedName(qualifiedName);
                break;
            case SalesforceObject.TYPE_NAME:
                ref = SalesforceObject.refByQualifiedName(qualifiedName);
                break;
            case SalesforceOrganization.TYPE_NAME:
                ref = SalesforceOrganization.refByQualifiedName(qualifiedName);
                break;
            case SalesforceReport.TYPE_NAME:
                ref = SalesforceReport.refByQualifiedName(qualifiedName);
                break;
            case SapErpAbapProgram.TYPE_NAME:
                ref = SapErpAbapProgram.refByQualifiedName(qualifiedName);
                break;
            case SapErpCdsView.TYPE_NAME:
                ref = SapErpCdsView.refByQualifiedName(qualifiedName);
                break;
            case SapErpColumn.TYPE_NAME:
                ref = SapErpColumn.refByQualifiedName(qualifiedName);
                break;
            case SapErpComponent.TYPE_NAME:
                ref = SapErpComponent.refByQualifiedName(qualifiedName);
                break;
            case SapErpFunctionModule.TYPE_NAME:
                ref = SapErpFunctionModule.refByQualifiedName(qualifiedName);
                break;
            case SapErpTable.TYPE_NAME:
                ref = SapErpTable.refByQualifiedName(qualifiedName);
                break;
            case SapErpTransactionCode.TYPE_NAME:
                ref = SapErpTransactionCode.refByQualifiedName(qualifiedName);
                break;
            case SapErpView.TYPE_NAME:
                ref = SapErpView.refByQualifiedName(qualifiedName);
                break;
            case Schema.TYPE_NAME:
                ref = Schema.refByQualifiedName(qualifiedName);
                break;
            case SchemaRegistrySubject.TYPE_NAME:
                ref = SchemaRegistrySubject.refByQualifiedName(qualifiedName);
                break;
            case SigmaDataElement.TYPE_NAME:
                ref = SigmaDataElement.refByQualifiedName(qualifiedName);
                break;
            case SigmaDataElementField.TYPE_NAME:
                ref = SigmaDataElementField.refByQualifiedName(qualifiedName);
                break;
            case SigmaDataset.TYPE_NAME:
                ref = SigmaDataset.refByQualifiedName(qualifiedName);
                break;
            case SigmaDatasetColumn.TYPE_NAME:
                ref = SigmaDatasetColumn.refByQualifiedName(qualifiedName);
                break;
            case SigmaPage.TYPE_NAME:
                ref = SigmaPage.refByQualifiedName(qualifiedName);
                break;
            case SigmaWorkbook.TYPE_NAME:
                ref = SigmaWorkbook.refByQualifiedName(qualifiedName);
                break;
            case SisenseDashboard.TYPE_NAME:
                ref = SisenseDashboard.refByQualifiedName(qualifiedName);
                break;
            case SisenseDatamodel.TYPE_NAME:
                ref = SisenseDatamodel.refByQualifiedName(qualifiedName);
                break;
            case SisenseDatamodelTable.TYPE_NAME:
                ref = SisenseDatamodelTable.refByQualifiedName(qualifiedName);
                break;
            case SisenseFolder.TYPE_NAME:
                ref = SisenseFolder.refByQualifiedName(qualifiedName);
                break;
            case SisenseWidget.TYPE_NAME:
                ref = SisenseWidget.refByQualifiedName(qualifiedName);
                break;
            case SnowflakeDynamicTable.TYPE_NAME:
                ref = SnowflakeDynamicTable.refByQualifiedName(qualifiedName);
                break;
            case SnowflakePipe.TYPE_NAME:
                ref = SnowflakePipe.refByQualifiedName(qualifiedName);
                break;
            case SnowflakeStage.TYPE_NAME:
                ref = SnowflakeStage.refByQualifiedName(qualifiedName);
                break;
            case SnowflakeStream.TYPE_NAME:
                ref = SnowflakeStream.refByQualifiedName(qualifiedName);
                break;
            case SnowflakeTag.TYPE_NAME:
                ref = SnowflakeTag.refByQualifiedName(qualifiedName);
                break;
            case SodaCheck.TYPE_NAME:
                ref = SodaCheck.refByQualifiedName(qualifiedName);
                break;
            case SourceTag.TYPE_NAME:
                ref = SourceTag.refByQualifiedName(qualifiedName);
                break;
            case SparkJob.TYPE_NAME:
                ref = SparkJob.refByQualifiedName(qualifiedName);
                break;
            case SupersetChart.TYPE_NAME:
                ref = SupersetChart.refByQualifiedName(qualifiedName);
                break;
            case SupersetDashboard.TYPE_NAME:
                ref = SupersetDashboard.refByQualifiedName(qualifiedName);
                break;
            case SupersetDataset.TYPE_NAME:
                ref = SupersetDataset.refByQualifiedName(qualifiedName);
                break;
            case Table.TYPE_NAME:
                ref = Table.refByQualifiedName(qualifiedName);
                break;
            case TablePartition.TYPE_NAME:
                ref = TablePartition.refByQualifiedName(qualifiedName);
                break;
            case TableauCalculatedField.TYPE_NAME:
                ref = TableauCalculatedField.refByQualifiedName(qualifiedName);
                break;
            case TableauDashboard.TYPE_NAME:
                ref = TableauDashboard.refByQualifiedName(qualifiedName);
                break;
            case TableauDashboardField.TYPE_NAME:
                ref = TableauDashboardField.refByQualifiedName(qualifiedName);
                break;
            case TableauDatasource.TYPE_NAME:
                ref = TableauDatasource.refByQualifiedName(qualifiedName);
                break;
            case TableauDatasourceField.TYPE_NAME:
                ref = TableauDatasourceField.refByQualifiedName(qualifiedName);
                break;
            case TableauFlow.TYPE_NAME:
                ref = TableauFlow.refByQualifiedName(qualifiedName);
                break;
            case TableauMetric.TYPE_NAME:
                ref = TableauMetric.refByQualifiedName(qualifiedName);
                break;
            case TableauProject.TYPE_NAME:
                ref = TableauProject.refByQualifiedName(qualifiedName);
                break;
            case TableauSite.TYPE_NAME:
                ref = TableauSite.refByQualifiedName(qualifiedName);
                break;
            case TableauWorkbook.TYPE_NAME:
                ref = TableauWorkbook.refByQualifiedName(qualifiedName);
                break;
            case TableauWorksheet.TYPE_NAME:
                ref = TableauWorksheet.refByQualifiedName(qualifiedName);
                break;
            case TableauWorksheetField.TYPE_NAME:
                ref = TableauWorksheetField.refByQualifiedName(qualifiedName);
                break;
            case ThoughtspotAnswer.TYPE_NAME:
                ref = ThoughtspotAnswer.refByQualifiedName(qualifiedName);
                break;
            case ThoughtspotColumn.TYPE_NAME:
                ref = ThoughtspotColumn.refByQualifiedName(qualifiedName);
                break;
            case ThoughtspotDashlet.TYPE_NAME:
                ref = ThoughtspotDashlet.refByQualifiedName(qualifiedName);
                break;
            case ThoughtspotLiveboard.TYPE_NAME:
                ref = ThoughtspotLiveboard.refByQualifiedName(qualifiedName);
                break;
            case ThoughtspotTable.TYPE_NAME:
                ref = ThoughtspotTable.refByQualifiedName(qualifiedName);
                break;
            case ThoughtspotView.TYPE_NAME:
                ref = ThoughtspotView.refByQualifiedName(qualifiedName);
                break;
            case ThoughtspotWorksheet.TYPE_NAME:
                ref = ThoughtspotWorksheet.refByQualifiedName(qualifiedName);
                break;
            case View.TYPE_NAME:
                ref = View.refByQualifiedName(qualifiedName);
                break;
            default:
                // Do nothing — not a supported Catalog subtype
                break;
        }
        return ref;
    }

    /** List of groups who administer this asset. (This is only used for certain asset types.) */
    SortedSet<String> getAdminGroups();

    /** List of roles who administer this asset. (This is only used for Connection assets.) */
    SortedSet<String> getAdminRoles();

    /** List of users who administer this asset. (This is only used for certain asset types.) */
    SortedSet<String> getAdminUsers();

    /** Detailed message to include in the announcement on this asset. */
    String getAnnouncementMessage();

    /** Brief title for the announcement on this asset. Required when announcementType is specified. */
    String getAnnouncementTitle();

    /** Type of announcement on this asset. */
    AtlanAnnouncementType getAnnouncementType();

    /** Time (epoch) at which the announcement was last updated, in milliseconds. */
    Long getAnnouncementUpdatedAt();

    /** Name of the user who last updated the announcement. */
    String getAnnouncementUpdatedBy();

    /** Checks that run on this asset. */
    SortedSet<IAnomaloCheck> getAnomaloChecks();

    /** Application owning the Asset. */
    IApplication getApplication();

    /** ApplicationField owning the Asset. */
    IApplicationField getApplicationField();

    /** Qualified name of the ApplicationField that contains this asset. */
    String getApplicationFieldQualifiedName();

    /** Qualified name of the Application that contains this asset. */
    String getApplicationQualifiedName();

    /** All associated Anomalo check types. */
    SortedSet<String> getAssetAnomaloAppliedCheckTypes();

    /** Total number of checks present in Anomalo for this asset. */
    Long getAssetAnomaloCheckCount();

    /** Stringified JSON object containing status of all Anomalo checks associated to this asset. */
    String getAssetAnomaloCheckStatuses();

    /** Status of data quality from Anomalo. */
    String getAssetAnomaloDQStatus();

    /** Total number of checks failed in Anomalo for this asset. */
    Long getAssetAnomaloFailedCheckCount();

    /** All associated Anomalo failed check types. */
    SortedSet<String> getAssetAnomaloFailedCheckTypes();

    /** Time (epoch) at which the last check was run via Anomalo. */
    Long getAssetAnomaloLastCheckRunAt();

    /** URL of the source in Anomalo. */
    String getAssetAnomaloSourceUrl();

    /** Cover image to use for this asset in the UI (applicable to only a few asset types). */
    String getAssetCoverImage();

    /** Name of the account in which this asset exists in dbt. */
    String getAssetDbtAccountName();

    /** Alias of this asset in dbt. */
    String getAssetDbtAlias();

    /** Version of the environment in which this asset is materialized in dbt. */
    String getAssetDbtEnvironmentDbtVersion();

    /** Name of the environment in which this asset is materialized in dbt. */
    String getAssetDbtEnvironmentName();

    /** Time (epoch) at which the job that materialized this asset in dbt last ran, in milliseconds. */
    Long getAssetDbtJobLastRun();

    /** Path in S3 to the artifacts saved from the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunArtifactS3Path();

    /** Whether artifacts were saved from the last run of the job that materialized this asset in dbt (true) or not (false). */
    Boolean getAssetDbtJobLastRunArtifactsSaved();

    /** Time (epoch) at which the job that materialized this asset in dbt was last created, in milliseconds. */
    Long getAssetDbtJobLastRunCreatedAt();

    /** Time (epoch) at which the job that materialized this asset in dbt was dequeued, in milliseconds. */
    Long getAssetDbtJobLastRunDequedAt();

    /** Thread ID of the user who executed the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunExecutedByThreadId();

    /** Branch in git from which the last run of the job that materialized this asset in dbt ran. */
    String getAssetDbtJobLastRunGitBranch();

    /** SHA hash in git for the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunGitSha();

    /** Whether docs were generated from the last run of the job that materialized this asset in dbt (true) or not (false). */
    Boolean getAssetDbtJobLastRunHasDocsGenerated();

    /** Whether sources were generated from the last run of the job that materialized this asset in dbt (true) or not (false). */
    Boolean getAssetDbtJobLastRunHasSourcesGenerated();

    /** Whether notifications were sent from the last run of the job that materialized this asset in dbt (true) or not (false). */
    Boolean getAssetDbtJobLastRunNotificationsSent();

    /** Thread ID of the owner of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunOwnerThreadId();

    /** Total duration the job that materialized this asset in dbt spent being queued. */
    String getAssetDbtJobLastRunQueuedDuration();

    /** Human-readable total duration of the last run of the job that materialized this asset in dbt spend being queued. */
    String getAssetDbtJobLastRunQueuedDurationHumanized();

    /** Run duration of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunRunDuration();

    /** Human-readable run duration of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunRunDurationHumanized();

    /** Time (epoch) at which the job that materialized this asset in dbt was started running, in milliseconds. */
    Long getAssetDbtJobLastRunStartedAt();

    /** Status message of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunStatusMessage();

    /** Total duration of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunTotalDuration();

    /** Human-readable total duration of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunTotalDurationHumanized();

    /** Time (epoch) at which the job that materialized this asset in dbt was last updated, in milliseconds. */
    Long getAssetDbtJobLastRunUpdatedAt();

    /** URL of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunUrl();

    /** Name of the job that materialized this asset in dbt. */
    String getAssetDbtJobName();

    /** Time (epoch) when the next run of the job that materializes this asset in dbt is scheduled. */
    Long getAssetDbtJobNextRun();

    /** Human-readable time when the next run of the job that materializes this asset in dbt is scheduled. */
    String getAssetDbtJobNextRunHumanized();

    /** Schedule of the job that materialized this asset in dbt. */
    String getAssetDbtJobSchedule();

    /** Human-readable cron schedule of the job that materialized this asset in dbt. */
    String getAssetDbtJobScheduleCronHumanized();

    /** Status of the job that materialized this asset in dbt. */
    String getAssetDbtJobStatus();

    /** Metadata for this asset in dbt, specifically everything under the 'meta' key in the dbt object. */
    String getAssetDbtMeta();

    /** Name of the package in which this asset exists in dbt. */
    String getAssetDbtPackageName();

    /** Name of the project in which this asset exists in dbt. */
    String getAssetDbtProjectName();

    /** URL of the semantic layer proxy for this asset in dbt. */
    String getAssetDbtSemanticLayerProxyUrl();

    /** Freshness criteria for the source of this asset in dbt. */
    String getAssetDbtSourceFreshnessCriteria();

    /** List of tags attached to this asset in dbt. */
    SortedSet<String> getAssetDbtTags();

    /** All associated dbt test statuses. */
    String getAssetDbtTestStatus();

    /** Unique identifier of this asset in dbt. */
    String getAssetDbtUniqueId();

    /** Name of the DBT workflow in Atlan that last updated the asset. */
    String getAssetDbtWorkflowLastUpdated();

    /** Name of the icon to use for this asset. (Only applies to glossaries, currently.) */
    AtlanIcon getAssetIcon();

    /** List of unique Monte Carlo alert names attached to this asset. */
    SortedSet<String> getAssetMcAlertQualifiedNames();

    /** List of Monte Carlo incident names attached to this asset. */
    SortedSet<String> getAssetMcIncidentNames();

    /** List of Monte Carlo incident priorities associated with this asset. */
    SortedSet<String> getAssetMcIncidentPriorities();

    /** List of unique Monte Carlo incident names attached to this asset. */
    SortedSet<String> getAssetMcIncidentQualifiedNames();

    /** List of Monte Carlo incident severities associated with this asset. */
    SortedSet<String> getAssetMcIncidentSeverities();

    /** List of Monte Carlo incident states associated with this asset. */
    SortedSet<String> getAssetMcIncidentStates();

    /** List of Monte Carlo incident sub-types associated with this asset. */
    SortedSet<String> getAssetMcIncidentSubTypes();

    /** List of Monte Carlo incident types associated with this asset. */
    SortedSet<String> getAssetMcIncidentTypes();

    /** Tracks whether this asset is monitored by MC or not */
    Boolean getAssetMcIsMonitored();

    /** Time (epoch) at which this asset was last synced from Monte Carlo. */
    Long getAssetMcLastSyncRunAt();

    /** List of Monte Carlo monitor names attached to this asset. */
    SortedSet<String> getAssetMcMonitorNames();

    /** List of unique Monte Carlo monitor names attached to this asset. */
    SortedSet<String> getAssetMcMonitorQualifiedNames();

    /** Schedules of all associated Monte Carlo monitors. */
    SortedSet<String> getAssetMcMonitorScheduleTypes();

    /** Statuses of all associated Monte Carlo monitors. */
    SortedSet<String> getAssetMcMonitorStatuses();

    /** Types of all associated Monte Carlo monitors. */
    SortedSet<String> getAssetMcMonitorTypes();

    /** Count of policies inside the asset */
    Long getAssetPoliciesCount();

    /** Array of policy ids governing this asset */
    SortedSet<String> getAssetPolicyGUIDs();

    /** Array of asset ids that equivalent to this asset. */
    SortedSet<String> getAssetRedirectGUIDs();

    /** Number of checks done via Soda. */
    Long getAssetSodaCheckCount();

    /** All associated Soda check statuses. */
    String getAssetSodaCheckStatuses();

    /** Status of data quality from Soda. */
    String getAssetSodaDQStatus();

    /** TBC */
    Long getAssetSodaLastScanAt();

    /** TBC */
    Long getAssetSodaLastSyncRunAt();

    /** TBC */
    String getAssetSodaSourceURL();

    /** List of tags attached to this asset. */
    SortedSet<String> getAssetTags();

    /** Color (in hexadecimal RGB) to use to represent this asset. */
    String getAssetThemeHex();

    /** Name to use for this type of asset, as a subtype of the actual typeName. */
    String getAssetUserDefinedType();

    /** Glossary terms that are linked to this asset. */
    SortedSet<IGlossaryTerm> getAssignedTerms();

    /** Status of this asset's certification. */
    CertificateStatus getCertificateStatus();

    /** Human-readable descriptive message used to provide further detail to certificateStatus. */
    String getCertificateStatusMessage();

    /** Time (epoch) at which the certification was last updated, in milliseconds. */
    Long getCertificateUpdatedAt();

    /** Name of the user who last updated the certification of this asset. */
    String getCertificateUpdatedBy();

    /** Simple name of the connection through which this asset is accessible. */
    String getConnectionName();

    /** Unique name of the connection through which this asset is accessible. */
    String getConnectionQualifiedName();

    /** Type of the connector through which this asset is accessible. */
    String getConnectorName();

    /** Latest version of the data contract (in any status) for this asset. */
    IDataContract getDataContractLatest();

    /** Latest certified version of the data contract for this asset. */
    IDataContract getDataContractLatestCertified();

    /** Unique name of this asset in dbt. */
    String getDbtQualifiedName();

    /** Description of this asset, for example as crawled from a source. Fallback for display purposes, if userDescription is empty. */
    String getDescription();

    /** Human-readable name of this asset used for display purposes (in user interface). */
    String getDisplayName();

    /** Array of domain guids linked to this asset */
    SortedSet<String> getDomainGUIDs();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** Whether this asset has contract (true) or not (false). */
    Boolean getHasContract();

    /** Whether this asset has lineage (true) or not (false). */
    Boolean getHasLineage();

    /** Data products for which this asset is an input port. */
    SortedSet<IDataProduct> getInputPortDataProducts();

    /** Tasks to which this asset provides input. */
    SortedSet<IAirflowTask> getInputToAirflowTasks();

    /** Processes to which this asset provides input. */
    SortedSet<ILineageProcess> getInputToProcesses();

    /** TBC */
    SortedSet<ISparkJob> getInputToSparkJobs();

    /** TBC */
    Boolean getIsAIGenerated();

    /** Whether this asset is discoverable through the UI (true) or not (false). */
    Boolean getIsDiscoverable();

    /** Whether this asset can be edited in the UI (true) or not (false). */
    Boolean getIsEditable();

    /** Indicates this asset is not fully-known, if true. */
    Boolean getIsPartial();

    /** Time (epoch) of the last operation that inserted, updated, or deleted rows, in milliseconds. */
    Long getLastRowChangedAt();

    /** Name of the last run of the crawler that last synchronized this asset. */
    String getLastSyncRun();

    /** Time (epoch) at which this asset was last crawled, in milliseconds. */
    Long getLastSyncRunAt();

    /** Name of the crawler that last synchronized this asset. */
    String getLastSyncWorkflowName();

    /** Custom order for sorting purpose, managed by client */
    String getLexicographicalSortOrder();

    /** Links that are attached to this asset. */
    SortedSet<ILink> getLinks();

    /** TBC */
    SortedSet<IMCIncident> getMcIncidents();

    /** Monitors that observe this asset. */
    SortedSet<IMCMonitor> getMcMonitors();

    /** TBC */
    SortedSet<IMetric> getMetrics();

    /** Attributes implemented by this asset. */
    SortedSet<IModelAttribute> getModelImplementedAttributes();

    /** Entities implemented by this asset. */
    SortedSet<IModelEntity> getModelImplementedEntities();

    /** Name of this asset. Fallback for display purposes, if displayName is empty. */
    String getName();

    /** Array of policy ids non-compliant to this asset */
    SortedSet<String> getNonCompliantAssetPolicyGUIDs();

    /** Tasks from which this asset is output. */
    SortedSet<IAirflowTask> getOutputFromAirflowTasks();

    /** Processes from which this asset is produced as output. */
    SortedSet<ILineageProcess> getOutputFromProcesses();

    /** TBC */
    SortedSet<ISparkJob> getOutputFromSparkJobs();

    /** Data products for which this asset is an output port. */
    SortedSet<IDataProduct> getOutputPortDataProducts();

    /** Array of product guids which have this asset as outputPort */
    SortedSet<String> getOutputProductGUIDs();

    /** List of groups who own this asset. */
    SortedSet<String> getOwnerGroups();

    /** List of users who own this asset. */
    SortedSet<String> getOwnerUsers();

    /** Popularity score for this asset. */
    Double getPopularityScore();

    /** Array of product guids linked to this asset */
    SortedSet<String> getProductGUIDs();

    /** TBC */
    String getQualifiedName();

    /** README that is linked to this asset. */
    IReadme getReadme();

    /** URL for sample data for this asset. */
    String getSampleDataUrl();

    /** TBC */
    SortedSet<ISchemaRegistrySubject> getSchemaRegistrySubjects();

    /** TBC */
    SortedSet<ISodaCheck> getSodaChecks();

    /** The unit of measure for sourceTotalCost. */
    SourceCostUnitType getSourceCostUnit();

    /** Time (epoch) at which this asset was created in the source system, in milliseconds. */
    Long getSourceCreatedAt();

    /** Name of the user who created this asset, in the source system. */
    String getSourceCreatedBy();

    /** URL to create an embed for a resource (for example, an image of a dashboard) within Atlan. */
    String getSourceEmbedURL();

    /** Timestamp of most recent read operation. */
    Long getSourceLastReadAt();

    /** List of owners of this asset, in the source system. */
    String getSourceOwners();

    /** List of most expensive warehouses with extra insights. */
    List<PopularityInsights> getSourceQueryComputeCostRecords();

    /** List of most expensive warehouse names. */
    SortedSet<String> getSourceQueryComputeCosts();

    /** Total count of all read operations at source. */
    Long getSourceReadCount();

    /** List of the most expensive queries that accessed this asset. */
    List<PopularityInsights> getSourceReadExpensiveQueryRecords();

    /** List of the most popular queries that accessed this asset. */
    List<PopularityInsights> getSourceReadPopularQueryRecords();

    /** Total cost of read queries at source. */
    Double getSourceReadQueryCost();

    /** List of usernames with extra insights for the most recent users who read this asset. */
    List<PopularityInsights> getSourceReadRecentUserRecords();

    /** List of usernames of the most recent users who read this asset. */
    SortedSet<String> getSourceReadRecentUsers();

    /** List of the slowest queries that accessed this asset. */
    List<PopularityInsights> getSourceReadSlowQueryRecords();

    /** List of usernames with extra insights for the users who read this asset the most. */
    List<PopularityInsights> getSourceReadTopUserRecords();

    /** List of usernames of the users who read this asset the most. */
    SortedSet<String> getSourceReadTopUsers();

    /** Total number of unique users that read data from asset. */
    Long getSourceReadUserCount();

    /** Total cost of all operations at source. */
    Double getSourceTotalCost();

    /** URL to the resource within the source application, used to create a button to view this asset in the source application. */
    String getSourceURL();

    /** Time (epoch) at which this asset was last updated in the source system, in milliseconds. */
    Long getSourceUpdatedAt();

    /** Name of the user who last updated this asset, in the source system. */
    String getSourceUpdatedBy();

    /** Users who have starred this asset. */
    SortedSet<String> getStarredBy();

    /** Number of users who have starred this asset. */
    Integer getStarredCount();

    /** List of usernames with extra information of the users who have starred an asset. */
    List<StarredDetails> getStarredDetails();

    /** Subtype of this asset. */
    String getSubType();

    /** Name of the Atlan workspace in which this asset exists. */
    String getTenantId();

    /** TBC */
    SortedSet<IAsset> getUserDefRelationshipFroms();

    /** TBC */
    SortedSet<IAsset> getUserDefRelationshipTos();

    /** Description of this asset, as provided by a user. If present, this will be used for the description in user interface. */
    String getUserDescription();

    /** View score for this asset. */
    Double getViewScore();

    /** List of groups who can view assets contained in a collection. (This is only used for certain asset types.) */
    SortedSet<String> getViewerGroups();

    /** List of users who can view assets contained in a collection. (This is only used for certain asset types.) */
    SortedSet<String> getViewerUsers();

    /** URL of an icon to use for this asset. (Only applies to CustomEntity and Fivetran Catalog assets, currently.) */
    String getIconUrl();

    /** Built-in connector type through which this asset is accessible. */
    AtlanConnectorType getConnectorType();

    /** Custom connector type through which this asset is accessible. */
    String getCustomConnectorType();

    /** Name of the type that defines the asset. */
    String getTypeName();

    /** Globally-unique identifier for the asset. */
    String getGuid();

    /** Human-readable name of the asset. */
    String getDisplayText();

    /** Status of the asset (if this is a related asset). */
    String getEntityStatus();

    /** Type of the relationship (if this is a related asset). */
    String getRelationshipType();

    /** Unique identifier of the relationship (when this is a related asset). */
    String getRelationshipGuid();

    /** Status of the relationship (when this is a related asset). */
    AtlanStatus getRelationshipStatus();

    /** Attributes specific to the relationship (unused). */
    RelationshipAttributes getRelationshipAttributes();

    /**
     * Attribute(s) that uniquely identify the asset (when this is a related asset).
     * If the guid is not provided, these must be provided.
     */
    UniqueAttributes getUniqueAttributes();

    /**
     * When true, indicates that this object represents a complete view of the entity.
     * When false, this object is only a reference or some partial view of the entity.
     */
    boolean isComplete();

    /**
     * Indicates whether this object can be used as a valid reference by GUID.
     * @return true if it is a valid GUID reference, false otherwise
     */
    boolean isValidReferenceByGuid();

    /**
     * Indicates whether this object can be used as a valid reference by qualifiedName.
     * @return true if it is a valid qualifiedName reference, false otherwise
     */
    boolean isValidReferenceByQualifiedName();
}
