/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum KeywordFields implements AtlanSearchableField {
    /** Unused. */
    ABBREVIATION("abbreviation"),
    /** Unused. */
    ADDITIONAL_ATTRIBUTES("additionalAttributes"),
    /** TBC */
    ADDITIONAL_INFO("additionalInfo"),
    /** TBC */
    ADLS_ACCOUNT_ACCESS_TIER("adlsAccountAccessTier"),
    /** TBC */
    ADLS_ACCOUNT_KIND("adlsAccountKind"),
    /** TBC */
    ADLS_ACCOUNT_PERFORMANCE("adlsAccountPerformance"),
    /** TBC */
    ADLS_ACCOUNT_PROVISION_STATE("adlsAccountProvisionState"),
    /** Unique name of the account for this ADLS asset. */
    ADLS_ACCOUNT_QUALIFIED_NAME("adlsAccountQualifiedName"),
    /** TBC */
    ADLS_ACCOUNT_REPLICATION("adlsAccountReplication"),
    /** TBC */
    ADLS_ACCOUNT_RESOURCE_GROUP("adlsAccountResourceGroup.keyword"),
    /** TBC */
    ADLS_ACCOUNT_SECONDARY_LOCATION("adlsAccountSecondaryLocation"),
    /** TBC */
    ADLS_ACCOUNT_SUBSCRIPTION("adlsAccountSubscription.keyword"),
    /** TBC */
    ADLS_CONTAINER_ENCRYPTION_SCOPE("adlsContainerEncryptionScope"),
    /** TBC */
    ADLS_CONTAINER_LEASE_STATE("adlsContainerLeaseState"),
    /** TBC */
    ADLS_CONTAINER_LEASE_STATUS("adlsContainerLeaseStatus"),
    /** Unique name of the container this object exists within. */
    ADLS_CONTAINER_QUALIFIED_NAME("adlsContainerQualifiedName"),
    /** TBC */
    ADLS_CONTAINER_URL("adlsContainerUrl.keyword"),
    /** TBC */
    ADLS_ENCRYPTION_TYPE("adlsEncryptionType"),
    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    ADLS_E_TAG("adlsETag"),
    /** TBC */
    ADLS_OBJECT_ACCESS_TIER("adlsObjectAccessTier"),
    /** TBC */
    ADLS_OBJECT_ARCHIVE_STATUS("adlsObjectArchiveStatus"),
    /** TBC */
    ADLS_OBJECT_CONTENT_LANGUAGE("adlsObjectContentLanguage.keyword"),
    /** TBC */
    ADLS_OBJECT_CONTENT_MD5HASH("adlsObjectContentMD5Hash"),
    /** TBC */
    ADLS_OBJECT_LEASE_STATE("adlsObjectLeaseState"),
    /** TBC */
    ADLS_OBJECT_LEASE_STATUS("adlsObjectLeaseStatus"),
    /** TBC */
    ADLS_OBJECT_METADATA("adlsObjectMetadata"),
    /** TBC */
    ADLS_OBJECT_TYPE("adlsObjectType"),
    /** TBC */
    ADLS_OBJECT_URL("adlsObjectUrl.keyword"),
    /** TBC */
    ADLS_OBJECT_VERSION_ID("adlsObjectVersionId"),
    /** TBC */
    ADLS_PRIMARY_DISK_STATE("adlsPrimaryDiskState"),
    /** List of groups who administer the asset. (This is only used for Connection assets.) */
    ADMIN_GROUPS("adminGroups"),
    /** List of roles who administer the asset. (This is only used for Connection assets.) */
    ADMIN_ROLES("adminRoles"),
    /** List of users who administer the asset. (This is only used for Connection assets.) */
    ADMIN_USERS("adminUsers"),
    /** TBC */
    AIRFLOW_DAG_NAME("airflowDagName.keyword"),
    /** TBC */
    AIRFLOW_DAG_QUALIFIED_NAME("airflowDagQualifiedName"),
    /** TBC */
    AIRFLOW_DAG_SCHEDULE("airflowDagSchedule"),
    /** Name of the run */
    AIRFLOW_RUN_NAME("airflowRunName"),
    /** OpenLineage state of the run */
    AIRFLOW_RUN_OPEN_LINEAGE_STATE("airflowRunOpenLineageState"),
    /** OpenLineage Version of the run */
    AIRFLOW_RUN_OPEN_LINEAGE_VERSION("airflowRunOpenLineageVersion"),
    /** Type of the run */
    AIRFLOW_RUN_TYPE("airflowRunType"),
    /** Airflow Version of the run */
    AIRFLOW_RUN_VERSION("airflowRunVersion"),
    /** TBC */
    AIRFLOW_TAGS("airflowTags"),
    /** TBC */
    AIRFLOW_TASK_CONNECTION_ID("airflowTaskConnectionId.keyword"),
    /** TBC */
    AIRFLOW_TASK_OPERATOR_CLASS("airflowTaskOperatorClass.keyword"),
    /** Pool on which this run happened */
    AIRFLOW_TASK_POOL("airflowTaskPool"),
    /** Queue on which this run happened */
    AIRFLOW_TASK_QUEUE("airflowTaskQueue"),
    /** TBC */
    AIRFLOW_TASK_SQL("airflowTaskSql"),
    /** Trigger rule of the run */
    AIRFLOW_TASK_TRIGGER_RULE("airflowTaskTriggerRule"),
    /** TBC */
    ALIAS("alias"),
    /** Detailed message to include in the announcement on this asset. */
    ANNOUNCEMENT_MESSAGE("announcementMessage"),
    /** Brief title for the announcement on this asset. Required when announcementType is specified. */
    ANNOUNCEMENT_TITLE("announcementTitle"),
    /** Type of announcement on the asset. */
    ANNOUNCEMENT_TYPE("announcementType"),
    /** Name of the user who last updated the announcement. */
    ANNOUNCEMENT_UPDATED_BY("announcementUpdatedBy"),
    /** TBC */
    API_EXTERNAL_DOCS("apiExternalDocs"),
    /** TBC */
    API_NAME("apiName.keyword"),
    /** TBC */
    API_PATH_AVAILABLE_OPERATIONS("apiPathAvailableOperations"),
    /** TBC */
    API_PATH_AVAILABLE_RESPONSE_CODES("apiPathAvailableResponseCodes"),
    /** TBC */
    API_PATH_RAW_URI("apiPathRawURI"),
    /** TBC */
    API_SPEC_CONTACT_EMAIL("apiSpecContactEmail"),
    /** TBC */
    API_SPEC_CONTACT_NAME("apiSpecContactName.keyword"),
    /** TBC */
    API_SPEC_CONTACT_URL("apiSpecContactURL"),
    /** TBC */
    API_SPEC_CONTRACT_VERSION("apiSpecContractVersion"),
    /** TBC */
    API_SPEC_LICENSE_NAME("apiSpecLicenseName.keyword"),
    /** TBC */
    API_SPEC_LICENSE_URL("apiSpecLicenseURL"),
    /** TBC */
    API_SPEC_NAME("apiSpecName.keyword"),
    /** TBC */
    API_SPEC_QUALIFIED_NAME("apiSpecQualifiedName"),
    /** TBC */
    API_SPEC_SERVICE_ALIAS("apiSpecServiceAlias"),
    /** TBC */
    API_SPEC_TERMS_OF_SERVICE_URL("apiSpecTermsOfServiceURL"),
    /** TBC */
    API_SPEC_TYPE("apiSpecType"),
    /** TBC */
    API_SPEC_VERSION("apiSpecVersion"),
    /** TBC */
    ASSET_DBT_ACCOUNT_NAME("assetDbtAccountName.keyword"),
    /** TBC */
    ASSET_DBT_ALIAS("assetDbtAlias.keyword"),
    /** TBC */
    ASSET_DBT_ENVIRONMENT_DBT_VERSION("assetDbtEnvironmentDbtVersion"),
    /** TBC */
    ASSET_DBT_ENVIRONMENT_NAME("assetDbtEnvironmentName.keyword"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_ARTIFACT_S3PATH("assetDbtJobLastRunArtifactS3Path"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_EXECUTED_BY_THREAD_ID("assetDbtJobLastRunExecutedByThreadId"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_GIT_BRANCH("assetDbtJobLastRunGitBranch"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_GIT_SHA("assetDbtJobLastRunGitSha"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_OWNER_THREAD_ID("assetDbtJobLastRunOwnerThreadId"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_QUEUED_DURATION("assetDbtJobLastRunQueuedDuration"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_QUEUED_DURATION_HUMANIZED("assetDbtJobLastRunQueuedDurationHumanized"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_RUN_DURATION("assetDbtJobLastRunRunDuration"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_RUN_DURATION_HUMANIZED("assetDbtJobLastRunRunDurationHumanized"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_STATUS_MESSAGE("assetDbtJobLastRunStatusMessage.keyword"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_TOTAL_DURATION("assetDbtJobLastRunTotalDuration"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_TOTAL_DURATION_HUMANIZED("assetDbtJobLastRunTotalDurationHumanized"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_URL("assetDbtJobLastRunUrl"),
    /** TBC */
    ASSET_DBT_JOB_NAME("assetDbtJobName.keyword"),
    /** TBC */
    ASSET_DBT_JOB_NEXT_RUN_HUMANIZED("assetDbtJobNextRunHumanized.keyword"),
    /** TBC */
    ASSET_DBT_JOB_SCHEDULE("assetDbtJobSchedule"),
    /** TBC */
    ASSET_DBT_JOB_STATUS("assetDbtJobStatus"),
    /** TBC */
    ASSET_DBT_META("assetDbtMeta"),
    /** TBC */
    ASSET_DBT_PACKAGE_NAME("assetDbtPackageName.keyword"),
    /** TBC */
    ASSET_DBT_PROJECT_NAME("assetDbtProjectName.keyword"),
    /** TBC */
    ASSET_DBT_SEMANTIC_LAYER_PROXY_URL("assetDbtSemanticLayerProxyUrl"),
    /** TBC */
    ASSET_DBT_SOURCE_FRESHNESS_CRITERIA("assetDbtSourceFreshnessCriteria"),
    /** TBC */
    ASSET_DBT_TAGS("assetDbtTags"),
    /** All associated dbt test statuses */
    ASSET_DBT_TEST_STATUS("assetDbtTestStatus"),
    /** TBC */
    ASSET_DBT_UNIQUE_ID("assetDbtUniqueId.keyword"),
    /** TBC */
    ASSET_MC_INCIDENT_NAMES("assetMcIncidentNames.keyword"),
    /** TBC */
    ASSET_MC_INCIDENT_QUALIFIED_NAMES("assetMcIncidentQualifiedNames"),
    /** TBC */
    ASSET_MC_INCIDENT_SEVERITIES("assetMcIncidentSeverities"),
    /** TBC */
    ASSET_MC_INCIDENT_STATES("assetMcIncidentStates"),
    /** TBC */
    ASSET_MC_INCIDENT_SUB_TYPES("assetMcIncidentSubTypes"),
    /** TBC */
    ASSET_MC_INCIDENT_TYPES("assetMcIncidentTypes"),
    /** TBC */
    ASSET_MC_MONITOR_NAMES("assetMcMonitorNames.keyword"),
    /** TBC */
    ASSET_MC_MONITOR_QUALIFIED_NAMES("assetMcMonitorQualifiedNames"),
    /** Schedules of all associated Monte Carlo monitors. */
    ASSET_MC_MONITOR_SCHEDULE_TYPES("assetMcMonitorScheduleTypes"),
    /** Statuses of all associated Monte Carlo monitors. */
    ASSET_MC_MONITOR_STATUSES("assetMcMonitorStatuses"),
    /** Types of all associated Monte Carlo monitors. */
    ASSET_MC_MONITOR_TYPES("assetMcMonitorTypes"),
    /** TBC */
    ASSET_TAGS("assetTags"),
    /** All terms attached to an asset, searchable by the term's qualifiedName. */
    ASSIGNED_TERMS("__meanings"),
    /** TBC */
    AST("ast"),
    /** TBC */
    ATLAS_SERVER_DISPLAY_NAME("AtlasServer.displayName"),
    /** TBC */
    ATLAS_SERVER_NAME("AtlasServer.name"),
    /** TBC */
    ATLAS_USER_PROFILE_NAME("__AtlasUserProfile.name"),
    /** TBC */
    ATLAS_USER_SAVED_SEARCH_NAME("__AtlasUserSavedSearch.name"),
    /** TBC */
    AUTH_SERVICE_CONFIG("authServiceConfig"),
    /** TBC */
    AUTH_SERVICE_TYPE("authServiceType"),
    /** 12-digit number that uniquely identifies an AWS account. */
    AWS_ACCOUNT_ID("awsAccountId"),
    /** Amazon Resource Name (ARN) for this asset. This uniquely identifies the asset in AWS, and thus must be unique across all AWS asset instances. */
    AWS_ARN("awsArn"),
    /** Root user's ID. */
    AWS_OWNER_ID("awsOwnerId"),
    /** Root user's name. */
    AWS_OWNER_NAME("awsOwnerName"),
    /** Group of AWS region and service objects. */
    AWS_PARTITION("awsPartition"),
    /** Physical region where the data center in which the asset exists is clustered. */
    AWS_REGION("awsRegion"),
    /** Unique resource ID assigned when a new resource is created. */
    AWS_RESOURCE_ID("awsResourceId"),
    /** Type of service in which the asset exists. */
    AWS_SERVICE("awsService"),
    /** List of tags that have been applied to the asset in AWS. */
    AWS_TAGS("awsTags"),
    /** TBC */
    AZURE_LOCATION("azureLocation"),
    /** TBC */
    AZURE_RESOURCE_ID("azureResourceId"),
    /** Tags that have been applied to this Azure asset. */
    AZURE_TAGS("azureTags"),
    /** TBC */
    BADGE_CONDITIONS("badgeConditions"),
    /** TBC */
    BADGE_METADATA_ATTRIBUTE("badgeMetadataAttribute"),
    /** Categories in which the term is organized, searchable by the qualifiedName of the category. */
    CATEGORIES("__categories"),
    /** Type of connection. */
    CATEGORY("category"),
    /** Status of the asset's certification. */
    CERTIFICATE_STATUS("certificateStatus"),
    /** Human-readable descriptive message that can optionally be submitted when the certificateStatus is changed. */
    CERTIFICATE_STATUS_MESSAGE("certificateStatusMessage"),
    /** Name of the user who last updated the certification of the asset. */
    CERTIFICATE_UPDATED_BY("certificateUpdatedBy"),
    /** TBC */
    CERTIFICATION_NOTE("certificationNote"),
    /** TBC */
    CERTIFIER("certifier"),
    /** TBC */
    CERTIFIER_DISPLAY_NAME("certifierDisplayName"),
    /** Link to a Slack channel that is used to discuss this access control object. */
    CHANNEL_LINK("channelLink"),
    /** TBC */
    CLIENT_ID("clientId"),
    /** Code that ran within the process. */
    CODE("code"),
    /** TBC */
    COLLECTION_QUALIFIED_NAME("collectionQualifiedName"),
    /** List of values in a histogram that represents the contents of the column. */
    COLUMN_HISTOGRAM("columnHistogram"),
    /** List of the greatest values in a column. */
    COLUMN_MAXS("columnMaxs"),
    /** List of the least values in a column. */
    COLUMN_MINS("columnMins"),
    /** TBC */
    COLUMN_TOP_VALUES("columnTopValues"),
    /** TBC */
    CONNECTION_DBT_ENVIRONMENTS("connectionDbtEnvironments"),
    /** TBC */
    CONNECTION_DETAILS("connectionDetails"),
    /** TBC */
    CONNECTION_NAME("connectionName"),
    /** Unique name of the connection through which this asset is accessible. */
    CONNECTION_QUALIFIED_NAME("connectionQualifiedName"),
    /** TBC */
    CONNECTION_SSO_CREDENTIAL_GUID("connectionSSOCredentialGuid"),
    /** Despite the name, this is not used for anything. Only the value of connectorName impacts icons. */
    CONNECTOR_ICON("connectorIcon"),
    /** Despite the name, this is not used for anything. Only the value of connectorName impacts icons. */
    CONNECTOR_IMAGE("connectorImage"),
    /** Type of the connector through which this asset is accessible. */
    CONNECTOR_TYPE("connectorName"),
    /** TBC */
    CONSTRAINT("constraint"),
    /** Atlan user who created this asset. */
    CREATED_BY("__createdBy"),
    /** TBC */
    CREDENTIAL_STRATEGY("credentialStrategy"),
    /** TBC */
    DASHBOARD_QUALIFIED_NAME("dashboardQualifiedName"),
    /** Type of dashboard in Salesforce. */
    DASHBOARD_TYPE("dashboardType"),
    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    DATABASE_NAME("databaseName.keyword"),
    /** Unique name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    DATABASE_QUALIFIED_NAME("databaseQualifiedName"),
    /** TBC */
    DATASET_QUALIFIED_NAME("datasetQualifiedName"),
    /** TBC */
    DATASOURCE_FIELD_TYPE("datasourceFieldType"),
    /** TBC */
    DATASOURCE_QUALIFIED_NAME("datasourceQualifiedName"),
    /** TBC */
    DATA_CATEGORY("dataCategory"),
    /** Owner of the asset within Google Data Studio. */
    DATA_STUDIO_ASSET_OWNER("dataStudioAssetOwner"),
    /** Title for the asset. */
    DATA_STUDIO_ASSET_TITLE("dataStudioAssetTitle.keyword"),
    /** Type of Google Data Studio asset. */
    DATA_STUDIO_ASSET_TYPE("dataStudioAssetType"),
    /** Data type of values in the field. */
    DATA_TYPE("dataType"),
    /** TBC */
    DBT_ACCOUNT_NAME("dbtAccountName.keyword"),
    /** TBC */
    DBT_ALIAS("dbtAlias.keyword"),
    /** TBC */
    DBT_COLUMN_PROCESS_JOB_STATUS("dbtColumnProcessJobStatus"),
    /** TBC */
    DBT_COMPILED_SQL("dbtCompiledSQL"),
    /** TBC */
    DBT_CONNECTION_CONTEXT("dbtConnectionContext"),
    /** TBC */
    DBT_ENVIRONMENT_DBT_VERSION("dbtEnvironmentDbtVersion.keyword"),
    /** TBC */
    DBT_ENVIRONMENT_NAME("dbtEnvironmentName.keyword"),
    /** TBC */
    DBT_ERROR("dbtError"),
    /** TBC */
    DBT_FRESHNESS_CRITERIA("dbtFreshnessCriteria"),
    /** TBC */
    DBT_JOB_NAME("dbtJobName.keyword"),
    /** TBC */
    DBT_JOB_NEXT_RUN_HUMANIZED("dbtJobNextRunHumanized.keyword"),
    /** TBC */
    DBT_JOB_SCHEDULE("dbtJobSchedule"),
    /** TBC */
    DBT_JOB_SCHEDULE_CRON_HUMANIZED("dbtJobScheduleCronHumanized.keyword"),
    /** TBC */
    DBT_JOB_STATUS("dbtJobStatus"),
    /** TBC */
    DBT_MATERIALIZATION_TYPE("dbtMaterializationType"),
    /** TBC */
    DBT_META("dbtMeta"),
    /** TBC */
    DBT_METRIC_FILTERS("dbtMetricFilters"),
    /** TBC */
    DBT_MODEL_COLUMN_DATA_TYPE("dbtModelColumnDataType"),
    /** TBC */
    DBT_MODEL_QUALIFIED_NAME("dbtModelQualifiedName"),
    /** TBC */
    DBT_PACKAGE_NAME("dbtPackageName.keyword"),
    /** TBC */
    DBT_PROCESS_JOB_STATUS("dbtProcessJobStatus"),
    /** TBC */
    DBT_PROJECT_NAME("dbtProjectName.keyword"),
    /** TBC */
    DBT_QUALIFIED_NAME("dbtQualifiedName"),
    /** TBC */
    DBT_RAW_SQL("dbtRawSQL"),
    /** TBC */
    DBT_SEMANTIC_LAYER_PROXY_URL("dbtSemanticLayerProxyUrl"),
    /** TBC */
    DBT_STATE("dbtState"),
    /** TBC */
    DBT_STATS("dbtStats"),
    /** TBC */
    DBT_STATUS("dbtStatus"),
    /** TBC */
    DBT_TAGS("dbtTags"),
    /** The compiled code of a test ( tests in dbt can be defined using python ) */
    DBT_TEST_COMPILED_CODE("dbtTestCompiledCode"),
    /** The compiled sql of a test */
    DBT_TEST_COMPILED_SQL("dbtTestCompiledSQL"),
    /** The error message in the case of state being "error" */
    DBT_TEST_ERROR("dbtTestError"),
    /** The language in which a dbt test is written. Example: sql,python */
    DBT_TEST_LANGUAGE("dbtTestLanguage"),
    /** The raw code of a test ( tests in dbt can be defined using python ) */
    DBT_TEST_RAW_CODE("dbtTestRawCode"),
    /** The raw sql of a test */
    DBT_TEST_RAW_SQL("dbtTestRawSQL"),
    /** The test results. Can be one of, in order of severity, "error", "fail", "warn", "pass" */
    DBT_TEST_STATE("dbtTestState"),
    /** Status provides the details of the results of a test. For errors, it reads "ERROR". */
    DBT_TEST_STATUS("dbtTestStatus"),
    /** TBC */
    DBT_UNIQUE_ID("dbtUniqueId.keyword"),
    /** TBC */
    DEFAULT_CREDENTIAL_GUID("defaultCredentialGuid"),
    /** TBC */
    DEFAULT_DATABASE_QUALIFIED_NAME("defaultDatabaseQualifiedName"),
    /** TBC */
    DEFAULT_SCHEMA_QUALIFIED_NAME("defaultSchemaQualifiedName"),
    /** TBC */
    DEFAULT_VALUE("defaultValue"),
    /** TBC */
    DEFAULT_VALUE_FORMULA("defaultValueFormula"),
    /** Definition of the materialized view (DDL). */
    DEFINITION("definition"),
    /** Asset sidebar tabs that should be hidden from this access control object. */
    DENY_ASSET_TABS("denyAssetTabs"),
    /** Unique identifiers (GUIDs) of custom metadata that should be hidden from this access control object. */
    DENY_CUSTOM_METADATA_GUIDS("denyCustomMetadataGuids"),
    /** Description of the asset, as crawled from a source. */
    DESCRIPTION("description.keyword"),
    /** List of column names on the report. */
    DETAIL_COLUMNS("detailColumns"),
    /** Name used for display purposes (in user interfaces). */
    DISPLAY_NAME("displayName.keyword"),
    /** Unused. */
    EXAMPLES("examples"),
    /** TBC */
    EXTERNAL_LOCATION("externalLocation"),
    /** TBC */
    EXTERNAL_LOCATION_FORMAT("externalLocationFormat"),
    /** TBC */
    EXTERNAL_LOCATION_REGION("externalLocationRegion"),
    /** TBC */
    FIELDS("fields"),
    /** URL giving the online location where the file can be accessed. */
    FILE_PATH("filePath"),
    /** Type of the file */
    FILE_TYPE("fileType"),
    /** TBC */
    FOLDER_NAME("folderName"),
    /** TBC */
    FORMULA("formula"),
    /** TBC */
    FULLY_QUALIFIED_NAME("fullyQualifiedName"),
    /** TBC */
    FULL_NAME("fullName"),
    /** Arguments that are passed in to the function. */
    FUNCTION_ARGUMENTS("functionArguments"),
    /** Code or set of statements that determine the output of the function. */
    FUNCTION_DEFINITION("functionDefinition"),
    /** The programming language in which the function is written. */
    FUNCTION_LANGUAGE("functionLanguage"),
    /** Data type of the value returned by the function. */
    FUNCTION_RETURN_TYPE("functionReturnType"),
    /** The type of function. */
    FUNCTION_TYPE("functionType"),
    /** TBC */
    GCS_ACCESS_CONTROL("gcsAccessControl"),
    /** Human-readable name of the bucket in which this object exists. */
    GCS_BUCKET_NAME("gcsBucketName.keyword"),
    /** qualifiedName of the bucket in which this object exists. */
    GCS_BUCKET_QUALIFIED_NAME("gcsBucketQualifiedName"),
    /** TBC */
    GCS_ENCRYPTION_TYPE("gcsEncryptionType"),
    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    GCS_E_TAG("gcsETag"),
    /** Information about how the object's content should be presented. */
    GCS_OBJECT_CONTENT_DISPOSITION("gcsObjectContentDisposition"),
    /** TBC */
    GCS_OBJECT_CONTENT_ENCODING("gcsObjectContentEncoding"),
    /** TBC */
    GCS_OBJECT_CONTENT_LANGUAGE("gcsObjectContentLanguage"),
    /** Type of content in the object. */
    GCS_OBJECT_CONTENT_TYPE("gcsObjectContentType"),
    /** TBC */
    GCS_OBJECT_CRC32C_HASH("gcsObjectCRC32CHash"),
    /** TBC */
    GCS_OBJECT_HOLD_TYPE("gcsObjectHoldType"),
    /** TBC */
    GCS_OBJECT_KEY("gcsObjectKey"),
    /** TBC */
    GCS_OBJECT_MD5HASH("gcsObjectMD5Hash"),
    /** TBC */
    GCS_OBJECT_MEDIA_LINK("gcsObjectMediaLink"),
    /** TBC */
    GCS_STORAGE_CLASS("gcsStorageClass"),
    /** Glossary in which the asset is contained, searchable by the qualifiedName of the glossary. */
    GLOSSARY("__glossary"),
    /** List of labels that have been applied to the asset in Google. */
    GOOGLE_LABELS("googleLabels"),
    /** TBC */
    GOOGLE_LOCATION("googleLocation"),
    /** TBC */
    GOOGLE_LOCATION_TYPE("googleLocationType"),
    /** ID of the project in which the asset exists. */
    GOOGLE_PROJECT_ID("googleProjectId"),
    /** Name of the project in which the asset exists. */
    GOOGLE_PROJECT_NAME("googleProjectName"),
    /** Service in Google in which the asset exists. */
    GOOGLE_SERVICE("googleService"),
    /** List of tags that have been applied to the asset in Google. */
    GOOGLE_TAGS("googleTags"),
    /** Globally unique identifier (GUID) of any object in Atlan. */
    GUID("__guid"),
    /** Host name of the connection's source. */
    HOST("host"),
    /** Icon for the link. */
    ICON("icon"),
    /** Type of icon for the link. */
    ICON_TYPE("iconType"),
    /** Assets that are inputs to this process. */
    INPUTS("inputs"),
    /** TBC */
    INPUT_FIELDS("inputFields"),
    /** TBC */
    KAFKA_CONSUMER_GROUP_TOPIC_CONSUMPTION_PROPERTIES("kafkaConsumerGroupTopicConsumptionProperties"),
    /** TBC */
    KAFKA_TOPIC_CLEANUP_POLICY("kafkaTopicCleanupPolicy"),
    /** TBC */
    KAFKA_TOPIC_COMPRESSION_TYPE("kafkaTopicCompressionType"),
    /** Names of the topics for this consumer group. */
    KAFKA_TOPIC_NAMES("kafkaTopicNames"),
    /** Unique names of the topics for this consumer group. */
    KAFKA_TOPIC_QUALIFIED_NAMES("kafkaTopicQualifiedNames"),
    /** Unused. */
    LANGUAGE("language"),
    /** Name of the last run of the crawler that last synchronized this asset. */
    LAST_SYNC_RUN("lastSyncRun"),
    /** Name of the crawler that last synchronized this asset. */
    LAST_SYNC_WORKFLOW_NAME("lastSyncWorkflowName"),
    /** TBC */
    LINK("link"),
    /** Unused. */
    LONG_DESCRIPTION("longDescription"),
    /** TBC */
    LOOKER_EXPLORE_QUALIFIED_NAME("lookerExploreQualifiedName"),
    /** TBC */
    LOOKER_FIELD_DATA_TYPE("lookerFieldDataType"),
    /** TBC */
    LOOKER_VIEW_QUALIFIED_NAME("lookerViewQualifiedName"),
    /** TBC */
    LOOKML_LINK_ID("lookmlLinkId"),
    /** Name of the classification in Atlan that is mapped to this tag. */
    MAPPED_ATLAN_TAG_NAME("mappedClassificationName"),
    /** TBC */
    MC_ASSET_QUALIFIED_NAMES("mcAssetQualifiedNames"),
    /** Unique identifier for the incident. */
    MC_INCIDENT_ID("mcIncidentId"),
    /** Severity of the incident. */
    MC_INCIDENT_SEVERITY("mcIncidentSeverity"),
    /** Status of the incident, for example whether it is being investigated or is already fixed. */
    MC_INCIDENT_STATE("mcIncidentState"),
    /** TBC */
    MC_INCIDENT_SUB_TYPES("mcIncidentSubTypes"),
    /** Type of incident. */
    MC_INCIDENT_TYPE("mcIncidentType"),
    /** Name of the warehouse in which the incident occurred. */
    MC_INCIDENT_WAREHOUSE("mcIncidentWarehouse"),
    /** TBC */
    MC_LABELS("mcLabels"),
    /** Unique identifier for the monitor. */
    MC_MONITOR_ID("mcMonitorId"),
    /** Namespace of the monitor. */
    MC_MONITOR_NAMESPACE("mcMonitorNamespace.keyword"),
    /** Comparison logic used for the rule. */
    MC_MONITOR_RULE_COMPARISONS("mcMonitorRuleComparisons"),
    /** SQL code for custom SQL rules. */
    MC_MONITOR_RULE_CUSTOM_SQL("mcMonitorRuleCustomSql"),
    /** Schedule details for the rule. */
    MC_MONITOR_RULE_SCHEDULE_CONFIG("mcMonitorRuleScheduleConfig"),
    /** Type of rule for the monitor. */
    MC_MONITOR_RULE_TYPE("mcMonitorRuleType"),
    /** Type of schedule for the monitor, for example fixed or dynamic. */
    MC_MONITOR_SCHEDULE_TYPE("mcMonitorScheduleType"),
    /** Status of the monitor. */
    MC_MONITOR_STATUS("mcMonitorStatus"),
    /** Type of monitor, for example field health (stats) or dimension tracking (categories). */
    MC_MONITOR_TYPE("mcMonitorType"),
    /** Name of the warehouse for the monitor. */
    MC_MONITOR_WAREHOUSE("mcMonitorWarehouse"),
    /** TBC */
    MERGE_RESULT_ID("mergeResultId"),
    /** TBC */
    METABASE_COLLECTION_NAME("metabaseCollectionName.keyword"),
    /** TBC */
    METABASE_COLLECTION_QUALIFIED_NAME("metabaseCollectionQualifiedName"),
    /** TBC */
    METABASE_COLOR("metabaseColor"),
    /** TBC */
    METABASE_NAMESPACE("metabaseNamespace"),
    /** TBC */
    METABASE_QUERY("metabaseQuery.keyword"),
    /** TBC */
    METABASE_QUERY_TYPE("metabaseQueryType"),
    /** TBC */
    METABASE_SLUG("metabaseSlug"),
    /** TBC */
    METRIC_SQL("metricSQL"),
    /** TBC */
    METRIC_TYPE("metricType"),
    /** Attribute form name, description, display format and expression as a JSON string. */
    MICRO_STRATEGY_ATTRIBUTE_FORMS("microStrategyAttributeForms"),
    /** Simple names of the related MicroStrategy attributes. */
    MICRO_STRATEGY_ATTRIBUTE_NAMES("microStrategyAttributeNames.keyword"),
    /** Unique names of the related MicroStrategy attributes. */
    MICRO_STRATEGY_ATTRIBUTE_QUALIFIED_NAMES("microStrategyAttributeQualifiedNames"),
    /** User who certified the asset in MicroStrategy. */
    MICRO_STRATEGY_CERTIFIED_BY("microStrategyCertifiedBy"),
    /** Simple names of the related MicroStrategy cubes. */
    MICRO_STRATEGY_CUBE_NAMES("microStrategyCubeNames.keyword"),
    /** Unique names of the related MicroStrategy cubes. */
    MICRO_STRATEGY_CUBE_QUALIFIED_NAMES("microStrategyCubeQualifiedNames"),
    /** Query used to create the cube. */
    MICRO_STRATEGY_CUBE_QUERY("microStrategyCubeQuery"),
    /** Whether the cube is an OLAP or MTDI cube. */
    MICRO_STRATEGY_CUBE_TYPE("microStrategyCubeType"),
    /** List of names of the dossier chapters. */
    MICRO_STRATEGY_DOSSIER_CHAPTER_NAMES("microStrategyDossierChapterNames"),
    /** Simple name of the dossier containing this visualization. */
    MICRO_STRATEGY_DOSSIER_NAME("microStrategyDossierName.keyword"),
    /** Unique name of the dossier containing this visualization. */
    MICRO_STRATEGY_DOSSIER_QUALIFIED_NAME("microStrategyDossierQualifiedName"),
    /** List of expressions for the fact. */
    MICRO_STRATEGY_FACT_EXPRESSIONS("microStrategyFactExpressions"),
    /** Simple names of the related MicroStrategy facts. */
    MICRO_STRATEGY_FACT_NAMES("microStrategyFactNames.keyword"),
    /** Unique names of the related MicroStrategy facts. */
    MICRO_STRATEGY_FACT_QUALIFIED_NAMES("microStrategyFactQualifiedNames"),
    /** Location of the asset within MicroStrategy. */
    MICRO_STRATEGY_LOCATION("microStrategyLocation"),
    /** Expression that defines the metric. */
    MICRO_STRATEGY_METRIC_EXPRESSION("microStrategyMetricExpression"),
    /** Simple names of the parent MicroStrategy metrics. */
    MICRO_STRATEGY_METRIC_PARENT_NAMES("microStrategyMetricParentNames.keyword"),
    /** Unique names of the parent MicroStrategy metrics. */
    MICRO_STRATEGY_METRIC_PARENT_QUALIFIED_NAMES("microStrategyMetricParentQualifiedNames"),
    /** Simple name of the related MicroStrategy project. */
    MICRO_STRATEGY_PROJECT_NAME("microStrategyProjectName.keyword"),
    /** Unique name of the related MicroStrategy project. */
    MICRO_STRATEGY_PROJECT_QUALIFIED_NAME("microStrategyProjectQualifiedName"),
    /** Simple names of the related MicroStrategy reports. */
    MICRO_STRATEGY_REPORT_NAMES("microStrategyReportNames.keyword"),
    /** Unique names of the related MicroStrategy reports. */
    MICRO_STRATEGY_REPORT_QUALIFIED_NAMES("microStrategyReportQualifiedNames"),
    /** Whether the report is a Grid or Chart. */
    MICRO_STRATEGY_REPORT_TYPE("microStrategyReportType"),
    /** Type of visualization. */
    MICRO_STRATEGY_VISUALIZATION_TYPE("microStrategyVisualizationType"),
    /** TBC */
    MODEL_NAME("modelName"),
    /** TBC */
    MODE_CHART_TYPE("modeChartType"),
    /** TBC */
    MODE_COLLECTION_STATE("modeCollectionState"),
    /** TBC */
    MODE_COLLECTION_TOKEN("modeCollectionToken"),
    /** TBC */
    MODE_COLLECTION_TYPE("modeCollectionType"),
    /** TBC */
    MODE_ID("modeId"),
    /** TBC */
    MODE_QUERY_NAME("modeQueryName.keyword"),
    /** TBC */
    MODE_QUERY_QUALIFIED_NAME("modeQueryQualifiedName"),
    /** TBC */
    MODE_REPORT_NAME("modeReportName.keyword"),
    /** TBC */
    MODE_REPORT_QUALIFIED_NAME("modeReportQualifiedName"),
    /** TBC */
    MODE_TOKEN("modeToken"),
    /** TBC */
    MODE_WORKSPACE_NAME("modeWorkspaceName.keyword"),
    /** TBC */
    MODE_WORKSPACE_QUALIFIED_NAME("modeWorkspaceQualifiedName"),
    /** TBC */
    MODE_WORKSPACE_USERNAME("modeWorkspaceUsername"),
    /** Atlan user who last updated the asset. */
    MODIFIED_BY("__modifiedBy"),
    /** Human-readable name of the asset. */
    NAME("name.keyword"),
    /** TBC */
    NOTE_TEXT("noteText"),
    /** TBC */
    OBJECT_QUALIFIED_NAME("objectQualifiedName"),
    /** TBC */
    OPERATION("operation"),
    /** TBC */
    OPERATION_PARAMS("operationParams"),
    /** TBC */
    ORGANIZATION_QUALIFIED_NAME("organizationQualifiedName"),
    /** Assets that are outputs from this process. */
    OUTPUTS("outputs"),
    /** TBC */
    OUTPUT_FIELDS("outputFields"),
    /** TBC */
    OUTPUT_STEPS("outputSteps"),
    /** List of groups who own the asset. */
    OWNER_GROUPS("ownerGroups"),
    /** TBC */
    OWNER_NAME("ownerName"),
    /** List of users who own the asset. */
    OWNER_USERS("ownerUsers"),
    /** TBC */
    PARAMS("params"),
    /** Parent category in which a subcategory is contained, searchable by the qualifiedName of the category. */
    PARENT_CATEGORY("__parentCategory"),
    /** TBC */
    PARENT_COLUMN_NAME("parentColumnName.keyword"),
    /** TBC */
    PARENT_COLUMN_QUALIFIED_NAME("parentColumnQualifiedName"),
    /** TBC */
    PARENT_QUALIFIED_NAME("parentQualifiedName"),
    /** TBC */
    PARTITION_LIST("partitionList"),
    /** TBC */
    PARTITION_STRATEGY("partitionStrategy"),
    /** Groups for whom this persona is accessible. */
    PERSONA_GROUPS("personaGroups"),
    /** Users for whom this persona is accessible. */
    PERSONA_USERS("personaUsers"),
    /** List of values from which a user can pick while adding a record. */
    PICKLIST_VALUES("picklistValues"),
    /** TBC */
    PINNED_BY("pinnedBy"),
    /** Actions included in the policy. */
    POLICY_ACTIONS("policyActions"),
    /** Category of access control object for the policy (for example, persona vs purpose). */
    POLICY_CATEGORY("policyCategory"),
    /** TBC */
    POLICY_CONDITIONS("policyConditions"),
    /** Groups to whom the policy applies. */
    POLICY_GROUPS("policyGroups"),
    /** TBC */
    POLICY_MASK_TYPE("policyMaskType"),
    /** Resources against which to apply the policy. */
    POLICY_RESOURCES("policyResources"),
    /** TBC */
    POLICY_RESOURCE_CATEGORY("policyResourceCategory"),
    /** TBC */
    POLICY_RESOURCE_SIGNATURE("policyResourceSignature"),
    /** Roles to whom the policy applies. */
    POLICY_ROLES("policyRoles"),
    /** Service that handles the policy (for example, atlas vs heka). */
    POLICY_SERVICE_NAME("policyServiceName"),
    /** TBC */
    POLICY_STRATEGY("policyStrategy"),
    /** Underlying kind of policy (for example, metadata vs data vs glossary). */
    POLICY_SUB_CATEGORY("policySubCategory"),
    /** Kind of policy (for example, allow vs deny). */
    POLICY_TYPE("policyType"),
    /** Users to whom the policy applies. */
    POLICY_USERS("policyUsers"),
    /** TBC */
    POLICY_VALIDITY_SCHEDULE("policyValiditySchedule"),
    /** TBC */
    POWER_BI_COLUMN_DATA_CATEGORY("powerBIColumnDataCategory"),
    /** TBC */
    POWER_BI_COLUMN_DATA_TYPE("powerBIColumnDataType"),
    /** TBC */
    POWER_BI_COLUMN_SUMMARIZE_BY("powerBIColumnSummarizeBy"),
    /** TBC */
    POWER_BI_ENDORSEMENT("powerBIEndorsement"),
    /** TBC */
    POWER_BI_FORMAT_STRING("powerBIFormatString"),
    /** TBC */
    POWER_BI_SORT_BY_COLUMN("powerBISortByColumn"),
    /** TBC */
    POWER_BI_TABLE_QUALIFIED_NAME("powerBITableQualifiedName"),
    /** TBC */
    POWER_BI_TABLE_SOURCE_EXPRESSIONS("powerBITableSourceExpressions"),
    /** TBC */
    PRESET_CHART_FORM_DATA("presetChartFormData"),
    /** Username of the user who last changed the collection. */
    PRESET_DASHBOARD_CHANGED_BY_NAME("presetDashboardChangedByName.keyword"),
    /** TBC */
    PRESET_DASHBOARD_CHANGED_BY_URL("presetDashboardChangedByURL"),
    /** qualifiedName of the Preset asset's collection. */
    PRESET_DASHBOARD_QUALIFIED_NAME("presetDashboardQualifiedName"),
    /** URL to a thumbnail illustration of the collection. */
    PRESET_DASHBOARD_THUMBNAIL_URL("presetDashboardThumbnailURL"),
    /** Name of the data source for the dataset. */
    PRESET_DATASET_DATASOURCE_NAME("presetDatasetDatasourceName.keyword"),
    /** Type of the dataset. */
    PRESET_DATASET_TYPE("presetDatasetType"),
    /** Hostname of the Preset workspace. */
    PRESET_WORKSPACE_HOSTNAME("presetWorkspaceHostname"),
    /** qualifiedName of the Preset asset's workspace. */
    PRESET_WORKSPACE_QUALIFIED_NAME("presetWorkspaceQualifiedName"),
    /** Region of the workspace. */
    PRESET_WORKSPACE_REGION("presetWorkspaceRegion"),
    /** Status of the workspace. */
    PRESET_WORKSPACE_STATUS("presetWorkspaceStatus"),
    /** TBC */
    PREVIEW_CREDENTIAL_STRATEGY("previewCredentialStrategy"),
    /** TBC */
    PROJECT_HIERARCHY("projectHierarchy"),
    /** TBC */
    PROJECT_NAME("projectName"),
    /** TBC */
    PROJECT_QUALIFIED_NAME("projectQualifiedName"),
    /** All propagated Atlan tags that exist on an asset, searchable by the internal hashed-string ID of the Atlan tag. */
    PROPAGATED_TRAIT_NAMES("__propagatedTraitNames"),
    /** Tags on which this purpose is applied. */
    PURPOSE_ATLAN_TAGS("purposeClassifications"),
    /** Unique identifier (in Qlik) of the app where the asset exists. */
    QLIK_APP_ID("qlikAppId"),
    /** Unique name of the app where the Qlik asset exists. */
    QLIK_APP_QUALIFIED_NAME("qlikAppQualifiedName"),
    /** Orientation of the chart. */
    QLIK_CHART_ORIENTATION("qlikChartOrientation"),
    /** Subtype of the chart, for example: bar, graph, pie, etc. */
    QLIK_CHART_TYPE("qlikChartType"),
    /** Subtype of the dataset. */
    QLIK_DATASET_SUBTYPE("qlikDatasetSubtype"),
    /** Technical name of the data asset. */
    QLIK_DATASET_TECHNICAL_NAME("qlikDatasetTechnicalName.keyword"),
    /** Type of the data asset, for example: qix-df, snowflake, etc. */
    QLIK_DATASET_TYPE("qlikDatasetType"),
    /** URI of the dataset. */
    QLIK_DATASET_URI("qlikDatasetUri"),
    /** Unique identifier of the Qlik asset in Qlik. */
    QLIK_ID("qlikId"),
    /** Origin App ID of the Qlik app. */
    QLIK_ORIGIN_APP_ID("qlikOriginAppId"),
    /** Unique identifier (in Qlik) of the owner of the asset. */
    QLIK_OWNER_ID("qlikOwnerId"),
    /** QRI of the Qlik object. */
    QLIK_QRI("qlikQRI"),
    /** Unique identifier (in Qlik) of the space where the asset exists. */
    QLIK_SPACE_ID("qlikSpaceId"),
    /** Unique name of the space where the Qlik asset exists. */
    QLIK_SPACE_QUALIFIED_NAME("qlikSpaceQualifiedName"),
    /** Type of space, for example: Private, Shared, etc. */
    QLIK_SPACE_TYPE("qlikSpaceType"),
    /** Unique fully-qualified name of the asset in Atlan. */
    QUALIFIED_NAME("qualifiedName"),
    /** TBC */
    QUERY_CONFIG("queryConfig"),
    /** TBC */
    QUERY_PREVIEW_CONFIG("queryPreviewConfig"),
    /** TBC */
    QUERY_USERNAME_STRATEGY("queryUsernameStrategy"),
    /** TBC */
    QUERY_USER_MAP("queryUserMap"),
    /** Calculated fields of quicksight analysis  */
    QUICK_SIGHT_ANALYSIS_CALCULATED_FIELDS("quickSightAnalysisCalculatedFields"),
    /** Filter groups used for quicksight analysis */
    QUICK_SIGHT_ANALYSIS_FILTER_GROUPS("quickSightAnalysisFilterGroups"),
    /** parameters used for quicksight analysis  */
    QUICK_SIGHT_ANALYSIS_PARAMETER_DECLARATIONS("quickSightAnalysisParameterDeclarations"),
    /** Qualified name of the QuickSight Analysis */
    QUICK_SIGHT_ANALYSIS_QUALIFIED_NAME("quickSightAnalysisQualifiedName"),
    /** Status of quicksight analysis */
    QUICK_SIGHT_ANALYSIS_STATUS("quickSightAnalysisStatus"),
    /** TBC */
    QUICK_SIGHT_DASHBOARD_QUALIFIED_NAME("quickSightDashboardQualifiedName"),
    /** Datatype of column in the dataset */
    QUICK_SIGHT_DATASET_FIELD_TYPE("quickSightDatasetFieldType"),
    /** Quicksight dataset importMode indicates a value that indicates whether you want to import the data into SPICE */
    QUICK_SIGHT_DATASET_IMPORT_MODE("quickSightDatasetImportMode"),
    /** Qualified name of the parent dataset */
    QUICK_SIGHT_DATASET_QUALIFIED_NAME("quickSightDatasetQualifiedName"),
    /** Detailed path of the folder */
    QUICK_SIGHT_FOLDER_HIERARCHY("quickSightFolderHierarchy"),
    /** Shared or private type of folder */
    QUICK_SIGHT_FOLDER_TYPE("quickSightFolderType"),
    /** TBC */
    QUICK_SIGHT_ID("quickSightId"),
    /** TBC */
    QUICK_SIGHT_SHEET_ID("quickSightSheetId"),
    /** TBC */
    QUICK_SIGHT_SHEET_NAME("quickSightSheetName.keyword"),
    /** TBC */
    RAW_DATA_TYPE_DEFINITION("rawDataTypeDefinition"),
    /** TBC */
    RAW_QUERY("rawQuery"),
    /** Name of the query from which the visualization was created. */
    REDASH_QUERY_NAME("redashQueryName.keyword"),
    /** Parameters for the Redash query. */
    REDASH_QUERY_PARAMETERS("redashQueryParameters"),
    /** Unique name of the query from which the visualization was created. */
    REDASH_QUERY_QUALIFIED_NAME("redashQueryQualifiedName"),
    /** Schedule of the Redash query. */
    REDASH_QUERY_SCHEDULE("redashQuerySchedule"),
    /** Human-readable schedule of the Redash query. */
    REDASH_QUERY_SCHEDULE_HUMANIZED("redashQueryScheduleHumanized"),
    /** SQL code of the Redash query. */
    REDASH_QUERY_SQL("redashQuerySQL"),
    /** Type of the Redash visualization. */
    REDASH_VISUALIZATION_TYPE("redashVisualizationType"),
    /** TBC */
    REFERENCE("reference"),
    /** TBC */
    REFRESH_METHOD("refreshMethod"),
    /** TBC */
    REFRESH_MODE("refreshMode"),
    /** Unused. */
    REPLICATED_FROM("replicatedFrom"),
    /** Unused. */
    REPLICATED_TO("replicatedTo"),
    /** TBC */
    REPORT_QUALIFIED_NAME("reportQualifiedName"),
    /** Type of report in Salesforce. */
    REPORT_TYPE("reportType"),
    /** TBC */
    RESOURCE_METADATA("resourceMetadata"),
    /** TBC */
    RESULT("result"),
    /** TBC */
    RESULT_SUMMARY("resultSummary"),
    /** TBC */
    ROLE("role"),
    /** TBC */
    ROLE_ID("roleId"),
    /** Name of the bucket in which the object exists. */
    S3BUCKET_NAME("s3BucketName"),
    /** qualifiedName of the bucket in which the object exists. */
    S3BUCKET_QUALIFIED_NAME("s3BucketQualifiedName"),
    /** TBC */
    S3ENCRYPTION("s3Encryption"),
    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    S3E_TAG("s3ETag"),
    /** Information about how the object's content should be presented. */
    S3OBJECT_CONTENT_DISPOSITION("s3ObjectContentDisposition"),
    /** Type of content in the object. */
    S3OBJECT_CONTENT_TYPE("s3ObjectContentType"),
    /** Unique identity of the object in an S3 bucket. This is usually the concatenation of any prefix (folder) in the S3 bucket with the name of the object (file) itself. */
    S3OBJECT_KEY("s3ObjectKey"),
    /** Storage class used for storing the object. */
    S3OBJECT_STORAGE_CLASS("s3ObjectStorageClass"),
    /** Version of the object. This is only applicable when versioning is enabled on the bucket in which the object exists. */
    S3OBJECT_VERSION_ID("s3ObjectVersionId"),
    /** TBC */
    SAMPLE_DATA_URL("sampleDataUrl"),
    /** TBC */
    SAVED_SEARCHES("savedSearches"),
    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    SCHEMA_NAME("schemaName.keyword"),
    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    SCHEMA_QUALIFIED_NAME("schemaQualifiedName"),
    /** Unique identifier for schema definition set by the schema registry */
    SCHEMA_REGISTRY_SCHEMA_ID("schemaRegistrySchemaId"),
    /** Type of language/specification used to define the schema like JSON, Protobuf etc. */
    SCHEMA_REGISTRY_SCHEMA_TYPE("schemaRegistrySchemaType"),
    /** Base name of the subject (i.e. without -key, -value prefixes) */
    SCHEMA_REGISTRY_SUBJECT_BASE_NAME("schemaRegistrySubjectBaseName"),
    /** List of asset qualified names that this subject is governing/validating. */
    SCHEMA_REGISTRY_SUBJECT_GOVERNING_ASSET_QUALIFIED_NAMES("schemaRegistrySubjectGoverningAssetQualifiedNames"),
    /** Latest schema version of the subject. */
    SCHEMA_REGISTRY_SUBJECT_LATEST_SCHEMA_VERSION("schemaRegistrySubjectLatestSchemaVersion"),
    /** Compatibility of the schema across versions. */
    SCHEMA_REGISTRY_SUBJECT_SCHEMA_COMPATIBILITY("schemaRegistrySubjectSchemaCompatibility"),
    /** TBC */
    SEARCH_PARAMETERS("searchParameters"),
    /** TBC */
    SEARCH_TYPE("searchType"),
    /** Unused. */
    SHORT_DESCRIPTION("shortDescription"),
    /** Human-readable name of the dataset that contains this column. */
    SIGMA_DATASET_NAME("sigmaDatasetName.keyword"),
    /** Unique name of the dataset that contains this column. */
    SIGMA_DATASET_QUALIFIED_NAME("sigmaDatasetQualifiedName"),
    /** TBC */
    SIGMA_DATA_ELEMENT_NAME("sigmaDataElementName.keyword"),
    /** TBC */
    SIGMA_DATA_ELEMENT_QUALIFIED_NAME("sigmaDataElementQualifiedName"),
    /** TBC */
    SIGMA_DATA_ELEMENT_QUERY("sigmaDataElementQuery"),
    /** TBC */
    SIGMA_DATA_ELEMENT_TYPE("sigmaDataElementType"),
    /** TBC */
    SIGMA_PAGE_NAME("sigmaPageName.keyword"),
    /** TBC */
    SIGMA_PAGE_QUALIFIED_NAME("sigmaPageQualifiedName"),
    /** TBC */
    SIGMA_WORKBOOK_NAME("sigmaWorkbookName.keyword"),
    /** TBC */
    SIGMA_WORKBOOK_QUALIFIED_NAME("sigmaWorkbookQualifiedName"),
    /** TBC */
    SITE_QUALIFIED_NAME("siteQualifiedName"),
    /** TBC */
    SNOWFLAKE_PIPE_NOTIFICATION_CHANNEL_NAME("snowflakePipeNotificationChannelName"),
    /** TBC */
    SNOWFLAKE_STREAM_MODE("snowflakeStreamMode"),
    /** TBC */
    SNOWFLAKE_STREAM_SOURCE_TYPE("snowflakeStreamSourceType"),
    /** TBC */
    SNOWFLAKE_STREAM_TYPE("snowflakeStreamType"),
    /** TBC */
    SOURCE_CONNECTION_NAME("sourceConnectionName"),
    /** The unit of measure for sourceTotalCost. */
    SOURCE_COST_UNIT("sourceCostUnit"),
    /** Who created the asset, in the source system. */
    SOURCE_CREATED_BY("sourceCreatedBy"),
    /** TBC */
    SOURCE_DEFINITION("sourceDefinition"),
    /** TBC */
    SOURCE_DEFINITION_DATABASE("sourceDefinitionDatabase"),
    /** TBC */
    SOURCE_DEFINITION_SCHEMA("sourceDefinitionSchema"),
    /** URL to create an embed for a resource (for example, an image of a dashboard) within Atlan. */
    SOURCE_EMBED_URL("sourceEmbedURL"),
    /** ID of the report in Salesforce. */
    SOURCE_ID("sourceId"),
    /** Despite the name, this is not used for anything. Only the value of connectorName impacts icons. */
    SOURCE_LOGO("sourceLogo"),
    /** TBC */
    SOURCE_OWNERS("sourceOwners"),
    /** List of most expensive warehouse names. */
    SOURCE_QUERY_COMPUTE_COSTS("sourceQueryComputeCostList"),
    /** List of most expensive warehouses with extra insights. */
    SOURCE_QUERY_COMPUTE_COST_RECORDS("sourceQueryComputeCostRecordList"),
    /** List of the most expensive queries that accessed this asset. */
    SOURCE_READ_EXPENSIVE_QUERY_RECORDS("sourceReadExpensiveQueryRecordList"),
    /** List of the most popular queries that accessed this asset. */
    SOURCE_READ_POPULAR_QUERY_RECORDS("sourceReadPopularQueryRecordList"),
    /** List of usernames of the most recent users who read the asset. */
    SOURCE_READ_RECENT_USERS("sourceReadRecentUserList"),
    /** List of usernames with extra insights for the most recent users who read the asset. */
    SOURCE_READ_RECENT_USER_RECORDS("sourceReadRecentUserRecordList"),
    /** List of the slowest queries that accessed this asset. */
    SOURCE_READ_SLOW_QUERY_RECORDS("sourceReadSlowQueryRecordList"),
    /** List of usernames of the users who read the asset the most. */
    SOURCE_READ_TOP_USERS("sourceReadTopUserList"),
    /** List of usernames with extra insights for the users who read the asset the most. */
    SOURCE_READ_TOP_USER_RECORDS("sourceReadTopUserRecordList"),
    /** TBC */
    SOURCE_SERVER_NAME("sourceServerName"),
    /** Who last updated the asset in the source system. */
    SOURCE_UPDATED_BY("sourceUpdatedBy"),
    /** URL to the resource within the source application. */
    SOURCE_URL("sourceURL"),
    /** SQL query that ran to produce the outputs. */
    SQL("sql"),
    /** TBC */
    SQL_TABLE_NAME("sqlTableName"),
    /** TBC */
    STALENESS("staleness"),
    /** Users who have starred this asset. */
    STARRED_BY("starredBy"),
    /** List of usernames with extra information of the users who have starred an asset */
    STARRED_DETAILS("starredDetailsList"),
    /** Asset status in Atlan (active vs deleted). */
    STATE("__state"),
    /** TBC */
    SUBTITLE_TEXT("subtitleText"),
    /** Subtype of the connection. */
    SUB_CATEGORY("subCategory"),
    /** TBC */
    SUB_DATA_TYPE("subDataType"),
    /** TBC */
    SUB_TYPE("subType"),
    /** All super types of an asset. */
    SUPER_TYPE_NAMES("__superTypeNames.keyword"),
    /** TBC */
    TABLEAU_DATASOURCE_FIELD_BIN_SIZE("tableauDatasourceFieldBinSize"),
    /** TBC */
    TABLEAU_DATASOURCE_FIELD_DATA_CATEGORY("tableauDatasourceFieldDataCategory"),
    /** TBC */
    TABLEAU_DATASOURCE_FIELD_DATA_TYPE("tableauDatasourceFieldDataType"),
    /** TBC */
    TABLEAU_DATASOURCE_FIELD_FORMULA("tableauDatasourceFieldFormula"),
    /** TBC */
    TABLEAU_DATASOURCE_FIELD_ROLE("tableauDatasourceFieldRole"),
    /** TBC */
    TABLEAU_DATA_TYPE("tableauDataType"),
    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    TABLE_NAME("tableName.keyword"),
    /** Unique name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    TABLE_QUALIFIED_NAME("tableQualifiedName"),
    /** Allowed values for the tag in the source system. These are denormalized from tagAttributes for ease of querying. */
    TAG_ALLOWED_VALUES("tagAllowedValues"),
    /** Attributes associated with the tag in the source system. */
    TAG_ATTRIBUTES("tagAttributes"),
    /** Unique identifier of the tag in the source system. */
    TAG_ID("tagId"),
    /** TBC */
    TAG_SERVICE("tagService"),
    /** TBC */
    TARGET_SERVER_NAME("targetServerName"),
    /** Name of the Atlan workspace in which the asset exists. */
    TENANT_ID("tenantId"),
    /** TBC */
    THOUGHTSPOT_CHART_TYPE("thoughtspotChartType"),
    /** Name of the Liveboard in which the Dashlet exists. */
    THOUGHTSPOT_LIVEBOARD_NAME("thoughtspotLiveboardName.keyword"),
    /** Unique name of the Liveboard in which the Dashlet exists. */
    THOUGHTSPOT_LIVEBOARD_QUALIFIED_NAME("thoughtspotLiveboardQualifiedName"),
    /** TBC */
    TOP_LEVEL_PROJECT_NAME("topLevelProjectName"),
    /** TBC */
    TOP_LEVEL_PROJECT_QUALIFIED_NAME("topLevelProjectQualifiedName"),
    /** All directly-assigned Atlan tags that exist on an asset, searchable by the internal hashed-string ID of the Atlan tag. */
    TRAIT_NAMES("__traitNames"),
    /** Type of the asset. For example Table, Column, and so on. */
    TYPE_NAME("__typeName.keyword"),
    /** TBC */
    UI_PARAMETERS("uiParameters"),
    /** TBC */
    UNIQUE_NAME("uniqueName"),
    /** TBC */
    UPSTREAM_COLUMNS("upstreamColumns"),
    /** TBC */
    UPSTREAM_DATASOURCES("upstreamDatasources"),
    /** TBC */
    UPSTREAM_FIELDS("upstreamFields"),
    /** TBC */
    UPSTREAM_TABLES("upstreamTables"),
    /** TBC */
    URLS("urls"),
    /** Unused. */
    USAGE("usage"),
    /** Description of the asset, as provided by a user. If present, this will be used for the description in user interfaces. If not present, the description will be used. */
    USER_DESCRIPTION("userDescription.keyword"),
    /** TBC */
    USER_NAME("userName"),
    /** TBC */
    VALIDATIONS("validations"),
    /** TBC */
    VARIABLES_SCHEMA_BASE64("variablesSchemaBase64"),
    /** TBC */
    VIEWER_GROUPS("viewerGroups"),
    /** TBC */
    VIEWER_USERS("viewerUsers"),
    /** TBC */
    VIEW_NAME("viewName"),
    /** Unique name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    VIEW_QUALIFIED_NAME("viewQualifiedName"),
    /** TBC */
    VISUAL_BUILDER_SCHEMA_BASE64("visualBuilderSchemaBase64"),
    /** TBC */
    WEB_URL("webUrl"),
    /** TBC */
    WORKBOOK_QUALIFIED_NAME("workbookQualifiedName"),
    /** TBC */
    WORKSPACE_QUALIFIED_NAME("workspaceQualifiedName"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    KeywordFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
