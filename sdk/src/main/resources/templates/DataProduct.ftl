<#macro all>
    /**
     * Builds the minimal object necessary for creating a DataProduct.
     *
     * @param client connectivity to the Atlan tenant where the DataProduct is intended to be created
     * @param name of the DataProduct
     * @param domainQualifiedName unique name of the DataDomain in which this product exists
     * @param assetSelection a query that defines which assets to include in the data product
     * @return the minimal request necessary to create the DataProduct, as a builder
     * @throws InvalidRequestException if the domain provided is without a qualifiedName
     */
    public static DataProductBuilder<?, ?> creator(
            AtlanClient client, String name, String domainQualifiedName, FluentSearch assetSelection)
            throws InvalidRequestException {
        return creator(name, domainQualifiedName, "").assetSelection(client, assetSelection);
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
        return DataProduct._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(domainQualifiedName + "/product/" + name)
                .name(name)
                .dataProductStatus(DataProductStatus.ACTIVE)
                .parentDomainQualifiedName(domainQualifiedName)
                .superDomainQualifiedName(StringUtils.getSuperDomainQualifiedName(domainQualifiedName))
                .dataDomain(DataDomain.refByQualifiedName(domainQualifiedName))
                .assetSelection(client, assetSelection)
                .dataProductAssetsPlaybookFilter("{\"condition\":\"AND\",\"isGroupLocked\":false,\"rules\":[]}")
                .daapStatus(DataProductStatus.ACTIVE);
    }

    /**
     * Builds the minimal object necessary for creating a DataProduct.
     *
     * @param name of the DataProduct
     * @param domainQualifiedName unique name of the DataDomain in which this product exists
     * @param assetSelection a string containing a query that defines which assets to include in the data product
     * @return the minimal request necessary to create the DataProduct, as a builder
     */
    public static DataProductBuilder<?, ?> creator(String name, String domainQualifiedName, String assetSelection) {
        return DataProduct._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(domainQualifiedName + "/product/" + name)
                .name(name)
                .dataProductStatus(DataProductStatus.ACTIVE)
                .parentDomainQualifiedName(domainQualifiedName)
                .superDomainQualifiedName(StringUtils.getSuperDomainQualifiedName(domainQualifiedName))
                .dataDomain(DataDomain.refByQualifiedName(domainQualifiedName))
                .dataProductAssetsDSL(assetSelection)
                .dataProductAssetsPlaybookFilter("{\"condition\":\"AND\",\"isGroupLocked\":false,\"rules\":[]}")
                .daapStatus(DataProductStatus.ACTIVE);
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
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves the assets associated with this data product.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a stream of the assets related to the data product
     * @throws AtlanException on any API problems
     */
    @JsonIgnore
    public IndexSearchResponse getAssets(AtlanClient client) throws AtlanException {
        try {
            DataProductAssetsDSL dsl = client.readValue(getDataProductAssetsDSL(), DataProductAssetsDSL.class);
            return client.assets.search(dsl.getQuery());
        } catch (IOException e) {
            throw new LogicException(ErrorCode.DATA_PRODUCT_QUERY_ERROR, e);
        }
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

    public abstract static class DataProductBuilder<C extends DataProduct, B extends DataProductBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {

        /**
         * Change the selection of assets for the data product, based on the specified Atlan fluent search.
         *
         * @param client connectivity to an Atlan tenant
         * @param assetSelection fluent search query that defines the assets to include in the data product
         * @return the builder for the data product, with an updated set of criteria for its assets
         */
        public B assetSelection(AtlanClient client, FluentSearch assetSelection) {
            return assetSelection(client, IndexSearchDSL.of(assetSelection.toUnfilteredQuery()));
        }

        /**
         * Change the selection of assets for the data product, based on the specified Atlan search query.
         *
         * @param client connectivity to an Atlan tenant
         * @param assetSelection search query that defines the assets to include in the data product
         * @return the builder for the data product, with an updated set of criteria for its assets
         */
        public B assetSelection(AtlanClient client, IndexSearchDSL assetSelection) {
            return this.dataProductAssetsDSL(
                    DataProductAssetsDSL.builder(assetSelection).build().toJson(client));
        }
    }
</#macro>
