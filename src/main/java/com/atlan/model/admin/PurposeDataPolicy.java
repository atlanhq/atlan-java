/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.enums.DataPolicyAction;
import com.atlan.model.enums.DataPolicyType;
import com.atlan.model.enums.MaskingType;
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
public class PurposeDataPolicy extends AbstractPolicy {
    private static final long serialVersionUID = 2L;

    /** All groups' names to whom the policy will apply. */
    SortedSet<String> groups;

    /** All users' names to whom the policy will apply. */
    SortedSet<String> users;

    /** All the actions included in the policy. */
    SortedSet<DataPolicyAction> actions;

    /** Type of data policy. */
    DataPolicyType type;

    /** Type of masking to apply, if {@link #type} is {@code masking}. */
    MaskingType mask;
}
