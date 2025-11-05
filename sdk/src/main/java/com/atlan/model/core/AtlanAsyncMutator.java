/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.AtlanClient;
import com.atlan.exception.ApiException;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.model.enums.AtlanTaskStatus;
import com.atlan.model.tasks.AtlanTask;
import com.atlan.net.HttpClient;
import org.slf4j.Logger;

import java.util.List;

/**
 * Interface implemented by asynchronous operations to allow blocking behavior.
 */
public interface AtlanAsyncMutator {

    /** Maximum number of times to retry for async operations. */
    int MAX_ASYNC_RETRIES = 60;

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

    /**
     * Block until any pending or in-progress background tasks for the given guids are completed.
     *
     * @param client connectivity to the Atlan tenant on which to run the task checks
     * @param guids list of guids to check for background tasks
     * @param maxRetries maximum number of retries to wait for the background tasks to complete
     * @param logger through which to record progress
     * @throws InterruptedException if the retry loop is interrupted
     * @throws ApiException if the maximum number of retries is hit without the background tasks being completed
     */
    static void blockForBackgroundTasks(AtlanClient client, List<String> guids, int maxRetries, Logger logger)
        throws InterruptedException, ApiException {
        int retries = 0;
        long openTaskCount;
        Throwable cause = null;
        try {
            do {
                openTaskCount = client
                    .tasks
                    .select()
                    .where(AtlanTask.ENTITY_GUID.in(guids))
                    .whereSome(AtlanTask.STATUS.match(AtlanTaskStatus.PENDING.getValue()))
                    .whereSome(AtlanTask.STATUS.match(AtlanTaskStatus.IN_PROGRESS.getValue()))
                    .minSomes(1)
                    .pageSize(1)
                    .stream()
                    .count();
                retries++;
                if (openTaskCount > 0) {
                    logger.debug("Waiting for {} tasks to complete on the entities", openTaskCount);
                    Thread.sleep(HttpClient.waitTime(retries).toMillis());
                } else {
                    logger.debug("Task queue clear for entities.");
                }
            } while (openTaskCount > 0 && retries < maxRetries);
        } catch (AtlanException e) {
            cause = e;
            retries = maxRetries;
        }
        if (retries == maxRetries) {
            throw new ApiException(ErrorCode.RETRY_OVERRUN, cause);
        }
    }
}
