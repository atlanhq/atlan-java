/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.atlan.AtlanClient;
import com.atlan.cache.ReflectionCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.Asset;
import com.atlan.model.relations.RelationshipAttributes;
import com.atlan.model.search.AggregationResult;
import com.atlan.model.structs.AtlanStruct;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for static objects used for serialization and deserialization.
 */
@Slf4j
public class Serde {

    public static final String DELETED_AUDIT_OBJECT = "(DELETED)";

    private static final Set<Module> SIMPLE_MODULES = createModules();

    /** Singular ObjectMapper through which to (de-)serialize raw POJOs, including all details, untranslated. */
    public static final ObjectMapper allInclusiveMapper =
            new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);

    /** JSONP mapper through which to do Jackson-based (de-)serialization of Elastic objects. */
    static final JsonpMapper jsonpMapper = new JacksonJsonpMapper();

    /** Singular ObjectMapper through which to (de-)serialize raw POJOs and YAML. */
    public static final ObjectMapper yamlMapper = createMapperYAML();

    private static final Map<String, JsonDeserializer<?>> deserializerCache = new ConcurrentHashMap<>();
    private static final Map<String, Class<?>> assetClasses;
    private static final Map<String, Class<?>> builderClasses;
    private static final Map<String, Class<?>> relationshipAttributeClasses;

    static {
        Map<String, Class<?>> assetMap = new HashMap<>();
        Map<String, Class<?>> builderMap = new HashMap<>();
        Map<String, Class<?>> relationshipAttributesMap = new HashMap<>();
        try (ScanResult scanResult = new ClassGraph()
                .enableExternalClasses()
                .ignoreClassVisibility()
                .setMaxBufferedJarRAMSize(16 * 1024 * 1024)
                .scan()) {
            for (ClassInfo info : scanResult.getSubclasses(Asset.AssetBuilder.class)) {
                String fullName = info.getName();
                if (fullName.endsWith("Impl")) {
                    try {
                        Class<?> builderClass = info.loadClass();
                        Class<?> typeClass = builderClass.getEnclosingClass();
                        String typeName =
                                (String) typeClass.getDeclaredField("TYPE_NAME").get(null);
                        assetMap.put(typeName, typeClass);
                        builderMap.put(typeName, builderClass);
                    } catch (NoSuchFieldException e) {
                        log.debug(
                                "Asset class is missing the static TYPE_NAME giving its type (this is fine if this is a relationship): {}",
                                fullName);
                    } catch (IllegalAccessException e) {
                        log.error("Unable to access the static TYPE_NAME for the asset class: {}", fullName, e);
                    }
                }
            }
            for (ClassInfo info :
                    scanResult.getSubclasses(RelationshipAttributes.RelationshipAttributesBuilder.class)) {
                String fullName = info.getName();
                if (fullName.endsWith("Impl")) {
                    try {
                        Class<?> builderClass = info.loadClass();
                        Class<?> typeClass = builderClass.getEnclosingClass();
                        String typeName =
                                (String) typeClass.getField("TYPE_NAME").get(null);
                        relationshipAttributesMap.put(typeName, typeClass);
                        builderMap.put(typeName, builderClass);
                    } catch (NoSuchFieldException e) {
                        log.error(
                                "Relationship attributes class is missing the static TYPE_NAME giving its type: {}",
                                fullName,
                                e);
                    } catch (IllegalAccessException e) {
                        log.error(
                                "Unable to access the static TYPE_NAME for the relationship attributes class: {}",
                                fullName,
                                e);
                    }
                }
            }
        }
        assetClasses = Collections.unmodifiableMap(assetMap);
        builderClasses = Collections.unmodifiableMap(builderMap);
        relationshipAttributeClasses = Collections.unmodifiableMap(relationshipAttributesMap);
    }

    public static Class<?> getAssetClassForType(String typeName) throws ClassNotFoundException {
        Class<?> result = assetClasses.getOrDefault(typeName, null);
        if (result != null) {
            return result;
        } else {
            throw new ClassNotFoundException("Unable to find asset class for typeName: " + typeName);
        }
    }

    public static Class<?> getRelationshipAttributesClassForType(String typeName) throws ClassNotFoundException {
        Class<?> result = relationshipAttributeClasses.getOrDefault(typeName, null);
        if (result != null) {
            return result;
        } else {
            throw new ClassNotFoundException("Unable to find relationship attributes class for typeName: " + typeName);
        }
    }

    public static Class<?> getBuilderClassForType(String typeName) throws ClassNotFoundException {
        Class<?> result = builderClasses.getOrDefault(typeName, null);
        if (result != null) {
            return result;
        } else {
            throw new ClassNotFoundException("Unable to find builder class for typeName: " + typeName);
        }
    }

    private static Set<Module> createModules() {
        Set<Module> set = new LinkedHashSet<>();
        // Elastic serializers
        SimpleModule elastic = new SimpleModule()
                .addSerializer(Aggregation.class, new ElasticObjectSerializer<>())
                .addDeserializer(Aggregation.class, new ElasticAggregationDeserializer())
                .addSerializer(Query.class, new ElasticObjectSerializer<>())
                .addDeserializer(Query.class, new ElasticQueryDeserializer())
                .addSerializer(SortOptions.class, new ElasticObjectSerializer<>())
                .addDeserializer(SortOptions.class, new ElasticSortOptionsDeserializer());
        set.add(elastic);
        // Struct deserializers - unwrap the outer `attributes` that appears in responses
        SimpleModule structs = new SimpleModule().setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(
                    DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                Class<?> enclosing = beanDesc.getBeanClass().getEnclosingClass();
                if (enclosing != null && enclosing.getSuperclass() == AtlanStruct.class) {
                    if (!deserializerCache.containsKey(beanDesc.getBeanClass().getCanonicalName())) {
                        deserializerCache.put(
                                beanDesc.getBeanClass().getCanonicalName(), new StructDeserializer(deserializer));
                    }
                    return deserializerCache.get(beanDesc.getBeanClass().getCanonicalName());
                }
                return deserializer;
            }
        });
        set.add(structs);
        SimpleModule aggResults =
                new SimpleModule().addDeserializer(AggregationResult.class, new AggregationResultDeserializer(null));
        set.add(aggResults);
        return set;
    }

    /**
     * Set up the tenant-specific serialization and deserialization.
     * @param client providing connectivity to a specific Atlan tenant
     * @return an ObjectMapper for the tenant-specific transformations
     */
    public static ObjectMapper createMapper(AtlanClient client) {
        // Set default options, using client-aware deserialization
        ObjectMapper om = new ObjectMapper(
                        null,
                        null,
                        new ClientAwareDeserializationContext(BeanDeserializerFactory.instance, null, client))
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Set standard (non-tenant-specific) modules
        for (Module m : SIMPLE_MODULES) {
            om.registerModule(m);
        }
        // Set client-aware serialization
        ClientAwareSerializerProvider casp = new ClientAwareSerializerProvider(client);
        om.setSerializerProvider(casp);
        return om;
    }

    /**
     * Set up the serialization and deserialization of tenant-agnostic YAML.
     * @return an ObjectMapper for tenant-agnostic YAML transformations
     */
    public static ObjectMapper createMapperYAML() {
        // Set default options, using client-aware deserialization
        ObjectMapper om = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID))
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        // Set standard (non-tenant-specific) modules
        for (Module m : SIMPLE_MODULES) {
            om.registerModule(m);
        }
        return om;
    }

    /**
     * Deserialize a value direct to an object.
     *
     * @param client connectivity to the Atlan tenant
     * @param jsonNode to deserialize
     * @param method to which the deserialized value will be built into an asset
     * @param fieldName name of the field into which the value is being deserialized
     * @return the deserialized object
     * @throws IOException if any unexpected (or unsupported) deserialization is requested
     */
    static Object deserialize(AtlanClient client, JsonNode jsonNode, Method method, String fieldName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (jsonNode.isValueNode()) {
            return deserializePrimitive(client, jsonNode, method, fieldName);
        } else if (jsonNode.isArray()) {
            return deserializeList(client, (ArrayNode) jsonNode, method, fieldName);
        } else if (jsonNode.isObject()) {
            return deserializeObject(client, jsonNode, method);
        }
        return null;
    }

    /**
     * Deserialize a value direct to a collection.
     *
     * @param client connectivity to the Atlan tenant
     * @param array to deserialize
     * @param method to which the deserialized value will be built into an asset
     * @param fieldName name of the field into which the value is being deserialized
     * @return the deserialized collection
     * @throws IOException if there is any unexpected error deserializing the array to a known Java object representation
     */
    static Collection<?> deserializeList(AtlanClient client, ArrayNode array, Method method, String fieldName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        Class<?> paramClass = ReflectionCache.getParameterOfMethod(method);
        List<Object> list = new ArrayList<>();
        for (JsonNode element : array) {
            Object deserialized = deserializeElement(client, element, method, fieldName);
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

    /**
     * Deserialize a value direct to an object.
     *
     * @param client connectivity to the Atlan tenant
     * @param jsonObject to deserialize
     * @param method to which the deserialized value will be built into an asset
     * @return the deserialized object
     */
    static Object deserializeObject(AtlanClient client, JsonNode jsonObject, Method method) {
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
     *
     * @param client connectivity to the Atlan tenant
     * @param element to deserialize
     * @param method to which the deserialized value will be built into an asset
     * @param fieldName name of the field into which the value is being deserialized
     * @return the deserialized object
     * @throws IOException if an array is found nested directly within another array (unsupported)
     */
    static Object deserializeElement(AtlanClient client, JsonNode element, Method method, String fieldName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        Type paramType = ReflectionCache.getParameterizedTypeOfMethod(method);
        Class<?> innerClass = ReflectionCache.getClassOfParameterizedType(paramType);
        if (element.isValueNode()) {
            if (fieldName.equals("purposeAtlanTags")) {
                String value;
                try {
                    value = client.getAtlanTagCache().getNameForSid(element.asText());
                } catch (NotFoundException e) {
                    value = Serde.DELETED_AUDIT_OBJECT;
                } catch (AtlanException e) {
                    throw new IOException("Unable to deserialize purposeAtlanTags.", e);
                }
                if (value == null) {
                    value = Serde.DELETED_AUDIT_OBJECT;
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

    /**
     * Deserialize a primitive value direct to an object.
     *
     * @param client connectivity to the Atlan tenant
     * @param primitive to deserialize
     * @param method to which the deserialized value will be built into an asset
     * @param fieldName name of the field into which the value is being deserialized
     * @return the deserialized primitive
     * @throws IOException if there is any unexpected error deserializing the name of an Atlan tag
     */
    static Object deserializePrimitive(AtlanClient client, JsonNode primitive, Method method, String fieldName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (primitive.isNull()) {
            // Explicitly deserialize null values to a representation
            // that we can identify on the object — necessary for audit entries
            return Removable.NULL;
        } else {
            Object value = JacksonUtils.deserializePrimitive(primitive, method);
            if (fieldName.equals("mappedAtlanTagName")) {
                try {
                    value = client.getAtlanTagCache().getNameForSid(primitive.asText());
                } catch (NotFoundException e) {
                    value = Serde.DELETED_AUDIT_OBJECT;
                } catch (AtlanException e) {
                    throw new IOException("Unable to deserialize mappedAtlanTagName.", e);
                }
                if (value == null) {
                    value = Serde.DELETED_AUDIT_OBJECT;
                }
            }
            return value;
        }
    }
}
