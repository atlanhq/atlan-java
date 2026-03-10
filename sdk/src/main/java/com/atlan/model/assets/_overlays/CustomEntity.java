/**
     * Builds the minimal object necessary to create a CustomEntity.
     *
     * @param name of the CustomEntity
     * @param connectionQualifiedName unique name of the connection through which the entity is accessible
     * @return the minimal object necessary to create the CustomEntity, as a builder
     */
    public static CustomEntity.CustomEntityBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return CustomEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }