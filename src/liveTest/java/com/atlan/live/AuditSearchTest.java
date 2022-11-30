/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.S3Bucket;
import com.atlan.model.assets.S3Object;
import com.atlan.model.core.Classification;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AuditActionType;
import com.atlan.model.search.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests various audit searches.
 * Note: since the search index is only eventually consistent with the metastore, there can be slight
 * delays between the data being persisted and it being searchable. As a result, there are retry loops
 * here in the tests to allow for that eventual consistency.
 */
@Test(groups = {"audit"})
@Slf4j
public class AuditSearchTest extends AtlanLiveTest {

    @Test(
            groups = {"search.entity.audit"},
            dependsOnGroups = {"update.s3bucket.again", "create.lineage.*"})
    void searchAuditLogBucket() {
        try {
            AuditSearchRequest requestByGuid =
                    AuditSearchRequest.byGuid(S3AssetTest.s3BucketGuid, 10).build();
            AuditSearchRequest requestByQN = AuditSearchRequest.byQualifiedName(
                            S3Bucket.TYPE_NAME, S3AssetTest.s3BucketQame, 10)
                    .build();

            AuditSearchResponse responseByGuid = requestByGuid.search();
            AuditSearchResponse responseByQN = requestByQN.search();

            assertNotNull(requestByGuid);
            assertNotNull(responseByQN);

            List<EntityAudit> auditsByGuid = responseByGuid.getEntityAudits();
            validateEntityAudits(auditsByGuid);
            List<EntityAudit> auditsByQN = responseByQN.getEntityAudits();
            validateEntityAudits(auditsByQN);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while searching S3Bucket audit history (entity updates).");
        }
    }

    @Test(
            groups = {"search.classification.audit"},
            dependsOnGroups = {"unlink.classification.s3object.all"})
    void searchAuditLogClassification() throws InterruptedException {
        try {
            AuditSearchRequest requestByGuid =
                    AuditSearchRequest.byGuid(S3AssetTest.s3Object2Guid, 20).build();
            AuditSearchRequest requestByQN = AuditSearchRequest.byQualifiedName(
                            S3Object.TYPE_NAME, S3AssetTest.s3Object2Qame, 20)
                    .build();

            AuditSearchResponse responseByGuid = requestByGuid.search();
            AuditSearchResponse responseByQN = requestByQN.search();

            assertNotNull(responseByGuid);
            assertNotNull(responseByQN);

            // Varies between 5 and 8 entries due to eventual consistency, so we will
            // do a retry loop until we get to the full 8 entries
            int count = 0;
            while (responseByGuid.getCount() < 8L && count < Atlan.getMaxNetworkRetries()) {
                Thread.sleep(2000);
                responseByGuid = requestByGuid.search();
                count++;
            }
            count = 0;
            while (responseByQN.getCount() < 8L && count < Atlan.getMaxNetworkRetries()) {
                Thread.sleep(2000);
                responseByQN = requestByQN.search();
                count++;
            }

            List<EntityAudit> auditsByGuid = responseByGuid.getEntityAudits();
            validateClassificationAudits(auditsByGuid);
            List<EntityAudit> auditsByQN = responseByQN.getEntityAudits();
            validateClassificationAudits(auditsByQN);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while searching S3Object audit history (classifications).");
        }
    }

    @Test(
            groups = {"search.cm.audit"},
            dependsOnGroups = {"unlink.cm.s3object.dq"})
    void searchAuditLogCustomMetadata() {
        try {
            AuditSearchRequest requestByGuid =
                    AuditSearchRequest.byGuid(S3AssetTest.s3Object1Guid, 20).build();
            AuditSearchRequest requestByQN = AuditSearchRequest.byQualifiedName(
                            S3Object.TYPE_NAME, S3AssetTest.s3Object1Qame, 20)
                    .build();

            AuditSearchResponse responseByGuid = requestByGuid.search();
            AuditSearchResponse responseByQN = requestByQN.search();

            assertNotNull(responseByGuid);
            assertNotNull(responseByQN);

            List<EntityAudit> auditsByGuid = responseByGuid.getEntityAudits();
            validateCustomMetadataAudits(auditsByGuid);
            List<EntityAudit> auditsByQN = responseByQN.getEntityAudits();
            validateCustomMetadataAudits(auditsByQN);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while searching S3Object audit history (custom metadata).");
        }
    }

    private void validateEntityAudits(List<EntityAudit> audits) {
        assertNotNull(audits);
        assertEquals(audits.size(), 5);
        EntityAudit one = audits.get(4);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_CREATE);
        AuditDetail detail = one.getDetail();
        assertTrue(detail instanceof S3Bucket);
        S3Bucket bucket = (S3Bucket) detail;
        assertEquals(bucket.getGuid(), S3AssetTest.s3BucketGuid);
        assertEquals(bucket.getQualifiedName(), S3AssetTest.s3BucketQame);
        assertEquals(bucket.getName(), S3AssetTest.S3_BUCKET_NAME);
        assertEquals(bucket.getAwsArn(), S3AssetTest.S3_BUCKET_ARN);
        assertNull(bucket.getCertificateStatus());
        one = audits.get(3);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof S3Bucket);
        bucket = (S3Bucket) detail;
        assertEquals(bucket.getGuid(), S3AssetTest.s3BucketGuid);
        assertEquals(bucket.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
        one = audits.get(2);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof S3Bucket);
        bucket = (S3Bucket) detail;
        assertEquals(bucket.getGuid(), S3AssetTest.s3BucketGuid);
        assertEquals(bucket.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
        assertEquals(bucket.getAnnouncementMessage(), "Automated testing of the Java client.");
        assertEquals(bucket.getAnnouncementTitle(), "Automated testing.");
        one = audits.get(1);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof S3Bucket);
        bucket = (S3Bucket) detail;
        assertEquals(bucket.getGuid(), S3AssetTest.s3BucketGuid);
        assertNotNull(bucket.getNullFields());
        assertEquals(bucket.getNullFields().size(), 1);
        assertTrue(bucket.getNullFields().contains("certificateStatus"));
        one = audits.get(0);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof S3Bucket);
        bucket = (S3Bucket) detail;
        assertEquals(bucket.getGuid(), S3AssetTest.s3BucketGuid);
        assertNotNull(bucket.getNullFields());
        assertEquals(bucket.getNullFields().size(), 3);
        assertTrue(bucket.getNullFields().contains("announcementType"));
        assertTrue(bucket.getNullFields().contains("announcementTitle"));
        assertTrue(bucket.getNullFields().contains("announcementMessage"));
    }

    private void validateClassificationAudits(List<EntityAudit> audits) {
        assertNotNull(audits);
        assertEquals(audits.size(), 8);
        EntityAudit one = audits.get(7);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_CREATE);
        AuditDetail detail = one.getDetail();
        assertTrue(detail instanceof S3Object);
        S3Object object = (S3Object) detail;
        assertEquals(object.getGuid(), S3AssetTest.s3Object2Guid);
        assertEquals(object.getQualifiedName(), S3AssetTest.s3Object2Qame);
        assertEquals(object.getName(), S3AssetTest.S3_OBJECT2_NAME);
        assertEquals(object.getAwsArn(), S3AssetTest.S3_OBJECT2_ARN);
        // Order of classifications could be either way here, as they are both added
        // in a single call
        Set<String> classificationAdds = new HashSet<>();
        one = audits.get(6);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_ADD);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        Classification classification = (Classification) detail;
        classificationAdds.add(classification.getTypeName());
        assertEquals(classification.getEntityGuid(), S3AssetTest.s3Object2Guid);
        one = audits.get(5);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_ADD);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        classification = (Classification) detail;
        classificationAdds.add(classification.getTypeName());
        assertEquals(classification.getEntityGuid(), S3AssetTest.s3Object2Guid);
        log.debug("Classifications added: {}", classificationAdds);
        assertEquals(classificationAdds.size(), 2);
        assertTrue(classificationAdds.contains(ClassificationTest.CLASSIFICATION_NAME1));
        assertTrue(classificationAdds.contains(ClassificationTest.CLASSIFICATION_NAME2));
        one = audits.get(4);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_DELETE);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        classification = (Classification) detail;
        assertEquals(classification.getTypeName(), ClassificationTest.CLASSIFICATION_NAME2);
        one = audits.get(3);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_ADD);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        classification = (Classification) detail;
        assertEquals(classification.getTypeName(), ClassificationTest.CLASSIFICATION_NAME2);
        assertEquals(classification.getEntityGuid(), S3AssetTest.s3Object2Guid);
        // Order of classifications could be either way here, as they are both deleted
        // in a single call
        Set<String> classificationDeletes = new HashSet<>();
        one = audits.get(2);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_DELETE);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        classification = (Classification) detail;
        classificationDeletes.add(classification.getTypeName());
        one = audits.get(1);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_DELETE);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        classification = (Classification) detail;
        classificationDeletes.add(classification.getTypeName());
        log.debug("Classifications deleted: {}", classificationDeletes);
        assertEquals(classificationDeletes.size(), 2);
        assertTrue(classificationDeletes.contains(ClassificationTest.CLASSIFICATION_NAME1));
        assertTrue(classificationDeletes.contains(ClassificationTest.CLASSIFICATION_NAME2));
        one = audits.get(0);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof S3Object);
        object = (S3Object) detail;
        assertEquals(object.getGuid(), S3AssetTest.s3Object2Guid);
        // Note: nothing else to check, as only classifications above were actually changed
    }

    private void validateCustomMetadataAudits(List<EntityAudit> audits) {
        assertNotNull(audits);
        assertEquals(audits.size(), 8);
        EntityAudit one = audits.get(7);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_CREATE);
        AuditDetail detail = one.getDetail();
        assertTrue(detail instanceof S3Object);
        S3Object object = (S3Object) detail;
        assertEquals(object.getGuid(), S3AssetTest.s3Object1Guid);
        assertEquals(object.getQualifiedName(), S3AssetTest.s3Object1Qame);
        assertEquals(object.getName(), S3AssetTest.S3_OBJECT1_NAME);
        assertEquals(object.getAwsArn(), S3AssetTest.S3_OBJECT1_ARN);
        one = audits.get(6);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        CustomMetadataAttributesAuditDetail cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CustomMetadataTest.CM_RACI);
        CustomMetadataTest.validateRACIAttributes(cmad.getAttributes());
        one = audits.get(5);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CustomMetadataTest.CM_IPR);
        CustomMetadataTest.validateIPRAttributes(cmad.getAttributes(), true);
        one = audits.get(4);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CustomMetadataTest.CM_QUALITY);
        CustomMetadataTest.validateDQAttributes(cmad.getAttributes());
        one = audits.get(3);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CustomMetadataTest.CM_IPR);
        Map<String, Object> attributes = cmad.getAttributes();
        assertNotNull(attributes);
        assertEquals(attributes.size(), 1);
        assertTrue(attributes.containsKey(CustomMetadataTest.CM_ATTR_IPR_MANDATORY));
        assertEquals(attributes.get(CustomMetadataTest.CM_ATTR_IPR_MANDATORY), false);
        one = audits.get(2);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CustomMetadataTest.CM_RACI);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 4);
        assertNull(attributes.get(CustomMetadataTest.CM_ATTR_RACI_RESPONSIBLE));
        assertNull(attributes.get(CustomMetadataTest.CM_ATTR_RACI_ACCOUNTABLE));
        assertNull(attributes.get(CustomMetadataTest.CM_ATTR_RACI_CONSULTED));
        assertNull(attributes.get(CustomMetadataTest.CM_ATTR_RACI_INFORMED));
        one = audits.get(1);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CustomMetadataTest.CM_IPR);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 5);
        assertNull(attributes.get(CustomMetadataTest.CM_ATTR_IPR_MANDATORY));
        assertNull(attributes.get(CustomMetadataTest.CM_ATTR_IPR_URL));
        assertNull(attributes.get(CustomMetadataTest.CM_ATTR_IPR_DATE));
        assertNull(attributes.get(CustomMetadataTest.CM_ATTR_IPR_LICENSE));
        assertNull(attributes.get(CustomMetadataTest.CM_ATTR_IPR_VERSION));
        one = audits.get(0);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CustomMetadataTest.CM_QUALITY);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 2);
        assertNull(attributes.get(CustomMetadataTest.CM_ATTR_QUALITY_SQL));
        assertNull(attributes.get(CustomMetadataTest.CM_ATTR_QUALITY_COUNT));
    }
}
