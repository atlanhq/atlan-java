<#macro imports>
import com.atlan.model.assets.Connection;
</#macro>
<#macro all>
    /**
     * Builds the minimal object necessary to create a CubeHierarchy.
     *
     * @param name of the CubeHierarchy
     * @param dimension in which the hierarchy should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the CubeHierarchy, as a builder
     * @throws InvalidRequestException if the dimension provided is without a qualifiedName
     */
    public static CubeHierarchyBuilder<?, ?> creator(String name, CubeDimension dimension)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", dimension.getConnectionQualifiedName());
        map.put("cubeName", dimension.getCubeName());
        map.put("cubeQualifiedName", dimension.getCubeQualifiedName());
        map.put("name", dimension.getName());
        map.put("qualifiedName", dimension.getQualifiedName());
        validateRelationship(CubeDimension.TYPE_NAME, map);
        return creator(
            name,
            dimension.getConnectionQualifiedName(),
            dimension.getCubeName(),
            dimension.getCubeQualifiedName(),
            dimension.getName(),
            dimension.getQualifiedName()
        ).cubeDimension(dimension.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CubeHierarchy.
     *
     * @param name of the CubeHierarchy
     * @param dimensionQualifiedName unique name of the cube dimension in which this CubeHierarchy exists
     * @return the minimal request necessary to create the CubeHierarchy, as a builder
     */
    public static CubeHierarchyBuilder<?, ?> creator(String name, String dimensionQualifiedName) {
        String dimensionSlug = StringUtils.getNameFromQualifiedName(dimensionQualifiedName);
        String dimensionName = IMultiDimensionalDataset.getNameFromSlug(dimensionSlug);
        String cubeQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dimensionQualifiedName);
        String cubeSlug = StringUtils.getNameFromQualifiedName(cubeQualifiedName);
        String cubeName = IMultiDimensionalDataset.getNameFromSlug(cubeSlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(cubeQualifiedName);
        return creator(name, connectionQualifiedName, cubeName, cubeQualifiedName, dimensionName, dimensionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a CubeHierarchy.
     *
     * @param name of the CubeHierarchy
     * @param connectionQualifiedName unique name of the connection in which the CubeHierarchy should be created
     * @param cubeName simple name of the Cube in which the CubeHierarchy should be created
     * @param cubeQualifiedName unique name of the Cube in which the CubeHierarchy should be created
     * @param dimensionName simple name of the CubeDimension in which the CubeHierarchy should be created
     * @param dimensionQualifiedName unique name of the CubeDimension in which the CubeHierarchy should be created
     * @return the minimal request necessary to create the CubeHierarchy, as a builder
     */
    public static CubeHierarchyBuilder<?, ?> creator(
        String name,
        String connectionQualifiedName,
        String cubeName,
        String cubeQualifiedName,
        String dimensionName,
        String dimensionQualifiedName
    ) {
        return CubeHierarchy._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(generateQualifiedName(name, dimensionQualifiedName))
            .cubeName(cubeName)
            .cubeQualifiedName(cubeQualifiedName)
            .cubeDimensionName(dimensionName)
            .cubeDimensionQualifiedName(dimensionQualifiedName)
            .cubeDimension(CubeDimension.refByQualifiedName(dimensionQualifiedName))
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a CubeHierarchy.
     *
     * @param qualifiedName of the CubeHierarchy
     * @param name of the CubeHierarchy
     * @return the minimal request necessary to update the CubeHierarchy, as a builder
     */
    public static CubeHierarchyBuilder<?, ?> updater(String qualifiedName, String name) {
        return CubeHierarchy._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique CubeHierarchy name.
     *
     * @param name of the CubeHierarchy
     * @param dimensionQualifiedName unique name of the cube dimension in which this CubeHierarchy exists
     * @return a unique name for the CubeHierarchy
     */
    public static String generateQualifiedName(String name, String dimensionQualifiedName) {
        return dimensionQualifiedName + "/" + IMultiDimensionalDataset.getSlugForName(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CubeHierarchy, from a potentially
     * more-complete CubeHierarchy object.
     *
     * @return the minimal object necessary to update the CubeHierarchy, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CubeHierarchy are not found in the initial object
     */
    @Override
    public CubeHierarchyBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
