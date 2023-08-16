/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.AtlanTagDef;
import com.atlan.model.typedefs.TypeDefResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating between Atlan-internal ID strings and human-readable names for
 * Atlan tags.
 */
@Slf4j
public class AtlanTagCache {

    private Map<String, AtlanTagDef> cacheById = new ConcurrentHashMap<>();
    private Map<String, String> mapIdToName = new ConcurrentHashMap<>();
    private Map<String, String> mapNameToId = new ConcurrentHashMap<>();
    private final Set<String> deletedIds = ConcurrentHashMap.newKeySet();
    private final Set<String> deletedNames = ConcurrentHashMap.newKeySet();

    private final TypeDefsEndpoint typeDefsEndpoint;

    public AtlanTagCache(TypeDefsEndpoint typeDefsEndpoint) {
        this.typeDefsEndpoint = typeDefsEndpoint;
    }

    /**
     * Refreshes the cache of Atlan tags by requesting the full set of Atlan tags from Atlan.
     *
     * @throws AtlanException on any API communication problem
     */
    public synchronized void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of Atlan tags...");
        TypeDefResponse response = typeDefsEndpoint.list(AtlanTypeCategory.ATLAN_TAG);
        List<AtlanTagDef> tags;
        if (response != null) {
            tags = response.getAtlanTagDefs();
        } else {
            tags = Collections.emptyList();
        }
        cacheById = new ConcurrentHashMap<>();
        mapIdToName = new ConcurrentHashMap<>();
        mapNameToId = new ConcurrentHashMap<>();
        for (AtlanTagDef clsDef : tags) {
            String typeId = clsDef.getName();
            cacheById.put(typeId, clsDef);
            mapIdToName.put(typeId, clsDef.getDisplayName());
            mapNameToId.put(clsDef.getDisplayName(), typeId);
        }
    }

    /**
     * Translate the provided human-readable Atlan tag name to the Atlan-internal ID string.
     *
     * @param name human-readable name of the Atlan tag
     * @return Atlan-internal ID string of the Atlan tag
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the Atlan tag cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the Atlan tag to retrieve
     */
    public String getIdForName(String name) throws AtlanException {
        if (name != null && !name.isEmpty()) {
            String cmId = mapNameToId.get(name);
            if (cmId == null && !deletedNames.contains(name)) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                cmId = mapNameToId.get(name);
                if (cmId == null) {
                    // If it's still not found after the refresh, mark it as deleted
                    deletedNames.add(name);
                    throw new NotFoundException(ErrorCode.ATLAN_TAG_NOT_FOUND_BY_NAME, name);
                }
            }
            return cmId;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ATLAN_TAG_NAME);
        }
    }

    /**
     * Translate the provided Atlan-internal Atlan tag ID string to the human-readable Atlan tag name.
     *
     * @param id Atlan-internal ID string of the Atlan tag
     * @return human-readable name of the Atlan tag
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the Atlan tag cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no ID was provided for the Atlan tag to retrieve
     */
    public String getNameForId(String id) throws AtlanException {
        if (id != null && !id.isEmpty()) {
            String cmName = mapIdToName.get(id);
            if (cmName == null && !deletedIds.contains(id)) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                cmName = mapIdToName.get(id);
                if (cmName == null) {
                    // If it's still not found after the refresh, mark it as deleted
                    deletedIds.add(id);
                    throw new NotFoundException(ErrorCode.ATLAN_TAG_NOT_FOUND_BY_ID, id);
                }
            }
            return cmName;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ATLAN_TAG_ID);
        }
    }
}
