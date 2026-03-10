/**
     * Builds the minimal object necessary to create an API spec.
     *
     * @param name of the API spec
     * @param connectionQualifiedName unique name of the connection through which the spec is accessible
     * @return the minimal object necessary to create the API spec, as a builder
     */
    public static APISpecBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return APISpec._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }