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

public class ClassificationDeserializer extends StdDeserializer<Classification> {
    private static final long serialVersionUID = 2L;

    public ClassificationDeserializer() {
        super(Classification.class);
    }

    @Override
    public Object deserializeWithType(
            JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(parser, context);
    }

    @Override
    public Classification deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        JsonNode root = parser.getCodec().readTree(parser);

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
