<#macro all>
    /**
     * Builds the minimal object necessary to create a Anaplan dimension.
     *
     * @param name of the dimension
     * @param model in which the dimension should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the dimension, as a builder
     * @throws InvalidRequestException if the model provided is without a qualifiedName
     */
    public static AnaplanDimension.AnaplanDimensionBuilder<?, ?> creator(String name, AnaplanModel model)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("modelQualifiedName", model.getQualifiedName());
        map.put("modelName", model.getName());
        map.put("connectionQualifiedName", model.getConnectionQualifiedName());
        map.put("workspaceName", model.getAnaplanWorkspaceName());
        map.put("workspaceQualifiedName", model.getAnaplanWorkspaceQualifiedName());
        validateRelationship(AnaplanWorkspace.TYPE_NAME, map);
        return creator(
                        name,
                        model.getConnectionQualifiedName(),
                        model.getName(),
                        model.getQualifiedName(),
                        model.getAnaplanWorkspaceName(),
                        model.getAnaplanWorkspaceQualifiedName())
                .anaplanModel(model.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Anaplan dimension.
     *
     * @param name of the dimension
     * @param modelQualifiedName unique name of the model in which this dimension exists
     * @return the minimal request necessary to create the dimension, as a builder
     */
    public static AnaplanDimension.AnaplanDimensionBuilder<?, ?> creator(String name, String modelQualifiedName) {
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(modelQualifiedName);
        String workspaceName = StringUtils.getNameFromQualifiedName(workspaceQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        String modelName = StringUtils.getNameFromQualifiedName(modelQualifiedName);
        return creator(
                name, connectionQualifiedName, modelName, modelQualifiedName, workspaceName, workspaceQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Anaplan dimension.
     *
     * @param name of the dimension
     * @param connectionQualifiedName unique name of the connection in which to create the dimension
     * @param modelName name of the model in which to create the dimension
     * @param modelQualifiedName unique name of the model in which to create the dimension
     * @param workspaceName name of the workspace in which to create the dimension
     * @param workspaceQualifiedName unique name of the workspace in which to create the dimension
     * @return the minimal request necessary to create the dimension, as a builder
     */
    public static AnaplanDimension.AnaplanDimensionBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String modelName,
            String modelQualifiedName,
            String workspaceName,
            String workspaceQualifiedName) {
        return AnaplanDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, modelQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .anaplanWorkspaceName(workspaceName)
                .anaplanWorkspaceQualifiedName(workspaceQualifiedName)
                .anaplanModelName(modelName)
                .anaplanModelQualifiedName(modelQualifiedName)
                .anaplanModel(AnaplanModel.refByQualifiedName(modelQualifiedName));
    }

    /**
     * Generate a unique dimension name.
     *
     * @param name of the dimension
     * @param modelQualifiedName unique name of the model in which this dimension exists
     * @return a unique name for the dimension
     */
    public static String generateQualifiedName(String name, String modelQualifiedName) {
        return modelQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a AnaplanDimension.
     *
     * @param qualifiedName of the AnaplanDimension
     * @param name of the AnaplanDimension
     * @return the minimal request necessary to update the AnaplanDimension, as a builder
     */
    public static AnaplanDimensionBuilder<?, ?> updater(String qualifiedName, String name) {
        return AnaplanDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AnaplanDimension, from a potentially
     * more-complete AnaplanDimension object.
     *
     * @return the minimal object necessary to update the AnaplanDimension, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AnaplanDimension are not found in the initial object
     */
    @Override
    public AnaplanDimensionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
