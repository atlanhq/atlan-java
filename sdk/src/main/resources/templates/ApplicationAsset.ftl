<#macro all>

    /**
     * Builds the minimal object necessary to create a Application_Asset asset.
     *
     * @param name of the application asset
     * @param connectionQualifiedName unique name of the connection through which the application asset is accessible
     * @return the minimal object necessary to create the application asset, as a builder
     */
    public static ApplicationAsset.ApplicationAssetBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return ApplicationAsset._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(connectionQualifiedName + "/" + name)
            .name(name)
            .connectionQualifiedName(connectionQualifiedName)
            .connectorType(AtlanConnectorType.APPLICATION);
    }

    /**
     * Builds the minimal object necessary to create a Application_Asset asset.
     *
     * @param name of the application asset
     * @param connectionQualifiedName unique name of the connection through which the application asset is accessible
     * @param applicationAssetId id of the application asset in the source system
     * @return the minimal object necessary to create the application asset, as a builder
     */
    public static ApplicationAsset.ApplicationAssetBuilder<?, ?> creator(String name, String connectionQualifiedName, String applicationAssetId) {
        return ApplicationAsset._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(connectionQualifiedName + "/" + name)
            .name(name)
            .connectionQualifiedName(connectionQualifiedName)
            .applicationId(applicationAssetId)
            .connectorType(AtlanConnectorType.APPLICATION);
    }

    /**
     * Builds the minimal object necessary to create a Application_Asset asset.
     *
     * @param name of the application asset
     * @param connectionQualifiedName unique name of the connection through which the application asset is accessible
     * @param applicationAssetId id of the application asset in the source system
     * @param applicationCatalogList list for catalog assets contained in this application asset
     * @return the minimal object necessary to create the application asset, as a builder
     */
    public static ApplicationAsset.ApplicationAssetBuilder<?, ?> creator(String name, String connectionQualifiedName, String applicationAssetId, SortedSet<ICatalog> applicationCatalogList) {
        return ApplicationAsset._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(connectionQualifiedName + "/" + name)
            .name(name)
            .connectionQualifiedName(connectionQualifiedName)
            .applicationId(applicationAssetId)
            .applicationCatalog(applicationCatalogList)
            .connectorType(AtlanConnectorType.APPLICATION);
    }

</#macro>
