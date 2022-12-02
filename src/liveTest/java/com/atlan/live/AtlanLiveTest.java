/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;

/**
 * Base class with utility methods and constants for live (integration) tests.
 */
public abstract class AtlanLiveTest {

    private static final String TESTING_STRING = "Automated testing of the Java SDK.";

    public static final AtlanCertificateStatus CERTIFICATE_STATUS = AtlanCertificateStatus.VERIFIED;
    public static final String CERTIFICATE_MESSAGE = TESTING_STRING;

    public static final AtlanAnnouncementType ANNOUNCEMENT_TYPE = AtlanAnnouncementType.INFORMATION;
    public static final String ANNOUNCEMENT_TITLE = "Java SDK testing.";
    public static final String ANNOUNCEMENT_MESSAGE = TESTING_STRING;

    public static final String DESCRIPTION = TESTING_STRING;

    static {
        Atlan.setApiToken(System.getenv("ATLAN_API_KEY"));
        Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"));
        Atlan.setMaxNetworkRetries(60);
    }

    /**
     * Validate an entity update response only includes a single updated entity.
     *
     * @param response the update response to validate
     * @return the single updated entity from the response
     */
    static Entity validateSingleUpdate(EntityMutationResponse response) {
        assertNotNull(response);
        assertTrue(response.getCreatedEntities().isEmpty());
        assertTrue(response.getDeletedEntities().isEmpty());
        assertEquals(response.getUpdatedEntities().size(), 1);
        return response.getUpdatedEntities().get(0);
    }

    /**
     * Validate an entity creation response only includes a single created entity.
     *
     * @param response the create response to validate
     * @return the single created entity from the response
     */
    static Entity validateSingleCreate(EntityMutationResponse response) {
        assertNotNull(response);
        assertTrue(response.getUpdatedEntities().isEmpty());
        assertTrue(response.getDeletedEntities().isEmpty());
        assertEquals(response.getCreatedEntities().size(), 1);
        return response.getCreatedEntities().get(0);
    }
}
