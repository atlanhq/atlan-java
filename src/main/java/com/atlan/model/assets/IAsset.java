/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.model.fields.BooleanField;
import com.atlan.model.fields.KeywordField;
import com.atlan.model.fields.KeywordTextField;
import com.atlan.model.fields.KeywordTextStemmedField;
import com.atlan.model.fields.NumericField;
import com.atlan.model.fields.NumericRankField;
import com.atlan.model.fields.RelationField;
import com.atlan.model.fields.SearchableRelationship;
import com.atlan.model.fields.TextField;
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
 * Base class for all assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface IAsset {

    /** List of groups who administer the asset. (This is only used for Connection assets.) */
    KeywordField ADMIN_GROUPS = new KeywordField("adminGroups", "adminGroups");

    /** List of roles who administer the asset. (This is only used for Connection assets.) */
    KeywordField ADMIN_ROLES = new KeywordField("adminRoles", "adminRoles");

    /** List of users who administer the asset. (This is only used for Connection assets.) */
    KeywordField ADMIN_USERS = new KeywordField("adminUsers", "adminUsers");

    /** Detailed message to include in the announcement on this asset. */
    KeywordField ANNOUNCEMENT_MESSAGE = new KeywordField("announcementMessage", "announcementMessage");

    /** Brief title for the announcement on this asset. Required when announcementType is specified. */
    KeywordField ANNOUNCEMENT_TITLE = new KeywordField("announcementTitle", "announcementTitle");

    /** Type of announcement on the asset. */
    KeywordField ANNOUNCEMENT_TYPE = new KeywordField("announcementType", "announcementType");

    /** Time (epoch) at which the announcement was last updated, in milliseconds. */
    NumericField ANNOUNCEMENT_UPDATED_AT = new NumericField("announcementUpdatedAt", "announcementUpdatedAt");

    /** Name of the user who last updated the announcement. */
    KeywordField ANNOUNCEMENT_UPDATED_BY = new KeywordField("announcementUpdatedBy", "announcementUpdatedBy");

    /** TBC */
    KeywordTextField ASSET_DBT_ACCOUNT_NAME =
            new KeywordTextField("assetDbtAccountName", "assetDbtAccountName.keyword", "assetDbtAccountName");

    /** TBC */
    KeywordTextField ASSET_DBT_ALIAS = new KeywordTextField("assetDbtAlias", "assetDbtAlias.keyword", "assetDbtAlias");

    /** TBC */
    KeywordField ASSET_DBT_ENVIRONMENT_DBT_VERSION =
            new KeywordField("assetDbtEnvironmentDbtVersion", "assetDbtEnvironmentDbtVersion");

    /** TBC */
    KeywordTextField ASSET_DBT_ENVIRONMENT_NAME = new KeywordTextField(
            "assetDbtEnvironmentName", "assetDbtEnvironmentName.keyword", "assetDbtEnvironmentName");

    /** TBC */
    NumericField ASSET_DBT_JOB_LAST_RUN = new NumericField("assetDbtJobLastRun", "assetDbtJobLastRun");

    /** TBC */
    KeywordField ASSET_DBT_JOB_LAST_RUN_ARTIFACT_S3PATH =
            new KeywordField("assetDbtJobLastRunArtifactS3Path", "assetDbtJobLastRunArtifactS3Path");

    /** TBC */
    BooleanField ASSET_DBT_JOB_LAST_RUN_ARTIFACTS_SAVED =
            new BooleanField("assetDbtJobLastRunArtifactsSaved", "assetDbtJobLastRunArtifactsSaved");

    /** TBC */
    NumericField ASSET_DBT_JOB_LAST_RUN_CREATED_AT =
            new NumericField("assetDbtJobLastRunCreatedAt", "assetDbtJobLastRunCreatedAt");

    /** TBC */
    NumericField ASSET_DBT_JOB_LAST_RUN_DEQUED_AT =
            new NumericField("assetDbtJobLastRunDequedAt", "assetDbtJobLastRunDequedAt");

    /** TBC */
    KeywordField ASSET_DBT_JOB_LAST_RUN_EXECUTED_BY_THREAD_ID =
            new KeywordField("assetDbtJobLastRunExecutedByThreadId", "assetDbtJobLastRunExecutedByThreadId");

    /** TBC */
    KeywordTextField ASSET_DBT_JOB_LAST_RUN_GIT_BRANCH = new KeywordTextField(
            "assetDbtJobLastRunGitBranch", "assetDbtJobLastRunGitBranch", "assetDbtJobLastRunGitBranch.text");

    /** TBC */
    KeywordField ASSET_DBT_JOB_LAST_RUN_GIT_SHA =
            new KeywordField("assetDbtJobLastRunGitSha", "assetDbtJobLastRunGitSha");

    /** TBC */
    BooleanField ASSET_DBT_JOB_LAST_RUN_HAS_DOCS_GENERATED =
            new BooleanField("assetDbtJobLastRunHasDocsGenerated", "assetDbtJobLastRunHasDocsGenerated");

    /** TBC */
    BooleanField ASSET_DBT_JOB_LAST_RUN_HAS_SOURCES_GENERATED =
            new BooleanField("assetDbtJobLastRunHasSourcesGenerated", "assetDbtJobLastRunHasSourcesGenerated");

    /** TBC */
    BooleanField ASSET_DBT_JOB_LAST_RUN_NOTIFICATIONS_SENT =
            new BooleanField("assetDbtJobLastRunNotificationsSent", "assetDbtJobLastRunNotificationsSent");

    /** TBC */
    KeywordField ASSET_DBT_JOB_LAST_RUN_OWNER_THREAD_ID =
            new KeywordField("assetDbtJobLastRunOwnerThreadId", "assetDbtJobLastRunOwnerThreadId");

    /** TBC */
    KeywordField ASSET_DBT_JOB_LAST_RUN_QUEUED_DURATION =
            new KeywordField("assetDbtJobLastRunQueuedDuration", "assetDbtJobLastRunQueuedDuration");

    /** TBC */
    KeywordField ASSET_DBT_JOB_LAST_RUN_QUEUED_DURATION_HUMANIZED =
            new KeywordField("assetDbtJobLastRunQueuedDurationHumanized", "assetDbtJobLastRunQueuedDurationHumanized");

    /** TBC */
    KeywordField ASSET_DBT_JOB_LAST_RUN_RUN_DURATION =
            new KeywordField("assetDbtJobLastRunRunDuration", "assetDbtJobLastRunRunDuration");

    /** TBC */
    KeywordField ASSET_DBT_JOB_LAST_RUN_RUN_DURATION_HUMANIZED =
            new KeywordField("assetDbtJobLastRunRunDurationHumanized", "assetDbtJobLastRunRunDurationHumanized");

    /** TBC */
    NumericField ASSET_DBT_JOB_LAST_RUN_STARTED_AT =
            new NumericField("assetDbtJobLastRunStartedAt", "assetDbtJobLastRunStartedAt");

    /** TBC */
    KeywordTextField ASSET_DBT_JOB_LAST_RUN_STATUS_MESSAGE = new KeywordTextField(
            "assetDbtJobLastRunStatusMessage",
            "assetDbtJobLastRunStatusMessage.keyword",
            "assetDbtJobLastRunStatusMessage");

    /** TBC */
    KeywordField ASSET_DBT_JOB_LAST_RUN_TOTAL_DURATION =
            new KeywordField("assetDbtJobLastRunTotalDuration", "assetDbtJobLastRunTotalDuration");

    /** TBC */
    KeywordField ASSET_DBT_JOB_LAST_RUN_TOTAL_DURATION_HUMANIZED =
            new KeywordField("assetDbtJobLastRunTotalDurationHumanized", "assetDbtJobLastRunTotalDurationHumanized");

    /** TBC */
    NumericField ASSET_DBT_JOB_LAST_RUN_UPDATED_AT =
            new NumericField("assetDbtJobLastRunUpdatedAt", "assetDbtJobLastRunUpdatedAt");

    /** TBC */
    KeywordField ASSET_DBT_JOB_LAST_RUN_URL = new KeywordField("assetDbtJobLastRunUrl", "assetDbtJobLastRunUrl");

    /** TBC */
    KeywordTextField ASSET_DBT_JOB_NAME =
            new KeywordTextField("assetDbtJobName", "assetDbtJobName.keyword", "assetDbtJobName");

    /** TBC */
    NumericField ASSET_DBT_JOB_NEXT_RUN = new NumericField("assetDbtJobNextRun", "assetDbtJobNextRun");

    /** TBC */
    KeywordTextField ASSET_DBT_JOB_NEXT_RUN_HUMANIZED = new KeywordTextField(
            "assetDbtJobNextRunHumanized", "assetDbtJobNextRunHumanized.keyword", "assetDbtJobNextRunHumanized");

    /** TBC */
    KeywordField ASSET_DBT_JOB_SCHEDULE = new KeywordField("assetDbtJobSchedule", "assetDbtJobSchedule");

    /** TBC */
    TextField ASSET_DBT_JOB_SCHEDULE_CRON_HUMANIZED =
            new TextField("assetDbtJobScheduleCronHumanized", "assetDbtJobScheduleCronHumanized");

    /** TBC */
    KeywordField ASSET_DBT_JOB_STATUS = new KeywordField("assetDbtJobStatus", "assetDbtJobStatus");

    /** TBC */
    KeywordField ASSET_DBT_META = new KeywordField("assetDbtMeta", "assetDbtMeta");

    /** TBC */
    KeywordTextField ASSET_DBT_PACKAGE_NAME =
            new KeywordTextField("assetDbtPackageName", "assetDbtPackageName.keyword", "assetDbtPackageName");

    /** TBC */
    KeywordTextField ASSET_DBT_PROJECT_NAME =
            new KeywordTextField("assetDbtProjectName", "assetDbtProjectName.keyword", "assetDbtProjectName");

    /** TBC */
    KeywordField ASSET_DBT_SEMANTIC_LAYER_PROXY_URL =
            new KeywordField("assetDbtSemanticLayerProxyUrl", "assetDbtSemanticLayerProxyUrl");

    /** TBC */
    KeywordField ASSET_DBT_SOURCE_FRESHNESS_CRITERIA =
            new KeywordField("assetDbtSourceFreshnessCriteria", "assetDbtSourceFreshnessCriteria");

    /** TBC */
    KeywordTextField ASSET_DBT_TAGS = new KeywordTextField("assetDbtTags", "assetDbtTags", "assetDbtTags.text");

    /** All associated dbt test statuses */
    KeywordField ASSET_DBT_TEST_STATUS = new KeywordField("assetDbtTestStatus", "assetDbtTestStatus");

    /** TBC */
    KeywordTextField ASSET_DBT_UNIQUE_ID =
            new KeywordTextField("assetDbtUniqueId", "assetDbtUniqueId.keyword", "assetDbtUniqueId");

    /** TBC */
    KeywordField ASSET_ICON = new KeywordField("assetIcon", "assetIcon");

    /** TBC */
    KeywordTextField ASSET_MC_INCIDENT_NAMES =
            new KeywordTextField("assetMcIncidentNames", "assetMcIncidentNames.keyword", "assetMcIncidentNames");

    /** TBC */
    KeywordTextField ASSET_MC_INCIDENT_QUALIFIED_NAMES = new KeywordTextField(
            "assetMcIncidentQualifiedNames", "assetMcIncidentQualifiedNames", "assetMcIncidentQualifiedNames.text");

    /** TBC */
    KeywordField ASSET_MC_INCIDENT_SEVERITIES =
            new KeywordField("assetMcIncidentSeverities", "assetMcIncidentSeverities");

    /** TBC */
    KeywordField ASSET_MC_INCIDENT_STATES = new KeywordField("assetMcIncidentStates", "assetMcIncidentStates");

    /** TBC */
    KeywordField ASSET_MC_INCIDENT_SUB_TYPES = new KeywordField("assetMcIncidentSubTypes", "assetMcIncidentSubTypes");

    /** TBC */
    KeywordField ASSET_MC_INCIDENT_TYPES = new KeywordField("assetMcIncidentTypes", "assetMcIncidentTypes");

    /** TBC */
    NumericField ASSET_MC_LAST_SYNC_RUN_AT = new NumericField("assetMcLastSyncRunAt", "assetMcLastSyncRunAt");

    /** TBC */
    KeywordTextField ASSET_MC_MONITOR_NAMES =
            new KeywordTextField("assetMcMonitorNames", "assetMcMonitorNames.keyword", "assetMcMonitorNames");

    /** TBC */
    KeywordTextField ASSET_MC_MONITOR_QUALIFIED_NAMES = new KeywordTextField(
            "assetMcMonitorQualifiedNames", "assetMcMonitorQualifiedNames", "assetMcMonitorQualifiedNames.text");

    /** Schedules of all associated Monte Carlo monitors. */
    KeywordField ASSET_MC_MONITOR_SCHEDULE_TYPES =
            new KeywordField("assetMcMonitorScheduleTypes", "assetMcMonitorScheduleTypes");

    /** Statuses of all associated Monte Carlo monitors. */
    KeywordField ASSET_MC_MONITOR_STATUSES = new KeywordField("assetMcMonitorStatuses", "assetMcMonitorStatuses");

    /** Types of all associated Monte Carlo monitors. */
    KeywordField ASSET_MC_MONITOR_TYPES = new KeywordField("assetMcMonitorTypes", "assetMcMonitorTypes");

    /** Soda check count */
    NumericField ASSET_SODA_CHECK_COUNT = new NumericField("assetSodaCheckCount", "assetSodaCheckCount");

    /** All associated soda check statuses */
    KeywordField ASSET_SODA_CHECK_STATUSES = new KeywordField("assetSodaCheckStatuses", "assetSodaCheckStatuses");

    /** Soda DQ Status */
    KeywordField ASSET_SODA_DQ_STATUS = new KeywordField("assetSodaDQStatus", "assetSodaDQStatus");

    /** TBC */
    NumericField ASSET_SODA_LAST_SCAN_AT = new NumericField("assetSodaLastScanAt", "assetSodaLastScanAt");

    /** TBC */
    NumericField ASSET_SODA_LAST_SYNC_RUN_AT = new NumericField("assetSodaLastSyncRunAt", "assetSodaLastSyncRunAt");

    /** TBC */
    KeywordField ASSET_SODA_SOURCE_URL = new KeywordField("assetSodaSourceURL", "assetSodaSourceURL");

    /** TBC */
    KeywordTextField ASSET_TAGS = new KeywordTextField("assetTags", "assetTags", "assetTags.text");

    /** Status of the asset's certification. */
    KeywordTextField CERTIFICATE_STATUS =
            new KeywordTextField("certificateStatus", "certificateStatus", "certificateStatus.text");

    /** Human-readable descriptive message that can optionally be submitted when the certificateStatus is changed. */
    KeywordField CERTIFICATE_STATUS_MESSAGE = new KeywordField("certificateStatusMessage", "certificateStatusMessage");

    /** Time (epoch) at which the certification was last updated, in milliseconds. */
    NumericField CERTIFICATE_UPDATED_AT = new NumericField("certificateUpdatedAt", "certificateUpdatedAt");

    /** Name of the user who last updated the certification of the asset. */
    KeywordField CERTIFICATE_UPDATED_BY = new KeywordField("certificateUpdatedBy", "certificateUpdatedBy");

    /** TBC */
    KeywordTextField CONNECTION_NAME = new KeywordTextField("connectionName", "connectionName", "connectionName.text");

    /** Unique name of the connection through which this asset is accessible. */
    KeywordTextField CONNECTION_QUALIFIED_NAME =
            new KeywordTextField("connectionQualifiedName", "connectionQualifiedName", "connectionQualifiedName.text");

    /** Type of the connector through which this asset is accessible. */
    KeywordField CONNECTOR_TYPE = new KeywordField("connectorName", "connectorName");

    /** TBC */
    KeywordTextField DBT_QUALIFIED_NAME =
            new KeywordTextField("dbtQualifiedName", "dbtQualifiedName", "dbtQualifiedName.text");

    /** Description of the asset, as crawled from a source. */
    KeywordTextField DESCRIPTION = new KeywordTextField("description", "description.keyword", "description");

    /** Name used for display purposes (in user interfaces). */
    KeywordTextField DISPLAY_NAME = new KeywordTextField("displayName", "displayName.keyword", "displayName");

    /** TBC */
    RelationField FILES = new RelationField("files");

    /** Indicates whether this asset has lineage (true) or not. */
    BooleanField HAS_LINEAGE = new BooleanField("__hasLineage", "__hasLineage");

    /** TBC */
    BooleanField IS_DISCOVERABLE = new BooleanField("isDiscoverable", "isDiscoverable");

    /** TBC */
    BooleanField IS_EDITABLE = new BooleanField("isEditable", "isEditable");

    /** Timestamp of last operation that inserted, updated, or deleted rows. */
    NumericField LAST_ROW_CHANGED_AT = new NumericField("lastRowChangedAt", "lastRowChangedAt");

    /** Name of the last run of the crawler that last synchronized this asset. */
    KeywordField LAST_SYNC_RUN = new KeywordField("lastSyncRun", "lastSyncRun");

    /** Time (epoch) at which the asset was last crawled, in milliseconds. */
    NumericField LAST_SYNC_RUN_AT = new NumericField("lastSyncRunAt", "lastSyncRunAt");

    /** Name of the crawler that last synchronized this asset. */
    KeywordField LAST_SYNC_WORKFLOW_NAME = new KeywordField("lastSyncWorkflowName", "lastSyncWorkflowName");

    /** Resources that are linked to this asset. */
    SearchableRelationship LINKS = new SearchableRelationship("links", "asset_links");

    /** TBC */
    RelationField MC_INCIDENTS = new RelationField("mcIncidents");

    /** TBC */
    RelationField MC_MONITORS = new RelationField("mcMonitors");

    /** TBC */
    RelationField METRICS = new RelationField("metrics");

    /** Human-readable name of the asset. */
    KeywordTextStemmedField NAME = new KeywordTextStemmedField("name", "name.keyword", "name", "name.stemmed");

    /** List of groups who own the asset. */
    KeywordField OWNER_GROUPS = new KeywordField("ownerGroups", "ownerGroups");

    /** List of users who own the asset. */
    KeywordField OWNER_USERS = new KeywordField("ownerUsers", "ownerUsers");

    /** TBC */
    NumericRankField POPULARITY_SCORE =
            new NumericRankField("popularityScore", "popularityScore", "popularityScore.rank_feature");

    /** README that is linked to this asset. */
    SearchableRelationship README = new SearchableRelationship("readme", "asset_readme");

    /** TBC */
    KeywordTextField SAMPLE_DATA_URL = new KeywordTextField("sampleDataUrl", "sampleDataUrl", "sampleDataUrl.text");

    /** TBC */
    RelationField SCHEMA_REGISTRY_SUBJECTS = new RelationField("schemaRegistrySubjects");

    /** TBC */
    RelationField SODA_CHECKS = new RelationField("sodaChecks");

    /** The unit of measure for sourceTotalCost. */
    KeywordField SOURCE_COST_UNIT = new KeywordField("sourceCostUnit", "sourceCostUnit");

    /** Time (epoch) at which the asset was created in the source system, in milliseconds. */
    NumericField SOURCE_CREATED_AT = new NumericField("sourceCreatedAt", "sourceCreatedAt");

    /** Who created the asset, in the source system. */
    KeywordField SOURCE_CREATED_BY = new KeywordField("sourceCreatedBy", "sourceCreatedBy");

    /** URL to create an embed for a resource (for example, an image of a dashboard) within Atlan. */
    KeywordField SOURCE_EMBED_URL = new KeywordField("sourceEmbedURL", "sourceEmbedURL");

    /** Timestamp of most recent read operation. */
    NumericField SOURCE_LAST_READ_AT = new NumericField("sourceLastReadAt", "sourceLastReadAt");

    /** TBC */
    KeywordField SOURCE_OWNERS = new KeywordField("sourceOwners", "sourceOwners");

    /** List of most expensive warehouses with extra insights. */
    KeywordField SOURCE_QUERY_COMPUTE_COST_RECORDS =
            new KeywordField("sourceQueryComputeCostRecordList", "sourceQueryComputeCostRecordList");

    /** List of most expensive warehouse names. */
    KeywordField SOURCE_QUERY_COMPUTE_COSTS =
            new KeywordField("sourceQueryComputeCostList", "sourceQueryComputeCostList");

    /** Total count of all read operations at source. */
    NumericField SOURCE_READ_COUNT = new NumericField("sourceReadCount", "sourceReadCount");

    /** List of the most expensive queries that accessed this asset. */
    KeywordField SOURCE_READ_EXPENSIVE_QUERY_RECORDS =
            new KeywordField("sourceReadExpensiveQueryRecordList", "sourceReadExpensiveQueryRecordList");

    /** List of the most popular queries that accessed this asset. */
    KeywordField SOURCE_READ_POPULAR_QUERY_RECORDS =
            new KeywordField("sourceReadPopularQueryRecordList", "sourceReadPopularQueryRecordList");

    /** Total cost of read queries at source. */
    NumericField SOURCE_READ_QUERY_COST = new NumericField("sourceReadQueryCost", "sourceReadQueryCost");

    /** List of usernames with extra insights for the most recent users who read the asset. */
    KeywordField SOURCE_READ_RECENT_USER_RECORDS =
            new KeywordField("sourceReadRecentUserRecordList", "sourceReadRecentUserRecordList");

    /** List of usernames of the most recent users who read the asset. */
    KeywordField SOURCE_READ_RECENT_USERS = new KeywordField("sourceReadRecentUserList", "sourceReadRecentUserList");

    /** List of the slowest queries that accessed this asset. */
    KeywordField SOURCE_READ_SLOW_QUERY_RECORDS =
            new KeywordField("sourceReadSlowQueryRecordList", "sourceReadSlowQueryRecordList");

    /** List of usernames with extra insights for the users who read the asset the most. */
    KeywordField SOURCE_READ_TOP_USER_RECORDS =
            new KeywordField("sourceReadTopUserRecordList", "sourceReadTopUserRecordList");

    /** List of usernames of the users who read the asset the most. */
    KeywordField SOURCE_READ_TOP_USERS = new KeywordField("sourceReadTopUserList", "sourceReadTopUserList");

    /** Total number of unique users that read data from asset. */
    NumericField SOURCE_READ_USER_COUNT = new NumericField("sourceReadUserCount", "sourceReadUserCount");

    /** Total cost of all operations at source. */
    NumericField SOURCE_TOTAL_COST = new NumericField("sourceTotalCost", "sourceTotalCost");

    /** URL to the resource within the source application. */
    KeywordField SOURCE_URL = new KeywordField("sourceURL", "sourceURL");

    /** Time (epoch) at which the asset was last updated in the source system, in milliseconds. */
    NumericField SOURCE_UPDATED_AT = new NumericField("sourceUpdatedAt", "sourceUpdatedAt");

    /** Who last updated the asset in the source system. */
    KeywordField SOURCE_UPDATED_BY = new KeywordField("sourceUpdatedBy", "sourceUpdatedBy");

    /** Users who have starred this asset. */
    KeywordField STARRED_BY = new KeywordField("starredBy", "starredBy");

    /** TBC */
    NumericField STARRED_COUNT = new NumericField("starredCount", "starredCount");

    /** List of usernames with extra information of the users who have starred an asset */
    KeywordField STARRED_DETAILS = new KeywordField("starredDetailsList", "starredDetailsList");

    /** TBC */
    KeywordField SUB_TYPE = new KeywordField("subType", "subType");

    /** Name of the Atlan workspace in which the asset exists. */
    KeywordField TENANT_ID = new KeywordField("tenantId", "tenantId");

    /** Description of the asset, as provided by a user. If present, this will be used for the description in user interfaces. If not present, the description will be used. */
    KeywordTextField USER_DESCRIPTION =
            new KeywordTextField("userDescription", "userDescription.keyword", "userDescription");

    /** TBC */
    NumericRankField VIEW_SCORE = new NumericRankField("viewScore", "viewScore", "viewScore.rank_feature");

    /** TBC */
    KeywordField VIEWER_GROUPS = new KeywordField("viewerGroups", "viewerGroups");

    /** TBC */
    KeywordField VIEWER_USERS = new KeywordField("viewerUsers", "viewerUsers");

    /** Unique fully-qualified name of the asset in Atlan. */
    KeywordTextField QUALIFIED_NAME = new KeywordTextField("qualifiedName", "qualifiedName", "qualifiedName.text");

    /** List of groups who administer the asset. (This is only used for Connection assets.) */
    SortedSet<String> getAdminGroups();

    /** List of roles who administer the asset. (This is only used for Connection assets.) */
    SortedSet<String> getAdminRoles();

    /** List of users who administer the asset. (This is only used for Connection assets.) */
    SortedSet<String> getAdminUsers();

    /** Detailed message to include in the announcement on this asset. */
    String getAnnouncementMessage();

    /** Brief title for the announcement on this asset. Required when announcementType is specified. */
    String getAnnouncementTitle();

    /** Type of announcement on the asset. */
    AtlanAnnouncementType getAnnouncementType();

    /** Time (epoch) at which the announcement was last updated, in milliseconds. */
    Long getAnnouncementUpdatedAt();

    /** Name of the user who last updated the announcement. */
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

    /** All associated dbt test statuses */
    String getAssetDbtTestStatus();

    /** TBC */
    String getAssetDbtUniqueId();

    /** TBC */
    String getAssetIcon();

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

    /** Schedules of all associated Monte Carlo monitors. */
    SortedSet<String> getAssetMcMonitorScheduleTypes();

    /** Statuses of all associated Monte Carlo monitors. */
    SortedSet<String> getAssetMcMonitorStatuses();

    /** Types of all associated Monte Carlo monitors. */
    SortedSet<String> getAssetMcMonitorTypes();

    /** Soda check count */
    Long getAssetSodaCheckCount();

    /** All associated soda check statuses */
    String getAssetSodaCheckStatuses();

    /** Soda DQ Status */
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

    /** Status of the asset's certification. */
    CertificateStatus getCertificateStatus();

    /** Human-readable descriptive message that can optionally be submitted when the certificateStatus is changed. */
    String getCertificateStatusMessage();

    /** Time (epoch) at which the certification was last updated, in milliseconds. */
    Long getCertificateUpdatedAt();

    /** Name of the user who last updated the certification of the asset. */
    String getCertificateUpdatedBy();

    /** TBC */
    String getConnectionName();

    /** Unique name of the connection through which this asset is accessible. */
    String getConnectionQualifiedName();

    /** Type of the connector through which this asset is accessible. */
    AtlanConnectorType getConnectorType();

    /** TBC */
    String getDbtQualifiedName();

    /** Description of the asset, as crawled from a source. */
    String getDescription();

    /** Name used for display purposes (in user interfaces). */
    String getDisplayName();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** Indicates whether this asset has lineage (true) or not. */
    Boolean getHasLineage();

    /** TBC */
    Boolean getIsDiscoverable();

    /** TBC */
    Boolean getIsEditable();

    /** Timestamp of last operation that inserted, updated, or deleted rows. */
    Long getLastRowChangedAt();

    /** Name of the last run of the crawler that last synchronized this asset. */
    String getLastSyncRun();

    /** Time (epoch) at which the asset was last crawled, in milliseconds. */
    Long getLastSyncRunAt();

    /** Name of the crawler that last synchronized this asset. */
    String getLastSyncWorkflowName();

    /** Resources that are linked to this asset. */
    SortedSet<ILink> getLinks();

    /** TBC */
    SortedSet<IMCIncident> getMcIncidents();

    /** TBC */
    SortedSet<IMCMonitor> getMcMonitors();

    /** TBC */
    SortedSet<IMetric> getMetrics();

    /** Human-readable name of the asset. */
    String getName();

    /** List of groups who own the asset. */
    SortedSet<String> getOwnerGroups();

    /** List of users who own the asset. */
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

    /** The unit of measure for sourceTotalCost. */
    SourceCostUnitType getSourceCostUnit();

    /** Time (epoch) at which the asset was created in the source system, in milliseconds. */
    Long getSourceCreatedAt();

    /** Who created the asset, in the source system. */
    String getSourceCreatedBy();

    /** URL to create an embed for a resource (for example, an image of a dashboard) within Atlan. */
    String getSourceEmbedURL();

    /** Timestamp of most recent read operation. */
    Long getSourceLastReadAt();

    /** TBC */
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

    /** List of usernames with extra insights for the most recent users who read the asset. */
    List<PopularityInsights> getSourceReadRecentUserRecords();

    /** List of usernames of the most recent users who read the asset. */
    SortedSet<String> getSourceReadRecentUsers();

    /** List of the slowest queries that accessed this asset. */
    List<PopularityInsights> getSourceReadSlowQueryRecords();

    /** List of usernames with extra insights for the users who read the asset the most. */
    List<PopularityInsights> getSourceReadTopUserRecords();

    /** List of usernames of the users who read the asset the most. */
    SortedSet<String> getSourceReadTopUsers();

    /** Total number of unique users that read data from asset. */
    Long getSourceReadUserCount();

    /** Total cost of all operations at source. */
    Double getSourceTotalCost();

    /** URL to the resource within the source application. */
    String getSourceURL();

    /** Time (epoch) at which the asset was last updated in the source system, in milliseconds. */
    Long getSourceUpdatedAt();

    /** Who last updated the asset in the source system. */
    String getSourceUpdatedBy();

    /** Users who have starred this asset. */
    SortedSet<String> getStarredBy();

    /** TBC */
    Integer getStarredCount();

    /** List of usernames with extra information of the users who have starred an asset */
    List<StarredDetails> getStarredDetails();

    /** TBC */
    String getSubType();

    /** Name of the Atlan workspace in which the asset exists. */
    String getTenantId();

    /** Description of the asset, as provided by a user. If present, this will be used for the description in user interfaces. If not present, the description will be used. */
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
