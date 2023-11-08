/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.*;
import com.atlan.model.typedefs.AtlanTagDef;
import com.atlan.model.typedefs.AtlanTagOptions;
import org.testng.annotations.Test;

/**
 * Utility methods for managing Atlan tags during testing.
 */
public class AtlanTagTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("tag");
    private static final String TAG_WITH_IMAGE = PREFIX + "_image";
    private static final String TAG_WITH_ICON = PREFIX + "_icon";
    private static final String TAG_WITH_EMOJI = PREFIX + "_emoji";

    /**
     * Create a new Atlan tag with a unique name.
     *
     * @param name to make the Atlan tag unique
     * @throws AtlanException on any error creating or reading-back the Atlan tag
     */
    static void createAtlanTag(String name) throws AtlanException {
        createAtlanTag(Atlan.getDefaultClient(), name);
    }

    /**
     * Create a new Atlan tag with a unique name.
     *
     * @param client connectivity to the Atlan tenant in which to create the tag
     * @param name to make the Atlan tag unique
     * @throws AtlanException on any error creating or reading-back the Atlan tag
     */
    static void createAtlanTag(AtlanClient client, String name) throws AtlanException {
        AtlanTagDef atlanTagDef = AtlanTagDef.creator(name, AtlanTagColor.GREEN).build();
        AtlanTagDef response = atlanTagDef.create(client);
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

    @Test(groups = {"tag.create.image"})
    void createTagWithImage() throws AtlanException {
        AtlanTagDef tag = AtlanTagDef.creator(
                        TAG_WITH_IMAGE,
                        "https://github.com/great-expectations/great_expectations/raw/develop/docs/docusaurus/static/img/gx-mark-160.png")
                .build();
        AtlanTagDef response = tag.create(Atlan.getDefaultClient());
        assertNotNull(response);
        assertEquals(response.getCategory(), AtlanTypeCategory.ATLAN_TAG);
        String uniqueName = response.getName();
        assertNotNull(uniqueName);
        assertNotEquals(uniqueName, TAG_WITH_IMAGE);
        assertNotNull(response.getGuid());
        assertEquals(response.getDisplayName(), TAG_WITH_IMAGE);
        assertNotNull(response.getOptions());
        assertNotNull(response.getOptions().getImageID());
        assertEquals(response.getOptions().getIconType(), TagIconType.IMAGE);
    }

    @Test(groups = {"tag.create.icon"})
    void createTagWithIcon() throws AtlanException {
        AtlanTagDef tag = AtlanTagDef.creator(TAG_WITH_ICON, AtlanIcon.BOOK_BOOKMARK, AtlanTagColor.YELLOW)
                .build();
        AtlanTagDef response = tag.create(Atlan.getDefaultClient());
        assertNotNull(response);
        assertEquals(response.getCategory(), AtlanTypeCategory.ATLAN_TAG);
        String uniqueName = response.getName();
        assertNotNull(uniqueName);
        assertNotEquals(uniqueName, TAG_WITH_ICON);
        assertNotNull(response.getGuid());
        assertEquals(response.getDisplayName(), TAG_WITH_ICON);
        assertEquals(response.getOptions().getIconType(), TagIconType.ICON);
    }

    @Test(groups = {"tag.create.emoji"})
    void createTagWithEmoji() throws AtlanException {
        AtlanTagDef tag = AtlanTagDef.creator(TAG_WITH_EMOJI, AtlanTagOptions.withEmoji("\uD83D\uDC4D"))
                .build();
        AtlanTagDef response = tag.create(Atlan.getDefaultClient());
        assertNotNull(response);
        assertEquals(response.getCategory(), AtlanTypeCategory.ATLAN_TAG);
        String uniqueName = response.getName();
        assertNotNull(uniqueName);
        assertNotEquals(uniqueName, TAG_WITH_EMOJI);
        assertNotNull(response.getGuid());
        assertEquals(response.getDisplayName(), TAG_WITH_EMOJI);
        assertEquals(response.getOptions().getIconType(), TagIconType.EMOJI);
    }

    @Test(
            groups = {"tag.purge"},
            dependsOnGroups = {"tag.create.*"},
            alwaysRun = true)
    void purgeTags() throws AtlanException {
        deleteAtlanTag(TAG_WITH_ICON);
        deleteAtlanTag(TAG_WITH_IMAGE);
        deleteAtlanTag(TAG_WITH_EMOJI);
    }
}
