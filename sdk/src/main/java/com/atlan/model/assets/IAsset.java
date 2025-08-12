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
import com.atlan.model.fields.KeywordTextStemmedField;
import com.atlan.model.fields.NumericField;
import com.atlan.model.fields.NumericRankField;
import com.atlan.model.fields.RelationField;
import com.atlan.model.fields.SearchableRelationship;
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
 * Base class for all assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface IAsset {

    /** List of groups who administer this asset. (This is only used for certain asset types.) */
    KeywordField ADMIN_GROUPS = new KeywordField("adminGroups", "adminGroups");

    /** List of roles who administer this asset. (This is only used for Connection assets.) */
    KeywordField ADMIN_ROLES = new KeywordField("adminRoles", "adminRoles");

    /** List of users who administer this asset. (This is only used for certain asset types.) */
    KeywordField ADMIN_USERS = new KeywordField("adminUsers", "adminUsers");

    /** Detailed message to include in the announcement on this asset. */
    TextField ANNOUNCEMENT_MESSAGE = new TextField("announcementMessage", "announcementMessage");

    /** Brief title for the announcement on this asset. Required when announcementType is specified. */
    TextField ANNOUNCEMENT_TITLE = new TextField("announcementTitle", "announcementTitle");

    /** Type of announcement on this asset. */
    KeywordField ANNOUNCEMENT_TYPE = new KeywordField("announcementType", "announcementType");

    /** Time (epoch) at which the announcement was last updated, in milliseconds. */
    NumericField ANNOUNCEMENT_UPDATED_AT = new NumericField("announcementUpdatedAt", "announcementUpdatedAt");

    /** Name of the user who last updated the announcement. */
    KeywordField ANNOUNCEMENT_UPDATED_BY = new KeywordField("announcementUpdatedBy", "announcementUpdatedBy");

    /** Checks that run on this asset. */
    RelationField ANOMALO_CHECKS = new RelationField("anomaloChecks");

    /** Application owning the Asset. */
    RelationField APPLICATION = new RelationField("application");

    /** ApplicationField owning the Asset. */
    RelationField APPLICATION_FIELD = new RelationField("applicationField");

    /** Qualified name of the ApplicationField that contains this asset. */
    KeywordField APPLICATION_FIELD_QUALIFIED_NAME =
            new KeywordField("applicationFieldQualifiedName", "applicationFieldQualifiedName");

    /** Qualified name of the Application that contains this asset. */
    KeywordField APPLICATION_QUALIFIED_NAME = new KeywordField("applicationQualifiedName", "applicationQualifiedName");

    /** All associated Anomalo check types. */
    KeywordField ASSET_ANOMALO_APPLIED_CHECK_TYPES =
            new KeywordField("assetAnomaloAppliedCheckTypes", "assetAnomaloAppliedCheckTypes");

    /** Total number of checks present in Anomalo for this asset. */
    NumericField ASSET_ANOMALO_CHECK_COUNT = new NumericField("assetAnomaloCheckCount", "assetAnomaloCheckCount");

    /** Stringified JSON object containing status of all Anomalo checks associated to this asset. */
    TextField ASSET_ANOMALO_CHECK_STATUSES = new TextField("assetAnomaloCheckStatuses", "assetAnomaloCheckStatuses");

    /** Status of data quality from Anomalo. */
    KeywordField ASSET_ANOMALO_DQ_STATUS = new KeywordField("assetAnomaloDQStatus", "assetAnomaloDQStatus");

    /** Total number of checks failed in Anomalo for this asset. */
    NumericField ASSET_ANOMALO_FAILED_CHECK_COUNT =
            new NumericField("assetAnomaloFailedCheckCount", "assetAnomaloFailedCheckCount");

    /** All associated Anomalo failed check types. */
    KeywordField ASSET_ANOMALO_FAILED_CHECK_TYPES =
            new KeywordField("assetAnomaloFailedCheckTypes", "assetAnomaloFailedCheckTypes");

    /** Time (epoch) at which the last check was run via Anomalo. */
    NumericField ASSET_ANOMALO_LAST_CHECK_RUN_AT =
            new NumericField("assetAnomaloLastCheckRunAt", "assetAnomaloLastCheckRunAt");

    /** URL of the source in Anomalo. */
    TextField ASSET_ANOMALO_SOURCE_URL = new TextField("assetAnomaloSourceUrl", "assetAnomaloSourceUrl");

    /** Cover image to use for this asset in the UI (applicable to only a few asset types). */
    TextField ASSET_COVER_IMAGE = new TextField("assetCoverImage", "assetCoverImage");

    /** Name of the account in which this asset exists in dbt. */
    KeywordTextField ASSET_DBT_ACCOUNT_NAME =
            new KeywordTextField("assetDbtAccountName", "assetDbtAccountName.keyword", "assetDbtAccountName");

    /** Alias of this asset in dbt. */
    KeywordTextField ASSET_DBT_ALIAS = new KeywordTextField("assetDbtAlias", "assetDbtAlias.keyword", "assetDbtAlias");

    /** Version of the environment in which this asset is materialized in dbt. */
    KeywordField ASSET_DBT_ENVIRONMENT_DBT_VERSION =
            new KeywordField("assetDbtEnvironmentDbtVersion", "assetDbtEnvironmentDbtVersion");

    /** Name of the environment in which this asset is materialized in dbt. */
    KeywordTextField ASSET_DBT_ENVIRONMENT_NAME = new KeywordTextField(
            "assetDbtEnvironmentName", "assetDbtEnvironmentName.keyword", "assetDbtEnvironmentName");

    /** Time (epoch) at which the job that materialized this asset in dbt last ran, in milliseconds. */
    NumericField ASSET_DBT_JOB_LAST_RUN = new NumericField("assetDbtJobLastRun", "assetDbtJobLastRun");

    /** Path in S3 to the artifacts saved from the last run of the job that materialized this asset in dbt. */
    KeywordField ASSET_DBT_JOB_LAST_RUN_ARTIFACT_S3PATH =
            new KeywordField("assetDbtJobLastRunArtifactS3Path", "assetDbtJobLastRunArtifactS3Path");

    /** Whether artifacts were saved from the last run of the job that materialized this asset in dbt (true) or not (false). */
    BooleanField ASSET_DBT_JOB_LAST_RUN_ARTIFACTS_SAVED =
            new BooleanField("assetDbtJobLastRunArtifactsSaved", "assetDbtJobLastRunArtifactsSaved");

    /** Time (epoch) at which the job that materialized this asset in dbt was last created, in milliseconds. */
    NumericField ASSET_DBT_JOB_LAST_RUN_CREATED_AT =
            new NumericField("assetDbtJobLastRunCreatedAt", "assetDbtJobLastRunCreatedAt");

    /** Time (epoch) at which the job that materialized this asset in dbt was dequeued, in milliseconds. */
    NumericField ASSET_DBT_JOB_LAST_RUN_DEQUED_AT =
            new NumericField("assetDbtJobLastRunDequedAt", "assetDbtJobLastRunDequedAt");

    /** Thread ID of the user who executed the last run of the job that materialized this asset in dbt. */
    KeywordField ASSET_DBT_JOB_LAST_RUN_EXECUTED_BY_THREAD_ID =
            new KeywordField("assetDbtJobLastRunExecutedByThreadId", "assetDbtJobLastRunExecutedByThreadId");

    /** Branch in git from which the last run of the job that materialized this asset in dbt ran. */
    KeywordTextField ASSET_DBT_JOB_LAST_RUN_GIT_BRANCH = new KeywordTextField(
            "assetDbtJobLastRunGitBranch", "assetDbtJobLastRunGitBranch", "assetDbtJobLastRunGitBranch.text");

    /** SHA hash in git for the last run of the job that materialized this asset in dbt. */
    KeywordField ASSET_DBT_JOB_LAST_RUN_GIT_SHA =
            new KeywordField("assetDbtJobLastRunGitSha", "assetDbtJobLastRunGitSha");

    /** Whether docs were generated from the last run of the job that materialized this asset in dbt (true) or not (false). */
    BooleanField ASSET_DBT_JOB_LAST_RUN_HAS_DOCS_GENERATED =
            new BooleanField("assetDbtJobLastRunHasDocsGenerated", "assetDbtJobLastRunHasDocsGenerated");

    /** Whether sources were generated from the last run of the job that materialized this asset in dbt (true) or not (false). */
    BooleanField ASSET_DBT_JOB_LAST_RUN_HAS_SOURCES_GENERATED =
            new BooleanField("assetDbtJobLastRunHasSourcesGenerated", "assetDbtJobLastRunHasSourcesGenerated");

    /** Whether notifications were sent from the last run of the job that materialized this asset in dbt (true) or not (false). */
    BooleanField ASSET_DBT_JOB_LAST_RUN_NOTIFICATIONS_SENT =
            new BooleanField("assetDbtJobLastRunNotificationsSent", "assetDbtJobLastRunNotificationsSent");

    /** Thread ID of the owner of the last run of the job that materialized this asset in dbt. */
    KeywordField ASSET_DBT_JOB_LAST_RUN_OWNER_THREAD_ID =
            new KeywordField("assetDbtJobLastRunOwnerThreadId", "assetDbtJobLastRunOwnerThreadId");

    /** Total duration the job that materialized this asset in dbt spent being queued. */
    KeywordField ASSET_DBT_JOB_LAST_RUN_QUEUED_DURATION =
            new KeywordField("assetDbtJobLastRunQueuedDuration", "assetDbtJobLastRunQueuedDuration");

    /** Human-readable total duration of the last run of the job that materialized this asset in dbt spend being queued. */
    KeywordField ASSET_DBT_JOB_LAST_RUN_QUEUED_DURATION_HUMANIZED =
            new KeywordField("assetDbtJobLastRunQueuedDurationHumanized", "assetDbtJobLastRunQueuedDurationHumanized");

    /** Run duration of the last run of the job that materialized this asset in dbt. */
    KeywordField ASSET_DBT_JOB_LAST_RUN_RUN_DURATION =
            new KeywordField("assetDbtJobLastRunRunDuration", "assetDbtJobLastRunRunDuration");

    /** Human-readable run duration of the last run of the job that materialized this asset in dbt. */
    KeywordField ASSET_DBT_JOB_LAST_RUN_RUN_DURATION_HUMANIZED =
            new KeywordField("assetDbtJobLastRunRunDurationHumanized", "assetDbtJobLastRunRunDurationHumanized");

    /** Time (epoch) at which the job that materialized this asset in dbt was started running, in milliseconds. */
    NumericField ASSET_DBT_JOB_LAST_RUN_STARTED_AT =
            new NumericField("assetDbtJobLastRunStartedAt", "assetDbtJobLastRunStartedAt");

    /** Status message of the last run of the job that materialized this asset in dbt. */
    KeywordTextField ASSET_DBT_JOB_LAST_RUN_STATUS_MESSAGE = new KeywordTextField(
            "assetDbtJobLastRunStatusMessage",
            "assetDbtJobLastRunStatusMessage.keyword",
            "assetDbtJobLastRunStatusMessage");

    /** Total duration of the last run of the job that materialized this asset in dbt. */
    KeywordField ASSET_DBT_JOB_LAST_RUN_TOTAL_DURATION =
            new KeywordField("assetDbtJobLastRunTotalDuration", "assetDbtJobLastRunTotalDuration");

    /** Human-readable total duration of the last run of the job that materialized this asset in dbt. */
    TextField ASSET_DBT_JOB_LAST_RUN_TOTAL_DURATION_HUMANIZED =
            new TextField("assetDbtJobLastRunTotalDurationHumanized", "assetDbtJobLastRunTotalDurationHumanized");

    /** Time (epoch) at which the job that materialized this asset in dbt was last updated, in milliseconds. */
    NumericField ASSET_DBT_JOB_LAST_RUN_UPDATED_AT =
            new NumericField("assetDbtJobLastRunUpdatedAt", "assetDbtJobLastRunUpdatedAt");

    /** URL of the last run of the job that materialized this asset in dbt. */
    KeywordField ASSET_DBT_JOB_LAST_RUN_URL = new KeywordField("assetDbtJobLastRunUrl", "assetDbtJobLastRunUrl");

    /** Name of the job that materialized this asset in dbt. */
    KeywordTextField ASSET_DBT_JOB_NAME =
            new KeywordTextField("assetDbtJobName", "assetDbtJobName.keyword", "assetDbtJobName");

    /** Time (epoch) when the next run of the job that materializes this asset in dbt is scheduled. */
    NumericField ASSET_DBT_JOB_NEXT_RUN = new NumericField("assetDbtJobNextRun", "assetDbtJobNextRun");

    /** Human-readable time when the next run of the job that materializes this asset in dbt is scheduled. */
    KeywordTextField ASSET_DBT_JOB_NEXT_RUN_HUMANIZED = new KeywordTextField(
            "assetDbtJobNextRunHumanized", "assetDbtJobNextRunHumanized.keyword", "assetDbtJobNextRunHumanized");

    /** Schedule of the job that materialized this asset in dbt. */
    KeywordField ASSET_DBT_JOB_SCHEDULE = new KeywordField("assetDbtJobSchedule", "assetDbtJobSchedule");

    /** Human-readable cron schedule of the job that materialized this asset in dbt. */
    TextField ASSET_DBT_JOB_SCHEDULE_CRON_HUMANIZED =
            new TextField("assetDbtJobScheduleCronHumanized", "assetDbtJobScheduleCronHumanized");

    /** Status of the job that materialized this asset in dbt. */
    KeywordField ASSET_DBT_JOB_STATUS = new KeywordField("assetDbtJobStatus", "assetDbtJobStatus");

    /** Metadata for this asset in dbt, specifically everything under the 'meta' key in the dbt object. */
    TextField ASSET_DBT_META = new TextField("assetDbtMeta", "assetDbtMeta");

    /** Name of the package in which this asset exists in dbt. */
    KeywordTextField ASSET_DBT_PACKAGE_NAME =
            new KeywordTextField("assetDbtPackageName", "assetDbtPackageName.keyword", "assetDbtPackageName");

    /** Name of the project in which this asset exists in dbt. */
    KeywordTextField ASSET_DBT_PROJECT_NAME =
            new KeywordTextField("assetDbtProjectName", "assetDbtProjectName.keyword", "assetDbtProjectName");

    /** URL of the semantic layer proxy for this asset in dbt. */
    KeywordField ASSET_DBT_SEMANTIC_LAYER_PROXY_URL =
            new KeywordField("assetDbtSemanticLayerProxyUrl", "assetDbtSemanticLayerProxyUrl");

    /** Freshness criteria for the source of this asset in dbt. */
    TextField ASSET_DBT_SOURCE_FRESHNESS_CRITERIA =
            new TextField("assetDbtSourceFreshnessCriteria", "assetDbtSourceFreshnessCriteria");

    /** List of tags attached to this asset in dbt. */
    KeywordTextField ASSET_DBT_TAGS = new KeywordTextField("assetDbtTags", "assetDbtTags", "assetDbtTags.text");

    /** All associated dbt test statuses. */
    KeywordField ASSET_DBT_TEST_STATUS = new KeywordField("assetDbtTestStatus", "assetDbtTestStatus");

    /** Unique identifier of this asset in dbt. */
    KeywordTextField ASSET_DBT_UNIQUE_ID =
            new KeywordTextField("assetDbtUniqueId", "assetDbtUniqueId.keyword", "assetDbtUniqueId");

    /** Name of the DBT workflow in Atlan that last updated the asset. */
    KeywordField ASSET_DBT_WORKFLOW_LAST_UPDATED =
            new KeywordField("assetDbtWorkflowLastUpdated", "assetDbtWorkflowLastUpdated");

    /** Name of the icon to use for this asset. (Only applies to glossaries, currently.) */
    TextField ASSET_ICON = new TextField("assetIcon", "assetIcon");

    /** Internal Popularity score for this asset. */
    NumericRankField ASSET_INTERNAL_POPULARITY_SCORE = new NumericRankField(
            "assetInternalPopularityScore",
            "assetInternalPopularityScore",
            "assetInternalPopularityScore.rank_feature");

    /** List of unique Monte Carlo alert names attached to this asset. */
    KeywordTextField ASSET_MC_ALERT_QUALIFIED_NAMES = new KeywordTextField(
            "assetMcAlertQualifiedNames", "assetMcAlertQualifiedNames", "assetMcAlertQualifiedNames.text");

    /** List of Monte Carlo incident names attached to this asset. */
    KeywordTextField ASSET_MC_INCIDENT_NAMES =
            new KeywordTextField("assetMcIncidentNames", "assetMcIncidentNames.keyword", "assetMcIncidentNames");

    /** List of Monte Carlo incident priorities associated with this asset. */
    KeywordField ASSET_MC_INCIDENT_PRIORITIES =
            new KeywordField("assetMcIncidentPriorities", "assetMcIncidentPriorities");

    /** List of unique Monte Carlo incident names attached to this asset. */
    KeywordTextField ASSET_MC_INCIDENT_QUALIFIED_NAMES = new KeywordTextField(
            "assetMcIncidentQualifiedNames", "assetMcIncidentQualifiedNames", "assetMcIncidentQualifiedNames.text");

    /** List of Monte Carlo incident severities associated with this asset. */
    KeywordField ASSET_MC_INCIDENT_SEVERITIES =
            new KeywordField("assetMcIncidentSeverities", "assetMcIncidentSeverities");

    /** List of Monte Carlo incident states associated with this asset. */
    KeywordField ASSET_MC_INCIDENT_STATES = new KeywordField("assetMcIncidentStates", "assetMcIncidentStates");

    /** List of Monte Carlo incident sub-types associated with this asset. */
    KeywordField ASSET_MC_INCIDENT_SUB_TYPES = new KeywordField("assetMcIncidentSubTypes", "assetMcIncidentSubTypes");

    /** List of Monte Carlo incident types associated with this asset. */
    KeywordField ASSET_MC_INCIDENT_TYPES = new KeywordField("assetMcIncidentTypes", "assetMcIncidentTypes");

    /** Tracks whether this asset is monitored by MC or not */
    BooleanField ASSET_MC_IS_MONITORED = new BooleanField("assetMcIsMonitored", "assetMcIsMonitored");

    /** Time (epoch) at which this asset was last synced from Monte Carlo. */
    NumericField ASSET_MC_LAST_SYNC_RUN_AT = new NumericField("assetMcLastSyncRunAt", "assetMcLastSyncRunAt");

    /** List of Monte Carlo monitor names attached to this asset. */
    KeywordTextField ASSET_MC_MONITOR_NAMES =
            new KeywordTextField("assetMcMonitorNames", "assetMcMonitorNames.keyword", "assetMcMonitorNames");

    /** List of unique Monte Carlo monitor names attached to this asset. */
    KeywordTextField ASSET_MC_MONITOR_QUALIFIED_NAMES = new KeywordTextField(
            "assetMcMonitorQualifiedNames", "assetMcMonitorQualifiedNames", "assetMcMonitorQualifiedNames.text");

    /** Schedules of all associated Monte Carlo monitors. */
    KeywordField ASSET_MC_MONITOR_SCHEDULE_TYPES =
            new KeywordField("assetMcMonitorScheduleTypes", "assetMcMonitorScheduleTypes");

    /** Statuses of all associated Monte Carlo monitors. */
    KeywordField ASSET_MC_MONITOR_STATUSES = new KeywordField("assetMcMonitorStatuses", "assetMcMonitorStatuses");

    /** Types of all associated Monte Carlo monitors. */
    KeywordField ASSET_MC_MONITOR_TYPES = new KeywordField("assetMcMonitorTypes", "assetMcMonitorTypes");

    /** Count of policies inside the asset */
    NumericField ASSET_POLICIES_COUNT = new NumericField("assetPoliciesCount", "assetPoliciesCount");

    /** Array of policy ids governing this asset */
    KeywordField ASSET_POLICY_GUIDS = new KeywordField("assetPolicyGUIDs", "assetPolicyGUIDs");

    /** Array of asset ids that equivalent to this asset. */
    KeywordField ASSET_REDIRECT_GUIDS = new KeywordField("assetRedirectGUIDs", "assetRedirectGUIDs");

    /** Number of checks done via Soda. */
    NumericField ASSET_SODA_CHECK_COUNT = new NumericField("assetSodaCheckCount", "assetSodaCheckCount");

    /** All associated Soda check statuses. */
    TextField ASSET_SODA_CHECK_STATUSES = new TextField("assetSodaCheckStatuses", "assetSodaCheckStatuses");

    /** Status of data quality from Soda. */
    KeywordField ASSET_SODA_DQ_STATUS = new KeywordField("assetSodaDQStatus", "assetSodaDQStatus");

    /** TBC */
    NumericField ASSET_SODA_LAST_SCAN_AT = new NumericField("assetSodaLastScanAt", "assetSodaLastScanAt");

    /** TBC */
    NumericField ASSET_SODA_LAST_SYNC_RUN_AT = new NumericField("assetSodaLastSyncRunAt", "assetSodaLastSyncRunAt");

    /** TBC */
    KeywordField ASSET_SODA_SOURCE_URL = new KeywordField("assetSodaSourceURL", "assetSodaSourceURL");

    /** Readme of this asset, as extracted from source. If present, this will be used for the readme in user interface. */
    KeywordTextField ASSET_SOURCE_README =
            new KeywordTextField("assetSourceReadme", "assetSourceReadme.keyword", "assetSourceReadme");

    /** List of tags attached to this asset. */
    KeywordTextField ASSET_TAGS = new KeywordTextField("assetTags", "assetTags", "assetTags.text");

    /** Color (in hexadecimal RGB) to use to represent this asset. */
    TextField ASSET_THEME_HEX = new TextField("assetThemeHex", "assetThemeHex");

    /** Name to use for this type of asset, as a subtype of the actual typeName. */
    KeywordField ASSET_USER_DEFINED_TYPE = new KeywordField("assetUserDefinedType", "assetUserDefinedType");

    /** Status of this asset's certification. */
    KeywordTextField CERTIFICATE_STATUS =
            new KeywordTextField("certificateStatus", "certificateStatus", "certificateStatus.text");

    /** Human-readable descriptive message used to provide further detail to certificateStatus. */
    TextField CERTIFICATE_STATUS_MESSAGE = new TextField("certificateStatusMessage", "certificateStatusMessage");

    /** Time (epoch) at which the certification was last updated, in milliseconds. */
    NumericField CERTIFICATE_UPDATED_AT = new NumericField("certificateUpdatedAt", "certificateUpdatedAt");

    /** Name of the user who last updated the certification of this asset. */
    KeywordField CERTIFICATE_UPDATED_BY = new KeywordField("certificateUpdatedBy", "certificateUpdatedBy");

    /** Simple name of the connection through which this asset is accessible. */
    KeywordTextField CONNECTION_NAME = new KeywordTextField("connectionName", "connectionName", "connectionName.text");

    /** Unique name of the connection through which this asset is accessible. */
    KeywordTextField CONNECTION_QUALIFIED_NAME =
            new KeywordTextField("connectionQualifiedName", "connectionQualifiedName", "connectionQualifiedName.text");

    /** Type of the connector through which this asset is accessible. */
    KeywordField CONNECTOR_NAME = new KeywordField("connectorName", "connectorName");

    /** Latest version of the data contract (in any status) for this asset. */
    RelationField DATA_CONTRACT_LATEST = new RelationField("dataContractLatest");

    /** Latest certified version of the data contract for this asset. */
    RelationField DATA_CONTRACT_LATEST_CERTIFIED = new RelationField("dataContractLatestCertified");

    /** Unique name of this asset in dbt. */
    KeywordTextField DBT_QUALIFIED_NAME =
            new KeywordTextField("dbtQualifiedName", "dbtQualifiedName", "dbtQualifiedName.text");

    /** Description of this asset, for example as crawled from a source. Fallback for display purposes, if userDescription is empty. */
    KeywordTextField DESCRIPTION = new KeywordTextField("description", "description.keyword", "description");

    /** Human-readable name of this asset used for display purposes (in user interface). */
    KeywordTextField DISPLAY_NAME = new KeywordTextField("displayName", "displayName.keyword", "displayName");

    /** Array of domain guids linked to this asset */
    KeywordField DOMAIN_GUIDS = new KeywordField("domainGUIDs", "domainGUIDs");

    /** TBC */
    RelationField FILES = new RelationField("files");

    /** Whether this asset has contract (true) or not (false). */
    BooleanField HAS_CONTRACT = new BooleanField("hasContract", "hasContract");

    /** Whether this asset has lineage (true) or not (false). */
    BooleanField HAS_LINEAGE = new BooleanField("__hasLineage", "__hasLineage");

    /** Data products for which this asset is an input port. */
    RelationField INPUT_PORT_DATA_PRODUCTS = new RelationField("inputPortDataProducts");

    /** TBC */
    BooleanField IS_AI_GENERATED = new BooleanField("isAIGenerated", "isAIGenerated");

    /** Whether this asset is discoverable through the UI (true) or not (false). */
    BooleanField IS_DISCOVERABLE = new BooleanField("isDiscoverable", "isDiscoverable");

    /** Whether this asset can be edited in the UI (true) or not (false). */
    BooleanField IS_EDITABLE = new BooleanField("isEditable", "isEditable");

    /** Indicates this asset is not fully-known, if true. */
    BooleanField IS_PARTIAL = new BooleanField("isPartial", "isPartial");

    /** Time (epoch) of the last operation that inserted, updated, or deleted rows, in milliseconds. */
    NumericField LAST_ROW_CHANGED_AT = new NumericField("lastRowChangedAt", "lastRowChangedAt");

    /** Name of the last run of the crawler that last synchronized this asset. */
    KeywordField LAST_SYNC_RUN = new KeywordField("lastSyncRun", "lastSyncRun");

    /** Time (epoch) at which this asset was last crawled, in milliseconds. */
    NumericField LAST_SYNC_RUN_AT = new NumericField("lastSyncRunAt", "lastSyncRunAt");

    /** Name of the crawler that last synchronized this asset. */
    KeywordField LAST_SYNC_WORKFLOW_NAME = new KeywordField("lastSyncWorkflowName", "lastSyncWorkflowName");

    /** Custom order for sorting purpose, managed by client */
    KeywordField LEXICOGRAPHICAL_SORT_ORDER = new KeywordField("lexicographicalSortOrder", "lexicographicalSortOrder");

    /** Links that are attached to this asset. */
    SearchableRelationship LINKS = new SearchableRelationship("links", "asset_links");

    /** TBC */
    RelationField MC_INCIDENTS = new RelationField("mcIncidents");

    /** Monitors that observe this asset. */
    RelationField MC_MONITORS = new RelationField("mcMonitors");

    /** TBC */
    RelationField METRICS = new RelationField("metrics");

    /** Name of this asset. Fallback for display purposes, if displayName is empty. */
    KeywordTextStemmedField NAME = new KeywordTextStemmedField("name", "name.keyword", "name", "name.stemmed");

    /** Array of policy ids non-compliant to this asset */
    KeywordField NON_COMPLIANT_ASSET_POLICY_GUIDS =
            new KeywordField("nonCompliantAssetPolicyGUIDs", "nonCompliantAssetPolicyGUIDs");

    /** Data products for which this asset is an output port. */
    RelationField OUTPUT_PORT_DATA_PRODUCTS = new RelationField("outputPortDataProducts");

    /** Array of product guids which have this asset as outputPort */
    KeywordField OUTPUT_PRODUCT_GUIDS = new KeywordField("outputProductGUIDs", "outputProductGUIDs");

    /** List of groups who own this asset. */
    KeywordField OWNER_GROUPS = new KeywordField("ownerGroups", "ownerGroups");

    /** List of users who own this asset. */
    KeywordField OWNER_USERS = new KeywordField("ownerUsers", "ownerUsers");

    /** Popularity score for this asset. */
    NumericRankField POPULARITY_SCORE =
            new NumericRankField("popularityScore", "popularityScore", "popularityScore.rank_feature");

    /** Array of product guids linked to this asset */
    KeywordField PRODUCT_GUIDS = new KeywordField("productGUIDs", "productGUIDs");

    /** README that is linked to this asset. */
    SearchableRelationship README = new SearchableRelationship("readme", "asset_readme");

    /** URL for sample data for this asset. */
    KeywordTextField SAMPLE_DATA_URL = new KeywordTextField("sampleDataUrl", "sampleDataUrl", "sampleDataUrl.text");

    /** TBC */
    RelationField SCHEMA_REGISTRY_SUBJECTS = new RelationField("schemaRegistrySubjects");

    /** TBC */
    RelationField SODA_CHECKS = new RelationField("sodaChecks");

    /** The unit of measure for sourceTotalCost. */
    KeywordField SOURCE_COST_UNIT = new KeywordField("sourceCostUnit", "sourceCostUnit");

    /** Time (epoch) at which this asset was created in the source system, in milliseconds. */
    NumericField SOURCE_CREATED_AT = new NumericField("sourceCreatedAt", "sourceCreatedAt");

    /** Name of the user who created this asset, in the source system. */
    KeywordField SOURCE_CREATED_BY = new KeywordField("sourceCreatedBy", "sourceCreatedBy");

    /** URL to create an embed for a resource (for example, an image of a dashboard) within Atlan. */
    KeywordField SOURCE_EMBED_URL = new KeywordField("sourceEmbedURL", "sourceEmbedURL");

    /** Timestamp of most recent read operation. */
    NumericField SOURCE_LAST_READ_AT = new NumericField("sourceLastReadAt", "sourceLastReadAt");

    /** List of owners of this asset, in the source system. */
    TextField SOURCE_OWNERS = new TextField("sourceOwners", "sourceOwners");

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

    /** List of usernames with extra insights for the most recent users who read this asset. */
    KeywordField SOURCE_READ_RECENT_USER_RECORDS =
            new KeywordField("sourceReadRecentUserRecordList", "sourceReadRecentUserRecordList");

    /** List of usernames of the most recent users who read this asset. */
    KeywordField SOURCE_READ_RECENT_USERS = new KeywordField("sourceReadRecentUserList", "sourceReadRecentUserList");

    /** List of the slowest queries that accessed this asset. */
    KeywordField SOURCE_READ_SLOW_QUERY_RECORDS =
            new KeywordField("sourceReadSlowQueryRecordList", "sourceReadSlowQueryRecordList");

    /** List of usernames with extra insights for the users who read this asset the most. */
    KeywordField SOURCE_READ_TOP_USER_RECORDS =
            new KeywordField("sourceReadTopUserRecordList", "sourceReadTopUserRecordList");

    /** List of usernames of the users who read this asset the most. */
    KeywordField SOURCE_READ_TOP_USERS = new KeywordField("sourceReadTopUserList", "sourceReadTopUserList");

    /** Total number of unique users that read data from asset. */
    NumericField SOURCE_READ_USER_COUNT = new NumericField("sourceReadUserCount", "sourceReadUserCount");

    /** Total cost of all operations at source. */
    NumericField SOURCE_TOTAL_COST = new NumericField("sourceTotalCost", "sourceTotalCost");

    /** URL to the resource within the source application, used to create a button to view this asset in the source application. */
    KeywordField SOURCE_URL = new KeywordField("sourceURL", "sourceURL");

    /** Time (epoch) at which this asset was last updated in the source system, in milliseconds. */
    NumericField SOURCE_UPDATED_AT = new NumericField("sourceUpdatedAt", "sourceUpdatedAt");

    /** Name of the user who last updated this asset, in the source system. */
    KeywordField SOURCE_UPDATED_BY = new KeywordField("sourceUpdatedBy", "sourceUpdatedBy");

    /** Users who have starred this asset. */
    KeywordField STARRED_BY = new KeywordField("starredBy", "starredBy");

    /** Number of users who have starred this asset. */
    NumericField STARRED_COUNT = new NumericField("starredCount", "starredCount");

    /** List of usernames with extra information of the users who have starred an asset. */
    KeywordField STARRED_DETAILS = new KeywordField("starredDetailsList", "starredDetailsList");

    /** Subtype of this asset. */
    KeywordField SUB_TYPE = new KeywordField("subType", "subType");

    /** Name of the Atlan workspace in which this asset exists. */
    TextField TENANT_ID = new TextField("tenantId", "tenantId");

    /** TBC */
    RelationField USER_DEF_RELATIONSHIP_FROMS = new RelationField("userDefRelationshipFrom");

    /** TBC */
    RelationField USER_DEF_RELATIONSHIP_TOS = new RelationField("userDefRelationshipTo");

    /** Description of this asset, as provided by a user. If present, this will be used for the description in user interface. */
    KeywordTextField USER_DESCRIPTION =
            new KeywordTextField("userDescription", "userDescription.keyword", "userDescription");

    /** View score for this asset. */
    NumericRankField VIEW_SCORE = new NumericRankField("viewScore", "viewScore", "viewScore.rank_feature");

    /** List of groups who can view assets contained in a collection. (This is only used for certain asset types.) */
    KeywordField VIEWER_GROUPS = new KeywordField("viewerGroups", "viewerGroups");

    /** List of users who can view assets contained in a collection. (This is only used for certain asset types.) */
    KeywordField VIEWER_USERS = new KeywordField("viewerUsers", "viewerUsers");

    /** Unique fully-qualified name of the asset in Atlan. */
    KeywordTextField QUALIFIED_NAME = new KeywordTextField("qualifiedName", "qualifiedName", "qualifiedName.text");

    /** Built-in connector type through which this asset is accessible. */
    KeywordField CONNECTOR_TYPE = new KeywordField("connectorName", "connectorName");

    /** Custom connector type through which this asset is accessible. */
    KeywordField CUSTOM_CONNECTOR_TYPE = new KeywordField("connectorName", "connectorName");

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

    /** Checks that run on this asset. */
    SortedSet<IAnomaloCheck> getAnomaloChecks();

    /** Application owning the Asset. */
    IApplication getApplication();

    /** ApplicationField owning the Asset. */
    IApplicationField getApplicationField();

    /** Qualified name of the ApplicationField that contains this asset. */
    String getApplicationFieldQualifiedName();

    /** Qualified name of the Application that contains this asset. */
    String getApplicationQualifiedName();

    /** All associated Anomalo check types. */
    SortedSet<String> getAssetAnomaloAppliedCheckTypes();

    /** Total number of checks present in Anomalo for this asset. */
    Long getAssetAnomaloCheckCount();

    /** Stringified JSON object containing status of all Anomalo checks associated to this asset. */
    String getAssetAnomaloCheckStatuses();

    /** Status of data quality from Anomalo. */
    String getAssetAnomaloDQStatus();

    /** Total number of checks failed in Anomalo for this asset. */
    Long getAssetAnomaloFailedCheckCount();

    /** All associated Anomalo failed check types. */
    SortedSet<String> getAssetAnomaloFailedCheckTypes();

    /** Time (epoch) at which the last check was run via Anomalo. */
    Long getAssetAnomaloLastCheckRunAt();

    /** URL of the source in Anomalo. */
    String getAssetAnomaloSourceUrl();

    /** Cover image to use for this asset in the UI (applicable to only a few asset types). */
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

    /** Internal Popularity score for this asset. */
    Double getAssetInternalPopularityScore();

    /** List of unique Monte Carlo alert names attached to this asset. */
    SortedSet<String> getAssetMcAlertQualifiedNames();

    /** List of Monte Carlo incident names attached to this asset. */
    SortedSet<String> getAssetMcIncidentNames();

    /** List of Monte Carlo incident priorities associated with this asset. */
    SortedSet<String> getAssetMcIncidentPriorities();

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

    /** Tracks whether this asset is monitored by MC or not */
    Boolean getAssetMcIsMonitored();

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

    /** Array of asset ids that equivalent to this asset. */
    SortedSet<String> getAssetRedirectGUIDs();

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

    /** Readme of this asset, as extracted from source. If present, this will be used for the readme in user interface. */
    String getAssetSourceReadme();

    /** List of tags attached to this asset. */
    SortedSet<String> getAssetTags();

    /** Color (in hexadecimal RGB) to use to represent this asset. */
    String getAssetThemeHex();

    /** Name to use for this type of asset, as a subtype of the actual typeName. */
    String getAssetUserDefinedType();

    /** Glossary terms that are linked to this asset. */
    SortedSet<IGlossaryTerm> getAssignedTerms();

    /** Status of this asset's certification. */
    CertificateStatus getCertificateStatus();

    /** Human-readable descriptive message used to provide further detail to certificateStatus. */
    String getCertificateStatusMessage();

    /** Time (epoch) at which the certification was last updated, in milliseconds. */
    Long getCertificateUpdatedAt();

    /** Name of the user who last updated the certification of this asset. */
    String getCertificateUpdatedBy();

    /** Simple name of the connection through which this asset is accessible. */
    String getConnectionName();

    /** Unique name of the connection through which this asset is accessible. */
    String getConnectionQualifiedName();

    /** Type of the connector through which this asset is accessible. */
    String getConnectorName();

    /** Latest version of the data contract (in any status) for this asset. */
    IDataContract getDataContractLatest();

    /** Latest certified version of the data contract for this asset. */
    IDataContract getDataContractLatestCertified();

    /** Unique name of this asset in dbt. */
    String getDbtQualifiedName();

    /** Description of this asset, for example as crawled from a source. Fallback for display purposes, if userDescription is empty. */
    String getDescription();

    /** Human-readable name of this asset used for display purposes (in user interface). */
    String getDisplayName();

    /** Array of domain guids linked to this asset */
    SortedSet<String> getDomainGUIDs();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** Whether this asset has contract (true) or not (false). */
    Boolean getHasContract();

    /** Whether this asset has lineage (true) or not (false). */
    Boolean getHasLineage();

    /** Data products for which this asset is an input port. */
    SortedSet<IDataProduct> getInputPortDataProducts();

    /** TBC */
    Boolean getIsAIGenerated();

    /** Whether this asset is discoverable through the UI (true) or not (false). */
    Boolean getIsDiscoverable();

    /** Whether this asset can be edited in the UI (true) or not (false). */
    Boolean getIsEditable();

    /** Indicates this asset is not fully-known, if true. */
    Boolean getIsPartial();

    /** Time (epoch) of the last operation that inserted, updated, or deleted rows, in milliseconds. */
    Long getLastRowChangedAt();

    /** Name of the last run of the crawler that last synchronized this asset. */
    String getLastSyncRun();

    /** Time (epoch) at which this asset was last crawled, in milliseconds. */
    Long getLastSyncRunAt();

    /** Name of the crawler that last synchronized this asset. */
    String getLastSyncWorkflowName();

    /** Custom order for sorting purpose, managed by client */
    String getLexicographicalSortOrder();

    /** Links that are attached to this asset. */
    SortedSet<ILink> getLinks();

    /** TBC */
    SortedSet<IMCIncident> getMcIncidents();

    /** Monitors that observe this asset. */
    SortedSet<IMCMonitor> getMcMonitors();

    /** TBC */
    SortedSet<IMetric> getMetrics();

    /** Name of this asset. Fallback for display purposes, if displayName is empty. */
    String getName();

    /** Array of policy ids non-compliant to this asset */
    SortedSet<String> getNonCompliantAssetPolicyGUIDs();

    /** Data products for which this asset is an output port. */
    SortedSet<IDataProduct> getOutputPortDataProducts();

    /** Array of product guids which have this asset as outputPort */
    SortedSet<String> getOutputProductGUIDs();

    /** List of groups who own this asset. */
    SortedSet<String> getOwnerGroups();

    /** List of users who own this asset. */
    SortedSet<String> getOwnerUsers();

    /** Popularity score for this asset. */
    Double getPopularityScore();

    /** Array of product guids linked to this asset */
    SortedSet<String> getProductGUIDs();

    /** Unique name for this asset. This is typically a concatenation of the asset's name onto its parent's qualifiedName. This must be unique across all assets of the same type. */
    String getQualifiedName();

    /** README that is linked to this asset. */
    IReadme getReadme();

    /** URL for sample data for this asset. */
    String getSampleDataUrl();

    /** TBC */
    SortedSet<ISchemaRegistrySubject> getSchemaRegistrySubjects();

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

    /** Users who have starred this asset. */
    SortedSet<String> getStarredBy();

    /** Number of users who have starred this asset. */
    Integer getStarredCount();

    /** List of usernames with extra information of the users who have starred an asset. */
    List<StarredDetails> getStarredDetails();

    /** Subtype of this asset. */
    String getSubType();

    /** Name of the Atlan workspace in which this asset exists. */
    String getTenantId();

    /** TBC */
    SortedSet<IAsset> getUserDefRelationshipFroms();

    /** TBC */
    SortedSet<IAsset> getUserDefRelationshipTos();

    /** Description of this asset, as provided by a user. If present, this will be used for the description in user interface. */
    String getUserDescription();

    /** View score for this asset. */
    Double getViewScore();

    /** List of groups who can view assets contained in a collection. (This is only used for certain asset types.) */
    SortedSet<String> getViewerGroups();

    /** List of users who can view assets contained in a collection. (This is only used for certain asset types.) */
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
