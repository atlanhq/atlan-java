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
import com.atlan.model.fields.NumericField;
import com.atlan.model.fields.RelationField;
import com.atlan.model.relations.UniqueAttributes;
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
 * Instance of a Snowflake stream in Atlan. These are used to capture changes in a table.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface ISnowflakeStream {

    public static final String TYPE_NAME = "SnowflakeStream";

    /** Schema in which this Snowflake stream exists. */
    RelationField SCHEMA = new RelationField("atlanSchema");

    /** Whether this stream is stale (true) or not (false). */
    BooleanField SNOWFLAKE_STREAM_IS_STALE = new BooleanField("snowflakeStreamIsStale", "snowflakeStreamIsStale");

    /** Mode of this stream. */
    KeywordField SNOWFLAKE_STREAM_MODE = new KeywordField("snowflakeStreamMode", "snowflakeStreamMode");

    /** Type of the source of this stream. */
    KeywordField SNOWFLAKE_STREAM_SOURCE_TYPE =
            new KeywordField("snowflakeStreamSourceType", "snowflakeStreamSourceType");

    /** Time (epoch) after which this stream will be stale, in milliseconds. */
    NumericField SNOWFLAKE_STREAM_STALE_AFTER =
            new NumericField("snowflakeStreamStaleAfter", "snowflakeStreamStaleAfter");

    /** Type of this stream, for example: standard, append-only, insert-only, etc. */
    KeywordField SNOWFLAKE_STREAM_TYPE = new KeywordField("snowflakeStreamType", "snowflakeStreamType");

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
    String getAssetCoverImage();

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
    String getConnectionName();

    /** TBC */
    String getConnectionQualifiedName();

    /** TBC */
    AtlanConnectorType getConnectorType();

    /** TBC */
    String getDatabaseName();

    /** TBC */
    String getDatabaseQualifiedName();

    /** TBC */
    SortedSet<IDbtModel> getDbtModels();

    /** TBC */
    String getDbtQualifiedName();

    /** TBC */
    SortedSet<IDbtSource> getDbtSources();

    /** TBC */
    SortedSet<IDbtTest> getDbtTests();

    /** TBC */
    String getDescription();

    /** TBC */
    String getDisplayName();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** TBC */
    Boolean getHasLineage();

    /** Tasks to which this asset provides input. */
    SortedSet<IAirflowTask> getInputToAirflowTasks();

    /** Processes to which this asset provides input. */
    SortedSet<ILineageProcess> getInputToProcesses();

    /** TBC */
    Boolean getIsAIGenerated();

    /** TBC */
    Boolean getIsDiscoverable();

    /** TBC */
    Boolean getIsEditable();

    /** TBC */
    Boolean getIsPartial();

    /** TBC */
    Boolean getIsProfiled();

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

    /** TBC */
    SortedSet<IMCIncident> getMcIncidents();

    /** Monitors that observe this asset. */
    SortedSet<IMCMonitor> getMcMonitors();

    /** TBC */
    SortedSet<IMetric> getMetrics();

    /** TBC */
    String getName();

    /** Tasks from which this asset is output. */
    SortedSet<IAirflowTask> getOutputFromAirflowTasks();

    /** Processes from which this asset is produced as output. */
    SortedSet<ILineageProcess> getOutputFromProcesses();

    /** Data products for which this asset is an output port. */
    SortedSet<IDataProduct> getOutputPortDataProducts();

    /** TBC */
    SortedSet<String> getOwnerGroups();

    /** TBC */
    SortedSet<String> getOwnerUsers();

    /** TBC */
    Double getPopularityScore();

    /** TBC */
    String getQualifiedName();

    /** TBC */
    Long getQueryCount();

    /** TBC */
    Long getQueryCountUpdatedAt();

    /** TBC */
    Long getQueryUserCount();

    /** TBC */
    Map<String, Long> getQueryUserMap();

    /** README that is linked to this asset. */
    IReadme getReadme();

    /** TBC */
    String getSampleDataUrl();

    /** Schema in which this Snowflake stream exists. */
    ISchema getSchema();

    /** TBC */
    String getSchemaName();

    /** TBC */
    String getSchemaQualifiedName();

    /** TBC */
    SortedSet<ISchemaRegistrySubject> getSchemaRegistrySubjects();

    /** Whether this stream is stale (true) or not (false). */
    Boolean getSnowflakeStreamIsStale();

    /** Mode of this stream. */
    String getSnowflakeStreamMode();

    /** Type of the source of this stream. */
    String getSnowflakeStreamSourceType();

    /** Time (epoch) after which this stream will be stale, in milliseconds. */
    Long getSnowflakeStreamStaleAfter();

    /** Type of this stream, for example: standard, append-only, insert-only, etc. */
    String getSnowflakeStreamType();

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
    String getSubType();

    /** TBC */
    String getTableName();

    /** TBC */
    String getTableQualifiedName();

    /** TBC */
    String getTenantId();

    /** TBC */
    String getUserDescription();

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
