/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.QueryParserSourceType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QueryParserRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** SQL query to be parsed. */
    String sql;

    /** Dialect to use when parsing the SQL. */
    QueryParserSourceType source;

    /** Default database name to use for unqualified objects in the SQL. */
    String defaultDatabase;

    /** Default schema name to use for unqualified objects in the SQL. */
    String defaultSchema;

    /** TBC */
    @Builder.Default
    Boolean linkOrphanColumnToFirstTable = false;

    /** TBC */
    @Builder.Default
    Boolean showJoin = true;

    /** TBC */
    @Builder.Default
    Boolean ignoreRecordSet = true;

    /** TBC */
    @Builder.Default
    Boolean ignoreCoordinate = true;

    /**
     * Builds the minimal object necessary to parse a query.
     *
     * @param sql the SQL code to parse
     * @param source type of data store for the SQL code
     * @return the minimal request necessary to parse a query, as a builder
     */
    public static QueryParserRequestBuilder<?, ?> creator(String sql, QueryParserSourceType source) {
        return QueryParserRequest.builder().sql(sql).source(source);
    }
}
