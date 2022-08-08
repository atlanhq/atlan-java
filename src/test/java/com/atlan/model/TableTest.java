package com.atlan.model;

import static org.testng.Assert.*;

import com.atlan.model.core.Classification;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.Reference;
import com.atlan.net.ApiResource;
import org.testng.annotations.Test;

public class TableTest {

    private static final Table full = Table.builder()
            .guid("guid")
            .displayText("displayText")
            .classification(Classification.of("classificationName1", "guid"))
            .classification(Classification.of("classificationName2", "guid"))
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
            .viewerUser("viewerUser")
            .viewerGroup("viewerGroup")
            .connectorName("connectorName")
            .connectionName("connectionName")
            .connectionQualifiedName("connectionQualifiedName")
            .isDiscoverable(true)
            .isEditable(true)
            .viewScore(123456.0)
            .popularityScore(123456.0)
            .sourceOwners("sourceOwners")
            .sourceURL("sourceURL")
            .lastSyncWorkflowName("lastSyncWorkflowName")
            .lastSyncRunAt(123456789L)
            .lastSyncRun("lastSyncRun")
            .sourceCreatedBy("sourceCreatedBy")
            .sourceCreatedAt(123456789L)
            .sourceUpdatedAt(123456789L)
            .sourceUpdatedBy("sourceUpdatedBy")
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
            .atlanSchema(Reference.to("Schema", "schemaGuid"))
            .column(Reference.to(Column.TYPE_NAME, "columnGuid1"))
            .column(Reference.to(Column.TYPE_NAME, "columnGuid2"))
            .build();

    private static Table frodo;
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
    void deserialization() {
        assertNotNull(serialized);
        frodo = ApiResource.GSON.fromJson(serialized, Table.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(serialized, backAgain, "Serialization is equivalent after serde loop.");
    }

    // TODO: Determine why the deserialized form would differ
    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(full, frodo, "Deserialization is equivalent after serde loop.");
    }
}
