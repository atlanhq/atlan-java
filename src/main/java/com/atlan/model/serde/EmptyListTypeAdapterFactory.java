package com.atlan.model.serde;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Creates type adapter to translate between empty lists and null values
 * (will always write empty lists as null, but always read nulls as empty lists).
 */
public class EmptyListTypeAdapterFactory implements TypeAdapterFactory {

    private final boolean createdByJsonAdapter;

    public static final EmptyListTypeAdapterFactory INSTANCE = new EmptyListTypeAdapterFactory(false);

    private EmptyListTypeAdapterFactory(boolean createdByJsonAdapter) {
        this.createdByJsonAdapter = createdByJsonAdapter;
    }

    /**
     * @deprecated Only intended to be called by Gson when used for {@code JsonAdapter}.
     */
    @Deprecated
    private EmptyListTypeAdapterFactory() {
        this(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!List.class.isAssignableFrom(type.getRawType())) {
            return null;
        }

        TypeAdapter<List<Object>> delegate = (TypeAdapter<List<Object>>)
                (createdByJsonAdapter ? gson.getAdapter(type) : gson.getDelegateAdapter(this, type));

        TypeAdapter<List<Object>> adapter = new TypeAdapter<>() {
            @Override
            public void write(JsonWriter out, List<Object> value) throws IOException {
                if (value == null || value.isEmpty()) {
                    delegate.write(out, null);
                } else {
                    delegate.write(out, value);
                }
            }

            @Override
            public List<Object> read(JsonReader in) throws IOException {
                List<Object> list = delegate.read(in);
                if (list == null) {
                    list = Collections.emptyList();
                }
                return list;
            }
        };
        return (TypeAdapter<T>) adapter.nullSafe();
    }
}
