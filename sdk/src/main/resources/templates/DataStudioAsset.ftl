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
        return DataStudioAsset.creator(name, connectionQualifiedName, assetType, UUID.randomUUID().toString());
    }

    /**
     * Builds the minimal object necessary to create a Google Data Studio asset.
     *
     * @param name of the asset
     * @param connectionQualifiedName unique name of the connection through which the asset is accessible
     * @param assetType type of the asset
     * @param gdsId unique identifier of the asset within Google Data Studio
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static DataStudioAssetBuilder<?, ?> creator(
            String name, String connectionQualifiedName, GoogleDataStudioAssetType assetType, String gdsId) {
        return DataStudioAsset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(gdsId, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .dataStudioAssetType(assetType);
    }

    /**
     * Generate a unique DataStudioAsset name.
     *
     * @param gdsId unique identifier of the asset within Google Data Studio
     * @param connectionQualifiedName unique name of the connection through which the DataStudioAsset is accessible
     * @return a unique name for the DataStudioAsset
     */
    public static String generateQualifiedName(String gdsId, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + gdsId;
    }

    /**
     * Builds the minimal object necessary to update a DataStudioAsset.
     *
     * @param qualifiedName of the DataStudioAsset
     * @param name of the DataStudioAsset
     * @return the minimal request necessary to update the DataStudioAsset, as a builder
     */
    public static DataStudioAssetBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataStudioAsset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
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
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
