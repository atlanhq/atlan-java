<#macro all>
    /**
     * Builds the minimal object necessary for creating a DataDomain.
     *
     * @param name of the DataDomain
     * @return the minimal request necessary to create the DataDomain, as a builder
     * @throws InvalidRequestException will never throw but required given signature of called method
     */
    public static DataDomainBuilder<?, ?> creator(String name) throws InvalidRequestException {
        return creator(name, (DataDomain) null);
    }

    /**
     * Builds the minimal object necessary for creating a DataDomain.
     *
     * @param name of the DataDomain
     * @param parent (optional) parent data domain in which to create this subdomain
     * @return the minimal request necessary to create the DataDomain, as a builder
     * @throws InvalidRequestException if the parent domain provided is without a qualifiedName
     */
    public static DataDomainBuilder<?, ?> creator(String name, DataDomain parent) throws InvalidRequestException {
        if (parent != null) {
            return creator(name, parent.getQualifiedName()).parentDomain(parent.trimToReference());
        }
        return creator(name, (String) null);
    }

    /**
     * Builds the minimal object necessary for creating a DataDomain.
     *
     * @param name of the DataDomain
     * @param parentDomainQualifiedName (optional) unique name of the data domain in which to create this subdomain
     * @return the minimal request necessary to create the DataDomain, as a builder
     */
    public static DataDomainBuilder<?, ?> creator(String name, String parentDomainQualifiedName) throws InvalidRequestException {
        String slug = IDataMesh.generateSlugForName(name);
        DataDomainBuilder<?, ?> builder = DataDomain._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(generateQualifiedName(slug, parentDomainQualifiedName))
            .name(name);
        if (parentDomainQualifiedName != null) {
            builder.parentDomain(DataDomain.refByQualifiedName(parentDomainQualifiedName))
                .parentDomainQualifiedName(parentDomainQualifiedName);
        }
        return builder;
    }

    /**
     * Generate a unique DataDomain name.
     *
     * @param slug unique URL for the DataDomain
     * @param parentDomainQualifiedName (optional) unique name of the parent domain, if this is a subdomain
     * @return a unique name for the DataDomain
     */
    public static String generateQualifiedName(String slug, String parentDomainQualifiedName) {
        return (parentDomainQualifiedName != null && !parentDomainQualifiedName.isEmpty())
            ? parentDomainQualifiedName + "/domain/" + slug
            : "default/domain/" + slug;
    }

    /**
     * Builds the minimal object necessary to update a DataDomain.
     *
     * @param qualifiedName of the DataDomain
     * @param name of the DataDomain
     * @return the minimal request necessary to update the DataDomain, as a builder
     */
    public static DataDomainBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataDomain._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataDomain, from a potentially
     * more-complete DataDomain object.
     *
     * @return the minimal object necessary to update the DataDomain, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataDomain are not found in the initial object
     */
    @Override
    public DataDomainBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DataDomain", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Find a DataDomain by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the domain, if found.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param name of the DataDomain
     * @return the DataDomain, if found
     * @throws AtlanException on any API problems, or if the DataDomain does not exist
     */
    public static List<DataDomain> findByName(String name)
            throws AtlanException {
        return findByName(name, (List<AtlanField>) null);
    }

    /**
     * Find a DataDomain by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param name of the DataDomain
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the DataDomain
     * @return the DataDomain, if found
     * @throws AtlanException on any API problems, or if the DataDomain does not exist
     */
    public static List<DataDomain> findByName(String name, Collection<String> attributes)
            throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a DataDomain by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param name of the DataDomain
     * @param attributes an optional collection of attributes (checked) to retrieve for the DataDomain
     * @return the DataDomain, if found
     * @throws AtlanException on any API problems, or if the DataDomain does not exist
     */
    public static List<DataDomain> findByName(String name, List<AtlanField> attributes)
            throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a DataDomain by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the domain, if found.
     *
     * @param client connectivity to the Atlan tenant on which to search for the DataDomain
     * @param name of the DataDomain
     * @return the DataDomain, if found
     * @throws AtlanException on any API problems, or if the DataDomain does not exist
     */
    public static List<DataDomain> findByName(AtlanClient client, String name)
            throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a DataDomain by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the DataDomain
     * @param name of the DataDomain
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the DataDomain
     * @return the DataDomain, if found
     * @throws AtlanException on any API problems, or if the DataDomain does not exist
     */
    public static List<DataDomain> findByName(AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<DataDomain> results = new ArrayList<>();
        DataDomain.select(client)
                .where(DataDomain.NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof DataDomain)
                .forEach(d -> results.add((DataDomain) d));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        }
        return results;
    }

    /**
     * Find a DataDomain by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the DataDomain
     * @param name of the DataDomain
     * @param attributes an optional collection of attributes (checked) to retrieve for the DataDomain
     * @return the DataDomain, if found
     * @throws AtlanException on any API problems, or if the DataDomain does not exist
     */
    public static List<DataDomain> findByName(AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<DataDomain> results = new ArrayList<>();
        DataDomain.select(client)
                .where(DataDomain.NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof DataDomain)
                .forEach(d -> results.add((DataDomain) d));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        }
        return results;
    }
</#macro>
