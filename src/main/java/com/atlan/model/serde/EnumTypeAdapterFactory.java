/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.serde;

import com.atlan.model.enums.AtlanEnum;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 * Creates type adapter for interface {@code AtlanEnum} able to de/serialize enumerations from and
 * to JSON.
 */
public class EnumTypeAdapterFactory implements TypeAdapterFactory {

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (!AtlanEnum.class.isAssignableFrom(type.getRawType())) {
      return null;
    }

    // final TypeAdapter<JsonElement> jsonElementAdapter = gson.getAdapter(JsonElement.class);

    TypeAdapter<AtlanEnum> paramEnum =
        new TypeAdapter<>() {
          @Override
          public void write(JsonWriter out, AtlanEnum value) throws IOException {
            System.out.println("Serializing an enumeration value: " + value);
            if (value.getValue().equals("")) {
              // need to restore serialize null setting
              // not to affect other fields
              boolean previousSetting = out.getSerializeNulls();
              out.setSerializeNulls(true);
              out.nullValue();
              out.setSerializeNulls(previousSetting);
            } else {
              out.value(value.getValue());
            }
          }

          @Override
          public AtlanEnum read(JsonReader in) {
            // String value = jsonElementAdapter.read(in).getAsString();
            // TODO: translate string value to an enum
            throw new UnsupportedOperationException(
                "No deserialization is expected from this private type adapter for enum param.");
          }
        };
    return (TypeAdapter<T>) paramEnum.nullSafe();
  }
}
