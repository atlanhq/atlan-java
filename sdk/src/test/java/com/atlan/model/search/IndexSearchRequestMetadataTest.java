/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.model.search;

import static org.testng.Assert.*;

import com.atlan.model.enums.UTMTags;
import java.util.List;
import org.testng.annotations.Test;

public class IndexSearchRequestMetadataTest {

    @Test
    void saveSearchLogOnlyWhenSettingIsTrue() {
        assertFalse(IndexSearchRequest.Metadata.saveSearchLogDefault(null));
        assertFalse(IndexSearchRequest.Metadata.saveSearchLogDefault(""));
        assertFalse(IndexSearchRequest.Metadata.saveSearchLogDefault("false"));
        assertFalse(IndexSearchRequest.Metadata.saveSearchLogDefault("1"));
        assertTrue(IndexSearchRequest.Metadata.saveSearchLogDefault("true"));
        assertTrue(IndexSearchRequest.Metadata.saveSearchLogDefault(" TRUE "));
    }

    @Test
    void followUpRequestsAreNeverLogged() {
        IndexSearchRequest.Metadata metadata = IndexSearchRequest.Metadata.followUp();
        assertFalse(metadata.getSaveSearchLog());
        assertEquals(metadata.getUtmTags(), List.of(UTMTags.PROJECT_SDK_JAVA.getValue()));
    }

    @Test
    void explicitSettingWins() {
        IndexSearchRequest.Metadata metadata =
                IndexSearchRequest.Metadata.builder().saveSearchLog(true).build();
        assertTrue(metadata.getSaveSearchLog());
    }
}
