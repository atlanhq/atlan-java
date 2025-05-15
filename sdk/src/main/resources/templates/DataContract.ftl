<#macro all>
    /**
     * Builds the minimal object necessary to create a DataContract.
     *
     * @param contract detailed specification of the contract
     * @param asset asset for which to create this contract
     * @return the minimal request necessary to create the DataContract, as a builder
     * @throws InvalidRequestException if the asset provided is without some required information
     */
    public static DataContractBuilder<?, ?> creator(String contract, Asset asset) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", asset.getQualifiedName());
        map.put("name", asset.getName());
        validateRelationship(asset.getTypeName(), map);
        return creator(contract, asset.getName(), asset.getQualifiedName());
    }

    /**
     * Builds the minimal object necessary to create a DataContract.
     *
     * @param contract detailed specification of the contract
     * @param assetQualifiedName unique name of the asset for which to create this contract
     * @return the minimal request necessary to create the DataContract, as a builder
     */
    public static DataContractBuilder<?, ?> creator(String contract, String assetQualifiedName) {
        return creator(contract, StringUtils.getNameFromQualifiedName(assetQualifiedName), assetQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DataContract.
     *
     * @param contract detailed specification of the contract
     * @param assetName simple name of the asset for which to create this contract
     * @param assetQualifiedName unique name of the asset for which to create this contract
     * @return the minimal request necessary to create the DataContract, as a builder
     */
    public static DataContractBuilder<?, ?> creator(String contract, String assetName, String assetQualifiedName) {
        String contractName = "Data contract for " + assetName;
        return DataContract._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(contractName)
                .qualifiedName(generateQualifiedName(assetQualifiedName))
                .dataContractJson(contract);
    }

    /**
     * Builds the minimal object necessary to update a DataContract.
     *
     * @param qualifiedName of the DataContract
     * @param name of the DataContract
     * @return the minimal request necessary to update the DataContract, as a builder
     */
    public static DataContractBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataContract._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique DataContract name.
     *
     * @param assetQualifiedName unique name of the asset for which this DataContract exists
     * @return a unique name for the DataContract
     */
    public static String generateQualifiedName(String assetQualifiedName) {
        return assetQualifiedName + "/contract";
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataContract, from a potentially
     * more-complete DataContract object.
     *
     * @return the minimal object necessary to update the DataContract, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataContract are not found in the initial object
     */
    @Override
    public DataContractBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
