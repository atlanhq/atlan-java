package com.atlan.serde;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class RemovableSerializer extends StdSerializer<Removable> {
    private static final long serialVersionUID = 2L;

    public RemovableSerializer() {
        this(null);
    }

    public RemovableSerializer(Class<Removable> t) {
        super(t);
    }

    @Override
    public void serialize(Removable removable, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
        if (removable.isJsonNull()) {
            if (removable.getType() == Removable.TYPE.PRIMITIVE) {
                gen.writeNull();
            } else {
                gen.writeStartArray();
                gen.writeEndArray();
            }
        }
    }
}
