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
import com.atlan.mock.MockTenant;
import com.atlan.model.assets.S3Object;
import java.io.IOException;
import org.testng.annotations.Test;

public class IndexSearchDSLTest {

    private static final SortOptions sort =
            SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("__timestamp").order(SortOrder.Asc))));
    private static final Query byState =
            TermQuery.of(t -> t.field("__state").value("ACTIVE"))._toQuery();
    private static final Query byType = TermQuery.of(
                    t -> t.field("__typeName.keyword").value(S3Object.TYPE_NAME))
            ._toQuery();
    private static final Query combined =
            BoolQuery.of(b -> b.must(byState).must(byType))._toQuery();

    private static final IndexSearchDSL full =
            IndexSearchDSL.builder(combined).size(10).sortOption(sort).build();

    private static IndexSearchDSL frodo;
    private static String serialized;

    @Test(groups = {"IndexSearchDSL.serialize"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized);
    }

    @Test(
            groups = {"IndexSearchDSL.deserialize"},
            dependsOnGroups = {"IndexSearchDSL.serialize"})
    void deserialization() throws IOException {
        assertNotNull(serialized);
        frodo = MockTenant.client.readValue(serialized, IndexSearchDSL.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"IndexSearchDSL.equivalency"},
            dependsOnGroups = {"IndexSearchDSL.serialize", "IndexSearchDSL.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    // TODO: deserialized equivalency appears problematic for underlying Elastic
    //  objects
}
