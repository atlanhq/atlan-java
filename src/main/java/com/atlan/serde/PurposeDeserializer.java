/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.AtlanTagCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.Purpose;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Custom deserialization of {@link Purpose} objects.
 * In particular, this translates from the Atlan-internal hashed-string representation for an Atlan tag into
 * the human-readable name for an Atlan tag.
 */
public class PurposeDeserializer extends StdDeserializer<Purpose> {
    private static final long serialVersionUID = 2L;

    public PurposeDeserializer() {
        super(Purpose.class);
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
    public Purpose deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    /**
     * Actually do the work of deserializing a purpose.
     *
     * @param root of the parsed JSON tree
     * @return the deserialized purpose
     * @throws IOException on any issues parsing the JSON
     */
    Purpose deserialize(JsonNode root) throws IOException {
        JsonNode tags = root.get("tags");
        if (!(tags instanceof ArrayNode)) {
            throw new IOException("Unable to deserialize purpose from: " + root);
        }
        SortedSet<String> clsNames = new TreeSet<>();
        ArrayNode tagsList = (ArrayNode) tags;
        for (int i = 0; i < tagsList.size(); i++) {
            String tagId = tagsList.get(i).asText();
            try {
                String tagName = AtlanTagCache.getNameForId(tagId);
                clsNames.add(tagName);
            } catch (AtlanException e) {
                throw new IOException("Unable to find Atlan tag with ID-string: " + tagId, e);
            }
        }

        return Purpose.builder()
                .id(JacksonUtils.deserializeString(root, "id"))
                .name(JacksonUtils.deserializeString(root, "name"))
                .displayName(JacksonUtils.deserializeString(root, "displayName"))
                .description(JacksonUtils.deserializeString(root, "description"))
                .tags(clsNames)
                .metadataPolicies(JacksonUtils.deserializeObject(root, "metadataPolicies", new TypeReference<>() {}))
                .dataPolicies(JacksonUtils.deserializeObject(root, "dataPolicies", new TypeReference<>() {}))
                .enabled(JacksonUtils.deserializeBoolean(root, "enabled"))
                .isActive(JacksonUtils.deserializeBoolean(root, "isActive"))
                .createdAt(JacksonUtils.deserializeLong(root, "createdAt"))
                .createdBy(JacksonUtils.deserializeString(root, "createdBy"))
                .updatedAt(JacksonUtils.deserializeLong(root, "updatedAt"))
                .updatedBy(JacksonUtils.deserializeString(root, "updatedBy"))
                .level(JacksonUtils.deserializeString(root, "level"))
                .version(JacksonUtils.deserializeString(root, "version"))
                .attributes(JacksonUtils.deserializeObject(root, "attributes", new TypeReference<>() {}))
                .build();
    }
}
