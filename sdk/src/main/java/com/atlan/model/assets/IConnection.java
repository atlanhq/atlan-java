/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.ConnectionDQEnvironmentSetupStatus;
import com.atlan.model.enums.DataQualityDimension;
import com.atlan.model.enums.DataQualityResult;
import com.atlan.model.enums.DataQualityScheduleType;
import com.atlan.model.enums.DataQualitySourceSyncStatus;
import com.atlan.model.enums.QueryUsernameStrategy;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.model.fields.BooleanField;
import com.atlan.model.fields.KeywordField;
import com.atlan.model.fields.NumericField;
import com.atlan.model.fields.RelationField;
import com.atlan.model.fields.TextField;
import com.atlan.model.relations.RelationshipAttributes;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.AssetExternalDQMetadata;
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
    TextField CATEGORY = new TextField("category", "category");

    /** Unique identifier (GUID) for the data quality credentials to use for this connection. */
    KeywordField CONNECTION_DQ_CREDENTIAL_GUID =
            new KeywordField("connectionDQCredentialGuid", "connectionDQCredentialGuid");

    /** Error message if data quality environment setup failed for this connection. */
    TextField CONNECTION_DQ_ENVIRONMENT_SETUP_ERROR_MESSAGE =
            new TextField("connectionDQEnvironmentSetupErrorMessage", "connectionDQEnvironmentSetupErrorMessage");

    /** Status of the data quality environment setup for this connection. */
    KeywordField CONNECTION_DQ_ENVIRONMENT_SETUP_STATUS =
            new KeywordField("connectionDQEnvironmentSetupStatus", "connectionDQEnvironmentSetupStatus");

    /** Timestamp when the data quality environment setup status was last updated. */
    NumericField CONNECTION_DQ_ENVIRONMENT_SETUP_STATUS_UPDATED_AT = new NumericField(
            "connectionDQEnvironmentSetupStatusUpdatedAt", "connectionDQEnvironmentSetupStatusUpdatedAt");

    /** Name of the database in the source environment for data quality. */
    KeywordField CONNECTION_DQ_ENVIRONMENT_SOURCE_DATABASE_NAME =
            new KeywordField("connectionDQEnvironmentSourceDatabaseName", "connectionDQEnvironmentSourceDatabaseName");

    /** TBC */
    KeywordField CONNECTION_DBT_ENVIRONMENTS =
            new KeywordField("connectionDbtEnvironments", "connectionDbtEnvironments");

    /** Whether data quality is enabled for this connection (true) or not (false). */
    BooleanField CONNECTION_IS_DQ_ENABLED = new BooleanField("connectionIsDQEnabled", "connectionIsDQEnabled");

    /** Unique identifier (GUID) for the SSO credentials to use for this connection. */
    KeywordField CONNECTION_SSO_CREDENTIAL_GUID =
            new KeywordField("connectionSSOCredentialGuid", "connectionSSOCredentialGuid");

    /** Configuration for a workflow run. */
    KeywordField CONNECTION_WORKFLOW_CONFIGURATION =
            new KeywordField("connectionWorkflowConfiguration", "connectionWorkflowConfiguration");

    /** Unused. Only the value of connectorType impacts icons. */
    TextField CONNECTOR_ICON = new TextField("connectorIcon", "connectorIcon");

    /** Unused. Only the value of connectorType impacts icons. */
    TextField CONNECTOR_IMAGE = new TextField("connectorImage", "connectorImage");

    /** Credential strategy to use for this connection for queries. */
    TextField CREDENTIAL_STRATEGY = new TextField("credentialStrategy", "credentialStrategy");

    /** Unique identifier (GUID) for the default credentials to use for this connection. */
    TextField DEFAULT_CREDENTIAL_GUID = new TextField("defaultCredentialGuid", "defaultCredentialGuid");

    /** Whether this connection has popularity insights (true) or not (false). */
    BooleanField HAS_POPULARITY_INSIGHTS = new BooleanField("hasPopularityInsights", "hasPopularityInsights");

    /** Host name of this connection's source. */
    TextField HOST = new TextField("host", "host");

    /** Connection process to which this asset provides input. */
    RelationField INPUT_TO_CONNECTION_PROCESSES = new RelationField("inputToConnectionProcesses");

    /** Whether sample data can be previewed for this connection (true) or not (false). */
    BooleanField IS_SAMPLE_DATA_PREVIEW_ENABLED =
            new BooleanField("isSampleDataPreviewEnabled", "isSampleDataPreviewEnabled");

    /** Number of rows after which results should be uploaded to storage. */
    NumericField OBJECT_STORAGE_UPLOAD_THRESHOLD =
            new NumericField("objectStorageUploadThreshold", "objectStorageUploadThreshold");

    /** Connection processs from which this asset is produced as output. */
    RelationField OUTPUT_FROM_CONNECTION_PROCESSES = new RelationField("outputFromConnectionProcesses");

    /** Policy strategy is a configuration that determines whether the Atlan policy will be applied to the results of insight queries and whether the query will be rewritten, applicable for stream api call made from insight screen */
    KeywordField POLICY_STRATEGY = new KeywordField("policyStrategy", "policyStrategy");

    /** Policy strategy is a configuration that determines whether the Atlan policy will be applied to the results of insight queries and whether the query will be rewritten. policyStrategyForSamplePreview config is applicable for sample preview call from assets screen */
    KeywordField POLICY_STRATEGY_FOR_SAMPLE_PREVIEW =
            new KeywordField("policyStrategyForSamplePreview", "policyStrategyForSamplePreview");

    /** Number of days over which popularity is calculated, for example 30 days. */
    NumericField POPULARITY_INSIGHTS_TIMEFRAME =
            new NumericField("popularityInsightsTimeframe", "popularityInsightsTimeframe");

    /** Port number to this connection's source. */
    NumericField PORT = new NumericField("port", "port");

    /** Credential strategy to use for this connection for preview queries. */
    KeywordField PREVIEW_CREDENTIAL_STRATEGY =
            new KeywordField("previewCredentialStrategy", "previewCredentialStrategy");

    /** Query config for this connection. */
    TextField QUERY_CONFIG = new TextField("queryConfig", "queryConfig");

    /** Configuration for preview queries. */
    KeywordField QUERY_PREVIEW_CONFIG = new KeywordField("queryPreviewConfig", "queryPreviewConfig");

    /** Maximum time a query should be allowed to run before timing out. */
    NumericField QUERY_TIMEOUT = new NumericField("queryTimeout", "queryTimeout");

    /** Username strategy to use for this connection for queries. */
    KeywordField QUERY_USERNAME_STRATEGY = new KeywordField("queryUsernameStrategy", "queryUsernameStrategy");

    /** Maximum number of rows that can be returned for the source. */
    NumericField ROW_LIMIT = new NumericField("rowLimit", "rowLimit");

    /** Unused. Only the value of connectorType impacts icons. */
    TextField SOURCE_LOGO = new TextField("sourceLogo", "sourceLogo");

    /** Subcategory of this connection. */
    TextField SUB_CATEGORY = new TextField("subCategory", "subCategory");

    /** Whether to upload to S3, GCP, or another storage location (true) or not (false). */
    BooleanField USE_OBJECT_STORAGE = new BooleanField("useObjectStorage", "useObjectStorage");

    /** TBC */
    BooleanField VECTOR_EMBEDDINGS_ENABLED = new BooleanField("vectorEmbeddingsEnabled", "vectorEmbeddingsEnabled");

    /** TBC */
    NumericField VECTOR_EMBEDDINGS_UPDATED_AT =
            new NumericField("vectorEmbeddingsUpdatedAt", "vectorEmbeddingsUpdatedAt");

    /** List of groups who administer this asset. (This is only used for certain asset types.) */
    SortedSet<String> getAdminGroups();

    /** List of roles who administer this asset. (This is only used for Connection assets.) */
    SortedSet<String> getAdminRoles();

    /** List of users who administer this asset. (This is only used for certain asset types.) */
    SortedSet<String> getAdminUsers();

    /** Whether using this connection to run queries on the source is allowed (true) or not (false). */
    Boolean getAllowQuery();

    /** Whether using this connection to run preview queries on the source is allowed (true) or not (false). */
    Boolean getAllowQueryPreview();

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

    /** Expectation of data freshness from Source. */
    Long getAssetDQFreshnessExpectation();

    /** Value of data freshness from Source. */
    Long getAssetDQFreshnessValue();

    /** Overall result of all the dq rules. If any one rule failed, then fail else pass. */
    DataQualityResult getAssetDQResult();

    /** Qualified name of the column used for row scope filtering in DQ rules for this asset. */
    String getAssetDQRowScopeFilterColumnQualifiedName();

    /** List of all the dimensions of attached rules. */
    SortedSet<DataQualityDimension> getAssetDQRuleAttachedDimensions();

    /** List of all the types of attached rules. */
    SortedSet<String> getAssetDQRuleAttachedRuleTypes();

    /** Count of failed DQ rules attached to this asset. */
    Long getAssetDQRuleFailedCount();

    /** List of all the dimensions of failed rules. */
    SortedSet<DataQualityDimension> getAssetDQRuleFailedDimensions();

    /** List of all the types of failed rules. */
    SortedSet<String> getAssetDQRuleFailedRuleTypes();

    /** Time (epoch) at which the last dq rule ran. */
    Long getAssetDQRuleLastRunAt();

    /** Count of passed DQ rules attached to this asset. */
    Long getAssetDQRulePassedCount();

    /** List of all the dimensions for which all the rules passed. */
    SortedSet<DataQualityDimension> getAssetDQRulePassedDimensions();

    /** List of all the types of rules for which all the rules passed. */
    SortedSet<String> getAssetDQRulePassedRuleTypes();

    /** Tag for the result of the DQ rules. Eg, rule_pass:completeness:null_count. */
    SortedSet<String> getAssetDQRuleResultTags();

    /** Count of DQ rules attached to this asset. */
    Long getAssetDQRuleTotalCount();

    /** Crontab of the DQ rule that will run at datasource. */
    String getAssetDQScheduleCrontab();

    /** Error code in the case of sync state being "error". */
    String getAssetDQScheduleSourceSyncErrorCode();

    /** Error message in the case of sync state being "error". */
    String getAssetDQScheduleSourceSyncErrorMessage();

    /** Raw error message from the source. */
    String getAssetDQScheduleSourceSyncRawError();

    /** Latest sync status of the schedule to the source. */
    DataQualitySourceSyncStatus getAssetDQScheduleSourceSyncStatus();

    /** Time (epoch) at which the schedule synced to the source. */
    Long getAssetDQScheduleSourceSyncedAt();

    /** Timezone of the DQ rule schedule that will run at datasource */
    String getAssetDQScheduleTimeZone();

    /** Type of schedule of the DQ rule that will run at datasource. */
    DataQualityScheduleType getAssetDQScheduleType();

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

    /** DQ metadata captured for asset from external DQ tool(s). */
    Map<String, AssetExternalDQMetadata> getAssetExternalDQMetadataDetails();

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

    /** Name of the space that contains this asset. */
    String getAssetSpaceName();

    /** Unique name of the space that contains this asset. */
    String getAssetSpaceQualifiedName();

    /** List of tags attached to this asset. */
    SortedSet<String> getAssetTags();

    /** Color (in hexadecimal RGB) to use to represent this asset. */
    String getAssetThemeHex();

    /** Name to use for this type of asset, as a subtype of the actual typeName. */
    String getAssetUserDefinedType();

    /** Glossary terms that are linked to this asset. */
    SortedSet<IGlossaryTerm> getAssignedTerms();

    /** Type of connection, for example WAREHOUSE, RDBMS, etc. */
    AtlanConnectionCategory getCategory();

    /** Status of this asset's certification. */
    CertificateStatus getCertificateStatus();

    /** Human-readable descriptive message used to provide further detail to certificateStatus. */
    String getCertificateStatusMessage();

    /** Time (epoch) at which the certification was last updated, in milliseconds. */
    Long getCertificateUpdatedAt();

    /** Name of the user who last updated the certification of this asset. */
    String getCertificateUpdatedBy();

    /** Unique identifier (GUID) for the data quality credentials to use for this connection. */
    String getConnectionDQCredentialGuid();

    /** Error message if data quality environment setup failed for this connection. */
    String getConnectionDQEnvironmentSetupErrorMessage();

    /** Status of the data quality environment setup for this connection. */
    ConnectionDQEnvironmentSetupStatus getConnectionDQEnvironmentSetupStatus();

    /** Timestamp when the data quality environment setup status was last updated. */
    Long getConnectionDQEnvironmentSetupStatusUpdatedAt();

    /** Name of the database in the source environment for data quality. */
    String getConnectionDQEnvironmentSourceDatabaseName();

    /** TBC */
    SortedSet<String> getConnectionDbtEnvironments();

    /** Whether data quality is enabled for this connection (true) or not (false). */
    Boolean getConnectionIsDQEnabled();

    /** Simple name of the connection through which this asset is accessible. */
    String getConnectionName();

    /** Unique name of the connection through which this asset is accessible. */
    String getConnectionQualifiedName();

    /** Unique identifier (GUID) for the SSO credentials to use for this connection. */
    String getConnectionSSOCredentialGuid();

    /** Configuration for a workflow run. */
    Map<String, String> getConnectionWorkflowConfiguration();

    /** Unused. Only the value of connectorType impacts icons. */
    String getConnectorIcon();

    /** Unused. Only the value of connectorType impacts icons. */
    String getConnectorImage();

    /** Type of the connector through which this asset is accessible. */
    String getConnectorName();

    /** Credential strategy to use for this connection for queries. */
    String getCredentialStrategy();

    /** Latest version of the data contract (in any status) for this asset. */
    IDataContract getDataContractLatest();

    /** Latest certified version of the data contract for this asset. */
    IDataContract getDataContractLatestCertified();

    /** Unique name of this asset in dbt. */
    String getDbtQualifiedName();

    /** Unique identifier (GUID) for the default credentials to use for this connection. */
    String getDefaultCredentialGuid();

    /** Description of this asset, for example as crawled from a source. Fallback for display purposes, if userDescription is empty. */
    String getDescription();

    /** Human-readable name of this asset used for display purposes (in user interface). */
    String getDisplayName();

    /** Array of domain guids linked to this asset */
    SortedSet<String> getDomainGUIDs();

    /** Rules that are applied on this dataset. */
    SortedSet<IDataQualityRule> getDqBaseDatasetRules();

    /** Rules where this dataset is referenced. */
    SortedSet<IDataQualityRule> getDqReferenceDatasetRules();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** Whether this asset has contract (true) or not (false). */
    Boolean getHasContract();

    /** Whether this asset has lineage (true) or not (false). */
    Boolean getHasLineage();

    /** Whether this connection has popularity insights (true) or not (false). */
    Boolean getHasPopularityInsights();

    /** Host name of this connection's source. */
    String getHost();

    /** Data products for which this asset is an input port. */
    SortedSet<IDataProduct> getInputPortDataProducts();

    /** Connection process to which this asset provides input. */
    SortedSet<IConnectionProcess> getInputToConnectionProcesses();

    /** TBC */
    Boolean getIsAIGenerated();

    /** Whether this asset is discoverable through the UI (true) or not (false). */
    Boolean getIsDiscoverable();

    /** Whether this asset can be edited in the UI (true) or not (false). */
    Boolean getIsEditable();

    /** Indicates this asset is not fully-known, if true. */
    Boolean getIsPartial();

    /** Whether sample data can be previewed for this connection (true) or not (false). */
    Boolean getIsSampleDataPreviewEnabled();

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

    /** Number of rows after which results should be uploaded to storage. */
    Long getObjectStorageUploadThreshold();

    /** Connection processs from which this asset is produced as output. */
    SortedSet<IConnectionProcess> getOutputFromConnectionProcesses();

    /** Data products for which this asset is an output port. */
    SortedSet<IDataProduct> getOutputPortDataProducts();

    /** Array of product guids which have this asset as outputPort */
    SortedSet<String> getOutputProductGUIDs();

    /** List of groups who own this asset. */
    SortedSet<String> getOwnerGroups();

    /** List of users who own this asset. */
    SortedSet<String> getOwnerUsers();

    /** Policy strategy is a configuration that determines whether the Atlan policy will be applied to the results of insight queries and whether the query will be rewritten, applicable for stream api call made from insight screen */
    String getPolicyStrategy();

    /** Policy strategy is a configuration that determines whether the Atlan policy will be applied to the results of insight queries and whether the query will be rewritten. policyStrategyForSamplePreview config is applicable for sample preview call from assets screen */
    String getPolicyStrategyForSamplePreview();

    /** Number of days over which popularity is calculated, for example 30 days. */
    Long getPopularityInsightsTimeframe();

    /** Popularity score for this asset. */
    Double getPopularityScore();

    /** Port number to this connection's source. */
    Integer getPort();

    /** Credential strategy to use for this connection for preview queries. */
    String getPreviewCredentialStrategy();

    /** Array of product guids linked to this asset */
    SortedSet<String> getProductGUIDs();

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

    /** Unused. Only the value of connectorType impacts icons. */
    String getSourceLogo();

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

    /** Subcategory of this connection. */
    String getSubCategory();

    /** Subtype of this asset. */
    String getSubType();

    /** Name of the Atlan workspace in which this asset exists. */
    String getTenantId();

    /** Whether to upload to S3, GCP, or another storage location (true) or not (false). */
    Boolean getUseObjectStorage();

    /** TBC */
    SortedSet<IAsset> getUserDefRelationshipFroms();

    /** TBC */
    SortedSet<IAsset> getUserDefRelationshipTos();

    /** Description of this asset, as provided by a user. If present, this will be used for the description in user interface. */
    String getUserDescription();

    /** TBC */
    Boolean getVectorEmbeddingsEnabled();

    /** TBC */
    Long getVectorEmbeddingsUpdatedAt();

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
