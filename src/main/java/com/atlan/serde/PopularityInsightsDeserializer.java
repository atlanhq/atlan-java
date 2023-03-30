/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.assets.PopularityInsights;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

/**
 * Custom deserialization of {@link PopularityInsights} objects.
 * In particular, this flattens the nested type + attributes structure to simplify usage.
 */
public class PopularityInsightsDeserializer extends StdDeserializer<PopularityInsights> {
    private static final long serialVersionUID = 2L;

    public PopularityInsightsDeserializer() {
        super(PopularityInsights.class);
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
    public PopularityInsights deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    /**
     * Actually do the work of deserializing a popularity insights record.
     *
     * @param root of the parsed JSON tree
     * @return the deserialized popularity insights
     * @throws IOException on any issues parsing the JSON
     */
    PopularityInsights deserialize(JsonNode root) throws IOException {
        JsonNode attributes = root.get("attributes");
        if (attributes == null) {
            // PopularityInsights embedded in events (mutatedDetails) do not have an outer 'attributes' wrapper
            attributes = root;
        }
        return PopularityInsights.builder()
                .recordTotalUserCount(JacksonUtils.deserializeLong(attributes, "recordTotalUserCount"))
                .recordUser(JacksonUtils.deserializeString(attributes, "recordUser"))
                .recordQueryCount(JacksonUtils.deserializeLong(attributes, "recordQueryCount"))
                .recordWarehouse(JacksonUtils.deserializeString(attributes, "recordWarehouse"))
                .recordComputeCost(JacksonUtils.deserializeDouble(attributes, "recordComputeCost"))
                .recordLastTimestamp(JacksonUtils.deserializeLong(attributes, "recordLastTimestamp"))
                .recordQuery(JacksonUtils.deserializeString(attributes, "recordQuery"))
                .recordQueryDuration(JacksonUtils.deserializeLong(attributes, "recordQueryDuration"))
                .recordMaxComputeCost(JacksonUtils.deserializeDouble(attributes, "recordMaxComputeCost"))
                .recordComputeCostUnit(
                        JacksonUtils.deserializeObject(attributes, "recordComputeCostUnit", new TypeReference<>() {}))
                .build();
    }
}
