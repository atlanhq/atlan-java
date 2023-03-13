/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.PurposeMetadataPolicyAction;
import java.util.Collection;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PurposeMetadataPolicy extends AbstractPurposePolicy {
    private static final long serialVersionUID = 2L;

    /** All the actions included in the policy. */
    @Singular
    SortedSet<PurposeMetadataPolicyAction> actions;

    /** Fixed value. */
    @Builder.Default
    String type = "metadata";

    /**
     * Builds the minimal object necessary to create a metadata policy for a purpose.
     *
     * @param name short description of the policy
     * @param users collection of usernames whose access is controlled by the policy
     * @param groups collection of group names whose access is controlled by the policy
     * @param allUsers whether to apply this policy to all users (overriding previous parameters) (true) or only some users (false)
     * @param actions the collection of actions the policy allows or denies
     * @param allow whether to allow the actions provided (true) or explicitly deny them (false)
     * @return the minimal request necessary to create the metadata policy for a purpose, as a builder
     * @throws InvalidRequestException if at least one user or group is not specified, and allUsers is false
     */
    public static PurposeMetadataPolicyBuilder<?, ?> creator(
            String name,
            Collection<String> users,
            Collection<String> groups,
            boolean allUsers,
            Collection<PurposeMetadataPolicyAction> actions,
            boolean allow)
            throws InvalidRequestException {
        boolean userSpecified = false;
        PurposeMetadataPolicyBuilder<?, ?> builder = PurposeMetadataPolicy.builder()
                .name(name)
                .description("")
                .actions(actions)
                .allow(allow);
        if (allUsers) {
            userSpecified = true;
            builder = builder.allUsers(true);
        } else {
            if (users != null && !users.isEmpty()) {
                userSpecified = true;
                builder = builder.users(users);
            }
            if (groups != null && !groups.isEmpty()) {
                userSpecified = true;
                builder = builder.groups(groups);
            }
        }
        if (userSpecified) {
            return builder;
        } else {
            throw new InvalidRequestException(ErrorCode.NO_USERS_FOR_POLICY);
        }
    }
}
