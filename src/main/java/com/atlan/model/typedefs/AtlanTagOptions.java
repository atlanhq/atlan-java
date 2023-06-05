/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanTagColor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Options that can be defined for an Atlan tag.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class AtlanTagOptions extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * Instantiate a new set of Atlan tag options from the provided parameters.
     * @param color for the Atlan tag
     * @return the Atlan tag options
     */
    public static AtlanTagOptions of(AtlanTagColor color) {
        return new AtlanTagOptions(color);
    }

    private AtlanTagOptions(AtlanTagColor color) {
        this.color = color;
    }

    /** Color to use for the Atlan tag. */
    AtlanTagColor color;
}
