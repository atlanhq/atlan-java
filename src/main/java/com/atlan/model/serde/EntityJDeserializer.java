package com.atlan.model.serde;

import com.atlan.cache.ClassificationCacheJ;
import com.atlan.cache.CustomMetadataCacheJ;
import com.atlan.exception.AtlanException;
import com.atlan.model.*;
import com.atlan.model.core.ClassificationJ;
import com.atlan.model.core.EntityJ;
import com.atlan.net.AtlanObjectJ;
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

public class EntityJDeserializer extends StdDeserializer<EntityJ> {

    private static final long serialVersionUID = 2L;

    public EntityJDeserializer() {
        this(null);
    }

    public EntityJDeserializer(Class<?> t) {
        super(t);
    }

    @Override
    public Object deserializeWithType(
            JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(parser, context);
    }

    @Override
    public EntityJ deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        JsonNode root = parser.getCodec().readTree(parser);
        JsonNode attributes = root.get("attributes");
        JsonNode relationshipAttributes = root.get("relationshipAttributes");
        JsonNode businessAttributes = root.get("businessAttributes");
        JsonNode classificationNames = root.get("classificationNames");

        EntityJ.EntityJBuilder<?, ?> builder;
        String typeName = root.get("typeName").asText();

        // TODO: figure out how to avoid needing to maintain this switch statement
        if (typeName == null) {
            builder = IndistinctAssetJ.builder();
        } else {
            switch (typeName) {
                case ReadmeJ.TYPE_NAME:
                    builder = ReadmeJ.builder();
                    break;
                case GlossaryJ.TYPE_NAME:
                    builder = GlossaryJ.builder();
                    break;
                case GlossaryCategoryJ.TYPE_NAME:
                    builder = GlossaryCategoryJ.builder();
                    break;
                case GlossaryTermJ.TYPE_NAME:
                    builder = GlossaryTermJ.builder();
                    break;
                case ConnectionJ.TYPE_NAME:
                    builder = ConnectionJ.builder();
                    break;
                case TableJ.TYPE_NAME:
                    builder = TableJ.builder();
                    break;
                case ColumnJ.TYPE_NAME:
                    builder = ColumnJ.builder();
                    break;
                case S3BucketJ.TYPE_NAME:
                    builder = S3BucketJ.builder();
                    break;
                case S3ObjectJ.TYPE_NAME:
                    builder = S3ObjectJ.builder();
                    break;
                case LineageProcessJ.TYPE_NAME:
                    builder = LineageProcessJ.builder();
                    break;
                case ColumnProcessJ.TYPE_NAME:
                    builder = ColumnProcessJ.builder();
                    break;
                default:
                    builder = IndistinctAssetJ.builder();
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
        Set<ClassificationJ> classifications =
                JacksonUtils.deserializeObject(root, "classifications", new TypeReference<>() {});
        if (classifications != null) {
            builder = builder.classifications(classifications);
        }
        Set<String> meaningNames = JacksonUtils.deserializeObject(root, "meaningNames", new TypeReference<>() {});
        if (meaningNames != null) {
            builder = builder.meaningNames(meaningNames);
        }

        EntityJ value = builder.build();

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

        Map<String, CustomMetadataAttributesJ> cm = null;
        if (businessAttributes != null) {
            // Translate these into custom metadata structure
            try {
                cm = CustomMetadataCacheJ.getCustomMetadataFromBusinessAttributes(businessAttributes);
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
                    String name = ClassificationCacheJ.getNameForId(element.asText());
                    clsNames.add(name);
                }
            } catch (AtlanException e) {
                throw new IOException("Unable to deserialize classification name.", e);
            }
        }

        // Special cases to wrap-up:
        // Decode the Readme's description after deserialization
        if (typeName != null && typeName.equals("Readme")) {
            ReadmeJ readme = (ReadmeJ) value;
            readme.setDescription(StringUtils.decodeContent(readme.getDescription()));
        }

        value.setCustomMetadataSets(cm);
        value.setClassificationNames(clsNames);

        return value;
    }

    private void deserialize(EntityJ value, JsonNode jsonNode, Method method)
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

    private void deserializeList(EntityJ value, ArrayNode array, Method method)
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

    private void deserializeObject(EntityJ value, JsonNode jsonObject, Method method)
            throws IllegalAccessException, InvocationTargetException, IOException {
        Class<?> paramClass = ReflectionUtils.getParameterOfMethod(method);
        method.invoke(value, AtlanObjectJ.mapper.readValue(jsonObject.toString(), paramClass));
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
            return AtlanObjectJ.mapper.readValue(element.toString(), innerClass);
        }
        return null;
    }

    private void deserializePrimitive(EntityJ value, JsonNode primitive, Method method)
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

    private void deserializeNumber(EntityJ value, JsonNode primitive, Method method)
            throws IllegalAccessException, InvocationTargetException, IOException {
        Object number = JacksonUtils.deserializeNumber(primitive, method);
        method.invoke(value, number);
    }
}
