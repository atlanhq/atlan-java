/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Link;
import com.atlan.model.assets.S3Bucket;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.AtlanStatus;
import org.testng.annotations.Test;

public class LinkTest extends AtlanLiveTest {

    private static final String linkTitle = "Atlan";
    private static final String linkURL = "https://www.atlan.com";

    public static String linkGuid = null;
    public static String linkQame = null;

    @Test(
            groups = {"create.link"},
            dependsOnGroups = {"create.s3object"})
    void addLink() {
        try {
            Link link = Link.creator(S3Bucket.refByGuid(S3AssetTest.s3BucketGuid), linkTitle, linkURL)
                    .build();
            EntityMutationResponse response = link.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertTrue(one instanceof Link);
            link = (Link) one;
            assertNotNull(link);
            linkGuid = link.getGuid();
            assertNotNull(linkGuid);
            linkQame = link.getQualifiedName();
            assertNotNull(linkQame);
            assertEquals(link.getName(), linkTitle);
            assertEquals(link.getLink(), linkURL);
            assertEquals(response.getUpdatedEntities().size(), 1);
            one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof S3Bucket);
            S3Bucket s3Bucket = (S3Bucket) one;
            assertNotNull(s3Bucket);
            assertEquals(s3Bucket.getGuid(), S3AssetTest.s3BucketGuid);
            assertEquals(s3Bucket.getQualifiedName(), S3AssetTest.s3BucketQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a link.");
        }
    }

    @Test(
            groups = {"purge.link"},
            dependsOnGroups = {"create.*", "update.*", "read.*", "search.*", "link.*", "unlink.*"},
            alwaysRun = true)
    void purgeLink() {
        try {
            EntityMutationResponse response = Link.purge(linkGuid);
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 1);
            Entity one = response.getDeletedEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof Link);
            Link link = (Link) one;
            assertEquals(link.getGuid(), linkGuid);
            assertEquals(link.getQualifiedName(), linkQame);
            assertEquals(link.getStatus(), AtlanStatus.DELETED);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error during link deletion.");
        }
    }
}
