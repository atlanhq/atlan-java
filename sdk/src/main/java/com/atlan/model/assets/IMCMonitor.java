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
import com.atlan.model.fields.TextField;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.MCRuleComparison;
import com.atlan.model.structs.MCRuleSchedule;
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
 * Instance of a Monte Carlo monitor in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface IMCMonitor {

    public static final String TYPE_NAME = "MCMonitor";

    /** Condition on which the monitor produces an alert. */
    TextField MC_MONITOR_ALERT_CONDITION = new TextField("mcMonitorAlertCondition", "mcMonitorAlertCondition");

    /** Assets impacted by this monitor. */
    RelationField MC_MONITOR_ASSETS = new RelationField("mcMonitorAssets");

    /** Rate at which this monitor is breached. */
    NumericField MC_MONITOR_BREACH_RATE = new NumericField("mcMonitorBreachRate", "mcMonitorBreachRate");

    /** Unique identifier for this monitor, from Monte Carlo. */
    KeywordField MC_MONITOR_ID = new KeywordField("mcMonitorId", "mcMonitorId");

    /** Number of incidents associated with this monitor. */
    NumericField MC_MONITOR_INCIDENT_COUNT = new NumericField("mcMonitorIncidentCount", "mcMonitorIncidentCount");

    /** Namespace of this monitor. */
    KeywordTextField MC_MONITOR_NAMESPACE =
            new KeywordTextField("mcMonitorNamespace", "mcMonitorNamespace.keyword", "mcMonitorNamespace");

    /** Comparison logic used for the rule. */
    KeywordField MC_MONITOR_RULE_COMPARISONS = new KeywordField("mcMonitorRuleComparisons", "mcMonitorRuleComparisons");

    /** SQL code for custom SQL rules. */
    KeywordField MC_MONITOR_RULE_CUSTOM_SQL = new KeywordField("mcMonitorRuleCustomSql", "mcMonitorRuleCustomSql");

    /** Whether the rule is currently snoozed (true) or not (false). */
    BooleanField MC_MONITOR_RULE_IS_SNOOZED = new BooleanField("mcMonitorRuleIsSnoozed", "mcMonitorRuleIsSnoozed");

    /** Time at which the next execution of the rule should occur. */
    NumericField MC_MONITOR_RULE_NEXT_EXECUTION_TIME =
            new NumericField("mcMonitorRuleNextExecutionTime", "mcMonitorRuleNextExecutionTime");

    /** Time at which the previous execution of the rule occurred. */
    NumericField MC_MONITOR_RULE_PREVIOUS_EXECUTION_TIME =
            new NumericField("mcMonitorRulePreviousExecutionTime", "mcMonitorRulePreviousExecutionTime");

    /** Schedule details for the rule. */
    KeywordField MC_MONITOR_RULE_SCHEDULE_CONFIG =
            new KeywordField("mcMonitorRuleScheduleConfig", "mcMonitorRuleScheduleConfig");

    /** Readable description of the schedule for the rule. */
    TextField MC_MONITOR_RULE_SCHEDULE_CONFIG_HUMANIZED =
            new TextField("mcMonitorRuleScheduleConfigHumanized", "mcMonitorRuleScheduleConfigHumanized");

    /** Type of rule for this monitor. */
    KeywordField MC_MONITOR_RULE_TYPE = new KeywordField("mcMonitorRuleType", "mcMonitorRuleType");

    /** Type of schedule for this monitor, for example: fixed or dynamic. */
    KeywordField MC_MONITOR_SCHEDULE_TYPE = new KeywordField("mcMonitorScheduleType", "mcMonitorScheduleType");

    /** Status of this monitor. */
    KeywordField MC_MONITOR_STATUS = new KeywordField("mcMonitorStatus", "mcMonitorStatus");

    /** Type of this monitor, for example: field health (stats) or dimension tracking (categories). */
    KeywordField MC_MONITOR_TYPE = new KeywordField("mcMonitorType", "mcMonitorType");

    /** Name of the warehouse for this monitor. */
    KeywordField MC_MONITOR_WAREHOUSE = new KeywordField("mcMonitorWarehouse", "mcMonitorWarehouse");

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
    String getConnectionName();

    /** TBC */
    String getConnectionQualifiedName();

    /** TBC */
    AtlanConnectorType getConnectorType();

    /** TBC */
    String getDbtQualifiedName();

    /** TBC */
    String getDescription();

    /** TBC */
    String getDisplayName();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** TBC */
    Boolean getHasLineage();

    /** TBC */
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
    SortedSet<String> getMcAssetQualifiedNames();

    /** Incidents that exist within this monitor. */
    SortedSet<IMCIncident> getMcIncidents();

    /** TBC */
    SortedSet<String> getMcLabels();

    /** Condition on which the monitor produces an alert. */
    String getMcMonitorAlertCondition();

    /** Assets impacted by this monitor. */
    SortedSet<IAsset> getMcMonitorAssets();

    /** Rate at which this monitor is breached. */
    Double getMcMonitorBreachRate();

    /** Unique identifier for this monitor, from Monte Carlo. */
    String getMcMonitorId();

    /** Number of incidents associated with this monitor. */
    Long getMcMonitorIncidentCount();

    /** Namespace of this monitor. */
    String getMcMonitorNamespace();

    /** Comparison logic used for the rule. */
    List<MCRuleComparison> getMcMonitorRuleComparisons();

    /** SQL code for custom SQL rules. */
    String getMcMonitorRuleCustomSql();

    /** Whether the rule is currently snoozed (true) or not (false). */
    Boolean getMcMonitorRuleIsSnoozed();

    /** Time at which the next execution of the rule should occur. */
    Long getMcMonitorRuleNextExecutionTime();

    /** Time at which the previous execution of the rule occurred. */
    Long getMcMonitorRulePreviousExecutionTime();

    /** Schedule details for the rule. */
    List<MCRuleSchedule> getMcMonitorRuleScheduleConfig();

    /** Readable description of the schedule for the rule. */
    String getMcMonitorRuleScheduleConfigHumanized();

    /** Type of rule for this monitor. */
    String getMcMonitorRuleType();

    /** Type of schedule for this monitor, for example: fixed or dynamic. */
    String getMcMonitorScheduleType();

    /** Status of this monitor. */
    String getMcMonitorStatus();

    /** Type of this monitor, for example: field health (stats) or dimension tracking (categories). */
    String getMcMonitorType();

    /** Name of the warehouse for this monitor. */
    String getMcMonitorWarehouse();

    /** Monitors that observe this asset. */
    SortedSet<IMCMonitor> getMcMonitors();

    /** TBC */
    SortedSet<IMetric> getMetrics();

    /** TBC */
    String getName();

    /** TBC */
    SortedSet<IAirflowTask> getOutputFromAirflowTasks();

    /** Processes from which this asset is produced as output. */
    SortedSet<ILineageProcess> getOutputFromProcesses();

    /** TBC */
    SortedSet<String> getOwnerGroups();

    /** TBC */
    SortedSet<String> getOwnerUsers();

    /** TBC */
    Double getPopularityScore();

    /** TBC */
    String getQualifiedName();

    /** README that is linked to this asset. */
    IReadme getReadme();

    /** TBC */
    String getSampleDataUrl();

    /** TBC */
    SortedSet<ISchemaRegistrySubject> getSchemaRegistrySubjects();

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
    SortedSet<String> getStarredBy();

    /** TBC */
    Integer getStarredCount();

    /** TBC */
    List<StarredDetails> getStarredDetails();

    /** TBC */
    String getSubType();

    /** TBC */
    String getTenantId();

    /** TBC */
    String getUserDescription();

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
