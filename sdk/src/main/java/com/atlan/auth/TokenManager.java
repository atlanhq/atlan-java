/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.auth;

import static com.atlan.net.HttpClient.waitTime;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.AuthenticationException;
import com.atlan.exception.ErrorCode;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.TypeDefResponse;
import com.atlan.net.HttpClient;
import com.atlan.util.StringUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage authorization tokens in a standard way.
 */
public abstract class TokenManager {
    private final Logger logger = LoggerFactory.getLogger(TokenManager.class);

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final AtomicInteger refreshRetryCount = new AtomicInteger(0);
    private final int maxRetries = 5;
    protected volatile char[] token;

    protected TokenManager(char[] token) {
        this.token = token;
    }

    /**
     * Retrieve the header to use for Authorization using this token.
     *
     * @param client through which to retrieve the token (if a refresh is needed)
     * @return the value for the Authorization header
     * @throws AtlanException on any API communication issue during attempted refresh (if needed)
     */
    public final String getHeader(AtlanClient client) throws AtlanException {
        if (token == null) {
            refresh(client);
        }
        lock.readLock().lock();
        try {
            return getAuthHeader();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Retrieve the header to use for Authorization using this token.
     *
     * @return the value for the Authorization header
     */
    protected abstract String getAuthHeader();

    /**
     * Refresh the managed token for a new, valid token.
     *
     * @param client through which to refresh the token
     * @return true iff the refresh was successful
     * @throws AtlanException on any API communication issue during the attempted refresh
     */
    public final boolean refresh(AtlanClient client) throws AtlanException {
        lock.writeLock().lock();
        try {
            boolean success = refreshToken(client);
            while (!success && refreshRetryCount.incrementAndGet() < maxRetries) {
                HttpClient.waitTime(refreshRetryCount.get());
                success = refreshToken(client);
            }
            refreshRetryCount.set(0);
            if (success) {
                validateActive(client);
            }
            return success;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Refresh the managed token for a new, valid token.
     *
     * @param client through which to refresh the token
     * @return true iff the refresh was successful
     * @throws AtlanException on any API communication issue during the attempted refresh
     */
    protected abstract boolean refreshToken(AtlanClient client) throws AtlanException;

    /**
     * Confirm the token held is valid.
     *
     * @throws AuthenticationException if the token held is invalid (non-existent, empty, or contains invalid characters)
     */
    public void validate() throws AuthenticationException {
        lock.readLock().lock();
        try {
            if (token == null) {
                throw new AuthenticationException(ErrorCode.NO_TOKEN);
            } else if (token.length == 0) {
                throw new AuthenticationException(ErrorCode.EMPTY_TOKEN);
            } else if (StringUtils.containsWhitespace(new String(token))) {
                throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Confirm the token held is active (able to access information programmatically).
     *
     * @param client through which to test the active nature of the token
     * @throws AtlanException on any API communication issue during the active check
     */
    private void validateActive(AtlanClient client) throws AtlanException {
        TypeDefResponse td = client.typeDefs.list(List.of(AtlanTypeCategory.STRUCT));
        int retryCount = 1;
        try {
            // Before retrying this particular request, first confirm the refreshed token is "active"
            //  (by making and retrying a call that should retrieve details only when truly active)
            while (retryCount < client.getMaxNetworkRetries()
                    && (td == null
                            || td.getStructDefs() == null
                            || td.getStructDefs().isEmpty())) {
                Thread.sleep(waitTime(retryCount).toMillis());
                td = client.typeDefs.list(List.of(AtlanTypeCategory.STRUCT));
                retryCount++;
            }
        } catch (InterruptedException e) {
            logger.warn("Token active check retry loop interrupted.", e);
        }
    }
}
