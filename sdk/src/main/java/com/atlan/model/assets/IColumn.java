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

    /** TBC */
    SortedSet<String> getAdminGroups();

    /** TBC */
    SortedSet<String> getAdminRoles();

    /** TBC */
    SortedSet<String> getAdminUsers();

    /** TBC */
    String getAnnouncementMessage();

    /** TBC */
    String getAnnouncementTitle();

    /** TBC */
    AtlanAnnouncementType getAnnouncementType();

    /** TBC */
    Long getAnnouncementUpdatedAt();

    /** TBC */
    String getAnnouncementUpdatedBy();

    /** TBC */
    String getAssetDbtAccountName();

    /** TBC */
    String getAssetDbtAlias();

    /** TBC */
    String getAssetDbtEnvironmentDbtVersion();

    /** TBC */
    String getAssetDbtEnvironmentName();

    /** TBC */
    Long getAssetDbtJobLastRun();

    /** TBC */
    String getAssetDbtJobLastRunArtifactS3Path();

    /** TBC */
    Boolean getAssetDbtJobLastRunArtifactsSaved();

    /** TBC */
    Long getAssetDbtJobLastRunCreatedAt();

    /** TBC */
    Long getAssetDbtJobLastRunDequedAt();

    /** TBC */
    String getAssetDbtJobLastRunExecutedByThreadId();

    /** TBC */
    String getAssetDbtJobLastRunGitBranch();

    /** TBC */
    String getAssetDbtJobLastRunGitSha();

    /** TBC */
    Boolean getAssetDbtJobLastRunHasDocsGenerated();

    /** TBC */
    Boolean getAssetDbtJobLastRunHasSourcesGenerated();

    /** TBC */
    Boolean getAssetDbtJobLastRunNotificationsSent();

    /** TBC */
    String getAssetDbtJobLastRunOwnerThreadId();

    /** TBC */
    String getAssetDbtJobLastRunQueuedDuration();

    /** TBC */
    String getAssetDbtJobLastRunQueuedDurationHumanized();

    /** TBC */
    String getAssetDbtJobLastRunRunDuration();

    /** TBC */
    String getAssetDbtJobLastRunRunDurationHumanized();

    /** TBC */
    Long getAssetDbtJobLastRunStartedAt();

    /** TBC */
    String getAssetDbtJobLastRunStatusMessage();

    /** TBC */
    String getAssetDbtJobLastRunTotalDuration();

    /** TBC */
    String getAssetDbtJobLastRunTotalDurationHumanized();

    /** TBC */
    Long getAssetDbtJobLastRunUpdatedAt();

    /** TBC */
    String getAssetDbtJobLastRunUrl();

    /** TBC */
    String getAssetDbtJobName();

    /** TBC */
    Long getAssetDbtJobNextRun();

    /** TBC */
    String getAssetDbtJobNextRunHumanized();

    /** TBC */
    String getAssetDbtJobSchedule();

    /** TBC */
    String getAssetDbtJobScheduleCronHumanized();

    /** TBC */
    String getAssetDbtJobStatus();

    /** TBC */
    String getAssetDbtMeta();

    /** TBC */
    String getAssetDbtPackageName();

    /** TBC */
    String getAssetDbtProjectName();

    /** TBC */
    String getAssetDbtSemanticLayerProxyUrl();

    /** TBC */
    String getAssetDbtSourceFreshnessCriteria();

    /** TBC */
    SortedSet<String> getAssetDbtTags();

    /** TBC */
    String getAssetDbtTestStatus();

    /** TBC */
    String getAssetDbtUniqueId();

    /** TBC */
    AtlanIcon getAssetIcon();

    /** TBC */
    SortedSet<String> getAssetMcIncidentNames();

    /** TBC */
    SortedSet<String> getAssetMcIncidentQualifiedNames();

    /** TBC */
    SortedSet<String> getAssetMcIncidentSeverities();

    /** TBC */
    SortedSet<String> getAssetMcIncidentStates();

    /** TBC */
    SortedSet<String> getAssetMcIncidentSubTypes();

    /** TBC */
    SortedSet<String> getAssetMcIncidentTypes();

    /** TBC */
    Long getAssetMcLastSyncRunAt();

    /** TBC */
    SortedSet<String> getAssetMcMonitorNames();

    /** TBC */
    SortedSet<String> getAssetMcMonitorQualifiedNames();

    /** TBC */
    SortedSet<String> getAssetMcMonitorScheduleTypes();

    /** TBC */
    SortedSet<String> getAssetMcMonitorStatuses();

    /** TBC */
    SortedSet<String> getAssetMcMonitorTypes();

    /** TBC */
    Long getAssetSodaCheckCount();

    /** TBC */
    String getAssetSodaCheckStatuses();

    /** TBC */
    String getAssetSodaDQStatus();

    /** TBC */
    Long getAssetSodaLastScanAt();

    /** TBC */
    Long getAssetSodaLastSyncRunAt();

    /** TBC */
    String getAssetSodaSourceURL();

    /** TBC */
    SortedSet<String> getAssetTags();

    /** Glossary terms that are linked to this asset. */
    SortedSet<IGlossaryTerm> getAssignedTerms();

    /** TBC */
    CertificateStatus getCertificateStatus();

    /** TBC */
    String getCertificateStatusMessage();

    /** TBC */
    Long getCertificateUpdatedAt();

    /** TBC */
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
    List<Histogram> getColumnHistogram();

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

    /** TBC */
    String getConnectionName();

    /** TBC */
    String getConnectionQualifiedName();

    /** TBC */
    AtlanConnectorType getConnectorType();

    /** TBC */
    SortedSet<IMetric> getDataQualityMetricDimensions();

    /** Data type of values in this column. */
    String getDataType();

    /** TBC */
    String getDatabaseName();

    /** TBC */
    String getDatabaseQualifiedName();

    /** TBC */
    SortedSet<IDbtMetric> getDbtMetrics();

    /** TBC */
    SortedSet<IDbtModelColumn> getDbtModelColumns();

    /** TBC */
    SortedSet<IDbtModel> getDbtModels();

    /** TBC */
    String getDbtQualifiedName();

    /** TBC */
    SortedSet<IDbtSource> getDbtSources();

    /** TBC */
    SortedSet<IDbtTest> getDbtTests();

    /** Default value for this column. */
    String getDefaultValue();

    /** TBC */
    String getDescription();

    /** TBC */
    String getDisplayName();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** Column this foreign key column refers to. */
    IColumn getForeignKeyFrom();

    /** Columns that use this column as a foreign key. */
    SortedSet<IColumn> getForeignKeyTo();

    /** TBC */
    Boolean getHasLineage();

    /** Tasks to which this asset provides input. */
    SortedSet<IAirflowTask> getInputToAirflowTasks();

    /** Processes to which this asset provides input. */
    SortedSet<ILineageProcess> getInputToProcesses();

    /** TBC */
    Boolean getIsAIGenerated();

    /** Whether this column is a clustered column (true) or not (false). */
    Boolean getIsClustered();

    /** TBC */
    Boolean getIsDiscoverable();

    /** Whether this column is a distribution column (true) or not (false). */
    Boolean getIsDist();

    /** TBC */
    Boolean getIsEditable();

    /** When true, this column is a foreign key to another table. NOTE: this must be true when using the foreignKeyTo relationship to specify columns that refer to this column as a foreign key. */
    Boolean getIsForeign();

    /** When true, this column is indexed in the database. */
    Boolean getIsIndexed();

    /** When true, the values in this column can be null. */
    Boolean getIsNullable();

    /** Whether this column is a partition column (true) or not (false). */
    Boolean getIsPartition();

    /** Whether this column is pinned (true) or not (false). */
    Boolean getIsPinned();

    /** When true, this column is the primary key for the table. */
    Boolean getIsPrimary();

    /** TBC */
    Boolean getIsProfiled();

    /** Whether this column is a sort column (true) or not (false). */
    Boolean getIsSort();

    /** TBC */
    Long getLastProfiledAt();

    /** TBC */
    Long getLastRowChangedAt();

    /** TBC */
    String getLastSyncRun();

    /** TBC */
    Long getLastSyncRunAt();

    /** TBC */
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

    /** TBC */
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
    SortedSet<String> getOwnerGroups();

    /** TBC */
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

    /** TBC */
    Double getPopularityScore();

    /** Total number of digits allowed, when the dataType is numeric. */
    Integer getPrecision();

    /** TBC */
    String getQualifiedName();

    /** Queries that access this column. */
    SortedSet<IAtlanQuery> getQueries();

    /** TBC */
    Long getQueryCount();

    /** TBC */
    Long getQueryCountUpdatedAt();

    /** TBC */
    Long getQueryUserCount();

    /** TBC */
    Map<String, Long> getQueryUserMap();

    /** TBC */
    String getRawDataTypeDefinition();

    /** README that is linked to this asset. */
    IReadme getReadme();

    /** TBC */
    String getSampleDataUrl();

    /** TBC */
    String getSchemaName();

    /** TBC */
    String getSchemaQualifiedName();

    /** TBC */
    SortedSet<ISchemaRegistrySubject> getSchemaRegistrySubjects();

    /** Snowflake dynamic table in which this column exists. */
    ISnowflakeDynamicTable getSnowflakeDynamicTable();

    /** TBC */
    SortedSet<ISodaCheck> getSodaChecks();

    /** TBC */
    SourceCostUnitType getSourceCostUnit();

    /** TBC */
    Long getSourceCreatedAt();

    /** TBC */
    String getSourceCreatedBy();

    /** TBC */
    String getSourceEmbedURL();

    /** TBC */
    Long getSourceLastReadAt();

    /** TBC */
    String getSourceOwners();

    /** TBC */
    List<PopularityInsights> getSourceQueryComputeCostRecords();

    /** TBC */
    SortedSet<String> getSourceQueryComputeCosts();

    /** TBC */
    Long getSourceReadCount();

    /** TBC */
    List<PopularityInsights> getSourceReadExpensiveQueryRecords();

    /** TBC */
    List<PopularityInsights> getSourceReadPopularQueryRecords();

    /** TBC */
    Double getSourceReadQueryCost();

    /** TBC */
    List<PopularityInsights> getSourceReadRecentUserRecords();

    /** TBC */
    SortedSet<String> getSourceReadRecentUsers();

    /** TBC */
    List<PopularityInsights> getSourceReadSlowQueryRecords();

    /** TBC */
    List<PopularityInsights> getSourceReadTopUserRecords();

    /** TBC */
    SortedSet<String> getSourceReadTopUsers();

    /** TBC */
    Long getSourceReadUserCount();

    /** TBC */
    Double getSourceTotalCost();

    /** TBC */
    String getSourceURL();

    /** TBC */
    Long getSourceUpdatedAt();

    /** TBC */
    String getSourceUpdatedBy();

    /** TBC */
    SortedSet<IDbtSource> getSqlDBTSources();

    /** TBC */
    SortedSet<IDbtModel> getSqlDbtModels();

    /** TBC */
    SortedSet<String> getStarredBy();

    /** TBC */
    Integer getStarredCount();

    /** TBC */
    List<StarredDetails> getStarredDetails();

    /** Sub-data type of this column. */
    String getSubDataType();

    /** TBC */
    String getSubType();

    /** Table in which this column exists. */
    ITable getTable();

    /** TBC */
    String getTableName();

    /** Table partition that contains this column. */
    ITablePartition getTablePartition();

    /** TBC */
    String getTableQualifiedName();

    /** TBC */
    String getTenantId();

    /** TBC */
    String getUserDescription();

    /** Validations for this column. */
    Map<String, String> getValidations();

    /** View in which this column exists. */
    IView getView();

    /** TBC */
    String getViewName();

    /** TBC */
    String getViewQualifiedName();

    /** TBC */
    Double getViewScore();

    /** TBC */
    SortedSet<String> getViewerGroups();

    /** TBC */
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
