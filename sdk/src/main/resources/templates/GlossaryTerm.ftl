<#macro all>
    /**
     * Builds the minimal object necessary for creating a term.
     *
     * @param name of the term
     * @param glossary in which the term should be created
     * @return the minimal request necessary to create the term, as a builder
     * @throws InvalidRequestException if the glossary provided is without a GUID or qualifiedName
     */
    public static GlossaryTermBuilder<?, ?> creator(String name, Glossary glossary) throws InvalidRequestException {
        return creator(name, (String) null).anchor(glossary.trimToReference());
    }

    /**
     * Builds the minimal object necessary for creating a term.
     *
     * @param name of the term
     * @param glossaryId unique identifier of the term's glossary, either is real GUID or qualifiedName
     * @return the minimal request necessary to create the term, as a builder
     */
    public static GlossaryTermBuilder<?, ?> creator(String name, String glossaryId) {
        Glossary anchor = StringUtils.isUUID(glossaryId)
                ? Glossary.refByGuid(glossaryId)
                : Glossary.refByQualifiedName(glossaryId);
        return GlossaryTerm._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(name)
                .name(name)
                .anchor(anchor);
    }

    /**
     * Builds the minimal object necessary to update a GlossaryTerm.
     *
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique identifier of the GlossaryTerm's glossary
     * @return the minimal request necessary to update the GlossaryTerm, as a builder
     */
    public static GlossaryTermBuilder<?, ?> updater(String qualifiedName, String name, String glossaryGuid) {
        // Turns out that updating a term requires the glossary GUID, and will not work
        // with the qualifiedName of the glossary
        return GlossaryTerm._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name)
                .anchor(Glossary.refByGuid(glossaryGuid));
    }

    /**
     * Builds the minimal object necessary to apply an update to a GlossaryTerm, from a potentially
     * more-complete GlossaryTerm object.
     *
     * @return the minimal object necessary to update the GlossaryTerm, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for GlossaryTerm are not found in the initial object
     */
    @Override
    public GlossaryTermBuilder<?, ?> trimToRequired() throws InvalidRequestException {
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
     * Find a GlossaryTerm by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the term, if found. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     *
     * @param client connectivity to the Atlan tenant in which to search for the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryName human-readable name of the Glossary in which the category exists
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByName(AtlanClient client, String name, String glossaryName) throws AtlanException {
        return findByName(client, name, glossaryName, (List<AtlanField>) null);
    }

    /**
     * Find a GlossaryTerm by its human-readable name. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     *
     * @param client connectivity to the Atlan tenant in which to search for the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryName human-readable name of the Glossary in which the category exists
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the GlossaryTerm
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByName(AtlanClient client, String name, String glossaryName, Collection<String> attributes)
            throws AtlanException {
        Glossary glossary = Glossary.findByName(client, glossaryName);
        return findByNameFast(client, name, glossary.getQualifiedName(), attributes);
    }

    /**
     * Find a GlossaryTerm by its human-readable name. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     *
     * @param client connectivity to the Atlan tenant in which to search for the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryName human-readable name of the Glossary in which the category exists
     * @param attributes an optional collection of attributes (checked) to retrieve for the GlossaryTerm
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByName(AtlanClient client, String name, String glossaryName, List<AtlanField> attributes)
            throws AtlanException {
        Glossary glossary = Glossary.findByName(client, glossaryName);
        return findByNameFast(client, name, glossary.getQualifiedName(), attributes);
    }

    /**
     * Find a GlossaryTerm by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the term, if found.
     *
     * @param client connectivity to the Atlan tenant in which to search for the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByNameFast(AtlanClient client, String name, String glossaryQualifiedName) throws AtlanException {
        return findByNameFast(client, name, glossaryQualifiedName, (List<AtlanField>) null);
    }

    /**
     * Find a GlossaryTerm by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the GlossaryTerm
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByNameFast(AtlanClient client, String name, String glossaryQualifiedName, Collection<String> attributes)
            throws AtlanException {
        List<GlossaryTerm> results = new ArrayList<>();
        GlossaryTerm.select(client)
                .where(GlossaryTerm.NAME.eq(name))
                .where(GlossaryTerm.ANCHOR.eq(glossaryQualifiedName))
                .includeOnResults(GlossaryTerm.ANCHOR)
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .includeOnRelations(Asset.NAME)
                .pageSize(2)
                .stream()
                .limit(2)
                .filter(a -> a instanceof GlossaryTerm)
                .forEach(t -> results.add((GlossaryTerm) t));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        } else if (results.size() > 1) {
            log.warn(
                    "Multiple terms found with the name '{}' in glossary '{}', returning only the first.",
                    name,
                    glossaryQualifiedName);
        }
        return results.get(0);
    }

    /**
     * Find a GlossaryTerm by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @param attributes an optional collection of attributes (checked) to retrieve for the GlossaryTerm
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByNameFast(AtlanClient client, String name, String glossaryQualifiedName, List<AtlanField> attributes)
            throws AtlanException {
        List<GlossaryTerm> results = new ArrayList<>();
        GlossaryTerm.select(client)
                .where(GlossaryTerm.NAME.eq(name))
                .where(GlossaryTerm.ANCHOR.eq(glossaryQualifiedName))
                .includeOnResults(GlossaryTerm.ANCHOR)
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .includeOnRelations(Asset.NAME)
                .pageSize(2)
                .stream()
                .limit(2)
                .filter(a -> a instanceof GlossaryTerm)
                .forEach(t -> results.add((GlossaryTerm) t));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        } else if (results.size() > 1) {
            log.warn(
                    "Multiple terms found with the name '{}' in glossary '{}', returning only the first.",
                    name,
                    glossaryQualifiedName);
        }
        return results.get(0);
    }

    /**
     * Remove the system description from a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryTerm's description
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeDescription(AtlanClient client, String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryTerm) Asset.removeDescription(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Remove the user's description from a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryTerm's description
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeUserDescription(AtlanClient client, String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryTerm) Asset.removeUserDescription(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Remove the owners from a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryTerm's owners
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeOwners(AtlanClient client, String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryTerm) Asset.removeOwners(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the certificate on a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant on which to update the GlossaryTerm's certificate
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GlossaryTerm, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm updateCertificate(
            AtlanClient client, String qualifiedName, String name, String glossaryGuid, CertificateStatus certificate, String message)
            throws AtlanException {
        return (GlossaryTerm) Asset.updateCertificate(client, updater(qualifiedName, name, glossaryGuid), certificate, message);
    }

    /**
     * Remove the certificate from a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryTerm's certificate
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeCertificate(AtlanClient client, String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryTerm) Asset.removeCertificate(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the announcement on a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant on which to update the GlossaryTerm's announcement
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the updated GlossaryTerm, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm updateAnnouncement(
            AtlanClient client,
            String qualifiedName,
            String name,
            String glossaryGuid,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        return (GlossaryTerm)
                Asset.updateAnnouncement(client, updater(qualifiedName, name, glossaryGuid), type, title, message);
    }

    /**
     * Remove the announcement from a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryTerm's announcement
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeAnnouncement(AtlanClient client, String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryTerm) Asset.removeAnnouncement(client, updater(qualifiedName, name, glossaryGuid));
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
