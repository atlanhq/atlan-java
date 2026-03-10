/**
     * Builds the minimal object necessary to create an Anaplan System Dimension.
     *
     * @param name of the Anaplan System Dimension
     * @param connectionQualifiedName unique name of the connection through which the system dimension is accessible
     * @return the minimal object necessary to create the Anaplan system dimension, as a builder
     */
    public static AnaplanSystemDimension.AnaplanSystemDimensionBuilder<?, ?> creator(
            String name, String connectionQualifiedName) {
        return AnaplanSystemDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }