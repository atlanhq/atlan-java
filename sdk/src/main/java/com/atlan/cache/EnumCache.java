/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.exception.*;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.EnumDef;
import com.atlan.model.typedefs.TypeDefResponse;
import com.atlan.net.HttpClient;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for accessing details of an enumeration.
 */
@Slf4j
public class EnumCache {

    private volatile Map<String, EnumDef> cacheById = new ConcurrentHashMap<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final TypeDefsEndpoint typeDefsEndpoint;

    public EnumCache(TypeDefsEndpoint typeDefsEndpoint) {
        this.typeDefsEndpoint = typeDefsEndpoint;
    }

    /**
     * Refreshes the cache of enumerations by requesting the full set of enumerations from Atlan.
     *
     * @throws AtlanException on any API communication problem
     */
    public void refreshCache() throws AtlanException {
        lock.writeLock().lock();
        try {
            log.debug("Refreshing cache of enumerations...");
            TypeDefResponse response = typeDefsEndpoint.list(AtlanTypeCategory.ENUM);
            int retryCount = 1;
            try {
                while (retryCount < typeDefsEndpoint.getClient().getMaxNetworkRetries()
                        && (response == null
                                || response.getEnumDefs() == null
                                || response.getEnumDefs().isEmpty())) {
                    Thread.sleep(HttpClient.waitTime(retryCount).toMillis());
                    response = typeDefsEndpoint.list(List.of(AtlanTypeCategory.ENUM));
                    retryCount++;
                }
            } catch (InterruptedException e) {
                log.warn(" ... retry loop interrupted.", e);
            }
            if (response == null
                    || response.getEnumDefs() == null
                    || response.getEnumDefs().isEmpty()) {
                throw new AuthenticationException(ErrorCode.EXPIRED_API_TOKEN);
            }
            List<EnumDef> enumerations = response.getEnumDefs();
            cacheById = new ConcurrentHashMap<>();
            for (EnumDef enumDef : enumerations) {
                String typeId = enumDef.getName();
                cacheById.put(typeId, enumDef);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Thread-safe cache retrieval of the enumeration typedef, by its name.
     *
     * @param name of the EnumDef
     * @return the enumeration typedef itself (if cached), or null
     */
    protected EnumDef getObjectByName(String name) {
        lock.readLock().lock();
        try {
            return cacheById.get(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Retrieve the enumeration definition by its name.
     *
     * @param name human-readable name of the enumeration
     * @return the enumeration definition
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the enumeration cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the enumeration to retrieve
     */
    public EnumDef getByName(String name) throws AtlanException {
        return getByName(name, true);
    }

    /**
     * Retrieve the enumeration definition by its name.
     *
     * @param name human-readable name of the enumeration
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return the enumeration definition
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the enumeration cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the enumeration to retrieve
     */
    public EnumDef getByName(String name, boolean allowRefresh) throws AtlanException {
        if (name != null && !name.isEmpty()) {
            EnumDef enumDef = getObjectByName(name);
            if (enumDef == null) {
                // If not found, refresh the cache and look again (could be stale)
                if (allowRefresh) {
                    refreshCache();
                    enumDef = getObjectByName(name);
                }
                if (enumDef == null) {
                    // If still not found, throw an exception indicating that outcome
                    throw new NotFoundException(ErrorCode.ENUM_NOT_FOUND, name);
                }
            }
            return enumDef;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ENUM_NAME);
        }
    }
}
