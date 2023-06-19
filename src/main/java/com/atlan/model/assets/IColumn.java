/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.ColumnValueFrequencyMap;
import com.atlan.model.structs.Histogram;
import com.atlan.model.structs.PopularityInsights;
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
    String getAssetDbtUniqueId();

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
    SortedSet<String> getAssetTags();

    /** TBC */
    SortedSet<IGlossaryTerm> getAssignedTerms();

    /** TBC */
    CertificateStatus getCertificateStatus();

    /** TBC */
    String getCertificateStatusMessage();

    /** TBC */
    Long getCertificateUpdatedAt();

    /** TBC */
    String getCertificateUpdatedBy();

    /** TBC */
    Double getColumnAverage();

    /** Average length of values in a string column. */
    Double getColumnAverageLength();

    /** TBC */
    SortedSet<IDbtModelColumn> getColumnDbtModelColumns();

    /** Level of nesting, used for STRUCT/NESTED columns */
    Integer getColumnDepthLevel();

    /** Number of rows that contain distinct values. */
    Integer getColumnDistinctValuesCount();

    /** TBC */
    Long getColumnDistinctValuesCountLong();

    /** Number of rows that contain duplicate values. */
    Integer getColumnDuplicateValuesCount();

    /** TBC */
    Long getColumnDuplicateValuesCountLong();

    /** List of values in a histogram that represents the contents of the column. */
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

    /** TBC */
    Long getColumnMissingValuesCountLong();

    /** Percentage of rows in a column that do not contain content. */
    Double getColumnMissingValuesPercentage();

    /** Calculated standard deviation of the values in a numeric column. */
    Double getColumnStandardDeviation();

    /** Calculated sum of the values in a numeric column. */
    Double getColumnSum();

    /** TBC */
    List<ColumnValueFrequencyMap> getColumnTopValues();

    /** Number of rows in which a value in this column appears only once. */
    Integer getColumnUniqueValuesCount();

    /** TBC */
    Long getColumnUniqueValuesCountLong();

    /** Ratio indicating how unique data in the column is: 0 indicates that all values are the same, 100 indicates that all values in the column are unique. */
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

    /** Data type of values in the column. */
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
    String getDefaultValue();

    /** TBC */
    String getDescription();

    /** TBC */
    String getDisplayName();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** Column this column refers to as a foreign key. */
    IColumn getForeignKeyFrom();

    /** All the columns that refer to this column as a foreign key. NOTE: when providing values to this relationship, isForeign must also be set to true. */
    SortedSet<IColumn> getForeignKeyTo();

    /** TBC */
    Boolean getHasLineage();

    /** TBC */
    SortedSet<ILineageProcess> getInputToProcesses();

    /** TBC */
    Boolean getIsClustered();

    /** TBC */
    Boolean getIsDiscoverable();

    /** TBC */
    Boolean getIsDist();

    /** TBC */
    Boolean getIsEditable();

    /** When true, this column is a foreign key to another table. NOTE: this must be true when using the foreignKeyTo relationship to specify columns that refer to this column as a foreign key. */
    Boolean getIsForeign();

    /** When true, this column is indexed in the database. */
    Boolean getIsIndexed();

    /** When true, the values in this column can be null. */
    Boolean getIsNullable();

    /** TBC */
    Boolean getIsPartition();

    /** TBC */
    Boolean getIsPinned();

    /** When true, this column is the primary key for the table. */
    Boolean getIsPrimary();

    /** TBC */
    Boolean getIsProfiled();

    /** TBC */
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

    /** TBC */
    SortedSet<ILink> getLinks();

    /** Materialized view in which this column exists, or empty if the column instead exists in a table or view. */
    IMaterializedView getMaterializedView();

    /** Maximum length of a value in this column. */
    Long getMaxLength();

    /** TBC */
    SortedSet<IMCIncident> getMcIncidents();

    /** TBC */
    SortedSet<IMCMonitor> getMcMonitors();

    /** TBC */
    SortedSet<IMetric> getMetricTimestamps();

    /** TBC */
    SortedSet<IMetric> getMetrics();

    /** TBC */
    String getName();

    /** TBC */
    Integer getNestedColumnCount();

    /** TBC */
    SortedSet<IColumn> getNestedColumns();

    /** Number of digits allowed to the right of the decimal point. */
    Double getNumericScale();

    /** Order (position) in which the column appears in the table (starting at 1). */
    Integer getOrder();

    /** TBC */
    SortedSet<ILineageProcess> getOutputFromProcesses();

    /** TBC */
    SortedSet<String> getOwnerGroups();

    /** TBC */
    SortedSet<String> getOwnerUsers();

    /** TBC */
    IColumn getParentColumn();

    /** TBC */
    String getParentColumnName();

    /** TBC */
    String getParentColumnQualifiedName();

    /** TBC */
    Integer getPartitionOrder();

    /** TBC */
    Long getPinnedAt();

    /** TBC */
    String getPinnedBy();

    /** TBC */
    Double getPopularityScore();

    /** Total number of digits allowed when the dataType is numeric. */
    Integer getPrecision();

    /** TBC */
    String getQualifiedName();

    /** Queries that involve this column. */
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

    /** TBC */
    IReadme getReadme();

    /** TBC */
    String getSampleDataUrl();

    /** TBC */
    String getSchemaName();

    /** TBC */
    String getSchemaQualifiedName();

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
    String getSubDataType();

    /** TBC */
    String getSubType();

    /** Table in which this column exists, or empty if the column instead exists in a view or materialized view. */
    ITable getTable();

    /** TBC */
    String getTableName();

    /** TBC */
    ITablePartition getTablePartition();

    /** TBC */
    String getTableQualifiedName();

    /** TBC */
    String getTenantId();

    /** TBC */
    String getUserDescription();

    /** TBC */
    Map<String, String> getValidations();

    /** View in which this column exists, or empty if the column instead exists in a table or materialized view. */
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
