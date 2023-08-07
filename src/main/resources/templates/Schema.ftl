<#macro all>
    /**
     * Builds the minimal object necessary to create a schema.
     *
     * @param name of the schema
     * @param databaseQualifiedName unique name of the database in which this schema exists
     * @return the minimal request necessary to create the schema, as a builder
     */
    public static SchemaBuilder<?, ?> creator(String name, String databaseQualifiedName) {
        String[] tokens = databaseQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return Schema._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, databaseQualifiedName))
                .connectorType(connectorType)
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Schema", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
