/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.ClassificationCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.Purpose;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Custom serialization of {@link Purpose} objects.
 * In particular, this translates from the human-readable name into the Atlan-internal hashed-string representation for
 * a classification.
 */
public class PurposeSerializer extends StdSerializer<Purpose> {
    private static final long serialVersionUID = 2L;

    public PurposeSerializer() {
        this(null);
    }

    public PurposeSerializer(Class<Purpose> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serializeWithType(
            Purpose value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        serialize(value, gen, serializers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(Purpose cls, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {

        Set<String> tags = cls.getTags();
        SortedSet<String> tagIds = new TreeSet<>();
        for (String tag : tags) {
            if (tag != null) {
                try {
                    String id = ClassificationCache.getIdForName(tag);
                    tagIds.add(id);
                } catch (AtlanException e) {
                    throw new IOException("Unable to find classification with name: " + tag, e);
                }
            }
        }

        gen.writeStartObject();
        JacksonUtils.serializeString(gen, "id", cls.getId());
        JacksonUtils.serializeString(gen, "name", cls.getName());
        JacksonUtils.serializeString(gen, "displayName", cls.getDisplayName());
        JacksonUtils.serializeString(gen, "description", cls.getDescription());
        JacksonUtils.serializeObject(gen, "tags", tagIds);
        JacksonUtils.serializeObject(gen, "metadataPolicies", cls.getMetadataPolicies());
        JacksonUtils.serializeObject(gen, "dataPolicies", cls.getDataPolicies());
        JacksonUtils.serializeBoolean(gen, "enabled", cls.getEnabled());
        JacksonUtils.serializeBoolean(gen, "isActive", cls.getIsActive());
        JacksonUtils.serializeLong(gen, "createdAt", cls.getCreatedAt());
        JacksonUtils.serializeString(gen, "createdBy", cls.getCreatedBy());
        JacksonUtils.serializeLong(gen, "updatedAt", cls.getUpdatedAt());
        JacksonUtils.serializeString(gen, "updatedBy", cls.getUpdatedBy());
        JacksonUtils.serializeString(gen, "level", cls.getLevel());
        JacksonUtils.serializeString(gen, "version", cls.getVersion());
        JacksonUtils.serializeObject(gen, "attributes", cls.getAttributes());
        gen.writeEndObject();
    }
}
