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
public enum NumericFields implements AtlanSearchableField {
    /** Time (epoch) when the acccess tier for this object was last modified, in milliseconds. */
    ADLS_OBJECT_ACCESS_TIER_LAST_MODIFIED_TIME("adlsObjectAccessTierLastModifiedTime"),
    /** Number of objects that exist within this container. */
    ADLS_OBJECT_COUNT("adlsObjectCount"),
    /** Size of this object. */
    ADLS_OBJECT_SIZE("adlsObjectSize"),
    /** Duration between scheduled runs in seconds */
    AIRFLOW_DAG_SCHEDULE_DELTA("airflowDagScheduleDelta"),
    /** End time of the run */
    AIRFLOW_RUN_END_TIME("airflowRunEndTime"),
    /** Start time of the run */
    AIRFLOW_RUN_START_TIME("airflowRunStartTime"),
    /** Pool slots used for the run */
    AIRFLOW_TASK_POOL_SLOTS("airflowTaskPoolSlots"),
    /** Priority weight of the run */
    AIRFLOW_TASK_PRIORITY_WEIGHT("airflowTaskPriorityWeight"),
    /** Retry required for the run */
    AIRFLOW_TASK_RETRY_NUMBER("airflowTaskRetryNumber"),
    /** Time (epoch) at which the announcement was last updated, in milliseconds. */
    ANNOUNCEMENT_UPDATED_AT("announcementUpdatedAt"),
    /** Time (epoch) at which the job that materialized this asset in dbt last ran, in milliseconds. */
    ASSET_DBT_JOB_LAST_RUN("assetDbtJobLastRun"),
    /** Time (epoch) at which the job that materialized this asset in dbt was last created, in milliseconds. */
    ASSET_DBT_JOB_LAST_RUN_CREATED_AT("assetDbtJobLastRunCreatedAt"),
    /** Time (epoch) at which the job that materialized this asset in dbt was dequeued, in milliseconds. */
    ASSET_DBT_JOB_LAST_RUN_DEQUED_AT("assetDbtJobLastRunDequedAt"),
    /** Time (epoch) at which the job that materialized this asset in dbt was started running, in milliseconds. */
    ASSET_DBT_JOB_LAST_RUN_STARTED_AT("assetDbtJobLastRunStartedAt"),
    /** Time (epoch) at which the job that materialized this asset in dbt was last updated, in milliseconds. */
    ASSET_DBT_JOB_LAST_RUN_UPDATED_AT("assetDbtJobLastRunUpdatedAt"),
    /** Time (epoch) when the next run of the job that materializes this asset in dbt is scheduled. */
    ASSET_DBT_JOB_NEXT_RUN("assetDbtJobNextRun"),
    /** Time (epoch) at which this asset was last synced from Monte Carlo. */
    ASSET_MC_LAST_SYNC_RUN_AT("assetMcLastSyncRunAt"),
    /** Number of checks done via Soda. */
    ASSET_SODA_CHECK_COUNT("assetSodaCheckCount"),
    /** TBC */
    ASSET_SODA_LAST_SCAN_AT("assetSodaLastScanAt"),
    /** TBC */
    ASSET_SODA_LAST_SYNC_RUN_AT("assetSodaLastSyncRunAt"),
    /** TBC */
    AUTH_SERVICE_POLICY_LAST_SYNC("authServicePolicyLastSync"),
    /** Time (epoch) at which the certification was last updated, in milliseconds. */
    CERTIFICATE_UPDATED_AT("certificateUpdatedAt"),
    /** Average value in this column. */
    COLUMN_AVERAGE("columnAverage"),
    /** Average length of values in a string column. */
    COLUMN_AVERAGE_LENGTH("columnAverageLength"),
    /** Number of columns in this materialized view. */
    COLUMN_COUNT("columnCount"),
    /** Level of nesting of this column, used for STRUCT and NESTED columns. */
    COLUMN_DEPTH_LEVEL("columnDepthLevel"),
    /** Number of rows that contain distinct values. */
    COLUMN_DISTINCT_VALUES_COUNT("columnDistinctValuesCount"),
    /** Number of rows that contain distinct values. */
    COLUMN_DISTINCT_VALUES_COUNT_LONG("columnDistinctValuesCountLong"),
    /** Number of rows that contain duplicate values. */
    COLUMN_DUPLICATE_VALUES_COUNT("columnDuplicateValuesCount"),
    /** Number of rows that contain duplicate values. */
    COLUMN_DUPLICATE_VALUES_COUNT_LONG("columnDuplicateValuesCountLong"),
    /** Greatest value in a numeric column. */
    COLUMN_MAX("columnMax"),
    /** Length of the longest value in a string column. */
    COLUMN_MAXIMUM_STRING_LENGTH("columnMaximumStringLength"),
    /** Arithmetic mean of the values in a numeric column. */
    COLUMN_MEAN("columnMean"),
    /** Calculated median of the values in a numeric column. */
    COLUMN_MEDIAN("columnMedian"),
    /** Least value in a numeric column. */
    COLUMN_MIN("columnMin"),
    /** Length of the shortest value in a string column. */
    COLUMN_MINIMUM_STRING_LENGTH("columnMinimumStringLength"),
    /** Number of rows in a column that do not contain content. */
    COLUMN_MISSING_VALUES_COUNT("columnMissingValuesCount"),
    /** Number of rows in a column that do not contain content. */
    COLUMN_MISSING_VALUES_COUNT_LONG("columnMissingValuesCountLong"),
    /** Percentage of rows in a column that do not contain content. */
    COLUMN_MISSING_VALUES_PERCENTAGE("columnMissingValuesPercentage"),
    /** Calculated standard deviation of the values in a numeric column. */
    COLUMN_STANDARD_DEVIATION("columnStandardDeviation"),
    /** Calculated sum of the values in a numeric column. */
    COLUMN_SUM("columnSum"),
    /** Ratio indicating how unique data in this column is: 0 indicates that all values are the same, 100 indicates that all values in this column are unique. */
    COLUMN_UNIQUENESS_PERCENTAGE("columnUniquenessPercentage"),
    /** Number of rows in which a value in this column appears only once. */
    COLUMN_UNIQUE_VALUES_COUNT("columnUniqueValuesCount"),
    /** Number of rows in which a value in this column appears only once. */
    COLUMN_UNIQUE_VALUES_COUNT_LONG("columnUniqueValuesCountLong"),
    /** Calculated variance of the values in a numeric column. */
    COLUMN_VARIANCE("columnVariance"),
    /** Number of dashboards in this workspace. */
    DASHBOARD_COUNT("dashboardCount"),
    /** Number of dataflows in this workspace. */
    DATAFLOW_COUNT("dataflowCount"),
    /** Number of datasets in this workspace. */
    DATASET_COUNT("datasetCount"),
    /** TBC */
    DBT_JOB_LAST_RUN("dbtJobLastRun"),
    /** TBC */
    DBT_JOB_NEXT_RUN("dbtJobNextRun"),
    /** TBC */
    DBT_MODEL_COLUMN_ORDER("dbtModelColumnOrder"),
    /** TBC */
    DBT_MODEL_COMPILE_COMPLETED_AT("dbtModelCompileCompletedAt"),
    /** TBC */
    DBT_MODEL_COMPILE_STARTED_AT("dbtModelCompileStartedAt"),
    /** TBC */
    DBT_MODEL_EXECUTE_COMPLETED_AT("dbtModelExecuteCompletedAt"),
    /** TBC */
    DBT_MODEL_EXECUTE_STARTED_AT("dbtModelExecuteStartedAt"),
    /** TBC */
    DBT_MODEL_EXECUTION_TIME("dbtModelExecutionTime"),
    /** TBC */
    DBT_MODEL_RUN_ELAPSED_TIME("dbtModelRunElapsedTime"),
    /** TBC */
    DBT_MODEL_RUN_GENERATED_AT("dbtModelRunGeneratedAt"),
    /** TBC */
    END_TIME("endTime"),
    /** Number of fields in this object. */
    FIELD_COUNT("fieldCount"),
    /** Effective time for retention of objects in this bucket. */
    GCS_BUCKET_RETENTION_EFFECTIVE_TIME("gcsBucketRetentionEffectiveTime"),
    /** Retention period for objects in this bucket. */
    GCS_BUCKET_RETENTION_PERIOD("gcsBucketRetentionPeriod"),
    /** Version of metadata for this asset at this generation. Used for preconditions and detecting changes in metadata. A metageneration number is only meaningful in the context of a particular generation of a particular asset. */
    GCS_META_GENERATION_ID("gcsMetaGenerationId"),
    /** Number of objects within the bucket. */
    GCS_OBJECT_COUNT("gcsObjectCount"),
    /** Time (epoch) at which this object's data was last modified, in milliseconds. */
    GCS_OBJECT_DATA_LAST_MODIFIED_TIME("gcsObjectDataLastModifiedTime"),
    /** Generation ID of this object. */
    GCS_OBJECT_GENERATION_ID("gcsObjectGenerationId"),
    /** Retention expiration date of this object. */
    GCS_OBJECT_RETENTION_EXPIRATION_DATE("gcsObjectRetentionExpirationDate"),
    /** Object size in bytes. */
    GCS_OBJECT_SIZE("gcsObjectSize"),
    /** Number of the project in which the asset exists. */
    GOOGLE_PROJECT_NUMBER("googleProjectNumber"),
    /** Time (epoch) when this column was imagined, in milliseconds. */
    GUACAMOLE_CONCEPTUALIZED("guacamoleConceptualized"),
    /** Consolidated quantification metric spanning number of columns, rows, and sparsity of population. */
    GUACAMOLE_SIZE("guacamoleSize"),
    /** Maximum size of a Guacamole column. */
    GUACAMOLE_WIDTH("guacamoleWidth"),
    /** Number of members in this consumer group. */
    KAFKA_CONSUMER_GROUP_MEMBER_COUNT("kafkaConsumerGroupMemberCount"),
    /** Number of partitions for this topic. */
    KAFKA_TOPIC_PARTITIONS_COUNT("kafkaTopicPartitionsCount"),
    /** Number of (unexpired) messages in this topic. */
    KAFKA_TOPIC_RECORD_COUNT("kafkaTopicRecordCount"),
    /** Replication factor for this topic. */
    KAFKA_TOPIC_REPLICATION_FACTOR("kafkaTopicReplicationFactor"),
    /** Amount of time messages will be retained in this topic, in milliseconds. */
    KAFKA_TOPIC_RETENTION_TIME_IN_MS("kafkaTopicRetentionTimeInMs"),
    /** Segment size for this topic. */
    KAFKA_TOPIC_SEGMENT_BYTES("kafkaTopicSegmentBytes"),
    /** Size of this topic, in bytes. */
    KAFKA_TOPIC_SIZE_IN_BYTES("kafkaTopicSizeInBytes"),
    /** Time (epoch) at which this asset was last profiled, in milliseconds. */
    LAST_PROFILED_AT("lastProfiledAt"),
    /** Time (epoch) of the last operation that inserted, updated, or deleted rows, in milliseconds. */
    LAST_ROW_CHANGED_AT("lastRowChangedAt"),
    /** Time (epoch) at which this asset was last crawled, in milliseconds. */
    LAST_SYNC_RUN_AT("lastSyncRunAt"),
    /** Deprecated. */
    LOOKER_TIMES_USED("lookerTimesUsed"),
    /** Identifier of the Look used to create this tile, from Looker. */
    LOOK_ID("lookId"),
    /** Number of components within the job. */
    MATILLION_JOB_COMPONENT_COUNT("matillionJobComponentCount"),
    /** Number of projects within the group. */
    MATILLION_PROJECT_COUNT("matillionProjectCount"),
    /** Number of jobs in the project. */
    MATILLION_PROJECT_JOB_COUNT("matillionProjectJobCount"),
    /** Maximum length of this field. */
    MAX_LENGTH("maxLength"),
    /** Rate at which this monitor is breached. */
    MC_MONITOR_BREACH_RATE("mcMonitorBreachRate"),
    /** Number of incidents associated with this monitor. */
    MC_MONITOR_INCIDENT_COUNT("mcMonitorIncidentCount"),
    /** Time at which the next execution of the rule should occur. */
    MC_MONITOR_RULE_NEXT_EXECUTION_TIME("mcMonitorRuleNextExecutionTime"),
    /** Time at which the previous execution of the rule occurred. */
    MC_MONITOR_RULE_PREVIOUS_EXECUTION_TIME("mcMonitorRulePreviousExecutionTime"),
    /** TBC */
    METABASE_DASHBOARD_COUNT("metabaseDashboardCount"),
    /** TBC */
    METABASE_QUESTION_COUNT("metabaseQuestionCount"),
    /** Time (epoch) this asset was certified in MicroStrategy, in milliseconds. */
    MICRO_STRATEGY_CERTIFIED_AT("microStrategyCertifiedAt"),
    /** TBC */
    MODE_CHART_COUNT("modeChartCount"),
    /** Number of collections in this workspace. */
    MODE_COLLECTION_COUNT("modeCollectionCount"),
    /** TBC */
    MODE_QUERY_COUNT("modeQueryCount"),
    /** TBC */
    MODE_REPORT_IMPORT_COUNT("modeReportImportCount"),
    /** TBC */
    MODE_REPORT_PUBLISHED_AT("modeReportPublishedAt"),
    /** Time (in milliseconds) when the asset was last updated. */
    MODIFICATION_TIMESTAMP("__modificationTimestamp"),
    /** Average size of an object in the collection. */
    MONGO_DB_COLLECTION_AVERAGE_OBJECT_SIZE("mongoDBCollectionAverageObjectSize"),
    /** Seconds after which documents in a time series collection or clustered collection expire. */
    MONGO_DB_COLLECTION_EXPIRE_AFTER_SECONDS("mongoDBCollectionExpireAfterSeconds"),
    /** Maximum number of documents allowed in a capped collection. */
    MONGO_DB_COLLECTION_MAXIMUM_DOCUMENT_COUNT("mongoDBCollectionMaximumDocumentCount"),
    /** Maximum size allowed in a capped collection. */
    MONGO_DB_COLLECTION_MAX_SIZE("mongoDBCollectionMaxSize"),
    /** Number of indexes on the collection. */
    MONGO_DB_COLLECTION_NUM_INDEXES("mongoDBCollectionNumIndexes"),
    /** Number of orphaned documents in the collection. */
    MONGO_DB_COLLECTION_NUM_ORPHAN_DOCS("mongoDBCollectionNumOrphanDocs"),
    /** Total size of all indexes. */
    MONGO_DB_COLLECTION_TOTAL_INDEX_SIZE("mongoDBCollectionTotalIndexSize"),
    /** Number of collections in the database. */
    MONGO_DB_DATABASE_COLLECTION_COUNT("mongoDBDatabaseCollectionCount"),
    /** Number of columns nested within this (STRUCT or NESTED) column. */
    NESTED_COLUMN_COUNT("nestedColumnCount"),
    /** Number of digits allowed to the right of the decimal point. */
    NUMERIC_SCALE("numericScale"),
    /** Number of rows after which results should be uploaded to storage. */
    OBJECT_STORAGE_UPLOAD_THRESHOLD("objectStorageUploadThreshold"),
    /** TBC */
    OPERATION_END_TIME("operationEndTime"),
    /** TBC */
    OPERATION_START_TIME("operationStartTime"),
    /** Order (position) of this field within the object. */
    ORDER("order"),
    /** Number of pages in this report. */
    PAGE_COUNT("pageCount"),
    /** Number of sub-partitions of this partition. */
    PARTITION_COUNT("partitionCount"),
    /** Order (position) of this partition column in the table. */
    PARTITION_ORDER("partitionOrder"),
    /** Time (epoch) at which this column was pinned, in milliseconds. */
    PINNED_AT("pinnedAt"),
    /** TBC */
    POLICY_PRIORITY("policyPriority"),
    /** Number of days over which popularity is calculated, for example 30 days. */
    POPULARITY_INSIGHTS_TIMEFRAME("popularityInsightsTimeframe"),
    /** Popularity score for this asset. */
    POPULARITY_SCORE("popularityScore"),
    /** Port number to this connection's source. */
    PORT("port"),
    /** Number of columns in this table. */
    POWER_BI_TABLE_COLUMN_COUNT("powerBITableColumnCount"),
    /** Number of measures in this table. */
    POWER_BI_TABLE_MEASURE_COUNT("powerBITableMeasureCount"),
    /** Total number of digits allowed */
    PRECISION("precision"),
    /** TBC */
    PRESET_DASHBOARD_CHART_COUNT("presetDashboardChartCount"),
    /** Identifier of the dashboard in which this asset exists, in Preset. */
    PRESET_DASHBOARD_ID("presetDashboardId"),
    /** TBC */
    PRESET_DATASET_ID("presetDatasetId"),
    /** TBC */
    PRESET_WORKSPACE_CLUSTER_ID("presetWorkspaceClusterId"),
    /** TBC */
    PRESET_WORKSPACE_DASHBOARD_COUNT("presetWorkspaceDashboardCount"),
    /** TBC */
    PRESET_WORKSPACE_DATASET_COUNT("presetWorkspaceDatasetCount"),
    /** TBC */
    PRESET_WORKSPACE_DEPLOYMENT_ID("presetWorkspaceDeploymentId"),
    /** Identifier of the workspace in which this asset exists, in Preset. */
    PRESET_WORKSPACE_ID("presetWorkspaceId"),
    /** Static space used by this app, in bytes. */
    QLIK_APP_STATIC_BYTE_SIZE("qlikAppStaticByteSize"),
    /** Number of times this asset has been queried. */
    QUERY_COUNT("queryCount"),
    /** Time (epoch) at which the query count was last updated, in milliseconds. */
    QUERY_COUNT_UPDATED_AT("queryCountUpdatedAt"),
    /** Identifier for the query used to build this tile, from Looker. */
    QUERY_ID("queryID"),
    /** Maximum time a query should be allowed to run before timing out. */
    QUERY_TIMEOUT("queryTimeout"),
    /** Number of unique users who have queried this asset. */
    QUERY_USER_COUNT("queryUserCount"),
    /** Time (epoch) at which this dashboard was last published, in milliseconds. */
    QUICK_SIGHT_DASHBOARD_LAST_PUBLISHED_TIME("quickSightDashboardLastPublishedTime"),
    /** Version number of the published dashboard. */
    QUICK_SIGHT_DASHBOARD_PUBLISHED_VERSION_NUMBER("quickSightDashboardPublishedVersionNumber"),
    /** Number of columns present in this dataset. */
    QUICK_SIGHT_DATASET_COLUMN_COUNT("quickSightDatasetColumnCount"),
    /** Number of widgets in this dashboard. */
    REDASH_DASHBOARD_WIDGET_COUNT("redashDashboardWidgetCount"),
    /** Time (epoch) when this query was last executed, in milliseconds. */
    REDASH_QUERY_LAST_EXECUTED_AT("redashQueryLastExecutedAt"),
    /** Elapsed time of the last execution of this query. */
    REDASH_QUERY_LAST_EXECUTION_RUNTIME("redashQueryLastExecutionRuntime"),
    /** Number of reports in this workspace. */
    REPORT_COUNT("reportCount"),
    /** TBC */
    RESULT_COUNT("resultCount"),
    /** Identifier of the ResultMarkerLookup entry, from Looker. */
    RESULT_MAKER_ID("resultMakerID"),
    /** Number of rows in this materialized view. */
    ROW_COUNT("rowCount"),
    /** Maximum number of rows that can be returned for the source. */
    ROW_LIMIT("rowLimit"),
    /** Number of objects within the bucket. */
    S3OBJECT_COUNT("s3ObjectCount"),
    /** Time (epoch) at which this object was last updated, in milliseconds, or when it was created if it has never been modified. */
    S3OBJECT_LAST_MODIFIED_TIME("s3ObjectLastModifiedTime"),
    /** Object size in bytes. */
    S3OBJECT_SIZE("s3ObjectSize"),
    /** Number of schemas in this database. */
    SCHEMA_COUNT("schemaCount"),
    /** Number of columns in this dataset. */
    SIGMA_DATASET_COLUMN_COUNT("sigmaDatasetColumnCount"),
    /** Number of data elements on this page. */
    SIGMA_DATA_ELEMENT_COUNT("sigmaDataElementCount"),
    /** Number of fields in this data element. */
    SIGMA_DATA_ELEMENT_FIELD_COUNT("sigmaDataElementFieldCount"),
    /** Number of pages in this workbook. */
    SIGMA_PAGE_COUNT("sigmaPageCount"),
    /** Number of widgets in this dashboard. */
    SISENSE_DASHBOARD_WIDGET_COUNT("sisenseDashboardWidgetCount"),
    /** Time (epoch) when this datamodel was last built, in milliseconds. */
    SISENSE_DATAMODEL_LAST_BUILD_TIME("sisenseDatamodelLastBuildTime"),
    /** Time (epoch) when this datamodel was last published, in milliseconds. */
    SISENSE_DATAMODEL_LAST_PUBLISH_TIME("sisenseDatamodelLastPublishTime"),
    /** Time (epoch) when this datamodel was last built successfully, in milliseconds. */
    SISENSE_DATAMODEL_LAST_SUCCESSFUL_BUILD_TIME("sisenseDatamodelLastSuccessfulBuildTime"),
    /** Number of columns present in this datamodel table. */
    SISENSE_DATAMODEL_TABLE_COLUMN_COUNT("sisenseDatamodelTableColumnCount"),
    /** Number of tables in this datamodel. */
    SISENSE_DATAMODEL_TABLE_COUNT("sisenseDatamodelTableCount"),
    /** Number of columns used in this widget. */
    SISENSE_WIDGET_COLUMN_COUNT("sisenseWidgetColumnCount"),
    /** Size of this materialized view, in bytes. */
    SIZE_BYTES("sizeBytes"),
    /** Time (epoch) after which this stream will be stale, in milliseconds. */
    SNOWFLAKE_STREAM_STALE_AFTER("snowflakeStreamStaleAfter"),
    /** TBC */
    SODA_CHECK_INCIDENT_COUNT("sodaCheckIncidentCount"),
    /** TBC */
    SODA_CHECK_LAST_SCAN_AT("sodaCheckLastScanAt"),
    /** Identifier of the user who last updated the dashboard, from Looker. */
    SOURCELAST_UPDATER_ID("sourcelastUpdaterId"),
    /** Number of subfolders in this folder. */
    SOURCE_CHILD_COUNT("sourceChildCount"),
    /** Identifier for the folder's content metadata in Looker. */
    SOURCE_CONTENT_METADATA_ID("sourceContentMetadataId"),
    /** Time (epoch) at which this asset was created in the source system, in milliseconds. */
    SOURCE_CREATED_AT("sourceCreatedAt"),
    /** Identifier of the user who created the folder, from Looker. */
    SOURCE_CREATOR_ID("sourceCreatorId"),
    /** Timestamp (epoch) when the dashboard was last accessed by a user, in milliseconds. */
    SOURCE_LAST_ACCESSED_AT("sourceLastAccessedAt"),
    /** Timestamp of most recent read operation. */
    SOURCE_LAST_READ_AT("sourceLastReadAt"),
    /** Timestamp (epoch) when the dashboard was last viewed by a user. */
    SOURCE_LAST_VIEWED_AT("sourceLastViewedAt"),
    /** Identifier of the dashboard's content metadata, from Looker. */
    SOURCE_METADATA_ID("sourceMetadataId"),
    /** Identifier of the parent folder of this folder, from Looker. */
    SOURCE_PARENT_ID("sourceParentID"),
    /** Identifier of the query for the Look, from Looker. */
    SOURCE_QUERY_ID("sourceQueryId"),
    /** Total count of all read operations at source. */
    SOURCE_READ_COUNT("sourceReadCount"),
    /** Total cost of read queries at source. */
    SOURCE_READ_QUERY_COST("sourceReadQueryCost"),
    /** Total number of unique users that read data from asset. */
    SOURCE_READ_USER_COUNT("sourceReadUserCount"),
    /** Total cost of all operations at source. */
    SOURCE_TOTAL_COST("sourceTotalCost"),
    /** Time (epoch) at which this asset was last updated in the source system, in milliseconds. */
    SOURCE_UPDATED_AT("sourceUpdatedAt"),
    /** Identifier of the user who created this dashboard, from Looker. */
    SOURCE_USER_ID("sourceUserId"),
    /** Number of times the dashboard has been viewed through the Looker web UI. */
    SOURCE_VIEW_COUNT("sourceViewCount"),
    /** Time (epoch) from which this materialized view is stale, in milliseconds. */
    STALE_SINCE_DATE("staleSinceDate"),
    /** Number of users who have starred this asset. */
    STARRED_COUNT("starredCount"),
    /** TBC */
    START_TIME("startTime"),
    /** Number of tables in this schema. */
    TABLE_COUNT("tableCount"),
    /** Number of tiles in this table. */
    TILE_COUNT("tileCount"),
    /** Time (in milliseconds) when the asset was created. */
    TIMESTAMP("__timestamp"),
    /** Number of views in this schema. */
    VIEW_COUNT("viewsCount"),
    /** View score for this asset. */
    VIEW_SCORE("viewScore"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    NumericFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
