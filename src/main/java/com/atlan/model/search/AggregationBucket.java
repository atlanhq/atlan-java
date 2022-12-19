/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Captures the results of a single bucket within an aggregation.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AggregationBucket extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Key of the field representing this bucket of aggregate results. */
    String key;

    /** Number of results that fit within this bucket of the aggregation. */
    @JsonProperty("doc_count")
    Long docCount;
}
