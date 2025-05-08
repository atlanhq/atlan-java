<#macro all>
    /**
     * Builds the minimal object necessary to create a Anaplan list.
     *
     * @param name of the list
     * @param model in which the list should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the list, as a builder
     * @throws InvalidRequestException if the model provided is without a qualifiedName
     */
    public static AnaplanList.AnaplanListBuilder<?, ?> creator(String name, AnaplanModel model)
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
     * Builds the minimal object necessary to create a Anaplan list.
     *
     * @param name of the list
     * @param modelQualifiedName unique name of the model in which this list exists
     * @return the minimal request necessary to create the list, as a builder
     */
    public static AnaplanList.AnaplanListBuilder<?, ?> creator(String name, String modelQualifiedName) {
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(modelQualifiedName);
        String workspaceName = StringUtils.getNameFromQualifiedName(workspaceQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        String modelName = StringUtils.getNameFromQualifiedName(modelQualifiedName);
        return creator(
                name, connectionQualifiedName, modelName, modelQualifiedName, workspaceName, workspaceQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Anaplan list.
     *
     * @param name of the list
     * @param connectionQualifiedName unique name of the connection in which to create the list
     * @param modelName name of the model in which to create the list
     * @param modelQualifiedName unique name of the model in which to create the list
     * @param workspaceName name of the workspace in which to create the list
     * @param workspaceQualifiedName unique name of the workspace in which to create the list
     * @return the minimal request necessary to create the list, as a builder
     */
    public static AnaplanList.AnaplanListBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String modelName,
            String modelQualifiedName,
            String workspaceName,
            String workspaceQualifiedName) {
        return AnaplanList._internal()
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
     * Generate a unique list name.
     *
     * @param name of the list
     * @param modelQualifiedName unique name of the model in which this list exists
     * @return a unique name for the list
     */
    public static String generateQualifiedName(String name, String modelQualifiedName) {
        return modelQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a AnaplanList.
     *
     * @param qualifiedName of the AnaplanList
     * @param name of the AnaplanList
     * @return the minimal request necessary to update the AnaplanList, as a builder
     */
    public static AnaplanListBuilder<?, ?> updater(String qualifiedName, String name) {
        return AnaplanList._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AnaplanList, from a potentially
     * more-complete AnaplanList object.
     *
     * @return the minimal object necessary to update the AnaplanList, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AnaplanList are not found in the initial object
     */
    @Override
    public AnaplanListBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
