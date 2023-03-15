/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.assets.Asset;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanRequestStatus;
import com.atlan.model.enums.AtlanRequestType;
import com.atlan.serde.AtlanRequestDeserializer;
import com.atlan.serde.AtlanRequestSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@JsonSerialize(using = AtlanRequestSerializer.class)
@JsonDeserialize(using = AtlanRequestDeserializer.class)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AtlanRequest extends AtlanObject {

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
    String tenantId;

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
    Object destinationValueArray;

    /** Unused. */
    Object destinationValueObject;

    /** Type of asset the change was requested against. */
    String entityType;

    /** Type of change the request is for. */
    AtlanRequestType requestType;

    /** Unused. */
    Object confidenceScore;

    /** Unused. */
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
    Object requestsBatch;

    /** Type of approval required — currently only `single` is supported. */
    @Builder.Default
    String approvalType = "single";

    /** Unused. */
    Object assignedApprovers;

    /** Details about the requested classification, if any. */
    AtlanRequestPayload payload;

    /** Unused. */
    Object accessStartDate;

    /** Unused. */
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
    Object sourceEntity;

    /** Limited details about the asset this request was made against. */
    Asset destinationEntity;
}
