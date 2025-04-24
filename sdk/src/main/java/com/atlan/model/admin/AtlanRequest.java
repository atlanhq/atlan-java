/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanRequestStatus;
import com.atlan.serde.AtlanRequestDeserializer;
import com.atlan.serde.AtlanRequestSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(using = AtlanRequestSerializer.class)
@JsonDeserialize(using = AtlanRequestDeserializer.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "requestType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AttributeRequest.class, name = AttributeRequest.REQUEST_TYPE),
    @JsonSubTypes.Type(value = TermLinkRequest.class, name = TermLinkRequest.REQUEST_TYPE),
    @JsonSubTypes.Type(value = AtlanTagRequest.class, name = AtlanTagRequest.REQUEST_TYPE),
    @JsonSubTypes.Type(value = CustomMetadataRequest.class, name = CustomMetadataRequest.REQUEST_TYPE),
})
@ToString(callSuper = true)
@SuppressWarnings("serial")
public abstract class AtlanRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Unique identifier for the request (GUID). */
    String id;

    /** Unique version name for the request. */
    String version;

    /** If true, the request is still open. */
    Boolean isActive;

    /** Date and time at which the request was created. */
    final Long createdAt;

    /** Date and time at which the request was last updated. */
    final Long updatedAt;

    /** User who created the request. */
    String createdBy;

    /** Name of the tenant for the request (usually `default`). */
    @Builder.Default
    String tenantId = "default";

    /** Should be `static` for an ATTRIBUTE or CUSTOM_METADATA requestType, and `atlas` for other request types. */
    String sourceType;

    /** Unique identifier (GUID) of the asset being related through this request, for example the term's GUID for a TERM_LINK. */
    String sourceGuid;

    /** Unique name of the asset being related through this request, for example the term's qualifiedName for a TERM_LINK. */
    String sourceQualifiedName;

    /** TBC */
    String sourceAttribute;

    /** Unique identifier (GUID) of the asset this request was made against. */
    String destinationGuid;

    /** Unique name of the asset this request was made against. */
    String destinationQualifiedName;

    /** Attribute the request was made against. */
    String destinationAttribute;

    /** Requested value for the attribute. */
    String destinationValue;

    /** Type of the destination attribute value, for example `array`. */
    String destinationValueType;

    /** Unused. */
    @JsonIgnore
    Object destinationValueArray;

    /** Unused. */
    @JsonIgnore
    Object destinationValueObject;

    /** Type of asset the change was requested against. */
    String entityType;

    /** Type of change the request is for. */
    String requestType;

    /** Unused. */
    @JsonIgnore
    Object confidenceScore;

    /** Unused. */
    @JsonIgnore
    Object botRunId;

    /** User who approved the request. */
    String approvedBy;

    /** User who rejected the request. */
    String rejectedBy;

    /** Status of the request. */
    AtlanRequestStatus status;

    /** Comment recorded with the approval or rejection of the request. */
    String message;

    /** Unused. */
    @JsonIgnore
    Object requestsBatch;

    /** Type of approval required — currently only `single` is supported. */
    @Builder.Default
    String approvalType = "single";

    /** Unused. */
    @JsonIgnore
    Object assignedApprovers;

    /** Unused. */
    @JsonIgnore
    Object accessStartDate;

    /** Unused. */
    @JsonIgnore
    Object accessEndDate;

    /** TBC */
    Long hash;

    /** TBC */
    Boolean isDuplicate;

    /** Semantic to use when applying the destination value, for example `append`. */
    String destinationValueAction;

    /** Names of users (in Keycloak) that can approve this request. */
    @Singular
    SortedSet<String> requestApproverUsers;

    /** TBC */
    @Singular
    SortedSet<String> requestApproverGroups;

    /** Names of roles (in Keycloak) that can approve this request. */
    @Singular
    SortedSet<String> requestApproverRoles;

    /** Names of users (in Keycloak) that can deny this request. */
    @Singular
    SortedSet<String> requestDenyUsers;

    /** TBC */
    @Singular
    SortedSet<String> requestDenyGroups;

    /** Unique identifiers (GUIDs) of the roles that can deny this request. */
    @Singular
    SortedSet<String> requestDenyRoles;

    /** TBC */
    @JsonIgnore
    Object sourceEntity;

    /** Limited details about the asset this request was made against. */
    Asset destinationEntity;

    /**
     * Create the request in Atlan that is represented by this object.
     *
     * @param client connectivity to the Atlan tenant in which to create the request
     * @throws AtlanException on any API interaction issues
     */
    public void create(AtlanClient client) throws AtlanException {
        client.requests.create(this);
    }

    /**
     * Retrieve the list of requests defined in Atlan as you would via the Admin UI.
     *
     * @param client connectivity to the Atlan tenant from which to list requests
     * @return a list of all the requests in Atlan
     * @throws AtlanException on any API communication issue
     */
    public static AtlanRequestResponse list(AtlanClient client) throws AtlanException {
        return client.requests.list();
    }

    /**
     * Fetch a single request by its unique identifier (GUID).
     *
     * @param client connectivity to the Atlan tenant from which to fetch the request
     * @param guid unique identifier (GUID) of the request to fetch.
     * @return the single request, or null if none was found
     * @throws AtlanException on any API communication issue
     */
    public static AtlanRequest retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return client.requests.get(guid);
    }

    /**
     * Approve the specified request in Atlan.
     *
     * @param client connectivity to the Atlan tenant in which to approve the request
     * @param guid unique identifier (GUID) of the request to approve
     * @param message (optional) message to include with the approval
     * @return true if the approval succeeded, otherwise false
     * @throws AtlanException on any API interaction issues
     */
    public static boolean approve(AtlanClient client, String guid, String message) throws AtlanException {
        return client.requests.approve(guid, message);
    }

    /**
     * Reject the specified request in Atlan.
     *
     * @param client connectivity to the Atlan tenant in which to reject the request
     * @param guid unique identifier (GUID) of the request to reject
     * @param message (optional) message to include with the rejection
     * @return true if the rejection succeeded, otherwise false
     * @throws AtlanException on any API interaction issues
     */
    public static boolean reject(AtlanClient client, String guid, String message) throws AtlanException {
        return client.requests.reject(guid, message);
    }
}
