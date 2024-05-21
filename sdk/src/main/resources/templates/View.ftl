<#macro all>
    /**
     * Builds the minimal object necessary to create a view.
     *
     * @param name of the view
     * @param schema in which the view should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the view, as a builder
     * @throws InvalidRequestException if the schema provided is without a qualifiedName
     */
    public static ViewBuilder<?, ?> creator(String name, Schema schema) throws InvalidRequestException {
        validateRelationship(Schema.TYPE_NAME, Map.of(
            "connectionQualifiedName", schema.getConnectionQualifiedName(),
            "databaseName", schema.getDatabaseName(),
            "databaseQualifiedName", schema.getDatabaseQualifiedName(),
            "name", schema.getName(),
            "qualifiedName", schema.getQualifiedName()
        ));
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
     * Builds the minimal object necessary to create a view.
     *
     * @param name of the view
     * @param schemaQualifiedName unique name of the schema in which this view exists
     * @return the minimal request necessary to create the view, as a builder
     */
    public static ViewBuilder<?, ?> creator(String name, String schemaQualifiedName) {
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return creator(name, connectionQualifiedName, databaseName, databaseQualifiedName, schemaName, schemaQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a view.
     *
     * @param name of the view
     * @param connectionQualifiedName unique name of the connection in which to create the View
     * @param databaseName simple name of the Database in which to create the View
     * @param databaseQualifiedName unique name of the Database in which to create the View
     * @param schemaName simple name of the Schema in which to create the View
     * @param schemaQualifiedName unique name of the Schema in which to create the View
     * @return the minimal request necessary to create the view, as a builder
     */
    public static ViewBuilder<?, ?> creator(
        String name,
        String connectionQualifiedName,
        String databaseName,
        String databaseQualifiedName,
        String schemaName,
        String schemaQualifiedName
    ) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return View._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(generateQualifiedName(name, schemaQualifiedName))
            .connectorType(connectorType)
            .schemaName(schemaName)
            .schemaQualifiedName(schemaQualifiedName)
            .schema(Schema.refByQualifiedName(schemaQualifiedName))
            .databaseName(databaseName)
            .databaseQualifiedName(databaseQualifiedName)
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique view name.
     *
     * @param name of the view
     * @param schemaQualifiedName unique name of the schema in which this view exists
     * @return a unique name for the view
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the minimal request necessary to update the View, as a builder
     */
    public static ViewBuilder<?, ?> updater(String qualifiedName, String name) {
        return View._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a View, from a potentially
     * more-complete View object.
     *
     * @return the minimal object necessary to update the View, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for View are not found in the initial object
     */
    @Override
    public ViewBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
