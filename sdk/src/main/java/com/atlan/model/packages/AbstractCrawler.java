/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.Credential;
import com.atlan.model.assets.Connection;
import com.atlan.model.enums.AtlanConnectorType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("cast")
public abstract class AbstractCrawler extends AbstractPackage {

    /**
     * Builds a connection using the provided parameters, which will be the target for the package
     * to crawl assets.
     *
     * @param client connectivity to the Atlan tenant on which the package will run
     * @param connectionName name for the connection
     * @param connectionType type of connector for the connection
     * @param roles admin roles for the connection
     * @param groups admin groups for the connection
     * @param users admin users for the connection
     * @param allowQuery whether to allow data to be queried in the connection (true) or not (false)
     * @param allowQueryPreview whether to allow sample data to be viewed for assets in the connection (true) or not (false)
     * @param rowLimit maximum number of rows that can be returned by a query
     * @param sourceLogo logo to use for the source
     * @throws AtlanException if there is not at least one role, group or user defined as an admin (or any of them are invalid)
     */
    @JsonIgnore
    public static Connection getConnection(
            AtlanClient client,
            String connectionName,
            AtlanConnectorType connectionType,
            List<String> roles,
            List<String> groups,
            List<String> users,
            boolean allowQuery,
            boolean allowQueryPreview,
            long rowLimit,
            String sourceLogo)
            throws AtlanException {
        return Connection.creator(client, connectionName, connectionType, roles, groups, users)
                .allowQuery(allowQuery)
                .allowQueryPreview(allowQueryPreview)
                .rowLimit(rowLimit)
                .defaultCredentialGuid("{{credentialGuid}}")
                .sourceLogo(sourceLogo)
                .isDiscoverable(true)
                .isEditable(false)
                .build();
    }

    /**
     * Build an exact match filter from the provided map of databases and schemas.
     *
     * @param rawFilter map keyed by database name with each value being a list of schemas
     * @return an exact-match filter map, usable in crawlers include / exclude filters
     */
    static Map<String, List<String>> buildHierarchicalFilter(Map<String, List<String>> rawFilter) {
        Map<String, List<String>> toInclude = new HashMap<>();
        if (rawFilter != null) {
            for (Map.Entry<String, List<String>> entry : rawFilter.entrySet()) {
                String dbName = entry.getKey();
                List<String> schemas = entry.getValue();
                List<String> exactSchemas = new ArrayList<>();
                for (String schema : schemas) {
                    exactSchemas.add("^" + schema + "$");
                }
                toInclude.put("^" + dbName + "$", exactSchemas);
            }
        }
        return toInclude;
    }

    /**
     * Build a filter from the provided list of object names / IDs.
     *
     * @param rawFilter list of objects for the filter
     * @return a filter map, usable in crawlers include / exclude filters
     */
    static Map<String, Map<String, String>> buildFlatFilter(List<String> rawFilter) {
        Map<String, Map<String, String>> toInclude = new HashMap<>();
        if (rawFilter != null) {
            for (String entry : rawFilter) {
                toInclude.put(entry, Collections.emptyMap());
            }
        }
        return toInclude;
    }

    /**
     * Build a filter for dbt projects from the provided map of account and project IDs.
     *
     * @param rawFilter map keyed by account ID with a list of project IDs as its value
     * @return a filter map, usable in the dbt Cloud crawler include / exclude filters
     */
    static Map<String, Map<String, Map<String, String>>> buildDbtCloudFilter(Map<String, List<String>> rawFilter) {
        Map<String, Map<String, Map<String, String>>> toInclude = new HashMap<>();
        if (rawFilter != null) {
            for (Map.Entry<String, List<String>> entry : rawFilter.entrySet()) {
                String accountId = entry.getKey();
                if (!toInclude.containsKey(accountId)) {
                    toInclude.put(accountId, new HashMap<>());
                }
                List<String> projects = entry.getValue();
                for (String projectId : projects) {
                    toInclude.get(accountId).put(projectId, Collections.emptyMap());
                }
            }
        }
        return toInclude;
    }

    public abstract static class AbstractCrawlerBuilder<
                    C extends AbstractCrawler, B extends AbstractCrawlerBuilder<C, B>>
            extends AbstractPackageBuilder<C, B> {

        /** Connectivity to an Atlan tenant on which the crawler is intended to run. */
        protected AtlanClient client;

        /** Connection through which the crawler will manage its assets. */
        protected Connection connection;

        /** Local credential builder, to gradually build up a complete set of credentials across operations. */
        protected final Credential.CredentialBuilder<?, ?> localCreds = Credential.builder();

        /**
         * Set up the crawler with its foundational information.
         *
         * @param prefix the unique prefix that identifies the type of the crawler
         * @param name the unique name of the crawler package
         * @param client connectivity to an Atlan tenant through which to run the crawler
         * @param connection through which the crawler will manage its assets
         * @return the builder, with all the common elements of the crawler configured
         */
        B setup(String prefix, String name, AtlanClient client, Connection connection) {
            this.client = client;
            this.connection = connection;
            this.epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this._prefix(prefix)
                    ._name(name)
                    ._runName(prefix + "-" + epoch)
                    .metadata();
        }
    }
}
