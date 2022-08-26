package com.atlan.model.serde;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class RemovableJSerializer extends StdSerializer<RemovableJ> {
    private static final long serialVersionUID = 2L;

    public RemovableJSerializer() {
        this(null);
    }

    public RemovableJSerializer(Class<RemovableJ> t) {
        super(t);
    }

    @Override
    public void serialize(RemovableJ removable, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
        if (removable.isJsonNull()) {
            if (removable.getType() == RemovableJ.TYPE.PRIMITIVE) {
                gen.writeNull();
            } else {
                gen.writeStartArray();
                gen.writeEndArray();
            }
        }
    }
}
