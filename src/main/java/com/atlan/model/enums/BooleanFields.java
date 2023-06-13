/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum BooleanFields implements AtlanSearchableField {
    /** TBC */
    ADLS_CONTAINER_VERSION_LEVEL_IMMUTABILITY_SUPPORT("adlsContainerVersionLevelImmutabilitySupport"),
    /** TBC */
    ADLS_OBJECT_SERVER_ENCRYPTED("adlsObjectServerEncrypted"),
    /** TBC */
    ADLS_OBJECT_VERSION_LEVEL_IMMUTABILITY_SUPPORT("adlsObjectVersionLevelImmutabilitySupport"),
    /** When true, allow the source to be queried. */
    ALLOW_QUERY("allowQuery"),
    /** When true, allow data previews of the source. */
    ALLOW_QUERY_PREVIEW("allowQueryPreview"),
    /** TBC */
    API_IS_AUTH_OPTIONAL("apiIsAuthOptional"),
    /** TBC */
    API_PATH_IS_INGRESS_EXPOSED("apiPathIsIngressExposed"),
    /** TBC */
    API_PATH_IS_TEMPLATED("apiPathIsTemplated"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_ARTIFACTS_SAVED("assetDbtJobLastRunArtifactsSaved"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_HAS_DOCS_GENERATED("assetDbtJobLastRunHasDocsGenerated"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_HAS_SOURCES_GENERATED("assetDbtJobLastRunHasSourcesGenerated"),
    /** TBC */
    ASSET_DBT_JOB_LAST_RUN_NOTIFICATIONS_SENT("assetDbtJobLastRunNotificationsSent"),
    /** TBC */
    AUTH_SERVICE_IS_ENABLED("authServiceIsEnabled"),
    /** TBC */
    GCS_BUCKET_RETENTION_LOCKED("gcsBucketRetentionLocked"),
    /** TBC */
    GCS_BUCKET_VERSIONING_ENABLED("gcsBucketVersioningEnabled"),
    /** TBC */
    GCS_REQUESTER_PAYS("gcsRequesterPays"),
    /** TBC */
    HAS_EXTRACTS("hasExtracts"),
    /** Indicates whether this asset has lineage (true) or not. */
    HAS_LINEAGE("__hasLineage"),
    /** Whether the connection has popularity insights (true) or not (false). */
    HAS_POPULARITY_INSIGHTS("hasPopularityInsights"),
    /** Whether the access object is activated (true) or deactivated (false). */
    IS_ACCESS_CONTROL_ENABLED("isAccessControlEnabled"),
    /** TBC */
    IS_CALCULATED("isCalculated"),
    /** TBC */
    IS_CASE_SENSITIVE("isCaseSensitive"),
    /** TBC */
    IS_CERTIFIED("isCertified"),
    /** TBC */
    IS_CLUSTERED("isClustered"),
    /** Whether the object is a custom object (true) or not (false). */
    IS_CUSTOM("isCustom"),
    /** TBC */
    IS_DISCOVERABLE("isDiscoverable"),
    /** TBC */
    IS_DIST("isDist"),
    /** TBC */
    IS_EDITABLE("isEditable"),
    /** TBC */
    IS_ENCRYPTED("isEncrypted"),
    /** When true, this column is a foreign key to another table. */
    IS_FOREIGN("isForeign"),
    /** TBC */
    IS_GLOBAL("isGlobal"),
    /** When true, this column is indexed in the database. */
    IS_INDEXED("isIndexed"),
    /** TBC */
    IS_MERGABLE("isMergable"),
    /** TBC */
    IS_NULLABLE("isNullable"),
    /** TBC */
    IS_PARTITION("isPartition"),
    /** TBC */
    IS_PARTITIONED("isPartitioned"),
    /** TBC */
    IS_PINNED("isPinned"),
    /** Whether the policy is activated (true) or deactivated (false). */
    IS_POLICY_ENABLED("isPolicyEnabled"),
    /** Whether the field references a record of multiple objects (true) or not (false). */
    IS_POLYMORPHIC_FOREIGN_KEY("isPolymorphicForeignKey"),
    /** When true, this column is the primary key for the table. */
    IS_PRIMARY("isPrimary"),
    /** TBC */
    IS_PRIVATE("isPrivate"),
    /** Whether the asset has been profiled (true) or not (false). */
    IS_PROFILED("isProfiled"),
    /** TBC */
    IS_PUBLISHED("isPublished"),
    /** TBC */
    IS_QUERYABLE("isQueryable"),
    /** TBC */
    IS_QUERY_PREVIEW("isQueryPreview"),
    /** Whether sample data can be previewed for this connection (true) or not (false). */
    IS_SAMPLE_DATA_PREVIEW_ENABLED("isSampleDataPreviewEnabled"),
    /** TBC */
    IS_SORT("isSort"),
    /** TBC */
    IS_SQL_SNIPPET("isSqlSnippet"),
    /** Whether this materialized view is temporary (true) or not (false). */
    IS_TEMPORARY("isTemporary"),
    /** TBC */
    IS_TOP_LEVEL_PROJECT("isTopLevelProject"),
    /** Whether the asset is soft-deleted in Google Data Studio (true) or not (false). */
    IS_TRASHED_DATA_STUDIO_ASSET("isTrashedDataStudioAsset"),
    /** TBC */
    IS_UNIQUE("isUnique"),
    /** TBC */
    IS_VISUAL_QUERY("isVisualQuery"),
    /** TBC */
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
    /** TBC */
    POLICY_DELEGATE_ADMIN("policyDelegateAdmin"),
    /** TBC */
    POWER_BI_IS_EXTERNAL_MEASURE("powerBIIsExternalMeasure"),
    /** TBC */
    POWER_BI_IS_HIDDEN("powerBIIsHidden"),
    /** Whether the collection is managed externally (true) or not (false). */
    PRESET_DASHBOARD_IS_MANAGED_EXTERNALLY("presetDashboardIsManagedExternally"),
    /** Whether the collection is published (true) or not (false). */
    PRESET_DASHBOARD_IS_PUBLISHED("presetDashboardIsPublished"),
    /** Whether the workspace is in maintenance mode (true) or not (false). */
    PRESET_WORKSPACE_IS_IN_MAINTENANCE_MODE("presetWorkspaceIsInMaintenanceMode"),
    /** Whether public collections are allowed in the workspace (true) or not (false). */
    PRESET_WORKSPACE_PUBLIC_DASHBOARDS_ALLOWED("presetWorkspacePublicDashboardsAllowed"),
    /** Whether section access or data masking is enabled (true) or not (false). */
    QLIK_HAS_SECTION_ACCESS("qlikHasSectionAccess"),
    /** Whether the app is in direct query mode (true) or not (false). */
    QLIK_IS_DIRECT_QUERY_MODE("qlikIsDirectQueryMode"),
    /** Whether the app is encrypted (true) or not (false). */
    QLIK_IS_ENCRYPTED("qlikIsEncrypted"),
    /** Whether the asset is published in Qlik (true) or not (false). */
    QLIK_IS_PUBLISHED("qlikIsPublished"),
    /** Whether the sheet is approved (true) or not (false). */
    QLIK_SHEET_IS_APPROVED("qlikSheetIsApproved"),
    /** Whether the asset is published in Redash (true) or not (false). */
    REDASH_IS_PUBLISHED("redashIsPublished"),
    /** Whether versioning is enabled for the bucket. */
    S3BUCKET_VERSIONING_ENABLED("s3BucketVersioningEnabled"),
    /** TBC */
    SIGMA_DATA_ELEMENT_FIELD_IS_HIDDEN("sigmaDataElementFieldIsHidden"),
    /** TBC */
    SNOWFLAKE_PIPE_IS_AUTO_INGEST_ENABLED("snowflakePipeIsAutoIngestEnabled"),
    /** TBC */
    SNOWFLAKE_STREAM_IS_STALE("snowflakeStreamIsStale"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    BooleanFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
