/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.serde;

import com.atlan.model.GlossaryTerm;
import com.atlan.model.core.Entity;
import com.atlan.model.UnknownAsset;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 * Creates type adapter for interface {@code Entity} able to deserialize raw JSON to subtype
 * implementation based on discriminator field {@code typeName}.
 */
public class EntityTypeAdapterFactory implements TypeAdapterFactory {

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (!Entity.class.isAssignableFrom(type.getRawType())) {
      return null;
    }
    final String discriminator = "typeName";
    final TypeAdapter<JsonElement> jsonElementAdapter = gson.getAdapter(JsonElement.class);
    final TypeAdapter<com.atlan.model.core.Entity> entityAdapter =
        gson.getDelegateAdapter(this, TypeToken.get(com.atlan.model.core.Entity.class));
    final TypeAdapter<UnknownAsset> assetStubAdapter =
        gson.getDelegateAdapter(this, TypeToken.get(UnknownAsset.class));
    final TypeAdapter<com.atlan.model.GlossaryTerm> glossaryTermAdapter =
        gson.getDelegateAdapter(this, TypeToken.get(com.atlan.model.GlossaryTerm.class));

    TypeAdapter<Entity> resultCustomTypeAdapter =
        new TypeAdapter<>() {
          @Override
          public void write(JsonWriter out, Entity value) throws IOException {
            String typeName = value.getTypeName();
            switch (typeName) {
              case "AtlasGlossaryTerm":
                glossaryTermAdapter.write(out, (GlossaryTerm) value);
                break;
              default:
                entityAdapter.write(out, value);
                break;
            }
          }

          @Override
          public Entity read(JsonReader in) throws IOException {
            JsonObject object = jsonElementAdapter.read(in).getAsJsonObject();
            Entity objectResult;
            String typeName = object.getAsJsonPrimitive(discriminator).getAsString();
            switch (typeName) {
              case "AtlasGlossaryTerm":
                objectResult = glossaryTermAdapter.fromJsonTree(object);
                break;
              default:
                objectResult = assetStubAdapter.fromJsonTree(object);
                break;
            }
            return objectResult;
          }
        };
    return (TypeAdapter<T>) resultCustomTypeAdapter.nullSafe();
  }
}
