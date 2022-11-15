/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import java.util.*;

public abstract class AbstractCrawler {

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
}
