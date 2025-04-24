/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.fields.BooleanField;
import com.atlan.model.fields.KeywordField;
import com.atlan.model.fields.NumericField;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for entries in the search log.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SearchLogEntry extends AtlanObject {
    private static final long serialVersionUID = 2L;

    private static final String UNMAPPED = "__NON_ASSET__";

    /** Details of the browser or other client used to make the request. */
    public static final KeywordField USER_AGENT = new KeywordField(UNMAPPED, "userAgent");

    /** Hostname of the tenant against which the search was run. */
    public static final KeywordField HOST = new KeywordField(UNMAPPED, "host");

    /** IP address from which the search was run. */
    public static final KeywordField IP_ADDRESS = new KeywordField(UNMAPPED, "ipAddress");

    /** Username of the user who ran the search. */
    public static final KeywordField USER = new KeywordField(UNMAPPED, "userName");

    /** GUID(s) of asset(s) that were in the results of the search. */
    public static final KeywordField ENTITY_ID = new KeywordField("guid", "entityGuidsAll");

    /** Unique name(s) of asset(s) that were in the results of the search. */
    public static final KeywordField QUALIFIED_NAME = new KeywordField("qualifiedName", "entityQFNamesAll");

    /** Name(s) of the types of assets that were in the results of the search. */
    public static final KeywordField TYPE_NAME = new KeywordField("typeName", "entityTypeNamesAll");

    /** Tag(s) that were sent in the search request. */
    public static final KeywordField UTM_TAGS = new KeywordField(UNMAPPED, "utmTags");

    /** Text a user entered for their search. */
    public static final KeywordField SEARCH_INPUT = new KeywordField(UNMAPPED, "searchInput");

    /** Whether the search had any results (true) or not (false). */
    public static final BooleanField HAS_RESULT = new BooleanField(UNMAPPED, "hasResult");

    /** Number of results for the search. */
    public static final NumericField RESULTS_COUNT = new NumericField(UNMAPPED, "resultsCount");

    /** Elapsed time to produce the results for the search, in milliseconds. */
    public static final NumericField RESPONSE_TIME = new NumericField(UNMAPPED, "responseTime");

    /** Time (epoch-style) at which the search was logged, in milliseconds. */
    public static final NumericField LOGGED_AT = new NumericField(UNMAPPED, "createdAt");

    /** Time (epoch-style) at which the search was initiated, in milliseconds. */
    public static final NumericField SEARCHED_AT = new NumericField(UNMAPPED, "timestamp");

    /** Whether the search was successful (false) or not (true). */
    public static final BooleanField FAILED = new BooleanField(UNMAPPED, "failed");

    /** Unique name of the persona through which the search or view was made. */
    String persona;

    /** Details of the browser or other client used to make the request. */
    String userAgent;

    /** Hostname of the tenant against which the search was run. */
    String host;

    /** IP address from which the search was run. */
    String ipAddress;

    /** Username of the user who ran the search. */
    String userName;

    /** List of all GUIDs for assets that were results of the search. */
    @JsonProperty("entityGuidsAll")
    List<String> resultGuids;

    /** List of all qualifiedNames for assets that were results of the search. */
    @JsonProperty("entityQFNamesAll")
    List<String> resultQualifiedNames;

    /** List of all GUIDs for assets that were results of the search, that the user is permitted to see. */
    @JsonProperty("entityGuidsAllowed")
    List<String> resultGuidsAllowed;

    /** List of all qualifiedNames for assets that were results of the search, that the user is permitted to see. */
    @JsonProperty("entityQFNamesAllowed")
    List<String> resultQualifiedNamesAllowed;

    /** List of all typeNames for assets that were results of the search. */
    @JsonProperty("entityTypeNamesAll")
    List<String> resultTypeNames;

    /** List of all typeNames for assets that were results of the search, that the user is permitted to see. */
    @JsonProperty("entityTypeNamesAllowed")
    List<String> resultTypeNamesAllowed;

    /** List of the UTM tags that were sent in the search request. */
    List<String> utmTags;

    /** Text a user entered for their search. */
    String searchInput;

    /** Whether the search had any results (true) or not (false). */
    Boolean hasResult;

    /** Number of results for the search. */
    Long resultsCount;

    /** Elapsed time to produce the results for the search, in milliseconds. */
    Long responseTime;

    /** Time (epoch-style) at which the search was logged, in milliseconds. */
    Long createdAt;

    /** Time (epoch-style) at which the search was run, in milliseconds. */
    Long timestamp;

    /** Whether the search was successful (false) or not (true). */
    Boolean failed;

    /** DSL of the full search request that was made. */
    @JsonProperty("request.dsl")
    IndexSearchDSL requestDsl;

    /** DSL of the full search request that was made, as a string. */
    @JsonProperty("request.dslText")
    String requestDslText;

    /** List of attribute (names) that were requested as part of the search. */
    @JsonProperty("request.attributes")
    List<String> requestAttributes;

    /** List of relationship attribute (names) that were requested as part of the search. */
    @JsonProperty("request.relationAttributes")
    List<String> requestRelationAttributes;
}
