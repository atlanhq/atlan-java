/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.enums.AssetSidebarTab;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

@Test(groups = {"persona"})
public class PersonaTest extends AtlanLiveTest {

    public static final String PERSONA_NAME = "JavaClient Persona";

    public static String personaGuid = null;

    @Test(groups = {"create.personas"})
    void createPersonas() {
        try {
            Persona persona = Persona.creator(PERSONA_NAME).build();
            Persona result = persona.create();
            assertNotNull(result);
            personaGuid = result.getId();
            assertNotNull(personaGuid);
            assertEquals(result.getDisplayName(), PERSONA_NAME);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when creating personas.");
        }
    }

    @Test(
            groups = {"read.personas.1"},
            dependsOnGroups = {"create.personas"})
    void retrievePersonas1() {
        try {
            List<Persona> personas = Persona.retrieveAll();
            assertNotNull(personas);
            assertTrue(personas.size() >= 1);
            Persona one = Persona.retrieveByName(PERSONA_NAME);
            assertNotNull(one);
            assertEquals(one.getId(), personaGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when retrieving personas.");
        }
    }

    @Test(
            groups = {"update.personas"},
            dependsOnGroups = {"create.personas"})
    void updatePersonas() {
        try {
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
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when updating personas.");
        }
    }

    @Test(
            groups = {"read.personas.2"},
            dependsOnGroups = {"update.personas"})
    void retrievePersonas2() {
        try {
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
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when retrieving personas.");
        }
    }

    @Test(
            groups = {"purge.personas"},
            // TODO        dependsOnGroups = {"create.*", "update.*", "read.*", "search.*", "link.*", "unlink.*"},
            dependsOnGroups = {"create.*", "update.*", "read.*"},
            alwaysRun = true)
    void purgePersonas() {
        try {
            Persona.delete(personaGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when deleting personas.");
        }
    }
}
