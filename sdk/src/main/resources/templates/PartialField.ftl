<#macro all>
    /**
     * Builds the minimal object necessary to create a PartialField.
     *
     * @param name of the PartialField
     * @param resolvedType the type that the partial asset should have when it becomes a full asset
     * @param parent in which the PartialField should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the PartialField, as a builder
     * @throws InvalidRequestException if the parent provided is without a qualifiedName
     */
    public static PartialFieldBuilder<?, ?> creator(String name, String resolvedType, ICatalog parent) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", parent.getConnectionQualifiedName());
        map.put("qualifiedName", parent.getQualifiedName());
        validateRelationship(parent.getTypeName(), map);
        return creator(
            name,
            resolvedType,
            parent.getConnectionQualifiedName(),
            parent.getQualifiedName(),
            parent.getTypeName()
        ).partialParentAsset(parent.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a PartialField.
     *
     * @param name of the PartialField
     * @param resolvedType the type that the partial asset should have when it becomes a full asset
     * @param parentQualifiedName unique name of the asset in which the PartialField is contained
     * @param parentType typeName of the asset in which the PartialField should be created
     * @return the minimal object necessary to create the PartialField, as a builder
     */
    public static PartialFieldBuilder<?, ?> creator(String name, String resolvedType, String parentQualifiedName, String parentType) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(parentQualifiedName);
        return creator(name, resolvedType, connectionQualifiedName, parentQualifiedName, parentType);
    }

    /**
     * Builds the minimal object necessary to create a PartialField.
     *
     * @param name of the PartialField
     * @param resolvedType the type that the partial asset should have when it becomes a full asset
     * @param connectionQualifiedName unique name of the connection in which the PartialField should be created
     * @param parentQualifiedName unique name of the asset in which the PartialField should be created
     * @param parentType typeName of the asset in which the PartialField should be created
     * @return the minimal object necessary to create the PartialField, as a builder
     */
    public static PartialFieldBuilder<?, ?> creator(String name, String resolvedType, String connectionQualifiedName, String parentQualifiedName, String parentType) {
        return PartialField._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(generateQualifiedName(name, parentQualifiedName))
            .name(name)
            .partialResolvedTypeName(resolvedType)
            .connectionQualifiedName(connectionQualifiedName)
            .partialParentQualifiedName(parentQualifiedName)
            .partialParentType(parentType)
            .partialParentAsset(ICatalog.getLineageReference(parentType, parentQualifiedName));
    }

    /**
     * Generate a unique PartialField name.
     *
     * @param name of the PartialField
     * @param parentQualifiedName unique name of the asset in which the PartialField is contained
     * @return a unique name for the PartialField
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a PartialField.
     *
     * @param qualifiedName of the PartialField
     * @param name of the PartialField
     * @return the minimal request necessary to update the PartialField, as a builder
     */
    public static PartialFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return PartialField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PartialField, from a potentially
     * more-complete PartialField object.
     *
     * @return the minimal object necessary to update the PartialField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PartialField are not found in the initial object
     */
    @Override
    public PartialFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
