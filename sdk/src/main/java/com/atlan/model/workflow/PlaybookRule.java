/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class PlaybookRule extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Human-readable name of the rule. */
    String name;

    /** Type of rule. */
    @Builder.Default
    String type = "atlan-indexsearch";

    /** Configuration of the search filter for the rule. */
    PlaybookRuleConfig config;

    /** UI-based form of the search filter for the rule. */
    @JsonIgnore
    Object filterSchema;

    /** Actions to apply to the matching assets. */
    @Singular
    List<PlaybookAction> actions;

    /** UI-based form of the actions to apply on the matching assets. */
    @JsonIgnore
    Object actionsUISchema;
}
