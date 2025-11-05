/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.LogicException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.search.AuditSearchRequest;
import com.atlan.model.search.AuditSearchResponse;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.net.HttpClient;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.io.IOException;
import java.util.Random;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * Base class with utility methods and constants for live (integration) tests.
 */
public abstract class AtlanLiveTest {

    // TODO: This cannot be dynamic because a user must first be verified
    //  before they can be linked — so we must use a hard-coded value for
    //  a username that we know is verified and active in the environment
    public static final String FIXED_USER = "chris";

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

    protected final AtlanClient client = new AtlanClient();

    @BeforeClass
    public void setLogName() {
        // TODO: this doesn't really work -- different tests' entries are showing up in others' log files
        //  when run in parallel (works fine for running tests individually)
        ThreadContext.put("className", this.getClass().getSimpleName());
        client.setMaxNetworkRetries(10);
    }

    @AfterClass
    public void teardown() throws IOException {
        client.close();
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
    protected Asset validateSingleUpdate(AssetMutationResponse response) {
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

    /**
     * Validate an asset was deleted as expected, and if not log out full details of its activity
     * log to see what could be the culprit.
     *
     * @param toValidate the asset that should be deleted
     * @param log into which to write full details if the asset was not deleted as expected
     * @throws AtlanException on any API communication issues
     */
    protected void validateDeletedAsset(Asset toValidate, Logger log) throws AtlanException {
        Asset deleted = Asset.get(client, toValidate.getGuid(), false);
        assertNotNull(deleted);
        assertEquals(deleted.getGuid(), toValidate.getGuid());
        assertEquals(deleted.getQualifiedName(), toValidate.getQualifiedName());
        assertEquals(deleted.getTypeName(), toValidate.getTypeName());
        int count = 0;
        try {
            while (deleted.getStatus() != AtlanStatus.DELETED && count < client.getMaxNetworkRetries()) {
                log.debug(
                        "Asset that should be deleted is not -- retrying (#{}): {}::{}",
                        count,
                        deleted.getTypeName(),
                        deleted.getQualifiedName());
                Thread.sleep(HttpClient.waitTime(count).toMillis());
                deleted = Asset.get(client, toValidate.getGuid(), false);
                count++;
            }
        } catch (InterruptedException e) {
            throw new LogicException(ErrorCode.ERROR_PASSTHROUGH, e);
        }
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    /**
     * Since search is eventually consistent, retry it until we arrive at the number of results
     * we expect (or hit the retry limit).
     *
     * @param request search request to run
     * @param expectedSize expected number of results from the search
     * @return the response, either with the expected number of results or after exceeding the retry limit
     * @throws AtlanException on any API communication issues
     * @throws InterruptedException if the busy-wait loop for retries is interrupted
     */
    protected IndexSearchResponse retrySearchUntil(IndexSearchRequest request, long expectedSize)
            throws AtlanException, InterruptedException {
        return retrySearchUntil(request, expectedSize, client.getMaxNetworkRetries());
    }

    /**
     * Since search is eventually consistent, retry it until we arrive at the number of results
     * we expect (or hit the retry limit).
     *
     * @param request search request to run
     * @param expectedSize expected number of results from the search
     * @param maxRetries maximum number of times to retry the search
     * @return the response, either with the expected number of results or after exceeding the retry limit
     * @throws AtlanException on any API communication issues
     * @throws InterruptedException if the busy-wait loop for retries is interrupted
     */
    protected IndexSearchResponse retrySearchUntil(IndexSearchRequest request, long expectedSize, int maxRetries)
            throws AtlanException, InterruptedException {
        return retrySearchUntil(request, expectedSize, false, maxRetries);
    }

    /**
     * Since search is eventually consistent, retry it until we arrive at the number of results
     * we expect (or hit the retry limit).
     *
     * @param request search request to run
     * @param expectedSize expected number of results from the search
     * @param isDeleteQuery whether to include finding archived (soft-deleted) assets
     * @param maxRetries maximum number of times to retry the search
     * @return the response, either with the expected number of results or after exceeding the retry limit
     * @throws AtlanException on any API communication issues
     * @throws InterruptedException if the busy-wait loop for retries is interrupted
     */
    protected IndexSearchResponse retrySearchUntil(
            IndexSearchRequest request, long expectedSize, boolean isDeleteQuery, int maxRetries)
            throws AtlanException, InterruptedException {
        int count = 1;
        IndexSearchResponse response = request.search(client);
        boolean remainingActive = false;
        if (isDeleteQuery) {
            remainingActive = !response.getAssets().stream()
                    .filter(it -> it.getStatus() != AtlanStatus.DELETED)
                    .toList()
                    .isEmpty();
        }
        while ((response.getApproximateCount() < expectedSize || remainingActive) && count < maxRetries) {
            Thread.sleep(HttpClient.waitTime(count).toMillis());
            response = request.search(client);
            if (isDeleteQuery) {
                remainingActive = !response.getAssets().stream()
                        .filter(it -> it.getStatus() != AtlanStatus.DELETED)
                        .toList()
                        .isEmpty();
            }
            count++;
        }
        assertNotNull(response);
        assertFalse(
                response.getApproximateCount() < expectedSize,
                "Search retries (" + maxRetries + ") overran - found " + response.getApproximateCount()
                        + " results when expecting " + expectedSize + ".");
        return response;
    }

    /**
     * Since search is eventually consistent, retry it until we arrive at the number of results
     * we expect (or hit the retry limit).
     *
     * @param request search request to run
     * @param expectedSize expected number of results from the search
     * @return the response, either with the expected number of results or after exceeding the retry limit
     * @throws AtlanException on any API communication issues
     * @throws InterruptedException if the busy-wait loop for retries is interrupted
     */
    protected AuditSearchResponse retrySearchUntil(AuditSearchRequest request, long expectedSize)
            throws AtlanException, InterruptedException {
        int count = 1;
        AuditSearchResponse response = request.search(client);
        while (response.getCount() < expectedSize && count < client.getMaxNetworkRetries()) {
            Thread.sleep(HttpClient.waitTime(count).toMillis());
            response = request.search(client);
            count++;
        }
        assertNotNull(response);
        assertFalse(
                response.getCount() < expectedSize,
                "Audit search retries overran - found " + response.getCount() + " results when expecting "
                        + expectedSize + ".");
        return response;
    }
}
