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
    public static CubeHierarchyBuilder<?, ?> creator(String name, CubeDimension dimension) throws InvalidRequestException {
        if (dimension.getQualifiedName() == null || dimension.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "CubeDimension", "qualifiedName");
        }
        return creator(name, dimension.getQualifiedName()).cubeDimension(dimension.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CubeHierarchy.
     *
     * @param name of the CubeHierarchy
     * @param dimensionQualifiedName unique name of the cube dimension in which this CubeHierarchy exists
     * @return the minimal request necessary to create the CubeHierarchy, as a builder
     */
    public static CubeHierarchyBuilder<?, ?> creator(String name, String dimensionQualifiedName) {
        String dimensionName = StringUtils.getNameFromQualifiedName(dimensionQualifiedName);
        String cubeQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dimensionQualifiedName);
        String cubeName = StringUtils.getNameFromQualifiedName(cubeQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(cubeQualifiedName);
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return CubeHierarchy._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, dimensionQualifiedName))
                .cubeName(cubeName)
                .cubeQualifiedName(cubeQualifiedName)
                .cubeDimensionName(dimensionName)
                .cubeDimensionQualifiedName(dimensionQualifiedName)
                .cubeDimension(CubeDimension.refByQualifiedName(dimensionQualifiedName))
                .connectorType(connectorType)
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
        return dimensionQualifiedName + "/" + name;
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, TYPE_NAME, String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
