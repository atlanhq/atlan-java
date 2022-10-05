/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Types of connectors that exist in Atlan. All assets that are created with
 * one of these connector types will have a branded icon.
 */
public enum AtlanConnectorType implements AtlanEnum {
    SNOWFLAKE("snowflake", AtlanConnectionCategory.WAREHOUSE),
    TABLEAU("tableau", AtlanConnectionCategory.BI),
    REDSHIFT("redshift", AtlanConnectionCategory.WAREHOUSE),
    MYSQL("mysql", AtlanConnectionCategory.WAREHOUSE),
    MSSQL("mssql", AtlanConnectionCategory.WAREHOUSE),
    DATABRICKS("databricks", AtlanConnectionCategory.LAKE),
    BIGQUERY("bigquery", AtlanConnectionCategory.WAREHOUSE),
    POSTGRES("postgres", AtlanConnectionCategory.DATABASE),
    ATHENA("athena", AtlanConnectionCategory.QUERY_ENGINE),
    ORACLE("oracle", AtlanConnectionCategory.WAREHOUSE),
    GLUE("glue", AtlanConnectionCategory.LAKE),
    POWERBI("powerbi", AtlanConnectionCategory.BI),
    LOOKER("looker", AtlanConnectionCategory.BI),
    METABASE("metabase", AtlanConnectionCategory.BI),
    MODE("mode", AtlanConnectionCategory.BI),
    SALESFORCE("salesforce", AtlanConnectionCategory.SAAS),
    S3("s3", AtlanConnectionCategory.OBJECT_STORE),
    DATASTUDIO("datastudio", AtlanConnectionCategory.BI),
    TRINO("trino", AtlanConnectionCategory.DATABASE),
    PRESTO("presto", AtlanConnectionCategory.DATABASE),
    NETSUITE("netsuite", AtlanConnectionCategory.WAREHOUSE),
    FIVETRAN("fivetran", AtlanConnectionCategory.ELT),
    DBT("dbt", AtlanConnectionCategory.ENRICHER),
    VERTICA("vertica", AtlanConnectionCategory.WAREHOUSE),
    PRESET("preset", AtlanConnectionCategory.BI),
    DYNAMODB("dynamodb", AtlanConnectionCategory.WAREHOUSE);

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    @Getter
    private final AtlanConnectionCategory category;

    AtlanConnectorType(String value, AtlanConnectionCategory category) {
        this.value = value;
        this.category = category;
    }

    public static AtlanConnectorType fromValue(String value) {
        for (AtlanConnectorType b : AtlanConnectorType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
