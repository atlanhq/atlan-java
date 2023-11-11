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
    /** Time (epoch) when this column was imagined, in milliseconds. */
    GUACAMOLE_CONCEPTUALIZED("guacamoleConceptualized"),
    /** Consolidated quantification metric spanning number of columns, rows, and sparsity of population. */
    GUACAMOLE_SIZE("guacamoleSize"),
    /** Maximum size of a Guacamole column. */
    GUACAMOLE_WIDTH("guacamoleWidth"),
    /** Time (epoch) at which this asset was last profiled, in milliseconds. */
    LAST_PROFILED_AT("lastProfiledAt"),
    /** Time (epoch) of the last operation that inserted, updated, or deleted rows, in milliseconds. */
    LAST_ROW_CHANGED_AT("lastRowChangedAt"),
    /** Time (epoch) at which this asset was last crawled, in milliseconds. */
    LAST_SYNC_RUN_AT("lastSyncRunAt"),
    /** Maximum length of a value in this column. */
    MAX_LENGTH("maxLength"),
    /** Rate at which this monitor is breached. */
    MC_MONITOR_BREACH_RATE("mcMonitorBreachRate"),
    /** Number of incidents associated with this monitor. */
    MC_MONITOR_INCIDENT_COUNT("mcMonitorIncidentCount"),
    /** Time at which the next execution of the rule should occur. */
    MC_MONITOR_RULE_NEXT_EXECUTION_TIME("mcMonitorRuleNextExecutionTime"),
    /** Time at which the previous execution of the rule occurred. */
    MC_MONITOR_RULE_PREVIOUS_EXECUTION_TIME("mcMonitorRulePreviousExecutionTime"),
    /** Time (in milliseconds) when the asset was last updated. */
    MODIFICATION_TIMESTAMP("__modificationTimestamp"),
    /** Number of columns nested within this (STRUCT or NESTED) column. */
    NESTED_COLUMN_COUNT("nestedColumnCount"),
    /** Number of digits allowed to the right of the decimal point. */
    NUMERIC_SCALE("numericScale"),
    /** Order (position) in which this column appears in the table (starting at 1). */
    ORDER("order"),
    /** Number of sub-partitions of this partition. */
    PARTITION_COUNT("partitionCount"),
    /** Order (position) of this partition column in the table. */
    PARTITION_ORDER("partitionOrder"),
    /** Time (epoch) at which this column was pinned, in milliseconds. */
    PINNED_AT("pinnedAt"),
    /** Popularity score for this asset. */
    POPULARITY_SCORE("popularityScore"),
    /** Total number of digits allowed, when the dataType is numeric. */
    PRECISION("precision"),
    /** Number of times this asset has been queried. */
    QUERY_COUNT("queryCount"),
    /** Time (epoch) at which the query count was last updated, in milliseconds. */
    QUERY_COUNT_UPDATED_AT("queryCountUpdatedAt"),
    /** Number of unique users who have queried this asset. */
    QUERY_USER_COUNT("queryUserCount"),
    /** Number of rows in this materialized view. */
    ROW_COUNT("rowCount"),
    /** Size of this materialized view, in bytes. */
    SIZE_BYTES("sizeBytes"),
    /** TBC */
    SODA_CHECK_INCIDENT_COUNT("sodaCheckIncidentCount"),
    /** TBC */
    SODA_CHECK_LAST_SCAN_AT("sodaCheckLastScanAt"),
    /** Time (epoch) at which this asset was created in the source system, in milliseconds. */
    SOURCE_CREATED_AT("sourceCreatedAt"),
    /** Timestamp of most recent read operation. */
    SOURCE_LAST_READ_AT("sourceLastReadAt"),
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
    /** Time (epoch) from which this materialized view is stale, in milliseconds. */
    STALE_SINCE_DATE("staleSinceDate"),
    /** Number of users who have starred this asset. */
    STARRED_COUNT("starredCount"),
    /** Number of tables in this schema. */
    TABLE_COUNT("tableCount"),
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
