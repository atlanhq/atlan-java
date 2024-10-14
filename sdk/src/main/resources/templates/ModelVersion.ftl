<#macro all>
    /**
     * Builds the minimal object necessary to create a ModelVersion.
     *
     * @param name of the ModelVersion
     * @param model in which the version should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the ModelVersion, as a builder
     * @throws InvalidRequestException if the model provided is without a qualifiedName
     */
    public static ModelVersionBuilder<?, ?> creator(String name, ModelDataModel model) throws InvalidRequestException {
        validateRelationship(ModelDataModel.TYPE_NAME, Map.of(
            "connectionQualifiedName", model.getConnectionQualifiedName(),
            "name", model.getName(),
            "qualifiedName", model.getQualifiedName()
        ));
        return creator(
            name,
            model.getConnectionQualifiedName(),
            model.getName(),
            model.getQualifiedName()
        ).modelDataModel(model.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ModelVersion.
     *
     * @param name of the ModelVersion
     * @param modelQualifiedName unique name of the model in which this ModelVersion exists
     * @return the minimal request necessary to create the ModelVersion, as a builder
     */
    public static ModelVersionBuilder<?, ?> creator(String name, String modelQualifiedName) {
        String modelSlug = StringUtils.getNameFromQualifiedName(modelQualifiedName);
        String modelName = IModel.getNameFromSlug(modelSlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(modelQualifiedName);
        return creator(name, connectionQualifiedName, modelName, modelQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelVersion.
     *
     * @param name of the ModelVersion
     * @param connectionQualifiedName unique name of the connection in which to create this ModelVersion
     * @param modelName simple name of the model in which to create this ModelVersion
     * @param modelQualifiedName unique name of the model in which to create this ModelVersion
     * @return the minimal request necessary to create the ModelVersion, as a builder
     */
    public static ModelVersionBuilder<?, ?> creator(String name, String connectionQualifiedName, String modelName, String modelQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return ModelVersion._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(generateQualifiedName(name, modelQualifiedName))
            .modelName(modelName)
            .modelQualifiedName(modelQualifiedName)
            .modelDataModel(ModelDataModel.refByQualifiedName(modelQualifiedName))
            .connectorType(connectorType)
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ModelVersion.
     *
     * @param qualifiedName of the ModelVersion
     * @param name of the ModelVersion
     * @return the minimal request necessary to update the ModelVersion, as a builder
     */
    public static ModelVersionBuilder<?, ?> updater(String qualifiedName, String name) {
        return ModelVersion._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique ModelVersion name.
     *
     * @param name of the ModelVersion
     * @param modelQualifiedName unique name of the model in which this ModelVersion exists
     * @return a unique name for the ModelVersion
     */
    public static String generateQualifiedName(String name, String modelQualifiedName) {
        return modelQualifiedName + "/" + IModel.getSlugForName(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModelVersion, from a potentially
     * more-complete ModelVersion object.
     *
     * @return the minimal object necessary to update the ModelVersion, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModelVersion are not found in the initial object
     */
    @Override
    public ModelVersionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
