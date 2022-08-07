package com.atlan.model;

import static org.testng.Assert.*;

import com.atlan.model.core.Classification;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.serde.IndistinctAsset;
import com.atlan.net.ApiResource;
import org.testng.annotations.Test;

public class LineageProcessTest {

    private static final LineageProcess full = LineageProcess.builder()
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
            .code("code")
            .sql("sql")
            .ast("ast")
            .input(Reference.to(IndistinctAsset.TYPE_NAME, "input1"))
            .input(Reference.to(IndistinctAsset.TYPE_NAME, "input2"))
            .output(Reference.to(IndistinctAsset.TYPE_NAME, "output1"))
            .output(Reference.to(IndistinctAsset.TYPE_NAME, "output2"))
            .columnProcess(Reference.to(LineageProcess.TYPE_NAME, "columnProcess1"))
            .columnProcess(Reference.to(LineageProcess.TYPE_NAME, "columnProcess2"))
            .build();

    private static LineageProcess frodo;
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
        frodo = ApiResource.GSON.fromJson(serialized, LineageProcess.class);
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
