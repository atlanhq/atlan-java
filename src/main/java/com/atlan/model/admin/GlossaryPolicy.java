/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.enums.GlossaryPolicyAction;
import java.util.SortedSet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryPolicy extends AbstractPolicy {
    private static final long serialVersionUID = 2L;

    /** All glossaries' qualifiedNames to include in the policy. */
    @Singular
    SortedSet<String> glossaryQualifiedNames;

    /** All the actions included in the policy. */
    @Singular
    SortedSet<GlossaryPolicyAction> actions;
}
