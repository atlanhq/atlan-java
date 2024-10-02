/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.exception.*;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.ITag;
import com.atlan.model.fields.AtlanField;
import com.atlan.util.StringUtils;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating between source-synced tags and the qualifiedName of such
 * tags.
 * - id = qualifiedName of the source tag (with epoch), for example: default/snowflake/1234567890/DB/SCHEMA/TAG_NAME
 * - name = simple name of the form {{connectorType}}/{{connectorName}}@@DB/SCHEMA/TAG_NAME, for example: snowflake/development@@DB/SCHEMA/TAG_NAME
 */
@Slf4j
public class SourceTagCache extends AbstractAssetCache {

    private static final List<AtlanField> tagAttributes = List.of(Asset.NAME, ITag.MAPPED_ATLAN_TAG_NAME);

    private final Map<String, Set<String>> atlanTagIdToGuids = new ConcurrentHashMap<>();

    public SourceTagCache(AtlanClient client) {
        super(client);
    }

    /** {@inheritDoc} */
    @Override
    protected String cache(Asset asset) {
        String guid = super.cache(asset);
        if (guid != null && asset instanceof ITag tag) {
            String tagId = tag.getMappedAtlanTagName();
            if (tagId != null) {
                if (!atlanTagIdToGuids.containsKey(tagId)) {
                    atlanTagIdToGuids.put(tagId, new HashSet<>());
                }
                atlanTagIdToGuids.get(tagId).add(guid);
            }
        }
        return guid;
    }

    /** {@inheritDoc} */
    @Override
    public void lookupByGuid(String guid) throws AtlanException {
        if (guid != null && !guid.isEmpty()) {
            Optional<Asset> candidate = client
                    .assets
                    .select()
                    .where(Asset.SUPER_TYPE_NAMES.eq(ITag.TYPE_NAME))
                    .where(Asset.GUID.eq(guid))
                    .includesOnResults(tagAttributes)
                    .stream()
                    .findFirst();
            if (candidate.isPresent() && candidate.get() instanceof ITag tag) {
                cache((Asset) tag);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void lookupByQualifiedName(String sourceTagQN) throws AtlanException {
        if (sourceTagQN != null && !sourceTagQN.isEmpty()) {
            Optional<Asset> candidate = client
                    .assets
                    .select()
                    .where(Asset.SUPER_TYPE_NAMES.eq(ITag.TYPE_NAME))
                    .where(Asset.QUALIFIED_NAME.eq(sourceTagQN))
                    .includesOnResults(tagAttributes)
                    .stream()
                    .findFirst();
            if (candidate.isPresent() && candidate.get() instanceof ITag tag) {
                cache((Asset) tag);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void lookupByName(ObjectName name) throws AtlanException {
        if (name instanceof SourceTagName stn) {
            ObjectName connectionName = stn.getConnection();
            String connectionQN =
                    client.getConnectionCache().getByName(connectionName).getQualifiedName();
            String sourceTagQN = connectionQN + "/" + stn.getPartialTagName();
            Optional<Asset> candidate = client
                    .assets
                    .select()
                    .where(Asset.SUPER_TYPE_NAMES.eq(ITag.TYPE_NAME))
                    .where(Asset.QUALIFIED_NAME.eq(sourceTagQN))
                    .includesOnResults(tagAttributes)
                    .stream()
                    .findFirst();
            if (candidate.isPresent() && candidate.get() instanceof ITag tag) {
                cache((Asset) tag);
            }
        }
    }

    /**
     * Logic to refresh the cache for a single object from Atlan.
     *
     * @param internalAtlanTagId internal hashed-string ID for the mapped Atlan tag
     * @throws AtlanException on any underlying API issues
     */
    public void lookupByMappedAtlanTag(String internalAtlanTagId) throws AtlanException {
        if (internalAtlanTagId != null && !internalAtlanTagId.isEmpty()) {
            List<Asset> candidates = client
                    .assets
                    .select()
                    .where(Asset.SUPER_TYPE_NAMES.eq(ITag.TYPE_NAME))
                    .where(ITag.MAPPED_ATLAN_TAG_NAME.eq(internalAtlanTagId))
                    .includesOnResults(tagAttributes)
                    .stream()
                    .toList();
            if (!candidates.isEmpty()) {
                for (Asset candidate : candidates) {
                    cache(candidate);
                }
            }
        }
    }

    /**
     * Retrieve tags from the cache by their mapped Atlan tag, looking it up and
     * adding it to the cache if it is not found there.
     *
     * @param internalAtlanTagId internal hashed-string ID for the mapped Atlan tag
     * @return all mapped tags (if found)
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no internal hashed-string Atlan ID was provided
     */
    public List<ITag> getByMappedAtlanTag(String internalAtlanTagId) throws AtlanException {
        return getByMappedAtlanTag(internalAtlanTagId, true);
    }

    /**
     * Retrieve tags from the cache by their mapped Atlan tag.
     *
     * @param internalAtlanTagId internal hashed-string ID for the mapped Atlan tag
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return all mapped tags (if found)
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no internal hashed-string Atlan ID was provided
     */
    public List<ITag> getByMappedAtlanTag(String internalAtlanTagId, boolean allowRefresh) throws AtlanException {
        if (internalAtlanTagId != null && !internalAtlanTagId.isEmpty()) {
            Set<String> found = atlanTagIdToGuids.get(internalAtlanTagId);
            if (found == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                lookupByMappedAtlanTag(internalAtlanTagId);
                found = atlanTagIdToGuids.get(internalAtlanTagId);
            }
            if (found == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, internalAtlanTagId);
            }
            List<ITag> list = new ArrayList<>();
            for (String guid : found) {
                ITag tag = (ITag) getByGuid(guid, false);
                if (tag != null) {
                    list.add(tag);
                }
            }
            return list;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ID);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ObjectName getName(Asset asset) {
        if (asset instanceof ITag tag) {
            try {
                return new SourceTagName(client, tag);
            } catch (AtlanException e) {
                log.error("Unable to construct a source tag name for: {}", asset.getQualifiedName());
                log.debug("Details:", e);
            }
        }
        return null;
    }

    /**
     * Unique identity for a source tag, in the form: {{connectorType}}/{{connectorName}}@@DB/SCHEMA/TAG_NAME
     * For example: snowflake/development@@DB/SCHEMA/TAG_NAME
     */
    @Getter
    @EqualsAndHashCode
    public static final class SourceTagName implements ObjectName {
        private static final String CONNECTION_DELIMITER = "@@";

        ObjectName connection;
        String partialTagName;

        public SourceTagName(AtlanClient client, ITag tag) throws AtlanException {
            String sourceTagQN = tag.getQualifiedName();
            String connectionQN = StringUtils.getConnectionQualifiedName(sourceTagQN);
            Connection conn = (Connection) client.getConnectionCache().getByQualifiedName(connectionQN);
            this.connection = new ConnectionCache.ConnectionName(conn);
            this.partialTagName = sourceTagQN.substring(connectionQN.length() + 1);
        }

        public SourceTagName(String identity) {
            if (identity != null && !identity.isEmpty()) {
                String[] tokens = identity.split(CONNECTION_DELIMITER);
                if (tokens.length == 2) {
                    this.connection = new ConnectionCache.ConnectionName(tokens[0]);
                    this.partialTagName = tokens[1];
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return connection.toString() + CONNECTION_DELIMITER + partialTagName;
        }
    }
}
