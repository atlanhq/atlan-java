/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.enums.GlossaryPolicyAction;
import java.util.Collection;
import java.util.SortedSet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
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

    /**
     * Builds the minimal object necessary to create a glossary policy for a persona.
     *
     * @param name short description of the policy
     * @param glossaryQualifiedNames the qualifiedNames of all glossaries this policy will control
     * @param actions the collection of actions the policy allows or denies
     * @param allow whether to allow the actions provided (true) or explicitly deny them (false)
     * @return the minimal request necessary to create the glossary policy for a persona, as a builder
     */
    public static GlossaryPolicyBuilder<?, ?> creator(
            String name,
            Collection<String> glossaryQualifiedNames,
            Collection<GlossaryPolicyAction> actions,
            boolean allow) {
        return GlossaryPolicy.builder()
                .name(name)
                .glossaryQualifiedNames(glossaryQualifiedNames)
                .actions(actions)
                .allow(allow);
    }
}
