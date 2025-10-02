/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.events;

import static org.testng.Assert.*;

import com.atlan.mock.MockAtlanTenant;
import com.atlan.mock.MockTenant;
import com.atlan.model.assets.Glossary;
import com.atlan.model.assets.GlossaryTerm;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AssetCreateTest {

    private final AtlanEvent full = AtlanEvent.builder()
            .msgSourceIP("msgSourceIP")
            .msgCreatedBy("msgCreatedBy")
            .msgCreationTime(123456789L)
            .payload(AssetCreatePayload.builder()
                    .asset(GlossaryTerm._internal()
                            .guid("guid1")
                            .qualifiedName("qualifiedName")
                            .anchor(Glossary.refByGuid("guid2"))
                            .build())
                    .build())
            .build();

    @BeforeClass
    void init() throws InterruptedException {
        MockAtlanTenant.initializeClient();
    }

    @Test
    void serdeCycleAssetCreateEvent() throws IOException {
        assertNotNull(full, "Unable to build sample instance of AssetCreateEvent,");
        final int hash = full.hashCode();
        // Builder equivalency
        assertEquals(
                full.toBuilder().build(),
                full,
                "Unable to converting AssetCreateEvent via builder back to its original state,");
        // Serialization
        final String serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized, "Unable to serialize sample instance of AssetCreateEvent,");
        assertEquals(full.hashCode(), hash, "Serialization mutated the original value,");
        // Deserialization
        final AtlanEvent frodo = MockTenant.client.readValue(serialized, AtlanEvent.class);
        assertNotNull(frodo, "Unable to reverse-read serialized value back into an instance of AssetCreateEvent,");
        // Serialization equivalency
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
        // Deserialization equivalency
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
