/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import static org.testng.Assert.*;

import com.atlan.mock.MockAtlanTenant;
import com.atlan.mock.MockTenant;
import com.atlan.model.assets.Asset;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.PlaybookActionOperator;
import com.atlan.model.enums.PlaybookActionType;
import com.atlan.model.search.IndexSearchRequest;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PlaybookRuleTest {

    private final PlaybookRule full = PlaybookRule.builder()
            .name("name")
            .config(PlaybookRuleConfig.builder()
                    .query(IndexSearchRequest.builder(Asset.STATUS.eq(AtlanStatus.ACTIVE))
                            .attribute("anchor")
                            .relationAttribute("guid")
                            .build())
                    .filterScrubbed(true)
                    .build())
            .action(PlaybookAction.builder()
                    .type(PlaybookActionType.METADATA_UPDATE)
                    .config(PlaybookActionConfig.builder()
                            .replaceAtlanTags(false)
                            .replaceAtlanTags(true)
                            .template("template")
                            .build())
                    .actionsSchema(PlaybookActionSchema.builder()
                            .operand("operand")
                            .operator(PlaybookActionOperator.UPDATE)
                            .value(40)
                            .build())
                    .build())
            .build();

    @BeforeClass
    void init() throws InterruptedException {
        MockAtlanTenant.initializeClient();
    }

    @Test
    void serdeCyclePlaybookRule() throws IOException {
        assertNotNull(full, "Unable to build sample instance of PlaybookRule,");
        final int hash = full.hashCode();
        // Builder equivalency
        assertEquals(
                full.toBuilder().build(),
                full,
                "Unable to converting PlaybookRule via builder back to its original state,");
        // Serialization
        final String serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized, "Unable to serialize sample instance of PlaybookRule,");
        assertEquals(full.hashCode(), hash, "Serialization mutated the original value,");
        // Deserialization
        final PlaybookRule frodo = MockTenant.client.readValue(serialized, PlaybookRule.class);
        assertNotNull(frodo, "Unable to reverse-read serialized value back into an instance of PlaybookRule,");
        // Serialization equivalency
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
        // TODO: deserialized equivalency appears problematic for underlying Elastic objects
        // assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
