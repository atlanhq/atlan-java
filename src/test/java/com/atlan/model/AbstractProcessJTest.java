package com.atlan.model;

import static org.testng.Assert.*;

import com.atlan.model.relations.ReferenceJ;
import java.util.Collections;
import org.testng.annotations.Test;

public class AbstractProcessJTest {

    @Test
    void generateQualifiedName() {
        final String qn1_hash = "546514cb0b4bc8c2ca746d88b5501423";
        String qn1 = AbstractProcessJ.generateQualifiedName(
                "one",
                "two",
                "three",
                "four",
                Collections.singletonList(ReferenceJ.to("five", "six")),
                Collections.singletonList(ReferenceJ.to("seven", "eight")),
                null);
        String qn2 = AbstractProcessJ.generateQualifiedName(
                "one",
                "two",
                "three",
                "four",
                Collections.singletonList(ReferenceJ.to("five", "six")),
                Collections.singletonList(ReferenceJ.to("seven", "eight")),
                null);
        String qn3 = AbstractProcessJ.generateQualifiedName(
                "one",
                "two",
                "three",
                "four",
                Collections.singletonList(ReferenceJ.to("five", "six")),
                Collections.singletonList(ReferenceJ.to("seven", "eighs")),
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
        final String qn1_hash = "e8149d04a290112d586a7d9738c5a682";
        String qn1 = AbstractProcessJ.generateQualifiedName(
                "one",
                "two",
                "three",
                "four",
                Collections.singletonList(ReferenceJ.to("five", "six")),
                Collections.singletonList(ReferenceJ.to("seven", "eight")),
                ReferenceJ.to("nine", "ten"));
        String qn2 = AbstractProcessJ.generateQualifiedName(
                "one",
                "two",
                "three",
                "four",
                Collections.singletonList(ReferenceJ.to("five", "six")),
                Collections.singletonList(ReferenceJ.to("seven", "eight")),
                ReferenceJ.to("nine", "ten"));
        String qn3 = AbstractProcessJ.generateQualifiedName(
                "one",
                "two",
                "three",
                "four",
                Collections.singletonList(ReferenceJ.to("five", "six")),
                Collections.singletonList(ReferenceJ.to("seven", "eight")),
                ReferenceJ.to("nine", "teo"));
        assertNotNull(qn1);
        assertNotNull(qn2);
        assertNotNull(qn3);
        assertEquals(qn1, qn1_hash, "Hashes are consistent across time and platforms.");
        assertEquals(qn1, qn2, "Hashes are consistent when generated from the same inputs.");
        assertNotEquals(qn2, qn3, "Hashes with only a single letter difference are different.");
    }
}
