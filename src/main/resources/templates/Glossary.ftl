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
     * Find a Glossary by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the glossary, if found.
     *
     * @param name of the Glossary
     * @return the Glossary, if found
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public static Glossary findByName(String name) throws AtlanException {
        return findByName(name, null);
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
     * Find a Glossary by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the glossary, if found.
     *
     * @param client connectivity to the Atlan tenant on which to search for the Glossary
     * @param name of the Glossary
     * @return the Glossary, if found
     * @throws AtlanException on any API problems, or if the Glossary does not exist
     */
    public static Glossary findByName(AtlanClient client, String name) throws AtlanException {
        return findByName(client, name, null);
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
    public static Glossary findByName(AtlanClient client, String name, Collection<String> attributes) throws AtlanException {
        List<Glossary> results = new ArrayList<>();
        Glossary.all(client)
                .filter(QueryFactory.where(KeywordFields.NAME).eq(name))
                .attributes(attributes == null ? Collections.emptyList() : attributes)
                .batch(2)
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
        Set<String> topCategories = new LinkedHashSet<>();
        Map<String, GlossaryCategory> categoryMap = new HashMap<>();
        GlossaryCategory.all(client)
                .filter(QueryFactory.where(KeywordFields.GLOSSARY).eq(getQualifiedName()))
                .attribute("parentCategory")
                .attributes(attributes == null ? Collections.emptyList() : attributes)
                .batch(20)
                .sort(QueryFactory.Sort.by(KeywordFields.NAME, SortOrder.Asc))
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
        return new CategoryHierarchy(topCategories, categoryMap);
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
                } else {
                    map.put(category.getGuid(), category);
                }
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
    public static Glossary removeDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Glossary)
                Asset.removeDescription(client, _internal().qualifiedName(qualifiedName).name(name));
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
    public static Glossary removeUserDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Glossary) Asset.removeUserDescription(
                client, _internal().qualifiedName(qualifiedName).name(name));
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
        return (Glossary)
                Asset.removeOwners(client, _internal().qualifiedName(qualifiedName).name(name));
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
            String qualifiedName, String name, CertificateStatus certificate, String message)
            throws AtlanException {
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
        return (Glossary) Asset.updateCertificate(client, _internal().name(name), TYPE_NAME, qualifiedName, certificate, message);
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
    public static Glossary removeCertificate(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Glossary)
                Asset.removeCertificate(client, _internal().qualifiedName(qualifiedName).name(name));
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
            AtlanClient client, String qualifiedName, String name, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (Glossary)
                Asset.updateAnnouncement(client, _internal().name(name), TYPE_NAME, qualifiedName, type, title, message);
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
    public static Glossary removeAnnouncement(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Glossary)
                Asset.removeAnnouncement(client, _internal().qualifiedName(qualifiedName).name(name));
    }
</#macro>
