/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.api.TypeDefsEndpoint;
import com.atlan.exception.*;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.CustomMetadataDef;
import com.atlan.model.typedefs.TypeDefResponse;
import com.atlan.net.HttpClient;
import com.atlan.serde.Removable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating between Atlan-internal ID strings and human-readable names for
 * custom metadata (including attributes).
 */
@Slf4j
public class CustomMetadataCache extends AbstractMassTrackingCache<CustomMetadataDef> {

    private volatile Map<String, AttributeDef> attrCacheBySid = new ConcurrentHashMap<>();

    private volatile Map<String, Map<String, String>> mapAttrSidToName = new ConcurrentHashMap<>();
    private volatile Map<String, Map<String, String>> mapAttrNameToSid = new ConcurrentHashMap<>();
    private volatile Set<String> archivedAttrSids = ConcurrentHashMap.newKeySet();

    private final TypeDefsEndpoint typeDefsEndpoint;

    public CustomMetadataCache(AtlanClient client) {
        super(client, "cm");
        this.typeDefsEndpoint = client.typeDefs;
    }

    /** {@inheritDoc} */
    @Override
    protected void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of custom metadata...");
        TypeDefResponse response =
                typeDefsEndpoint.list(List.of(AtlanTypeCategory.CUSTOM_METADATA, AtlanTypeCategory.STRUCT));
        int retryCount = 1;
        try {
            while (retryCount < client.getMaxNetworkRetries()
                    && (response == null
                            || response.getStructDefs() == null
                            || response.getStructDefs().isEmpty())) {
                Thread.sleep(HttpClient.waitTime(retryCount).toMillis());
                response = typeDefsEndpoint.list(List.of(AtlanTypeCategory.CUSTOM_METADATA, AtlanTypeCategory.STRUCT));
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
        List<CustomMetadataDef> customMetadata = response.getCustomMetadataDefs();
        attrCacheBySid.clear();
        mapAttrSidToName.clear();
        mapAttrNameToSid.clear();
        archivedAttrSids.clear();
        super.refreshCache();
        for (CustomMetadataDef bmDef : customMetadata) {
            String typeId = bmDef.getName();
            cache(bmDef.getGuid(), typeId, bmDef.getDisplayName(), bmDef);
            mapAttrSidToName.put(typeId, new ConcurrentHashMap<>());
            mapAttrNameToSid.put(typeId, new ConcurrentHashMap<>());
            for (AttributeDef attributeDef : bmDef.getAttributeDefs()) {
                String attrId = attributeDef.getName();
                String attrName = attributeDef.getDisplayName();
                mapAttrSidToName.get(typeId).put(attrId, attrName);
                attrCacheBySid.put(attrId, attributeDef);
                if (attributeDef.isArchived()) {
                    archivedAttrSids.add(attrId);
                } else {
                    String existingId =
                            mapAttrNameToSid.get(typeId).put(attributeDef.getDisplayName(), attributeDef.getName());
                    if (existingId != null) {
                        throw new LogicException(
                                ErrorCode.DUPLICATE_CUSTOM_ATTRIBUTES,
                                attributeDef.getDisplayName(),
                                bmDef.getDisplayName());
                    }
                }
            }
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

    private AttributeDef getAttrById(String id) {
        lock.readLock().lock();
        try {
            return attrCacheBySid.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    private Map<String, String> getAttrNameFromId(String id) {
        lock.readLock().lock();
        try {
            return mapAttrSidToName.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    private Map<String, String> getAttrIdFromName(String name) {
        lock.readLock().lock();
        try {
            return mapAttrNameToSid.get(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    private boolean isArchivedAttr(String id) {
        lock.readLock().lock();
        try {
            return archivedAttrSids.contains(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Retrieve all the (active) custom metadata attributes. The map will be keyed by custom metadata set
     * name, and the value will be a listing of all the (active) attributes within that set (with all the details
     * of each of those attributes).
     *
     * @return a map from custom metadata set name to all details about all its active attributes
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public Map<String, List<AttributeDef>> getAllCustomAttributes() throws AtlanException {
        return getAllCustomAttributes(false);
    }

    /**
     * Retrieve all the custom metadata attributes. The map will be keyed by custom metadata set
     * name, and the value will be a listing of all the attributes within that set (with all the details
     * of each of those attributes).
     *
     * @param includeDeleted if true, include the archived (deleted) custom attributes; otherwise only include active custom attributes
     * @return a map from custom metadata set name to all details about all its attributes
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public Map<String, List<AttributeDef>> getAllCustomAttributes(boolean includeDeleted) throws AtlanException {
        return getAllCustomAttributes(includeDeleted, false);
    }

    /**
     * Retrieve all the custom metadata attributes. The map will be keyed by custom metadata set
     * name, and the value will be a listing of all the attributes within that set (with all the details
     * of each of those attributes).
     *
     * @param includeDeleted if true, include the archived (deleted) custom attributes; otherwise only include active custom attributes
     * @param forceRefresh if true, will refresh the custom metadata cache; if false, will only refresh
     *                     the cache if it is empty
     * @return a map from custom metadata set name to all details about all its attributes
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public Map<String, List<AttributeDef>> getAllCustomAttributes(boolean includeDeleted, boolean forceRefresh)
            throws AtlanException {
        if (isEmpty() || forceRefresh) {
            forceRefresh();
        }
        Map<String, List<AttributeDef>> map = new HashMap<>();
        Stream<Map.Entry<String, CustomMetadataDef>> entrySet = entrySet();
        lock.readLock().lock();
        try {
            AtomicReference<AtlanException> found = new AtomicReference<>();
            entrySet.forEach(entry -> {
                try {
                    String id = entry.getKey();
                    String typeName = getNameForId(id);
                    CustomMetadataDef typeDef = entry.getValue();
                    List<AttributeDef> attributeDefs = typeDef.getAttributeDefs();
                    List<AttributeDef> toInclude;
                    if (includeDeleted) {
                        toInclude = attributeDefs;
                    } else {
                        toInclude = new ArrayList<>();
                        for (AttributeDef attributeDef : attributeDefs) {
                            if (!attributeDef.isArchived()) {
                                toInclude.add(attributeDef);
                            }
                        }
                    }
                    map.put(typeName, toInclude);
                } catch (AtlanException e) {
                    if (found.get() != null) {
                        found.get().addSuppressed(e);
                    } else {
                        found.set(e);
                    }
                }
            });
            if (found.get() != null) {
                throw found.get();
            }
        } finally {
            lock.readLock().unlock();
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * Translate the provided human-readable custom metadata set and attribute names to the Atlan-internal ID string
     * for the attribute.
     *
     * @param setName human-readable name of the custom metadata set
     * @param attributeName human-readable name of the attribute
     * @return Atlan-internal ID string for the attribute
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the custom metadata cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the custom metadata to retrieve
     */
    public String getAttrIdForName(String setName, String attributeName) throws AtlanException {
        return getAttrIdForName(setName, attributeName, true);
    }

    /**
     * Translate the provided human-readable custom metadata set and attribute names to the Atlan-internal ID string
     * for the attribute.
     *
     * @param setName human-readable name of the custom metadata set
     * @param attributeName human-readable name of the attribute
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return Atlan-internal ID string for the attribute
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the custom metadata cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the custom metadata to retrieve
     */
    public String getAttrIdForName(String setName, String attributeName, boolean allowRefresh) throws AtlanException {
        String setId = getSidForName(setName, allowRefresh);
        return getAttrIdForNameFromSetId(setId, attributeName, allowRefresh);
    }

    /**
     * Translate the provided human-readable custom metadata set and attribute names to the Atlan-internal ID string
     * for the attribute.
     *
     * @param setName human-readable name of the custom metadata set
     * @param attributeName human-readable name of the attribute
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return Atlan-internal ID string for the attribute
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the custom metadata cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the custom metadata to retrieve
     */
    public String getAttrIdForName(String setName, String attributeName, long minimumTime) throws AtlanException {
        String setId = getSidForName(setName, minimumTime);
        return getAttrIdForNameFromSetId(setId, attributeName, minimumTime);
    }

    /**
     * Retrieve a single custom attribute name to include on search results.
     *
     * @param setName human-readable name of the custom metadata set for which to retrieve the custom metadata attribute name
     * @param attributeName human-readable name of the attribute
     * @return the attribute name, strictly useful for inclusion in search results
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the custom metadata cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the custom metadata to retrieve
     * @see com.atlan.model.search.IndexSearchRequest.IndexSearchRequestBuilder#attributes(Collection)
     */
    public String getAttributeForSearchResults(String setName, String attributeName) throws AtlanException {
        return getAttributeForSearchResults(setName, attributeName, true);
    }

    /**
     * Retrieve a single custom attribute name to include on search results.
     *
     * @param setName human-readable name of the custom metadata set for which to retrieve the custom metadata attribute name
     * @param attributeName human-readable name of the attribute
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return the attribute name, strictly useful for inclusion in search results
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the custom metadata cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the custom metadata to retrieve
     * @see com.atlan.model.search.IndexSearchRequest.IndexSearchRequestBuilder#attributes(Collection)
     */
    public String getAttributeForSearchResults(String setName, String attributeName, boolean allowRefresh)
            throws AtlanException {
        return getAttributeForSearchResults(setName, attributeName, allowRefresh ? Long.MAX_VALUE : Long.MIN_VALUE);
    }

    /**
     * Retrieve a single custom attribute name to include on search results.
     *
     * @param setName human-readable name of the custom metadata set for which to retrieve the custom metadata attribute name
     * @param attributeName human-readable name of the attribute
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return the attribute name, strictly useful for inclusion in search results
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the custom metadata cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the custom metadata to retrieve
     * @see com.atlan.model.search.IndexSearchRequest.IndexSearchRequestBuilder#attributes(Collection)
     */
    public String getAttributeForSearchResults(String setName, String attributeName, long minimumTime)
            throws AtlanException {
        String setId = getSidForName(setName, false);
        String attrId = _getAttributeForSearchResults(setId, attributeName);
        if (attrId == null) {
            // If we've not found any names, refresh the cache and look again (could be stale)
            refresh(minimumTime);
            attrId = _getAttributeForSearchResults(setId, attributeName);
        }
        return attrId;
    }

    /**
     * Retrieve the full set of custom attributes to include on search results.
     *
     * @param setName human-readable name of the custom metadata set for which to retrieve a set of attribute names
     * @return a set of the attribute names, strictly useful for inclusion in search results
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the custom metadata cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the custom metadata to retrieve
     * @see com.atlan.model.search.IndexSearchRequest.IndexSearchRequestBuilder#attributes(Collection)
     */
    public Set<String> getAttributesForSearchResults(String setName) throws AtlanException {
        return getAttributesForSearchResults(setName, true);
    }

    /**
     * Retrieve the full set of custom attributes to include on search results.
     *
     * @param setName human-readable name of the custom metadata set for which to retrieve a set of attribute names
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return a set of the attribute names, strictly useful for inclusion in search results
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the custom metadata cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the custom metadata to retrieve
     * @see com.atlan.model.search.IndexSearchRequest.IndexSearchRequestBuilder#attributes(Collection)
     */
    public Set<String> getAttributesForSearchResults(String setName, boolean allowRefresh) throws AtlanException {
        return getAttributesForSearchResults(setName, allowRefresh ? Long.MAX_VALUE : Long.MIN_VALUE);
    }

    /**
     * Retrieve the full set of custom attributes to include on search results.
     *
     * @param setName human-readable name of the custom metadata set for which to retrieve a set of attribute names
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return a set of the attribute names, strictly useful for inclusion in search results
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the custom metadata cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the custom metadata to retrieve
     * @see com.atlan.model.search.IndexSearchRequest.IndexSearchRequestBuilder#attributes(Collection)
     */
    public Set<String> getAttributesForSearchResults(String setName, long minimumTime) throws AtlanException {
        String setId = getSidForName(setName, false);
        Set<String> dotNames = _getAttributesForSearchResults(setId);
        if (dotNames == null) {
            // If we've not found any names, refresh the cache and look again (could be stale)
            refresh(minimumTime);
            dotNames = _getAttributesForSearchResults(setId);
        }
        return dotNames == null ? Collections.emptySet() : Collections.unmodifiableSet(dotNames);
    }

    private Set<String> _getAttributesForSearchResults(String setId) {
        Map<String, String> subMap = getAttrIdFromName(setId);
        if (subMap != null) {
            Collection<String> attrIds = subMap.values();
            Set<String> dotNames = new HashSet<>();
            for (String attrId : attrIds) {
                dotNames.add(setId + "." + attrId);
            }
            return dotNames;
        }
        return null;
    }

    private String _getAttributeForSearchResults(String setId, String attrName) {
        Map<String, String> subMap = getAttrIdFromName(setId);
        if (subMap != null && subMap.containsKey(attrName)) {
            return setId + "." + subMap.get(attrName);
        }
        return null;
    }

    /**
     * Retrieve a specific custom metadata attribute definition by its unique Atlan-internal ID string.
     *
     * @param attributeId Atlan-internal ID string for the custom metadata attribute
     * @return attribute definition for the custom metadata attribute
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public AttributeDef getAttributeDef(String attributeId) throws AtlanException {
        return getAttributeDef(attributeId, true);
    }

    /**
     * Retrieve a specific custom metadata attribute definition by its unique Atlan-internal ID string.
     *
     * @param attributeId Atlan-internal ID string for the custom metadata attribute
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return attribute definition for the custom metadata attribute
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public AttributeDef getAttributeDef(String attributeId, boolean allowRefresh) throws AtlanException {
        return getAttributeDef(attributeId, allowRefresh ? Long.MAX_VALUE : Long.MIN_VALUE);
    }

    /**
     * Retrieve a specific custom metadata attribute definition by its unique Atlan-internal ID string.
     *
     * @param attributeId Atlan-internal ID string for the custom metadata attribute
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return attribute definition for the custom metadata attribute
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public AttributeDef getAttributeDef(String attributeId, long minimumTime) throws AtlanException {
        if (attributeId == null || attributeId.isEmpty())
            throw new InvalidRequestException(ErrorCode.MISSING_CM_ATTR_ID);
        AttributeDef found = getAttrById(attributeId);
        if (found == null) {
            refresh(minimumTime);
            found = getAttrById(attributeId);
        }
        return found;
    }

    /**
     * Translate the provided human-readable custom metadata attribute name to the Atlan-internal ID string.
     *
     * @param setId Atlan-internal ID string for the custom metadata set
     * @param attributeName human-readable name of the attribute
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return Atlan-internal ID string for the attribute
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the custom metadata property cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the custom metadata property to retrieve
     */
    private String getAttrIdForNameFromSetId(String setId, String attributeName, boolean allowRefresh)
            throws AtlanException {
        return getAttrIdForNameFromSetId(setId, attributeName, allowRefresh ? Long.MAX_VALUE : Long.MIN_VALUE);
    }

    /**
     * Translate the provided human-readable custom metadata attribute name to the Atlan-internal ID string.
     *
     * @param setId Atlan-internal ID string for the custom metadata set
     * @param attributeName human-readable name of the attribute
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return Atlan-internal ID string for the attribute
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the custom metadata property cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the custom metadata property to retrieve
     */
    private String getAttrIdForNameFromSetId(String setId, String attributeName, long minimumTime)
            throws AtlanException {
        if (setId == null || setId.isEmpty()) throw new InvalidRequestException(ErrorCode.MISSING_CM_ID);
        if (attributeName == null || attributeName.isEmpty())
            throw new InvalidRequestException(ErrorCode.MISSING_CM_ATTR_ID);
        Map<String, String> subMap = getAttrIdFromName(setId);
        String attrId = null;
        if (subMap != null) {
            attrId = subMap.get(attributeName);
        }
        if (attrId == null) {
            // If not found, refresh the cache and look again (could be stale)
            refresh(minimumTime);
            subMap = getAttrIdFromName(setId);
            if (subMap == null) {
                throw new NotFoundException(ErrorCode.CM_NO_ATTRIBUTES, setId);
            }
        } else {
            return attrId;
        }
        attrId = subMap.get(attributeName);
        if (attrId == null) {
            throw new NotFoundException(ErrorCode.CM_ATTR_NOT_FOUND_BY_NAME, attributeName, setId);
        }
        return attrId;
    }

    /**
     * Translate the provided Atlan-internal ID for a custom metadata attribute to the human-readable attribute name.
     *
     * @param setId Atlan-internal ID string for the custom metadata set
     * @param attributeId Atlan-internal ID string for the attribute
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return human-readable name of the attribute
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the custom metadata property cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no ID was provided for the custom metadata property to retrieve
     */
    private String getAttrNameForIdFromSetId(String setId, String attributeId, long minimumTime) throws AtlanException {
        if (setId == null || setId.isEmpty()) throw new InvalidRequestException(ErrorCode.MISSING_CM_ID);
        if (attributeId == null || attributeId.isEmpty())
            throw new InvalidRequestException(ErrorCode.MISSING_CM_ATTR_ID);
        Map<String, String> subMap = getAttrNameFromId(setId);
        String attrName = null;
        if (subMap != null) {
            attrName = subMap.get(attributeId);
        }
        if (attrName == null) {
            // If not found, refresh the cache and look again (could be stale)
            refresh(minimumTime);
            subMap = getAttrNameFromId(setId);
            if (subMap == null) {
                throw new NotFoundException(ErrorCode.CM_NO_ATTRIBUTES, setId);
            }
        } else {
            return attrName;
        }
        attrName = subMap.get(attributeId);
        if (attrName == null) {
            throw new NotFoundException(ErrorCode.CM_ATTR_NOT_FOUND_BY_ID, attributeId, setId);
        }
        return attrName;
    }

    /**
     * Construct a full set of attributes for the given custom metadata, but where all the values are null.
     *
     * @param customMetadataName for which to construct the empty map
     * @return a map from custom metadata attribute name (human-readable) to null values
     * @throws AtlanException on any API issues
     */
    public Map<String, Object> getEmptyAttributes(String customMetadataName) throws AtlanException {
        return getEmptyAttributes(customMetadataName, true);
    }

    /**
     * Construct a full set of attributes for the given custom metadata, but where all the values are null.
     *
     * @param customMetadataName for which to construct the empty map
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return a map from custom metadata attribute name (human-readable) to null values
     * @throws AtlanException on any API issues
     */
    public Map<String, Object> getEmptyAttributes(String customMetadataName, boolean allowRefresh)
            throws AtlanException {
        String cmId = getSidForName(customMetadataName, allowRefresh);
        Map<String, String> attributes = getAttrIdFromName(cmId);
        Map<String, Object> empty = new LinkedHashMap<>();
        for (String attrName : attributes.keySet()) {
            empty.put(attrName, Removable.NULL);
        }
        return empty;
    }

    /**
     * Translate the provided custom metadata object into a business attributes object.
     * We receive the businessAttributes object (rather than creating a new one) to initialize it with
     * any existing business attributes.
     *
     * @param customMetadata custom metadata object
     * @param businessAttributes business attributes object, which will be changed
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public void getBusinessAttributesFromCustomMetadata(
            Map<String, CustomMetadataAttributes> customMetadata, Map<String, Map<String, Object>> businessAttributes)
            throws AtlanException {
        getBusinessAttributesFromCustomMetadata(customMetadata, businessAttributes, true);
    }

    /**
     * Translate the provided custom metadata object into a business attributes object.
     * We receive the businessAttributes object (rather than creating a new one) to initialize it with
     * any existing business attributes.
     *
     * @param customMetadata custom metadata object
     * @param businessAttributes business attributes object, which will be changed
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public void getBusinessAttributesFromCustomMetadata(
            Map<String, CustomMetadataAttributes> customMetadata,
            Map<String, Map<String, Object>> businessAttributes,
            boolean allowRefresh)
            throws AtlanException {
        if (customMetadata != null) {
            for (Map.Entry<String, CustomMetadataAttributes> entry : customMetadata.entrySet()) {
                String cmName = entry.getKey();
                String cmId = getSidForName(cmName, allowRefresh);
                CustomMetadataAttributes attrs = entry.getValue();
                Map<String, Object> bmAttrs = businessAttributes.getOrDefault(cmId, new LinkedHashMap<>());
                getAttributesFromCustomMetadata(cmId, cmName, attrs, bmAttrs, allowRefresh);
                businessAttributes.put(cmId, bmAttrs);
            }
        }
    }

    /**
     * Translate the provided custom metadata attributes object into a hashed-string ID map of attributes and values.
     * We receive the attributes object (rather than creating a new one) to initialize it with any existing attributes.
     *
     * @param cmId hashed-string ID of the custom metadata set (structure)
     * @param cmName human-readable name of the custom metadata set (structure)
     * @param cma custom metadata attributes in human-readable form
     * @param attributes map of custom metadata attributes keyed by hashed-string ID of the attribute
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public void getAttributesFromCustomMetadata(
            String cmId, String cmName, CustomMetadataAttributes cma, Map<String, Object> attributes)
            throws AtlanException {
        getAttributesFromCustomMetadata(cmId, cmName, cma, attributes, true);
    }

    /**
     * Translate the provided custom metadata attributes object into a hashed-string ID map of attributes and values.
     * We receive the attributes object (rather than creating a new one) to initialize it with any existing attributes.
     *
     * @param cmId hashed-string ID of the custom metadata set (structure)
     * @param cmName human-readable name of the custom metadata set (structure)
     * @param cma custom metadata attributes in human-readable form
     * @param attributes map of custom metadata attributes keyed by hashed-string ID of the attribute
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public void getAttributesFromCustomMetadata(
            String cmId,
            String cmName,
            CustomMetadataAttributes cma,
            Map<String, Object> attributes,
            boolean allowRefresh)
            throws AtlanException {
        // Start by placing in any custom metadata for archived attributes
        if (cma.getArchivedAttributes() != null) {
            for (Map.Entry<String, Object> archived :
                    cma.getArchivedAttributes().entrySet()) {
                String archivedAttrName = archived.getKey();
                String archivedAttrId = getAttrIdForNameFromSetId(cmId, archivedAttrName, allowRefresh);
                attributes.put(archivedAttrId, archived.getValue());
            }
        }
        // Then layer on top all the active custom metadata attributes
        getIdMapFromNameMap(cmName, cma.getAttributes(), attributes, allowRefresh);
    }

    /**
     * Translate the provided map keyed by human-readable attribute name into a map of keyed by attribute id.
     * We receive the idToValue map (rather than creating a new one) to initialize it with
     * any existing business attributes.
     *
     * @param customMetadataName human-readable name of the custom metadata
     * @param nameToValue the attributes and their values for the custom metadata
     * @param idToValue the business metadata to map into
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public void getIdMapFromNameMap(
            String customMetadataName, Map<String, Object> nameToValue, Map<String, Object> idToValue)
            throws AtlanException {
        getIdMapFromNameMap(customMetadataName, nameToValue, idToValue, true);
    }

    /**
     * Translate the provided map keyed by human-readable attribute name into a map of keyed by attribute id.
     * We receive the idToValue map (rather than creating a new one) to initialize it with
     * any existing business attributes.
     *
     * @param customMetadataName human-readable name of the custom metadata
     * @param nameToValue the attributes and their values for the custom metadata
     * @param idToValue the business metadata to map into
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public void getIdMapFromNameMap(
            String customMetadataName,
            Map<String, Object> nameToValue,
            Map<String, Object> idToValue,
            boolean allowRefresh)
            throws AtlanException {
        String cmId = getSidForName(customMetadataName, allowRefresh);
        for (Map.Entry<String, Object> entry : nameToValue.entrySet()) {
            String attrName = entry.getKey();
            String cmAttrId = getAttrIdForNameFromSetId(cmId, attrName, allowRefresh);
            idToValue.put(cmAttrId, entry.getValue());
        }
    }

    /**
     * Translate the provided business attributes object into a custom metadata object.
     *
     * @param businessAttributes business attributes object
     * @return custom metadata object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public Map<String, CustomMetadataAttributes> getCustomMetadataFromBusinessAttributes(JsonNode businessAttributes)
            throws AtlanException {
        return getCustomMetadataFromBusinessAttributes(businessAttributes, true);
    }

    /**
     * Translate the provided business attributes object into a custom metadata object.
     *
     * @param businessAttributes business attributes object
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return custom metadata object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public Map<String, CustomMetadataAttributes> getCustomMetadataFromBusinessAttributes(
            JsonNode businessAttributes, boolean allowRefresh) throws AtlanException {
        return getCustomMetadataFromBusinessAttributes(
                businessAttributes, allowRefresh ? Long.MAX_VALUE : Long.MIN_VALUE);
    }

    /**
     * Translate the provided business attributes object into a custom metadata object.
     *
     * @param businessAttributes business attributes object
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return custom metadata object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public Map<String, CustomMetadataAttributes> getCustomMetadataFromBusinessAttributes(
            JsonNode businessAttributes, long minimumTime) throws AtlanException {
        Map<String, CustomMetadataAttributes> map = new LinkedHashMap<>();
        Iterator<String> itrCM = businessAttributes.fieldNames();
        while (itrCM.hasNext()) {
            String cmId = itrCM.next();
            try {
                String cmName = getNameForSid(cmId, minimumTime);
                JsonNode bmAttrs = businessAttributes.get(cmId);
                CustomMetadataAttributes cma = getCustomMetadataAttributes(cmId, bmAttrs, minimumTime);
                if (!cma.isEmpty()) {
                    map.put(cmName, cma);
                }
            } catch (NotFoundException e) {
                log.warn(
                        "Custom metadata with ID {} could not be found, likely deleted in parallel with this processing so it will be skipped.",
                        cmId);
                log.debug("Details:", e);
            }
        }
        return map;
    }

    /**
     * Translate the provided attributes structure into a human-readable map of attribute names to values.
     *
     * @param cmId custom metadata hashed-string ID
     * @param attributes embedded attributes structure with hashed-string IDs
     * @return deserialized CustomMetadataAttributes object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public CustomMetadataAttributes getCustomMetadataAttributes(String cmId, JsonNode attributes)
            throws AtlanException {
        return getCustomMetadataAttributes(cmId, attributes, true);
    }

    /**
     * Translate the provided attributes structure into a human-readable map of attribute names to values.
     *
     * @param cmId custom metadata hashed-string ID
     * @param attributes embedded attributes structure with hashed-string IDs
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return deserialized CustomMetadataAttributes object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public CustomMetadataAttributes getCustomMetadataAttributes(String cmId, JsonNode attributes, boolean allowRefresh)
            throws AtlanException {
        return getCustomMetadataAttributes(cmId, attributes, allowRefresh ? Long.MAX_VALUE : Long.MIN_VALUE);
    }

    /**
     * Translate the provided attributes structure into a human-readable map of attribute names to values.
     *
     * @param cmId custom metadata hashed-string ID
     * @param attributes embedded attributes structure with hashed-string IDs
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return deserialized CustomMetadataAttributes object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public CustomMetadataAttributes getCustomMetadataAttributes(String cmId, JsonNode attributes, long minimumTime)
            throws AtlanException {
        CustomMetadataAttributes.CustomMetadataAttributesBuilder<?, ?> builder = CustomMetadataAttributes.builder();
        Iterator<String> itrCMA = attributes.fieldNames();
        while (itrCMA.hasNext()) {
            String attrId = itrCMA.next();
            String cmAttrName = getAttrNameForIdFromSetId(cmId, attrId, minimumTime);
            JsonNode jsonValue = attributes.get(attrId);
            if (jsonValue.isArray()) {
                Set<Object> values = new HashSet<>();
                ArrayNode array = (ArrayNode) jsonValue;
                for (JsonNode element : array) {
                    Object primitive = deserializePrimitive(element);
                    values.add(primitive);
                }
                if (!values.isEmpty()) {
                    // It seems assets that previously had multivalued custom metadata that was later
                    // removed retain an empty set for that attribute, but this is equivalent to the
                    // custom metadata not existing from a UI and delete-ability perspective (so we will
                    // treat as non-existent in the deserialization as well)
                    if (isArchivedAttr(attrId)) {
                        builder.archivedAttribute(cmAttrName, values);
                    } else {
                        builder.attribute(cmAttrName, values);
                    }
                }
            } else if (jsonValue.isValueNode()) {
                Object primitive = deserializePrimitive(jsonValue);
                if (isArchivedAttr(attrId)) {
                    builder.archivedAttribute(cmAttrName, primitive);
                } else {
                    builder.attribute(cmAttrName, primitive);
                }
            } else {
                throw new LogicException(ErrorCode.UNABLE_TO_DESERIALIZE, jsonValue.toString());
            }
        }
        return builder.build();
    }

    /**
     * Translate the provided search result-embedded custom metadata into a custom metadata object.
     *
     * @param embeddedAttributes map of custom metadata that was embedded in search result attributes, keyed
     *                           by {@code <cmId>.<attrId>} with the value of that custom attribute
     * @return custom metadata object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public Map<String, CustomMetadataAttributes> getCustomMetadataFromSearchResult(
            Map<String, JsonNode> embeddedAttributes) throws AtlanException {
        return getCustomMetadataFromSearchResult(embeddedAttributes, true);
    }

    /**
     * Translate the provided search result-embedded custom metadata into a custom metadata object.
     *
     * @param embeddedAttributes map of custom metadata that was embedded in search result attributes, keyed
     *                           by {@code <cmId>.<attrId>} with the value of that custom attribute
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return custom metadata object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public Map<String, CustomMetadataAttributes> getCustomMetadataFromSearchResult(
            Map<String, JsonNode> embeddedAttributes, boolean allowRefresh) throws AtlanException {
        return getCustomMetadataFromSearchResult(embeddedAttributes, allowRefresh ? Long.MAX_VALUE : Long.MIN_VALUE);
    }

    /**
     * Translate the provided search result-embedded custom metadata into a custom metadata object.
     *
     * @param embeddedAttributes map of custom metadata that was embedded in search result attributes, keyed
     *                           by {@code <cmId>.<attrId>} with the value of that custom attribute
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return custom metadata object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public Map<String, CustomMetadataAttributes> getCustomMetadataFromSearchResult(
            Map<String, JsonNode> embeddedAttributes, long minimumTime) throws AtlanException {

        Map<String, CustomMetadataAttributes> map = new LinkedHashMap<>();

        Map<String, CustomMetadataAttributes.CustomMetadataAttributesBuilder<?, ?>> builderMap = new LinkedHashMap<>();
        for (Map.Entry<String, JsonNode> entry : embeddedAttributes.entrySet()) {
            String compositeId = entry.getKey();
            int indexOfDot = compositeId.indexOf(".");
            if (indexOfDot > 0) {
                String cmId = compositeId.substring(0, indexOfDot);
                try {
                    String cmName = getNameForSid(cmId, minimumTime);
                    if (!builderMap.containsKey(cmName)) {
                        builderMap.put(cmName, CustomMetadataAttributes.builder());
                    }
                    String attrId = compositeId.substring(indexOfDot + 1);
                    String cmAttrName = getAttrNameForIdFromSetId(cmId, attrId, minimumTime);
                    JsonNode jsonValue = entry.getValue();
                    if (jsonValue.isArray()) {
                        Set<Object> values = new HashSet<>();
                        ArrayNode array = (ArrayNode) jsonValue;
                        for (JsonNode element : array) {
                            Object primitive = deserializePrimitive(element);
                            values.add(primitive);
                        }
                        if (!values.isEmpty()) {
                            // It seems assets that previously had multivalued custom metadata that was later
                            // removed retain an empty set for that attribute, but this is equivalent to the
                            // custom metadata not existing from a UI and delete-ability perspective (so we will
                            // treat as non-existent in the deserialization as well)
                            if (isArchivedAttr(attrId)) {
                                builderMap.get(cmName).archivedAttribute(cmAttrName, values);
                            } else {
                                builderMap.get(cmName).attribute(cmAttrName, values);
                            }
                        }
                    } else if (jsonValue.isValueNode()) {
                        Object primitive = deserializePrimitive(jsonValue);
                        if (isArchivedAttr(attrId)) {
                            builderMap.get(cmName).archivedAttribute(cmAttrName, primitive);
                        } else {
                            builderMap.get(cmName).attribute(cmAttrName, primitive);
                        }
                    } else {
                        throw new LogicException(ErrorCode.UNABLE_TO_DESERIALIZE, jsonValue.toString());
                    }
                } catch (NotFoundException e) {
                    log.warn(
                            "Custom metadata with ID {} could not be found, likely deleted in parallel with this processing so it will be skipped.",
                            cmId);
                    log.debug("Details:", e);
                }
            }
        }

        for (Map.Entry<String, CustomMetadataAttributes.CustomMetadataAttributesBuilder<?, ?>> entry :
                builderMap.entrySet()) {
            String cmName = entry.getKey();
            CustomMetadataAttributes cma = entry.getValue().build();
            if (!cma.isEmpty()) {
                map.put(cmName, cma);
            }
        }
        return map;
    }

    public static Object deserializePrimitive(JsonNode jsonValue) throws LogicException {
        if (jsonValue.isValueNode()) {
            if (jsonValue.isTextual()) {
                return jsonValue.asText();
            } else if (jsonValue.isBoolean()) {
                return jsonValue.asBoolean();
            } else if (jsonValue.isIntegralNumber()) {
                return jsonValue.asLong();
            } else if (jsonValue.isFloatingPointNumber()) {
                return jsonValue.asDouble();
            } else if (jsonValue.isNull()) {
                return null;
            } else {
                throw new LogicException(ErrorCode.UNABLE_TO_DESERIALIZE, jsonValue.toString());
            }
        } else {
            throw new LogicException(ErrorCode.UNABLE_TO_DESERIALIZE, jsonValue.toString());
        }
    }
}
