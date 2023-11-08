/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Class used to define the composition of multiple filters for objects when retrieving lineage.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class FilterList extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Whether the criteria must all match (AND) or any matching is sufficient (OR). */
    String condition;

    /** Basis on which to compare a result for inclusion. */
    @JsonProperty("criterion")
    @Singular("criterion")
    List<EntityFilter> criteria;
}
