/* SPDX-License-Identifier: Apache-2.0
   Copyright 2025 Atlan Pte. Ltd. */
package com.atlan.net;

import static org.testng.Assert.*;

import com.atlan.exception.ApiConnectionException;
import com.atlan.exception.AtlanException;
import com.atlan.mock.MockAtlanTenant;
import com.atlan.mock.MockTenant;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link HttpURLConnectionClient}, specifically for the error-stream fallback logic.
 */
public class HttpURLConnectionClientTest {

    @BeforeClass
    void init() throws InterruptedException {
        MockAtlanTenant.initializeClient();
    }

    /**
     * Regression test for PLA2-36: a 401 response with no body must be returned as an
     * {@link AtlanResponseStream} with code 401 instead of being converted into an
     * {@link ApiConnectionException}.  Prior to the fix, {@code getInputStream()} was called for
     * the fallback path, which throws {@link java.io.IOException} for non-2xx status codes; the
     * resulting exception hid the 401 from the retry logic so the OAuth token was never refreshed.
     */
    @Test
    void testOAuth401WithNoBodyReturnsResponseCode() throws AtlanException {
        HttpURLConnectionClient httpClient = new HttpURLConnectionClient();
        AtlanRequest request = new AtlanRequest(
                MockTenant.client,
                ApiResource.RequestMethod.GET,
                MockTenant.client.getBaseUrl() + "/api/meta/test/oauth-401-no-body",
                (String) null,
                null,
                "test-request-id");

        // The key assertion: must NOT throw an exception; must return a stream with the 401 code.
        AtlanResponseStream response = httpClient.requestStream(request);
        assertNotNull(response, "Response stream must not be null for a 401 with no body");
        assertEquals(response.code(), 401, "Response code must be 401 so retry logic can trigger OAuth refresh");
    }
}
