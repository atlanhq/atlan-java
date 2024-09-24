/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.cache.SourceTagCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.ITag;
import com.atlan.model.structs.SourceTagAttachment;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

/**
 * Custom deserialization of {@link SourceTagAttachment} objects.
 * In particular, this translates from one of many representations of the source-tag to its other representations.
 */
public class SourceTagAttachmentDeserializer extends StdDeserializer<SourceTagAttachment> {
    private static final long serialVersionUID = 2L;

    private final AtlanClient client;

    public SourceTagAttachmentDeserializer(AtlanClient client) {
        super(SourceTagAttachment.class);
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
    public SourceTagAttachment deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.readValueAsTree();
        SourceTagAttachment sta = null;
        if (root != null) {
            TreeNode attributes = root.get("attributes");
            if (attributes != null && attributes.isObject()) {
                // If there is an `attributes` key in the object, it's nested, so deserialize the nested content
                try (JsonParser nested = attributes.traverse(parser.getCodec())) {
                    sta = deserializeNested(nested);
                    sta.setRawJsonObject(root);
                }
            } else {
                // Otherwise, reset the parser back to the start of the sequence and deserialize it
                try (JsonParser restart =
                        JsonParserSequence.createFlattened(true, root.traverse(parser.getCodec()), parser)) {
                    sta = deserializeNested(restart);
                    sta.setRawJsonObject(root);
                }
            }
        }
        return sta;
    }

    private SourceTagAttachment deserializeNested(JsonParser nested) throws IOException {
        nested.nextToken(); // Consume the opening of the nested object
        JsonNode root = nested.getCodec().readTree(nested);

        JsonNode jsGuid = root.get("sourceTagGuid");
        JsonNode jsQN = root.get("sourceTagQualifiedName");
        JsonNode jsName = root.get("sourceTagName");

        // TODO: all of the above are nested inside `attributes`...

        ITag source = null;
        AtlanException error = null;
        String failedIdType = "";
        String failedValue = "";
        if (jsGuid != null && !jsGuid.isNull() && !jsGuid.asText().isEmpty()) {
            try {
                source = (ITag) client.getSourceTagCache().getByGuid(jsGuid.asText());
            } catch (AtlanException e) {
                failedIdType = "GUID";
                failedValue = jsGuid.asText();
                error = e;
            }
        }
        if (source == null && jsQN != null && !jsQN.isNull() && !jsQN.asText().isEmpty()) {
            try {
                source = (ITag) client.getSourceTagCache().getByQualifiedName(jsQN.asText());
            } catch (AtlanException e) {
                failedIdType = "qualifiedName";
                failedValue = jsQN.asText();
                error = e;
            }
        }
        if (source == null
                && jsName != null
                && !jsName.isNull()
                && !jsName.asText().isEmpty()) {
            try {
                SourceTagCache.SourceTagName name = new SourceTagCache.SourceTagName(jsName.asText());
                source = (ITag) client.getSourceTagCache().getByName(name);
            } catch (AtlanException e) {
                failedIdType = "name";
                failedValue = jsName.asText();
                error = e;
            }
        }
        if (source == null) {
            throw new IOException("Unable to find source tag by " + failedIdType + ": " + failedValue, error);
        }

        // TODO: Unfortunately, attempts to use a AtlanTagBeanDeserializerModifier to avoid the direct
        //  deserialization below were not successful — something to investigate another time
        String sourceTagQualifiedName = source.getQualifiedName();
        SourceTagAttachment.SourceTagAttachmentBuilder<?, ?> builder = SourceTagAttachment.builder()
                .sourceTagName(source.getName())
                .sourceTagQualifiedName(sourceTagQualifiedName)
                .sourceTagGuid(source.getGuid())
                .sourceTagConnectorName(Connection.getConnectorTypeFromQualifiedName(sourceTagQualifiedName)
                        .getValue())
                .isSourceTagSynced(JacksonUtils.deserializeBoolean(root, "isSourceTagSynced"))
                .sourceTagSyncTimestamp(JacksonUtils.deserializeLong(root, "sourceTagSyncTimestamp"))
                .sourceTagSyncError(JacksonUtils.deserializeString(root, "sourceTagSyncError"));
        JsonNode stv = root.get("sourceTagValue");
        if (stv != null && !stv.isNull()) {
            builder.sourceTagValues(
                    JacksonUtils.deserializeObject(client, root, "sourceTagValue", new TypeReference<>() {}));
        }
        return builder.build();
    }
}
