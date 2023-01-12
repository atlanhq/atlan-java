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
    POSTGRES("postgres", AtlanConnectionCategory.DATABASE),
    ATHENA("athena", AtlanConnectionCategory.QUERY_ENGINE),
    DATABRICKS("databricks", AtlanConnectionCategory.LAKE),
    POWERBI("powerbi", AtlanConnectionCategory.BI),
    BIGQUERY("bigquery", AtlanConnectionCategory.WAREHOUSE),
    LOOKER("looker", AtlanConnectionCategory.BI),
    METABASE("metabase", AtlanConnectionCategory.BI),
    SALESFORCE("salesforce", AtlanConnectionCategory.SAAS),
    MYSQL("mysql", AtlanConnectionCategory.WAREHOUSE),
    MSSQL("mssql", AtlanConnectionCategory.WAREHOUSE),
    S3("s3", AtlanConnectionCategory.OBJECT_STORE),
    PRESTO("presto", AtlanConnectionCategory.DATABASE),
    TRINO("trino", AtlanConnectionCategory.DATABASE),
    DATASTUDIO("datastudio", AtlanConnectionCategory.BI),
    GLUE("glue", AtlanConnectionCategory.LAKE),
    ORACLE("oracle", AtlanConnectionCategory.WAREHOUSE),
    NETSUITE("netsuite", AtlanConnectionCategory.WAREHOUSE),
    MODE("mode", AtlanConnectionCategory.BI),
    DBT("dbt", AtlanConnectionCategory.ELT),
    FIVETRAN("fivetran", AtlanConnectionCategory.ELT),
    VERTICA("vertica", AtlanConnectionCategory.WAREHOUSE),
    PRESET("preset", AtlanConnectionCategory.BI),
    API("api", AtlanConnectionCategory.API),
    DYNAMODB("dynamodb", AtlanConnectionCategory.WAREHOUSE),
    GCS("gcs", AtlanConnectionCategory.OBJECT_STORE),
    HIVE("hive", AtlanConnectionCategory.WAREHOUSE),
    SAPHANA("sap-hana", AtlanConnectionCategory.WAREHOUSE),
    ADLS("adls", AtlanConnectionCategory.OBJECT_STORE);

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
