/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.Glossary;
import com.atlan.model.enums.*;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Test(groups = {"persona"})
@Slf4j
public class PersonaTest extends AtlanLiveTest {

    private static final String PREFIX = "PersonaTest";

    private static final String PERSONA_NAME = PREFIX;
    private static final String CONNECTION_NAME = "java-sdk-" + PREFIX;
    private static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.GCS;
    private static final String GLOSSARY_NAME = PREFIX;

    private static String personaGuid = null;

    private static Connection connection = null;
    private static Glossary glossary = null;

    @Test(groups = {"create.connection"})
    void createConnection() throws AtlanException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(groups = {"create.glossary"})
    void createGlossary() throws AtlanException {
        glossary = GlossaryTest.createGlossary(GLOSSARY_NAME);
    }

    @Test(groups = {"create.personas"})
    void createPersonas() throws AtlanException {
        Persona persona = Persona.creator(PERSONA_NAME).build();
        Persona result = persona.create();
        assertNotNull(result);
        personaGuid = result.getId();
        assertNotNull(personaGuid);
        assertEquals(result.getDisplayName(), PERSONA_NAME);
    }

    @Test(
            groups = {"read.personas.1"},
            dependsOnGroups = {"create.personas"})
    void retrievePersonas1() throws AtlanException {
        List<Persona> personas = Persona.retrieveAll();
        assertNotNull(personas);
        assertTrue(personas.size() >= 1);
        Persona one = Persona.retrieveByName(PERSONA_NAME);
        assertNotNull(one);
        assertEquals(one.getId(), personaGuid);
    }

    @Test(
            groups = {"update.personas"},
            dependsOnGroups = {"create.personas"})
    void updatePersonas() throws AtlanException {
        Persona persona = Persona.updater(personaGuid, PERSONA_NAME)
                .description("Now with a description!")
                .attributes(Persona.PersonaAttributes.builder()
                        .preferences(Persona.PersonaPreferences.builder()
                                .assetTabDeny(AssetSidebarTab.LINEAGE)
                                .assetTabDeny(AssetSidebarTab.RELATIONS)
                                .assetTabDeny(AssetSidebarTab.QUERIES)
                                .build())
                        .build())
                .build();
        persona.update();
    }

    @Test(
            groups = {"update.personas.policy"},
            dependsOnGroups = {"update.personas", "create.connection", "create.glossary"})
    void addPoliciesToPersona() throws AtlanException {
        Persona persona = Persona.retrieveByName(PERSONA_NAME);
        PersonaMetadataPolicy metadata = PersonaMetadataPolicy.creator(
                        "Simple read access",
                        connection.getGuid(),
                        Set.of(connection.getQualifiedName()),
                        Set.of(PersonaMetadataPolicyAction.READ),
                        true)
                .build();
        AbstractPolicy policy = persona.addPolicy(metadata);
        assertTrue(policy instanceof PersonaMetadataPolicy);
        PersonaDataPolicy data = PersonaDataPolicy.creator(
                        "Allow access to data",
                        connection.getGuid(),
                        Set.of(connection.getQualifiedName()),
                        Set.of(DataPolicyAction.SELECT),
                        true)
                .build();
        policy = persona.addPolicy(data);
        assertTrue(policy instanceof PersonaDataPolicy);
        GlossaryPolicy glossaryPolicy = GlossaryPolicy.creator(
                        "All glossaries",
                        Set.of(glossary.getQualifiedName()),
                        Set.of(GlossaryPolicyAction.CREATE, GlossaryPolicyAction.UPDATE),
                        true)
                .build();
        policy = persona.addPolicy(glossaryPolicy);
        assertTrue(policy instanceof GlossaryPolicy);
    }

    @Test(
            groups = {"read.personas.2"},
            dependsOnGroups = {"update.personas.*"})
    void retrievePersonas2() throws AtlanException {
        Persona one = Persona.retrieveByName(PERSONA_NAME);
        assertNotNull(one);
        assertEquals(one.getId(), personaGuid);
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
        assertEquals(one.getGlossaryPolicies().size(), 1);
    }

    @Test(
            groups = {"purge.personas"},
            dependsOnGroups = {"create.*", "update.*", "read.*"},
            alwaysRun = true)
    void purgePersonas() throws AtlanException {
        Persona.delete(personaGuid);
    }

    @Test(
            groups = {"purge.glossary"},
            dependsOnGroups = {"create.*", "read.*", "update.*", "purge.personas"},
            alwaysRun = true)
    void purgeGlossary() throws AtlanException {
        Glossary.purge(glossary.getGuid());
    }

    @Test(
            groups = {"purge.connection"},
            dependsOnGroups = {"create.*", "read.*", "update.*", "purge.personas"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
