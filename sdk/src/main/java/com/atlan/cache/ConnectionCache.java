/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.exception.*;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.fields.AtlanField;
import java.util.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating between a connection's simplified name its details.
 * - id = qualifiedName of the connection (with epoch), for example: default/snowflake/1234567890
 * - name = simple name of the form {{connectorType}}/{{connectorName}}, for example: snowflake/development
 */
@Slf4j
public class ConnectionCache extends AbstractAssetCache {

    private static final List<AtlanField> connectionAttributes =
            List.of(Connection.NAME, Connection.CONNECTOR_TYPE, Connection.STATUS);

    public ConnectionCache(AtlanClient client) {
        super(client);
    }

    /** {@inheritDoc} */
    @Override
    public void lookupByGuid(String guid) throws AtlanException {
        if (guid != null && !guid.isEmpty()) {
            Optional<Asset> candidate =
                    Connection.select(client)
                            .where(Connection.GUID.eq(guid))
                            .includesOnResults(connectionAttributes)
                            .stream()
                            .findFirst();
            if (candidate.isPresent() && candidate.get() instanceof Connection connection) {
                cache(connection);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void lookupByQualifiedName(String connectionQN) throws AtlanException {
        if (connectionQN != null && !connectionQN.isEmpty()) {
            Optional<Asset> candidate = Connection.select(client)
                    .where(Connection.QUALIFIED_NAME.eq(connectionQN))
                    .includesOnResults(connectionAttributes)
                    .stream()
                    .findFirst();
            if (candidate.isPresent() && candidate.get() instanceof Connection connection) {
                cache(connection);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void lookupByName(ObjectName name) throws AtlanException {
        if (name instanceof ConnectionName identity) {
            List<Connection> results =
                    Connection.findByName(client, identity.getName(), identity.getType(), connectionAttributes);
            if (!results.isEmpty()) {
                if (results.size() > 1) {
                    log.warn(
                            "Found multiple connections of the same type with the same name, caching only the first: {}",
                            name);
                }
                cache(results.get(0));
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public ObjectName getName(Asset asset) {
        if (asset instanceof Connection connection) {
            return new ConnectionName(connection);
        }
        return null;
    }

    /**
     * Unique identity for a connection, in the form: {{type}}/{{name}}
     * For example: snowflake/development
     */
    @Getter
    @EqualsAndHashCode
    public static final class ConnectionName implements ObjectName {
        String name;
        String type;

        public ConnectionName(Connection connection) {
            this.name = connection.getName();
            this.type = connection.getConnectorName();
        }

        public ConnectionName(String identity) {
            if (identity != null && !identity.isEmpty()) {
                String[] tokens = identity.split("/");
                if (tokens.length > 1) {
                    this.type = tokens[0];
                    this.name = identity.substring(tokens[0].length() + 1);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return type + "/" + name;
        }
    }
}
