/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import java.util.Comparator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class AbstractPolicy extends AtlanObject implements Comparable<AbstractPolicy> {

    // Sort policies in a set based first on their name (mandatory and required to be unique)
    private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
    private static final Comparator<AbstractPolicy> policyComparator =
            Comparator.comparing(AbstractPolicy::getName, stringComparator);

    /** Unique identifier (GUID) of the policy. */
    final String id;

    /** Name of the policy. */
    String name;

    /** Explanation of the policy. */
    String description;

    /** Whether the actions are granted (true) or explicitly denied (false). */
    Boolean allow;

    /** Time (epoch) at which this policy was created, in milliseconds. */
    final Long createdAt;

    /** Username of the user who created this policy. */
    final String createdBy;

    /** Time (epoch) at which this policy was updated, in milliseconds. */
    final Long updatedAt;

    /** Username of the user who updated this policy. */
    final String updatedBy;

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(AbstractPolicy o) {
        return policyComparator.compare(this, o);
    }
}
