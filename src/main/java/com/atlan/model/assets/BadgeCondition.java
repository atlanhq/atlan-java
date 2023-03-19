/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a badge condition.
 */
@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BadgeCondition extends AtlanObject {
    /** TBC. */
    String badgeConditionOperator;

    /** TBC. */
    String badgeConditionValue;

    /** TBC. */
    String badgeConditionColorhex;
}
