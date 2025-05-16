<#macro all>
    /**
     * Builds the minimal object necessary to create a DocumentDBCollection.
     *
     * @param name of the DocumentDBCollection
     * @param documentDBDatabase in which the DocumentDBCollection should be created, which must have at least
     *                a qualifiedName
     * @return the minimal request necessary to create the DocumentDBCollection, as a builder
     * @throws InvalidRequestException if the DocumentDBDatabase provided is without a qualifiedName
     */
    public static DocumentDBCollectionBuilder<?, ?> creator(
        String name,
        DocumentDBDatabase documentDBDatabase
    ) throws InvalidRequestException {
        validateRelationship(DocumentDBDatabase.TYPE_NAME, Map.of(
            "qualifiedName", documentDBDatabase.getQualifiedName()
        ));
        return creator(
            name,
            documentDBDatabase.getQualifiedName()
        ).documentDBDatabase(documentDBDatabase.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a DocumentDBCollection.
     *
     * @param name unique name of the DocumentDBCollection
     * @param databaseQualifiedName unique name of the DocumentDBDatabase in which this collection exists
     * @return the minimal object necessary to create the DocumentDBCollection, as a builder
     */
    public static DocumentDBCollectionBuilder<?, ?> creator(
        String name,
        String databaseQualifiedName
    ) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        String[] fields = databaseQualifiedName.split("/");
        String databaseName = fields.length > 0 ? fields[fields.length - 1] : null;
        
        return DocumentDBCollection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(databaseQualifiedName + "/" + name)
                .name(name)
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .documentDBDatabase(DocumentDBDatabase.refByQualifiedName(databaseQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DocumentDBCollection.
     *
     * @param name of the DocumentDBCollection
     * @param databaseQualifiedName unique name of the database in which this collection exists
     * @param databaseName simple name of the database in which this collection exists (optional, if not provided it will be derived from the databaseQualifiedName)
     * @param connectionQualifiedName unique name of the connection through which the collection is accessible (optional, if not provided it will be derived from the databaseQualifiedName)
     * @return the minimal object necessary to create the DocumentDBCollection, as a builder
     */
    public static DocumentDBCollectionBuilder<?, ?> creator(
        String name,
        String databaseQualifiedName,
        String databaseName,
        String connectionQualifiedName
    ) {
        if (connectionQualifiedName == null) {
            connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        }
        
        if (databaseName == null) {
            String[] fields = databaseQualifiedName.split("/");
            databaseName = fields.length > 0 ? fields[fields.length - 1] : null;
        }
        
        String qualifiedName = databaseQualifiedName + "/" + name;
        
        return DocumentDBCollection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name)
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .documentDBDatabase(DocumentDBDatabase.refByQualifiedName(databaseQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DocumentDBCollection.
     *
     * @param qualifiedName of the DocumentDBCollection
     * @param name of the DocumentDBCollection
     * @return the minimal request necessary to update the DocumentDBCollection, as a builder
     */
    public static DocumentDBCollectionBuilder<?, ?> updater(
        String qualifiedName,
        String name
    ) {
        return DocumentDBCollection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DocumentDBCollection, from a potentially
     * more-complete DocumentDBCollection object.
     *
     * @return the minimal object necessary to update the DocumentDBCollection, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DocumentDBCollection are not found in the initial object
     */
    @Override
    public DocumentDBCollectionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro> 