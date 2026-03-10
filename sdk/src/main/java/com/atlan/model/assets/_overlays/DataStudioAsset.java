// IMPORT: import com.atlan.model.enums.GoogleDataStudioAssetType;
// IMPORT: import com.atlan.model.structs.GoogleLabel;
// IMPORT: import com.atlan.model.structs.GoogleTag;
// IMPORT: import java.util.UUID;

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
        return DataStudioAsset.creator(
                name, connectionQualifiedName, assetType, UUID.randomUUID().toString());
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