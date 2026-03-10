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
     * Generate a unique DataContract name.
     *
     * @param assetQualifiedName unique name of the asset for which this DataContract exists
     * @return a unique name for the DataContract
     */
    public static String generateQualifiedName(String assetQualifiedName) {
        return assetQualifiedName + "/contract";
    }