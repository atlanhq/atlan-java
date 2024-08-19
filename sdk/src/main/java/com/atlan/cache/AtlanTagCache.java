/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.exception.*;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.AtlanTagDef;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.TypeDefResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating between Atlan-internal ID strings and human-readable names for
 * Atlan tags.
 */
@Slf4j
public class AtlanTagCache extends AbstractMassCache {

    private Map<String, String> mapIdToSourceTagsAttrId = new ConcurrentHashMap<>();
    private Set<String> deletedIds = ConcurrentHashMap.newKeySet();
    private Set<String> deletedNames = ConcurrentHashMap.newKeySet();

    private final TypeDefsEndpoint typeDefsEndpoint;

    public AtlanTagCache(TypeDefsEndpoint typeDefsEndpoint) {
        this.typeDefsEndpoint = typeDefsEndpoint;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of Atlan tags...");
        TypeDefResponse response =
                typeDefsEndpoint.list(List.of(AtlanTypeCategory.ATLAN_TAG, AtlanTypeCategory.STRUCT));
        if (response == null
                || response.getStructDefs() == null
                || response.getStructDefs().isEmpty()) {
            throw new AuthenticationException(ErrorCode.EXPIRED_API_TOKEN);
        }
        List<AtlanTagDef> tags = response.getAtlanTagDefs();
        mapIdToSourceTagsAttrId = new ConcurrentHashMap<>();
        deletedIds = ConcurrentHashMap.newKeySet();
        deletedNames = ConcurrentHashMap.newKeySet();
        for (AtlanTagDef clsDef : tags) {
            String typeId = clsDef.getName();
            cache(typeId, clsDef.getDisplayName());
            List<AttributeDef> attrs = clsDef.getAttributeDefs();
            String sourceTagsId = "";
            if (attrs != null && !attrs.isEmpty()) {
                for (AttributeDef attr : attrs) {
                    if ("sourceTagAttachment".equals(attr.getDisplayName())) {
                        sourceTagsId = attr.getName();
                    }
                }
            }
            mapIdToSourceTagsAttrId.put(typeId, sourceTagsId);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getIdForName(String name, boolean allowRefresh) throws AtlanException {
        if (name != null && deletedNames.contains(name)) {
            return null;
        }
        try {
            return super.getIdForName(name, allowRefresh);
        } catch (NotFoundException e) {
            // If it's not already marked deleted, mark it as deleted
            deletedNames.add(name);
            throw e;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getNameForId(String id, boolean allowRefresh) throws AtlanException {
        if (id != null && deletedIds.contains(id)) {
            return null;
        }
        try {
            return super.getNameForId(id, allowRefresh);
        } catch (NotFoundException e) {
            // If it's not already marked deleted, mark it as deleted
            deletedIds.add(id);
            throw e;
        }
    }

    /**
     * Translate the provided Atlan-internal Atlan tag ID string to the Atlan-internal name of the
     * attribute that captures tag attachment details (for source-synced tags).
     *
     * @param id Atlan-internal ID string of the Atlan tag
     * @return Atlan-internal ID string of the attribute containing source-synced tag attachment details
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the Atlan tag cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no ID was provided for the Atlan tag
     */
    public String getSourceTagsAttrId(String id) throws AtlanException {
        return getSourceTagsAttrId(id, true);
    }

    /**
     * Translate the provided Atlan-internal Atlan tag ID string to the Atlan-internal name of the
     * attribute that captures tag attachment details (for source-synced tags).
     *
     * @param id Atlan-internal ID string of the Atlan tag
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return Atlan-internal ID string of the attribute containing source-synced tag attachment details
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the Atlan tag cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no ID was provided for the Atlan tag
     */
    public String getSourceTagsAttrId(String id, boolean allowRefresh) throws AtlanException {
        if (id != null && !id.isEmpty()) {
            String attrId = mapIdToSourceTagsAttrId.get(id);
            if (attrId == null && !deletedIds.contains(id)) {
                // If not found, refresh the cache and look again (could be stale)
                if (allowRefresh) {
                    refreshCache();
                    attrId = mapIdToSourceTagsAttrId.get(id);
                }
                if (attrId == null) {
                    // If it's still not found after the refresh, mark it as deleted
                    deletedIds.add(id);
                    throw new NotFoundException(ErrorCode.ATLAN_TAG_NOT_FOUND_BY_ID, id);
                }
            }
            return attrId;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ATLAN_TAG_ID);
        }
    }
}
