package com.atlan.model.serde;

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

@Slf4j
public class ElasticObjectJSerializer<T extends JsonpSerializable> extends StdSerializer<T> {
    private static final long serialVersionUID = 2L;

    // Create our own local mapper to avoid a race condition on initialization
    private static final JsonpMapper localMapper = new JacksonJsonpMapper();

    public ElasticObjectJSerializer() {
        this(null);
    }

    public ElasticObjectJSerializer(Class<T> t) {
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
