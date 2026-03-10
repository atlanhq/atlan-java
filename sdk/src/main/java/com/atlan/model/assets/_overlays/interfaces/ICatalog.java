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
            case AppWorkflowRun.TYPE_NAME:
                ref = AppWorkflowRun.refByQualifiedName(qualifiedName);
                break;
            case Application.TYPE_NAME:
                ref = Application.refByQualifiedName(qualifiedName);
                break;
            case ApplicationField.TYPE_NAME:
                ref = ApplicationField.refByQualifiedName(qualifiedName);
                break;
            case AtlanAppDeployment.TYPE_NAME:
                ref = AtlanAppDeployment.refByQualifiedName(qualifiedName);
                break;
            case AtlanAppInstalled.TYPE_NAME:
                ref = AtlanAppInstalled.refByQualifiedName(qualifiedName);
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
            case BigqueryRoutine.TYPE_NAME:
                ref = BigqueryRoutine.refByQualifiedName(qualifiedName);
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
            case CognosColumn.TYPE_NAME:
                ref = CognosColumn.refByQualifiedName(qualifiedName);
                break;
            case CognosDashboard.TYPE_NAME:
                ref = CognosDashboard.refByQualifiedName(qualifiedName);
                break;
            case CognosDataset.TYPE_NAME:
                ref = CognosDataset.refByQualifiedName(qualifiedName);
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
            case DataQualityRule.TYPE_NAME:
                ref = DataQualityRule.refByQualifiedName(qualifiedName);
                break;
            case DataQualityRuleTemplate.TYPE_NAME:
                ref = DataQualityRuleTemplate.refByQualifiedName(qualifiedName);
                break;
            case DataStudioAsset.TYPE_NAME:
                ref = DataStudioAsset.refByQualifiedName(qualifiedName);
                break;
            case Database.TYPE_NAME:
                ref = Database.refByQualifiedName(qualifiedName);
                break;
            case DatabricksAIModelContext.TYPE_NAME:
                ref = DatabricksAIModelContext.refByQualifiedName(qualifiedName);
                break;
            case DatabricksAIModelVersion.TYPE_NAME:
                ref = DatabricksAIModelVersion.refByQualifiedName(qualifiedName);
                break;
            case DatabricksExternalLocation.TYPE_NAME:
                ref = DatabricksExternalLocation.refByQualifiedName(qualifiedName);
                break;
            case DatabricksExternalLocationPath.TYPE_NAME:
                ref = DatabricksExternalLocationPath.refByQualifiedName(qualifiedName);
                break;
            case DatabricksMetricView.TYPE_NAME:
                ref = DatabricksMetricView.refByQualifiedName(qualifiedName);
                break;
            case DatabricksNotebook.TYPE_NAME:
                ref = DatabricksNotebook.refByQualifiedName(qualifiedName);
                break;
            case DatabricksUnityCatalogTag.TYPE_NAME:
                ref = DatabricksUnityCatalogTag.refByQualifiedName(qualifiedName);
                break;
            case DatabricksVolume.TYPE_NAME:
                ref = DatabricksVolume.refByQualifiedName(qualifiedName);
                break;
            case DatabricksVolumePath.TYPE_NAME:
                ref = DatabricksVolumePath.refByQualifiedName(qualifiedName);
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
            case DremioColumn.TYPE_NAME:
                ref = DremioColumn.refByQualifiedName(qualifiedName);
                break;
            case DremioFolder.TYPE_NAME:
                ref = DremioFolder.refByQualifiedName(qualifiedName);
                break;
            case DremioPhysicalDataset.TYPE_NAME:
                ref = DremioPhysicalDataset.refByQualifiedName(qualifiedName);
                break;
            case DremioSource.TYPE_NAME:
                ref = DremioSource.refByQualifiedName(qualifiedName);
                break;
            case DremioSpace.TYPE_NAME:
                ref = DremioSpace.refByQualifiedName(qualifiedName);
                break;
            case DremioVirtualDataset.TYPE_NAME:
                ref = DremioVirtualDataset.refByQualifiedName(qualifiedName);
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
            case FabricActivity.TYPE_NAME:
                ref = FabricActivity.refByQualifiedName(qualifiedName);
                break;
            case FabricDashboard.TYPE_NAME:
                ref = FabricDashboard.refByQualifiedName(qualifiedName);
                break;
            case FabricDataPipeline.TYPE_NAME:
                ref = FabricDataPipeline.refByQualifiedName(qualifiedName);
                break;
            case FabricDataflow.TYPE_NAME:
                ref = FabricDataflow.refByQualifiedName(qualifiedName);
                break;
            case FabricDataflowEntityColumn.TYPE_NAME:
                ref = FabricDataflowEntityColumn.refByQualifiedName(qualifiedName);
                break;
            case FabricPage.TYPE_NAME:
                ref = FabricPage.refByQualifiedName(qualifiedName);
                break;
            case FabricReport.TYPE_NAME:
                ref = FabricReport.refByQualifiedName(qualifiedName);
                break;
            case FabricSemanticModel.TYPE_NAME:
                ref = FabricSemanticModel.refByQualifiedName(qualifiedName);
                break;
            case FabricSemanticModelTable.TYPE_NAME:
                ref = FabricSemanticModelTable.refByQualifiedName(qualifiedName);
                break;
            case FabricSemanticModelTableColumn.TYPE_NAME:
                ref = FabricSemanticModelTableColumn.refByQualifiedName(qualifiedName);
                break;
            case FabricVisual.TYPE_NAME:
                ref = FabricVisual.refByQualifiedName(qualifiedName);
                break;
            case FabricWorkspace.TYPE_NAME:
                ref = FabricWorkspace.refByQualifiedName(qualifiedName);
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
            case MicroStrategyColumn.TYPE_NAME:
                ref = MicroStrategyColumn.refByQualifiedName(qualifiedName);
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
            case PartialField.TYPE_NAME:
                ref = PartialField.refByQualifiedName(qualifiedName);
                break;
            case PartialObject.TYPE_NAME:
                ref = PartialObject.refByQualifiedName(qualifiedName);
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
            case QlikColumn.TYPE_NAME:
                ref = QlikColumn.refByQualifiedName(qualifiedName);
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
            case S3Prefix.TYPE_NAME:
                ref = S3Prefix.refByQualifiedName(qualifiedName);
                break;
            case SageMakerUnifiedStudioAssetSchema.TYPE_NAME:
                ref = SageMakerUnifiedStudioAssetSchema.refByQualifiedName(qualifiedName);
                break;
            case SageMakerUnifiedStudioProject.TYPE_NAME:
                ref = SageMakerUnifiedStudioProject.refByQualifiedName(qualifiedName);
                break;
            case SageMakerUnifiedStudioPublishedAsset.TYPE_NAME:
                ref = SageMakerUnifiedStudioPublishedAsset.refByQualifiedName(qualifiedName);
                break;
            case SageMakerUnifiedStudioSubscribedAsset.TYPE_NAME:
                ref = SageMakerUnifiedStudioSubscribedAsset.refByQualifiedName(qualifiedName);
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
            case SnowflakeAIModelContext.TYPE_NAME:
                ref = SnowflakeAIModelContext.refByQualifiedName(qualifiedName);
                break;
            case SnowflakeAIModelVersion.TYPE_NAME:
                ref = SnowflakeAIModelVersion.refByQualifiedName(qualifiedName);
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
