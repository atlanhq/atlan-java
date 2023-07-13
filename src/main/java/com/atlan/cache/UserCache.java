/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

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
public class UserCache {

    private Map<String, String> mapIdToName = new ConcurrentHashMap<>();
    private Map<String, String> mapNameToId = new ConcurrentHashMap<>();
    private Map<String, String> mapEmailToId = new ConcurrentHashMap<>();

    private synchronized void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of users...");
        List<AtlanUser> users = AtlanUser.retrieveAll();
        mapIdToName = new ConcurrentHashMap<>();
        mapNameToId = new ConcurrentHashMap<>();
        mapEmailToId = new ConcurrentHashMap<>();
        for (AtlanUser user : users) {
            String userId = user.getId();
            String userName = user.getUsername();
            String email = user.getEmail();
            mapIdToName.put(userId, userName);
            mapNameToId.put(userName, userId);
            mapEmailToId.put(email, userId);
        }
    }

    /**
     * Translate the provided human-readable username to its GUID.
     * @param username human-readable name of the user
     * @return GUID of the user
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the user cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the user to retrieve
     */
    public String getIdForName(String username) throws AtlanException {
        if (username != null && username.length() > 0) {
            String userId = mapNameToId.get(username);
            if (userId == null) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                userId = mapNameToId.get(username);
                if (userId == null) {
                    throw new NotFoundException(ErrorCode.USER_NOT_FOUND_BY_NAME, username);
                }
            }
            return userId;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_USER_NAME);
        }
    }

    /**
     * Translate the provided email address to its GUID.
     * @param email email address of the user
     * @return GUID of the user
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the user cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the user to retrieve
     */
    public String getIdForEmail(String email) throws AtlanException {
        if (email != null && email.length() > 0) {
            String userId = mapEmailToId.get(email);
            if (userId == null) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                userId = mapEmailToId.get(email);
                if (userId == null) {
                    throw new NotFoundException(ErrorCode.USER_NOT_FOUND_BY_EMAIL, email);
                }
            }
            return userId;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_USER_EMAIL);
        }
    }

    /**
     * Translate the provided user GUID to the user's username.
     * @param id GUID of the user
     * @return username of the user
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the user cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the user to retrieve
     */
    public String getNameForId(String id) throws AtlanException {
        if (id != null && id.length() > 0) {
            String userName = mapIdToName.get(id);
            if (userName == null) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                userName = mapIdToName.get(id);
                if (userName == null) {
                    throw new NotFoundException(ErrorCode.USER_NOT_FOUND_BY_ID, id);
                }
            }
            return userName;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_USER_ID);
        }
    }
}
