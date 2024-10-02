/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.ITag;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.serde.Removable;
import com.atlan.serde.Serde;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Class to compose compound queries combining various conditions, that can be used
 * to generate links (URLs) for the discovery page in Atlan.
 */
@SuperBuilder(builderMethodName = "_internal")
@EqualsAndHashCode
@Slf4j
public class LinkableQuery {

    private AtlanClient client;

    /**
     * Begin building a linkable query.
     *
     * @param client connectivity to the Atlan tenant for which to build the query.
     * @return a builder upon which the query can be composed
     */
    public static LinkableQueryBuilder<?, ?> builder(AtlanClient client) {
        return _internal().client(client);
    }

    /**
     * Only assets with one of these certificate statuses will be included.
     * To include assets with no certificate, use {@code null} as a value in the list.
     */
    @Singular
    private List<CertificateStatus> certificateStatuses;

    /**
     * Asset hierarchy (connector type, connection qualifiedName) to limit assets.
     */
    private AssetHierarchy hierarchy;

    /**
     * Criteria based on properties that exist across all asset types, that must be present on every asset.
     */
    @Singular
    private List<DiscoveryFilter> properties;

    // TODO: Specialized filters (dbt, table/view, etc)

    /**
     * Criteria based on matching one or more of the provided tags (and optionally a value), that must
     * be present on every asset.
     */
    @Singular
    private List<TagFilter> tags;

    /** List of types by which to limit the assets. */
    @Singular
    private List<String> typeNames;

    /** Convert the linkable query into a string. */
    @Override
    public String toString() {
        FilterParams fp = new FilterParams(this);
        try {
            return Serde.allInclusiveMapper.writeValueAsString(fp);
        } catch (JsonProcessingException e) {
            log.error("Unable to convert parameters to a linkable query.", e);
            return "";
        }
    }

    /**
     * Convert this linkable query into an actual link (URL).
     * @return the URL (without tenant domain / hostname) for accessing this limited set of assets
     */
    public String getUrl() {
        String params = toString();
        // Note: needs to be double-url-encoded
        String encoded = StringUtils.encodeContent(StringUtils.encodeContent(params));
        return "/assets?searchAndFilterCriteria=" + encoded;
    }

    /**
     * Convert this linkable query into an actual link (URL).
     * Note: this will not work if the code is running in the tenant itself (e.g. via a custom package).
     * @return the full URL (including tenant domain) for accessing this limited set of assets in the tenant
     */
    public String getFullUrl() {
        return client.getBaseUrl() + getUrl();
    }

    @Getter
    @SuperBuilder
    @EqualsAndHashCode
    public static final class AssetHierarchy {
        /** Name of the connector type to limit assets by. */
        String connectorName;

        /** Unique name of the connection to limit assets by. */
        String connectionQualifiedName;

        /**
         * Name of the attribute within the lowest level of the asset hierarchy to limit assets by.
         * For example, if you want to limit assets by schema, use {@code schemaQualifiedName}, but if
         * you only want to limit assets by database use {@code databaseQualifiedName}.
         */
        String attributeName;

        /** Qualified name prefix that matches the provided {@code attributeName} by which to limit assets. */
        String attributeValue;
    }

    @Getter
    private static final class FilterParams {
        private final Map<String, Object> filters = new LinkedHashMap<>();
        private final Map<String, Object> postFilters = new LinkedHashMap<>();

        FilterParams(LinkableQuery query) {
            if (query.certificateStatuses != null) {
                List<Object> list = new ArrayList<>();
                for (CertificateStatus status : query.certificateStatuses) {
                    if (status == null) {
                        list.add(Removable.NULL);
                    } else {
                        list.add(status.getValue());
                    }
                }
                filters.put("certificateStatus", list);
            }
            if (query.hierarchy != null) {
                filters.put("hierarchy", query.hierarchy);
            }
            if (query.properties != null) {
                Map<String, Object> propertyMap = new LinkedHashMap<>();
                for (DiscoveryFilter filter : query.properties) {
                    propertyMap.put(filter.operand, List.of(filter));
                }
                filters.put("properties", propertyMap);
            }
            if (query.tags != null) {
                List<TagFilter> tags = new ArrayList<>();
                for (TagFilter filter : query.tags) {
                    if (filter.displayName != null && !filter.displayName.isBlank()) {
                        try {
                            String clsId = query.client.getAtlanTagCache().getIdForName(filter.displayName);
                            TagFilter.TagFilterBuilder<?, ?> builder =
                                    filter.toBuilder().name(clsId);
                            if (filter.tagValues != null && !filter.tagValues.isEmpty()) {
                                List<ITag> sourceTags =
                                        query.client.getSourceTagCache().getByMappedAtlanTag(clsId);
                                List<String> qualifiedNames = sourceTags.stream()
                                        .map(ITag::getQualifiedName)
                                        .toList();
                                builder.clearTagValues();
                                for (TagFilter.TagValue value : filter.tagValues) {
                                    if (value.tagQFNames == null || value.tagQFNames.isEmpty()) {
                                        TagFilter.TagValue withQNs = value.toBuilder()
                                                .tagQFNames(qualifiedNames)
                                                .build();
                                        builder.tagValue(withQNs);
                                    } else {
                                        // If there is already a specific qualifiedName, retain it
                                        builder.tagValue(value);
                                    }
                                }
                            }
                            TagFilter resolved = builder.build();
                            tags.add(resolved);
                        } catch (AtlanException e) {
                            log.error("Unable to translate tag -- skipping: {}", filter.displayName, e);
                        }
                    }
                }
                filters.put("__traitNames", Map.of("classifications", tags));
            }
            if (query.typeNames != null) {
                List<TypeName> types = new ArrayList<>();
                for (String typeName : query.typeNames) {
                    types.add(new TypeName(typeName));
                }
                postFilters.put("typeName", types);
            }
        }
    }

    @Getter
    private static final class TypeName {
        String id;
        String label;

        TypeName(String typeName) {
            this.id = typeName;
            this.label = StringUtils.getTitleCase(typeName);
        }
    }

    public abstract static class LinkableQueryBuilder<C extends LinkableQuery, B extends LinkableQueryBuilder<C, B>> {

        /**
         * Limit assets to a given connection.
         * @param connection for which to limit assets
         * @return the query builder, limited to assets from the specific connection
         */
        public B forConnection(Connection connection) {
            String qn = connection.getQualifiedName();
            return hierarchy(AssetHierarchy.builder()
                    .connectionQualifiedName(connection.getQualifiedName())
                    .connectorName(
                            Connection.getConnectorTypeFromQualifiedName(qn).getValue())
                    .attributeName("")
                    .attributeValue("")
                    .build());
        }

        /**
         * Convert this linkable query into an actual link (URL).
         * @return the URL (without tenant domain / hostname) for accessing this limited set of assets
         */
        public String toUrl() {
            return build().getUrl();
        }

        /**
         * Convert this linkable query into an actual link (URL).
         * Note: this will not work if the code is running in the tenant itself (e.g. via a custom package).
         * @return the full URL (including tenant domain) for accessing this limited set of assets in the tenant
         */
        public String toFullUrl() {
            return build().getFullUrl();
        }
    }
}
