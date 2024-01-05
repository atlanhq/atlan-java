/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QueryRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** SQL query to run. */
    String sql;

    /** Unique name of the connection to use for the query. */
    String dataSourceName;

    /** Default schema name to use for unqualified objects in the SQL, in the form {@code DB.SCHEMA}. */
    String defaultSchema;

    /**
     * Builds the minimal object necessary to run a query.
     *
     * @param sql the SQL code to parse
     * @param connectionQualifiedName qualifiedName of the connection on which to run the query
     * @return the minimal request necessary to run a query, as a builder
     */
    public static QueryRequestBuilder<?, ?> creator(String sql, String connectionQualifiedName) {
        return QueryRequest.builder().sql(sql).dataSourceName(connectionQualifiedName);
    }
}
