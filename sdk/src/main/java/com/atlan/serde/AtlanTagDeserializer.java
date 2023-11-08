/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AtlanTag;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

/**
 * Custom deserialization of {@link AtlanTag} objects.
 * In particular, this translates from the Atlan-internal hashed-string representation for an Atlan tag into
 * the human-readable name for an Atlan tag.
 */
public class AtlanTagDeserializer extends StdDeserializer<AtlanTag> {
    private static final long serialVersionUID = 2L;

    private final AtlanClient client;

    public AtlanTagDeserializer(AtlanClient client) {
        super(AtlanTag.class);
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
    public AtlanTag deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    /**
     * Actually do the work of deserializing an Atlan tag.
     *
     * @param root of the parsed JSON tree
     * @return the deserialized Atlan tag
     * @throws IOException on any issues parsing the JSON
     */
    AtlanTag deserialize(JsonNode root) throws IOException {
        String clsId = root.get("typeName").asText();
        if (clsId == null) {
            throw new IOException("Unable to deserialize Atlan tag from: " + root);
        }
        String clsName = null;
        try {
            // Translate the ID-string to a human-readable name
            clsName = client.getAtlanTagCache().getNameForId(clsId);
        } catch (NotFoundException e) {
            // Do nothing: if not found, the Atlan tag was deleted since but the
            // audit record remains
        } catch (AtlanException e) {
            throw new IOException("Unable to find Atlan tag with ID-string: " + clsId, e);
        }

        if (clsName == null) {
            return AtlanTag.builder()
                    .typeName(Serde.DELETED_AUDIT_OBJECT)
                    .entityGuid(JacksonUtils.deserializeString(root, "entityGuid"))
                    .entityStatus(
                            JacksonUtils.deserializeObject(client, root, "entityStatus", new TypeReference<>() {}))
                    .propagate(JacksonUtils.deserializeBoolean(root, "propagate"))
                    .removePropagationsOnEntityDelete(
                            JacksonUtils.deserializeBoolean(root, "removePropagationsOnEntityDelete"))
                    .restrictPropagationThroughLineage(
                            JacksonUtils.deserializeBoolean(root, "restrictPropagationThroughLineage"))
                    .build();
        } else {
            // TODO: Unfortunately, attempts to use a AtlanTagBeanDeserializerModifier to avoid the direct
            //  deserialization below were not successful — something to investigate another time
            return AtlanTag.builder()
                    .typeName(clsName)
                    .entityGuid(JacksonUtils.deserializeString(root, "entityGuid"))
                    .entityStatus(
                            JacksonUtils.deserializeObject(client, root, "entityStatus", new TypeReference<>() {}))
                    .propagate(JacksonUtils.deserializeBoolean(root, "propagate"))
                    .removePropagationsOnEntityDelete(
                            JacksonUtils.deserializeBoolean(root, "removePropagationsOnEntityDelete"))
                    .restrictPropagationThroughLineage(
                            JacksonUtils.deserializeBoolean(root, "restrictPropagationThroughLineage"))
                    .build();
        }
    }
}
