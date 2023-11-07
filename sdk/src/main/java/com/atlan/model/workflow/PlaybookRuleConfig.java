/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.search.IndexSearchRequest;
import lombok.Builder;
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
public class PlaybookRuleConfig extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Search to run to filter results for the actions. */
    IndexSearchRequest query;

    /** Whether to exclude scrubbed assets (true) or include them (false). */
    @Builder.Default
    Boolean filterScrubbed = true;
}
