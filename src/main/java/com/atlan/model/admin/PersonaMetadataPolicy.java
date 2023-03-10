/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.enums.PersonaMetadataPolicyAction;
import java.util.Collection;
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
public class PersonaMetadataPolicy extends AbstractPersonaPolicy {
    private static final long serialVersionUID = 2L;

    /** All the actions included in the policy. */
    @Singular
    SortedSet<PersonaMetadataPolicyAction> actions;

    /**
     * Builds the minimal object necessary to create a metadata policy for a persona.
     *
     * @param name short description of the policy
     * @param connectionId unique identifier (GUID) of the connection whose assets will be controlled by this policy
     * @param assetPrefixes the qualifiedName prefixes for all assets to include in this policy
     * @param actions the collection of actions the policy allows or denies
     * @param allow whether to allow the actions provided (true) or explicitly deny them (false)
     * @return the minimal request necessary to create the metadata policy for a persona, as a builder
     */
    public static PersonaMetadataPolicyBuilder<?, ?> creator(
            String name,
            String connectionId,
            Collection<String> assetPrefixes,
            Collection<PersonaMetadataPolicyAction> actions,
            boolean allow) {
        return PersonaMetadataPolicy.builder()
                .name(name)
                .description("")
                .connectionId(connectionId)
                .assets(assetPrefixes)
                .actions(actions)
                .allow(allow);
    }
}
