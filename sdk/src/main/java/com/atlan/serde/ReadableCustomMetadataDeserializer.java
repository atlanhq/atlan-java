/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.LogicException;
import com.atlan.model.core.CustomMetadataAttributes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom deserialization of a map from custom metadata set name to its values.
 * In particular, this retains human-readable names throughout.
 */
@Slf4j
public class ReadableCustomMetadataDeserializer extends StdDeserializer<Map<String, CustomMetadataAttributes>> {
    private static final long serialVersionUID = 2L;

    public ReadableCustomMetadataDeserializer() {
        super(CustomMetadataMapDeserializer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object deserializeWithType(
            JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(parser, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, CustomMetadataAttributes> deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    /**
     * Actually do the work of deserializing a custom metadata map.
     *
     * @param root of the parsed JSON tree
     * @return the deserialized custom metadata details
     * @throws IOException on any issues parsing the JSON
     */
    Map<String, CustomMetadataAttributes> deserialize(JsonNode root) throws IOException {
        Map<String, CustomMetadataAttributes> map = new HashMap<>();

        // Iterate through all the top-level field names of the object...
        for (Iterator<String> it = root.fieldNames(); it.hasNext(); ) {
            String cmName = it.next();
            CustomMetadataAttributes.CustomMetadataAttributesBuilder<?, ?> builder = CustomMetadataAttributes.builder();
            JsonNode attributeNames = root.get(cmName);
            try {
                for (Iterator<String> attrs = attributeNames.fieldNames(); attrs.hasNext(); ) {
                    String attrName = attrs.next();
                    JsonNode jsonValue = attributeNames.get(attrName);
                    if (jsonValue.isArray()) {
                        Set<Object> values = new HashSet<>();
                        ArrayNode array = (ArrayNode) jsonValue;
                        for (JsonNode element : array) {
                            Object primitive = CustomMetadataCache.deserializePrimitive(element);
                            values.add(primitive);
                        }
                        if (!values.isEmpty()) {
                            builder.attribute(attrName, values);
                        }
                    } else if (jsonValue.isValueNode()) {
                        Object primitive = CustomMetadataCache.deserializePrimitive(jsonValue);
                        builder.attribute(attrName, primitive);
                    } else {
                        throw new LogicException(ErrorCode.UNABLE_TO_DESERIALIZE, jsonValue.toString());
                    }
                }
            } catch (AtlanException e) {
                log.error("Unable to translate one of the provided custom metadata attributes of: {}.", cmName, e);
            }
            map.put(cmName, builder.build());
        }
        return map.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(map);
    }
}
