/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import java.util.Collections;
import java.util.List;
import org.testng.annotations.Test;

public class AbstractProcessTest {

    private static final String connectionQualifiedName = "default/s3/1234567890";

    @Test
    void generateQualifiedName() {
        final String qn1_hash = "default/s3/1234567890/c8677615a623753c85dbf57704972a1e";
        String qn1 = LineageProcess.generateQualifiedName(
                "one",
                connectionQualifiedName,
                null,
                Collections.singletonList(S3Object.refByGuid("six")),
                Collections.singletonList(Table.refByGuid("eight")),
                null);
        String qn2 = LineageProcess.generateQualifiedName(
                "one",
                connectionQualifiedName,
                null,
                Collections.singletonList(S3Object.refByGuid("six")),
                Collections.singletonList(Table.refByGuid("eight")),
                null);
        String qn3 = LineageProcess.generateQualifiedName(
                "one",
                connectionQualifiedName,
                null,
                Collections.singletonList(S3Object.refByGuid("six")),
                Collections.singletonList(Table.refByGuid("eighs")),
                null);
        System.out.println("QN1_hash: " + qn1);
        assertNotNull(qn1);
        assertNotNull(qn2);
        assertNotNull(qn3);
        assertEquals(qn1, qn1_hash, "Hashes are consistent across time and platforms.");
        assertEquals(qn1, qn2, "Hashes are consistent when generated from the same inputs.");
        assertNotEquals(qn2, qn3, "Hashes with only a single letter difference are different.");
    }

    @Test
    void generateQualifiedNameColumnLevel() {
        final String qn1_hash = "default/s3/1234567890/6c75e1468822c2ef2089d05688950f3a";
        String qn1 = LineageProcess.generateQualifiedName(
                "one",
                connectionQualifiedName,
                null,
                Collections.singletonList(S3Object.refByGuid("six")),
                Collections.singletonList(Table.refByGuid("eight")),
                LineageProcess.refByGuid("ten"));
        String qn2 = LineageProcess.generateQualifiedName(
                "one",
                connectionQualifiedName,
                null,
                Collections.singletonList(S3Object.refByGuid("six")),
                Collections.singletonList(Table.refByGuid("eight")),
                LineageProcess.refByGuid("ten"));
        System.out.println("QN1_hash: " + qn1);
        String qn3 = LineageProcess.generateQualifiedName(
                "one",
                connectionQualifiedName,
                null,
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

    @Test
    void generateQualifiedNameWithId() {
        final String processId = "two";
        final String qn1_static = connectionQualifiedName + "/" + processId;
        String qn1 = LineageProcess.generateQualifiedName(
                "one",
                connectionQualifiedName,
                processId,
                List.of(S3Object.refByGuid("three"), GCSObject.refByGuid("four")),
                List.of(Table.refByGuid("five"), View.refByGuid("six")),
                null);
        String qn2 = LineageProcess.generateQualifiedName(
                "nine",
                connectionQualifiedName,
                processId,
                List.of(S3Object.refByGuid("seven"), GCSObject.refByGuid("eight")),
                List.of(Table.refByGuid("nine"), View.refByGuid("ten")),
                null);
        String qn3 = LineageProcess.generateQualifiedName(
                "one",
                connectionQualifiedName,
                "twp",
                List.of(S3Object.refByGuid("three"), GCSObject.refByGuid("four")),
                List.of(Table.refByGuid("five"), View.refByGuid("six")),
                null);
        assertNotNull(qn1);
        assertNotNull(qn2);
        assertEquals(qn1, qn1_static, "Static ID'd processes are consistent across time and platforms.");
        assertEquals(
                qn1, qn2, "Processes with IDs are the same even when generated from different inputs and outputs.");
        assertNotEquals(qn2, qn3, "Processes with only a single letter difference are different.");
    }
}
