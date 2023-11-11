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
public enum TextFields implements AtlanSearchableField {
    /** Name of the account in which this asset exists in dbt. */
    ASSET_DBT_ACCOUNT_NAME("assetDbtAccountName"),
    /** Alias of this asset in dbt. */
    ASSET_DBT_ALIAS("assetDbtAlias"),
    /** Name of the environment in which this asset is materialized in dbt. */
    ASSET_DBT_ENVIRONMENT_NAME("assetDbtEnvironmentName"),
    /** Branch in git from which the last run of the job that materialized this asset in dbt ran. */
    ASSET_DBT_JOB_LAST_RUN_GIT_BRANCH("assetDbtJobLastRunGitBranch.text"),
    /** Status message of the last run of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_LAST_RUN_STATUS_MESSAGE("assetDbtJobLastRunStatusMessage"),
    /** Name of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_NAME("assetDbtJobName"),
    /** Human-readable time when the next run of the job that materializes this asset in dbt is scheduled. */
    ASSET_DBT_JOB_NEXT_RUN_HUMANIZED("assetDbtJobNextRunHumanized"),
    /** Human-readable cron schedule of the job that materialized this asset in dbt. */
    ASSET_DBT_JOB_SCHEDULE_CRON_HUMANIZED("assetDbtJobScheduleCronHumanized"),
    /** Name of the package in which this asset exists in dbt. */
    ASSET_DBT_PACKAGE_NAME("assetDbtPackageName"),
    /** Name of the project in which this asset exists in dbt. */
    ASSET_DBT_PROJECT_NAME("assetDbtProjectName"),
    /** List of tags attached to this asset in dbt. */
    ASSET_DBT_TAGS("assetDbtTags.text"),
    /** Unique identifier of this asset in dbt. */
    ASSET_DBT_UNIQUE_ID("assetDbtUniqueId"),
    /** List of Monte Carlo incident names attached to this asset. */
    ASSET_MC_INCIDENT_NAMES("assetMcIncidentNames"),
    /** List of unique Monte Carlo incident names attached to this asset. */
    ASSET_MC_INCIDENT_QUALIFIED_NAMES("assetMcIncidentQualifiedNames.text"),
    /** List of Monte Carlo monitor names attached to this asset. */
    ASSET_MC_MONITOR_NAMES("assetMcMonitorNames"),
    /** List of unique Monte Carlo monitor names attached to this asset. */
    ASSET_MC_MONITOR_QUALIFIED_NAMES("assetMcMonitorQualifiedNames.text"),
    /** List of tags attached to this asset. */
    ASSET_TAGS("assetTags.text"),
    /** All Atlan tags that exist on an asset, whether directly assigned or propagated, searchable by the internal hashed-string ID of the Atlan tag. */
    ATLAN_TAGS_TEXT("__classificationsText"),
    /** Status of this asset's certification. */
    CERTIFICATE_STATUS("certificateStatus.text"),
    /** Unique name of the collection in which this query exists. */
    COLLECTION_QUALIFIED_NAME("collectionQualifiedName.text"),
    /** Simple name of the connection through which this asset is accessible. */
    CONNECTION_NAME("connectionName.text"),
    /** Unique name of the connection through which this asset is accessible. */
    CONNECTION_QUALIFIED_NAME("connectionQualifiedName.text"),
    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    DATABASE_NAME("databaseName"),
    /** Data type of values in this column. */
    DATA_TYPE("dataType.text"),
    /** Unique name of this asset in dbt. */
    DBT_QUALIFIED_NAME("dbtQualifiedName.text"),
    /** Unique name of the default database to use for this query. */
    DEFAULT_DATABASE_QUALIFIED_NAME("defaultDatabaseQualifiedName.text"),
    /** Unique name of the default schema to use for this query. */
    DEFAULT_SCHEMA_QUALIFIED_NAME("defaultSchemaQualifiedName.text"),
    /** Description of this asset, for example as crawled from a source. Fallback for display purposes, if userDescription is empty. */
    DESCRIPTION("description"),
    /** Human-readable name of this asset used for display purposes (in user interface). */
    DISPLAY_NAME("displayName"),
    /** All terms attached to an asset, as a single comma-separated string. */
    MEANINGS_TEXT("__meaningsText"),
    /** Name of this asset. Fallback for display purposes, if displayName is empty. */
    NAME("name"),
    /** Simple name of the column this column is nested within, for STRUCT and NESTED columns. */
    PARENT_COLUMN_NAME("parentColumnName"),
    /** Unique name of the column this column is nested within, for STRUCT and NESTED columns. */
    PARENT_COLUMN_QUALIFIED_NAME("parentColumnQualifiedName.text"),
    /** Unique name of the parent collection or folder in which this query exists. */
    PARENT_QUALIFIED_NAME("parentQualifiedName.text"),
    /** Unique fully-qualified name of the asset in Atlan. */
    QUALIFIED_NAME("qualifiedName.text"),
    /** URL for sample data for this asset. */
    SAMPLE_DATA_URL("sampleDataUrl.text"),
    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    SCHEMA_NAME("schemaName"),
    /** All super types of an asset. */
    SUPER_TYPE_NAMES("__superTypeNames"),
    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    TABLE_NAME("tableName"),
    /** Type of the asset. For example Table, Column, and so on. */
    TYPE_NAME("__typeName"),
    /** Description of this asset, as provided by a user. If present, this will be used for the description in user interface. */
    USER_DESCRIPTION("userDescription"),
    /** Simple name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    VIEW_NAME("viewName"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    TextFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
