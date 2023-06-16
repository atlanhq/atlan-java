/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

public class CatalogTest {

    @Test
    void getReference() {
        ICatalog one = Catalog.getLineageReference(Column.TYPE_NAME, "one");
        assertTrue(one instanceof IColumn);
        ICatalog two = Catalog.getLineageReference(Column.TYPE_NAME, "one");
        assertTrue(two instanceof IColumn);
        assertEquals(one, two, "References are consistent when generated from the same inputs.");
    }
}
