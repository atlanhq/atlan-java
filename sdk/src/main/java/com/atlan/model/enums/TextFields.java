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
    /** Unused. Abbreviation of the term. */
    ABBREVIATION("abbreviation"),
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
    /** Tags assigned to the asset in Airflow. */
    AIRFLOW_TAGS("airflowTags"),
    /** Identifier for the connection this task accesses. */
    AIRFLOW_TASK_CONNECTION_ID("airflowTaskConnectionId"),
    /** Class name for the operator this task uses. */
    AIRFLOW_TASK_OPERATOR_CLASS("airflowTaskOperatorClass"),
    /** SQL code that executes through this task. */
    AIRFLOW_TASK_SQL("airflowTaskSql"),
    /** Alias for this materialized view. */
    ALIAS("alias"),
    /** Detailed message to include in the announcement on this asset. */
    ANNOUNCEMENT_MESSAGE("announcementMessage"),
    /** Brief title for the announcement on this asset. Required when announcementType is specified. */
    ANNOUNCEMENT_TITLE("announcementTitle"),
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
    /** TBC */
    ASSET_COVER_IMAGE("assetCoverImage"),
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
    /** Human-readable total duration of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_TOTAL_DURATION_HUMANIZED("assetDbtJobLastRunTotalDurationHumanized"),
    /** Name of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_NAME("assetDbtJobName"),
    /** Human-readable time when the next run of the job that materializes this asset in dbt is scheduled. */
    ASSET_DBT_JOB_NEXT_RUN_HUMANIZED("assetDbtJobNextRunHumanized"),
    /** Human-readable cron schedule of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_SCHEDULE_CRON_HUMANIZED("assetDbtJobScheduleCronHumanized"),
    /** Metadata for this asset in dbt, specifically everything under the 'meta' key in the dbt object. */
    ASSET_DBT_META("assetDbtMeta"),
    /** Name of the package in which this asset exists in dbt. */
    ASSET_DBT_PACKAGE_NAME("assetDbtPackageName"),
    /** Name of the project in which this asset exists in dbt. */
    ASSET_DBT_PROJECT_NAME("assetDbtProjectName"),
    /** Freshness criteria for the source of this asset in dbt. */
    ASSET_DBT_SOURCE_FRESHNESS_CRITERIA("assetDbtSourceFreshnessCriteria"),
    /** List of tags attached to this asset in dbt. */
    ASSET_DBT_TAGS("assetDbtTags.text"),
    /** Unique identifier of this asset in dbt. */
    ASSET_DBT_UNIQUE_ID("assetDbtUniqueId"),
    /** Name of the icon to use for this asset. (Only applies to glossaries, currently.) */
    ASSET_ICON("assetIcon"),
    /** List of unique Monte Carlo alert names attached to this asset. */
    ASSET_MC_ALERT_QUALIFIED_NAMES("assetMcAlertQualifiedNames.text"),
    /** List of Monte Carlo incident names attached to this asset. */
    ASSET_MC_INCIDENT_NAMES("assetMcIncidentNames"),
    /** List of unique Monte Carlo incident names attached to this asset. */
    ASSET_MC_INCIDENT_QUALIFIED_NAMES("assetMcIncidentQualifiedNames.text"),
    /** List of Monte Carlo monitor names attached to this asset. */
    ASSET_MC_MONITOR_NAMES("assetMcMonitorNames"),
    /** List of unique Monte Carlo monitor names attached to this asset. */
    ASSET_MC_MONITOR_QUALIFIED_NAMES("assetMcMonitorQualifiedNames.text"),
    /** All associated Soda check statuses. */
    ASSET_SODA_CHECK_STATUSES("assetSodaCheckStatuses"),
    /** List of tags attached to this asset. */
    ASSET_TAGS("assetTags.text"),
    /** Color (in hexadecimal RGB) to use to represent this asset. */
    ASSET_THEME_HEX("assetThemeHex"),
    /** Parsed AST of the code or SQL statements that describe the logic of this process. */
    AST("ast"),
    /** All Atlan tags that exist on an asset, whether directly assigned or propagated, searchable by the internal hashed-string ID of the Atlan tag. */
    ATLAN_TAGS_TEXT("__classificationsText"),
    /** TBC */
    ATLAS_SERVER_DISPLAY_NAME("AtlasServer.displayName"),
    /** TBC */
    ATLAS_SERVER_NAME("AtlasServer.name"),
    /** TBC */
    ATLAS_USER_PROFILE_NAME("__AtlasUserProfile.name"),
    /** TBC */
    ATLAS_USER_SAVED_SEARCH_NAME("__AtlasUserSavedSearch.name"),
    /** Amazon Resource Name (ARN) for this asset. This uniquely identifies the asset in AWS, and thus must be unique across all AWS asset instances. */
    AWS_ARN("awsArn.text"),
    /** Root user's name. */
    AWS_OWNER_NAME("awsOwnerName.text"),
    /** Resource identifier of this asset in Azure. */
    AZURE_RESOURCE_ID("azureResourceId.text"),
    /** Simple name of the AzureServiceBus Namespace in which this asset exists. */
    AZURE_SERVICE_BUS_NAMESPACE_NAME("azureServiceBusNamespaceName"),
    /** Business Policy Exception Filter ES DSL to denote the associate asset/s involved. */
    BUSINESS_POLICY_EXCEPTION_FILTER_DSL("businessPolicyExceptionFilterDSL"),
    /** Business Policy Filter ES DSL to denote the associate asset/s involved. */
    BUSINESS_POLICY_FILTER_DSL("businessPolicyFilterDSL"),
    /** Filter ES DSL to denote the associate asset/s involved. */
    BUSINESS_POLICY_INCIDENT_FILTER_DSL("businessPolicyIncidentFilterDSL"),
    /** Unique name of the business policy through which this asset is accessible. */
    BUSINESS_POLICY_QUALIFIED_NAME("businessPolicyQualifiedName.text"),
    /** Selected approval workflow id for business policy */
    BUSINESS_POLICY_SELECTED_APPROVAL_WF("businessPolicySelectedApprovalWF"),
    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    CALCULATION_VIEW_NAME("calculationViewName"),
    /** Type of connection, for example WAREHOUSE, RDBMS, etc. */
    CATEGORY("category"),
    /** Status of this asset's certification. */
    CERTIFICATE_STATUS("certificateStatus.text"),
    /** Human-readable descriptive message used to provide further detail to certificateStatus. */
    CERTIFICATE_STATUS_MESSAGE("certificateStatusMessage"),
    /** Notes related to this datasource being cerfified, in Tableau. */
    CERTIFICATION_NOTE("certificationNote"),
    /** Name of the user who cerified this datasource, in Tableau. */
    CERTIFIER_DISPLAY_NAME("certifierDisplayName"),
    /** TBC */
    CHANNEL_LINK("channelLink"),
    /** TBC */
    CLIENT_ID("clientId"),
    /** Code that ran within the process. */
    CODE("code"),
    /** Tooltip text present for the Cognos asset */
    COGNOS_DEFAULT_SCREEN_TIP("cognosDefaultScreenTip"),
    /** Name of the parent asset in Cognos */
    COGNOS_PARENT_NAME("cognosParentName"),
    /** Unique name of the collection in which this query exists. */
    COLLECTION_QUALIFIED_NAME("collectionQualifiedName.text"),
    /** List of the greatest values in a column. */
    COLUMN_MAXS("columnMaxs"),
    /** List of the least values in a column. */
    COLUMN_MINS("columnMins"),
    /** Simple name of the connection through which this asset is accessible. */
    CONNECTION_NAME("connectionName.text"),
    /** Unique name of the connection through which this asset is accessible. */
    CONNECTION_QUALIFIED_NAME("connectionQualifiedName.text"),
    /** Unused. Only the value of connectorType impacts icons. */
    CONNECTOR_ICON("connectorIcon"),
    /** Unused. Only the value of connectorType impacts icons. */
    CONNECTOR_IMAGE("connectorImage"),
    /** Constraint that defines this table partition. */
    CONSTRAINT("constraint"),
    /** Credential strategy to use for this connection for queries. */
    CREDENTIAL_STRATEGY("credentialStrategy"),
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
    /** Unique name of the dashboard in which this tile is pinned. */
    DASHBOARD_QUALIFIED_NAME("dashboardQualifiedName"),
    /** Type of dashboard in Salesforce. */
    DASHBOARD_TYPE("dashboardType"),
    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    DATABASE_NAME("databaseName"),
    /** Unique name of the dataset used to build this report. */
    DATASET_QUALIFIED_NAME("datasetQualifiedName"),
    /** Type of this datasource field. */
    DATASOURCE_FIELD_TYPE("datasourceFieldType"),
    /** Unique name of the datasource in which this datasource field exists. */
    DATASOURCE_QUALIFIED_NAME("datasourceQualifiedName"),
    /** Data category of this field. */
    DATA_CATEGORY("dataCategory"),
    /** (Deprecated) Replaced by dataContractSpec attribute. */
    DATA_CONTRACT_JSON("dataContractJson"),
    /** Actual content of the contract in YAML string format. Any changes to this string should create a new instance (with new sequential version number). */
    DATA_CONTRACT_SPEC("dataContractSpec"),
    /** Search DSL used to define which assets are part of this data product. */
    DATA_PRODUCT_ASSETS_DSL("dataProductAssetsDSL"),
    /** Playbook filter to define which assets are part of this data product. */
    DATA_PRODUCT_ASSETS_PLAYBOOK_FILTER("dataProductAssetsPlaybookFilter"),
    /** Title of the Google Data Studio asset. */
    DATA_STUDIO_ASSET_TITLE("dataStudioAssetTitle"),
    /** Data type of values in this field. */
    DATA_TYPE("dataType.text"),
    /** TBC */
    DBT_ACCOUNT_NAME("dbtAccountName"),
    /** TBC */
    DBT_ALIAS("dbtAlias"),
    /** TBC */
    DBT_COMPILED_SQL("dbtCompiledSQL"),
    /** TBC */
    DBT_CONNECTION_CONTEXT("dbtConnectionContext"),
    /** TBC */
    DBT_ENVIRONMENT_DBT_VERSION("dbtEnvironmentDbtVersion"),
    /** TBC */
    DBT_ENVIRONMENT_NAME("dbtEnvironmentName"),
    /** TBC */
    DBT_ERROR("dbtError"),
    /** TBC */
    DBT_FRESHNESS_CRITERIA("dbtFreshnessCriteria"),
    /** TBC */
    DBT_JOB_NAME("dbtJobName"),
    /** TBC */
    DBT_JOB_NEXT_RUN_HUMANIZED("dbtJobNextRunHumanized"),
    /** TBC */
    DBT_JOB_SCHEDULE("dbtJobSchedule"),
    /** TBC */
    DBT_JOB_SCHEDULE_CRON_HUMANIZED("dbtJobScheduleCronHumanized"),
    /** TBC */
    DBT_MATERIALIZATION_TYPE("dbtMaterializationType"),
    /** TBC */
    DBT_META("dbtMeta"),
    /** TBC */
    DBT_MODEL_QUALIFIED_NAME("dbtModelQualifiedName.text"),
    /** TBC */
    DBT_PACKAGE_NAME("dbtPackageName"),
    /** TBC */
    DBT_PROJECT_NAME("dbtProjectName"),
    /** Unique name of this asset in dbt. */
    DBT_QUALIFIED_NAME("dbtQualifiedName.text"),
    /** TBC */
    DBT_RAW_SQL("dbtRawSQL"),
    /** TBC */
    DBT_STATS("dbtStats"),
    /** TBC */
    DBT_TAGS("dbtTags"),
    /** Compiled code of the test (when the test is defined using Python). */
    DBT_TEST_COMPILED_CODE("dbtTestCompiledCode"),
    /** Compiled SQL of the test. */
    DBT_TEST_COMPILED_SQL("dbtTestCompiledSQL"),
    /** Error message in the case of state being "error". */
    DBT_TEST_ERROR("dbtTestError"),
    /** Language in which the test is written, for example: SQL or Python. */
    DBT_TEST_LANGUAGE("dbtTestLanguage"),
    /** Raw code of the test (when the test is defined using Python). */
    DBT_TEST_RAW_CODE("dbtTestRawCode"),
    /** Raw SQL of the test. */
    DBT_TEST_RAW_SQL("dbtTestRawSQL"),
    /** TBC */
    DBT_UNIQUE_ID("dbtUniqueId"),
    /** Unique identifier (GUID) for the default credentials to use for this connection. */
    DEFAULT_CREDENTIAL_GUID("defaultCredentialGuid"),
    /** Unique name of the default database to use for this query. */
    DEFAULT_DATABASE_QUALIFIED_NAME("defaultDatabaseQualifiedName.text"),
    /** TBC */
    DEFAULT_NAVIGATION("defaultNavigation"),
    /** Unique name of the default schema to use for this query. */
    DEFAULT_SCHEMA_QUALIFIED_NAME("defaultSchemaQualifiedName.text"),
    /** Default value for this column. */
    DEFAULT_VALUE("defaultValue"),
    /** Formula for the default value for this field. */
    DEFAULT_VALUE_FORMULA("defaultValueFormula"),
    /** SQL definition of this materialized view. */
    DEFINITION("definition"),
    /** TBC */
    DENY_ASSET_FILTERS("denyAssetFilters"),
    /** TBC */
    DENY_ASSET_TYPES("denyAssetTypes"),
    /** TBC */
    DENY_NAVIGATION_PAGES("denyNavigationPages"),
    /** Description of this asset, for example as crawled from a source. Fallback for display purposes, if userDescription is empty. */
    DESCRIPTION("description"),
    /** Human-readable name of this asset used for display purposes (in user interface). */
    DISPLAY_NAME("displayName"),
    /** An ISO-8601 representation of the time the DataSet was last run. */
    DOMO_DATASET_LAST_RUN("domoDatasetLastRun"),
    /** A domain of the datam model in which this asset exists. */
    D_M_DATA_MODEL_DOMAIN("dMDataModelDomain"),
    /** Simple name of the model in which this asset exists, or empty if it is itself a data model. */
    D_M_DATA_MODEL_NAME("dMDataModelName"),
    /** A namespace of the data model in which this asset exists. */
    D_M_DATA_MODEL_NAMESPACE("dMDataModelNamespace"),
    /** Simple name of the entity in which this asset exists, or empty if it is itself a data model entity. */
    D_M_ENTITY_NAME("dMEntityName"),
    /** Simple name of the version in which this asset exists, or empty if it is itself a data model version. */
    D_M_VERSION_NAME("dMVersionName"),
    /** Unused. Exmaples of the term. */
    EXAMPLES("examples"),
    /** External location of this partition, for example: an S3 object location. */
    EXTERNAL_LOCATION("externalLocation"),
    /** Region of the external location of this partition, for example: S3 region. */
    EXTERNAL_LOCATION_REGION("externalLocationRegion"),
    /** Deprecated. */
    FIELDS("fields"),
    /** URL giving the online location where the file can be accessed. */
    FILE_PATH("filePath"),
    /** Name of the parent folder in Looker that contains this dashboard. */
    FOLDER_NAME("folderName"),
    /** Formula for this field, if it is a calculated field. */
    FORMULA("formula"),
    /** Name used internally in Tableau to uniquely identify this field. */
    FULLY_QUALIFIED_NAME("fullyQualifiedName"),
    /** TBC */
    FULL_NAME("fullName"),
    /** Code or set of statements that determine the output of the function. */
    FUNCTION_DEFINITION("functionDefinition"),
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
    /** Host name of this connection's source. */
    HOST("host"),
    /** Icon for the link. */
    ICON("icon"),
    /** Help text for this field. */
    INLINE_HELP_TEXT("inlineHelpText"),
    /** Unused. Language of the glossary's contents. */
    LANGUAGE("language"),
    /** URL to the resource. */
    LINK("link"),
    /** Unused. Detailed definition of the term. See 'readme' instead. */
    LONG_DESCRIPTION("longDescription"),
    /** Raw SQL query string. */
    LONG_RAW_QUERY("longRawQuery"),
    /** Unique name of the dashboard in which this field is used. */
    LOOKER_DASHBOARD_QUALIFIED_NAME("lookerDashboardQualifiedName.text"),
    /** Unique name of the Explore in which this field exists. */
    LOOKER_EXPLORE_QUALIFIED_NAME("lookerExploreQualifiedName.text"),
    /** Unique name of the look in which this field is used. */
    LOOKER_LOOK_QUALIFIED_NAME("lookerLookQualifiedName.text"),
    /** Unique name of the tile in which this field is used. */
    LOOKER_TILE_QUALIFIED_NAME("lookerTileQualifiedName.text"),
    /** File path of this view within the project. */
    LOOKER_VIEW_FILE_PATH("lookerViewFilePath"),
    /** Unique name of the view in which this field exists. */
    LOOKER_VIEW_QUALIFIED_NAME("lookerViewQualifiedName.text"),
    /** Identifier for the LoomML link. */
    LOOKML_LINK_ID("lookmlLinkId"),
    /** SQL queries used by the component. */
    MATILLION_COMPONENT_SQLS("matillionComponentSqls"),
    /** List of environments in the project. */
    MATILLION_ENVIRONMENTS("matillionEnvironments"),
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
    /** Current point in time state of a project. */
    MATILLION_VERSION("matillionVersion"),
    /** List of versions in the project. */
    MATILLION_VERSIONS("matillionVersions"),
    /** Condition on which the monitor produces an alert. */
    MC_MONITOR_ALERT_CONDITION("mcMonitorAlertCondition"),
    /** Namespace of this monitor. */
    MC_MONITOR_NAMESPACE("mcMonitorNamespace"),
    /** SQL code for custom SQL rules. */
    MC_MONITOR_RULE_CUSTOM_SQL("mcMonitorRuleCustomSql"),
    /** Readable description of the schedule for the rule. */
    MC_MONITOR_RULE_SCHEDULE_CONFIG_HUMANIZED("mcMonitorRuleScheduleConfigHumanized"),
    /** All terms attached to an asset, as a single comma-separated string. */
    MEANINGS_TEXT("__meaningsText"),
    /** Identifier for the merge result. */
    MERGE_RESULT_ID("mergeResultId"),
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
    /** SQL query used to compute the metric. */
    METRIC_SQL("metricSQL"),
    /** List of time grains to be applied to the metric query. */
    METRIC_TIME_GRAINS("metricTimeGrains"),
    /** JSON string specifying the attribute's name, description, displayFormat, etc. */
    MICRO_STRATEGY_ATTRIBUTE_FORMS("microStrategyAttributeForms"),
    /** List of simple names of attributes related to this metric. */
    MICRO_STRATEGY_ATTRIBUTE_NAMES("microStrategyAttributeNames"),
    /** List of unique names of attributes related to this metric. */
    MICRO_STRATEGY_ATTRIBUTE_QUALIFIED_NAMES("microStrategyAttributeQualifiedNames.text"),
    /** Simple names of the cubes related to this asset. */
    MICRO_STRATEGY_CUBE_NAMES("microStrategyCubeNames"),
    /** Unique names of the cubes related to this asset. */
    MICRO_STRATEGY_CUBE_QUALIFIED_NAMES("microStrategyCubeQualifiedNames.text"),
    /** Query used to create the cube. */
    MICRO_STRATEGY_CUBE_QUERY("microStrategyCubeQuery"),
    /** List of chapter names in this dossier. */
    MICRO_STRATEGY_DOSSIER_CHAPTER_NAMES("microStrategyDossierChapterNames"),
    /** Simple name of the dossier in which this visualization exists. */
    MICRO_STRATEGY_DOSSIER_NAME("microStrategyDossierName"),
    /** Unique name of the dossier in which this visualization exists. */
    MICRO_STRATEGY_DOSSIER_QUALIFIED_NAME("microStrategyDossierQualifiedName.text"),
    /** List of expressions for this fact. */
    MICRO_STRATEGY_FACT_EXPRESSIONS("microStrategyFactExpressions"),
    /** List of simple names of facts related to this metric. */
    MICRO_STRATEGY_FACT_NAMES("microStrategyFactNames"),
    /** List of unique names of facts related to this metric. */
    MICRO_STRATEGY_FACT_QUALIFIED_NAMES("microStrategyFactQualifiedNames.text"),
    /** Text specifiying this metric's expression. */
    MICRO_STRATEGY_METRIC_EXPRESSION("microStrategyMetricExpression"),
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
    /** Name of the parent model of this Explore. */
    MODEL_NAME("modelName"),
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
    /** Name of the field containing the date in each time series document. */
    MONGO_DB_COLLECTION_TIME_FIELD("mongoDBCollectionTimeField"),
    /** Name of this asset. Fallback for display purposes, if displayName is empty. */
    NAME("name"),
    /** Text of notes added to the tile. */
    NOTE_TEXT("noteText"),
    /** Represents attributes for describing the key schema for the table and indexes. */
    NO_SQL_SCHEMA_DEFINITION("noSQLSchemaDefinition"),
    /** TBC */
    OPERATION("operation"),
    /** TBC */
    OPERATION_PARAMS("operationParams"),
    /** Fully-qualified name of the organization in Salesforce. */
    ORGANIZATION_QUALIFIED_NAME("organizationQualifiedName"),
    /** TBC */
    OWNER_NAME("ownerName"),
    /** TBC */
    PARAMS("params"),
    /** Simple name of the column this column is nested within, for STRUCT and NESTED columns. */
    PARENT_COLUMN_NAME("parentColumnName"),
    /** Unique name of the column this column is nested within, for STRUCT and NESTED columns. */
    PARENT_COLUMN_QUALIFIED_NAME("parentColumnQualifiedName.text"),
    /** Unique name of the parent domain in which this asset exists. */
    PARENT_DOMAIN_QUALIFIED_NAME("parentDomainQualifiedName.text"),
    /** Unique name of the parent collection or folder in which this query exists. */
    PARENT_QUALIFIED_NAME("parentQualifiedName.text"),
    /** List of sub-partitions in this partition. */
    PARTITION_LIST("partitionList"),
    /** List of values from which a user can pick while adding a record. */
    PICKLIST_VALUES("picklistValues"),
    /** Format of this asset, as specified in the FORMAT_STRING of the MDX cell property. */
    POWER_BI_FORMAT_STRING("powerBIFormatString"),
    /** DAX expression for this measure. */
    POWER_BI_MEASURE_EXPRESSION("powerBIMeasureExpression"),
    /** Unique name of the Power BI table in which this asset exists. */
    POWER_BI_TABLE_QUALIFIED_NAME("powerBITableQualifiedName.text"),
    /** Power Query M expressions for the table. */
    POWER_BI_TABLE_SOURCE_EXPRESSIONS("powerBITableSourceExpressions"),
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
    /** Name of the parent project of this Explore. */
    PROJECT_NAME("projectName"),
    /** Unique name of the project in which this dashboard exists. */
    PROJECT_QUALIFIED_NAME("projectQualifiedName"),
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
    /** Query config for this connection. */
    QUERY_CONFIG("queryConfig"),
    /** List of field names calculated by this analysis. */
    QUICK_SIGHT_ANALYSIS_CALCULATED_FIELDS("quickSightAnalysisCalculatedFields"),
    /** List of filter groups used for this analysis. */
    QUICK_SIGHT_ANALYSIS_FILTER_GROUPS("quickSightAnalysisFilterGroups"),
    /** List of parameters used for this analysis. */
    QUICK_SIGHT_ANALYSIS_PARAMETER_DECLARATIONS("quickSightAnalysisParameterDeclarations"),
    /** Unique name of the QuickSight analysis in which this visual exists. */
    QUICK_SIGHT_ANALYSIS_QUALIFIED_NAME("quickSightAnalysisQualifiedName.text"),
    /** Unique name of the dashboard in which this visual exists. */
    QUICK_SIGHT_DASHBOARD_QUALIFIED_NAME("quickSightDashboardQualifiedName.text"),
    /** Unique name of the dataset in which this field exists. */
    QUICK_SIGHT_DATASET_QUALIFIED_NAME("quickSightDatasetQualifiedName.text"),
    /** TBC */
    QUICK_SIGHT_SHEET_NAME("quickSightSheetName"),
    /** TBC */
    RAW_DATA_TYPE_DEFINITION("rawDataTypeDefinition"),
    /** Deprecated. See 'longRawQuery' instead. */
    RAW_QUERY("rawQuery"),
    /** Simple name of the query from which this visualization is created. */
    REDASH_QUERY_NAME("redashQueryName"),
    /** Parameters of this query. */
    REDASH_QUERY_PARAMETERS("redashQueryParameters"),
    /** Unique name of the query from which this visualization is created. */
    REDASH_QUERY_QUALIFIED_NAME("redashQueryQualifiedName.text"),
    /** Schdule for this query in readable text for overview tab and filtering. */
    REDASH_QUERY_SCHEDULE_HUMANIZED("redashQueryScheduleHumanized.text"),
    /** SQL code of this query. */
    REDASH_QUERY_SQL("redashQuerySQL"),
    /** Reference to the resource. */
    REFERENCE("reference"),
    /** Unique name of the report in which this page exists. */
    REPORT_QUALIFIED_NAME("reportQualifiedName"),
    /** TBC */
    RESULT("result"),
    /** TBC */
    RESULT_SUMMARY("resultSummary"),
    /** Role of this field, for example: 'dimension', 'measure', or 'unknown'. */
    ROLE("role"),
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
    /** TBC */
    SEARCH_PARAMETERS("searchParameters"),
    /** TBC */
    SEARCH_TYPE("searchType"),
    /** Unused. Brief summary of the term. See 'description' and 'userDescription' instead. */
    SHORT_DESCRIPTION("shortDescription"),
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
    /** TBC */
    SIGMA_DATA_ELEMENT_QUERY("sigmaDataElementQuery"),
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
    /** SQL expression of this datamodel table. */
    SISENSE_DATAMODEL_TABLE_EXPRESSION("sisenseDatamodelTableExpression"),
    /** Unique name of the parent folder in which this folder exists. */
    SISENSE_FOLDER_PARENT_FOLDER_QUALIFIED_NAME("sisenseFolderParentFolderQualifiedName.text"),
    /** Unique name of the dashboard in which this widget exists. */
    SISENSE_WIDGET_DASHBOARD_QUALIFIED_NAME("sisenseWidgetDashboardQualifiedName.text"),
    /** Unique name of the folder in which this widget exists. */
    SISENSE_WIDGET_FOLDER_QUALIFIED_NAME("sisenseWidgetFolderQualifiedName.text"),
    /** Unique name of the site in which this dashboard exists. */
    SITE_QUALIFIED_NAME("siteQualifiedName"),
    /** Name of the notification channel for this pipe. */
    SNOWFLAKE_PIPE_NOTIFICATION_CHANNEL_NAME("snowflakePipeNotificationChannelName.text"),
    /** Definition of the check in Soda. */
    SODA_CHECK_DEFINITION("sodaCheckDefinition"),
    /** Connection name for the Explore, from Looker. */
    SOURCE_CONNECTION_NAME("sourceConnectionName"),
    /** Deprecated. */
    SOURCE_DEFINITION("sourceDefinition"),
    /** Deprecated. */
    SOURCE_DEFINITION_DATABASE("sourceDefinitionDatabase"),
    /** Deprecated. */
    SOURCE_DEFINITION_SCHEMA("sourceDefinitionSchema"),
    /** Identifier of the dashboard in Salesforce. */
    SOURCE_ID("sourceId"),
    /** Unused. Only the value of connectorType impacts icons. */
    SOURCE_LOGO("sourceLogo"),
    /** List of owners of this asset, in the source system. */
    SOURCE_OWNERS("sourceOwners"),
    /** TBC */
    SOURCE_SERVER_NAME("sourceServerName"),
    /** Name of the Spark app containing this Spark Job For eg. extract_raw_data */
    SPARK_APP_NAME("sparkAppName"),
    /** SQL query that ran to produce the outputs. */
    SQL("sql"),
    /** Name of the SQL table used to declare the Explore. */
    SQL_TABLE_NAME("sqlTableName"),
    /** Staleness of this materialized view. */
    STALENESS("staleness"),
    /** Text for the subtitle for text tiles. */
    SUBTITLE_TEXT("subtitleText"),
    /** Subcategory of this connection. */
    SUB_CATEGORY("subCategory"),
    /** Description markdown of the chart. */
    SUPERSET_CHART_DESCRIPTION_MARKDOWN("supersetChartDescriptionMarkdown"),
    /** Name of the user who changed the dashboard. */
    SUPERSET_DASHBOARD_CHANGED_BY_NAME("supersetDashboardChangedByName"),
    /** URL of the user profile that changed the dashboard */
    SUPERSET_DASHBOARD_CHANGED_BY_URL("supersetDashboardChangedByURL"),
    /** Unique name of the dashboard in which this asset exists. */
    SUPERSET_DASHBOARD_QUALIFIED_NAME("supersetDashboardQualifiedName.text"),
    /** URL for the dashboard thumbnail image in superset. */
    SUPERSET_DASHBOARD_THUMBNAIL_URL("supersetDashboardThumbnailURL"),
    /** Name of the datasource for the dataset. */
    SUPERSET_DATASET_DATASOURCE_NAME("supersetDatasetDatasourceName"),
    /** Unique name of the top-level domain in which this asset exists. */
    SUPER_DOMAIN_QUALIFIED_NAME("superDomainQualifiedName.text"),
    /** All super types of an asset. */
    SUPER_TYPE_NAMES("__superTypeNames"),
    /** Bin size of this field. */
    TABLEAU_DATASOURCE_FIELD_BIN_SIZE("tableauDatasourceFieldBinSize"),
    /** Data category of this field. */
    TABLEAU_DATASOURCE_FIELD_DATA_CATEGORY("tableauDatasourceFieldDataCategory"),
    /** Data type of this field. */
    TABLEAU_DATASOURCE_FIELD_DATA_TYPE("tableauDatasourceFieldDataType.text"),
    /** Formula for this field. */
    TABLEAU_DATASOURCE_FIELD_FORMULA("tableauDatasourceFieldFormula"),
    /** Role of this field, for example: 'dimension', 'measure', or 'unknown'. */
    TABLEAU_DATASOURCE_FIELD_ROLE("tableauDatasourceFieldRole"),
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
    /** TBC */
    TARGET_SERVER_NAME("targetServerName"),
    /** comment for the action executed by user */
    TASK_EXECUTION_COMMENT("taskExecutionComment"),
    /** contains the payload that is proposed to the task */
    TASK_PROPOSALS("taskProposals"),
    /** comment of requestor for the task */
    TASK_REQUESTOR_COMMENT("taskRequestorComment"),
    /** Name of the Atlan workspace in which this asset exists. */
    TENANT_ID("tenantId"),
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
    /** Simple name of the top-level project in which this workbook exists. */
    TOP_LEVEL_PROJECT_NAME("topLevelProjectName"),
    /** Unique name of the top-level project in which this dashboard exists. */
    TOP_LEVEL_PROJECT_QUALIFIED_NAME("topLevelProjectQualifiedName"),
    /** Type of the asset. For example Table, Column, and so on. */
    TYPE_NAME("__typeName"),
    /** TBC */
    UI_PARAMETERS("uiParameters"),
    /** TBC */
    UNIQUE_NAME("uniqueName"),
    /** TBC */
    URLS("urls"),
    /** Unused. Intended usage for the term. */
    USAGE("usage"),
    /** Description of this asset, as provided by a user. If present, this will be used for the description in user interface. */
    USER_DESCRIPTION("userDescription"),
    /** TBC */
    USER_NAME("userName"),
    /** Base64-encoded string of the variables to use in this query. */
    VARIABLES_SCHEMA_BASE64("variablesSchemaBase64"),
    /** Name of the view for the Explore. */
    VIEW_NAME("viewName"),
    /** Base64-encoded string for the visual query builder. */
    VISUAL_BUILDER_SCHEMA_BASE64("visualBuilderSchemaBase64"),
    /** Deprecated. */
    WEB_URL("webUrl"),
    /** Unique name of the workbook in which this dashboard exists. */
    WORKBOOK_QUALIFIED_NAME("workbookQualifiedName"),
    /** Details of the workflow. */
    WORKFLOW_CONFIG("workflowConfig"),
    /** The comment added by the requester */
    WORKFLOW_RUN_COMMENT("workflowRunComment"),
    /** Details of the approval workflow run. */
    WORKFLOW_RUN_CONFIG("workflowRunConfig"),
    /** Time duration after which a run of this workflow will expire. */
    WORKFLOW_RUN_EXPIRES_IN("workflowRunExpiresIn"),
    /** Unique name of the workspace in which this dataset exists. */
    WORKSPACE_QUALIFIED_NAME("workspaceQualifiedName"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    TextFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
