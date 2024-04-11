/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.cache.ReflectionCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.util.JacksonUtils;
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
import lombok.extern.slf4j.Slf4j;

/**
 * Deserialization of all {@link Asset} objects, down through the entire inheritance hierarchy.
 * This custom deserialization is necessary to flatten some specific aspects of complexity in Atlan's payloads:
 * <ul>
 *     <li>The nested <code>attributes</code> and <code>relationshipAttributes</code> structures.</li>
 *     <li>The possibility that the same (relationship) attribute could appear in either of these nested structures.</li>
 *     <li>Handling the extension of properties as you traverse down the inheritance structures, without needing to also extend these nested structures through inheritance.</li>
 *     <li>Automatically translating the nested <code>businessAttributes</code> structure into custom metadata, including translating from Atlan's internal hashed-string representations into human-readable names.</li>
 * </ul>
 */
@Slf4j
public class AssetDeserializer extends StdDeserializer<Asset> {

    private static final long serialVersionUID = 2L;
    private final AtlanClient client;

    public AssetDeserializer(AtlanClient client) {
        this(Asset.class, client);
    }

    public AssetDeserializer(Class<?> t, AtlanClient client) {
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
    public Asset deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    /**
     * Actually do the work of deserializing an asset.
     *
     * @param root of the parsed JSON tree
     * @return the deserialized asset
     * @throws IOException on any issues parsing the JSON
     */
    @SuppressWarnings("deprecation") // Suppress deprecation notice on use of atlanTagNames builder
    Asset deserialize(JsonNode root) throws IOException {

        JsonNode attributes = root.get("attributes");
        JsonNode relationshipGuid = root.get("relationshipGuid");
        JsonNode relationshipAttributes = root.get("relationshipAttributes");
        JsonNode businessAttributes = root.get("businessAttributes");
        JsonNode classificationNames = root.get("classificationNames");

        Asset.AssetBuilder<?, ?> builder;
        Class<?> assetClass;

        JsonNode typeNameJson = root.get("typeName");
        String typeName = null;

        if (typeNameJson == null || typeNameJson.isNull()) {
            builder = IndistinctAsset._internal();
            assetClass = IndistinctAsset.class;
        } else {
            typeName = root.get("typeName").asText();
            try {
                assetClass = Serde.getAssetClassForType(typeName);
                Method method = assetClass.getMethod("_internal");
                Object result = method.invoke(null);
                builder = (Asset.AssetBuilder<?, ?>) result;
            } catch (ClassNotFoundException
                    | NoSuchMethodException
                    | InvocationTargetException
                    | IllegalAccessException e) {
                log.error(
                        "Unable to dynamically retrieve asset for typeName {}, falling back to an IndistinctAsset.",
                        typeName,
                        e);
                builder = IndistinctAsset._internal();
                assetClass = IndistinctAsset.class;
            }
        }

        // Start by deserializing all the non-attribute properties (defined at Asset-level)
        builder.typeName(JacksonUtils.deserializeString(root, "typeName"))
                .guid(JacksonUtils.deserializeString(root, "guid"))
                .displayText(JacksonUtils.deserializeString(root, "displayText"))
                // Include reference attributes, for related entities
                .entityStatus(JacksonUtils.deserializeString(root, "entityStatus"))
                .relationshipType(JacksonUtils.deserializeString(root, "relationshipType"))
                .relationshipGuid(JacksonUtils.deserializeString(root, "relationshipGuid"))
                .relationshipStatus(
                        JacksonUtils.deserializeObject(client, root, "relationshipStatus", new TypeReference<>() {}))
                .uniqueAttributes(
                        JacksonUtils.deserializeObject(client, root, "uniqueAttributes", new TypeReference<>() {}))
                .status(JacksonUtils.deserializeObject(client, root, "status", new TypeReference<>() {}))
                .createdBy(JacksonUtils.deserializeString(root, "createdBy"))
                .updatedBy(JacksonUtils.deserializeString(root, "updatedBy"))
                .createTime(JacksonUtils.deserializeLong(root, "createTime"))
                .updateTime(JacksonUtils.deserializeLong(root, "updateTime"))
                .deleteHandler(JacksonUtils.deserializeString(root, "deleteHandler"))
                .isIncomplete(JacksonUtils.deserializeBoolean(root, "isIncomplete"));
        Set<AtlanTag> atlanTags =
                JacksonUtils.deserializeObject(client, root, "classifications", new TypeReference<>() {});
        if (atlanTags != null) {
            builder.atlanTags(atlanTags);
        }
        TreeSet<String> meaningNames =
                JacksonUtils.deserializeObject(client, root, "meaningNames", new TypeReference<>() {});
        if (meaningNames != null) {
            builder.meaningNames(meaningNames);
        }
        TreeSet<Meaning> meanings = JacksonUtils.deserializeObject(client, root, "meanings", new TypeReference<>() {});
        if (meanings != null) {
            builder.meanings(meanings);
        }
        TreeSet<String> pendingTasks =
                JacksonUtils.deserializeObject(client, root, "pendingTasks", new TypeReference<>() {});
        if (pendingTasks != null) {
            builder.pendingTasks(pendingTasks);
        }

        Class<?> builderClass = builder.getClass();

        Map<String, JsonNode> leftOverAttributes = new HashMap<>();
        // If the same attribute appears in both 'attributes' and 'relationshipAttributes'
        // then retain the 'relationshipAttributes' (more information) and skip the 'attributes'
        // copy of the same
        Set<String> processedAttributes = new HashSet<>();

        // Only process relationshipAttributes if this is a full asset, not a relationship
        // reference. (If it is a relationship reference, the relationshipGuid will be non-null.)
        if (relationshipGuid == null || relationshipGuid.isNull()) {
            if (relationshipAttributes != null && !relationshipAttributes.isNull()) {
                Iterator<String> itr = relationshipAttributes.fieldNames();
                while (itr.hasNext()) {
                    String relnKey = itr.next();
                    String deserializeName = ReflectionCache.getDeserializedName(assetClass, relnKey);
                    Method method = ReflectionCache.getSetter(builderClass, deserializeName);
                    if (method != null) {
                        try {
                            Object value = deserialize(relationshipAttributes.get(relnKey), method, deserializeName);
                            boolean set = ReflectionCache.setValue(builder, deserializeName, value);
                            if (set) {
                                processedAttributes.add(deserializeName);
                            }
                        } catch (NoSuchMethodException e) {
                            throw new IOException("Missing fromValue method for enum.", e);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new IOException("Failed to deserialize through reflection.", e);
                        }
                    }
                }
            }
        }

        if (attributes != null && !attributes.isNull()) {
            Iterator<String> itr = attributes.fieldNames();
            while (itr.hasNext()) {
                String attrKey = itr.next();
                String deserializeName = ReflectionCache.getDeserializedName(assetClass, attrKey);
                // Only proceed with deserializing the 'attributes' copy of an attribute if
                // it was not already deserialized as a more complete relationship (above)
                if (!processedAttributes.contains(deserializeName)) {
                    Method method = ReflectionCache.getSetter(builderClass, deserializeName);
                    if (method != null) {
                        try {
                            Object value = deserialize(attributes.get(attrKey), method, deserializeName);
                            ReflectionCache.setValue(builder, deserializeName, value);
                        } catch (NoSuchMethodException e) {
                            throw new IOException("Missing fromValue method for enum.", e);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new IOException("Failed to deserialize through reflection.", e);
                        }
                    } else {
                        // If the setter was not found, still retain it for later processing
                        // (this is where custom attributes will end up for search results)
                        leftOverAttributes.put(attrKey, attributes.get(attrKey));
                    }
                }
            }
        }

        // Custom attributes can come from two places, only one of which should ever have data...
        Map<String, CustomMetadataAttributes> cm = null;

        // 1. For search results, they're embedded in `attributes` in the form <cmId>.<attrId>
        if (!leftOverAttributes.isEmpty()) {
            // Translate these into custom metadata structure
            try {
                cm = client.getCustomMetadataCache().getCustomMetadataFromSearchResult(leftOverAttributes);
            } catch (AtlanException e) {
                throw new IOException("Unable to deserialize custom metadata from search result.", e);
            }
        }

        // 2. For asset retrievals, they're all in a `businessAttributes` dict
        if (businessAttributes != null) {
            // Translate these into custom metadata structure
            try {
                cm = client.getCustomMetadataCache().getCustomMetadataFromBusinessAttributes(businessAttributes);
            } catch (AtlanException e) {
                throw new IOException("Unable to deserialize custom metadata.", e);
            }
        }

        Set<String> clsNames = null;
        if (classificationNames != null && classificationNames.isArray()) {
            clsNames = new HashSet<>();
            // Translate these IDs in to human-readable names
            try {
                for (JsonNode element : classificationNames) {
                    String name = client.getAtlanTagCache().getNameForId(element.asText());
                    clsNames.add(name);
                }
            } catch (AtlanException e) {
                throw new IOException("Unable to deserialize Atlan tag name.", e);
            }
        }

        // Special cases to wrap-up:
        // Decode the Readme's description after deserialization
        if (typeName != null && typeName.equals("Readme")) {
            builder.description(StringUtils.decodeContent(builder.build().getDescription()));
        }

        if (cm != null) {
            builder.customMetadataSets(cm);
        }
        if (clsNames != null) {
            builder.atlanTagNames(clsNames);
        }

        Asset result = builder.build();
        result.setRawJsonObject(root);
        return result;
    }

    private Object deserialize(JsonNode jsonNode, Method method, String fieldName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (jsonNode.isValueNode()) {
            return deserializePrimitive(jsonNode, method, fieldName);
        } else if (jsonNode.isArray()) {
            return deserializeList((ArrayNode) jsonNode, method, fieldName);
        } else if (jsonNode.isObject()) {
            return deserializeObject(jsonNode, method);
        }
        return null;
    }

    private Collection<?> deserializeList(ArrayNode array, Method method, String fieldName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        Class<?> paramClass = ReflectionCache.getParameterOfMethod(method);
        List<Object> list = new ArrayList<>();
        for (JsonNode element : array) {
            Object deserialized = deserializeElement(element, method, fieldName);
            list.add(deserialized);
        }
        if (paramClass == Collection.class || paramClass == List.class) {
            return list;
        } else if (paramClass == Set.class || paramClass == SortedSet.class) {
            return new TreeSet<>(list);
        } else {
            throw new IOException("Unable to deserialize JSON list to Java class: " + paramClass.getCanonicalName());
        }
    }

    private Object deserializeObject(JsonNode jsonObject, Method method) {
        Class<?> paramClass = ReflectionCache.getParameterOfMethod(method);
        if (paramClass == Map.class
                && ReflectionCache.getParameterizedTypeOfMethod(method)
                        .getTypeName()
                        .equals("java.util.Map<? extends java.lang.String, ? extends java.lang.Long>")) {
            // TODO: Unclear why this cannot be handled more generically, but nothing else seems to work
            return client.convertValue(jsonObject, new TypeReference<Map<String, Long>>() {});
        } else {
            return client.convertValue(jsonObject, paramClass);
        }
    }

    /**
     * Deserialize a value direct to an object.
     * @param element to deserialize
     * @param method to which the deserialized value will be built into an asset
     * @param fieldName name of the field into which the value is being deserialized
     * @return the deserialized object
     * @throws IOException if an array is found nested directly within another array (unsupported)
     */
    private Object deserializeElement(JsonNode element, Method method, String fieldName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        Type paramType = ReflectionCache.getParameterizedTypeOfMethod(method);
        Class<?> innerClass = ReflectionCache.getClassOfParameterizedType(paramType);
        if (element.isValueNode()) {
            if (fieldName.equals("purposeAtlanTags")) {
                String value;
                try {
                    value = client.getAtlanTagCache().getNameForId(element.asText());
                } catch (NotFoundException e) {
                    value = Serde.DELETED_AUDIT_OBJECT;
                } catch (AtlanException e) {
                    throw new IOException("Unable to deserialize purposeAtlanTags.", e);
                }
                return value;
            }
            return JacksonUtils.deserializePrimitive(element, method, innerClass);
        } else if (element.isArray()) {
            throw new IOException("Directly-nested arrays are not supported.");
        } else if (element.isObject()) {
            return client.convertValue(element, innerClass);
        }
        return null;
    }

    private Object deserializePrimitive(JsonNode primitive, Method method, String fieldName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (primitive.isNull()) {
            // Explicitly deserialize null values to a representation
            // that we can identify on the object — necessary for audit entries
            return Removable.NULL;
        } else {
            Object value = JacksonUtils.deserializePrimitive(primitive, method);
            if (fieldName.equals("mappedAtlanTagName")) {
                try {
                    value = client.getAtlanTagCache().getNameForId(primitive.asText());
                } catch (NotFoundException e) {
                    value = Serde.DELETED_AUDIT_OBJECT;
                } catch (AtlanException e) {
                    throw new IOException("Unable to deserialize mappedAtlanTagName.", e);
                }
            }
            return value;
        }
    }
}
