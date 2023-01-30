/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.assets.PopularityInsights;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

/**
 * Custom serialization of {@link PopularityInsights} objects.
 * In particular, this translates from the human-readable name into the Atlan-internal hashed-string representation for
 * a classification.
 */
public class PopularityInsightsSerializer extends StdSerializer<PopularityInsights> {
    private static final long serialVersionUID = 2L;

    public PopularityInsightsSerializer() {
        this(null);
    }

    public PopularityInsightsSerializer(Class<PopularityInsights> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serializeWithType(
            PopularityInsights value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        serialize(value, gen, serializers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(PopularityInsights pi, JsonGenerator gen, SerializerProvider sp) throws IOException {
        gen.writeStartObject();
        JacksonUtils.serializeString(gen, "typeName", "PopularityInsights");
        gen.writeFieldName("attributes");
        gen.writeStartObject();
        JacksonUtils.serializeLong(gen, "recordTotalUserCount", pi.getRecordTotalUserCount());
        JacksonUtils.serializeString(gen, "recordUser", pi.getRecordUser());
        JacksonUtils.serializeLong(gen, "recordQueryCount", pi.getRecordQueryCount());
        JacksonUtils.serializeString(gen, "recordWarehouse", pi.getRecordWarehouse());
        JacksonUtils.serializeDouble(gen, "recordComputeCost", pi.getRecordComputeCost());
        JacksonUtils.serializeLong(gen, "recordLastTimestamp", pi.getRecordLastTimestamp());
        JacksonUtils.serializeString(gen, "recordQuery", pi.getRecordQuery());
        JacksonUtils.serializeLong(gen, "recordQueryDuration", pi.getRecordQueryDuration());
        JacksonUtils.serializeDouble(gen, "recordMaxComputeCost", pi.getRecordMaxComputeCost());
        SourceCostUnitType unit = pi.getRecordComputeCostUnit();
        if (unit != null) {
            JacksonUtils.serializeString(gen, "recordComputeCostUnit", unit.getValue());
        }
        gen.writeEndObject();
        gen.writeEndObject();
    }
}
