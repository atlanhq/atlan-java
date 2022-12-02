/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanClassificationColor;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.ClassificationDef;

/**
 * Utility methods for managing classifications during testing.
 */
public abstract class ClassificationTest extends AtlanLiveTest {

    /**
     * Create a new classification with a unique name.
     *
     * @param name to make the classification unique
     * @throws AtlanException on any error creating or reading-back the classification
     */
    static void createClassification(String name) throws AtlanException {
        ClassificationDef classificationDef = ClassificationDef.toCreate(name, AtlanClassificationColor.GREEN);
        ClassificationDef response = classificationDef.create();
        assertNotNull(response);
        assertEquals(response.getCategory(), AtlanTypeCategory.CLASSIFICATION);
        String uniqueName = response.getName();
        assertNotNull(uniqueName);
        assertNotEquals(uniqueName, name);
        assertNotNull(response.getGuid());
        assertEquals(response.getDisplayName(), name);
    }

    /**
     * Delete the classification with the provided name.
     *
     * @param name of the classification to delete
     * @throws AtlanException on any error deleting the classification
     */
    static void deleteClassification(String name) throws AtlanException {
        ClassificationDef.purge(name);
    }
}
