/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlasGlossaryType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a glossary in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
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
    AtlasGlossaryType glossaryType;

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
     * Builds the minimal object necessary to create a relationship to a Glossary, from a potentially
     * more-complete Glossary object.
     *
     * @return the minimal object necessary to relate to the Glossary
     * @throws InvalidRequestException if any of the minimal set of required properties for a Glossary relationship are not found in the initial object
     */
    @Override
    public Glossary trimToReference() throws InvalidRequestException {
        if (this.getGuid() != null && !this.getGuid().isEmpty()) {
            return refByGuid(this.getGuid());
        }
        if (this.getQualifiedName() != null && !this.getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getQualifiedName());
        }
        if (this.getUniqueAttributes() != null
                && this.getUniqueAttributes().getQualifiedName() != null
                && !this.getUniqueAttributes().getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getUniqueAttributes().getQualifiedName());
        }
        throw new InvalidRequestException(
                ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, TYPE_NAME, "guid, qualifiedName");
    }

    /**
     * Start a fluent search that will return all Glossary assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Glossary assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all Glossary assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all Glossary assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Glossarys will be included
     * @return a fluent search that includes all Glossary assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(Asset.TYPE_NAME.eq(TYPE_NAME));
        if (!includeArchived) {
            builder.active();
        }
        return builder;
    }

    /**
     * Reference to a Glossary by GUID. Use this to create a relationship to this Glossary,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the Glossary to reference
     * @return reference to a Glossary that can be used for defining a relationship to a Glossary
     */
    public static Glossary refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Glossary by GUID. Use this to create a relationship to this Glossary,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the Glossary to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Glossary that can be used for defining a relationship to a Glossary
     */
    public static Glossary refByGuid(String guid, Reference.SaveSemantic semantic) {
        return Glossary._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a Glossary by qualifiedName. Use this to create a relationship to this Glossary,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the Glossary to reference
     * @return reference to a Glossary that can be used for defining a relationship to a Glossary
     */
    public static Glossary refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Glossary by qualifiedName. Use this to create a relationship to this Glossary,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the Glossary to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Glossary that can be used for defining a relationship to a Glossary
     */
    public static Glossary refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return Glossary._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a Glossary by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Glossary to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Glossary, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Glossary does not exist or the provided GUID is not a Glossary
     */
    @JsonIgnore
    public static Glossary get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a Glossary by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Glossary to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Glossary, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Glossary does not exist or the provided GUID is not a Glossary
     */
    @JsonIgnore
    public static Glossary get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Glossary) {
                return (Glossary) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof Glossary) {
                return (Glossary) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a Glossary by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Glossary to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Glossary, including any relationships
     * @return the requested Glossary, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Glossary does not exist or the provided GUID is not a Glossary
     */
    @JsonIgnore
    public static Glossary get(AtlanClient client, String id, Collection<AtlanField> attributes) throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a Glossary by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Glossary to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Glossary, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the Glossary
     * @return the requested Glossary, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Glossary does not exist or the provided GUID is not a Glossary
     */
    @JsonIgnore
    public static Glossary get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = Glossary.select(client)
                    .where(Glossary.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof Glossary) {
                return (Glossary) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = Glossary.select(client)
                    .where(Glossary.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof Glossary) {
                return (Glossary) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
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
     * Builds the minimal object necessary for creating a Glossary.
     *
     * @param name of the Glossary
     * @return the minimal object necessary to create the Glossary, as a builder
     */
    public static GlossaryBuilder<?, ?> creator(String name) {
        return Glossary._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(name)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to update a Glossary.
     *
     * @param guid unique identifier of the Glossary
     * @param name of the Glossary
     * @return the minimal object necessary to update the Glossary, as a builder
     */
    public static GlossaryBuilder<?, ?> updater(String guid, String name) {
        return Glossary._internal().guid(guid).qualifiedName(name).name(name);
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
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getGuid(), this.getName());
    }

    /**
     * Find a Glossary by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the glossary, if found.
     *
     * @param client connectivity to the Atlan tenant on which to search for the Glossary
     * @param name of the Glossary
     * @return the Glossary, if found
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public static Glossary findByName(AtlanClient client, String name) throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a Glossary by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant on which to search for the Glossary
     * @param name of the Glossary
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the Glossary
     * @return the Glossary, if found
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public static Glossary findByName(AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<Glossary> results = new ArrayList<>();
        Glossary.select(client)
                .where(Glossary.NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .includeOnRelations(Asset.NAME)
                .pageSize(2)
                .stream()
                .limit(2)
                .filter(a -> a instanceof Glossary)
                .forEach(g -> results.add((Glossary) g));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        } else if (results.size() > 1) {
            log.warn("Multiple glossaries found with the name '{}', returning only the first.", name);
        }
        return results.get(0);
    }

    /**
     * Find a Glossary by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant on which to search for the Glossary
     * @param name of the Glossary
     * @param attributes an optional collection of attributes (checked) to retrieve for the Glossary
     * @return the Glossary, if found
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public static Glossary findByName(AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<Glossary> results = new ArrayList<>();
        Glossary.select(client)
                .where(Glossary.NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .includeOnRelations(Asset.NAME)
                .pageSize(2)
                .stream()
                .limit(2)
                .filter(a -> a instanceof Glossary)
                .forEach(g -> results.add((Glossary) g));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        } else if (results.size() > 1) {
            log.warn("Multiple glossaries found with the name '{}', returning only the first.", name);
        }
        return results.get(0);
    }

    /**
     * Retrieve the qualifiedNames of all glossaries that exist in Atlan.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the qualifiedNames
     * @return list of all glossary qualifiedNames
     * @throws AtlanException on any API problems
     */
    public static List<String> getAllQualifiedNames(AtlanClient client) throws AtlanException {
        return Glossary.select(client).includeOnResults(Glossary.QUALIFIED_NAME).pageSize(50).stream()
                .map(Asset::getQualifiedName)
                .collect(Collectors.toList());
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
        return getHierarchy(client, (List<AtlanField>) null);
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
     * @param attributes (unchecked) to retrieve for each category in the hierarchy
     * @return a traversable category hierarchy
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public CategoryHierarchy getHierarchy(AtlanClient client, Collection<String> attributes) throws AtlanException {
        return _getHierarchy(client, attributes, null);
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
     * @param attributes (checked) to retrieve for each category in the hierarchy
     * @return a traversable category hierarchy
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public CategoryHierarchy getHierarchy(AtlanClient client, List<AtlanField> attributes) throws AtlanException {
        return getHierarchy(client, attributes, null);
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
     * @param attributes (checked) to retrieve for each category in the hierarchy
     * @param relatedAttributes (checked) to retrieve for each relationship attribute retrieved for each category in the hierarchy
     * @return a traversable category hierarchy
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public CategoryHierarchy getHierarchy(
            AtlanClient client, List<AtlanField> attributes, List<AtlanField> relatedAttributes) throws AtlanException {
        List<String> stringAttributes = new ArrayList<>();
        if (attributes != null) {
            stringAttributes =
                    attributes.stream().map(AtlanField::getAtlanFieldName).collect(Collectors.toList());
        }
        return _getHierarchy(client, stringAttributes, relatedAttributes);
    }

    private CategoryHierarchy _getHierarchy(
            AtlanClient client, Collection<String> attributes, List<AtlanField> relatedAttributes)
            throws AtlanException {
        if (qualifiedName == null) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_QUERY_PARAM, Glossary.TYPE_NAME, "qualifiedName");
        }
        Set<String> topCategories = new LinkedHashSet<>();
        Map<String, GlossaryCategory> categoryMap = new HashMap<>();

        GlossaryCategory.select(client)
                .where(GlossaryCategory.ANCHOR.eq(getQualifiedName()))
                .includeOnResults(GlossaryCategory.PARENT_CATEGORY)
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .includesOnRelations(relatedAttributes == null ? Collections.emptyList() : relatedAttributes)
                .sort(GlossaryCategory.NAME.order(SortOrder.Asc))
                .stream()
                .filter(a -> a instanceof GlossaryCategory)
                .forEach(c -> {
                    GlossaryCategory category = (GlossaryCategory) c;
                    categoryMap.put(category.getGuid(), category);
                    if (category.getParentCategory() == null) {
                        topCategories.add(category.getGuid());
                    }
                });
        if (topCategories.isEmpty()) {
            throw new NotFoundException(ErrorCode.NO_CATEGORIES, getGuid(), getQualifiedName());
        }
        return new CategoryHierarchy(client, topCategories, categoryMap, attributes, relatedAttributes);
    }

    /**
     * Utility class for traversing the category hierarchy in a Glossary.
     */
    public static class CategoryHierarchy {

        private final transient AtlanClient client;
        private final Collection<String> attributes;
        private final List<AtlanField> relatedAttributes;
        private final Set<String> topLevel;
        private final List<GlossaryCategory> rootCategories;
        private final Map<String, GlossaryCategory> map;

        private CategoryHierarchy(
                AtlanClient client,
                Set<String> topLevel,
                Map<String, GlossaryCategory> stubMap,
                Collection<String> attributes,
                List<AtlanField> relatedAttributes) {
            this.client = client;
            this.attributes = attributes;
            this.relatedAttributes = relatedAttributes;
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
                    if (fullParent != null) {
                        SortedSet<IGlossaryCategory> children = new TreeSet<>(fullParent.getChildrenCategories());
                        children.add(category);
                        fullParent.setChildrenCategories(children);
                        map.put(parent.getGuid(), fullParent);
                    } else {
                        log.debug(
                                "Unable to find referenced parent category '{}', could be in a different glossary.",
                                parentGuid);
                        try {
                            GlossaryCategory.select(client)
                                    .where(GlossaryCategory.GUID.eq(parentGuid))
                                    .includeOnResults(GlossaryCategory.PARENT_CATEGORY)
                                    ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                                    .includesOnRelations(
                                            relatedAttributes == null ? Collections.emptyList() : relatedAttributes)
                                    .sort(GlossaryCategory.NAME.order(SortOrder.Asc))
                                    .stream()
                                    .filter(a -> a instanceof GlossaryCategory)
                                    .forEach(c -> {
                                        GlossaryCategory fetched = (GlossaryCategory) c;
                                        map.put(fetched.getGuid(), fetched);
                                        if (fetched.getParentCategory() == null) {
                                            topLevel.add(fetched.getGuid());
                                        }
                                    });
                        } catch (AtlanException e) {
                            log.warn(
                                    "Unable to find reference parent category '{}', skipping it in traversal.",
                                    parentGuid);
                            log.debug("Full details: ", e);
                        }
                    }
                }
                map.put(category.getGuid(), category);
            }
        }

        /**
         * Retrieve a specific category from anywhere in the hierarchy by its unique identifier (GUID).
         *
         * @param guid of the category to retrieve
         * @return the requested category
         */
        public GlossaryCategory getCategory(String guid) {
            return map.get(guid);
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
     * @param client connectivity to the Atlan tenant from which to remove the Glossary's description
     * @param qualifiedName of the Glossary
     * @param name of the Glossary
     * @return the updated Glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Glossary) Asset.removeDescription(
                client, _internal().qualifiedName(qualifiedName).name(name));
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
                client, _internal().qualifiedName(qualifiedName).name(name));
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
                client, _internal().qualifiedName(qualifiedName).name(name));
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
                Asset.updateCertificate(client, _internal().name(name), TYPE_NAME, qualifiedName, certificate, message);
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
                client, _internal().qualifiedName(qualifiedName).name(name));
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
        return (Glossary) Asset.updateAnnouncement(
                client, _internal().name(name), TYPE_NAME, qualifiedName, type, title, message);
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
                client, _internal().qualifiedName(qualifiedName).name(name));
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
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static Glossary appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Glossary) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
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
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
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
     * Remove an Atlan tag from a Glossary.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Glossary
     * @param qualifiedName of the Glossary
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Glossary
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
