/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.mock.MockTenant;
import com.atlan.model.assets.GlossaryTerm;
import com.atlan.model.enums.*;
import java.io.IOException;
import org.testng.annotations.Test;

public class AtlanRequestTest {

    private static final AtlanRequest full = AttributeRequest.builder()
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

    private static AtlanRequest frodo;
    private static String serialized;
    private static final int HASH = full.hashCode();

    @Test(groups = {"AtlanRequest.serialize"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized);
        assertEquals(full.hashCode(), HASH, "Object is mutated by serialization.");
    }

    @Test(
            groups = {"AtlanRequest.deserialize"},
            dependsOnGroups = {"AtlanRequest.serialize"})
    void deserialization() throws IOException {
        assertNotNull(serialized);
        frodo = MockTenant.client.readValue(serialized, AtlanRequest.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"AtlanRequest.equivalency"},
            dependsOnGroups = {"AtlanRequest.serialize", "AtlanRequest.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"AtlanRequest.equivalency"},
            dependsOnGroups = {"AtlanRequest.serialize", "AtlanRequest.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
