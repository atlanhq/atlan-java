/**
     * Builds the minimal object necessary to create a Anaplan model.
     *
     * @param name of the model
     * @param workspace in which the model should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the model, as a builder
     * @throws InvalidRequestException if the workspace provided is without a qualifiedName
     */
    public static AnaplanModel.AnaplanModelBuilder<?, ?> creator(String name, AnaplanWorkspace workspace)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("workspaceQualifiedName", workspace.getQualifiedName());
        map.put("workspaceName", workspace.getName());
        map.put("connectionQualifiedName", workspace.getConnectionQualifiedName());
        validateRelationship(AnaplanWorkspace.TYPE_NAME, map);
        return creator(name, workspace.getConnectionQualifiedName(), workspace.getName(), workspace.getQualifiedName())
                .anaplanWorkspace(workspace.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Anaplan model.
     *
     * @param name of the model
     * @param workspaceQualifiedName unique name of the workspace in which this model exists
     * @return the minimal request necessary to create the model, as a builder
     */
    public static AnaplanModel.AnaplanModelBuilder<?, ?> creator(String name, String workspaceQualifiedName) {
        String workspaceName = StringUtils.getNameFromQualifiedName(workspaceQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return creator(name, connectionQualifiedName, workspaceName, workspaceQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Anaplan model.
     *
     * @param name of the model
     * @param connectionQualifiedName unique name of the connection in which to create the model
     * @param workspaceName name of the workspace in which to create the model
     * @param workspaceQualifiedName unique name of the workspace in which to create the model
     * @return the minimal request necessary to create the model, as a builder
     */
    public static AnaplanModel.AnaplanModelBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String workspaceName, String workspaceQualifiedName) {
        return AnaplanModel._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, workspaceQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .anaplanWorkspaceName(workspaceName)
                .anaplanWorkspaceQualifiedName(workspaceQualifiedName)
                .anaplanWorkspace(AnaplanWorkspace.refByQualifiedName(workspaceQualifiedName));
    }

    /**
     * Generate a unique model name.
     *
     * @param name of the model
     * @param workspaceQualifiedName unique name of the workspace in which this model exists
     * @return a unique name for the model
     */
    public static String generateQualifiedName(String name, String workspaceQualifiedName) {
        return workspaceQualifiedName + "/" + name;
    }