/**
     * Builds the minimal object necessary to create a Cube.
     *
     * @param name of the Cube
     * @param connectionQualifiedName unique name of the connection in which this Cube exists
     * @return the minimal request necessary to create the Cube, as a builder
     */
    public static CubeBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return Cube._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

/**
     * Generate a unique Cube name.
     *
     * @param name of the Cube
     * @param connectionQualifiedName unique name of the connection in which this Cube exists
     * @return a unique name for the Cube
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }