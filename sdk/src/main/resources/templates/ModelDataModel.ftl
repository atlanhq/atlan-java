<#macro imports>
import com.atlan.model.enums.DataModelType;
</#macro>
<#macro all>
    /**
     * Builds the minimal object necessary to create a ModelDataModel.
     *
     * @param name of the ModelDataModel
     * @param connectionQualifiedName unique name of the connection in which this ModelDataModel exists
     * @param modelType type of model in which this entity exists
     * @return the minimal request necessary to create the ModelDataModel, as a builder
     */
    public static ModelDataModelBuilder<?, ?> creator(String name, String connectionQualifiedName, DataModelType modelType) {
        return ModelDataModel._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .modelType(modelType.getValue())
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ModelDataModel.
     *
     * @param qualifiedName of the ModelDataModel
     * @param name of the ModelDataModel
     * @return the minimal request necessary to update the ModelDataModel, as a builder
     */
    public static ModelDataModelBuilder<?, ?> updater(String qualifiedName, String name) {
        return ModelDataModel._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
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

    /**
     * Builds the minimal object necessary to apply an update to a ModelDataModel, from a potentially
     * more-complete ModelDataModel object.
     *
     * @return the minimal object necessary to update the ModelDataModel, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModelDataModel are not found in the initial object
     */
    @Override
    public ModelDataModelBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
