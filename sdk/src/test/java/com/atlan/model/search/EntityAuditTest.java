/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.model.search;

import static org.testng.Assert.*;

import com.atlan.mock.MockAtlanTenant;
import com.atlan.mock.MockTenant;
import com.atlan.model.enums.AuditActionType;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class EntityAuditTest {

    @BeforeClass
    void init() throws InterruptedException {
        MockAtlanTenant.initializeClient();
    }

    @DataProvider
    Object[][] auditActions() {
        return new Object[][] {
            {"LABEL_ADD", AuditActionType.LABEL_ADD},
            {"LABEL_DELETE", AuditActionType.LABEL_DELETE},
            {"ENTITY_PURGE", AuditActionType.ENTITY_PURGE},
            {"CUSTOM_ATTRIBUTE_UPDATE", AuditActionType.CUSTOM_ATTRIBUTE_UPDATE},
        };
    }

    @Test(dataProvider = "auditActions")
    void deserializeAuditAction(String raw, AuditActionType expected) throws IOException {
        String json = "{\"entityQualifiedName\":\"default/glossary/category\","
                + "\"typeName\":\"AtlasGlossaryCategory\","
                + "\"entityId\":\"00000000-0000-0000-0000-000000000000\","
                + "\"timestamp\":1790000000000,"
                + "\"created\":1790000000000,"
                + "\"user\":\"example-user\","
                + "\"action\":\"" + raw + "\","
                + "\"eventKey\":\"00000000-0000-0000-0000-000000000000:1790000000000\"}";
        EntityAudit audit = MockTenant.client.readValue(json, EntityAudit.class);
        assertNotNull(audit, "Unable to deserialize an audit entry with action " + raw + ",");
        assertEquals(audit.getAction(), expected);
        assertEquals(AuditActionType.fromValue(raw), expected);
    }
}
