/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.ClassificationDef;
import com.atlan.model.typedefs.TypeDefResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating between Atlan-internal ID strings and human-readable names for
 * classifications.
 */
@Slf4j
public class ClassificationCache {

    private static Map<String, ClassificationDef> cacheById = new ConcurrentHashMap<>();
    private static Map<String, String> mapIdToName = new ConcurrentHashMap<>();
    private static Map<String, String> mapNameToId = new ConcurrentHashMap<>();
    private static Set<String> deletedIds = ConcurrentHashMap.newKeySet();
    private static Set<String> deletedNames = ConcurrentHashMap.newKeySet();

    private static synchronized void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of classifications...");
        TypeDefResponse response = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.CLASSIFICATION);
        List<ClassificationDef> classifications;
        if (response != null) {
            classifications = response.getClassificationDefs();
        } else {
            classifications = Collections.emptyList();
        }
        cacheById = new ConcurrentHashMap<>();
        mapIdToName = new ConcurrentHashMap<>();
        mapNameToId = new ConcurrentHashMap<>();
        for (ClassificationDef clsDef : classifications) {
            String typeId = clsDef.getName();
            cacheById.put(typeId, clsDef);
            mapIdToName.put(typeId, clsDef.getDisplayName());
            mapNameToId.put(clsDef.getDisplayName(), typeId);
        }
    }

    /**
     * Translate the provided human-readable classification name to the Atlan-internal ID string.
     *
     * @param name human-readable name of the classification
     * @return Atlan-internal ID string of the classification
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the classification cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the classification to retrieve
     */
    public static String getIdForName(String name) throws AtlanException {
        if (name != null && name.length() > 0) {
            String cmId = mapNameToId.get(name);
            if (cmId == null && !deletedNames.contains(name)) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                cmId = mapNameToId.get(name);
                if (cmId == null) {
                    // If it's still not found after the refresh, mark it as deleted
                    deletedNames.add(name);
                    throw new NotFoundException(
                            "Classification with name '" + name + "' does not exist.", "ATLAN_JAVA_404_701", null);
                }
            }
            return cmId;
        } else {
            throw new InvalidRequestException(
                    "No name was provided when attempting to retrieve a classification.",
                    "name",
                    "ATLAN_JAVA_400_701",
                    null);
        }
    }

    /**
     * Translate the provided Atlan-internal classification ID string to the human-readable classification name.
     *
     * @param id Atlan-internal ID string of the classification
     * @return human-readable name of the classification
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the classification cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no ID was provided for the classification to retrieve
     */
    public static String getNameForId(String id) throws AtlanException {
        if (id != null && id.length() > 0) {
            String cmName = mapIdToName.get(id);
            if (cmName == null && !deletedIds.contains(id)) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                cmName = mapIdToName.get(id);
                if (cmName == null) {
                    // If it's still not found after the refresh, mark it as deleted
                    deletedIds.add(id);
                    throw new NotFoundException(
                            "Classification with ID '" + id + "' does not exist.", "ATLAN_JAVA_404_702", null);
                }
            }
            return cmName;
        } else {
            throw new InvalidRequestException(
                    "No ID was provided when attempting to retrieve a classification.",
                    "id",
                    "ATLAN_JAVA_400_702",
                    null);
        }
    }
}
