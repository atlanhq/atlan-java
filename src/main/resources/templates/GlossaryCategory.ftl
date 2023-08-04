<#macro all>
    /**
     * Builds the minimal object necessary for creating a GlossaryCategory. At least one of glossaryGuid or
     * glossaryQualifiedName must be provided.
     *
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique identifier of the GlossaryCategory's glossary
     * @param glossaryQualifiedName unique name of the GlossaryCategory's glossary
     * @return the minimal object necessary to create the GlossaryCategory, as a builder
     */
    public static GlossaryCategoryBuilder<?, ?> creator(
            String name, String glossaryGuid, String glossaryQualifiedName) {
        return GlossaryCategory._internal()
                .qualifiedName(name)
                .name(name)
                .anchor(Glossary.anchorLink(glossaryGuid, glossaryQualifiedName));
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
                .qualifiedName(qualifiedName)
                .name(name)
                .anchor(Glossary.anchorLink(glossaryGuid, null));
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (this.getAnchor() == null
                || !this.getAnchor().isValidReferenceByGuid()) {
            missing.add("anchor.guid");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "GlossaryCategory", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName(), this.getAnchor().getGuid());
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the category, if found. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param name of the GlossaryCategory
     * @param glossaryName name of the Glossary in which the category exists
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByName(String name, String glossaryName)
            throws AtlanException {
        return findByName(name, glossaryName, null);
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param name of the GlossaryCategory
     * @param glossaryName name of the Glossary in which the category exists
     * @param attributes an optional collection of attributes to retrieve for the GlossaryCategory
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByName(String name, String glossaryName, Collection<String> attributes)
            throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, glossaryName, attributes);
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
        return findByName(client, name, glossaryName, null);
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
     * @param attributes an optional collection of attributes to retrieve for the GlossaryCategory
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByName(AtlanClient client, String name, String glossaryName, Collection<String> attributes)
            throws AtlanException {
        Glossary glossary = Glossary.findByName(client, glossaryName, null);
        return findByNameFast(client, name, glossary.getQualifiedName(), attributes);
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the category, if found.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param name of the GlossaryCategory
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByNameFast(String name, String glossaryQualifiedName) throws AtlanException {
        return findByNameFast(name, glossaryQualifiedName, null);
    }

    /**
     * Find a GlossaryCategory by its human-readable name.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param name of the GlossaryCategory
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @param attributes an optional collection of attributes to retrieve for the GlossaryCategory
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByNameFast(
            String name, String glossaryQualifiedName, Collection<String> attributes) throws AtlanException {
        return findByNameFast(Atlan.getDefaultClient(), name, glossaryQualifiedName, attributes);
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
        return findByNameFast(client, name, glossaryQualifiedName, null);
    }

    /**
     * Find a GlossaryCategory by its human-readable name.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @param attributes an optional collection of attributes to retrieve for the GlossaryCategory
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByNameFast(
            AtlanClient client, String name, String glossaryQualifiedName, Collection<String> attributes) throws AtlanException {
        List<GlossaryCategory> results = new ArrayList<>();
        GlossaryCategory.all(client)
            .filter(QueryFactory.have(KeywordFields.NAME).eq(name))
            .filter(QueryFactory.have(KeywordFields.GLOSSARY).eq(glossaryQualifiedName))
            .attributes(attributes == null ? Collections.emptyList() : attributes)
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
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeDescription(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid);
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
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeUserDescription(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid);
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
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeOwners(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid);
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
     * @param qualifiedName of the GlossaryCategory
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GlossaryCategory, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory updateCertificate(
            String qualifiedName, String name, String glossaryGuid, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid, certificate, message);
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
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeCertificate(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid);
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
            String qualifiedName,
            String name,
            String glossaryGuid,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid, type, title, message);
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
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeAnnouncement(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid);
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
</#macro>
