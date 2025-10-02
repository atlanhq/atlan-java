/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.atlan.mock.MockAtlanTenant;
import com.atlan.mock.MockTenant;
import com.atlan.model.assets.S3Object;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class IndexSearchDSLTest {

    private final SortOptions sort =
            SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("__timestamp").order(SortOrder.Asc))));
    private final Query byState =
            TermQuery.of(t -> t.field("__state").value("ACTIVE"))._toQuery();
    private final Query byType = TermQuery.of(t -> t.field("__typeName.keyword").value(S3Object.TYPE_NAME))
            ._toQuery();
    private final Query combined =
            BoolQuery.of(b -> b.must(byState).must(byType))._toQuery();

    private final IndexSearchDSL full =
            IndexSearchDSL.builder(combined).size(10).sortOption(sort).build();

    @BeforeClass
    void init() throws InterruptedException {
        MockAtlanTenant.initializeClient();
    }

    @Test
    void serdeCycleIndexSearchDSL() throws IOException {
        assertNotNull(full, "Unable to build sample instance of IndexSearchDSL,");
        final int hash = full.hashCode();
        // Builder equivalency
        assertEquals(
                full.toBuilder().build(),
                full,
                "Unable to converting IndexSearchDSL via builder back to its original state,");
        // Serialization
        final String serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized, "Unable to serialize sample instance of IndexSearchDSL,");
        assertEquals(full.hashCode(), hash, "Serialization mutated the original value,");
        // Deserialization
        final IndexSearchDSL frodo = MockTenant.client.readValue(serialized, IndexSearchDSL.class);
        assertNotNull(frodo, "Unable to reverse-read serialized value back into an instance of IndexSearchDSL,");
        // Serialization equivalency
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
        // TODO: deserialized equivalency appears problematic for underlying Elastic objects
        // assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
