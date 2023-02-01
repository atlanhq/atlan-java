/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
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
            "You must first use Atlan.setBaseUrl() before making an API call."),

    INVALID_REQUEST_PASSTHROUGH(
            400,
            "ATLAN-JAVA-400-000",
            "Server responded with {0}: {1}",
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
    MISSING_CLASSIFICATION_NAME(
            400,
            "ATLAN-JAVA-400-006",
            "No name was provided when attempting to retrieve a classification.",
            "You must provide a name of a classification when attempting to retrieve one."),
    MISSING_CLASSIFICATION_ID(
            400,
            "ATLAN-JAVA-400-007",
            "No ID was provided when attempting to retrieve a classification.",
            "You must provide an ID of a classification when attempting to retrieve one."),
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
            "Atlan currently only allows you to create type definitions for new custom metadata, enumerations and classifications."),
    UNABLE_TO_UPDATE_TYPEDEF_CATEGORY(
            400,
            "ATLAN-JAVA-400-016",
            "Unable to update type definitions of category: {0}.",
            "Atlan currently only allows you to update type definitions for custom metadata, enumerations and classifications."),
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
    NO_CLASSIFICATION_FOR_PURPOSE(
            400,
            "ATLAN-JAVA-400-025",
            "No classifications provided for the purpose.",
            "You must specify at least one classification to create a new purpose."),
    NO_USERS_FOR_POLICY(
            400,
            "ATLAN-JAVA-400-026",
            "No user or group specified for the policy.",
            "You must specify at least one user, group or allUsers to whom the policy in a purpose will be applied."),
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

    AUTHENTICATION_PASSTHROUGH(
            401,
            "ATLAN-JAVA-401-000",
            "Server responded with {0}: {1}",
            "Check the details of the server's message to correct your request."),
    NO_API_TOKEN(
            401,
            "ATLAN-JAVA-401-001",
            "No API token provided.",
            "Set your API token using `Atlan.setApiToken(\"<API-TOKEN>\");`. You can generate API tokens from the Atlan Admin Center. See "
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

    PERMISSION_PASSTHROUGH(
            403,
            "ATLAN-JAVA-403-000",
            "Server responded with {0}: {1}",
            "Check the details of the server's message to correct your request."),

    NOT_FOUND_PASSTHROUGH(
            404,
            "ATLAN-JAVA-404-000",
            "Server responded with {0}: {1}",
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
    CLASSIFICATION_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-006",
            "Classification with name {0} does not exist.",
            "Verify the classification name provided is a valid classification name. This should be the human-readable name of the classification."),
    CLASSIFICATION_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-007",
            "Classification with ID {0} does not exist.",
            "Verify the classification ID provided is a valid classification ID. This should be the Atlan-internal hashed string representation."),
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

    CONFLICT_PASSTHROUGH(
            409,
            "ATLAN-JAVA-409-000",
            "Server responded with {0}: {1}",
            "Check the details of the server's message to correct your request."),

    RATE_LIMIT_PASSTHROUGH(
            429,
            "ATLAN-JAVA-429-000",
            "Server responded with {0}: {1}",
            "Check the details of the server's message to correct your request."),

    ERROR_PASSTHROUGH(
            500,
            "ATLAN-JAVA-500-000",
            "Server responded with {0}: {1}",
            "Check the details of the server's message to correct your request."),

    DUPLICATE_CUSTOM_ATTRIBUTES(
            500,
            "ATLAN-JAVA-500-001",
            "Multiple custom attributes with exactly the same name ({0}) were found for: {1}.",
            ErrorCode.RAISE_GITHUB_ISSUE),
    UNABLE_TO_DESERIALIZE(500, "ATLAN-JAVA-500-002", "Unable to deserialize value: [0]", ErrorCode.RAISE_GITHUB_ISSUE),
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
    ;

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
