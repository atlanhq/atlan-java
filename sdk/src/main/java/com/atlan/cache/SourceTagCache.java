/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.exception.*;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.ITag;
import com.atlan.model.fields.AtlanField;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.atlan.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating between source-synced tags and the qualifiedName of such
 * tags.
 * - id = qualifiedName of the source tag (with epoch), for example: default/snowflake/1234567890/DB/SCHEMA/TAG_NAME
 * - name = simple name of the form {{connectorType}}/{{connectorName}}@@DB/SCHEMA/TAG_NAME, for example: snowflake/development@@DB/SCHEMA/TAG_NAME
 */
@Slf4j
public class SourceTagCache extends AbstractLazyCache {

    private static final String CONNECTION_DELIMITER = "@@";
    private final Map<String, ITag> idToTag = new ConcurrentHashMap<>();
    private static final List<AtlanField> tagAttributes = List.of(Asset.NAME);

    private final AtlanClient client;

    public SourceTagCache(AtlanClient client) {
        this.client = client;
    }

    /** {@inheritDoc} */
    @Override
    public void lookupById(String sourceTagQN) throws AtlanException {
        if (sourceTagQN != null) {
            Optional<Asset> candidate = client.assets.select()
                .where(Asset.SUPER_TYPE_NAMES.eq(ITag.TYPE_NAME))
                .where(Asset.QUALIFIED_NAME.eq(sourceTagQN))
                .includesOnResults(tagAttributes)
                .stream()
                .findFirst();
            if (candidate.isPresent() && candidate.get() instanceof ITag tag) {
                String connectionQN = StringUtils.getConnectionQualifiedName(sourceTagQN);
                String connectionName = client.getConnectionCache().getNameForId(connectionQN);
                String sourceTagPartial = sourceTagQN.substring(connectionQN.length() + 1);
                cache(sourceTagQN, connectionName + CONNECTION_DELIMITER + sourceTagPartial);
                idToTag.put(sourceTagQN, tag);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void lookupByName(String name) throws AtlanException {
        if (name != null) {
            String[] tokens = name.split(CONNECTION_DELIMITER);
            if (tokens.length == 2) {
                String connectionString = tokens[0];
                String sourceTagPartialQN = tokens[1];
                String connectionQN = client.getConnectionCache().getIdForName(connectionString);
                String sourceTagQN = connectionQN + "/" + sourceTagPartialQN;
                Optional<Asset> candidate = client.assets.select()
                    .where(Asset.SUPER_TYPE_NAMES.eq(ITag.TYPE_NAME))
                    .where(Asset.QUALIFIED_NAME.eq(sourceTagQN))
                    .includesOnResults(tagAttributes)
                    .stream()
                    .findFirst();
                if (candidate.isPresent() && candidate.get() instanceof ITag tag) {
                    cache(sourceTagQN, name);
                    idToTag.put(sourceTagQN, tag);
                }
            }
        }
    }

    /**
     * Retrieve a source tag by its qualifiedName.
     *
     * @param sourceTagQN unique name of the source tag
     * @return the source tag's definition
     * @throws AtlanException on any issue with the underlying API calls
     */
    public ITag getSourceTagById(String sourceTagQN) throws AtlanException {
        getNameForId(sourceTagQN); // Make sure it's cached before trying to retrieve it
        return idToTag.get(sourceTagQN);
    }

    /**
     * Retrieve a source tag by its simplified name.
     *
     * @param name simplified name of the source tag (excluding the connection's epoch)
     * @return the source tag's definition
     * @throws AtlanException on any issue with the underlying API calls
     */
    public ITag getSourceTagByName(String name) throws AtlanException {
        String id = getIdForName(name); // Make sure it's cached before trying to retrieve it
        return idToTag.get(id);
    }
}
