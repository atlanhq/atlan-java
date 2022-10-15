/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.enums.DataPolicyAction;
import com.atlan.model.enums.DataPolicyType;
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
public class PersonaDataPolicy extends AbstractPersonaPolicy {
    private static final long serialVersionUID = 2L;

    /** Unused. */
    final String connectionName = "";

    /** All the actions included in the policy. */
    @Singular
    SortedSet<DataPolicyAction> actions;

    /** Fixed value (cannot apply masking on persona data policies). */
    final DataPolicyType type = null;
}
