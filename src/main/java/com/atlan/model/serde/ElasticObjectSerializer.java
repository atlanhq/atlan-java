/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.serde;

import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.JsonpSerializable;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import jakarta.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.lang.reflect.Type;

public class ElasticObjectSerializer<T extends JsonpSerializable> implements JsonSerializer<T> {

    private static final JsonpMapper mapper = new JacksonJsonpMapper();

    /** Serializes any serializable Elastic object (aggregation, query, etc) into a JSON string. */
    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {

        if (src != null) {
            StringWriter sw = new StringWriter();
            JsonGenerator generator = mapper.jsonProvider().createGenerator(sw);
            src.serialize(generator, mapper);
            generator.close();
            return JsonParser.parseString(sw.toString());
        } else {
            return null;
        }
    }
}
