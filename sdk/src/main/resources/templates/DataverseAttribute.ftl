<#macro all>
    /**
     * Builds the minimal object necessary to create a DataverseAttribute.
     *
     * @param name of the attribute
     * @param entity in which the attribute should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the attribute, as a builder
     * @throws InvalidRequestException if the entity provided is without a qualifiedName
     */
    public static DataverseAttribute.DataverseAttributeBuilder<?, ?> creator(String name, DataverseEntity entity)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("entityQualifiedName", entity.getQualifiedName());
        map.put("entityName", entity.getName());
        map.put("connectionQualifiedName", entity.getConnectionQualifiedName());
        validateRelationship(DataverseEntity.TYPE_NAME, map);
        return creator(name, entity.getConnectionQualifiedName(), entity.getQualifiedName())
                .dataverseEntity(entity.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a DataverseAttribute.
     *
     * @param name of the attribute
     * @param entityQualifiedName unique name of the entity in which this attribute exists
     * @return the minimal request necessary to create the attribute, as a builder
     */
    public static DataverseAttribute.DataverseAttributeBuilder<?, ?> creator(String name, String entityQualifiedName) {
        String entityName = StringUtils.getNameFromQualifiedName(entityQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(entityQualifiedName);
        return creator(name, connectionQualifiedName, entityQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DataverseAttribute.
     *
     * @param name of the attribute
     * @param connectionQualifiedName unique name of the connection in which to create the attribute
     * @param entityQualifiedName unique name of the entity in which to create the attribute
     * @return the minimal request necessary to create the attribute, as a builder
     */
    public static DataverseAttribute.DataverseAttributeBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String entityQualifiedName) {
        return DataverseAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, entityQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .dataverseEntityQualifiedName(entityQualifiedName)
                .dataverseEntity(DataverseEntity.refByQualifiedName(entityQualifiedName));
    }

    /**
     * Generate a unique attribute name.
     *
     * @param name of the attribute
     * @param entityQualifiedName unique name of the entity in which this attribute exists
     * @return a unique name for the attribute
     */
    public static String generateQualifiedName(String name, String entityQualifiedName) {
        return entityQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a DataverseAttribute.
     *
     * @param qualifiedName of the DataverseAttribute
     * @param name of the DataverseAttribute
     * @return the minimal request necessary to update the DataverseAttribute, as a builder
     */
    public static DataverseAttributeBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataverseAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataverseAttribute, from a potentially
     * more-complete DataverseAttribute object.
     *
     * @return the minimal object necessary to update the DataverseAttribute, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataverseAttribute are not found in the initial object
     */
    @Override
    public DataverseAttributeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
