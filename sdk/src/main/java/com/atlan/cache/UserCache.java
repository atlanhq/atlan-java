/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.ApiTokensEndpoint;
import com.atlan.api.UsersEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating Atlan-internal users into their various IDs.
 */
@Slf4j
public class UserCache extends AbstractMassCache<AtlanUser> {

    private volatile Map<String, String> mapEmailToId = new ConcurrentHashMap<>();

    private final UsersEndpoint usersEndpoint;
    private final ApiTokensEndpoint apiTokensEndpoint;

    public UserCache(UsersEndpoint usersEndpoint, ApiTokensEndpoint apiTokensEndpoint) {
        super();
        this.usersEndpoint = usersEndpoint;
        this.apiTokensEndpoint = apiTokensEndpoint;
        this.bulkRefresh.set(false); // Default to a lazily-loaded cache for users
    }

    /** {@inheritDoc} */
    @Override
    protected void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of users...");
        List<AtlanUser> users = usersEndpoint.list();
        initializeOffHeap("user", users.size(), users.get(0), AtlanUser.class);
        mapEmailToId.clear();
        for (AtlanUser user : users) {
            String userId = user.getId();
            String userName = user.getUsername();
            String email = user.getEmail();
            if (userId != null && userName != null && email != null) {
                cache(userId, userName, user);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void cache(String id, String name, AtlanUser object) {
        super.cache(id, name, object);
        if (object != null) {
            String email = object.getEmail();
            if (email != null) {
                mapEmailToId.put(email, id);
            }
        }
    }

    private String getIdFromEmail(String email) {
        lock.readLock().lock();
        try {
            return mapEmailToId.get(email);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Translate the provided human-readable username to its GUID.
     *
     * @param username human-readable name of the user
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return unique identifier (GUID) of the user
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the user cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the user to retrieve
     */
    @Override
    public String getIdForName(String username, boolean allowRefresh) throws AtlanException {
        try {
            // If it is already cached, return it from the cache
            return super.getIdForName(username, false);
        } catch (NotFoundException e) {
            // Otherwise, if we are translating an API token, short-circuit any further cache refresh
            if (username.startsWith(ApiToken.API_USERNAME_PREFIX)) {
                ApiToken token = apiTokensEndpoint.getById(username);
                if (token == null) {
                    throw new NotFoundException(ErrorCode.API_TOKEN_NOT_FOUND_BY_NAME, username);
                } else {
                    cache(token.getId(), username, null);
                    return token.getId();
                }
            }
            // Otherwise, attempt to retrieve it and allow the cache to be refreshed when doing so
            return super.getIdForName(username, allowRefresh);
        }
    }

    /**
     * Translate the provided email address to its GUID.
     *
     * @param email email address of the user
     * @return unique identifier (GUID) of the user
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the user cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the user to retrieve
     */
    public String getIdForEmail(String email) throws AtlanException {
        return getIdForEmail(email, true);
    }

    /**
     * Translate the provided email address to its GUID.
     *
     * @param email email address of the user
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return unique identifier (GUID) of the user
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the user cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the user to retrieve
     */
    public String getIdForEmail(String email, boolean allowRefresh) throws AtlanException {
        if (email != null && !email.isEmpty()) {
            String userId = getIdFromEmail(email);
            if (userId == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                cacheByEmail(email);
                userId = getIdFromEmail(email);
            }
            if (userId == null) {
                throw new NotFoundException(ErrorCode.USER_NOT_FOUND_BY_EMAIL, email);
            }
            return userId;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_USER_EMAIL);
        }
    }

    /**
     * Translate the provided user GUID to the user's username.
     *
     * @param id unique identifier (GUID) of the user
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return username of the user
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the user cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the user to retrieve
     */
    @Override
    public String getNameForId(String id, boolean allowRefresh) throws AtlanException {
        try {
            // If it is already cached, return it from the cache
            return super.getNameForId(id, false);
        } catch (NotFoundException e) {
            // Otherwise, check if it is an API token (short-circuit any further cache refresh)
            ApiToken token = apiTokensEndpoint.getByGuid(id);
            if (token != null) {
                String username = token.getApiTokenUsername();
                cache(id, username, null);
                return username;
            }
            // Otherwise, attempt to retrieve it and allow the cache to be refreshed when doing so
            return super.getNameForId(id, allowRefresh);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void lookupByName(String username) throws AtlanException {
        if (username.startsWith(ApiToken.API_USERNAME_PREFIX)) {
            ApiToken token = apiTokensEndpoint.getById(username);
            if (token == null) {
                throw new NotFoundException(ErrorCode.API_TOKEN_NOT_FOUND_BY_NAME, username);
            } else {
                cache(token.getId(), username, null);
            }
        } else {
            AtlanUser user = usersEndpoint.getByUsername(username);
            if (user != null) {
                cache(user.getId(), username, user);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void lookupById(String id) throws AtlanException {
        try {
            AtlanUser user = usersEndpoint.getByGuid(id);
            if (user != null) {
                cache(id, user.getUsername(), user);
            }
        } catch (NotFoundException e) {
            // Otherwise, check if it is an API token
            ApiToken token = apiTokensEndpoint.getByGuid(id);
            if (token != null) {
                cache(id, token.getApiTokenUsername(), null);
            }
        }
    }

    /**
     * Wraps a single object lookup for the cache with necessary concurrency controls.
     *
     * @param email unique email address for the user
     * @throws AtlanException on any error communicating with Atlan
     */
    public void cacheByEmail(String email) throws AtlanException {
        if (bulkRefresh.get()) {
            refresh();
        } else {
            lock.writeLock().lock();
            try {
                lookupByEmail(email);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    /**
     * Logic to look up a single object for the cache.
     *
     * @param email unique email address for the user
     * @throws AtlanException on any error communicating with Atlan
     */
    protected void lookupByEmail(String email) throws AtlanException {
        List<AtlanUser> users = usersEndpoint.getByEmail(email);
        if (users != null && !users.isEmpty()) {
            for (AtlanUser user : users) {
                cache(user.getId(), user.getUsername(), user);
            }
        }
    }
}
