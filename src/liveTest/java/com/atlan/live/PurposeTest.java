/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.api.EntityBulkEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.AuthPolicy;
import com.atlan.model.assets.IAuthPolicy;
import com.atlan.model.assets.Purpose;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.typedefs.AtlanTagDef;
import com.atlan.net.HttpClient;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

public class PurposeTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Purpose");

    public static final String PURPOSE_NAME = PREFIX;

    public static final String ATLAN_TAG_NAME = PREFIX;

    public static Purpose purpose = null;

    @Test(groups = {"purpose.invalid.purpose"})
    void createInvalidPurpose() {
        assertThrows(InvalidRequestException.class, () -> Purpose.creator(PURPOSE_NAME, null)
                .build());
    }

    @Test(groups = {"purpose.create.atlantag"})
    void createAtlanTag() throws AtlanException {
        AtlanTagDef cls =
                AtlanTagDef.creator(ATLAN_TAG_NAME, AtlanTagColor.GREEN).build();
        AtlanTagDef response = cls.create();
        assertNotNull(response);
    }

    @Test(
            groups = {"purpose.create.purposes"},
            dependsOnGroups = {"purpose.create.atlantag"})
    void createPurposes() throws AtlanException {
        Purpose toCreate = Purpose.creator(PURPOSE_NAME, List.of(ATLAN_TAG_NAME))
                .description("Example purpose for testing purposes.")
                .build();
        AssetMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        assertEquals(response.getCreatedAssets().size(), 1);
        assertTrue(response.getCreatedAssets().get(0) instanceof Purpose);
        purpose = (Purpose) response.getCreatedAssets().get(0);
        assertNotNull(purpose);
        assertNotNull(purpose.getGuid());
        assertEquals(purpose.getDisplayName(), PURPOSE_NAME);
    }

    @Test(
            groups = {"purpose.update.purposes"},
            dependsOnGroups = {"purpose.create.purposes"})
    void updatePurposes() throws AtlanException {
        Purpose toUpdate = Purpose.updater(purpose.getQualifiedName(), purpose.getName(), true)
                .description("Now with a description!")
                .denyAssetTab(AssetSidebarTab.LINEAGE)
                .denyAssetTab(AssetSidebarTab.RELATIONS)
                .denyAssetTab(AssetSidebarTab.QUERIES)
                .build();
        AssetMutationResponse response = toUpdate.upsert();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Purpose);
        Purpose found = (Purpose) one;
        assertEquals(found.getGuid(), purpose.getGuid());
        assertEquals(found.getDescription(), "Now with a description!");
        assertEquals(found.getDenyAssetTabs().size(), 3);
    }

    @Test(
            groups = {"purpose.read.purposes.1"},
            dependsOnGroups = {"purpose.update.purposes"})
    void findPurposeByName() throws AtlanException, InterruptedException {
        List<Purpose> purposes = Purpose.findByName(PURPOSE_NAME, null);
        int count = 0;
        while (purposes.isEmpty() && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(HttpClient.waitTime(count).toMillis());
            purposes = Purpose.findByName(PURPOSE_NAME, null);
            count++;
        }
        assertNotNull(purposes);
        assertEquals(purposes.size(), 1);
        assertEquals(purposes.get(0).getGuid(), purpose.getGuid());
    }

    @Test(
            groups = {"purpose.update.purposes.policy"},
            dependsOnGroups = {"purpose.update.purposes"})
    void addPoliciesToPurpose() throws AtlanException {
        AuthPolicy metadata = Purpose.createMetadataPolicy(
                        "Simple read access",
                        purpose.getGuid(),
                        AuthPolicyType.ALLOW,
                        Set.of(PurposeMetadataAction.READ),
                        null,
                        null,
                        true)
                .build();
        AuthPolicy data = Purpose.createDataPolicy(
                        "Mask the data", purpose.getGuid(), AuthPolicyType.DATA_MASK, null, null, true)
                .policyMaskType(DataMaskingType.HASH)
                .build();
        AssetMutationResponse response = EntityBulkEndpoint.upsert(List.of(metadata, data), false);
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Purpose);
        assertEquals(one.getGuid(), purpose.getGuid());
        assertEquals(response.getCreatedAssets().size(), 2);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof AuthPolicy);
        one = response.getCreatedAssets().get(1);
        assertTrue(one instanceof AuthPolicy);
    }

    @Test(
            groups = {"purpose.read.purposes.2"},
            dependsOnGroups = {"purpose.update.purposes.policy"})
    void retrievePurposes2() throws AtlanException {
        Purpose one = Purpose.retrieveByQualifiedName(purpose.getQualifiedName());
        assertNotNull(one);
        assertEquals(one.getGuid(), purpose.getGuid());
        assertEquals(one.getDescription(), "Now with a description!");
        Set<AssetSidebarTab> denied = one.getDenyAssetTabs();
        assertEquals(denied.size(), 3);
        assertTrue(denied.contains(AssetSidebarTab.LINEAGE));
        assertTrue(denied.contains(AssetSidebarTab.RELATIONS));
        assertTrue(denied.contains(AssetSidebarTab.QUERIES));
        Set<IAuthPolicy> policies = one.getPolicies();
        assertEquals(policies.size(), 2);
        for (IAuthPolicy policy : policies) {
            // Need to retrieve the full policy if we want to see any info about it
            // (what comes back on the Purpose itself are just policy references)
            AuthPolicy full = AuthPolicy.retrieveByGuid(policy.getGuid());
            assertNotNull(full);
            String subCat = full.getPolicySubCategory();
            assertNotNull(subCat);
            assertTrue(Set.of("metadata", "data").contains(subCat));
            switch (subCat) {
                case "metadata":
                    assertNotNull(full.getPolicyActions());
                    assertEquals(full.getPolicyActions().size(), 1);
                    assertTrue(full.getPolicyActions().contains(PurposeMetadataAction.READ));
                    assertEquals(full.getPolicyType(), AuthPolicyType.ALLOW);
                    break;
                case "data":
                    assertNotNull(full.getPolicyActions());
                    assertEquals(full.getPolicyActions().size(), 1);
                    assertTrue(full.getPolicyActions().contains(DataAction.SELECT));
                    assertEquals(full.getPolicyType(), AuthPolicyType.DATA_MASK);
                    assertNotNull(full.getPolicyMaskType());
                    assertEquals(full.getPolicyMaskType(), DataMaskingType.HASH);
                    break;
            }
        }
    }

    @Test(
            groups = {"purpose.purge.purposes"},
            dependsOnGroups = {"purpose.create.*", "purpose.update.*", "purpose.read.*"},
            alwaysRun = true)
    void purgePurposes() throws AtlanException {
        Purpose.purge(purpose.getGuid());
    }

    @Test(
            groups = {"purpose.purge.atlantags"},
            dependsOnGroups = {"purpose.create.*", "purpose.update.*", "purpose.read.*", "purpose.purge.purposes"},
            alwaysRun = true)
    void purgeAtlanTags() throws AtlanException {
        AtlanTagDef.purge(ATLAN_TAG_NAME);
    }
}
