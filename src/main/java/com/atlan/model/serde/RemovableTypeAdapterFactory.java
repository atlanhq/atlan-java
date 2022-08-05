package com.atlan.model.serde;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

        Type parameterType = null;
        Type parameterizedType = type.getType();
        if (parameterizedType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) parameterizedType).getActualTypeArguments();
            parameterType = typeArguments[0];
        }

        Class<?> parameterClass = (Class<?>) parameterType;
        final TypeAdapter<Object> delegateAdapter =
                (TypeAdapter<Object>) gson.getDelegateAdapter(this, TypeToken.get(parameterClass));

        TypeAdapter<Removable<?>> nullableTypeAdapter = new TypeAdapter<>() {
            @Override
            public void write(JsonWriter out, Removable<?> value) throws IOException {
                if (value.isJsonNull()) {
                    boolean previousSetting = out.getSerializeNulls();
                    out.setSerializeNulls(true);
                    out.nullValue();
                    out.setSerializeNulls(previousSetting);
                } else {
                    delegateAdapter.write(out, value.getValue());
                }
            }

            @Override
            public Removable<?> read(JsonReader in) throws IOException {
                JsonToken next = in.peek();
                Removable<?> value;
                switch (next) {
                    case NULL:
                        value = Removable.NULL;
                        break;
                    case BEGIN_OBJECT:
                        value = Removable.of(delegateAdapter.read(in));
                        break;
                    case STRING:
                    default:
                        value = Removable.of(in.nextString());
                        break;
                }
                return value;
            }
        };
        return (TypeAdapter<T>) nullableTypeAdapter.nullSafe();
    }
}
