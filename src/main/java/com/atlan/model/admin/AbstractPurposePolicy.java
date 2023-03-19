/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.SortedSet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractPurposePolicy extends AbstractPolicy {

    /** Whether this policy applies to all users (true) or not (false). */
    Boolean allUsers;

    /**
     * All groups' names to whom the policy will apply.
     * Note that these are the internal names of the groups, typically in all lowercase and without spaces.
     * @see AtlanGroup#name
     */
    @Singular
    @JsonInclude(JsonInclude.Include.ALWAYS)
    SortedSet<String> groups;

    /**
     * All users' names to whom the policy will apply.
     * @see AtlanUser#username
     */
    @Singular
    @JsonInclude(JsonInclude.Include.ALWAYS)
    SortedSet<String> users;
}
