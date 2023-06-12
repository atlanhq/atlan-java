<#macro all>
    /**
     * Builds the minimal object necessary to create a Google Data Studio asset.
     *
     * @param name of the asset
     * @param connectionQualifiedName unique name of the connection through which the asset is accessible
     * @param assetType type of the asset
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static DataStudioAssetBuilder<?, ?> creator(
            String name, String connectionQualifiedName, GoogleDataStudioAssetType assetType) {
        return DataStudioAsset.builder()
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.DATASTUDIO)
                .dataStudioAssetType(assetType);
    }

    /**
     * Builds the minimal object necessary to update a DataStudioAsset.
     *
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the minimal request necessary to update the DataStudioAsset, as a builder
     */
    public static DataStudioAssetBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataStudioAsset.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataStudioAsset, from a potentially
     * more-complete DataStudioAsset object.
     *
     * @return the minimal object necessary to update the DataStudioAsset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataStudioAsset are not found in the initial object
     */
    @Override
    public DataStudioAssetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DataStudioAsset", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }


</#macro>
