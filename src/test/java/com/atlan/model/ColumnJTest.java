package com.atlan.model;

import static org.testng.Assert.*;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.ReferenceJ;
import com.atlan.net.ApiResourceJ;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.Test;

public class ColumnJTest {

    private static final ColumnJ full = ColumnJ.builder()
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
            .readme(ReferenceJ.to("Readme", "readmeGuid"))
            .meaning(ReferenceJ.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .meaning(ReferenceJ.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
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
            .dataType("dataType")
            .subDataType("subDataType")
            .order(1)
            .isPartition(false)
            .partitionOrder(1)
            .isClustered(true)
            .isPrimary(true)
            .isForeign(false)
            .isIndexed(true)
            .isSort(false)
            .isDist(true)
            .isPinned(true)
            .pinnedBy("pinnedBy")
            .pinnedAt(123456789L)
            .precision(123)
            .defaultValue("defaultValue")
            .isNullable(false)
            .numericScale(123.456F)
            .maxLength(123456L)
            .table(ReferenceJ.to(Table.TYPE_NAME, "tableGuid"))
            .tablePartition(ReferenceJ.to("TablePartition", "tablePartitionGuid"))
            .view(ReferenceJ.to("View", "viewGuid"))
            .materializedView(ReferenceJ.to("MaterializedView", "materializedViewGuid"))
            .query(ReferenceJ.to("Query", "queryGuid1"))
            .query(ReferenceJ.to("Query", "queryGuid2"))
            .build();

    private static ColumnJ frodo;
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
        frodo = ApiResourceJ.mapper.readValue(serialized, ColumnJ.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
