/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.AggregationHitsResult;
import com.atlan.model.search.AggregationMetricResult;
import com.atlan.model.search.AggregationResult;
import com.fasterxml.jackson.core.JsonParser;
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
        JsonNode buckets = root.get("buckets"); // exists on bucket results
        JsonNode hits = root.get("hits"); // exists on top-hits results
        if ((root.has("value") && (value == null || value.isNull()))
                || (root.has("buckets") && (buckets == null || buckets.isNull()))
                || (root.has("hits") && (hits == null || hits.isNull()))) {
            // If the JSON has explicit null values, return those as explicit nulls rather than errors
            return null;
        }
        try (JsonParser next = root.traverse(parser.getCodec())) {
            AggregationResult result;
            next.nextToken();
            if (value != null && value.isNumber()) {
                // Delegate to metrics deserialization
                result = context.readValue(next, AggregationMetricResult.class);
            } else if (buckets != null) {
                // Delegate to bucket deserialization
                result = context.readValue(next, AggregationBucketResult.class);
            } else if (hits != null) {
                // Delegate to hits deserialization
                result = context.readValue(next, AggregationHitsResult.class);
            } else {
                throw new IOException("Aggregation currently not handled: " + root);
            }
            result.setRawJsonObject(root);
            return result;
        }
    }
}
