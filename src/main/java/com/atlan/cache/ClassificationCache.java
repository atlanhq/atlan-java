package com.atlan.cache;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.responses.TypeDefResponse;
import com.atlan.model.typedefs.ClassificationDef;
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

    private static synchronized void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of classifications...");
        TypeDefResponse response = TypeDefsEndpoint.getTypeDefs("classification");
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
     * @param name human-readable name of the classification
     * @return Atlan-internal ID string of the classification
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static String getIdForName(String name) throws AtlanException {
        String cmId = mapNameToId.get(name);
        if (cmId != null) {
            // If found, return straight away
            return cmId;
        } else {
            // Otherwise, refresh the cache and look again (could be stale)
            refreshCache();
            return mapNameToId.get(name);
        }
    }

    /**
     * Translate the provided Atlan-internal classification ID string to the human-readable classification name.
     * @param id Atlan-internal ID string of the classification
     * @return human-readable name of the classification
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static String getNameForId(String id) throws AtlanException {
        String cmName = mapIdToName.get(id);
        if (cmName != null) {
            // If found, return straight away
            return cmName;
        } else {
            // Otherwise, refresh the cache and look again (could be stale)
            refreshCache();
            return mapIdToName.get(id);
        }
    }
}
