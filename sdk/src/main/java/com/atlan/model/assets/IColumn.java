/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.model.fields.BooleanField;
import com.atlan.model.fields.KeywordField;
import com.atlan.model.fields.KeywordTextField;
import com.atlan.model.fields.NumericField;
import com.atlan.model.fields.RelationField;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.ColumnValueFrequencyMap;
import com.atlan.model.structs.Histogram;
import com.atlan.model.structs.PopularityInsights;
import com.atlan.model.structs.StarredDetails;
import com.atlan.serde.AssetDeserializer;
import com.atlan.serde.AssetSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;

/**
 * Instance of a column in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface IColumn {

    public static final String TYPE_NAME = "Column";

    /** Calculate view in which this column exists. */
    RelationField CALCULATION_VIEW = new RelationField("calculationView");

    /** Average value in this column. */
    NumericField COLUMN_AVERAGE = new NumericField("columnAverage", "columnAverage");

    /** Average length of values in a string column. */
    NumericField COLUMN_AVERAGE_LENGTH = new NumericField("columnAverageLength", "columnAverageLength");

    /** TBC */
    RelationField COLUMN_DBT_MODEL_COLUMNS = new RelationField("columnDbtModelColumns");

    /** Level of nesting of this column, used for STRUCT and NESTED columns. */
    NumericField COLUMN_DEPTH_LEVEL = new NumericField("columnDepthLevel", "columnDepthLevel");

    /** Number of rows that contain distinct values. */
    NumericField COLUMN_DISTINCT_VALUES_COUNT =
            new NumericField("columnDistinctValuesCount", "columnDistinctValuesCount");

    /** Number of rows that contain distinct values. */
    NumericField COLUMN_DISTINCT_VALUES_COUNT_LONG =
            new NumericField("columnDistinctValuesCountLong", "columnDistinctValuesCountLong");

    /** Number of rows that contain duplicate values. */
    NumericField COLUMN_DUPLICATE_VALUES_COUNT =
            new NumericField("columnDuplicateValuesCount", "columnDuplicateValuesCount");

    /** Number of rows that contain duplicate values. */
    NumericField COLUMN_DUPLICATE_VALUES_COUNT_LONG =
            new NumericField("columnDuplicateValuesCountLong", "columnDuplicateValuesCountLong");

    /** List of values in a histogram that represents the contents of this column. */
    KeywordField COLUMN_HISTOGRAM = new KeywordField("columnHistogram", "columnHistogram");

    /** Greatest value in a numeric column. */
    NumericField COLUMN_MAX = new NumericField("columnMax", "columnMax");

    /** Length of the longest value in a string column. */
    NumericField COLUMN_MAXIMUM_STRING_LENGTH =
            new NumericField("columnMaximumStringLength", "columnMaximumStringLength");

    /** List of the greatest values in a column. */
    KeywordField COLUMN_MAXS = new KeywordField("columnMaxs", "columnMaxs");

    /** Arithmetic mean of the values in a numeric column. */
    NumericField COLUMN_MEAN = new NumericField("columnMean", "columnMean");

    /** Calculated median of the values in a numeric column. */
    NumericField COLUMN_MEDIAN = new NumericField("columnMedian", "columnMedian");

    /** Least value in a numeric column. */
    NumericField COLUMN_MIN = new NumericField("columnMin", "columnMin");

    /** Length of the shortest value in a string column. */
    NumericField COLUMN_MINIMUM_STRING_LENGTH =
            new NumericField("columnMinimumStringLength", "columnMinimumStringLength");

    /** List of the least values in a column. */
    KeywordField COLUMN_MINS = new KeywordField("columnMins", "columnMins");

    /** Number of rows in a column that do not contain content. */
    NumericField COLUMN_MISSING_VALUES_COUNT = new NumericField("columnMissingValuesCount", "columnMissingValuesCount");

    /** Number of rows in a column that do not contain content. */
    NumericField COLUMN_MISSING_VALUES_COUNT_LONG =
            new NumericField("columnMissingValuesCountLong", "columnMissingValuesCountLong");

    /** Percentage of rows in a column that do not contain content. */
    NumericField COLUMN_MISSING_VALUES_PERCENTAGE =
            new NumericField("columnMissingValuesPercentage", "columnMissingValuesPercentage");

    /** Calculated standard deviation of the values in a numeric column. */
    NumericField COLUMN_STANDARD_DEVIATION = new NumericField("columnStandardDeviation", "columnStandardDeviation");

    /** Calculated sum of the values in a numeric column. */
    NumericField COLUMN_SUM = new NumericField("columnSum", "columnSum");

    /** List of top values in this column. */
    KeywordField COLUMN_TOP_VALUES = new KeywordField("columnTopValues", "columnTopValues");

    /** Number of rows in which a value in this column appears only once. */
    NumericField COLUMN_UNIQUE_VALUES_COUNT = new NumericField("columnUniqueValuesCount", "columnUniqueValuesCount");

    /** Number of rows in which a value in this column appears only once. */
    NumericField COLUMN_UNIQUE_VALUES_COUNT_LONG =
            new NumericField("columnUniqueValuesCountLong", "columnUniqueValuesCountLong");

    /** Ratio indicating how unique data in this column is: 0 indicates that all values are the same, 100 indicates that all values in this column are unique. */
    NumericField COLUMN_UNIQUENESS_PERCENTAGE =
            new NumericField("columnUniquenessPercentage", "columnUniquenessPercentage");

    /** Calculated variance of the values in a numeric column. */
    NumericField COLUMN_VARIANCE = new NumericField("columnVariance", "columnVariance");

    /** TBC */
    RelationField DATA_QUALITY_METRIC_DIMENSIONS = new RelationField("dataQualityMetricDimensions");

    /** Data type of values in this column. */
    KeywordTextField DATA_TYPE = new KeywordTextField("dataType", "dataType", "dataType.text");

    /** TBC */
    RelationField DBT_METRICS = new RelationField("dbtMetrics");

    /** TBC */
    RelationField DBT_MODEL_COLUMNS = new RelationField("dbtModelColumns");

    /** Default value for this column. */
    KeywordField DEFAULT_VALUE = new KeywordField("defaultValue", "defaultValue");

    /** Column this foreign key column refers to. */
    RelationField FOREIGN_KEY_FROM = new RelationField("foreignKeyFrom");

    /** Columns that use this column as a foreign key. */
    RelationField FOREIGN_KEY_TO = new RelationField("foreignKeyTo");

    /** Whether this column is a clustered column (true) or not (false). */
    BooleanField IS_CLUSTERED = new BooleanField("isClustered", "isClustered");

    /** Whether this column is a distribution column (true) or not (false). */
    BooleanField IS_DIST = new BooleanField("isDist", "isDist");

    /** When true, this column is a foreign key to another table. NOTE: this must be true when using the foreignKeyTo relationship to specify columns that refer to this column as a foreign key. */
    BooleanField IS_FOREIGN = new BooleanField("isForeign", "isForeign");

    /** When true, this column is indexed in the database. */
    BooleanField IS_INDEXED = new BooleanField("isIndexed", "isIndexed");

    /** When true, the values in this column can be null. */
    BooleanField IS_NULLABLE = new BooleanField("isNullable", "isNullable");

    /** Whether this column is a partition column (true) or not (false). */
    BooleanField IS_PARTITION = new BooleanField("isPartition", "isPartition");

    /** Whether this column is pinned (true) or not (false). */
    BooleanField IS_PINNED = new BooleanField("isPinned", "isPinned");

    /** When true, this column is the primary key for the table. */
    BooleanField IS_PRIMARY = new BooleanField("isPrimary", "isPrimary");

    /** Whether this column is a sort column (true) or not (false). */
    BooleanField IS_SORT = new BooleanField("isSort", "isSort");

    /** Materialized view in which this column exists. */
    RelationField MATERIALIZED_VIEW = new RelationField("materialisedView");

    /** Maximum length of a value in this column. */
    NumericField MAX_LENGTH = new NumericField("maxLength", "maxLength");

    /** TBC */
    RelationField METRIC_TIMESTAMPS = new RelationField("metricTimestamps");

    /** Number of columns nested within this (STRUCT or NESTED) column. */
    NumericField NESTED_COLUMN_COUNT = new NumericField("nestedColumnCount", "nestedColumnCount");

    /** Nested columns that exist within this column. */
    RelationField NESTED_COLUMNS = new RelationField("nestedColumns");

    /** Number of digits allowed to the right of the decimal point. */
    NumericField NUMERIC_SCALE = new NumericField("numericScale", "numericScale");

    /** Order (position) in which this column appears in the table (starting at 1). */
    NumericField ORDER = new NumericField("order", "order");

    /** Column in which this sub-column is nested. */
    RelationField PARENT_COLUMN = new RelationField("parentColumn");

    /** Simple name of the column this column is nested within, for STRUCT and NESTED columns. */
    KeywordTextField PARENT_COLUMN_NAME =
            new KeywordTextField("parentColumnName", "parentColumnName.keyword", "parentColumnName");

    /** Unique name of the column this column is nested within, for STRUCT and NESTED columns. */
    KeywordTextField PARENT_COLUMN_QUALIFIED_NAME = new KeywordTextField(
            "parentColumnQualifiedName", "parentColumnQualifiedName", "parentColumnQualifiedName.text");

    /** Order (position) of this partition column in the table. */
    NumericField PARTITION_ORDER = new NumericField("partitionOrder", "partitionOrder");

    /** Time (epoch) at which this column was pinned, in milliseconds. */
    NumericField PINNED_AT = new NumericField("pinnedAt", "pinnedAt");

    /** User who pinned this column. */
    KeywordField PINNED_BY = new KeywordField("pinnedBy", "pinnedBy");

    /** Total number of digits allowed, when the dataType is numeric. */
    NumericField PRECISION = new NumericField("precision", "precision");

    /** Queries that access this column. */
    RelationField QUERIES = new RelationField("queries");

    /** TBC */
    KeywordField RAW_DATA_TYPE_DEFINITION = new KeywordField("rawDataTypeDefinition", "rawDataTypeDefinition");

    /** Snowflake dynamic table in which this column exists. */
    RelationField SNOWFLAKE_DYNAMIC_TABLE = new RelationField("snowflakeDynamicTable");

    /** Sub-data type of this column. */
    KeywordField SUB_DATA_TYPE = new KeywordField("subDataType", "subDataType");

    /** Table in which this column exists. */
    RelationField TABLE = new RelationField("table");

    /** Table partition that contains this column. */
    RelationField TABLE_PARTITION = new RelationField("tablePartition");

    /** Validations for this column. */
    KeywordField VALIDATIONS = new KeywordField("validations", "validations");

    /** View in which this column exists. */
    RelationField VIEW = new RelationField("view");

    /** List of groups who administer this asset. (This is only used for certain asset types.) */
    SortedSet<String> getAdminGroups();

    /** List of roles who administer this asset. (This is only used for Connection assets.) */
    SortedSet<String> getAdminRoles();

    /** List of users who administer this asset. (This is only used for certain asset types.) */
    SortedSet<String> getAdminUsers();

    /** Detailed message to include in the announcement on this asset. */
    String getAnnouncementMessage();

    /** Brief title for the announcement on this asset. Required when announcementType is specified. */
    String getAnnouncementTitle();

    /** Type of announcement on this asset. */
    AtlanAnnouncementType getAnnouncementType();

    /** Time (epoch) at which the announcement was last updated, in milliseconds. */
    Long getAnnouncementUpdatedAt();

    /** Name of the user who last updated the announcement. */
    String getAnnouncementUpdatedBy();

    /** TBC */
    String getAssetCoverImage();

    /** Name of the account in which this asset exists in dbt. */
    String getAssetDbtAccountName();

    /** Alias of this asset in dbt. */
    String getAssetDbtAlias();

    /** Version of the environment in which this asset is materialized in dbt. */
    String getAssetDbtEnvironmentDbtVersion();

    /** Name of the environment in which this asset is materialized in dbt. */
    String getAssetDbtEnvironmentName();

    /** Time (epoch) at which the job that materialized this asset in dbt last ran, in milliseconds. */
    Long getAssetDbtJobLastRun();

    /** Path in S3 to the artifacts saved from the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunArtifactS3Path();

    /** Whether artifacts were saved from the last run of the job that materialized this asset in dbt (true) or not (false). */
    Boolean getAssetDbtJobLastRunArtifactsSaved();

    /** Time (epoch) at which the job that materialized this asset in dbt was last created, in milliseconds. */
    Long getAssetDbtJobLastRunCreatedAt();

    /** Time (epoch) at which the job that materialized this asset in dbt was dequeued, in milliseconds. */
    Long getAssetDbtJobLastRunDequedAt();

    /** Thread ID of the user who executed the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunExecutedByThreadId();

    /** Branch in git from which the last run of the job that materialized this asset in dbt ran. */
    String getAssetDbtJobLastRunGitBranch();

    /** SHA hash in git for the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunGitSha();

    /** Whether docs were generated from the last run of the job that materialized this asset in dbt (true) or not (false). */
    Boolean getAssetDbtJobLastRunHasDocsGenerated();

    /** Whether sources were generated from the last run of the job that materialized this asset in dbt (true) or not (false). */
    Boolean getAssetDbtJobLastRunHasSourcesGenerated();

    /** Whether notifications were sent from the last run of the job that materialized this asset in dbt (true) or not (false). */
    Boolean getAssetDbtJobLastRunNotificationsSent();

    /** Thread ID of the owner of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunOwnerThreadId();

    /** Total duration the job that materialized this asset in dbt spent being queued. */
    String getAssetDbtJobLastRunQueuedDuration();

    /** Human-readable total duration of the last run of the job that materialized this asset in dbt spend being queued. */
    String getAssetDbtJobLastRunQueuedDurationHumanized();

    /** Run duration of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunRunDuration();

    /** Human-readable run duration of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunRunDurationHumanized();

    /** Time (epoch) at which the job that materialized this asset in dbt was started running, in milliseconds. */
    Long getAssetDbtJobLastRunStartedAt();

    /** Status message of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunStatusMessage();

    /** Total duration of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunTotalDuration();

    /** Human-readable total duration of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunTotalDurationHumanized();

    /** Time (epoch) at which the job that materialized this asset in dbt was last updated, in milliseconds. */
    Long getAssetDbtJobLastRunUpdatedAt();

    /** URL of the last run of the job that materialized this asset in dbt. */
    String getAssetDbtJobLastRunUrl();

    /** Name of the job that materialized this asset in dbt. */
    String getAssetDbtJobName();

    /** Time (epoch) when the next run of the job that materializes this asset in dbt is scheduled. */
    Long getAssetDbtJobNextRun();

    /** Human-readable time when the next run of the job that materializes this asset in dbt is scheduled. */
    String getAssetDbtJobNextRunHumanized();

    /** Schedule of the job that materialized this asset in dbt. */
    String getAssetDbtJobSchedule();

    /** Human-readable cron schedule of the job that materialized this asset in dbt. */
    String getAssetDbtJobScheduleCronHumanized();

    /** Status of the job that materialized this asset in dbt. */
    String getAssetDbtJobStatus();

    /** Metadata for this asset in dbt, specifically everything under the 'meta' key in the dbt object. */
    String getAssetDbtMeta();

    /** Name of the package in which this asset exists in dbt. */
    String getAssetDbtPackageName();

    /** Name of the project in which this asset exists in dbt. */
    String getAssetDbtProjectName();

    /** URL of the semantic layer proxy for this asset in dbt. */
    String getAssetDbtSemanticLayerProxyUrl();

    /** Freshness criteria for the source of this asset in dbt. */
    String getAssetDbtSourceFreshnessCriteria();

    /** List of tags attached to this asset in dbt. */
    SortedSet<String> getAssetDbtTags();

    /** All associated dbt test statuses. */
    String getAssetDbtTestStatus();

    /** Unique identifier of this asset in dbt. */
    String getAssetDbtUniqueId();

    /** Name of the DBT workflow in Atlan that last updated the asset. */
    String getAssetDbtWorkflowLastUpdated();

    /** Name of the icon to use for this asset. (Only applies to glossaries, currently.) */
    AtlanIcon getAssetIcon();

    /** List of Monte Carlo incident names attached to this asset. */
    SortedSet<String> getAssetMcIncidentNames();

    /** List of unique Monte Carlo incident names attached to this asset. */
    SortedSet<String> getAssetMcIncidentQualifiedNames();

    /** List of Monte Carlo incident severities associated with this asset. */
    SortedSet<String> getAssetMcIncidentSeverities();

    /** List of Monte Carlo incident states associated with this asset. */
    SortedSet<String> getAssetMcIncidentStates();

    /** List of Monte Carlo incident sub-types associated with this asset. */
    SortedSet<String> getAssetMcIncidentSubTypes();

    /** List of Monte Carlo incident types associated with this asset. */
    SortedSet<String> getAssetMcIncidentTypes();

    /** Time (epoch) at which this asset was last synced from Monte Carlo. */
    Long getAssetMcLastSyncRunAt();

    /** List of Monte Carlo monitor names attached to this asset. */
    SortedSet<String> getAssetMcMonitorNames();

    /** List of unique Monte Carlo monitor names attached to this asset. */
    SortedSet<String> getAssetMcMonitorQualifiedNames();

    /** Schedules of all associated Monte Carlo monitors. */
    SortedSet<String> getAssetMcMonitorScheduleTypes();

    /** Statuses of all associated Monte Carlo monitors. */
    SortedSet<String> getAssetMcMonitorStatuses();

    /** Types of all associated Monte Carlo monitors. */
    SortedSet<String> getAssetMcMonitorTypes();

    /** Count of policies inside the asset */
    Long getAssetPoliciesCount();

    /** Array of policy ids governing this asset */
    SortedSet<String> getAssetPolicyGUIDs();

    /** Number of checks done via Soda. */
    Long getAssetSodaCheckCount();

    /** All associated Soda check statuses. */
    String getAssetSodaCheckStatuses();

    /** Status of data quality from Soda. */
    String getAssetSodaDQStatus();

    /** TBC */
    Long getAssetSodaLastScanAt();

    /** TBC */
    Long getAssetSodaLastSyncRunAt();

    /** TBC */
    String getAssetSodaSourceURL();

    /** List of tags attached to this asset. */
    SortedSet<String> getAssetTags();

    /** Color (in hexadecimal RGB) to use to represent this asset. */
    String getAssetThemeHex();

    /** Glossary terms that are linked to this asset. */
    SortedSet<IGlossaryTerm> getAssignedTerms();

    /** Calculate view in which this column exists. */
    ICalculationView getCalculationView();

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    String getCalculationViewName();

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    String getCalculationViewQualifiedName();

    /** Status of this asset's certification. */
    CertificateStatus getCertificateStatus();

    /** Human-readable descriptive message used to provide further detail to certificateStatus. */
    String getCertificateStatusMessage();

    /** Time (epoch) at which the certification was last updated, in milliseconds. */
    Long getCertificateUpdatedAt();

    /** Name of the user who last updated the certification of this asset. */
    String getCertificateUpdatedBy();

    /** Average value in this column. */
    Double getColumnAverage();

    /** Average length of values in a string column. */
    Double getColumnAverageLength();

    /** TBC */
    SortedSet<IDbtModelColumn> getColumnDbtModelColumns();

    /** Level of nesting of this column, used for STRUCT and NESTED columns. */
    Integer getColumnDepthLevel();

    /** Number of rows that contain distinct values. */
    Integer getColumnDistinctValuesCount();

    /** Number of rows that contain distinct values. */
    Long getColumnDistinctValuesCountLong();

    /** Number of rows that contain duplicate values. */
    Integer getColumnDuplicateValuesCount();

    /** Number of rows that contain duplicate values. */
    Long getColumnDuplicateValuesCountLong();

    /** List of values in a histogram that represents the contents of this column. */
    Histogram getColumnHistogram();

    /** Greatest value in a numeric column. */
    Double getColumnMax();

    /** Length of the longest value in a string column. */
    Integer getColumnMaximumStringLength();

    /** List of the greatest values in a column. */
    SortedSet<String> getColumnMaxs();

    /** Arithmetic mean of the values in a numeric column. */
    Double getColumnMean();

    /** Calculated median of the values in a numeric column. */
    Double getColumnMedian();

    /** Least value in a numeric column. */
    Double getColumnMin();

    /** Length of the shortest value in a string column. */
    Integer getColumnMinimumStringLength();

    /** List of the least values in a column. */
    SortedSet<String> getColumnMins();

    /** Number of rows in a column that do not contain content. */
    Integer getColumnMissingValuesCount();

    /** Number of rows in a column that do not contain content. */
    Long getColumnMissingValuesCountLong();

    /** Percentage of rows in a column that do not contain content. */
    Double getColumnMissingValuesPercentage();

    /** Calculated standard deviation of the values in a numeric column. */
    Double getColumnStandardDeviation();

    /** Calculated sum of the values in a numeric column. */
    Double getColumnSum();

    /** List of top values in this column. */
    List<ColumnValueFrequencyMap> getColumnTopValues();

    /** Number of rows in which a value in this column appears only once. */
    Integer getColumnUniqueValuesCount();

    /** Number of rows in which a value in this column appears only once. */
    Long getColumnUniqueValuesCountLong();

    /** Ratio indicating how unique data in this column is: 0 indicates that all values are the same, 100 indicates that all values in this column are unique. */
    Double getColumnUniquenessPercentage();

    /** Calculated variance of the values in a numeric column. */
    Double getColumnVariance();

    /** Simple name of the connection through which this asset is accessible. */
    String getConnectionName();

    /** Unique name of the connection through which this asset is accessible. */
    String getConnectionQualifiedName();

    /** Type of the connector through which this asset is accessible. */
    AtlanConnectorType getConnectorType();

    /** Latest version of the data contract (in any status) for this asset. */
    IDataContract getDataContractLatest();

    /** Latest certified version of the data contract for this asset. */
    IDataContract getDataContractLatestCertified();

    /** TBC */
    SortedSet<IMetric> getDataQualityMetricDimensions();

    /** Data type of values in this column. */
    String getDataType();

    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    String getDatabaseName();

    /** Unique name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    String getDatabaseQualifiedName();

    /** TBC */
    SortedSet<IDbtMetric> getDbtMetrics();

    /** TBC */
    SortedSet<IDbtModelColumn> getDbtModelColumns();

    /** TBC */
    SortedSet<IDbtModel> getDbtModels();

    /** Unique name of this asset in dbt. */
    String getDbtQualifiedName();

    /** TBC */
    SortedSet<IDbtSource> getDbtSources();

    /** TBC */
    SortedSet<IDbtTest> getDbtTests();

    /** Default value for this column. */
    String getDefaultValue();

    /** Description of this asset, for example as crawled from a source. Fallback for display purposes, if userDescription is empty. */
    String getDescription();

    /** Human-readable name of this asset used for display purposes (in user interface). */
    String getDisplayName();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** Column this foreign key column refers to. */
    IColumn getForeignKeyFrom();

    /** Columns that use this column as a foreign key. */
    SortedSet<IColumn> getForeignKeyTo();

    /** Whether this asset has contract (true) or not (false). */
    Boolean getHasContract();

    /** Whether this asset has lineage (true) or not (false). */
    Boolean getHasLineage();

    /** Data products for which this asset is an input port. */
    SortedSet<IDataProduct> getInputPortDataProducts();

    /** Tasks to which this asset provides input. */
    SortedSet<IAirflowTask> getInputToAirflowTasks();

    /** Processes to which this asset provides input. */
    SortedSet<ILineageProcess> getInputToProcesses();

    /** TBC */
    SortedSet<ISparkJob> getInputToSparkJobs();

    /** TBC */
    Boolean getIsAIGenerated();

    /** Whether this column is a clustered column (true) or not (false). */
    Boolean getIsClustered();

    /** Whether this asset is discoverable through the UI (true) or not (false). */
    Boolean getIsDiscoverable();

    /** Whether this column is a distribution column (true) or not (false). */
    Boolean getIsDist();

    /** Whether this asset can be edited in the UI (true) or not (false). */
    Boolean getIsEditable();

    /** When true, this column is a foreign key to another table. NOTE: this must be true when using the foreignKeyTo relationship to specify columns that refer to this column as a foreign key. */
    Boolean getIsForeign();

    /** When true, this column is indexed in the database. */
    Boolean getIsIndexed();

    /** When true, the values in this column can be null. */
    Boolean getIsNullable();

    /** TBC */
    Boolean getIsPartial();

    /** Whether this column is a partition column (true) or not (false). */
    Boolean getIsPartition();

    /** Whether this column is pinned (true) or not (false). */
    Boolean getIsPinned();

    /** When true, this column is the primary key for the table. */
    Boolean getIsPrimary();

    /** Whether this asset has been profiled (true) or not (false). */
    Boolean getIsProfiled();

    /** Whether this column is a sort column (true) or not (false). */
    Boolean getIsSort();

    /** Time (epoch) at which this asset was last profiled, in milliseconds. */
    Long getLastProfiledAt();

    /** Time (epoch) of the last operation that inserted, updated, or deleted rows, in milliseconds. */
    Long getLastRowChangedAt();

    /** Name of the last run of the crawler that last synchronized this asset. */
    String getLastSyncRun();

    /** Time (epoch) at which this asset was last crawled, in milliseconds. */
    Long getLastSyncRunAt();

    /** Name of the crawler that last synchronized this asset. */
    String getLastSyncWorkflowName();

    /** Links that are attached to this asset. */
    SortedSet<ILink> getLinks();

    /** Materialized view in which this column exists. */
    IMaterializedView getMaterializedView();

    /** Maximum length of a value in this column. */
    Long getMaxLength();

    /** TBC */
    SortedSet<IMCIncident> getMcIncidents();

    /** Monitors that observe this asset. */
    SortedSet<IMCMonitor> getMcMonitors();

    /** TBC */
    SortedSet<IMetric> getMetricTimestamps();

    /** TBC */
    SortedSet<IMetric> getMetrics();

    /** Name of this asset. Fallback for display purposes, if displayName is empty. */
    String getName();

    /** Number of columns nested within this (STRUCT or NESTED) column. */
    Integer getNestedColumnCount();

    /** Nested columns that exist within this column. */
    SortedSet<IColumn> getNestedColumns();

    /** Number of digits allowed to the right of the decimal point. */
    Double getNumericScale();

    /** Order (position) in which this column appears in the table (starting at 1). */
    Integer getOrder();

    /** Tasks from which this asset is output. */
    SortedSet<IAirflowTask> getOutputFromAirflowTasks();

    /** Processes from which this asset is produced as output. */
    SortedSet<ILineageProcess> getOutputFromProcesses();

    /** TBC */
    SortedSet<ISparkJob> getOutputFromSparkJobs();

    /** Data products for which this asset is an output port. */
    SortedSet<IDataProduct> getOutputPortDataProducts();

    /** List of groups who own this asset. */
    SortedSet<String> getOwnerGroups();

    /** List of users who own this asset. */
    SortedSet<String> getOwnerUsers();

    /** Column in which this sub-column is nested. */
    IColumn getParentColumn();

    /** Simple name of the column this column is nested within, for STRUCT and NESTED columns. */
    String getParentColumnName();

    /** Unique name of the column this column is nested within, for STRUCT and NESTED columns. */
    String getParentColumnQualifiedName();

    /** Order (position) of this partition column in the table. */
    Integer getPartitionOrder();

    /** Time (epoch) at which this column was pinned, in milliseconds. */
    Long getPinnedAt();

    /** User who pinned this column. */
    String getPinnedBy();

    /** Popularity score for this asset. */
    Double getPopularityScore();

    /** Total number of digits allowed, when the dataType is numeric. */
    Integer getPrecision();

    /** Unique name for this asset. This is typically a concatenation of the asset's name onto its parent's qualifiedName. This must be unique across all assets of the same type. */
    String getQualifiedName();

    /** Queries that access this column. */
    SortedSet<IAtlanQuery> getQueries();

    /** Number of times this asset has been queried. */
    Long getQueryCount();

    /** Time (epoch) at which the query count was last updated, in milliseconds. */
    Long getQueryCountUpdatedAt();

    /** Number of unique users who have queried this asset. */
    Long getQueryUserCount();

    /** Map of unique users who have queried this asset to the number of times they have queried it. */
    Map<String, Long> getQueryUserMap();

    /** TBC */
    String getRawDataTypeDefinition();

    /** README that is linked to this asset. */
    IReadme getReadme();

    /** URL for sample data for this asset. */
    String getSampleDataUrl();

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    String getSchemaName();

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    String getSchemaQualifiedName();

    /** TBC */
    SortedSet<ISchemaRegistrySubject> getSchemaRegistrySubjects();

    /** Snowflake dynamic table in which this column exists. */
    ISnowflakeDynamicTable getSnowflakeDynamicTable();

    /** TBC */
    SortedSet<ISodaCheck> getSodaChecks();

    /** The unit of measure for sourceTotalCost. */
    SourceCostUnitType getSourceCostUnit();

    /** Time (epoch) at which this asset was created in the source system, in milliseconds. */
    Long getSourceCreatedAt();

    /** Name of the user who created this asset, in the source system. */
    String getSourceCreatedBy();

    /** URL to create an embed for a resource (for example, an image of a dashboard) within Atlan. */
    String getSourceEmbedURL();

    /** Timestamp of most recent read operation. */
    Long getSourceLastReadAt();

    /** List of owners of this asset, in the source system. */
    String getSourceOwners();

    /** List of most expensive warehouses with extra insights. */
    List<PopularityInsights> getSourceQueryComputeCostRecords();

    /** List of most expensive warehouse names. */
    SortedSet<String> getSourceQueryComputeCosts();

    /** Total count of all read operations at source. */
    Long getSourceReadCount();

    /** List of the most expensive queries that accessed this asset. */
    List<PopularityInsights> getSourceReadExpensiveQueryRecords();

    /** List of the most popular queries that accessed this asset. */
    List<PopularityInsights> getSourceReadPopularQueryRecords();

    /** Total cost of read queries at source. */
    Double getSourceReadQueryCost();

    /** List of usernames with extra insights for the most recent users who read this asset. */
    List<PopularityInsights> getSourceReadRecentUserRecords();

    /** List of usernames of the most recent users who read this asset. */
    SortedSet<String> getSourceReadRecentUsers();

    /** List of the slowest queries that accessed this asset. */
    List<PopularityInsights> getSourceReadSlowQueryRecords();

    /** List of usernames with extra insights for the users who read this asset the most. */
    List<PopularityInsights> getSourceReadTopUserRecords();

    /** List of usernames of the users who read this asset the most. */
    SortedSet<String> getSourceReadTopUsers();

    /** Total number of unique users that read data from asset. */
    Long getSourceReadUserCount();

    /** Total cost of all operations at source. */
    Double getSourceTotalCost();

    /** URL to the resource within the source application, used to create a button to view this asset in the source application. */
    String getSourceURL();

    /** Time (epoch) at which this asset was last updated in the source system, in milliseconds. */
    Long getSourceUpdatedAt();

    /** Name of the user who last updated this asset, in the source system. */
    String getSourceUpdatedBy();

    /** TBC */
    SortedSet<IDbtSource> getSqlDBTSources();

    /** TBC */
    SortedSet<IDbtModel> getSqlDbtModels();

    /** Users who have starred this asset. */
    SortedSet<String> getStarredBy();

    /** Number of users who have starred this asset. */
    Integer getStarredCount();

    /** List of usernames with extra information of the users who have starred an asset. */
    List<StarredDetails> getStarredDetails();

    /** Sub-data type of this column. */
    String getSubDataType();

    /** Subtype of this asset. */
    String getSubType();

    /** Table in which this column exists. */
    ITable getTable();

    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    String getTableName();

    /** Table partition that contains this column. */
    ITablePartition getTablePartition();

    /** Unique name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    String getTableQualifiedName();

    /** Name of the Atlan workspace in which this asset exists. */
    String getTenantId();

    /** Description of this asset, as provided by a user. If present, this will be used for the description in user interface. */
    String getUserDescription();

    /** Validations for this column. */
    Map<String, String> getValidations();

    /** View in which this column exists. */
    IView getView();

    /** Simple name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    String getViewName();

    /** Unique name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    String getViewQualifiedName();

    /** View score for this asset. */
    Double getViewScore();

    /** List of groups who can view assets contained in a collection. (This is only used for certain asset types.) */
    SortedSet<String> getViewerGroups();

    /** List of users who can view assets contained in a collection. (This is only used for certain asset types.) */
    SortedSet<String> getViewerUsers();

    /** Name of the type that defines the asset. */
    String getTypeName();

    /** Globally-unique identifier for the asset. */
    String getGuid();

    /** Human-readable name of the asset. */
    String getDisplayText();

    /** Status of the asset (if this is a related asset). */
    String getEntityStatus();

    /** Type of the relationship (if this is a related asset). */
    String getRelationshipType();

    /** Unique identifier of the relationship (when this is a related asset). */
    String getRelationshipGuid();

    /** Status of the relationship (when this is a related asset). */
    AtlanStatus getRelationshipStatus();

    /** Attributes specific to the relationship (unused). */
    Map<String, Object> getRelationshipAttributes();

    /**
     * Attribute(s) that uniquely identify the asset (when this is a related asset).
     * If the guid is not provided, these must be provided.
     */
    UniqueAttributes getUniqueAttributes();

    /**
     * When true, indicates that this object represents a complete view of the entity.
     * When false, this object is only a reference or some partial view of the entity.
     */
    boolean isComplete();

    /**
     * Indicates whether this object can be used as a valid reference by GUID.
     * @return true if it is a valid GUID reference, false otherwise
     */
    boolean isValidReferenceByGuid();

    /**
     * Indicates whether this object can be used as a valid reference by qualifiedName.
     * @return true if it is a valid qualifiedName reference, false otherwise
     */
    boolean isValidReferenceByQualifiedName();
}
