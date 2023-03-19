/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Histogram extends AtlanObject {
    /** TBC */
    List<Double> boundaries;

    /** TBC. */
    List<Double> frequencies;
}
