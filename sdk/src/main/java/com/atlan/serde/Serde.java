/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.atlan.AtlanClient;
import com.atlan.model.assets.Asset;
import com.atlan.model.structs.AtlanStruct;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
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

    private static final Map<String, JsonDeserializer<?>> deserializerCache = new ConcurrentHashMap<>();
    private static final Map<String, Class<?>> assetClasses = scanAssets();

    private static Map<String, Class<?>> scanAssets() {
        Map<String, Class<?>> assetMap = new HashMap<>();
        try (ScanResult scanResult = new ClassGraph().enableExternalClasses().scan()) {
            for (ClassInfo info : scanResult.getSubclasses(Asset.class)) {
                String fullName = info.getName();
                try {
                    Class<?> typeClass = Class.forName(fullName);
                    String typeName = (String) typeClass.getField("TYPE_NAME").get(null);
                    assetMap.put(typeName, typeClass);
                } catch (ClassNotFoundException e) {
                    log.error("Unable to load class: {}", fullName, e);
                } catch (NoSuchFieldException e) {
                    log.error("Asset class is missing the static TYPE_NAME giving its type: {}", fullName, e);
                } catch (IllegalAccessException e) {
                    log.error("Unable to access the static TYPE_NAME for the asset class: {}", fullName, e);
                }
            }
        }
        return Collections.unmodifiableMap(assetMap);
    }

    public static Class<?> getAssetClassForType(String typeName) throws ClassNotFoundException {
        Class<?> result = assetClasses.getOrDefault(typeName, null);
        if (result != null) {
            return result;
        } else {
            throw new ClassNotFoundException("Unable to find asset class for typeName: " + typeName);
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
}
