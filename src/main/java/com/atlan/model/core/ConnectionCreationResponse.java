/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.Atlan;
import com.atlan.exception.ApiException;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.PermissionException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.net.HttpClient;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class ConnectionCreationResponse extends AssetMutationResponse implements AtlanAsyncMutator {
    private static final long serialVersionUID = 2L;

    /**
     * Block until the connection that was created is confirmed to be accessible,
     * since policies linked to the connection are created asynchronously.
     * @throws ApiException if the retry loop is interrupted or the maximum number of retries is hit
     */
    @Override
    public AssetMutationResponse block() throws ApiException {
        List<Asset> toCheck = getCreatedAssets();
        if (toCheck != null && !toCheck.isEmpty()) {
            try {
                retrieveAndCheck(toCheck, 0);
            } catch (InterruptedException e) {
                throw new ApiException(ErrorCode.RETRIES_INTERRUPTED, e);
            }
        }
        return this;
    }

    /**
     * Continually iterate through the list of created connections to confirm they have been created.
     * Only retry up to the maximum defined by Atlan.getMaxNetworkRetries()
     *
     * @param toCheck list of assets to confirm are accessible
     * @param retryCount current retry
     * @throws InterruptedException if the retry loop is interrupted
     * @throws ApiException if the maximum number of retries is hit without confirming accessibility
     */
    private void retrieveAndCheck(List<Asset> toCheck, int retryCount) throws InterruptedException, ApiException {
        List<Asset> leftovers = new ArrayList<>();
        for (Asset one : toCheck) {
            if (one instanceof Connection) {
                // Only even attempt to look at an asset if it is a connection, otherwise skip it
                // entirely
                try {
                    Asset candidate = Asset.retrieveMinimal(one.getGuid());
                    if (candidate == null) {
                        // Since the retry logic in this case is actually embedded in the retrieveMinimal
                        // call, if we get to this point without retrieving the connection we have by
                        // definition overrun the retry limit
                        throw new ApiException(ErrorCode.RETRY_OVERRUN, null);
                    }
                } catch (PermissionException e) {
                    // If we get a permission exception after the built-in retries above, throw it
                    // onwards as a retry overrun
                    throw new ApiException(ErrorCode.RETRY_OVERRUN, e);
                } catch (AtlanException e) {
                    // If there was some other exception, we should try again
                    leftovers.add(one);
                }
            }
        }
        if (!leftovers.isEmpty()) {
            if (retryCount == Atlan.getMaxNetworkRetries()) {
                throw new ApiException(ErrorCode.RETRY_OVERRUN, null);
            } else {
                Thread.sleep(HttpClient.waitTime(retryCount).toMillis());
                retrieveAndCheck(leftovers, retryCount + 1);
            }
        }
    }
}
