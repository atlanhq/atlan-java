/**
     * Builds the minimal object necessary to create a Preset collection.
     *
     * @param name of the collection
     * @param workspace in which the collection should be created, which must have at least
     *                  a qualifiedName
     * @return the minimal request necessary to create the collection, as a builder
     * @throws InvalidRequestException if the workspace provided is without a qualifiedName
     */
    public static PresetDashboardBuilder<?, ?> creator(String name, PresetWorkspace workspace)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", workspace.getConnectionQualifiedName());
        map.put("qualifiedName", workspace.getQualifiedName());
        validateRelationship(PresetWorkspace.TYPE_NAME, map);
        return creator(name, workspace.getConnectionQualifiedName(), workspace.getQualifiedName())
                .presetWorkspace(workspace.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Preset collection.
     *
     * @param name of the collection
     * @param workspaceQualifiedName unique name of the workspace in which the collection exists
     * @return the minimal object necessary to create the collection, as a builder
     */
    public static PresetDashboardBuilder<?, ?> creator(String name, String workspaceQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return creator(name, connectionQualifiedName, workspaceQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Preset collection.
     *
     * @param name of the collection
     * @param connectionQualifiedName unique name of the connection in which to create the PresetDashboard
     * @param workspaceQualifiedName unique name of the PresetWorkspace in which to create the PresetDashboard
     * @return the minimal object necessary to create the collection, as a builder
     */
    public static PresetDashboardBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String workspaceQualifiedName) {
        return PresetDashboard._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(workspaceQualifiedName + "/" + name)
                .presetWorkspaceQualifiedName(workspaceQualifiedName)
                .presetWorkspace(PresetWorkspace.refByQualifiedName(workspaceQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }