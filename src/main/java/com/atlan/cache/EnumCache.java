/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.EnumDef;
import com.atlan.model.typedefs.TypeDefResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for accessing details of an enumeration.
 */
@Slf4j
public class EnumCache {

    private static Map<String, EnumDef> cacheById = new ConcurrentHashMap<>();

    private static synchronized void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of enumerations...");
        TypeDefResponse response = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.ENUM);
        List<EnumDef> enumerations;
        if (response != null) {
            enumerations = response.getEnumDefs();
        } else {
            enumerations = Collections.emptyList();
        }
        cacheById = new ConcurrentHashMap<>();
        for (EnumDef enumDef : enumerations) {
            String typeId = enumDef.getName();
            cacheById.put(typeId, enumDef);
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
    public static EnumDef getByName(String name) throws AtlanException {
        if (name != null && name.length() > 0) {
            EnumDef enumDef = cacheById.get(name);
            if (enumDef == null) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                enumDef = cacheById.get(name);
                if (enumDef == null) {
                    // If still not found, throw an exception indicating that outcome
                    throw new NotFoundException(
                            "Enumeration (options) with name '" + name + "' does not exist.",
                            "ATLAN_JAVA_404_700",
                            null);
                }
            }
            return enumDef;
        } else {
            throw new InvalidRequestException(
                    "No name was provided when attempting to retrieve an enumeration.",
                    "name",
                    "ATLAN_JAVA_400_700",
                    null);
        }
    }
}
