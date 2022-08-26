package com.atlan.model;

import static org.testng.Assert.*;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.ReferenceJ;
import com.atlan.net.ApiResourceJ;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.Test;

public class TableJTest {

    private static final TableJ full = TableJ.builder()
            .guid("guid")
            .displayText("displayText")
            .status(AtlanStatus.ACTIVE)
            .createdBy("createdBy")
            .updatedBy("updatedBy")
            .createTime(123456789L)
            .updateTime(123456789L)
            .isIncomplete(false)
            .qualifiedName("qualifiedName")
            .name("name")
            .displayName("displayName")
            .description("description")
            .userDescription("userDescription")
            .tenantId("tenantId")
            .certificateStatus(AtlanCertificateStatus.VERIFIED)
            .certificateStatusMessage("certificateStatusMessage")
            .certificateUpdatedBy("certificateUpdatedBy")
            .certificateUpdatedAt(123456789L)
            .announcementTitle("announcementTitle")
            .announcementMessage("announcementMessage")
            .announcementUpdatedAt(123456789L)
            .announcementUpdatedBy("announcementUpdatedBy")
            .announcementType(AtlanAnnouncementType.INFORMATION)
            .ownerUser("ownerUser")
            .ownerGroup("ownerGroup")
            .adminUser("adminUser")
            .adminGroup("adminGroup")
            .adminRole("adminRole")
            .viewerUser("viewerUser")
            .viewerGroup("viewerGroup")
            .connectorName("connectorName")
            .connectionName("connectionName")
            .connectionQualifiedName("connectionQualifiedName")
            .__hasLineage(false)
            .isDiscoverable(true)
            .isEditable(true)
            .viewScore(123456.0)
            .popularityScore(123456.0)
            .sourceOwners("sourceOwners")
            .sourceURL("sourceURL")
            .sourceEmbedURL("sourceEmbedURL")
            .lastSyncWorkflowName("lastSyncWorkflowName")
            .lastSyncRunAt(123456789L)
            .lastSyncRun("lastSyncRun")
            .sourceCreatedBy("sourceCreatedBy")
            .sourceCreatedAt(123456789L)
            .sourceUpdatedAt(123456789L)
            .sourceUpdatedBy("sourceUpdatedBy")
            .link(ReferenceJ.to("Resource", "linkGuid1"))
            .link(ReferenceJ.to("Resource", "linkGuid2"))
            .readme(ReferenceJ.to(ReadmeJ.TYPE_NAME, "readmeGuid"))
            .meaning(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid1"))
            .meaning(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid2"))
            .queryCount(123L)
            .queryUserCount(123L)
            .queryCountUpdatedAt(123456789L)
            .databaseName("databaseName")
            .databaseQualifiedName("databaseQualifiedName")
            .schemaName("schemaName")
            .schemaQualifiedName("schemaQualifiedName")
            .tableName("tableName")
            .tableQualifiedName("tableQualifiedName")
            .viewName("viewName")
            .viewQualifiedName("viewQualifiedName")
            .columnCount(123L)
            .rowCount(1234567890L)
            .sizeBytes(1234567890L)
            .alias("alias")
            .isTemporary(false)
            .isQueryPreview(true)
            .externalLocation("externalLocation")
            .externalLocationRegion("externalLocationRegion")
            .externalLocationFormat("externalLocationFormat")
            .isPartitioned(true)
            .partitionStrategy("partitionStrategy")
            .partitionCount(12L)
            .partitionList("partitionList")
            .atlanSchema(ReferenceJ.to("Schema", "schemaGuid"))
            .column(ReferenceJ.to(ColumnJ.TYPE_NAME, "columnGuid1"))
            .column(ReferenceJ.to(ColumnJ.TYPE_NAME, "columnGuid2"))
            .build();

    private static TableJ frodo;
    private static String serialized;

    @Test(groups = {"serialize"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"deserialize"},
            dependsOnGroups = {"serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = ApiResourceJ.mapper.readValue(serialized, TableJ.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is equivalent after serde loop.");
    }

    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is equivalent after serde loop.");
    }
}
