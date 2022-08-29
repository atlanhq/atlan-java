/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.ClassificationCache;
import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.*;
import com.atlan.model.core.Classification;
import com.atlan.model.core.Entity;
import com.atlan.util.JacksonUtils;
import com.atlan.util.ReflectionUtils;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * Deserialization of all {@link Entity} objects, down through the entire inheritance hierarchy.
 * This custom deserialization is necessary to flatten some specific aspects of complexity in Atlan's payloads:
 * <ul>
 *     <li>The nested <code>attributes</code> and <code>relationshipAttributes</code> structures.</li>
 *     <li>The possibility that the same (relationship) attribute could appear in either of these nested structures.</li>
 *     <li>Handling the extension of properties as you traverse down the inheritance structures, without needing to also extend these nested structures through inheritance.</li>
 *     <li>Automatically translating the nested <code>businessAttributes</code> structure into custom metadata, including translating from Atlan's internal hashed-string representations into human-readable names.</li>
 * </ul>
 */
public class EntityDeserializer extends StdDeserializer<Entity> {

    private static final long serialVersionUID = 2L;

    public EntityDeserializer() {
        this(null);
    }

    public EntityDeserializer(Class<?> t) {
        super(t);
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
    public Entity deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        JsonNode root = parser.getCodec().readTree(parser);
        JsonNode attributes = root.get("attributes");
        JsonNode relationshipAttributes = root.get("relationshipAttributes");
        JsonNode businessAttributes = root.get("businessAttributes");
        JsonNode classificationNames = root.get("classificationNames");

        Entity.EntityBuilder<?, ?> builder;
        String typeName = root.get("typeName").asText();

        // TODO: figure out how to avoid needing to maintain this switch statement
        if (typeName == null) {
            builder = IndistinctAsset.builder();
        } else {
            switch (typeName) {
                case Readme.TYPE_NAME:
                    builder = Readme.builder();
                    break;
                case Glossary.TYPE_NAME:
                    builder = Glossary.builder();
                    break;
                case GlossaryCategory.TYPE_NAME:
                    builder = GlossaryCategory.builder();
                    break;
                case GlossaryTerm.TYPE_NAME:
                    builder = GlossaryTerm.builder();
                    break;
                case Connection.TYPE_NAME:
                    builder = Connection.builder();
                    break;
                case Table.TYPE_NAME:
                    builder = Table.builder();
                    break;
                case Column.TYPE_NAME:
                    builder = Column.builder();
                    break;
                case S3Bucket.TYPE_NAME:
                    builder = S3Bucket.builder();
                    break;
                case S3Object.TYPE_NAME:
                    builder = S3Object.builder();
                    break;
                case LineageProcess.TYPE_NAME:
                    builder = LineageProcess.builder();
                    break;
                case ColumnProcess.TYPE_NAME:
                    builder = ColumnProcess.builder();
                    break;
                default:
                    builder = IndistinctAsset.builder();
                    break;
            }
        }

        // Start by deserializing all the non-attribute properties (defined at Entity-level)
        builder = builder.typeName(JacksonUtils.deserializeString(root, "typeName"))
                .guid(JacksonUtils.deserializeString(root, "guid"))
                .displayText(JacksonUtils.deserializeString(root, "displayText"))
                .status(JacksonUtils.deserializeObject(root, "status", new TypeReference<>() {}))
                .createdBy(JacksonUtils.deserializeString(root, "createdBy"))
                .updatedBy(JacksonUtils.deserializeString(root, "updatedBy"))
                .createTime(JacksonUtils.deserializeLong(root, "createTime"))
                .updateTime(JacksonUtils.deserializeLong(root, "updateTime"))
                .deleteHandler(JacksonUtils.deserializeString(root, "deleteHandler"))
                .isIncomplete(JacksonUtils.deserializeBoolean(root, "isIncomplete"));
        Set<Classification> classifications =
                JacksonUtils.deserializeObject(root, "classifications", new TypeReference<>() {});
        if (classifications != null) {
            builder = builder.classifications(classifications);
        }
        Set<String> meaningNames = JacksonUtils.deserializeObject(root, "meaningNames", new TypeReference<>() {});
        if (meaningNames != null) {
            builder = builder.meaningNames(meaningNames);
        }

        Entity value = builder.build();

        Map<String, Method> setterMap = new LinkedHashMap<>();
        ReflectionUtils.getSetterMethods(setterMap, value.getClass());

        if (attributes != null && !attributes.isNull()) {
            Iterator<String> itr = attributes.fieldNames();
            while (itr.hasNext()) {
                String attrKey = itr.next();
                Method method = setterMap.get(attrKey);
                if (method != null) {
                    try {
                        deserialize(value, attributes.get(attrKey), method);
                    } catch (NoSuchMethodException e) {
                        throw new IOException("Missing fromValue method for enum.", e);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IOException("Failed to deserialize through reflection.", e);
                    }
                }
            }
        }

        if (relationshipAttributes != null && !relationshipAttributes.isNull()) {
            Iterator<String> itr = relationshipAttributes.fieldNames();
            while (itr.hasNext()) {
                String relnKey = itr.next();
                Method method = setterMap.get(relnKey);
                if (method != null) {
                    try {
                        deserialize(value, relationshipAttributes.get(relnKey), method);
                    } catch (NoSuchMethodException e) {
                        throw new IOException("Missing fromValue method for enum.", e);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IOException("Failed to deserialize through reflection.", e);
                    }
                }
            }
        }

        Map<String, CustomMetadataAttributes> cm = null;
        if (businessAttributes != null) {
            // Translate these into custom metadata structure
            try {
                cm = CustomMetadataCache.getCustomMetadataFromBusinessAttributes(businessAttributes);
            } catch (AtlanException e) {
                e.printStackTrace();
                throw new IOException("Unable to deserialize custom metadata.", e);
            }
        }

        Set<String> clsNames = null;
        if (classificationNames != null && classificationNames.isArray()) {
            clsNames = new HashSet<>();
            // Translate these IDs in to human-readable names
            try {
                for (JsonNode element : classificationNames) {
                    String name = ClassificationCache.getNameForId(element.asText());
                    clsNames.add(name);
                }
            } catch (AtlanException e) {
                throw new IOException("Unable to deserialize classification name.", e);
            }
        }

        // Special cases to wrap-up:
        // Decode the Readme's description after deserialization
        if (typeName != null && typeName.equals("Readme")) {
            Readme readme = (Readme) value;
            readme.setDescription(StringUtils.decodeContent(readme.getDescription()));
        }

        value.setCustomMetadataSets(cm);
        value.setClassificationNames(clsNames);

        return value;
    }

    private void deserialize(Entity value, JsonNode jsonNode, Method method)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (jsonNode.isValueNode()) {
            deserializePrimitive(value, jsonNode, method);
        } else if (jsonNode.isArray()) {
            deserializeList(value, (ArrayNode) jsonNode, method);
        } else if (jsonNode.isObject()) {
            deserializeObject(value, jsonNode, method);
        }
        // Only type left is null, which we don't need to explicitly set (it's the default)
    }

    private void deserializeList(Entity value, ArrayNode array, Method method)
            throws IllegalAccessException, InvocationTargetException, IOException {
        Class<?> paramClass = ReflectionUtils.getParameterOfMethod(method);
        List<Object> list = new ArrayList<>();
        for (JsonNode element : array) {
            Object deserialized = deserializeElement(element, method);
            list.add(deserialized);
        }
        if (paramClass == List.class) {
            method.invoke(value, list);
        } else if (paramClass == Set.class) {
            method.invoke(value, new LinkedHashSet<>(list));
        } else {
            throw new IOException("Unable to deserialize JSON list to Java class: " + paramClass.getCanonicalName());
        }
    }

    private void deserializeObject(Entity value, JsonNode jsonObject, Method method)
            throws IllegalAccessException, InvocationTargetException, IOException {
        Class<?> paramClass = ReflectionUtils.getParameterOfMethod(method);
        method.invoke(value, Serde.mapper.readValue(jsonObject.toString(), paramClass));
    }

    /**
     * Deserialize a value direct to an object.
     * @param element to deserialize
     * @param method to which the deserialized value will be built into an entity
     * @return the deserialized object
     * @throws IOException if an array is found nested directly within another array (unsupported)
     */
    private Object deserializeElement(JsonNode element, Method method) throws IOException {
        if (element.isValueNode()) {
            return JacksonUtils.deserializePrimitive(element, method);
        } else if (element.isArray()) {
            throw new IOException("Directly-nested arrays are not supported.");
        } else if (element.isObject()) {
            Type paramType = ReflectionUtils.getParameterizedTypeOfMethod(method);
            Class<?> innerClass = ReflectionUtils.getClassOfParameterizedType(paramType);
            return Serde.mapper.readValue(element.toString(), innerClass);
        }
        return null;
    }

    private void deserializePrimitive(Entity value, JsonNode primitive, Method method)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (primitive.isTextual()) {
            Class<?> paramClass = ReflectionUtils.getParameterOfMethod(method);
            if (paramClass.isEnum()) {
                Method fromValue = paramClass.getMethod("fromValue", String.class);
                method.invoke(value, fromValue.invoke(null, primitive.asText()));
            } else {
                method.invoke(value, primitive.asText());
            }
        } else if (primitive.isBoolean()) {
            method.invoke(value, primitive.asBoolean());
        } else if (primitive.isNumber()) {
            deserializeNumber(value, primitive, method);
        }
    }

    private void deserializeNumber(Entity value, JsonNode primitive, Method method)
            throws IllegalAccessException, InvocationTargetException, IOException {
        Object number = JacksonUtils.deserializeNumber(primitive, method);
        method.invoke(value, number);
    }
}
