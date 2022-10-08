/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Specialized response for updating a user, as the details included are different
 * from those when creating or retrieving a user.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateUserResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Username of the user within Atlan. */
    String username;

    /** Unique identifier (GUID) of the user within Atlan. */
    String id;

    /** Email address of the user. */
    String email;

    /** When true, the email address of the user has been verified. */
    Boolean emailVerified;

    /** When true, the user is enabled. When false, the user has been deactivated. */
    Boolean enabled;

    /** First name of the user. */
    String firstName;

    /** Last name (surname) of the user. */
    String lastName;

    /** Detailed attributes of the user. */
    AtlanUser.UserAttributes attributes;

    /** Time (epoch) at which the user was created, in milliseconds. */
    Long createdTimestamp;

    /** TBC */
    Boolean totp;

    /** TBC */
    @JsonIgnore // TODO
    String disableableCredentialTypes;

    /** TBC */
    @JsonIgnore // TODO
    String requiredActions;

    /** TBC */
    @JsonIgnore // TODO
    String access;
}
