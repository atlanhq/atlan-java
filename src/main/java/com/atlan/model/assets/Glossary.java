/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.LogicException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import java.util.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a glossary in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class Glossary extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossary";

    /** Fixed typeName for Glossaries. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Unused.
     * @see #description
     */
    @Attribute
    String shortDescription;

    /**
     * Unused.
     * @see #description
     */
    @Attribute
    String longDescription;

    /** Unused. */
    @Attribute
    String language;

    /** Unused. */
    @Attribute
    String usage;

    /** Unused. */
    @Attribute
    @Singular
    Map<String, String> additionalAttributes;

    /** Terms within this glossary. */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> terms;

    /** Categories within this glossary. */
    @Attribute
    @Singular
    SortedSet<GlossaryCategory> categories;

    /**
     * Set up the minimal object required to reference a glossary. Only one of the following is required.
     *
     * @param glossaryGuid unique identifier of the glossary for the term
     * @param glossaryQualifiedName unique name of the glossary
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
     * Builds the minimal object necessary for creating a glossary.
     *
     * @param name of the glossary
     * @return the minimal object necessary to create the glossary, as a builder
     */
    public static GlossaryBuilder<?, ?> creator(String name) {
        return Glossary.builder().qualifiedName(name).name(name);
    }

    /**
     * Builds the minimal object necessary to update a glossary.
     *
     * @param guid unique identifier of the glossary
     * @param name of the glossary
     * @return the minimal object necessary to update the glossary, as a builder
     */
    public static GlossaryBuilder<?, ?> updater(String guid, String name) {
        return Glossary.builder().guid(guid).qualifiedName(name).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Glossary, from a potentially
     * more-complete Glossary object.
     *
     * @return the minimal object necessary to update the Glossary, as a builder
     */
    @Override
    protected GlossaryBuilder<?, ?> trimToRequired() {
        return updater(this.getGuid(), this.getName());
    }

    /**
     * Find a glossary by its human-readable name.
     *
     * @param name of the glossary
     * @param attributes an optional collection of attributes to retrieve for the glossary
     * @return the glossary, if found
     * @throws AtlanException on any API problems, or if the glossary does not exist
     */
    public static Glossary findByName(String name, Collection<String> attributes) throws AtlanException {
        Query byType = QueryFactory.withType(TYPE_NAME);
        Query byName = QueryFactory.withExactName(name);
        Query active = QueryFactory.active();
        Query filter = BoolQuery.of(b -> b.filter(byType, byName, active))._toQuery();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> builder = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().from(0).size(2).query(filter).build());
        if (attributes != null && !attributes.isEmpty()) {
            builder.attributes(attributes);
        }
        IndexSearchRequest request = builder.build();
        IndexSearchResponse response = request.search();
        if (response != null) {
            long count = response.getApproximateCount();
            if (count > 1) {
                log.warn("Multiple glossaries found with the name '{}', returning only the first.", name);
            }
            List<Entity> results = response.getEntities();
            if (results != null && !results.isEmpty()) {
                Entity first = results.get(0);
                if (first instanceof Glossary) {
                    return (Glossary) first;
                } else {
                    throw new LogicException(
                            "Found a non-glossary result when searching for only glossaries.",
                            "ATLAN-JAVA-CLIENT-500-090",
                            500);
                }
            }
        }
        throw new NotFoundException(
                "Unable to find a glossary with the name: " + name, "ATLAN-JAVA-CLIENT-404-090", 404, null);
    }

    /**
     * Retrieves a Glossary by its GUID, complete with all of its relationships.
     *
     * @param guid of the Glossary to retrieve
     * @return the requested full Glossary, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Glossary does not exist or the provided GUID is not a Glossary
     */
    public static Glossary retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof Glossary) {
            return (Glossary) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a Glossary.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
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
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof Glossary) {
            return (Glossary) entity;
        } else {
            throw new NotFoundException(
                    "No Glossary found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
        }
    }

    /**
     * Retrieve category hierarchy in this glossary, in a traversable form. You can traverse in either
     * depth-first ({@link CategoryHierarchy#depthFirst()}) or breadth-first ({@link CategoryHierarchy#breadthFirst()})
     * order. Both return an ordered list of {@link GlossaryCategory} objects.
     *
     * @return a traversable category hierarchy
     * @throws AtlanException on any API problems, or if the glossary does not exist
     */
    public CategoryHierarchy getHierarchy() throws AtlanException {
        return getHierarchy(null);
    }

    /**
     * Retrieve category hierarchy in this glossary, in a traversable form. You can traverse in either
     * depth-first ({@link CategoryHierarchy#depthFirst()}) or breadth-first ({@link CategoryHierarchy#breadthFirst()})
     * order. Both return an ordered list of {@link GlossaryCategory} objects.
     * Note: by default, each category will have a minimal set of information (name, GUID, qualifiedName). If you
     * want additional details about each category, specify the attributes you want in the {@code attributes} parameter
     * to this method.
     *
     * @param attributes to retrieve for each category in the hierarchy
     * @return a traversable category hierarchy
     * @throws AtlanException on any API problems, or if the glossary does not exist
     */
    public CategoryHierarchy getHierarchy(List<String> attributes) throws AtlanException {
        if (qualifiedName == null) {
            throw new InvalidRequestException(
                    "Insufficient glossary to query against: no qualifiedName.",
                    "qualifiedName",
                    "ATLAN-JAVA-CLIENT-400-091",
                    400,
                    null);
        }
        Query byType = QueryFactory.withType(GlossaryCategory.TYPE_NAME);
        Query byGlossaryQN = TermQuery.of(t -> t.field("__glossary").value(getQualifiedName()))
                ._toQuery();
        Query active = QueryFactory.active();
        Query filter = BoolQuery.of(b -> b.filter(byType, byGlossaryQN, active))._toQuery();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> builder = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder()
                        .from(0)
                        .size(20)
                        .query(filter)
                        .sortOption(SortOptions.of(s -> s.field(
                                FieldSort.of(f -> f.field("name.keyword").order(SortOrder.Asc)))))
                        .build())
                .attribute("parentCategory");
        if (attributes != null) {
            builder = builder.attributes(attributes);
        }
        IndexSearchRequest request = builder.build();
        IndexSearchResponse response = request.search();
        if (response != null) {
            Set<String> topCategories = new LinkedHashSet<>();
            Map<String, GlossaryCategory> categoryMap = new HashMap<>();
            List<Entity> results = response.getEntities();
            // First build up a map in-memory of all the categories
            while (results != null) {
                for (Entity one : results) {
                    if (one instanceof GlossaryCategory) {
                        GlossaryCategory category = (GlossaryCategory) one;
                        categoryMap.put(category.getGuid(), category);
                        if (category.getParentCategory() == null) {
                            topCategories.add(category.getGuid());
                        }
                    } else {
                        throw new LogicException(
                                "Found a non-category result when searching for only categories.",
                                "ATLAN-JAVA-CLIENT-500-091",
                                500);
                    }
                }
                response = response.getNextPage();
                results = response.getEntities();
            }
            return new CategoryHierarchy(topCategories, categoryMap);
        }
        throw new NotFoundException(
                "Unable to find any categories in glossary: " + getGuid() + "/" + getQualifiedName(),
                "ATLAN-JAVA-CLIENT-404-091",
                404,
                null);
    }

    /**
     * Utility class for traversing the category hierarchy in a glossary.
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
                GlossaryCategory parent = category.getParentCategory();
                if (parent != null) {
                    String parentGuid = parent.getGuid();
                    GlossaryCategory fullParent = map.getOrDefault(parentGuid, stubMap.get(parentGuid));
                    SortedSet<GlossaryCategory> children = new TreeSet<>(fullParent.getChildrenCategories());
                    children.add(category);
                    fullParent.setChildrenCategories(children);
                    map.put(parent.getGuid(), fullParent);
                }
            }
        }

        /**
         * Retrieve only the root-level categories (those with no parents).
         *
         * @return the root-level categories of the glossary
         */
        public List<GlossaryCategory> getRootCategories() {
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
        public List<GlossaryCategory> breadthFirst() {
            List<GlossaryCategory> top = getRootCategories();
            List<GlossaryCategory> all = new ArrayList<>(top);
            bfs(all, top);
            return Collections.unmodifiableList(all);
        }

        /**
         * Retrieve all the categories in the hierarchy in depth-first traversal order.
         *
         * @return all categories in depth-first order
         */
        public List<GlossaryCategory> depthFirst() {
            List<GlossaryCategory> all = new ArrayList<>();
            dfs(all, getRootCategories());
            return Collections.unmodifiableList(all);
        }

        private void bfs(List<GlossaryCategory> list, Collection<GlossaryCategory> toAdd) {
            for (GlossaryCategory node : toAdd) {
                list.addAll(node.getChildrenCategories());
            }
            for (GlossaryCategory node : toAdd) {
                bfs(list, node.getChildrenCategories());
            }
        }

        private void dfs(List<GlossaryCategory> list, Collection<GlossaryCategory> toAdd) {
            for (GlossaryCategory node : toAdd) {
                list.add(node);
                dfs(list, node.getChildrenCategories());
            }
        }
    }

    /**
     * Update the certificate on a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Glossary, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Glossary updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (Glossary) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return (Glossary)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Glossary updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (Glossary) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return (Glossary)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the Glossary
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Glossary.
     *
     * @param qualifiedName of the Glossary
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the Glossary
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
