/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.util;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.mock.MockAtlanTenant;
import com.atlan.mock.MockTenant;
import com.atlan.model.assets.Table;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.AtlanTagHandling;
import com.atlan.model.enums.CustomMetadataHandling;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for AssetBatch's reporting of archived (soft-deleted) assets that an update-only batch restores.
 * The search and save responses are served by the WireMock mappings in src/test/resources/wiremock/mappings.
 */
public class AssetBatchTest {

    private static final String CONNECTION_QN = "default/snowflake/1700000000";
    private static final String ARCHIVED_QN = CONNECTION_QN + "/DB/SCH/ARCHIVED_TBL";
    private static final String LIVE_QN = CONNECTION_QN + "/DB/SCH/LIVE_TBL";
    private static final String NO_STATUS_QN = CONNECTION_QN + "/DB/SCH/NOSTATUS_TBL";

    @BeforeClass
    void init() throws InterruptedException {
        MockAtlanTenant.initializeClient();
    }

    private static AssetBatch updateOnlyBatch() {
        return new AssetBatch(
                MockTenant.client, 20, AtlanTagHandling.IGNORE, CustomMetadataHandling.IGNORE, false, true);
    }

    @Test
    void updateOnlyCountsRestoredArchivedAsset() throws AtlanException {
        try (AssetBatch batch = updateOnlyBatch()) {
            batch.add(Table.updater(ARCHIVED_QN, "ARCHIVED_TBL")
                    .status(AtlanStatus.ACTIVE)
                    .build());
            batch.flush();
            // The archived table is still matched and updated (behaviour unchanged) ...
            assertEquals(batch.getNumUpdated().get(), 1L);
            assertEquals(batch.getNumSkipped().get(), 0L);
            // ... but it is now reported as an archived asset that was restored
            assertEquals(batch.getNumArchivedRestored().get(), 1L);
        }
    }

    @Test
    void updateOnlyDoesNotCountActiveAsset() throws AtlanException {
        try (AssetBatch batch = updateOnlyBatch()) {
            batch.add(Table.updater(LIVE_QN, "LIVE_TBL")
                    .status(AtlanStatus.ACTIVE)
                    .build());
            batch.flush();
            assertEquals(batch.getNumUpdated().get(), 1L);
            assertEquals(batch.getNumArchivedRestored().get(), 0L);
        }
    }

    @Test
    void updateOnlyDoesNotCountArchivedAssetSentWithoutActiveStatus() throws AtlanException {
        try (AssetBatch batch = updateOnlyBatch()) {
            batch.add(Table.updater(NO_STATUS_QN, "NOSTATUS_TBL").build());
            batch.flush();
            assertEquals(batch.getNumUpdated().get(), 1L);
            assertEquals(batch.getNumArchivedRestored().get(), 0L);
        }
    }
}
