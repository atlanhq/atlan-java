/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.HekaFlow;
import com.atlan.model.enums.ParsingFlow;
import com.atlan.model.enums.QueryStatus;
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
@SuppressWarnings("serial")
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

        /** TBC */
        Boolean autoIncrement;

        /** TBC */
        Boolean caseSensitive;

        /** TBC */
        Boolean searchable;

        /** TBC */
        Boolean currency;

        /** TBC */
        Integer nullable;

        /** TBC */
        Boolean signed;

        /** TBC */
        Integer displaySize;

        /** Display value for the column's name. */
        String label;

        /** Name of the column (technical). */
        String columnName;

        /** Name of the schema in which this column's table is contained. */
        String schemaName;

        /** TBC */
        Integer precision;

        /** TBC */
        Integer scale;

        /** Name of the table in which the column is contained. */
        String tableName;

        /** Name of the database in which the table's schema is contained. */
        String catalogName;

        /** TBC */
        Boolean readOnly;

        /** TBC */
        Boolean writable;

        /** TBC */
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
        QueryStatus status;

        /** TBC */
        String parsedQuery;

        /** Query that was sent to the data store. */
        String pushdownQuery;

        /** How long the query took to run, in milliseconds. */
        Long executionTime;

        /** TBC */
        String sourceQueryId;

        /** TBC */
        String resultOutputLocation;

        /** List of any warnings produced when running the query. */
        List<String> warnings;

        /** How the query was parsed prior to running. */
        ParsingFlow parsingFlow;

        /** How the query was run. */
        HekaFlow hekaFlow;

        /** TBC */
        String s3UploadPath;

        /** TBC */
        @JsonProperty("source_first_connection_time")
        Integer sourceFirstConnectionTime;

        /** TBC */
        @JsonProperty("source_first_connection_time_perc")
        Double sourceFirstConnectionPercentage;

        /** TBC */
        @JsonProperty("explain_call_time_perc")
        Double explainCallTimePercentage;

        /** TBC */
        @JsonProperty("init_data_source_time")
        Integer initDataSourceTime;

        /** TBC */
        @JsonProperty("init_data_source_time_perc")
        Double initDataSourcePercentage;

        /** TBC */
        @JsonProperty("authorization_time")
        Integer authorizationTime;

        /** TBC */
        @JsonProperty("authorization_time_perc")
        Double authorizationPercentage;

        /** TBC */
        @JsonProperty("rewrite_validation_time")
        Integer rewriteValidationTime;

        /** TBC */
        @JsonProperty("rewrite_validation_time_perc")
        Double rewriteValidationPercentage;

        /** Elapsed time to extract table metadata, in milliseconds. */
        @JsonProperty("extract_table_metadata_time")
        Integer extractTableMetadataTime;

        /** TBC */
        @JsonProperty("extract_table_metadata_time_perc")
        Double extractTableMetadataPercentage;

        /** Elapsed time to run the query (from internal engine), in milliseconds. */
        @JsonProperty("execution_time")
        Integer executionTimeInternal;

        /** TBC */
        @JsonProperty("execution_time_perc")
        Double executionPercentage;

        /** TBC */
        @JsonProperty("bypass_parsing_time")
        Integer bypassQueryTime;

        /** TBC */
        @JsonProperty("bypass_parsing_time_perc")
        Double bypassParsingPercentage;

        /** TBC */
        @JsonProperty("check_insights_enabled_time")
        Integer checkInsightsEnabledTime;

        /** TBC */
        @JsonProperty("check_insights_enabled_time_perc")
        Double checkInsightsEnabledPercentage;

        /** TBC */
        @JsonProperty("initialization_time")
        Integer initializationTime;

        /** TBC */
        @JsonProperty("initialization_time_perc")
        Double initializationPercentage;

        /** TBC */
        @JsonProperty("extract_credentials_time")
        Integer extractCredentialsTime;

        /** TBC */
        @JsonProperty("extract_credentials_time_perc")
        Double extractCredentialsPercentage;

        /** TBC */
        @JsonProperty("overall_time")
        Integer overallTime;

        /** TBC */
        @JsonProperty("overall_time_perc")
        Double overallTimePercentage;

        /** TBC */
        @JsonProperty("heka_atlan_time")
        Integer hekaAtlanTime;

        /** TBC */
        @JsonProperty("calcite_parsing_time_perc")
        Double calciteParsingPercentage;

        /** TBC */
        @JsonProperty("calcite_validation_time_perc")
        Double calciteValidationPercentage;

        /** Metadata about the asset used in the query, in case of any errors. */
        AssetDetails asset;

        /** Detailed back-end error message that could be helpful for developers. */
        String developerMessage;

        /** Line number of the query that had a validation error, if any. */
        Long line;

        /** Column position of the validation error, if any. */
        Long column;

        /** Name of the object that caused the validation error, if any. */
        String object;
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
        String table;
    }
}
