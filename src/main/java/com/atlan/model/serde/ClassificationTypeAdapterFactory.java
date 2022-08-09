/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.serde;

import com.atlan.cache.ClassificationCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.Classification;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 * Creates type adapter for interface {@link Classification} able to deserialize Atlan-internal IDs to human-readable
 * names and vice versa on serialization.
 */
public class ClassificationTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!Classification.class.isAssignableFrom(type.getRawType())) {
            return null;
        }

        final TypeAdapter<JsonElement> jsonElementAdapter = gson.getAdapter(JsonElement.class);
        final TypeAdapter<Classification> classificationAdapter =
                gson.getDelegateAdapter(this, TypeToken.get(Classification.class));

        TypeAdapter<Classification> resultCustomTypeAdapter = new TypeAdapter<>() {
            @Override
            public void write(JsonWriter out, Classification value) throws IOException {
                // Approach: change the object itself, then serialize it at the end
                String classificationName = value.getTypeName();
                String classificationId;
                try {
                    classificationId = ClassificationCache.getIdForName(classificationName);
                } catch (AtlanException e) {
                    throw new IOException("Unable to find classification with name: " + classificationName, e);
                }
                value.setTypeName(classificationId);
                classificationAdapter.write(out, value);
            }

            @Override
            public Classification read(JsonReader in) throws IOException {
                JsonObject object = jsonElementAdapter.read(in).getAsJsonObject();
                JsonPrimitive classificationId =
                        object.getAsJsonPrimitive("typeName").getAsJsonPrimitive();
                if (classificationId == null || !classificationId.isString()) {
                    throw new IOException("Unable to deserialize classification from: " + object);
                }
                String classificationName;
                try {
                    classificationName = ClassificationCache.getNameForId(classificationId.getAsString());
                } catch (AtlanException e) {
                    throw new IOException("Unable to find classification with ID-string: " + classificationId, e);
                }
                object.addProperty("typeName", classificationName);
                return classificationAdapter.fromJsonTree(object);
            }
        };
        return (TypeAdapter<T>) resultCustomTypeAdapter.nullSafe();
    }
}
