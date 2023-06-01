/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanTagColor;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.AtlanTagDef;

/**
 * Utility methods for managing Atlan tags during testing.
 */
public abstract class AtlanTagTest extends AtlanLiveTest {

    /**
     * Create a new Atlan tag with a unique name.
     *
     * @param name to make the Atlan tag unique
     * @throws AtlanException on any error creating or reading-back the Atlan tag
     */
    static void createAtlanTag(String name) throws AtlanException {
        AtlanTagDef atlanTagDef = AtlanTagDef.creator(name, AtlanTagColor.GREEN).build();
        AtlanTagDef response = atlanTagDef.create();
        assertNotNull(response);
        assertEquals(response.getCategory(), AtlanTypeCategory.ATLAN_TAG);
        String uniqueName = response.getName();
        assertNotNull(uniqueName);
        assertNotEquals(uniqueName, name);
        assertNotNull(response.getGuid());
        assertEquals(response.getDisplayName(), name);
    }

    /**
     * Delete the Atlan tag with the provided name.
     *
     * @param name of the Atlan tag to delete
     * @throws AtlanException on any error deleting the Atlan tag
     */
    static void deleteAtlanTag(String name) throws AtlanException {
        AtlanTagDef.purge(name);
    }
}
