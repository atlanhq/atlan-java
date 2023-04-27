/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;

/**
 * Base class with utility methods and constants for live (integration) tests.
 */
public abstract class AtlanLiveTest {

    protected static final String PREFIX = "jsdk-";

    private static final String TESTING_STRING = "Automated testing of the Java SDK.";

    public static final CertificateStatus CERTIFICATE_STATUS = CertificateStatus.VERIFIED;
    public static final String CERTIFICATE_MESSAGE = TESTING_STRING;

    public static final AtlanAnnouncementType ANNOUNCEMENT_TYPE = AtlanAnnouncementType.INFORMATION;
    public static final String ANNOUNCEMENT_TITLE = "Java SDK testing.";
    public static final String ANNOUNCEMENT_MESSAGE = TESTING_STRING;

    public static final String DESCRIPTION = TESTING_STRING;

    static {
        Atlan.setApiToken(System.getenv("ATLAN_API_KEY"));
        Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"));
        Atlan.setMaxNetworkRetries(20);
    }

    /**
     * Validate an asset update response only includes a single updated asset.
     *
     * @param response the update response to validate
     * @return the single updated asset from the response
     */
    protected static Asset validateSingleUpdate(AssetMutationResponse response) {
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        return response.getUpdatedAssets().get(0);
    }

    /**
     * Validate an asset creation response only includes a single created asset.
     *
     * @param response the create response to validate
     * @return the single created asset from the response
     */
    protected static Asset validateSingleCreate(AssetMutationResponse response) {
        assertNotNull(response);
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        return response.getCreatedAssets().get(0);
    }
}
