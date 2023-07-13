/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.util.Random;

/**
 * Base class with utility methods and constants for live (integration) tests.
 */
public abstract class AtlanLiveTest {

    private static final char[] ALPHABET = {
        '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
        'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    private static final String NANO_ID = NanoIdUtils.randomNanoId(new Random(), ALPHABET, 5);
    private static final String PREFIX = "jsdk_";

    private static final String TESTING_STRING = "Automated testing of the Java SDK.";

    public static final CertificateStatus CERTIFICATE_STATUS = CertificateStatus.VERIFIED;
    public static final String CERTIFICATE_MESSAGE = TESTING_STRING;

    public static final AtlanAnnouncementType ANNOUNCEMENT_TYPE = AtlanAnnouncementType.INFORMATION;
    public static final String ANNOUNCEMENT_TITLE = "Java SDK testing.";
    public static final String ANNOUNCEMENT_MESSAGE = TESTING_STRING;

    public static final String DESCRIPTION = TESTING_STRING;

    static {
        Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"));
        Atlan.setApiToken(System.getenv("ATLAN_API_KEY"));
        Atlan.setMaxNetworkRetries(20);
    }

    protected static String makeUnique(String input) {
        return PREFIX + input + "_" + NANO_ID;
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
