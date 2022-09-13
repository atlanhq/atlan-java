/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.lineage.AbstractProcess;
import com.atlan.model.relations.Reference;
import java.util.Collections;
import org.testng.annotations.Test;

public class AbstractProcessTest {

    @Test
    void generateQualifiedName() {
        final String qn1_hash = "643892f7e1e3af8e141b0f75070a4321";
        String qn1 = AbstractProcess.generateQualifiedName(
                "one",
                AtlanConnectorType.S3,
                "three",
                "four",
                Collections.singletonList(Reference.to("five", "six")),
                Collections.singletonList(Reference.to("seven", "eight")),
                null);
        String qn2 = AbstractProcess.generateQualifiedName(
                "one",
                AtlanConnectorType.S3,
                "three",
                "four",
                Collections.singletonList(Reference.to("five", "six")),
                Collections.singletonList(Reference.to("seven", "eight")),
                null);
        String qn3 = AbstractProcess.generateQualifiedName(
                "one",
                AtlanConnectorType.S3,
                "three",
                "four",
                Collections.singletonList(Reference.to("five", "six")),
                Collections.singletonList(Reference.to("seven", "eighs")),
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
        final String qn1_hash = "6c58211472c0fda1e4fcfb6b73a2785e";
        String qn1 = AbstractProcess.generateQualifiedName(
                "one",
                AtlanConnectorType.S3,
                "three",
                "four",
                Collections.singletonList(Reference.to("five", "six")),
                Collections.singletonList(Reference.to("seven", "eight")),
                Reference.to("nine", "ten"));
        String qn2 = AbstractProcess.generateQualifiedName(
                "one",
                AtlanConnectorType.S3,
                "three",
                "four",
                Collections.singletonList(Reference.to("five", "six")),
                Collections.singletonList(Reference.to("seven", "eight")),
                Reference.to("nine", "ten"));
        String qn3 = AbstractProcess.generateQualifiedName(
                "one",
                AtlanConnectorType.S3,
                "three",
                "four",
                Collections.singletonList(Reference.to("five", "six")),
                Collections.singletonList(Reference.to("seven", "eight")),
                Reference.to("nine", "teo"));
        assertNotNull(qn1);
        assertNotNull(qn2);
        assertNotNull(qn3);
        assertEquals(qn1, qn1_hash, "Hashes are consistent across time and platforms.");
        assertEquals(qn1, qn2, "Hashes are consistent when generated from the same inputs.");
        assertNotEquals(qn2, qn3, "Hashes with only a single letter difference are different.");
    }
}
