/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

public enum ErrorCode implements ExceptionMessageSet {
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
            "Role with name '{0}' does not exist.",
            "Verify the role name provided is a valid role name."),
    ROLE_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-005",
            "Role with GUID '{0}' does not exist.",
            "Verify the role GUID provided is a valid role GUID."),
    CLASSIFICATION_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-006",
            "Classification with name '{0}' does not exist.",
            "Verify the classification name provided is a valid classification name. This should be the human-readable name of the classification."),
    CLASSIFICATION_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-007",
            "Classification with ID '{0}' does not exist.",
            "Verify the classification ID provided is a valid classification ID. This should be the Atlan-internal hashed string representation."),
    CM_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-008",
            "Custom metadata with name '{0}' does not exist.",
            "Verify the custom metadata name provided is a valid custom metadata name. This should be the human-readable name of the custom metadata."),
    CM_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-009",
            "Custom metadata with ID '{0}' does not exist.",
            "Verify the custom metadata ID provided is a valid custom metadata ID. This should be the Atlan-internal hashed string representation."),
    CM_NO_ATTRIBUTES(
            404,
            "ATLAN-JAVA-404-010",
            "Custom metadata with ID '{0}' does not have any attributes.",
            "Verify the custom metadata ID you are accessing has attributes defined before attempting to retrieve one of them."),
    CM_ATTR_NOT_FOUND_BY_NAME(
            404,
            "ATLAN-JAVA-404-011",
            "Custom metadata property with name '{0}' does not exist in custom metadata '{1}'.",
            "Verify the custom metadata ID you are accessing has the attribute you are looking for defined. The name of the attribute should be the human-readable name."),
    CM_ATTR_NOT_FOUND_BY_ID(
            404,
            "ATLAN-JAVA-404-012",
            "Custom metadata property with ID '{0}' does not exist in custom metadata '{1}'.",
            "Verify the custom metadata ID you are accessing has the attribute you are looking for defined. The ID of the attribute should be the Atlan-internal hashed string representation."),
    ENUM_NOT_FOUND(
            404,
            "ATLAN-JAVA-404-013",
            "Enumeration with name '{0}' does not exist.",
            "Verify the enumeration name provided is a valid enumeration name. This should be the human-readable name of the enumeration."),

    DUPLICATE_CUSTOM_ATTRIBUTES(
            500,
            "ATLAN-JAVA-500-001",
            "Multiple custom attributes with exactly the same name ({0}) were found for: {1}.",
            "Please raise an issue on the Java SDK GitHub repository providing context in which this error occurred."),
    UNABLE_TO_DESERIALIZE(
            500,
            "ATLAN-JAVA-500-002",
            "Unable to deserialize value: [0]",
            "Please raise an issue on the Java SDK GitHub repository providing context in which this error occurred."),
    ;

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
