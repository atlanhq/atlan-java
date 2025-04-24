/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.CustomMetadataAttributes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom deserialization of a map from custom metadata set name to its values.
 * In particular, this translates from the Atlan-internal hashed-string representation for custom metadata into
 * the human-readable names for custom metadata.
 */
@Slf4j
public class CustomMetadataMapDeserializer extends StdDeserializer<Map<String, CustomMetadataAttributes>> {
    private static final long serialVersionUID = 2L;

    private final transient AtlanClient client;

    public CustomMetadataMapDeserializer(AtlanClient client) {
        super(CustomMetadataMapDeserializer.class);
        this.client = client;
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
            String cmId = it.next();
            try {
                String cmName = client.getCustomMetadataCache().getNameForSid(cmId);
                CustomMetadataAttributes cma =
                        client.getCustomMetadataCache().getCustomMetadataAttributes(cmId, root.get(cmId));
                map.put(cmName, cma);
            } catch (AtlanException e) {
                log.error(
                        "Unable to find custom metadata with ID-string {}, or to translate one of the provided attributes within it.",
                        cmId,
                        e);
            }
        }

        return map.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(map);
    }
}
