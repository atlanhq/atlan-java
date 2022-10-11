/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
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

    /** Singular ObjectMapper through which to do Jackson-based (de-)serialization. */
    public static final ObjectMapper mapper = createMapper();

    /** Singular ObjectMapper through which to (de-)serialize native POJOs, including all details. */
    public static final ObjectMapper allInclusiveMapper =
            new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);

    /** JSONP mapper through which to do Jackson-based (de-)serialization of Elastic objects. */
    static final JsonpMapper jsonpMapper = new JacksonJsonpMapper();

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
