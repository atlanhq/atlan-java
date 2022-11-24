/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.ClassificationCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.Classification;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

/**
 * Custom deserialization of {@link Classification} objects.
 * In particular, this translates from the Atlan-internal hashed-string representation for a classification into
 * the human-readable name for a classification.
 */
public class ClassificationDeserializer extends StdDeserializer<Classification> {
    private static final long serialVersionUID = 2L;

    public ClassificationDeserializer() {
        super(Classification.class);
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
    public Classification deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    /**
     * Actually do the work of deserializing a classification.
     *
     * @param root of the parsed JSON tree
     * @return the deserialized classification
     * @throws IOException on any issues parsing the JSON
     */
    Classification deserialize(JsonNode root) throws IOException {
        String clsId = root.get("typeName").asText();
        if (clsId == null) {
            throw new IOException("Unable to deserialize classification from: " + root);
        }
        String clsName;
        try {
            // Translate the ID-string to a human-readable name
            clsName = ClassificationCache.getNameForId(clsId);
        } catch (AtlanException e) {
            throw new IOException("Unable to find classification with ID-string: " + clsId, e);
        }

        // TODO: Unfortunately, attempts to use a ClassificationBeanDeserializerModifier to avoid the direct
        //  deserialization below were not successful — something to investigate another time
        return Classification.builder()
                .typeName(clsName)
                .entityGuid(JacksonUtils.deserializeString(root, "entityGuid"))
                .entityStatus(JacksonUtils.deserializeObject(root, "entityStatus", new TypeReference<>() {}))
                .propagate(JacksonUtils.deserializeBoolean(root, "propagate"))
                .removePropagationsOnEntityDelete(
                        JacksonUtils.deserializeBoolean(root, "removePropagationsOnEntityDelete"))
                .restrictPropagationThroughLineage(
                        JacksonUtils.deserializeBoolean(root, "restrictPropagationThroughLineage"))
                .build();
    }
}
