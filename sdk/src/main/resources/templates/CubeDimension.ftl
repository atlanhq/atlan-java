<#macro imports>
import com.atlan.model.assets.Connection;
</#macro>
<#macro all>
    /**
     * Builds the minimal object necessary to create a CubeDimension.
     *
     * @param name of the CubeDimension
     * @param cube in which the dimension should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the CubeDimension, as a builder
     * @throws InvalidRequestException if the cube provided is without a qualifiedName
     */
    public static CubeDimensionBuilder<?, ?> creator(String name, Cube cube) throws InvalidRequestException {
        if (cube.getQualifiedName() == null || cube.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "Cube", "qualifiedName");
        }
        return creator(name, cube.getQualifiedName()).cube(cube.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CubeDimension.
     *
     * @param name of the CubeDimension
     * @param cubeQualifiedName unique name of the cube in which this CubeDimension exists
     * @return the minimal request necessary to create the CubeDimension, as a builder
     */
    public static CubeDimensionBuilder<?, ?> creator(String name, String cubeQualifiedName) {
        String cubeSlug = StringUtils.getNameFromQualifiedName(cubeQualifiedName);
        String cubeName = IMultiDimensionalDataset.getNameFromSlug(cubeSlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(cubeQualifiedName);
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return CubeDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, cubeQualifiedName))
                .cubeName(cubeName)
                .cubeQualifiedName(cubeQualifiedName)
                .cube(Cube.refByQualifiedName(cubeQualifiedName))
                .connectorType(connectorType)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a CubeDimension.
     *
     * @param qualifiedName of the CubeDimension
     * @param name of the CubeDimension
     * @return the minimal request necessary to update the CubeDimension, as a builder
     */
    public static CubeDimensionBuilder<?, ?> updater(String qualifiedName, String name) {
        return CubeDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique CubeDimension name.
     *
     * @param name of the CubeDimension
     * @param cubeQualifiedName unique name of the cube in which this CubeDimension exists
     * @return a unique name for the CubeDimension
     */
    public static String generateQualifiedName(String name, String cubeQualifiedName) {
        return cubeQualifiedName + "/" + IMultiDimensionalDataset.getSlugForName(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CubeDimension, from a potentially
     * more-complete CubeDimension object.
     *
     * @return the minimal object necessary to update the CubeDimension, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CubeDimension are not found in the initial object
     */
    @Override
    public CubeDimensionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
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
