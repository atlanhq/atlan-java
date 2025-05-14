/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.AIDatasetType;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.model.fields.KeywordField;
import com.atlan.model.fields.RelationField;
import com.atlan.model.fields.TextField;
import com.atlan.model.relations.RelationshipAttributes;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.PopularityInsights;
import com.atlan.model.structs.StarredDetails;
import com.atlan.serde.AssetDeserializer;
import com.atlan.serde.AssetSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;

/**
 * Instance of a lineage process in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface ILineageProcess {

    public static final String TYPE_NAME = "Process";

    /** TBC */
    TextField ADDITIONAL_ETL_CONTEXT = new TextField("additionalEtlContext", "additionalEtlContext");

    /** TBC */
    RelationField ADF_ACTIVITY = new RelationField("adfActivity");

    /** TBC */
    KeywordField AI_DATASET_TYPE = new KeywordField("aiDatasetType", "aiDatasetType");

    /** TBC */
    RelationField AIRFLOW_TASKS = new RelationField("airflowTasks");

    /** TBC */
    TextField AST = new TextField("ast", "ast");

    /** TBC */
    TextField CODE = new TextField("code", "code");

    /** TBC */
    RelationField COLUMN_PROCESSES = new RelationField("columnProcesses");

    /** TBC */
    RelationField FIVETRAN_CONNECTOR = new RelationField("fivetranConnector");

    /** TBC */
    KeywordField INPUTS = new KeywordField("inputs", "inputs");

    /** TBC */
    RelationField MATILLION_COMPONENT = new RelationField("matillionComponent");

    /** TBC */
    KeywordField OUTPUTS = new KeywordField("outputs", "outputs");

    /** TBC */
    KeywordField PARENT_CONNECTION_PROCESS_QUALIFIED_NAMES =
            new KeywordField("parentConnectionProcessQualifiedName", "parentConnectionProcessQualifiedName");

    /** TBC */
    RelationField POWER_BI_DATAFLOW = new RelationField("powerBIDataflow");

    /** TBC */
    RelationField SPARK_JOBS = new RelationField("sparkJobs");

    /** TBC */
    TextField SQL = new TextField("sql", "sql");

    /** TBC */
    String getAdditionalEtlContext();

    /** TBC */
    IAdfActivity getAdfActivity();

    /** TBC */
    SortedSet<String> getAdminGroups();

    /** TBC */
    SortedSet<String> getAdminRoles();

    /** TBC */
    SortedSet<String> getAdminUsers();

    /** TBC */
    AIDatasetType getAiDatasetType();

    /** TBC */
    SortedSet<IAirflowTask> getAirflowTasks();

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
    SortedSet<IAnomaloCheck> getAnomaloChecks();

    /** TBC */
    IApplication getApplication();

    /** TBC */
    IApplicationField getApplicationField();

    /** TBC */
    String getApplicationFieldQualifiedName();

    /** TBC */
    String getApplicationQualifiedName();

    /** TBC */
    SortedSet<String> getAssetAnomaloAppliedCheckTypes();

    /** TBC */
    Long getAssetAnomaloCheckCount();

    /** TBC */
    String getAssetAnomaloCheckStatuses();

    /** TBC */
    String getAssetAnomaloDQStatus();

    /** TBC */
    Long getAssetAnomaloFailedCheckCount();

    /** TBC */
    SortedSet<String> getAssetAnomaloFailedCheckTypes();

    /** TBC */
    Long getAssetAnomaloLastCheckRunAt();

    /** TBC */
    String getAssetAnomaloSourceUrl();

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
    String getAssetDbtWorkflowLastUpdated();

    /** TBC */
    AtlanIcon getAssetIcon();

    /** TBC */
    SortedSet<String> getAssetMcAlertQualifiedNames();

    /** TBC */
    SortedSet<String> getAssetMcIncidentNames();

    /** TBC */
    SortedSet<String> getAssetMcIncidentPriorities();

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
    Boolean getAssetMcIsMonitored();

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
    Long getAssetPoliciesCount();

    /** TBC */
    SortedSet<String> getAssetPolicyGUIDs();

    /** TBC */
    SortedSet<String> getAssetRedirectGUIDs();

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

    /** TBC */
    String getAssetThemeHex();

    /** TBC */
    SortedSet<IGlossaryTerm> getAssignedTerms();

    /** TBC */
    String getAst();

    /** TBC */
    CertificateStatus getCertificateStatus();

    /** TBC */
    String getCertificateStatusMessage();

    /** TBC */
    Long getCertificateUpdatedAt();

    /** TBC */
    String getCertificateUpdatedBy();

    /** TBC */
    String getCode();

    /** TBC */
    SortedSet<IColumnProcess> getColumnProcesses();

    /** TBC */
    String getConnectionName();

    /** TBC */
    String getConnectionQualifiedName();

    /** TBC */
    String getConnectorName();

    /** TBC */
    IDataContract getDataContractLatest();

    /** TBC */
    IDataContract getDataContractLatestCertified();

    /** TBC */
    String getDbtQualifiedName();

    /** TBC */
    String getDescription();

    /** TBC */
    String getDisplayName();

    /** TBC */
    SortedSet<String> getDomainGUIDs();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** TBC */
    IFivetranConnector getFivetranConnector();

    /** TBC */
    Boolean getHasContract();

    /** TBC */
    Boolean getHasLineage();

    /** TBC */
    SortedSet<IDataProduct> getInputPortDataProducts();

    /** TBC */
    SortedSet<ICatalog> getInputs();

    /** TBC */
    Boolean getIsAIGenerated();

    /** TBC */
    Boolean getIsDiscoverable();

    /** TBC */
    Boolean getIsEditable();

    /** TBC */
    Boolean getIsPartial();

    /** TBC */
    Long getLastRowChangedAt();

    /** TBC */
    String getLastSyncRun();

    /** TBC */
    Long getLastSyncRunAt();

    /** TBC */
    String getLastSyncWorkflowName();

    /** TBC */
    String getLexicographicalSortOrder();

    /** TBC */
    SortedSet<ILink> getLinks();

    /** TBC */
    IMatillionComponent getMatillionComponent();

    /** TBC */
    SortedSet<IMCIncident> getMcIncidents();

    /** TBC */
    SortedSet<IMCMonitor> getMcMonitors();

    /** TBC */
    SortedSet<IMetric> getMetrics();

    /** TBC */
    String getName();

    /** TBC */
    SortedSet<String> getNonCompliantAssetPolicyGUIDs();

    /** TBC */
    SortedSet<IDataProduct> getOutputPortDataProducts();

    /** TBC */
    SortedSet<String> getOutputProductGUIDs();

    /** TBC */
    SortedSet<ICatalog> getOutputs();

    /** TBC */
    SortedSet<String> getOwnerGroups();

    /** TBC */
    SortedSet<String> getOwnerUsers();

    /** TBC */
    SortedSet<String> getParentConnectionProcessQualifiedNames();

    /** TBC */
    Double getPopularityScore();

    /** TBC */
    IPowerBIDataflow getPowerBIDataflow();

    /** TBC */
    SortedSet<String> getProductGUIDs();

    /** TBC */
    String getQualifiedName();

    /** TBC */
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
    SortedSet<ISparkJob> getSparkJobs();

    /** TBC */
    String getSql();

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
    SortedSet<IAsset> getUserDefRelationshipFroms();

    /** TBC */
    SortedSet<IAsset> getUserDefRelationshipTos();

    /** TBC */
    String getUserDescription();

    /** TBC */
    Double getViewScore();

    /** TBC */
    SortedSet<String> getViewerGroups();

    /** TBC */
    SortedSet<String> getViewerUsers();

    /** URL of an icon to use for this asset. (Only applies to CustomEntity and Fivetran Catalog assets, currently.) */
    String getIconUrl();

    /** Built-in connector type through which this asset is accessible. */
    AtlanConnectorType getConnectorType();

    /** Custom connector type through which this asset is accessible. */
    String getCustomConnectorType();

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
    RelationshipAttributes getRelationshipAttributes();

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
