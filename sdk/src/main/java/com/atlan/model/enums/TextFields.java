/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
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
    /** Resource group for this account. */
    ADLS_ACCOUNT_RESOURCE_GROUP("adlsAccountResourceGroup"),
    /** Subscription for this account. */
    ADLS_ACCOUNT_SUBSCRIPTION("adlsAccountSubscription"),
    /** Unique name of the container this object exists within. */
    ADLS_CONTAINER_QUALIFIED_NAME("adlsContainerQualifiedName.text"),
    /** URL of this container. */
    ADLS_CONTAINER_URL("adlsContainerUrl"),
    /** Cache control of this object. */
    ADLS_OBJECT_CACHE_CONTROL("adlsObjectCacheControl"),
    /** Language of this object's contents. */
    ADLS_OBJECT_CONTENT_LANGUAGE("adlsObjectContentLanguage"),
    /** Content type of this object. */
    ADLS_OBJECT_CONTENT_TYPE("adlsObjectContentType"),
    /** URL of this object. */
    ADLS_OBJECT_URL("adlsObjectUrl"),
    /** Simple name of the DAG this task is contained within. */
    AIRFLOW_DAG_NAME("airflowDagName"),
    /** Identifier for the connection this task accesses. */
    AIRFLOW_TASK_CONNECTION_ID("airflowTaskConnectionId"),
    /** Class name for the operator this task uses. */
    AIRFLOW_TASK_OPERATOR_CLASS("airflowTaskOperatorClass"),
    /** Name of this asset in the Salesforce API. */
    API_NAME("apiName"),
    /** Absolute path to an individual endpoint. */
    API_PATH_RAW_URI("apiPathRawURI.text"),
    /** Descriptive summary intended to apply to all operations in this path. */
    API_PATH_SUMMARY("apiPathSummary"),
    /** Email address for a contact responsible for the API specification. */
    API_SPEC_CONTACT_EMAIL("apiSpecContactEmail.text"),
    /** Name of the contact responsible for the API specification. */
    API_SPEC_CONTACT_NAME("apiSpecContactName"),
    /** URL pointing to the contact information. */
    API_SPEC_CONTACT_URL("apiSpecContactURL.text"),
    /** Name of the license under which the API specification is available. */
    API_SPEC_LICENSE_NAME("apiSpecLicenseName"),
    /** URL to the license under which the API specification is available. */
    API_SPEC_LICENSE_URL("apiSpecLicenseURL.text"),
    /** Simple name of the API spec, if this asset is contained in an API spec. */
    API_SPEC_NAME("apiSpecName"),
    /** Unique name of the API spec, if this asset is contained in an API spec. */
    API_SPEC_QUALIFIED_NAME("apiSpecQualifiedName.text"),
    /** Service alias for the API specification. */
    API_SPEC_SERVICE_ALIAS("apiSpecServiceAlias.text"),
    /** URL to the terms of service for the API specification. */
    API_SPEC_TERMS_OF_SERVICE_URL("apiSpecTermsOfServiceURL.text"),
    /** Name of the account in which this asset exists in dbt. */
    ASSET_DBT_ACCOUNT_NAME("assetDbtAccountName"),
    /** Alias of this asset in dbt. */
    ASSET_DBT_ALIAS("assetDbtAlias"),
    /** Name of the environment in which this asset is materialized in dbt. */
    ASSET_DBT_ENVIRONMENT_NAME("assetDbtEnvironmentName"),
    /** Branch in git from which the last run of the job that materialized this asset in dbt ran. */
    ASSET_DBT_JOB_LAST_RUN_GIT_BRANCH("assetDbtJobLastRunGitBranch.text"),
    /** Status message of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_STATUS_MESSAGE("assetDbtJobLastRunStatusMessage"),
    /** Name of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_NAME("assetDbtJobName"),
    /** Human-readable time when the next run of the job that materializes this asset in dbt is scheduled. */
    ASSET_DBT_JOB_NEXT_RUN_HUMANIZED("assetDbtJobNextRunHumanized"),
    /** Human-readable cron schedule of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_SCHEDULE_CRON_HUMANIZED("assetDbtJobScheduleCronHumanized"),
    /** Name of the package in which this asset exists in dbt. */
    ASSET_DBT_PACKAGE_NAME("assetDbtPackageName"),
    /** Name of the project in which this asset exists in dbt. */
    ASSET_DBT_PROJECT_NAME("assetDbtProjectName"),
    /** List of tags attached to this asset in dbt. */
    ASSET_DBT_TAGS("assetDbtTags.text"),
    /** Unique identifier of this asset in dbt. */
    ASSET_DBT_UNIQUE_ID("assetDbtUniqueId"),
    /** List of Monte Carlo incident names attached to this asset. */
    ASSET_MC_INCIDENT_NAMES("assetMcIncidentNames"),
    /** List of unique Monte Carlo incident names attached to this asset. */
    ASSET_MC_INCIDENT_QUALIFIED_NAMES("assetMcIncidentQualifiedNames.text"),
    /** List of Monte Carlo monitor names attached to this asset. */
    ASSET_MC_MONITOR_NAMES("assetMcMonitorNames"),
    /** List of unique Monte Carlo monitor names attached to this asset. */
    ASSET_MC_MONITOR_QUALIFIED_NAMES("assetMcMonitorQualifiedNames.text"),
    /** List of tags attached to this asset. */
    ASSET_TAGS("assetTags.text"),
    /** All Atlan tags that exist on an asset, whether directly assigned or propagated, searchable by the internal hashed-string ID of the Atlan tag. */
    ATLAN_TAGS_TEXT("__classificationsText"),
    /** Amazon Resource Name (ARN) for this asset. This uniquely identifies the asset in AWS, and thus must be unique across all AWS asset instances. */
    AWS_ARN("awsArn.text"),
    /** Root user's name. */
    AWS_OWNER_NAME("awsOwnerName.text"),
    /** Resource identifier of this asset in Azure. */
    AZURE_RESOURCE_ID("azureResourceId.text"),
    /** Simple name of the AzureServiceBus Namespace in which this asset exists. */
    AZURE_SERVICE_BUS_NAMESPACE_NAME("azureServiceBusNamespaceName"),
    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    CALCULATION_VIEW_NAME("calculationViewName"),
    /** Status of this asset's certification. */
    CERTIFICATE_STATUS("certificateStatus.text"),
    /** Unique name of the collection in which this query exists. */
    COLLECTION_QUALIFIED_NAME("collectionQualifiedName.text"),
    /** Simple name of the connection through which this asset is accessible. */
    CONNECTION_NAME("connectionName.text"),
    /** Unique name of the connection through which this asset is accessible. */
    CONNECTION_QUALIFIED_NAME("connectionQualifiedName.text"),
    /** Simple name of the cube dimension in which this asset exists, or empty if it is itself a dimension. */
    CUBE_DIMENSION_NAME("cubeDimensionName"),
    /** Expression used to calculate this measure. */
    CUBE_FIELD_MEASURE_EXPRESSION("cubeFieldMeasureExpression"),
    /** Simple name of the dimension hierarchy in which this asset exists, or empty if it is itself a hierarchy. */
    CUBE_HIERARCHY_NAME("cubeHierarchyName"),
    /** Simple name of the cube in which this asset exists, or empty if it is itself a cube. */
    CUBE_NAME("cubeName"),
    /** Name of the parent field in which this field is nested. */
    CUBE_PARENT_FIELD_NAME("cubeParentFieldName"),
    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    DATABASE_NAME("databaseName"),
    /** Title of the Google Data Studio asset. */
    DATA_STUDIO_ASSET_TITLE("dataStudioAssetTitle"),
    /** Data type of values in this field. */
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
    /** Unique name of this asset in dbt. */
    DBT_QUALIFIED_NAME("dbtQualifiedName.text"),
    /** Raw code of the test (when the test is defined using Python). */
    DBT_TEST_RAW_CODE("dbtTestRawCode.text"),
    /** Raw SQL of the test. */
    DBT_TEST_RAW_SQL("dbtTestRawSQL.text"),
    /** TBC */
    DBT_UNIQUE_ID("dbtUniqueId"),
    /** Unique name of the default database to use for this query. */
    DEFAULT_DATABASE_QUALIFIED_NAME("defaultDatabaseQualifiedName.text"),
    /** Unique name of the default schema to use for this query. */
    DEFAULT_SCHEMA_QUALIFIED_NAME("defaultSchemaQualifiedName.text"),
    /** Description of this asset, for example as crawled from a source. Fallback for display purposes, if userDescription is empty. */
    DESCRIPTION("description"),
    /** Human-readable name of this asset used for display purposes (in user interface). */
    DISPLAY_NAME("displayName"),
    /** Lifecycle rules for this bucket. */
    GCS_BUCKET_LIFECYCLE_RULES("gcsBucketLifecycleRules"),
    /** Simple name of the bucket in which this object exists. */
    GCS_BUCKET_NAME("gcsBucketName"),
    /** Unique name of the bucket in which this object exists. */
    GCS_BUCKET_QUALIFIED_NAME("gcsBucketQualifiedName.text"),
    /** Retention policy for this bucket. */
    GCS_BUCKET_RETENTION_POLICY("gcsBucketRetentionPolicy"),
    /** Key of this object, in GCS. */
    GCS_OBJECT_KEY("gcsObjectKey.text"),
    /** Media link to this object. */
    GCS_OBJECT_MEDIA_LINK("gcsObjectMediaLink.text"),
    /** ID of the project in which the asset exists. */
    GOOGLE_PROJECT_ID("googleProjectId.text"),
    /** Name of the project in which the asset exists. */
    GOOGLE_PROJECT_NAME("googleProjectName.text"),
    /** Help text for this field. */
    INLINE_HELP_TEXT("inlineHelpText"),
    /** Unique name of the Explore in which this field exists. */
    LOOKER_EXPLORE_QUALIFIED_NAME("lookerExploreQualifiedName.text"),
    /** Unique name of the view in which this field exists. */
    LOOKER_VIEW_QUALIFIED_NAME("lookerViewQualifiedName.text"),
    /** Simple name of the Matillion group to which the project belongs. */
    MATILLION_GROUP_NAME("matillionGroupName"),
    /** Unique name of the Matillion group to which the project belongs. */
    MATILLION_GROUP_QUALIFIED_NAME("matillionGroupQualifiedName.text"),
    /** Simple name of the job to which the component belongs. */
    MATILLION_JOB_NAME("matillionJobName"),
    /** Path of the job within the project. Jobs can be managed at multiple folder levels within a project. */
    MATILLION_JOB_PATH("matillionJobPath.text"),
    /** Unique name of the job to which the component belongs. */
    MATILLION_JOB_QUALIFIED_NAME("matillionJobQualifiedName.text"),
    /** Simple name of the project to which the job belongs. */
    MATILLION_PROJECT_NAME("matillionProjectName"),
    /** Unique name of the project to which the job belongs. */
    MATILLION_PROJECT_QUALIFIED_NAME("matillionProjectQualifiedName.text"),
    /** Condition on which the monitor produces an alert. */
    MC_MONITOR_ALERT_CONDITION("mcMonitorAlertCondition"),
    /** Namespace of this monitor. */
    MC_MONITOR_NAMESPACE("mcMonitorNamespace"),
    /** Readable description of the schedule for the rule. */
    MC_MONITOR_RULE_SCHEDULE_CONFIG_HUMANIZED("mcMonitorRuleScheduleConfigHumanized"),
    /** All terms attached to an asset, as a single comma-separated string. */
    MEANINGS_TEXT("__meaningsText"),
    /** Simple name of the Metabase collection in which this asset exists. */
    METABASE_COLLECTION_NAME("metabaseCollectionName"),
    /** Unique name of the Metabase collection in which this asset exists. */
    METABASE_COLLECTION_QUALIFIED_NAME("metabaseCollectionQualifiedName.text"),
    /** TBC */
    METABASE_NAMESPACE("metabaseNamespace.text"),
    /** TBC */
    METABASE_QUERY("metabaseQuery"),
    /** TBC */
    METABASE_QUERY_TYPE("metabaseQueryType.text"),
    /** TBC */
    METABASE_SLUG("metabaseSlug.text"),
    /** Filters to be applied to the metric query. */
    METRIC_FILTERS("metricFilters"),
    /** List of time grains to be applied to the metric query. */
    METRIC_TIME_GRAINS("metricTimeGrains"),
    /** List of simple names of attributes related to this metric. */
    MICRO_STRATEGY_ATTRIBUTE_NAMES("microStrategyAttributeNames"),
    /** List of unique names of attributes related to this metric. */
    MICRO_STRATEGY_ATTRIBUTE_QUALIFIED_NAMES("microStrategyAttributeQualifiedNames.text"),
    /** Simple names of the cubes related to this asset. */
    MICRO_STRATEGY_CUBE_NAMES("microStrategyCubeNames"),
    /** Unique names of the cubes related to this asset. */
    MICRO_STRATEGY_CUBE_QUALIFIED_NAMES("microStrategyCubeQualifiedNames.text"),
    /** Simple name of the dossier in which this visualization exists. */
    MICRO_STRATEGY_DOSSIER_NAME("microStrategyDossierName"),
    /** Unique name of the dossier in which this visualization exists. */
    MICRO_STRATEGY_DOSSIER_QUALIFIED_NAME("microStrategyDossierQualifiedName.text"),
    /** List of simple names of facts related to this metric. */
    MICRO_STRATEGY_FACT_NAMES("microStrategyFactNames"),
    /** List of unique names of facts related to this metric. */
    MICRO_STRATEGY_FACT_QUALIFIED_NAMES("microStrategyFactQualifiedNames.text"),
    /** List of simple names of parent metrics of this metric. */
    MICRO_STRATEGY_METRIC_PARENT_NAMES("microStrategyMetricParentNames"),
    /** List of unique names of parent metrics of this metric. */
    MICRO_STRATEGY_METRIC_PARENT_QUALIFIED_NAMES("microStrategyMetricParentQualifiedNames.text"),
    /** Simple name of the project in which this asset exists. */
    MICRO_STRATEGY_PROJECT_NAME("microStrategyProjectName"),
    /** Unique name of the project in which this asset exists. */
    MICRO_STRATEGY_PROJECT_QUALIFIED_NAME("microStrategyProjectQualifiedName.text"),
    /** Simple names of the reports related to this asset. */
    MICRO_STRATEGY_REPORT_NAMES("microStrategyReportNames"),
    /** Unique names of the reports related to this asset. */
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
    /** Subtype of a MongoDB collection, for example: Capped, Time Series, etc. */
    MONGO_DB_COLLECTION_SUBTYPE("mongoDBCollectionSubtype.text"),
    /** Name of this asset. Fallback for display purposes, if displayName is empty. */
    NAME("name"),
    /** Represents attributes for describing the key schema for the table and indexes. */
    NO_SQL_SCHEMA_DEFINITION("noSQLSchemaDefinition"),
    /** Simple name of the column this column is nested within, for STRUCT and NESTED columns. */
    PARENT_COLUMN_NAME("parentColumnName"),
    /** Unique name of the column this column is nested within, for STRUCT and NESTED columns. */
    PARENT_COLUMN_QUALIFIED_NAME("parentColumnQualifiedName.text"),
    /** Unique name of the parent domain in which this asset exists. */
    PARENT_DOMAIN_QUALIFIED_NAME("parentDomainQualifiedName.text"),
    /** Unique name of the parent collection or folder in which this query exists. */
    PARENT_QUALIFIED_NAME("parentQualifiedName.text"),
    /** DAX expression for this measure. */
    POWER_BI_MEASURE_EXPRESSION("powerBIMeasureExpression"),
    /** Unique name of the Power BI table in which this asset exists. */
    POWER_BI_TABLE_QUALIFIED_NAME("powerBITableQualifiedName.text"),
    /** TBC */
    PRESET_CHART_DESCRIPTION_MARKDOWN("presetChartDescriptionMarkdown"),
    /** TBC */
    PRESET_DASHBOARD_CHANGED_BY_NAME("presetDashboardChangedByName"),
    /** Unique name of the dashboard in which this asset exists. */
    PRESET_DASHBOARD_QUALIFIED_NAME("presetDashboardQualifiedName.text"),
    /** TBC */
    PRESET_DATASET_DATASOURCE_NAME("presetDatasetDatasourceName"),
    /** TBC */
    PRESET_WORKSPACE_HOSTNAME("presetWorkspaceHostname.text"),
    /** Unique name of the workspace in which this asset exists. */
    PRESET_WORKSPACE_QUALIFIED_NAME("presetWorkspaceQualifiedName.text"),
    /** TBC */
    PRESET_WORKSPACE_REGION("presetWorkspaceRegion.text"),
    /** Unique name of the app where this asset belongs. */
    QLIK_APP_QUALIFIED_NAME("qlikAppQualifiedName.text"),
    /** Footnote of this chart. */
    QLIK_CHART_FOOTNOTE("qlikChartFootnote"),
    /** Subtitle of this chart. */
    QLIK_CHART_SUBTITLE("qlikChartSubtitle"),
    /** Technical name of this asset. */
    QLIK_DATASET_TECHNICAL_NAME("qlikDatasetTechnicalName"),
    /** URI of this dataset. */
    QLIK_DATASET_URI("qlikDatasetUri.text"),
    /** Unique QRI of this asset, from Qlik. */
    QLIK_QRI("qlikQRI.text"),
    /** Unique name of the space in which this asset exists. */
    QLIK_SPACE_QUALIFIED_NAME("qlikSpaceQualifiedName.text"),
    /** Unique fully-qualified name of the asset in Atlan. */
    QUALIFIED_NAME("qualifiedName.text"),
    /** Unique name of the QuickSight analysis in which this visual exists. */
    QUICK_SIGHT_ANALYSIS_QUALIFIED_NAME("quickSightAnalysisQualifiedName.text"),
    /** Unique name of the dashboard in which this visual exists. */
    QUICK_SIGHT_DASHBOARD_QUALIFIED_NAME("quickSightDashboardQualifiedName.text"),
    /** Unique name of the dataset in which this field exists. */
    QUICK_SIGHT_DATASET_QUALIFIED_NAME("quickSightDatasetQualifiedName.text"),
    /** TBC */
    QUICK_SIGHT_SHEET_NAME("quickSightSheetName"),
    /** Simple name of the query from which this visualization is created. */
    REDASH_QUERY_NAME("redashQueryName"),
    /** Unique name of the query from which this visualization is created. */
    REDASH_QUERY_QUALIFIED_NAME("redashQueryQualifiedName.text"),
    /** Schdule for this query in readable text for overview tab and filtering. */
    REDASH_QUERY_SCHEDULE_HUMANIZED("redashQueryScheduleHumanized.text"),
    /** Simple name of the bucket in which this object exists. */
    S3BUCKET_NAME("s3BucketName.text"),
    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    S3E_TAG("s3ETag.text"),
    /** Unique identity of this object in an S3 bucket. This is usually the concatenation of any prefix (folder) in the S3 bucket with the name of the object (file) itself. */
    S3OBJECT_KEY("s3ObjectKey.text"),
    /** URL for sample data for this asset. */
    SAMPLE_DATA_URL("sampleDataUrl.text"),
    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    SCHEMA_NAME("schemaName"),
    /** Definition of the latest schema in the subject. */
    SCHEMA_REGISTRY_SUBJECT_LATEST_SCHEMA_DEFINITION("schemaRegistrySubjectLatestSchemaDefinition"),
    /** Simple name of the dataset in which this column exists. */
    SIGMA_DATASET_NAME("sigmaDatasetName"),
    /** Unique name of the dataset in which this column exists. */
    SIGMA_DATASET_QUALIFIED_NAME("sigmaDatasetQualifiedName.text"),
    /** TBC */
    SIGMA_DATA_ELEMENT_FIELD_FORMULA("sigmaDataElementFieldFormula"),
    /** Simple name of the data element in which this asset exists. */
    SIGMA_DATA_ELEMENT_NAME("sigmaDataElementName"),
    /** Unique name of the data element in which this asset exists. */
    SIGMA_DATA_ELEMENT_QUALIFIED_NAME("sigmaDataElementQualifiedName.text"),
    /** Simple name of the page on which this asset exists. */
    SIGMA_PAGE_NAME("sigmaPageName"),
    /** Unique name of the page on which this asset exists. */
    SIGMA_PAGE_QUALIFIED_NAME("sigmaPageQualifiedName.text"),
    /** Simple name of the workbook in which this asset exists. */
    SIGMA_WORKBOOK_NAME("sigmaWorkbookName"),
    /** Unique name of the workbook in which this asset exists. */
    SIGMA_WORKBOOK_QUALIFIED_NAME("sigmaWorkbookQualifiedName.text"),
    /** Unique name of the folder in which this dashboard exists. */
    SISENSE_DASHBOARD_FOLDER_QUALIFIED_NAME("sisenseDashboardFolderQualifiedName.text"),
    /** Unique name of the datamodel in which this datamodel table exists. */
    SISENSE_DATAMODEL_QUALIFIED_NAME("sisenseDatamodelQualifiedName.text"),
    /** Unique name of the parent folder in which this folder exists. */
    SISENSE_FOLDER_PARENT_FOLDER_QUALIFIED_NAME("sisenseFolderParentFolderQualifiedName.text"),
    /** Unique name of the dashboard in which this widget exists. */
    SISENSE_WIDGET_DASHBOARD_QUALIFIED_NAME("sisenseWidgetDashboardQualifiedName.text"),
    /** Unique name of the folder in which this widget exists. */
    SISENSE_WIDGET_FOLDER_QUALIFIED_NAME("sisenseWidgetFolderQualifiedName.text"),
    /** Name of the notification channel for this pipe. */
    SNOWFLAKE_PIPE_NOTIFICATION_CHANNEL_NAME("snowflakePipeNotificationChannelName.text"),
    /** Name of the Spark app containing this Spark Job For eg. extract_raw_data */
    SPARK_APP_NAME("sparkAppName"),
    /** Unique name of the top-level domain in which this asset exists. */
    SUPER_DOMAIN_QUALIFIED_NAME("superDomainQualifiedName.text"),
    /** All super types of an asset. */
    SUPER_TYPE_NAMES("__superTypeNames"),
    /** Data type of this field. */
    TABLEAU_DATASOURCE_FIELD_DATA_TYPE("tableauDatasourceFieldDataType.text"),
    /** Data type of the field, from Tableau. */
    TABLEAU_DATA_TYPE("tableauDataType.text"),
    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    TABLE_NAME("tableName"),
    /** Allowed values for the tag in the source system. These are denormalized from tagAttributes for ease of querying. */
    TAG_ALLOWED_VALUES("tagAllowedValues.text"),
    /** Represents associated tag value */
    TAG_ATTACHMENT_STRING_VALUE("tagAttachmentStringValue.text"),
    /** Represents associated source tag's qualified name */
    TAG_QUALIFIED_NAME("tagQualifiedName.text"),
    /** Simple name of the liveboard in which this dashlet exists. */
    THOUGHTSPOT_LIVEBOARD_NAME("thoughtspotLiveboardName"),
    /** Unique name of the liveboard in which this dashlet exists. */
    THOUGHTSPOT_LIVEBOARD_QUALIFIED_NAME("thoughtspotLiveboardQualifiedName.text"),
    /** TBC */
    THOUGHTSPOT_QUESTION_TEXT("thoughtspotQuestionText"),
    /** Unique name of the table in which this column exists. */
    THOUGHTSPOT_TABLE_QUALIFIED_NAME("thoughtspotTableQualifiedName.text"),
    /** Unique name of the view in which this column exists. */
    THOUGHTSPOT_VIEW_QUALIFIED_NAME("thoughtspotViewQualifiedName.text"),
    /** Unique name of the worksheet in which this column exists. */
    THOUGHTSPOT_WORKSHEET_QUALIFIED_NAME("thoughtspotWorksheetQualifiedName.text"),
    /** Type of the asset. For example Table, Column, and so on. */
    TYPE_NAME("__typeName"),
    /** Description of this asset, as provided by a user. If present, this will be used for the description in user interface. */
    USER_DESCRIPTION("userDescription"),
    /** Name of the view for the Explore. */
    VIEW_NAME("viewName"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    TextFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
