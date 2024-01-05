/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import com.atlan.net.ApiEventStreamResource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Response containing the details of a parsed SQL query.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class QueryResponse extends ApiEventStreamResource {
    private static final long serialVersionUID = 2L;

    /**
     * Create a single consolidated response from multiple events related to the same query.
     * (All ApiEventStreamResource objects must have a List constructor like this, as it is
     * used as part of the response deserialization process, via reflection.)
     *
     * @param events detailing the streamed results of a query
     */
    public QueryResponse(List<QueryResponse> events) {
        if (events != null) {
            rows = new ArrayList<>();
            columns = new ArrayList<>();
            // Populate the results from all events that have rows
            for (QueryResponse event : events) {
                if (event.getRows() != null && !event.getRows().isEmpty()) {
                    rows.addAll(event.getRows());
                }
                if (columns.isEmpty()
                        && event.getColumns() != null
                        && !event.getColumns().isEmpty()) {
                    // Only need to do this once
                    columns = event.getColumns();
                }
            }
            // Populate the remainder from the final event
            QueryResponse lastEvent = events.get(events.size() - 1);
            requestId = lastEvent.getRequestId();
            errorName = lastEvent.getErrorName();
            errorMessage = lastEvent.getErrorMessage();
            errorCode = lastEvent.getErrorCode();
            queryId = lastEvent.getQueryId();
            details = lastEvent.getDetails();
        }
    }

    /**
     * All-args constructor needed by Jackson to deserialize individual events.
     * (Used by Jackson as part of the response deserialization process.)
     *
     * @param requestId see {@link #requestId}
     * @param errorName see {@link #errorName}
     * @param errorMessage see {@link #errorMessage}
     * @param errorCode see {@link #errorCode}
     * @param queryId see {@link #queryId}
     * @param rows see {@link #rows}
     * @param columns see {@link #columns}
     * @param details see {@link #details}
     */
    @JsonCreator
    public QueryResponse(
            @JsonProperty("requestId") String requestId,
            @JsonProperty("errorName") String errorName,
            @JsonProperty("errorMessage") String errorMessage,
            @JsonProperty("errorCode") String errorCode,
            @JsonProperty("queryId") String queryId,
            @JsonProperty("rows") List<List<String>> rows,
            @JsonProperty("columns") List<ColumnDetails> columns,
            @JsonProperty("details") QueryDetails details) {
        this.requestId = requestId;
        this.errorName = errorName;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.queryId = queryId;
        this.rows = rows;
        this.columns = columns;
        this.details = details;
    }

    /** Unique identifier for the request, if there was any error. */
    String requestId;

    /** Unique name for the error, if there was any error. */
    String errorName;

    /** Explanation of the error, if there was any error. */
    String errorMessage;

    /** Unique code for the error, if there was any error. */
    String errorCode;

    /** Unique identifier (GUID) for the specific run of the query. */
    String queryId;

    /**
     * Results of the query. Each element is of the outer list is a single row,
     * while each inner list gives the column values for that row (in order).
     */
    List<List<String>> rows;

    /**
     * Columns that are present in each row of the results. The order of the elements
     * of this list will match the order of the inner list of values for the {@link #rows}.
     */
    List<ColumnDetails> columns;

    /** Details about the query. */
    QueryDetails details;

    /** Details about the columns that were returned from a query that was run. */
    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class ColumnDetails extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Position of the column (1-based). */
        Integer ordinal;

        Boolean autoIncrement;

        Boolean caseSensitive;
        Boolean searchable;
        Boolean currency;
        Integer nullable;
        Boolean signed;
        Integer displaySize;

        /** Display value for the column's name. */
        String label;

        /** Name of the column (technical). */
        String columnName;

        /** Name of the schema in which this column's table is contained. */
        String schemaName;

        Integer precision;

        Integer scale;

        /** Name of the table in which the column is contained. */
        String tableName;

        /** Name of the database in which the table's schema is contained. */
        String catalogName;

        Boolean readOnly;
        Boolean writable;
        Boolean definitelyWritable;

        /** Canonical name of the Java class representing this column's values. */
        String columnClassName;

        /** Details about the (SQL) data type of the column. */
        ColumnType type;
    }

    /** Details about the type of column that was returned from a query that was run. */
    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class ColumnType extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Numeric identifier for this column, generally matches the ordinal of the column. */
        Integer id;

        /** SQL name of the data type for this column. */
        String name;

        /** TBC */
        String rep;
    }

    /** Details about a query that was run. */
    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class QueryDetails extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Total number of results returned by the query. */
        Long totalRowsStreamed;

        /** Status of the query. */
        String status; // TODO: enum -- started, in-progress, completed, error <-- different structure to the rest in
        // this case

        /** TBC */
        String parsedQuery;

        /** Query that was sent to the data store. */
        String pushdownQuery;

        /** How long the query took to run, in milliseconds. */
        Long executionTime;

        String sourceQueryId;

        String resultOutputLocation;

        List<Object> warnings;

        String parsingFlow;

        String hekaFlow;

        String s3UploadPath;

        @JsonProperty("source_first_connection_time_perc")
        Double sourceFirstConnectionTimePercentage;

        @JsonProperty("explain_call_time_perc")
        Double explainCallTimePercentage;

        // TODO: include remaining properties
        /*
            More...
            "init_data_source_time_perc": 0.0,
        "authorization_time_perc": 0.0,
        "rewrite_validation_time_perc": 0.001,
        "extract_table_metadata_time": 32,
        "execution_time": 370,
        "bypass_parsing_time": 3,
        "check_insights_enabled_time": 0,
        "initialization_time_perc": 0.0,
        "rewrite_validation_time": 2,
        "init_data_source_time": 0,
        "extract_credentials_time_perc": 0.0,
        "overall_time_perc": 1.0,
        "heka_atlan_time": 1847,
        "bypass_parsing_time_perc": 0.001,
        "extract_credentials_time": 0,
        "extract_table_metadata_time_perc": 0.014,
        "initialization_time": 1,
        "calcite_parsing_time_perc": 0,
        "overall_time": 2217,
        "calcite_validation_time_perc": 0,
        "execution_time_perc": 0.167,
        "source_first_connection_time": 1633,
        "authorization_time": 0,
        "check_insights_enabled_time_perc": 0.0
             */

        /** Detailed back-end error message that could be helpful for developers. */
        String developerMessage;
    }

    /** Details about an asset that was queried, in case of errors. */
    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class AssetDetails extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Simple name of the connection. */
        String connectionName;

        /** Unique name of the connection. */
        String connectionQN;

        /** Simple name of the database. */
        String database;

        /** Simple name of the schema. */
        String schema;

        /** Simple name of the table. */
        String table; // TODO: what about views?
    }
}
