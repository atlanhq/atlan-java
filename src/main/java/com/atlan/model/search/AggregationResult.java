/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.core.AtlanObject;
import com.atlan.serde.AggregationResultDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;

/** Base class for all aggregation results in a search. */
@JsonDeserialize(using = AggregationResultDeserializer.class)
@ToString(callSuper = true)
public abstract class AggregationResult extends AtlanObject {}
