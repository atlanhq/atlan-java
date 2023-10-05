/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import javax.annotation.processing.Generated;
import lombok.Getter;

/**
 * Enumeration of all index fields.
 * @deprecated these are now enumerated as constants in the various asset interfaces, as instances of one of the following: {@link com.atlan.model.fields.BooleanField}, {@link com.atlan.model.fields.KeywordField}, {@link com.atlan.model.fields.KeywordTextField}, {@link com.atlan.model.fields.KeywordTextStemmedField}, {@link com.atlan.model.fields.NumericField}, {@link com.atlan.model.fields.NumericRankField}, {@link com.atlan.model.fields.RelationField}, or {@link com.atlan.model.fields.TextField}
 */
@Deprecated
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum TextFields implements AtlanSearchableField {
    /** Unique name of the account for this ADLS asset. */
    ADLS_ACCOUNT_QUALIFIED_NAME("adlsAccountQualifiedName.text"),
    /** TBC */
    ADLS_ACCOUNT_RESOURCE_GROUP("adlsAccountResourceGroup"),
    /** TBC */
    ADLS_ACCOUNT_SUBSCRIPTION("adlsAccountSubscription"),
    /** Unique name of the container this object exists within. */
    ADLS_CONTAINER_QUALIFIED_NAME("adlsContainerQualifiedName.text"),
    /** TBC */
    ADLS_CONTAINER_URL("adlsContainerUrl"),
    /** TBC */
    ADLS_OBJECT_CACHE_CONTROL("adlsObjectCacheControl"),
    /** TBC */
    ADLS_OBJECT_CONTENT_LANGUAGE("adlsObjectContentLanguage"),
    /** TBC */
    ADLS_OBJECT_CONTENT_TYPE("adlsObjectContentType"),
    /** TBC */
    ADLS_OBJECT_URL("adlsObjectUrl"),
    /** TBC */
    AIRFLOW_DAG_NAME("airflowDagName"),
    /** TBC */
    AIRFLOW_TASK_CONNECTION_ID("airflowTaskConnectionId"),
    /** TBC */
    AIRFLOW_TASK_OPERATOR_CLASS("airflowTaskOperatorClass"),
    /** TBC */
    API_NAME("apiName"),
    /** TBC */
    API_PATH_RAW_URI("apiPathRawURI.text"),
    /** TBC */
    API_PATH_SUMMARY("apiPathSummary"),
    /** TBC */
    API_SPEC_CONTACT_EMAIL("apiSpecContactEmail.text"),
    /** TBC */
    API_SPEC_CONTACT_NAME("apiSpecContactName"),
    /** TBC */
    API_SPEC_CONTACT_URL("apiSpecContactURL.text"),
    /** TBC */
    API_SPEC_LICENSE_NAME("apiSpecLicenseName"),
    /** TBC */
    API_SPEC_LICENSE_URL("apiSpecLicenseURL.text"),
    /** TBC */
    API_SPEC_NAME("apiSpecName"),
    /** TBC */
    API_SPEC_QUALIFIED_NAME("apiSpecQualifiedName.text"),
    /** TBC */
    API_SPEC_SERVICE_ALIAS("apiSpecServiceAlias.text"),
    /** TBC */
    API_SPEC_TERMS_OF_SERVICE_URL("apiSpecTermsOfServiceURL.text"),
    /** TBC */
    ASSET_DBT_ACCOUNT_NAME("assetDbtAccountName"),
    /** TBC */
    ASSET_DBT_ALIAS("assetDbtAlias"),
    /** TBC */
    ASSET_DBT_ENVIRONMENT_NAME("assetDbtEnvironmentName"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_GIT_BRANCH("assetDbtJobLastRunGitBranch.text"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_STATUS_MESSAGE("assetDbtJobLastRunStatusMessage"),
    /** TBC */
    ASSET_DBT_JOB_NAME("assetDbtJobName"),
    /** TBC */
    ASSET_DBT_JOB_NEXT_RUN_HUMANIZED("assetDbtJobNextRunHumanized"),
    /** TBC */
    ASSET_DBT_JOB_SCHEDULE_CRON_HUMANIZED("assetDbtJobScheduleCronHumanized"),
    /** TBC */
    ASSET_DBT_PACKAGE_NAME("assetDbtPackageName"),
    /** TBC */
    ASSET_DBT_PROJECT_NAME("assetDbtProjectName"),
    /** TBC */
    ASSET_DBT_TAGS("assetDbtTags.text"),
    /** TBC */
    ASSET_DBT_UNIQUE_ID("assetDbtUniqueId"),
    /** TBC */
    ASSET_MC_INCIDENT_NAMES("assetMcIncidentNames"),
    /** TBC */
    ASSET_MC_INCIDENT_QUALIFIED_NAMES("assetMcIncidentQualifiedNames.text"),
    /** TBC */
    ASSET_MC_MONITOR_NAMES("assetMcMonitorNames"),
    /** TBC */
    ASSET_MC_MONITOR_QUALIFIED_NAMES("assetMcMonitorQualifiedNames.text"),
    /** TBC */
    ASSET_TAGS("assetTags.text"),
    /** All Atlan tags that exist on an asset, whether directly assigned or propagated, searchable by the internal hashed-string ID of the Atlan tag. */
    ATLAN_TAGS_TEXT("__classificationsText"),
    /** Amazon Resource Name (ARN) for this asset. This uniquely identifies the asset in AWS, and thus must be unique across all AWS asset instances. */
    AWS_ARN("awsArn.text"),
    /** Root user's name. */
    AWS_OWNER_NAME("awsOwnerName.text"),
    /** TBC */
    AZURE_RESOURCE_ID("azureResourceId.text"),
    /** Status of the asset's certification. */
    CERTIFICATE_STATUS("certificateStatus.text"),
    /** TBC */
    COLLECTION_QUALIFIED_NAME("collectionQualifiedName.text"),
    /** TBC */
    CONNECTION_NAME("connectionName.text"),
    /** Unique name of the connection through which this asset is accessible. */
    CONNECTION_QUALIFIED_NAME("connectionQualifiedName.text"),
    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    DATABASE_NAME("databaseName"),
    /** Title for the asset. */
    DATA_STUDIO_ASSET_TITLE("dataStudioAssetTitle"),
    /** Data type of values in the field. */
    DATA_TYPE("dataType.text"),
    /** TBC */
    DBT_ACCOUNT_NAME("dbtAccountName"),
    /** TBC */
    DBT_ALIAS("dbtAlias"),
    /** TBC */
    DBT_ENVIRONMENT_DBT_VERSION("dbtEnvironmentDbtVersion"),
    /** TBC */
    DBT_ENVIRONMENT_NAME("dbtEnvironmentName"),
    /** TBC */
    DBT_JOB_NAME("dbtJobName"),
    /** TBC */
    DBT_JOB_NEXT_RUN_HUMANIZED("dbtJobNextRunHumanized"),
    /** TBC */
    DBT_JOB_SCHEDULE_CRON_HUMANIZED("dbtJobScheduleCronHumanized"),
    /** TBC */
    DBT_MODEL_QUALIFIED_NAME("dbtModelQualifiedName.text"),
    /** TBC */
    DBT_PACKAGE_NAME("dbtPackageName"),
    /** TBC */
    DBT_PROJECT_NAME("dbtProjectName"),
    /** TBC */
    DBT_QUALIFIED_NAME("dbtQualifiedName.text"),
    /** The raw code of a test ( tests in dbt can be defined using python ) */
    DBT_TEST_RAW_CODE("dbtTestRawCode.text"),
    /** The raw sql of a test */
    DBT_TEST_RAW_SQL("dbtTestRawSQL.text"),
    /** TBC */
    DBT_UNIQUE_ID("dbtUniqueId"),
    /** TBC */
    DEFAULT_DATABASE_QUALIFIED_NAME("defaultDatabaseQualifiedName.text"),
    /** TBC */
    DEFAULT_SCHEMA_QUALIFIED_NAME("defaultSchemaQualifiedName.text"),
    /** Description of the asset, as crawled from a source. */
    DESCRIPTION("description"),
    /** Name used for display purposes (in user interfaces). */
    DISPLAY_NAME("displayName"),
    /** TBC */
    GCS_BUCKET_LIFECYCLE_RULES("gcsBucketLifecycleRules"),
    /** Human-readable name of the bucket in which this object exists. */
    GCS_BUCKET_NAME("gcsBucketName"),
    /** qualifiedName of the bucket in which this object exists. */
    GCS_BUCKET_QUALIFIED_NAME("gcsBucketQualifiedName.text"),
    /** TBC */
    GCS_BUCKET_RETENTION_POLICY("gcsBucketRetentionPolicy"),
    /** TBC */
    GCS_OBJECT_KEY("gcsObjectKey.text"),
    /** TBC */
    GCS_OBJECT_MEDIA_LINK("gcsObjectMediaLink.text"),
    /** ID of the project in which the asset exists. */
    GOOGLE_PROJECT_ID("googleProjectId.text"),
    /** Name of the project in which the asset exists. */
    GOOGLE_PROJECT_NAME("googleProjectName.text"),
    /** TBC */
    INLINE_HELP_TEXT("inlineHelpText"),
    /** TBC */
    LOOKER_EXPLORE_QUALIFIED_NAME("lookerExploreQualifiedName.text"),
    /** TBC */
    LOOKER_VIEW_QUALIFIED_NAME("lookerViewQualifiedName.text"),
    /** Name of the matillion group to which the matillion project belongs */
    MATILLION_GROUP_NAME("matillionGroupName"),
    /** Qualified name of the matillion group to which the matillion project belongs */
    MATILLION_GROUP_QUALIFIED_NAME("matillionGroupQualifiedName.text"),
    /** Name of the matillion job to which the matillion component belongs */
    MATILLION_JOB_NAME("matillionJobName"),
    /** The hierarchy path of a job under a matillion project. Jobs can be managed at multiple folder levels under a matillion project */
    MATILLION_JOB_PATH("matillionJobPath.text"),
    /** Qualified name of the matillion job to which the matillion component belongs */
    MATILLION_JOB_QUALIFIED_NAME("matillionJobQualifiedName.text"),
    /** Name of the matillion project to which the matillion job belongs */
    MATILLION_PROJECT_NAME("matillionProjectName"),
    /** Qualified name of the matillion project to which the matillion job belongs */
    MATILLION_PROJECT_QUALIFIED_NAME("matillionProjectQualifiedName.text"),
    /** Condition on which the monitor produces an alert. */
    MC_MONITOR_ALERT_CONDITION("mcMonitorAlertCondition"),
    /** Namespace of the monitor. */
    MC_MONITOR_NAMESPACE("mcMonitorNamespace"),
    /** Readable description of the schedule for the rule. */
    MC_MONITOR_RULE_SCHEDULE_CONFIG_HUMANIZED("mcMonitorRuleScheduleConfigHumanized"),
    /** All terms attached to an asset, as a single comma-separated string. */
    MEANINGS_TEXT("__meaningsText"),
    /** TBC */
    METABASE_COLLECTION_NAME("metabaseCollectionName"),
    /** TBC */
    METABASE_COLLECTION_QUALIFIED_NAME("metabaseCollectionQualifiedName.text"),
    /** TBC */
    METABASE_NAMESPACE("metabaseNamespace.text"),
    /** TBC */
    METABASE_QUERY("metabaseQuery"),
    /** TBC */
    METABASE_QUERY_TYPE("metabaseQueryType.text"),
    /** TBC */
    METABASE_SLUG("metabaseSlug.text"),
    /** TBC */
    METRIC_FILTERS("metricFilters"),
    /** TBC */
    METRIC_TIME_GRAINS("metricTimeGrains"),
    /** Simple names of the related MicroStrategy attributes. */
    MICRO_STRATEGY_ATTRIBUTE_NAMES("microStrategyAttributeNames"),
    /** Unique names of the related MicroStrategy attributes. */
    MICRO_STRATEGY_ATTRIBUTE_QUALIFIED_NAMES("microStrategyAttributeQualifiedNames.text"),
    /** Simple names of the related MicroStrategy cubes. */
    MICRO_STRATEGY_CUBE_NAMES("microStrategyCubeNames"),
    /** Unique names of the related MicroStrategy cubes. */
    MICRO_STRATEGY_CUBE_QUALIFIED_NAMES("microStrategyCubeQualifiedNames.text"),
    /** Simple name of the dossier containing this visualization. */
    MICRO_STRATEGY_DOSSIER_NAME("microStrategyDossierName"),
    /** Unique name of the dossier containing this visualization. */
    MICRO_STRATEGY_DOSSIER_QUALIFIED_NAME("microStrategyDossierQualifiedName.text"),
    /** Simple names of the related MicroStrategy facts. */
    MICRO_STRATEGY_FACT_NAMES("microStrategyFactNames"),
    /** Unique names of the related MicroStrategy facts. */
    MICRO_STRATEGY_FACT_QUALIFIED_NAMES("microStrategyFactQualifiedNames.text"),
    /** Simple names of the parent MicroStrategy metrics. */
    MICRO_STRATEGY_METRIC_PARENT_NAMES("microStrategyMetricParentNames"),
    /** Unique names of the parent MicroStrategy metrics. */
    MICRO_STRATEGY_METRIC_PARENT_QUALIFIED_NAMES("microStrategyMetricParentQualifiedNames.text"),
    /** Simple name of the related MicroStrategy project. */
    MICRO_STRATEGY_PROJECT_NAME("microStrategyProjectName"),
    /** Unique name of the related MicroStrategy project. */
    MICRO_STRATEGY_PROJECT_QUALIFIED_NAME("microStrategyProjectQualifiedName.text"),
    /** Simple names of the related MicroStrategy reports. */
    MICRO_STRATEGY_REPORT_NAMES("microStrategyReportNames"),
    /** Unique names of the related MicroStrategy reports. */
    MICRO_STRATEGY_REPORT_QUALIFIED_NAMES("microStrategyReportQualifiedNames.text"),
    /** TBC */
    MODE_QUERY_NAME("modeQueryName"),
    /** TBC */
    MODE_QUERY_PREVIEW("modeQueryPreview"),
    /** TBC */
    MODE_QUERY_QUALIFIED_NAME("modeQueryQualifiedName.text"),
    /** TBC */
    MODE_RAW_QUERY("modeRawQuery"),
    /** TBC */
    MODE_REPORT_NAME("modeReportName"),
    /** TBC */
    MODE_REPORT_QUALIFIED_NAME("modeReportQualifiedName.text"),
    /** TBC */
    MODE_TOKEN("modeToken.text"),
    /** TBC */
    MODE_WORKSPACE_NAME("modeWorkspaceName"),
    /** TBC */
    MODE_WORKSPACE_QUALIFIED_NAME("modeWorkspaceQualifiedName.text"),
    /** TBC */
    MODE_WORKSPACE_USERNAME("modeWorkspaceUsername.text"),
    /** Definition of the schema applicable for the collection. */
    MONGO_DB_COLLECTION_SCHEMA_DEFINITION("mongoDBCollectionSchemaDefinition"),
    /** Subtype of a MongoDB collection (e.g. Capped, Time Series etc.) */
    MONGO_DB_COLLECTION_SUBTYPE("mongoDBCollectionSubtype.text"),
    /** Human-readable name of the asset. */
    NAME("name"),
    /** TBC */
    PARENT_COLUMN_NAME("parentColumnName"),
    /** TBC */
    PARENT_COLUMN_QUALIFIED_NAME("parentColumnQualifiedName.text"),
    /** TBC */
    PARENT_QUALIFIED_NAME("parentQualifiedName.text"),
    /** TBC */
    POWER_BI_MEASURE_EXPRESSION("powerBIMeasureExpression"),
    /** TBC */
    POWER_BI_TABLE_QUALIFIED_NAME("powerBITableQualifiedName.text"),
    /** Markdown-based description of the chart. */
    PRESET_CHART_DESCRIPTION_MARKDOWN("presetChartDescriptionMarkdown"),
    /** Username of the user who last changed the collection. */
    PRESET_DASHBOARD_CHANGED_BY_NAME("presetDashboardChangedByName"),
    /** qualifiedName of the Preset asset's collection. */
    PRESET_DASHBOARD_QUALIFIED_NAME("presetDashboardQualifiedName.text"),
    /** Name of the data source for the dataset. */
    PRESET_DATASET_DATASOURCE_NAME("presetDatasetDatasourceName"),
    /** Hostname of the Preset workspace. */
    PRESET_WORKSPACE_HOSTNAME("presetWorkspaceHostname.text"),
    /** qualifiedName of the Preset asset's workspace. */
    PRESET_WORKSPACE_QUALIFIED_NAME("presetWorkspaceQualifiedName.text"),
    /** Region of the workspace. */
    PRESET_WORKSPACE_REGION("presetWorkspaceRegion.text"),
    /** Unique name of the app where the Qlik asset exists. */
    QLIK_APP_QUALIFIED_NAME("qlikAppQualifiedName.text"),
    /** Footnote on the chart. */
    QLIK_CHART_FOOTNOTE("qlikChartFootnote"),
    /** Subtitle of the chart. */
    QLIK_CHART_SUBTITLE("qlikChartSubtitle"),
    /** Technical name of the data asset. */
    QLIK_DATASET_TECHNICAL_NAME("qlikDatasetTechnicalName"),
    /** URI of the dataset. */
    QLIK_DATASET_URI("qlikDatasetUri.text"),
    /** QRI of the Qlik object. */
    QLIK_QRI("qlikQRI.text"),
    /** Unique name of the space where the Qlik asset exists. */
    QLIK_SPACE_QUALIFIED_NAME("qlikSpaceQualifiedName.text"),
    /** Unique fully-qualified name of the asset in Atlan. */
    QUALIFIED_NAME("qualifiedName.text"),
    /** Qualified name of the QuickSight Analysis */
    QUICK_SIGHT_ANALYSIS_QUALIFIED_NAME("quickSightAnalysisQualifiedName.text"),
    /** TBC */
    QUICK_SIGHT_DASHBOARD_QUALIFIED_NAME("quickSightDashboardQualifiedName.text"),
    /** Qualified name of the parent dataset */
    QUICK_SIGHT_DATASET_QUALIFIED_NAME("quickSightDatasetQualifiedName.text"),
    /** TBC */
    QUICK_SIGHT_SHEET_NAME("quickSightSheetName"),
    /** Name of the query from which the visualization was created. */
    REDASH_QUERY_NAME("redashQueryName"),
    /** Unique name of the query from which the visualization was created. */
    REDASH_QUERY_QUALIFIED_NAME("redashQueryQualifiedName.text"),
    /** Human-readable schedule of the Redash query. */
    REDASH_QUERY_SCHEDULE_HUMANIZED("redashQueryScheduleHumanized.text"),
    /** Name of the bucket in which the object exists. */
    S3BUCKET_NAME("s3BucketName.text"),
    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    S3E_TAG("s3ETag.text"),
    /** Unique identity of the object in an S3 bucket. This is usually the concatenation of any prefix (folder) in the S3 bucket with the name of the object (file) itself. */
    S3OBJECT_KEY("s3ObjectKey.text"),
    /** TBC */
    SAMPLE_DATA_URL("sampleDataUrl.text"),
    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    SCHEMA_NAME("schemaName"),
    /** Definition of the latest schema in the subject. */
    SCHEMA_REGISTRY_SUBJECT_LATEST_SCHEMA_DEFINITION("schemaRegistrySubjectLatestSchemaDefinition"),
    /** Human-readable name of the dataset that contains this column. */
    SIGMA_DATASET_NAME("sigmaDatasetName"),
    /** Unique name of the dataset that contains this column. */
    SIGMA_DATASET_QUALIFIED_NAME("sigmaDatasetQualifiedName.text"),
    /** TBC */
    SIGMA_DATA_ELEMENT_FIELD_FORMULA("sigmaDataElementFieldFormula"),
    /** TBC */
    SIGMA_DATA_ELEMENT_NAME("sigmaDataElementName"),
    /** TBC */
    SIGMA_DATA_ELEMENT_QUALIFIED_NAME("sigmaDataElementQualifiedName.text"),
    /** TBC */
    SIGMA_PAGE_NAME("sigmaPageName"),
    /** TBC */
    SIGMA_PAGE_QUALIFIED_NAME("sigmaPageQualifiedName.text"),
    /** TBC */
    SIGMA_WORKBOOK_NAME("sigmaWorkbookName"),
    /** TBC */
    SIGMA_WORKBOOK_QUALIFIED_NAME("sigmaWorkbookQualifiedName.text"),
    /** TBC */
    SNOWFLAKE_PIPE_NOTIFICATION_CHANNEL_NAME("snowflakePipeNotificationChannelName.text"),
    /** All super types of an asset. */
    SUPER_TYPE_NAMES("__superTypeNames"),
    /** TBC */
    TABLEAU_DATASOURCE_FIELD_DATA_TYPE("tableauDatasourceFieldDataType.text"),
    /** TBC */
    TABLEAU_DATA_TYPE("tableauDataType.text"),
    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    TABLE_NAME("tableName"),
    /** Allowed values for the tag in the source system. These are denormalized from tagAttributes for ease of querying. */
    TAG_ALLOWED_VALUES("tagAllowedValues.text"),
    /** Name of the Liveboard in which the Dashlet exists. */
    THOUGHTSPOT_LIVEBOARD_NAME("thoughtspotLiveboardName"),
    /** Unique name of the Liveboard in which the Dashlet exists. */
    THOUGHTSPOT_LIVEBOARD_QUALIFIED_NAME("thoughtspotLiveboardQualifiedName.text"),
    /** TBC */
    THOUGHTSPOT_QUESTION_TEXT("thoughtspotQuestionText"),
    /** Type of the asset. For example Table, Column, and so on. */
    TYPE_NAME("__typeName"),
    /** Description of the asset, as provided by a user. If present, this will be used for the description in user interfaces. If not present, the description will be used. */
    USER_DESCRIPTION("userDescription"),
    /** TBC */
    VIEW_NAME("viewName"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    TextFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
