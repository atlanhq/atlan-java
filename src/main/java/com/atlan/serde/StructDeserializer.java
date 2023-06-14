/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.structs.AtlanStruct;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

/**
 * Deserialization of all {@link AtlanStruct} objects.
 * This custom deserialization is necessary to flatten some specific aspects of complexity in Atlan's struct payloads:
 * <ul>
 *     <li>The nested <code>attributes</code> structure.</li>
 * </ul>
 */
public class StructDeserializer extends StdDeserializer<AtlanStruct> implements ResolvableDeserializer {

    private static final long serialVersionUID = 2L;

    private final JsonDeserializer<?> defaultDeserializer;

    public StructDeserializer(JsonDeserializer<?> defaultDeserializer) {
        super(AtlanStruct.class);
        this.defaultDeserializer = defaultDeserializer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
            throws IOException {
        return deserialize(p, ctxt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AtlanStruct deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.readValueAsTree();
        if (root != null) {
            JsonNode attributes = root.get("attributes");
            if (attributes != null && attributes.isObject()) {
                // If there is an `attributes` key in the object, it's nested, so deserialize the nested content
                try (JsonParser nested = attributes.traverse(parser.getCodec())) {
                    return deserializeNested(nested, context);
                }
            } else {
                // Otherwise, deserialize it as-is
                try (JsonParser notNested = root.traverse(parser.getCodec())) {
                    return deserializeNested(notNested, context);
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
    }

    private AtlanStruct deserializeNested(JsonParser nested, DeserializationContext context) throws IOException {
        nested.nextToken(); // Prepare parser for deserialization
        return (AtlanStruct) defaultDeserializer.deserialize(nested, context);
    }
}
