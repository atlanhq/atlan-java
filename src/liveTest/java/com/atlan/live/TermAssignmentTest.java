/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.assets.GlossaryTerm;
import com.atlan.model.assets.S3Object;
import com.atlan.model.enums.AtlanStatus;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

public class TermAssignmentTest extends AtlanLiveTest {

    @Test(
            groups = {"link.asset.term"},
            dependsOnGroups = {"create.*"})
    void linkAssetToTerms() {
        try {
            S3Object result = S3Object.replaceTerms(
                    S3AssetTest.s3Object1Qame,
                    S3AssetTest.S3_OBJECT1_NAME,
                    List.of(GlossaryTerm.refByGuid(GlossaryTest.termGuid1)));
            assertNotNull(result);
            S3Object s3Object1 = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(s3Object1);
            assertTrue(s3Object1.isComplete());
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            Set<GlossaryTerm> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 1);
            Set<AtlanStatus> statuses =
                    terms.stream().map(GlossaryTerm::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(statuses.size(), 1);
            assertTrue(statuses.contains(AtlanStatus.ACTIVE));
            Set<String> types = terms.stream().map(GlossaryTerm::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(GlossaryTerm.TYPE_NAME));
            Set<String> guids = terms.stream().map(GlossaryTerm::getGuid).collect(Collectors.toSet());
            assertEquals(guids.size(), 1);
            assertTrue(guids.contains(GlossaryTest.termGuid1));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to link an asset to a term.");
        }
    }

    @Test(
            groups = {"unlink.asset.term"},
            dependsOnGroups = {"link.asset.term", "search.s3object.term.fromAsset"})
    void removeAssetToTermLinks() {
        try {
            S3Object result = S3Object.replaceTerms(S3AssetTest.s3Object1Qame, S3AssetTest.S3_OBJECT1_NAME, null);
            assertNotNull(result);
            S3Object s3Object1 = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(s3Object1);
            assertTrue(s3Object1.isComplete());
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            Set<GlossaryTerm> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 1);
            Set<AtlanStatus> status =
                    terms.stream().map(GlossaryTerm::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 1);
            assertTrue(status.contains(AtlanStatus.DELETED));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove assigned terms from an asset.");
        }
    }

    @Test(
            groups = {"link.asset.term.1"},
            dependsOnGroups = {"unlink.asset.term"})
    void linkAssetToTerm1() {
        try {
            S3Object result = S3Object.appendTerms(
                    S3AssetTest.s3Object1Qame, List.of(GlossaryTerm.refByGuid(GlossaryTest.termGuid1)));
            assertNotNull(result);
            S3Object s3Object1 = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(s3Object1);
            assertTrue(s3Object1.isComplete());
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            Set<GlossaryTerm> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 2);
            Set<AtlanStatus> status =
                    terms.stream().map(GlossaryTerm::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 2);
            assertTrue(status.contains(AtlanStatus.ACTIVE));
            assertTrue(status.contains(AtlanStatus.DELETED));
            for (GlossaryTerm term : terms) {
                if (term.getRelationshipStatus() == AtlanStatus.ACTIVE) {
                    assertEquals(term.getGuid(), GlossaryTest.termGuid1);
                }
            }
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to re-assign a term to an asset.");
        }
    }

    @Test(
            groups = {"link.asset.term.2"},
            dependsOnGroups = {"link.asset.term.1"})
    void linkAssetToTerm2() {
        try {
            S3Object result = S3Object.appendTerms(
                    S3AssetTest.s3Object1Qame, List.of(GlossaryTerm.refByGuid(GlossaryTest.termGuid2)));
            assertNotNull(result);
            S3Object s3Object1 = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(s3Object1);
            assertTrue(s3Object1.isComplete());
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            Set<GlossaryTerm> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 3);
            Set<AtlanStatus> status =
                    terms.stream().map(GlossaryTerm::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 2);
            assertTrue(status.contains(AtlanStatus.ACTIVE));
            assertTrue(status.contains(AtlanStatus.DELETED));
            for (GlossaryTerm term : terms) {
                if (term.getRelationshipStatus() == AtlanStatus.ACTIVE) {
                    assertTrue(term.getGuid().equals(GlossaryTest.termGuid1)
                            || term.getGuid().equals(GlossaryTest.termGuid2));
                }
            }
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to assign an additional term to an asset.");
        }
    }

    @Test(
            groups = {"unlink.asset.term.1"},
            dependsOnGroups = {"link.asset.term.2"})
    void unlinkAssetToTerm1() {
        try {
            S3Object result = S3Object.removeTerms(
                    S3AssetTest.s3Object1Qame, List.of(GlossaryTerm.refByGuid(GlossaryTest.termGuid1)));
            assertNotNull(result);
            S3Object s3Object1 = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(s3Object1);
            assertTrue(s3Object1.isComplete());
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            Set<GlossaryTerm> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 3);
            Set<AtlanStatus> status =
                    terms.stream().map(GlossaryTerm::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 2);
            assertTrue(status.contains(AtlanStatus.ACTIVE));
            assertTrue(status.contains(AtlanStatus.DELETED));
            for (GlossaryTerm term : terms) {
                if (term.getRelationshipStatus() == AtlanStatus.ACTIVE) {
                    assertEquals(term.getGuid(), GlossaryTest.termGuid2);
                }
            }
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove an assigned term from an asset.");
        }
    }

    @Test(
            groups = {"unlink.asset.term.2"},
            dependsOnGroups = {"unlink.asset.term.1"})
    void unlinkAssetToTerm2() {
        try {
            S3Object result = S3Object.removeTerms(
                    S3AssetTest.s3Object1Qame, List.of(GlossaryTerm.refByGuid(GlossaryTest.termGuid2)));
            assertNotNull(result);
            S3Object s3Object1 = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(s3Object1);
            assertTrue(s3Object1.isComplete());
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            Set<GlossaryTerm> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 3);
            Set<AtlanStatus> status =
                    terms.stream().map(GlossaryTerm::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 1);
            assertTrue(status.contains(AtlanStatus.DELETED));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove another assigned term from an asset.");
        }
    }
}
