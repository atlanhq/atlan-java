/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.mock.MockAtlanTenant;
import com.atlan.mock.MockTenant;
import com.atlan.model.assets.GlossaryTerm;
import com.atlan.model.enums.*;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AtlanRequestTest {

    private final AttributeRequest full = AttributeRequest.builder()
            .id("id")
            .version("version")
            .isActive(true)
            .createdAt(123456789L)
            .updatedAt(123456789L)
            .createdBy("createdBy")
            .tenantId("tenantId")
            .sourceGuid("sourceGuid")
            .sourceQualifiedName("sourceQualifiedName")
            .sourceAttribute("sourceAttribute")
            .destinationGuid("destinationGuid")
            .destinationQualifiedName("destinationQualifiedName")
            .destinationAttribute("destinationAttribute")
            .destinationValue("destinationValue")
            .destinationValueType("destinationValueType")
            // .destinationValueArray()
            // .destinationValueObject()
            .entityType(GlossaryTerm.TYPE_NAME)
            // .confidenceScore()
            // .botRunId()
            .approvedBy("approvedBy")
            .rejectedBy("rejectedBy")
            .status(AtlanRequestStatus.PENDING)
            .message("message")
            // .requestsBatch()
            .approvalType("single")
            // .assignedApprovers()
            // .payload()
            // .accessStartDate()
            // .accessEndDate()
            .hash(123456L)
            .isDuplicate(false)
            .destinationValueAction("destinationValueAction")
            .requestApproverUser("user1")
            .requestApproverUser("user2")
            .requestApproverGroup("group1")
            .requestApproverGroup("group2")
            .requestApproverRole("role1")
            .requestApproverRole("role2")
            .requestDenyUser("user1")
            .requestDenyUser("user2")
            .requestDenyGroup("group1")
            .requestDenyGroup("group2")
            .requestDenyRole("role1")
            .requestDenyRole("role2")
            // .sourceEntity()
            .destinationEntity(GlossaryTerm._internal()
                    .name("name")
                    .guid("guid")
                    .displayName("displayName")
                    .build())
            .build();

    @BeforeClass
    void init() throws InterruptedException {
        MockAtlanTenant.initializeClient();
    }

    @Test
    void serdeCycleAtlanRequest() throws IOException {
        assertNotNull(full, "Unable to build sample instance of AtlanRequest,");
        final int hash = full.hashCode();
        // Builder equivalency
        assertEquals(
                full.toBuilder().build(),
                full,
                "Unable to converting AtlanRequest via builder back to its original state,");
        // Serialization
        final String serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized, "Unable to serialize sample instance of AtlanRequest,");
        assertEquals(full.hashCode(), hash, "Serialization mutated the original value,");
        // Deserialization
        final AtlanRequest frodo = MockTenant.client.readValue(serialized, AtlanRequest.class);
        assertNotNull(frodo, "Unable to reverse-read serialized value back into an instance of AtlanRequest,");
        // Serialization equivalency
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
        // Deserialization equivalency
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
