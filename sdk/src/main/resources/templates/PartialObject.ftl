<#macro all>
    /**
     * Builds the minimal object necessary to create a PartialObject.
     *
     * @param name of the PartialObject
     * @param resolvedType the type that the partial asset should have when it becomes a full asset
     * @param asset in which the PartialObject should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the PartialObject, as a builder
     * @throws InvalidRequestException if the asset provided is without a qualifiedName
     */
    public static PartialObjectBuilder<?, ?> creator(String name, String resolvedType, ICatalog asset) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", asset.getConnectionQualifiedName());
        map.put("qualifiedName", asset.getQualifiedName());
        validateRelationship(asset.getTypeName(), map);
        return creator(
            name,
            resolvedType,
            asset.getConnectionQualifiedName(),
            asset.getQualifiedName(),
            asset.getTypeName()
        ).partialParentAsset(asset.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a PartialObject.
     *
     * @param name of the PartialObject
     * @param resolvedType the type that the partial asset should have when it becomes a full asset
     * @param parentQualifiedName unique name of the asset in which the PartialObject is contained
     * @param parentType typeName of the asset in which the PartialObject should be created
     * @return the minimal object necessary to create the PartialObject, as a builder
     */
    public static PartialObjectBuilder<?, ?> creator(String name, String resolvedType, String parentQualifiedName, String parentType) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(parentQualifiedName);
        return creator(name, resolvedType, connectionQualifiedName, parentQualifiedName, parentType);
    }

    /**
     * Builds the minimal object necessary to create a PartialObject.
     *
     * @param name of the PartialObject
     * @param resolvedType the type that the partial asset should have when it becomes a full asset
     * @param connectionQualifiedName unique name of the connection in which to create the PartialObject
     * @return the minimal request necessary to create the PartialObject, as a builder
     * @throws InvalidRequestException if the asset provided is without a qualifiedName
     */
    public static PartialObjectBuilder<?, ?> creator(String name, String resolvedType, String connectionQualifiedName) throws InvalidRequestException {
        return PartialObject._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .partialResolvedTypeName(resolvedType)
            .qualifiedName(generateQualifiedName(name, resolvedType, connectionQualifiedName))
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a PartialObject.
     *
     * @param name of the PartialObject
     * @param resolvedType the type that the partial asset should have when it becomes a full asset
     * @param connectionQualifiedName unique name of the connection in which the PartialObject should be created
     * @param parentQualifiedName unique name of the asset in which the PartialObject should be created
     * @param parentType typeName of the asset in which the PartialObject should be created
     * @return the minimal object necessary to create the PartialObject, as a builder
     */
    public static PartialObjectBuilder<?, ?> creator(String name, String resolvedType, String connectionQualifiedName, String parentQualifiedName, String parentType) {
        return PartialObject._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(generateQualifiedName(name, resolvedType, parentQualifiedName))
            .name(name)
            .partialResolvedTypeName(resolvedType)
            .connectionQualifiedName(connectionQualifiedName)
            .partialParentQualifiedName(parentQualifiedName)
            .partialParentType(parentType)
            .partialParentAsset(ICatalog.getLineageReference(parentType, parentQualifiedName));
    }

    /**
     * Generate a unique PartialObject name.
     *
     * @param name of the PartialObject
     * @param resolvedType the type that the partial asset should have when it becomes a full asset
     * @param parentQualifiedName unique name of the asset in which the PartialObject is contained
     * @return a unique name for the PartialObject
     */
    public static String generateQualifiedName(String name, String resolvedType, String parentQualifiedName) {
        return parentQualifiedName + "/" + resolvedType + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a PartialObject.
     *
     * @param qualifiedName of the PartialObject
     * @param name of the PartialObject
     * @return the minimal request necessary to update the PartialObject, as a builder
     */
    public static PartialObjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return PartialObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PartialObject, from a potentially
     * more-complete PartialObject object.
     *
     * @return the minimal object necessary to update the PartialObject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PartialObject are not found in the initial object
     */
    @Override
    public PartialObjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
