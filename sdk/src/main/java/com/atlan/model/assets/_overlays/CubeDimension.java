/**
     * Builds the minimal object necessary to create a CubeDimension.
     *
     * @param name of the CubeDimension
     * @param cube in which the dimension should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the CubeDimension, as a builder
     * @throws InvalidRequestException if the cube provided is without a qualifiedName
     */
    public static CubeDimensionBuilder<?, ?> creator(String name, Cube cube) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", cube.getQualifiedName());
        map.put("name", cube.getName());
        map.put("connectionQualifiedName", cube.getConnectionQualifiedName());
        validateRelationship(Cube.TYPE_NAME, map);
        return creator(name, cube.getConnectionQualifiedName(), cube.getName(), cube.getQualifiedName())
                .cube(cube.trimToReference());
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
        return creator(name, connectionQualifiedName, cubeName, cubeQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a CubeDimension.
     *
     * @param name of the CubeDimension
     * @param connectionQualifiedName unique name of the connection in which to create this CubeDimension
     * @param cubeName simple name of the cube in which to create this CubeDimension
     * @param cubeQualifiedName unique name of the cube in which to create this CubeDimension
     * @return the minimal request necessary to create the CubeDimension, as a builder
     */
    public static CubeDimensionBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String cubeName, String cubeQualifiedName) {
        return CubeDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, cubeQualifiedName))
                .cubeName(cubeName)
                .cubeQualifiedName(cubeQualifiedName)
                .cube(Cube.refByQualifiedName(cubeQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
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