<#macro all>
    /**
     * Builds the minimal object necessary to create a ModelAttributeAssociation.
     *
     * @param name of the ModelAttributeAssociation
     * @param from entity (version-agnostic) from which the association exists
     * @param to entity (version-agnostic) to which the association exists
     * @return the minimal request necessary to create the ModelAttributeAssociation, as a builder
     * @throws InvalidRequestException if the from or to are provided without a modelVersionAgnosticQualifiedName
     */
    public static ModelAttributeAssociationBuilder<?, ?> creator(String name, ModelAttribute from, ModelAttribute to) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("from_connectionQualifiedName", from.getConnectionQualifiedName());
        map.put("from_qualifiedName", from.getModelVersionAgnosticQualifiedName());
        map.put("to_qualifiedName", to.getModelVersionAgnosticQualifiedName());
        validateRelationship(ModelAttribute.TYPE_NAME, map);
        return creator(
            name,
            from.getConnectionQualifiedName(),
            from.getModelVersionAgnosticQualifiedName(),
            to.getModelVersionAgnosticQualifiedName()
        ).modelAttributeAssociationFrom(from.trimToReference()).modelAttributeAssociationTo(to.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ModelAttributeAssociation.
     *
     * @param name of the ModelAttributeAssociation
     * @param fromQualifiedName unique (version-agnostic) name of the entity from which the association exists
     * @param toQualifiedName unique (version-agnostic) name of the entity to which the association exists
     * @return the minimal request necessary to create the ModelAttributeAssociation, as a builder
     */
    public static ModelAttributeAssociationBuilder<?, ?> creator(String name, String fromQualifiedName, String toQualifiedName) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(fromQualifiedName);
        return creator(name, connectionQualifiedName, fromQualifiedName, toQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelAttributeAssociation.
     *
     * @param name of the ModelAttributeAssociation
     * @param connectionQualifiedName unique name of the connection in which to create this ModelAttributeAssociation
     * @param fromQualifiedName unique (version-agnostic) name of the entity from which the association exists
     * @param toQualifiedName unique (version-agnostic) name of the entity to which the association exists
     * @return the minimal request necessary to create the ModelAttributeAssociation, as a builder
     */
    public static ModelAttributeAssociationBuilder<?, ?> creator(
        String name, String connectionQualifiedName, String fromQualifiedName, String toQualifiedName) {
        String qualifiedName = generateQualifiedName(name, fromQualifiedName, toQualifiedName);
        String modelEntityQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(fromQualifiedName);
        String modelEntityName = IModel.getNameFromSlug(StringUtils.getNameFromQualifiedName(modelEntityQualifiedName));
        String modelQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(modelEntityQualifiedName);
        String modelName = IModel.getNameFromSlug(StringUtils.getNameFromQualifiedName(modelQualifiedName));
        return ModelAttributeAssociation._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(qualifiedName)
            .modelVersionAgnosticQualifiedName(qualifiedName)
            .connectionQualifiedName(connectionQualifiedName)
            .modelName(modelName)
            .modelQualifiedName(modelQualifiedName)
            .modelEntityName(modelEntityName)
            .modelEntityQualifiedName(modelEntityQualifiedName)
            .modelAttributeAssociationFrom(ModelAttribute.refByQualifiedName(fromQualifiedName))
            .modelAttributeAssociationFromQualifiedName(fromQualifiedName)
            .modelAttributeAssociationTo(ModelAttribute.refByQualifiedName(toQualifiedName))
            .modelAttributeAssociationToQualifiedName(toQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ModelAttributeAssociation.
     *
     * @param versionAgnosticQualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the minimal request necessary to update the ModelAttributeAssociation, as a builder
     */
    public static ModelAttributeAssociationBuilder<?, ?> updater(String versionAgnosticQualifiedName, String name) {
        return ModelAttributeAssociation._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .modelVersionAgnosticQualifiedName(versionAgnosticQualifiedName)
            .name(name);
    }

    /**
     * Builds the minimal object necessary to update a ModelAttributeAssociation.
     *
     * @param qualifiedName of the ModelAttributeAssociation
     * @param name of the ModelAttributeAssociation
     * @return the minimal request necessary to update the ModelAttributeAssociation, as a builder
     */
    public static ModelAttributeAssociationBuilder<?, ?> updaterForVersion(String qualifiedName, String name) {
        return ModelAttributeAssociation._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(qualifiedName)
            .name(name);
    }

    /**
     * Generate a unique ModelAttributeAssociation name.
     *
     * @param name of the ModelAttributeAssociation
     * @param fromQualifiedName unique (version-agnostic) name of the entity from which the association exists
     * @param toQualifiedName unique (version-agnostic) name of the entity to which the association exists
     * @return a unique name for the ModelAttributeAssociation
     */
    public static String generateQualifiedName(String name, String fromQualifiedName, String toQualifiedName) {
        return fromQualifiedName + "<<" + name + ">>" + toQualifiedName;
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModelAttributeAssociation, from a potentially
     * more-complete ModelAttributeAssociation object.
     *
     * @return the minimal object necessary to update the ModelAttributeAssociation, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModelAttributeAssociation are not found in the initial object
     */
    @Override
    public ModelAttributeAssociationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("modelVersionAgnosticQualifiedName", this.getModelVersionAgnosticQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getModelVersionAgnosticQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
