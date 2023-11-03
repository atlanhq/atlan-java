/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

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
public class AttributeDefOptions extends AtlanObject {
    private static final long serialVersionUID = 2L;

    public static final Set<String> ALL_ASSET_TYPES = Set.of(
            ADLSAccount.TYPE_NAME,
            ADLSContainer.TYPE_NAME,
            ADLSObject.TYPE_NAME,
            APIPath.TYPE_NAME,
            APISpec.TYPE_NAME,
            AirflowDag.TYPE_NAME,
            AirflowTask.TYPE_NAME,
            AtlanCollection.TYPE_NAME,
            AtlanQuery.TYPE_NAME,
            AuthPolicy.TYPE_NAME,
            AuthService.TYPE_NAME,
            AzureEventHub.TYPE_NAME,
            AzureEventHubConsumerGroup.TYPE_NAME,
            BIProcess.TYPE_NAME,
            Badge.TYPE_NAME,
            Column.TYPE_NAME,
            ColumnProcess.TYPE_NAME,
            Connection.TYPE_NAME,
            DataStudioAsset.TYPE_NAME,
            Database.TYPE_NAME,
            DbtColumnProcess.TYPE_NAME,
            DbtMetric.TYPE_NAME,
            DbtModel.TYPE_NAME,
            DbtModelColumn.TYPE_NAME,
            DbtProcess.TYPE_NAME,
            DbtSource.TYPE_NAME,
            DbtTag.TYPE_NAME,
            DbtTest.TYPE_NAME,
            Folder.TYPE_NAME,
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
            MongoDBCollection.TYPE_NAME,
            MongoDBDatabase.TYPE_NAME,
            Persona.TYPE_NAME,
            PowerBIColumn.TYPE_NAME,
            PowerBIDashboard.TYPE_NAME,
            PowerBIDataflow.TYPE_NAME,
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
            S3Bucket.TYPE_NAME,
            S3Object.TYPE_NAME,
            SalesforceDashboard.TYPE_NAME,
            SalesforceField.TYPE_NAME,
            SalesforceObject.TYPE_NAME,
            SalesforceOrganization.TYPE_NAME,
            SalesforceReport.TYPE_NAME,
            Schema.TYPE_NAME,
            SchemaRegistrySubject.TYPE_NAME,
            SigmaDataElement.TYPE_NAME,
            SigmaDataElementField.TYPE_NAME,
            SigmaDataset.TYPE_NAME,
            SigmaDatasetColumn.TYPE_NAME,
            SigmaPage.TYPE_NAME,
            SigmaWorkbook.TYPE_NAME,
            SnowflakeDynamicTable.TYPE_NAME,
            SnowflakePipe.TYPE_NAME,
            SnowflakeStream.TYPE_NAME,
            SnowflakeTag.TYPE_NAME,
            SodaCheck.TYPE_NAME,
            Table.TYPE_NAME,
            TablePartition.TYPE_NAME,
            TableauCalculatedField.TYPE_NAME,
            TableauDashboard.TYPE_NAME,
            TableauDatasource.TYPE_NAME,
            TableauDatasourceField.TYPE_NAME,
            TableauFlow.TYPE_NAME,
            TableauMetric.TYPE_NAME,
            TableauProject.TYPE_NAME,
            TableauSite.TYPE_NAME,
            TableauWorkbook.TYPE_NAME,
            TableauWorksheet.TYPE_NAME,
            ThoughtspotAnswer.TYPE_NAME,
            ThoughtspotDashlet.TYPE_NAME,
            ThoughtspotLiveboard.TYPE_NAME,
            View.TYPE_NAME);
    public static final Set<String> ALL_GLOSSARY_TYPES =
            Set.of(Glossary.TYPE_NAME, GlossaryTerm.TYPE_NAME, GlossaryCategory.TYPE_NAME);
    public static final Set<String> ALL_OTHER_TYPES = Set.of(File.TYPE_NAME);

    /**
     * Instantiate a new set of attribute options from the provided parameters.
     * @param type primitive type of the attribute
     * @param optionsName name of the options (enumeration) if the primitive type is an enumeration (can be null otherwise)
     * @return the attribute options
     * @throws AtlanException on any API issues looking up existing connections and glossaries
     */
    public static AttributeDefOptions of(AtlanCustomAttributePrimitiveType type, String optionsName)
            throws AtlanException {
        AttributeDefOptionsBuilder<?, ?> builder = AttributeDefOptions.builder()
                .primitiveType(type)
                .applicableConnections(Connection.getAllQualifiedNames())
                .applicableAssetTypes(ALL_ASSET_TYPES)
                .applicableGlossaries(Glossary.getAllQualifiedNames())
                .applicableGlossaryTypes(ALL_GLOSSARY_TYPES)
                .applicableOtherAssetTypes(ALL_OTHER_TYPES);
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
     * Note: this is only used when customMetadataVersion < v2.
     * @deprecated see {@link #applicableAssetTypes}, {@link #applicableGlossaryTypes}, {@link #applicableOtherAssetTypes} instead
     */
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    @Deprecated
    Set<String> customApplicableEntityTypes;

    /**
     * Qualified names of connections to which to restrict the attribute.
     * Only assets within one of these connections will have this attribute available.
     * To further restrict the types of assets within the glossaries, see {@link #applicableAssetTypes}.
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
     * by the appropriate initial setup {@link #of(AtlanCustomAttributePrimitiveType, String)} or archival
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
        if (options.applicableAssetTypes != null) {
            this.applicableAssetTypes = options.applicableAssetTypes;
        }
        if (options.applicableGlossaryTypes != null) {
            this.applicableGlossaryTypes = options.applicableGlossaryTypes;
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
}
