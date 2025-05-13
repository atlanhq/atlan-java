/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.api.TypeDefsEndpoint;
import com.atlan.exception.*;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.AtlanTagDef;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.TypeDefResponse;
import com.atlan.net.HttpClient;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating between Atlan-internal ID strings and human-readable names for
 * Atlan tags.
 */
@Slf4j
public class AtlanTagCache extends AbstractMassTrackingCache<AtlanTagDef> {

    private volatile Map<String, String> mapSidToSourceTagsAttrSid = new ConcurrentHashMap<>();

    private final TypeDefsEndpoint typeDefsEndpoint;

    public AtlanTagCache(AtlanClient client) {
        super(client, "tag");
        this.typeDefsEndpoint = client.typeDefs;
    }

    /** {@inheritDoc} */
    @Override
    protected void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of Atlan tags...");
        TypeDefResponse response =
                typeDefsEndpoint.list(List.of(AtlanTypeCategory.ATLAN_TAG, AtlanTypeCategory.STRUCT));
        int retryCount = 1;
        try {
            while (retryCount < client.getMaxNetworkRetries()
                    && (response == null
                            || response.getStructDefs() == null
                            || response.getStructDefs().isEmpty())) {
                Thread.sleep(HttpClient.waitTime(retryCount).toMillis());
                response = typeDefsEndpoint.list(List.of(AtlanTypeCategory.ATLAN_TAG, AtlanTypeCategory.STRUCT));
                retryCount++;
            }
        } catch (InterruptedException e) {
            log.warn(" ... retry loop interrupted.", e);
        }
        if (response == null
                || response.getStructDefs() == null
                || response.getStructDefs().isEmpty()) {
            throw new AuthenticationException(ErrorCode.EXPIRED_API_TOKEN);
        }
        List<AtlanTagDef> tags = response.getAtlanTagDefs();
        mapSidToSourceTagsAttrSid.clear();
        super.refreshCache();
        for (AtlanTagDef clsDef : tags) {
            String typeId = clsDef.getName();
            cache(clsDef.getGuid(), typeId, clsDef.getDisplayName(), clsDef);
            List<AttributeDef> attrs = clsDef.getAttributeDefs();
            String sourceTagsId = "";
            if (attrs != null && !attrs.isEmpty()) {
                for (AttributeDef attr : attrs) {
                    if ("sourceTagAttachment".equals(attr.getDisplayName())) {
                        sourceTagsId = attr.getName();
                    }
                }
            }
            mapSidToSourceTagsAttrSid.put(typeId, sourceTagsId);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void lookupByName(String name) {
        // Nothing to do here, can only be looked up by internal ID
    }

    /** {@inheritDoc} */
    @Override
    protected void lookupById(String id) {
        // Since we can only look up in one direction, we should only allow bulk refresh
    }

    private String getSourceTagsAttrIdFromId(String id) {
        lock.readLock().lock();
        try {
            return mapSidToSourceTagsAttrSid.get(id);
        } finally {
            lock.readLock().unlock();
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
        return getSourceTagsAttrId(id, allowRefresh ? Long.MAX_VALUE : Long.MIN_VALUE);
    }

    /**
     * Translate the provided Atlan-internal Atlan tag ID string to the Atlan-internal name of the
     * attribute that captures tag attachment details (for source-synced tags).
     *
     * @param id Atlan-internal ID string of the Atlan tag
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return Atlan-internal ID string of the attribute containing source-synced tag attachment details
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the Atlan tag cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no ID was provided for the Atlan tag
     */
    public String getSourceTagsAttrId(String id, long minimumTime) throws AtlanException {
        if (id == null || id.isEmpty()) throw new InvalidRequestException(ErrorCode.MISSING_ATLAN_TAG_ID);
        String attrId = getSourceTagsAttrIdFromId(id);
        if (attrId == null && !isDeletedId(id)) {
            // If not found, refresh the cache and look again (could be stale)
            refresh(minimumTime);
            attrId = getSourceTagsAttrIdFromId(id);
            if (attrId == null) {
                // If it's still not found after the refresh, mark it as deleted
                addDeletedId(id);
                throw new NotFoundException(ErrorCode.ATLAN_TAG_NOT_FOUND_BY_ID, id);
            }
        }
        return attrId;
    }
}
