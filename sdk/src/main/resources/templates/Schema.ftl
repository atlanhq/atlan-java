<#macro all>
    /**
     * Builds the minimal object necessary to create a schema.
     *
     * @param name of the schema
     * @param database in which the schema should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the schema, as a builder
     * @throws InvalidRequestException if the database provided is without a qualifiedName
     */
    public static SchemaBuilder<?, ?> creator(String name, Database database) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", database.getQualifiedName());
        map.put("name", database.getName());
        map.put("connectionQualifiedName", database.getConnectionQualifiedName());
        validateRelationship(Database.TYPE_NAME, map);
        return creator(
            name,
            database.getConnectionQualifiedName(),
            database.getName(),
            database.getQualifiedName()
        ).database(database.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a schema.
     *
     * @param name of the schema
     * @param databaseQualifiedName unique name of the database in which this schema exists
     * @return the minimal request necessary to create the schema, as a builder
     */
    public static SchemaBuilder<?, ?> creator(String name, String databaseQualifiedName) {
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return creator(name, connectionQualifiedName, databaseName, databaseQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a schema.
     *
     * @param name of the schema
     * @param connectionQualifiedName unique name of the connection in which to create the Schema
     * @param databaseName simple name of the database in which to create the Schema
     * @param databaseQualifiedName unique name of the database in which to create the Schema
     * @return the minimal request necessary to create the schema, as a builder
     */
    public static SchemaBuilder<?, ?> creator(String name, String connectionQualifiedName, String databaseName, String databaseQualifiedName) {
        return Schema._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(generateQualifiedName(name, databaseQualifiedName))
            .databaseName(databaseName)
            .databaseQualifiedName(databaseQualifiedName)
            .database(Database.refByQualifiedName(databaseQualifiedName))
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique schema name.
     *
     * @param name of the schema
     * @param databaseQualifiedName unique name of the database in which this schema exists
     * @return a unique name for the schema
     */
    public static String generateQualifiedName(String name, String databaseQualifiedName) {
        return databaseQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a Schema.
     *
     * @param qualifiedName of the Schema
     * @param name of the Schema
     * @return the minimal request necessary to update the Schema, as a builder
     */
    public static SchemaBuilder<?, ?> updater(String qualifiedName, String name) {
        return Schema._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Schema, from a potentially
     * more-complete Schema object.
     *
     * @return the minimal object necessary to update the Schema, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Schema are not found in the initial object
     */
    @Override
    public SchemaBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
