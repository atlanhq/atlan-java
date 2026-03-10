/**
     * Builds the minimal object necessary to create a DocumentDBDatabase.
     *
     * @param name of the DocumentDBDatabase
     * @param connectionQualifiedName unique name of the connection through which the database is accessible
     * @return the minimal object necessary to create the DocumentDBDatabase, as a builder
     */
    public static DocumentDBDatabaseBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return DocumentDBDatabase._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }