<#macro all>
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
    public static Glossary findByName(AtlanClient client, String name, Collection<String> attributes) throws AtlanException {
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
    public static Glossary findByName(AtlanClient client, String name, List<AtlanField> attributes) throws AtlanException {
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
        return Glossary.select(client)
            .includeOnResults(Glossary.QUALIFIED_NAME)
            .pageSize(50)
            .stream()
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
    public CategoryHierarchy getHierarchy(AtlanClient client, List<AtlanField> attributes, List<AtlanField> relatedAttributes) throws AtlanException {
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
                                    "Unable to find referenced parent category '{}', skipping it in traversal.",
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
    public static Glossary removeDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Glossary)
                Asset.removeDescription(client, _internal().qualifiedName(qualifiedName).name(name));
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
    public static Glossary removeUserDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
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
        return (Glossary)
                Asset.removeOwners(client, _internal().qualifiedName(qualifiedName).name(name));
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
        return (Glossary) Asset.updateCertificate(client, _internal().name(name), TYPE_NAME, qualifiedName, certificate, message);
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
    public static Glossary removeCertificate(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Glossary)
                Asset.removeCertificate(client, _internal().qualifiedName(qualifiedName).name(name));
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
            AtlanClient client, String qualifiedName, String name, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (Glossary)
                Asset.updateAnnouncement(client, _internal().name(name), TYPE_NAME, qualifiedName, type, title, message);
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
    public static Glossary removeAnnouncement(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Glossary)
                Asset.removeAnnouncement(client, _internal().qualifiedName(qualifiedName).name(name));
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
