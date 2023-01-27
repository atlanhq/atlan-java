/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.AtlanConnectorType;
import java.util.Collections;
import org.testng.annotations.Test;

public class AbstractProcessTest {

    @Test
    void generateQualifiedName() {
        final String qn1_hash = "four/643892f7e1e3af8e141b0f75070a4321";
        String qn1 = AbstractProcess.generateQualifiedName(
                "one",
                AtlanConnectorType.S3,
                "three",
                "four",
                Collections.singletonList(S3Object.refByGuid("six")),
                Collections.singletonList(Table.refByGuid("eight")),
                null);
        String qn2 = AbstractProcess.generateQualifiedName(
                "one",
                AtlanConnectorType.S3,
                "three",
                "four",
                Collections.singletonList(S3Object.refByGuid("six")),
                Collections.singletonList(Table.refByGuid("eight")),
                null);
        String qn3 = AbstractProcess.generateQualifiedName(
                "one",
                AtlanConnectorType.S3,
                "three",
                "four",
                Collections.singletonList(S3Object.refByGuid("six")),
                Collections.singletonList(Table.refByGuid("eighs")),
                null);
        assertNotNull(qn1);
        assertNotNull(qn2);
        assertNotNull(qn3);
        assertEquals(qn1, qn1_hash, "Hashes are consistent across time and platforms.");
        assertEquals(qn1, qn2, "Hashes are consistent when generated from the same inputs.");
        assertNotEquals(qn2, qn3, "Hashes with only a single letter difference are different.");
    }

    @Test
    void generateQualifiedNameColumnLevel() {
        final String qn1_hash = "four/6c58211472c0fda1e4fcfb6b73a2785e";
        String qn1 = AbstractProcess.generateQualifiedName(
                "one",
                AtlanConnectorType.S3,
                "three",
                "four",
                Collections.singletonList(S3Object.refByGuid("six")),
                Collections.singletonList(Table.refByGuid("eight")),
                LineageProcess.refByGuid("ten"));
        String qn2 = AbstractProcess.generateQualifiedName(
                "one",
                AtlanConnectorType.S3,
                "three",
                "four",
                Collections.singletonList(S3Object.refByGuid("six")),
                Collections.singletonList(Table.refByGuid("eight")),
                LineageProcess.refByGuid("ten"));
        String qn3 = AbstractProcess.generateQualifiedName(
                "one",
                AtlanConnectorType.S3,
                "three",
                "four",
                Collections.singletonList(S3Object.refByGuid("six")),
                Collections.singletonList(Table.refByGuid("eight")),
                LineageProcess.refByGuid("teo"));
        assertNotNull(qn1);
        assertNotNull(qn2);
        assertNotNull(qn3);
        assertEquals(qn1, qn1_hash, "Hashes are consistent across time and platforms.");
        assertEquals(qn1, qn2, "Hashes are consistent when generated from the same inputs.");
        assertNotEquals(qn2, qn3, "Hashes with only a single letter difference are different.");
    }
}
