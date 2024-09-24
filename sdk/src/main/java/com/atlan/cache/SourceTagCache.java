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

    private static final List<AtlanField> tagAttributes = List.of(Asset.NAME);

    public SourceTagCache(AtlanClient client) {
        super(client);
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
