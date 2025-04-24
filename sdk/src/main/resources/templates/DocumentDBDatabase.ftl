<#macro all>
    /**
     * Builds the minimal object necessary to create a DocumentDBDatabase.
     *
     * @param name of the DocumentDBDatabase
     * @param connectionQualifiedName unique name of the connection through which the database is accessible
     * @return the minimal object necessary to create the DocumentDBDatabase, as a builder
     */
    public static DocumentDBDatabaseBuilder<?, ?> creator(
        String name,
        String connectionQualifiedName
    ) {
        return DocumentDBDatabase._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName));
    }

    /**
     * Builds the minimal object necessary to update a DocumentDBDatabase.
     *
     * @param qualifiedName of the DocumentDBDatabase
     * @param name of the DocumentDBDatabase
     * @return the minimal request necessary to update the DocumentDBDatabase, as a builder
     */
    public static DocumentDBDatabaseBuilder<?, ?> updater(
        String qualifiedName,
        String name
    ) {
        return DocumentDBDatabase._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DocumentDBDatabase, from a potentially
     * more-complete DocumentDBDatabase object.
     *
     * @return the minimal object necessary to update the DocumentDBDatabase, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DocumentDBDatabase are not found in the initial object
     */
    @Override
    public DocumentDBDatabaseBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro> 