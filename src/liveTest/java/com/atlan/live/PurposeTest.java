/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.admin.*;
import com.atlan.model.enums.*;
import com.atlan.model.typedefs.ClassificationDef;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

public class PurposeTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Purpose");

    public static final String PURPOSE_NAME = PREFIX;

    public static final String CLS_NAME = PREFIX;

    public static String purposeGuid = null;

    @Test(groups = {"purpose.invalid.purpose"})
    void createInvalidPurpose() {
        assertThrows(
                InvalidRequestException.class,
                () -> Purpose.creator(PURPOSE_NAME, null).build().create());
    }

    @Test(groups = {"purpose.create.classification"})
    void createClassification() throws AtlanException {
        ClassificationDef cls = ClassificationDef.creator(CLS_NAME, AtlanClassificationColor.GREEN)
                .build();
        ClassificationDef response = cls.create();
        assertNotNull(response);
    }

    @Test(
            groups = {"purpose.create.purposes"},
            dependsOnGroups = {"purpose.create.classification"})
    void createPurposes() throws AtlanException {
        Purpose purpose = Purpose.creator(PURPOSE_NAME, List.of(CLS_NAME))
                .description("Example purpose for testing purposes.")
                .build();
        Purpose result = purpose.create();
        assertNotNull(result);
        purposeGuid = result.getId();
        assertNotNull(purposeGuid);
        assertEquals(result.getDisplayName(), PURPOSE_NAME);
    }

    @Test(
            groups = {"purpose.read.purposes.1"},
            dependsOnGroups = {"purpose.create.purposes"})
    void retrievePurposes1() throws AtlanException {
        List<Purpose> purposes = Purpose.retrieveAll();
        assertNotNull(purposes);
        assertTrue(purposes.size() >= 1);
        Purpose one = Purpose.retrieveByName(PURPOSE_NAME);
        assertNotNull(one);
        assertEquals(one.getId(), purposeGuid);
        assertNotNull(one.getTags());
        assertEquals(one.getTags().size(), 1);
        assertTrue(one.getTags().contains(CLS_NAME));
    }

    @Test(
            groups = {"purpose.update.purposes"},
            dependsOnGroups = {"purpose.create.purposes"})
    void updatePurposes() throws AtlanException {
        Purpose purpose = Purpose.retrieveByName(PURPOSE_NAME);
        assertNotNull(purpose);
        purpose = purpose.toBuilder()
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
    }

    @Test(
            groups = {"purpose.update.purposes.policy"},
            dependsOnGroups = {"purpose.update.purposes"})
    void addPoliciesToPurpose() throws AtlanException {
        Purpose purpose = Purpose.retrieveByName(PURPOSE_NAME).toBuilder()
                .metadataPolicy(PurposeMetadataPolicy.creator(
                                "Simple read access", null, null, true, Set.of(PurposeMetadataPolicyAction.READ), true)
                        .build())
                .dataPolicy(PurposeDataPolicy.creator(
                                "Mask the data",
                                null,
                                null,
                                true,
                                Set.of(DataPolicyAction.SELECT),
                                DataPolicyType.MASKING,
                                true)
                        .mask(MaskingType.HASH)
                        .build())
                .build();
        Purpose result = purpose.update();
        assertNotNull(result);
        assertNotNull(result.getMetadataPolicies());
        assertEquals(result.getMetadataPolicies().size(), 1);
        assertNotNull(result.getDataPolicies());
        assertEquals(result.getDataPolicies().size(), 1);
    }

    @Test(
            groups = {"purpose.read.purposes.2"},
            dependsOnGroups = {"purpose.update.purposes.policy"})
    void retrievePurposes2() throws AtlanException {
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
        assertEquals(one.getMetadataPolicies().size(), 1);
        assertEquals(one.getDataPolicies().size(), 1);
    }

    @Test(
            groups = {"purpose.purge.purposes"},
            dependsOnGroups = {"purpose.create.*", "purpose.update.*", "purpose.read.*"},
            alwaysRun = true)
    void purgePurposes() throws AtlanException {
        Purpose.delete(purposeGuid);
    }

    @Test(
            groups = {"purpose.purge.classifications"},
            dependsOnGroups = {"purpose.create.*", "purpose.update.*", "purpose.read.*", "purpose.purge.purposes"},
            alwaysRun = true)
    void purgeClassifications() throws AtlanException {
        ClassificationDef.purge(CLS_NAME);
    }
}
