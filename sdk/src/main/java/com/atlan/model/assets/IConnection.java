/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.QueryUsernameStrategy;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.model.fields.BooleanField;
import com.atlan.model.fields.KeywordField;
import com.atlan.model.fields.NumericField;
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
 * Instance of a connection to a data source in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface IConnection {

    public static final String TYPE_NAME = "Connection";

    /** Whether using this connection to run queries on the source is allowed (true) or not (false). */
    BooleanField ALLOW_QUERY = new BooleanField("allowQuery", "allowQuery");

    /** Whether using this connection to run preview queries on the source is allowed (true) or not (false). */
    BooleanField ALLOW_QUERY_PREVIEW = new BooleanField("allowQueryPreview", "allowQueryPreview");

    /** Type of connection, for example WAREHOUSE, RDBMS, etc. */
    KeywordField CATEGORY = new KeywordField("category", "category");

    /** TBC */
    KeywordField CONNECTION_DBT_ENVIRONMENTS =
            new KeywordField("connectionDbtEnvironments", "connectionDbtEnvironments");

    /** Unique identifier (GUID) for the SSO credentials to use for this connection. */
    KeywordField CONNECTION_SSO_CREDENTIAL_GUID =
            new KeywordField("connectionSSOCredentialGuid", "connectionSSOCredentialGuid");

    /** Unused. Only the value of connectorType impacts icons. */
    KeywordField CONNECTOR_ICON = new KeywordField("connectorIcon", "connectorIcon");

    /** Unused. Only the value of connectorType impacts icons. */
    KeywordField CONNECTOR_IMAGE = new KeywordField("connectorImage", "connectorImage");

    /** Credential strategy to use for this connection for queries. */
    KeywordField CREDENTIAL_STRATEGY = new KeywordField("credentialStrategy", "credentialStrategy");

    /** Unique identifier (GUID) for the default credentials to use for this connection. */
    KeywordField DEFAULT_CREDENTIAL_GUID = new KeywordField("defaultCredentialGuid", "defaultCredentialGuid");

    /** Whether this connection has popularity insights (true) or not (false). */
    BooleanField HAS_POPULARITY_INSIGHTS = new BooleanField("hasPopularityInsights", "hasPopularityInsights");

    /** Host name of this connection's source. */
    KeywordField HOST = new KeywordField("host", "host");

    /** Whether sample data can be previewed for this connection (true) or not (false). */
    BooleanField IS_SAMPLE_DATA_PREVIEW_ENABLED =
            new BooleanField("isSampleDataPreviewEnabled", "isSampleDataPreviewEnabled");

    /** Number of rows after which results should be uploaded to storage. */
    NumericField OBJECT_STORAGE_UPLOAD_THRESHOLD =
            new NumericField("objectStorageUploadThreshold", "objectStorageUploadThreshold");

    /** Policy strategy to use for this connection. */
    KeywordField POLICY_STRATEGY = new KeywordField("policyStrategy", "policyStrategy");

    /** Number of days over which popularity is calculated, for example 30 days. */
    NumericField POPULARITY_INSIGHTS_TIMEFRAME =
            new NumericField("popularityInsightsTimeframe", "popularityInsightsTimeframe");

    /** Port number to this connection's source. */
    NumericField PORT = new NumericField("port", "port");

    /** Credential strategy to use for this connection for preview queries. */
    KeywordField PREVIEW_CREDENTIAL_STRATEGY =
            new KeywordField("previewCredentialStrategy", "previewCredentialStrategy");

    /** Query config for this connection. */
    KeywordField QUERY_CONFIG = new KeywordField("queryConfig", "queryConfig");

    /** Configuration for preview queries. */
    KeywordField QUERY_PREVIEW_CONFIG = new KeywordField("queryPreviewConfig", "queryPreviewConfig");

    /** Maximum time a query should be allowed to run before timing out. */
    NumericField QUERY_TIMEOUT = new NumericField("queryTimeout", "queryTimeout");

    /** Username strategy to use for this connection for queries. */
    KeywordField QUERY_USERNAME_STRATEGY = new KeywordField("queryUsernameStrategy", "queryUsernameStrategy");

    /** Maximum number of rows that can be returned for the source. */
    NumericField ROW_LIMIT = new NumericField("rowLimit", "rowLimit");

    /** Unused. Only the value of connectorType impacts icons. */
    KeywordField SOURCE_LOGO = new KeywordField("sourceLogo", "sourceLogo");

    /** Subcategory of this connection. */
    KeywordField SUB_CATEGORY = new KeywordField("subCategory", "subCategory");

    /** Whether to upload to S3, GCP, or another storage location (true) or not (false). */
    BooleanField USE_OBJECT_STORAGE = new BooleanField("useObjectStorage", "useObjectStorage");

    /** TBC */
    BooleanField VECTOR_EMBEDDINGS_ENABLED = new BooleanField("vectorEmbeddingsEnabled", "vectorEmbeddingsEnabled");

    /** TBC */
    SortedSet<String> getAdminGroups();

    /** TBC */
    SortedSet<String> getAdminRoles();

    /** TBC */
    SortedSet<String> getAdminUsers();

    /** Whether using this connection to run queries on the source is allowed (true) or not (false). */
    Boolean getAllowQuery();

    /** Whether using this connection to run preview queries on the source is allowed (true) or not (false). */
    Boolean getAllowQueryPreview();

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

    /** Type of connection, for example WAREHOUSE, RDBMS, etc. */
    AtlanConnectionCategory getCategory();

    /** TBC */
    CertificateStatus getCertificateStatus();

    /** TBC */
    String getCertificateStatusMessage();

    /** TBC */
    Long getCertificateUpdatedAt();

    /** TBC */
    String getCertificateUpdatedBy();

    /** TBC */
    SortedSet<String> getConnectionDbtEnvironments();

    /** TBC */
    String getConnectionName();

    /** TBC */
    String getConnectionQualifiedName();

    /** Unique identifier (GUID) for the SSO credentials to use for this connection. */
    String getConnectionSSOCredentialGuid();

    /** Unused. Only the value of connectorType impacts icons. */
    String getConnectorIcon();

    /** Unused. Only the value of connectorType impacts icons. */
    String getConnectorImage();

    /** TBC */
    AtlanConnectorType getConnectorType();

    /** Credential strategy to use for this connection for queries. */
    String getCredentialStrategy();

    /** TBC */
    String getDbtQualifiedName();

    /** Unique identifier (GUID) for the default credentials to use for this connection. */
    String getDefaultCredentialGuid();

    /** TBC */
    String getDescription();

    /** TBC */
    String getDisplayName();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** TBC */
    Boolean getHasLineage();

    /** Whether this connection has popularity insights (true) or not (false). */
    Boolean getHasPopularityInsights();

    /** Host name of this connection's source. */
    String getHost();

    /** TBC */
    Boolean getIsAIGenerated();

    /** TBC */
    Boolean getIsDiscoverable();

    /** TBC */
    Boolean getIsEditable();

    /** Whether sample data can be previewed for this connection (true) or not (false). */
    Boolean getIsSampleDataPreviewEnabled();

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

    /** Number of rows after which results should be uploaded to storage. */
    Long getObjectStorageUploadThreshold();

    /** TBC */
    SortedSet<String> getOwnerGroups();

    /** TBC */
    SortedSet<String> getOwnerUsers();

    /** Policy strategy to use for this connection. */
    String getPolicyStrategy();

    /** Number of days over which popularity is calculated, for example 30 days. */
    Long getPopularityInsightsTimeframe();

    /** TBC */
    Double getPopularityScore();

    /** Port number to this connection's source. */
    Integer getPort();

    /** Credential strategy to use for this connection for preview queries. */
    String getPreviewCredentialStrategy();

    /** TBC */
    String getQualifiedName();

    /** Query config for this connection. */
    String getQueryConfig();

    /** Configuration for preview queries. */
    Map<String, String> getQueryPreviewConfig();

    /** Maximum time a query should be allowed to run before timing out. */
    Long getQueryTimeout();

    /** Username strategy to use for this connection for queries. */
    QueryUsernameStrategy getQueryUsernameStrategy();

    /** README that is linked to this asset. */
    IReadme getReadme();

    /** Maximum number of rows that can be returned for the source. */
    Long getRowLimit();

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

    /** Unused. Only the value of connectorType impacts icons. */
    String getSourceLogo();

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

    /** Subcategory of this connection. */
    String getSubCategory();

    /** TBC */
    String getSubType();

    /** TBC */
    String getTenantId();

    /** Whether to upload to S3, GCP, or another storage location (true) or not (false). */
    Boolean getUseObjectStorage();

    /** TBC */
    String getUserDescription();

    /** TBC */
    Boolean getVectorEmbeddingsEnabled();

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
