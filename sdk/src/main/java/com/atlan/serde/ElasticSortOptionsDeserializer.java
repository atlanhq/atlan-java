/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import co.elastic.clients.elasticsearch._types.SortOptions;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.io.StringReader;

public class ElasticSortOptionsDeserializer extends StdDeserializer<SortOptions> {

    private static final long serialVersionUID = 2L;

    public ElasticSortOptionsDeserializer() {
        this(null);
    }

    public ElasticSortOptionsDeserializer(Class<?> t) {
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
    public SortOptions deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        StringReader sr = new StringReader(parser.readValueAsTree().toString());
        jakarta.json.stream.JsonParser jsonParser =
                Serde.jsonpMapper.jsonProvider().createParser(sr);
        return SortOptions._DESERIALIZER.deserialize(jsonParser, Serde.jsonpMapper);
    }
}
