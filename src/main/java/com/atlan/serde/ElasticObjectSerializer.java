/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.JsonpSerializable;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom serialization of Elastic objects into the JSON expected by Atlan's search interfaces.
 * We reuse the Elastic client to allow full richness and flexibility in defining the queries themselves, but
 * this custom serialization is then necessary as the underlying objects of the Elastic client itself do not exactly
 * match the expected JSON serialization format. (Thankfully all we need to do is call Elastic's own underlying
 * serialization methods to get there, though!)
 * @param <T> the type of Elastic object to serialize
 */
@Slf4j
public class ElasticObjectSerializer<T extends JsonpSerializable> extends StdSerializer<T> {
    private static final long serialVersionUID = 2L;

    // Create our own local mapper to avoid a race condition on initialization
    private static final JsonpMapper localMapper = new JacksonJsonpMapper();

    public ElasticObjectSerializer() {
        this(null);
    }

    public ElasticObjectSerializer(Class<T> t) {
        super(t);
    }

    @Override
    public void serializeWithType(T src, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        serialize(src, gen, serializers);
    }

    @Override
    public void serialize(T src, JsonGenerator gen, SerializerProvider sp) throws IOException, JsonProcessingException {
        if (src != null) {
            StringWriter sw = new StringWriter();
            jakarta.json.stream.JsonGenerator generator =
                    localMapper.jsonProvider().createGenerator(sw);
            src.serialize(generator, localMapper);
            generator.close();
            gen.writeRawValue(sw.toString());
        }
    }
}
