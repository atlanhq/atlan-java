<#macro all>
    /**
     * Builds the minimal object necessary to create a DataModeling connections DataModel.
     *
     * @param name of the DataModel
     * @param connectionQualifiedName unique name of the connection through which the DataModel is accessible
     * @return the minimal object necessary to create the datamodel, as a builder
     */
    public static DataModelBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return DataModel._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(connectionQualifiedName, name))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.DATA_MODELING);
    }

    /**
     * Builds the minimal object necessary to update a DataModel.
     *
     * @param qualifiedName of the DataModel
     * @param name of the DataModel
     * @return the minimal request necessary to update the DataModel, as a builder
     */
    public static DataModelBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataModel._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataModel, from a potentially
     * more-complete DataModel object.
     *
     * @return the minimal object necessary to update the DataModel, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataModel are not found in the initial object
     */
    @Override
    public DataModelBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique DataModel workspace name.
     *
     * @param connectionQualifiedName unique name of the connection
     * @param name for the DataModel
     * @return a unique name for the DataModel
     */
    private static String generateQualifiedName(String connectionQualifiedName, String name) {
        return connectionQualifiedName + "/" + name;
    }
</#macro>
