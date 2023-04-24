<#macro all>
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
        Query filter = QueryFactory.CompoundQuery.builder()
                .must(QueryFactory.beActive())
                .must(QueryFactory.beOfType(TYPE_NAME))
                .must(QueryFactory.have(KeywordFields.NAME).eq(name))
                .build()
                ._toQuery();
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
        return getHierarchy(null);
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
            builder.attributes(attributes);
        }
        IndexSearchRequest request = builder.build();
        IndexSearchResponse response = request.search();
        if (response != null) {
            Set<String> topCategories = new LinkedHashSet<>();
            Map<String, GlossaryCategory> categoryMap = new HashMap<>();
            List<Asset> results = response.getAssets();
            // First build up a map in-memory of all the categories
            while (results != null) {
                for (Asset one : results) {
                    if (one instanceof GlossaryCategory) {
                        GlossaryCategory category = (GlossaryCategory) one;
                        categoryMap.put(category.getGuid(), category);
                        if (category.getParentCategory() == null) {
                            topCategories.add(category.getGuid());
                        }
                    } else {
                        throw new LogicException(ErrorCode.FOUND_UNEXPECTED_ASSET_TYPE, GlossaryCategory.TYPE_NAME);
                    }
                }
                response = response.getNextPage();
                results = response.getAssets();
            }
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
         * @return the root-level categories of the Glossary
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
</#macro>
