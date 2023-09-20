/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Types of packages (workflows) that exist in Atlan.
 */
public enum AtlanPackageType implements AtlanEnum {
    AIRFLOW("atlan-airflow"),
    ATHENA("atlan-athena"),
    AWS_LAMBDA_TRIGGER("atlan-aws-lambda-trigger"),
    AZURE_EVENT_HUB("atlan-azure-event-hub"),
    BIGQUERY("atlan-bigquery"),
    BIGQUERY_MINER("atlan-bigquery-miner"),
    CONNECTION_DELETE("atlan-connection-delete"),
    DATABRICKS("atlan-databricks"),
    DATABRICKS_LINEAGE("atlan-databricks-lineage"),
    DBT("atlan-dbt"),
    FIVETRAN("atlan-fivetran"),
    GLUE("atlan-glue"),
    HIVE("atlan-hive"),
    HIVE_MINER("atlan-hive-miner"),
    KAFKA("atlan-kafka"),
    KAFKA_AIVEN("atlan-kafka-aiven"),
    KAFKA_CONFLUENT_CLOUD("atlan-kafka-confluent-cloud"),
    KAFKA_REDPANDA("atlan-kafka-redpanda"),
    LOOKER("atlan-looker"),
    MATILLION("atlan-matillion"),
    METABASE("atlan-metabase"),
    MICROSTRATEGY("atlan-microstrategy"),
    MODE("atlan-mode"),
    MONTE_CARLO("atlan-monte-carlo"),
    MSSQL("atlan-mssql"),
    MSSQL_MINER("atlan-mssql-miner"),
    MYSQL("atlan-mysql"),
    ORACLE("atlan-oracle"),
    POSTGRES("atlan-postgres"),
    POWERBI("atlan-powerbi"),
    POWERBI_MINER("atlan-powerbi-miner"),
    PRESTO("atlan-presto"),
    QLIK_SENSE("atlan-qlik-sense"),
    QLIK_SENSE_ENTERPRISE_WINDOWS("atlan-qlik-sense-enterprise-windows"),
    QUICKSIGHT("atlan-quicksight"),
    REDASH("atlan-redash"),
    REDSHIFT("atlan-redshift"),
    REDSHIFT_MINER("atlan-redshift-miner"),
    SALESFORCE("atlan-salesforce"),
    SAP_HANA("atlan-sap-hana"),
    SCHEMA_REGISTRY_CONFLUENT("atlan-schema-registry-confluent"),
    SIGMA("atlan-sigma"),
    SNOWFLAKE("atlan-snowflake"),
    SNOWFLAKE_MINER("atlan-snowflake-miner"),
    SODA("atlan-soda"),
    SYNAPSE("atlan-synapse"),
    TABLEAU("atlan-tableau"),
    TERADATA("atlan-teradata"),
    TERADATA_MINER("atlan-teradata-miner"),
    THOUGHTSPOT("atlan-thoughtspot"),
    TRINO("atlan-trino"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanPackageType(String value) {
        this.value = value;
    }

    public static AtlanPackageType fromValue(String value) {
        for (AtlanPackageType b : AtlanPackageType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
