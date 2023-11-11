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
    /** Alias for this table. */
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
    /** Atlan user who created this asset. */
    CREATED_BY("__createdBy"),
    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    DATABASE_NAME("databaseName.keyword"),
    /** Unique name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    DATABASE_QUALIFIED_NAME("databaseQualifiedName"),
    /** Data type of values in this column. */
    DATA_TYPE("dataType"),
    /** Unique name of this asset in dbt. */
    DBT_QUALIFIED_NAME("dbtQualifiedName"),
    /** Unique name of the default database to use for this query. */
    DEFAULT_DATABASE_QUALIFIED_NAME("defaultDatabaseQualifiedName"),
    /** Unique name of the default schema to use for this query. */
    DEFAULT_SCHEMA_QUALIFIED_NAME("defaultSchemaQualifiedName"),
    /** Default value for this column. */
    DEFAULT_VALUE("defaultValue"),
    /** SQL definition of this materialized view. */
    DEFINITION("definition"),
    /** Description of this asset, for example as crawled from a source. Fallback for display purposes, if userDescription is empty. */
    DESCRIPTION("description.keyword"),
    /** Human-readable name of this asset used for display purposes (in user interface). */
    DISPLAY_NAME("displayName.keyword"),
    /** Unused. Exmaples of the term. */
    EXAMPLES("examples"),
    /** External location of this table, for example: an S3 object location. */
    EXTERNAL_LOCATION("externalLocation"),
    /** Format of the external location of this table, for example: JSON, CSV, PARQUET, etc. */
    EXTERNAL_LOCATION_FORMAT("externalLocationFormat"),
    /** Region of the external location of this table, for example: S3 region. */
    EXTERNAL_LOCATION_REGION("externalLocationRegion"),
    /** Glossary in which the asset is contained, searchable by the qualifiedName of the glossary. */
    GLOSSARY("__glossary"),
    /** Rough measure of the IOPS allocated to the table's processing. */
    GUACAMOLE_TEMPERATURE("guacamoleTemperature"),
    /** Globally unique identifier (GUID) of any object in Atlan. */
    GUID("__guid"),
    /** Assets that are inputs to this process. */
    INPUTS("inputs"),
    /** Name of the last run of the crawler that last synchronized this asset. */
    LAST_SYNC_RUN("lastSyncRun"),
    /** Name of the crawler that last synchronized this asset. */
    LAST_SYNC_WORKFLOW_NAME("lastSyncWorkflowName"),
    /** Unused. Detailed definition of the term. See 'readme' instead. */
    LONG_DESCRIPTION("longDescription"),
    /** Raw SQL query string. */
    LONG_RAW_QUERY("longRawQuery"),
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
    /** List of partitions in this table. */
    PARTITION_LIST("partitionList"),
    /** Partition strategy for this table. */
    PARTITION_STRATEGY("partitionStrategy"),
    /** User who pinned this column. */
    PINNED_BY("pinnedBy"),
    /** All propagated Atlan tags that exist on an asset, searchable by the internal hashed-string ID of the Atlan tag. */
    PROPAGATED_TRAIT_NAMES("__propagatedTraitNames"),
    /** Unique fully-qualified name of the asset in Atlan. */
    QUALIFIED_NAME("qualifiedName"),
    /** Configuration for preview queries. */
    QUERY_PREVIEW_CONFIG("queryPreviewConfig"),
    /** Map of unique users who have queried this asset to the number of times they have queried it. */
    QUERY_USER_MAP("queryUserMap"),
    /** TBC */
    RAW_DATA_TYPE_DEFINITION("rawDataTypeDefinition"),
    /** Deprecated. See 'longRawQuery' instead. */
    RAW_QUERY("rawQuery"),
    /** Refresh method for this materialized view. */
    REFRESH_METHOD("refreshMethod"),
    /** Refresh mode for this materialized view. */
    REFRESH_MODE("refreshMode"),
    /** Unused. List of servers where this entity is replicated from. */
    REPLICATED_FROM("replicatedFrom"),
    /** Unused. List of servers where this entity is replicated to. */
    REPLICATED_TO("replicatedTo"),
    /** URL for sample data for this asset. */
    SAMPLE_DATA_URL("sampleDataUrl"),
    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    SCHEMA_NAME("schemaName.keyword"),
    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    SCHEMA_QUALIFIED_NAME("schemaQualifiedName"),
    /** Unused. Brief summary of the term. See 'description' and 'userDescription' instead. */
    SHORT_DESCRIPTION("shortDescription"),
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
