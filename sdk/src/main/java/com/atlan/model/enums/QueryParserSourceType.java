/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum QueryParserSourceType implements AtlanEnum {
    ANSI("ansi"),
    BIGQUERY("bigquery"),
    HANA("hana"),
    HIVE("hive"),
    MSSQL("mssql"),
    MYSQL("mysql"),
    ORACLE("oracle"),
    POSTGRESQL("postgresql"),
    REDSHIFT("redshift"),
    SNOWFLAKE("snowflake"),
    SPARKSQL("sparksql"),
    ATHENA("athena");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    QueryParserSourceType(String value) {
        this.value = value;
    }

    public static QueryParserSourceType fromValue(String value) {
        for (QueryParserSourceType b : QueryParserSourceType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
