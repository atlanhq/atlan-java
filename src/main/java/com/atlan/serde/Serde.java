/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.atlan.model.assets.Asset;
import com.atlan.model.structs.AtlanStruct;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for static objects used for serialization and deserialization.
 */
@Slf4j
public class Serde {

    public static final String DELETED_AUDIT_OBJECT = "(DELETED)";

    private static final Set<Module> modules = createModules();

    /** Singular ObjectMapper through which to do Jackson-based (de-)serialization. */
    public static final ObjectMapper mapper = createMapper();

    /** Singular ObjectMapper through which to (de-)serialize native POJOs, including all details. */
    public static final ObjectMapper allInclusiveMapper =
            new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);

    /** JSONP mapper through which to do Jackson-based (de-)serialization of Elastic objects. */
    static final JsonpMapper jsonpMapper = new JacksonJsonpMapper();

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
                    return new StructDeserializer(deserializer);
                }
                return deserializer;
            }
        });
        set.add(structs);
        return set;
    }

    private static ObjectMapper createMapper() {
        ObjectMapper om = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        for (Module m : modules) {
            om.registerModule(m);
        }
        return om;
    }
}
