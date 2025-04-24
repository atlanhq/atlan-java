/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import java.util.SortedSet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public abstract class AbstractPersonaPolicy extends AbstractPolicy {
    private static final long serialVersionUID = 2L;

    /** Unique identifier (GUID) of the connection to which this policy applies. */
    String connectionId;

    /**
     * All assets' qualifiedNames to include in the policy.
     * These act as prefixes, so any assets within these will also be included in the
     * policy. (For example, if you give the qualifiedName of a schema, all tables and
     * columns in that schema are also included in the policy.)
     */
    @Singular
    SortedSet<String> assets;
}
