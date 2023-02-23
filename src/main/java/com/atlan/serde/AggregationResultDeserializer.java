/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.AggregationMetricResult;
import com.atlan.model.search.AggregationResult;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

/**
 * Deserialization of all {@link AggregationResult} objects.
 */
public class AggregationResultDeserializer extends StdDeserializer<AggregationResult> {

    private static final long serialVersionUID = 2L;

    public AggregationResultDeserializer() {
        this(null);
    }

    public AggregationResultDeserializer(Class<?> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object deserializeWithType(
            JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(parser, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AggregationResult deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.getCodec().readTree(parser);
        JsonNode value = root.get("value"); // only exists on metric results
        JsonNode buckets = root.get("buckets"); // exists on entities and custom metadata
        if ((root.has("value") && value == null) || (root.has("buckets") && buckets == null)) {
            // If the JSON has explicit null values, return those as explicit nulls rather than errors
            return null;
        } else if (value != null && value.isNumber()) {
            // Delegate to metrics deserialization
            return AggregationMetricResult.builder().value(value.asDouble()).build();
        } else if (buckets != null) {
            // Delegate to bucket deserialization
            return AggregationBucketResult.builder()
                    .docCountErrorUpperBound(JacksonUtils.deserializeLong(root, "doc_count_error_upper_bound"))
                    .sumOtherDocCount(JacksonUtils.deserializeLong(root, "sum_other_doc_count"))
                    .buckets(JacksonUtils.deserializeObject(root, "buckets", new TypeReference<>() {}))
                    .build();
        } else {
            throw new IOException("Aggregation currently not handled: " + root);
        }
    }
}
