/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.discovery;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.AtlanGroup;
import com.atlan.model.admin.AtlanUser;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.GlossaryTerm;
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

    /** Owners by which to limit assets. */
    private Owners owners;

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

    /** Terms by which to limit assets. */
    private Terms terms;

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
    static final class AssetHierarchy {
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
    @SuperBuilder
    @EqualsAndHashCode
    @SuppressWarnings("cast")
    static final class Owners {
        /** List of UUIDs of owners to limit assets by. */
        @Singular
        List<String> ownerIds;

        /** Listing of details for the owners that are specified, keyed by the username. */
        @Singular
        Map<String, OwnerDetails> selectedOwners;

        /** List of usernames of owners to limit assets by. */
        @Singular
        List<String> ownerUsers;

        /** Listing of details for the owners that are specified, keyed by the group alias. */
        @Singular
        Map<String, AtlanGroup> selectedGroups;

        /** List of group aliases of owners to limit assets by. */
        @Singular
        List<String> ownerGroups;

        /** If true, include assets with no owners, otherwise only include assets with selected owners. */
        Boolean empty;
    }

    @Getter
    @SuperBuilder
    @EqualsAndHashCode
    static final class Terms {
        /** Whether to include assets with no terms assigned (true) or not (false). */
        Boolean empty;

        /** Comparison operator to use for matching the terms specified. */
        String operator;

        /** Details of the terms to use for matching. */
        @Singular
        List<TermDetails> terms;
    }

    @Getter
    @SuperBuilder
    @EqualsAndHashCode
    static final class OwnerDetails {
        /** First name of the user. */
        String firstName;

        /** UUID of the user. */
        String id;

        /** Username of the user. */
        String username;

        /** Surname of the user. */
        String lastName;

        /** Whether the user is active (true) or deactivated (false). */
        Boolean enabled;

        /** Email address of the user. */
        String email;

        public static OwnerDetails from(AtlanUser user) {
            if (user == null) return null;
            return builder()
                    .firstName(user.getFirstName())
                    .id(user.getId())
                    .username(user.getUsername())
                    .lastName(user.getLastName())
                    .enabled(user.getEnabled())
                    .email(user.getEmail())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @EqualsAndHashCode
    @SuppressWarnings("cast")
    static final class TermDetails {
        /** UUID of the term. */
        String guid;

        /** Unique name of the term. */
        String qualifiedName;

        /** Type of the term. */
        final String typeName = GlossaryTerm.TYPE_NAME;

        /** Attributes of the term. */
        Map<String, String> attributes;
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
                    propertyMap.put(filter.filterKey, List.of(filter));
                }
                filters.put("properties", propertyMap);
            }
            if (query.tags != null) {
                filters.put("__traitNames", Map.of("classifications", query.tags));
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
         * Limit assets to a specified subset of those in a connection.
         * @param qualifiedNamePrefix full qualifiedName prefix that all assets in the subset should start with
         * @param denormalizedAttributeName name of the denormalized attribute where the prefix can be found on all assets (for example, {@code schemaQualifiedName})
         * @return the query builder, limited to a subset of assets in a connection
         */
        public B forPrefix(String qualifiedNamePrefix, String denormalizedAttributeName) {
            String connectionQN = StringUtils.getConnectionQualifiedName(qualifiedNamePrefix);
            if (connectionQN != null) {
                return hierarchy(AssetHierarchy.builder()
                        .connectionQualifiedName(connectionQN)
                        .connectorName(Connection.getConnectorTypeFromQualifiedName(connectionQN)
                                .getValue())
                        .attributeName(denormalizedAttributeName)
                        .attributeValue(qualifiedNamePrefix)
                        .build());
            } else {
                return hierarchy(AssetHierarchy.builder().build());
            }
        }

        /**
         * Limit assets to those with a particular Atlan tag assigned.
         * @param tagName human-readable name of the Atlan tag
         * @return the query builder, limited to assets with the Atlan tag assigned
         */
        public B withTag(String tagName) {
            return tag(TagFilter.of(client, tagName));
        }

        /**
         * Limit assets to those with a particular source tag assigned, with a particular value.
         * @param tagName human-readable name of the Atlan tag mapped to a source tag
         * @param value of the source tag
         * @return the query builder, limited to assets with the Atlan tag assigned with a particular value
         */
        public B withTagValue(String tagName, String value) {
            return tag(TagFilter.of(client, tagName, value));
        }

        /**
         * Limit assets to those without any owners defined (individuals or groups).
         * @return the query builder, limited to assets without any owners assigned
         */
        public B withoutOwners() {
            return owners(Owners.builder().empty(true).build());
        }

        /**
         * Limit assets to those with any of the specified owners.
         * @param usernames (optional) list of usernames to match as owners
         * @param groups (optional) list of internal group names to match as owners
         * @return the query builder, limited to assets with any of the specified owners assigned
         * @throws AtlanException if there are problems confirming any of the provided owners
         */
        public B withOwners(List<String> usernames, List<String> groups) throws AtlanException {
            if ((usernames == null || usernames.isEmpty()) && (groups == null || groups.isEmpty())) {
                return withoutOwners();
            }
            Owners.OwnersBuilder<?, ?> builder = Owners.builder();
            if (usernames != null) {
                for (String username : usernames) {
                    AtlanUser user = client.getUserCache().getByName(username, true);
                    builder.ownerUser(username).ownerId(user.getId()).selectedOwner(username, OwnerDetails.from(user));
                }
            }
            if (groups != null) {
                for (String alias : groups) {
                    AtlanGroup group = client.getGroupCache().getByName(alias, true);
                    builder.ownerGroup(alias).selectedGroup(alias, group);
                }
            }
            return owners(builder.build());
        }

        /**
         * Limit assets to those with any of the specified terms assigned.
         * @param terms minimal details about the terms, which must include at least GUID, qualifiedName, and name
         * @return the query builder, limited to assets with any of the specified terms assigned
         */
        public B withAnyOf(List<GlossaryTerm> terms) {
            return withTerms("equals", terms);
        }

        /**
         * Limit assets to those with all the specified terms assigned.
         * @param terms minimal details about the terms, which must include at least GUID, qualifiedName, and name
         * @return the query builder, limited to assets with all the specified terms assigned
         */
        public B withAll(List<GlossaryTerm> terms) {
            return withTerms("AND", terms);
        }

        /**
         * Limit assets to those with none of the specified terms assigned.
         * @param terms minimal details about the terms, which must include at least GUID, qualifiedName, and name
         * @return the query builder, limited to assets with none of the specified terms assigned
         */
        public B withNoneOf(List<GlossaryTerm> terms) {
            return withTerms("NAND", terms);
        }

        /**
         * Limit assets to those with no terms assigned.
         * @return the query builder, limited to assets without any terms assigned
         */
        public B withoutTerms() {
            return terms(Terms.builder().operator("isNull").build());
        }

        /**
         * Limit assets to those with any terms assigned.
         * @return the query builder, limited to assets with any terms assigned
         */
        public B withAnyTerm() {
            return terms(Terms.builder().operator("isNotNull").build());
        }

        private B withTerms(String operator, List<GlossaryTerm> terms) {
            Terms.TermsBuilder<?, ?> builder = Terms.builder().operator(operator);
            for (GlossaryTerm term : terms) {
                builder.term(TermDetails.builder()
                        .guid(term.getGuid())
                        .qualifiedName(term.getQualifiedName())
                        .attributes(Map.of("name", term.getName()))
                        .build());
            }
            return terms(builder.build());
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
