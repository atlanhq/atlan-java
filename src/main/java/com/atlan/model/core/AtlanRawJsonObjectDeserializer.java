/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class AtlanRawJsonObjectDeserializer implements JsonDeserializer<AtlanRawJsonObject> {
  /** Deserializes a JSON payload into a {@link AtlanRawJsonObject} object. */
  @Override
  public AtlanRawJsonObject deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    AtlanRawJsonObject object = new AtlanRawJsonObject();
    object.json = json.getAsJsonObject();
    return object;
  }
}
