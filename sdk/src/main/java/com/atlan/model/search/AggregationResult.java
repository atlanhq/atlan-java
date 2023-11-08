/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.atlan.serde.AggregationResultDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;

/** Base class for all aggregation results in a search. */
@JsonDeserialize(using = AggregationResultDeserializer.class)
@ToString(callSuper = true)
public abstract class AggregationResult extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * Retrieve the numeric value from the provided aggregation result.
     *
     * @return the numeric result for the aggregation
     * @throws InvalidRequestException if the provided aggregation result is not a metric
     */
    public double getMetric() throws InvalidRequestException {
        if (this instanceof AggregationMetricResult) {
            return ((AggregationMetricResult) this).getValue();
        } else {
            throw new InvalidRequestException(ErrorCode.NOT_AGGREGATION_METRIC);
        }
    }
}
