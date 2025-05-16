<#macro all>
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
        return creator(
            name,
            workspace.getConnectionQualifiedName(),
            workspace.getQualifiedName()
        ).presetWorkspace(workspace.trimToReference());
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
    public static PresetDashboardBuilder<?, ?> creator(String name, String connectionQualifiedName, String workspaceQualifiedName) {
        return PresetDashboard._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(workspaceQualifiedName + "/" + name)
            .presetWorkspaceQualifiedName(workspaceQualifiedName)
            .presetWorkspace(PresetWorkspace.refByQualifiedName(workspaceQualifiedName))
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param name of the PresetDashboard
     * @return the minimal request necessary to update the PresetDashboard, as a builder
     */
    public static PresetDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetDashboard._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PresetDashboard, from a potentially
     * more-complete PresetDashboard object.
     *
     * @return the minimal object necessary to update the PresetDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PresetDashboard are not found in the initial object
     */
    @Override
    public PresetDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
