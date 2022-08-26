/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.requests;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.core.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class SingleEntityRequest extends AtlanObject {
    /** The entity to update. */
    Entity entity;
}
