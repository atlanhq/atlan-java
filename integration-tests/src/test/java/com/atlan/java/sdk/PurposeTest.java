/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.ApiToken;
import com.atlan.model.admin.QueryRequest;
import com.atlan.model.admin.QueryResponse;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.AuthPolicy;
import com.atlan.model.assets.Column;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.Database;
import com.atlan.model.assets.IAuthPolicy;
import com.atlan.model.assets.Persona;
import com.atlan.model.assets.Purpose;
import com.atlan.model.assets.Schema;
import com.atlan.model.assets.Table;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.net.HttpClient;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

public class PurposeTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Purpose");

    public static final String PURPOSE_NAME = PREFIX;
    public static final String ATLAN_TAG_NAME = PREFIX;
    public static final String EXISTING_GROUP_NAME = "admins";
    private static final String API_TOKEN_NAME = PREFIX;

    private static final String PERSONA_NAME = "Data Assets";
    private static final String DB_NAME = "ANALYTICS";
    private static final String SCHEMA_NAME = "WIDE_WORLD_IMPORTERS";
    private static final String TABLE_NAME = "STG_PACKAGE_TYPES";
    private static final String COLUMN_NAME = "PACKAGE_TYPE_NAME";

    public static Persona persona = null;
    public static Purpose purpose = null;
    private static ApiToken token = null;
    private static QueryRequest query = null;
    private static Connection snowflake = null;
    private static String columnQualifiedName = null;

    @Test(groups = {"purpose.invalid.purpose"})
    void createInvalidPurpose() {
        assertThrows(InvalidRequestException.class, () -> Purpose.creator(PURPOSE_NAME, null)
                .build());
    }

    @Test(groups = {"purpose.create.query"})
    void createQuery() throws AtlanException {
        // NOTE: This requires pre-existing assets:
        //  - Snowflake connection called "development" with a specific pre-existing table
        //  - Persona called "Data Assets" with a data policy granting query access to the Snowflake table
        snowflake = Connection.findByName("development", AtlanConnectorType.SNOWFLAKE)
                .get(0);
        query = QueryRequest.creator("SELECT * FROM \"" + TABLE_NAME + "\" LIMIT 50;", snowflake.getQualifiedName())
                .defaultSchema(DB_NAME + "." + SCHEMA_NAME)
                .build();
        String databaseQN = Database.generateQualifiedName(DB_NAME, snowflake.getQualifiedName());
        String schemaQN = Schema.generateQualifiedName(SCHEMA_NAME, databaseQN);
        String tableQN = Table.generateQualifiedName(TABLE_NAME, schemaQN);
        columnQualifiedName = Column.generateQualifiedName(COLUMN_NAME, tableQN);
        persona = Persona.findByName(PERSONA_NAME).get(0);
    }

    @Test(groups = {"purpose.create.atlantag"})
    void createAtlanTag() throws AtlanException {
        AtlanTagTest.createAtlanTag(ATLAN_TAG_NAME);
    }

    @Test(groups = {"purpose.create.token"})
    void createToken() throws AtlanException {
        token = RequestsTest.createToken(API_TOKEN_NAME);
        assertNotNull(token);
        assertNotNull(token.getAttributes());
        assertNotNull(token.getAttributes().getAccessToken());
        // After creating the token, assign it to the "Data Assets" persona to grant it query access
        ApiToken result = Atlan.getDefaultClient()
                .apiTokens
                .update(token.getId(), token.getDisplayName(), null, Set.of(persona.getQualifiedName()));
        assertNotNull(result);
        // Note: need to read the token back again to see its associated personas -- will leave that to later...
        assertNotNull(result.getAttributes());
        assertNotNull(result.getAttributes().getPersonas());
        assertEquals(result.getAttributes().getPersonas().size(), 1);
    }

    @Test(
            groups = {"purpose.create.purposes"},
            dependsOnGroups = {"purpose.create.atlantag"})
    void createPurposes() throws AtlanException {
        Purpose toCreate = Purpose.creator(PURPOSE_NAME, List.of(ATLAN_TAG_NAME))
                .description("Example purpose for testing purposes.")
                .build();
        AssetMutationResponse response = toCreate.save();
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
        AssetMutationResponse response = toUpdate.save();
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
        List<Purpose> purposes = null;
        int count = 0;
        while (count < Atlan.getMaxNetworkRetries()) {
            try {
                purposes = Purpose.findByName(PURPOSE_NAME);
                break;
            } catch (NotFoundException e) {
                Thread.sleep(HttpClient.waitTime(count).toMillis());
                count++;
            }
        }
        assertNotNull(purposes);
        assertEquals(purposes.size(), 1);
        assertEquals(purposes.get(0).getGuid(), purpose.getGuid());
    }

    @Test(
            groups = {"purpose.update.purposes.policy"},
            dependsOnGroups = {"purpose.update.purposes", "purpose.create.token"})
    void addPoliciesToPurpose() throws AtlanException {
        AuthPolicy metadata = Purpose.createMetadataPolicy(
                        "Simple read access",
                        purpose.getGuid(),
                        AuthPolicyType.ALLOW,
                        Set.of(PurposeMetadataAction.READ),
                        List.of(EXISTING_GROUP_NAME),
                        null,
                        false)
                .build();
        AuthPolicy data = Purpose.createDataPolicy(
                        "Mask the data",
                        purpose.getGuid(),
                        AuthPolicyType.DATA_MASK,
                        null,
                        List.of(token.getApiTokenUsername()),
                        false)
                .policyMaskType(DataMaskingType.REDACT)
                .build();
        AssetMutationResponse response = Atlan.getDefaultClient().assets.save(List.of(metadata, data), false);
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
        Purpose one = Purpose.get(purpose.getQualifiedName());
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
            AuthPolicy full = AuthPolicy.get(policy.getGuid());
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
                    assertEquals(full.getPolicyMaskType(), DataMaskingType.REDACT);
                    break;
            }
        }
    }

    @Test(
            groups = {"purpose.update.asset"},
            dependsOnGroups = {"purpose.create.atlantag", "purpose.create.query"})
    void assignTagToAsset() throws AtlanException {
        Column result = Column.appendAtlanTags(columnQualifiedName, List.of(ATLAN_TAG_NAME), false, false, false);
        assertNotNull(result);
    }

    @Test(
            groups = {"purpose.read.query"},
            dependsOnGroups = {"purpose.create.query", "purpose.read.purposes.2", "purpose.update.asset"})
    void runQueryWithoutPolicy() throws AtlanException {
        QueryResponse response = Atlan.getDefaultClient().queries.stream(query);
        assertNotNull(response);
        assertNotNull(response.getRows());
        assertTrue(response.getRows().size() > 1);
        List<String> row = response.getRows().get(0);
        assertFalse(row.isEmpty());
        assertEquals(row.size(), 5);
        assertFalse(row.get(2).isEmpty());
        assertFalse(row.get(2).startsWith("Xx")); // Ensure it is NOT redacted
    }

    @Test(
            groups = {"purpose.read.token"},
            dependsOnGroups = {"purpose.read.purposes.2", "purpose.create.token"})
    void confirmTokenPermissions() throws AtlanException {
        ApiToken result = ApiToken.retrieveByName(API_TOKEN_NAME);
        assertNotNull(result.getAttributes());
        assertNotNull(result.getAttributes().getPersonas());
        assertEquals(result.getAttributes().getPersonas().size(), 1);
        assertEquals(
                result.getAttributes().getPersonas().first().getPersonaQualifiedName(), persona.getQualifiedName());
    }

    @Test(
            groups = {"purpose.read.query"},
            dependsOnGroups = {
                "purpose.create.query",
                "purpose.read.purposes.2",
                "purpose.update.asset",
                "purpose.read.token"
            })
    void runQueryWithPolicy() throws AtlanException, InterruptedException {
        AtlanClient redacted = Atlan.getClient(Atlan.getBaseUrl(), PREFIX);
        redacted.setApiToken(token.getAttributes().getAccessToken());
        // The policy will take some time to go into effect -- start by waiting a
        // reasonable set amount of time (limit the same query re-running multiple times on data store)
        Thread.sleep(60000);
        // Then use a retry loop, just in case
        QueryResponse response = null;
        HekaFlow found = HekaFlow.BYPASS; // As long as Heka was bypassed, policy was not applied
        int count = 0;
        while (found == HekaFlow.BYPASS && count < 30) {
            Thread.sleep(HttpClient.waitTime(count).toMillis());
            response = redacted.queries.stream(query);
            assertNotNull(response);
            assertNotNull(response.getDetails());
            QueryStatus status = response.getDetails().getStatus();
            if (status != QueryStatus.ERROR) {
                // Only update the flow if there was no error, otherwise wait and retry
                found = response.getDetails().getHekaFlow();
            }
            count++;
        }
        assertNotNull(response);
        assertNotNull(response.getRows());
        assertTrue(response.getRows().size() > 1);
        List<String> row = response.getRows().get(0);
        assertFalse(row.isEmpty());
        assertEquals(row.size(), 5);
        assertFalse(row.get(2).isEmpty());
        assertTrue(row.get(2).startsWith("Xx")); // Ensure it IS redacted
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
        Column.removeAtlanTag(columnQualifiedName, ATLAN_TAG_NAME);
        AtlanTagTest.deleteAtlanTag(ATLAN_TAG_NAME);
    }

    @Test(
            groups = {"purpose.purge.token"},
            dependsOnGroups = {"purpose.create.*", "purpose.read.*", "purpose.update.*", "purpose.purge.purposes"},
            alwaysRun = true)
    void purgeToken() throws AtlanException {
        if (token != null) {
            RequestsTest.deleteToken(token.getId());
        } else {
            ApiToken local = Atlan.getDefaultClient().apiTokens.get(API_TOKEN_NAME);
            RequestsTest.deleteToken(local.getId());
        }
    }
}
