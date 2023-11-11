/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.probable.guacamole.model.enums;

import com.atlan.model.enums.AtlanSearchableField;
import javax.annotation.processing.Generated;
import lombok.Getter;

/**
 * Enumeration of all index fields.
 * @deprecated these are now enumerated as constants in the various asset interfaces, as instances of one of the following: {@link com.atlan.model.fields.BooleanField}, {@link com.atlan.model.fields.KeywordField}, {@link com.atlan.model.fields.KeywordTextField}, {@link com.atlan.model.fields.KeywordTextStemmedField}, {@link com.atlan.model.fields.NumericField}, {@link com.atlan.model.fields.NumericRankField}, {@link com.atlan.model.fields.RelationField}, or {@link com.atlan.model.fields.TextField}
 */
@Deprecated
@Generated(value = "com.probable.guacamole.generators.POJOGenerator")
public enum KeywordFields implements AtlanSearchableField {
    /** Unused. Abbreviation of the term. */
    ABBREVIATION("abbreviation"),
    /** Unused. Arbitrary set of additional attributes for the terrm. */
    ADDITIONAL_ATTRIBUTES("additionalAttributes"),
    /** List of groups who administer this asset. (This is only used for certain asset types.) */
    ADMIN_GROUPS("adminGroups"),
    /** List of roles who administer this asset. (This is only used for Connection assets.) */
    ADMIN_ROLES("adminRoles"),
    /** List of users who administer this asset. (This is only used for certain asset types.) */
    ADMIN_USERS("adminUsers"),
    /** TBC */
    AIRFLOW_DAG_NAME("airflowDagName.keyword"),
    /** TBC */
    AIRFLOW_DAG_QUALIFIED_NAME("airflowDagQualifiedName"),
    /** Name of the run */
    AIRFLOW_RUN_NAME("airflowRunName"),
    /** OpenLineage state of the run */
    AIRFLOW_RUN_OPEN_LINEAGE_STATE("airflowRunOpenLineageState"),
    /** OpenLineage Version of the run */
    AIRFLOW_RUN_OPEN_LINEAGE_VERSION("airflowRunOpenLineageVersion"),
    /** Type of the run */
    AIRFLOW_RUN_TYPE("airflowRunType"),
    /** Airflow Version of the run */
    AIRFLOW_RUN_VERSION("airflowRunVersion"),
    /** TBC */
    AIRFLOW_TAGS("airflowTags"),
    /** TBC */
    AIRFLOW_TASK_CONNECTION_ID("airflowTaskConnectionId.keyword"),
    /** TBC */
    AIRFLOW_TASK_OPERATOR_CLASS("airflowTaskOperatorClass.keyword"),
    /** Pool on which this run happened */
    AIRFLOW_TASK_POOL("airflowTaskPool"),
    /** Queue on which this run happened */
    AIRFLOW_TASK_QUEUE("airflowTaskQueue"),
    /** TBC */
    AIRFLOW_TASK_SQL("airflowTaskSql"),
    /** Trigger rule of the run */
    AIRFLOW_TASK_TRIGGER_RULE("airflowTaskTriggerRule"),
    /** Alias for this materialized view. */
    ALIAS("alias"),
    /** Detailed message to include in the announcement on this asset. */
    ANNOUNCEMENT_MESSAGE("announcementMessage"),
    /** Brief title for the announcement on this asset. Required when announcementType is specified. */
    ANNOUNCEMENT_TITLE("announcementTitle"),
    /** Type of announcement on this asset. */
    ANNOUNCEMENT_TYPE("announcementType"),
    /** Name of the user who last updated the announcement. */
    ANNOUNCEMENT_UPDATED_BY("announcementUpdatedBy"),
    /** Name of the account in which this asset exists in dbt. */
    ASSET_DBT_ACCOUNT_NAME("assetDbtAccountName.keyword"),
    /** Alias of this asset in dbt. */
    ASSET_DBT_ALIAS("assetDbtAlias.keyword"),
    /** Version of the environment in which this asset is materialized in dbt. */
    ASSET_DBT_ENVIRONMENT_DBT_VERSION("assetDbtEnvironmentDbtVersion"),
    /** Name of the environment in which this asset is materialized in dbt. */
    ASSET_DBT_ENVIRONMENT_NAME("assetDbtEnvironmentName.keyword"),
    /** Path in S3 to the artifacts saved from the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_ARTIFACT_S3PATH("assetDbtJobLastRunArtifactS3Path"),
    /** Thread ID of the user who executed the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_EXECUTED_BY_THREAD_ID("assetDbtJobLastRunExecutedByThreadId"),
    /** Branch in git from which the last run of the job that materialized this asset in dbt ran. */
    ASSET_DBT_JOB_LAST_RUN_GIT_BRANCH("assetDbtJobLastRunGitBranch"),
    /** SHA hash in git for the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_GIT_SHA("assetDbtJobLastRunGitSha"),
    /** Thread ID of the owner of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_OWNER_THREAD_ID("assetDbtJobLastRunOwnerThreadId"),
    /** Total duration the job that materialized this asset in dbt spent being queued. */
    ASSET_DBT_JOB_LAST_RUN_QUEUED_DURATION("assetDbtJobLastRunQueuedDuration"),
    /** Human-readable total duration of the last run of the job that materialized this asset in dbt spend being queued. */
    ASSET_DBT_JOB_LAST_RUN_QUEUED_DURATION_HUMANIZED("assetDbtJobLastRunQueuedDurationHumanized"),
    /** Run duration of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_RUN_DURATION("assetDbtJobLastRunRunDuration"),
    /** Human-readable run duration of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_RUN_DURATION_HUMANIZED("assetDbtJobLastRunRunDurationHumanized"),
    /** Status message of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_STATUS_MESSAGE("assetDbtJobLastRunStatusMessage.keyword"),
    /** Total duration of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_TOTAL_DURATION("assetDbtJobLastRunTotalDuration"),
    /** Human-readable total duration of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_TOTAL_DURATION_HUMANIZED("assetDbtJobLastRunTotalDurationHumanized"),
    /** URL of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_URL("assetDbtJobLastRunUrl"),
    /** Name of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_NAME("assetDbtJobName.keyword"),
    /** Human-readable time when the next run of the job that materializes this asset in dbt is scheduled. */
    ASSET_DBT_JOB_NEXT_RUN_HUMANIZED("assetDbtJobNextRunHumanized.keyword"),
    /** Schedule of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_SCHEDULE("assetDbtJobSchedule"),
    /** Status of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_STATUS("assetDbtJobStatus"),
    /** Metadata for this asset in dbt, specifically everything under the 'meta' key in the dbt object. */
    ASSET_DBT_META("assetDbtMeta"),
    /** Name of the package in which this asset exists in dbt. */
    ASSET_DBT_PACKAGE_NAME("assetDbtPackageName.keyword"),
    /** Name of the project in which this asset exists in dbt. */
    ASSET_DBT_PROJECT_NAME("assetDbtProjectName.keyword"),
    /** URL of the semantic layer proxy for this asset in dbt. */
    ASSET_DBT_SEMANTIC_LAYER_PROXY_URL("assetDbtSemanticLayerProxyUrl"),
    /** Freshness criteria for the source of this asset in dbt. */
    ASSET_DBT_SOURCE_FRESHNESS_CRITERIA("assetDbtSourceFreshnessCriteria"),
    /** List of tags attached to this asset in dbt. */
    ASSET_DBT_TAGS("assetDbtTags"),
    /** All associated dbt test statuses. */
    ASSET_DBT_TEST_STATUS("assetDbtTestStatus"),
    /** Unique identifier of this asset in dbt. */
    ASSET_DBT_UNIQUE_ID("assetDbtUniqueId.keyword"),
    /** Name of the icon to use for this asset. (Only applies to glossaries, currently.) */
    ASSET_ICON("assetIcon"),
    /** List of Monte Carlo incident names attached to this asset. */
    ASSET_MC_INCIDENT_NAMES("assetMcIncidentNames.keyword"),
    /** List of unique Monte Carlo incident names attached to this asset. */
    ASSET_MC_INCIDENT_QUALIFIED_NAMES("assetMcIncidentQualifiedNames"),
    /** List of Monte Carlo incident severities associated with this asset. */
    ASSET_MC_INCIDENT_SEVERITIES("assetMcIncidentSeverities"),
    /** List of Monte Carlo incident states associated with this asset. */
    ASSET_MC_INCIDENT_STATES("assetMcIncidentStates"),
    /** List of Monte Carlo incident sub-types associated with this asset. */
    ASSET_MC_INCIDENT_SUB_TYPES("assetMcIncidentSubTypes"),
    /** List of Monte Carlo incident types associated with this asset. */
    ASSET_MC_INCIDENT_TYPES("assetMcIncidentTypes"),
    /** List of Monte Carlo monitor names attached to this asset. */
    ASSET_MC_MONITOR_NAMES("assetMcMonitorNames.keyword"),
    /** List of unique Monte Carlo monitor names attached to this asset. */
    ASSET_MC_MONITOR_QUALIFIED_NAMES("assetMcMonitorQualifiedNames"),
    /** Schedules of all associated Monte Carlo monitors. */
    ASSET_MC_MONITOR_SCHEDULE_TYPES("assetMcMonitorScheduleTypes"),
    /** Statuses of all associated Monte Carlo monitors. */
    ASSET_MC_MONITOR_STATUSES("assetMcMonitorStatuses"),
    /** Types of all associated Monte Carlo monitors. */
    ASSET_MC_MONITOR_TYPES("assetMcMonitorTypes"),
    /** All associated Soda check statuses. */
    ASSET_SODA_CHECK_STATUSES("assetSodaCheckStatuses"),
    /** Status of data quality from Soda. */
    ASSET_SODA_DQ_STATUS("assetSodaDQStatus"),
    /** TBC */
    ASSET_SODA_SOURCE_URL("assetSodaSourceURL"),
    /** List of tags attached to this asset. */
    ASSET_TAGS("assetTags"),
    /** All terms attached to an asset, searchable by the term's qualifiedName. */
    ASSIGNED_TERMS("__meanings"),
    /** Parsed AST of the code or SQL statements that describe the logic of this process. */
    AST("ast"),
    /** Categories in which the term is organized, searchable by the qualifiedName of the category. */
    CATEGORIES("__categories"),
    /** Status of this asset's certification. */
    CERTIFICATE_STATUS("certificateStatus"),
    /** Human-readable descriptive message used to provide further detail to certificateStatus. */
    CERTIFICATE_STATUS_MESSAGE("certificateStatusMessage"),
    /** Name of the user who last updated the certification of this asset. */
    CERTIFICATE_UPDATED_BY("certificateUpdatedBy"),
    /** Code that ran within the process. */
    CODE("code"),
    /** Unique name of the collection in which this query exists. */
    COLLECTION_QUALIFIED_NAME("collectionQualifiedName"),
    /** List of values in a histogram that represents the contents of this column. */
    COLUMN_HISTOGRAM("columnHistogram"),
    /** List of the greatest values in a column. */
    COLUMN_MAXS("columnMaxs"),
    /** List of the least values in a column. */
    COLUMN_MINS("columnMins"),
    /** List of top values in this column. */
    COLUMN_TOP_VALUES("columnTopValues"),
    /** Simple name of the connection through which this asset is accessible. */
    CONNECTION_NAME("connectionName"),
    /** Unique name of the connection through which this asset is accessible. */
    CONNECTION_QUALIFIED_NAME("connectionQualifiedName"),
    /** Type of the connector through which this asset is accessible. */
    CONNECTOR_TYPE("connectorName"),
    /** Constraint that defines this table partition. */
    CONSTRAINT("constraint"),
    /** Atlan user who created this asset. */
    CREATED_BY("__createdBy"),
    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    DATABASE_NAME("databaseName.keyword"),
    /** Unique name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    DATABASE_QUALIFIED_NAME("databaseQualifiedName"),
    /** Data type of values in this column. */
    DATA_TYPE("dataType"),
    /** TBC */
    DBT_ACCOUNT_NAME("dbtAccountName.keyword"),
    /** TBC */
    DBT_ALIAS("dbtAlias.keyword"),
    /** TBC */
    DBT_COMPILED_SQL("dbtCompiledSQL"),
    /** TBC */
    DBT_CONNECTION_CONTEXT("dbtConnectionContext"),
    /** TBC */
    DBT_ENVIRONMENT_DBT_VERSION("dbtEnvironmentDbtVersion.keyword"),
    /** TBC */
    DBT_ENVIRONMENT_NAME("dbtEnvironmentName.keyword"),
    /** TBC */
    DBT_ERROR("dbtError"),
    /** TBC */
    DBT_FRESHNESS_CRITERIA("dbtFreshnessCriteria"),
    /** TBC */
    DBT_JOB_NAME("dbtJobName.keyword"),
    /** TBC */
    DBT_JOB_NEXT_RUN_HUMANIZED("dbtJobNextRunHumanized.keyword"),
    /** TBC */
    DBT_JOB_SCHEDULE("dbtJobSchedule"),
    /** TBC */
    DBT_JOB_SCHEDULE_CRON_HUMANIZED("dbtJobScheduleCronHumanized.keyword"),
    /** TBC */
    DBT_JOB_STATUS("dbtJobStatus"),
    /** TBC */
    DBT_MATERIALIZATION_TYPE("dbtMaterializationType"),
    /** TBC */
    DBT_META("dbtMeta"),
    /** TBC */
    DBT_METRIC_FILTERS("dbtMetricFilters"),
    /** TBC */
    DBT_MODEL_COLUMN_DATA_TYPE("dbtModelColumnDataType"),
    /** TBC */
    DBT_MODEL_QUALIFIED_NAME("dbtModelQualifiedName"),
    /** TBC */
    DBT_PACKAGE_NAME("dbtPackageName.keyword"),
    /** TBC */
    DBT_PROJECT_NAME("dbtProjectName.keyword"),
    /** Unique name of this asset in dbt. */
    DBT_QUALIFIED_NAME("dbtQualifiedName"),
    /** TBC */
    DBT_RAW_SQL("dbtRawSQL"),
    /** TBC */
    DBT_SEMANTIC_LAYER_PROXY_URL("dbtSemanticLayerProxyUrl"),
    /** TBC */
    DBT_STATE("dbtState"),
    /** TBC */
    DBT_STATS("dbtStats"),
    /** TBC */
    DBT_STATUS("dbtStatus"),
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
    /** Test results. Can be one of, in order of severity, "error", "fail", "warn", "pass". */
    DBT_TEST_STATE("dbtTestState"),
    /** Details of the results of the test. For errors, it reads "ERROR". */
    DBT_TEST_STATUS("dbtTestStatus"),
    /** TBC */
    DBT_UNIQUE_ID("dbtUniqueId.keyword"),
    /** Unique name of the default database to use for this query. */
    DEFAULT_DATABASE_QUALIFIED_NAME("defaultDatabaseQualifiedName"),
    /** Unique name of the default schema to use for this query. */
    DEFAULT_SCHEMA_QUALIFIED_NAME("defaultSchemaQualifiedName"),
    /** Default value for this column. */
    DEFAULT_VALUE("defaultValue"),
    /** SQL statements used to define the dynamic table. */
    DEFINITION("definition"),
    /** Description of this asset, for example as crawled from a source. Fallback for display purposes, if userDescription is empty. */
    DESCRIPTION("description.keyword"),
    /** Human-readable name of this asset used for display purposes (in user interface). */
    DISPLAY_NAME("displayName.keyword"),
    /** Unused. Exmaples of the term. */
    EXAMPLES("examples"),
    /** External location of this partition, for example: an S3 object location. */
    EXTERNAL_LOCATION("externalLocation"),
    /** Format of the external location of this partition, for example: JSON, CSV, PARQUET, etc. */
    EXTERNAL_LOCATION_FORMAT("externalLocationFormat"),
    /** Region of the external location of this partition, for example: S3 region. */
    EXTERNAL_LOCATION_REGION("externalLocationRegion"),
    /** URL giving the online location where the file can be accessed. */
    FILE_PATH("filePath"),
    /** Type (extension) of the file. */
    FILE_TYPE("fileType"),
    /** Glossary in which the asset is contained, searchable by the qualifiedName of the glossary. */
    GLOSSARY("__glossary"),
    /** Rough measure of the IOPS allocated to the table's processing. */
    GUACAMOLE_TEMPERATURE("guacamoleTemperature"),
    /** Globally unique identifier (GUID) of any object in Atlan. */
    GUID("__guid"),
    /** Icon for the link. */
    ICON("icon"),
    /** Type of icon for the link, for example: image or emoji. */
    ICON_TYPE("iconType"),
    /** Assets that are inputs to this process. */
    INPUTS("inputs"),
    /** Name of the last run of the crawler that last synchronized this asset. */
    LAST_SYNC_RUN("lastSyncRun"),
    /** Name of the crawler that last synchronized this asset. */
    LAST_SYNC_WORKFLOW_NAME("lastSyncWorkflowName"),
    /** URL to the resource. */
    LINK("link"),
    /** Unused. Detailed definition of the term. See 'readme' instead. */
    LONG_DESCRIPTION("longDescription"),
    /** Raw SQL query string. */
    LONG_RAW_QUERY("longRawQuery"),
    /** List of unique names of assets that are part of this Monte Carlo asset. */
    MC_ASSET_QUALIFIED_NAMES("mcAssetQualifiedNames"),
    /** Identifier of this incident, from Monte Carlo. */
    MC_INCIDENT_ID("mcIncidentId"),
    /** Severity of this incident. */
    MC_INCIDENT_SEVERITY("mcIncidentSeverity"),
    /** State of this incident. */
    MC_INCIDENT_STATE("mcIncidentState"),
    /** Subtypes of this incident. */
    MC_INCIDENT_SUB_TYPES("mcIncidentSubTypes"),
    /** Type of this incident. */
    MC_INCIDENT_TYPE("mcIncidentType"),
    /** Name of this incident's warehouse. */
    MC_INCIDENT_WAREHOUSE("mcIncidentWarehouse"),
    /** List of labels for this Monte Carlo asset. */
    MC_LABELS("mcLabels"),
    /** Unique identifier for this monitor, from Monte Carlo. */
    MC_MONITOR_ID("mcMonitorId"),
    /** Namespace of this monitor. */
    MC_MONITOR_NAMESPACE("mcMonitorNamespace.keyword"),
    /** Comparison logic used for the rule. */
    MC_MONITOR_RULE_COMPARISONS("mcMonitorRuleComparisons"),
    /** SQL code for custom SQL rules. */
    MC_MONITOR_RULE_CUSTOM_SQL("mcMonitorRuleCustomSql"),
    /** Schedule details for the rule. */
    MC_MONITOR_RULE_SCHEDULE_CONFIG("mcMonitorRuleScheduleConfig"),
    /** Type of rule for this monitor. */
    MC_MONITOR_RULE_TYPE("mcMonitorRuleType"),
    /** Type of schedule for this monitor, for example: fixed or dynamic. */
    MC_MONITOR_SCHEDULE_TYPE("mcMonitorScheduleType"),
    /** Status of this monitor. */
    MC_MONITOR_STATUS("mcMonitorStatus"),
    /** Type of this monitor, for example: field health (stats) or dimension tracking (categories). */
    MC_MONITOR_TYPE("mcMonitorType"),
    /** Name of the warehouse for this monitor. */
    MC_MONITOR_WAREHOUSE("mcMonitorWarehouse"),
    /** SQL query used to compute the metric. */
    METRIC_SQL("metricSQL"),
    /** Type of the metric. */
    METRIC_TYPE("metricType"),
    /** Atlan user who last updated the asset. */
    MODIFIED_BY("__modifiedBy"),
    /** Name of this asset. Fallback for display purposes, if displayName is empty. */
    NAME("name.keyword"),
    /** Assets that are outputs from this process. */
    OUTPUTS("outputs"),
    /** List of groups who own this asset. */
    OWNER_GROUPS("ownerGroups"),
    /** List of users who own this asset. */
    OWNER_USERS("ownerUsers"),
    /** Parent category in which a subcategory is contained, searchable by the qualifiedName of the category. */
    PARENT_CATEGORY("__parentCategory"),
    /** Simple name of the column this column is nested within, for STRUCT and NESTED columns. */
    PARENT_COLUMN_NAME("parentColumnName.keyword"),
    /** Unique name of the column this column is nested within, for STRUCT and NESTED columns. */
    PARENT_COLUMN_QUALIFIED_NAME("parentColumnQualifiedName"),
    /** Unique name of the parent collection or folder in which this query exists. */
    PARENT_QUALIFIED_NAME("parentQualifiedName"),
    /** List of sub-partitions in this partition. */
    PARTITION_LIST("partitionList"),
    /** Partition strategy of this partition. */
    PARTITION_STRATEGY("partitionStrategy"),
    /** User who pinned this column. */
    PINNED_BY("pinnedBy"),
    /** All propagated Atlan tags that exist on an asset, searchable by the internal hashed-string ID of the Atlan tag. */
    PROPAGATED_TRAIT_NAMES("__propagatedTraitNames"),
    /** Unique fully-qualified name of the asset in Atlan. */
    QUALIFIED_NAME("qualifiedName"),
    /** Configuration for the query preview of this materialized view. */
    QUERY_PREVIEW_CONFIG("queryPreviewConfig"),
    /** Map of unique users who have queried this asset to the number of times they have queried it. */
    QUERY_USER_MAP("queryUserMap"),
    /** TBC */
    RAW_DATA_TYPE_DEFINITION("rawDataTypeDefinition"),
    /** Deprecated. See 'longRawQuery' instead. */
    RAW_QUERY("rawQuery"),
    /** Reference to the resource. */
    REFERENCE("reference"),
    /** Refresh method for this materialized view. */
    REFRESH_METHOD("refreshMethod"),
    /** Refresh mode for this materialized view. */
    REFRESH_MODE("refreshMode"),
    /** Unused. List of servers where this entity is replicated from. */
    REPLICATED_FROM("replicatedFrom"),
    /** Unused. List of servers where this entity is replicated to. */
    REPLICATED_TO("replicatedTo"),
    /** Metadata of the resource. */
    RESOURCE_METADATA("resourceMetadata"),
    /** URL for sample data for this asset. */
    SAMPLE_DATA_URL("sampleDataUrl"),
    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    SCHEMA_NAME("schemaName.keyword"),
    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    SCHEMA_QUALIFIED_NAME("schemaQualifiedName"),
    /** Unique identifier for schema definition set by the schema registry. */
    SCHEMA_REGISTRY_SCHEMA_ID("schemaRegistrySchemaId"),
    /** Type of language or specification used to define the schema, for example: JSON, Protobuf, etc. */
    SCHEMA_REGISTRY_SCHEMA_TYPE("schemaRegistrySchemaType"),
    /** Base name of the subject, without -key, -value prefixes. */
    SCHEMA_REGISTRY_SUBJECT_BASE_NAME("schemaRegistrySubjectBaseName"),
    /** List of asset qualified names that this subject is governing/validating. */
    SCHEMA_REGISTRY_SUBJECT_GOVERNING_ASSET_QUALIFIED_NAMES("schemaRegistrySubjectGoverningAssetQualifiedNames"),
    /** Latest schema version of the subject. */
    SCHEMA_REGISTRY_SUBJECT_LATEST_SCHEMA_VERSION("schemaRegistrySubjectLatestSchemaVersion"),
    /** Compatibility of the schema across versions. */
    SCHEMA_REGISTRY_SUBJECT_SCHEMA_COMPATIBILITY("schemaRegistrySubjectSchemaCompatibility"),
    /** Unused. Brief summary of the term. See 'description' and 'userDescription' instead. */
    SHORT_DESCRIPTION("shortDescription"),
    /** Definition of the check in Soda. */
    SODA_CHECK_DEFINITION("sodaCheckDefinition"),
    /** Status of the check in Soda. */
    SODA_CHECK_EVALUATION_STATUS("sodaCheckEvaluationStatus"),
    /** Identifier of the check in Soda. */
    SODA_CHECK_ID("sodaCheckId"),
    /** The unit of measure for sourceTotalCost. */
    SOURCE_COST_UNIT("sourceCostUnit"),
    /** Name of the user who created this asset, in the source system. */
    SOURCE_CREATED_BY("sourceCreatedBy"),
    /** URL to create an embed for a resource (for example, an image of a dashboard) within Atlan. */
    SOURCE_EMBED_URL("sourceEmbedURL"),
    /** List of owners of this asset, in the source system. */
    SOURCE_OWNERS("sourceOwners"),
    /** List of most expensive warehouse names. */
    SOURCE_QUERY_COMPUTE_COSTS("sourceQueryComputeCostList"),
    /** List of most expensive warehouses with extra insights. */
    SOURCE_QUERY_COMPUTE_COST_RECORDS("sourceQueryComputeCostRecordList"),
    /** List of the most expensive queries that accessed this asset. */
    SOURCE_READ_EXPENSIVE_QUERY_RECORDS("sourceReadExpensiveQueryRecordList"),
    /** List of the most popular queries that accessed this asset. */
    SOURCE_READ_POPULAR_QUERY_RECORDS("sourceReadPopularQueryRecordList"),
    /** List of usernames of the most recent users who read this asset. */
    SOURCE_READ_RECENT_USERS("sourceReadRecentUserList"),
    /** List of usernames with extra insights for the most recent users who read this asset. */
    SOURCE_READ_RECENT_USER_RECORDS("sourceReadRecentUserRecordList"),
    /** List of the slowest queries that accessed this asset. */
    SOURCE_READ_SLOW_QUERY_RECORDS("sourceReadSlowQueryRecordList"),
    /** List of usernames of the users who read this asset the most. */
    SOURCE_READ_TOP_USERS("sourceReadTopUserList"),
    /** List of usernames with extra insights for the users who read this asset the most. */
    SOURCE_READ_TOP_USER_RECORDS("sourceReadTopUserRecordList"),
    /** Name of the user who last updated this asset, in the source system. */
    SOURCE_UPDATED_BY("sourceUpdatedBy"),
    /** URL to the resource within the source application, used to create a button to view this asset in the source application. */
    SOURCE_URL("sourceURL"),
    /** SQL query that ran to produce the outputs. */
    SQL("sql"),
    /** Staleness of this materialized view. */
    STALENESS("staleness"),
    /** Users who have starred this asset. */
    STARRED_BY("starredBy"),
    /** List of usernames with extra information of the users who have starred an asset. */
    STARRED_DETAILS("starredDetailsList"),
    /** Asset status in Atlan (active vs deleted). */
    STATE("__state"),
    /** Sub-data type of this column. */
    SUB_DATA_TYPE("subDataType"),
    /** Subtype of this asset. */
    SUB_TYPE("subType"),
    /** All super types of an asset. */
    SUPER_TYPE_NAMES("__superTypeNames.keyword"),
    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    TABLE_NAME("tableName.keyword"),
    /** Unique name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    TABLE_QUALIFIED_NAME("tableQualifiedName"),
    /** Name of the Atlan workspace in which this asset exists. */
    TENANT_ID("tenantId"),
    /** All directly-assigned Atlan tags that exist on an asset, searchable by the internal hashed-string ID of the Atlan tag. */
    TRAIT_NAMES("__traitNames"),
    /** Type of the asset. For example Table, Column, and so on. */
    TYPE_NAME("__typeName.keyword"),
    /** Unused. Intended usage for the term. */
    USAGE("usage"),
    /** Description of this asset, as provided by a user. If present, this will be used for the description in user interface. */
    USER_DESCRIPTION("userDescription.keyword"),
    /** Validations for this column. */
    VALIDATIONS("validations"),
    /** Base64-encoded string of the variables to use in this query. */
    VARIABLES_SCHEMA_BASE64("variablesSchemaBase64"),
    /** List of groups who can view assets contained in a collection. (This is only used for certain asset types.) */
    VIEWER_GROUPS("viewerGroups"),
    /** List of users who can view assets contained in a collection. (This is only used for certain asset types.) */
    VIEWER_USERS("viewerUsers"),
    /** Simple name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    VIEW_NAME("viewName.keyword"),
    /** Unique name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    VIEW_QUALIFIED_NAME("viewQualifiedName"),
    /** Base64-encoded string for the visual query builder. */
    VISUAL_BUILDER_SCHEMA_BASE64("visualBuilderSchemaBase64"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    KeywordFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
