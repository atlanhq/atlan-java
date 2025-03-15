/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
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
    ADLS("adls", AtlanConnectionCategory.OBJECT_STORE),
    SIGMA("sigma", AtlanConnectionCategory.BI),
    SYNAPSE("synapse", AtlanConnectionCategory.WAREHOUSE),
    AIRFLOW("airflow", AtlanConnectionCategory.ELT),
    OPENLINEAGE("openlineage", AtlanConnectionCategory.ELT),
    DATAFLOW("dataflow", AtlanConnectionCategory.ELT),
    QLIKSENSE("qlik-sense", AtlanConnectionCategory.BI),
    KAFKA("kafka", AtlanConnectionCategory.EVENT_BUS),
    QUICKSIGHT("quicksight", AtlanConnectionCategory.BI),
    SAP_IQ("sap-iq", AtlanConnectionCategory.WAREHOUSE),
    HEX("hex", AtlanConnectionCategory.ELT),
    TERADATA("teradata", AtlanConnectionCategory.WAREHOUSE),
    YUGABYTEDB("yugabytedb", AtlanConnectionCategory.DATABASE),
    IBM_INFORMIX("ibm-informix", AtlanConnectionCategory.DATABASE),
    SAP_SQL("sap-sql", AtlanConnectionCategory.DATABASE),
    ORACLE_TIMESTEN("oracle-timesten", AtlanConnectionCategory.DATABASE),
    PERCONA_SERVER("percona-server", AtlanConnectionCategory.DATABASE),
    AURORA("aurora", AtlanConnectionCategory.DATABASE),
    SAP_MAXDB("sap-maxdb", AtlanConnectionCategory.DATABASE),
    SQLITE("sqlite", AtlanConnectionCategory.DATABASE),
    ROCKSET("rockset", AtlanConnectionCategory.WAREHOUSE),
    MONGODB("mongodb", AtlanConnectionCategory.DATABASE),
    GREENPLUM("greenplum", AtlanConnectionCategory.WAREHOUSE),
    MONETDB("monetdb", AtlanConnectionCategory.WAREHOUSE),
    ALLOYDB("alloydb", AtlanConnectionCategory.DATABASE),
    COCKROACHDB("cockroachdb", AtlanConnectionCategory.DATABASE),
    AZURE_COSMOS_DB("azure-cosmos-db", AtlanConnectionCategory.DATABASE),
    AZURE_ANALYSIS_SERVICES("azure-analysis-services", AtlanConnectionCategory.WAREHOUSE),
    SINGLESTORE("singlestore", AtlanConnectionCategory.WAREHOUSE),
    FIREBIRD("firebird", AtlanConnectionCategory.DATABASE),
    THOUGHTSPOT("thoughtspot", AtlanConnectionCategory.BI),
    CLICKHOUSE("clickhouse", AtlanConnectionCategory.WAREHOUSE),
    MULESOFT("mulesoft", AtlanConnectionCategory.API),
    CLARI("clari", AtlanConnectionCategory.SAAS),
    MARKETO("marketo", AtlanConnectionCategory.SAAS),
    AZURE_DATA_LAKE("azure-data-lake", AtlanConnectionCategory.LAKE),
    DELTA_LAKE("delta-lake", AtlanConnectionCategory.LAKE),
    MINISQL("minisql", AtlanConnectionCategory.DATABASE),
    ICEBERG("iceberg", AtlanConnectionCategory.WAREHOUSE),
    IMPALA("impala", AtlanConnectionCategory.WAREHOUSE),
    SPARK_SQL("spark-sql", AtlanConnectionCategory.LAKE),
    MARIADB("mariadb", AtlanConnectionCategory.DATABASE),
    FIREBOLT("firebolt", AtlanConnectionCategory.WAREHOUSE),
    CLOUDERA_DATA_WAREHOUSE("cloudera-data-warehouse", AtlanConnectionCategory.WAREHOUSE),
    STARBURST_GALAXY("starburst-galaxy", AtlanConnectionCategory.WAREHOUSE),
    REDIS("redis", AtlanConnectionCategory.DATABASE),
    GRAPHQL("graphql", AtlanConnectionCategory.DATABASE),
    ALTERYX("alteryx", AtlanConnectionCategory.BI),
    REDASH("redash", AtlanConnectionCategory.BI),
    SISENSE("sisense", AtlanConnectionCategory.BI),
    MONTE_CARLO("monte-carlo", AtlanConnectionCategory.DATA_QUALITY),
    SODA("soda", AtlanConnectionCategory.DATA_QUALITY),
    MATILLION("matillion", AtlanConnectionCategory.ELT),
    AIVEN_KAFKA("aiven-kafka", AtlanConnectionCategory.EVENT_BUS),
    APACHE_KAFKA("apache-kafka", AtlanConnectionCategory.EVENT_BUS),
    AZURE_EVENT_HUB("azure-event-hub", AtlanConnectionCategory.EVENT_BUS),
    CONFLUENT_KAFKA("confluent-kafka", AtlanConnectionCategory.EVENT_BUS),
    REDPANDA_KAFKA("redpanda-kafka", AtlanConnectionCategory.EVENT_BUS),
    AMAZON_MSK("amazon-msk", AtlanConnectionCategory.EVENT_BUS),
    CONFLUENT_SCHEMA_REGISTRY("confluent-schema-registry", AtlanConnectionCategory.SCHEMA_REGISTRY),
    GAINSIGHT("gainsight", AtlanConnectionCategory.DATABASE),
    AIRFLOW_ASTRONOMER("airflow-astronomer", AtlanConnectionCategory.ELT),
    AIRFLOW_MWAA("airflow-mwaa", AtlanConnectionCategory.ELT),
    AIRFLOW_CLOUD_COMPOSER("airflow-cloud-composer", AtlanConnectionCategory.ELT),
    SPARK("spark", AtlanConnectionCategory.ELT),
    MPARTICLE("mparticle", AtlanConnectionCategory.DATABASE),
    ESSBASE("essbase", AtlanConnectionCategory.DATABASE),
    GENERIC("genericdb", AtlanConnectionCategory.DATABASE),
    FILE("file", AtlanConnectionCategory.OBJECT_STORE),
    MICROSTRATEGY("microstrategy", AtlanConnectionCategory.BI),
    AWS_SITE_WISE("aws-sitewise", AtlanConnectionCategory.DATABASE),
    AWS_GREENGRASS("aws-greengrass", AtlanConnectionCategory.DATABASE),
    COGNITE("cognite", AtlanConnectionCategory.SAAS),
    SYNDIGO("syndigo", AtlanConnectionCategory.SAAS),
    NETEZZA("netezza", AtlanConnectionCategory.WAREHOUSE),
    AZURE_SERVICE_BUS("azureservicebus", AtlanConnectionCategory.EVENT_BUS),
    SUPERSET("superset", AtlanConnectionCategory.BI),
    PREFECT("prefect", AtlanConnectionCategory.ELT),
    AZURE_DATA_FACTORY("azure-data-factory", AtlanConnectionCategory.ELT),
    MODEL("model", AtlanConnectionCategory.DATABASE),
    IICS("iics", AtlanConnectionCategory.ELT),
    ABINITIO("abinitio", AtlanConnectionCategory.ELT),
    SAP_S4_HANA("sap-s4-hana", AtlanConnectionCategory.WAREHOUSE),
    ANOMALO("anomalo", AtlanConnectionCategory.DATA_QUALITY),
    INRIVER("inriver", AtlanConnectionCategory.DATABASE),
    IBM_DB2("ibmdb2", AtlanConnectionCategory.DATABASE),
    AZURE_ACTIVE_DIRECTORY("azure-active-directory", AtlanConnectionCategory.SAAS),
    ADOBE_EXPERIENCE_MANAGER("adobe-experience-manager", AtlanConnectionCategory.SAAS),
    ADOBE_TARGET("adobe-target", AtlanConnectionCategory.SAAS),
    APACHE_PULSAR("apache-pulsar", AtlanConnectionCategory.EVENT_BUS),
    TREASURE_DATA("treasure-data", AtlanConnectionCategory.SAAS),
    SAP_GIGYA("sap-gigya", AtlanConnectionCategory.SAAS),
    SAP_HYBRIS("sap-hybris", AtlanConnectionCategory.SAAS),
    BIGID("bigid", AtlanConnectionCategory.SAAS),
    AWS_LAMBDA("aws-lambda", AtlanConnectionCategory.ELT),
    AWS_ECS("aws-ecs", AtlanConnectionCategory.ELT),
    AWS_BATCH("aws-batch", AtlanConnectionCategory.ELT),
    AWS_SAGEMAKER("aws-sagemaker", AtlanConnectionCategory.ELT),
    SHARE_POINT("share-point", AtlanConnectionCategory.SAAS),
    RDS("rds", AtlanConnectionCategory.WAREHOUSE),
    SHARED_DRIVE("shared-drive", AtlanConnectionCategory.OBJECT_STORE),
    UNKNOWN_CUSTOM("(custom)", AtlanConnectionCategory.API),
    APP("app", AtlanConnectionCategory.APP),
    ANAPLAN("anaplan", AtlanConnectionCategory.BI),
    CUSTOM("custom", AtlanConnectionCategory.CUSTOM),
    DATAVERSE("dataverse", AtlanConnectionCategory.SAAS),
    COSMOS("cosmos", AtlanConnectionCategory.DATABASE),
    DOMO("domo", AtlanConnectionCategory.BI),
    COGNOS("cognos", AtlanConnectionCategory.BI),
    CRATEDB("cratedb", AtlanConnectionCategory.DATABASE),
    KX("kx", AtlanConnectionCategory.DATABASE),
    DOCUMENTDB("documentdb", AtlanConnectionCategory.DATABASE);

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
        if (value == null || value.isEmpty()) {
            return null;
        }
        for (AtlanConnectorType b : AtlanConnectorType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return UNKNOWN_CUSTOM;
    }
}
