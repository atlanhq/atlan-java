package com.atlan.model.serde;

import com.atlan.cache.ClassificationCacheJ;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.ClassificationJ;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

public class ClassificationJDeserializer extends StdDeserializer<ClassificationJ> {
    private static final long serialVersionUID = 2L;

    private final JsonDeserializer<ClassificationJ> defaultDeserializer;

    public ClassificationJDeserializer() {
        this(null);
    }

    public ClassificationJDeserializer(JsonDeserializer<ClassificationJ> defaultDeserializer) {
        super(ClassificationJ.class);
        this.defaultDeserializer = defaultDeserializer;
    }

    @Override
    public Object deserializeWithType(
            JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(parser, context);
    }

    @Override
    public ClassificationJ deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        JsonNode root = parser.getCodec().readTree(parser);

        String clsId = root.get("typeName").asText();
        if (clsId == null) {
            throw new IOException("Unable to deserialize classification from: " + root);
        }
        String clsName;
        try {
            // Translate the ID-string to a human-readable name
            clsName = ClassificationCacheJ.getNameForId(clsId);
        } catch (AtlanException e) {
            throw new IOException("Unable to find classification with ID-string: " + clsId, e);
        }

        return ClassificationJ.builder()
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
