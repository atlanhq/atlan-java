/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.net;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.model.core.AtlanRawJsonObject;
import com.atlan.model.core.AtlanRawJsonObjectDeserializer;
import com.atlan.model.serde.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public abstract class AtlanObject {

    public AtlanObject() {
        // Do nothing - needed for Lombok SuperBuilder generations...
    }

    public static final Gson GSON = createGson();

    private static Gson createGson() {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(AtlanRawJsonObject.class, new AtlanRawJsonObjectDeserializer())
                .registerTypeAdapter(Aggregation.class, new ElasticObjectSerializer<Aggregation>())
                .registerTypeAdapter(Query.class, new ElasticObjectSerializer<Query>())
                .registerTypeAdapter(SortOptions.class, new ElasticObjectSerializer<SortOptions>());
        for (TypeAdapterFactory factory : ApiResourceTypeAdapterFactoryProvider.getAll()) {
            // Keep an eye on these, as they may impact search vs heavily-inherited (with transient
            // override) structures
            builder.registerTypeAdapterFactory(factory);
        }
        return builder.create();
    }

    @Override
    public String toString() {
        return String.format(
                "<%s@%s> JSON: %s", this.getClass().getName(), System.identityHashCode(this), this.toJson());
    }

    public String toJson() {
        return GSON.toJson(this);
    }
}
