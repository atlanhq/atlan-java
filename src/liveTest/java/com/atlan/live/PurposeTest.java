/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.admin.*;
import com.atlan.model.enums.AssetSidebarTab;
import com.atlan.model.enums.AtlanClassificationColor;
import com.atlan.model.typedefs.ClassificationDef;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

@Test(groups = {"purpose"})
public class PurposeTest extends AtlanLiveTest {

    public static final String PURPOSE_NAME = "JavaClient Purpose";

    public static final String CLS_NAME = "JC PCLS";

    public static String purposeGuid = null;

    @Test(groups = {"invalid.purpose"})
    void createInvalidPurpose() {
        assertThrows(
                InvalidRequestException.class,
                () -> Purpose.creator(PURPOSE_NAME, null).build().create());
    }

    @Test(groups = {"create.classification"})
    void createClassification() throws AtlanException {
        ClassificationDef cls = ClassificationDef.creator(CLS_NAME, AtlanClassificationColor.GREEN)
                .build();
        ClassificationDef response = cls.create();
        assertNotNull(response);
    }

    @Test(
            groups = {"create.purposes"},
            dependsOnGroups = {"create.classification"})
    void createPurposes() {
        try {
            Purpose purpose = Purpose.creator(PURPOSE_NAME, List.of(CLS_NAME))
                .description("Example purpose for testing purposes.")
                .build();
            Purpose result = purpose.create();
            assertNotNull(result);
            purposeGuid = result.getId();
            assertNotNull(purposeGuid);
            assertEquals(result.getDisplayName(), PURPOSE_NAME);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when creating purposes.");
        }
    }

    @Test(
            groups = {"read.purposes.1"},
            dependsOnGroups = {"create.purposes"})
    void retrievePurposes1() {
        try {
            List<Purpose> purposes = Purpose.retrieveAll();
            assertNotNull(purposes);
            assertTrue(purposes.size() >= 1);
            Purpose one = Purpose.retrieveByName(PURPOSE_NAME);
            assertNotNull(one);
            assertEquals(one.getId(), purposeGuid);
            assertNotNull(one.getTags());
            assertEquals(one.getTags().size(), 1);
            assertTrue(one.getTags().contains(CLS_NAME));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when retrieving purposes.");
        }
    }

    @Test(
            groups = {"update.purposes"},
            dependsOnGroups = {"create.purposes"})
    void updatePurposes() {
        try {
            Purpose purpose = Purpose.updater(purposeGuid, PURPOSE_NAME, List.of(CLS_NAME))
                    .description("Now with a description!")
                    .attributes(Purpose.PurposeAttributes.builder()
                            .preferences(Purpose.PurposePreferences.builder()
                                    .assetTabDeny(AssetSidebarTab.LINEAGE)
                                    .assetTabDeny(AssetSidebarTab.RELATIONS)
                                    .assetTabDeny(AssetSidebarTab.QUERIES)
                                    .build())
                            .build())
                    .build();
            purpose.update();
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when updating purposes.");
        }
    }

    @Test(
            groups = {"read.purposes.2"},
            dependsOnGroups = {"update.purposes"})
    void retrievePurposes2() {
        try {
            Purpose one = Purpose.retrieveByName(PURPOSE_NAME);
            assertNotNull(one);
            assertEquals(one.getId(), purposeGuid);
            assertEquals(one.getDescription(), "Now with a description!");
            assertNotNull(one.getAttributes());
            assertNotNull(one.getAttributes().getPreferences());
            Set<AssetSidebarTab> denied = one.getAttributes().getPreferences().getAssetTabsDenyList();
            assertEquals(denied.size(), 3);
            assertTrue(denied.contains(AssetSidebarTab.LINEAGE));
            assertTrue(denied.contains(AssetSidebarTab.RELATIONS));
            assertTrue(denied.contains(AssetSidebarTab.QUERIES));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when retrieving purposes.");
        }
    }

    @Test(
            groups = {"purge.purposes"},
            dependsOnGroups = {"create.*", "update.*", "read.*"},
            alwaysRun = true)
    void purgePurposes() {
        try {
            Purpose.delete(purposeGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when deleting purposes.");
        }
    }

    @Test(
            groups = {"purge.classifications"},
            dependsOnGroups = {"create.*", "update.*", "read.*"},
            alwaysRun = true)
    void purgeClassifications() throws AtlanException {
        ClassificationDef.purge(CLS_NAME);
    }
}
