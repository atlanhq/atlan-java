/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.enums.DataPolicyAction;
import com.atlan.model.enums.DataPolicyType;
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
public class PersonaDataPolicy extends AbstractPersonaPolicy {
    private static final long serialVersionUID = 2L;

    /** Unused. */
    final String connectionName = "";

    /** All the actions included in the policy. */
    @Singular
    SortedSet<DataPolicyAction> actions;

    /** Fixed value (cannot apply masking on persona data policies). */
    final DataPolicyType type = null;

    /**
     * Builds the minimal object necessary to create a data policy for a persona.
     *
     * @param name short description of the policy
     * @param connectionId unique identifier (GUID) of the connection whose assets will be controlled by this policy
     * @param assetPrefixes the qualifiedName prefixes for all assets to include in this policy
     * @param actions the collection of actions the policy allows or denies
     * @param allow whether to allow the actions provided (true) or explicitly deny them (false)
     * @return the minimal request necessary to create the data policy for a persona, as a builder
     */
    public static PersonaDataPolicyBuilder<?, ?> creator(
            String name,
            String connectionId,
            Collection<String> assetPrefixes,
            Collection<DataPolicyAction> actions,
            boolean allow) {
        return PersonaDataPolicy.builder()
                .name(name)
                .description("")
                .connectionId(connectionId)
                .assets(assetPrefixes)
                .actions(actions)
                .allow(allow);
    }
}
