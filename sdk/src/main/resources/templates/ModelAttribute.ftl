<#macro imports>
import com.atlan.model.assets.Connection;
</#macro>
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
        validateRelationship(ModelEntity.TYPE_NAME, Map.of(
            "connectionQualifiedName", entity.getConnectionQualifiedName(),
            "name", entity.getName(),
            "qualifiedName", entity.modelVersionAgnosticQualifiedName()
        ));
        return creator(
            name,
            entity.getConnectionQualifiedName(),
            entity.getName(),
            entity.modelVersionAgnosticQualifiedName()
        );
    }

    /**
     * Builds the minimal object necessary to create a ModelAttribute.
     *
     * @param name of the ModelAttribute
     * @param entityQualifiedName unique (version-agnostic) name of the entity in which this ModelAttribute exists
     * @return the minimal request necessary to create the ModelAttribute, as a builder
     */
    public static ModelAttributeBuilder<?, ?> creator(String name, String entityQualifiedName) {
        String entitySlug = StringUtils.getNameFromQualifiedName(entityQualifiedName);
        String entityName = IModel.getNameFromSlug(entitySlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(entityQualifiedName);
        return creator(name, connectionQualifiedName, entityName, entityQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelAttribute.
     *
     * @param name of the ModelAttribute
     * @param connectionQualifiedName unique name of the connection in which to create this ModelAttribute
     * @param entityName simple name (version-agnostic) of the entity in which to create this ModelAttribute
     * @param entityQualifiedName unique name (version-agnostic) of the entity in which to create this ModelAttribute
     * @return the minimal request necessary to create the ModelAttribute, as a builder
     */
    public static ModelAttributeBuilder<?, ?> creator(String name, String connectionQualifiedName, String entityName, String entityQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return ModelAttribute._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .modelVersionAgnosticQualifiedName(generateQualifiedName(name, entityQualifiedName))
            .connectorType(connectorType)
            .connectionQualifiedName(connectionQualifiedName);
    }

    // TODO: determine how version-agnostic updates should work (what to use as qualifiedName of entity?)

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
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
