/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import javax.annotation.processing.Generated;
import lombok.Getter;

/**
 * Enumeration of all index fields.
 * @deprecated these are now enumerated as constants in the various asset interfaces, as instances of one of the following: {@link com.atlan.model.fields.BooleanField}, {@link com.atlan.model.fields.KeywordField}, {@link com.atlan.model.fields.KeywordTextField}, {@link com.atlan.model.fields.KeywordTextStemmedField}, {@link com.atlan.model.fields.NumericField}, {@link com.atlan.model.fields.NumericRankField}, {@link com.atlan.model.fields.RelationField}, or {@link com.atlan.model.fields.TextField}
 */
@Deprecated
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum KeywordFields implements AtlanSearchableField {
    /** Unused. Abbreviation of the term. */
    ABBREVIATION("abbreviation"),
    /** Unused. Arbitrary set of additional attributes for the terrm. */
    ADDITIONAL_ATTRIBUTES("additionalAttributes"),
    /** TBC */
    ADDITIONAL_INFO("additionalInfo"),
    /** Access tier of this account. */
    ADLS_ACCOUNT_ACCESS_TIER("adlsAccountAccessTier"),
    /** Kind of this account. */
    ADLS_ACCOUNT_KIND("adlsAccountKind"),
    /** Performance of this account. */
    ADLS_ACCOUNT_PERFORMANCE("adlsAccountPerformance"),
    /** Provision state of this account. */
    ADLS_ACCOUNT_PROVISION_STATE("adlsAccountProvisionState"),
    /** Unique name of the account for this ADLS asset. */
    ADLS_ACCOUNT_QUALIFIED_NAME("adlsAccountQualifiedName"),
    /** Replication of this account. */
    ADLS_ACCOUNT_REPLICATION("adlsAccountReplication"),
    /** Resource group for this account. */
    ADLS_ACCOUNT_RESOURCE_GROUP("adlsAccountResourceGroup.keyword"),
    /** Secondary location of the ADLS account. */
    ADLS_ACCOUNT_SECONDARY_LOCATION("adlsAccountSecondaryLocation"),
    /** Subscription for this account. */
    ADLS_ACCOUNT_SUBSCRIPTION("adlsAccountSubscription.keyword"),
    /** Encryption scope of this container. */
    ADLS_CONTAINER_ENCRYPTION_SCOPE("adlsContainerEncryptionScope"),
    /** Lease state of this container. */
    ADLS_CONTAINER_LEASE_STATE("adlsContainerLeaseState"),
    /** Lease status of this container. */
    ADLS_CONTAINER_LEASE_STATUS("adlsContainerLeaseStatus"),
    /** Unique name of the container this object exists within. */
    ADLS_CONTAINER_QUALIFIED_NAME("adlsContainerQualifiedName"),
    /** URL of this container. */
    ADLS_CONTAINER_URL("adlsContainerUrl.keyword"),
    /** Type of encryption for this account. */
    ADLS_ENCRYPTION_TYPE("adlsEncryptionType"),
    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    ADLS_E_TAG("adlsETag"),
    /** Access tier of this object. */
    ADLS_OBJECT_ACCESS_TIER("adlsObjectAccessTier"),
    /** Archive status of this object. */
    ADLS_OBJECT_ARCHIVE_STATUS("adlsObjectArchiveStatus"),
    /** Language of this object's contents. */
    ADLS_OBJECT_CONTENT_LANGUAGE("adlsObjectContentLanguage.keyword"),
    /** MD5 hash of this object's contents. */
    ADLS_OBJECT_CONTENT_MD5HASH("adlsObjectContentMD5Hash"),
    /** State of this object's lease. */
    ADLS_OBJECT_LEASE_STATE("adlsObjectLeaseState"),
    /** Status of this object's lease. */
    ADLS_OBJECT_LEASE_STATUS("adlsObjectLeaseStatus"),
    /** Metadata associated with this object, from ADLS. */
    ADLS_OBJECT_METADATA("adlsObjectMetadata"),
    /** Type of this object. */
    ADLS_OBJECT_TYPE("adlsObjectType"),
    /** URL of this object. */
    ADLS_OBJECT_URL("adlsObjectUrl.keyword"),
    /** Identifier of the version of this object, from ADLS. */
    ADLS_OBJECT_VERSION_ID("adlsObjectVersionId"),
    /** Primary disk state of this account. */
    ADLS_PRIMARY_DISK_STATE("adlsPrimaryDiskState"),
    /** List of groups who administer this asset. (This is only used for certain asset types.) */
    ADMIN_GROUPS("adminGroups"),
    /** List of roles who administer this asset. (This is only used for Connection assets.) */
    ADMIN_ROLES("adminRoles"),
    /** List of users who administer this asset. (This is only used for certain asset types.) */
    ADMIN_USERS("adminUsers"),
    /** Simple name of the DAG this task is contained within. */
    AIRFLOW_DAG_NAME("airflowDagName.keyword"),
    /** Unique name of the DAG this task is contained within. */
    AIRFLOW_DAG_QUALIFIED_NAME("airflowDagQualifiedName"),
    /** Schedule for the DAG. */
    AIRFLOW_DAG_SCHEDULE("airflowDagSchedule"),
    /** Name of the run. */
    AIRFLOW_RUN_NAME("airflowRunName"),
    /** State of the run in OpenLineage. */
    AIRFLOW_RUN_OPEN_LINEAGE_STATE("airflowRunOpenLineageState"),
    /** Version of the run in OpenLineage. */
    AIRFLOW_RUN_OPEN_LINEAGE_VERSION("airflowRunOpenLineageVersion"),
    /** Type of the run. */
    AIRFLOW_RUN_TYPE("airflowRunType"),
    /** Version of the run in Airflow. */
    AIRFLOW_RUN_VERSION("airflowRunVersion"),
    /** Tags assigned to the asset in Airflow. */
    AIRFLOW_TAGS("airflowTags"),
    /** Identifier for the connection this task accesses. */
    AIRFLOW_TASK_CONNECTION_ID("airflowTaskConnectionId.keyword"),
    /** Class name for the operator this task uses. */
    AIRFLOW_TASK_OPERATOR_CLASS("airflowTaskOperatorClass.keyword"),
    /** Pool on which this run happened. */
    AIRFLOW_TASK_POOL("airflowTaskPool"),
    /** Queue on which this run happened. */
    AIRFLOW_TASK_QUEUE("airflowTaskQueue"),
    /** SQL code that executes through this task. */
    AIRFLOW_TASK_SQL("airflowTaskSql"),
    /** Trigger for the run. */
    AIRFLOW_TASK_TRIGGER_RULE("airflowTaskTriggerRule"),
    /** Alias for this materialized view. */
    ALIAS("alias"),
    /** Detailed message to include in the announcement on this asset. */
    ANNOUNCEMENT_MESSAGE("announcementMessage"),
    /** Brief title for the announcement on this asset. Required when announcementType is specified. */
    ANNOUNCEMENT_TITLE("announcementTitle"),
    /** Type of announcement on this asset. */
    ANNOUNCEMENT_TYPE("announcementType"),
    /** Name of the user who last updated the announcement. */
    ANNOUNCEMENT_UPDATED_BY("announcementUpdatedBy"),
    /** External documentation of the API. */
    API_EXTERNAL_DOCS("apiExternalDocs"),
    /** Name of this asset in the Salesforce API. */
    API_NAME("apiName.keyword"),
    /** List of the operations available on the endpoint. */
    API_PATH_AVAILABLE_OPERATIONS("apiPathAvailableOperations"),
    /** Response codes available on the path across all operations. */
    API_PATH_AVAILABLE_RESPONSE_CODES("apiPathAvailableResponseCodes"),
    /** Absolute path to an individual endpoint. */
    API_PATH_RAW_URI("apiPathRawURI"),
    /** Email address for a contact responsible for the API specification. */
    API_SPEC_CONTACT_EMAIL("apiSpecContactEmail"),
    /** Name of the contact responsible for the API specification. */
    API_SPEC_CONTACT_NAME("apiSpecContactName.keyword"),
    /** URL pointing to the contact information. */
    API_SPEC_CONTACT_URL("apiSpecContactURL"),
    /** Version of the contract for the API specification. */
    API_SPEC_CONTRACT_VERSION("apiSpecContractVersion"),
    /** Name of the license under which the API specification is available. */
    API_SPEC_LICENSE_NAME("apiSpecLicenseName.keyword"),
    /** URL to the license under which the API specification is available. */
    API_SPEC_LICENSE_URL("apiSpecLicenseURL"),
    /** Simple name of the API spec, if this asset is contained in an API spec. */
    API_SPEC_NAME("apiSpecName.keyword"),
    /** Unique name of the API spec, if this asset is contained in an API spec. */
    API_SPEC_QUALIFIED_NAME("apiSpecQualifiedName"),
    /** Service alias for the API specification. */
    API_SPEC_SERVICE_ALIAS("apiSpecServiceAlias"),
    /** URL to the terms of service for the API specification. */
    API_SPEC_TERMS_OF_SERVICE_URL("apiSpecTermsOfServiceURL"),
    /** Type of API, for example: OpenAPI, GraphQL, etc. */
    API_SPEC_TYPE("apiSpecType"),
    /** Version of the API specification. */
    API_SPEC_VERSION("apiSpecVersion"),
    /** TBC */
    ASSET_COVER_IMAGE("assetCoverImage"),
    /** Name of the account in which this asset exists in dbt. */
    ASSET_DBT_ACCOUNT_NAME("assetDbtAccountName.keyword"),
    /** Alias of this asset in dbt. */
    ASSET_DBT_ALIAS("assetDbtAlias.keyword"),
    /** Version of the environment in which this asset is materialized in dbt. */
    ASSET_DBT_ENVIRONMENT_DBT_VERSION("assetDbtEnvironmentDbtVersion"),
    /** Name of the environment in which this asset is materialized in dbt. */
    ASSET_DBT_ENVIRONMENT_NAME("assetDbtEnvironmentName.keyword"),
    /** Path in S3 to the artifacts saved from the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_ARTIFACT_S3PATH("assetDbtJobLastRunArtifactS3Path"),
    /** Thread ID of the user who executed the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_EXECUTED_BY_THREAD_ID("assetDbtJobLastRunExecutedByThreadId"),
    /** Branch in git from which the last run of the job that materialized this asset in dbt ran. */
    ASSET_DBT_JOB_LAST_RUN_GIT_BRANCH("assetDbtJobLastRunGitBranch"),
    /** SHA hash in git for the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_GIT_SHA("assetDbtJobLastRunGitSha"),
    /** Thread ID of the owner of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_OWNER_THREAD_ID("assetDbtJobLastRunOwnerThreadId"),
    /** Total duration the job that materialized this asset in dbt spent being queued. */
    ASSET_DBT_JOB_LAST_RUN_QUEUED_DURATION("assetDbtJobLastRunQueuedDuration"),
    /** Human-readable total duration of the last run of the job that materialized this asset in dbt spend being queued. */
    ASSET_DBT_JOB_LAST_RUN_QUEUED_DURATION_HUMANIZED("assetDbtJobLastRunQueuedDurationHumanized"),
    /** Run duration of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_RUN_DURATION("assetDbtJobLastRunRunDuration"),
    /** Human-readable run duration of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_RUN_DURATION_HUMANIZED("assetDbtJobLastRunRunDurationHumanized"),
    /** Status message of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_STATUS_MESSAGE("assetDbtJobLastRunStatusMessage.keyword"),
    /** Total duration of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_TOTAL_DURATION("assetDbtJobLastRunTotalDuration"),
    /** Human-readable total duration of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_TOTAL_DURATION_HUMANIZED("assetDbtJobLastRunTotalDurationHumanized"),
    /** URL of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_URL("assetDbtJobLastRunUrl"),
    /** Name of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_NAME("assetDbtJobName.keyword"),
    /** Human-readable time when the next run of the job that materializes this asset in dbt is scheduled. */
    ASSET_DBT_JOB_NEXT_RUN_HUMANIZED("assetDbtJobNextRunHumanized.keyword"),
    /** Schedule of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_SCHEDULE("assetDbtJobSchedule"),
    /** Status of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_STATUS("assetDbtJobStatus"),
    /** Metadata for this asset in dbt, specifically everything under the 'meta' key in the dbt object. */
    ASSET_DBT_META("assetDbtMeta"),
    /** Name of the package in which this asset exists in dbt. */
    ASSET_DBT_PACKAGE_NAME("assetDbtPackageName.keyword"),
    /** Name of the project in which this asset exists in dbt. */
    ASSET_DBT_PROJECT_NAME("assetDbtProjectName.keyword"),
    /** URL of the semantic layer proxy for this asset in dbt. */
    ASSET_DBT_SEMANTIC_LAYER_PROXY_URL("assetDbtSemanticLayerProxyUrl"),
    /** Freshness criteria for the source of this asset in dbt. */
    ASSET_DBT_SOURCE_FRESHNESS_CRITERIA("assetDbtSourceFreshnessCriteria"),
    /** List of tags attached to this asset in dbt. */
    ASSET_DBT_TAGS("assetDbtTags"),
    /** All associated dbt test statuses. */
    ASSET_DBT_TEST_STATUS("assetDbtTestStatus"),
    /** Unique identifier of this asset in dbt. */
    ASSET_DBT_UNIQUE_ID("assetDbtUniqueId.keyword"),
    /** Name of the DBT workflow in Atlan that last updated the asset. */
    ASSET_DBT_WORKFLOW_LAST_UPDATED("assetDbtWorkflowLastUpdated"),
    /** Name of the icon to use for this asset. (Only applies to glossaries, currently.) */
    ASSET_ICON("assetIcon"),
    /** List of Monte Carlo incident names attached to this asset. */
    ASSET_MC_INCIDENT_NAMES("assetMcIncidentNames.keyword"),
    /** List of unique Monte Carlo incident names attached to this asset. */
    ASSET_MC_INCIDENT_QUALIFIED_NAMES("assetMcIncidentQualifiedNames"),
    /** List of Monte Carlo incident severities associated with this asset. */
    ASSET_MC_INCIDENT_SEVERITIES("assetMcIncidentSeverities"),
    /** List of Monte Carlo incident states associated with this asset. */
    ASSET_MC_INCIDENT_STATES("assetMcIncidentStates"),
    /** List of Monte Carlo incident sub-types associated with this asset. */
    ASSET_MC_INCIDENT_SUB_TYPES("assetMcIncidentSubTypes"),
    /** List of Monte Carlo incident types associated with this asset. */
    ASSET_MC_INCIDENT_TYPES("assetMcIncidentTypes"),
    /** List of Monte Carlo monitor names attached to this asset. */
    ASSET_MC_MONITOR_NAMES("assetMcMonitorNames.keyword"),
    /** List of unique Monte Carlo monitor names attached to this asset. */
    ASSET_MC_MONITOR_QUALIFIED_NAMES("assetMcMonitorQualifiedNames"),
    /** Schedules of all associated Monte Carlo monitors. */
    ASSET_MC_MONITOR_SCHEDULE_TYPES("assetMcMonitorScheduleTypes"),
    /** Statuses of all associated Monte Carlo monitors. */
    ASSET_MC_MONITOR_STATUSES("assetMcMonitorStatuses"),
    /** Types of all associated Monte Carlo monitors. */
    ASSET_MC_MONITOR_TYPES("assetMcMonitorTypes"),
    /** Array of policy ids governing this asset */
    ASSET_POLICY_GUI_DS("assetPolicyGUIDs"),
    /** All associated Soda check statuses. */
    ASSET_SODA_CHECK_STATUSES("assetSodaCheckStatuses"),
    /** Status of data quality from Soda. */
    ASSET_SODA_DQ_STATUS("assetSodaDQStatus"),
    /** TBC */
    ASSET_SODA_SOURCE_URL("assetSodaSourceURL"),
    /** List of tags attached to this asset. */
    ASSET_TAGS("assetTags"),
    /** Color (in hexadecimal RGB) to use to represent this asset. */
    ASSET_THEME_HEX("assetThemeHex"),
    /** All terms attached to an asset, searchable by the term's qualifiedName. */
    ASSIGNED_TERMS("__meanings"),
    /** Parsed AST of the code or SQL statements that describe the logic of this process. */
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
    AZURE_EVENT_HUB_STATUS("azureEventHubStatus"),
    /** Location of this asset in Azure. */
    AZURE_LOCATION("azureLocation"),
    /** Resource identifier of this asset in Azure. */
    AZURE_RESOURCE_ID("azureResourceId"),
    /** Simple name of the AzureServiceBus Namespace in which this asset exists. */
    AZURE_SERVICE_BUS_NAMESPACE_NAME("azureServiceBusNamespaceName.keyword"),
    /** Unique name of the AzureServiceBus Namespace in which this asset exists. */
    AZURE_SERVICE_BUS_NAMESPACE_QUALIFIED_NAME("azureServiceBusNamespaceQualifiedName"),
    /** Tags that have been applied to this asset in Azure. */
    AZURE_TAGS("azureTags"),
    /** List of conditions that determine the colors to diplay for various values. */
    BADGE_CONDITIONS("badgeConditions"),
    /** Custom metadata attribute for which to show the badge. */
    BADGE_METADATA_ATTRIBUTE("badgeMetadataAttribute"),
    /** Base parent Guid for policy used in version */
    BUSINESS_POLICY_BASE_PARENT_GUID("businessPolicyBaseParentGuid"),
    /** Business Policy Exception Filter ES DSL to denote the associate asset/s involved. */
    BUSINESS_POLICY_EXCEPTION_FILTER_DSL("businessPolicyExceptionFilterDSL"),
    /** List of groups who are part of this exception */
    BUSINESS_POLICY_EXCEPTION_GROUPS("businessPolicyExceptionGroups"),
    /** List of users who are part of this exception */
    BUSINESS_POLICY_EXCEPTION_USERS("businessPolicyExceptionUsers"),
    /** Business Policy Filter ES DSL to denote the associate asset/s involved. */
    BUSINESS_POLICY_FILTER_DSL("businessPolicyFilterDSL"),
    /** business policy guid for which log are created */
    BUSINESS_POLICY_ID("businessPolicyId"),
    /** Filter ES DSL to denote the associate asset/s involved. */
    BUSINESS_POLICY_INCIDENT_FILTER_DSL("businessPolicyIncidentFilterDSL"),
    /** policy ids related to this incident */
    BUSINESS_POLICY_INCIDENT_RELATED_POLICY_GUI_DS("businessPolicyIncidentRelatedPolicyGUIDs"),
    /** business policy type for which log are created */
    BUSINESS_POLICY_LOG_POLICY_TYPE("businessPolicyLogPolicyType"),
    /** Unique name of the business policy through which this asset is accessible. */
    BUSINESS_POLICY_QUALIFIED_NAME("businessPolicyQualifiedName"),
    /** Duration for the business policy to complete review. */
    BUSINESS_POLICY_REVIEW_PERIOD("businessPolicyReviewPeriod"),
    /** Selected approval workflow id for business policy */
    BUSINESS_POLICY_SELECTED_APPROVAL_WF("businessPolicySelectedApprovalWF"),
    /** Type of business policy */
    BUSINESS_POLICY_TYPE("businessPolicyType"),
    /** The owner who activated the calculation view */
    CALCULATION_VIEW_ACTIVATED_BY("calculationViewActivatedBy"),
    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    CALCULATION_VIEW_NAME("calculationViewName.keyword"),
    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    CALCULATION_VIEW_QUALIFIED_NAME("calculationViewQualifiedName"),
    /** Categories in which the term is organized, searchable by the qualifiedName of the category. */
    CATEGORIES("__categories"),
    /** Type of connection, for example WAREHOUSE, RDBMS, etc. */
    CATEGORY("category"),
    /** TBC */
    CATEGORY_TYPE("categoryType"),
    /** Status of this asset's certification. */
    CERTIFICATE_STATUS("certificateStatus"),
    /** Human-readable descriptive message used to provide further detail to certificateStatus. */
    CERTIFICATE_STATUS_MESSAGE("certificateStatusMessage"),
    /** Name of the user who last updated the certification of this asset. */
    CERTIFICATE_UPDATED_BY("certificateUpdatedBy"),
    /** Notes related to this datasource being cerfified, in Tableau. */
    CERTIFICATION_NOTE("certificationNote"),
    /** Users that have marked this datasource as cerified, in Tableau. */
    CERTIFIER("certifier"),
    /** Name of the user who cerified this datasource, in Tableau. */
    CERTIFIER_DISPLAY_NAME("certifierDisplayName"),
    /** TBC */
    CHANNEL_LINK("channelLink"),
    /** TBC */
    CLIENT_ID("clientId"),
    /** Code that ran within the process. */
    CODE("code"),
    /** Connection string of a cognos datasource */
    COGNOS_DATASOURCE_CONNECTION_STRING("cognosDatasourceConnectionString"),
    /** Tooltip text present for the Cognos asset */
    COGNOS_DEFAULT_SCREEN_TIP("cognosDefaultScreenTip"),
    /** ID of the asset in Cognos */
    COGNOS_ID("cognosId"),
    /** Name of the parent asset in Cognos */
    COGNOS_PARENT_NAME("cognosParentName.keyword"),
    /** Qualified name of the parent asset in Cognos */
    COGNOS_PARENT_QUALIFIED_NAME("cognosParentQualifiedName"),
    /** Path of the asset in Cognos. E.g. /content/folder[@name='Folder Name'] */
    COGNOS_PATH("cognosPath"),
    /** Cognos type of the Cognos asset. E.g. report, dashboard, package, etc. */
    COGNOS_TYPE("cognosType"),
    /** Version of the Cognos asset */
    COGNOS_VERSION("cognosVersion"),
    /** Unique name of the collection in which this query exists. */
    COLLECTION_QUALIFIED_NAME("collectionQualifiedName"),
    /** List of values in a histogram that represents the contents of this column. */
    COLUMN_HISTOGRAM("columnHistogram"),
    /** List of the greatest values in a column. */
    COLUMN_MAXS("columnMaxs"),
    /** List of the least values in a column. */
    COLUMN_MINS("columnMins"),
    /** List of top values in this column. */
    COLUMN_TOP_VALUES("columnTopValues"),
    /** TBC */
    CONNECTION_DBT_ENVIRONMENTS("connectionDbtEnvironments"),
    /** Connection details of the datasource. */
    CONNECTION_DETAILS("connectionDetails"),
    /** Simple name of the connection through which this asset is accessible. */
    CONNECTION_NAME("connectionName"),
    /** Unique name of the connection through which this asset is accessible. */
    CONNECTION_QUALIFIED_NAME("connectionQualifiedName"),
    /** Unique identifier (GUID) for the SSO credentials to use for this connection. */
    CONNECTION_SSO_CREDENTIAL_GUID("connectionSSOCredentialGuid"),
    /** Unused. Only the value of connectorType impacts icons. */
    CONNECTOR_ICON("connectorIcon"),
    /** Unused. Only the value of connectorType impacts icons. */
    CONNECTOR_IMAGE("connectorImage"),
    /** Type of the connector through which this asset is accessible. */
    CONNECTOR_TYPE("connectorName"),
    /** Constraint that defines this table partition. */
    CONSTRAINT("constraint"),
    /** Atlan user who created this asset. */
    CREATED_BY("__createdBy"),
    /** Credential strategy to use for this connection for queries. */
    CREDENTIAL_STRATEGY("credentialStrategy"),
    /** Simple name of the cube dimension in which this asset exists, or empty if it is itself a dimension. */
    CUBE_DIMENSION_NAME("cubeDimensionName.keyword"),
    /** Unique name of the cube dimension in which this asset exists, or empty if it is itself a dimension. */
    CUBE_DIMENSION_QUALIFIED_NAME("cubeDimensionQualifiedName"),
    /** Expression used to calculate this measure. */
    CUBE_FIELD_MEASURE_EXPRESSION("cubeFieldMeasureExpression.keyword"),
    /** Simple name of the dimension hierarchy in which this asset exists, or empty if it is itself a hierarchy. */
    CUBE_HIERARCHY_NAME("cubeHierarchyName.keyword"),
    /** Unique name of the dimension hierarchy in which this asset exists, or empty if it is itself a hierarchy. */
    CUBE_HIERARCHY_QUALIFIED_NAME("cubeHierarchyQualifiedName"),
    /** Simple name of the cube in which this asset exists, or empty if it is itself a cube. */
    CUBE_NAME("cubeName.keyword"),
    /** Name of the parent field in which this field is nested. */
    CUBE_PARENT_FIELD_NAME("cubeParentFieldName.keyword"),
    /** Unique name of the parent field in which this field is nested. */
    CUBE_PARENT_FIELD_QUALIFIED_NAME("cubeParentFieldQualifiedName"),
    /** Unique name of the cube in which this asset exists, or empty if it is itself a cube. */
    CUBE_QUALIFIED_NAME("cubeQualifiedName"),
    /** Criticality of this data product. */
    DAAP_CRITICALITY("daapCriticality"),
    /** Input ports guids for this data product. */
    DAAP_INPUT_PORT_GUIDS("daapInputPortGuids"),
    /** Output ports guids for this data product. */
    DAAP_OUTPUT_PORT_GUIDS("daapOutputPortGuids"),
    /** Information sensitivity of this data product. */
    DAAP_SENSITIVITY("daapSensitivity"),
    /** Status of this data product. */
    DAAP_STATUS("daapStatus"),
    /** Visibility of a data product. */
    DAAP_VISIBILITY("daapVisibility"),
    /** list of groups for product visibility control */
    DAAP_VISIBILITY_GROUPS("daapVisibilityGroups"),
    /** list of users for product visibility control */
    DAAP_VISIBILITY_USERS("daapVisibilityUsers"),
    /** Unique name of the dashboard in which this tile is pinned. */
    DASHBOARD_QUALIFIED_NAME("dashboardQualifiedName"),
    /** Type of dashboard in Salesforce. */
    DASHBOARD_TYPE("dashboardType"),
    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    DATABASE_NAME("databaseName.keyword"),
    /** Unique name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    DATABASE_QUALIFIED_NAME("databaseQualifiedName"),
    /** Unique name of the dataset used to build this report. */
    DATASET_QUALIFIED_NAME("datasetQualifiedName"),
    /** Type of this datasource field. */
    DATASOURCE_FIELD_TYPE("datasourceFieldType"),
    /** Unique name of the datasource in which this datasource field exists. */
    DATASOURCE_QUALIFIED_NAME("datasourceQualifiedName"),
    /** Data category of this field. */
    DATA_CATEGORY("dataCategory"),
    /** Unique identifier of the asset associated with this data contract. */
    DATA_CONTRACT_ASSET_GUID("dataContractAssetGuid"),
    /** (Deprecated) Replaced by dataContractSpec attribute. */
    DATA_CONTRACT_JSON("dataContractJson"),
    /** Actual content of the contract in YAML string format. Any changes to this string should create a new instance (with new sequential version number). */
    DATA_CONTRACT_SPEC("dataContractSpec"),
    /** Search DSL used to define which assets are part of this data product. */
    DATA_PRODUCT_ASSETS_DSL("dataProductAssetsDSL"),
    /** Playbook filter to define which assets are part of this data product. */
    DATA_PRODUCT_ASSETS_PLAYBOOK_FILTER("dataProductAssetsPlaybookFilter"),
    /** Criticality of this data product. */
    DATA_PRODUCT_CRITICALITY("dataProductCriticality"),
    /** Information sensitivity of this data product. */
    DATA_PRODUCT_SENSITIVITY("dataProductSensitivity"),
    /** Status of this data product. */
    DATA_PRODUCT_STATUS("dataProductStatus"),
    /** Visibility of a data product. */
    DATA_PRODUCT_VISIBILITY("dataProductVisibility"),
    /** Owner of the asset, from Google Data Studio. */
    DATA_STUDIO_ASSET_OWNER("dataStudioAssetOwner"),
    /** Title of the Google Data Studio asset. */
    DATA_STUDIO_ASSET_TITLE("dataStudioAssetTitle.keyword"),
    /** Type of the Google Data Studio asset, for example: REPORT or DATA_SOURCE. */
    DATA_STUDIO_ASSET_TYPE("dataStudioAssetType"),
    /** Data type of values in this field. */
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
    /** Unique name of this asset in dbt. */
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
    /** Compiled code of the test (when the test is defined using Python). */
    DBT_TEST_COMPILED_CODE("dbtTestCompiledCode"),
    /** Compiled SQL of the test. */
    DBT_TEST_COMPILED_SQL("dbtTestCompiledSQL"),
    /** Error message in the case of state being "error". */
    DBT_TEST_ERROR("dbtTestError"),
    /** Language in which the test is written, for example: SQL or Python. */
    DBT_TEST_LANGUAGE("dbtTestLanguage"),
    /** Raw code of the test (when the test is defined using Python). */
    DBT_TEST_RAW_CODE("dbtTestRawCode"),
    /** Raw SQL of the test. */
    DBT_TEST_RAW_SQL("dbtTestRawSQL"),
    /** Test results. Can be one of, in order of severity, "error", "fail", "warn", "pass". */
    DBT_TEST_STATE("dbtTestState"),
    /** Details of the results of the test. For errors, it reads "ERROR". */
    DBT_TEST_STATUS("dbtTestStatus"),
    /** TBC */
    DBT_UNIQUE_ID("dbtUniqueId.keyword"),
    /** Unique identifier (GUID) for the default credentials to use for this connection. */
    DEFAULT_CREDENTIAL_GUID("defaultCredentialGuid"),
    /** Unique name of the default database to use for this query. */
    DEFAULT_DATABASE_QUALIFIED_NAME("defaultDatabaseQualifiedName"),
    /** TBC */
    DEFAULT_NAVIGATION("defaultNavigation"),
    /** Unique name of the default schema to use for this query. */
    DEFAULT_SCHEMA_QUALIFIED_NAME("defaultSchemaQualifiedName"),
    /** Default value for this column. */
    DEFAULT_VALUE("defaultValue"),
    /** Formula for the default value for this field. */
    DEFAULT_VALUE_FORMULA("defaultValueFormula"),
    /** SQL definition of this materialized view. */
    DEFINITION("definition"),
    /** TBC */
    DENY_ASSET_FILTERS("denyAssetFilters"),
    /** TBC */
    DENY_ASSET_TABS("denyAssetTabs"),
    /** TBC */
    DENY_ASSET_TYPES("denyAssetTypes"),
    /** TBC */
    DENY_CUSTOM_METADATA_GUIDS("denyCustomMetadataGuids"),
    /** TBC */
    DENY_NAVIGATION_PAGES("denyNavigationPages"),
    /** Description of this asset, for example as crawled from a source. Fallback for display purposes, if userDescription is empty. */
    DESCRIPTION("description.keyword"),
    /** List of column names on the report. */
    DETAIL_COLUMNS("detailColumns"),
    /** Human-readable name of this asset used for display purposes (in user interface). */
    DISPLAY_NAME("displayName.keyword"),
    /** TBC */
    DISPLAY_PREFERENCES("displayPreferences"),
    /** Type of the Domo Card. */
    DOMO_CARD_TYPE("domoCardType"),
    /** Type of the Domo Card. */
    DOMO_CARD_TYPE_VALUE("domoCardTypeValue"),
    /** Type of Domo Dataset Column. */
    DOMO_DATASET_COLUMN_TYPE("domoDatasetColumnType"),
    /** An ISO-8601 representation of the time the DataSet was last run. */
    DOMO_DATASET_LAST_RUN("domoDatasetLastRun"),
    /** Qualified name of domo dataset of this column. */
    DOMO_DATASET_QUALIFIED_NAME("domoDatasetQualifiedName"),
    /** Id of the Domo dataset. */
    DOMO_ID("domoId"),
    /** Id of the owner of the Domo dataset. */
    DOMO_OWNER_ID("domoOwnerId"),
    /** Specifies the partition key of the DynamoDB Table/Index */
    DYNAMO_DB_PARTITION_KEY("dynamoDBPartitionKey"),
    /** Specifies attributes that are projected from the DynamoDB table into the index */
    DYNAMO_DB_SECONDARY_INDEX_PROJECTION_TYPE("dynamoDBSecondaryIndexProjectionType"),
    /** Specifies the sort key of the DynamoDB Table/Index */
    DYNAMO_DB_SORT_KEY("dynamoDBSortKey"),
    /** Status of the DynamoDB Asset */
    DYNAMO_DB_STATUS("dynamoDBStatus"),
    /** Unused. Exmaples of the term. */
    EXAMPLES("examples"),
    /** External location of this partition, for example: an S3 object location. */
    EXTERNAL_LOCATION("externalLocation"),
    /** Format of the external location of this partition, for example: JSON, CSV, PARQUET, etc. */
    EXTERNAL_LOCATION_FORMAT("externalLocationFormat"),
    /** Region of the external location of this partition, for example: S3 region. */
    EXTERNAL_LOCATION_REGION("externalLocationRegion"),
    /** Deprecated. */
    FIELDS("fields"),
    /** URL giving the online location where the file can be accessed. */
    FILE_PATH("filePath"),
    /** Type (extension) of the file. */
    FILE_TYPE("fileType"),
    /** Name of the parent folder in Looker that contains this dashboard. */
    FOLDER_NAME("folderName"),
    /** Formula for this field, if it is a calculated field. */
    FORMULA("formula"),
    /** Name used internally in Tableau to uniquely identify this field. */
    FULLY_QUALIFIED_NAME("fullyQualifiedName"),
    /** TBC */
    FULL_NAME("fullName"),
    /** Arguments that are passed in to the function. */
    FUNCTION_ARGUMENTS("functionArguments"),
    /** Code or set of statements that determine the output of the function. */
    FUNCTION_DEFINITION("functionDefinition"),
    /** Programming language in which the function is written. */
    FUNCTION_LANGUAGE("functionLanguage"),
    /** Data type of the value returned by the function. */
    FUNCTION_RETURN_TYPE("functionReturnType"),
    /** Type of function. */
    FUNCTION_TYPE("functionType"),
    /** Access control list for this asset. */
    GCS_ACCESS_CONTROL("gcsAccessControl"),
    /** Simple name of the bucket in which this object exists. */
    GCS_BUCKET_NAME("gcsBucketName.keyword"),
    /** Unique name of the bucket in which this object exists. */
    GCS_BUCKET_QUALIFIED_NAME("gcsBucketQualifiedName"),
    /** Encryption algorithm used to encrypt this asset. */
    GCS_ENCRYPTION_TYPE("gcsEncryptionType"),
    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    GCS_E_TAG("gcsETag"),
    /** Information about how this object's content should be presented. */
    GCS_OBJECT_CONTENT_DISPOSITION("gcsObjectContentDisposition"),
    /** Content encoding of this object. */
    GCS_OBJECT_CONTENT_ENCODING("gcsObjectContentEncoding"),
    /** Language of this object's contents. */
    GCS_OBJECT_CONTENT_LANGUAGE("gcsObjectContentLanguage"),
    /** Type of content in this object. */
    GCS_OBJECT_CONTENT_TYPE("gcsObjectContentType"),
    /** CRC32C hash of this object. */
    GCS_OBJECT_CRC32C_HASH("gcsObjectCRC32CHash"),
    /** Type of hold on this object. */
    GCS_OBJECT_HOLD_TYPE("gcsObjectHoldType"),
    /** Key of this object, in GCS. */
    GCS_OBJECT_KEY("gcsObjectKey"),
    /** MD5 hash of this object. */
    GCS_OBJECT_MD5HASH("gcsObjectMD5Hash"),
    /** Media link to this object. */
    GCS_OBJECT_MEDIA_LINK("gcsObjectMediaLink"),
    /** Storage class of this asset. */
    GCS_STORAGE_CLASS("gcsStorageClass"),
    /** Glossary in which the asset is contained, searchable by the qualifiedName of the glossary. */
    GLOSSARY("__glossary"),
    /** TBC */
    GLOSSARY_TYPE("glossaryType"),
    /** List of labels that have been applied to the asset in Google. */
    GOOGLE_LABELS("googleLabels"),
    /** Location of this asset in Google. */
    GOOGLE_LOCATION("googleLocation"),
    /** Type of location of this asset in Google. */
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
    /** Host name of this connection's source. */
    HOST("host"),
    /** Icon for the link. */
    ICON("icon"),
    /** Type of icon for the link, for example: image or emoji. */
    ICON_TYPE("iconType"),
    /** Status of this asset's severity. */
    INCIDENT_SEVERITY("incidentSeverity"),
    /** Assets that are inputs to this process. */
    INPUTS("inputs"),
    /** List of fields that are inputs to this flow. */
    INPUT_FIELDS("inputFields"),
    /** List of consumption properties for Kafka topics, for this consumer group. */
    KAFKA_CONSUMER_GROUP_TOPIC_CONSUMPTION_PROPERTIES("kafkaConsumerGroupTopicConsumptionProperties"),
    /** Cleanup policy for this topic. */
    KAFKA_TOPIC_CLEANUP_POLICY("kafkaTopicCleanupPolicy"),
    /** Type of compression used for this topic. */
    KAFKA_TOPIC_COMPRESSION_TYPE("kafkaTopicCompressionType"),
    /** Simple names of the topics consumed by this consumer group. */
    KAFKA_TOPIC_NAMES("kafkaTopicNames"),
    /** Unique names of the topics consumed by this consumer group. */
    KAFKA_TOPIC_QUALIFIED_NAMES("kafkaTopicQualifiedNames"),
    /** Unused. Language of the glossary's contents. */
    LANGUAGE("language"),
    /** Name of the last run of the crawler that last synchronized this asset. */
    LAST_SYNC_RUN("lastSyncRun"),
    /** Name of the crawler that last synchronized this asset. */
    LAST_SYNC_WORKFLOW_NAME("lastSyncWorkflowName"),
    /** URL to the resource. */
    LINK("link"),
    /** Unique name of the Linked Schema on which this Schema is dependent. This concept is mostly applicable for linked datasets/datasource in Google BigQuery via Analytics Hub Listing */
    LINKED_SCHEMA_QUALIFIED_NAME("linkedSchemaQualifiedName"),
    /** Unused. Detailed definition of the term. See 'readme' instead. */
    LONG_DESCRIPTION("longDescription"),
    /** Raw SQL query string. */
    LONG_RAW_QUERY("longRawQuery"),
    /** Unique name of the dashboard in which this field is used. */
    LOOKER_DASHBOARD_QUALIFIED_NAME("lookerDashboardQualifiedName"),
    /** Unique name of the Explore in which this field exists. */
    LOOKER_EXPLORE_QUALIFIED_NAME("lookerExploreQualifiedName"),
    /** Deprecated. */
    LOOKER_FIELD_DATA_TYPE("lookerFieldDataType"),
    /** Unique name of the look in which this field is used. */
    LOOKER_LOOK_QUALIFIED_NAME("lookerLookQualifiedName"),
    /** Unique name of the tile in which this field is used. */
    LOOKER_TILE_QUALIFIED_NAME("lookerTileQualifiedName"),
    /** File name of this view. */
    LOOKER_VIEW_FILE_NAME("lookerViewFileName"),
    /** File path of this view within the project. */
    LOOKER_VIEW_FILE_PATH("lookerViewFilePath"),
    /** Unique name of the view in which this field exists. */
    LOOKER_VIEW_QUALIFIED_NAME("lookerViewQualifiedName"),
    /** Identifier for the LoomML link. */
    LOOKML_LINK_ID("lookmlLinkId"),
    /** Name of the classification in Atlan that is mapped to this tag. */
    MAPPED_ATLAN_TAG_NAME("mappedClassificationName"),
    /** Unique identifier of the component in Matillion. */
    MATILLION_COMPONENT_ID("matillionComponentId"),
    /** Unique identifier for the type of the component in Matillion. */
    MATILLION_COMPONENT_IMPLEMENTATION_ID("matillionComponentImplementationId"),
    /** Last five run statuses of the component within a job. */
    MATILLION_COMPONENT_LAST_FIVE_RUN_STATUS("matillionComponentLastFiveRunStatus"),
    /** Latest run status of the component within a job. */
    MATILLION_COMPONENT_LAST_RUN_STATUS("matillionComponentLastRunStatus"),
    /** Job details of the job to which the component internally links. */
    MATILLION_COMPONENT_LINKED_JOB("matillionComponentLinkedJob"),
    /** SQL queries used by the component. */
    MATILLION_COMPONENT_SQLS("matillionComponentSqls"),
    /** List of environments in the project. */
    MATILLION_ENVIRONMENTS("matillionEnvironments"),
    /** Simple name of the Matillion group to which the project belongs. */
    MATILLION_GROUP_NAME("matillionGroupName.keyword"),
    /** Unique name of the Matillion group to which the project belongs. */
    MATILLION_GROUP_QUALIFIED_NAME("matillionGroupQualifiedName"),
    /** Simple name of the job to which the component belongs. */
    MATILLION_JOB_NAME("matillionJobName.keyword"),
    /** Path of the job within the project. Jobs can be managed at multiple folder levels within a project. */
    MATILLION_JOB_PATH("matillionJobPath"),
    /** Unique name of the job to which the component belongs. */
    MATILLION_JOB_QUALIFIED_NAME("matillionJobQualifiedName"),
    /** How the job is scheduled, for example: weekly or monthly. */
    MATILLION_JOB_SCHEDULE("matillionJobSchedule"),
    /** Type of the job, for example: orchestration or transformation. */
    MATILLION_JOB_TYPE("matillionJobType"),
    /** Simple name of the project to which the job belongs. */
    MATILLION_PROJECT_NAME("matillionProjectName.keyword"),
    /** Unique name of the project to which the job belongs. */
    MATILLION_PROJECT_QUALIFIED_NAME("matillionProjectQualifiedName"),
    /** Current point in time state of a project. */
    MATILLION_VERSION("matillionVersion"),
    /** List of versions in the project. */
    MATILLION_VERSIONS("matillionVersions"),
    /** List of unique names of assets that are part of this Monte Carlo asset. */
    MC_ASSET_QUALIFIED_NAMES("mcAssetQualifiedNames"),
    /** Identifier of this incident, from Monte Carlo. */
    MC_INCIDENT_ID("mcIncidentId"),
    /** Severity of this incident. */
    MC_INCIDENT_SEVERITY("mcIncidentSeverity"),
    /** State of this incident. */
    MC_INCIDENT_STATE("mcIncidentState"),
    /** Subtypes of this incident. */
    MC_INCIDENT_SUB_TYPES("mcIncidentSubTypes"),
    /** Type of this incident. */
    MC_INCIDENT_TYPE("mcIncidentType"),
    /** Name of this incident's warehouse. */
    MC_INCIDENT_WAREHOUSE("mcIncidentWarehouse"),
    /** List of labels for this Monte Carlo asset. */
    MC_LABELS("mcLabels"),
    /** Unique identifier for this monitor, from Monte Carlo. */
    MC_MONITOR_ID("mcMonitorId"),
    /** Namespace of this monitor. */
    MC_MONITOR_NAMESPACE("mcMonitorNamespace.keyword"),
    /** Comparison logic used for the rule. */
    MC_MONITOR_RULE_COMPARISONS("mcMonitorRuleComparisons"),
    /** SQL code for custom SQL rules. */
    MC_MONITOR_RULE_CUSTOM_SQL("mcMonitorRuleCustomSql"),
    /** Schedule details for the rule. */
    MC_MONITOR_RULE_SCHEDULE_CONFIG("mcMonitorRuleScheduleConfig"),
    /** Type of rule for this monitor. */
    MC_MONITOR_RULE_TYPE("mcMonitorRuleType"),
    /** Type of schedule for this monitor, for example: fixed or dynamic. */
    MC_MONITOR_SCHEDULE_TYPE("mcMonitorScheduleType"),
    /** Status of this monitor. */
    MC_MONITOR_STATUS("mcMonitorStatus"),
    /** Type of this monitor, for example: field health (stats) or dimension tracking (categories). */
    MC_MONITOR_TYPE("mcMonitorType"),
    /** Name of the warehouse for this monitor. */
    MC_MONITOR_WAREHOUSE("mcMonitorWarehouse"),
    /** Identifier for the merge result. */
    MERGE_RESULT_ID("mergeResultId"),
    /** Simple name of the Metabase collection in which this asset exists. */
    METABASE_COLLECTION_NAME("metabaseCollectionName.keyword"),
    /** Unique name of the Metabase collection in which this asset exists. */
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
    /** SQL query used to compute the metric. */
    METRIC_SQL("metricSQL"),
    /** Type of the metric. */
    METRIC_TYPE("metricType"),
    /** JSON string specifying the attribute's name, description, displayFormat, etc. */
    MICRO_STRATEGY_ATTRIBUTE_FORMS("microStrategyAttributeForms"),
    /** List of simple names of attributes related to this metric. */
    MICRO_STRATEGY_ATTRIBUTE_NAMES("microStrategyAttributeNames.keyword"),
    /** List of unique names of attributes related to this metric. */
    MICRO_STRATEGY_ATTRIBUTE_QUALIFIED_NAMES("microStrategyAttributeQualifiedNames"),
    /** User who certified this asset, in MicroStrategy. */
    MICRO_STRATEGY_CERTIFIED_BY("microStrategyCertifiedBy"),
    /** Simple names of the cubes related to this asset. */
    MICRO_STRATEGY_CUBE_NAMES("microStrategyCubeNames.keyword"),
    /** Unique names of the cubes related to this asset. */
    MICRO_STRATEGY_CUBE_QUALIFIED_NAMES("microStrategyCubeQualifiedNames"),
    /** Query used to create the cube. */
    MICRO_STRATEGY_CUBE_QUERY("microStrategyCubeQuery"),
    /** Type of cube, for example: OLAP or MTDI. */
    MICRO_STRATEGY_CUBE_TYPE("microStrategyCubeType"),
    /** List of chapter names in this dossier. */
    MICRO_STRATEGY_DOSSIER_CHAPTER_NAMES("microStrategyDossierChapterNames"),
    /** Simple name of the dossier in which this visualization exists. */
    MICRO_STRATEGY_DOSSIER_NAME("microStrategyDossierName.keyword"),
    /** Unique name of the dossier in which this visualization exists. */
    MICRO_STRATEGY_DOSSIER_QUALIFIED_NAME("microStrategyDossierQualifiedName"),
    /** List of expressions for this fact. */
    MICRO_STRATEGY_FACT_EXPRESSIONS("microStrategyFactExpressions"),
    /** List of simple names of facts related to this metric. */
    MICRO_STRATEGY_FACT_NAMES("microStrategyFactNames.keyword"),
    /** List of unique names of facts related to this metric. */
    MICRO_STRATEGY_FACT_QUALIFIED_NAMES("microStrategyFactQualifiedNames"),
    /** Location of this asset in MicroStrategy. */
    MICRO_STRATEGY_LOCATION("microStrategyLocation"),
    /** Text specifiying this metric's expression. */
    MICRO_STRATEGY_METRIC_EXPRESSION("microStrategyMetricExpression"),
    /** List of simple names of parent metrics of this metric. */
    MICRO_STRATEGY_METRIC_PARENT_NAMES("microStrategyMetricParentNames.keyword"),
    /** List of unique names of parent metrics of this metric. */
    MICRO_STRATEGY_METRIC_PARENT_QUALIFIED_NAMES("microStrategyMetricParentQualifiedNames"),
    /** Simple name of the project in which this asset exists. */
    MICRO_STRATEGY_PROJECT_NAME("microStrategyProjectName.keyword"),
    /** Unique name of the project in which this asset exists. */
    MICRO_STRATEGY_PROJECT_QUALIFIED_NAME("microStrategyProjectQualifiedName"),
    /** Simple names of the reports related to this asset. */
    MICRO_STRATEGY_REPORT_NAMES("microStrategyReportNames.keyword"),
    /** Unique names of the reports related to this asset. */
    MICRO_STRATEGY_REPORT_QUALIFIED_NAMES("microStrategyReportQualifiedNames"),
    /** Type of report, for example: Grid or Chart. */
    MICRO_STRATEGY_REPORT_TYPE("microStrategyReportType"),
    /** Type of visualization. */
    MICRO_STRATEGY_VISUALIZATION_TYPE("microStrategyVisualizationType"),
    /** Name of the parent model of this Explore. */
    MODEL_NAME("modelName"),
    /** Type of chart. */
    MODE_CHART_TYPE("modeChartType"),
    /** State of this collection. */
    MODE_COLLECTION_STATE("modeCollectionState"),
    /** TBC */
    MODE_COLLECTION_TOKEN("modeCollectionToken"),
    /** Type of this collection. */
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
    /** Subtype of a MongoDB collection, for example: Capped, Time Series, etc. */
    MONGO_DB_COLLECTION_SUBTYPE("mongoDBCollectionSubtype"),
    /** Name of the field containing the date in each time series document. */
    MONGO_DB_COLLECTION_TIME_FIELD("mongoDBCollectionTimeField"),
    /** Closest match to the time span between consecutive incoming measurements. */
    MONGO_DB_COLLECTION_TIME_GRANULARITY("mongoDBCollectionTimeGranularity"),
    /** Name of this asset. Fallback for display purposes, if displayName is empty. */
    NAME("name.keyword"),
    /** Text of notes added to the tile. */
    NOTE_TEXT("noteText"),
    /** Unique name of the object in which this field exists. */
    OBJECT_QUALIFIED_NAME("objectQualifiedName"),
    /** TBC */
    OPERATION("operation"),
    /** TBC */
    OPERATION_PARAMS("operationParams"),
    /** Fully-qualified name of the organization in Salesforce. */
    ORGANIZATION_QUALIFIED_NAME("organizationQualifiedName"),
    /** Assets that are outputs from this process. */
    OUTPUTS("outputs"),
    /** List of fields that are outputs from this flow. */
    OUTPUT_FIELDS("outputFields"),
    /** List of steps that are outputs from this flow. */
    OUTPUT_STEPS("outputSteps"),
    /** List of groups who own this asset. */
    OWNER_GROUPS("ownerGroups"),
    /** TBC */
    OWNER_NAME("ownerName"),
    /** List of users who own this asset. */
    OWNER_USERS("ownerUsers"),
    /** TBC */
    PARAMS("params"),
    /** Parent category in which a subcategory is contained, searchable by the qualifiedName of the category. */
    PARENT_CATEGORY("__parentCategory"),
    /** Simple name of the column this column is nested within, for STRUCT and NESTED columns. */
    PARENT_COLUMN_NAME("parentColumnName.keyword"),
    /** Unique name of the column this column is nested within, for STRUCT and NESTED columns. */
    PARENT_COLUMN_QUALIFIED_NAME("parentColumnQualifiedName"),
    /** Unique name of the parent domain in which this asset exists. */
    PARENT_DOMAIN_QUALIFIED_NAME("parentDomainQualifiedName"),
    /** Unique name of the parent collection or folder in which this query exists. */
    PARENT_QUALIFIED_NAME("parentQualifiedName"),
    /** List of sub-partitions in this partition. */
    PARTITION_LIST("partitionList"),
    /** Partition strategy of this partition. */
    PARTITION_STRATEGY("partitionStrategy"),
    /** TBC */
    PERSONA_GROUPS("personaGroups"),
    /** TBC */
    PERSONA_USERS("personaUsers"),
    /** List of values from which a user can pick while adding a record. */
    PICKLIST_VALUES("picklistValues"),
    /** User who pinned this column. */
    PINNED_BY("pinnedBy"),
    /** TBC */
    POLICY_ACTIONS("policyActions"),
    /** TBC */
    POLICY_CATEGORY("policyCategory"),
    /** TBC */
    POLICY_CONDITIONS("policyConditions"),
    /** TBC */
    POLICY_GROUPS("policyGroups"),
    /** TBC */
    POLICY_MASK_TYPE("policyMaskType"),
    /** TBC */
    POLICY_RESOURCES("policyResources"),
    /** TBC */
    POLICY_RESOURCE_CATEGORY("policyResourceCategory"),
    /** TBC */
    POLICY_RESOURCE_SIGNATURE("policyResourceSignature"),
    /** TBC */
    POLICY_ROLES("policyRoles"),
    /** TBC */
    POLICY_SERVICE_NAME("policyServiceName"),
    /** Policy strategy is a configuration that determines whether the Atlan policy will be applied to the results of insight queries and whether the query will be rewritten, applicable for stream api call made from insight screen */
    POLICY_STRATEGY("policyStrategy"),
    /** Policy strategy is a configuration that determines whether the Atlan policy will be applied to the results of insight queries and whether the query will be rewritten. policyStrategyForSamplePreview config is applicable for sample preview call from assets screen */
    POLICY_STRATEGY_FOR_SAMPLE_PREVIEW("policyStrategyForSamplePreview"),
    /** TBC */
    POLICY_SUB_CATEGORY("policySubCategory"),
    /** TBC */
    POLICY_TYPE("policyType"),
    /** TBC */
    POLICY_USERS("policyUsers"),
    /** TBC */
    POLICY_VALIDITY_SCHEDULE("policyValiditySchedule"),
    /** Data category that describes the data in this column. */
    POWER_BI_COLUMN_DATA_CATEGORY("powerBIColumnDataCategory"),
    /** Data type of this column. */
    POWER_BI_COLUMN_DATA_TYPE("powerBIColumnDataType"),
    /** Aggregate function to use for summarizing this column. */
    POWER_BI_COLUMN_SUMMARIZE_BY("powerBIColumnSummarizeBy"),
    /** Endorsement status of this asset, in Power BI. */
    POWER_BI_ENDORSEMENT("powerBIEndorsement"),
    /** Format of this asset, as specified in the FORMAT_STRING of the MDX cell property. */
    POWER_BI_FORMAT_STRING("powerBIFormatString"),
    /** Name of a column in the same table to use to order this column. */
    POWER_BI_SORT_BY_COLUMN("powerBISortByColumn"),
    /** Unique name of the Power BI table in which this asset exists. */
    POWER_BI_TABLE_QUALIFIED_NAME("powerBITableQualifiedName"),
    /** Power Query M expressions for the table. */
    POWER_BI_TABLE_SOURCE_EXPRESSIONS("powerBITableSourceExpressions"),
    /** TBC */
    PRESET_CHART_FORM_DATA("presetChartFormData"),
    /** TBC */
    PRESET_DASHBOARD_CHANGED_BY_NAME("presetDashboardChangedByName.keyword"),
    /** TBC */
    PRESET_DASHBOARD_CHANGED_BY_URL("presetDashboardChangedByURL"),
    /** Unique name of the dashboard in which this asset exists. */
    PRESET_DASHBOARD_QUALIFIED_NAME("presetDashboardQualifiedName"),
    /** TBC */
    PRESET_DASHBOARD_THUMBNAIL_URL("presetDashboardThumbnailURL"),
    /** TBC */
    PRESET_DATASET_DATASOURCE_NAME("presetDatasetDatasourceName.keyword"),
    /** TBC */
    PRESET_DATASET_TYPE("presetDatasetType"),
    /** TBC */
    PRESET_WORKSPACE_HOSTNAME("presetWorkspaceHostname"),
    /** Unique name of the workspace in which this asset exists. */
    PRESET_WORKSPACE_QUALIFIED_NAME("presetWorkspaceQualifiedName"),
    /** TBC */
    PRESET_WORKSPACE_REGION("presetWorkspaceRegion"),
    /** TBC */
    PRESET_WORKSPACE_STATUS("presetWorkspaceStatus"),
    /** Credential strategy to use for this connection for preview queries. */
    PREVIEW_CREDENTIAL_STRATEGY("previewCredentialStrategy"),
    /** List of top-level projects and their nested child projects. */
    PROJECT_HIERARCHY("projectHierarchy"),
    /** Name of the parent project of this Explore. */
    PROJECT_NAME("projectName"),
    /** Unique name of the project in which this dashboard exists. */
    PROJECT_QUALIFIED_NAME("projectQualifiedName"),
    /** All propagated Atlan tags that exist on an asset, searchable by the internal hashed-string ID of the Atlan tag. */
    PROPAGATED_TRAIT_NAMES("__propagatedTraitNames"),
    /** TBC */
    PURPOSE_ATLAN_TAGS("purposeClassifications"),
    /** Identifier of the app in which this asset belongs, from Qlik. */
    QLIK_APP_ID("qlikAppId"),
    /** Unique name of the app where this asset belongs. */
    QLIK_APP_QUALIFIED_NAME("qlikAppQualifiedName"),
    /** Orientation of this chart. */
    QLIK_CHART_ORIENTATION("qlikChartOrientation"),
    /** Subtype of this chart, for example: bar, graph, pie, etc. */
    QLIK_CHART_TYPE("qlikChartType"),
    /** Subtype this dataset asset. */
    QLIK_DATASET_SUBTYPE("qlikDatasetSubtype"),
    /** Technical name of this asset. */
    QLIK_DATASET_TECHNICAL_NAME("qlikDatasetTechnicalName.keyword"),
    /** Type of this data asset, for example: qix-df, snowflake, etc. */
    QLIK_DATASET_TYPE("qlikDatasetType"),
    /** URI of this dataset. */
    QLIK_DATASET_URI("qlikDatasetUri"),
    /** Identifier of this asset, from Qlik. */
    QLIK_ID("qlikId"),
    /** Value of originAppId for this app. */
    QLIK_ORIGIN_APP_ID("qlikOriginAppId"),
    /** Identifier of the owner of this asset, in Qlik. */
    QLIK_OWNER_ID("qlikOwnerId"),
    /** Unique QRI of this asset, from Qlik. */
    QLIK_QRI("qlikQRI"),
    /** Identifier of the space in which this asset exists, from Qlik. */
    QLIK_SPACE_ID("qlikSpaceId"),
    /** Unique name of the space in which this asset exists. */
    QLIK_SPACE_QUALIFIED_NAME("qlikSpaceQualifiedName"),
    /** Type of this space, for exmaple: Private, Shared, etc. */
    QLIK_SPACE_TYPE("qlikSpaceType"),
    /** Unique fully-qualified name of the asset in Atlan. */
    QUALIFIED_NAME("qualifiedName"),
    /** Query config for this connection. */
    QUERY_CONFIG("queryConfig"),
    /** Configuration for the query preview of this materialized view. */
    QUERY_PREVIEW_CONFIG("queryPreviewConfig"),
    /** Username strategy to use for this connection for queries. */
    QUERY_USERNAME_STRATEGY("queryUsernameStrategy"),
    /** Map of unique users who have queried this asset to the number of times they have queried it. */
    QUERY_USER_MAP("queryUserMap"),
    /** List of field names calculated by this analysis. */
    QUICK_SIGHT_ANALYSIS_CALCULATED_FIELDS("quickSightAnalysisCalculatedFields"),
    /** List of filter groups used for this analysis. */
    QUICK_SIGHT_ANALYSIS_FILTER_GROUPS("quickSightAnalysisFilterGroups"),
    /** List of parameters used for this analysis. */
    QUICK_SIGHT_ANALYSIS_PARAMETER_DECLARATIONS("quickSightAnalysisParameterDeclarations"),
    /** Unique name of the QuickSight analysis in which this visual exists. */
    QUICK_SIGHT_ANALYSIS_QUALIFIED_NAME("quickSightAnalysisQualifiedName"),
    /** Status of this analysis, for example: CREATION_IN_PROGRESS, UPDATE_SUCCESSFUL, etc. */
    QUICK_SIGHT_ANALYSIS_STATUS("quickSightAnalysisStatus"),
    /** Unique name of the dashboard in which this visual exists. */
    QUICK_SIGHT_DASHBOARD_QUALIFIED_NAME("quickSightDashboardQualifiedName"),
    /** Datatype of this field, for example: STRING, INTEGER, etc. */
    QUICK_SIGHT_DATASET_FIELD_TYPE("quickSightDatasetFieldType"),
    /** Import mode for this dataset, for example: SPICE or DIRECT_QUERY. */
    QUICK_SIGHT_DATASET_IMPORT_MODE("quickSightDatasetImportMode"),
    /** Unique name of the dataset in which this field exists. */
    QUICK_SIGHT_DATASET_QUALIFIED_NAME("quickSightDatasetQualifiedName"),
    /** Detailed path of this folder. */
    QUICK_SIGHT_FOLDER_HIERARCHY("quickSightFolderHierarchy"),
    /** Type of this folder, for example: SHARED. */
    QUICK_SIGHT_FOLDER_TYPE("quickSightFolderType"),
    /** TBC */
    QUICK_SIGHT_ID("quickSightId"),
    /** TBC */
    QUICK_SIGHT_SHEET_ID("quickSightSheetId"),
    /** TBC */
    QUICK_SIGHT_SHEET_NAME("quickSightSheetName.keyword"),
    /** TBC */
    RAW_DATA_TYPE_DEFINITION("rawDataTypeDefinition"),
    /** Deprecated. See 'longRawQuery' instead. */
    RAW_QUERY("rawQuery"),
    /** Simple name of the query from which this visualization is created. */
    REDASH_QUERY_NAME("redashQueryName.keyword"),
    /** Parameters of this query. */
    REDASH_QUERY_PARAMETERS("redashQueryParameters"),
    /** Unique name of the query from which this visualization is created. */
    REDASH_QUERY_QUALIFIED_NAME("redashQueryQualifiedName"),
    /** Schedule for this query. */
    REDASH_QUERY_SCHEDULE("redashQuerySchedule"),
    /** Schdule for this query in readable text for overview tab and filtering. */
    REDASH_QUERY_SCHEDULE_HUMANIZED("redashQueryScheduleHumanized"),
    /** SQL code of this query. */
    REDASH_QUERY_SQL("redashQuerySQL"),
    /** Type of this visualization. */
    REDASH_VISUALIZATION_TYPE("redashVisualizationType"),
    /** Reference to the resource. */
    REFERENCE("reference"),
    /** Refresh method for this materialized view. */
    REFRESH_METHOD("refreshMethod"),
    /** Refresh mode for this materialized view. */
    REFRESH_MODE("refreshMode"),
    /** TBC */
    REPLICATED_FROM("replicatedFrom"),
    /** TBC */
    REPLICATED_TO("replicatedTo"),
    /** Unique name of the report in which this page exists. */
    REPORT_QUALIFIED_NAME("reportQualifiedName"),
    /** Type of report in Salesforce. */
    REPORT_TYPE("reportType"),
    /** Metadata of the resource. */
    RESOURCE_METADATA("resourceMetadata"),
    /** TBC */
    RESULT("result"),
    /** TBC */
    RESULT_SUMMARY("resultSummary"),
    /** Role of this field, for example: 'dimension', 'measure', or 'unknown'. */
    ROLE("role"),
    /** TBC */
    ROLE_ID("roleId"),
    /** Simple name of the bucket in which this object exists. */
    S3BUCKET_NAME("s3BucketName"),
    /** Unique name of the bucket in which this object exists. */
    S3BUCKET_QUALIFIED_NAME("s3BucketQualifiedName"),
    /** TBC */
    S3ENCRYPTION("s3Encryption"),
    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    S3E_TAG("s3ETag"),
    /** Information about how this object's content should be presented. */
    S3OBJECT_CONTENT_DISPOSITION("s3ObjectContentDisposition"),
    /** Type of content in this object, for example: text/plain, application/json, etc. */
    S3OBJECT_CONTENT_TYPE("s3ObjectContentType"),
    /** Unique identity of this object in an S3 bucket. This is usually the concatenation of any prefix (folder) in the S3 bucket with the name of the object (file) itself. */
    S3OBJECT_KEY("s3ObjectKey"),
    /** Storage class used for storing this object, for example: standard, intelligent-tiering, glacier, etc. */
    S3OBJECT_STORAGE_CLASS("s3ObjectStorageClass"),
    /** Version of this object. This is only applicable when versioning is enabled on the bucket in which this object exists. */
    S3OBJECT_VERSION_ID("s3ObjectVersionId"),
    /** URL for sample data for this asset. */
    SAMPLE_DATA_URL("sampleDataUrl"),
    /** TBC */
    SAVED_SEARCHES("savedSearches"),
    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    SCHEMA_NAME("schemaName.keyword"),
    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    SCHEMA_QUALIFIED_NAME("schemaQualifiedName"),
    /** Unique identifier for schema definition set by the schema registry. */
    SCHEMA_REGISTRY_SCHEMA_ID("schemaRegistrySchemaId"),
    /** Type of language or specification used to define the schema, for example: JSON, Protobuf, etc. */
    SCHEMA_REGISTRY_SCHEMA_TYPE("schemaRegistrySchemaType"),
    /** Base name of the subject, without -key, -value prefixes. */
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
    /** Unused. Brief summary of the term. See 'description' and 'userDescription' instead. */
    SHORT_DESCRIPTION("shortDescription"),
    /** Simple name of the dataset in which this column exists. */
    SIGMA_DATASET_NAME("sigmaDatasetName.keyword"),
    /** Unique name of the dataset in which this column exists. */
    SIGMA_DATASET_QUALIFIED_NAME("sigmaDatasetQualifiedName"),
    /** Simple name of the data element in which this asset exists. */
    SIGMA_DATA_ELEMENT_NAME("sigmaDataElementName.keyword"),
    /** Unique name of the data element in which this asset exists. */
    SIGMA_DATA_ELEMENT_QUALIFIED_NAME("sigmaDataElementQualifiedName"),
    /** TBC */
    SIGMA_DATA_ELEMENT_QUERY("sigmaDataElementQuery"),
    /** TBC */
    SIGMA_DATA_ELEMENT_TYPE("sigmaDataElementType"),
    /** Simple name of the page on which this asset exists. */
    SIGMA_PAGE_NAME("sigmaPageName.keyword"),
    /** Unique name of the page on which this asset exists. */
    SIGMA_PAGE_QUALIFIED_NAME("sigmaPageQualifiedName"),
    /** Simple name of the workbook in which this asset exists. */
    SIGMA_WORKBOOK_NAME("sigmaWorkbookName.keyword"),
    /** Unique name of the workbook in which this asset exists. */
    SIGMA_WORKBOOK_QUALIFIED_NAME("sigmaWorkbookQualifiedName"),
    /** Unique name of the folder in which this dashboard exists. */
    SISENSE_DASHBOARD_FOLDER_QUALIFIED_NAME("sisenseDashboardFolderQualifiedName"),
    /** Unique name of the datamodel in which this datamodel table exists. */
    SISENSE_DATAMODEL_QUALIFIED_NAME("sisenseDatamodelQualifiedName"),
    /** Default relation type for this datamodel. 'extract' type Datamodels have regular relations by default. 'live' type Datamodels have direct relations by default. */
    SISENSE_DATAMODEL_RELATION_TYPE("sisenseDatamodelRelationType"),
    /** Revision of this datamodel. */
    SISENSE_DATAMODEL_REVISION("sisenseDatamodelRevision"),
    /** Hostname of the server on which this datamodel was created. */
    SISENSE_DATAMODEL_SERVER("sisenseDatamodelServer"),
    /** SQL expression of this datamodel table. */
    SISENSE_DATAMODEL_TABLE_EXPRESSION("sisenseDatamodelTableExpression"),
    /** JSON specifying the LiveQuery settings of this datamodel table. */
    SISENSE_DATAMODEL_TABLE_LIVE_QUERY_SETTINGS("sisenseDatamodelTableLiveQuerySettings"),
    /** JSON specifying the refresh schedule of this datamodel table. */
    SISENSE_DATAMODEL_TABLE_SCHEDULE("sisenseDatamodelTableSchedule"),
    /** Type of this datamodel table, for example: 'base' for regular tables, 'custom' for SQL expression-based tables. */
    SISENSE_DATAMODEL_TABLE_TYPE("sisenseDatamodelTableType"),
    /** Type of this datamodel, for example: 'extract' or 'custom'. */
    SISENSE_DATAMODEL_TYPE("sisenseDatamodelType"),
    /** Unique name of the parent folder in which this folder exists. */
    SISENSE_FOLDER_PARENT_FOLDER_QUALIFIED_NAME("sisenseFolderParentFolderQualifiedName"),
    /** Unique name of the dashboard in which this widget exists. */
    SISENSE_WIDGET_DASHBOARD_QUALIFIED_NAME("sisenseWidgetDashboardQualifiedName"),
    /** Unique name of the folder in which this widget exists. */
    SISENSE_WIDGET_FOLDER_QUALIFIED_NAME("sisenseWidgetFolderQualifiedName"),
    /** Size of this widget. */
    SISENSE_WIDGET_SIZE("sisenseWidgetSize"),
    /** Subtype of this widget. */
    SISENSE_WIDGET_SUB_TYPE("sisenseWidgetSubType"),
    /** Unique name of the site in which this dashboard exists. */
    SITE_QUALIFIED_NAME("siteQualifiedName"),
    /** Name of the notification channel for this pipe. */
    SNOWFLAKE_PIPE_NOTIFICATION_CHANNEL_NAME("snowflakePipeNotificationChannelName"),
    /** Mode of this stream. */
    SNOWFLAKE_STREAM_MODE("snowflakeStreamMode"),
    /** Type of the source of this stream. */
    SNOWFLAKE_STREAM_SOURCE_TYPE("snowflakeStreamSourceType"),
    /** Type of this stream, for example: standard, append-only, insert-only, etc. */
    SNOWFLAKE_STREAM_TYPE("snowflakeStreamType"),
    /** Definition of the check in Soda. */
    SODA_CHECK_DEFINITION("sodaCheckDefinition"),
    /** Status of the check in Soda. */
    SODA_CHECK_EVALUATION_STATUS("sodaCheckEvaluationStatus"),
    /** Identifier of the check in Soda. */
    SODA_CHECK_ID("sodaCheckId"),
    /** Connection name for the Explore, from Looker. */
    SOURCE_CONNECTION_NAME("sourceConnectionName"),
    /** The unit of measure for sourceTotalCost. */
    SOURCE_COST_UNIT("sourceCostUnit"),
    /** Name of the user who created this asset, in the source system. */
    SOURCE_CREATED_BY("sourceCreatedBy"),
    /** Deprecated. */
    SOURCE_DEFINITION("sourceDefinition"),
    /** Deprecated. */
    SOURCE_DEFINITION_DATABASE("sourceDefinitionDatabase"),
    /** Deprecated. */
    SOURCE_DEFINITION_SCHEMA("sourceDefinitionSchema"),
    /** URL to create an embed for a resource (for example, an image of a dashboard) within Atlan. */
    SOURCE_EMBED_URL("sourceEmbedURL"),
    /** Identifier of the dashboard in Salesforce. */
    SOURCE_ID("sourceId"),
    /** Unused. Only the value of connectorType impacts icons. */
    SOURCE_LOGO("sourceLogo"),
    /** List of owners of this asset, in the source system. */
    SOURCE_OWNERS("sourceOwners"),
    /** List of most expensive warehouse names. */
    SOURCE_QUERY_COMPUTE_COSTS("sourceQueryComputeCostList"),
    /** List of most expensive warehouses with extra insights. */
    SOURCE_QUERY_COMPUTE_COST_RECORDS("sourceQueryComputeCostRecordList"),
    /** List of the most expensive queries that accessed this asset. */
    SOURCE_READ_EXPENSIVE_QUERY_RECORDS("sourceReadExpensiveQueryRecordList"),
    /** List of the most popular queries that accessed this asset. */
    SOURCE_READ_POPULAR_QUERY_RECORDS("sourceReadPopularQueryRecordList"),
    /** List of usernames of the most recent users who read this asset. */
    SOURCE_READ_RECENT_USERS("sourceReadRecentUserList"),
    /** List of usernames with extra insights for the most recent users who read this asset. */
    SOURCE_READ_RECENT_USER_RECORDS("sourceReadRecentUserRecordList"),
    /** List of the slowest queries that accessed this asset. */
    SOURCE_READ_SLOW_QUERY_RECORDS("sourceReadSlowQueryRecordList"),
    /** List of usernames of the users who read this asset the most. */
    SOURCE_READ_TOP_USERS("sourceReadTopUserList"),
    /** List of usernames with extra insights for the users who read this asset the most. */
    SOURCE_READ_TOP_USER_RECORDS("sourceReadTopUserRecordList"),
    /** TBC */
    SOURCE_SERVER_NAME("sourceServerName"),
    /** Name of the user who last updated this asset, in the source system. */
    SOURCE_UPDATED_BY("sourceUpdatedBy"),
    /** URL to the resource within the source application, used to create a button to view this asset in the source application. */
    SOURCE_URL("sourceURL"),
    /** Name of the Spark app containing this Spark Job For eg. extract_raw_data */
    SPARK_APP_NAME("sparkAppName.keyword"),
    /** The Spark master URL eg. local, local[4], or spark://master:7077 */
    SPARK_MASTER("sparkMaster"),
    /** OpenLineage state of the Spark Job run eg. COMPLETE */
    SPARK_RUN_OPEN_LINEAGE_STATE("sparkRunOpenLineageState"),
    /** OpenLineage Version of the Spark Job run eg. 1.1.0 */
    SPARK_RUN_OPEN_LINEAGE_VERSION("sparkRunOpenLineageVersion"),
    /** Spark Version for the Spark Job run eg. 3.4.1 */
    SPARK_RUN_VERSION("sparkRunVersion"),
    /** SQL query that ran to produce the outputs. */
    SQL("sql"),
    /** Name of the SQL table used to declare the Explore. */
    SQL_TABLE_NAME("sqlTableName"),
    /** TBC */
    STAKEHOLDER_DOMAIN_QUALIFIED_NAME("stakeholderDomainQualifiedName"),
    /** qualified name array representing the Domains for which this StakeholderTitle is applicable */
    STAKEHOLDER_TITLE_DOMAIN_QUALIFIED_NAMES("stakeholderTitleDomainQualifiedNames"),
    /** TBC */
    STAKEHOLDER_TITLE_GUID("stakeholderTitleGuid"),
    /** Staleness of this materialized view. */
    STALENESS("staleness"),
    /** Users who have starred this asset. */
    STARRED_BY("starredBy"),
    /** List of usernames with extra information of the users who have starred an asset. */
    STARRED_DETAILS("starredDetailsList"),
    /** Asset status in Atlan (active vs deleted). */
    STATE("__state"),
    /** Text for the subtitle for text tiles. */
    SUBTITLE_TEXT("subtitleText"),
    /** Subcategory of this connection. */
    SUB_CATEGORY("subCategory"),
    /** Sub-data type of this column. */
    SUB_DATA_TYPE("subDataType"),
    /** Subtype of this asset. */
    SUB_TYPE("subType"),
    /** Data stored for the chart in key value pairs. */
    SUPERSET_CHART_FORM_DATA("supersetChartFormData"),
    /** Name of the user who changed the dashboard. */
    SUPERSET_DASHBOARD_CHANGED_BY_NAME("supersetDashboardChangedByName.keyword"),
    /** URL of the user profile that changed the dashboard */
    SUPERSET_DASHBOARD_CHANGED_BY_URL("supersetDashboardChangedByURL"),
    /** Unique name of the dashboard in which this asset exists. */
    SUPERSET_DASHBOARD_QUALIFIED_NAME("supersetDashboardQualifiedName"),
    /** URL for the dashboard thumbnail image in superset. */
    SUPERSET_DASHBOARD_THUMBNAIL_URL("supersetDashboardThumbnailURL"),
    /** Name of the datasource for the dataset. */
    SUPERSET_DATASET_DATASOURCE_NAME("supersetDatasetDatasourceName.keyword"),
    /** Type of the dataset in superset. */
    SUPERSET_DATASET_TYPE("supersetDatasetType"),
    /** Unique name of the top-level domain in which this asset exists. */
    SUPER_DOMAIN_QUALIFIED_NAME("superDomainQualifiedName"),
    /** All super types of an asset. */
    SUPER_TYPE_NAMES("__superTypeNames.keyword"),
    /** Bin size of this field. */
    TABLEAU_DATASOURCE_FIELD_BIN_SIZE("tableauDatasourceFieldBinSize"),
    /** Data category of this field. */
    TABLEAU_DATASOURCE_FIELD_DATA_CATEGORY("tableauDatasourceFieldDataCategory"),
    /** Data type of this field. */
    TABLEAU_DATASOURCE_FIELD_DATA_TYPE("tableauDatasourceFieldDataType"),
    /** Formula for this field. */
    TABLEAU_DATASOURCE_FIELD_FORMULA("tableauDatasourceFieldFormula"),
    /** Role of this field, for example: 'dimension', 'measure', or 'unknown'. */
    TABLEAU_DATASOURCE_FIELD_ROLE("tableauDatasourceFieldRole"),
    /** Data type of the field, from Tableau. */
    TABLEAU_DATA_TYPE("tableauDataType"),
    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    TABLE_NAME("tableName.keyword"),
    /** Unique name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    TABLE_QUALIFIED_NAME("tableQualifiedName"),
    /** Allowed values for the tag in the source system. These are denormalized from tagAttributes for ease of querying. */
    TAG_ALLOWED_VALUES("tagAllowedValues"),
    /** Represents associated tag value */
    TAG_ATTACHMENT_STRING_VALUE("tagAttachmentStringValue"),
    /** Attributes associated with the tag in the source system. */
    TAG_ATTRIBUTES("tagAttributes"),
    /** Unique identifier of the tag in the source system. */
    TAG_ID("tagId"),
    /** Represents associated source tag's qualified name */
    TAG_QUALIFIED_NAME("tagQualifiedName"),
    /** TBC */
    TAG_SERVICE("tagService"),
    /** TBC */
    TARGET_SERVER_NAME("targetServerName"),
    /** List of actions associated with this task. */
    TASK_ACTIONS("taskActions"),
    /** username of the user who created this task */
    TASK_CREATED_BY("taskCreatedBy"),
    /** action executed by the recipient */
    TASK_EXECUTION_ACTION("taskExecutionAction"),
    /** comment for the action executed by user */
    TASK_EXECUTION_COMMENT("taskExecutionComment"),
    /** contains the payload that is proposed to the task */
    TASK_PROPOSALS("taskProposals"),
    /** recipient of the task */
    TASK_RECIPIENT("taskRecipient"),
    /** assetId to preview */
    TASK_RELATED_ASSET_GUID("taskRelatedAssetGuid"),
    /** requestor of the task */
    TASK_REQUESTOR("taskRequestor"),
    /** comment of requestor for the task */
    TASK_REQUESTOR_COMMENT("taskRequestorComment"),
    /** type of task */
    TASK_TYPE("taskType"),
    /** username of the user who updated this task */
    TASK_UPDATED_BY("taskUpdatedBy"),
    /** Name of the Atlan workspace in which this asset exists. */
    TENANT_ID("tenantId"),
    /** TBC */
    TERM_TYPE("termType"),
    /** TBC */
    THOUGHTSPOT_CHART_TYPE("thoughtspotChartType"),
    /** Specifies the technical format of data stored in a column such as integer, float, string, date, boolean etc. */
    THOUGHTSPOT_COLUMN_DATA_TYPE("thoughtspotColumnDataType"),
    /** Defines the analytical role of a column in data analysis categorizing it as a dimension, measure, or attribute. */
    THOUGHTSPOT_COLUMN_TYPE("thoughtspotColumnType"),
    /** Simple name of the liveboard in which this dashlet exists. */
    THOUGHTSPOT_LIVEBOARD_NAME("thoughtspotLiveboardName.keyword"),
    /** Unique name of the liveboard in which this dashlet exists. */
    THOUGHTSPOT_LIVEBOARD_QUALIFIED_NAME("thoughtspotLiveboardQualifiedName"),
    /** Unique name of the table in which this column exists. */
    THOUGHTSPOT_TABLE_QUALIFIED_NAME("thoughtspotTableQualifiedName"),
    /** Unique name of the view in which this column exists. */
    THOUGHTSPOT_VIEW_QUALIFIED_NAME("thoughtspotViewQualifiedName"),
    /** Unique name of the worksheet in which this column exists. */
    THOUGHTSPOT_WORKSHEET_QUALIFIED_NAME("thoughtspotWorksheetQualifiedName"),
    /** Simple name of the top-level project in which this workbook exists. */
    TOP_LEVEL_PROJECT_NAME("topLevelProjectName"),
    /** Unique name of the top-level project in which this dashboard exists. */
    TOP_LEVEL_PROJECT_QUALIFIED_NAME("topLevelProjectQualifiedName"),
    /** All directly-assigned Atlan tags that exist on an asset, searchable by the internal hashed-string ID of the Atlan tag. */
    TRAIT_NAMES("__traitNames"),
    /** Type of the asset. For example Table, Column, and so on. */
    TYPE_NAME("__typeName.keyword"),
    /** TBC */
    UI_PARAMETERS("uiParameters"),
    /** TBC */
    UNIQUE_NAME("uniqueName"),
    /** Columns upstream to this field. */
    UPSTREAM_COLUMNS("upstreamColumns"),
    /** List of datasources that are upstream of this datasource. */
    UPSTREAM_DATASOURCES("upstreamDatasources"),
    /** Fields upstream to this field. */
    UPSTREAM_FIELDS("upstreamFields"),
    /** List of tables that are upstream of this datasource. */
    UPSTREAM_TABLES("upstreamTables"),
    /** TBC */
    URLS("urls"),
    /** Unused. Intended usage for the term. */
    USAGE("usage"),
    /** Description of this asset, as provided by a user. If present, this will be used for the description in user interface. */
    USER_DESCRIPTION("userDescription.keyword"),
    /** TBC */
    USER_NAME("userName"),
    /** Validations for this column. */
    VALIDATIONS("validations"),
    /** Base64-encoded string of the variables to use in this query. */
    VARIABLES_SCHEMA_BASE64("variablesSchemaBase64"),
    /** List of groups who can view assets contained in a collection. (This is only used for certain asset types.) */
    VIEWER_GROUPS("viewerGroups"),
    /** List of users who can view assets contained in a collection. (This is only used for certain asset types.) */
    VIEWER_USERS("viewerUsers"),
    /** Name of the view for the Explore. */
    VIEW_NAME("viewName.keyword"),
    /** Unique name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    VIEW_QUALIFIED_NAME("viewQualifiedName"),
    /** Base64-encoded string for the visual query builder. */
    VISUAL_BUILDER_SCHEMA_BASE64("visualBuilderSchemaBase64"),
    /** Deprecated. */
    WEB_URL("webUrl"),
    /** Unique name of the workbook in which this dashboard exists. */
    WORKBOOK_QUALIFIED_NAME("workbookQualifiedName"),
    /** Details of the workflow. */
    WORKFLOW_CONFIG("workflowConfig"),
    /** Username of the user who created this workflow. */
    WORKFLOW_CREATED_BY("workflowCreatedBy"),
    /** The comment added by the requester */
    WORKFLOW_RUN_COMMENT("workflowRunComment"),
    /** Details of the approval workflow run. */
    WORKFLOW_RUN_CONFIG("workflowRunConfig"),
    /** Username of the user who created this workflow run. */
    WORKFLOW_RUN_CREATED_BY("workflowRunCreatedBy"),
    /** Time duration after which a run of this workflow will expire. */
    WORKFLOW_RUN_EXPIRES_IN("workflowRunExpiresIn"),
    /** The asset for which this run was created. */
    WORKFLOW_RUN_ON_ASSET_GUID("workflowRunOnAssetGuid"),
    /** Status of the run. */
    WORKFLOW_RUN_STATUS("workflowRunStatus"),
    /** Type of the workflow from which this run was created. */
    WORKFLOW_RUN_TYPE("workflowRunType"),
    /** Username of the user who updated this workflow run. */
    WORKFLOW_RUN_UPDATED_BY("workflowRunUpdatedBy"),
    /** GUID of the workflow from which this run was created. */
    WORKFLOW_RUN_WORKFLOW_GUID("workflowRunWorkflowGuid"),
    /** Status of the workflow. */
    WORKFLOW_STATUS("workflowStatus"),
    /** GUID of the workflow template from which this workflow was created. */
    WORKFLOW_TEMPLATE_GUID("workflowTemplateGuid"),
    /** Type of the workflow. */
    WORKFLOW_TYPE("workflowType"),
    /** Username of the user who updated this workflow. */
    WORKFLOW_UPDATED_BY("workflowUpdatedBy"),
    /** Unique name of the workspace in which this dataset exists. */
    WORKSPACE_QUALIFIED_NAME("workspaceQualifiedName"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    KeywordFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
