/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.model.core.Classification;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Utility class for static objects used for serialization and deserialization.
 */
public class Serde {

    private static final Set<Module> modules = createModules();

    public static final ObjectMapper mapper = createMapper();

    private static Set<Module> createModules() {
        Set<Module> set = new LinkedHashSet<>();
        // Elastic serializers
        SimpleModule elastic = new SimpleModule()
                .addSerializer(Aggregation.class, new ElasticObjectSerializer<>())
                .addSerializer(Query.class, new ElasticObjectSerializer<>())
                .addSerializer(SortOptions.class, new ElasticObjectSerializer<>());
        set.add(elastic);
        // Classification translators
        SimpleModule clsSerde = new SimpleModule()
                .addDeserializer(Classification.class, new ClassificationDeserializer())
                .setSerializerModifier(new ClassificationBeanSerializerModifier());
        set.add(clsSerde);
        return set;
    }

    private static ObjectMapper createMapper() {
        ObjectMapper om = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        for (Module m : modules) {
            om.registerModule(m);
        }
        return om;
    }
}
