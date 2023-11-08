/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.PlaybookActionType;
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
public class PlaybookAction extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Type of action to take. */
    PlaybookActionType type;

    /** Configuration for the action to take. */
    PlaybookActionConfig config;

    /** Detail of the singular action to apply. */
    PlaybookActionSchema actionsSchema;
}
