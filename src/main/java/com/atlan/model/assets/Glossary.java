/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.LogicException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.KeywordFields;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a glossary in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class Glossary extends Asset implements IGlossary, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossary";

    /** Fixed typeName for Glossarys. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    Map<String, String> additionalAttributes;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryCategory> categories;

    /** TBC */
    @Attribute
    String language;

    /** TBC */
    @Attribute
    String longDescription;

    /** TBC */
    @Attribute
    String shortDescription;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryTerm> terms;

    /** TBC */
    @Attribute
    String usage;

    /**
     * Start an asset filter that will return all Glossary assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Glossary assets will be included.
     *
     * @return an asset filter that includes all Glossary assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(false);
    }

    /**
     * Start an asset filter that will return all Glossary assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) Glossarys will be included
     * @return an asset filter that includes all Glossary assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder = AssetFilter.builder().filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a Glossary by GUID.
     *
     * @param guid the GUID of the Glossary to reference
     * @return reference to a Glossary that can be used for defining a relationship to a Glossary
     */
    public static Glossary refByGuid(String guid) {
        return Glossary.builder().guid(guid).build();
    }

    /**
     * Reference to a Glossary by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Glossary to reference
     * @return reference to a Glossary that can be used for defining a relationship to a Glossary
     */
    public static Glossary refByQualifiedName(String qualifiedName) {
        return Glossary.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Glossary by its GUID, complete with all of its relationships.
     *
     * @param guid of the Glossary to retrieve
     * @return the requested full Glossary, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Glossary does not exist or the provided GUID is not a Glossary
     */
    public static Glossary retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a Glossary by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the Glossary to retrieve
     * @return the requested full Glossary, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Glossary does not exist or the provided GUID is not a Glossary
     */
    public static Glossary retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof Glossary) {
            return (Glossary) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "Glossary");
        }
    }

    /**
     * Retrieves a Glossary by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Glossary to retrieve
     * @return the requested full Glossary, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Glossary does not exist
     */
    public static Glossary retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a Glossary by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the Glossary to retrieve
     * @return the requested full Glossary, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Glossary does not exist
     */
    public static Glossary retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof Glossary) {
            return (Glossary) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "Glossary");
        }
    }

    /**
     * Restore the archived (soft-deleted) Glossary to active.
     *
     * @param qualifiedName for the Glossary
     * @return true if the Glossary is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) Glossary to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the Glossary
     * @return true if the Glossary is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Set up the minimal object required to reference a Glossary. Only one of the following is required.
     *
     * @param glossaryGuid unique identifier of the Glossary for the term
     * @param glossaryQualifiedName unique name of the Glossary
     * @return a builder that can be further extended with other metadata
     */
    static Glossary anchorLink(String glossaryGuid, String glossaryQualifiedName) {
        Glossary anchor = null;
        if (glossaryGuid == null && glossaryQualifiedName == null) {
            return null;
        } else if (glossaryGuid != null) {
            anchor = Glossary.refByGuid(glossaryGuid);
        } else {
            anchor = Glossary.refByQualifiedName(glossaryQualifiedName);
        }
        return anchor;
    }

    /**
     * Builds the minimal object necessary for creating a Glossary.
     *
     * @param name of the Glossary
     * @return the minimal object necessary to create the Glossary, as a builder
     */
    public static GlossaryBuilder<?, ?> creator(String name) {
        return Glossary.builder().qualifiedName(name).name(name);
    }

    /**
     * Builds the minimal object necessary to update a Glossary.
     *
     * @param guid unique identifier of the Glossary
     * @param name of the Glossary
     * @return the minimal object necessary to update the Glossary, as a builder
     */
    public static GlossaryBuilder<?, ?> updater(String guid, String name) {
        return Glossary.builder().guid(guid).qualifiedName(name).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Glossary, from a potentially
     * more-complete Glossary object.
     *
     * @return the minimal object necessary to update the Glossary, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Glossary are not found in the initial object
     */
    @Override
    public GlossaryBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getGuid() == null || this.getGuid().length() == 0) {
            missing.add("guid");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Glossary", String.join(",", missing));
        }
        return updater(this.getGuid(), this.getName());
    }

    /**
     * Find a Glossary by its human-readable name.
     *
     * @param name of the Glossary
     * @param attributes an optional collection of attributes to retrieve for the Glossary
     * @return the Glossary, if found
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public static Glossary findByName(String name, Collection<String> attributes) throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a Glossary by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant on which to search for the Glossary
     * @param name of the Glossary
     * @param attributes an optional collection of attributes to retrieve for the Glossary
     * @return the Glossary, if found
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public static Glossary findByName(AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        Query filter = QueryFactory.CompoundQuery.builder()
                .must(QueryFactory.beActive())
                .must(QueryFactory.beOfType(TYPE_NAME))
                .must(QueryFactory.have(KeywordFields.NAME).eq(name))
                .build()
                ._toQuery();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> builder = IndexSearchRequest.builder(
                IndexSearchDSL.builder(filter).size(2).build());
        if (attributes != null && !attributes.isEmpty()) {
            builder.attributes(attributes);
        }
        IndexSearchRequest request = builder.build();
        IndexSearchResponse response = request.search(client);
        if (response != null) {
            long count = response.getApproximateCount();
            if (count > 1) {
                log.warn("Multiple glossaries found with the name '{}', returning only the first.", name);
            }
            List<Asset> results = response.getAssets();
            if (results != null && !results.isEmpty()) {
                Asset first = results.get(0);
                if (first instanceof Glossary) {
                    return (Glossary) first;
                } else {
                    throw new LogicException(ErrorCode.FOUND_UNEXPECTED_ASSET_TYPE);
                }
            }
        }
        throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
    }

    /**
     * Retrieve category hierarchy in this Glossary, in a traversable form. You can traverse in either
     * depth-first ({@link CategoryHierarchy#depthFirst()}) or breadth-first ({@link CategoryHierarchy#breadthFirst()})
     * order. Both return an ordered list of {@link GlossaryCategory} objects.
     *
     * @return a traversable category hierarchy
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public CategoryHierarchy getHierarchy() throws AtlanException {
        return getHierarchy(Atlan.getDefaultClient());
    }

    /**
     * Retrieve category hierarchy in this Glossary, in a traversable form. You can traverse in either
     * depth-first ({@link CategoryHierarchy#depthFirst()}) or breadth-first ({@link CategoryHierarchy#breadthFirst()})
     * order. Both return an ordered list of {@link GlossaryCategory} objects.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the hierarchy
     * @return a traversable category hierarchy
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public CategoryHierarchy getHierarchy(AtlanClient client) throws AtlanException {
        return getHierarchy(client, null);
    }

    /**
     * Retrieve category hierarchy in this Glossary, in a traversable form. You can traverse in either
     * depth-first ({@link CategoryHierarchy#depthFirst()}) or breadth-first ({@link CategoryHierarchy#breadthFirst()})
     * order. Both return an ordered list of {@link GlossaryCategory} objects.
     * Note: by default, each category will have a minimal set of information (name, GUID, qualifiedName). If you
     * want additional details about each category, specify the attributes you want in the {@code attributes} parameter
     * to this method.
     *
     * @param attributes to retrieve for each category in the hierarchy
     * @return a traversable category hierarchy
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public CategoryHierarchy getHierarchy(List<String> attributes) throws AtlanException {
        return getHierarchy(Atlan.getDefaultClient(), attributes);
    }

    /**
     * Retrieve category hierarchy in this Glossary, in a traversable form. You can traverse in either
     * depth-first ({@link CategoryHierarchy#depthFirst()}) or breadth-first ({@link CategoryHierarchy#breadthFirst()})
     * order. Both return an ordered list of {@link GlossaryCategory} objects.
     * Note: by default, each category will have a minimal set of information (name, GUID, qualifiedName). If you
     * want additional details about each category, specify the attributes you want in the {@code attributes} parameter
     * to this method.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the hierarchy
     * @param attributes to retrieve for each category in the hierarchy
     * @return a traversable category hierarchy
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public CategoryHierarchy getHierarchy(AtlanClient client, List<String> attributes) throws AtlanException {
        if (qualifiedName == null) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_QUERY_PARAM, Glossary.TYPE_NAME, "qualifiedName");
        }
        Query filter = QueryFactory.CompoundQuery.builder()
                .must(QueryFactory.beActive())
                .must(QueryFactory.beOfType(GlossaryCategory.TYPE_NAME))
                .must(QueryFactory.have(KeywordFields.GLOSSARY).eq(getQualifiedName()))
                .build()
                ._toQuery();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> builder = IndexSearchRequest.builder(
                        IndexSearchDSL.builder(filter)
                                .size(20)
                                .sortOption(QueryFactory.Sort.by(KeywordFields.NAME, SortOrder.Asc))
                                .build())
                .attribute("parentCategory");
        if (attributes != null) {
            builder.attributes(attributes);
        }
        IndexSearchRequest request = builder.build();
        IndexSearchResponse response = request.search(client);
        if (response != null) {
            Set<String> topCategories = new LinkedHashSet<>();
            Map<String, GlossaryCategory> categoryMap = new HashMap<>();
            // First build up a map in-memory of all the categories
            response.stream().filter(c -> (c instanceof GlossaryCategory)).forEach(c -> {
                GlossaryCategory category = (GlossaryCategory) c;
                categoryMap.put(category.getGuid(), category);
                if (category.getParentCategory() == null) {
                    topCategories.add(category.getGuid());
                }
            });
            // Then pass that over to a CategoryHierarchy to order
            return new CategoryHierarchy(topCategories, categoryMap);
        }
        throw new NotFoundException(ErrorCode.NO_CATEGORIES, getGuid(), getQualifiedName());
    }

    /**
     * Utility class for traversing the category hierarchy in a Glossary.
     */
    public static class CategoryHierarchy {

        private final Set<String> topLevel;
        private final List<GlossaryCategory> rootCategories;
        private final Map<String, GlossaryCategory> map;

        private CategoryHierarchy(Set<String> topLevel, Map<String, GlossaryCategory> stubMap) {
            this.topLevel = topLevel;
            this.rootCategories = new ArrayList<>();
            this.map = new LinkedHashMap<>();
            buildMaps(stubMap);
        }

        private void buildMaps(Map<String, GlossaryCategory> stubMap) {
            for (Map.Entry<String, GlossaryCategory> entry : stubMap.entrySet()) {
                GlossaryCategory category = entry.getValue();
                IGlossaryCategory parent = category.getParentCategory();
                if (parent != null) {
                    String parentGuid = parent.getGuid();
                    GlossaryCategory fullParent = map.getOrDefault(parentGuid, stubMap.get(parentGuid));
                    SortedSet<IGlossaryCategory> children = new TreeSet<>(fullParent.getChildrenCategories());
                    children.add(category);
                    fullParent.setChildrenCategories(children);
                    map.put(parent.getGuid(), fullParent);
                }
            }
        }

        /**
         * Retrieve only the root-level categories (those with no parents).
         *
         * @return the root-level categories of the Glossary
         */
        public List<IGlossaryCategory> getRootCategories() {
            if (rootCategories.isEmpty()) {
                for (String top : topLevel) {
                    rootCategories.add(map.get(top));
                }
            }
            return Collections.unmodifiableList(rootCategories);
        }

        /**
         * Retrieve all the categories in the hierarchy in breadth-first traversal order.
         *
         * @return all categories in breadth-first order
         */
        public List<IGlossaryCategory> breadthFirst() {
            List<IGlossaryCategory> top = getRootCategories();
            List<IGlossaryCategory> all = new ArrayList<>(top);
            bfs(all, top);
            return Collections.unmodifiableList(all);
        }

        /**
         * Retrieve all the categories in the hierarchy in depth-first traversal order.
         *
         * @return all categories in depth-first order
         */
        public List<IGlossaryCategory> depthFirst() {
            List<IGlossaryCategory> all = new ArrayList<>();
            dfs(all, getRootCategories());
            return Collections.unmodifiableList(all);
        }

        private void bfs(List<IGlossaryCategory> list, Collection<IGlossaryCategory> toAdd) {
            for (IGlossaryCategory node : toAdd) {
                list.addAll(node.getChildrenCategories());
            }
            for (IGlossaryCategory node : toAdd) {
                bfs(list, node.getChildrenCategories());
            }
        }

        private void dfs(List<IGlossaryCategory> list, Collection<IGlossaryCategory> toAdd) {
            for (IGlossaryCategory node : toAdd) {
                list.add(node);
                dfs(list, node.getChildrenCategories());
            }
        }
    }

    /**
     * Remove the system description from a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @return the updated Glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a Glossary.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Glossary's description
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @return the updated Glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Glossary) Asset.removeDescription(
                client, builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @return the updated Glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a Glossary.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Glossary's description
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @return the updated Glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Glossary) Asset.removeUserDescription(
                client, builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @return the updated Glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a Glossary.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Glossary's owners
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @return the updated Glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Glossary) Asset.removeOwners(
                client, builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Glossary, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Glossary updateCertificate(
            String qualifiedName, String name, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, name, certificate, message);
    }

    /**
     * Update the certificate on a Glossary.
     *
     * @param client connectivity to the Atlan client on which to update the Glossary's certificate
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Glossary, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Glossary updateCertificate(
            AtlanClient client, String qualifiedName, String name, CertificateStatus certificate, String message)
            throws AtlanException {
        return (Glossary)
                Asset.updateCertificate(client, builder().name(name), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @return the updated Glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a Glossary.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Glossary's certificate
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @return the updated Glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Glossary) Asset.removeCertificate(
                client, builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Glossary updateAnnouncement(
            String qualifiedName, String name, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, name, type, title, message);
    }

    /**
     * Update the announcement on a Glossary.
     *
     * @param client connectivity to the Atlan tenant on which to update the Glossary's announcement
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Glossary updateAnnouncement(
            AtlanClient client,
            String qualifiedName,
            String name,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        return (Glossary)
                Asset.updateAnnouncement(client, builder().name(name), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @return the updated Glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a Glossary.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Glossary's announcement
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @return the updated Glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Glossary) Asset.removeAnnouncement(
                client, builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add Atlan tags to a Glossary, without replacing existing Atlan tags linked to the Glossary.
     * Note: this operation must make two API calls — one to retrieve the Glossary's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Glossary
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Glossary
     */
    public static Glossary appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Glossary, without replacing existing Atlan tags linked to the Glossary.
     * Note: this operation must make two API calls — one to retrieve the Glossary's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Glossary
     * @param qualifiedName of the Glossary
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Glossary
     */
    public static Glossary appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Glossary) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Glossary, without replacing existing Atlan tags linked to the Glossary.
     * Note: this operation must make two API calls — one to retrieve the Glossary's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Glossary
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Glossary
     */
    public static Glossary appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Glossary, without replacing existing Atlan tags linked to the Glossary.
     * Note: this operation must make two API calls — one to retrieve the Glossary's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Glossary
     * @param qualifiedName of the Glossary
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Glossary
     */
    public static Glossary appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Glossary) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Glossary
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Glossary.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the Glossary
     * @param qualifiedName of the Glossary
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Glossary
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Glossary
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Glossary.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the Glossary
     * @param qualifiedName of the Glossary
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Glossary
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Glossary
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a Glossary.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Glossary
     * @param qualifiedName of the Glossary
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Glossary
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
