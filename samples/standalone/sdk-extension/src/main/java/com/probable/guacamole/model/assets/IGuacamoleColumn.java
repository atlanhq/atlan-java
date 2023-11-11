/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.probable.guacamole.model.assets;

import com.atlan.model.assets.IAtlanQuery;
import com.atlan.model.assets.IColumn;
import com.atlan.model.assets.IGlossaryTerm;
import com.atlan.model.assets.ILineageProcess;
import com.atlan.model.assets.IMaterializedView;
import com.atlan.model.assets.ITable;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.SourceCostUnitType;
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
 * Specialized form of a column specific to Guacamole.
 */
@Generated(value = "com.probable.guacamole.generators.POJOGenerator")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface IGuacamoleColumn {

    public static final String TYPE_NAME = "GuacamoleColumn";

    /** Time (epoch) when this column was imagined, in milliseconds. */
    NumericField GUACAMOLE_CONCEPTUALIZED = new NumericField("guacamoleConceptualized", "guacamoleConceptualized");

    /** Specialized table that contains this specialized column. */
    RelationField GUACAMOLE_TABLE = new RelationField("guacamoleTable");

    /** Maximum size of a Guacamole column. */
    NumericField GUACAMOLE_WIDTH = new NumericField("guacamoleWidth", "guacamoleWidth");

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

    /** TBC */
    Double getColumnAverage();

    /** TBC */
    Double getColumnAverageLength();

    /** TBC */
    SortedSet<IDbtModelColumn> getColumnDbtModelColumns();

    /** TBC */
    Integer getColumnDepthLevel();

    /** TBC */
    Integer getColumnDistinctValuesCount();

    /** TBC */
    Long getColumnDistinctValuesCountLong();

    /** TBC */
    Integer getColumnDuplicateValuesCount();

    /** TBC */
    Long getColumnDuplicateValuesCountLong();

    /** TBC */
    List<Histogram> getColumnHistogram();

    /** TBC */
    Double getColumnMax();

    /** TBC */
    Integer getColumnMaximumStringLength();

    /** TBC */
    SortedSet<String> getColumnMaxs();

    /** TBC */
    Double getColumnMean();

    /** TBC */
    Double getColumnMedian();

    /** TBC */
    Double getColumnMin();

    /** TBC */
    Integer getColumnMinimumStringLength();

    /** TBC */
    SortedSet<String> getColumnMins();

    /** TBC */
    Integer getColumnMissingValuesCount();

    /** TBC */
    Long getColumnMissingValuesCountLong();

    /** TBC */
    Double getColumnMissingValuesPercentage();

    /** TBC */
    Double getColumnStandardDeviation();

    /** TBC */
    Double getColumnSum();

    /** TBC */
    List<ColumnValueFrequencyMap> getColumnTopValues();

    /** TBC */
    Integer getColumnUniqueValuesCount();

    /** TBC */
    Long getColumnUniqueValuesCountLong();

    /** TBC */
    Double getColumnUniquenessPercentage();

    /** TBC */
    Double getColumnVariance();

    /** TBC */
    String getConnectionName();

    /** TBC */
    String getConnectionQualifiedName();

    /** TBC */
    AtlanConnectorType getConnectorType();

    /** TBC */
    SortedSet<IMetric> getDataQualityMetricDimensions();

    /** TBC */
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

    /** TBC */
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

    /** Time (epoch) when this column was imagined, in milliseconds. */
    Long getGuacamoleConceptualized();

    /** Specialized table that contains this specialized column. */
    IGuacamoleTable getGuacamoleTable();

    /** Maximum size of a Guacamole column. */
    Long getGuacamoleWidth();

    /** TBC */
    Boolean getHasLineage();

    /** TBC */
    SortedSet<IAirflowTask> getInputToAirflowTasks();

    /** Processes to which this asset provides input. */
    SortedSet<ILineageProcess> getInputToProcesses();

    /** TBC */
    Boolean getIsAIGenerated();

    /** TBC */
    Boolean getIsClustered();

    /** TBC */
    Boolean getIsDiscoverable();

    /** TBC */
    Boolean getIsDist();

    /** TBC */
    Boolean getIsEditable();

    /** TBC */
    Boolean getIsForeign();

    /** TBC */
    Boolean getIsIndexed();

    /** TBC */
    Boolean getIsNullable();

    /** TBC */
    Boolean getIsPartition();

    /** TBC */
    Boolean getIsPinned();

    /** TBC */
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

    /** Links that are attached to this asset. */
    SortedSet<ILink> getLinks();

    /** Materialized view in which this column exists. */
    IMaterializedView getMaterializedView();

    /** TBC */
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

    /** TBC */
    Integer getNestedColumnCount();

    /** Nested columns that exist within this column. */
    SortedSet<IColumn> getNestedColumns();

    /** TBC */
    Double getNumericScale();

    /** TBC */
    Integer getOrder();

    /** TBC */
    SortedSet<IAirflowTask> getOutputFromAirflowTasks();

    /** Processes from which this asset is produced as output. */
    SortedSet<ILineageProcess> getOutputFromProcesses();

    /** TBC */
    SortedSet<String> getOwnerGroups();

    /** TBC */
    SortedSet<String> getOwnerUsers();

    /** Column in which this sub-column is nested. */
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

    /** TBC */
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

    /** TBC */
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

    /** TBC */
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
