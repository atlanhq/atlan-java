/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.AtlanClient;
import com.atlan.exception.ApiException;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.Asset;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.AtlanTaskStatus;
import com.atlan.model.tasks.AtlanTask;
import com.atlan.net.HttpClient;
import com.atlan.net.RequestOptions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Slf4j
public class AssetDeletionResponse extends AssetMutationResponse implements AtlanAsyncMutator {
    private static final long serialVersionUID = 2L;

    /** Connectivity to the Atlan tenant where the asset deletion was run. */
    @Setter
    @JsonIgnore
    private transient AtlanClient client;

    /**
     * Block until the asset that was deleted is confirmed to be deleted,
     * since deletion occurs asynchronously.
     * @throws ApiException if the retry loop is interrupted or the maximum number of retries is hit
     */
    @Override
    public AssetMutationResponse block() throws ApiException {
        List<Asset> toCheck = getDeletedAssets();
        if (toCheck != null && !toCheck.isEmpty()) {
            try {
                retrieveAndCheck(toCheck, 0);
                blockForBackgroundTasks(
                        client, toCheck.stream().map(Asset::getGuid).collect(Collectors.toList()), MAX_ASYNC_RETRIES);
            } catch (InterruptedException e) {
                throw new ApiException(ErrorCode.RETRIES_INTERRUPTED, e);
            }
        }
        return this;
    }

    /**
     * Continually iterate through the list of deleted assets to confirm they have been removed.
     * Only retry up to the maximum defined by Atlan.getMaxNetworkRetries()
     *
     * @param toCheck list of assets to confirm are deleted
     * @param retryCount current retry
     * @throws InterruptedException if the retry loop is interrupted
     * @throws ApiException if the maximum number of retries is hit without confirming the deletion
     */
    private void retrieveAndCheck(List<Asset> toCheck, int retryCount) throws InterruptedException, ApiException {
        List<Asset> leftovers = new ArrayList<>();
        for (Asset one : toCheck) {
            try {
                // Note that there seems to be some eventual consistency delay between a
                // retrieveMinimal and a retrieveFull - to ensure delete status is fully
                // consistent we need to retrieve the full asset
                AssetResponse response = client.assets.get(
                        one.getGuid(),
                        false,
                        false,
                        RequestOptions.from(client)
                                .maxNetworkRetries(MAX_ASYNC_RETRIES)
                                .build());
                Asset candidate = response.getAsset();
                if (candidate != null && candidate.getStatus() == AtlanStatus.ACTIVE) {
                    leftovers.add(candidate);
                }
            } catch (NotFoundException e) {
                // If it is not found, it was successfully deleted (purged), so we
                // do not need to look for it any further
            } catch (AtlanException e) {
                // If there was some other exception, we should try again
                leftovers.add(one);
            }
        }
        if (!leftovers.isEmpty()) {
            if (retryCount == client.getMaxNetworkRetries()) {
                throw new ApiException(ErrorCode.RETRY_OVERRUN, null);
            } else {
                log.debug("Deletion of {} assets still pending, continuing to block (and wait).", leftovers.size());
                Thread.sleep(HttpClient.waitTime(retryCount).toMillis());
                retrieveAndCheck(leftovers, retryCount + 1);
            }
        }
    }

    /**
     * Block until any pending or in-progress background tasks for the given guids are completed.
     *
     * @param client connectivity to the Atlan tenant on which to run the task checks
     * @param guids list of guids to check for background tasks
     * @param maxRetries maximum number of retries to wait for the background tasks to complete
     * @throws InterruptedException if the retry loop is interrupted
     * @throws ApiException if the maximum number of retries is hit without the background tasks being completed
     */
    public static void blockForBackgroundTasks(AtlanClient client, List<String> guids, int maxRetries)
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
                    log.debug("Waiting for {} tasks to complete on the deleted entities", openTaskCount);
                    Thread.sleep(HttpClient.waitTime(retries).toMillis());
                } else {
                    log.debug("Task queue clear for deleted entities.");
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
