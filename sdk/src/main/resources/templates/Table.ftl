<#macro all>
    /**
     * Builds the minimal object necessary to create a table.
     *
     * @param name of the table
     * @param schema in which the table should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the table, as a builder
     * @throws InvalidRequestException if the schema provided is without a qualifiedName
     */
    public static TableBuilder<?, ?> creator(String name, Schema schema) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", schema.getConnectionQualifiedName());
        map.put("databaseName", schema.getDatabaseName());
        map.put("databaseQualifiedName", schema.getDatabaseQualifiedName());
        map.put("name", schema.getName());
        map.put("qualifiedName", schema.getQualifiedName());
        validateRelationship(Schema.TYPE_NAME, map);
        return creator(
            name,
            schema.getConnectionQualifiedName(),
            schema.getDatabaseName(),
            schema.getDatabaseQualifiedName(),
            schema.getName(),
            schema.getQualifiedName()
        ).schema(schema.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a table.
     *
     * @param name of the table
     * @param schemaQualifiedName unique name of the schema in which this table exists
     * @return the minimal request necessary to create the table, as a builder
     */
    public static TableBuilder<?, ?> creator(String name, String schemaQualifiedName) {
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return creator(name, connectionQualifiedName, databaseName, databaseQualifiedName, schemaName, schemaQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a table.
     *
     * @param name of the table
     * @param connectionQualifiedName unique name of the connection in which to create the Table
     * @param databaseName simple name of the Database in which to create the Table
     * @param databaseQualifiedName unique name of the Database in which to create the Table
     * @param schemaName simple name of the Schema in which to create the Table
     * @param schemaQualifiedName unique name of the Schema in which to create the Table
     * @return the minimal request necessary to create the table, as a builder
     */
    public static TableBuilder<?, ?> creator(
        String name,
        String connectionQualifiedName,
        String databaseName,
        String databaseQualifiedName,
        String schemaName,
        String schemaQualifiedName
    ) {
        return Table._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(generateQualifiedName(name, schemaQualifiedName))
            .schemaName(schemaName)
            .schemaQualifiedName(schemaQualifiedName)
            .schema(Schema.refByQualifiedName(schemaQualifiedName))
            .databaseName(databaseName)
            .databaseQualifiedName(databaseQualifiedName)
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a Table.
     *
     * @param qualifiedName of the Table
     * @param name of the Table
     * @return the minimal request necessary to update the Table, as a builder
     */
    public static TableBuilder<?, ?> updater(String qualifiedName, String name) {
        return Table._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique table name.
     *
     * @param name of the table
     * @param schemaQualifiedName unique name of the schema in which this table exists
     * @return a unique name for the table
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a Table, from a potentially
     * more-complete Table object.
     *
     * @return the minimal object necessary to update the Table, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Table are not found in the initial object
     */
    @Override
    public TableBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
