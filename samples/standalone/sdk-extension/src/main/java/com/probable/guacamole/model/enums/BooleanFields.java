/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.probable.guacamole.model.enums;

import com.atlan.model.enums.AtlanSearchableField;
import javax.annotation.processing.Generated;
import lombok.Getter;

/**
 * Enumeration of all index fields.
 * @deprecated these are now enumerated as constants in the various asset interfaces, as instances of one of the following: {@link com.atlan.model.fields.BooleanField}, {@link com.atlan.model.fields.KeywordField}, {@link com.atlan.model.fields.KeywordTextField}, {@link com.atlan.model.fields.KeywordTextStemmedField}, {@link com.atlan.model.fields.NumericField}, {@link com.atlan.model.fields.NumericRankField}, {@link com.atlan.model.fields.RelationField}, or {@link com.atlan.model.fields.TextField}
 */
@Deprecated
@Generated(value = "com.probable.guacamole.generators.POJOGenerator")
public enum BooleanFields implements AtlanSearchableField {
    /** Whether artifacts were saved from the last run of the job that materialized this asset in dbt (true) or not (false). */
    ASSET_DBT_JOB_LAST_RUN_ARTIFACTS_SAVED("assetDbtJobLastRunArtifactsSaved"),
    /** Whether docs were generated from the last run of the job that materialized this asset in dbt (true) or not (false). */
    ASSET_DBT_JOB_LAST_RUN_HAS_DOCS_GENERATED("assetDbtJobLastRunHasDocsGenerated"),
    /** Whether sources were generated from the last run of the job that materialized this asset in dbt (true) or not (false). */
    ASSET_DBT_JOB_LAST_RUN_HAS_SOURCES_GENERATED("assetDbtJobLastRunHasSourcesGenerated"),
    /** Whether notifications were sent from the last run of the job that materialized this asset in dbt (true) or not (false). */
    ASSET_DBT_JOB_LAST_RUN_NOTIFICATIONS_SENT("assetDbtJobLastRunNotificationsSent"),
    /** Whether this table is currently archived (true) or not (false). */
    GUACAMOLE_ARCHIVED("guacamoleArchived"),
    /** Whether this asset has lineage (true) or not (false). */
    HAS_LINEAGE("__hasLineage"),
    /** TBC */
    IS_AI_GENERATED("isAIGenerated"),
    /** Whether this column is a clustered column (true) or not (false). */
    IS_CLUSTERED("isClustered"),
    /** Whether this asset is discoverable through the UI (true) or not (false). */
    IS_DISCOVERABLE("isDiscoverable"),
    /** Whether this column is a distribution column (true) or not (false). */
    IS_DIST("isDist"),
    /** Whether this asset can be edited in the UI (true) or not (false). */
    IS_EDITABLE("isEditable"),
    /** When true, this column is a foreign key to another table. NOTE: this must be true when using the foreignKeyTo relationship to specify columns that refer to this column as a foreign key. */
    IS_FOREIGN("isForeign"),
    /** Whether the resource is global (true) or not (false). */
    IS_GLOBAL("isGlobal"),
    /** When true, this column is indexed in the database. */
    IS_INDEXED("isIndexed"),
    /** When true, the values in this column can be null. */
    IS_NULLABLE("isNullable"),
    /** Whether this column is a partition column (true) or not (false). */
    IS_PARTITION("isPartition"),
    /** Whether this partition is further partitioned (true) or not (false). */
    IS_PARTITIONED("isPartitioned"),
    /** Whether this column is pinned (true) or not (false). */
    IS_PINNED("isPinned"),
    /** When true, this column is the primary key for the table. */
    IS_PRIMARY("isPrimary"),
    /** Whether this query is private (true) or shared (false). */
    IS_PRIVATE("isPrivate"),
    /** Whether this asset has been profiled (true) or not (false). */
    IS_PROFILED("isProfiled"),
    /** Whether it's possible to run a preview query on this materialized view (true) or not (false). */
    IS_QUERY_PREVIEW("isQueryPreview"),
    /** Whether this column is a sort column (true) or not (false). */
    IS_SORT("isSort"),
    /** Whether this query is a SQL snippet (true) or not (false). */
    IS_SQL_SNIPPET("isSqlSnippet"),
    /** Whether this materialized view is temporary (true) or not (false). */
    IS_TEMPORARY("isTemporary"),
    /** Whether this query is a visual query (true) or not (false). */
    IS_VISUAL_QUERY("isVisualQuery"),
    /** Whether the rule is currently snoozed (true) or not (false). */
    MC_MONITOR_RULE_IS_SNOOZED("mcMonitorRuleIsSnoozed"),
    /** Whether the subject is a schema for the keys of the messages (true) or not (false). */
    SCHEMA_REGISTRY_SUBJECT_IS_KEY_SCHEMA("schemaRegistrySubjectIsKeySchema"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    BooleanFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
