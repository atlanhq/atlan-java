/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.exception.*;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanTagColor;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.AtlanTagDef;
import com.atlan.model.typedefs.AtlanTagOptions;
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
public class AtlanTagCache extends AbstractMassCache<AtlanTagDef> {

    private static final AtlanTagDef EXEMPLAR_TAG = AtlanTagDef.builder()
            .guid(UUID.randomUUID().toString())
            .name("a3ZhUpnaq4Q9o37gV2anFX")
            .displayName("Tag Name")
            .createdBy("someone")
            .updatedBy("someone")
            .createTime(1234567890123L)
            .updateTime(1234567890123L)
            .version(1L)
            .typeVersion("1.0")
            .description("Could be empty")
            .options(AtlanTagOptions.withIcon(AtlanIcon.ATLAN_TAG, AtlanTagColor.GRAY))
            .skipDisplayNameUniquenessCheck(false)
            .build();

    private volatile Map<String, String> mapSidToSourceTagsAttrSid = new ConcurrentHashMap<>();
    private volatile Set<String> deletedSids = ConcurrentHashMap.newKeySet();
    private volatile Set<String> deletedNames = ConcurrentHashMap.newKeySet();

    private final TypeDefsEndpoint typeDefsEndpoint;

    public AtlanTagCache(TypeDefsEndpoint typeDefsEndpoint) {
        super("tag", EXEMPLAR_TAG, AtlanTagDef.class);
        this.typeDefsEndpoint = typeDefsEndpoint;
    }

    /** {@inheritDoc} */
    @Override
    protected void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of Atlan tags...");
        TypeDefResponse response =
                typeDefsEndpoint.list(List.of(AtlanTypeCategory.ATLAN_TAG, AtlanTypeCategory.STRUCT));
        if (response == null
                || response.getStructDefs() == null
                || response.getStructDefs().isEmpty()) {
            throw new AuthenticationException(ErrorCode.EXPIRED_API_TOKEN);
        }
        List<AtlanTagDef> tags = response.getAtlanTagDefs();
        setParameters(tags.size(), tags.isEmpty() ? null : tags.get(0));
        mapSidToSourceTagsAttrSid.clear();
        deletedSids.clear();
        deletedNames.clear();
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

    private boolean isDeletedName(String name) {
        lock.readLock().lock();
        try {
            return deletedNames.contains(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    private void addDeletedName(String name) {
        lock.writeLock().lock();
        try {
            deletedNames.add(name);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean isDeletedId(String id) {
        lock.readLock().lock();
        try {
            return deletedSids.contains(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    private void addDeletedId(String id) {
        lock.writeLock().lock();
        try {
            deletedSids.add(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getIdForName(String name, boolean allowRefresh) throws AtlanException {
        if (name != null && isDeletedName(name)) {
            return null;
        }
        try {
            return super.getIdForName(name, allowRefresh);
        } catch (NotFoundException e) {
            // If it's not already marked deleted, mark it as deleted
            addDeletedName(name);
            throw e;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getNameForId(String id, boolean allowRefresh) throws AtlanException {
        if (id != null && isDeletedId(id)) {
            return null;
        }
        try {
            return super.getNameForId(id, allowRefresh);
        } catch (NotFoundException e) {
            // If it's not already marked deleted, mark it as deleted
            addDeletedId(id);
            throw e;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getSidForName(String name, boolean allowRefresh) throws AtlanException {
        if (name != null && isDeletedName(name)) {
            return null;
        }
        try {
            return super.getSidForName(name, allowRefresh);
        } catch (NotFoundException e) {
            // If it's not already marked deleted, mark it as deleted
            addDeletedName(name);
            throw e;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getNameForSid(String id, boolean allowRefresh) throws AtlanException {
        if (id != null && isDeletedId(id)) {
            return null;
        }
        try {
            return super.getNameForSid(id, allowRefresh);
        } catch (NotFoundException e) {
            // If it's not already marked deleted, mark it as deleted
            addDeletedId(id);
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
            String attrId = getSourceTagsAttrIdFromId(id);
            if (attrId == null && !isDeletedId(id)) {
                // If not found, refresh the cache and look again (could be stale)
                if (allowRefresh) {
                    refresh();
                    attrId = getSourceTagsAttrIdFromId(id);
                }
                if (attrId == null) {
                    // If it's still not found after the refresh, mark it as deleted
                    addDeletedId(id);
                    throw new NotFoundException(ErrorCode.ATLAN_TAG_NOT_FOUND_BY_ID, id);
                }
            }
            return attrId;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ATLAN_TAG_ID);
        }
    }
}
