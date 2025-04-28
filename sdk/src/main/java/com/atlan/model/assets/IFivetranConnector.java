/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.FivetranConnectorStatus;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.model.fields.BooleanField;
import com.atlan.model.fields.KeywordField;
import com.atlan.model.fields.KeywordTextField;
import com.atlan.model.fields.NumericField;
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
 * Instance of a Fivetran connector asset in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface IFivetranConnector {

    public static final String TYPE_NAME = "FivetranConnector";

    /** Total credits used by this destination */
    NumericField FIVETRAN_CONNECTOR_CREDITS_USED =
            new NumericField("fivetranConnectorCreditsUsed", "fivetranConnectorCreditsUsed");

    /** Destination name added by the user on Fivetran */
    KeywordField FIVETRAN_CONNECTOR_DESTINATION_NAME =
            new KeywordField("fivetranConnectorDestinationName", "fivetranConnectorDestinationName");

    /** Type of destination on Fivetran. Eg: redshift, bigquery etc. */
    KeywordField FIVETRAN_CONNECTOR_DESTINATION_TYPE =
            new KeywordField("fivetranConnectorDestinationType", "fivetranConnectorDestinationType");

    /** URL to open the destination details on Fivetran */
    KeywordField FIVETRAN_CONNECTOR_DESTINATION_URL =
            new KeywordField("fivetranConnectorDestinationURL", "fivetranConnectorDestinationURL");

    /** Extract time in seconds in the latest sync on fivetran */
    NumericField FIVETRAN_CONNECTOR_LAST_SYNC_EXTRACT_TIME_SECONDS = new NumericField(
            "fivetranConnectorLastSyncExtractTimeSeconds", "fivetranConnectorLastSyncExtractTimeSeconds");

    /** Extracted data volume in metabytes in the latest sync on Fivetran */
    NumericField FIVETRAN_CONNECTOR_LAST_SYNC_EXTRACT_VOLUME_MEGABYTES = new NumericField(
            "fivetranConnectorLastSyncExtractVolumeMegabytes", "fivetranConnectorLastSyncExtractVolumeMegabytes");

    /** Timestamp (epoch) when the latest sync finished on Fivetran, in milliseconds */
    NumericField FIVETRAN_CONNECTOR_LAST_SYNC_FINISHED_AT =
            new NumericField("fivetranConnectorLastSyncFinishedAt", "fivetranConnectorLastSyncFinishedAt");

    /** ID of the latest sync */
    KeywordField FIVETRAN_CONNECTOR_LAST_SYNC_ID =
            new KeywordField("fivetranConnectorLastSyncId", "fivetranConnectorLastSyncId");

    /** Load time in seconds in the latest sync on Fivetran */
    NumericField FIVETRAN_CONNECTOR_LAST_SYNC_LOAD_TIME_SECONDS =
            new NumericField("fivetranConnectorLastSyncLoadTimeSeconds", "fivetranConnectorLastSyncLoadTimeSeconds");

    /** Loaded data volume in metabytes in the latest sync on Fivetran */
    NumericField FIVETRAN_CONNECTOR_LAST_SYNC_LOAD_VOLUME_MEGABYTES = new NumericField(
            "fivetranConnectorLastSyncLoadVolumeMegabytes", "fivetranConnectorLastSyncLoadVolumeMegabytes");

    /** Process time in seconds in the latest sync on Fivetran */
    NumericField FIVETRAN_CONNECTOR_LAST_SYNC_PROCESS_TIME_SECONDS = new NumericField(
            "fivetranConnectorLastSyncProcessTimeSeconds", "fivetranConnectorLastSyncProcessTimeSeconds");

    /** Process volume in metabytes in the latest sync on Fivetran */
    NumericField FIVETRAN_CONNECTOR_LAST_SYNC_PROCESS_VOLUME_MEGABYTES = new NumericField(
            "fivetranConnectorLastSyncProcessVolumeMegabytes", "fivetranConnectorLastSyncProcessVolumeMegabytes");

    /** Failure reason for the latest sync on Fivetran. If status is FAILURE, this is the description of the reason why the sync failed. If status is FAILURE_WITH_TASK, this is the description of the Error. If status is RESCHEDULED, this is the description of the reason why the sync is rescheduled. */
    KeywordTextField FIVETRAN_CONNECTOR_LAST_SYNC_REASON = new KeywordTextField(
            "fivetranConnectorLastSyncReason",
            "fivetranConnectorLastSyncReason.keyword",
            "fivetranConnectorLastSyncReason");

    /** Timestamp (epoch) at which the latest sync is rescheduled at on Fivetran */
    NumericField FIVETRAN_CONNECTOR_LAST_SYNC_RESCHEDULED_AT =
            new NumericField("fivetranConnectorLastSyncRescheduledAt", "fivetranConnectorLastSyncRescheduledAt");

    /** Timestamp (epoch) when the latest sync started on Fivetran, in milliseconds */
    NumericField FIVETRAN_CONNECTOR_LAST_SYNC_STARTED_AT =
            new NumericField("fivetranConnectorLastSyncStartedAt", "fivetranConnectorLastSyncStartedAt");

    /** Number of tables synced in the latest sync on Fivetran */
    NumericField FIVETRAN_CONNECTOR_LAST_SYNC_TABLES_SYNCED =
            new NumericField("fivetranConnectorLastSyncTablesSynced", "fivetranConnectorLastSyncTablesSynced");

    /** Failure task type for the latest sync on Fivetran. If status is FAILURE_WITH_TASK or RESCHEDULED, this field displays the type of the Error that caused the failure or rescheduling, respectively, e.g., reconnect, update_service_account, etc. */
    KeywordField FIVETRAN_CONNECTOR_LAST_SYNC_TASK_TYPE =
            new KeywordField("fivetranConnectorLastSyncTaskType", "fivetranConnectorLastSyncTaskType");

    /** Total sync time in seconds in the latest sync on Fivetran */
    NumericField FIVETRAN_CONNECTOR_LAST_SYNC_TOTAL_TIME_SECONDS =
            new NumericField("fivetranConnectorLastSyncTotalTimeSeconds", "fivetranConnectorLastSyncTotalTimeSeconds");

    /** Increase in the percentage of free MAR compared to the previous month */
    NumericField FIVETRAN_CONNECTOR_MONTHLY_ACTIVE_ROWS_CHANGE_PERCENTAGE_FREE = new NumericField(
            "fivetranConnectorMonthlyActiveRowsChangePercentageFree",
            "fivetranConnectorMonthlyActiveRowsChangePercentageFree");

    /** Increase in the percentage of paid MAR compared to the previous month */
    NumericField FIVETRAN_CONNECTOR_MONTHLY_ACTIVE_ROWS_CHANGE_PERCENTAGE_PAID = new NumericField(
            "fivetranConnectorMonthlyActiveRowsChangePercentagePaid",
            "fivetranConnectorMonthlyActiveRowsChangePercentagePaid");

    /** Increase in the percentage of total MAR compared to the previous month */
    NumericField FIVETRAN_CONNECTOR_MONTHLY_ACTIVE_ROWS_CHANGE_PERCENTAGE_TOTAL = new NumericField(
            "fivetranConnectorMonthlyActiveRowsChangePercentageTotal",
            "fivetranConnectorMonthlyActiveRowsChangePercentageTotal");

    /** Free Monthly Active Rows used by the connector in the past month */
    NumericField FIVETRAN_CONNECTOR_MONTHLY_ACTIVE_ROWS_FREE =
            new NumericField("fivetranConnectorMonthlyActiveRowsFree", "fivetranConnectorMonthlyActiveRowsFree");

    /** Percentage of the account's total free MAR used by this connector */
    NumericField FIVETRAN_CONNECTOR_MONTHLY_ACTIVE_ROWS_FREE_PERCENTAGE_OF_ACCOUNT = new NumericField(
            "fivetranConnectorMonthlyActiveRowsFreePercentageOfAccount",
            "fivetranConnectorMonthlyActiveRowsFreePercentageOfAccount");

    /** Paid Monthly Active Rows used by the connector in the past month */
    NumericField FIVETRAN_CONNECTOR_MONTHLY_ACTIVE_ROWS_PAID =
            new NumericField("fivetranConnectorMonthlyActiveRowsPaid", "fivetranConnectorMonthlyActiveRowsPaid");

    /** Percentage of the account's total paid MAR used by this connector */
    NumericField FIVETRAN_CONNECTOR_MONTHLY_ACTIVE_ROWS_PAID_PERCENTAGE_OF_ACCOUNT = new NumericField(
            "fivetranConnectorMonthlyActiveRowsPaidPercentageOfAccount",
            "fivetranConnectorMonthlyActiveRowsPaidPercentageOfAccount");

    /** Total Monthly Active Rows used by the connector in the past month */
    NumericField FIVETRAN_CONNECTOR_MONTHLY_ACTIVE_ROWS_TOTAL =
            new NumericField("fivetranConnectorMonthlyActiveRowsTotal", "fivetranConnectorMonthlyActiveRowsTotal");

    /** Percentage of the account's total MAR used by this connector */
    NumericField FIVETRAN_CONNECTOR_MONTHLY_ACTIVE_ROWS_TOTAL_PERCENTAGE_OF_ACCOUNT = new NumericField(
            "fivetranConnectorMonthlyActiveRowsTotalPercentageOfAccount",
            "fivetranConnectorMonthlyActiveRowsTotalPercentageOfAccount");

    /** Connector name added by the user on Fivetran */
    KeywordField FIVETRAN_CONNECTOR_NAME = new KeywordField("fivetranConnectorName", "fivetranConnectorName");

    /** Sync frequency for the connector in number of hours. Eg: Every 6 hours */
    KeywordField FIVETRAN_CONNECTOR_SYNC_FREQUENCY =
            new KeywordField("fivetranConnectorSyncFrequency", "fivetranConnectorSyncFrequency");

    /** Boolean to indicate whether the sync for this connector is paused or not */
    BooleanField FIVETRAN_CONNECTOR_SYNC_PAUSED =
            new BooleanField("fivetranConnectorSyncPaused", "fivetranConnectorSyncPaused");

    /** Timestamp (epoch) on which the connector was setup on Fivetran, in milliseconds */
    NumericField FIVETRAN_CONNECTOR_SYNC_SETUP_ON =
            new NumericField("fivetranConnectorSyncSetupOn", "fivetranConnectorSyncSetupOn");

    /** Email ID of the user who setpu the connector on Fivetran */
    KeywordField FIVETRAN_CONNECTOR_SYNC_SETUP_USER_EMAIL =
            new KeywordField("fivetranConnectorSyncSetupUserEmail", "fivetranConnectorSyncSetupUserEmail");

    /** Full name of the user who setup the connector on Fivetran */
    KeywordField FIVETRAN_CONNECTOR_SYNC_SETUP_USER_FULL_NAME =
            new KeywordField("fivetranConnectorSyncSetupUserFullName", "fivetranConnectorSyncSetupUserFullName");

    /** Total five tables sorted by MAR synced by this connector */
    TextField FIVETRAN_CONNECTOR_TOP_TABLES_BY_MAR =
            new TextField("fivetranConnectorTopTablesByMAR", "fivetranConnectorTopTablesByMAR");

    /** Total number of tables synced by this connector */
    NumericField FIVETRAN_CONNECTOR_TOTAL_TABLES_SYNCED =
            new NumericField("fivetranConnectorTotalTablesSynced", "fivetranConnectorTotalTablesSynced");

    /** Type of connector on Fivetran. Eg: snowflake, google_analytics, notion etc. */
    KeywordField FIVETRAN_CONNECTOR_TYPE = new KeywordField("fivetranConnectorType", "fivetranConnectorType");

    /** URL to open the connector details on Fivetran */
    KeywordField FIVETRAN_CONNECTOR_URL = new KeywordField("fivetranConnectorURL", "fivetranConnectorURL");

    /** Total usage cost by this destination */
    NumericField FIVETRAN_CONNECTOR_USAGE_COST =
            new NumericField("fivetranConnectorUsageCost", "fivetranConnectorUsageCost");

    /** Processes related to this Fivetran connector */
    RelationField PROCESSES = new RelationField("processes");

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

    /** Application asset containing this Asset. */
    IApplication getApplication();

    /** ApplicationField asset containing this Asset. */
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

    /** TBC */
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

    /** List of tags attached to this asset. */
    SortedSet<String> getAssetTags();

    /** Color (in hexadecimal RGB) to use to represent this asset. */
    String getAssetThemeHex();

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
    AtlanConnectorType getConnectorType();

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

    /** Total credits used by this destination */
    Double getFivetranConnectorCreditsUsed();

    /** Destination name added by the user on Fivetran */
    String getFivetranConnectorDestinationName();

    /** Type of destination on Fivetran. Eg: redshift, bigquery etc. */
    String getFivetranConnectorDestinationType();

    /** URL to open the destination details on Fivetran */
    String getFivetranConnectorDestinationURL();

    /** Extract time in seconds in the latest sync on fivetran */
    Double getFivetranConnectorLastSyncExtractTimeSeconds();

    /** Extracted data volume in metabytes in the latest sync on Fivetran */
    Double getFivetranConnectorLastSyncExtractVolumeMegabytes();

    /** Timestamp (epoch) when the latest sync finished on Fivetran, in milliseconds */
    Long getFivetranConnectorLastSyncFinishedAt();

    /** ID of the latest sync */
    String getFivetranConnectorLastSyncId();

    /** Load time in seconds in the latest sync on Fivetran */
    Double getFivetranConnectorLastSyncLoadTimeSeconds();

    /** Loaded data volume in metabytes in the latest sync on Fivetran */
    Double getFivetranConnectorLastSyncLoadVolumeMegabytes();

    /** Process time in seconds in the latest sync on Fivetran */
    Double getFivetranConnectorLastSyncProcessTimeSeconds();

    /** Process volume in metabytes in the latest sync on Fivetran */
    Double getFivetranConnectorLastSyncProcessVolumeMegabytes();

    /** Failure reason for the latest sync on Fivetran. If status is FAILURE, this is the description of the reason why the sync failed. If status is FAILURE_WITH_TASK, this is the description of the Error. If status is RESCHEDULED, this is the description of the reason why the sync is rescheduled. */
    String getFivetranConnectorLastSyncReason();

    /** Timestamp (epoch) at which the latest sync is rescheduled at on Fivetran */
    Long getFivetranConnectorLastSyncRescheduledAt();

    /** Timestamp (epoch) when the latest sync started on Fivetran, in milliseconds */
    Long getFivetranConnectorLastSyncStartedAt();

    /** Number of tables synced in the latest sync on Fivetran */
    Long getFivetranConnectorLastSyncTablesSynced();

    /** Failure task type for the latest sync on Fivetran. If status is FAILURE_WITH_TASK or RESCHEDULED, this field displays the type of the Error that caused the failure or rescheduling, respectively, e.g., reconnect, update_service_account, etc. */
    String getFivetranConnectorLastSyncTaskType();

    /** Total sync time in seconds in the latest sync on Fivetran */
    Double getFivetranConnectorLastSyncTotalTimeSeconds();

    /** Increase in the percentage of free MAR compared to the previous month */
    Double getFivetranConnectorMonthlyActiveRowsChangePercentageFree();

    /** Increase in the percentage of paid MAR compared to the previous month */
    Double getFivetranConnectorMonthlyActiveRowsChangePercentagePaid();

    /** Increase in the percentage of total MAR compared to the previous month */
    Double getFivetranConnectorMonthlyActiveRowsChangePercentageTotal();

    /** Free Monthly Active Rows used by the connector in the past month */
    Long getFivetranConnectorMonthlyActiveRowsFree();

    /** Percentage of the account's total free MAR used by this connector */
    Double getFivetranConnectorMonthlyActiveRowsFreePercentageOfAccount();

    /** Paid Monthly Active Rows used by the connector in the past month */
    Long getFivetranConnectorMonthlyActiveRowsPaid();

    /** Percentage of the account's total paid MAR used by this connector */
    Double getFivetranConnectorMonthlyActiveRowsPaidPercentageOfAccount();

    /** Total Monthly Active Rows used by the connector in the past month */
    Long getFivetranConnectorMonthlyActiveRowsTotal();

    /** Percentage of the account's total MAR used by this connector */
    Double getFivetranConnectorMonthlyActiveRowsTotalPercentageOfAccount();

    /** Connector name added by the user on Fivetran */
    String getFivetranConnectorName();

    /** Sync frequency for the connector in number of hours. Eg: Every 6 hours */
    String getFivetranConnectorSyncFrequency();

    /** Boolean to indicate whether the sync for this connector is paused or not */
    Boolean getFivetranConnectorSyncPaused();

    /** Timestamp (epoch) on which the connector was setup on Fivetran, in milliseconds */
    Long getFivetranConnectorSyncSetupOn();

    /** Email ID of the user who setpu the connector on Fivetran */
    String getFivetranConnectorSyncSetupUserEmail();

    /** Full name of the user who setup the connector on Fivetran */
    String getFivetranConnectorSyncSetupUserFullName();

    /** Total five tables sorted by MAR synced by this connector */
    String getFivetranConnectorTopTablesByMAR();

    /** Total number of tables synced by this connector */
    Long getFivetranConnectorTotalTablesSynced();

    /** Type of connector on Fivetran. Eg: snowflake, google_analytics, notion etc. */
    String getFivetranConnectorType();

    /** URL to open the connector details on Fivetran */
    String getFivetranConnectorURL();

    /** Total usage cost by this destination */
    Double getFivetranConnectorUsageCost();

    /** Number of records updated in the latest sync on Fivetran */
    Long getFivetranLastSyncRecordsUpdated();

    /** Status of the latest sync on Fivetran. */
    FivetranConnectorStatus getFivetranLastSyncStatus();

    /** Name of the atlan fivetran workflow that updated this asset */
    String getFivetranWorkflowName();

    /** Whether this asset has contract (true) or not (false). */
    Boolean getHasContract();

    /** Whether this asset has lineage (true) or not (false). */
    Boolean getHasLineage();

    /** Data products for which this asset is an input port. */
    SortedSet<IDataProduct> getInputPortDataProducts();

    /** Tasks to which this asset provides input. */
    SortedSet<IAirflowTask> getInputToAirflowTasks();

    /** Processes to which this asset provides input. */
    SortedSet<ILineageProcess> getInputToProcesses();

    /** TBC */
    SortedSet<ISparkJob> getInputToSparkJobs();

    /** TBC */
    Boolean getIsAIGenerated();

    /** Whether this asset is discoverable through the UI (true) or not (false). */
    Boolean getIsDiscoverable();

    /** Whether this asset can be edited in the UI (true) or not (false). */
    Boolean getIsEditable();

    /** TBC */
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

    /** Attributes implemented by this asset. */
    SortedSet<IModelAttribute> getModelImplementedAttributes();

    /** Entities implemented by this asset. */
    SortedSet<IModelEntity> getModelImplementedEntities();

    /** Name of this asset. Fallback for display purposes, if displayName is empty. */
    String getName();

    /** Array of policy ids non-compliant to this asset */
    SortedSet<String> getNonCompliantAssetPolicyGUIDs();

    /** Tasks from which this asset is output. */
    SortedSet<IAirflowTask> getOutputFromAirflowTasks();

    /** Processes from which this asset is produced as output. */
    SortedSet<ILineageProcess> getOutputFromProcesses();

    /** TBC */
    SortedSet<ISparkJob> getOutputFromSparkJobs();

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

    /** Processes related to this Fivetran connector */
    SortedSet<ILineageProcess> getProcesses();

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
