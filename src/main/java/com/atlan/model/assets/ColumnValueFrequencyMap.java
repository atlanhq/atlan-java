/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ColumnValueFrequencyMap extends AtlanObject {
    /** Value for the column. */
    String columnValue;

    /** How many records contain that value. */
    Long columnValueFrequency;
}
