/**
     * Builds the minimal object necessary to create a Dataverse entity.
     *
     * @param name of the Dataverse entity
     * @param connectionQualifiedName unique name of the connection through which the entity is accessible
     * @return the minimal object necessary to create the Dataverse entity, as a builder
     */
    public static DataverseEntity.DataverseEntityBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return DataverseEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }