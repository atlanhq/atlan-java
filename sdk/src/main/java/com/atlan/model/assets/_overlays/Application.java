/**
     * Builds the minimal object necessary to create an Application asset.
     *
     * @param name of the application
     * @param connectionQualifiedName unique name of the connection through which the application is accessible
     * @return the minimal object necessary to create the application, as a builder
     */
    public static ApplicationBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return Application._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

/**
     * Generate a unique application name.
     *
     * @param name of the application
     * @param connectionQualifiedName unique name of the connection in which this application exists
     * @return a unique name for the module
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }