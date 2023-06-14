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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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

    private static final Map<String, JsonDeserializer<?>> deserializerCache = new ConcurrentHashMap<>();

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
                        log.info("Caching new struct deserializer for: {}", enclosing.getCanonicalName());
                        deserializerCache.put(
                                beanDesc.getBeanClass().getCanonicalName(), new StructDeserializer(deserializer));
                    }
                    return deserializerCache.get(beanDesc.getBeanClass().getCanonicalName());
                }
                return deserializer;
            }
        });
        set.add(structs);
        SimpleModule assets = new SimpleModule().setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(
                    DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                Class<?> enclosing = beanDesc.getBeanClass().getEnclosingClass();
                // Enclosing class because it's actually the (embedded) builder we'll see
                if (inheritsFromAsset(enclosing)) {
                    if (!deserializerCache.containsKey(beanDesc.getBeanClass().getCanonicalName())) {
                        log.info("Caching new asset deserializer for: {}", enclosing.getCanonicalName());
                        deserializerCache.put(
                                beanDesc.getBeanClass().getCanonicalName(), new AssetDeserializerV2(deserializer));
                    }
                    return deserializerCache.get(beanDesc.getBeanClass().getCanonicalName());
                }
                return deserializer;
            }
        });
        set.add(assets);
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

    private static boolean inheritsFromAsset(Class<?> clazz) {
        if (clazz == null) {
            return false;
        } else if (clazz == Asset.class) {
            return true;
        } else {
            return inheritsFromAsset(clazz.getSuperclass());
        }
    }
}
