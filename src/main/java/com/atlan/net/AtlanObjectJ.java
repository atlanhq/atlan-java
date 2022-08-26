/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.net;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.model.serde.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public abstract class AtlanObjectJ {

    public AtlanObjectJ() {
        // Do nothing - needed for Lombok SuperBuilder generations...
    }

    private static final Set<Module> modules = createModules();

    public static final ObjectMapper mapper = createMapper();

    private static Set<Module> createModules() {
        Set<Module> set = new LinkedHashSet<>();
        // Elastic serializers
        SimpleModule elastic = new SimpleModule()
                .addSerializer(Aggregation.class, new ElasticObjectJSerializer<>())
                .addSerializer(Query.class, new ElasticObjectJSerializer<>())
                .addSerializer(SortOptions.class, new ElasticObjectJSerializer<>());
        set.add(elastic);
        // Classification translators
        SimpleModule clsSerde = new SimpleModule()
                .setDeserializerModifier(new ClassificationJBeanDeserializerModifier())
                .setSerializerModifier(new ClassificationJBeanSerializerModifier());
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

    @Override
    public String toString() {
        return String.format(
                "<%s@%s> JSON: %s", this.getClass().getName(), System.identityHashCode(this), this.toJson());
    }

    public String toJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Unable to serialize this object: {}", this.getClass().getName(), e);
        }
        return null;
    }
}
