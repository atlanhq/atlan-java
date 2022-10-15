/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import java.util.SortedSet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractPurposePolicy extends AbstractPolicy {

    /** All groups' names to whom the policy will apply. */
    SortedSet<String> groups;

    /** All users' names to whom the policy will apply. */
    SortedSet<String> users;
}
