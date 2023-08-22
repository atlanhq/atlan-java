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
public enum NumericFields implements AtlanSearchableField {
    /** TBC */
    ADLS_OBJECT_ACCESS_TIER_LAST_MODIFIED_TIME("adlsObjectAccessTierLastModifiedTime"),
    /** Number of objects that exist within this container. */
    ADLS_OBJECT_COUNT("adlsObjectCount"),
    /** TBC */
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
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN("assetDbtJobLastRun"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_CREATED_AT("assetDbtJobLastRunCreatedAt"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_DEQUED_AT("assetDbtJobLastRunDequedAt"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_STARTED_AT("assetDbtJobLastRunStartedAt"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_UPDATED_AT("assetDbtJobLastRunUpdatedAt"),
    /** TBC */
    ASSET_DBT_JOB_NEXT_RUN("assetDbtJobNextRun"),
    /** TBC */
    ASSET_MC_LAST_SYNC_RUN_AT("assetMcLastSyncRunAt"),
    /** Soda check count */
    ASSET_SODA_CHECK_COUNT("assetSodaCheckCount"),
    /** TBC */
    ASSET_SODA_LAST_SCAN_AT("assetSodaLastScanAt"),
    /** TBC */
    ASSET_SODA_LAST_SYNC_RUN_AT("assetSodaLastSyncRunAt"),
    /** TBC */
    AUTH_SERVICE_POLICY_LAST_SYNC("authServicePolicyLastSync"),
    /** Time (epoch) at which the certification was last updated, in milliseconds. */
    CERTIFICATE_UPDATED_AT("certificateUpdatedAt"),
    /** TBC */
    COLUMN_AVERAGE("columnAverage"),
    /** Average length of values in a string column. */
    COLUMN_AVERAGE_LENGTH("columnAverageLength"),
    /** Number of columns in this materialized view. */
    COLUMN_COUNT("columnCount"),
    /** Level of nesting, used for STRUCT/NESTED columns */
    COLUMN_DEPTH_LEVEL("columnDepthLevel"),
    /** Number of rows that contain distinct values. */
    COLUMN_DISTINCT_VALUES_COUNT("columnDistinctValuesCount"),
    /** TBC */
    COLUMN_DISTINCT_VALUES_COUNT_LONG("columnDistinctValuesCountLong"),
    /** Number of rows that contain duplicate values. */
    COLUMN_DUPLICATE_VALUES_COUNT("columnDuplicateValuesCount"),
    /** TBC */
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
    /** TBC */
    COLUMN_MISSING_VALUES_COUNT_LONG("columnMissingValuesCountLong"),
    /** Percentage of rows in a column that do not contain content. */
    COLUMN_MISSING_VALUES_PERCENTAGE("columnMissingValuesPercentage"),
    /** Calculated standard deviation of the values in a numeric column. */
    COLUMN_STANDARD_DEVIATION("columnStandardDeviation"),
    /** Calculated sum of the values in a numeric column. */
    COLUMN_SUM("columnSum"),
    /** Ratio indicating how unique data in the column is: 0 indicates that all values are the same, 100 indicates that all values in the column are unique. */
    COLUMN_UNIQUENESS_PERCENTAGE("columnUniquenessPercentage"),
    /** Number of rows in which a value in this column appears only once. */
    COLUMN_UNIQUE_VALUES_COUNT("columnUniqueValuesCount"),
    /** TBC */
    COLUMN_UNIQUE_VALUES_COUNT_LONG("columnUniqueValuesCountLong"),
    /** Calculated variance of the values in a numeric column. */
    COLUMN_VARIANCE("columnVariance"),
    /** TBC */
    DASHBOARD_COUNT("dashboardCount"),
    /** TBC */
    DATAFLOW_COUNT("dataflowCount"),
    /** TBC */
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
    /** Number of fields in the object. */
    FIELD_COUNT("fieldCount"),
    /** TBC */
    GCS_BUCKET_RETENTION_EFFECTIVE_TIME("gcsBucketRetentionEffectiveTime"),
    /** TBC */
    GCS_BUCKET_RETENTION_PERIOD("gcsBucketRetentionPeriod"),
    /** TBC */
    GCS_META_GENERATION_ID("gcsMetaGenerationId"),
    /** TBC */
    GCS_OBJECT_COUNT("gcsObjectCount"),
    /** TBC */
    GCS_OBJECT_DATA_LAST_MODIFIED_TIME("gcsObjectDataLastModifiedTime"),
    /** TBC */
    GCS_OBJECT_GENERATION_ID("gcsObjectGenerationId"),
    /** TBC */
    GCS_OBJECT_RETENTION_EXPIRATION_DATE("gcsObjectRetentionExpirationDate"),
    /** Object size in bytes. */
    GCS_OBJECT_SIZE("gcsObjectSize"),
    /** TBC */
    GOOGLE_PROJECT_NUMBER("googleProjectNumber"),
    /** TBC */
    KAFKA_CONSUMER_GROUP_MEMBER_COUNT("kafkaConsumerGroupMemberCount"),
    /** TBC */
    KAFKA_TOPIC_PARTITIONS_COUNT("kafkaTopicPartitionsCount"),
    /** Number of (unexpired) messages in this topic. */
    KAFKA_TOPIC_RECORD_COUNT("kafkaTopicRecordCount"),
    /** TBC */
    KAFKA_TOPIC_REPLICATION_FACTOR("kafkaTopicReplicationFactor"),
    /** TBC */
    KAFKA_TOPIC_SEGMENT_BYTES("kafkaTopicSegmentBytes"),
    /** TBC */
    KAFKA_TOPIC_SIZE_IN_BYTES("kafkaTopicSizeInBytes"),
    /** Time (epoch) at which the asset was last profiled, in milliseconds. */
    LAST_PROFILED_AT("lastProfiledAt"),
    /** Timestamp of last operation that inserted, updated, or deleted rows. */
    LAST_ROW_CHANGED_AT("lastRowChangedAt"),
    /** Time (epoch) at which the asset was last crawled, in milliseconds. */
    LAST_SYNC_RUN_AT("lastSyncRunAt"),
    /** TBC */
    LOOKER_TIMES_USED("lookerTimesUsed"),
    /** TBC */
    LOOK_ID("lookId"),
    /** TBC */
    MAX_LENGTH("maxLength"),
    /** TBC */
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
    /** Date when the asset was certified in MicroStrategy. */
    MICRO_STRATEGY_CERTIFIED_AT("microStrategyCertifiedAt"),
    /** TBC */
    MODE_CHART_COUNT("modeChartCount"),
    /** TBC */
    MODE_COLLECTION_COUNT("modeCollectionCount"),
    /** TBC */
    MODE_QUERY_COUNT("modeQueryCount"),
    /** TBC */
    MODE_REPORT_IMPORT_COUNT("modeReportImportCount"),
    /** TBC */
    MODE_REPORT_PUBLISHED_AT("modeReportPublishedAt"),
    /** Time (in milliseconds) when the asset was last updated. */
    MODIFICATION_TIMESTAMP("__modificationTimestamp"),
    /** TBC */
    NESTED_COLUMN_COUNT("nestedColumnCount"),
    /** Number of digits allowed to the right of the decimal point. */
    NUMERIC_SCALE("numericScale"),
    /** TBC */
    OPERATION_END_TIME("operationEndTime"),
    /** TBC */
    OPERATION_START_TIME("operationStartTime"),
    /** TBC */
    ORDER("order"),
    /** TBC */
    PAGE_COUNT("pageCount"),
    /** TBC */
    PARTITION_COUNT("partitionCount"),
    /** TBC */
    PARTITION_ORDER("partitionOrder"),
    /** TBC */
    PINNED_AT("pinnedAt"),
    /** TBC */
    POLICY_PRIORITY("policyPriority"),
    /** Number of days over which popularity is calculated, for example 30 days. */
    POPULARITY_INSIGHTS_TIMEFRAME("popularityInsightsTimeframe"),
    /** TBC */
    POPULARITY_SCORE("popularityScore"),
    /** Port number to the connection's source. */
    PORT("port"),
    /** TBC */
    POWER_BI_TABLE_COLUMN_COUNT("powerBITableColumnCount"),
    /** TBC */
    POWER_BI_TABLE_MEASURE_COUNT("powerBITableMeasureCount"),
    /** Total number of digits allowed. */
    PRECISION("precision"),
    /** Number of charts within the collection. */
    PRESET_DASHBOARD_CHART_COUNT("presetDashboardChartCount"),
    /** ID of the Preset asset's collection. */
    PRESET_DASHBOARD_ID("presetDashboardId"),
    /** ID of the dataset. */
    PRESET_DATASET_ID("presetDatasetId"),
    /** ID of the cluster for the Preset workspace. */
    PRESET_WORKSPACE_CLUSTER_ID("presetWorkspaceClusterId"),
    /** Number of collections in the workspace. */
    PRESET_WORKSPACE_DASHBOARD_COUNT("presetWorkspaceDashboardCount"),
    /** Number of datasets in the workspace. */
    PRESET_WORKSPACE_DATASET_COUNT("presetWorkspaceDatasetCount"),
    /** ID of the deployment for the Preset workspace. */
    PRESET_WORKSPACE_DEPLOYMENT_ID("presetWorkspaceDeploymentId"),
    /** ID of the Preset asset's workspace. */
    PRESET_WORKSPACE_ID("presetWorkspaceId"),
    /** Static space taken up by the app. */
    QLIK_APP_STATIC_BYTE_SIZE("qlikAppStaticByteSize"),
    /** TBC */
    QUERY_COUNT("queryCount"),
    /** Time (epoch) at which the query count was last updated, in milliseconds. */
    QUERY_COUNT_UPDATED_AT("queryCountUpdatedAt"),
    /** TBC */
    QUERY_ID("queryID"),
    /** TBC */
    QUERY_TIMEOUT("queryTimeout"),
    /** TBC */
    QUERY_USER_COUNT("queryUserCount"),
    /** Last published time of dashboard */
    QUICK_SIGHT_DASHBOARD_LAST_PUBLISHED_TIME("quickSightDashboardLastPublishedTime"),
    /** Version number of the dashboard published */
    QUICK_SIGHT_DASHBOARD_PUBLISHED_VERSION_NUMBER("quickSightDashboardPublishedVersionNumber"),
    /** Quicksight dataset column count indicates number of columns present in the dataset */
    QUICK_SIGHT_DATASET_COLUMN_COUNT("quickSightDatasetColumnCount"),
    /** Number of widgets in the Redash dashboard. */
    REDASH_DASHBOARD_WIDGET_COUNT("redashDashboardWidgetCount"),
    /** Time when the Redash query was last executed. */
    REDASH_QUERY_LAST_EXECUTED_AT("redashQueryLastExecutedAt"),
    /** Elapsed time of the last run of the Redash query. */
    REDASH_QUERY_LAST_EXECUTION_RUNTIME("redashQueryLastExecutionRuntime"),
    /** TBC */
    REPORT_COUNT("reportCount"),
    /** TBC */
    RESULT_COUNT("resultCount"),
    /** TBC */
    RESULT_MAKER_ID("resultMakerID"),
    /** Number of rows in this materialized view. */
    ROW_COUNT("rowCount"),
    /** Maximum number of rows that can be returned for the source. */
    ROW_LIMIT("rowLimit"),
    /** Number of objects within the bucket. */
    S3OBJECT_COUNT("s3ObjectCount"),
    /** Time (epoch) at which the object was last updated, in milliseconds, or when it was created if it has never been modified. */
    S3OBJECT_LAST_MODIFIED_TIME("s3ObjectLastModifiedTime"),
    /** Object size in bytes. */
    S3OBJECT_SIZE("s3ObjectSize"),
    /** Number of schemas in this database. */
    SCHEMA_COUNT("schemaCount"),
    /** Number of columns that exist within this dataset. */
    SIGMA_DATASET_COLUMN_COUNT("sigmaDatasetColumnCount"),
    /** Number of data elements that exist within this page. */
    SIGMA_DATA_ELEMENT_COUNT("sigmaDataElementCount"),
    /** Number of data element fields within this data element. */
    SIGMA_DATA_ELEMENT_FIELD_COUNT("sigmaDataElementFieldCount"),
    /** Number of pages that exist within this workbook. */
    SIGMA_PAGE_COUNT("sigmaPageCount"),
    /** Size of the materialized view in bytes. */
    SIZE_BYTES("sizeBytes"),
    /** TBC */
    SNOWFLAKE_STREAM_STALE_AFTER("snowflakeStreamStaleAfter"),
    /** TBC */
    SODA_CHECK_INCIDENT_COUNT("sodaCheckIncidentCount"),
    /** TBC */
    SODA_CHECK_LAST_SCAN_AT("sodaCheckLastScanAt"),
    /** TBC */
    SOURCELAST_UPDATER_ID("sourcelastUpdaterId"),
    /** TBC */
    SOURCE_CHILD_COUNT("sourceChildCount"),
    /** TBC */
    SOURCE_CONTENT_METADATA_ID("sourceContentMetadataId"),
    /** Time (epoch) at which the asset was created in the source system, in milliseconds. */
    SOURCE_CREATED_AT("sourceCreatedAt"),
    /** TBC */
    SOURCE_CREATOR_ID("sourceCreatorId"),
    /** TBC */
    SOURCE_LAST_ACCESSED_AT("sourceLastAccessedAt"),
    /** Timestamp of most recent read operation. */
    SOURCE_LAST_READ_AT("sourceLastReadAt"),
    /** TBC */
    SOURCE_LAST_VIEWED_AT("sourceLastViewedAt"),
    /** TBC */
    SOURCE_METADATA_ID("sourceMetadataId"),
    /** TBC */
    SOURCE_PARENT_ID("sourceParentID"),
    /** TBC */
    SOURCE_QUERY_ID("sourceQueryId"),
    /** Total count of all read operations at source. */
    SOURCE_READ_COUNT("sourceReadCount"),
    /** Total cost of read queries at source. */
    SOURCE_READ_QUERY_COST("sourceReadQueryCost"),
    /** Total number of unique users that read data from asset. */
    SOURCE_READ_USER_COUNT("sourceReadUserCount"),
    /** Total cost of all operations at source. */
    SOURCE_TOTAL_COST("sourceTotalCost"),
    /** Time (epoch) at which the asset was last updated in the source system, in milliseconds. */
    SOURCE_UPDATED_AT("sourceUpdatedAt"),
    /** TBC */
    SOURCE_USER_ID("sourceUserId"),
    /** TBC */
    SOURCE_VIEW_COUNT("sourceViewCount"),
    /** TBC */
    STALE_SINCE_DATE("staleSinceDate"),
    /** TBC */
    STARRED_COUNT("starredCount"),
    /** TBC */
    START_TIME("startTime"),
    /** Number of tables in this schema. */
    TABLE_COUNT("tableCount"),
    /** TBC */
    TILE_COUNT("tileCount"),
    /** Time (in milliseconds) when the asset was created. */
    TIMESTAMP("__timestamp"),
    /** Number of views in this schema. */
    VIEW_COUNT("viewsCount"),
    /** TBC */
    VIEW_SCORE("viewScore"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    NumericFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
