/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import java.util.Collections;
import java.util.List;
import org.testng.annotations.Test;

public class CatalogTest {

    @Test
    void getReference() {
        Catalog one = Catalog.getLineageReference(Column.TYPE_NAME, "one");
        assertTrue(one instanceof Column);
        Catalog two = Catalog.getLineageReference(Column.TYPE_NAME, "one");
        assertTrue(two instanceof Column);
        assertEquals(one, two, "References are consistent when generated from the same inputs.");
    }
}
