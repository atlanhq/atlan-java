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
public enum BooleanFields implements AtlanSearchableField {
    /** Whether this container supports version-level immutability (true) or not (false). */
    ADLS_CONTAINER_VERSION_LEVEL_IMMUTABILITY_SUPPORT("adlsContainerVersionLevelImmutabilitySupport"),
    /** Whether this object is server encrypted (true) or not (false). */
    ADLS_OBJECT_SERVER_ENCRYPTED("adlsObjectServerEncrypted"),
    /** Whether this object supports version-level immutability (true) or not (false). */
    ADLS_OBJECT_VERSION_LEVEL_IMMUTABILITY_SUPPORT("adlsObjectVersionLevelImmutabilitySupport"),
    /** Whether using this connection to run queries on the source is allowed (true) or not (false). */
    ALLOW_QUERY("allowQuery"),
    /** Whether using this connection to run preview queries on the source is allowed (true) or not (false). */
    ALLOW_QUERY_PREVIEW("allowQueryPreview"),
    /** Whether authentication is optional (true) or required (false). */
    API_IS_AUTH_OPTIONAL("apiIsAuthOptional"),
    /** Whether the path is exposed as an ingress (true) or not (false). */
    API_PATH_IS_INGRESS_EXPOSED("apiPathIsIngressExposed"),
    /** Whether the endpoint's path contains replaceable parameters (true) or not (false). */
    API_PATH_IS_TEMPLATED("apiPathIsTemplated"),
    /** Whether artifacts were saved from the last run of the job that materialized this asset in dbt (true) or not (false). */
    ASSET_DBT_JOB_LAST_RUN_ARTIFACTS_SAVED("assetDbtJobLastRunArtifactsSaved"),
    /** Whether docs were generated from the last run of the job that materialized this asset in dbt (true) or not (false). */
    ASSET_DBT_JOB_LAST_RUN_HAS_DOCS_GENERATED("assetDbtJobLastRunHasDocsGenerated"),
    /** Whether sources were generated from the last run of the job that materialized this asset in dbt (true) or not (false). */
    ASSET_DBT_JOB_LAST_RUN_HAS_SOURCES_GENERATED("assetDbtJobLastRunHasSourcesGenerated"),
    /** Whether notifications were sent from the last run of the job that materialized this asset in dbt (true) or not (false). */
    ASSET_DBT_JOB_LAST_RUN_NOTIFICATIONS_SENT("assetDbtJobLastRunNotificationsSent"),
    /** TBC */
    AUTH_SERVICE_IS_ENABLED("authServiceIsEnabled"),
    /** Whether the function is stored or executed externally (true) or internally (false). */
    FUNCTION_IS_EXTERNAL("functionIsExternal"),
    /** Whether the function must re-compute if there are no underlying changes in the values (false) or not (true). */
    FUNCTION_IS_MEMOIZABLE("functionIsMemoizable"),
    /** Whether sensitive information of the function is omitted for unauthorized users (true) or not (false). */
    FUNCTION_IS_SECURE("functionIsSecure"),
    /** Whether retention is locked for this bucket (true) or not (false). */
    GCS_BUCKET_RETENTION_LOCKED("gcsBucketRetentionLocked"),
    /** Whether versioning is enabled on the bucket (true) or not (false). */
    GCS_BUCKET_VERSIONING_ENABLED("gcsBucketVersioningEnabled"),
    /** Whether the requester pays header was sent when this asset was created (true) or not (false). */
    GCS_REQUESTER_PAYS("gcsRequesterPays"),
    /** Whether this datasource has extracts (true) or not (false). */
    HAS_EXTRACTS("hasExtracts"),
    /** Whether this asset has lineage (true) or not (false). */
    HAS_LINEAGE("__hasLineage"),
    /** Whether this connection has popularity insights (true) or not (false). */
    HAS_POPULARITY_INSIGHTS("hasPopularityInsights"),
    /** TBC */
    IS_ACCESS_CONTROL_ENABLED("isAccessControlEnabled"),
    /** TBC */
    IS_AI_GENERATED("isAIGenerated"),
    /** Whether this field is calculated (true) or not (false). */
    IS_CALCULATED("isCalculated"),
    /** Whether this field is case sensitive (true) or in-sensitive (false). */
    IS_CASE_SENSITIVE("isCaseSensitive"),
    /** Whether this datasource is certified in Tableau (true) or not (false). */
    IS_CERTIFIED("isCertified"),
    /** Whether this column is a clustered column (true) or not (false). */
    IS_CLUSTERED("isClustered"),
    /** Whether this object is a custom object (true) or not (false). */
    IS_CUSTOM("isCustom"),
    /** Whether this asset is discoverable through the UI (true) or not (false). */
    IS_DISCOVERABLE("isDiscoverable"),
    /** Whether this column is a distribution column (true) or not (false). */
    IS_DIST("isDist"),
    /** Whether this asset can be edited in the UI (true) or not (false). */
    IS_EDITABLE("isEditable"),
    /** Whether this field is encrypted (true) or not (false). */
    IS_ENCRYPTED("isEncrypted"),
    /** When true, this column is a foreign key to another table. NOTE: this must be true when using the foreignKeyTo relationship to specify columns that refer to this column as a foreign key. */
    IS_FOREIGN("isForeign"),
    /** Whether the resource is global (true) or not (false). */
    IS_GLOBAL("isGlobal"),
    /** When true, this column is indexed in the database. */
    IS_INDEXED("isIndexed"),
    /** Whether this object is mergable (true) or not (false). */
    IS_MERGABLE("isMergable"),
    /** Whether this field allows null values (true) or not (false). */
    IS_NULLABLE("isNullable"),
    /** Whether this column is a partition column (true) or not (false). */
    IS_PARTITION("isPartition"),
    /** Whether this partition is further partitioned (true) or not (false). */
    IS_PARTITIONED("isPartitioned"),
    /** Whether this column is pinned (true) or not (false). */
    IS_PINNED("isPinned"),
    /** TBC */
    IS_POLICY_ENABLED("isPolicyEnabled"),
    /** Whether this field references a record of multiple objects (true) or not (false). */
    IS_POLYMORPHIC_FOREIGN_KEY("isPolymorphicForeignKey"),
    /** When true, this column is the primary key for the table. */
    IS_PRIMARY("isPrimary"),
    /** Whether this query is private (true) or shared (false). */
    IS_PRIVATE("isPrivate"),
    /** Whether this asset has been profiled (true) or not (false). */
    IS_PROFILED("isProfiled"),
    /** Whether this asset is published (true) or still a work in progress (false). */
    IS_PUBLISHED("isPublished"),
    /** Whether this object is queryable (true) or not (false). */
    IS_QUERYABLE("isQueryable"),
    /** Whether it's possible to run a preview query on this materialized view (true) or not (false). */
    IS_QUERY_PREVIEW("isQueryPreview"),
    /** Whether sample data can be previewed for this connection (true) or not (false). */
    IS_SAMPLE_DATA_PREVIEW_ENABLED("isSampleDataPreviewEnabled"),
    /** Whether this column is a sort column (true) or not (false). */
    IS_SORT("isSort"),
    /** Whether this query is a SQL snippet (true) or not (false). */
    IS_SQL_SNIPPET("isSqlSnippet"),
    /** Whether this materialized view is temporary (true) or not (false). */
    IS_TEMPORARY("isTemporary"),
    /** Whether this project is a top-level project (true) or not (false). */
    IS_TOP_LEVEL_PROJECT("isTopLevelProject"),
    /** Whether the Google Data Studio asset has been trashed (true) or not (false). */
    IS_TRASHED_DATA_STUDIO_ASSET("isTrashedDataStudioAsset"),
    /** Whether this field must have unique values (true) or not (false). */
    IS_UNIQUE("isUnique"),
    /** Whether this query is a visual query (true) or not (false). */
    IS_VISUAL_QUERY("isVisualQuery"),
    /** Whether this topic is an internal topic (true) or not (false). */
    KAFKA_TOPIC_IS_INTERNAL("kafkaTopicIsInternal"),
    /** Whether the rule is currently snoozed (true) or not (false). */
    MC_MONITOR_RULE_IS_SNOOZED("mcMonitorRuleIsSnoozed"),
    /** TBC */
    METABASE_IS_PERSONAL_COLLECTION("metabaseIsPersonalCollection"),
    /** Whether the asset is certified in MicroStrategy (true) or not (false). */
    MICRO_STRATEGY_IS_CERTIFIED("microStrategyIsCertified"),
    /** TBC */
    MODE_IS_PUBLIC("modeIsPublic"),
    /** TBC */
    MODE_IS_SHARED("modeIsShared"),
    /** Whether the collection is capped (true) or not (false). */
    MONGO_DB_COLLECTION_IS_CAPPED("mongoDBCollectionIsCapped"),
    /** TBC */
    POLICY_DELEGATE_ADMIN("policyDelegateAdmin"),
    /** Whether this measure is external (true) or internal (false). */
    POWER_BI_IS_EXTERNAL_MEASURE("powerBIIsExternalMeasure"),
    /** Whether this asset is hidden in Power BI (true) or not (false). */
    POWER_BI_IS_HIDDEN("powerBIIsHidden"),
    /** TBC */
    PRESET_DASHBOARD_IS_MANAGED_EXTERNALLY("presetDashboardIsManagedExternally"),
    /** TBC */
    PRESET_DASHBOARD_IS_PUBLISHED("presetDashboardIsPublished"),
    /** TBC */
    PRESET_WORKSPACE_IS_IN_MAINTENANCE_MODE("presetWorkspaceIsInMaintenanceMode"),
    /** TBC */
    PRESET_WORKSPACE_PUBLIC_DASHBOARDS_ALLOWED("presetWorkspacePublicDashboardsAllowed"),
    /** Whether section access or data masking is enabled on the source (true) or not (false). */
    QLIK_HAS_SECTION_ACCESS("qlikHasSectionAccess"),
    /** Whether this app is in direct query mode (true) or not (false). */
    QLIK_IS_DIRECT_QUERY_MODE("qlikIsDirectQueryMode"),
    /** Whether this app is encrypted (true) or not (false). */
    QLIK_IS_ENCRYPTED("qlikIsEncrypted"),
    /** Whether this asset is published in Qlik (true) or not (false). */
    QLIK_IS_PUBLISHED("qlikIsPublished"),
    /** Whether this is approved (true) or not (false). */
    QLIK_SHEET_IS_APPROVED("qlikSheetIsApproved"),
    /** Whether this asset is published in Redash (true) or not (false). */
    REDASH_IS_PUBLISHED("redashIsPublished"),
    /** Whether versioning is enabled for the bucket (true) or not (false). */
    S3BUCKET_VERSIONING_ENABLED("s3BucketVersioningEnabled"),
    /** Whether the subject is a schema for the keys of the messages (true) or not (false). */
    SCHEMA_REGISTRY_SUBJECT_IS_KEY_SCHEMA("schemaRegistrySubjectIsKeySchema"),
    /** Whether this field is hidden (true) or not (false). */
    SIGMA_DATA_ELEMENT_FIELD_IS_HIDDEN("sigmaDataElementFieldIsHidden"),
    /** Whether this datamodel table is hidden in Sisense (true) or not (false). */
    SISENSE_DATAMODEL_TABLE_IS_HIDDEN("sisenseDatamodelTableIsHidden"),
    /** Whether this datamodel table is materialised (true) or not (false). */
    SISENSE_DATAMODEL_TABLE_IS_MATERIALIZED("sisenseDatamodelTableIsMaterialized"),
    /** Whether auto-ingest is enabled for this pipe (true) or not (false). */
    SNOWFLAKE_PIPE_IS_AUTO_INGEST_ENABLED("snowflakePipeIsAutoIngestEnabled"),
    /** Whether this stream is stale (true) or not (false). */
    SNOWFLAKE_STREAM_IS_STALE("snowflakeStreamIsStale"),
    /** Whether to upload to S3, GCP, or another storage location (true) or not (false). */
    USE_OBJECT_STORAGE("useObjectStorage"),
    /** TBC */
    VECTOR_EMBEDDINGS_ENABLED("vectorEmbeddingsEnabled"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    BooleanFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
