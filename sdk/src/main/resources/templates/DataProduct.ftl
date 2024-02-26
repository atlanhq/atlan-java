<#macro all>
    /**
     * Builds the minimal object necessary for creating a DataProduct.
     *
     * @param name of the DataProduct
     * @param domain in which to create the DataProduct
     * @param assetSelection a query that defines which assets to include in the data product
     * @return the minimal request necessary to create the DataProduct, as a builder
     * @throws InvalidRequestException if the domain provided is without a qualifiedName
     */
    public static DataProductBuilder<?, ?> creator(String name, DataDomain domain, FluentSearch assetSelection) throws InvalidRequestException {
        return creator(Atlan.getDefaultClient(), name, domain, assetSelection);
    }

    /**
     * Builds the minimal object necessary for creating a DataProduct.
     *
     * @param client connectivity to the Atlan tenant where the DataProduct is intended to be created
     * @param name of the DataProduct
     * @param domain in which to create the DataProduct
     * @param assetSelection a query that defines which assets to include in the data product
     * @return the minimal request necessary to create the DataProduct, as a builder
     * @throws InvalidRequestException if the domain provided is without a qualifiedName
     */
    public static DataProductBuilder<?, ?> creator(AtlanClient client, String name, DataDomain domain, FluentSearch assetSelection) throws InvalidRequestException {
        return creator(client, name, domain, IndexSearchDSL.of(assetSelection.toUnfilteredQuery()));
    }

    /**
     * Builds the minimal object necessary for creating a DataProduct.
     *
     * @param name of the DataProduct
     * @param domain in which to create the DataProduct
     * @param assetSelection a query that defines which assets to include in the data product
     * @return the minimal request necessary to create the DataProduct, as a builder
     * @throws InvalidRequestException if the domain provided is without a qualifiedName
     */
    public static DataProductBuilder<?, ?> creator(String name, DataDomain domain, IndexSearchDSL assetSelection) throws InvalidRequestException {
        return creator(Atlan.getDefaultClient(), name, domain, assetSelection);
    }

    /**
     * Builds the minimal object necessary for creating a DataProduct.
     *
     * @param client connectivity to the Atlan tenant where the DataProduct is intended to be created
     * @param name of the DataProduct
     * @param domain in which to create the DataProduct
     * @param assetSelection a query that defines which assets to include in the data product
     * @return the minimal request necessary to create the DataProduct, as a builder
     * @throws InvalidRequestException if the domain provided is without a qualifiedName
     */
    public static DataProductBuilder<?, ?> creator(AtlanClient client, String name, DataDomain domain, IndexSearchDSL assetSelection) throws InvalidRequestException {
        if (domain.getQualifiedName() == null || domain.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "DataDomain", "qualifiedName");
        }
        return creator(client, name, domain.getQualifiedName(), assetSelection).dataDomain(domain.trimToReference());
    }

    /**
     * Builds the minimal object necessary for creating a DataProduct.
     *
     * @param client connectivity to the Atlan tenant where the DataProduct is intended to be created
     * @param name of the DataProduct
     * @param domainQualifiedName unique name of the DataDomain in which this product exists
     * @param assetSelection a query that defines which assets to include in the data product
     * @return the minimal request necessary to create the DataProduct, as a builder
     */
    public static DataProductBuilder<?, ?> creator(AtlanClient client, String name, String domainQualifiedName, IndexSearchDSL assetSelection) {
        String slug = IDataMesh.generateSlugForName(name);
        return DataProduct._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(domainQualifiedName, slug))
                .name(name)
                .dataProductStatus(DataProductStatus.ACTIVE)
                .parentDomainQualifiedName(domainQualifiedName)
                .superDomainQualifiedName(StringUtils.getSuperDomainQualifiedName(domainQualifiedName))
                .dataDomain(DataDomain.refByQualifiedName(domainQualifiedName))
                .dataProductAssetsDSL(DataProductAssetsDSL.builder(assetSelection).build().toJson(client))
                .dataProductAssetsPlaybookFilter("{\"condition\":\"AND\",\"isGroupLocked\":false,\"rules\":[]}");
    }

    /**
     * Generate a unique DataProduct name.
     *
     * @param domainQualifiedName unique name of the DataDomain in which this product exists
     * @param slug unique URL for the DataProduct
     * @return a unique name for the DataProduct
     */
    public static String generateQualifiedName(String domainQualifiedName, String slug) {
        return domainQualifiedName + "/product/" + slug;
    }

    /**
     * Builds the minimal object necessary to update a DataProduct.
     *
     * @param qualifiedName of the DataProduct
     * @param name of the DataProduct
     * @return the minimal request necessary to update the DataProduct, as a builder
     */
    public static DataProductBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataProduct._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataProduct, from a potentially
     * more-complete DataProduct object.
     *
     * @return the minimal object necessary to update the DataProduct, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataProduct are not found in the initial object
     */
    @Override
    public DataProductBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DataProduct", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Find a DataProduct by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the domain, if found.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param name of the DataProduct
     * @return the DataProduct, if found
     * @throws AtlanException on any API problems, or if the DataProduct does not exist
     */
    public static List<DataProduct> findByName(String name)
            throws AtlanException {
        return findByName(name, (List<AtlanField>) null);
    }

    /**
     * Find a DataProduct by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param name of the DataProduct
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the DataProduct
     * @return the DataProduct, if found
     * @throws AtlanException on any API problems, or if the DataProduct does not exist
     */
    public static List<DataProduct> findByName(String name, Collection<String> attributes)
            throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a DataProduct by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param name of the DataProduct
     * @param attributes an optional collection of attributes (checked) to retrieve for the DataProduct
     * @return the DataProduct, if found
     * @throws AtlanException on any API problems, or if the DataProduct does not exist
     */
    public static List<DataProduct> findByName(String name, List<AtlanField> attributes)
            throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a DataProduct by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the domain, if found.
     *
     * @param client connectivity to the Atlan tenant on which to search for the DataProduct
     * @param name of the DataProduct
     * @return the DataProduct, if found
     * @throws AtlanException on any API problems, or if the DataProduct does not exist
     */
    public static List<DataProduct> findByName(AtlanClient client, String name)
            throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a DataProduct by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the DataProduct
     * @param name of the DataProduct
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the DataProduct
     * @return the DataProduct, if found
     * @throws AtlanException on any API problems, or if the DataProduct does not exist
     */
    public static List<DataProduct> findByName(AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<DataProduct> results = new ArrayList<>();
        DataProduct.select(client)
                .where(DataProduct.NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof DataProduct)
                .forEach(d -> results.add((DataProduct) d));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        }
        return results;
    }

    /**
     * Find a DataProduct by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the DataProduct
     * @param name of the DataProduct
     * @param attributes an optional collection of attributes (checked) to retrieve for the DataProduct
     * @return the DataProduct, if found
     * @throws AtlanException on any API problems, or if the DataProduct does not exist
     */
    public static List<DataProduct> findByName(AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<DataProduct> results = new ArrayList<>();
        DataProduct.select(client)
                .where(DataProduct.NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof DataProduct)
                .forEach(d -> results.add((DataProduct) d));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        }
        return results;
    }
</#macro>
