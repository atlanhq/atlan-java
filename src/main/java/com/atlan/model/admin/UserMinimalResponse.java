/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Specialized response for minimalist user details, as returned by some operations.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class UserMinimalResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /**
     * Convert this minimalist response into an AtlanUser object.
     *
     * @return an AtlanUser representation of the same user
     */
    public AtlanUser toAtlanUser() {
        return AtlanUser.builder()
                .username(username)
                .id(id)
                .email(email)
                .emailVerified(emailVerified)
                .enabled(enabled)
                .firstName(firstName)
                .lastName(lastName)
                .attributes(attributes)
                .createdTimestamp(createdTimestamp)
                .build();
    }

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
