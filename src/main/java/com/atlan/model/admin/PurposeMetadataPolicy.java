/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.enums.PurposeMetadataPolicyAction;
import java.util.SortedSet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PurposeMetadataPolicy extends AbstractPolicy {
    private static final long serialVersionUID = 2L;

    /** All groups' names to whom the policy will apply. */
    SortedSet<String> groups;

    /** All users' names to whom the policy will apply. */
    SortedSet<String> users;

    /** All the actions included in the policy. */
    SortedSet<PurposeMetadataPolicyAction> actions;

    /** Fixed value. */
    final String type = "meta";
}
