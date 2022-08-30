/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Readme;
import com.atlan.model.assets.S3Bucket;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.AtlanStatus;
import org.testng.annotations.Test;

public class ReadmeTest extends AtlanLiveTest {

    private static final String readmeContent =
            "<h1>This is a test</h1><h2>With some headings</h2><p>And some normal content.</p>";

    public static String readmeGuid = null;
    public static String readmeQame = null;

    @Test(
            groups = {"create.readme"},
            dependsOnGroups = {"create.s3object"})
    void addReadme() {
        try {
            Readme readme = Readme.creator(
                            S3Bucket.TYPE_NAME, S3AssetTest.s3BucketGuid, S3AssetTest.S3_BUCKET_NAME, readmeContent)
                    .build();
            EntityMutationResponse response = readme.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertTrue(one instanceof Readme);
            readme = (Readme) one;
            assertNotNull(readme);
            readmeGuid = readme.getGuid();
            assertNotNull(readmeGuid);
            readmeQame = readme.getQualifiedName();
            assertNotNull(readmeQame);
            assertEquals(readme.getDescription(), readmeContent);
            assertEquals(response.getUpdatedEntities().size(), 1);
            one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof S3Bucket);
            S3Bucket s3Bucket = (S3Bucket) one;
            assertNotNull(s3Bucket);
            assertEquals(s3Bucket.getGuid(), S3AssetTest.s3BucketGuid);
            assertEquals(s3Bucket.getQualifiedName(), S3AssetTest.s3BucketQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a README.");
        }
    }

    @Test(
            groups = {"purge.readme"},
            dependsOnGroups = {"create.*", "update.*", "read.*", "search.*", "link.*", "unlink.*"},
            alwaysRun = true)
    void purgeReadme() {
        try {
            EntityMutationResponse response = Readme.purge(readmeGuid);
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 1);
            Entity one = response.getDeletedEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof Readme);
            Readme readme = (Readme) one;
            assertEquals(readme.getGuid(), readmeGuid);
            assertEquals(readme.getQualifiedName(), readmeQame);
            assertEquals(readme.getStatus(), AtlanStatus.DELETED);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error during README deletion.");
        }
    }
}
