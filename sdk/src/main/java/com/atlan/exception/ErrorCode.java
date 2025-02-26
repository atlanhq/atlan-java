/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

public enum ErrorCode implements ExceptionMessageSet {
    CONNECTION_ERROR(
            -1,
            "ATLAN-JAVA--1-001",
            "IOException occurred during API request to Atlan: {0}.",
            "Please check your internet connection and try again. If this problem persists,"
                    + "you should check Atlan's availability via a browser,"
                    + " or let us know at support@atlan.com."),
    NO_BASE_URL(
            -1,
            "ATLAN-JAVA--1-002",
            "No base URL is configured in the SDK.",
            "You must specify a URL for your tenant, before running any operation that makes an underlying API call."),

    INTERNAL_ONLY(
            -1,
            "ATLAN-JAVA--1-003",
            "This operation can only be used within the Atlan cluster.",
            "You cannot use this operation from any external call to Atlan, it can only be done within the back-end cluster."),

    INVALID_REQUEST_PASSTHROUGH(
            400,
            "ATLAN-JAVA-400-000",
            "Server responded with an invalid request -- {0}: {1} -- caused by: {2}",
            "Check the details of the server's message to correct your request."),
    MISSING_GROUP_ID(
            400,
            "ATLAN-JAVA-400-001",
            "No ID was provided when attempting to retrieve or update the group.",
            "You must provide an ID when attempting to retrieve or update a group."),
    MISSING_USER_ID(
            400,
            "ATLAN-JAVA-400-002",
            "No ID was provided attempting to retrieve or update the user.",
            "You must provide an ID when attempting to retrieve or update a user."),
    MISSING_TERM_GUID(
            400,
            "ATLAN-JAVA-400-003",
            "No GUID was specified for the term to be removed.",
            "You must provide the GUID of the term to be removed."),
    MISSING_ROLE_NAME(
            400,
            "ATLAN-JAVA-400-004",
            "No name was provided when attempting to retrieve a role.",
            "You must provide a name of a role when attempting to retrieve one."),
    MISSING_ROLE_ID(
            400,
            "ATLAN-JAVA-400-005",
            "No ID was provided when attempting to retrieve a role.",
            "You must provide an ID of a role when attempting to retrieve one."),
    MISSING_ATLAN_TAG_NAME(
            400,
            "ATLAN-JAVA-400-006",
            "No name was provided when attempting to retrieve an Atlan tag.",
            "You must provide a name of an Atlan tag when attempting to retrieve one."),
    MISSING_ATLAN_TAG_ID(
            400,
            "ATLAN-JAVA-400-007",
            "No ID was provided when attempting to retrieve an Atlan tag.",
            "You must provide an ID of an Atlan tag when attempting to retrieve one."),
    MISSING_CM_NAME(
            400,
            "ATLAN-JAVA-400-008",
            "No name was provided when attempting to retrieve custom metadata.",
            "You must provide a name for custom metadata when attempting to retrieve it."),
    MISSING_CM_ID(
            400,
            "ATLAN-JAVA-400-009",
            "No ID was provided when attempting to retrieve custom metadata.",
            "You must provide an ID for custom metadata when attempting to retrieve it."),
    MISSING_CM_ATTR_NAME(
            400,
            "ATLAN-JAVA-400-010",
            "No name was provided when attempting to retrieve a custom metadata property.",
            "You must provide a name for the custom metadata property when attempting to retrieve it."),
    MISSING_CM_ATTR_ID(
            400,
            "ATLAN-JAVA-400-011",
            "No ID was provided when attempting to retrieve a custom metadata property.",
            "You must provide an ID for the custom metadata property when attempting to retrieve it."),
    MISSING_ENUM_NAME(
            400,
            "ATLAN-JAVA-400-012",
            "No name was provided when attempting to retrieve an enumeration.",
            "You must provide a name for the enumeration when attempting to retrieve it."),
    NO_GRAPH_WITH_PROCESS(
            400,
            "ATLAN-JAVA-400-013",
            "Lineage was retrieved using hideProces=false. We do not provide a graph view in this case.",
            "Retry your request for lineage setting hideProcess=true."),
    UNABLE_TO_TRANSLATE_FILTERS(
            400,
            "ATLAN-JAVA-400-014",
            "Unable to translate the provided include/exclude asset filters into JSON.",
            "Verify the filters you provided. If the problem persists, please raise an issue on the Java SDK GitHub repository providing context in which this error occurred."),
    UNABLE_TO_CREATE_TYPEDEF_CATEGORY(
            400,
            "ATLAN-JAVA-400-015",
            "Unable to create new type definitions of category: {0}.",
            "Atlan currently only allows you to create type definitions for new custom metadata, enumerations and Atlan tags."),
    UNABLE_TO_UPDATE_TYPEDEF_CATEGORY(
            400,
            "ATLAN-JAVA-400-016",
            "Unable to update type definitions of category: {0}.",
            "Atlan currently only allows you to update type definitions for custom metadata, enumerations and Atlan tags."),
    MISSING_GUID_FOR_DELETE(
            400,
            "ATLAN-JAVA-400-017",
            "Insufficient information provided to delete assets.",
            "You must provide the GUID of the asset you want to delete."),
    MISSING_REQUIRED_UPDATE_PARAM(
            400,
            "ATLAN-JAVA-400-018",
            "One or more required parameters to update {0} are missing: {1}.",
            "You must provide all of the parameters listed to update assets of this type."),
    JSON_ERROR(
            400,
            "ATLAN-JAVA-400-019",
            "Invalid response object from API: {0}. (HTTP response code was {1}). Additional details: {2}.",
            "Atlan was unable to produce a valid response to your request. Please verify your request is valid."),
    NOTHING_TO_ENCODE(
            400,
            "ATLAN-JAVA-400-020",
            "Invalid null ID found for url path formatting.",
            "Verify the string ID argument to the API method is what you expect. It could be either the string ID itself is null or the relevant field in your Atlan object is null."),
    MISSING_REQUIRED_QUERY_PARAM(
            400,
            "ATLAN-JAVA-400-021",
            "One or more required parameters to query {0} are missing: {1}.",
            "You must provide all of the parameters listed to query assets of this type."),
    NO_CONNECTION_ADMIN(
            400,
            "ATLAN-JAVA-400-022",
            "No admin provided for the connection.",
            "You must specify at least one connection admin through adminRoles, adminGroups, or adminUsers to create a new connection. Without at least one admin, the connection will be inaccessible to all."),
    MISSING_PERSONA_ID(
            400,
            "ATLAN-JAVA-400-023",
            "No ID was provided when attempting to update the persona.",
            "You must provide an ID when attempting to update a persona."),
    MISSING_PURPOSE_ID(
            400,
            "ATLAN-JAVA-400-024",
            "No ID was provided when attempting to update the purpose.",
            "You must provide an ID when attempting to update a purpose."),
    NO_ATLAN_TAG_FOR_PURPOSE(
            400,
            "ATLAN-JAVA-400-025",
            "No Atlan tags provided for the purpose.",
            "You must specify at least one Atlan tag to create a new purpose."),
    NO_USERS_FOR_POLICY(
            400,
            "ATLAN-JAVA-400-026",
            "No user or group specified for the policy.",
            "You must specify at least one user or group to whom the policy in a purpose will be applied."),
    MISSING_GROUP_NAME(
            400,
            "ATLAN-JAVA-400-027",
            "No name was provided when attempting to retrieve a group.",
            "You must provide a name of a group when attempting to retrieve one."),
    MISSING_USER_NAME(
            400,
            "ATLAN-JAVA-400-028",
            "No name was provided when attempting to retrieve a user.",
            "You must provide a name of a user when attempting to retrieve one."),
    MISSING_USER_EMAIL(
            400,
            "ATLAN-JAVA-400-029",
            "No email address was provided when attempting to retrieve a user.",
            "You must provide an email address of a user when attempting to retrieve one."),
    MISSING_GROUP_ALIAS(
            400,
            "ATLAN-JAVA-400-030",
            "No alias was provided when attempting to retrieve or update the group.",
            "You must provide an alias when attempting to retrieve or update a group."),
    NOT_AGGREGATION_METRIC(
            400,
            "ATLAN-JAVA-400-031",
            "Requested extracting a metric from a non-metric aggregation result.",
            "You must provide an aggregation result that is a metric aggregation to extract a numeric metric."),
    MISSING_TOKEN_ID(
            400,
            "ATLAN-JAVA-400-032",
            "No ID was provided when attempting to update the API token.",
            "You must provide an ID when attempting to update an API token."),
    MISSING_TOKEN_NAME(
            400,
            "ATLAN-JAVA-400-033",
            "No displayName was provided when attempting to update the API token.",
            "You must provide a displayName for the API token when attempting to update it."),
    INVALID_LINEAGE_DIRECTION(
            400,
            "ATLAN-JAVA-400-034",
            "Can only request upstream or downstream lineage (not both) through the lineage list API.",
            "Change your provided 'direction' parameter to either upstream or downstream."),
    INVALID_URL(
            400,
            "ATLAN-JAVA-400-035",
            "The URL provided for uploading a file was invalid.",
            "Check the provided URL and attempt to upload again."),
    INACCESSIBLE_URL(
            400,
            "ATLAN-JAVA-400-036",
            "The URL provided could not be accessed.",
            "Check the provided URL and attempt to upload again."),
    NO_ATLAN_CLIENT(
            400,
            "ATLAN-JAVA-400-037",
            "No Atlan client has been provided.",
            "You must provide an Atlan client to this operation, or it has no information about which Atlan tenant to run against."),
    MISSING_REQUIRED_RELATIONSHIP_PARAM(
            400,
            "ATLAN-JAVA-400-038",
            "One or more required parameters to create a relationship to {0} are missing: {1}.",
            "You must provide all of the parameters listed to relate to assets of this type."),
    INVALID_QUERY(
            400,
            "ATLAN-JAVA-400-039",
            "Cannot create a {0} query on field: {1}.",
            "You can either try a different field, or try a different kind of query."),
    MISSING_CREDENTIALS(
            400,
            "ATLAN-JAVA-400-040",
            "Missing privileged credentials to impersonate users.",
            "You must have both CLIENT_ID and CLIENT_SECRET configured to be able to impersonate users."),
    FULL_UPDATE_ONLY(
            400,
            "ATLAN-JAVA-400-041",
            "Objects of type {0} should only be updated in full.",
            "For objects of this type you should not use trimToRequired but instead update the object in full."),
    CATEGORIES_CANNOT_BE_ARCHIVED(
            400,
            "ATLAN-JAVA-400-042",
            "Categories cannot be archived (soft-deleted): {0}.",
            "Please use the purge operation if you wish to remove a category."),
    INVALID_CREDENTIALS(
            400,
            "ATLAN-JAVA-400-043",
            "Credentials provided did not work: {0}.",
            "Please double-check your credentials and test them again."),
    MISSING_NAME(
            400,
            "ATLAN-JAVA-400-044",
            "No name was provided when attempting to retrieve an object.",
            "You must provide the name of the object when attempting to retrieve one."),
    MISSING_ID(
            400,
            "ATLAN-JAVA-400-045",
            "No ID was provided when attempting to retrieve or update the object.",
            "You must provide an ID when attempting to retrieve or update an object."),
    SSO_GROUP_MAPPING_ALREADY_EXISTS(
            400,
            "ATLAN-JAVA-400-046",
            "SSO group mapping already exists between {0} (Atlan group) and {1} (SSO group).",
            "Use updateGroupMapping() to update the existing group mapping."),
    UNEXPECTED_NUMBER_OF_DATABASES_FOUND(
            400,
            "ATLAN-JAVA-400-047",
            "Expected {0} database name(s) matching the given pattern {1} but found {2}.",
            "Use a more restrictive regular expression."),
    OPENLINEAGE_NOT_CONFIGURED(
            400,
            "ATLAN-JAVA-400-048",
            "Requested OpenLineage connector type {0} is not configured.",
            "You must first run the appropriate marketplace package to configure OpenLineage for this connector before you can send events for it."),
    CANNOT_CACHE_REFRESH_BY_SID(
            400,
            "ATLAN-JAVA-400-049",
            "Objects with secondary IDs can only be cached in bulk, not individually.",
            "This code path is intentionally unreachable. If you receive this error, please raise a ticket on the GitHub repository for the SDK."),
    PLACEHOLDER_GUID(
            400,
            "ATLAN-JAVA-400-050",
            "A placeholder GUID was provided where a resolved GUID is required: {0}.",
            "You must provide a real (resolved) GUID, not a placeholder, when carrying out the requested operation."),

    AUTHENTICATION_PASSTHROUGH(
            401,
            "ATLAN-JAVA-401-000",
            "Server responded with an authentication error -- {0}: {1} -- caused by: {2}",
            "Your API or bearer token is either invalid or has expired, or you are attempting to access a URL you are not authorized to access. Ensure you are using a valid token, that there is no service outage for the underlying authorization component, or try obtaining a new token and try again."),
    NO_API_TOKEN(
            401,
            "ATLAN-JAVA-401-001",
            "No API token provided.",
            "Set your API token using `client.setApiToken(\"<API-TOKEN>\");`. You can generate API tokens from the Atlan Admin Center. See "
                    + "https://ask.atlan.com/hc/en-us/articles/8312649180049 for details or contact support at "
                    + "https://ask.atlan.com/hc/en-us/requests/new if you have any questions."),
    EMPTY_API_TOKEN(
            401,
            "ATLAN-JAVA-401-002",
            "Your API token is invalid, as it is an empty string.",
            "You can double-check your API token from the Atlan Admin Center. See "
                    + "https://ask.atlan.com/hc/en-us/articles/8312649180049 for details or contact support at "
                    + "https://ask.atlan.com/hc/en-us/requests/new if you have any questions."),
    INVALID_API_TOKEN(
            401,
            "ATLAN-JAVA-401-003",
            "Your API token is invalid, as it contains whitespace.",
            "You can double-check your API token from the Atlan Admin Center. See "
                    + "https://ask.atlan.com/hc/en-us/articles/8312649180049 for details or contact support at "
                    + "https://ask.atlan.com/hc/en-us/requests/new if you have any questions."),
    EXPIRED_API_TOKEN(
            401,
            "ATLAN-JAVA-401-004",
            "Your API token is no longer valid, it can no longer lookup base Atlan structures.",
            "You can double-check your API token from the Atlan Admin Center. See "
                    + "https://ask.atlan.com/hc/en-us/articles/8312649180049 for details or contact support at "
                    + "https://ask.atlan.com/hc/en-us/requests/new if you have any questions."),

    PERMISSION_PASSTHROUGH(
            403,
            "ATLAN-JAVA-403-000",
            "Server responded with a permission error -- {0}: {1} -- caused by: {2}",
            "Check the details of the server's message to correct your request."),
    UNABLE_TO_IMPERSONATE(
            403,
            "ATLAN-JAVA-403-001",
            "Unable to impersonate requested user.",
            "Check the details of your configured privileged credentials and the user you requested to impersonate."),
    UNABLE_TO_ESCALATE(
            403,
            "ATLAN-JAVA-403-002",
            "Unable to escalate to a privileged user.",
            "Check the details of your configured privileged credentials."),

    NOT_FOUND_PASSTHROUGH(
            404,
            "ATLAN-JAVA-404-000",
            "Server responded with a not found error -- {0}: {1} -- caused by: {2}",
            "Check the details of the server's message to correct your request."),

    ASSET_NOT_FOUND_BY_GUID(
            404,
            "ATLAN-JAVA-404-001",
            "Asset with GUID {0} does not exist.",
            "Verify the GUID of the asset you are trying to retrieve."),
    ASSET_NOT_TYPE_REQUESTED(
            404,
            "ATLAN-JAVA-404-002",
            "Asset with GUID {0} is not of the type requested: {1}.",
            "Verify the GUID and expected type of the asset you are trying to retrieve."),
    ASSET_NOT_FOUND_BY_QN(
            404,
            "ATLAN-JAVA-404-003",
            "Asset with qualifiedName {0} of type {1} does not exist.",
            "Verify the qualifiedName and expected type of the asset you are trying to retrieve."),
    ROLE_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-004",
            "Role with name {0} does not exist.",
            "Verify the role name provided is a valid role name."),
    ROLE_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-005",
            "Role with GUID {0} does not exist.",
            "Verify the role GUID provided is a valid role GUID."),
    ATLAN_TAG_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-006",
            "Atlan tag with name {0} does not exist.",
            "Verify the Atlan tag name provided is a valid Atlan tag name. This should be the human-readable name of the Atlan tag."),
    ATLAN_TAG_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-007",
            "Atlan tag with ID {0} does not exist.",
            "Verify the Atlan tag ID provided is a valid Atlan tag ID. This should be the Atlan-internal hashed string representation."),
    CM_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-008",
            "Custom metadata with name {0} does not exist.",
            "Verify the custom metadata name provided is a valid custom metadata name. This should be the human-readable name of the custom metadata."),
    CM_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-009",
            "Custom metadata with ID {0} does not exist.",
            "Verify the custom metadata ID provided is a valid custom metadata ID. This should be the Atlan-internal hashed string representation."),
    CM_NO_ATTRIBUTES(
            404,
            "ATLAN-JAVA-404-010",
            "Custom metadata with ID {0} does not have any attributes.",
            "Verify the custom metadata ID you are accessing has attributes defined before attempting to retrieve one of them."),
    CM_ATTR_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-011",
            "Custom metadata property with name {0} does not exist in custom metadata {1}.",
            "Verify the custom metadata ID you are accessing has the attribute you are looking for defined. The name of the attribute should be the human-readable name."),
    CM_ATTR_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-012",
            "Custom metadata property with ID {0} does not exist in custom metadata {1}.",
            "Verify the custom metadata ID you are accessing has the attribute you are looking for defined. The ID of the attribute should be the Atlan-internal hashed string representation."),
    ENUM_NOT_FOUND(
            404,
            "ATLAN-JAVA-404-013",
            "Enumeration with name {0} does not exist.",
            "Verify the enumeration name provided is a valid enumeration name. This should be the human-readable name of the enumeration."),
    ASSET_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-014",
            "The {0} asset could not be found by name: {1}.",
            "Verify the requested asset type and name exist in your Atlan environment."),
    NO_CATEGORIES(
            404,
            "ATLAN-JAVA-404-015",
            "Unable to find any categories in glossary with GUID {0} and qualifiedName {1}.",
            "Verify the requested glossary contains categories."),
    CONNECTION_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-016",
            "Unable to find a connection with the name {0} of type: {1}.",
            "Verify the requested connection exists in your Atlan environment."),
    GROUP_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-017",
            "Group with name {0} does not exist.",
            "Verify the group name provided is a valid group name."),
    GROUP_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-018",
            "Group with GUID {0} does not exist.",
            "Verify the role GUID provided is a valid group GUID."),
    USER_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-019",
            "User with username {0} does not exist.",
            "Verify the username provided is a valid username."),
    USER_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-020",
            "User with GUID {0} does not exist.",
            "Verify the user GUID provided is a valid user GUID."),
    USER_NOT_FOUND_BY_EMAIL(
            404,
            "ATLAN-JAVA-404-021",
            "User with email {0} does not exist.",
            "Verify the user email provided is a valid user email address."),
    GROUP_NOT_FOUND_BY_ALIAS(
            404,
            "ATLAN-JAVA-404-022",
            "Group with alias {0} does not exist.",
            "Verify the group alias provided is a valid group alias."),
    PERSONA_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-023",
            "Unable to find a persona with the name: {0}.",
            "Verify the requested persona exists in your Atlan environment."),
    PURPOSE_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-024",
            "Unable to find a purpose with the name: {0}.",
            "Verify the requested purpose exists in your Atlan environment."),
    COLLECTION_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-025",
            "Unable to find a query collection with the name: {0}.",
            "Verify the requested query collection exists in your Atlan environment."),
    QUERY_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-026",
            "Unable to find a query with the name: {0}.",
            "Verify the requested query exists in your Atlan environment."),
    API_TOKEN_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-027",
            "API token with name {0} does not exist.",
            "Verify the API token provided is a valid username for that token."),
    ID_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-028",
            "Object with name {0} does not exist.",
            "Verify the object name provided is a valid object name."),
    NAME_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-029",
            "Object with internal ID {0} does not exist.",
            "Verify the internal ID provided is a valid internal ID."),
    ID_NOT_FOUND_BY_QUALIFIED_NAME(
            404,
            "ATLAN-JAVA-404-030",
            "Object with qualifiedName {0} does not exist.",
            "Verify the object qualifiedName provided is a valid object qualifiedName."),
    SOURCE_TAG_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-031",
            "Source-synced tag with ID {0} does not exist.",
            "Verify the source-synced tag ID provided is a valid source-synced tag ID. This should be the qualifiedName of the source-synced tag in Atlan."),
    ROLES_NOT_FOUND(
            404,
            "ATLAN-JAVA-404-032",
            "No roles found in Atlan.",
            "Verify your tenant is fully configured. If you believe it is, please raise this with Atlan support."),
    ID_NOT_FOUND_BY_SID(
            404,
            "ATLAN-JAVA-404-033",
            "Object with secondary name {0} does not exist.",
            "Verify the object name provided is a valid secondary object name."),

    CONFLICT_PASSTHROUGH(
            409,
            "ATLAN-JAVA-409-000",
            "Server responded with a conflict -- {0}: {1} -- caused by: {2}",
            "Check the details of the server's message to correct your request."),
    RESERVED_SERVICE_TYPE(
            409,
            "ATLAN-JAVA-409-001",
            "Provided service type is reserved for internal Atlan use only: {0}",
            "You cannot create, update or remove any type definitions using this service type, it is reserved for Atlan use only."),

    RATE_LIMIT_PASSTHROUGH(
            429,
            "ATLAN-JAVA-429-000",
            "Server responded with a rate limit violation -- {0}: {1} -- caused by: {2}",
            "Check the details of the server's message to correct your request."),

    ERROR_PASSTHROUGH(
            500,
            "ATLAN-JAVA-500-000",
            "Server responded with an error -- {0}: {1} -- caused by: {2}",
            "Check the details of the server's message to correct your request."),

    DUPLICATE_CUSTOM_ATTRIBUTES(
            500,
            "ATLAN-JAVA-500-001",
            "Multiple custom attributes with exactly the same name ({0}) were found for: {1}.",
            ErrorCode.RAISE_GITHUB_ISSUE),
    UNABLE_TO_DESERIALIZE(500, "ATLAN-JAVA-500-002", "Unable to deserialize value: {0}", ErrorCode.RAISE_GITHUB_ISSUE),
    UNABLE_TO_PARSE_ORIGINAL_QUERY(
            500,
            "ATLAN-JAVA-500-003",
            "Unable to parse original query from the response.",
            ErrorCode.RAISE_GITHUB_ISSUE),
    FOUND_UNEXPECTED_ASSET_TYPE(
            500,
            "ATLAN-JAVA-500-004",
            "Found an asset type that does not match what was requested: {0}.",
            ErrorCode.RAISE_GITHUB_ISSUE),
    RETRIES_INTERRUPTED(
            500,
            "ATLAN-JAVA-500-005",
            "Loop for retrying a failed action was interrupted.",
            "Allow the retry loop to complete, or ignore this error if it was your intention to interrupt the retries."),
    RETRY_OVERRUN(
            500,
            "ATLAN-JAVA-500-006",
            "Loop for retrying a failed action hit the maximum number of retries.",
            "Increase the maximum number of retries through client.setMaxNetworkRetries(), or ignore this error if it was your intention to fail after a maximum number of retries was reached."),
    ASSET_MODIFICATION_ERROR(
            500,
            "ATLAN-JAVA-500-007",
            "Unable to modify asset to change {0}.",
            "Unable to modify this base property of an asset, which should not happen. Please raise an issue on the public atlan-java GitHub repository."),
    DATA_PRODUCT_QUERY_ERROR(
            500,
            "ATLAN-JAVA-500-008",
            "Unable to query assets for data product.",
            "Unable to run the query DSL for the data product, which should not happen. Please raise an issue on the public atlan-java GitHub repository.");

    private static final String RAISE_GITHUB_ISSUE =
            "Please raise an issue on the Java SDK GitHub repository providing context in which this error occurred.";

    private final ExceptionMessageDefinition messageDefinition;

    ErrorCode(int httpErrorCode, String errorId, String errorMessage, String userAction) {
        this.messageDefinition = ExceptionMessageDefinition.builder()
                .httpErrorCode(httpErrorCode)
                .errorId(errorId)
                .errorMessage(errorMessage)
                .userAction(userAction)
                .build();
    }

    @Override
    public ExceptionMessageDefinition getMessageDefinition() {
        return messageDefinition;
    }
}
