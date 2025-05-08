<#macro all>
    /**
     * Builds the minimal object necessary to create a ModelEntityAssociation.
     *
     * @param name of the ModelEntityAssociation
     * @param from entity (version-agnostic) from which the association exists
     * @param to entity (version-agnostic) to which the association exists
     * @return the minimal request necessary to create the ModelEntityAssociation, as a builder
     * @throws InvalidRequestException if the from or to are provided without a modelVersionAgnosticQualifiedName
     */
    public static ModelEntityAssociationBuilder<?, ?> creator(String name, ModelEntity from, ModelEntity to) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("from_connectionQualifiedName", from.getConnectionQualifiedName());
        map.put("from_qualifiedName", from.getModelVersionAgnosticQualifiedName());
        map.put("to_qualifiedName", to.getModelVersionAgnosticQualifiedName());
        validateRelationship(ModelEntity.TYPE_NAME, map);
        return creator(
            name,
            from.getConnectionQualifiedName(),
            from.getModelVersionAgnosticQualifiedName(),
            to.getModelVersionAgnosticQualifiedName()
        ).modelEntityAssociationFrom(from.trimToReference()).modelEntityAssociationTo(to.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ModelEntityAssociation.
     *
     * @param name of the ModelEntityAssociation
     * @param fromQualifiedName unique (version-agnostic) name of the entity from which the association exists
     * @param toQualifiedName unique (version-agnostic) name of the entity to which the association exists
     * @return the minimal request necessary to create the ModelEntityAssociation, as a builder
     */
    public static ModelEntityAssociationBuilder<?, ?> creator(String name, String fromQualifiedName, String toQualifiedName) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(fromQualifiedName);
        return creator(name, connectionQualifiedName, fromQualifiedName, toQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ModelEntityAssociation.
     *
     * @param name of the ModelEntityAssociation
     * @param connectionQualifiedName unique name of the connection in which to create this ModelEntityAssociation
     * @param fromQualifiedName unique (version-agnostic) name of the entity from which the association exists
     * @param toQualifiedName unique (version-agnostic) name of the entity to which the association exists
     * @return the minimal request necessary to create the ModelEntityAssociation, as a builder
     */
    public static ModelEntityAssociationBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String fromQualifiedName, String toQualifiedName) {
        String qualifiedName = generateQualifiedName(name, fromQualifiedName, toQualifiedName);
        String modelQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(fromQualifiedName);
        String modelName = IModel.getNameFromSlug(StringUtils.getNameFromQualifiedName(modelQualifiedName));
        return ModelEntityAssociation._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(qualifiedName)
                .modelVersionAgnosticQualifiedName(qualifiedName)
                .connectionQualifiedName(connectionQualifiedName)
                .modelName(modelName)
                .modelQualifiedName(modelQualifiedName)
                .modelEntityAssociationFrom(ModelEntity.refByQualifiedName(fromQualifiedName))
                .modelEntityAssociationFromQualifiedName(fromQualifiedName)
                .modelEntityAssociationTo(ModelEntity.refByQualifiedName(toQualifiedName))
                .modelEntityAssociationToQualifiedName(toQualifiedName)
                .modelEntityAssociationLabel(name);
    }

    /**
     * Builds the minimal object necessary to update a ModelEntityAssociation.
     *
     * @param versionAgnosticQualifiedName of the ModelEntityAssociation
     * @param name of the ModelEntityAssociation
     * @return the minimal request necessary to update the ModelEntityAssociation, as a builder
     */
    public static ModelEntityAssociationBuilder<?, ?> updater(String versionAgnosticQualifiedName, String name) {
        return ModelEntityAssociation._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .modelVersionAgnosticQualifiedName(versionAgnosticQualifiedName)
            .name(name);
    }

    /**
     * Builds the minimal object necessary to update a ModelEntityAssociation.
     *
     * @param qualifiedName of the ModelEntityAssociation
     * @param name of the ModelEntityAssociation
     * @return the minimal request necessary to update the ModelEntityAssociation, as a builder
     */
    public static ModelEntityAssociationBuilder<?, ?> updaterForVersion(String qualifiedName, String name) {
        return ModelEntityAssociation._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(qualifiedName)
            .name(name);
    }

    /**
     * Generate a unique ModelEntityAssociation name.
     *
     * @param name of the ModelEntityAssociation
     * @param fromQualifiedName unique (version-agnostic) name of the entity from which the association exists
     * @param toQualifiedName unique (version-agnostic) name of the entity to which the association exists
     * @return a unique name for the ModelEntityAssociation
     */
    public static String generateQualifiedName(String name, String fromQualifiedName, String toQualifiedName) {
        return fromQualifiedName + "<<" + name + ">>" + toQualifiedName;
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModelEntityAssociation, from a potentially
     * more-complete ModelEntityAssociation object.
     *
     * @return the minimal object necessary to update the ModelEntityAssociation, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModelEntityAssociation are not found in the initial object
     */
    @Override
    public ModelEntityAssociationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("modelVersionAgnosticQualifiedName", this.getModelVersionAgnosticQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getModelVersionAgnosticQualifiedName(), this.getName());
    }
</#macro>
