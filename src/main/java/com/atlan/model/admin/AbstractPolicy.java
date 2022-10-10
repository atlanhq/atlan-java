/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractPolicy extends AtlanObject {

    /** Name of the policy. */
    String name;

    /** Explanation of the policy. */
    String description;

    /** Whether the actions are granted (true) or explicitly denied (false). */
    Boolean allow;
}
