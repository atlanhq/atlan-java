/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.net.HttpClient;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class PersonaTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Persona");

    private static final String PERSONA_NAME = PREFIX;
    public static final String CONNECTION_NAME = PREFIX;
    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.SYNDIGO;
    private static final String GLOSSARY_NAME = PREFIX;
    public static final String EXISTING_GROUP_NAME = "admins";

    private static Persona persona = null;

    private static Connection connection = null;
    private static Glossary glossary = null;

    @Test(groups = {"persona.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(groups = {"persona.create.glossary"})
    void createGlossary() throws AtlanException {
        glossary = GlossaryTest.createGlossary(client, GLOSSARY_NAME);
    }

    @Test(groups = {"persona.create.personas"})
    void createPersonas() throws AtlanException {
        Persona toCreate = Persona.creator(PERSONA_NAME).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Persona);
        persona = (Persona) one;
        assertNotNull(persona.getGuid());
        assertNotNull(persona.getQualifiedName());
        assertEquals(persona.getName(), PERSONA_NAME);
        assertEquals(persona.getDisplayName(), PERSONA_NAME);
        assertNotEquals(persona.getQualifiedName(), PERSONA_NAME);
    }

    @Test(
            groups = {"persona.update.personas"},
            dependsOnGroups = {"persona.create.personas"})
    void updatePersonas() throws AtlanException {
        Persona toUpdate = Persona.updater(persona.getQualifiedName(), persona.getName(), true)
                .description("Now with a description!")
                .denyAssetTab(AssetSidebarTab.LINEAGE)
                .denyAssetTab(AssetSidebarTab.RELATIONS)
                .denyAssetTab(AssetSidebarTab.QUERIES)
                .personaGroups(List.of(EXISTING_GROUP_NAME))
                .build();
        AssetMutationResponse response = toUpdate.save(client);
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Persona);
        Persona found = (Persona) one;
        assertEquals(found.getGuid(), persona.getGuid());
        assertEquals(found.getDescription(), "Now with a description!");
        assertEquals(found.getDenyAssetTabs().size(), 3);
    }

    @Test(
            groups = {"persona.read.personas.1"},
            dependsOnGroups = {"persona.update.personas"})
    void findPersonaByName() throws AtlanException, InterruptedException {
        List<Persona> list = null;
        int count = 0;
        while (list == null && count < client.getMaxNetworkRetries()) {
            try {
                list = Persona.findByName(client, PERSONA_NAME);
            } catch (NotFoundException e) {
                Thread.sleep(HttpClient.waitTime(count).toMillis());
                count++;
            }
        }
        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getGuid(), persona.getGuid());
    }

    @Test(
            groups = {"persona.update.personas.policy"},
            dependsOnGroups = {"persona.update.personas", "persona.create.connection", "persona.create.glossary"})
    void addPoliciesToPersona() throws AtlanException {
        AuthPolicy metadata = Persona.createMetadataPolicy(
                        "Simple read access",
                        persona.getGuid(),
                        AuthPolicyType.ALLOW,
                        Set.of(PersonaMetadataAction.READ),
                        connection.getQualifiedName(),
                        Set.of("entity:" + connection.getQualifiedName()))
                .build();
        AuthPolicy data = Persona.createDataPolicy(
                        "Allow access to data",
                        persona.getGuid(),
                        AuthPolicyType.ALLOW,
                        connection.getQualifiedName(),
                        Set.of("entity:" + connection.getQualifiedName()))
                .build();
        AuthPolicy glossaryPolicy = Persona.createGlossaryPolicy(
                        "All glossaries",
                        persona.getGuid(),
                        AuthPolicyType.ALLOW,
                        Set.of(PersonaGlossaryAction.CREATE, PersonaGlossaryAction.UPDATE),
                        Set.of("entity:" + glossary.getQualifiedName()))
                .build();
        AssetMutationResponse response = client.assets.save(List.of(metadata, data, glossaryPolicy));
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Persona);
        Persona found = (Persona) one;
        assertEquals(found.getGuid(), persona.getGuid());
        assertEquals(response.getCreatedAssets().size(), 3);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof AuthPolicy);
        one = response.getCreatedAssets().get(1);
        assertTrue(one instanceof AuthPolicy);
        one = response.getCreatedAssets().get(2);
        assertTrue(one instanceof AuthPolicy);
    }

    @Test(
            groups = {"persona.read.personas.2"},
            dependsOnGroups = {"persona.update.personas.*"})
    void retrievePersonas2() throws AtlanException {
        Persona one = Persona.get(client, persona.getQualifiedName(), true);
        assertNotNull(one);
        assertEquals(one.getGuid(), persona.getGuid());
        assertEquals(one.getDescription(), "Now with a description!");
        Set<AssetSidebarTab> denied = one.getDenyAssetTabs();
        assertEquals(denied.size(), 3);
        assertTrue(denied.contains(AssetSidebarTab.LINEAGE));
        assertTrue(denied.contains(AssetSidebarTab.RELATIONS));
        assertTrue(denied.contains(AssetSidebarTab.QUERIES));
        Set<IAuthPolicy> policies = one.getPolicies();
        assertEquals(policies.size(), 3);
        for (IAuthPolicy policy : policies) {
            // Need to retrieve the full policy if we want to see any info about it
            // (what comes back on the Persona itself are just policy references)
            AuthPolicy full = AuthPolicy.get(client, policy.getGuid(), true);
            assertNotNull(full);
            String subCat = full.getPolicySubCategory();
            assertNotNull(subCat);
            assertTrue(Set.of("metadata", "data", "glossary").contains(subCat));
            assertEquals(full.getPolicyType(), AuthPolicyType.ALLOW);
            switch (subCat) {
                case "metadata":
                    assertNotNull(full.getPolicyActions());
                    assertEquals(full.getPolicyActions().size(), 1);
                    assertTrue(full.getPolicyActions().contains(PersonaMetadataAction.READ));
                    assertNotNull(full.getPolicyResources());
                    assertTrue(full.getPolicyResources().contains("entity:" + connection.getQualifiedName()));
                    break;
                case "data":
                    assertNotNull(full.getPolicyActions());
                    assertEquals(full.getPolicyActions().size(), 1);
                    assertTrue(full.getPolicyActions().contains(DataAction.SELECT));
                    assertNotNull(full.getPolicyResources());
                    assertTrue(full.getPolicyResources().contains("entity:" + connection.getQualifiedName()));
                    break;
                case "glossary":
                    assertNotNull(full.getPolicyActions());
                    assertEquals(full.getPolicyActions().size(), 2);
                    assertTrue(full.getPolicyActions().contains(PersonaGlossaryAction.CREATE));
                    assertTrue(full.getPolicyActions().contains(PersonaGlossaryAction.UPDATE));
                    assertNotNull(full.getPolicyResources());
                    assertTrue(full.getPolicyResources().contains("entity:" + glossary.getQualifiedName()));
                    break;
            }
        }
    }

    @Test(
            groups = {"persona.purge.personas"},
            dependsOnGroups = {"persona.create.*", "persona.update.*", "persona.read.*"},
            alwaysRun = true)
    void purgePersonas() throws AtlanException {
        Persona.purge(client, persona.getGuid()).block();
    }

    @Test(
            groups = {"persona.purge.glossary"},
            dependsOnGroups = {"persona.create.*", "persona.read.*", "persona.update.*", "persona.purge.personas"},
            alwaysRun = true)
    void purgeGlossary() throws AtlanException {
        Glossary.purge(client, glossary.getGuid()).block();
    }

    @Test(
            groups = {"persona.purge.connection"},
            dependsOnGroups = {"persona.create.*", "persona.read.*", "persona.update.*", "persona.purge.personas"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }
}
