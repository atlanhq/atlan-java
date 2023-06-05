/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import static org.testng.Assert.*;

import com.atlan.model.enums.PlaybookActionOperator;
import com.atlan.model.enums.PlaybookActionType;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.serde.Serde;
import com.atlan.util.QueryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.Test;

public class PlaybookRuleTest {

    private static final PlaybookRule full = PlaybookRule.builder()
            .name("name")
            .config(PlaybookRuleConfig.builder()
                    .query(IndexSearchRequest.builder()
                            .dsl(IndexSearchDSL.builder()
                                    .from(0)
                                    .query(QueryFactory.beActive())
                                    .build())
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
    private static PlaybookRule frodo;
    private static String serialized;

    @Test(groups = {"PlaybookRule.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"PlaybookRule.serialize"},
            dependsOnGroups = {"PlaybookRule.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"PlaybookRule.deserialize"},
            dependsOnGroups = {"PlaybookRule.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, PlaybookRule.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"PlaybookRule.equivalency"},
            dependsOnGroups = {"PlaybookRule.serialize", "PlaybookRule.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    // TODO: deserialized equivalency appears problematic for underlying Elastic
    //  objects
}
