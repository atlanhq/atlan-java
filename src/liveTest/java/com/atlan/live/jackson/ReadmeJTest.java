package com.atlan.live.jackson;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.live.AtlanLiveTest;
import com.atlan.model.ReadmeJ;
import com.atlan.model.S3BucketJ;
import com.atlan.model.core.EntityJ;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.responses.EntityMutationResponseJ;
import org.testng.annotations.Test;

public class ReadmeJTest extends AtlanLiveTest {

    private static final String readmeContent =
            "<h1>This is a test</h1><h2>With some headings</h2><p>And some normal content.</p>";

    public static String readmeGuid = null;
    public static String readmeQame = null;

    @Test(
            groups = {"create.readme"},
            dependsOnGroups = {"create.s3object"})
    void addReadme() {
        try {
            ReadmeJ readme = ReadmeJ.creator(
                            S3BucketJ.TYPE_NAME, S3AssetJTest.s3BucketGuid, S3AssetJTest.S3_BUCKET_NAME, readmeContent)
                    .build();
            EntityMutationResponseJ response = readme.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            EntityJ one = response.getCreatedEntities().get(0);
            assertTrue(one instanceof ReadmeJ);
            readme = (ReadmeJ) one;
            assertNotNull(readme);
            readmeGuid = readme.getGuid();
            assertNotNull(readmeGuid);
            readmeQame = readme.getQualifiedName();
            assertNotNull(readmeQame);
            assertEquals(readme.getDescription(), readmeContent);
            assertEquals(response.getUpdatedEntities().size(), 1);
            one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof S3BucketJ);
            S3BucketJ s3Bucket = (S3BucketJ) one;
            assertNotNull(s3Bucket);
            assertEquals(s3Bucket.getGuid(), S3AssetJTest.s3BucketGuid);
            assertEquals(s3Bucket.getQualifiedName(), S3AssetJTest.s3BucketQame);
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
            EntityMutationResponseJ response = ReadmeJ.purge(readmeGuid);
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 1);
            EntityJ one = response.getDeletedEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof ReadmeJ);
            ReadmeJ readme = (ReadmeJ) one;
            assertEquals(readme.getGuid(), readmeGuid);
            assertEquals(readme.getQualifiedName(), readmeQame);
            assertEquals(readme.getStatus(), AtlanStatus.DELETED);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error during README deletion.");
        }
    }
}
