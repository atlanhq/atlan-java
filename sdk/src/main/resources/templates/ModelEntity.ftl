<#macro all>
    /**
     * Builds the minimal object necessary to create a ModelEntity.
     *
     * @param name of the ModelEntity
     * @param model (version-agnostic) in which the entity should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the ModelEntity, as a builder
     * @throws InvalidRequestException if the model provided is without a qualifiedName
     */
    public static ModelEntityBuilder<?, ?> creator(String name, ModelDataModel model) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", model.getConnectionQualifiedName());
        map.put("name", model.getName());
        map.put("qualifiedName", model.getQualifiedName());
        map.put("modelType", model.getModelType());
        validateRelationship(ModelDataModel.TYPE_NAME, map);
        return creator(name, model.getConnectionQualifiedName(), model.getName(), model.getQualifiedName(), model.getModelType());
    }

    /**
     * Builds the minimal object necessary to create a ModelEntity.
     *
     * @param name of the ModelEntity
     * @param modelQualifiedName unique (version-agnostic) name of the model in which this ModelEntity exists
     * @param modelType type of model in which this entity exists
     * @return the minimal request necessary to create the ModelEntity, as a builder
     */
    public static ModelEntityBuilder<?, ?> creator(String name, String modelQualifiedName, String modelType) {
        String modelSlug = StringUtils.getNameFromQualifiedName(modelQualifiedName);
        String modelName = IModel.getNameFromSlug(modelSlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(modelQualifiedName);
        return creator(name, connectionQualifiedName, modelName, modelQualifiedName, modelType);
    }

    /**
     * Builds the minimal object necessary to create a ModelEntity.
     *
     * @param name of the ModelEntity
     * @param connectionQualifiedName unique name of the connection in which to create this ModelEntity
     * @param modelName simple name (version-agnostic) of the model in which to create this ModelEntity
     * @param modelQualifiedName unique name (version-agnostic) of the model in which to create this ModelEntity
     * @param modelType type of model in which this entity exists
     * @return the minimal request necessary to create the ModelEntity, as a builder
     */
    public static ModelEntityBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String modelName, String modelQualifiedName, String modelType) {
        return ModelEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .modelName(modelName)
                .modelQualifiedName(modelQualifiedName)
                .modelType(modelType)
                .modelVersionAgnosticQualifiedName(generateQualifiedName(name, modelQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelEntity.
     *
     * @param name of the ModelEntity
     * @param version in which the entity should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the ModelEntity, as a builder
     * @throws InvalidRequestException if the version provided is without a qualifiedName
     */
    public static ModelEntityBuilder<?, ?> creatorForVersion(String name, ModelVersion version) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", version.getConnectionQualifiedName());
        map.put("name", version.getName());
        map.put("qualifiedName", version.getQualifiedName());
        validateRelationship(ModelVersion.TYPE_NAME, map);
        return creatorForVersion(
            name,
            version.getConnectionQualifiedName(),
            version.getName(),
            version.getQualifiedName()
        ).modelVersion(version.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ModelEntity.
     *
     * @param name of the ModelEntity
     * @param versionQualifiedName unique name of the version in which this ModelEntity exists
     * @return the minimal request necessary to create the ModelEntity, as a builder
     */
    public static ModelEntityBuilder<?, ?> creatorForVersion(String name, String versionQualifiedName) {
        String versionSlug = StringUtils.getNameFromQualifiedName(versionQualifiedName);
        String versionName = IModel.getNameFromSlug(versionSlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(versionQualifiedName);
        return creatorForVersion(name, connectionQualifiedName, versionName, versionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelEntity.
     *
     * @param name of the ModelEntity
     * @param connectionQualifiedName unique name of the connection in which to create this ModelEntity
     * @param versionName simple name of the version in which to create this ModelEntity
     * @param versionQualifiedName unique name of the version in which to create this ModelEntity
     * @return the minimal request necessary to create the ModelEntity, as a builder
     */
    public static ModelEntityBuilder<?, ?> creatorForVersion(String name, String connectionQualifiedName, String versionName, String versionQualifiedName) {
        return ModelEntity._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(generateQualifiedName(name, versionQualifiedName))
            .modelVersionName(versionName)
            .modelVersionQualifiedName(versionQualifiedName)
            .modelVersion(ModelVersion.refByQualifiedName(versionQualifiedName))
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ModelEntity.
     *
     * @param versionAgnosticQualifiedName of the ModelEntity
     * @param name of the ModelEntity
     * @return the minimal request necessary to update the ModelEntity, as a builder
     */
    public static ModelEntityBuilder<?, ?> updater(String versionAgnosticQualifiedName, String name) {
        return ModelEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .modelVersionAgnosticQualifiedName(versionAgnosticQualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to update a ModelEntity.
     *
     * @param qualifiedName of the ModelEntity
     * @param name of the ModelEntity
     * @return the minimal request necessary to update the ModelEntity, as a builder
     */
    public static ModelEntityBuilder<?, ?> updaterForVersion(String qualifiedName, String name) {
        return ModelEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique ModelEntity name.
     *
     * @param name of the ModelEntity
     * @param parentQualifiedName unique name of the model or version in which this ModelEntity exists
     * @return a unique name for the ModelEntity
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + IModel.getSlugForName(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModelEntity, from a potentially
     * more-complete ModelEntity object.
     *
     * @return the minimal object necessary to update the ModelEntity, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModelEntity are not found in the initial object
     */
    @Override
    public ModelEntityBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("modelVersionAgnosticQualifiedName", this.getModelVersionAgnosticQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getModelVersionAgnosticQualifiedName(), this.getName());
    }
</#macro>
