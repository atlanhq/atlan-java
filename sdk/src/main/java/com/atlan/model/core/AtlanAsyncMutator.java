/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.exception.ApiException;

/**
 * Interface implemented by asynchronous operations to allow blocking behavior.
 */
public interface AtlanAsyncMutator {

    /** Maximum number of times to retry for async operations. */
    int MAX_ASYNC_RETRIES = 20;

    /**
     * Block until the mutating operation is confirmed to be completed.
     * Note that in most cases this will make additional API calls to confirm the mutation is
     * complete, and therefore will add extra load and potential delays to program execution.
     * In general, it will retry confirming the operation has completed up to the maximum defined
     * by Atlan.getMaxNetworkRetries()
     *
     * @return the original mutation response, only after the changes are confirmed
     * @throws ApiException if the retry loop is interrupted or the maximum number of retries is hit
     */
    AssetMutationResponse block() throws ApiException;
}
