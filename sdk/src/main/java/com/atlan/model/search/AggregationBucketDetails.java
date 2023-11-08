/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.fields.ISearchable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Captures the results of a single bucket within an aggregation.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class AggregationBucketDetails extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * Key of the field representing this bucket of aggregate results, as a string.
     * For example, when the key is a date (numeric) this will be something like "2015-01-01".
     */
    @JsonProperty("key_as_string")
    String keyAsString;

    /**
     * Key of the field representing this bucket of aggregate results.
     * This could be a string, a number (in the case of dates), or an array
     * (in the case of multi-term aggregations).
     */
    Object key;

    /** Number of results that fit within this bucket of the aggregation. */
    @JsonProperty("doc_count")
    Long docCount;

    /** TBC */
    @JsonProperty("max_matching_length")
    Long maxMatchingLength;

    /** End of a range (date, geo, IP, etc), as a number or string. */
    Object to;

    /** End of a range, as a string. */
    @JsonProperty("to_as_string")
    String toAsString;

    /** Start of a range (date, geo, IP, etc), as a number or string. */
    Object from;

    /** Start of a range, as a string. */
    @JsonProperty("from_as_string")
    String fromAsString;

    /** Map of results for the requested aggregations. */
    @JsonProperty(ISearchable.EMBEDDED_SOURCE_VALUE)
    @Getter(AccessLevel.PRIVATE)
    AggregationHitsResult _sourceValue;

    /**
     * Return the source value of the specified field for this bucket.
     *
     * @param field field in Atlan for which to retrieve the value
     * @return the value of the field in Atlan that is represented within this bucket, if any
     */
    @JsonIgnore
    public Object getSourceValue(AtlanField field) {
        if (_sourceValue != null
                && _sourceValue.getHits() != null
                && _sourceValue.getHits().getHits() != null
                && !_sourceValue.getHits().getHits().isEmpty()) {
            AggregationHitsResult.Details details =
                    _sourceValue.getHits().getHits().get(0);
            if (details != null && details.getSource() != null) {
                return details.getSource().getOrDefault(field.getAtlanFieldName(), null);
            }
        }
        return null;
    }
}
