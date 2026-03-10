// IMPORT: import com.atlan.model.enums.DataModelType;

/**
     * Builds the minimal object necessary to create a ModelDataModel.
     *
     * @param name of the ModelDataModel
     * @param connectionQualifiedName unique name of the connection in which this ModelDataModel exists
     * @param modelType type of model in which this entity exists
     * @return the minimal request necessary to create the ModelDataModel, as a builder
     */
    public static ModelDataModelBuilder<?, ?> creator(
            String name, String connectionQualifiedName, DataModelType modelType) {
        return ModelDataModel._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .modelType(modelType.getValue())
                .connectionQualifiedName(connectionQualifiedName);
    }

/**
     * Generate a unique ModelDataModel name.
     *
     * @param name of the ModelDataModel
     * @param connectionQualifiedName unique name of the connection in which this ModelDataModel exists
     * @return a unique name for the ModelDataModel
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }