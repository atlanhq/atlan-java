/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType;
import com.atlan.serde.SetToStringSerializer;
import com.atlan.serde.StringToSetDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Set;
import javax.annotation.processing.Generated;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Options that can be set on each attribute within a type definition.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AttributeDefOptions extends AtlanObject {
    private static final long serialVersionUID = 2L;

    public static final Set<String> ALL_ASSET_TYPES = Set.of(
            ADLSAccount.TYPE_NAME,
            ADLSContainer.TYPE_NAME,
            ADLSObject.TYPE_NAME,
            AIApplication.TYPE_NAME,
            AIModel.TYPE_NAME,
            APIField.TYPE_NAME,
            APIObject.TYPE_NAME,
            APIPath.TYPE_NAME,
            APIQuery.TYPE_NAME,
            APISpec.TYPE_NAME,
            AdfActivity.TYPE_NAME,
            AdfDataflow.TYPE_NAME,
            AdfDataset.TYPE_NAME,
            AdfLinkedservice.TYPE_NAME,
            AdfPipeline.TYPE_NAME,
            AirflowDag.TYPE_NAME,
            AirflowTask.TYPE_NAME,
            AnaplanApp.TYPE_NAME,
            AnaplanDimension.TYPE_NAME,
            AnaplanLineItem.TYPE_NAME,
            AnaplanList.TYPE_NAME,
            AnaplanModel.TYPE_NAME,
            AnaplanModule.TYPE_NAME,
            AnaplanPage.TYPE_NAME,
            AnaplanSystemDimension.TYPE_NAME,
            AnaplanView.TYPE_NAME,
            AnaplanWorkspace.TYPE_NAME,
            AnomaloCheck.TYPE_NAME,
            AppWorkflowRun.TYPE_NAME,
            Application.TYPE_NAME,
            ApplicationField.TYPE_NAME,
            AtlanCollection.TYPE_NAME,
            AtlanQuery.TYPE_NAME,
            AuthPolicy.TYPE_NAME,
            AuthService.TYPE_NAME,
            AzureEventHub.TYPE_NAME,
            AzureEventHubConsumerGroup.TYPE_NAME,
            AzureServiceBusNamespace.TYPE_NAME,
            AzureServiceBusSchema.TYPE_NAME,
            AzureServiceBusTopic.TYPE_NAME,
            BIProcess.TYPE_NAME,
            Badge.TYPE_NAME,
            BigqueryRoutine.TYPE_NAME,
            BigqueryTag.TYPE_NAME,
            BusinessPolicy.TYPE_NAME,
            BusinessPolicyException.TYPE_NAME,
            BusinessPolicyIncident.TYPE_NAME,
            BusinessPolicyLog.TYPE_NAME,
            CalculationView.TYPE_NAME,
            CassandraColumn.TYPE_NAME,
            CassandraIndex.TYPE_NAME,
            CassandraKeyspace.TYPE_NAME,
            CassandraTable.TYPE_NAME,
            CassandraView.TYPE_NAME,
            Cognite3DModel.TYPE_NAME,
            CogniteAsset.TYPE_NAME,
            CogniteEvent.TYPE_NAME,
            CogniteFile.TYPE_NAME,
            CogniteSequence.TYPE_NAME,
            CogniteTimeSeries.TYPE_NAME,
            CognosColumn.TYPE_NAME,
            CognosDashboard.TYPE_NAME,
            CognosDataset.TYPE_NAME,
            CognosDatasource.TYPE_NAME,
            CognosExploration.TYPE_NAME,
            CognosFile.TYPE_NAME,
            CognosFolder.TYPE_NAME,
            CognosModule.TYPE_NAME,
            CognosPackage.TYPE_NAME,
            CognosReport.TYPE_NAME,
            Column.TYPE_NAME,
            ColumnProcess.TYPE_NAME,
            Connection.TYPE_NAME,
            ConnectionProcess.TYPE_NAME,
            CosmosMongoDBAccount.TYPE_NAME,
            CosmosMongoDBCollection.TYPE_NAME,
            CosmosMongoDBDatabase.TYPE_NAME,
            Cube.TYPE_NAME,
            CubeDimension.TYPE_NAME,
            CubeField.TYPE_NAME,
            CubeHierarchy.TYPE_NAME,
            CustomEntity.TYPE_NAME,
            DataContract.TYPE_NAME,
            DataQualityRule.TYPE_NAME,
            DataQualityRuleTemplate.TYPE_NAME,
            DataStudioAsset.TYPE_NAME,
            Database.TYPE_NAME,
            DatabricksAIModelContext.TYPE_NAME,
            DatabricksAIModelVersion.TYPE_NAME,
            DatabricksExternalLocation.TYPE_NAME,
            DatabricksExternalLocationPath.TYPE_NAME,
            DatabricksNotebook.TYPE_NAME,
            DatabricksUnityCatalogTag.TYPE_NAME,
            DatabricksVolume.TYPE_NAME,
            DatabricksVolumePath.TYPE_NAME,
            DataverseAttribute.TYPE_NAME,
            DataverseEntity.TYPE_NAME,
            DbtColumnProcess.TYPE_NAME,
            DbtMetric.TYPE_NAME,
            DbtModel.TYPE_NAME,
            DbtModelColumn.TYPE_NAME,
            DbtProcess.TYPE_NAME,
            DbtSeed.TYPE_NAME,
            DbtSource.TYPE_NAME,
            DbtTag.TYPE_NAME,
            DbtTest.TYPE_NAME,
            DocumentDBCollection.TYPE_NAME,
            DocumentDBDatabase.TYPE_NAME,
            DomoCard.TYPE_NAME,
            DomoDashboard.TYPE_NAME,
            DomoDataset.TYPE_NAME,
            DomoDatasetColumn.TYPE_NAME,
            DremioColumn.TYPE_NAME,
            DremioFolder.TYPE_NAME,
            DremioPhysicalDataset.TYPE_NAME,
            DremioSource.TYPE_NAME,
            DremioSpace.TYPE_NAME,
            DremioVirtualDataset.TYPE_NAME,
            DynamoDBGlobalSecondaryIndex.TYPE_NAME,
            DynamoDBLocalSecondaryIndex.TYPE_NAME,
            DynamoDBTable.TYPE_NAME,
            FabricActivity.TYPE_NAME,
            FabricDashboard.TYPE_NAME,
            FabricDataPipeline.TYPE_NAME,
            FabricDataflow.TYPE_NAME,
            FabricDataflowEntityColumn.TYPE_NAME,
            FabricPage.TYPE_NAME,
            FabricReport.TYPE_NAME,
            FabricSemanticModel.TYPE_NAME,
            FabricSemanticModelTable.TYPE_NAME,
            FabricSemanticModelTableColumn.TYPE_NAME,
            FabricVisual.TYPE_NAME,
            FabricWorkspace.TYPE_NAME,
            FivetranConnector.TYPE_NAME,
            FlowControlOperation.TYPE_NAME,
            FlowDataset.TYPE_NAME,
            FlowDatasetOperation.TYPE_NAME,
            FlowField.TYPE_NAME,
            FlowFieldOperation.TYPE_NAME,
            FlowFolder.TYPE_NAME,
            FlowProject.TYPE_NAME,
            FlowReusableUnit.TYPE_NAME,
            Folder.TYPE_NAME,
            Form.TYPE_NAME,
            Function.TYPE_NAME,
            GCSBucket.TYPE_NAME,
            GCSObject.TYPE_NAME,
            Insight.TYPE_NAME,
            KafkaConsumerGroup.TYPE_NAME,
            KafkaTopic.TYPE_NAME,
            LineageProcess.TYPE_NAME,
            Link.TYPE_NAME,
            LookerDashboard.TYPE_NAME,
            LookerExplore.TYPE_NAME,
            LookerField.TYPE_NAME,
            LookerFolder.TYPE_NAME,
            LookerLook.TYPE_NAME,
            LookerModel.TYPE_NAME,
            LookerProject.TYPE_NAME,
            LookerQuery.TYPE_NAME,
            LookerTile.TYPE_NAME,
            LookerView.TYPE_NAME,
            MCIncident.TYPE_NAME,
            MCMonitor.TYPE_NAME,
            MaterializedView.TYPE_NAME,
            MatillionComponent.TYPE_NAME,
            MatillionGroup.TYPE_NAME,
            MatillionJob.TYPE_NAME,
            MatillionProject.TYPE_NAME,
            MetabaseCollection.TYPE_NAME,
            MetabaseDashboard.TYPE_NAME,
            MetabaseQuestion.TYPE_NAME,
            MicroStrategyAttribute.TYPE_NAME,
            MicroStrategyColumn.TYPE_NAME,
            MicroStrategyCube.TYPE_NAME,
            MicroStrategyDocument.TYPE_NAME,
            MicroStrategyDossier.TYPE_NAME,
            MicroStrategyFact.TYPE_NAME,
            MicroStrategyMetric.TYPE_NAME,
            MicroStrategyProject.TYPE_NAME,
            MicroStrategyReport.TYPE_NAME,
            MicroStrategyVisualization.TYPE_NAME,
            ModeChart.TYPE_NAME,
            ModeCollection.TYPE_NAME,
            ModeQuery.TYPE_NAME,
            ModeReport.TYPE_NAME,
            ModeWorkspace.TYPE_NAME,
            ModelAttribute.TYPE_NAME,
            ModelAttributeAssociation.TYPE_NAME,
            ModelDataModel.TYPE_NAME,
            ModelEntity.TYPE_NAME,
            ModelEntityAssociation.TYPE_NAME,
            ModelVersion.TYPE_NAME,
            MongoDBCollection.TYPE_NAME,
            MongoDBDatabase.TYPE_NAME,
            Persona.TYPE_NAME,
            PowerBIApp.TYPE_NAME,
            PowerBIColumn.TYPE_NAME,
            PowerBIDashboard.TYPE_NAME,
            PowerBIDataflow.TYPE_NAME,
            PowerBIDataflowEntityColumn.TYPE_NAME,
            PowerBIDataset.TYPE_NAME,
            PowerBIDatasource.TYPE_NAME,
            PowerBIMeasure.TYPE_NAME,
            PowerBIPage.TYPE_NAME,
            PowerBIReport.TYPE_NAME,
            PowerBITable.TYPE_NAME,
            PowerBITile.TYPE_NAME,
            PowerBIWorkspace.TYPE_NAME,
            PresetChart.TYPE_NAME,
            PresetDashboard.TYPE_NAME,
            PresetDataset.TYPE_NAME,
            PresetWorkspace.TYPE_NAME,
            Procedure.TYPE_NAME,
            Purpose.TYPE_NAME,
            QlikApp.TYPE_NAME,
            QlikChart.TYPE_NAME,
            QlikColumn.TYPE_NAME,
            QlikDataset.TYPE_NAME,
            QlikSheet.TYPE_NAME,
            QlikSpace.TYPE_NAME,
            QlikStream.TYPE_NAME,
            QuickSightAnalysis.TYPE_NAME,
            QuickSightAnalysisVisual.TYPE_NAME,
            QuickSightDashboard.TYPE_NAME,
            QuickSightDashboardVisual.TYPE_NAME,
            QuickSightDataset.TYPE_NAME,
            QuickSightDatasetField.TYPE_NAME,
            QuickSightFolder.TYPE_NAME,
            Readme.TYPE_NAME,
            ReadmeTemplate.TYPE_NAME,
            RedashDashboard.TYPE_NAME,
            RedashQuery.TYPE_NAME,
            RedashVisualization.TYPE_NAME,
            Response.TYPE_NAME,
            S3Bucket.TYPE_NAME,
            S3Object.TYPE_NAME,
            SalesforceDashboard.TYPE_NAME,
            SalesforceField.TYPE_NAME,
            SalesforceObject.TYPE_NAME,
            SalesforceOrganization.TYPE_NAME,
            SalesforceReport.TYPE_NAME,
            SapErpAbapProgram.TYPE_NAME,
            SapErpCdsView.TYPE_NAME,
            SapErpColumn.TYPE_NAME,
            SapErpComponent.TYPE_NAME,
            SapErpFunctionModule.TYPE_NAME,
            SapErpTable.TYPE_NAME,
            SapErpTransactionCode.TYPE_NAME,
            SapErpView.TYPE_NAME,
            Schema.TYPE_NAME,
            SchemaRegistrySubject.TYPE_NAME,
            SigmaDataElement.TYPE_NAME,
            SigmaDataElementField.TYPE_NAME,
            SigmaDataset.TYPE_NAME,
            SigmaDatasetColumn.TYPE_NAME,
            SigmaPage.TYPE_NAME,
            SigmaWorkbook.TYPE_NAME,
            SisenseDashboard.TYPE_NAME,
            SisenseDatamodel.TYPE_NAME,
            SisenseDatamodelTable.TYPE_NAME,
            SisenseFolder.TYPE_NAME,
            SisenseWidget.TYPE_NAME,
            SnowflakeAIModelContext.TYPE_NAME,
            SnowflakeAIModelVersion.TYPE_NAME,
            SnowflakeDynamicTable.TYPE_NAME,
            SnowflakePipe.TYPE_NAME,
            SnowflakeStage.TYPE_NAME,
            SnowflakeStream.TYPE_NAME,
            SnowflakeTag.TYPE_NAME,
            SodaCheck.TYPE_NAME,
            SourceTag.TYPE_NAME,
            SparkJob.TYPE_NAME,
            Stakeholder.TYPE_NAME,
            StakeholderTitle.TYPE_NAME,
            SupersetChart.TYPE_NAME,
            SupersetDashboard.TYPE_NAME,
            SupersetDataset.TYPE_NAME,
            Table.TYPE_NAME,
            TablePartition.TYPE_NAME,
            TableauCalculatedField.TYPE_NAME,
            TableauDashboard.TYPE_NAME,
            TableauDashboardField.TYPE_NAME,
            TableauDatasource.TYPE_NAME,
            TableauDatasourceField.TYPE_NAME,
            TableauFlow.TYPE_NAME,
            TableauMetric.TYPE_NAME,
            TableauProject.TYPE_NAME,
            TableauSite.TYPE_NAME,
            TableauWorkbook.TYPE_NAME,
            TableauWorksheet.TYPE_NAME,
            TableauWorksheetField.TYPE_NAME,
            TagAttachment.TYPE_NAME,
            Task.TYPE_NAME,
            ThoughtspotAnswer.TYPE_NAME,
            ThoughtspotColumn.TYPE_NAME,
            ThoughtspotDashlet.TYPE_NAME,
            ThoughtspotLiveboard.TYPE_NAME,
            ThoughtspotTable.TYPE_NAME,
            ThoughtspotView.TYPE_NAME,
            ThoughtspotWorksheet.TYPE_NAME,
            View.TYPE_NAME,
            Workflow.TYPE_NAME,
            WorkflowRun.TYPE_NAME);
    public static final Set<String> ALL_GLOSSARY_TYPES =
            Set.of(Glossary.TYPE_NAME, GlossaryTerm.TYPE_NAME, GlossaryCategory.TYPE_NAME);
    public static final Set<String> ALL_AI_TYPES = Set.of(AIApplication.TYPE_NAME, AIModel.TYPE_NAME);
    public static final Set<String> ALL_DOMAIN_TYPES = Set.of(DataDomain.TYPE_NAME, DataProduct.TYPE_NAME);
    public static final Set<String> ALL_OTHER_TYPES = Set.of(File.TYPE_NAME);
    public static final Set<String> ALL_DOMAINS = Set.of("*/super");

    /**
     * Instantiate a new set of attribute options from the provided parameters.
     * @param client connectivity to the Atlan tenant
     * @param type primitive type of the attribute
     * @param optionsName name of the options (enumeration) if the primitive type is an enumeration (can be null otherwise)
     * @return the attribute options
     * @throws AtlanException on any API issues looking up existing connections and glossaries
     */
    public static AttributeDefOptions of(AtlanClient client, AtlanCustomAttributePrimitiveType type, String optionsName)
            throws AtlanException {
        return of(client, type, optionsName, null);
    }

    /**
     * Instantiate a new set of attribute options from the provided parameters.
     * @param client connectivity to the Atlan tenant
     * @param type primitive type of the attribute
     * @param optionsName name of the options (enumeration) if the primitive type is an enumeration (can be null otherwise)
     * @param options starting point of options on which to extend
     * @return the attribute options
     * @throws AtlanException on any API issues looking up existing connections and glossaries
     */
    public static AttributeDefOptions of(
            AtlanClient client, AtlanCustomAttributePrimitiveType type, String optionsName, AttributeDefOptions options)
            throws AtlanException {
        AttributeDefOptionsBuilder<?, ?> builder;
        if (options != null) {
            // If we are provided options, use those as the starting point
            builder = options.toBuilder();
        } else {
            // Otherwise set defaults to allow the attribute to be available on all assets
            builder = AttributeDefOptions.builder()
                    .applicableConnections(Connection.getAllQualifiedNames(client))
                    .applicableAssetTypes(ALL_ASSET_TYPES)
                    .applicableGlossaries(Glossary.getAllQualifiedNames(client))
                    .applicableGlossaryTypes(ALL_GLOSSARY_TYPES)
                    .applicableAITypes(ALL_AI_TYPES)
                    .applicableDomains(ALL_DOMAINS)
                    .applicableDomainTypes(ALL_DOMAIN_TYPES)
                    .applicableOtherAssetTypes(ALL_OTHER_TYPES);
        }
        builder.primitiveType(type);
        switch (type) {
            case USERS:
            case GROUPS:
            case URL:
            case SQL:
                builder.customType(type.getValue());
                break;
            case OPTIONS:
                builder.isEnum(true).enumType(optionsName);
                break;
            default:
                // do nothing
                break;
        }
        return builder.build();
    }

    /** Indicates the version of the custom metadata structure. This determines which other options are available and used. */
    @Builder.Default
    String customMetadataVersion = "v2";

    /** Optional description of the attribute. */
    String description;

    /**
     * Set of entities on which this attribute can be applied.
     * Note: generally this should be left as-is. Any overrides should instead be applied through
     * one or more of {@link #applicableAssetTypes}, {@link #applicableGlossaryTypes}, or {@link #applicableOtherAssetTypes}.
     */
    @Builder.Default
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    Set<String> applicableEntityTypes = Set.of("Asset");

    /**
     * Set of entities on which this attribute should appear.
     * Note: this is only used when customMetadataVersion is less than v2.
     * @deprecated see {@link #applicableAssetTypes}, {@link #applicableGlossaryTypes}, {@link #applicableOtherAssetTypes} instead
     */
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    @Deprecated
    Set<String> customApplicableEntityTypes;

    /**
     * Qualified names of connections to which to restrict the attribute.
     * Only assets within one of these connections will have this attribute available.
     * To further restrict the types of assets within the connections, see {@link #applicableAssetTypes}.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    Set<String> applicableConnections;

    /**
     * Qualified names of glossaries to which to restrict the attribute.
     * Only glossary assets within one of these glossaries will have this attribute available.
     * To further restrict the types of assets within the glossaries, see {@link #applicableGlossaryTypes}.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    Set<String> applicableGlossaries;

    /**
     * Qualified names of domains to which to restrict the attribute.
     * Only domains and data products within one of these domains will have this attribute available.
     * To further restrict the types of assets within the domains, see {@link #applicableDomainTypes}.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    Set<String> applicableDomains;

    /**
     * Asset type names to which to restrict the attribute.
     * Only assets of one of these types will have this attribute available.
     * To further restrict the assets for this custom metadata by connection, see {@link #applicableConnections}.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    @JsonProperty("assetTypesList")
    Set<String> applicableAssetTypes;

    /**
     * AI asset type names to which to restrict the attribute.
     * Only AI assets of one of these types will have this attribute available.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    @JsonProperty("aiAssetsTypeList")
    Set<String> applicableAITypes;

    /**
     * Glossary type names to which to restrict the attribute.
     * Only glossary assets of one of these types will have this attribute available.
     * To further restrict the glossary content for this custom metadata by glossary, see {@link #applicableGlossaries}.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    @JsonProperty("glossaryTypeList")
    Set<String> applicableGlossaryTypes;

    /**
     * Data product type names to which to restrict the attribute.
     * These cover asset types in data products and data domains.
     * Only assets of one of these types will have this attribute available.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    @JsonProperty("domainTypesList")
    Set<String> applicableDomainTypes;

    /**
     * Any other asset type names to which to restrict the attribute.
     * These cover any asset type that is not managed within a connection or a glossary.
     * Only assets of one of these types will have this attribute available.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    @JsonProperty("otherAssetTypeList")
    Set<String> applicableOtherAssetTypes;

    /** Whether the attribute should be searchable (true) or not (false). */
    @Builder.Default
    Boolean allowSearch = false;

    /** Maximum length allowed for a string value. */
    @Builder.Default
    String maxStrLength = "100000000";

    /** Whether this attribute should appear in the filterable facets of discovery (true) or not (false). */
    @Builder.Default
    Boolean allowFiltering = true;

    /** Whether this attribute can have multiple values (true) or only a single value (false). */
    @Builder.Default
    Boolean multiValueSelect = false;

    /** Whether users will see this attribute in the overview tab of the sidebar (true) or not (false). */
    @Builder.Default
    Boolean showInOverview = false;

    /** Whether the attribute is deprecated ("true") or not (null or "false"). */
    String isDeprecated;

    /** Primitive type of the attribute. */
    AtlanCustomAttributePrimitiveType primitiveType;

    /** Whether the attribute is an enumeration (true) or not (false). */
    @Builder.Default
    Boolean isEnum = false;

    /** Name of the enumeration (options), when the attribute is an enumeration. */
    String enumType;

    /** Used for Atlan-specific types like {@code users}, {@code groups}, {@code url}, and {@code SQL}. */
    String customType;

    /** If true for a date attribute, then time-level precision is also available in the UI (otherwise only date-level) */
    Boolean hasTimePrecision;

    /** Whether the attribute has been deleted (true) or is still active (false). */
    Boolean isArchived;

    /** When the attribute was deleted. */
    Long archivedAt;

    /** User who deleted the attribute. */
    String archivedBy;

    /** TBC */
    String isSoftReference;

    /** TBC */
    String isAppendOnPartialUpdate;

    /**
     * Append the provided set of attribute definition options onto this attribute definition.
     * Note that certain options are ignored by the append, specifically: primitiveType, isEnum,
     * enumType, customType, multiValueSelect, isArchived, archivedAt, archivedBy. These should only be set
     * by the appropriate initial setup {@link #of(AtlanClient, AtlanCustomAttributePrimitiveType, String)} or archival
     * of an attribute definition.
     *
     * @param options to append to this set of attribute definition options
     * @return the combined set of attribute definition options
     */
    public AttributeDefOptions append(AttributeDefOptions options) {
        if (options == null) {
            return this;
        }
        if (options.description != null) {
            this.description = options.description;
        }
        if (options.applicableEntityTypes != null) {
            this.applicableEntityTypes = options.applicableEntityTypes;
        }
        if (options.customApplicableEntityTypes != null) {
            this.customApplicableEntityTypes = options.customApplicableEntityTypes;
        }
        if (options.applicableConnections != null) {
            this.applicableConnections = options.applicableConnections;
        }
        if (options.applicableGlossaries != null) {
            this.applicableGlossaries = options.applicableGlossaries;
        }
        if (options.applicableDomains != null) {
            this.applicableDomains = options.applicableDomains;
        }
        if (options.applicableAssetTypes != null) {
            this.applicableAssetTypes = options.applicableAssetTypes;
        }
        if (options.applicableGlossaryTypes != null) {
            this.applicableGlossaryTypes = options.applicableGlossaryTypes;
        }
        if (options.applicableAITypes != null) {
            this.applicableAITypes = options.applicableAITypes;
        }
        if (options.applicableDomainTypes != null) {
            this.applicableDomainTypes = options.applicableDomainTypes;
        }
        if (options.applicableOtherAssetTypes != null) {
            this.applicableOtherAssetTypes = options.applicableOtherAssetTypes;
        }
        if (options.allowSearch != null) {
            this.allowSearch = options.allowSearch;
        }
        if (options.maxStrLength != null) {
            this.maxStrLength = options.maxStrLength;
        }
        if (options.allowFiltering != null) {
            this.allowFiltering = options.allowFiltering;
        }
        if (options.showInOverview != null) {
            this.showInOverview = options.showInOverview;
        }
        if (options.isDeprecated != null) {
            this.isDeprecated = options.isDeprecated;
        }
        if (options.hasTimePrecision != null) {
            this.hasTimePrecision = options.hasTimePrecision;
        }
        return this;
    }

    public abstract static class AttributeDefOptionsBuilder<
                    C extends AttributeDefOptions, B extends AttributeDefOptionsBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
