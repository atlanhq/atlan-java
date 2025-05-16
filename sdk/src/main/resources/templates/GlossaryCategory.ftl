<#macro all>
    /**
     * Builds the minimal object necessary for creating a category.
     *
     * @param name of the category
     * @param glossary in which the category should be created
     * @return the minimal request necessary to create the category, as a builder
     * @throws InvalidRequestException if the glossary provided is without a GUID or qualifiedName
     */
    public static GlossaryCategoryBuilder<?, ?> creator(String name, Glossary glossary) throws InvalidRequestException {
        return creator(name, (String) null).anchor(glossary.trimToReference());
    }

    /**
     * Builds the minimal object necessary for creating a category.
     *
     * @param name of the category
     * @param glossaryId unique identifier of the category's glossary, either is real GUID or qualifiedName
     * @return the minimal request necessary to create the category, as a builder
     */
    public static GlossaryCategoryBuilder<?, ?> creator(String name, String glossaryId) {
        Glossary anchor = StringUtils.isUUID(glossaryId)
                ? Glossary.refByGuid(glossaryId)
                : Glossary.refByQualifiedName(glossaryId);
        return GlossaryCategory._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(name)
                .name(name)
                .anchor(anchor);
    }

    /**
     * Builds the minimal object necessary to update a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique identifier of the GlossaryCategory's glossary
     * @return the minimal request necessary to update the GlossaryCategory, as a builder
     */
    public static GlossaryCategoryBuilder<?, ?> updater(String qualifiedName, String name, String glossaryGuid) {
        // Turns out that updating a category requires the glossary GUID, and will not work
        // with the qualifiedName of the glossary
        return GlossaryCategory._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name)
                .anchor(Glossary.refByGuid(glossaryGuid));
    }

    /**
     * Builds the minimal object necessary to apply an update to a GlossaryCategory, from a potentially
     * more-complete GlossaryCategory object.
     *
     * @return the minimal object necessary to update the GlossaryCategory, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for GlossaryCategory are not found in the initial object
     */
    @Override
    public GlossaryCategoryBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        if (this.getAnchor() == null || !this.getAnchor().isValidReferenceByGuid()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, TYPE_NAME, "anchor.guid");
        }
        return updater(this.getQualifiedName(), this.getName(), this.getAnchor().getGuid());
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the category, if found. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     *
     * @param client connectivity to the Atlan tenant on which to search for the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryName name of the Glossary in which the category exists
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByName(AtlanClient client, String name, String glossaryName)
            throws AtlanException {
        return findByName(client, name, glossaryName, (List<AtlanField>) null);
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryName name of the Glossary in which the category exists
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the GlossaryCategory
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByName(AtlanClient client, String name, String glossaryName, Collection<String> attributes)
            throws AtlanException {
        Glossary glossary = Glossary.findByName(client, glossaryName);
        return findByNameFast(client, name, glossary.getQualifiedName(), attributes);
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryName name of the Glossary in which the category exists
     * @param attributes an optional collection of attributes (checked) to retrieve for the GlossaryCategory
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByName(AtlanClient client, String name, String glossaryName, List<AtlanField> attributes)
            throws AtlanException {
        Glossary glossary = Glossary.findByName(client, glossaryName);
        return findByNameFast(client, name, glossary.getQualifiedName(), attributes);
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the category, if found.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByNameFast(
            AtlanClient client, String name, String glossaryQualifiedName) throws AtlanException {
        return findByNameFast(client, name, glossaryQualifiedName, (List<AtlanField>) null);
    }

    /**
     * Find a GlossaryCategory by its human-readable name.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the GlossaryCategory
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByNameFast(
            AtlanClient client, String name, String glossaryQualifiedName, Collection<String> attributes) throws AtlanException {
        List<GlossaryCategory> results = new ArrayList<>();
        GlossaryCategory.select(client)
                .where(GlossaryCategory.NAME.eq(name))
                .where(GlossaryCategory.ANCHOR.eq(glossaryQualifiedName))
                .includeOnResults(GlossaryCategory.ANCHOR)
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .includeOnRelations(Asset.NAME)
                .stream()
                .filter(a -> a instanceof GlossaryCategory)
                .forEach(c -> results.add((GlossaryCategory) c));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        }
        return results;
    }

    /**
     * Find a GlossaryCategory by its human-readable name.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @param attributes an optional collection of attributes (checked) to retrieve for the GlossaryCategory
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByNameFast(
            AtlanClient client, String name, String glossaryQualifiedName, List<AtlanField> attributes) throws AtlanException {
        List<GlossaryCategory> results = new ArrayList<>();
        GlossaryCategory.select(client)
                .where(GlossaryCategory.NAME.eq(name))
                .where(GlossaryCategory.ANCHOR.eq(glossaryQualifiedName))
                .includeOnResults(GlossaryCategory.ANCHOR)
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .includeOnRelations(Asset.NAME)
                .stream()
                .filter(a -> a instanceof GlossaryCategory)
                .forEach(c -> results.add((GlossaryCategory) c));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        }
        return results;
    }

    /**
     * Remove the system description from a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryCategory's description
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeDescription(AtlanClient client, String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryCategory) Asset.removeDescription(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Remove the user's description from a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryCategory's description
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeUserDescription(AtlanClient client, String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryCategory) Asset.removeUserDescription(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Remove the owners from a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryCategory's owners
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeOwners(AtlanClient client, String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryCategory) Asset.removeOwners(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the certificate on a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant on which to update the GlossaryCategory's certificate
     * @param qualifiedName of the GlossaryCategory
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GlossaryCategory, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory updateCertificate(
            AtlanClient client, String qualifiedName, String name, String glossaryGuid, CertificateStatus certificate, String message)
            throws AtlanException {
        return (GlossaryCategory)
                Asset.updateCertificate(client, updater(qualifiedName, name, glossaryGuid), certificate, message);
    }

    /**
     * Remove the certificate from a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryCategory's certificate
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeCertificate(AtlanClient client, String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryCategory) Asset.removeCertificate(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the announcement on a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant on which to update the GlossaryCategory's announcement
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the updated GlossaryCategory, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory updateAnnouncement(
            AtlanClient client,
            String qualifiedName,
            String name,
            String glossaryGuid,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        return (GlossaryCategory)
                Asset.updateAnnouncement(client, updater(qualifiedName, name, glossaryGuid), type, title, message);
    }

    /**
     * Remove the announcement from a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryCategory's announcement
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeAnnouncement(AtlanClient client, String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryCategory) Asset.removeAnnouncement(client, updater(qualifiedName, name, glossaryGuid));
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
