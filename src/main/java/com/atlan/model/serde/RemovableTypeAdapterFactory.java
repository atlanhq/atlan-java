package com.atlan.model.serde;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 * Creates type adapter for interface {@code Removable} able to serialize null values to
 * JSON selectively, as decided at runtime.
 */
public class RemovableTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!Removable.class.isAssignableFrom(type.getRawType())) {
            return null;
        }

        // We will only ever serialize these values, and only ever a 'null' value,
        // so we need only handle a very limited scenario (anything else is unexpected
        // and we'll therefore throw an exception)
        TypeAdapter<Removable> nullableTypeAdapter = new TypeAdapter<>() {
            @Override
            public void write(JsonWriter out, Removable value) throws IOException {
                if (value.isJsonNull()) {
                    if (value.getType() == Removable.TYPE.LIST) {
                        // For a list, output an empty array rather than a null
                        out.beginArray();
                        out.endArray();
                    } else {
                        boolean previousSetting = out.getSerializeNulls();
                        out.setSerializeNulls(true);
                        out.nullValue();
                        out.setSerializeNulls(previousSetting);
                    }
                } else {
                    throw new IOException("Unable to serialize a non-null Removable value.");
                }
            }

            @Override
            public Removable read(JsonReader in) throws IOException {
                JsonToken next = in.peek();
                Removable value;
                switch (next) {
                    case NULL:
                        value = Removable.NULL;
                        break;
                    case STRING: // Could be a plain string, or an enum value
                    case BEGIN_OBJECT:
                    case BEGIN_ARRAY:
                    default:
                        throw new IOException("Unable to deserialize a non-null Removable value.");
                }
                return value;
            }
        };
        return (TypeAdapter<T>) nullableTypeAdapter.nullSafe();
    }
}
