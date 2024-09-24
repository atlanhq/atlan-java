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
import com.fasterxml.jackson.core.type.TypeReference;
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
        return deserialize(parser.getCodec().readTree(parser));
    }

    /**
     * Actually do the work of deserializing a source tag attachment.
     *
     * @param root of the parsed JSON tree
     * @return the deserialized source tag attachment
     * @throws IOException on any issues parsing the JSON
     */
    SourceTagAttachment deserialize(JsonNode root) throws IOException {
        JsonNode jsGuid = root.get("sourceTagGuid");
        JsonNode jsQN = root.get("sourceTagQualifiedName");
        JsonNode jsName = root.get("sourceTagName");

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
        return SourceTagAttachment.builder()
                .sourceTagName(source.getName())
                .sourceTagQualifiedName(sourceTagQualifiedName)
                .sourceTagGuid(source.getGuid())
                .sourceTagConnectorName(Connection.getConnectorTypeFromQualifiedName(sourceTagQualifiedName)
                        .getValue())
                .sourceTagValues(
                        JacksonUtils.deserializeObject(client, root, "sourceTagValues", new TypeReference<>() {}))
                .isSourceTagSynced(JacksonUtils.deserializeBoolean(root, "isSourceTagSynced"))
                .sourceTagSyncTimestamp(JacksonUtils.deserializeLong(root, "sourceTagSyncTimestamp"))
                .sourceTagSyncError(JacksonUtils.deserializeString(root, "sourceTagSyncError"))
                .build();
    }
}
