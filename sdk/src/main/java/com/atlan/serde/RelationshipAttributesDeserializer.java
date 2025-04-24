/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.cache.ReflectionCache;
import com.atlan.model.assets.*;
import com.atlan.model.relations.IndistinctRelationship;
import com.atlan.model.relations.RelationshipAttributes;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Deserialization of all {@link RelationshipAttributes} objects.
 * This custom deserialization is necessary to flatten some specific aspects of complexity in Atlan's payloads:
 * <ul>
 *     <li>The nested <code>attributes</code> structure.</li>
 * </ul>
 */
@Slf4j
public class RelationshipAttributesDeserializer extends StdDeserializer<RelationshipAttributes> {

    private static final long serialVersionUID = 2L;
    private final transient AtlanClient client;

    public RelationshipAttributesDeserializer(AtlanClient client) {
        this(Asset.class, client);
    }

    public RelationshipAttributesDeserializer(Class<?> t, AtlanClient client) {
        super(t);
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
    public RelationshipAttributes deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    /**
     * Actually do the work of deserializing relationship attributes.
     *
     * @param root of the parsed JSON tree
     * @return the deserialized relationship attributes
     * @throws IOException on any issues parsing the JSON
     */
    RelationshipAttributes deserialize(JsonNode root) throws IOException {

        JsonNode attributes = root.get("attributes");

        RelationshipAttributes.RelationshipAttributesBuilder<?, ?> builder;
        Class<?> relationshipClass;

        JsonNode typeNameJson = root.get("typeName");
        String typeName = null;

        if (typeNameJson == null || typeNameJson.isNull()) {
            builder = IndistinctRelationship._internal();
            relationshipClass = IndistinctRelationship.class;
        } else {
            typeName = root.get("typeName").asText();
            try {
                relationshipClass = Serde.getRelationshipAttributesClassForType(typeName);
                Method method = relationshipClass.getMethod("builder");
                Object result = method.invoke(null);
                builder = (RelationshipAttributes.RelationshipAttributesBuilder<?, ?>) result;
            } catch (ClassNotFoundException
                    | NoSuchMethodException
                    | InvocationTargetException
                    | IllegalAccessException e) {
                log.warn(
                        "Unable to dynamically retrieve relationship for typeName {}, falling back to an IndistinctRelationship.",
                        typeName,
                        e);
                builder = IndistinctRelationship._internal();
                relationshipClass = IndistinctRelationship.class;
            }
        }

        // Start by deserializing all the non-attribute properties
        builder.typeName(JacksonUtils.deserializeString(root, "typeName"));
        Class<?> builderClass = builder.getClass();

        if (attributes != null && !attributes.isNull()) {
            Iterator<String> itr = attributes.fieldNames();
            while (itr.hasNext()) {
                String relnKey = itr.next();
                String deserializeName = ReflectionCache.getDeserializedName(relationshipClass, relnKey);
                Method method = ReflectionCache.getSetter(builderClass, deserializeName);
                if (method != null) {
                    try {
                        Object value = Serde.deserialize(client, attributes.get(relnKey), method, deserializeName);
                        ReflectionCache.setValue(builder, deserializeName, value);
                    } catch (NoSuchMethodException e) {
                        throw new IOException("Missing fromValue method for enum.", e);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IOException("Failed to deserialize through reflection.", e);
                    }
                }
            }
        }

        RelationshipAttributes result = builder.build();
        result.setRawJsonObject(root);
        return result;
    }
}
