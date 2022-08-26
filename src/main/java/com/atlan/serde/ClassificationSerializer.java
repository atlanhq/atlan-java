package com.atlan.serde;

import com.atlan.cache.ClassificationCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.Classification;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class ClassificationSerializer extends StdSerializer<Classification> {
    private static final long serialVersionUID = 2L;

    private final JsonSerializer<Classification> defaultSerializer;

    public ClassificationSerializer() {
        this(null);
    }

    public ClassificationSerializer(JsonSerializer<Classification> defaultSerializer) {
        super(Classification.class);
        this.defaultSerializer = defaultSerializer;
    }

    @Override
    public void serializeWithType(
            Classification value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        serialize(value, gen, serializers);
    }

    @Override
    public void serialize(Classification cls, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
        String clsName = cls.getTypeName();
        String clsId;
        try {
            clsId = ClassificationCache.getIdForName(clsName);
        } catch (AtlanException e) {
            throw new IOException("Unable to find classification with name: " + clsName, e);
        }
        cls.setTypeName(clsId);
        defaultSerializer.serialize(cls, gen, sp);
    }
}
