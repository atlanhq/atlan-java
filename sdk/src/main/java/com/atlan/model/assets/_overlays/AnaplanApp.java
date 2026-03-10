/**
     * Builds the minimal object necessary to create an Anaplan app.
     *
     * @param name of the Anaplan app
     * @param connectionQualifiedName unique name of the connection through which the app is accessible
     * @return the minimal object necessary to create the Anaplan app, as a builder
     */
    public static AnaplanApp.AnaplanAppBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return AnaplanApp._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }