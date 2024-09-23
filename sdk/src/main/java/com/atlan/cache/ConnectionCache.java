/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.exception.*;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.fields.AtlanField;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating between a connection's simplified name its details.
 * - id = qualifiedName of the connection (with epoch), for example: default/snowflake/1234567890
 * - name = simple name of the form {{connectorType}}/{{connectorName}}, for example: snowflake/development
 */
@Slf4j
public class ConnectionCache extends AbstractLazyCache {

    private static final List<AtlanField> connectionAttributes = List.of(Connection.CONNECTOR_TYPE);

    private final AtlanClient client;

    public ConnectionCache(AtlanClient client) {
        this.client = client;
    }

    /** {@inheritDoc} */
    @Override
    public void lookupById(String connectionQN) throws AtlanException {
        if (connectionQN != null) {
            Optional<Asset> candidate = Connection.select(client)
                .where(Connection.QUALIFIED_NAME.eq(connectionQN))
                .includesOnResults(connectionAttributes)
                .stream()
                .findFirst();
            if (candidate.isPresent() && candidate.get() instanceof Connection connection) {
                cache(connectionQN,  connection.getConnectorType().getValue() + "/" + connection.getName());
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void lookupByName(String name) throws AtlanException {
        if (name != null) {
            String[] tokens = name.split("/");
            if (tokens.length > 1) {
                String connectorType = tokens[0];
                String connectorName = name.substring(connectorType.length() + 1);
                Optional<Asset> candidate = Connection.select(client)
                    .where(Connection.NAME.eq(connectorName))
                    .where(Connection.CONNECTOR_TYPE.eq(connectorType))
                    .includesOnResults(connectionAttributes)
                    .stream()
                    .findFirst();
                if (candidate.isPresent() && candidate.get() instanceof Connection connection) {
                    cache(connection.getQualifiedName(), name);
                }
            }
        }
    }
}
