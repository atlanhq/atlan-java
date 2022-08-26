package com.atlan.model.serde;

import com.atlan.cache.ClassificationCacheJ;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.ClassificationJ;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class ClassificationJSerializer extends StdSerializer<ClassificationJ> {
    private static final long serialVersionUID = 2L;

    private final JsonSerializer<ClassificationJ> defaultSerializer;

    public ClassificationJSerializer() {
        this(null);
    }

    public ClassificationJSerializer(JsonSerializer<ClassificationJ> defaultSerializer) {
        super(ClassificationJ.class);
        this.defaultSerializer = defaultSerializer;
    }

    @Override
    public void serializeWithType(
            ClassificationJ value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        serialize(value, gen, serializers);
    }

    @Override
    public void serialize(ClassificationJ cls, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
        String clsName = cls.getTypeName();
        String clsId;
        try {
            clsId = ClassificationCacheJ.getIdForName(clsName);
        } catch (AtlanException e) {
            throw new IOException("Unable to find classification with name: " + clsName, e);
        }
        cls.setTypeName(clsId);
        defaultSerializer.serialize(cls, gen, sp);
    }
}
