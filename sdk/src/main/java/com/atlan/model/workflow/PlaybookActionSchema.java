/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.PlaybookActionOperator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class PlaybookActionSchema extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Attribute on which to take the action. */
    String operand;

    /** Specific action to take on the attribute. */
    PlaybookActionOperator operator;

    /** Value to apply through the action. */
    Object value;
}
