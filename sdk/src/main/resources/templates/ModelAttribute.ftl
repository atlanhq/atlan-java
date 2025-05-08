<#macro all>
    /**
     * Builds the minimal object necessary to create a ModelAttribute.
     *
     * @param name of the ModelAttribute
     * @param entity (version-agnostic) in which the entity should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the ModelAttribute, as a builder
     * @throws InvalidRequestException if the entity provided is without a qualifiedName
     */
    public static ModelAttributeBuilder<?, ?> creator(String name, ModelEntity entity) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", entity.getConnectionQualifiedName());
        map.put("name", entity.getName());
        map.put("qualifiedName", entity.getModelVersionAgnosticQualifiedName());
        map.put("type", entity.getModelType());
        validateRelationship(ModelEntity.TYPE_NAME, map);
        return creator(
                name,
                entity.getConnectionQualifiedName(),
                entity.getName(),
                entity.getModelVersionAgnosticQualifiedName(),
                entity.getModelType())
            .clearModelAttributeEntities()
            .modelAttributeEntity(entity.trimToReference().toBuilder().semantic(SaveSemantic.APPEND).build());
    }

    /**
     * Builds the minimal object necessary to create a ModelAttribute.
     *
     * @param name of the ModelAttribute
     * @param entityQualifiedName unique (version-agnostic) name of the entity in which this ModelAttribute exists
     * @param modelType type of model in which this attribute exists
     * @return the minimal request necessary to create the ModelAttribute, as a builder
     */
    public static ModelAttributeBuilder<?, ?> creator(String name, String entityQualifiedName, String modelType) {
        String entitySlug = StringUtils.getNameFromQualifiedName(entityQualifiedName);
        String entityName = IModel.getNameFromSlug(entitySlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(entityQualifiedName);
        return creator(name, connectionQualifiedName, entityName, entityQualifiedName, modelType);
    }

    /**
     * Builds the minimal object necessary to create a ModelAttribute.
     *
     * @param name of the ModelAttribute
     * @param connectionQualifiedName unique name of the connection in which to create this ModelAttribute
     * @param entityName simple name (version-agnostic) of the entity in which to create this ModelAttribute
     * @param entityQualifiedName unique name (version-agnostic) of the entity in which to create this ModelAttribute
     * @param modelType type of model in which this attribute exists
     * @return the minimal request necessary to create the ModelAttribute, as a builder
     */
    public static ModelAttributeBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String entityName, String entityQualifiedName, String modelType) {
        String modelQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(entityQualifiedName);
        String modelName = IModel.getNameFromSlug(StringUtils.getNameFromQualifiedName(modelQualifiedName));
        return ModelAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .modelName(modelName)
                .modelQualifiedName(modelQualifiedName)
                .modelEntityName(entityName)
                .modelEntityQualifiedName(entityQualifiedName)
                .modelAttributeEntity(ModelEntity.refByQualifiedName(entityQualifiedName, SaveSemantic.APPEND))
                .modelVersionAgnosticQualifiedName(generateQualifiedName(name, entityQualifiedName))
                .modelType(modelType)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ModelAttribute.
     *
     * @param versionAgnosticQualifiedName of the ModelAttribute
     * @param name of the ModelAttribute
     * @return the minimal request necessary to update the ModelAttribute, as a builder
     */
    public static ModelAttributeBuilder<?, ?> updater(String versionAgnosticQualifiedName, String name) {
        return ModelAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .modelVersionAgnosticQualifiedName(versionAgnosticQualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to update a ModelAttribute.
     *
     * @param qualifiedName of the ModelAttribute
     * @param name of the ModelAttribute
     * @return the minimal request necessary to update the ModelAttribute, as a builder
     */
    public static ModelAttributeBuilder<?, ?> updaterForVersion(String qualifiedName, String name) {
        return ModelAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique ModelAttribute name.
     *
     * @param name of the ModelAttribute
     * @param parentQualifiedName unique name of the model or version in which this ModelAttribute exists
     * @return a unique name for the ModelAttribute
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + IModel.getSlugForName(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModelAttribute, from a potentially
     * more-complete ModelAttribute object.
     *
     * @return the minimal object necessary to update the ModelAttribute, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModelAttribute are not found in the initial object
     */
    @Override
    public ModelAttributeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("modelVersionAgnosticQualifiedName", this.getModelVersionAgnosticQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getModelVersionAgnosticQualifiedName(), this.getName());
    }
</#macro>
